package com.automationanywhere.botcommand.webautomation;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.CredentialAllowPassword;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;


@BotCommand
@CommandPkg(label = "Set Text", name = "alertsettext",
    description = "Set Alert prompt message",
    return_type = DataType.STRING,
    return_required = true,
    node_label = "for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Alerts", text_color
    = "#2F4F4F", background_color = "#2F4F4F")

public class AlertSetText {


  @Execute
  public static void action(
      @Idx(index = "1", type = AttributeType.SESSION)
      @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
          default_value_type = DataType.SESSION, default_value = "Default")
      @NotEmpty
      @SessionObject
      BrowserConnection session,
      @Idx(index = "2", type = AttributeType.CREDENTIAL)
      @Pkg(label = "Credential", default_value_type =
          DataType.STRING)
      @NotEmpty
      @CredentialAllowPassword
      SecureString keyscredential

  ) {
    try {
      if (session.isClosed()) {
        throw new BotCommandException("Valid browser automation session not found");
      }

      WebDriver driver = session.getDriver();
      try {
        driver.switchTo().alert().sendKeys(keyscredential.getInsecureString());
      } catch (NoAlertPresentException ignored) {

      }

    } catch (Exception e) {
      throw new BotCommandException("Set alert text unsuccessful: " + e.getMessage());
    }
  }


}