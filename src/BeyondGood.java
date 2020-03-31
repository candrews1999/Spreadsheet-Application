import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.cs3500.spreadsheets.controller.WorksheetController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyModel;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetCreator;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.value.Value;
import edu.cs3500.spreadsheets.view.WorksheetEditableView;
import edu.cs3500.spreadsheets.view.WorksheetTableView;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import edu.cs3500.spreadsheets.view.WorksheetView;

/**
 * The main class for our program. Program can either evaluate a cell, run the gui, or save the
 * spreadsheet.
 */
public class BeyondGood {
  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    try {
      switch (args[0]) {
        case "-gui":
          new WorksheetTableView(new ReadOnlyModel<>(new Worksheet())).render();
          return;
        case "-edit":
          WorksheetModel<CellInterface> model = new Worksheet();
          ReadOnlyModel<CellInterface> readModel = new ReadOnlyModel<>(model);
          WorksheetController control = new WorksheetController(model);
          WorksheetView editView = new WorksheetEditableView(readModel);
          control.setView(editView);
          editView.render();
          return;
        default:
          if (args.length > 4 || !args[0].equals("-in")) {
            throw new IllegalArgumentException("Malformed command line arguments.");
          }
      }
      FileReader fileReader = new FileReader(args[1]);
      WorksheetModel<CellInterface> model
              = WorksheetReader.read(new WorksheetCreator(), fileReader);
      ReadOnlyModel<CellInterface> readModel = new ReadOnlyModel<>(model);
      switch (args[2]) {
        case "-eval":
          try {
            Coord toEval = Coord.stringToCoord(args[3]);
            Value val = model.getCellAt(toEval.row, toEval.col).evaluate();
            System.out.append(val.toString());
          } catch (IllegalArgumentException e) {
            System.out.append(e.getMessage());
          }
          break;
        case "-gui":
          new WorksheetTableView(readModel).render();
          break;
        case "-save":
          PrintWriter file = new PrintWriter(args[3]);
          WorksheetTextualView textualView = new WorksheetTextualView(readModel, file);
          textualView.render();
          file.close();
          break;
        case "-edit":
          WorksheetController control = new WorksheetController(model);
          WorksheetView editView = new WorksheetEditableView(readModel);
          control.setView(editView);
          break;
        default:
          throw new IllegalArgumentException("Malformed command line arguments.");
      }
    } catch (IOException e) {
      System.out.append("File not found!");
    }
  }
}