package com.automationanywhere.botcommand.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

/**
 * @author Sumit Kumar
 */
public class SendKeysProcessor {

  private static boolean capsLockPressed = false;
  private static boolean shiftPressed = false;
  private static final Map<String, Consumer<Actions>> specialKeys = initializeSpecialKeys();

  public static void processInputString(String input, Actions action) {
    List<String> segments = parseInputString(input);

    for (String segment : segments) {
      if (specialKeys.containsKey(segment.toUpperCase())) {
        Consumer<Actions> actionConsumer = specialKeys.get(segment.toUpperCase());
        if (actionConsumer != null) {
          actionConsumer.accept(action);
        }
      } else {
        for (char c : segment.toCharArray()) {
          if (capsLockPressed && Character.isLetter(c)) {
            if (!shiftPressed) {
              action.keyDown(Keys.SHIFT).sendKeys(String.valueOf(c)).keyUp(Keys.SHIFT);
            } else {
              action.keyUp(Keys.SHIFT).sendKeys(String.valueOf(c)).keyDown(Keys.SHIFT);
            }
          } else {
            action.sendKeys(String.valueOf(c));
          }
        }
      }
    }
  }

  private static List<String> parseInputString(String input) {
    List<String> segments = new ArrayList<>();
    StringBuilder currentSegment = new StringBuilder();
    int i = 0;

    while (i < input.length()) {
      char c = input.charAt(i);

      if (c == '[') {
        // Check if the bracket could be starting a special key
        int endIndex = input.indexOf(']', i + 1);
        if (endIndex != -1) {
          String potentialSpecialKey = input.substring(i, endIndex + 1);
          if (specialKeys.containsKey(potentialSpecialKey.toUpperCase())) {
            // save characters scanned till now as separate segment
            if (currentSegment.length() > 0) {
              segments.add(currentSegment.toString());
              currentSegment.setLength(0);
            }
            segments.add(potentialSpecialKey);
            // Add the special key as a segment and move scanning to next character after key
            i = endIndex + 1;
            continue;
          }
        }
      }

      currentSegment.append(c);
      i++;
    }

    if (currentSegment.length() > 0) {
      segments.add(currentSegment.toString());
    }

    return segments;
  }

  private static Map<String, Consumer<Actions>> initializeSpecialKeys() {
    Map<String, Consumer<Actions>> keys = new HashMap<>();
    keys.put("[CTRL DOWN]", (a) -> a.keyDown(Keys.CONTROL));
    keys.put("[CTRL UP]", (a) -> a.keyUp(Keys.CONTROL));
    keys.put("[SHIFT DOWN]", (a) -> {
      a.keyDown(Keys.SHIFT);
      shiftPressed = true;
    });
    keys.put("[SHIFT UP]", (a) -> {
      a.keyUp(Keys.SHIFT);
      shiftPressed = false;
    });
    keys.put("[ALT DOWN]", (a) -> a.keyDown(Keys.ALT));
    keys.put("[ALT UP]", (a) -> a.keyUp(Keys.ALT));
    keys.put("[ALT-GR DOWN]", (a) -> a.keyDown(Keys.CONTROL).keyDown(Keys.ALT));
    keys.put("[ALT-GR UP]", (a) -> a.keyUp(Keys.ALT).keyUp(Keys.CONTROL));
    keys.put("[ENTER]", (a) -> a.sendKeys(Keys.ENTER));
    keys.put("[RETURN]", (a) -> a.sendKeys(Keys.ENTER));
    keys.put("[NUM-ENTER]", (a) -> a.sendKeys(Keys.ENTER));
    keys.put("[BACKSPACE]", (a) -> a.sendKeys(Keys.BACK_SPACE));
    keys.put("[TAB]", (a) -> a.sendKeys(Keys.TAB));
    keys.put("[ESCAPE]", (a) -> a.sendKeys(Keys.ESCAPE));
    keys.put("[ESC]", (a) -> a.sendKeys(Keys.ESCAPE));
    keys.put("[PAGE UP]", (a) -> a.sendKeys(Keys.PAGE_UP));
    keys.put("[PAGE DOWN]", (a) -> a.sendKeys(Keys.PAGE_DOWN));
    keys.put("[HOME]", (a) -> a.sendKeys(Keys.HOME));
    keys.put("[LEFT ARROW]", (a) -> a.sendKeys(Keys.ARROW_LEFT));
    keys.put("[UP ARROW]", (a) -> a.sendKeys(Keys.ARROW_UP));
    keys.put("[RIGHT ARROW]", (a) -> a.sendKeys(Keys.ARROW_RIGHT));
    keys.put("[DOWN ARROW]", (a) -> a.sendKeys(Keys.ARROW_DOWN));
    keys.put("[DELETE]", (a) -> a.sendKeys(Keys.DELETE));
    keys.put("[INSERT]", (a) -> a.sendKeys(Keys.INSERT));
    keys.put("[DOLLAR]", (a) -> a.sendKeys("$"));
    keys.put("[PAUSE]", (a) -> a.sendKeys(Keys.PAUSE));
    keys.put("[END]", (a) -> a.sendKeys(Keys.END));
    keys.put("[CAPS-LOCK]", (a) -> capsLockPressed = !capsLockPressed);
    // Handle function keys F1 to F12 dynamically
    for (int i = 1; i <= 12; i++) {
      int currentKeyIndex = i;
      keys.put("[F" + i + "]", (a) -> a.sendKeys(Keys.valueOf("F" + currentKeyIndex)));
    }
    return keys;
  }
}
