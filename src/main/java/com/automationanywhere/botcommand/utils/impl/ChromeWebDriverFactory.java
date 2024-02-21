package com.automationanywhere.botcommand.utils.impl;

import com.automationanywhere.botcommand.utils.interfaces.WebDriverFactory;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Sumit Kumar
 */
public class ChromeWebDriverFactory implements WebDriverFactory {
    private static final String CHROME_DRIVER_SYSTEM_PROPERTY = "webdriver.chrome.driver";

    @Override
    public WebDriver createDriver(List<String> arguments, Map<String, Object> prefs, String driverPath) {
        setDriverPath(driverPath);
        ChromeOptions options = new ChromeOptions();
        configureOptions(options, arguments, prefs);
        return new ChromeDriver(options);
    }

    private void setDriverPath(String driverPath) {
        if (driverPath != null && !driverPath.isBlank()) {
            System.setProperty(ChromeWebDriverFactory.CHROME_DRIVER_SYSTEM_PROPERTY, driverPath);
        }
    }

    private void configureOptions(ChromeOptions options, List<String> arguments, Map<String, Object> prefs) {
        options.addArguments(arguments);
        options.setExperimentalOption("prefs", prefs);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
    }
}