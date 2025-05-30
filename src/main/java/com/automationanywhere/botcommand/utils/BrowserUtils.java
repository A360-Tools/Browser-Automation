package com.automationanywhere.botcommand.utils;


import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;


public class BrowserUtils {


  public static final String CSS = "CSS";
  public static final String XPATH = "XPATH";
  public static final String TAG = "TAG";
  public static final String ID = "ID";
  public static final String JS = "JS";
  public static final String MODE_SIMULATE = "SIMULATE";
  public static final String MODE_JAVASCRIPT = "JAVASCRIPT";

  public BrowserUtils() {

  }

  public static WebElement waitForElementWithAttribute(WebDriver driver, String search, String type,
      String attribute, int timeoutInSeconds) {
    FluentWait<WebDriver> wait = new FluentWait<>(driver)
        .withTimeout(Duration.ofSeconds(timeoutInSeconds))
        .pollingEvery(Duration.ofMillis(50))
        .ignoring(NoSuchElementException.class);

    return wait.until(webDriver -> {
      WebElement element = getElement(driver, search, type);
      if (element != null && element.getAttribute(attribute) != null) {
        return element;
      }
      return null;
    });
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
}
    