package edu.cs3500.spreadsheets.controller;

import java.io.IOException;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A Feature interface that sends information between a WorksheetView to a WorksheetModel.
 */
public interface Features {

  /**
   * Allows a user to edit a Cell at the given Coord with the String newContent as the new original
   * String of the Cell.
   *
   * @param c          the Coord of the Cell the user wants to modify
   * @param newContent the new String of the Cell
   */
  void confirmChange(Coord c, String newContent);

  /**
   * Allows a user to delete the contents of a Cell at the given Coord.
   *
   * @param c the Coord of the Cell the user wants to delete
   */
  void deleteCell(Coord c);

  /**
   * Allows a user to save the current model that they are editing.
   *
   * @param name what the user wants to name the saved file
   */
  void saveModel(String name) throws IOException;

  /**
   * Allows a user to load a worksheet to edit.
   * @param name the name of the worksheet text file
   * @throws IOException if the file isn't found
   */
  void loadModel(String name) throws IOException;

  /**
   * Allows the user to resize a column.
   * @param presetColWidth the default column width
   * @param col the column to be resized
   * @param newSize the new size for the column
   */
  void resizeCol(int presetColWidth, int col, int newSize);

  /**
   * Allows the user to resize a row.
   * @param presetRowHeight the default row height
   * @param row the row to be resized
   * @param newSize the new size for the row
   */
  void resizeRow(int presetRowHeight, int row, int newSize);
}
