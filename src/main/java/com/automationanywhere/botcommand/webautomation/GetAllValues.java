package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
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

import java.util.LinkedHashMap;
import java.util.List;

import static com.automationanywhere.commandsdk.model.DataType.STRING;


@BotCommand
@CommandPkg(label = "Get Values", name = "getvalueselement",
        description = "Get Values of an element list",
        node_label = "and assign to {{returnTo}} for session {{session}}", icon = "pkg.svg", comment = true, group_label = "Get", text_color = "#2F4F4F", background_color = "#2F4F4F",
        return_type = DataType.DICTIONARY, return_sub_type = STRING, return_description = "key = <search>", return_label = "Values", return_required = true)


public class GetAllValues {


    @Execute
    public DictionaryValue action(
            @Idx(index = "1", type = AttributeType.SESSION) @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session", default_value_type = DataType.SESSION, default_value = "Default")
            @NotEmpty
            @SessionObject
            BrowserConnection session,
            @Idx(index = "2", type = AttributeType.LIST) @Pkg(label = "Searches", description = "Should match the type", default_value_type = STRING) @NotEmpty List<StringValue> searchList,
            @Idx(index = "3", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "3.1", pkg = @Pkg(label = "Search by Element XPath", value = BrowserUtils.XPATH)),
                    @Idx.Option(index = "3.2", pkg = @Pkg(label = "Search by Element Id", value = BrowserUtils.ID)),
                    @Idx.Option(index = "3.3", pkg = @Pkg(label = "Search by Tag name", value = BrowserUtils.TAG)),
                    @Idx.Option(index = "3.4", pkg = @Pkg(label = "Search by CSS Selector", value = BrowserUtils.CSS)),
                    @Idx.Option(index = "3.5", pkg = @Pkg(label = "JavaScript", value = BrowserUtils.JS))})
            @Pkg(label = "Search Type", default_value = BrowserUtils.CSS, default_value_type = STRING) @NotEmpty String type
    ) throws Exception {
        LinkedHashMap<String, Value> values = new LinkedHashMap<>();
        try {
            if (session.isClosed())
                throw new BotCommandException("Valid browser automation session not found");

            WebDriver driver = session.getDriver();
            BrowserUtils utils = new BrowserUtils();
            String library = session.getLibrary();

            for (StringValue stringValue : searchList) {
                String search = stringValue.get();
                values.put(search, new StringValue(utils.getValue(driver, search, type, library, 0, "")));
            }

        } catch (Exception e) {
            throw new BotCommandException("GETVALUES : " + e.getMessage());
        }

        DictionaryValue dictValue = new DictionaryValue();
        dictValue.set(values);
        return dictValue;


    }

}