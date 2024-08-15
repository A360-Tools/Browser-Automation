package com.automationanywhere.botcommand.utils.impl;

import com.automationanywhere.botcommand.utils.interfaces.WebDriverFactory;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Sumit Kumar
 */
public class EdgeWebDriverFactory implements WebDriverFactory {

    @Override
    public WebDriver createDriver(List<String> arguments, Map<String, Object> prefs, String driverPath) {
        EdgeDriverService driverService = createDriverService(driverPath);
        EdgeOptions options = new EdgeOptions();
        configureOptions(options, arguments, prefs);
        return new EdgeDriver(driverService, options);
    }

    private EdgeDriverService createDriverService(String driverPath) {
        EdgeDriverService service;
        if (driverPath != null && !driverPath.isBlank()) {
            service = new EdgeDriverService.Builder()
                    .usingDriverExecutable(new java.io.File(driverPath))
                    .build();
        } else {
            service = EdgeDriverService.createDefaultService();
        }
        return service;
    }

    private void configureOptions(EdgeOptions options, List<String> arguments, Map<String, Object> prefs) {
        options.addArguments(arguments);
        options.setExperimentalOption("prefs", prefs);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
    }
}