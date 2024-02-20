package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SelectModes;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


@BotCommand
@CommandPkg(label = "Clear Input", name = "clearinput",
        description = "Clears Input for an Element",
        node_label = "{{search}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Set", text_color = "#2F4F4F", background_color = "#2F4F4F")


public class ClearInput {


    @Execute
    public static void action(
            @Idx(index = "1", type = AttributeType.SESSION)
            @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,

            @Idx(index = "2", type = AttributeType.TEXTAREA)
            @Pkg(label = "Selector value", description = "xpath,css," +
                    " or id etc. based on search type", default_value_type = DataType.STRING)
            @NotEmpty String search,

            @Idx(index = "3", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "3.1", pkg = @Pkg(label = "Search by Element XPath", value = BrowserUtils.XPATH)),
                    @Idx.Option(index = "3.2", pkg = @Pkg(label = "Search by Element Id", value = BrowserUtils.ID)),
                    @Idx.Option(index = "3.3", pkg = @Pkg(label = "Search by Tag name", value = BrowserUtils.TAG)),
                    @Idx.Option(index = "3.4", pkg = @Pkg(label = "Search by CSS Selector", value = BrowserUtils.CSS)),
                    @Idx.Option(index = "3.5", pkg = @Pkg(label = "JavaScript", value = BrowserUtils.JS))})
            @Pkg(label = "Search Type", default_value = BrowserUtils.CSS, default_value_type = DataType.STRING)
            @NotEmpty String type,

            @Idx(index = "4", type = AttributeType.NUMBER)
            @Pkg(label = "Timeout (Seconds)", description = "No wait if 0", default_value_type = DataType.NUMBER, default_value = "0")
            @NotEmpty Number timeout,

            @Idx(index = "5", type = AttributeType.TEXT)
            @Pkg(label = "Wait for Attribute Value", default_value_type = DataType.STRING, default_value = "className")
            @NotEmpty String attribute,

            @Idx(index = "6", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "6.1", pkg = @Pkg(label = "Simulate", value = BrowserUtils.MODE_SIMULATE)),
                    @Idx.Option(index = "6.2", pkg = @Pkg(label = "Javascript",
                            value = BrowserUtils.MODE_JAVASCRIPT))})
            @Pkg(label = "Input Type", default_value = BrowserUtils.MODE_SIMULATE, default_value_type = DataType.STRING)
            @SelectModes
            @NotEmpty String interactionMode
    ) {

        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();
            WebElement element = BrowserUtils.waitForElementWithAttribute(driver, search, type, attribute, timeout.intValue());
            if (element == null) {
                throw new BotCommandException("Element did not load within timeout: Search by " + type + ", selector: " + search + ", attribute: " + attribute);
            }
            switch (interactionMode) {
                case BrowserUtils.MODE_SIMULATE:
                    element.clear();
                    break;
                case BrowserUtils.MODE_JAVASCRIPT:
                    ((JavascriptExecutor) driver).executeScript(
                            "var input = arguments[0];" + // Reference to your element
                                    "input.value = '';" + // Set the value you want
                                    "var event = new Event('change', {" +
                                    "bubbles: true," + // Event should bubble for most event handlers
                                    "cancelable: true" + // Event can be canceled
                                    "});" +
                                    "input.dispatchEvent(event);", // Dispatch the 'change' event
                            element);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            throw new BotCommandException("CLEAR " + search + " " + type + " : " + e.getMessage());
        }

    }

}