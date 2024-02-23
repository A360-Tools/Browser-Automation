import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.botcommand.webautomation.*;
import com.automationanywhere.core.security.SecureString;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author Sumit Kumar
 */
public class A360Challenge {
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
    //pre-requisite: Chrome profile with logged in challenge platform
    public void A360SupplyChainChallenge() throws IOException {
        HashMap<String, String> stateFullNameMap = getStringStringHashMap();

        //https://pathfinder.automationanywhere.com/challenges/automationanywherelabs-supplychainmanagement.html
        String challengeURL = "https://pathfinder.automationanywhere.com/challenges/automationanywherelabs-supplychainmanagement.html";
        String procurementURL = "https://pathfinder.automationanywhere.com/challenges/AutomationAnywhereLabs-POTrackingLogin.html";

        OpenBrowser.action(browserConnection, procurementURL, "maximized", null, null);
        String procurementHandle = GetCurrentWindow.action(browserConnection).get();
        SetValue.action(browserConnection, "inputEmail", BrowserUtils.ID, new SecureString(("admin" +
                "@procurementanywhere.com").getBytes()), 20, "className");
        SetValue.action(browserConnection, "inputPassword", BrowserUtils.ID, new SecureString(("paypacksh!p").getBytes()), 0, "className");
        DoClick.action(browserConnection, "//button[@type='button']", BrowserUtils.XPATH, 0, "className", BrowserUtils.MODE_SIMULATE);

        OpenNewTab.action(browserConnection);
        OpenBrowser.action(browserConnection, challengeURL, "maximized", null, null);
        String challengeHandle = GetCurrentWindow.action(browserConnection).get();

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
