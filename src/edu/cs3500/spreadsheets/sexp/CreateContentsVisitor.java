package edu.cs3500.spreadsheets.sexp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.ColumnReference;
import edu.cs3500.spreadsheets.model.cell.CyclicalContentVisitor;
import edu.cs3500.spreadsheets.model.cell.Formula;
import edu.cs3500.spreadsheets.model.cell.Reference;
import edu.cs3500.spreadsheets.model.cell.function.Compare;
import edu.cs3500.spreadsheets.model.cell.function.Function;
import edu.cs3500.spreadsheets.model.cell.function.Product;
import edu.cs3500.spreadsheets.model.cell.function.RemoveSpace;
import edu.cs3500.spreadsheets.model.cell.function.Sum;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Str;

/**
 * S-exp visitor that returns each S-exp as a CellContents.
 */
public class CreateContentsVisitor implements SexpVisitor<CellContents> {
  private WorksheetModel<CellInterface> model;
  private int row;
  private int col;

  /**
   * A CreateContentsVisitor constructor that takes in a Worksheet.
   *
   * @param model the Worksheet model
   */
  public CreateContentsVisitor(WorksheetModel<CellInterface> model, int row, int col) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null.");
    }
    this.model = model;
    this.row = row;
    this.col = col;
  }

  @Override
  public CellContents visitBoolean(boolean b) {
    return new Bool(b);
  }

  @Override
  public CellContents visitNumber(double d) {
    return new Dbl(d);
  }

  @Override
  public CellContents visitSList(List<Sexp> l) {
    Sexp first = l.get(0);
    Function func;
    if (first.accept(new IsSymbolVisitor())) {
      func = getFunction(first.toString());
    } else {
      throw new IllegalArgumentException("First of a formula needs to be a function.");
    }
    List<Sexp> lCopy = new ArrayList<>(l);
    lCopy.remove(0);
    List<CellContents> toAdd = new ArrayList<>();
    for (Sexp exp : lCopy) {
      CellContents content = exp.accept(this);
      toAdd.add(content);
    }
    return new Formula(toAdd, func);
  }

  private Function getFunction(String s) {
    switch (s) {
      case "SUM":
        return new Sum(model);
      case "PRODUCT":
        return new Product(model);
      case "<":
        return new Compare();
      case "REMOVESPACE":
        return new RemoveSpace();
      default:
        throw new IllegalArgumentException("Not a valid function symbol.");
    }
  }

  @Override
  public CellContents visitSymbol(String s) {
    if (s.contains(":")) {
      int colonIndex = s.indexOf(":");
      String first = s.substring(0, colonIndex);
      String second = s.substring(colonIndex + 1);
      Integer firstColRef = findColumnRef(first);
      Integer secondColRef = findColumnRef(second);
      if (firstColRef == null && secondColRef == null) {
        Coord firstCell = findCellRef(first);
        Coord secondCell = findCellRef(second);
        ArrayList<Coord> references = getAreaRef(firstCell, secondCell);
        return new Reference(references, model);
      } else if (firstColRef == null || secondColRef == null) {
        throw new IllegalArgumentException("Malformed column reference.");
      } else {
        ArrayList<Integer> referencedCols = getColumnRefs(firstColRef, secondColRef);
        return new ColumnReference(referencedCols, model);
      }
    } else {
      Coord firstCell = findCellRef(s);
      ArrayList<Coord> references = new ArrayList<>(Collections.singletonList(firstCell));
      return new Reference(references, model);
    }
  }

  private Integer findColumnRef(String s) {
    if (!s.matches(".*\\d.*")) {
      for (int i = 0; i < s.length(); i++) {
        if (!Character.isUpperCase(s.charAt(i))) {
          return null;
        }
      }
      int col = Coord.colNameToIndex(s);
      if (this.col == col) {
        throw new IllegalArgumentException("Can't have cyclical references.");
      } else {
        return Coord.colNameToIndex(s);
      }
    }
    return null;
  }

  private ArrayList<Integer> getColumnRefs(Integer first, Integer second) {
    ArrayList<Integer> toReturn = new ArrayList<>();
    if (second < first) {
      throw new IllegalArgumentException("Second column has to be greater than the first");
    }
    for (int i = first; i <= second; i++) {
      // will throw an error if there are cycles
      checkColCycles(i);
      toReturn.add(i);
    }
    return toReturn;
  }

  private void checkColCycles(int col) {
    List<CellInterface> filled = model.getAllCells();
    for (CellInterface c : filled) {
      Coord coord = c.getCoord();
      if (coord.col == col
              && c.getContents().accept(new CyclicalContentVisitor(this.row, this.col, model))) {
        throw new IllegalArgumentException("Can't have cyclical references.");
      }
    }
  }

  private Coord findCellRef(String s) {
    Coord cellRef = Coord.stringToCoord(s);
    int col = cellRef.col;
    int row = cellRef.row;
    // check if ref is cyclical to the cell itself
    if (this.row == row && this.col == col) {
      throw new IllegalArgumentException("Can't have cyclical references.");
    }
    // check for inner cyclical references
    CellInterface check = model.getCellAt(row, col);
    // if there is no contents at that cell, return get Cell
    if (check.getOriginal().equals("")) {
      return cellRef;
    }
    if (check.getContents().accept(new CyclicalContentVisitor(this.row, this.col, model))) {
      throw new IllegalArgumentException("Can't have cyclical references.");
    }
    return cellRef;
  }


  private ArrayList<Coord> getAreaRef(Coord first, Coord second) {
    int firstRow = first.row;
    int firstCol = first.col;
    int secondRow = second.row;
    int secondCol = second.col;
    ArrayList<Coord> referenced = new ArrayList<>();
    if (secondRow < firstRow || secondCol < firstCol) {
      throw new IllegalArgumentException(
              "Second cell has to have a row and col greater than the first cell");
    }
    for (int i = firstRow; i <= secondRow; i++) {
      for (int j = firstCol; j <= secondCol; j++) {
        referenced.add(new Coord(j, i));
      }
    }
    return referenced;
  }

  @Override
  public CellContents visitString(String s) {
    return new Str(s);
  }
}
