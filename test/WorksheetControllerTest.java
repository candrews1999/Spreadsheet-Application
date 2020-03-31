import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.cs3500.spreadsheets.controller.WorksheetController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyModel;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetCreator;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Str;
import edu.cs3500.spreadsheets.view.WorksheetTableView;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test class for WorksheetController.
 */
public class WorksheetControllerTest {

  private WorksheetModel<CellInterface> model = new Worksheet();
  private WorksheetController controller = new WorksheetController(model);

  // test setView
  @Test(expected = UnsupportedOperationException.class)
  public void testSetViewTextual() throws IOException {
    WorksheetTextualView textualView
            = new WorksheetTextualView(new ReadOnlyModel<>(model), new StringBuilder());
    controller.setView(textualView);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetViewTable() throws IOException {
    WorksheetTableView tableView
            = new WorksheetTableView(new ReadOnlyModel<>(model));
    controller.setView(tableView);
  }

  @Test
  public void testConfirmChange() {
    // check null before
    assertEquals(new Cell(new Coord(1, 1), null, ""),
            model.getCellAt(1, 1));
    // add Cell
    controller.confirmChange(new Coord(1, 1), "hi");
    // check after
    assertEquals(new Cell(new Coord(1, 1), new Str("hi"), "hi"),
            model.getCellAt(1, 1));
    // set Cell to new content
    controller.confirmChange(new Coord(1, 1), "5");
    // check after
    assertEquals(new Cell(new Coord(1, 1), new Dbl(5.0), "5"),
            model.getCellAt(1, 1));
  }

  @Test
  public void testDeleteCell() {
    // check null before
    assertEquals(new Cell(new Coord(1, 1), null, ""),
            model.getCellAt(1, 1));
    // test delete an empty Cell
    controller.deleteCell(new Coord(1, 1));
    // check after
    assertEquals(new Cell(new Coord(1, 1), null, ""),
            model.getCellAt(1, 1));
    // add Cell
    controller.confirmChange(new Coord(1, 1), "5");
    // check after
    assertEquals(new Cell(new Coord(1, 1), new Dbl(5.0), "5"),
            model.getCellAt(1, 1));
    // delete Cell
    controller.deleteCell(new Coord(1, 1));
    // check after
    assertEquals(new Cell(new Coord(1, 1), null, ""),
            model.getCellAt(1, 1));
  }

  @Test
  public void testSaveModel() throws IOException {
    // add Cells
    controller.confirmChange(new Coord(1, 1), "hi");
    controller.confirmChange(new Coord(1, 2), "5");
    controller.confirmChange(new Coord(1, 3), "true");
    // check additions
    Cell a1 = new Cell(new Coord(1, 1), new Str("hi"), "hi");
    Cell a2 = new Cell(new Coord(1, 2), new Dbl(5.0), "5");
    Cell a3 = new Cell(new Coord(1, 3), new Bool(true), "true");
    ArrayList<Cell> cells = new ArrayList<>(Arrays.asList(
            a1, a2, a3));
    assertTrue(cells.containsAll(model.getAllCells()) && model.getAllCells().size() == 3);
    // save model
    controller.saveModel("test/myTest.txt");
    // make new view with saved file
    FileReader saved = new FileReader("test/myTest.txt");
    WorksheetModel<CellInterface> afterSave = WorksheetReader.read(new WorksheetCreator(), saved);
    // check that model has the right Cells
    assertEquals(a1, afterSave.getCellAt(1, 1));
    assertEquals(a2, afterSave.getCellAt(2, 1));
    assertEquals(a3, afterSave.getCellAt(3, 1));
  }

  @Test
  public void testAddResizedColAndGetResizedCols() {
    controller.resizeCol(80, 8, 100);
    HashMap<Integer, Integer> r = model.getResizedCols();
    assertEquals(100, (int) r.get(8));
    controller.resizeCol(80, 8, 200);
    assertEquals(1, r.size());
    controller.resizeCol(80, 9, 220);
    r = model.getResizedCols();
    assertEquals(2, r.size());
    assertEquals(200, (int) r.get(8));
  }

  @Test
  public void testAddResizedRowAndGetResizedRows() {
    controller.resizeRow(80, 10, 110);
    HashMap<Integer, Integer> r = model.getResizedRows();
    assertEquals(110, (int) r.get(10));
    controller.resizeRow(80, 10, 100);
    assertEquals(1, r.size());
    controller.resizeRow(80, 9, 200);
    r = model.getResizedRows();
    assertEquals(2, r.size());
    assertEquals(100, (int) r.get(10));
  }
}