package com.automationanywhere.botcommand.utils;


import com.automationanywhere.botcommand.exception.BotCommandException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class BrowserUtils {


    public static final String CSS = "CSS";
    public static final String XPATH = "XPATH";
    public static final String TAG = "TAG";
    public static final String ID = "ID";
    public static final String JS = "JS";
    public static final String MODE_SIMULATE = "SIMULATE";
    public static final String MODE_JAVASCRIPT = "JAVASCRIPT";
    public static final String Click = "Click";

    public BrowserUtils() {

    }

    public static WebElement getElement(WebDriver driver, String search, String type) {
        WebElement element = null;
        try {
            switch (type) {
                case ID:
                    element = driver.findElement(By.id(search));
                    break;
                case CSS:
                    element = driver.findElement(By.cssSelector(search));
                    break;
                case XPATH:
                    element = driver.findElement(By.xpath(search));
                    break;
                case TAG:
                    element = driver.findElement(By.tagName(search));
                    break;
                case JS:
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    element = (WebElement) jsExecutor.executeScript(search);
                    break;
            }
        } catch (Exception ignored) {
        }
        return element;
    }

    public static boolean waitForElementWithAttribute(WebDriver driver, String js, String attribute,
                                                      int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds), Duration.ofMillis(200));

            return wait.until((ExpectedCondition<Boolean>) webDriver -> {
                Object attributeValue;
                try {
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
                    assert jsExecutor != null;
                    attributeValue = jsExecutor.executeScript("return " + js + "." + attribute);
                } catch (Exception e) {
                    attributeValue = null;
                }
                return attributeValue != null;
            });
        } catch (Exception e) {
            return false;
        }
    }

    public static String getJavaScriptPath(String search, String type) {
        switch (type) {
            case ID:
                return "document.getElementById(`" + search + "`)";
            case CSS:
                return "document.querySelector(`" + search + "`)";
            case XPATH:
                return "document.evaluate(`" + search + "`, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";
            case TAG:
                return "document.getElementsByTagName(`" + search + "`)[0]";
            case JS:
                return search;
            default:
                throw new BotCommandException("Unsupported search type: " + type);
        }
    }
}
    