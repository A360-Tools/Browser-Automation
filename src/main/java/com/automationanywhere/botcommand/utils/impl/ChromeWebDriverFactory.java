package com.automationanywhere.botcommand.utils.impl;

import com.automationanywhere.botcommand.utils.interfaces.WebDriverFactory;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Sumit Kumar
 */
public class ChromeWebDriverFactory implements WebDriverFactory {

    @Override
    public WebDriver createDriver(List<String> arguments, Map<String, Object> prefs, String driverPath) {
        ChromeDriverService driverService = createDriverService(driverPath);
        ChromeOptions options = new ChromeOptions();
        configureOptions(options, arguments, prefs);
        return new ChromeDriver(driverService, options);
    }

    private ChromeDriverService createDriverService(String driverPath) {
        ChromeDriverService service;
        if (driverPath != null && !driverPath.isBlank()) {
            service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new java.io.File(driverPath))
                    .build();
        } else {
            service = ChromeDriverService.createDefaultService();
        }
        return service;
    }

    private void configureOptions(ChromeOptions options, List<String> arguments, Map<String, Object> prefs) {
        options.addArguments(arguments);
        options.setExperimentalOption("prefs", prefs);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
    }
}