import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.webautomation.Admin1_StartSessionWebAutomation;
import com.automationanywhere.botcommand.webautomation.OpenBrowser;
import com.automationanywhere.botcommand.webautomation.OpenNewTab;
import com.automationanywhere.botcommand.webautomation.SelectWindow;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BrowserTest {

    private static BrowserConnection browserConnection;
    private static WebDriver driver;

    @BeforeClass
    public void setUp() throws Exception {
        Admin1_StartSessionWebAutomation session = new Admin1_StartSessionWebAutomation();
        browserConnection = (BrowserConnection) session.start(
                "chrome",
                null,
                null,
                null,
                null,
                null
        ).getSession();
        driver = browserConnection.getDriver();
    }

    @Test
    public void testOpenPageMaximized(){
        OpenBrowser.action(browserConnection, "https://example.com", "maximized", null, null);
        assertEquals(driver.getTitle(), "Example Domain");
    }

    @Test
    public void testChangeWindowByTitle(){
        OpenBrowser.action(browserConnection, "https://www.google.com", "maximized", null, null);
        OpenNewTab.action(browserConnection);
        OpenBrowser.action(browserConnection, "https://www.yahoo.com", "maximized", null, null);
        OpenNewTab.action(browserConnection);
        OpenBrowser.action(browserConnection, "https://www.reddit.com", "maximized", null, null);
        SelectWindow.action(browserConnection, "byTitle", "Yahoo");
        assertTrue(driver.getTitle().contains("Yahoo"));
    }
}
