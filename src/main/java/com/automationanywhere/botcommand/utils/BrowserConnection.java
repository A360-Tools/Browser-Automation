package com.automationanywhere.botcommand.utils;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.toolchain.runtime.session.CloseableSessionObject;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.*;

public class BrowserConnection implements CloseableSessionObject {
    private static final String EMPTY_STRING = "";
    private final String library;
    private WebDriver driver;

    public BrowserConnection(String profilePath, String browser, Boolean headless,
                             String libraryCode, String driverPath, List<String> stringArguments) {

        this.library = Optional.ofNullable(libraryCode).orElse(EMPTY_STRING);
        profilePath = Optional.ofNullable(profilePath).orElse(EMPTY_STRING);
        stringArguments = Optional.ofNullable(stringArguments).orElse(Collections.emptyList());
        headless = Optional.ofNullable(headless).orElse(Boolean.FALSE);

        List<String> arguments = new ArrayList<>(stringArguments);
        //add default arguments
        arguments.add("--disable-gpu");
        arguments.add("--ignore-certificate-errors");
        arguments.add("--disable-blink-features=AutomationControlled");
        if (headless)
            arguments.add("--headless=new");
        if (!profilePath.isBlank()) {
            arguments.add("user-data-dir=" + profilePath);
        }

        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = getChromeOptions(arguments);
                setupChromeDriver(driverPath, chromeOptions);
                break;
            case "edge":
                EdgeOptions edgeOptions = getEdgeOptions(arguments);
                setupEdgeDriver(driverPath, edgeOptions);
                break;
            default:
                throw new BotCommandException("Unsupported browser type: " + browser);
        }

        if (this.driver == null) {
            throw new BotCommandException("Unable to find the installed application: " + browser + " browser");
        }
    }

    private void setupChromeDriver(String driverPath, ChromeOptions options) {
        if (driverPath != null && !driverPath.isBlank()) {
            System.setProperty("webdriver.chrome.driver", driverPath);
        }
        this.driver = new ChromeDriver(options);
    }

    private void setupEdgeDriver(String driverPath, EdgeOptions options) {
        if (driverPath != null && !driverPath.isBlank()) {
            System.setProperty("webdriver.edge.driver", driverPath);
        }
        this.driver = new EdgeDriver(options);
    }

    private ChromeOptions getChromeOptions(List<String> arguments) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(arguments);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        return options;
    }

    private EdgeOptions getEdgeOptions(List<String> arguments) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments(arguments);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        return options;
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
}
