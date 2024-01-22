package com.automationanywhere.botcommand.webautomation;


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
@CommandPkg(label = "Navigate Page", name = "navigatepage",
        description = "Perform navigation on currently active tab",
        node_label = "{{navigateOption}} for session {{session}}", icon = "pkg.svg", group_label = "Page", comment = true, text_color = "#2F4F4F", background_color = "#2F4F4F")


public class Navigate {

    @Execute
    public void action(
            @Idx(index = "1", type = AttributeType.SESSION) @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,
            @Idx(index = "2", type = AttributeType.RADIO, options = {
                    @Idx.Option(index = "2.1", pkg = @Pkg(label = "Back", value = "back")),
                    @Idx.Option(index = "2.2", pkg = @Pkg(label = "Forward", value = "forward")),
                    @Idx.Option(index = "2.3", pkg = @Pkg(label = "Refresh", value = "refresh"))
            })
            @Pkg(label = "Navigation Type", default_value = "newtab", default_value_type = STRING) @NotEmpty String navigateOption
    ) throws Exception {

        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();
            BrowserUtils utils = new BrowserUtils();

            switch (navigateOption) {

                case "back":
                    utils.navigateBack(driver);
                    break;
                case "forward":
                    utils.navigateForward(driver);
                    break;
                case "refresh":
                    utils.refreshPage(driver);
                    break;
                default:
                    throw new BotCommandException("Invalid selection method");
            }

        } catch (Exception e) {
            throw new BotCommandException("Close currently active window: " + e.getMessage());
        }
    }

}