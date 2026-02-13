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
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;


@BotCommand
@CommandPkg(label = "Execute JavaScript", name = "executejs",
    description = "Execute JavaScript",
    node_label = "and assign to {{returnTo}} for session {{session}}", icon = "pkg.svg", comment = true,
    group_label = "JavaScript", text_color = "#2F4F4F", background_color = "#2F4F4F",
    return_type = DataType.STRING, return_label = "Value")

public class ExecuteJS {

  @Execute
  public static StringValue action(
      @Idx(index = "1", type = AttributeType.SESSION)
      @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
          default_value_type = DataType.SESSION, default_value = "Default")
      @NotEmpty
      @SessionObject
      BrowserConnection session,

      @Idx(index = "2", type = AttributeType.TEXTAREA)
      @Pkg(label = "JavaScript Code", description =
          "Use return keyword if expecting output value, eg: return " +
              "document.title", default_value_type = DataType.STRING)
      @NotEmpty String js
  ) {
    String value = "";
    try {
      if (session.isClosed()) {
        throw new BotCommandException("Valid browser automation session not found");
      }

      WebDriver driver = session.getDriver();
      String library = session.getLibrary();
      String fulljs = library + "\r\n" + js;
      JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
      value = jsExecutor.executeScript(fulljs).toString();

    } catch (JavascriptException e) {
      throw new BotCommandException("Execute JavaScript failed: Script execution error: " + e.getMessage());
    } catch (Exception e) {
      throw new BotCommandException("Execute JavaScript failed : " + e.getMessage());
    }
    return new StringValue(value);
  }

}