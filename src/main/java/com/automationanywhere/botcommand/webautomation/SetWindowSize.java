package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import static com.automationanywhere.commandsdk.model.DataType.STRING;


@BotCommand
@CommandPkg(label = "Set Window Dimension", name = "setwindowdimension",
        description = "Set current window dimension",
        node_label = "to {{width}} * {{height}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Window", text_color = "#2F4F4F", background_color = "#2F4F4F")


public class SetWindowSize {


    @Execute
    public void action(
            @Idx(index = "1", type = AttributeType.SESSION) @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,
            @Idx(index = "2", type = AttributeType.RADIO, options = {
                    @Idx.Option(index = "2.1", pkg = @Pkg(label = "Maximized", value = "maximized")),
                    @Idx.Option(index = "2.2", pkg = @Pkg(label = "Dimension", value = "dimensions")),
                    @Idx.Option(index = "2.3", pkg = @Pkg(label = "Full screen", value = "fullscreen")),
                    @Idx.Option(index = "2.4", pkg = @Pkg(label = "Minimized", value = "minimized"))
            })
            @Pkg(label = "Set window dimension", default_value = "maximized", default_value_type = STRING)
            @NotEmpty
            String selectMethod,

            @Idx(index = "2.2.1", type = AttributeType.NUMBER) @Pkg(label = "Window Width", default_value_type = DataType.NUMBER, default_value = "1920")
            @NotEmpty Number width,
            @Idx(index = "2.2.2", type = AttributeType.NUMBER) @Pkg(label = "Window Height", default_value_type = DataType.NUMBER, default_value = "1080")
            @NotEmpty Number height
    ) throws Exception {
        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();
            BrowserUtils utils = new BrowserUtils();

            switch (selectMethod) {
                case "maximized":
                    utils.maximizeCurrentWindow(driver);
                    break;
                case "minimized":
                    utils.minimizeCurrentWindow(driver);
                    break;
                case "dimensions":
                    Dimension dimension = new Dimension(width.intValue(), height.intValue());
                    utils.resizeCurrentWindow(driver, dimension);
                    break;
                case "fullscreen":
                    utils.fullScreenCurrentWindow(driver);
                    break;
            }

        } catch (Exception e) {
            throw new BotCommandException("Set window screen size : " + e.getMessage());
        }
    }

}