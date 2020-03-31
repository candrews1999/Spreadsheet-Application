package edu.cs3500.spreadsheets.model.cell.value;

import java.util.Objects;

import edu.cs3500.spreadsheets.model.cell.AbstractCellContents;
import edu.cs3500.spreadsheets.model.cell.CellContentsVisitor;

/**
 * A Bool which is a type of Value that is similar to a Boolean.
 */
public class Bool extends AbstractCellContents<Boolean> implements Value {

  /**
   * A constructor that takes in the contents of the Bool.
   *
   * @param content the Boolean value
   */
  public Bool(boolean content) {
    super(content);
  }

  @Override
  public Value evaluate() {
    return new Bool(contents);
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
    return visitor.visitBool(contents);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Bool)) {
      return false;
    }
    Bool that = (Bool) other;
    return that.contents == contents;
  }

  @Override
  public String toString() {
    return Boolean.toString(contents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contents);
  }
}
