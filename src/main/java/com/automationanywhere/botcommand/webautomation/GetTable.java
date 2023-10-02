package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.impl.TableValue;
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
import static com.automationanywhere.commandsdk.model.DataType.TABLE;


@BotCommand
@CommandPkg(label = "Get Table Content", name = "gettablecontent",
        description = "Get Table Content",
        node_label = "and assign to {{returnTo}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Get", text_color = "#2F4F4F", background_color = "#2F4F4F",
        return_type = TABLE, return_label = "Value", return_required = true)


public class GetTable {

    @Execute
    public TableValue action(
            @Idx(index = "1", type = AttributeType.SESSION) @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,
            @Idx(index = "2", type = AttributeType.TEXTAREA) @Pkg(label = "Search", description = "Should match the type", default_value_type = STRING) @NotEmpty String search,
            @Idx(index = "3", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "3.1", pkg = @Pkg(label = "Search by Element XPath", value = BrowserUtils.XPATH)),
                    @Idx.Option(index = "3.2", pkg = @Pkg(label = "Search by Element Id", value = BrowserUtils.ID)),
                    @Idx.Option(index = "3.3", pkg = @Pkg(label = "Search by Tag name", value = BrowserUtils.TAG)),
                    @Idx.Option(index = "3.4", pkg = @Pkg(label = "Search by CSS Selector", value = BrowserUtils.CSS)),
                    @Idx.Option(index = "3.5", pkg = @Pkg(label = "JavaScript", value = BrowserUtils.JS))})
            @Pkg(label = "Search Type", default_value = BrowserUtils.CSS, default_value_type = STRING) @NotEmpty String type,
            @Idx(index = "4", type = AttributeType.NUMBER) @Pkg(label = "Timeout (Seconds)", description = "No wait if 0", default_value_type = DataType.NUMBER, default_value = "0") @NotEmpty Number timeout,
            @Idx(index = "5", type = AttributeType.TEXT) @Pkg(label = "Wait for Attribute Value", default_value_type = STRING, default_value = "className") @NotEmpty String attribute,
            @Idx(index = "6", type = AttributeType.RADIO, options = {
                    @Idx.Option(index = "6.1", pkg = @Pkg(label = "NORMALIZED (Whitespace is normalized and trimmed)", value = "NORMALIZED_TEXT")),
                    @Idx.Option(index = "6.2", pkg = @Pkg(label = "WHOLE TEXT (Whitespace is not normalized and not trimmed)", value = "WHOLE_TEXT")),
            })
            @Pkg(label = "Text formatting options", default_value = "NORMALIZED_TEXT", default_value_type = STRING) @NotEmpty String textType,

            @Idx(index = "7", type = AttributeType.RADIO, options = {
                    @Idx.Option(index = "7.1", pkg = @Pkg(label = "ALL CHILDREN TEXT (All data within table data node)", value = "INCLUDE_CHILDREN")),
                    @Idx.Option(index = "7.2", pkg = @Pkg(label = "OWN TEXT (Only table data node)", value = "ONLY_SELF")),
            })
            @Pkg(label = "Data extraction options", default_value = "INCLUDE_CHILDREN", default_value_type = STRING) @NotEmpty String extractionType
    ) throws Exception {

        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();
            BrowserUtils utils = new BrowserUtils();
            String library = session.getLibrary();
            return utils.getTable(driver, search, type, library, timeout.intValue(), attribute, textType, extractionType);
        } catch (Exception e) {
            throw new BotCommandException("GETTABLE " + search + " " + type + " : " + e.getMessage());
        }

    }

}