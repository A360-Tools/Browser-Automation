package com.automationanywhere.botcommand.utils;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.impl.ChromeWebDriverFactory;
import com.automationanywhere.botcommand.utils.impl.EdgeWebDriverFactory;
import com.automationanywhere.botcommand.utils.interfaces.WebDriverFactory;
import com.automationanywhere.toolchain.runtime.session.CloseableSessionObject;
import org.openqa.selenium.WebDriver;

import java.util.*;
import java.util.stream.Collectors;

public class BrowserConnection implements CloseableSessionObject {
    public static final String CHROME = "CHROME";
    public static final String EDGE = "EDGE";

    private WebDriver driver;
    private final String library;

    // New fields to store constructor parameters
    private final String browserType;
    private final String profilePath;
    private final boolean headless;
    private final String driverPath;
    private final List<String> arguments;

    public BrowserConnection(
            String browserType,
            String profilePath,
            boolean headless,
            String library,
            String driverPath,
            List<String> arguments) {

        this.browserType = browserType;
        this.profilePath = profilePath;
        this.headless = headless;
        this.driverPath = driverPath;
        this.arguments = arguments;
        this.library = Optional.ofNullable(library).orElse("");

        initializeDriver();
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    public String getLibrary() {
        return this.library;
    }

    @Override
    public boolean isClosed() {
        return this.driver == null;
    }

    @Override
    public void close() {
        if (this.driver != null) {
            this.driver.quit();
            this.driver = null;
        }
    }

    public void reinitializeDriver() {
        close();
        initializeDriver();
    }

    private void initializeDriver() {
        List<String> driverArguments = prepareDriverArguments(headless, profilePath, arguments);
        Map<String, Object> driverPreferences = prepareDriverPreferences();
        WebDriverFactory factory = getWebDriverFactory(browserType);
        driver = factory.createDriver(driverArguments, driverPreferences, driverPath);
        if (driver == null) {
            throw new BotCommandException("Unable to initialize the WebDriver.");
        }
    }

    private static WebDriverFactory getWebDriverFactory(String browserType) {
        switch (browserType.toUpperCase()) {
            case CHROME:
                return new ChromeWebDriverFactory();
            case EDGE:
                return new EdgeWebDriverFactory();
            default:
                throw new BotCommandException("Unsupported browser type: " + browserType);
        }
    }

    private static Map<String, Object> prepareDriverPreferences() {
        return new HashMap<>() {{
            put(PreferenceKeys.CREDENTIALS_ENABLE_SERVICE.getKey(), false);
            put(PreferenceKeys.PASSWORD_MANAGER_ENABLED.getKey(), false);
            put(PreferenceKeys.AUTOFILL_PROFILE_ENABLED.getKey(), false);
            put(PreferenceKeys.AUTOFILL_CREDIT_CARD_ENABLED.getKey(), false);
            put(PreferenceKeys.EXIT_TYPE.getKey(), "Normal");
            put(PreferenceKeys.EXITED_CLEANLY.getKey(), true);
        }};
    }

    private static List<String> prepareDriverArguments(boolean headless, String profilePath, List<String> userArguments) {
        List<String> defaultArguments = Arrays.stream(DriverArguments.values())
                .map(DriverArguments::getArgument)
                .collect(Collectors.toList());

        List<String> mergedArguments = new ArrayList<>(userArguments != null ? userArguments : new ArrayList<>());
        mergedArguments.addAll(defaultArguments.stream()
                .filter(arg -> mergedArguments.stream().noneMatch(a -> a.startsWith(arg)))
                .collect(Collectors.toList()));

        mergedArguments.removeIf(a -> a.equals(DriverArguments.HEADLESS.getArgument()) || a.startsWith(DriverArguments.USER_DATA_DIR.getArgument()));

        if (headless) {
            mergedArguments.add(DriverArguments.HEADLESS.getArgument());
        }

        if (profilePath != null && !profilePath.isEmpty()) {
            mergedArguments.add(DriverArguments.USER_DATA_DIR.getArgument() + profilePath);
        }

        return mergedArguments.stream().distinct().collect(Collectors.toList());
    }


    private enum DriverArguments {
        NO_SANDBOX("--no-sandbox"),
        DISABLE_SESSION_CRASHED_BUBBLE("--disable-session-crashed-bubble"),
        DISABLE_INFOBARS("--disable-infobars"),
        DISABLE_RESTORE_SESSION_STATE("--disable-restore-session-state"),
        DISABLE_GPU("--disable-gpu"),
        IGNORE_CERTIFICATE_ERRORS("--ignore-certificate-errors"),
        DISABLE_BLINK_FEATURES("--disable-blink-features=AutomationControlled"),
        HEADLESS("--headless"),
        USER_DATA_DIR("user-data-dir=");

        private final String argument;

        DriverArguments(String argument) {
            this.argument = argument;
        }

        public String getArgument() {
            return argument;
        }
    }

    private enum PreferenceKeys {
        CREDENTIALS_ENABLE_SERVICE("credentials_enable_service"),
        PASSWORD_MANAGER_ENABLED("profile.password_manager_enabled"),
        AUTOFILL_PROFILE_ENABLED("autofill.profile_enabled"),
        AUTOFILL_CREDIT_CARD_ENABLED("autofill.credit_card_enabled"),
        EXIT_TYPE("exit_type"),
        EXITED_CLEANLY("exited_cleanly");

        private final String key;

        PreferenceKeys(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}