package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.ErrorVal;
import edu.cs3500.spreadsheets.model.cell.value.Str;
import edu.cs3500.spreadsheets.sexp.CreateContentsVisitor;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;

/**
 * An implementation of WorksheetModel used to hold content within Cells and support basic
 * functionality of an Excel worksheet.
 */
public class Worksheet implements WorksheetModel<CellInterface> {
  private HashMap<Coord, Cell> filled;
  // First Integer = col number     Second Integer = size
  private HashMap<Integer, Integer> resizedCols;
  private HashMap<Integer, Integer> resizedRows;

  /**
   * Constructor for a Worksheet which initializes the list of filled cells to empty.
   */
  public Worksheet() {
    this.filled = new HashMap<>();
    this.resizedCols = new HashMap<>();
    this.resizedRows = new HashMap<>();
  }

  @Override
  public Cell getCellAt(int row, int col) {
    Coord check = new Coord(col, row);
    Cell toReturn = filled.get(check);
    if (toReturn != null) {
      return toReturn;
    }
    return new Cell(check, null, "");
  }

  @Override
  public int numRows() {
    int max = 0;
    for (Map.Entry<Coord, Cell> entry : filled.entrySet()) {
      int row = entry.getKey().row;
      if (row > max) {
        max = row;
      }
    }
    return max;
  }

  @Override
  public int numCols() {
    int max = 0;
    for (Map.Entry<Coord, Cell> entry : filled.entrySet()) {
      int col = entry.getKey().col;
      if (col > max) {
        max = col;
      }
    }
    return max;
  }

  @Override
  public void setCell(int row, int col, String original) {
    Coord coord = new Coord(col, row);
    CellContents contents = parseToContents(coord, original);
    Cell toAdd = new Cell(coord, contents, original);
    filled.put(coord, toAdd);
  }

  private CellContents parseToContents(Coord coord, String original) {
    if (original.length() > 0) {
      Character first = original.charAt(0);
      CellContents cellC;
      if (first.equals('=')) {
        try {
          Sexp parsed = Parser.parse(original.substring(1));
          cellC = parsed.accept(new CreateContentsVisitor(this, coord.row, coord.col));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
          String message = "Error in cell " + coord + ": " + e.getMessage();
          cellC = new ErrorVal(message);
        }
      } else {
        try {
          double num = Double.parseDouble(original);
          cellC = new Dbl(num);
        } catch (NumberFormatException e) {
          if (original.equals("false")) {
            cellC = new Bool(false);
          } else if (original.equals("true")) {
            cellC = new Bool(true);
          } else {
            cellC = new Str(original);
          }
        }
      }
      return cellC;
    } else {
      return new Str(original);
    }
  }

  @Override
  public List<CellInterface> getAllCells() {
    ArrayList<CellInterface> toReturn = new ArrayList<>();
    for (Map.Entry<Coord, Cell> entry : filled.entrySet()) {
      toReturn.add(entry.getValue());
    }
    return toReturn;
  }

  @Override
  public void deleteCell(int row, int col) {
    filled.remove(new Coord(col, row));
  }

  @Override
  public void addResizedCol(int cellWidthPreset, int col, int newSize) {
    // unnecessary to store if newSize is preset
    if (cellWidthPreset == newSize) {
      resizedCols.remove(col);
    }
    this.resizedCols.put(col, newSize);
  }

  @Override
  public int getColWidth(int cellWidthPreset, int col) {
    Integer colSize = this.resizedCols.get(col);
    if (colSize != null) {
      return colSize;
    } else {
      return cellWidthPreset;
    }
  }

  @Override
  public int getLeftXOfCol(int cellWidthPreset, int col) {
    int widthTraveledSoFar = 0;
    for (int i = 1; i < col; i++) {
      int colWidth = this.getColWidth(cellWidthPreset, i);
      widthTraveledSoFar += colWidth;
    }
    return widthTraveledSoFar;
  }

  @Override
  public HashMap<Integer, Integer> getResizedCols() {
    HashMap<Integer, Integer> toReturn = new HashMap<>();
    for (Map.Entry<Integer, Integer> entry : resizedCols.entrySet()) {
      int col = entry.getKey();
      int size = entry.getValue();
      toReturn.put(col, size);
    }
    return toReturn;
  }

  @Override
  public int getRowHeight(int cellHeightPreset, int row) {
    Integer rowHeight = this.getResizedRows().get(row);
    if (rowHeight != null) {
      return rowHeight;
    } else {
      return cellHeightPreset;
    }
  }

  @Override
  public int getUpperYOfRow(int cellHeightPreset, int row) {
    int heightTraveledSoFar = 0;
    for (int i = 1; i < row; i++) {
      int rowHeight = this.getRowHeight(cellHeightPreset, i);
      heightTraveledSoFar += rowHeight;
    }
    return heightTraveledSoFar;
  }

  @Override
  public void addResizedRow(int cellHeightPreset, int row, int newSize) {
    // unnecessary to store if newSize is preset
    if (cellHeightPreset == newSize) {
      resizedRows.remove(row);
    }
    this.resizedRows.put(row, newSize);
  }

  @Override
  public HashMap<Integer, Integer> getResizedRows() {
    HashMap<Integer, Integer> toReturn = new HashMap<>();
    for (Map.Entry<Integer, Integer> entry : resizedRows.entrySet()) {
      int row = entry.getKey();
      int size = entry.getValue();
      toReturn.put(row, size);
    }
    return toReturn;
  }
}