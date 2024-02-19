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
import org.openqa.selenium.support.ui.Select;


@BotCommand
@CommandPkg(label = "Select", name = "selectelement",
        description = "Select an option from dropdown",
        node_label = "element {{search}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Set", text_color = "#2F4F4F", background_color = "#2F4F4F")


public class DoSelect {

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

            @Idx(index = "4", type = AttributeType.TEXT)
            @Pkg(label = "Value", default_value_type = DataType.STRING)
            @NotEmpty String newvalue,

            @Idx(index = "5", type = AttributeType.NUMBER)
            @Pkg(label = "Timeout (Seconds)", description = "No wait if 0", default_value_type = DataType.NUMBER, default_value = "0")
            @NotEmpty Number timeout,

            @Idx(index = "6", type = AttributeType.TEXT)
            @Pkg(label = "Wait for Attribute Value", default_value_type
                    = DataType.STRING, default_value = "className")
            @NotEmpty String attribute,

            @Idx(index = "7", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "7.1", pkg = @Pkg(label = "Simulate", value = BrowserUtils.MODE_SIMULATE)),
                    @Idx.Option(index = "7.2", pkg = @Pkg(label = "Javascript",
                            value = BrowserUtils.MODE_JAVASCRIPT))})
            @Pkg(label = "Input Type", default_value = BrowserUtils.MODE_SIMULATE, default_value_type = DataType.STRING)
            @SelectModes
            @NotEmpty String interactionMode
    ) {

        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();
            String jsPath = BrowserUtils.getJavaScriptPath(search, type);
            boolean elementLoaded = BrowserUtils.waitForElementWithAttribute(driver, jsPath, attribute, timeout.intValue());
            if (!elementLoaded)
                throw new BotCommandException("Element did not load within timeout: Search by " + type + ", and " + "selector: " + search);
            WebElement element = BrowserUtils.getElement(driver, search, type);
            switch (interactionMode) {
                case BrowserUtils.MODE_SIMULATE:
                    new Select(element).selectByVisibleText(newvalue);
                    break;
                case BrowserUtils.MODE_JAVASCRIPT:
                    ((JavascriptExecutor) driver).executeScript(
                            "var dropdown = arguments[0];" + // The dropdown element
                                    "var optionText = arguments[1];" + // The text of the option to select
                                    "Array.from(dropdown.options).forEach(function(option) {" + // Iterate over options
                                    "    if (option.text === optionText) {" + // Check if the option's text matches
                                    "        option.selected = true;" + // Select the matching option
                                    "    }" +
                                    "});" +
                                    "var event = new Event('change', {" + // Create a new 'change' event
                                    "    bubbles: true," + // Event should bubble for most event handlers
                                    "    cancelable: true" + // Event can be canceled
                                    "});" +
                                    "dropdown.dispatchEvent(event);", // Dispatch the 'change' event
                            element, newvalue);

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            throw new BotCommandException("SELECT " + search + " " + type + " : " + e.getMessage());
        }


    }

}