import org.junit.Test;

import edu.cs3500.spreadsheets.model.cell.value.StringNoSpaceVisitor;
import edu.cs3500.spreadsheets.model.cell.value.ValueVisitor;

import static org.junit.Assert.assertEquals;

/**
 * Test class for StringNoSpaceVisitor which is a Value visitor.
 */
public class StringNoSpaceVisitorTest {

  private ValueVisitor<String> visitor = new StringNoSpaceVisitor();

  @Test(expected = IllegalArgumentException.class)
  public void testVisitBool1() {
    assertEquals("", visitor.visitBool(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitBool2() {
    assertEquals("", visitor.visitBool(true));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitDbl() {
    assertEquals("", visitor.visitDbl(10.211));
  }

  @Test
  public void testVisitStr() {
    assertEquals("", visitor.visitStr(""));
    assertEquals("helloworld", visitor.visitStr("hello world"));
    assertEquals("anothertestwithmorespaces!",
            visitor.visitStr("another test with more spaces!"));
    assertEquals("AnotherTest!Wow!",
            visitor.visitStr("Another Test ! Wow!"));
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