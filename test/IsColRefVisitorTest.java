import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.cell.CellContentsVisitor;
import edu.cs3500.spreadsheets.model.cell.IsColReferenceVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Str;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for IsColRefVisitor which is a CellContents visitor.
 */
public class IsColRefVisitorTest {

  private CellContentsVisitor<ArrayList<Integer>> visitor = new IsColReferenceVisitor<>();

  @Test
  public void testVisitValue() {
    assertNull(visitor.visitValue(new Dbl(3.0)));
  }

  @Test
  public void testVisitReference() {
    ArrayList<Coord> list = new ArrayList<>(Arrays.asList(
            new Coord(2, 2), new Coord(3, 2)));
    assertNull(visitor.visitReference(list));
  }

  @Test
  public void testVisitFormula() {
    assertNull(visitor.visitFormula(
            new ArrayList<>(Arrays.asList(new Dbl(0.0), new Str("")))));
  }

  @Test
  public void testVisitColRef() {
    assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3)),
            visitor.visitColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3))));
  }
}