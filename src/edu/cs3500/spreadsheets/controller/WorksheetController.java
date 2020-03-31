package edu.cs3500.spreadsheets.controller;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyModel;
import edu.cs3500.spreadsheets.model.WorksheetCreator;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.view.WorksheetEditableView;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import edu.cs3500.spreadsheets.view.WorksheetView;

/**
 * A controller that connects a model and view by sending information between a WorksheetView and a
 * WorksheetModel.
 */
public class WorksheetController implements Features {
  private WorksheetModel<CellInterface> model;

  /**
   * A constructor that takes in an editable model that the controller controls and uses to
   * communicate with the view.
   *
   * @param model the model the controller is based on
   */
  public WorksheetController(WorksheetModel<CellInterface> model) {
    this.model = model;
  }

  /**
   * Sets the view and gives it features.
   *
   * @param view the view to set
   */
  public void setView(WorksheetView view) throws IOException {
    view.addFeatures(this);
    view.render();
  }

  @Override
  public void confirmChange(Coord c, String newContent) {
    model.setCell(c.row, c.col, newContent);
  }

  @Override
  public void deleteCell(Coord c) {
    model.deleteCell(c.row, c.col);
  }

  @Override
  public void saveModel(String name) throws IOException {
    PrintWriter file = new PrintWriter(name);
    WorksheetTextualView saved = new WorksheetTextualView(new ReadOnlyModel<>(model), file);
    saved.render();
    file.close();
  }

  @Override
  public void loadModel(String name) throws IOException {
    FileReader read = new FileReader(name);
    WorksheetModel<CellInterface> model = WorksheetReader.read(new WorksheetCreator(), read);
    ReadOnlyModel<CellInterface> readModel = new ReadOnlyModel<>(model);
    WorksheetController control = new WorksheetController(model);
    WorksheetView editView = new WorksheetEditableView(readModel);
    control.setView(editView);
  }

  @Override
  public void resizeCol(int presetColWidth, int col, int newSize) {
    model.addResizedCol(presetColWidth, col, newSize);
  }

  @Override
  public void resizeRow(int presetRowHeight, int row, int newSize) {
    model.addResizedRow(presetRowHeight, row, newSize);
  }
}
