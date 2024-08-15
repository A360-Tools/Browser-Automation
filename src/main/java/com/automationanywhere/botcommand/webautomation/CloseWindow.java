package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;

import java.util.Set;


@BotCommand
@CommandPkg(label = "Close Window", name = "closewindow",
        description = "Closes currently active window",
        node_label = "currently active for session {{session}}", icon = "pkg.svg", group_label = "Window", comment =
        true, text_color = "#2F4F4F", background_color = "#2F4F4F")


public class CloseWindow {

    @Execute
    public static void action(
            @Idx(index = "1", type = AttributeType.SESSION)
            @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
                    default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session
    ) {

        try {
            if (session.isClosed()) {
                throw new BotCommandException("Valid browser automation session not found");
            }

            WebDriver driver = session.getDriver();
            try {
                driver.close();
                Set<String> handles = driver.getWindowHandles();
                String lastHandle = handles.toArray(new String[ 0 ])[ handles.size() - 1 ];
                driver.switchTo().window(lastHandle);
            } catch (NoSuchSessionException ignored) {
            }


        } catch (Exception e) {
            throw new BotCommandException("Close currently active window failed: " + e.getMessage());
        }
    }

}