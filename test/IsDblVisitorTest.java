import org.junit.Test;

import edu.cs3500.spreadsheets.model.cell.value.IsDblVisitor;
import edu.cs3500.spreadsheets.model.cell.value.ValueVisitor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class for IsDblVisitor which is a Value visitor.
 */
public class IsDblVisitorTest {

  private ValueVisitor<Boolean> visitor = new IsDblVisitor();

  @Test
  public void testVisitBool() {
    assertFalse(visitor.visitBool(true));
    assertFalse(visitor.visitBool(false));
  }

  @Test
  public void testVisitDbl() {
    assertTrue(visitor.visitDbl(102.33));
    assertTrue(visitor.visitDbl(0.0));
  }

  @Test
  public void testVisitStr() {
    assertFalse(visitor.visitStr("hello"));
    assertFalse(visitor.visitStr("hi"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitStrNull() {
    visitor.visitStr(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitError() {
    visitor.visitError("hi");
  }
}