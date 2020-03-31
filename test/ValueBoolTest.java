import org.junit.Test;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.cell.CyclicalContentVisitor;
import edu.cs3500.spreadsheets.model.cell.IsReferenceVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.IsDblVisitor;
import edu.cs3500.spreadsheets.model.cell.value.NumericValueVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Value;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test class for Bool Value.
 */
public class ValueBoolTest {

  private Value falseB = new Bool(false);
  private Value trueB = new Bool(true);

  @Test
  public void testEvaluate() {
    assertEquals(new Bool(true), trueB.evaluate());
    assertEquals(new Bool(false), falseB.evaluate());
  }

  @Test
  public void testAcceptValueVisitorOne() {
    assertTrue(Math.abs(falseB.accept(new NumericValueVisitor()) - 0.0) < 0.0001);
    assertTrue(Math.abs(trueB.accept(new NumericValueVisitor()) - 0.0) < 0.0001);
  }

  @Test
  public void testAcceptValueVisitorTwo() {
    assertFalse(trueB.accept(new IsDblVisitor()));
    assertFalse(falseB.accept(new IsDblVisitor()));
  }

  @Test
  public void testAcceptContentsVisitorOne() {
    assertFalse(falseB.accept(new CyclicalContentVisitor(1, 1, new Worksheet())));
    assertFalse(trueB.accept(new CyclicalContentVisitor(1, 1, new Worksheet())));
  }

  @Test
  public void testAcceptContentsVisitorTwo() {
    assertNull(falseB.accept(new IsReferenceVisitor()));
  }

  @Test
  public void testEquals() {
    assertEquals(falseB, new Bool(false));
    assertEquals(trueB, new Bool(true));
    assertEquals(trueB, trueB);

    assertNotEquals(falseB, trueB);
    assertNotEquals(trueB, falseB);
    assertNotEquals(trueB, "hello");
    assertNotEquals(true, trueB);
  }

  @Test
  public void testToString() {
    assertEquals("true", trueB.toString());
    assertEquals("false", falseB.toString());
  }

  @Test
  public void testHashCode() {
    assertEquals(trueB.hashCode(), new Bool(true).hashCode());
    assertNotEquals(trueB.hashCode(), falseB.hashCode());
  }
}