package edu.cs3500.spreadsheets.model.cell;

import java.util.ArrayList;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A Reference class which represents when a Cell's contents refers to another Cell.
 */
public class Reference extends AbstractCellContents<ArrayList<Coord>> {
  private final WorksheetModel<CellInterface> model;

  /**
   * A constructor which holds the Cells this references.
   *
   * @param reference the Coords that this references
   * @throws IllegalArgumentException if the reference is null
   */
  public Reference(ArrayList<Coord> reference, WorksheetModel<CellInterface> model) {
    super(reference);
    this.model = model;
  }

  @Override
  public Value evaluate() {
    // In Excel, doing = (A1:A5) creates a reference that evaluates to what the first cell (A1)
    // evaluates to, so we implement the same behavior having the Reference's evaluation be the same
    // as the evaluation of the first Cell it references.
    Coord first = contents.get(0);
    CellInterface c = model.getCellAt(first.row, first.col);
    return c.evaluate();
  }

  @Override
  public <R> R accept(CellContentsVisitor<R> visitor) {
    return visitor.visitReference(contents);
  }

  public WorksheetModel<CellInterface> getModel() {
    return model;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Reference)) {
      return false;
    }
    Reference that = (Reference) other;
    return this.contents.equals(that.contents);
  }

  @Override
  public String toString() {
    StringBuilder soFar = new StringBuilder();
    for (Coord c : contents) {
      soFar.append(c.toString()).append(" ");
    }
    return soFar.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.contents);
  }
}
