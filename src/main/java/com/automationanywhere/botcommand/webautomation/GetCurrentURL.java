package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.WebDriver;

@BotCommand
@CommandPkg(label = "Get Current URL", name = "getcurrenturl",
        description = "Get Current URL",
        node_label = "and assign to {{returnTo}} for session {{session}}", icon = "pkg.svg", comment = true,
        group_label = "Get", text_color = "#2F4F4F", background_color = "#2F4F4F",
        return_type = DataType.STRING, return_label = "URL", return_required = true)


public class GetCurrentURL {

    @Execute
    public static StringValue action(
            @Idx(index = "1", type = AttributeType.SESSION)
            @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
                    default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session
    ) {
        String value = "";
        try {
            if (session.isClosed()) {
                throw new BotCommandException("Valid browser automation session not found");
            }

            WebDriver driver = session.getDriver();
            value = driver.getCurrentUrl();
        } catch (Exception e) {
            throw new BotCommandException("Get current URL failed : " + e.getMessage());
        }

        return new StringValue(value);


    }

}