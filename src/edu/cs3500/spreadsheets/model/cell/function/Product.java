package edu.cs3500.spreadsheets.model.cell.function;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.IsColReferenceVisitor;
import edu.cs3500.spreadsheets.model.cell.IsReferenceVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.NumericValueVisitor;
import edu.cs3500.spreadsheets.model.cell.value.IsDblVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A Function object that represents multiplying CellContents.
 */
public class Product implements Function {
  private final WorksheetModel<CellInterface> model;

  /**
   * A constructor that takes in the model that the Cells to be multiplied are in.
   *
   * @param model the model that the Cells are in
   */
  public Product(WorksheetModel<CellInterface> model) {
    this.model = model;
  }

  @Override
  public Value apply(List<CellContents> contents) {
    double soFar = 1;
    boolean foundDouble = false;
    for (CellContents cellContent : contents) {
      ArrayList<Coord> refs = cellContent.accept(new IsReferenceVisitor());
      ArrayList<Integer> colRefs = cellContent.accept(new IsColReferenceVisitor<>());
      boolean isDbl = cellContent.evaluate().accept(new IsDblVisitor());
      foundDouble = isDbl || foundDouble;
      if (refs != null) {
        for (Coord c : refs) {
          CellInterface cell = model.getCellAt(c.row, c.col);
          if (cell.getContents() != null) {
            Value refValue = cell.getContents().evaluate();
            boolean refDouble = refValue.accept(new IsDblVisitor());
            if (refDouble) {
              foundDouble = true;
              soFar *= refValue.accept(new NumericValueVisitor());
            }
          }
        }
      } else if (colRefs != null) {
        soFar *= multiplyColReferences(colRefs);
      } else if (isDbl) {
        soFar *= cellContent.evaluate().accept(new NumericValueVisitor());
      }
    }
    if (foundDouble) {
      return new Dbl(soFar);
    } else {
      return new Dbl(0.0);
    }
  }

  private double multiplyColReferences(ArrayList<Integer> referencedColumns) {
    List<CellInterface> filled = model.getAllCells();
    double soFar = 1;
    for (CellInterface c : filled) {
      if (referencedColumns.contains(c.getCoord().col)) {
        Value evaluated = c.evaluate();
        boolean isDbl = evaluated.accept(new IsDblVisitor());
        if (isDbl) {
          soFar *= c.evaluate().accept(new NumericValueVisitor());
        }
      }
    }
    return soFar;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    return other instanceof Product;
  }

  @Override
  public String toString() {
    return "Product";
  }

  @Override
  public int hashCode() {
    return 1;
  }
}
