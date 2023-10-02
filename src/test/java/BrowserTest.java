import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;

public class BrowserTest {

    private WebDriver driver;

    @BeforeTest
    public void setup() throws Exception {
        BrowserConnection connection = new BrowserConnection("", "Chrome", Boolean.FALSE, null, "", "");
        driver = connection.getDriver();
    }

    @Test
    public void launch() throws InterruptedException, AWTException {
        driver.get("https://developer.automationanywhere.com/challenges/financialvalidation-applogin.html");
        BrowserUtils utils = new BrowserUtils();
        utils.doClick(driver, "inputEmail", BrowserUtils.ID, BrowserUtils.Click, "", 0, "className");
        utils.sendKeys(driver, "inputEmail", BrowserUtils.ID, "abcd@email.com", "", 0, "className");
        Thread.sleep(4000);
    }
}
