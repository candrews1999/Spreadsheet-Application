import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetCreator;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.ColumnReference;
import edu.cs3500.spreadsheets.model.cell.Formula;
import edu.cs3500.spreadsheets.model.cell.Reference;
import edu.cs3500.spreadsheets.model.cell.function.Sum;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.ErrorVal;
import edu.cs3500.spreadsheets.model.cell.value.Str;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Used to test Worksheet model class.
 */
public class WorksheetTest {
  private WorksheetModel<CellInterface> w;

  private WorksheetModel<CellInterface> simpleW;

  private WorksheetModel<CellInterface> complexNoCycleW;

  private WorksheetModel<CellInterface> refFormulaModel;

  private WorksheetModel<CellInterface> emptyFileModel;

  private WorksheetModel<CellInterface> refMultipleModel;

  @Before
  public void setUp() throws FileNotFoundException {
    w = new Worksheet();

    // Setup simpleFile.txt
    FileReader simpleFile = new FileReader("test/simpleFile.txt");
    simpleW = WorksheetReader.read(new WorksheetCreator(), simpleFile);

    // Setup noCycle.txt
    FileReader complexNoCycle = new FileReader("test/noCycle.txt");
    complexNoCycleW = WorksheetReader.read(new WorksheetCreator(), complexNoCycle);

    // Setup refFormula.txt
    FileReader refFormula = new FileReader("test/refFormula.txt");
    refFormulaModel = WorksheetReader.read(new WorksheetCreator(), refFormula);

    // Setup emptyFile.txt
    FileReader emptyFile = new FileReader("test/emptyFile.txt");
    emptyFileModel = WorksheetReader.read(new WorksheetCreator(), emptyFile);

    // Setup multReferences.txt
    FileReader refMultiple = new FileReader("test/multReferences.txt");
    refMultipleModel = WorksheetReader.read(new WorksheetCreator(), refMultiple);
  }

  @Test
  public void simpleFileTest() {
    assertEquals(new Cell(new Coord(1, 1), new Dbl(5), "5"),
            simpleW.getCellAt(1, 1));
    assertEquals(new Dbl(5),
            simpleW.getCellAt(1, 1).getContents().evaluate());
    assertEquals(new Bool(true),
            simpleW.getCellAt(2, 1).getContents().evaluate());
    assertEquals(new Bool(true),
            simpleW.getCellAt(3, 1).getContents().evaluate());
    assertEquals(new Str("\"hi\""),
            simpleW.getCellAt(1, 2).getContents().evaluate());
    assertEquals(new Dbl(6.9),
            simpleW.getCellAt(2, 2).getContents().evaluate());
    assertEquals(new Dbl(11.9),
            simpleW.getCellAt(3, 2).getContents().evaluate());
    assertEquals(4, simpleW.numRows());
    assertEquals(2, simpleW.numCols());
    Cell a1 = new Cell(new Coord(1, 1), new Dbl(5.0), "5");
    Cell a3 = new Cell(new Coord(1, 3), new Bool(true), "true");
    Cell a2 = new Cell(new Coord(1, 2),
            new Reference(new ArrayList<>(Collections.singletonList(new Coord(1, 3))),
                    simpleW),
            "=A3");
    Cell b2 = new Cell(new Coord(2, 2), new Dbl(6.9), "6.9");
    ArrayList<Cell> simpleCells = new ArrayList<>(Arrays.asList(a1, a2, a3,
            new Cell(new Coord(2, 1), new Str("\"hi\""), "\"hi\""),
            b2,
            new Cell(new Coord(2, 3),
                    new Formula(new ArrayList<>(Arrays.asList(
                            new Reference(new ArrayList<>(
                                    Collections.singletonList(new Coord(1, 1))), simpleW),
                            new Reference(new ArrayList<>(
                                    Collections.singletonList(new Coord(1, 2))), simpleW),
                            new Reference(new ArrayList<>(
                                    Collections.singletonList(new Coord(2, 2))),
                                    simpleW))),
                            new Sum(simpleW)),
                    "=(SUM A1 A2 B2)"),
            new Cell(new Coord(2, 4), new Str("whatwhatwhatwhatwhat"),
                    "whatwhatwhatwhatwhat")));
    assertTrue(simpleCells.containsAll(simpleW.getAllCells())
            && simpleW.getAllCells().containsAll(simpleCells));
    simpleW.deleteCell(1, 1);
    assertFalse(simpleCells.containsAll(simpleW.getAllCells())
            && simpleW.getAllCells().containsAll(simpleCells));
    simpleCells.remove(a1);
    assertTrue(simpleW.getAllCells().containsAll(simpleCells));
    assertEquals(new Cell(new Coord(1, 1), null, ""),
            simpleW.getCellAt(1, 1));
  }

