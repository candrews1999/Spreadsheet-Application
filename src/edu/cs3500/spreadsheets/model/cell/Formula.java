package edu.cs3500.spreadsheets.model.cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.cell.function.Function;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A Formula class that represents a function being carried out on CellContents.
 */
public class Formula extends AbstractCellContents<List<CellContents>> {
  private final Function func;

  /**
   * A constructor for Formula that takes in the CellContents in the Formula and the Function to be
   * applied.
   *
   * @param toApply the CellContents in the formula
   * @param func    the Function to apply to the CellContents
   * @throws IllegalArgumentException if the function or list are null
   */
  public Formula(List<CellContents> toApply, Function func) {
    super(toApply);
    if (toApply.size() == 0) {
      throw new IllegalArgumentException("Can't have function with no arguments.");
    }
    if (func == null) {
      throw new IllegalArgumentException("Function can't be null.");
    }
    this.func = func;
  }

  @Override
  public Value evaluate() {
    return func.apply(this.contents);
  }

  @Override
  public <R> R accept(CellContentsVisitor<R> visitor) {
    return visitor.visitFormula(new ArrayList<>(this.contents));
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Formula)) {
      return false;
    }
    Formula that = (Formula) other;
    return this.contents.equals(that.contents) && this.func.equals(that.func);
  }

  @Override
  public String toString() {
    return func.toString() + ": " + this.contents.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.contents, this.func);
  }
}
