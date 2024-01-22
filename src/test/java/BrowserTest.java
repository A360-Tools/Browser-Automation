import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.webautomation.OpenBrowser;
import com.automationanywhere.botcommand.webautomation.OpenNewTab;
import com.automationanywhere.botcommand.webautomation.SelectWindow;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BrowserTest {

    private static BrowserConnection session;
    private static WebDriver driver;

    @BeforeTest
    public void setup() throws Exception {
        session = new BrowserConnection("", "Chrome", Boolean.FALSE, null, "", null);
        driver = session.getDriver();
    }

    @Test
    public void testOpenPageMaximized() throws Exception {
        OpenBrowser openBrowser = new OpenBrowser();
        openBrowser.action(session, "https://example.com", "maximized", null, null);
        assertEquals(driver.getTitle(), "Example Domain");
    }

    @Test
    public void testChangeWindowByTitle() throws Exception {
        OpenBrowser openBrowser = new OpenBrowser();
        SelectWindow selectWindow = new SelectWindow();
        OpenNewTab openNewTab = new OpenNewTab();
        openBrowser.action(session, "https://www.google.com", "maximized", null, null);
        openNewTab.action(session);
        openBrowser.action(session, "https://www.yahoo.com", "maximized", null, null);
        openNewTab.action(session);
        openBrowser.action(session, "https://www.reddit.com", "maximized", null, null);
        selectWindow.action(session, "byTitle", "Yahoo");
        assertTrue(driver.getTitle().contains("Yahoo"));
    }
}
