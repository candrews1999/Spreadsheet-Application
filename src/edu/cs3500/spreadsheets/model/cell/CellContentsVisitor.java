package edu.cs3500.spreadsheets.model.cell;

import java.util.ArrayList;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A visitor that visits different CellContents.
 *
 * @param <R> the return type of each visitor method
 */
public interface CellContentsVisitor<R> {
  /**
   * Process a Value CellContent.
   *
   * @param v the Value
   * @return the desired result
   */
  R visitValue(Value v);

  /**
   * Process a Reference CellContent's referenced Cells.
   *
   * @param refs the Referenced Coords
   * @return the desired result
   */
  R visitReference(ArrayList<Coord> refs);

  /**
   * Process a Formula CellContent's apply list.
   *
   * @param toApply the list of CellContents of the Formula
   * @return the desired result
   */
  R visitFormula(ArrayList<CellContents> toApply);

  /**
   * Process a ColumnReference's referenced columns.
   *
   * @param refs the referenced columns'
   * @return the desired result
   */
  R visitColumnReference(ArrayList<Integer> refs);
}
