package edu.cs3500.spreadsheets.model.cell;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * An interface for an individual Cell in a Worksheet. All Cells have a Coord, original String, and
 * can be evaluated.
 */
public interface CellInterface {

  /**
   * Returns the CellContents of the Cell.
   *
   * @return the Cell's contents
   */
  CellContents getContents();

  /**
   * Returns the Coord of this Cell.
   *
   * @return the Cell's Coord
   */
  Coord getCoord();

  /**
   * Returns the original String of this Cell.
   *
   * @return the contents of the Cell in String form
   */
  String getOriginal();

  /**
   * Returns the evaluation of the Cell.
   *
   * @return the Cell's Value
   */
  Value evaluate();
}
