package com.automationanywhere.botcommand.webautomation;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.GreaterThanEqualTo;
import com.automationanywhere.commandsdk.annotations.rules.LessThanEqualTo;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SelectModes;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.print.PageMargin;
import org.openqa.selenium.print.PageSize;
import org.openqa.selenium.print.PrintOptions;

@BotCommand
@CommandPkg(label = "Save Page to PDF", name = "savetopdf",
    description = "Save current page as PDF with customizable options",
    node_label = "to {{filePath}} for session {{session}}", icon = "pkg.svg", comment = true,
    group_label = "Page", text_color = "#2F4F4F", background_color = "#2F4F4F")
public class SaveToPDF {

  @Execute
  public static void action(
      @Idx(index = "1", type = AttributeType.SESSION)
      @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
          default_value_type = DataType.SESSION, default_value = "Default")
      @NotEmpty
      @SessionObject
      BrowserConnection session,

      @Idx(index = "2", type = AttributeType.FILE)
      @Pkg(label = "File Path", description = "Full path where PDF should be saved (including .pdf extension)",
          default_value_type = DataType.FILE)
      @NotEmpty String filePath,

      @Idx(index = "3", type = AttributeType.RADIO, options = {
          @Idx.Option(index = "3.1", pkg = @Pkg(label = "Portrait", value = "PORTRAIT")),
          @Idx.Option(index = "3.2", pkg = @Pkg(label = "Landscape", value = "LANDSCAPE"))
      })
      @Pkg(label = "Orientation", default_value = "PORTRAIT", default_value_type = DataType.STRING)
      @NotEmpty String orientation,

      @Idx(index = "4", type = AttributeType.TEXT)
      @Pkg(label = "Page Ranges", description = "e.g., '1-3,5,7-9' or leave empty for all pages",
          default_value_type = DataType.STRING)
      String pageRanges,

      @Idx(index = "5", type = AttributeType.RADIO, options = {
          @Idx.Option(index = "5.1", pkg = @Pkg(label = "A4 (21.0 x 29.7 cm)", value = "A4")),
          @Idx.Option(index = "5.2", pkg = @Pkg(label = "Letter (21.6 x 27.9 cm)", value = "LETTER")),
          @Idx.Option(index = "5.3", pkg = @Pkg(label = "Legal (21.6 x 35.6 cm)", value = "LEGAL")),
          @Idx.Option(index = "5.4", pkg = @Pkg(label = "Custom", value = "CUSTOM"))
      })
      @Pkg(label = "Page Size", default_value = "A4", default_value_type = DataType.STRING)
      @SelectModes
      @NotEmpty String pageSize,

      @Idx(index = "5.4.1", type = AttributeType.NUMBER)
      @Pkg(label = "Custom Width (cm)", default_value_type = DataType.NUMBER, default_value = "21.0")
      Number customWidth,

      @Idx(index = "5.4.2", type = AttributeType.NUMBER)
      @Pkg(label = "Custom Height (cm)", default_value_type = DataType.NUMBER, default_value = "29.7")
      Number customHeight,

      @Idx(index = "6", type = AttributeType.RADIO, options = {
          @Idx.Option(index = "6.1", pkg = @Pkg(label = "No Margins (0.0 cm)", value = "NONE")),
          @Idx.Option(index = "6.2", pkg = @Pkg(label = "Small (0.5 cm)", value = "SMALL")),
          @Idx.Option(index = "6.3", pkg = @Pkg(label = "Standard (1.0 cm)", value = "STANDARD")),
          @Idx.Option(index = "6.4", pkg = @Pkg(label = "Large (2.0 cm)", value = "LARGE")),
          @Idx.Option(index = "6.5", pkg = @Pkg(label = "Custom", value = "CUSTOM"))
      })
      @Pkg(label = "Margin Settings", default_value = "STANDARD", default_value_type = DataType.STRING)
      @SelectModes
      @NotEmpty String marginSettings,

      @Idx(index = "6.5.1", type = AttributeType.NUMBER)
      @Pkg(label = "Custom Top Margin (cm)", default_value_type = DataType.NUMBER, default_value = "1.0")
      Number customTopMargin,

      @Idx(index = "6.5.2", type = AttributeType.NUMBER)
      @Pkg(label = "Custom Bottom Margin (cm)", default_value_type = DataType.NUMBER, default_value = "1.0")
      Number customBottomMargin,

      @Idx(index = "6.5.3", type = AttributeType.NUMBER)
      @Pkg(label = "Custom Left Margin (cm)", default_value_type = DataType.NUMBER, default_value = "1.0")
      Number customLeftMargin,

