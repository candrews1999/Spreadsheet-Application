package edu.cs3500.spreadsheets.model.cell;

import java.util.ArrayList;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A CellContent visitor that checks for cyclical references.
 */
public class CyclicalContentVisitor implements CellContentsVisitor<Boolean> {
  private int row;
  private int col;
  private WorksheetModel<CellInterface> model;

  /**
   * A constructor that takes in the row and column of the Cell to check and the model the Cell is
   * from.
   *
   * @param row the row of the Cell
   * @param col the column of the Cell
   */
  public CyclicalContentVisitor(int row, int col, WorksheetModel<CellInterface> model) {
    this.row = row;
    this.col = col;
    this.model = model;
  }

  @Override
  public Boolean visitValue(Value v) {
    return false;
  }

  @Override
  public Boolean visitReference(ArrayList<Coord> refs) {
    for (Coord c : refs) {
      int cCol = c.col;
      int cRow = c.row;
      if ((cCol == col && cRow == row)
              || (model.getCellAt(cRow, cCol).getContents() != null
              && (model.getCellAt(cRow, cCol).getContents().accept(this)))) {
        throw new IllegalArgumentException("Can't have cyclical references.");
      }
    }
    return false;
  }

  @Override
  public Boolean visitFormula(ArrayList<CellContents> toApply) {
    for (CellContents c : toApply) {
      if (c.accept(this)) {
        throw new IllegalArgumentException("Can't have cyclical references.");
      }
    }
    return false;
  }

  @Override
  public Boolean visitColumnReference(ArrayList<Integer> refs) {
    for (Integer i : refs) {
      if (i == col) {
        throw new IllegalArgumentException("Can't have cyclical references.");
      }
    }
    return false;
  }
}
