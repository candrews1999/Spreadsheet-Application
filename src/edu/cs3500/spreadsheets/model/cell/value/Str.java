package edu.cs3500.spreadsheets.model.cell.value;

import java.util.Objects;

import edu.cs3500.spreadsheets.model.cell.AbstractCellContents;
import edu.cs3500.spreadsheets.model.cell.CellContentsVisitor;

/**
 * A Str which is a type of Value that is similar to a String.
 */
public class Str extends AbstractCellContents<String> implements Value {

  /**
   * A constructor that takes in the content of the Str.
   *
   * @param content the String content
   * @throws IllegalArgumentException if the content is null
   */
  public Str(String content) {
    super(content);
  }

  @Override
  public Value evaluate() {
    return new Str(contents);
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
    return visitor.visitStr(contents);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Str)) {
      return false;
    }
    Str that = (Str) other;
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