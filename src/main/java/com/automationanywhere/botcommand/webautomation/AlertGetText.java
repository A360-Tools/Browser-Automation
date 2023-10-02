package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import static com.automationanywhere.commandsdk.model.AttributeType.SESSION;
import static com.automationanywhere.commandsdk.model.DataType.STRING;


@BotCommand
@CommandPkg(label = "Get Text", name = "alertgettext",
        description = "Get Alert text message",
        return_type = STRING,
        return_required = true,
        node_label = "for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Alerts", text_color = "#2F4F4F", background_color = "#2F4F4F")


public class AlertGetText {


    @Execute
    public StringValue action(
            @Idx(index = "1", type = SESSION)
            @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session

    ) throws Exception {
        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();
            BrowserUtils utils = new BrowserUtils();
            String alertText = "";
            try {
                alertText = utils.getAlert(driver);
            } catch (NoAlertPresentException ignored) {

            }
            utils.toDefault(driver);
            return new StringValue(alertText);
        } catch (Exception e) {
            throw new BotCommandException("Get alert text unsuccessful: " + e.getMessage());
        }
    }


}