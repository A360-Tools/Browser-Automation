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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

@BotCommand
@CommandPkg(label = "Navigate Page", name = "navigatepage",
    description = "Perform navigation on currently active tab",
    node_label = "{{navigateOption}} for session {{session}}", icon = "pkg.svg", group_label = "Page", comment =
    true, text_color = "#2F4F4F", background_color = "#2F4F4F")

public class Navigate {

  @Execute
  public static void action(
      @Idx(index = "1", type = AttributeType.SESSION)
      @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
          default_value_type = DataType.SESSION, default_value = "Default")
      @NotEmpty
      @SessionObject
      BrowserConnection session,
      @Idx(index = "2", type = AttributeType.RADIO, options = {
          @Idx.Option(index = "2.1", pkg = @Pkg(label = "Back", value = "back")),
          @Idx.Option(index = "2.2", pkg = @Pkg(label = "Forward", value = "forward")),
          @Idx.Option(index = "2.3", pkg = @Pkg(label = "Refresh", value = "refresh"))
      })
      @Pkg(label = "Navigation Type", default_value = "newtab", default_value_type = DataType.STRING)
      @NotEmpty String navigateOption
  ) {

    try {
      if (session.isClosed()) {
        throw new BotCommandException("Valid browser automation session not found");
      }

      WebDriver driver = session.getDriver();
      switch (navigateOption) {
        case "back":
          driver.navigate().back();
          break;
        case "forward":
          driver.navigate().forward();
          break;
        case "refresh":
          driver.navigate().refresh();
          break;
        default:
          throw new BotCommandException("Invalid selection method");
      }

    } catch (WebDriverException e) {
      throw new BotCommandException("Navigate failed: Browser communication error: " + e.getMessage());
    } catch (Exception e) {
      throw new BotCommandException("Navigate window failed: " + e.getMessage());
    }
  }

}