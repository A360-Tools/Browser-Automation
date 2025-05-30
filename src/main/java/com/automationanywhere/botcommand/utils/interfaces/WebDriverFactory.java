package com.automationanywhere.botcommand.utils.interfaces;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;

/**
 * @author Sumit Kumar
 */
public interface WebDriverFactory {

  WebDriver createDriver(List<String> arguments, Map<String, Object> prefs, String driverPath);
}