import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.botcommand.webautomation.*;
import com.automationanywhere.core.security.SecureString;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowserTest {

    private static BrowserConnection browserConnection;
    private static String screenshotFolderPath;

    @BeforeClass
    public static void setUp() throws Exception {
        Admin1_StartSessionWebAutomation session = new Admin1_StartSessionWebAutomation();
        screenshotFolderPath = "src/test/target/test-artifacts/screenshot";
        Files.createDirectories(Path.of(screenshotFolderPath));
        String localAppDataPath = System.getenv("LOCALAPPDATA");
        String testProfilePath = Paths.get(localAppDataPath, "Google", "Chrome", "User Data", "Test Profile").toString();

        // Initialize browserConnection only once
        browserConnection = (BrowserConnection) session.start(
                "chrome",
                null,
                testProfilePath,
                null,
                null,
                null
        ).getSession();
    }

    @AfterClass
    public static void tearDown() {
        if (browserConnection != null) {
            Admin2_EndSessionWebAutomation session = new Admin2_EndSessionWebAutomation();
            session.end(browserConnection);
        }
    }

    @NotNull
    private static HashMap<String, String> getStringStringHashMap() {
        HashMap<String, String> stateFullNameMap = new HashMap<>();
        // Adding the key-value pairs into the HashMap
        stateFullNameMap.put("AK", "Tim Langer");
        stateFullNameMap.put("AL", "Grant Brown");
        stateFullNameMap.put("AR", "Johnny Reagan");
        stateFullNameMap.put("AZ", "Matthew Ireton");
        stateFullNameMap.put("CA", "Matthew Ireton");
        stateFullNameMap.put("CO", "Mitch Manders");
        stateFullNameMap.put("CT", "Tim Langer");
        stateFullNameMap.put("DC", "Matthew Ireton");
        stateFullNameMap.put("DE", "Grant Brown");
        stateFullNameMap.put("FL", "Ryan Neises");
        stateFullNameMap.put("GA", "Ryan Neises");
        stateFullNameMap.put("HI", "Matthew Ireton");
        stateFullNameMap.put("IA", "Tim Langer");
        stateFullNameMap.put("ID", "Matthew Ireton");
        stateFullNameMap.put("IL", "Mitch Manders");
        stateFullNameMap.put("IN", "Mitch Manders");
        stateFullNameMap.put("KS", "Tim Langer");
        stateFullNameMap.put("KY", "Mitch Manders");
        stateFullNameMap.put("LA", "Johnny Reagan");
        stateFullNameMap.put("MA", "Grant Brown");
        stateFullNameMap.put("MD", "Grant Brown");
        stateFullNameMap.put("ME", "Tim Langer");
        stateFullNameMap.put("MI", "Mitch Manders");
        stateFullNameMap.put("MN", "Mitch Manders");
        stateFullNameMap.put("MO", "Mitch Manders");
        stateFullNameMap.put("MS", "Johnny Reagan");
        stateFullNameMap.put("MT", "Tim Langer");
        stateFullNameMap.put("NC", "Ryan Neises");
        stateFullNameMap.put("ND", "Tim Langer");
        stateFullNameMap.put("NE", "Mitch Manders");
        stateFullNameMap.put("NH", "Tim Langer");
        stateFullNameMap.put("NJ", "Grant Brown");
        stateFullNameMap.put("NM", "Mitch Manders");
        stateFullNameMap.put("NV", "Matthew Ireton");
        stateFullNameMap.put("NY", "Grant Brown");
        stateFullNameMap.put("OH", "Grant Brown");
        stateFullNameMap.put("OK", "Johnny Reagan");
        stateFullNameMap.put("OR", "Matthew Ireton");
        stateFullNameMap.put("PA", "Grant Brown");
        stateFullNameMap.put("RI", "Grant Brown");
        stateFullNameMap.put("SC", "Ryan Neises");
        stateFullNameMap.put("SD", "Mitch Manders");
        stateFullNameMap.put("TN", "Tim Langer");
        stateFullNameMap.put("TX", "Johnny Reagan");
        stateFullNameMap.put("UT", "Matthew Ireton");
        stateFullNameMap.put("VA", "Tim Langer");
        stateFullNameMap.put("VT", "Grant Brown");
        stateFullNameMap.put("WA", "Matthew Ireton");
        stateFullNameMap.put("WI", "Mitch Manders");
        stateFullNameMap.put("WV", "Tim Langer");
        stateFullNameMap.put("WY", "Mitch Manders");
        return stateFullNameMap;
    }

    @Test
    public void testLaunch() {
        OpenBrowser.action(browserConnection, "https://google.com", "maximized", null, null);
        OpenBrowser.action(browserConnection, "https://google.com", "dimensions", 1920, 1080);
    }

    @Test
    public void testAlertsAndClick() {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/javascript_alerts", "maximized", null, null);
        DoClick.action(browserConnection, "//button[text()='Click for JS Alert']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_SIMULATE);
        AlertAccept.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_SIMULATE);
        AlertAccept.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_SIMULATE);
        AlertDismiss.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Prompt']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_SIMULATE);
        AlertSetText.action(browserConnection, new SecureString(("admin@procurementanywhere.com").getBytes()));
        AlertAccept.action(browserConnection);
    }

    @Test
    public void testAlertsAndClickModeJS() {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/javascript_alerts", "maximized", null, null);
        DoClick.action(browserConnection, "//button[text()='Click for JS Alert']", BrowserUtils.XPATH, 0, "className",
                BrowserUtils.MODE_JAVASCRIPT);
        AlertAccept.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_JAVASCRIPT);
        AlertAccept.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_JAVASCRIPT);
        AlertDismiss.action(browserConnection);
        DoClick.action(browserConnection, "//button[text()='Click for JS Prompt']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_JAVASCRIPT);
        AlertSetText.action(browserConnection, new SecureString(("admin@procurementanywhere.com").getBytes()));
        AlertAccept.action(browserConnection);
    }

    @Test
    public void testCheckboxes() {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/checkboxes", "maximized", null, null);
        DoCheck.action(browserConnection, "(//input[@type='checkbox'])[1]", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_SIMULATE);
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
        DoCheck.action(browserConnection, "(//input[@type='checkbox'])[1]", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_JAVASCRIPT);
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
        DoDoubleClick.action(browserConnection, "//body", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_SIMULATE);
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
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/context_menu", "maximized", null, null);
        DoRightClick.action(browserConnection, "hot-spot", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_SIMULATE);
        Thread.sleep(1000);
        AlertAccept.action(browserConnection);
    }

    @Test
    public void testRightClickModeJS() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/context_menu", "maximized", null, null);
        DoRightClick.action(browserConnection, "hot-spot", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_JAVASCRIPT);
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
        SetValue.action(browserConnection, "password", BrowserUtils.ID, new SecureString(("SuperSecretPassword!").getBytes()), 0,
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
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/drag_and_drop", "maximized", null, null);
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
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/infinite_scroll", "maximized", null, null);
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
    public void selectFrame() throws InterruptedException {
        OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/iframe", "maximized", null, null);
        SelectFrame.action(browserConnection, "mce_0_ifr", BrowserUtils.ID, 0, "className");
        ClearInput.action(browserConnection, "tinymce", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_SIMULATE);
        Thread.sleep(2000);
        DoClick.action(browserConnection, "tinymce", BrowserUtils.ID, 0, "className", BrowserUtils.MODE_SIMULATE);
        SendKeys.action(browserConnection, "body", BrowserUtils.CSS, "KEYS", "test data to type", null, 0, "className");
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

    public void A360SupplyChainChallenge() throws IOException, InterruptedException {
        HashMap<String, String> stateFullNameMap = getStringStringHashMap();

        //https://pathfinder.automationanywhere.com/challenges/automationanywherelabs-supplychainmanagement.html
        String challengeURL = "https://pathfinder.automationanywhere.com/challenges/automationanywherelabs-supplychainmanagement.html";
        String procurementURL = "https://pathfinder.automationanywhere.com/challenges/AutomationAnywhereLabs-POTrackingLogin.html";
        OpenBrowser.action(browserConnection, challengeURL, "maximized", null, null);
        String challengeHandle = GetCurrentWindow.action(browserConnection).get();

        OpenNewTab.action(browserConnection);
        OpenBrowser.action(browserConnection, procurementURL, "maximized", null, null);
        String procurementHandle = GetCurrentWindow.action(browserConnection).get();
        SetValue.action(browserConnection, "inputEmail", BrowserUtils.ID, new SecureString(("admin@procurementanywhere.com").getBytes()), 0, "className");
        SetValue.action(browserConnection, "inputPassword", BrowserUtils.ID, new SecureString(("paypacksh!p").getBytes()), 0, "className");
        DoClick.action(browserConnection, "//button[@type='button']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_SIMULATE);

        int i = 1;
        while (i < 8) {
            SelectWindow.action(browserConnection, "byHandle", challengeHandle);
            String currentPO = GetValue.action(browserConnection, "PONumber" + i, BrowserUtils.ID, 0, "className").get();

            SelectWindow.action(browserConnection, "byHandle", procurementHandle);
            ClearInput.action(browserConnection, "//input[@type='search']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_SIMULATE);
            DoClick.action(browserConnection, "//input[@type='search']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_SIMULATE);
            SendKeys.action(browserConnection, "//input[@type='search']", BrowserUtils.XPATH, "KEYS",
                    currentPO, null, 0, "className");

            String state = GetText.action(browserConnection, "//tbody//td[5]", BrowserUtils.XPATH, 10, "className",
                    BrowserUtils.MODE_SIMULATE).get();
            String date = GetText.action(browserConnection, "//tbody//td[7]", BrowserUtils.XPATH, 0, "className",
                    BrowserUtils.MODE_SIMULATE).get();
            String total = GetText.action(browserConnection, "//tbody//td[8]", BrowserUtils.XPATH, 0, "className",
                    BrowserUtils.MODE_SIMULATE).get().replaceAll("\\$", "").strip();
            String agent = stateFullNameMap.get(state);

            SelectWindow.action(browserConnection, "byHandle", challengeHandle);
            SetValue.action(browserConnection, "shipDate" + i, BrowserUtils.ID, new SecureString(date.getBytes()), 0, "className");
            SetValue.action(browserConnection, "orderTotal" + i, BrowserUtils.ID, new SecureString(total.getBytes()), 0, "className");
            DoSelect.action(browserConnection, "agent" + i, BrowserUtils.ID, agent, 0, "className", BrowserUtils.MODE_SIMULATE);
            i++;
        }
        DoClick.action(browserConnection, "submit_button", BrowserUtils.ID, 0, "className",
                BrowserUtils.MODE_SIMULATE);
        String GUID = GetValue.action(browserConnection, "//input[@title='Unique GUID']", BrowserUtils.XPATH, 10,
                "className").get();
        File screenshotFile = ((TakesScreenshot) browserConnection.getDriver()).getScreenshotAs(OutputType.FILE);
        Path destinationPath = Path.of(screenshotFolderPath, GUID + ".png");
        Files.copy(screenshotFile.toPath(), destinationPath);
    }
}
