package edu.cs3500.spreadsheets.model.cell;

import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A CellContents interface which represents the different types of content a Cell can hold.
 */
public interface CellContents {

  /**
   * Evaluates the Cell's contents.
   *
   * @return the cell's evaluated content
   */
  Value evaluate();

  /**
   * Accepts a CellContents visitor.
   *
   * @param visitor the CellContents visitor
   * @param <R>     the return type
   * @return the desired result
   */
  <R> R accept(CellContentsVisitor<R> visitor);
}
