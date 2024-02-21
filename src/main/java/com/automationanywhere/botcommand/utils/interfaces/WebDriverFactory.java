package com.automationanywhere.botcommand.utils.interfaces;

import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

/**
 * @author Sumit Kumar
 */
public interface WebDriverFactory {
    WebDriver createDriver(List<String> arguments, Map<String, Object> prefs, String driverPath);
}