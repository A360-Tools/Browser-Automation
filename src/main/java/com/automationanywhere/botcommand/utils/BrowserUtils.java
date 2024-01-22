package com.automationanywhere.botcommand.utils;


import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.google.common.io.Files;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;


public class BrowserUtils {


    public static final String CSS = "CSS";
    public static final String XPATH = "XPATH";
    public static final String TAG = "TAG";
    public static final String ID = "ID";
    public static final String JS = "JS";

    public static final String Click = "Click";
    public static final String DoubleClick = "DoubleClick";
    public static final String RightClick = "RightClick";


    public BrowserUtils() {

    }


    public String getPageContent(WebDriver driver) {

        return driver.getPageSource();

    }

    public void openURL(WebDriver driver, String url) {

        driver.get(url);

    }

    public void navigateForward(WebDriver driver) {

        driver.navigate().forward();

    }

    public void navigateBack(WebDriver driver) {

        driver.navigate().back();

    }


    public void refreshPage(WebDriver driver) {

        driver.navigate().refresh();

    }

    public void doClick(WebDriver driver, String search, String type, String clickType, String library, Integer timeout, String attribute) {
        if (type.equals(BrowserUtils.JS)) {
            String jspath = getJSScript(search, type);
            Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript(library + "\r\n" + jspath + ".click()");
        } else {
            WebElement element = findElement(driver, search, type);
            if (element != null) {
                String jspath = getJSScript(search, type);
                Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
                Actions actions = new Actions(driver);
                switch (clickType) {
                    case Click:
                        actions.click(element).perform();
                        break;
                    case DoubleClick:
                        actions.doubleClick(element).perform();
                        break;
                    case RightClick:
                        actions.contextClick(element).perform();
                        break;

                }

            }

        }

    }


    public void doSubmit(WebDriver driver, String search, String type, String library, Integer timeout, String attribute) {
        if (type.equals(BrowserUtils.JS)) {
            String jspath = getJSScript(search, type);
            Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript(library + "\r\n" + jspath + ".submit()");
        } else {
            WebElement element = findElement(driver, search, type);
            if (element != null) {
                String jspath = getJSScript(search, type);
                Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
                element.submit();
            }
        }
    }


    public void doCheck(WebDriver driver, String search, String type, String library, Integer timeout, String attribute) {

        String jspath = getJSScript(search, type);

        Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        jsExecutor.executeScript(library + "\r\n" + jspath + ".checked = true");

    }

    public void doUnCheck(WebDriver driver, String search, String type, String library, Integer timeout, String attribute) {

        String jspath = getJSScript(search, type);

        Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        jsExecutor.executeScript(library + "\r\n" + jspath + ".checked = false");
    }

    public void doSelect(WebDriver driver, String search, String type, String library, String newvalue, Integer timeout, String attribute) {
        String jspath = getJSScript(search, type);
        Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String script = library + "\r\n function internalsetSelected(el, newvalue) {\r\n" +
                "    for (var i = 0; i < el.options.length; ++i) {\r\n" +
                "        if (el.options[i].text === newvalue)\r\n" +
                "            el.options[i].selected = true;\r\n" +
                "    }\r\n" +
                "}\r\n" +
                "newvalue = \"" + newvalue + "\";el =" + jspath + ";internalsetSelected(el	,newvalue);";
        jsExecutor.executeScript(script);
    }

    public void scrollIntoView(WebDriver driver, String search, String type, String library) {
        String jspath = getJSScript(search, type);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(library + "\r\n element = " + jspath + ";element.scrollIntoView(true);");

    }

    public String getValue(WebDriver driver, String search, String type, String library, Integer timeout, String attribute) {
        String jspath = getJSScript(search, type);
        Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String value = "";
        try {
            value = jsExecutor.executeScript(library + "\r\n var value = " + jspath + ".value;return value").toString();
        } catch (Exception e) {
            value = "";
        }

        return value;

    }


    public String getText(WebDriver driver, String search, String type, String library, Integer timeout, String attribute) {

        String jspath = getJSScript(search, type);
        Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String value = "";
        try {
            value = jsExecutor.executeScript(library + "\r\n var value = " + jspath + ".innerText;return value").toString();
        } catch (Exception e) {
            value = "";
        }

        return value;

    }


