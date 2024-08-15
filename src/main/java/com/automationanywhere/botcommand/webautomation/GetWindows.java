package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@BotCommand
@CommandPkg(label = "Get Windows", name = "getwindows",
        description = "Get Windows",
        node_label = "of session {{sessionName}} and assign to {{returnTo}} for session {{session}}", icon = "pkg" +
        ".svg", group_label = "Window", comment = true, text_color = "#2F4F4F", background_color = "#2F4F4F",
        return_type = DataType.LIST, return_sub_type = DataType.STRING, return_label = "Window Handles",
        return_required = true)

public class GetWindows {

    @Execute
    public static ListValue action(
            @Idx(index = "1", type = AttributeType.SESSION) @Pkg(label = "Browser Automation session",
                    description = "Set valid Browser Automation session", default_value_type = DataType.SESSION,
                    default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session
    ) {
        try {
            if (session.isClosed()) {
                throw new BotCommandException("Valid browser automation session not found");
            }

            WebDriver driver = session.getDriver();
            Set<String> handles = driver.getWindowHandles();
            List<StringValue> handleList = new ArrayList<>();
            for (String handle : handles) {
                handleList.add(new StringValue(handle));
            }
            ListValue returnList = new ListValue();
            returnList.set(handleList);
            return returnList;
        } catch (Exception e) {
            throw new BotCommandException("Get windows failed : " + e.getMessage());
        }
    }
}