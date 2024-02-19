package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
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

import java.util.LinkedHashMap;
import java.util.List;


@BotCommand
@CommandPkg(label = "Get Attributes", name = "getvalueselement",
        description = "Get attribute value of an element list",
        node_label = "and assign to {{returnTo}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Get", text_color = "#2F4F4F", background_color = "#2F4F4F",
        return_type = DataType.DICTIONARY, return_sub_type = DataType.STRING, return_description = "key = <search>", return_label = "Values", return_required = true)


public class GetAllValues {


    @Execute
    public static DictionaryValue action(
            @Idx(index = "1", type = AttributeType.SESSION)
            @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,

            @Idx(index = "2", type = AttributeType.TEXTAREA)
            @Pkg(label = "Selector value", description = "xpath,css," +
                    " or id etc. based on search type", default_value_type = DataType.STRING)
            @NotEmpty List<StringValue> searchList,

            @Idx(index = "3", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "3.1", pkg = @Pkg(label = "Search by Element XPath", value = BrowserUtils.XPATH)),
                    @Idx.Option(index = "3.2", pkg = @Pkg(label = "Search by Element Id", value = BrowserUtils.ID)),
                    @Idx.Option(index = "3.3", pkg = @Pkg(label = "Search by Tag name", value = BrowserUtils.TAG)),
                    @Idx.Option(index = "3.4", pkg = @Pkg(label = "Search by CSS Selector", value = BrowserUtils.CSS)),
                    @Idx.Option(index = "3.5", pkg = @Pkg(label = "JavaScript", value = BrowserUtils.JS))})
            @Pkg(label = "Search Type", default_value = BrowserUtils.CSS, default_value_type = DataType.STRING)
            @NotEmpty String type,

            @Idx(index = "4", type = AttributeType.TEXT)
            @Pkg(label = "Attribute", default_value = "value", default_value_type = DataType.STRING)
            @NotEmpty
            String attributename,

            @Idx(index = "5", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "5.1", pkg = @Pkg(label = "Simulate", value = BrowserUtils.MODE_SIMULATE)),
                    @Idx.Option(index = "5.2", pkg = @Pkg(label = "Javascript",
                            value = BrowserUtils.MODE_JAVASCRIPT))})
            @Pkg(label = "Input Type", default_value = BrowserUtils.MODE_JAVASCRIPT, default_value_type = DataType.STRING)
            @SelectModes
            @NotEmpty String interactionMode
    ) {
        LinkedHashMap<String, Value> values = new LinkedHashMap<>();
        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();

            for (StringValue stringValue : searchList) {
                String value = "";
                String search = stringValue.get();
                String jsPath = BrowserUtils.getJavaScriptPath(search, type);
                WebElement element = BrowserUtils.getElement(driver, jsPath, type);
                switch (interactionMode) {
                    case BrowserUtils.MODE_SIMULATE:
                        value = element.getAttribute(attributename);
                        break;
                    case BrowserUtils.MODE_JAVASCRIPT:
                        value = ((JavascriptExecutor) driver).executeScript(
                                "arguments[0].arguments[1];",
                                element, attributename).toString();
                        break;
                    default:
                        break;
                }
                values.put(search, new StringValue(value));
            }

        } catch (Exception e) {
            throw new BotCommandException("GETVALUES : " + e.getMessage());
        }

        DictionaryValue dictValue = new DictionaryValue();
        dictValue.set(values);
        return dictValue;
    }

}