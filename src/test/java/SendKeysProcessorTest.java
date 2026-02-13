import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.automationanywhere.botcommand.utils.SendKeysProcessor;
import java.lang.reflect.Field;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SendKeysProcessorTest {

  private Actions actions;

  @BeforeMethod
  public void setUp() throws Exception {
    actions = mock(Actions.class, Mockito.RETURNS_SELF);

    // Reset static fields via reflection
    Field capsLockField = SendKeysProcessor.class.getDeclaredField("capsLockPressed");
    capsLockField.setAccessible(true);
    capsLockField.set(null, false);

    Field shiftField = SendKeysProcessor.class.getDeclaredField("shiftPressed");
    shiftField.setAccessible(true);
    shiftField.set(null, false);
  }

  @Test
  public void testPlainText() {
    SendKeysProcessor.processInputString("hello", actions);
    InOrder order = inOrder(actions);
    order.verify(actions).sendKeys("h");
    order.verify(actions).sendKeys("e");
    order.verify(actions).sendKeys("l");
    order.verify(actions).sendKeys("l");
    order.verify(actions).sendKeys("o");
  }

  @Test
  public void testSpecialKeyEnter() {
    SendKeysProcessor.processInputString("[ENTER]", actions);
    verify(actions).sendKeys(Keys.ENTER);
  }

  @Test
  public void testSpecialKeyTab() {
    SendKeysProcessor.processInputString("[TAB]", actions);
    verify(actions).sendKeys(Keys.TAB);
  }

  @Test
  public void testMixedTextAndSpecialKeys() {
    SendKeysProcessor.processInputString("hello[ENTER]world", actions);
    InOrder order = inOrder(actions);
    order.verify(actions).sendKeys("h");
    order.verify(actions).sendKeys("e");
    order.verify(actions).sendKeys("l");
    order.verify(actions).sendKeys("l");
    order.verify(actions).sendKeys("o");
    order.verify(actions).sendKeys(Keys.ENTER);
    order.verify(actions).sendKeys("w");
    order.verify(actions).sendKeys("o");
    order.verify(actions).sendKeys("r");
    order.verify(actions).sendKeys("l");
    order.verify(actions).sendKeys("d");
  }

  @Test
  public void testShiftModifier() {
    SendKeysProcessor.processInputString("[SHIFT DOWN]abc[SHIFT UP]", actions);
    InOrder order = inOrder(actions);
    order.verify(actions).keyDown(Keys.SHIFT);
    order.verify(actions).sendKeys("a");
    order.verify(actions).sendKeys("b");
    order.verify(actions).sendKeys("c");
    order.verify(actions).keyUp(Keys.SHIFT);
  }

  @Test
  public void testCtrlModifier() {
    SendKeysProcessor.processInputString("[CTRL DOWN]a[CTRL UP]", actions);
    InOrder order = inOrder(actions);
    order.verify(actions).keyDown(Keys.CONTROL);
    order.verify(actions).sendKeys("a");
    order.verify(actions).keyUp(Keys.CONTROL);
  }

  @Test
  public void testCapsLock() {
    SendKeysProcessor.processInputString("[CAPS-LOCK]abc[CAPS-LOCK]def", actions);
    InOrder order = inOrder(actions);
    // capsLock ON: letters get keyDown(SHIFT)...keyUp(SHIFT)
    order.verify(actions).keyDown(Keys.SHIFT);
    order.verify(actions).sendKeys("a");
    order.verify(actions).keyUp(Keys.SHIFT);
    order.verify(actions).keyDown(Keys.SHIFT);
    order.verify(actions).sendKeys("b");
    order.verify(actions).keyUp(Keys.SHIFT);
    order.verify(actions).keyDown(Keys.SHIFT);
    order.verify(actions).sendKeys("c");
    order.verify(actions).keyUp(Keys.SHIFT);
    // capsLock OFF: normal send
    order.verify(actions).sendKeys("d");
    order.verify(actions).sendKeys("e");
    order.verify(actions).sendKeys("f");
  }

  @Test
  public void testCapsLockWithShift() {
    // When both capsLock and shift are active, letters should NOT be shifted (inverse)
    SendKeysProcessor.processInputString("[CAPS-LOCK][SHIFT DOWN]abc[SHIFT UP]", actions);
    InOrder order = inOrder(actions);
    // capsLock toggled ON, then shift down
    order.verify(actions).keyDown(Keys.SHIFT);
    // With capsLock+shift: keyUp(SHIFT), send char, keyDown(SHIFT)
    order.verify(actions).keyUp(Keys.SHIFT);
    order.verify(actions).sendKeys("a");
    order.verify(actions).keyDown(Keys.SHIFT);
    order.verify(actions).keyUp(Keys.SHIFT);
    order.verify(actions).sendKeys("b");
    order.verify(actions).keyDown(Keys.SHIFT);
    order.verify(actions).keyUp(Keys.SHIFT);
    order.verify(actions).sendKeys("c");
    order.verify(actions).keyDown(Keys.SHIFT);
    // shift up
    order.verify(actions).keyUp(Keys.SHIFT);
  }

  @Test
  public void testFunctionKeys() {
    SendKeysProcessor.processInputString("[F1][F5][F12]", actions);
    InOrder order = inOrder(actions);
    order.verify(actions).sendKeys(Keys.F1);
    order.verify(actions).sendKeys(Keys.F5);
    order.verify(actions).sendKeys(Keys.F12);
  }

  @Test
  public void testBracketNotSpecialKey() {
    // [notakey] is not a recognized special key, should be treated as plain text
    SendKeysProcessor.processInputString("[notakey]", actions);
    InOrder order = inOrder(actions);
    order.verify(actions).sendKeys("[");
    order.verify(actions).sendKeys("n");
    order.verify(actions).sendKeys("o");
    order.verify(actions).sendKeys("t");
    order.verify(actions).sendKeys("a");
    order.verify(actions).sendKeys("k");
    order.verify(actions).sendKeys("e");
    order.verify(actions).sendKeys("y");
    order.verify(actions).sendKeys("]");
  }

  @Test
  public void testUnmatchedBracket() {
    SendKeysProcessor.processInputString("hello[world", actions);
    InOrder order = inOrder(actions);
    order.verify(actions).sendKeys("h");
    order.verify(actions).sendKeys("e");
    order.verify(actions).sendKeys("l");
    order.verify(actions).sendKeys("l");
    order.verify(actions).sendKeys("o");
    order.verify(actions).sendKeys("[");
    order.verify(actions).sendKeys("w");
    order.verify(actions).sendKeys("o");
    order.verify(actions).sendKeys("r");
    order.verify(actions).sendKeys("l");
    order.verify(actions).sendKeys("d");
  }

  @Test
  public void testMultipleModifiers() {
    SendKeysProcessor.processInputString("[CTRL DOWN][SHIFT DOWN]a[SHIFT UP][CTRL UP]", actions);
    InOrder order = inOrder(actions);
    order.verify(actions).keyDown(Keys.CONTROL);
    order.verify(actions).keyDown(Keys.SHIFT);
    order.verify(actions).sendKeys("a");
    order.verify(actions).keyUp(Keys.SHIFT);
    order.verify(actions).keyUp(Keys.CONTROL);
  }

  @Test
  public void testNavigationKeys() {
    SendKeysProcessor.processInputString("[LEFT ARROW][PAGE UP][HOME][END]", actions);
    InOrder order = inOrder(actions);
    order.verify(actions).sendKeys(Keys.ARROW_LEFT);
    order.verify(actions).sendKeys(Keys.PAGE_UP);
    order.verify(actions).sendKeys(Keys.HOME);
    order.verify(actions).sendKeys(Keys.END);
  }

  @Test
  public void testEmptyInput() {
    SendKeysProcessor.processInputString("", actions);
    verifyNoInteractions(actions);
  }

  @Test
  public void testDollarKey() {
    SendKeysProcessor.processInputString("[DOLLAR]", actions);
    verify(actions).sendKeys("$");
  }
}
