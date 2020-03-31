import org.junit.Test;

import edu.cs3500.spreadsheets.model.cell.IsReferenceVisitor;
import edu.cs3500.spreadsheets.model.cell.value.ErrorVal;
import edu.cs3500.spreadsheets.model.cell.value.IsDblVisitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for ErrorVal Value.
 */
public class ErrorValTest {

  private ErrorVal hi = new ErrorVal("hi");

  @Test
  public void testEvaluate() {
    assertEquals(new ErrorVal("testing"), new ErrorVal("testing").evaluate());
    assertEquals(hi, hi.evaluate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAcceptValueVisitor() {
    hi.accept(new IsDblVisitor());
  }

  @Test
  public void testAcceptContentsVisitor() {
    assertNull(hi.accept(new IsReferenceVisitor()));
  }

  @Test
  public void testEquals() {
    assertEquals(hi, new ErrorVal("hi"));
    assertNotEquals(new ErrorVal("hello"), hi);
    assertNotEquals(hi, "hi");
    assertNotEquals("hi", hi);
  }

  @Test
  public void testToString() {
    assertEquals("hello", new ErrorVal("hello").toString());
    assertEquals("bye", new ErrorVal("bye").toString());
  }

  @Test
  public void testHashCode() {
    assertEquals(hi.hashCode(), new ErrorVal("hi").hashCode());
    assertNotEquals(new ErrorVal("hello").hashCode(), hi.hashCode());
  }
}