package com.automationanywhere.botcommand.webautomation;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.DataType;

import static com.automationanywhere.commandsdk.model.AttributeType.SESSION;


@BotCommand
@CommandPkg(label = "End session", name = "EndSessionWebAutomation", group_label = "Session", description = "Ends session and closes the browser",
        comment = true,
        text_color = "#2F4F4F",
        background_color = "#2F4F4F",
        icon = "pkg.svg",
        node_label = "{{session}}")
public class Admin2_EndSessionWebAutomation {

    @Execute
    public void end(
            @Idx(index = "1", type = SESSION) @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session) {
        if (session.isClosed()) {
            throw new BotCommandException("Valid browser automation session not found");
        } else {
            session.close();
        }
    }

}