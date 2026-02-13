package com.automationanywhere.botcommand.webautomation;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.FileExtension;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SelectModes;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@BotCommand
@CommandPkg(label = "Take Screenshot", name = "takescreenshot",
    description = "Take a screenshot of the page or a specific element",
    node_label = "to {{filePath}} for session {{session}}", icon = "pkg.svg", comment = true,
    group_label = "Page", text_color = "#2F4F4F", background_color = "#2F4F4F")
public class TakeScreenshot {

  @Execute
  public static void action(
      @Idx(index = "1", type = AttributeType.SESSION)
      @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
          default_value_type = DataType.SESSION, default_value = "Default")
      @NotEmpty
      @SessionObject
      BrowserConnection session,

      @Idx(index = "2", type = AttributeType.FILE)
      @Pkg(label = "File Path", description = "Full path where screenshot should be saved (including .png extension)",
          default_value_type = DataType.FILE)
      @FileExtension("png")
      @NotEmpty String filePath,

      @Idx(index = "3", type = AttributeType.SELECT, options = {
          @Idx.Option(index = "3.1", pkg = @Pkg(label = "Page", value = "PAGE")),
          @Idx.Option(index = "3.2", pkg = @Pkg(label = "Element", value = "ELEMENT"))
      })
      @Pkg(label = "Screenshot Type", default_value = "PAGE", default_value_type = DataType.STRING)
      @SelectModes
      @NotEmpty String screenshotType,

      @Idx(index = "3.2.1", type = AttributeType.TEXTAREA)
      @Pkg(label = "Selector value", description = "xpath, css, or id etc. based on search type",
          default_value_type = DataType.STRING)
      @NotEmpty String search,

      @Idx(index = "3.2.2", type = AttributeType.SELECT, options = {
          @Idx.Option(index = "3.2.2.1", pkg = @Pkg(label = "Search by Element XPath", value = BrowserUtils.XPATH)),
          @Idx.Option(index = "3.2.2.2", pkg = @Pkg(label = "Search by Element Id", value = BrowserUtils.ID)),
          @Idx.Option(index = "3.2.2.3", pkg = @Pkg(label = "Search by Tag name", value = BrowserUtils.TAG)),
          @Idx.Option(index = "3.2.2.4", pkg = @Pkg(label = "Search by CSS Selector", value = BrowserUtils.CSS)),
          @Idx.Option(index = "3.2.2.5", pkg = @Pkg(label = "JavaScript", value = BrowserUtils.JS))})
      @Pkg(label = "Search Type", default_value = BrowserUtils.CSS, default_value_type = DataType.STRING)
      @NotEmpty String type,

      @Idx(index = "3.2.3", type = AttributeType.NUMBER)
      @Pkg(label = "Timeout (Seconds)", description = "No wait if 0", default_value_type = DataType.NUMBER,
          default_value = "0")
      @NotEmpty Number timeout,

      @Idx(index = "3.2.4", type = AttributeType.TEXT)
      @Pkg(label = "Wait for Attribute Value", default_value_type = DataType.STRING, default_value = "className")
      @NotEmpty String attribute
  ) {
    try {
      if (session.isClosed()) {
        throw new BotCommandException("Valid browser automation session not found");
      }

      WebDriver driver = session.getDriver();
      byte[] screenshotBytes;

      switch (screenshotType) {
        case "PAGE":
          screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
          break;
        case "ELEMENT":
          WebElement element = BrowserUtils.waitForElementWithAttribute(driver, search, type,
              attribute, timeout.intValue());
          if (element == null) {
            throw new BotCommandException(
                "Element did not load within timeout: Search by " + type + ", selector: "
                    + search + ", attribute: " + attribute);
          }
          screenshotBytes = element.getScreenshotAs(OutputType.BYTES);
          break;
        default:
          throw new BotCommandException("Invalid screenshot type: " + screenshotType);
      }

      // Ensure the file path ends with .png
      String normalizedPath = filePath;
      if (!normalizedPath.toLowerCase().endsWith(".png")) {
        normalizedPath += ".png";
      }

      // Create parent directories if they don't exist
      File file = new File(normalizedPath);
      File parentDir = file.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        if (!parentDir.mkdirs()) {
          throw new BotCommandException(
              "Failed to create parent directories for: " + normalizedPath);
        }
      }

      try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(screenshotBytes);
        fos.flush();
      }

    } catch (IOException e) {
      throw new BotCommandException("Failed to save screenshot file: " + e.getMessage());
    } catch (Exception e) {
      throw new BotCommandException("Take screenshot failed: " + e.getMessage(), e);
    }
  }
}
