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

@BotCommand
@CommandPkg(label = "Reset To Default", name = "todefaultcontent",
    description = "Reset to Default Content (if set to a frame before)",
    node_label = "content for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Frame",
    text_color = "#2F4F4F", background_color = "#2F4F4F")

public class ToDefault {

  @Execute
  public static void action(
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
      driver.switchTo().defaultContent();
    } catch (Exception e) {
      throw new BotCommandException("Reset to default failed " + e.getMessage());
    }


  }

}