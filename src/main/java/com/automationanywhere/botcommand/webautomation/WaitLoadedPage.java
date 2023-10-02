package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.impl.BooleanValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.WebDriver;

import static com.automationanywhere.commandsdk.model.AttributeType.SESSION;

@BotCommand
@CommandPkg(label = "Wait Page Loaded", name = "pageloaded",
        description = "Wait until Page is loaded",
        node_label = "for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Wait", text_color = "#2F4F4F", background_color = "#2F4F4F",
        return_type = DataType.BOOLEAN, return_label = "Status")


public class WaitLoadedPage {

    @Execute
    public BooleanValue action(

            @Idx(index = "1", type = SESSION) @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,
            @Idx(index = "2", type = AttributeType.NUMBER)
            @Pkg(label = "Timeout (Seconds)", default_value_type = DataType.NUMBER, default_value = "10")
            @NotEmpty Number timeout
    ) throws Exception {

        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();
            BrowserUtils utils = new BrowserUtils();
            Boolean isLoaded = utils.waitUntilPageLoaded(driver, timeout.intValue());
            return new BooleanValue(isLoaded);
        } catch (Exception e) {
            throw new BotCommandException("WAITPAGE : " + e.getMessage());
        }


    }

}