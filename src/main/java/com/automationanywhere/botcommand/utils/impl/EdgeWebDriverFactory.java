package com.automationanywhere.botcommand.utils.impl;

import com.automationanywhere.botcommand.utils.interfaces.WebDriverFactory;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Sumit Kumar
 */
public class EdgeWebDriverFactory implements WebDriverFactory {
    private static final String EDGE_DRIVER_SYSTEM_PROPERTY = "webdriver.edge.driver";

    @Override
    public WebDriver createDriver(List<String> arguments, Map<String, Object> prefs, String driverPath) {
        setDriverPath(driverPath);
        EdgeOptions options = new EdgeOptions();
        configureOptions(options, arguments, prefs);
        return new EdgeDriver(options);
    }

    private void setDriverPath(String driverPath) {
        if (driverPath != null && !driverPath.isBlank()) {
            System.setProperty(EdgeWebDriverFactory.EDGE_DRIVER_SYSTEM_PROPERTY, driverPath);
        }
    }

    private void configureOptions(EdgeOptions options, List<String> arguments, Map<String, Object> prefs) {
        options.addArguments(arguments);
        options.setExperimentalOption("prefs", prefs);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
    }
}