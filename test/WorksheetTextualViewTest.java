import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cs3500.spreadsheets.controller.WorksheetController;
import edu.cs3500.spreadsheets.model.ReadOnlyModel;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetCreator;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WorksheetTextualView.
 */
public class WorksheetTextualViewTest {

  private WorksheetTextualView view;
  private PrintWriter out;
  private WorksheetModel<CellInterface> simpleFileW;
  private ReadOnlyModel<CellInterface> simpleW;
  private ReadOnlyModel<CellInterface> refFormulaModel;
  private ReadOnlyModel<CellInterface> emptyFileModel;
  private ReadOnlyModel<CellInterface> readResized;

  @Before
  public void setUp() throws FileNotFoundException {
    out = new PrintWriter("test/myTestFile.txt");

    // Setup simpleFile.txt
    FileReader simpleFile = new FileReader("test/simpleFile.txt");
    simpleFileW = WorksheetReader.read(new WorksheetCreator(), simpleFile);
    simpleW = new ReadOnlyModel<>(simpleFileW);

    // Setup refFormula.txt
    FileReader refFormula = new FileReader("test/refFormula.txt");
    WorksheetModel<CellInterface> refFormulaModelW
            = WorksheetReader.read(new WorksheetCreator(), refFormula);
    refFormulaModel = new ReadOnlyModel<>(refFormulaModelW);

    // Setup emptyFile.txt
    FileReader emptyFile = new FileReader("test/emptyFile.txt");
    WorksheetModel<CellInterface> emptyFileModelW = WorksheetReader.read(new WorksheetCreator(),
            emptyFile);
    emptyFileModel = new ReadOnlyModel<>(emptyFileModelW);

    WorksheetModel<CellInterface> modelResized = new Worksheet();
    modelResized.addResizedCol(80, 1, 500);
    modelResized.addResizedCol(80, 2, 300);
    modelResized.addResizedCol(80, 5, 10);
    modelResized.addResizedRow(20, 2, 200);
    modelResized.addResizedRow(20, 4, 300);
    modelResized.addResizedRow(20, 6, 400);
    readResized = new ReadOnlyModel<>(modelResized);
  }

