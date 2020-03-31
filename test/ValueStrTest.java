import org.junit.Test;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.cell.CyclicalContentVisitor;
import edu.cs3500.spreadsheets.model.cell.IsReferenceVisitor;
import edu.cs3500.spreadsheets.model.cell.value.IsDblVisitor;
import edu.cs3500.spreadsheets.model.cell.value.NumericValueVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Str;
import edu.cs3500.spreadsheets.model.cell.value.Value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for Str Value.
 */
public class ValueStrTest {

  private Value hello = new Str("hello");
  private Value empty = new Str("");
  private Value hi = new Str("hi");

  @Test(expected = IllegalArgumentException.class)
  public void testNull() {
    new Str(null);
  }

  @Test
  public void testEvaluate() {
    assertEquals(new Str("hello"), hello.evaluate());
    assertEquals(new Str(""), empty.evaluate());
    assertEquals(new Str("hi"), hi.evaluate());
  }

  @Test
  public void testAcceptValueVisitorOne() {
    assertTrue(Math.abs(hello.accept(new NumericValueVisitor()) - 0.0) < 0.0001);
    assertTrue(Math.abs(empty.accept(new NumericValueVisitor()) - 0.0) < 0.0001);
  }

  @Test
  public void testAcceptValueVisitorTwo() {
    assertFalse(hello.accept(new IsDblVisitor()));
    assertFalse(empty.accept(new IsDblVisitor()));
  }

  @Test
  public void testAcceptContentVisitorOne() {
    assertFalse(hello.accept(new CyclicalContentVisitor(1, 1, new Worksheet())));
  }

  @Test
  public void testAcceptContentVisitorTwo() {
    assertNull(hello.accept(new IsReferenceVisitor()));
  }

  @Test
  public void testEquals() {
    assertEquals(new Str("hello"), hello);
    assertEquals(new Str(""), empty);
    assertEquals(hello, hello);

    assertNotEquals(new Str("hello"), empty);
    assertNotEquals(new Str("Hello"), hello);
    assertNotEquals(hello, 10);
    assertNotEquals("hello", hello);
  }

  @Test
  public void testToString() {
    assertEquals("hello", hello.toString());
    assertEquals("", empty.toString());
    assertEquals("Jill says \"hi\" to me everyday and she has two \\ everyday.",
            new Str("Jill says \"hi\" to me everyday and she has two \\ everyday.")
                    .toString());
  }

  @Test
  public void testHashCode() {
    assertEquals(hello.hashCode(), new Str("hello").hashCode());
    assertNotEquals(hello.hashCode(), empty.hashCode());
  }
}