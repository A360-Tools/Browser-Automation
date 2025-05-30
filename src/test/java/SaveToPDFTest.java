import static com.automationanywhere.botcommand.utils.BrowserConnection.CHROME;

import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.webautomation.Admin1_StartSessionWebAutomation;
import com.automationanywhere.botcommand.webautomation.Admin2_EndSessionWebAutomation;
import com.automationanywhere.botcommand.webautomation.OpenBrowser;
import com.automationanywhere.botcommand.webautomation.SaveToPDF;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Updated test class for PDF saving functionality with new margin settings
 *
 * @author Sumit Kumar
 */
public class SaveToPDFTest {

  private static BrowserConnection browserConnection;
  private static String testOutputDir;

  @BeforeClass
  public static void setUp() throws Exception {
    Admin1_StartSessionWebAutomation session = new Admin1_StartSessionWebAutomation();
    testOutputDir = "src/test/target/test-artifacts/pdf-updated";
    Files.createDirectories(Path.of(testOutputDir));

    String localAppDataPath = System.getenv("LOCALAPPDATA");
    String testProfilePath =
        Paths.get(localAppDataPath, "Google", "Chrome", "User Data", "Test Profile").toString();

    // Initialize browserConnection only once
    browserConnection = (BrowserConnection) session.start(
        CHROME,
        false,
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
  public void testSavePageToPDFBasic() throws Exception {
    // Navigate to a test page
    OpenBrowser.action(browserConnection, "https://www.example.com", "maximized", null, null);

    String pdfPath = testOutputDir + File.separator + "basic_test.pdf";

    // Save with default settings using new margin structure
    SaveToPDF.action(
        browserConnection,
        pdfPath,
        "PORTRAIT",      // orientation
        null,            // pageRanges (all pages)
        "A4",            // pageSize
        null,            // customWidth
        null,            // customHeight
        "STANDARD",      // marginSettings (1.0cm all sides)
        null,            // customTopMargin
        null,            // customBottomMargin
        null,            // customLeftMargin
        null,            // customRightMargin
        1.0,             // scale
        true,            // printBackground
        false            // shrinkToFit
    );

    // Verify PDF was created
    File pdfFile = new File(pdfPath);
    Assert.assertTrue(pdfFile.exists(), "PDF file should be created");
    Assert.assertTrue(pdfFile.length() > 0, "PDF file should not be empty");
  }

  @Test
  public void testSavePageToPDFLandscape() throws Exception {
    // Navigate to a content-rich page
    OpenBrowser.action(browserConnection, "https://www.wikipedia.org", "maximized", null, null);

    String pdfPath = testOutputDir + File.separator + "landscape_test.pdf";

    // Save in landscape mode with small margins
    SaveToPDF.action(
        browserConnection,
        pdfPath,
        "LANDSCAPE",     // orientation
        null,            // pageRanges
        "LETTER",        // pageSize
        null,            // customWidth
        null,            // customHeight
        "SMALL",         // marginSettings (0.5cm all sides)
        null,            // customTopMargin
        null,            // customBottomMargin
        null,            // customLeftMargin
        null,            // customRightMargin
        0.8,             // scale (80%)
        true,            // printBackground
        true             // shrinkToFit
    );

    // Verify PDF was created
    File pdfFile = new File(pdfPath);
    Assert.assertTrue(pdfFile.exists(), "Landscape PDF file should be created");
    Assert.assertTrue(pdfFile.length() > 0, "Landscape PDF file should not be empty");
  }

  @Test
  public void testSavePageToPDFCustomSizeAndMargins() throws Exception {
    // Navigate to a test page
    OpenBrowser.action(browserConnection, "https://www.google.com", "maximized", null, null);

    String pdfPath = testOutputDir + File.separator + "custom_size_margins_test.pdf";

    // Save with custom page size and custom margins
    SaveToPDF.action(
        browserConnection,
        pdfPath,
        "PORTRAIT",      // orientation
        null,            // pageRanges
        "CUSTOM",        // pageSize
        15.0,            // customWidth (15cm)
        20.0,            // customHeight (20cm)
        "CUSTOM",        // marginSettings
        2.5,             // customTopMargin (2.5cm)
        1.5,             // customBottomMargin (1.5cm)
        1.0,             // customLeftMargin (1.0cm)
        3.0,             // customRightMargin (3.0cm)
        1.2,             // scale (120%)
        false,           // printBackground
        false            // shrinkToFit
    );

    // Verify PDF was created
    File pdfFile = new File(pdfPath);
    Assert.assertTrue(pdfFile.exists(), "Custom size and margins PDF file should be created");
    Assert.assertTrue(pdfFile.length() > 0, "Custom size and margins PDF file should not be empty");
  }

  @Test
  public void testSavePageToPDFWithPageRanges() throws Exception {
    // Navigate to a multi-page content
    OpenBrowser.action(browserConnection, "https://www.selenium.dev/documentation/", "maximized",
        null, null);

    String pdfPath = testOutputDir + File.separator + "page_ranges_test.pdf";

    // Save specific page ranges with large margins
    SaveToPDF.action(
        browserConnection,
        pdfPath,
        "PORTRAIT",      // orientation
        "1-2",           // pageRanges (first 2 pages only)
        "A4",            // pageSize
        null,            // customWidth
        null,            // customHeight
        "LARGE",         // marginSettings (2.0cm all sides)
        null,            // customTopMargin
        null,            // customBottomMargin
        null,            // customLeftMargin
        null,            // customRightMargin
        0.9,             // scale (90%)
        true,            // printBackground
        true             // shrinkToFit
    );

    // Verify PDF was created
    File pdfFile = new File(pdfPath);
    Assert.assertTrue(pdfFile.exists(), "Page ranges PDF file should be created");
    Assert.assertTrue(pdfFile.length() > 0, "Page ranges PDF file should not be empty");
  }

  @Test
  public void testSavePageToPDFNoMargins() throws Exception {
    // Test the no margins option
    OpenBrowser.action(browserConnection, "https://www.example.com", "maximized", null, null);

    String pdfPath = testOutputDir + File.separator + "no_margins_test.pdf";

    SaveToPDF.action(
        browserConnection,
        pdfPath,
        "PORTRAIT",
        null,
        "A4",
        null, null,
        "NONE",          // No margins (0.0cm all sides)
        null, null, null, null,
        1.0,
        true,
        false
    );

    // Verify PDF was created
    File pdfFile = new File(pdfPath);
    Assert.assertTrue(pdfFile.exists(), "No margins PDF file should be created");
    Assert.assertTrue(pdfFile.length() > 0, "No margins PDF file should not be empty");
  }

  @Test
  public void testSavePageToPDFAutoExtension() throws Exception {
    // Test that .pdf extension is automatically added if missing
    OpenBrowser.action(browserConnection, "https://www.example.com", "maximized", null, null);

    String pdfPathWithoutExtension = testOutputDir + File.separator + "auto_extension_test";
    String expectedPdfPath = pdfPathWithoutExtension + ".pdf";

    SaveToPDF.action(
        browserConnection,
        pdfPathWithoutExtension, // No .pdf extension
        "PORTRAIT",
        null,
        "A4",
        null, null,
        "STANDARD",      // marginSettings
        null, null, null, null,
        1.0,
        true,
        false
    );

    // Verify PDF was created with .pdf extension
    File pdfFile = new File(expectedPdfPath);
    Assert.assertTrue(pdfFile.exists(), "PDF file should be created with .pdf extension");
  }

  @Test
  public void testSavePageToPDFDirectoryCreation() throws Exception {
    // Test that parent directories are created if they don't exist
    OpenBrowser.action(browserConnection, "https://www.example.com", "maximized", null, null);

    String nestedPath =
        testOutputDir + File.separator + "nested" + File.separator + "directory" + File.separator
            + "test.pdf";

    SaveToPDF.action(
        browserConnection,
        nestedPath,
        "PORTRAIT",
        null,
        "A4",
        null, null,
        "STANDARD",      // marginSettings
        null, null, null, null,
        1.0,
        true,
        false
    );

    // Verify PDF was created and directories were created
    File pdfFile = new File(nestedPath);
    Assert.assertTrue(pdfFile.exists(), "PDF file should be created in nested directory");
    Assert.assertTrue(pdfFile.getParentFile().exists(), "Parent directories should be created");
  }

  @Test
  public void testAllMarginPresets() throws Exception {
    // Test all margin presets on the same page
    OpenBrowser.action(browserConnection, "https://www.google.com", "maximized", null, null);

    String[] marginTypes = {"NONE", "SMALL", "STANDARD", "LARGE"};
    String[] marginLabels = {"none", "small", "standard", "large"};

    for (int i = 0; i < marginTypes.length; i++) {
      String pdfPath = testOutputDir + File.separator + "preset_" + marginLabels[i] + "_margin.pdf";

      SaveToPDF.action(
          browserConnection,
          pdfPath,
          "PORTRAIT",
          null,
          "A4",
          null, null,
          marginTypes[i],  // Different margin preset
          null, null, null, null,
          1.0,
          true,
          false
      );

      File pdfFile = new File(pdfPath);
      Assert.assertTrue(pdfFile.exists(), marginLabels[i] + " margin preset PDF should be created");
      System.out.println(
          marginLabels[i].toUpperCase() + " margins PDF: " + pdfFile.length() + " bytes");
    }

    System.out.println("All margin presets tested successfully");
  }

  @Test
  public void testProfessionalReportFormat() throws Exception {
    // Test a realistic professional report scenario
    OpenBrowser.action(browserConnection,
        "https://www.sec.gov/edgar/searchedgar/companysearch.html", "maximized", null, null);

    String pdfPath = testOutputDir + File.separator + "professional_report.pdf";

    SaveToPDF.action(
        browserConnection,
        pdfPath,
        "PORTRAIT",      // Professional orientation
        "1-3",           // First 3 pages
        "A4",            // Standard business size
        null, null,
        "LARGE",         // Professional margins
        null, null, null, null,
        1.0,             // Full scale
        true,            // Include backgrounds
        false            // Don't shrink to fit
    );

    File pdfFile = new File(pdfPath);
    Assert.assertTrue(pdfFile.exists(), "Professional report PDF should be created");
    Assert.assertTrue(pdfFile.length() > 0, "Professional report PDF should not be empty");
    System.out.println("Professional report PDF created: " + pdfFile.length() + " bytes");
  }

  @Test
  public void testCompactFormFormat() throws Exception {
    // Test a compact form format
    OpenBrowser.action(browserConnection, "https://www.w3schools.com/html/html_forms.asp",
        "maximized", null, null);

    String pdfPath = testOutputDir + File.separator + "compact_form.pdf";

    SaveToPDF.action(
        browserConnection,
        pdfPath,
        "PORTRAIT",
        null,
        "LETTER",        // US form standard
        null, null,
        "SMALL",         // Compact margins
        null, null, null, null,
        1.0,
        true,
        true             // Shrink to fit
    );

    File pdfFile = new File(pdfPath);
    Assert.assertTrue(pdfFile.exists(), "Compact form PDF should be created");
    Assert.assertTrue(pdfFile.length() > 0, "Compact form PDF should not be empty");
    System.out.println("Compact form PDF created: " + pdfFile.length() + " bytes");
  }
}