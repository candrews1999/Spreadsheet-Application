package edu.cs3500.spreadsheets.model.cell;

import java.util.ArrayList;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A CellContents Visitor that sees if the CellContent is a Reference.
 */
public class IsReferenceVisitor implements CellContentsVisitor<ArrayList<Coord>> {
  @Override
  public ArrayList<Coord> visitValue(Value v) {
    return null;
  }

  @Override
  public ArrayList<Coord> visitReference(ArrayList<Coord> refs) {
    return new ArrayList<>(refs);
  }

  @Override
  public ArrayList<Coord> visitFormula(ArrayList<CellContents> toApply) {
    return null;
  }

  @Override
  public ArrayList<Coord> visitColumnReference(ArrayList<Integer> refs) {
    return null;
  }
}
