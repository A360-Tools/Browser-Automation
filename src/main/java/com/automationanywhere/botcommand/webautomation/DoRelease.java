package com.automationanywhere.botcommand.webautomation;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;


@BotCommand
@CommandPkg(label = "Release", name = "release",
        description = "Release mouse button",
        node_label = "{{search}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Click",
        text_color = "#2F4F4F", background_color = "#2F4F4F")


public class DoRelease {

    @Execute
    public static void action(
            @Idx(index = "1", type = AttributeType.SESSION)
            @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
                    default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session) {

        try {
            if (session.isClosed()) {
                throw new BotCommandException("Valid browser automation session not found");
            }

            WebDriver driver = session.getDriver();
            Actions actions = new Actions(driver);
            actions.release().perform();
        } catch (Exception e) {
            throw new BotCommandException("Release failed " + e.getMessage());
        }
    }

}