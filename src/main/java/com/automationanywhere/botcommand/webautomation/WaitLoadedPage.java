package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.impl.BooleanValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;

@BotCommand
@CommandPkg(label = "Wait Page Loaded", name = "pageloaded",
    description = "Wait until Page is loaded",
    node_label = "for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Wait", text_color =
    "#2F4F4F", background_color = "#2F4F4F",
    return_type = DataType.BOOLEAN, return_label = "Status")

public class WaitLoadedPage {

  @Execute
  public static BooleanValue action(

      @Idx(index = "1", type = AttributeType.SESSION)
      @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
          default_value_type = DataType.SESSION, default_value = "Default")
      @NotEmpty
      @SessionObject
      BrowserConnection session,

      @Idx(index = "2", type = AttributeType.NUMBER)
      @Pkg(label = "Timeout (Seconds)", default_value_type = DataType.NUMBER, default_value = "10")
      @NotEmpty Number timeout
  ) {

    try {
      if (session.isClosed()) {
        throw new BotCommandException("Valid browser automation session not found");
      }

      WebDriver driver = session.getDriver();

      // Wait for the page to load within the specified timeout
      new WebDriverWait(driver, Duration.ofSeconds(timeout.intValue()))
          .until(webDriver -> ((JavascriptExecutor) webDriver)
              .executeScript("return document.readyState")
              .toString().equals("complete"));

      // After waiting, check if the page is really loaded
      boolean isLoaded = ((JavascriptExecutor) driver)
          .executeScript("return document.readyState")
          .toString().equals("complete");

      return new BooleanValue(isLoaded);
    } catch (WebDriverException e) {
      throw new BotCommandException("Wait page loaded failed: Browser communication error: " + e.getMessage());
    } catch (Exception e) {
      throw new BotCommandException("Wait page loaded failed: " + e.getMessage());
    }


  }

}