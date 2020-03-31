package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * A value type representing coordinates in a {@link Worksheet}.
 */
public class Coord {
  public final int row;
  public final int col;

  /**
   * A Coord constructor represents a col and row.
   *
   * @param col the column
   * @param row the row
   */
  public Coord(int col, int row) {
    if (row < 1 || col < 1) {
      throw new IllegalArgumentException("Coordinates should be strictly positive");
    }
    this.row = row;
    this.col = col;
  }

  /**
   * Converts from the A-Z column naming system to a 1-indexed numeric value.
   *
   * @param name the column name
   * @return the corresponding column index
   */
  public static int colNameToIndex(String name) {
    name = name.toUpperCase();
    int ans = 0;
    for (int i = 0; i < name.length(); i++) {
      ans *= 26;
      ans += (name.charAt(i) - 'A' + 1);
    }
    return ans;
  }

  /**
   * Converts a 1-based column index into the A-Z column naming system.
   *
   * @param index the column index
   * @return the corresponding column name
   */
  public static String colIndexToName(int index) {
    StringBuilder ans = new StringBuilder();
    while (index > 0) {
      int colNum = (index - 1) % 26;
      ans.insert(0, Character.toChars('A' + colNum));
      index = (index - colNum) / 26;
    }
    return ans.toString();
  }

  /**
   * Converts a String into a Coord.
   *
   * @param evalCell the String representing a Coord
   * @return the Coord with the String's row and column
   */
  public static Coord stringToCoord(String evalCell) {
    if (evalCell == null || evalCell.length() == 0) {
      throw new IllegalArgumentException("Can't have empty String.");
    }
    boolean endOfLetters = false;
    String columnName = "";
    StringBuilder rowNumString = new StringBuilder();
    for (int i = 0; i < evalCell.length(); i++) {
      char curr = evalCell.charAt(i);
      if (Character.isDigit(curr) && i != 0) {
        endOfLetters = true;
        rowNumString.append(curr);
      } else if (Character.isLetter(curr) && !endOfLetters && Character.isUpperCase(curr)) {
        columnName += curr;
      } else {
        throw new IllegalArgumentException("Malformed cell.");
      }
    }
    int col = Coord.colNameToIndex(columnName);
    int row = Integer.parseInt(rowNumString.toString());
    return new Coord(col, row);
  }

  @Override
  public String toString() {
    return colIndexToName(this.col) + this.row;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coord coord = (Coord) o;
    return row == coord.row
            && col == coord.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }
}
