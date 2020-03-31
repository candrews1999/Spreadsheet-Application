package edu.cs3500.spreadsheets.model.cell.value;

import java.util.Objects;

import edu.cs3500.spreadsheets.model.cell.AbstractCellContents;
import edu.cs3500.spreadsheets.model.cell.CellContentsVisitor;

/**
 * A Dbl which is a type of Value that is similar to a Double.
 */
public class Dbl extends AbstractCellContents<Double> implements Value {

  /**
   * A constructor that takes in the content of the Dbl.
   *
   * @param contents the contents of the Value
   */
  public Dbl(double contents) {
    super(contents);
  }

  @Override
  public Value evaluate() {
    return new Dbl(contents);
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
    return visitor.visitDbl(contents);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Dbl)) {
      return false;
    }
    Dbl that = (Dbl) other;
    return Math.abs(that.contents - contents) < 0.0001;
  }

  @Override
  public String toString() {
    return String.format("%f", contents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contents);
  }
}
