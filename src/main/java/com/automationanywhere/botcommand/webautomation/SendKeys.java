package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.*;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

@BotCommand
@CommandPkg(label = "Send Key Strokes", name = "sendkeys",
        description = "Send Keys to an Element",
        node_label = " to {{search}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Set", text_color = "#2F4F4F", background_color = "#2F4F4F")


public class SendKeys {
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
            @CredentialOnly
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

            WebDriver driver = session.getDriver();
            if (inputtype.equals("KEYS")) {
                keyString = keys;
            } else {
                keyString = keyscredential.getInsecureString();
            }
            keyString = keyString.replace("\r\n", "[ENTER]").replace("\n", "[ENTER]");

            String jsPath = BrowserUtils.getJavaScriptPath(search, type);
            boolean elementLoaded = BrowserUtils.waitForElementWithAttribute(driver, jsPath, attribute, timeout.intValue());
            if (!elementLoaded)
                throw new BotCommandException("Element did not load within timeout: Search by " + type + ", and " + "selector: " + search);
            WebElement element = BrowserUtils.getElement(driver, search, type);

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
                        if (key.equals("CAPS-LOCK"))
                            capslockPressed = !capslockPressed;
                        i = endIndex + 1; // Advance to the character after the closing ']'
                    } else {
                        i++; // Move to the next character
                    }
                } else {
                    action.sendKeys(Character.toString(character));
                    i++;
                }
            }

            action.sendKeys(element, "").perform();
        } catch (Exception e) {
            throw new BotCommandException("SENDKEYS " + search + " " + type + " : " + e.getMessage());
        }
    }

    private static void handleSpecialKey(Actions action, String key, boolean capslockPressed) {
        switch (key) {
            case "CTRL DOWN":
                action.keyDown(Keys.CONTROL);
                break;
            case "CTRL UP":
                action.keyUp(Keys.CONTROL);
                break;
            case "SHIFT DOWN":
                action.keyDown(Keys.SHIFT);
                break;
            case "SHIFT UP":
                action.keyUp(Keys.SHIFT);
                break;
            case "ALT DOWN":
                action.keyDown(Keys.ALT);
                break;
            case "ALT UP":
                action.keyUp(Keys.ALT);
                break;
            case "ALT-GR DOWN":
                action.keyDown(Keys.CONTROL).keyDown(Keys.ALT);
                break;
            case "ALT-GR UP":
                action.keyUp(Keys.ALT).keyUp(Keys.CONTROL);
                break;
            case "PAGE DOWN":
                action.sendKeys(Keys.PAGE_DOWN);
                break;
            case "PAGE UP":
                action.sendKeys(Keys.PAGE_UP);
                break;
            case "ENTER":
            case "RETURN":
            case "NUM-ENTER":
                action.sendKeys(Keys.ENTER);
                break;
            case "BACKSPACE":
                action.sendKeys(Keys.BACK_SPACE);
                break;
            case "INSERT":
                action.sendKeys(Keys.INSERT);
                break;
            case "DELETE":
                action.sendKeys(Keys.DELETE);
                break;
            case "HOME":
                action.sendKeys(Keys.HOME);
                break;
            case "PAUSE":
                action.sendKeys(Keys.PAUSE);
                break;
            case "DOLLAR":
                action.sendKeys("$");
                break;
            case "TAB":
                action.sendKeys(Keys.TAB);
                break;
            case "END":
                action.sendKeys(Keys.END);
                break;
            case "LEFT-ARROW":
                action.sendKeys(Keys.ARROW_LEFT);
                break;
            case "RIGHT-ARROW":
                action.sendKeys(Keys.ARROW_RIGHT);
                break;
            case "UP-ARROW":
                action.sendKeys(Keys.ARROW_UP);
                break;
            case "DOWN-ARROW":
                action.sendKeys(Keys.ARROW_DOWN);
                break;
            case "ESC":
                action.sendKeys(Keys.ESCAPE);
                break;
            case "CAPS-LOCK":
                if (!capslockPressed) {
                    action.keyDown(Keys.SHIFT);
                } else {
                    action.keyUp(Keys.SHIFT);
                }
                break;
            case "F1":
            case "F2":
            case "F3":
            case "F4":
            case "F5":
            case "F6":
            case "F7":
            case "F8":
            case "F9":
            case "F10":
            case "F11":
            case "F12":
                handleFunctionKey(action, key);
                break;
            default:
                throw new BotCommandException("Unsupported Key: " + key);
        }
    }

    private static void handleFunctionKey(Actions action, String key) {
        int keyCode = Integer.parseInt(key.substring(1));
        action.sendKeys(Keys.valueOf("F" + keyCode));
    }
}