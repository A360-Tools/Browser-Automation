package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.CredentialAllowPassword;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SelectModes;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@BotCommand
@CommandPkg(label = "Send Key Strokes", name = "sendkeys",
        description = "Send Keys to an Element",
        node_label = " to {{search}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Set", text_color = "#2F4F4F", background_color = "#2F4F4F")


public class SendKeys {
    private static final Map<String, Consumer<Actions>> specialKeys = new HashMap<>();

    static {
        specialKeys.put("CTRL DOWN", (a) -> a.keyDown(Keys.CONTROL));
        specialKeys.put("CTRL UP", (a) -> a.keyUp(Keys.CONTROL));
        specialKeys.put("SHIFT DOWN", (a) -> a.keyDown(Keys.SHIFT));
        specialKeys.put("SHIFT UP", (a) -> a.keyUp(Keys.SHIFT));
        specialKeys.put("ALT DOWN", (a) -> a.keyDown(Keys.ALT));
        specialKeys.put("ALT UP", (a) -> a.keyUp(Keys.ALT));
        specialKeys.put("ALT-GR DOWN", (a) -> a.keyDown(Keys.CONTROL).keyDown(Keys.ALT));
        specialKeys.put("ALT-GR UP", (a) -> a.keyUp(Keys.ALT).keyUp(Keys.CONTROL));
        specialKeys.put("ENTER", (a) -> a.sendKeys(Keys.ENTER));
        specialKeys.put("RETURN", (a) -> a.sendKeys(Keys.ENTER));
        specialKeys.put("NUM-ENTER", (a) -> a.sendKeys(Keys.ENTER));
        specialKeys.put("BACKSPACE", (a) -> a.sendKeys(Keys.BACK_SPACE));
        specialKeys.put("TAB", (a) -> a.sendKeys(Keys.TAB));
        specialKeys.put("ESCAPE", (a) -> a.sendKeys(Keys.ESCAPE));
        specialKeys.put("ESC", (a) -> a.sendKeys(Keys.ESCAPE));
        specialKeys.put("PAGE UP", (a) -> a.sendKeys(Keys.PAGE_UP));
        specialKeys.put("PAGE DOWN", (a) -> a.sendKeys(Keys.PAGE_DOWN));
        specialKeys.put("HOME", (a) -> a.sendKeys(Keys.HOME));
        specialKeys.put("LEFT ARROW", (a) -> a.sendKeys(Keys.ARROW_LEFT));
        specialKeys.put("UP ARROW", (a) -> a.sendKeys(Keys.ARROW_UP));
        specialKeys.put("RIGHT ARROW", (a) -> a.sendKeys(Keys.ARROW_RIGHT));
        specialKeys.put("DOWN ARROW", (a) -> a.sendKeys(Keys.ARROW_DOWN));
        specialKeys.put("DELETE", (a) -> a.sendKeys(Keys.DELETE));
        specialKeys.put("INSERT", (a) -> a.sendKeys(Keys.INSERT));
        specialKeys.put("DOLLAR", (a) -> a.sendKeys("$"));
        specialKeys.put("PAUSE", (a) -> a.sendKeys(Keys.PAUSE));
        specialKeys.put("END", (a) -> a.sendKeys(Keys.END));
        // Handle function keys F1 to F12 dynamically
        for (int i = 1; i <= 12; i++) {
            int currentKeyIndex = i;
            specialKeys.put("F" + i, (a) -> a.sendKeys(Keys.valueOf("F" + currentKeyIndex)));
        }
    }

    public static void handleSpecialKey(Actions action, String key, Boolean capsLockPressed) {
        if ("CAPS-LOCK".equals(key)) {
            capsLockPressed = !capsLockPressed;
            if (capsLockPressed) {
                action.keyDown(Keys.SHIFT); // Simulate Caps Lock being on by holding Shift
            } else {
                action.keyUp(Keys.SHIFT); // Release Shift when Caps Lock is toggled off
            }
        } else if (specialKeys.containsKey(key)) {
            specialKeys.get(key).accept(action);
        } else {
            // Unsupported special keys treated as normal text
            action.sendKeys("[" + key + "]");
        }
    }

