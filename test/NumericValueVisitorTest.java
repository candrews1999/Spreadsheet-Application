import org.junit.Test;

import edu.cs3500.spreadsheets.model.cell.value.NumericValueVisitor;
import edu.cs3500.spreadsheets.model.cell.value.ValueVisitor;

import static org.junit.Assert.assertTrue;

/**
 * Test class for NumericValueVisitor which is a Value visitor.
 */
public class NumericValueVisitorTest {

  private ValueVisitor<Double> visitor = new NumericValueVisitor();

  @Test
  public void testVisitBool() {
    assertTrue(visitor.visitBool(false) - 0.0 < 0.0001);
    assertTrue(visitor.visitBool(true) - 0.0 < 0.0001);
  }

  @Test
  public void testVisitDbl() {
    assertTrue(Math.abs(visitor.visitDbl(0.0) - 0.0) < 0.0001);
    assertTrue(Math.abs(visitor.visitDbl(57.342) - 57.342) < 0.0001);
    assertTrue(Math.abs(visitor.visitDbl(-7.00) + 7.0) < 0.0001);
  }

  @Test
  public void testVisitStr() {
    assertTrue(visitor.visitStr("hellooooo") - 0.0 < 0.0001);
    assertTrue(visitor.visitStr("false") - 0.0 < 0.0001);
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