package com.automationanywhere.botcommand.webautomation;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.WebDriver;

import java.util.regex.Pattern;

@BotCommand
@CommandPkg(label = "Select Window", name = "selectwindow",
        description = "Select/Activate by window's handle or title",
        node_label = "with selection method {{selectionMethod}} for session {{session}}",
        icon = "pkg.svg", group_label = "Window", comment = true,
        text_color = "#2F4F4F", background_color = "#2F4F4F")

public class SelectWindow {

    @Execute
    public static void action(
            @Idx(index = "1", type = AttributeType.SESSION)
            @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
                    default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,

            @Idx(index = "2", type = AttributeType.RADIO, options = {
                    @Idx.Option(index = "2.1", pkg = @Pkg(label = "By Handle", value = "byHandle")),
                    @Idx.Option(index = "2.2", pkg = @Pkg(label = "By Title(Regex match)", value = "byTitle"))})
            @Pkg(label = "Selection Method", default_value = "byHandle", default_value_type = DataType.STRING)
            @NotEmpty String selectionMethod,

            @Idx(index = "3", type = AttributeType.TEXT)
            @Pkg(label = "Window Handle or Regex to match title", default_value_type = DataType.STRING)
            @NotEmpty String handleOrTitle
    ) {

        try {
            if (session.isClosed()) {
                throw new BotCommandException("Valid browser automation session not found");
            }

            WebDriver driver = session.getDriver();
            switch (selectionMethod) {
                case "byHandle":
                    driver.switchTo().window(handleOrTitle);
                    break;
                case "byTitle":
                    String originalWindow = driver.getWindowHandle();
                    Pattern pattern = Pattern.compile(handleOrTitle);
                    boolean matchFound = false;

                    for (String handle : driver.getWindowHandles()) {
                        driver.switchTo().window(handle);
                        if (pattern.matcher(driver.getTitle()).find()) {
                            matchFound = true;
                            break; //
                        }
                    }

                    if (!matchFound) {
                        driver.switchTo().window(originalWindow);
                        throw new BotCommandException("No window with title matching regex '" + handleOrTitle + "' " +
                                "found");
                    }
                    break;
                default:
                    throw new BotCommandException("Invalid selection method");
            }
        } catch (Exception e) {
            throw new BotCommandException("Select window failed: " + e.getMessage());
        }
    }
}