    public String doFocus(WebDriver driver, String search, String type, String library, Integer timeout, String attribute) {

        String jspath = getJSScript(search, type);
        Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String value = "";
        try {
            value = jsExecutor.executeScript(library + "\r\n" + jspath + ".focus();").toString();
        } catch (Exception e) {
            value = "";
        }

        return value;

    }


    public Boolean isPageLoaded(WebDriver driver) {
        String jsQuery = "function pageLoaded() "
                + "{var loadingStatus=(document.readyState=='complete');"
                + "return loadingStatus;};"
                + "return pageLoaded()";

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (Boolean) jsExecutor.executeScript(jsQuery);
    }

    public void resizeCurrentWindow(WebDriver driver, Dimension dimension) {
        driver.manage().window().setSize(dimension);
    }

    public void maximizeCurrentWindow(WebDriver driver) {
        driver.manage().window().maximize();
    }

    public void minimizeCurrentWindow(WebDriver driver) {
        driver.manage().window().minimize();
    }

    public void fullScreenCurrentWindow(WebDriver driver) {
        driver.manage().window().fullscreen();
    }

    public Boolean waitUntilPageLoaded(WebDriver driver, Integer seconds) throws InterruptedException {
        Boolean isLoaded = false;

        for (int i = 0; i < seconds; i++) {
            isLoaded = isPageLoaded(driver);
            if (isLoaded) {
                break;
            }
            Thread.sleep(1000);
        }

        return isLoaded;
    }


    public Boolean waitUntilElementLoaded(WebDriver driver, String search, String type, String library, Integer seconds, String attribute) {
        String jspath = getJSScript(search, type);
        return waitforElement(driver, jspath, library, seconds, attribute);
    }


    public void setValue(WebDriver driver, String search, String type, String library, String newvalue, Integer timeout, String attribute) {

        String jspath = getJSScript(search, type);

        Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(library + "\r\n" + jspath + ".value=\"" + newvalue + "\"");
    }

    public String executeJS(WebDriver driver, String js) {

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String value = jsExecutor.executeScript(js).toString();
        return (value != null) ? value : "";

    }


    public String getCurrentURL(WebDriver driver) {

        return driver.getCurrentUrl();
    }

    public String getCurrentSessionID(WebDriver driver, String browser) {
        String id = "";
        if (browser.equals("chrome")) {
            id = ((ChromeDriver) driver).getSessionId().toString();
        }
        if (browser.equals("edge")) {
            id = ((EdgeDriver) driver).getSessionId().toString();
        }


        return id;

    }

