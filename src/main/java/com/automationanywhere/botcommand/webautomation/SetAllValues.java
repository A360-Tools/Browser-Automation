package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@BotCommand
@CommandPkg(label = "Set Values", name = "setvalueselement",
    description = "Set values of an element list",
    node_label = "for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Set", text_color =
    "#2F4F4F", background_color = "#2F4F4F")

public class SetAllValues {

  @Execute
  public static void action(
      @Idx(index = "1", type = AttributeType.SESSION)
      @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
          default_value_type = DataType.SESSION, default_value = "Default")
      @NotEmpty
      @SessionObject
      BrowserConnection session,

      @Idx(index = "2", type = AttributeType.LIST)
      @Pkg(label = "Searches", description = "Should match the type", default_value_type = DataType.STRING)
      @NotEmpty List<StringValue> searchList,

      @Idx(index = "3", type = AttributeType.SELECT, options = {
          @Idx.Option(index = "3.1", pkg = @Pkg(label = "Search by Element XPath", value =
              BrowserUtils.XPATH)),
          @Idx.Option(index = "3.2", pkg = @Pkg(label = "Search by Element Id", value = BrowserUtils.ID)),
          @Idx.Option(index = "3.3", pkg = @Pkg(label = "Search by Tag name", value = BrowserUtils.TAG)),
          @Idx.Option(index = "3.4", pkg = @Pkg(label = "Search by CSS Selector", value = BrowserUtils.CSS)),
          @Idx.Option(index = "3.5", pkg = @Pkg(label = "JavaScript", value = BrowserUtils.JS))})
      @Pkg(label = "Search Type", default_value = BrowserUtils.CSS, default_value_type = DataType.STRING)
      @NotEmpty String type,

      @Idx(index = "4", type = AttributeType.LIST)
      @Pkg(label = "Values", default_value_type = DataType.STRING)
      @NotEmpty List<StringValue> newvalues
  ) {
    try {
      if (session.isClosed()) {
        throw new BotCommandException("Valid browser automation session not found");
      }
      WebDriver driver = session.getDriver();
      JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
      int i = 0;
      while (i < searchList.size()) {
        String search = searchList.get(i).get();
        WebElement element = BrowserUtils.getElement(driver, search, type);
        jsExecutor.executeScript(
            "var input = arguments[0];" + // Reference to your element
                "input.value = arguments[1];" + // Set the value you want
                "var event = new Event('change', {" +
                "bubbles: true," + // Event should bubble for most event handlers
                "cancelable: true" + // Event can be canceled
                "});" +
                "input.dispatchEvent(event);", // Dispatch the 'change' event
            element, newvalues.get(i).get());
        i++;
      }

    } catch (Exception e) {
      throw new BotCommandException("Set all values failed : " + e.getMessage());
    }
  }

}