      @Idx(index = "6.5.4", type = AttributeType.NUMBER)
      @Pkg(label = "Custom Right Margin (cm)", default_value_type = DataType.NUMBER, default_value = "1.0")
      Number customRightMargin,

      @Idx(index = "7", type = AttributeType.NUMBER)
      @Pkg(label = "Scale", description = "Scale factor (0.1 to 2.0, where 1.0 = 100%)",
          default_value_type = DataType.NUMBER, default_value = "1.0")
      @GreaterThanEqualTo("0.1")
      @LessThanEqualTo("2.0")
      @NotEmpty Number scale,

      @Idx(index = "8", type = AttributeType.BOOLEAN)
      @Pkg(label = "Print Background", description = "Include background colors and images",
          default_value_type = DataType.BOOLEAN, default_value = "true")
      @NotEmpty Boolean printBackground,

      @Idx(index = "9", type = AttributeType.BOOLEAN)
      @Pkg(label = "Shrink to Fit", description = "Shrink content to fit page width",
          default_value_type = DataType.BOOLEAN, default_value = "false")
      @NotEmpty Boolean shrinkToFit
  ) {
    try {
      if (session.isClosed()) {
        throw new BotCommandException("Valid browser automation session not found");
      }

      WebDriver driver = session.getDriver();

      // Check if driver supports printing
      if (!(driver instanceof PrintsPage)) {
        throw new BotCommandException("Current browser driver does not support PDF printing");
      }

      PrintsPage printer = (PrintsPage) driver;
      PrintOptions printOptions = new PrintOptions();

      // Set orientation
      if ("LANDSCAPE".equals(orientation)) {
        printOptions.setOrientation(PrintOptions.Orientation.LANDSCAPE);
      } else {
        printOptions.setOrientation(PrintOptions.Orientation.PORTRAIT);
      }

      // Set page ranges if specified
      if (pageRanges != null && !pageRanges.trim().isEmpty()) {
        printOptions.setPageRanges(pageRanges.trim());
      }

      // Set page size
      PageSize pageSizeObj;
      switch (pageSize) {
        case "A4":
          pageSizeObj = new PageSize(21.0, 29.7);
          break;
        case "LETTER":
          pageSizeObj = new PageSize(21.6, 27.9);
          break;
        case "LEGAL":
          pageSizeObj = new PageSize(21.6, 35.6);
          break;
        case "CUSTOM":
          pageSizeObj = new PageSize(customWidth.doubleValue(), customHeight.doubleValue());
          break;
        default:
          pageSizeObj = new PageSize(21.0, 29.7); // Default to A4
          break;
      }
      printOptions.setPageSize(pageSizeObj);

      // Set margins based on selected setting
      PageMargin margins;
      switch (marginSettings) {
        case "NONE":
          margins = new PageMargin(0.0, 0.0, 0.0, 0.0);
          break;
        case "SMALL":
          margins = new PageMargin(0.5, 0.5, 0.5, 0.5);
          break;
        case "STANDARD":
          margins = new PageMargin(1.0, 1.0, 1.0, 1.0);
          break;
        case "LARGE":
          margins = new PageMargin(2.0, 2.0, 2.0, 2.0);
          break;
        case "CUSTOM":
          margins = new PageMargin(
              customTopMargin.doubleValue(),
              customBottomMargin.doubleValue(),
              customLeftMargin.doubleValue(),
              customRightMargin.doubleValue()
          );
          break;
        default:
          margins = new PageMargin(1.0, 1.0, 1.0, 1.0); // Default to standard
          break;
      }
      printOptions.setPageMargin(margins);

      // Set scale (validate range)
      double scaleValue = scale.doubleValue();
      if (scaleValue < 0.1 || scaleValue > 2.0) {
        throw new BotCommandException("Scale must be between 0.1 and 2.0");
      }
      printOptions.setScale(scaleValue);

      // Set background printing
      printOptions.setBackground(printBackground);

      // Set shrink to fit
      printOptions.setShrinkToFit(shrinkToFit);

      // Generate PDF
      Pdf pdf = printer.print(printOptions);

      // Get PDF content as Base64 string and decode it to bytes
      String base64Content = pdf.getContent();
      byte[] pdfContent = Base64.getDecoder().decode(base64Content);

      // Ensure the file path ends with .pdf
      String normalizedPath = filePath;
      if (!normalizedPath.toLowerCase().endsWith(".pdf")) {
        normalizedPath += ".pdf";
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

      // Save PDF to file using FileOutputStream
      try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(pdfContent);
        fos.flush();
      }

    } catch (IOException e) {
      throw new BotCommandException("Failed to save PDF file: " + e.getMessage());
    } catch (Exception e) {
      throw new BotCommandException("Save to PDF failed: " + e.getMessage());
    }
  }
}