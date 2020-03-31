package edu.cs3500.spreadsheets.model.cell.value;

import java.util.Objects;

import edu.cs3500.spreadsheets.model.cell.AbstractCellContents;
import edu.cs3500.spreadsheets.model.cell.CellContentsVisitor;

/**
 * An Error Value that represents when a Cell has an error.
 */
public class ErrorVal extends AbstractCellContents<String> implements Value {

  /**
   * A constructor that takes in the error message.
   *
   * @param message the specific error message of the ErrorVal
   */
  public ErrorVal(String message) {
    super(message);
  }

  @Override
  public Value evaluate() {
    return new ErrorVal(contents);
  }

  @Override
  public <R> R accept(CellContentsVisitor<R> visitor) {
    return visitor.visitValue(this);
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    if (visitor == null) {
      throw new IllegalArgumentException("Visitor can't be null.");
    }
    return visitor.visitError(this.contents);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof ErrorVal)) {
      return false;
    }
    ErrorVal that = (ErrorVal) other;
    return that.contents.equals(contents);
  }

  @Override
  public String toString() {
    return contents;
  }

  @Override
  public int hashCode() {
    return Objects.hash(contents);
  }
}