    @Execute
    public static void action(
            @Idx(index = "1", type = AttributeType.SESSION) @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,

            @Idx(index = "2", type = AttributeType.TEXTAREA) @Pkg(label = "Search", description = "Should match the type", default_value_type = DataType.STRING)
            @NotEmpty String search,

            @Idx(index = "3", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "3.1", pkg = @Pkg(label = "Search by Element XPath", value = BrowserUtils.XPATH)),
                    @Idx.Option(index = "3.2", pkg = @Pkg(label = "Search by Element Id", value = BrowserUtils.ID)),
                    @Idx.Option(index = "3.3", pkg = @Pkg(label = "Search by Tag name", value = BrowserUtils.TAG)),
                    @Idx.Option(index = "3.4", pkg = @Pkg(label = "Search by CSS Selector", value = BrowserUtils.CSS)),
                    @Idx.Option(index = "3.5", pkg = @Pkg(label = "JavaScript", value = BrowserUtils.JS))})
            @Pkg(label = "Search Type", default_value = BrowserUtils.CSS, default_value_type = DataType.STRING)
            @NotEmpty String type,

            @Idx(index = "4", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "4.1", pkg = @Pkg(label = "Keys", value = "KEYS")),
                    @Idx.Option(index = "4.2", pkg = @Pkg(label = "Credential", value = "CREDENTIAL"))})
            @Pkg(label = "Keys", default_value = "KEYS", default_value_type = DataType.STRING)
            @SelectModes
            @NotEmpty
            String inputtype,

            @Idx(index = "4.1.1", type = AttributeType.KEYPRESS)
            @Pkg(label = "Keys", default_value_type = DataType.STRING)
            @NotEmpty
            String keys,

            @Idx(index = "4.2.1", type = AttributeType.CREDENTIAL)
            @Pkg(label = "Credential", default_value_type =
                    DataType.STRING)
            @NotEmpty
            @CredentialAllowPassword
            SecureString keyscredential,

            @Idx(index = "5", type = AttributeType.NUMBER)
            @Pkg(label = "Timeout (Seconds)", description = "No wait if 0", default_value_type = DataType.NUMBER, default_value = "0")
            @NotEmpty Number timeout,

            @Idx(index = "6", type = AttributeType.TEXT)
            @Pkg(label = "Wait for Attribute Value", default_value_type = DataType.STRING, default_value = "className")
            @NotEmpty String attribute

    ) {

        String keyString = "";
        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            if (inputtype.equals("KEYS")) {
                keyString = keys;
            } else {
                keyString = keyscredential.getInsecureString();
            }
            keyString = keyString.replace("\r\n", "[ENTER]").replace("\n", "[ENTER]");

            WebDriver driver = session.getDriver();
            WebElement element = BrowserUtils.waitForElementWithAttribute(driver, search, type, attribute, timeout.intValue());
            if (element == null) {
                throw new BotCommandException("Element did not load within timeout: Search by " + type + ", selector: " + search + ", attribute: " + attribute);
            }

            Actions action = new Actions(driver);
            boolean capslockPressed = false;

            int i = 0;
            while (i < keyString.length()) {
                char character = keyString.charAt(i);
                if (character == '[') {
                    // Extract the special key and handle accordingly
                    int endIndex = keyString.indexOf(']', i);
                    if (endIndex != -1) {
                        String key = keyString.substring(i + 1, endIndex);
                        handleSpecialKey(action, key, capslockPressed);
                        i = endIndex + 1; // Advance to the character after the closing ']'
                    } else {
                        i++; // Move to the next character
                    }
                } else {
                    action.sendKeys(String.valueOf(character));
                    i++;
                }
            }

            action.sendKeys(element, "").perform();
        } catch (Exception e) {
            throw new BotCommandException("SENDKEYS " + search + " " + type + " : " + e.getMessage());
        }
    }
}