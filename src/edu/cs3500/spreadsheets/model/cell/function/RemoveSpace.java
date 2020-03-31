package edu.cs3500.spreadsheets.model.cell.function;

import java.util.List;

import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.value.Str;
import edu.cs3500.spreadsheets.model.cell.value.StringNoSpaceVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A Function object that represents removing white spaces from a CellContent that has to be a Str.
 */
public class RemoveSpace implements Function {
  @Override
  public Value apply(List<CellContents> contents) {
    if (contents.size() != 1) {
      throw new IllegalArgumentException("Can only remove space from one cell.");
    }
    Value first = contents.get(0).evaluate();
    return new Str(first.accept(new StringNoSpaceVisitor()));
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    return other instanceof RemoveSpace;
  }

  @Override
  public String toString() {
    return "RemoveSpace";
  }

  @Override
  public int hashCode() {
    return 2;
  }
}
