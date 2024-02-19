package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

@BotCommand
@CommandPkg(label = "Get Details", name = "elementdetails",
        description = "Get Element Details",
        node_label = "of {{search}} and assign to {{returnTo}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Get", text_color = "#2F4F4F", background_color = "#2F4F4F",
        return_type = DataType.DICTIONARY, return_label = "Details with keys:tag, text, value, class, name,id, topX, " +
        "topY, height,width",
        return_required = true)


public class GetDetails {

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
            @Pkg(label = "Timeout (Seconds)", description = "No wait " +
                    "if 0", default_value_type = DataType.NUMBER, default_value = "0")
            @NotEmpty Number timeout,

            @Idx(index = "5", type = AttributeType.TEXT)
            @Pkg(label = "Wait for Attribute Value", default_value_type
                    = DataType.STRING, default_value = "className")
            @NotEmpty String attribute
    ) {

        HashMap<String, Value> detailsMapValue = new HashMap<>();

        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();
            String jsPath = BrowserUtils.getJavaScriptPath(search, type);
            boolean elementLoaded = BrowserUtils.waitForElementWithAttribute(driver, jsPath, attribute, timeout.intValue());
            if (!elementLoaded)
                throw new BotCommandException("Element did not load within timeout: Search by " + type + ", and " + "selector: " + search);
            WebElement element = BrowserUtils.getElement(driver, search, type);

            HashMap<String, String> details = new HashMap<>();
            if (element != null) {

                String elAttribute = element.getTagName();
                if (elAttribute != null) {
                    details.put("tag", elAttribute);
                }
                elAttribute = element.getText();
                if (elAttribute != null) {
                    details.put("text", elAttribute);
                }
                elAttribute = element.getAttribute("value");
                if (elAttribute != null) {
                    details.put("value", elAttribute);
                }
                elAttribute = element.getAttribute("class");
                if (elAttribute != null) {
                    details.put("class", elAttribute);
                }
                elAttribute = element.getAttribute("name");
                if (elAttribute != null) {
                    details.put("name", elAttribute);
                }
                elAttribute = element.getAttribute("id");
                if (elAttribute != null) {
                    details.put("id", elAttribute);
                }
                elAttribute = ((Integer) element.getRect().getX()).toString();
                details.put("topX", elAttribute);
                elAttribute = ((Integer) element.getRect().getY()).toString();
                details.put("topY", elAttribute);
                elAttribute = ((Integer) element.getRect().getHeight()).toString();
                details.put("height", elAttribute);
                elAttribute = ((Integer) element.getRect().getWidth()).toString();
                details.put("width", elAttribute);
            }
            for (Map.Entry<String, String> entry : details.entrySet()) {
                detailsMapValue.put(entry.getKey(), new StringValue(entry.getValue()));
            }
            DictionaryValue dictValue = new DictionaryValue();
            dictValue.set(detailsMapValue);
            return dictValue;
        } catch (Exception e) {
            throw new BotCommandException("GETDETAILS " + search + " " + type + " : " + e.getMessage());
        }

    }

}