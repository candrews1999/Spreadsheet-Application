package edu.cs3500.spreadsheets.model.cell;

import java.util.ArrayList;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A CellContent visitor that checks if the CellContent is a column reference. If it is, returns the
 * referenced columns otherwise returns null.
 *
 * @param <R> the referenced columns
 */
public class IsColReferenceVisitor<R> implements CellContentsVisitor<ArrayList<Integer>> {
  @Override
  public ArrayList<Integer> visitValue(Value v) {
    return null;
  }

  @Override
  public ArrayList<Integer> visitReference(ArrayList<Coord> refs) {
    return null;
  }

  @Override
  public ArrayList<Integer> visitFormula(ArrayList<CellContents> toApply) {
    return null;
  }

  @Override
  public ArrayList<Integer> visitColumnReference(ArrayList<Integer> refs) {
    return new ArrayList<>(refs);
  }
}
