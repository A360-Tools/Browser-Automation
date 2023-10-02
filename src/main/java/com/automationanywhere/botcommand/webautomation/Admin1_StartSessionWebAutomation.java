package com.automationanywhere.botcommand.webautomation;

import com.automationanywhere.botcommand.data.impl.SessionValue;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.commandsdk.model.ReturnSettingsType;

import static com.automationanywhere.commandsdk.model.DataType.SESSION;
import static com.automationanywhere.commandsdk.model.DataType.STRING;


@BotCommand
@CommandPkg(label = "Start Session", name = "StartSessionWebAutomation", description = "Start New Session",
        comment = true, group_label = "Session", text_color = "#2F4F4F", background_color = "#2F4F4F",
        icon = "pkg.svg", node_label = "{{returnTo}}",
        return_label = "Browser Automation session",
        return_settings = {ReturnSettingsType.SESSION_TARGET},
        return_type = SESSION,
        return_name = "Default",
        return_required = true)
public class Admin1_StartSessionWebAutomation {
    @Execute
    public SessionValue start(
            @Idx(index = "1", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "1.1", pkg = @Pkg(label = "Chrome", value = "Chrome")),
                    @Idx.Option(index = "1.2", pkg = @Pkg(label = "Edge", value = "Edge")),
            })
            @Pkg(label = "Browser", default_value = "Chrome", default_value_type = STRING)
            @NotEmpty String browser,
            @Idx(index = "2", type = AttributeType.BOOLEAN) @Pkg(label = "Headless", default_value_type = DataType.BOOLEAN, default_value = "false") @NotEmpty Boolean headless,
            @Idx(index = "3", type = AttributeType.TEXT) @Pkg(label = "User Profile path", default_value_type = DataType.FILE) String profilepath,
            @Idx(index = "4", type = AttributeType.NUMBER) @Pkg(label = "Existing Remote Session Port", default_value_type = DataType.NUMBER) Number port,
            @Idx(index = "5", type = AttributeType.TEXTAREA) @Pkg(label = "Function Library", default_value_type = DataType.STRING) String library,
            @Idx(index = "6", type = AttributeType.FILE) @Pkg(label = "WebDriver path", default_value_type = DataType.FILE) String driverpath
    ) throws Exception {

        profilepath = (profilepath == null) ? "" : profilepath;
        BrowserConnection connection = new BrowserConnection(profilepath, browser, headless, (port == null) ? null : port.intValue(), library, driverpath);

        return SessionValue
                .builder()
                .withSessionObject(connection)
                .build();
    }

}