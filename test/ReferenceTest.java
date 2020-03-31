import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.Reference;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Str;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for a Reference which is a CellContent.
 */
public class ReferenceTest {

  private WorksheetModel<CellInterface> model;
  private Cell strCell;
  private CellContents refStrValue;
  private CellContents refDblValue;
  private CellContents refBoolTValue;
  private CellContents refRefRefStr;
  private CellContents refFormulaSum;

  @Before
  public void setUp() {
    model = new Worksheet();
    model.setCell(1, 1, "happy");
    model.setCell(2, 1, "10.654");
    model.setCell(3, 1, "true");
    model.setCell(4, 1, "=A1");
    model.setCell(2, 2, "=(SUM A1 A2 A3 5.55 12)");
    strCell = new Cell(new Coord(1, 1), new Str("happy"), "happy");
    refStrValue = new Reference(
            new ArrayList<>(Collections.singletonList(new Coord(1, 1))), model);
    refDblValue = new Reference(
            new ArrayList<>(Collections.singletonList(new Coord(1, 2))), model);
    refBoolTValue = new Reference(
            new ArrayList<>(Collections.singletonList(new Coord(1, 3))), model);
    refRefRefStr = new Reference(new ArrayList<>(
            Collections.singletonList(new Coord(1, 4))), model);
    refFormulaSum = new Reference(
            new ArrayList<>(Collections.singletonList(new Coord(2, 2))), model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNull() {
    new Reference(null, new Worksheet());
  }

  @Test
  public void testEvaluateRefValues() {
    assertEquals(new Str("happy"), refStrValue.evaluate());
    assertEquals(new Dbl(10.654), refDblValue.evaluate());
    assertEquals(new Bool(true), refBoolTValue.evaluate());
  }

  @Test
  public void testEvaluateRefRefs() {
    assertEquals(new Str("happy"), refRefRefStr.evaluate());
    assertEquals(new Str("happy"),
            new Reference(new ArrayList<>(Collections.singletonList(new Coord(1, 1))),
                    model).evaluate());
  }

  @Test
  public void testEvaluateRefFormula() {
    assertEquals(new Dbl(28.204), refFormulaSum.evaluate());
  }

  @Test
  public void testEquals() {
    assertNotEquals(strCell, refRefRefStr);
    assertEquals(new Reference(new ArrayList<>(
            Collections.singletonList(new Coord(1, 4))), model), refRefRefStr);
  }

  @Test
  public void testHash() {
    assertNotEquals(strCell.hashCode(), refRefRefStr.hashCode());
    assertEquals(new Reference(new ArrayList<>(
                    Collections.singletonList(new Coord(1, 4))), model).hashCode(),
            refRefRefStr.hashCode());
  }
}