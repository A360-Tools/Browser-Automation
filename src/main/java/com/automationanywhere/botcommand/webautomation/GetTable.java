package com.automationanywhere.botcommand.webautomation;


import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.utils.BrowserConnection;
import com.automationanywhere.botcommand.utils.BrowserUtils;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.SessionObject;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


@BotCommand
@CommandPkg(label = "Get Table Content", name = "gettablecontent",
    description = "Get Table Content",
    node_label = "and assign to {{returnTo}} for session {{session}}", icon = "pkg.svg", comment = true,
    group_label = "Get", text_color = "#2F4F4F", background_color = "#2F4F4F",
    return_type = DataType.TABLE, return_label = "Value", return_required = true)

public class GetTable {

  @Execute
  public static TableValue action(
      @Idx(index = "1", type = AttributeType.SESSION)
      @Pkg(label = "Browser Automation session", description = "Set valid Browser Automation session",
          default_value_type = DataType.SESSION, default_value = "Default")
      @NotEmpty
      @SessionObject
      BrowserConnection session,

      @Idx(index = "2", type = AttributeType.TEXTAREA)
      @Pkg(label = "Selector value", description = "xpath,css," +
          " or id etc. based on search type", default_value_type = DataType.STRING)
      @NotEmpty String search,

      @Idx(index = "3", type = AttributeType.SELECT, options = {
          @Idx.Option(index = "3.1", pkg = @Pkg(label = "Search by Element XPath", value =
              BrowserUtils.XPATH)),
          @Idx.Option(index = "3.2", pkg = @Pkg(label = "Search by Element Id", value = BrowserUtils.ID)),
          @Idx.Option(index = "3.3", pkg = @Pkg(label = "Search by Tag name", value = BrowserUtils.TAG)),
          @Idx.Option(index = "3.4", pkg = @Pkg(label = "Search by CSS Selector", value = BrowserUtils.CSS)),
          @Idx.Option(index = "3.5", pkg = @Pkg(label = "JavaScript", value = BrowserUtils.JS))})
      @Pkg(label = "Search Type", default_value = BrowserUtils.CSS, default_value_type = DataType.STRING)
      @NotEmpty String type,

      @Idx(index = "4", type = AttributeType.NUMBER)
      @Pkg(label = "Timeout (Seconds)", description = "No wait if 0", default_value_type = DataType.NUMBER,
          default_value = "0")
      @NotEmpty Number timeout,

      @Idx(index = "5", type = AttributeType.TEXT)
      @Pkg(label = "Wait for Attribute Value", default_value_type = DataType.STRING, default_value = "className")
      @NotEmpty String attribute,

      @Idx(index = "6", type = AttributeType.RADIO, options = {
          @Idx.Option(index = "6.1", pkg = @Pkg(label = "NORMALIZED (Whitespace is normalized and trimmed)"
              , value = "NORMALIZED_TEXT")),
          @Idx.Option(index = "6.2", pkg = @Pkg(label =
              "WHOLE TEXT (Whitespace is not normalized and not " +
                  "trimmed)", value = "WHOLE_TEXT")),
      })
      @Pkg(label = "Text formatting options", default_value = "NORMALIZED_TEXT", default_value_type =
          DataType.STRING)
      @NotEmpty String textType,

      @Idx(index = "7", type = AttributeType.RADIO, options = {
          @Idx.Option(index = "7.1", pkg = @Pkg(label =
              "ALL CHILDREN TEXT (All data within table data " +
                  "node)", value = "INCLUDE_CHILDREN")),
          @Idx.Option(index = "7.2", pkg = @Pkg(label = "OWN TEXT (Only table data node)", value =
              "ONLY_SELF")),
      })
      @Pkg(label = "Data extraction options", default_value = "INCLUDE_CHILDREN", default_value_type =
          DataType.STRING)
      @NotEmpty String extractionType
  ) {

    try {
      if (session.isClosed()) {
        throw new BotCommandException("Valid browser automation session not found");
      }

      WebDriver driver = session.getDriver();
      WebElement element = BrowserUtils.waitForElementWithAttribute(driver, search, type, attribute,
          timeout.intValue());
      if (element == null) {
        throw new BotCommandException(
            "Element did not load within timeout: Search by " + type + ", selector:" +
                " " + search + ", attribute: " + attribute);
      }

      String bodyHTML = element.getAttribute("outerHTML");
      Document doc = Jsoup.parseBodyFragment(bodyHTML);
      Element tableToExtract = doc.select("table").get(0);
      List<Schema> schemaList = new ArrayList<>();
      List<Row> rowList = new ArrayList<>();

      Elements rows = tableToExtract.select("tr");
      Elements headers = rows.get(0).select("th");
      for (Element header : headers) {
        schemaList.add(new Schema(getTextFromElement(header, textType, extractionType)));
      }
      int maxColumnCount = headers.size();
      int startRow = 0;

      if (!headers.isEmpty()) {
        startRow = 1;
      }

      for (int i = startRow; i < rows.size(); i++) {
        Element row = rows.get(i);
        List<Value> rowValues = new ArrayList<>();
        Elements cells = row.select("td");
        for (Element cell : cells) {
          rowValues.add(new StringValue(getTextFromElement(cell, textType, extractionType)));
        }
        rowList.add(new Row(rowValues));
        maxColumnCount = Math.max(rowValues.size(), maxColumnCount);
      }

      addMissingColumnValues(rowList, maxColumnCount);

      while (schemaList.size() < maxColumnCount) {
        schemaList.add(new Schema(""));
      }

      Table outputTable = new Table(schemaList, rowList);
      return new TableValue(outputTable);
    } catch (Exception e) {
      throw new BotCommandException(
          "Get table failed " + search + " " + type + " : " + e.getMessage());
    }

  }

  private static String getTextFromElement(Element element, String textType,
      String extractionType) {
    if (textType.equalsIgnoreCase("WHOLE_TEXT") && extractionType.equalsIgnoreCase(
        "INCLUDE_CHILDREN")) {
      return element.wholeText();
    }
    if (textType.equalsIgnoreCase("WHOLE_TEXT") && extractionType.equalsIgnoreCase("ONLY_SELF")) {
      return element.wholeOwnText();
    }
    if (textType.equalsIgnoreCase("NORMALIZED_TEXT") && extractionType.equalsIgnoreCase(
        "ONLY_SELF")) {
      return element.ownText();
    }
    return element.text();
  }

  private static void addMissingColumnValues(List<Row> rowList, int maxColumnCount) {
    for (Row row : rowList) {
      while (row.getValues().size() < maxColumnCount) {
        row.getValues().add(new StringValue());
      }
    }
  }
}