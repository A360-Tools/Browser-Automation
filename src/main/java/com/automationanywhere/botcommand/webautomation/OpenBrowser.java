package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

@BotCommand
@CommandPkg(label = "Open URL", name = "openurl",
        description = "Open Page with provided URL",
        node_label = "{{url}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Navigation",
        text_color = "#2F4F4F", background_color = "#2F4F4F")


public class OpenBrowser {

    @Execute
    public static void action(
            @Idx(index = "1", type = AttributeType.SESSION) @Pkg(label = "Browser Automation session",
                    description = "Set valid Browser Automation session", default_value_type = DataType.SESSION,
                    default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,

            @Idx(index = "2", type = AttributeType.TEXT) @Pkg(label = "URL", default_value_type = DataType.STRING)
            @NotEmpty String url,

            @Idx(index = "3", type = AttributeType.RADIO, options = {
                    @Idx.Option(index = "3.1", pkg = @Pkg(label = "Maximized", value = "maximized")),
                    @Idx.Option(index = "3.2", pkg = @Pkg(label = "Dimension", value = "dimensions")),
                    @Idx.Option(index = "3.3", pkg = @Pkg(label = "Full screen", value = "fullscreen")),
                    @Idx.Option(index = "3.4", pkg = @Pkg(label = "Minimized", value = "minimized"))
            })
            @Pkg(label = "Set window dimension", default_value = "maximized", default_value_type = DataType.STRING)
            @NotEmpty
            String selectMethod,

            @Idx(index = "3.2.1", type = AttributeType.NUMBER) @Pkg(label = "Window Width", default_value_type =
                    DataType.NUMBER, default_value = "1920")
            @NotEmpty Number width,

            @Idx(index = "3.2.2", type = AttributeType.NUMBER) @Pkg(label = "Window Height", default_value_type =
                    DataType.NUMBER, default_value = "1080")
            @NotEmpty Number height
    ) {
        try {
            if (session.isClosed()) {
                throw new BotCommandException("Valid browser automation session not found");
            }

            WebDriver driver = session.getDriver();
            switch (selectMethod) {
                case "maximized":
                    driver.manage().window().maximize();
                    break;
                case "minimized":
                    driver.manage().window().minimize();
                    break;
                case "dimensions":
                    Dimension dimension = new Dimension(width.intValue(), height.intValue());
                    driver.manage().window().setSize(dimension);
                    break;
                case "fullscreen":
                    driver.manage().window().fullscreen();
                    break;
                default:
                    throw new BotCommandException("Invalid selection method");
            }

            driver.get(url);
        } catch (Exception e) {
            throw new BotCommandException("OPENPAGE " + url + " : " + e.getMessage());
        }
    }

}