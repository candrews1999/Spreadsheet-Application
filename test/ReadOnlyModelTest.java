import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyModel;
import edu.cs3500.spreadsheets.model.WorksheetCreator;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.Formula;
import edu.cs3500.spreadsheets.model.cell.Reference;
import edu.cs3500.spreadsheets.model.cell.function.Sum;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.ErrorVal;
import edu.cs3500.spreadsheets.model.cell.value.Str;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Used to test a ReadOnlyModel.
 */
public class ReadOnlyModelTest {
  private ReadOnlyModel<CellInterface> readSimple;
  private ReadOnlyModel<CellInterface> readEmpty;

  @Before
  public void setUp() throws FileNotFoundException {
    // Setup simpleFile.txt
    FileReader simpleFile = new FileReader("test/simpleFile.txt");
    WorksheetModel<CellInterface> simpleW
            = WorksheetReader.read(new WorksheetCreator(), simpleFile);
    readSimple = new ReadOnlyModel<>(simpleW);

    // Setup emptyFile.txt
    FileReader emptyFile = new FileReader("test/emptyFile.txt");
    WorksheetModel<CellInterface> emptyFileModel
            = WorksheetReader.read(new WorksheetCreator(), emptyFile);
    readEmpty = new ReadOnlyModel<>(emptyFileModel);
  }

  @Test
  public void simpleFileTest() {
    assertEquals(new Cell(new Coord(1, 1), new Dbl(5), "5"),
            readSimple.getCellAt(1, 1));
    assertEquals(new Dbl(5),
            readSimple.getCellAt(1, 1).getContents().evaluate());
    assertEquals(new Bool(true),
            readSimple.getCellAt(2, 1).getContents().evaluate());
    assertEquals(new Bool(true),
            readSimple.getCellAt(3, 1).getContents().evaluate());
    assertEquals(new Str("\"hi\""),
            readSimple.getCellAt(1, 2).getContents().evaluate());
    assertEquals(new Dbl(6.9),
            readSimple.getCellAt(2, 2).getContents().evaluate());
    assertEquals(new Dbl(11.9),
            readSimple.getCellAt(3, 2).getContents().evaluate());
    assertEquals(4, readSimple.numRows());
    assertEquals(2, readSimple.numCols());
    Cell a1 = new Cell(new Coord(1, 1), new Dbl(5.0), "5");
    Cell a3 = new Cell(new Coord(1, 3), new Bool(true), "true");
    Cell a2 = new Cell(new Coord(1, 2),
            new Reference(new ArrayList<>(Collections.singletonList(new Coord(1, 3))),
                    readSimple),
            "=A3");
    // test delete Cell
    Cell b2 = new Cell(new Coord(2, 2), new Dbl(6.9), "6.9");
    ArrayList<Cell> simpleCells = new ArrayList<>(Arrays.asList(a1, a2, a3,
            new Cell(new Coord(2, 1), new Str("\"hi\""), "\"hi\""),
            b2,
            new Cell(new Coord(2, 3),
                    new Formula(new ArrayList<>(Arrays.asList(
                            new Reference(new ArrayList<>(
                                    Collections.singletonList(new Coord(1, 1))),
                                    readSimple),
                            new Reference(new ArrayList<>(
                                    Collections.singletonList(new Coord(1, 2))),
                                    readSimple),
                            new Reference(new ArrayList<>(
                                    Collections.singletonList(new Coord(2, 2))),
                                    readSimple))),
                            new Sum(readSimple)),
                    "=(SUM A1 A2 B2)"),
            new Cell(new Coord(2, 4), new Str("whatwhatwhatwhatwhat"),
                    "whatwhatwhatwhatwhat")));
    assertTrue(simpleCells.containsAll(readSimple.getAllCells()));
    readSimple.deleteCell(1, 1);
    assertTrue(simpleCells.containsAll(readSimple.getAllCells()));
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
    assertEquals(0, readEmpty.numCols());
    assertEquals(0, readEmpty.numRows());
    assertEquals(new Cell(new Coord(1, 1), null, ""),
            readEmpty.getCellAt(1, 1));
    assertEquals(new ArrayList<>(), readEmpty.getAllCells());
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

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaError() throws FileNotFoundException {
    FileReader file = new FileReader("test/formulaError.txt");
    WorksheetModel<CellInterface> model = WorksheetReader.read(new WorksheetCreator(), file);
    model.getCellAt(3, 1).evaluate();
  }

  @Test
  public void testSetCell() {
    assertEquals(new ArrayList<>(), readEmpty.getAllCells());
    readEmpty.setCell(1, 1, "");
    assertEquals(new ArrayList<>(), readEmpty.getAllCells());
  }
}