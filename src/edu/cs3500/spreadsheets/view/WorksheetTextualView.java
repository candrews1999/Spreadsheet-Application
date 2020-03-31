package edu.cs3500.spreadsheets.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.ReadOnlyModel;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellInterface;

/**
 * A TextualView that renders the cells in a Worksheet as a text file. Each cell would be its
 * coordinate and its original String with line breaks between each cell.
 */
public class WorksheetTextualView implements WorksheetView {
  private final Appendable out;
  private final WorksheetModel<CellInterface> model;

  /**
   * Constructor for a WorksheetTextualView which takes in a model and an Appendable.
   *
   * @param model the model that the view is based off of
   * @param out   the Appendable that the view writes to
   * @throws IllegalArgumentException if the model or Appendable are null
   */
  public WorksheetTextualView(ReadOnlyModel<CellInterface> model, Appendable out) {
    if (model == null || out == null) {
      throw new IllegalArgumentException("Model and appendable can't be null");
    }
    this.model = model;
    this.out = out;
  }

  @Override
  public void render() throws IOException {
    out.append(toString());
  }

  @Override
  public void addFeatures(Features features) {
    throw new UnsupportedOperationException("Can't add features to textual view.");
  }

  @Override
  public String toString() {
    List<?> filled = model.getAllCells();
    StringBuilder toReturn = new StringBuilder();
    for (Object o : filled) {
      toReturn.append(o.toString()).append("\n");
    }
    HashMap<Integer, Integer> resizedCols = model.getResizedCols();
    for (Map.Entry<Integer, Integer> cols : resizedCols.entrySet()) {
      toReturn.append("COL ")
              .append(cols.getKey()).append(" ").append(cols.getValue()).append("\n");
    }
    HashMap<Integer, Integer> resizedRows = model.getResizedRows();
    int i = 0;
    for (Map.Entry<Integer, Integer> rows : resizedRows.entrySet()) {
      toReturn.append("ROW ")
              .append(rows.getKey()).append(" ").append(rows.getValue());
      if (i < resizedRows.size() - 1) {
        toReturn.append("\n");
      }
      i++;
    }
    return toReturn.toString();
  }
}
