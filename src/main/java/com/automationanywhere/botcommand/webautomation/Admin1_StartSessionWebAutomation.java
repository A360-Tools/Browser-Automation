package com.automationanywhere.botcommand.webautomation;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.SessionValue;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.ListType;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.commandsdk.model.ReturnSettingsType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.automationanywhere.botcommand.utils.BrowserConnection.CHROME;
import static com.automationanywhere.botcommand.utils.BrowserConnection.EDGE;


@BotCommand
@CommandPkg(label = "Start Session", name = "StartSessionWebAutomation", description = "Start New Session",
        comment = true, group_label = "Session", text_color = "#2F4F4F", background_color = "#2F4F4F",
        icon = "pkg.svg", node_label = "{{returnTo}}",
        return_label = "Browser Automation session",
        return_settings = {ReturnSettingsType.SESSION_TARGET},
        return_type = DataType.SESSION,
        return_name = "Default",
        documentation_url = "https://github.com/A360-Tools/Browser-Automation",
        return_required = true)
public class Admin1_StartSessionWebAutomation {
    @Execute
    public SessionValue start(
            @Idx(index = "1", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "1.1", pkg = @Pkg(label = "Chrome", value = CHROME)),
                    @Idx.Option(index = "1.2", pkg = @Pkg(label = "Edge", value = EDGE)),
            })
            @Pkg(label = "Browser", default_value = CHROME, default_value_type = DataType.STRING)
            @NotEmpty String browser,

            @Idx(index = "2", type = AttributeType.BOOLEAN)
            @Pkg(label = "Headless", default_value_type = DataType.BOOLEAN, default_value = "false")
            @NotEmpty Boolean headless,

            @Idx(index = "3", type = AttributeType.TEXT)
            @Pkg(label = "User Profile path", default_value_type = DataType.FILE)
            String profilepath,

            @Idx(index = "4", type = AttributeType.TEXTAREA)
            @Pkg(label = "Function Library", default_value_type = DataType.STRING)
            String library,

            @Idx(index = "5", type = AttributeType.FILE)
            @Pkg(label = "WebDriver path", default_value_type = DataType.FILE)
            String driverpath,

            @Idx(index = "6", type = AttributeType.LIST)
            @Pkg(label = "List of launch option arguments", description = "Eg. Following are added by default " +
                    "--disable-gpu , --ignore-certificate-errors, --disable-blink-features=AutomationControlled")
            @ListType(DataType.STRING)
            List<Value> arguments

    ) {

        List<String> stringArguments = Optional.ofNullable(arguments)
                .orElse(Collections.emptyList())
                .stream()
                .map(Value::get)
                .map(Object::toString)
                .collect(Collectors.toList());
        headless = Optional.ofNullable(headless).orElse(Boolean.FALSE);
        BrowserConnection connection = new BrowserConnection(browser,profilepath, headless, library, driverpath,
                stringArguments);

        return SessionValue.builder()
                .withSessionObject(connection)
                .build();
    }

}