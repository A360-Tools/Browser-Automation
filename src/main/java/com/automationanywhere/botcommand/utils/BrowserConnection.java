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
    private static final String EMPTY_STRING = "";
    private final String library;
    private WebDriver driver;
    private Map<String, WebDriverFactory> factoryMap;

    public BrowserConnection(String profilePath, String browser, Boolean headless,
                             String libraryCode, String driverPath, List<String> stringArguments,
                             Map<String, Object> prefs) {

        initializeFactoryMap();
        this.library = Optional.ofNullable(libraryCode).orElse(EMPTY_STRING);
        List<String> arguments = prepareArguments(profilePath, headless, stringArguments);
        prefs = Optional.ofNullable(prefs).orElse(new HashMap<>());

        WebDriverFactory factory = factoryMap.get(browser.toUpperCase());
        if (factory == null) {
            throw new BotCommandException("Unsupported browser type: " + browser);
        }
        this.driver = factory.createDriver(arguments, prefs, driverPath);

        if (this.driver == null) {
            throw new BotCommandException("Unable to initialize the WebDriver for: " + browser + " browser");
        }
    }

    private void initializeFactoryMap() {
        factoryMap = new HashMap<>();
        factoryMap.put("CHROME", new ChromeWebDriverFactory());
        factoryMap.put("EDGE", new EdgeWebDriverFactory());
    }

    private List<String> prepareArguments(String profilePath, Boolean headless, List<String> stringArguments) {
        List<String> arguments = new ArrayList<>(Optional.ofNullable(stringArguments).orElse(Collections.emptyList()));
        arguments.addAll(getDefaultArguments());
        if (Optional.ofNullable(headless).orElse(Boolean.FALSE)) {
            arguments.add("--headless");
        }
        Optional.ofNullable(profilePath).filter(path -> !path.isBlank()).ifPresent(path -> arguments.add("user-data" +
                "-dir=" + path));
        return arguments;
    }

    private static List<String> getDefaultArguments() {
        return Arrays.stream(DriverDefaultArgument.values())
                .map(DriverDefaultArgument::getArgument)
                .collect(Collectors.toList());
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
        }
        this.driver = null;
    }

    public enum DriverDefaultArgument {
        DISABLE_GPU("--disable-gpu"),
        IGNORE_CERTIFICATE_ERRORS("--ignore-certificate-errors"),
        DISABLE_BLINK_FEATURES("--disable-blink-features=AutomationControlled");

        private final String argument;

        DriverDefaultArgument(String argument) {
            this.argument = argument;
        }

        public String getArgument() {
            return argument;
        }
    }
}
