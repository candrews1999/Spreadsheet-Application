import org.junit.Test;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.cell.CyclicalContentVisitor;
import edu.cs3500.spreadsheets.model.cell.IsReferenceVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.IsDblVisitor;
import edu.cs3500.spreadsheets.model.cell.value.NumericValueVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class for Dbl Value.
 */
public class ValueDblTest {

  private Value zero = new Dbl(0.0);
  private Value negative = new Dbl(-10.55);
  private Value other = new Dbl(102.7654);

  @Test
  public void testEvaluate() {
    assertEquals(new Dbl(0.0), zero.evaluate());
    assertEquals(new Dbl(-10.55), negative.evaluate());
    assertEquals(new Dbl(102.7654), other.evaluate());
  }

  @Test
  public void testAcceptValueVisitorOne() {
    assertTrue(zero.accept(new NumericValueVisitor()) - 0 < 0.0001);
    assertTrue(Math.abs(negative.accept(new NumericValueVisitor()) + 10.55) < 0.0001);
    assertTrue(Math.abs(other.accept(new NumericValueVisitor()) - 102.7654) < 0.0001);
  }

  @Test
  public void testAcceptValueVisitorTwo() {
    assertTrue(zero.accept(new IsDblVisitor()));
    assertTrue(negative.accept(new IsDblVisitor()));
    assertTrue(other.accept(new IsDblVisitor()));
  }

  @Test
  public void testAcceptContentsVisitorOne() {
    assertFalse(zero.accept(new CyclicalContentVisitor(1, 1, new Worksheet())));
  }

  @Test
  public void testAcceptContentsVisitorTwo() {
    assertNull(negative.accept(new IsReferenceVisitor()));
  }

  @Test
  public void testEquals() {
    assertEquals(zero, new Dbl(0.0));
    assertEquals(negative, new Dbl(-10.55));
    assertEquals(other, new Dbl(102.765432));
    assertEquals(other, other);

    assertNotEquals(other, "hello");
    assertNotEquals(0, zero);
    assertNotEquals(zero, new Dbl(0.01));
    assertNotEquals(negative, new Dbl(10.55));
  }

  @Test
  public void testToString() {
    assertEquals("-10.550000", negative.toString());
    assertEquals("0.000000", zero.toString());
  }

  @Test
  public void testHashCode() {
    assertEquals(new Dbl(0).hashCode(), zero.hashCode());
    assertNotEquals(zero.hashCode(), negative.hashCode());
  }
}