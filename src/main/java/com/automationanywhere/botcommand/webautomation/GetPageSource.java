package com.automationanywhere.botcommand.webautomation;


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

import static com.automationanywhere.commandsdk.model.DataType.STRING;

@BotCommand
@CommandPkg(label = "Get Page Source", name = "pagesource",
        description = "Get Page Source of a Page",
        node_label = "and assign to {{returnTo}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Get", text_color = "#2F4F4F", background_color = "#2F4F4F",
        return_type = STRING, return_label = "HTML", return_required = true)


public class GetPageSource {

    @Execute
    public StringValue action(
            @Idx(index = "1", type = AttributeType.SESSION) @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session
    ) throws Exception {

        String html = "";
        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();
            BrowserUtils utils = new BrowserUtils();
            html = utils.getPageContent(driver);
        } catch (Exception e) {
            throw new BotCommandException("GETPAGESORUCE : " + e.getMessage());
        }
        return new StringValue(html);

    }

}