package edu.cs3500.spreadsheets.model;

import java.util.HashMap;
import java.util.List;

/**
 * A model interface for a worksheet document. A Worksheet has a type of Cell that is used to
 * represent a Cell in a typical Excel spreadsheet. Each Cell can hold data and be evaluated into a
 * Value type.
 */
public interface WorksheetModel<T> {

  /**
   * Gets the cell at the given row and column.
   *
   * @param row the row of the cell (1-indexed)
   * @param col the column of the cell (1-indexed)
   * @return the cell at the given location or null if the location is blank
   */
  T getCellAt(int row, int col);

  /**
   * The number of rows that have Cells in the worksheet.
   *
   * @return the number of rows in the sheet
   */
  int numRows();

  /**
   * The number of columns that have Cells in the worksheet.
   *
   * @return the number of columns in the sheet
   */
  int numCols();

  /**
   * Sets a cell with the given contents at the given location.
   *
   * @param row      the row to add the cell to (1-indexed)
   * @param col      the column to add the cell to (1-indexed)
   * @param contents the contents of the cell
   * @throws IllegalArgumentException if the content is malformed
   */
  void setCell(int row, int col, String contents);

  /**
   * Returns all the cells in the worksheet that have been filled.
   *
   * @return the cells in the sheet
   */
  List<T> getAllCells();

  /**
   * Deletes a Cell at the given location.
   *
   * @param row the row of the Cell to delete (1-indexed)
   * @param col the column of the Cell to delete (1-indexed)
   */
  void deleteCell(int row, int col);

  /**
   * Gets the column width for the given column. If the column has not been resized, preset will be
   * returned.
   * @param cellWidthPreset the default cell width
   * @param col the column in question
   * @return the column width
   */
  int getColWidth(int cellWidthPreset, int col);

  /**
   * Gets the left most x coord of the given column.
   * @param cellWidthPreset the default cell width
   * @param col the column in question
   * @return the leftmost x coord of the column
   */
  int getLeftXOfCol(int cellWidthPreset, int col);

  /**
   * Adds a new custom resize to the record of column resizes. If the newSize is the same as the
   * preset, the entry associated with the column will be deleted.
   * @param cellWidthPreset the default cell width
   * @param col the column in question
   * @param newSize the new size to saved
   */
  void addResizedCol(int cellWidthPreset, int col, int newSize);

  /**
   * Gets the resized Column entries for the model.
   * @return the resized columns as a HashMap.
   */
  HashMap<Integer, Integer> getResizedCols();

  /**
   * Gets the row height for the given row. If the row has not been resized, preset will be
   * returned.
   * @param cellHeightPreset the default cell Height
   * @param row the row in question
   * @return the row height
   */
  int getRowHeight(int cellHeightPreset, int row);

  /**
   * Gets the upper most y coord of the given row.
   * @param cellHeightPreset the default cell Height
   * @param row the row in question
   * @return the upper most y coord of the row
   */
  int getUpperYOfRow(int cellHeightPreset, int row);

  /**
   * Adds a new custom resize to the record of row resizes. If the newSize is the same as the
   * preset, the entry associated with the row will be deleted.
   * @param cellHeightPreset the default cell Height
   * @param row the row in question
   * @param newSize the new size to saved
   */
  void addResizedRow(int cellHeightPreset, int row, int newSize);

  /**
   * Gets the resized Row entries for the model.
   * @return the resized rows as a HashMap.
   */
  HashMap<Integer, Integer> getResizedRows();
}