  @Test
  public void colRefExample() throws FileNotFoundException {
    FileReader f = new FileReader("test/ColRefExample.txt");
    WorksheetModel<CellInterface> model = WorksheetReader.read(new WorksheetCreator(), f);
    assertEquals(new Cell(new Coord(3, 1),
            new Formula(new ArrayList<>(Collections.singletonList(
                    new ColumnReference(new ArrayList<>(Arrays.asList(1, 2)), model))),
                    new Sum(model)), "= (SUM A:B)"), model.getCellAt(1, 3));
  }

  @Test
  public void simpleCycleTest() throws FileNotFoundException {
    FileReader simpleCycle = new FileReader("test/simpleCycle.txt");
    WorksheetModel<CellInterface> model = WorksheetReader.read(new WorksheetCreator(), simpleCycle);
    assertEquals(new Cell(new Coord(1, 4),
            new ErrorVal("Error in cell A4: Can't have cyclical references."),
            "=A5"), model.getCellAt(4, 1));
  }

  @Test
  public void complexNoCycleTest() {
    assertEquals(new Cell(new Coord(1, 1), new Bool(true), "true"),
            complexNoCycleW.getCellAt(1, 1));
    assertEquals(new Cell(new Coord(1, 2), new Dbl(1.0), "1.0"),
            complexNoCycleW.getCellAt(2, 1));
    assertEquals(new Dbl(1.0),
            complexNoCycleW.getCellAt(3, 1).getContents().evaluate());
    assertEquals(new Dbl(1.0),
            complexNoCycleW.getCellAt(1, 2).getContents().evaluate());
    assertEquals(new Dbl(35.1),
            complexNoCycleW.getCellAt(100, 52).getContents().evaluate());
    assertEquals(new Dbl(240.1),
            complexNoCycleW.getCellAt(1, 6).getContents().evaluate());
    assertEquals(new Bool(false),
            complexNoCycleW.getCellAt(1, 7).getContents().evaluate());
    assertEquals(new Str("hiihavespaces"),
            complexNoCycleW.getCellAt(3, 7).getContents().evaluate());
  }

  @Test
  public void testRefFormula() {
    assertEquals(new Dbl(15.0),
            refFormulaModel.getCellAt(1, 1).evaluate());
    assertEquals(new Dbl(15.0),
            refFormulaModel.getCellAt(1, 1).evaluate());
    assertEquals(new Dbl(23.0),
            refFormulaModel.getCellAt(2, 1).evaluate());
    assertEquals(new Dbl(23.0),
            refFormulaModel.getCellAt(2, 1).evaluate());
    assertEquals(new Dbl(38.0),
            refFormulaModel.getCellAt(3, 1).evaluate());
    assertEquals(new Dbl(38.0),
            refFormulaModel.getCellAt(4, 1).evaluate());
    assertEquals(new Dbl(1444.0),
            refFormulaModel.getCellAt(5, 1).evaluate());
    assertEquals(new Dbl(152.0),
            refFormulaModel.getCellAt(7, 1).evaluate());
    assertEquals(new Dbl(38),
            refFormulaModel.getCellAt(4, 1).evaluate());
  }

  @Test
  public void testUpdate() throws FileNotFoundException {
    FileReader file = new FileReader("test/updateCell.txt");
    WorksheetModel<CellInterface> model = WorksheetReader.read(new WorksheetCreator(), file);
    assertEquals(new Dbl(5), model.getCellAt(1, 1).evaluate());
    assertTrue(new ArrayList<>(Arrays.asList(new Cell(new Coord(1, 1),
                    new Dbl(2), "2"),
            new Cell(new Coord(1, 2), new Dbl(5), "5")))
            .containsAll(new ArrayList<Cell>()));
  }

