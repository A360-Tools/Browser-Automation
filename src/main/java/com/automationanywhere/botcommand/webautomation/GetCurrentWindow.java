package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.impl.StringValue;
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

@BotCommand
@CommandPkg(label = "Get Current Window", name = "getcurrentwindow",
    description = "Get Current Window Handle",
    node_label = "and assign to {{returnTo}} for session {{session}}", icon = "pkg.svg", group_label = "Window",
    comment = true, text_color = "#2F4F4F", background_color = "#2F4F4F",
    return_type = DataType.STRING, return_label = "Window Handle", return_required = true)

public class GetCurrentWindow {

  @Execute
  public static StringValue action(
      @Idx(index = "1", type = AttributeType.SESSION)
      @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
          default_value_type = DataType.SESSION, default_value = "Default")
      @NotEmpty
      @SessionObject
      BrowserConnection session
  ) {

    try {
      if (session.isClosed()) {
        throw new BotCommandException("Valid browser automation session not found");
      }

      WebDriver driver = session.getDriver();
      String window = driver.getWindowHandle();
      return new StringValue(window);
    } catch (Exception e) {
      throw new BotCommandException("Get current window failed : " + e.getMessage());
    }
  }

}