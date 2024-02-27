import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.botcommand.webautomation.*;
import com.automationanywhere.core.security.SecureString;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrowserTest {

    private static BrowserConnection browserConnection;

    @BeforeClass
    public static void setUp() throws Exception {
        Admin1_StartSessionWebAutomation session = new Admin1_StartSessionWebAutomation();
        String localAppDataPath = System.getenv("LOCALAPPDATA");
        String testProfilePath =
                Paths.get(localAppDataPath, "Google", "Chrome", "User Data", "Test Profile").toString();
        // Initialize browserConnection only once
        browserConnection = (BrowserConnection) session.start(
                "chrome",
                null,
                testProfilePath,
                null,
                null,
                null,
                Boolean.FALSE
        ).getSession();
    }

    @AfterClass
    public static void tearDown() {
        if (browserConnection != null) {
            Admin2_EndSessionWebAutomation session = new Admin2_EndSessionWebAutomation();
            session.end(browserConnection);
        }
    }


    @Test
    public void testLaunch() {
        OpenBrowser.action(browserConnection, "https://google.com", "maximized", null, null);
        OpenBrowser.action(browserConnection, "https://google.com", "dimensions", 1920, 1080);
    }

    @Test
    public void testAlertsAndClick() {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/javascript_alerts", "maximized",
                null, null);
        DoClick.action(browserConnection, "//button[text()='Click for JS Alert']", BrowserUtils.XPATH, 0, "className"
                , BrowserUtils.MODE_SIMULATE);
        AlertAccept.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH, 0,
                "className", BrowserUtils.MODE_SIMULATE);
        AlertAccept.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH, 0,
                "className", BrowserUtils.MODE_SIMULATE);
        AlertDismiss.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Prompt']", BrowserUtils.XPATH, 0, "className"
                , BrowserUtils.MODE_SIMULATE);
        AlertSetText.action(browserConnection, new SecureString(("admin@procurementanywhere.com").getBytes()));
        AlertAccept.action(browserConnection);
    }

    @Test
    public void getTable() {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/tables", "maximized", null, null);
        Table table = GetTable.action(browserConnection, "table1", BrowserUtils.ID, 0, "className", "NORMALIZED_TEXT",
                "INCLUDE_CHILDREN").get();
        List<Schema> schemaList = table.getSchema();
        List<Row> rows = table.getRows();
        Assert.assertNotNull(schemaList, "Schema list should not be null");
        Assert.assertFalse(schemaList.isEmpty(), "Schema list should not be empty");

        for (Schema schema : schemaList) {
            Assert.assertNotNull(schema.getName(), "Schema name should not be null");
            Assert.assertFalse(schema.getName().isEmpty(), "Schema name should not be empty");
        }

        for (Row row : rows) {
            for (Value value : row.getValues()) {
                Assert.assertNotNull(value, "Value not be null");
            }
        }
    }

    @Test
    public void testAlertsAndClickModeJS() {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/javascript_alerts", "maximized",
                null, null);
        DoClick.action(browserConnection, "//button[text()='Click for JS Alert']", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_JAVASCRIPT);
        AlertAccept.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH, 0,
                "className", BrowserUtils.MODE_JAVASCRIPT);
        AlertAccept.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH, 0,
                "className", BrowserUtils.MODE_JAVASCRIPT);
        AlertDismiss.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Prompt']", BrowserUtils.XPATH, 0, "className"
                , BrowserUtils.MODE_JAVASCRIPT);
        AlertSetText.action(browserConnection, new SecureString(("admin@procurementanywhere.com").getBytes()));
        AlertAccept.action(browserConnection);
    }

    @Test
    public void testCheckboxes() {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/checkboxes", "maximized", null, null);
        DoCheck.action(browserConnection, "(//input[@type='checkbox'])[1]", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_SIMULATE);
        DoUnCheck.action(browserConnection, "(//input[@type='checkbox'])[1]", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_SIMULATE);
        DoCheck.action(browserConnection, "(//input[@type='checkbox'])[2]", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_SIMULATE);
        DoUnCheck.action(browserConnection, "(//input[@type='checkbox'])[2]", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_SIMULATE);
    }

    @Test
    public void testCheckboxesModeJS() {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/checkboxes", "maximized", null, null);
        DoCheck.action(browserConnection, "(//input[@type='checkbox'])[1]", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_JAVASCRIPT);
        DoUnCheck.action(browserConnection, "(//input[@type='checkbox'])[1]", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_JAVASCRIPT);
        DoCheck.action(browserConnection, "(//input[@type='checkbox'])[2]", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_JAVASCRIPT);
        DoUnCheck.action(browserConnection, "(//input[@type='checkbox'])[2]", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_JAVASCRIPT);
    }

    @Test
    public void testMouseHold() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://devicetests.com/mouse-test", "maximized", null, null);
        DoClicknHold.action(browserConnection, "//body", BrowserUtils.XPATH, 0, "className");
        Thread.sleep(4000);
        DoRelease.action(browserConnection);
    }

    @Test
    public void testMouseDoubleClick() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://devicetests.com/mouse-test", "maximized", null, null);
        DoDoubleClick.action(browserConnection, "//body", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_SIMULATE);
        Thread.sleep(4000);
    }

    @Test
    public void testFocus() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/login", "maximized", null, null);
        DoFocus.action(browserConnection, "username", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_SIMULATE);
        Thread.sleep(1000);
        DoFocus.action(browserConnection, "password", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_SIMULATE);
        Thread.sleep(1000);
        DoFocus.action(browserConnection, "//button[@type='submit']", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_SIMULATE);
        Thread.sleep(1000);
    }

    @Test
    public void testFocusModeJS() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/login", "maximized", null, null);
        DoFocus.action(browserConnection, "username", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_JAVASCRIPT);
        Thread.sleep(1000);
        DoFocus.action(browserConnection, "password", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_JAVASCRIPT);
        Thread.sleep(1000);
        DoFocus.action(browserConnection, "//button[@type='submit']", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_JAVASCRIPT);
        Thread.sleep(1000);
    }

    @Test
    public void testRightClick() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/context_menu", "maximized", null,
                null);
        DoRightClick.action(browserConnection, "hot-spot", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_SIMULATE);
        Thread.sleep(1000);
        AlertAccept.action(browserConnection);
    }

    @Test
    public void testRightClickModeJS() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/context_menu", "maximized", null,
                null);
        DoRightClick.action(browserConnection, "hot-spot", BrowserUtils.ID, 0, "className",
                BrowserUtils.MODE_JAVASCRIPT);
        Thread.sleep(1000);
        AlertAccept.action(browserConnection);
    }

    @Test
    public void testSelectDropdown() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/dropdown", "maximized", null, null);
        DoSelect.action(browserConnection, "dropdown", BrowserUtils.ID, "Option 1", 10, "className",
                BrowserUtils.MODE_SIMULATE);
        Thread.sleep(2000);
        DoSelect.action(browserConnection, "dropdown", BrowserUtils.ID, "Option 2", 10, "className",
                BrowserUtils.MODE_SIMULATE);
        Thread.sleep(2000);
    }

    @Test
    public void testSelectDropdownModeJS() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/dropdown", "maximized", null, null);
        DoSelect.action(browserConnection, "dropdown", BrowserUtils.ID, "Option 1", 10, "className",
                BrowserUtils.MODE_JAVASCRIPT);
        Thread.sleep(2000);
        DoSelect.action(browserConnection, "dropdown", BrowserUtils.ID, "Option 2", 10, "className",
                BrowserUtils.MODE_JAVASCRIPT);
        Thread.sleep(2000);
    }

    @Test
    public void submitForm() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/login", "maximized", null, null);
        SetValue.action(browserConnection, "username", BrowserUtils.ID, new SecureString(("tomsmith").getBytes()), 0,
                "className");
        SetValue.action(browserConnection, "password", BrowserUtils.ID,
                new SecureString(("SuperSecretPassword!").getBytes()), 0,
                "className");
        DoSubmit.action(browserConnection, "login", BrowserUtils.ID, 0, "className",
                BrowserUtils.MODE_SIMULATE);
        Thread.sleep(2000);
    }

    @Test
    public void submitFormModeJS() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/login", "maximized", null, null);
        List<StringValue> searchList = new ArrayList<>();
        searchList.add(new StringValue("username"));
        searchList.add(new StringValue("password"));
        List<StringValue> valueList = new ArrayList<>();
        valueList.add(new StringValue("tomsmith"));
        valueList.add(new StringValue("SuperSecretPassword!"));
        SetAllValues.action(browserConnection, searchList, BrowserUtils.ID, valueList);
        DoSubmit.action(browserConnection, "login", BrowserUtils.ID, 0, "className",
                BrowserUtils.MODE_JAVASCRIPT);
        Thread.sleep(2000);
    }

    @Test
    public void dragAndDrop() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/drag_and_drop", "maximized", null,
                null);
        Thread.sleep(2000);
        DragandDrop.action(browserConnection, "column-b", "column-a", BrowserUtils.ID, 0, "className");
        Thread.sleep(2000);
        DragandDrop.action(browserConnection, "column-a", "column-b", BrowserUtils.ID, 0, "className");
        Thread.sleep(2000);
    }

    @Test
    public void testExecuteJs() {
        OpenBrowser.action(browserConnection, "https://www.google.com/", "maximized", null, null);
        String title = ExecuteJS.action(browserConnection, "return document.title;").get();
        Assert.assertEquals("Google", title);
    }

    @Test
    public void testGetURL() {
        OpenBrowser.action(browserConnection, "https://www.google.com/", "maximized", null, null);
        String title = GetCurrentURL.action(browserConnection).get();
        Assert.assertEquals("https://www.google.com/", title);
    }

    @Test
    public void testGetElementDetails() {
        OpenBrowser.action(browserConnection, "https://www.google.com/", "maximized", null, null);
        Map<String, Value> details =
                GetDetails.action(browserConnection, "//textarea[@name='q']", BrowserUtils.XPATH, 0, "className").get();
        Assert.assertEquals(details.get("tag").get(), "textarea");
        System.out.println(details);
    }

    @Test
    public void testGetPageSource() {
        OpenBrowser.action(browserConnection, "https://www.google.com/", "maximized", null, null);
        String source = GetPageSource.action(browserConnection).get();
        Assert.assertNotNull(source);
    }

    @Test
    public void moveTo() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/hovers", "maximized", null, null);
        Thread.sleep(1000);
        MoveTo.action(browserConnection, "(//div[@class='figure'])[1]", BrowserUtils.XPATH, 0, "className");
        Thread.sleep(1000);
        MoveTo.action(browserConnection, "(//div[@class='figure'])[2]", BrowserUtils.XPATH, 0, "className");
        Thread.sleep(1000);
        MoveTo.action(browserConnection, "(//div[@class='figure'])[3]", BrowserUtils.XPATH, 0, "className");
        Thread.sleep(1000);
    }

    @Test
    public void scrollToModeJS() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/infinite_scroll", "maximized", null
                , null);
        Thread.sleep(1000);
        int i = 1;
        while (i < 10) {
            ScrollTo.action(browserConnection, "(//div[@class='jscroll-added'])[" + i + "]", BrowserUtils.XPATH, 0,
                    "className",
                    BrowserUtils.MODE_JAVASCRIPT);
            Thread.sleep(500);
            ++i;
        }
    }

    @Test
    public void sendKeys() throws InterruptedException, IOException, UnsupportedFlavorException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/key_presses", "maximized", null,
                null);
        DoClick.action(browserConnection, "target", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_SIMULATE);
        SendKeys.action(browserConnection, "target", BrowserUtils.ID, "KEYS",
                "[SHIFT DOWN]abcd[SHIFT UP]de [[CAPS-LOCK]fcg[CAPS-LOCK]h[SHIFT DOWN]245[SHIFT UP][CTRL DOWN]ac[CTRL " +
                        "UP]", null, 0,
                "className", Boolean.FALSE);
        //ABCDde [FCGh@$%
        String expected = GetValue.action(browserConnection, "target", BrowserUtils.ID, 0, "className").get();
        Assert.assertEquals(expected, "ABCDde [FCGh@$%");
        // Get the system clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // Get the clipboard contents as a string
        String result = (String) clipboard.getData(DataFlavor.stringFlavor);
        Assert.assertEquals(result, "ABCDde [FCGh@$%");
    }

    @Test
    public void selectFrame() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/iframe", "maximized", null, null);
        SelectFrame.action(browserConnection, "mce_0_ifr", BrowserUtils.ID, 0, "className");
        ClearInput.action(browserConnection, "tinymce", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_SIMULATE);
        Thread.sleep(2000);
        DoClick.action(browserConnection, "tinymce", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_SIMULATE);
        SendKeys.action(browserConnection, "body", BrowserUtils.CSS, "KEYS", "test data to type", null, 0, "className"
                , Boolean.FALSE);
        Thread.sleep(2000);
    }

    @Test
    public void SelectWindowByTitle() {
        OpenBrowser.action(browserConnection, "https://www.google.com", "maximized", null, null);
        OpenNewTab.action(browserConnection);
        OpenBrowser.action(browserConnection, "https://yahoo.com", "maximized", null, null);
        SelectWindow.action(browserConnection, "byTitle", "Google");
    }

    @Test
    public void SelectWindowByHandle() {
        OpenBrowser.action(browserConnection, "https://www.google.com", "maximized", null, null);
        String googleHandle = GetCurrentWindow.action(browserConnection).get();
        OpenNewTab.action(browserConnection);
        OpenBrowser.action(browserConnection, "https://yahoo.com", "maximized", null, null);
        SelectWindow.action(browserConnection, "byHandle", googleHandle);
    }

}
