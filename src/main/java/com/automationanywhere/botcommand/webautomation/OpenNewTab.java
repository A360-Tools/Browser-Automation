package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Set;


@BotCommand
@CommandPkg(label = "Open New Tab", name = "opennewtab",
        description = "Opens new tab and moves driver focus to new tab",
        node_label = "in session {{session}}", icon = "pkg.svg", group_label = "Navigation", comment = true,
        text_color =
        "#2F4F4F", background_color = "#2F4F4F")


public class OpenNewTab {

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
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            Set<String> oldTabs = driver.getWindowHandles();
            jsExecutor.executeScript("window.open()");
            Set<String> newTabs = driver.getWindowHandles();
            newTabs.removeAll(oldTabs);
            if (newTabs.iterator().hasNext()) {
                driver.switchTo().window(newTabs.iterator().next());
            }
        } catch (Exception e) {
            throw new BotCommandException("Close currently active window: " + e.getMessage());
        }
    }

}