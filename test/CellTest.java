import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.Formula;
import edu.cs3500.spreadsheets.model.cell.Reference;
import edu.cs3500.spreadsheets.model.cell.function.Sum;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Str;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for a Cell.
 */
public class CellTest {

  private WorksheetModel<CellInterface> model = new Worksheet();
  private CellContents strHi = new Str("hi");
  private CellContents dbl10Half = new Dbl(10.55);
  private Cell strCellA1 = new Cell(new Coord(1, 1), strHi, "hi");
  private Cell dblCellJ10 = new Cell(new Coord(10, 10), dbl10Half, "10.55");
  private Cell blankA1 = new Cell(new Coord(1, 1), null, "");
  private Formula formSumA1K11;
  private Cell sumA1K11InF6;
  private Formula formSumA5;
  private Cell sumA5InF3;
  private Reference refA6;
  private Cell a6;

  @Before
  public void setUp() {
    model.setCell(1, 1, "hi");
    model.setCell(10, 10, "10.55");
    model.setCell(6, 6, "=(SUM A1 J10)");
    model.setCell(6, 3, "=(SUM F6 F6)");
    model.setCell(6, 1, "=A5");
    formSumA1K11 = new Formula(
            new ArrayList<>(Arrays.asList(strHi, dbl10Half)), new Sum(model));
    sumA1K11InF6 = new Cell(new Coord(6, 6), formSumA1K11,
            "=(SUM A1 J10)");
    formSumA5 = new Formula(
            new ArrayList<>(Arrays.asList(formSumA1K11, formSumA1K11)), new Sum(model));
    sumA5InF3 = new Cell(new Coord(6, 3), formSumA5, "=(SUM F6 F6)");
    refA6 = new Reference(new ArrayList<>(Arrays.asList(new Coord(6, 3),
            new Coord(6, 6))), model);
    a6 = new Cell(new Coord(1, 6), refA6, "=F3:F6");
  }

  // test invalid Cells
  @Test(expected = IllegalArgumentException.class)
  public void testNullOriginal() {
    new Cell(new Coord(1, 1), strHi, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeRow() {
    new Cell(new Coord(-1, 1), new Str(""), "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeColumn() {
    new Cell(new Coord(1, -1), strHi, "hi");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCoord() {
    new Cell(null, strHi, "");
  }

  @Test
  public void testGetCoord() {
    assertEquals(new Coord(1, 1), strCellA1.getCoord());
    assertEquals(new Coord(10, 10), dblCellJ10.getCoord());
    assertEquals(new Coord(6, 6), sumA1K11InF6.getCoord());
    assertEquals(new Coord(6, 3), sumA5InF3.getCoord());
    assertEquals(new Coord(1, 6), a6.getCoord());
  }

  @Test
  public void testGetContents() {
    assertEquals(strHi, strCellA1.getContents());
    assertEquals(dbl10Half, dblCellJ10.getContents());
    assertEquals(formSumA1K11, sumA1K11InF6.getContents());
    assertEquals(formSumA5, sumA5InF3.getContents());
    assertEquals(refA6, a6.getContents());
  }

  @Test
  public void testGetNullContents() {
    assertNull(blankA1.getContents());
  }

  @Test
  public void testGetOriginal() {
    assertEquals("hi", strCellA1.getOriginal());
    assertEquals("10.55", dblCellJ10.getOriginal());
    assertEquals("=(SUM A1 J10)", sumA1K11InF6.getOriginal());
    assertEquals("=F3:F6", a6.getOriginal());
  }

  @Test
  public void testGetOriginalEmpty() {
    assertEquals("", blankA1.getOriginal());
  }

  @Test
  public void testEvaluate() {
    assertEquals(strHi, strCellA1.evaluate());
    assertEquals(dbl10Half, dblCellJ10.evaluate());
    assertEquals(new Dbl(10.550000), sumA1K11InF6.evaluate());
    assertEquals(new Dbl(0.0), a6.evaluate());
  }

  @Test
  public void testEquals() {
    assertEquals(strCellA1, strCellA1);
    assertEquals(strCellA1, new Cell(new Coord(1, 1), new Str("hi"), "hi"));
    assertEquals(new Cell(new Coord(1, 1), strHi, "hi"), strCellA1);
    assertEquals(
            new Cell(new Coord(6, 3),
                    new Formula(new ArrayList<>(Arrays.asList(formSumA1K11, formSumA1K11)),
                            new Sum(model)), "=(SUM A1 E4)"),
            sumA5InF3);
    assertEquals(new Cell(new Coord(1, 6), refA6, "=A5"), a6);
    assertEquals(new Cell(new Coord(1, 1), null, ""), blankA1);
    assertNotEquals(strCellA1, "hi");
    assertNotEquals(new Str("hi"), strCellA1);
    assertNotEquals(strCellA1, new Cell(new Coord(1, 1),
            new Bool(false), "false"));
    assertNotEquals(new Cell(new Coord(1, 1), strHi, ""), blankA1);
    assertNotEquals(blankA1, new Cell(new Coord(1, 1), strHi, ""));
  }

  @Test
  public void testToString() {
    assertEquals("A1 hi",
            strCellA1.toString());
    assertEquals("A6 =F3:F6",
            a6.toString());
    assertEquals("F6 =(SUM A1 J10)",
            sumA1K11InF6.toString());
  }

  @Test
  public void testHashCode() {
    Cell a6Copy = new Cell(new Coord(1, 6), refA6, "=F3:F6");
    Cell sumA1K11Copy = new Cell(new Coord(6, 6), formSumA1K11, "=(SUM A1 J10)");
    assertEquals(strCellA1.hashCode(),
            new Cell(new Coord(1, 1), strHi, "hi").hashCode());
    assertNotEquals(strCellA1.hashCode(), dblCellJ10.hashCode());
    assertEquals(a6Copy.hashCode(), a6.hashCode());
    assertEquals(sumA1K11Copy.hashCode(), sumA1K11InF6.hashCode());
  }
}