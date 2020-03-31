package edu.cs3500.spreadsheets.model.cell;

import java.util.Objects;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * An individual Cell in a Worksheet.
 */
public final class Cell implements CellInterface {
  private final CellContents contents;
  private final String original;
  private final Coord coord;

  /**
   * Constructor for an individual spreadsheet cell. Each cell has its Coord, CellContents, and
   * original String.
   *
   * @param coord    the Coord of the Cell
   * @param contents the contents of the Cell or null if the Cell is blank
   * @param original the original String of the Cell
   * @throws IllegalArgumentException if the Coord or original String are null
   */
  public Cell(Coord coord, CellContents contents, String original) {
    if (coord == null || original == null) {
      throw new IllegalArgumentException("Coord and original String can't be null.");
    }
    this.contents = contents;
    this.original = original;
    this.coord = coord;
  }

  @Override
  public CellContents getContents() {
    return this.contents;
  }

  @Override
  public Coord getCoord() {
    return this.coord;
  }

  @Override
  public String getOriginal() {
    return this.original;
  }

  @Override
  public Value evaluate() {
    try {
      if (contents != null) {
        return this.contents.evaluate();
      } else {
        return new Dbl(0.0);
      }
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Error in cell " + coord + ": " + e.getMessage());
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Cell)) {
      return false;
    }
    Cell that = (Cell) other;
    boolean sameCoord = that.coord.equals(coord);
    // special cases for instance where contents is null because Cell is blank
    if (contents == null && that.contents == null) {
      return sameCoord;
    }
    if (contents == null || that.contents == null) {
      return false;
    }
    return sameCoord && that.contents.equals(contents);
  }

  @Override
  public String toString() {
    return coord.toString() + " " + this.original;
  }

  @Override
  public int hashCode() {
    return Objects.hash(coord, contents, original);
  }
}