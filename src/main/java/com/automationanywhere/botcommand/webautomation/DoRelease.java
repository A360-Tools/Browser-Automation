package com.automationanywhere.botcommand.webautomation;

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
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;


@BotCommand
@CommandPkg(label = "Release", name = "release",
    description = "Release mouse button",
    node_label = "{{search}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Click",
    text_color = "#2F4F4F", background_color = "#2F4F4F")

public class DoRelease {

  @Execute
  public static void action(
      @Idx(index = "1", type = AttributeType.SESSION)
      @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
          default_value_type = DataType.SESSION, default_value = "Default")
      @NotEmpty
      @SessionObject
      BrowserConnection session) {

    try {
      if (session.isClosed()) {
        throw new BotCommandException("Valid browser automation session not found");
      }

      WebDriver driver = session.getDriver();
      Actions actions = new Actions(driver);
      actions.release().perform();
    } catch (StaleElementReferenceException e) {
      throw new BotCommandException("Release failed" + ": Element is no longer attached to the DOM. The page may have refreshed or the element was removed.");
    } catch (ElementNotInteractableException e) {
      throw new BotCommandException("Release failed" + ": Element found but not interactable. It may be hidden, disabled, or covered.");
    } catch (TimeoutException e) {
      throw new BotCommandException("Release failed" + ": Timed out waiting for element. " + e.getMessage());
    } catch (InvalidSelectorException e) {
      throw new BotCommandException("Release failed" + ": Invalid selector. " + e.getMessage());
    } catch (Exception e) {
      throw new BotCommandException("Release failed " + e.getMessage());
    }
  }

}