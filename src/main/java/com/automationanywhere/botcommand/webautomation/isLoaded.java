package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.impl.BooleanValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;


@BotCommand
@CommandPkg(label = "Get Load Status", name = "isloaded",
        description = "Is Page Loaded",
        node_label = "and assign to {{returnTo}} for session {{session}}", icon = "pkg.svg", comment = true,
        group_label = "Get", text_color = "#2F4F4F", background_color = "#2F4F4F",
        return_type = DataType.BOOLEAN, return_label = "Status", return_required = true)


public class isLoaded {

    @Execute
    public static BooleanValue action(
            @Idx(index = "1", type = AttributeType.SESSION)
            @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
                    default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session
    ) {
        boolean isLoaded;
        try {
            if (session.isClosed()) {
                throw new BotCommandException("Valid browser automation session not found");
            }

            WebDriver driver = session.getDriver();
            isLoaded =
                    ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equalsIgnoreCase(
                    "complete");
        } catch (Exception e) {
            throw new BotCommandException("PAGEISLOADED : " + e.getMessage());
        }
        return new BooleanValue(isLoaded);
    }

}