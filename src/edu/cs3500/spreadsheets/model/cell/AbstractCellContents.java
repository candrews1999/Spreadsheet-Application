package edu.cs3500.spreadsheets.model.cell;

/**
 * An abstract CellContents class for abstracting behavior between CellContents.
 *
 * @param <T> the data types of the CellContents
 */
public abstract class AbstractCellContents<T> implements CellContents {
  protected final T contents;

  /**
   * An abstract constructor for CellContents that takes in the content of the Cell.
   *
   * @param contents the content of the cell
   */
  public AbstractCellContents(T contents) {
    if (contents == null) {
      throw new IllegalArgumentException("Contents can't be null.");
    }
    this.contents = contents;
  }
}
