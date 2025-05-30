import static com.automationanywhere.botcommand.utils.BrowserConnection.CHROME;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.botcommand.webautomation.Admin1_StartSessionWebAutomation;
import com.automationanywhere.botcommand.webautomation.Admin2_EndSessionWebAutomation;
import com.automationanywhere.botcommand.webautomation.AlertAccept;
import com.automationanywhere.botcommand.webautomation.AlertDismiss;
import com.automationanywhere.botcommand.webautomation.AlertSetText;
import com.automationanywhere.botcommand.webautomation.ClearInput;
import com.automationanywhere.botcommand.webautomation.CloseWindow;
import com.automationanywhere.botcommand.webautomation.DoCheck;
import com.automationanywhere.botcommand.webautomation.DoClick;
import com.automationanywhere.botcommand.webautomation.DoClicknHold;
import com.automationanywhere.botcommand.webautomation.DoDoubleClick;
import com.automationanywhere.botcommand.webautomation.DoFocus;
import com.automationanywhere.botcommand.webautomation.DoRelease;
import com.automationanywhere.botcommand.webautomation.DoRightClick;
import com.automationanywhere.botcommand.webautomation.DoSelect;
import com.automationanywhere.botcommand.webautomation.DoSubmit;
import com.automationanywhere.botcommand.webautomation.DoUnCheck;
import com.automationanywhere.botcommand.webautomation.DragandDrop;
import com.automationanywhere.botcommand.webautomation.ExecuteJS;
import com.automationanywhere.botcommand.webautomation.GetCurrentURL;
import com.automationanywhere.botcommand.webautomation.GetCurrentWindow;
import com.automationanywhere.botcommand.webautomation.GetDetails;
import com.automationanywhere.botcommand.webautomation.GetPageSource;
import com.automationanywhere.botcommand.webautomation.GetTable;
import com.automationanywhere.botcommand.webautomation.GetValue;
import com.automationanywhere.botcommand.webautomation.MoveTo;
import com.automationanywhere.botcommand.webautomation.OpenBrowser;
import com.automationanywhere.botcommand.webautomation.OpenNewTab;
import com.automationanywhere.botcommand.webautomation.OpenNewWindow;
import com.automationanywhere.botcommand.webautomation.ScrollTo;
import com.automationanywhere.botcommand.webautomation.SelectFrame;
import com.automationanywhere.botcommand.webautomation.SelectWindow;
import com.automationanywhere.botcommand.webautomation.SendKeys;
import com.automationanywhere.botcommand.webautomation.SetAllValues;
import com.automationanywhere.botcommand.webautomation.SetValue;
import com.automationanywhere.core.security.SecureString;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
        CHROME,
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


  @Test
  public void testLaunch() {
    OpenBrowser.action(browserConnection, "https://google.com", "maximized", null, null);
    OpenBrowser.action(browserConnection, "https://google.com", "dimensions", 1920, 1080);
  }

  @Test
  public void testAlertsAndClick() {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/javascript_alerts",
        "maximized",
        null, null);
    DoClick.action(browserConnection, "//button[text()='Click for JS Alert']", BrowserUtils.XPATH,
        0, "className"
        , BrowserUtils.MODE_SIMULATE);
    AlertAccept.action(browserConnection);
    DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH,
        0,
        "className", BrowserUtils.MODE_SIMULATE);
    AlertAccept.action(browserConnection);
    DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH,
        0,
        "className", BrowserUtils.MODE_SIMULATE);
    AlertDismiss.action(browserConnection);
    DoClick.action(browserConnection, "//button[text()='Click for JS Prompt']", BrowserUtils.XPATH,
        0, "className"
        , BrowserUtils.MODE_SIMULATE);
    AlertSetText.action(browserConnection,
        new SecureString(("admin@procurementanywhere.com").getBytes()));
    AlertAccept.action(browserConnection);
  }

  @Test
  public void getTable() {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/tables", "maximized",
        null, null);
    Table table = GetTable.action(browserConnection, "table1", BrowserUtils.ID, 0, "className",
        "NORMALIZED_TEXT",
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
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/javascript_alerts",
        "maximized",
        null, null);
    DoClick.action(browserConnection, "//button[text()='Click for JS Alert']", BrowserUtils.XPATH,
        0, "className",
        BrowserUtils.MODE_JAVASCRIPT);
    AlertAccept.action(browserConnection);
    DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH,
        0,
        "className", BrowserUtils.MODE_JAVASCRIPT);
    AlertAccept.action(browserConnection);
    DoClick.action(browserConnection, "//button[text()='Click for JS Confirm']", BrowserUtils.XPATH,
        0,
        "className", BrowserUtils.MODE_JAVASCRIPT);
    AlertDismiss.action(browserConnection);
    DoClick.action(browserConnection, "//button[text()='Click for JS Prompt']", BrowserUtils.XPATH,
        0, "className"
        , BrowserUtils.MODE_JAVASCRIPT);
    AlertSetText.action(browserConnection,
        new SecureString(("admin@procurementanywhere.com").getBytes()));
    AlertAccept.action(browserConnection);
  }

  @Test
  public void testCheckboxes() {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/checkboxes",
        "maximized", null, null);
    DoCheck.action(browserConnection, "(//input[@type='checkbox'])[1]", BrowserUtils.XPATH, 0,
        "className",
        BrowserUtils.MODE_SIMULATE);
    DoUnCheck.action(browserConnection, "(//input[@type='checkbox'])[1]", BrowserUtils.XPATH, 0,
        "className",
        BrowserUtils.MODE_SIMULATE);
    DoCheck.action(browserConnection, "(//input[@type='checkbox'])[2]", BrowserUtils.XPATH, 0,
        "className",
        BrowserUtils.MODE_SIMULATE);
    DoUnCheck.action(browserConnection, "(//input[@type='checkbox'])[2]", BrowserUtils.XPATH, 0,
        "className",
        BrowserUtils.MODE_SIMULATE);
  }

  @Test
  public void testCheckboxesModeJS() {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/checkboxes",
        "maximized", null, null);
    DoCheck.action(browserConnection, "(//input[@type='checkbox'])[1]", BrowserUtils.XPATH, 0,
        "className",
        BrowserUtils.MODE_JAVASCRIPT);
    DoUnCheck.action(browserConnection, "(//input[@type='checkbox'])[1]", BrowserUtils.XPATH, 0,
        "className",
        BrowserUtils.MODE_JAVASCRIPT);
    DoCheck.action(browserConnection, "(//input[@type='checkbox'])[2]", BrowserUtils.XPATH, 0,
        "className",
        BrowserUtils.MODE_JAVASCRIPT);
    DoUnCheck.action(browserConnection, "(//input[@type='checkbox'])[2]", BrowserUtils.XPATH, 0,
        "className",
        BrowserUtils.MODE_JAVASCRIPT);
  }

  @Test
  public void testMouseHold() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://devicetests.com/mouse-test", "maximized", null,
        null);
    DoClicknHold.action(browserConnection, "//body", BrowserUtils.XPATH, 0, "className");
    Thread.sleep(4000);
    DoRelease.action(browserConnection);
  }

  @Test
  public void testMouseDoubleClick() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://devicetests.com/mouse-test", "maximized", null,
        null);
    DoDoubleClick.action(browserConnection, "//body", BrowserUtils.XPATH, 0, "className",
        BrowserUtils.MODE_SIMULATE);
    Thread.sleep(4000);
  }

  @Test
  public void testFocus() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/login", "maximized",
        null, null);
    DoFocus.action(browserConnection, "username", BrowserUtils.ID, 0, "className",
        BrowserUtils.MODE_SIMULATE);
    Thread.sleep(1000);
    DoFocus.action(browserConnection, "password", BrowserUtils.ID, 0, "className",
        BrowserUtils.MODE_SIMULATE);
    Thread.sleep(1000);
    DoFocus.action(browserConnection, "//button[@type='submit']", BrowserUtils.XPATH, 0,
        "className",
        BrowserUtils.MODE_SIMULATE);
    Thread.sleep(1000);
  }

  @Test
  public void testFocusModeJS() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/login", "maximized",
        null, null);
    DoFocus.action(browserConnection, "username", BrowserUtils.ID, 0, "className",
        BrowserUtils.MODE_JAVASCRIPT);
    Thread.sleep(1000);
    DoFocus.action(browserConnection, "password", BrowserUtils.ID, 0, "className",
        BrowserUtils.MODE_JAVASCRIPT);
    Thread.sleep(1000);
    DoFocus.action(browserConnection, "//button[@type='submit']", BrowserUtils.XPATH, 0,
        "className",
        BrowserUtils.MODE_JAVASCRIPT);
    Thread.sleep(1000);
  }

  @Test
  public void testRightClick() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/context_menu",
        "maximized", null,
        null);
    DoRightClick.action(browserConnection, "hot-spot", BrowserUtils.ID, 0, "className",
        BrowserUtils.MODE_SIMULATE);
    Thread.sleep(1000);
    AlertAccept.action(browserConnection);
  }

  @Test
  public void testRightClickModeJS() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/context_menu",
        "maximized", null,
        null);
    DoRightClick.action(browserConnection, "hot-spot", BrowserUtils.ID, 0, "className",
        BrowserUtils.MODE_JAVASCRIPT);
    Thread.sleep(1000);
    AlertAccept.action(browserConnection);
  }

  @Test
  public void testSelectDropdown() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/dropdown",
        "maximized", null, null);
    DoSelect.action(browserConnection, "dropdown", BrowserUtils.ID, "Option 1", 10, "className",
        BrowserUtils.MODE_SIMULATE);
    Thread.sleep(2000);
    DoSelect.action(browserConnection, "dropdown", BrowserUtils.ID, "Option 2", 10, "className",
        BrowserUtils.MODE_SIMULATE);
    Thread.sleep(2000);
  }

  @Test
  public void testSelectDropdownModeJS() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/dropdown",
        "maximized", null, null);
    DoSelect.action(browserConnection, "dropdown", BrowserUtils.ID, "Option 1", 10, "className",
        BrowserUtils.MODE_JAVASCRIPT);
    Thread.sleep(2000);
    DoSelect.action(browserConnection, "dropdown", BrowserUtils.ID, "Option 2", 10, "className",
        BrowserUtils.MODE_JAVASCRIPT);
    Thread.sleep(2000);
  }

  @Test
  public void submitForm() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/login", "maximized",
        null, null);
    SetValue.action(browserConnection, "username", BrowserUtils.ID,
        new SecureString(("tomsmith").getBytes()), 0,
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
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/login", "maximized",
        null, null);
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
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/drag_and_drop",
        "maximized", null,
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
    int totalOpen = browserConnection.getDriver().getWindowHandles().size();
    for (int i = 0; i < totalOpen; i++) {

      CloseWindow.action(browserConnection);
    }
    OpenBrowser.action(browserConnection, "https://www.google.com/", "maximized", null, null);
    title = GetCurrentURL.action(browserConnection).get();
    Assert.assertEquals("https://www.google.com/", title);
  }

  @Test
  public void testGetElementDetails() {
    OpenBrowser.action(browserConnection, "https://www.google.com/", "maximized", null, null);
    Map<String, Value> details =
        GetDetails.action(browserConnection, "//textarea[@name='q']", BrowserUtils.XPATH, 0,
            "className").get();
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
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/hovers", "maximized",
        null, null);
    Thread.sleep(1000);
    MoveTo.action(browserConnection, "(//div[@class='figure'])[1]", BrowserUtils.XPATH, 0,
        "className");
    Thread.sleep(1000);
    MoveTo.action(browserConnection, "(//div[@class='figure'])[2]", BrowserUtils.XPATH, 0,
        "className");
    Thread.sleep(1000);
    MoveTo.action(browserConnection, "(//div[@class='figure'])[3]", BrowserUtils.XPATH, 0,
        "className");
    Thread.sleep(1000);
  }

  @Test
  public void scrollToModeJS() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/infinite_scroll",
        "maximized", null
        , null);
    Thread.sleep(1000);
    int i = 1;
    while (i < 10) {
      ScrollTo.action(browserConnection, "(//div[@class='jscroll-added'])[" + i + "]",
          BrowserUtils.XPATH, 0,
          "className",
          BrowserUtils.MODE_JAVASCRIPT);
      Thread.sleep(500);
      ++i;
    }
  }

  @Test
  public void sendKeys() throws InterruptedException, IOException, UnsupportedFlavorException {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/key_presses",
        "maximized", null,
        null);
    DoClick.action(browserConnection, "target", BrowserUtils.ID, 0, "className",
        BrowserUtils.MODE_SIMULATE);
    SendKeys.action(browserConnection, "target", BrowserUtils.ID, "KEYS",
        "[SHIFT DOWN]abcd[SHIFT UP]de [[CAPS-LOCK]fcg[CAPS-LOCK]h[SHIFT DOWN]245[SHIFT UP][CTRL DOWN]ac[CTRL "
            +
            "UP]", null, 0,
        "className", Boolean.FALSE);
    //ABCDde [FCGh@$%
    String expected = GetValue.action(browserConnection, "target", BrowserUtils.ID, 0, "className")
        .get();
    Assert.assertEquals(expected, "ABCDde [FCGh@$%");
    // Get the system clipboard
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    // Get the clipboard contents as a string
    String result = (String) clipboard.getData(DataFlavor.stringFlavor);
    Assert.assertEquals(result, "ABCDde [FCGh@$%");
  }

  //site limit reached
