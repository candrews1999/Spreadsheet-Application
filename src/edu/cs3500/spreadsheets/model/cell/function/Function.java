package edu.cs3500.spreadsheets.model.cell.function;

import java.util.List;

import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * An interface that represents a Function object to be applied over Cells.
 */
public interface Function {

  /**
   * Applies the function object.
   *
   * @param contents the CellContents to apply the Function to
   * @return the resulting Value based on the Function
   * @throws IllegalArgumentException if the list is not appropriate for the Function
   */
  Value apply(List<CellContents> contents);
}
