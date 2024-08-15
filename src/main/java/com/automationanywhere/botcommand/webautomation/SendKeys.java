package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.botcommand.utils.SendKeysProcessor;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.CredentialAllowPassword;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SelectModes;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

@BotCommand
@CommandPkg(label = "Send Key Strokes", name = "sendkeys",
        description = "Send Keys to an Element",
        node_label = " to {{search}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Set",
        text_color = "#2F4F4F", background_color = "#2F4F4F")


public class SendKeys {

    @Execute
    public static void action(
            @Idx(index = "1", type = AttributeType.SESSION) @Pkg(label = "Browser Automation session", description =
                    "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value =
                    "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,

            @Idx(index = "2", type = AttributeType.TEXTAREA) @Pkg(label = "Search", description = "Should match the " +
                    "type", default_value_type = DataType.STRING)
            @NotEmpty String search,

            @Idx(index = "3", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "3.1", pkg = @Pkg(label = "Search by Element XPath", value =
                            BrowserUtils.XPATH)),
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
            @Pkg(label = "Timeout (Seconds)", description = "No wait if 0", default_value_type = DataType.NUMBER,
                    default_value = "0")
            @NotEmpty Number timeout,

            @Idx(index = "6", type = AttributeType.TEXT)
            @Pkg(label = "Wait for Attribute Value", default_value_type = DataType.STRING, default_value = "className")
            @NotEmpty String attribute,

            @Idx(index = "7", type = AttributeType.BOOLEAN)
            @Pkg(label = "Perform click before send keys", default_value_type = DataType.BOOLEAN, default_value =
                    "true")
            @NotEmpty Boolean performClick

    ) {

        String keyString = "";
        try {
            if (session.isClosed()) {
                throw new BotCommandException("Valid browser automation session not found");
            }

            if (inputtype.equals("KEYS")) {
                keyString = keys;
            } else {
                keyString = keyscredential.getInsecureString();
            }
            keyString = keyString.replace("\r\n", "[ENTER]").replace("\n", "[ENTER]");

            WebDriver driver = session.getDriver();
            WebElement element = BrowserUtils.waitForElementWithAttribute(driver, search, type, attribute,
                    timeout.intValue());
            if (element == null) {
                throw new BotCommandException("Element did not load within timeout: Search by " + type + ", selector:" +
                        " " + search + ", attribute: " + attribute);
            }

            Actions action = new Actions(driver);
            SendKeysProcessor.processInputString(keyString, action);
            if (performClick) {
                element.click();
            }
            action.sendKeys(element, "").perform();
        } catch (Exception e) {
            throw new BotCommandException("Send keys failed " + search + " " + type + " : " + e.getMessage());
        }
    }
}