    private Boolean waitforElement(WebDriver driver, String js, String library, Integer seconds, String attribute) {
        Boolean status = false;
        if (seconds > 0) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds), Duration.ofMillis(200));
                JavascriptExecutor jsexec = (JavascriptExecutor) driver;

                final Object[] out = new Object[1];
                status = wait.until((ExpectedCondition<Boolean>) d -> {
                    try {
                        out[0] = jsexec.executeScript(library + "\r\n return " + js + "." + attribute);
                    } catch (Exception e) {
                        out[0] = null;
                    }
                    return out[0] != null;
                });

            } catch (Exception e) {
                status = false;
            }
        } else {
            status = true;
        }

        return status;
    }


    public void quit(WebDriver driver) {
        driver.quit();
    }


    public void clearInput(WebDriver driver, String search, String type, String library, Integer timeout, String attribute) {
        WebElement element = findElement(driver, search, type);
        if (element != null) {
            String jspath = getJSScript(search, type);
            Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
            element.clear();

        }
    }


    public void draganddrop(WebDriver driver, String search1, String search2, String type, String library, Integer timeout, String attribute) {

        WebElement fromElement = findElement(driver, search1, type);
        WebElement toElement = findElement(driver, search2, type);
        if (fromElement != null && toElement != null) {
            String jspath = getJSScript(search1, type);
            Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
            Actions actions = new Actions(driver);
            actions.dragAndDrop(fromElement, toElement).perform();
        }

    }


    public void clickandHold(WebDriver driver, String search, String type, String library, Integer timeout, String attribute) {

        WebElement element = findElement(driver, search, type);
        if (element != null) {
            String jspath = getJSScript(search, type);
            Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
            Actions actions = new Actions(driver);
            actions.clickAndHold(element).perform();
        }

    }


    public void moveTo(WebDriver driver, String search, String type, String library, Integer timeout, String attribute) {

        WebElement element = findElement(driver, search, type);
        if (element != null) {
            String jspath = getJSScript(search, type);
            Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
        }

    }


    public void release(WebDriver driver) {

        Actions actions = new Actions(driver);
        actions.release().perform();

    }


    public Map<String, String> elementDetails(WebDriver driver, String search, String type, String filename, String library, Integer timeout, String attribute) throws IOException {

        WebElement element = findElement(driver, search, type);
        HashMap<String, String> details = new HashMap<>();
        if (element != null) {
            String jspath = getJSScript(search, type);
            Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
            String elAttribute = element.getTagName();
            if (elAttribute != null) {
                details.put("tag", elAttribute);
            }
            elAttribute = element.getText();
            if (elAttribute != null) {
                details.put("text", elAttribute);
            }

            elAttribute = element.getAttribute("value");
            if (elAttribute != null) {
                details.put("value", elAttribute);
            }

            elAttribute = element.getAttribute("class");
            if (elAttribute != null) {
                details.put("class", elAttribute);
            }

            elAttribute = ((Integer) element.getRect().getX()).toString();
            details.put("topX", elAttribute);

            elAttribute = ((Integer) element.getRect().getY()).toString();
            details.put("topY", elAttribute);
            elAttribute = ((Integer) element.getRect().getHeight()).toString();
            details.put("height", elAttribute);
            elAttribute = ((Integer) element.getRect().getWidth()).toString();
            details.put("width", elAttribute);

            if (!filename.isEmpty()) {
                File screenshot = element.getScreenshotAs(OutputType.FILE);
                if (screenshot != null) {
                    File copied = new File(filename);
                    Files.copy(screenshot, copied);
                }
            }


        }

        return details;

    }

    public void switchToFrame(WebDriver driver, String search, String type, String library, Integer timeout, String attribute) {

        WebElement frameelement = findElement(driver, search, type);
        if (frameelement != null) {
            String jspath = getJSScript(search, type);
            Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
            driver.switchTo().frame(frameelement);
        }
    }


    public void switchToWindow(WebDriver driver, String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    public void switchToWindowWithTitleRegex(WebDriver driver, String titleRegex) {
        String originalWindow = driver.getWindowHandle();
        Pattern pattern = Pattern.compile(titleRegex);
        boolean matchFound = false;

        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (pattern.matcher(driver.getTitle()).find()) {
                matchFound = true;
                break; //
            }
        }

        if (!matchFound) {
            driver.switchTo().window(originalWindow);
            throw new BotCommandException("No window with title matching regex '" + titleRegex + "' found");
        }
    }

    public void newTab(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        ArrayList oldtabs = new ArrayList(driver.getWindowHandles());
        jsExecutor.executeScript("window.open()");
        ArrayList newtabs = new ArrayList(driver.getWindowHandles());
        newtabs.removeAll(oldtabs);
        if (newtabs.size() == 1)
            driver.switchTo().window(newtabs.get(0).toString());
    }

    public void closeCurrentWindow(WebDriver driver) {
        driver.close();
    }

    public void closeWindow(WebDriver driver, String windowHandle) {
        String prevWindowHandle = driver.getWindowHandle();
        driver.switchTo().window(windowHandle).close();
        if (!prevWindowHandle.equals(windowHandle))
            driver.switchTo().window(prevWindowHandle);
    }

    public List<String> getWindowHandles(WebDriver driver) {
        Set<String> handles = driver.getWindowHandles();
        return new ArrayList<>(handles);
    }

    public String getCurrentWindowHandle(WebDriver driver) {
        return driver.getWindowHandle();
    }


    public void toDefault(WebDriver driver) {
        driver.switchTo().defaultContent();

    }

    public void acceptAlert(WebDriver driver) {
        driver.switchTo().alert().accept();
    }

    public void dismissAlert(WebDriver driver) {
        driver.switchTo().alert().dismiss();
    }

    public String getAlert(WebDriver driver) {
        return driver.switchTo().alert().getText();
    }

    public void setAlerts(WebDriver driver, String textToSet) {
        driver.switchTo().alert().sendKeys(textToSet);
    }

    public void sendKeys(WebDriver driver, String search, String type, String text, String library, Integer timeout, String attribute) throws AWTException {

        char[] characters = text.toCharArray();
        boolean capslockpressed = false;

        Actions action = new Actions(driver);
        WebElement element = findElement(driver, search, type);
        Robot robot = new Robot();
        if (element != null) {
            String jspath = getJSScript(search, type);
            Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
            for (int i = 0; i < characters.length; i++) {
                if (characters[i] == '[') {
                    String key = "";
                    boolean endbracket = false;
                    int j = i;
                    while (j < characters.length && !endbracket) {
                        key = text.substring(i, j + 1);
                        if (characters[j] == ']') {
                            endbracket = true;
                            switch (key) {
                                case "[CTRL DOWN]":
                                    action = action.keyDown(Keys.CONTROL);
                                    break;
                                case "[CTRL UP]":
                                    action = action.keyUp(Keys.CONTROL);
                                    break;
                                case "[SHIFT DOWN]":
                                    action = action.keyDown(Keys.SHIFT);
                                    break;
                                case "[SHIFT UP]":
                                    action = action.keyUp(Keys.SHIFT);
                                    break;
                                case "[ALT DOWN]":
                                    action = action.keyDown(Keys.ALT);
                                    break;
                                case "[ALT UP]":
                                    action = action.keyUp(Keys.ALT);
                                    break;
                                case "[ALT-GR DOWN]":
                                    action = action.keyDown(Keys.CONTROL).keyDown(Keys.ALT);
                                    break;
                                case "[ALT-GR UP]":
                                    action = action.keyUp(Keys.ALT).keyUp(Keys.CONTROL);
                                    break;
                                case "[PAGE DOWN]":
                                    action = action.sendKeys(Keys.PAGE_DOWN);
                                    break;
                                case "[PAGE UP]":
                                    action = action.sendKeys(Keys.PAGE_UP);
                                    break;
                                case "[ENTER]":
                                    action = action.sendKeys(Keys.ENTER);
                                    break;
                                case "[BACKSPACE]":
                                    action = action.sendKeys(Keys.BACK_SPACE);
                                    break;
                                case "[CAPS-LOCK]":
                                    if (!capslockpressed) {
                                        action = action.keyDown(Keys.SHIFT);
                                        capslockpressed = true;
                                    } else {
                                        action = action.keyUp(Keys.SHIFT);
                                        capslockpressed = false;
                                    }
                                    break;
                                case "[INSERT]":
                                    action = action.sendKeys(Keys.INSERT);
                                    break;
                                case "[DELETE]":
                                    action = action.sendKeys(Keys.DELETE);
                                    break;
                                case "[HOME]":
                                    action = action.sendKeys(Keys.HOME);
                                    break;
                                case "[PAUSE]":
                                    action = action.sendKeys(Keys.PAUSE);
                                    break;
                                case "[DOLLAR]":
                                    action = action.sendKeys("$");
                                    break;
                                case "[TAB]":
                                    action = action.sendKeys(Keys.TAB);
                                    break;
                                case "[END]":
                                    action = action.sendKeys(Keys.END);
                                    break;
                                case "[LEFT-ARROW]":
                                    action = action.sendKeys(Keys.ARROW_LEFT);
                                    break;
                                case "[RIGHT-ARROW]":
                                    action = action.sendKeys(Keys.ARROW_RIGHT);
                                    break;
                                case "[UP-ARROW]":
                                    action = action.sendKeys(Keys.ARROW_UP);
                                    break;
                                case "[DOWN-ARROW]":
                                    action = action.sendKeys(Keys.ARROW_DOWN);
                                    break;
                                case "[ESC]":
                                    action = action.sendKeys(Keys.ESCAPE);
                                    break;
                                case "[F1]":
                                    robot.keyPress(KeyEvent.VK_F1);
                                    break;
                                case "[F2]":
                                    robot.keyPress(KeyEvent.VK_F2);
                                    break;
                                case "[F3]":
                                    robot.keyPress(KeyEvent.VK_F3);
                                    break;
                                case "[F4]":
                                    robot.keyPress(KeyEvent.VK_F4);
                                    break;
                                case "[F5]":
                                    robot.keyPress(KeyEvent.VK_F5);
                                    break;
                                case "[F6]":
                                    robot.keyPress(KeyEvent.VK_F6);
                                    break;
                                case "[F7]":
                                    robot.keyPress(KeyEvent.VK_F7);
                                    break;
                                case "[F8]":
                                    robot.keyPress(KeyEvent.VK_F8);
                                    break;
                                case "[F9]":
                                    robot.keyPress(KeyEvent.VK_F9);
                                    break;
                                case "[F10]":
                                    robot.keyPress(KeyEvent.VK_F10);
                                    break;
                                case "[F11]":
                                    robot.keyPress(KeyEvent.VK_F11);
                                    break;
                                case "[F12]":
                                    robot.keyPress(KeyEvent.VK_F12);
                                    break;
                                default:
                                    action.sendKeys(key);
                                    break;
                            }
                        }
                        j++;
                    }
                    if (!endbracket) {
                        action.sendKeys(key);
                        i = j;
                    } else {
                        i = j - 1;
                    }


                } else {
                    action.sendKeys(Character.toString(characters[i]));

                }

            }

            action.sendKeys(element, "").perform();

        }

    }


    private WebElement findElement(WebDriver driver, String search, String type) {
        WebElement element = null;
        try {
            switch (type) {
                case ID:
                    element = driver.findElement(By.id(search));
                    break;
                case CSS:
                    element = driver.findElement(By.cssSelector(search));
                    break;
                case XPATH:
                    element = driver.findElement(By.xpath(search));
                    break;
                case TAG:
                    element = driver.findElement(By.tagName(search));
                    break;
            }
        } catch (Exception ignored) {

        }
        return element;

    }


    private String getJSScript(String search, String type) {

        String js = null;
        search = search.replaceAll("\"", "'");
        switch (type) {
            case ID:
                js = "document.getElementById(\"" + search + "\")";
                break;
            case CSS:
                js = "document.querySelector(\"" + search + "\")";
                break;
            case XPATH:
                js = "document.evaluate(\"" + search + "\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";
                break;
            case TAG:
                js = "document.getElementsByTagName(\"" + search + "\")[0]";
                break;
            case JS:
                js = search;
                break;
        }

        return js;

    }

    private String getTextFromElement(Element element, String textType, String extractionType) {
        if (textType.equalsIgnoreCase("WHOLE_TEXT") && extractionType.equalsIgnoreCase("INCLUDE_CHILDREN"))
            return element.wholeText();
        if (textType.equalsIgnoreCase("WHOLE_TEXT") && extractionType.equalsIgnoreCase("ONLY_SELF"))
            return element.wholeOwnText();
        if (textType.equalsIgnoreCase("NORMALIZED_TEXT") && extractionType.equalsIgnoreCase("ONLY_SELF"))
            return element.ownText();
        return element.text();
    }

    private void addMissingColumnValues(List<Row> rowList, int maxColumnCount) {
        for (Row row : rowList) {
            while (row.getValues().size() < maxColumnCount)
                row.getValues().add(new StringValue());
        }
    }

    public TableValue getTable(WebDriver driver, String search, String type, String library, Integer timeout, String attribute, String textType, String extractionType) {
        String jspath = getJSScript(search, type);
        Boolean status = waitforElement(driver, jspath, library, timeout, attribute);
        WebElement table = findElement(driver, search, type);

        String bodyHTML = table.getAttribute("outerHTML");
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

        if (!headers.isEmpty())
            startRow = 1;

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

        while (schemaList.size() < maxColumnCount)
            schemaList.add(new Schema(""));

        Table outputTable = new Table(schemaList, rowList);
        return new TableValue(outputTable);
    }

}
    