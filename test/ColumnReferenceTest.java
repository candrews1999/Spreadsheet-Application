import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.ColumnReference;
import edu.cs3500.spreadsheets.model.cell.CyclicalContentVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Str;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test class for ColumnReference class which is a type of CellContents.
 */
public class ColumnReferenceTest {
  private WorksheetModel<CellInterface> model;
  private CellContents refAtoE;

  @Before
  public void setUp() {
    model = new Worksheet();
    refAtoE = new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)), model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullReferencedList() {
    new ColumnReference(null, model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    new ColumnReference(new ArrayList<>(Collections.singletonList(1)), null);
  }

  @Test
  public void testEvaluateSimple1() {
    assertEquals(new Dbl(0.0), refAtoE.evaluate());
  }

  @Test
  public void testEvaluateSimple2() {
    model.setCell(1, 1, "hi");
    assertEquals(new Str("hi"), model.getCellAt(1, 1).evaluate());
    assertEquals(new Str("hi"), refAtoE.evaluate());
  }

  @Test
  public void testEvaluateMultItems() {
    model.setCell(1, 1, "5");
    model.setCell(100, 5, "10");
    assertEquals(new Dbl(5.0), refAtoE.evaluate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAcceptCycle() {
    refAtoE.accept(new CyclicalContentVisitor(1, 1, model));
  }

  @Test
  public void testAcceptNoCycle() {
    assertFalse(refAtoE.accept(new CyclicalContentVisitor(1, 10, model)));
  }

  @Test
  public void testEquals() {
    assertNotEquals(new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3, 4)), model),
            refAtoE);
    assertEquals(new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)), model),
            refAtoE);
    assertNotEquals(refAtoE, "hi");
    assertEquals(refAtoE, refAtoE);
  }

  @Test
  public void testToString() {
    assertEquals("1 2 3 4 5 ", refAtoE.toString());
  }

  @Test
  public void testHashCode() {
    assertEquals(refAtoE.hashCode(), refAtoE.hashCode());
    assertNotEquals(new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3, 4)),
            model).hashCode(), refAtoE.hashCode());
    assertEquals(new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)),
            model).hashCode(), refAtoE.hashCode());
  }
}