//    @Test
  public void selectFrame() throws InterruptedException {
    OpenBrowser.action(browserConnection, "https://the-internet.herokuapp.com/iframe", "maximized",
        null, null);
    SelectFrame.action(browserConnection, "mce_0_ifr", BrowserUtils.ID, 0, "className");
    ClearInput.action(browserConnection, "tinymce", BrowserUtils.ID, 0, "className",
        BrowserUtils.MODE_SIMULATE);
    Thread.sleep(2000);
    DoClick.action(browserConnection, "tinymce", BrowserUtils.ID, 0, "className",
        BrowserUtils.MODE_SIMULATE);
    SendKeys.action(browserConnection, "body", BrowserUtils.CSS, "KEYS", "test data to type", null,
        0, "className"
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

  @Test
  public void testOpenNewWindow() {
    Set<String> initialHandles;
    try {
      initialHandles = browserConnection.getDriver().getWindowHandles();
    } catch (NoSuchSessionException e) {
      initialHandles = new HashSet<>();
    }
    OpenBrowser.action(browserConnection, "https://example.com", "maximized", null, null);

    OpenNewWindow.action(browserConnection);
    Assert.assertEquals(browserConnection.getDriver().getWindowHandles().size(),
        initialHandles.size() + 1, "A new " +
            "window " +
            "should have been opened");
  }

  @Test
  public void testCloseWindow() {
    Set<String> initialHandles;
    try {
      initialHandles = browserConnection.getDriver().getWindowHandles();
    } catch (NoSuchSessionException e) {
      initialHandles = new HashSet<>();
    }
    OpenBrowser.action(browserConnection, "https://example.com", "maximized", null, null);
    // Open two additional windows
    OpenNewWindow.action(browserConnection);
    OpenNewWindow.action(browserConnection);
    Assert.assertEquals(browserConnection.getDriver().getWindowHandles().size(),
        initialHandles.size() + 2, "Three");

    // Close the remaining windows
    int totalOpen = browserConnection.getDriver().getWindowHandles().size();
    for (int i = 0; i < totalOpen; i++) {

      CloseWindow.action(browserConnection);
    }

    try {
      initialHandles = browserConnection.getDriver().getWindowHandles();
    } catch (NoSuchSessionException e) {
      initialHandles = new HashSet<>();
    }
    Assert.assertEquals(initialHandles.size(), 0, "all windows should be closed");
    // Close non existent windows, no error expected as already closed windows
    CloseWindow.action(browserConnection);
    CloseWindow.action(browserConnection);
    //OpenNewWindow should not throw non existent session even after all windows are closed
    //OpenNewWindow should be able to open new window
    OpenNewWindow.action(browserConnection);
    Assert.assertEquals(browserConnection.getDriver().getWindowHandles().size(), 1,
        "single window should be " +
            "present");
    CloseWindow.action(browserConnection);
    try {
      initialHandles = browserConnection.getDriver().getWindowHandles();
    } catch (NoSuchSessionException e) {
      initialHandles = new HashSet<>();
    }
    Assert.assertEquals(initialHandles.size(), 0, "all windows should be closed");
  }

  @Test
  public void testTabAndWindowInteractions() {
    Set<String> initialHandles;
    try {
      initialHandles = browserConnection.getDriver().getWindowHandles();
    } catch (NoSuchSessionException e) {
      initialHandles = new HashSet<>();
    }
    OpenBrowser.action(browserConnection, "https://example.com", "maximized", null, null);
    // Open a new tab and a new window
    OpenNewTab.action(browserConnection);
    OpenNewWindow.action(browserConnection);
    Set<String> handles = browserConnection.getDriver().getWindowHandles();
    Assert.assertEquals(browserConnection.getDriver().getWindowHandles().size(),
        initialHandles.size() + 2, "Three " +
            "browser" +
            " contexts should be " +
            "open");

    // Switch to each context and verify unique content
    for (String handle : handles) {
      browserConnection.getDriver().switchTo().window(handle);
      OpenBrowser.action(browserConnection, "https://example.com", "maximized", null, null);
      WebElement body = browserConnection.getDriver().findElement(By.tagName("body"));
      Assert.assertNotNull(body, "Each context should have loaded content");
    }
  }

}
