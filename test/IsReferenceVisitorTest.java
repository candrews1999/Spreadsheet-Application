import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.cell.CellContentsVisitor;
import edu.cs3500.spreadsheets.model.cell.IsReferenceVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Str;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for IsReferenceVisitor which is a CellContents visitor.
 */
public class IsReferenceVisitorTest {

  private CellContentsVisitor<ArrayList<Coord>> visitor = new IsReferenceVisitor();

  @Test
  public void testVisitValue() {
    assertNull(visitor.visitValue(new Str("")));
    assertNull(visitor.visitValue(new Bool(false)));
    assertNull(visitor.visitValue(new Dbl(0.0)));
  }

  @Test
  public void testVisitReference() {
    ArrayList<Coord> list = new ArrayList<>(Arrays.asList(
            new Coord(2, 2), new Coord(3, 2)));
    assertEquals(list, visitor.visitReference(list));
  }

  @Test
  public void testVisitFormula() {
    assertNull(visitor.visitFormula(
            new ArrayList<>(Arrays.asList(new Dbl(0.0), new Str("")))));
  }
}