  // test null constructors
  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    new WorksheetTextualView(null, out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullWriter() {
    new WorksheetTextualView(new ReadOnlyModel<>(new Worksheet()), null);
  }

  // test render
  @Test
  public void testRenderSimple() throws IOException {
    view = new WorksheetTextualView(simpleW, out);
    view.render();
    out.close();
    FileReader newFile = new FileReader("test/myTestFile.txt");
    WorksheetModel<CellInterface> afterSave = WorksheetReader.read(new WorksheetCreator(), newFile);
    assertTrue(simpleW.getAllCells().containsAll(afterSave.getAllCells())
            && afterSave.getAllCells().containsAll(simpleW.getAllCells()));
    assertEquals(simpleW.getCellAt(1, 1), afterSave.getCellAt(1, 1));
    assertEquals(simpleW.getCellAt(2, 1), afterSave.getCellAt(2, 1));
    assertEquals(simpleW.getCellAt(3, 1), afterSave.getCellAt(3, 1));
    assertEquals(simpleW.getCellAt(1, 2), afterSave.getCellAt(1, 2));
    assertEquals(simpleW.getCellAt(2, 2), afterSave.getCellAt(2, 2));
    assertEquals(simpleW.getCellAt(3, 2), afterSave.getCellAt(3, 2));
    assertEquals(simpleW.getCellAt(4, 2), afterSave.getCellAt(4, 2));
  }

  @Test
  public void testRenderUpdate() throws IOException {
    view = new WorksheetTextualView(refFormulaModel, out);
    view.render();
    out.close();
    FileReader newFile = new FileReader("test/myTestFile.txt");
    WorksheetModel<CellInterface> afterSave = WorksheetReader.read(new WorksheetCreator(), newFile);
    assertEquals(refFormulaModel.getCellAt(1, 1), afterSave.getCellAt(1, 1));
    assertEquals(refFormulaModel.getCellAt(2, 1), afterSave.getCellAt(2, 1));
    assertEquals(refFormulaModel.getCellAt(3, 1), afterSave.getCellAt(3, 1));
    assertEquals(refFormulaModel.getCellAt(4, 1), afterSave.getCellAt(4, 1));
    assertEquals(refFormulaModel.getCellAt(5, 1), afterSave.getCellAt(5, 1));
    assertEquals(refFormulaModel.getCellAt(7, 1), afterSave.getCellAt(7, 1));
  }

  @Test
  public void testRenderWResize() throws IOException {
    view = new WorksheetTextualView(readResized, out);
    view.render();
    out.close();
    FileReader newFile = new FileReader("test/myTestFile.txt");
    WorksheetModel<CellInterface> afterSave = WorksheetReader.read(new WorksheetCreator(), newFile);
    assertEquals(500, afterSave.getColWidth(80, 1));
    assertEquals(300, afterSave.getColWidth(80, 2));
    assertEquals(10, afterSave.getColWidth(80, 5));
    assertEquals(200, afterSave.getRowHeight(20, 2));
    assertEquals(300, afterSave.getRowHeight(20, 4));
    assertEquals(400, afterSave.getRowHeight(20, 6));
  }

  @Test
  public void testRenderEmpty() throws IOException {
    view = new WorksheetTextualView(emptyFileModel, out);
    view.render();
    out.close();
    FileReader newFile = new FileReader("test/myTestFile.txt");
    WorksheetModel<CellInterface> afterSave = WorksheetReader.read(new WorksheetCreator(), newFile);
    assertEquals(0, afterSave.getAllCells().size());
  }

  @Test
  public void testRenderWError() throws IOException {
    FileReader errorFile = new FileReader("test/simpleCycle.txt");
    WorksheetModel<CellInterface> error = WorksheetReader.read(new WorksheetCreator(), errorFile);
    ReadOnlyModel<CellInterface> errorW = new ReadOnlyModel<>(error);
    view = new WorksheetTextualView(errorW, out);
    view.render();
    out.close();
    FileReader newFile = new FileReader("test/myTestFile.txt");
    WorksheetModel<CellInterface> afterSave = WorksheetReader.read(new WorksheetCreator(), newFile);
    ReadOnlyModel<CellInterface> afterSaveW = new ReadOnlyModel<>(afterSave);
    assertEquals(errorW.getCellAt(2, 1), afterSave.getCellAt(2, 1));
    assertEquals(errorW.getCellAt(1, 1), afterSave.getCellAt(1, 1));
    view = new WorksheetTextualView(afterSaveW, new StringBuilder());
    assertEquals(errorW.getCellAt(5, 1), afterSave.getCellAt(5, 1));
    assertEquals(errorW.getCellAt(4, 1), afterSave.getCellAt(4, 1));
    assertEquals(errorW.getCellAt(3, 1), afterSave.getCellAt(3, 1));
  }

  // test toString
  @Test
  public void testToStringSimple() {
    view = new WorksheetTextualView(simpleW, out);
    List<String> lines = new ArrayList<>(
            Arrays.asList("A2 =A3", "B3 =(SUM A1 A2 B2)", "A1 5", "B2 6.9",
                    "B1 \"hi\"", "A3 true", "B4 whatwhatwhatwhatwhat"));
    String viewString = view.toString();
    List<String> viewLines = new ArrayList<>(Arrays.asList(viewString.split("\\r?\\n")));
    assertTrue(lines.containsAll(viewLines) && viewLines.containsAll(lines));
  }

  @Test
  public void testToStringUpdated() {
    view = new WorksheetTextualView(refFormulaModel, out);
    List<String> lines = new ArrayList<>(
            Arrays.asList("A1 15", "A2 =(SUM A1 3 5)", "A3 =(SUM A1:A2)", "A4 =A3",
                    "A5 =(PRODUCT A3:A4)", "A7 =(SUM A1 A1 A2 A2 A3 A3)"));
    String viewString = view.toString();
    List<String> viewLines = new ArrayList<>(Arrays.asList(viewString.split("\\r?\\n")));
    assertTrue(lines.containsAll(viewLines) && viewLines.containsAll(lines));
  }

  @Test
  public void testToStringEmpty() {
    view = new WorksheetTextualView(emptyFileModel, out);
    assertEquals("", view.toString());
  }

  @Test
  public void testToStringWResize() {
    view = new WorksheetTextualView(readResized, out);
    String resized = "COL 1 500\n"
            + "COL 2 300\n"
            + "COL 5 10\n"
            + "ROW 2 200\n"
            + "ROW 4 300\n"
            + "ROW 6 400";
    assertEquals(resized, view.toString());
  }

  // test addFeatures
  @Test(expected = UnsupportedOperationException.class)
  public void testAddFeatures() {
    view = new WorksheetTextualView(simpleW, out);
    view.addFeatures(new WorksheetController(simpleFileW));
  }
}