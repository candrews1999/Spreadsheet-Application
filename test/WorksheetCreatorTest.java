import org.junit.Before;
import org.junit.Test;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetCreator;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Str;

import static org.junit.Assert.assertEquals;

/**
 * Test class for WorksheetCreator.
 */
public class WorksheetCreatorTest {

  private WorksheetCreator wC;

  @Before
  public void setUp() {
    wC = new WorksheetCreator();
  }

  @Test
  public void testWorksheetCreator() {
    Cell a1 = new Cell(
            new Coord(1, 1),
            new Dbl(1.0), "1");
    assertEquals(a1, wC.createCell(1, 1, "1")
                    .createWorksheet().getCellAt(1, 1));
    Cell c3 = new Cell(
            new Coord(3, 3),
            new Bool(true), "true");
    assertEquals(c3,
            wC.createCell(3, 3, "true")
                    .createWorksheet().getCellAt(3, 3));
    Cell b4 = new Cell(
            new Coord(2, 4), new Str("A1"), "A1");
    assertEquals(b4,
            wC.createCell(2, 4, "A1")
                    .createWorksheet().getCellAt(4, 2));
    Cell z1000 = new Cell(
            new Coord(1000, 26),
            new Dbl(10), "10");
    assertEquals(z1000, wC.createCell(1000, 26, "10")
            .createWorksheet().getCellAt(26, 1000));
  }

  @Test
  public void testWorksheetCreatorSizing() {
    wC.changeColSize(1, 300);
    wC.changeColSize(2, 500);
    wC.createCell(1, 1, "HI");
    wC.changeRowSize(1, 400);
    wC.changeRowSize(10, 400);
    WorksheetModel<CellInterface> worksheet = wC.createWorksheet();
    assertEquals(new Cell(new Coord(1, 1), new Str("HI"), "HI"),
            worksheet.getCellAt(1, 1));
    assertEquals(300, worksheet.getColWidth(30, 1));
    assertEquals(500, worksheet.getColWidth(30, 2));
    assertEquals(400, worksheet.getRowHeight(30, 1));
    assertEquals(400, worksheet.getRowHeight(30, 10));
  }
}