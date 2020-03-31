package edu.cs3500.spreadsheets.model;

import java.util.HashMap;
import java.util.List;

/**
 * A ReadOnlyModel is a WorksheetModel that can't be modified and can only return data about a given
 * model.
 */
public class ReadOnlyModel<T> implements WorksheetModel<T> {
  private final WorksheetModel<T> model;

  /**
   * A constructor that takes in the model the ReadOnlyModel is based on.
   *
   * @param model the model to be used
   */
  public ReadOnlyModel(WorksheetModel<T> model) {
    this.model = model;
  }

  @Override
  public T getCellAt(int row, int col) {
    return model.getCellAt(row, col);
  }

  @Override
  public int numRows() {
    return model.numRows();
  }

  @Override
  public int numCols() {
    return model.numCols();
  }

  @Override
  public void setCell(int row, int col, String contents) {
    // read only model can't add any Cells
  }

  @Override
  public List<T> getAllCells() {
    return model.getAllCells();
  }

  @Override
  public void deleteCell(int row, int col) {
    // read only model can't delete any Cells
  }

  @Override
  public int getColWidth(int cellWidthPreset, int col) {
    return model.getColWidth(cellWidthPreset, col);
  }

  @Override
  public int getLeftXOfCol(int cellWidthPreset, int col) {
    return model.getLeftXOfCol(cellWidthPreset, col);
  }

  @Override
  public void addResizedCol(int cellWidthPreset, int col, int newSize) {
    // read only model can't change col size
  }

  @Override
  public HashMap<Integer, Integer> getResizedCols() {
    return model.getResizedCols();
  }

  @Override
  public int getRowHeight(int cellHeightPreset, int row) {
    return model.getRowHeight(cellHeightPreset, row);
  }

  @Override
  public int getUpperYOfRow(int cellHeightPreset, int row) {
    return model.getUpperYOfRow(cellHeightPreset, row);
  }

  @Override
  public void addResizedRow(int cellHeightPreset, int row, int newSize) {
    // read only model can't add a resized row
  }

  @Override
  public HashMap<Integer, Integer> getResizedRows() {
    return model.getResizedRows();
  }
}