  @Test
  public void testEmptyFile() {
    assertEquals(0, emptyFileModel.numCols());
    assertEquals(0, emptyFileModel.numRows());
    assertEquals(new Cell(new Coord(1, 1), null, ""),
            emptyFileModel.getCellAt(1, 1));
  }

  @Test
  public void testDirectRefFormula() throws FileNotFoundException {
    FileReader directRefCycle = new FileReader("test/directRefFormula.txt");
    WorksheetModel<CellInterface> model
            = WorksheetReader.read(new WorksheetCreator(), directRefCycle);
    assertEquals(new Cell(new Coord(1, 5),
                    new ErrorVal("Error in cell A5: Can't have cyclical references."),
                    "=(PRODUCT A1 A2 A3 A4 A5)"),
            model.getCellAt(5, 1));
  }

  @Test
  public void testMultipleReferences() {
    assertEquals(new Dbl(4), refMultipleModel.getCellAt(1, 1).evaluate());
    assertEquals(new Dbl(8), refMultipleModel.getCellAt(2, 1).evaluate());
    assertEquals(new Dbl(16), refMultipleModel.getCellAt(1, 2).evaluate());
    assertEquals(new Dbl(24), refMultipleModel.getCellAt(4, 2).evaluate());
    assertEquals(new Dbl(12288), refMultipleModel.getCellAt(1, 3).evaluate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaError() throws FileNotFoundException {
    FileReader file = new FileReader("test/formulaError.txt");
    WorksheetModel<CellInterface> model = WorksheetReader.read(new WorksheetCreator(), file);
    model.getCellAt(3, 1).evaluate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetCellSelfCyclicalCellContent() {
    w.setCell(0, 0, "=A1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetCellSelfCyclicalCellContentInner() {
    w.setCell(1, 1, "=B1"); // 0 0 is A1
    System.out.println(w.getCellAt(0, 0).getContents()); // A1's contents are null
    w.setCell(1, 2, "=A1"); // 0 1 i
  }

  @Test
  public void testAddResizedColAndGetResizedCols() {
    w.addResizedCol(80, 8, 100);
    HashMap<Integer, Integer> r = w.getResizedCols();
    assertEquals(100, (int) r.get(8));
    w.addResizedCol(80, 8, 200);
    assertEquals(1, r.size());
    w.addResizedCol(80, 9, 220);
    r = w.getResizedCols();
    assertEquals(2, r.size());
    assertEquals(200, (int) r.get(8));
  }

  @Test
  public void testAddResizedRowAndGetResizedRows() {
    w.addResizedRow(80, 10, 110);
    HashMap<Integer, Integer> r = w.getResizedRows();
    assertEquals(110, (int) r.get(10));
    w.addResizedRow(80, 10, 100);
    assertEquals(1, r.size());
    w.addResizedRow(80, 9, 200);
    r = w.getResizedRows();
    assertEquals(2, r.size());
    assertEquals(100, (int) r.get(10));
  }

  @Test
  public void testGetRowHeight() {
    w.addResizedRow(80, 10, 110);
    assertEquals(110, w.getRowHeight(80, 10));
    assertEquals(80, w.getRowHeight(80, 11));
  }

  @Test
  public void testGetColWidth() {
    w.addResizedCol(80, 10, 110);
    assertEquals(110, w.getColWidth(80, 10));
    assertEquals(80, w.getColWidth(80, 11));
  }

  @Test
  public void testGetLeftXOfCol() {
    w.addResizedCol(80, 10, 300);
    w.addResizedCol(80, 9, 200);
    assertEquals(840, w.getLeftXOfCol(80, 10));
    assertEquals(640, w.getLeftXOfCol(80, 9));
    assertEquals(1140, w.getLeftXOfCol(80, 11));
    assertEquals(0, w.getLeftXOfCol(80, 1));
    assertEquals(80, w.getLeftXOfCol(80, 2));
  }

  @Test
  public void testGetUpperYOfRow() {
    w.addResizedRow(80, 10, 300);
    w.addResizedRow(80, 9, 200);
    assertEquals(840, w.getUpperYOfRow(80, 10));
    assertEquals(640, w.getUpperYOfRow(80, 9));
    assertEquals(1140, w.getUpperYOfRow(80, 11));
    assertEquals(0, w.getUpperYOfRow(80, 1));
    assertEquals(80, w.getUpperYOfRow(80, 2));
  }
}