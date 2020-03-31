package edu.cs3500.spreadsheets.model.cell.value;

import edu.cs3500.spreadsheets.model.cell.CellContents;

/**
 * A Value interface which represents CellContents that are Value types.
 */
public interface Value extends CellContents {

  /**
   * Accepts a ValueVisitor.
   *
   * @param visitor the visitor type
   * @return the desired result
   * @throws IllegalArgumentException if the visitor is null
   */
  <R> R accept(ValueVisitor<R> visitor);
}