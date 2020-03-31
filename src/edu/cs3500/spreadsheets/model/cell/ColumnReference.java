package edu.cs3500.spreadsheets.model.cell;

import java.util.ArrayList;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A class that represents a reference to an entire column in a spreadsheet. A column is represented
 * by its the number of column it is.
 */
public class ColumnReference extends AbstractCellContents<ArrayList<Integer>> {
  private final WorksheetModel<CellInterface> model;

  /**
   * A constructor that takes in the list of columns being referenced. Each column is represented by
   * the number of column it is.
   *
   * @param referenced the columns being referenced
   */
  public ColumnReference(ArrayList<Integer> referenced, WorksheetModel<CellInterface> model) {
    super(referenced);
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null");
    }
    this.model = model;
  }

  @Override
  public Value evaluate() {
    return model.getCellAt(1, contents.get(0)).evaluate();
  }

  @Override
  public <R> R accept(CellContentsVisitor<R> visitor) {
    return visitor.visitColumnReference(new ArrayList<>(this.contents));
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof ColumnReference)) {
      return false;
    }
    ColumnReference that = (ColumnReference) other;
    return this.contents.equals(that.contents);
  }

  @Override
  public String toString() {
    StringBuilder soFar = new StringBuilder();
    for (Integer i : contents) {
      soFar.append(i.toString()).append(" ");
    }
    return soFar.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.contents);
  }
}
