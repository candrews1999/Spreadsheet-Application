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
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A Function object that represents summing CellContents.
 */
public class Sum implements Function {
  private final WorksheetModel<CellInterface> model;

  /**
   * A constructor that takes in the model that the Cells to be summed are in.
   *
   * @param model the model that the Cells are in
   */
  public Sum(WorksheetModel<CellInterface> model) {
    this.model = model;
  }

  @Override
  public Value apply(List<CellContents> contents) {
    double soFar = 0;
    for (CellContents cellContent : contents) {
      ArrayList<Coord> refs = cellContent.accept(new IsReferenceVisitor());
      ArrayList<Integer> colRefs = cellContent.accept(new IsColReferenceVisitor<>());
      if (refs != null) {
        for (Coord c : refs) {
          CellInterface cell = model.getCellAt(c.row, c.col);
          soFar += cell.evaluate().accept(new NumericValueVisitor());
        }
      } else if (colRefs != null) {
        soFar += sumColReferences(colRefs);
      } else {
        soFar += cellContent.evaluate().accept(new NumericValueVisitor());
      }
    }
    return new Dbl(soFar);
  }

  private double sumColReferences(ArrayList<Integer> referencedColumns) {
    List<CellInterface> filled = model.getAllCells();
    double soFar = 0;
    for (CellInterface c : filled) {
      if (referencedColumns.contains(c.getCoord().col)) {
        soFar += c.evaluate().accept(new NumericValueVisitor());
      }
    }
    return soFar;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    return other instanceof Sum;
  }

  @Override
  public String toString() {
    return "Sum";
  }

  @Override
  public int hashCode() {
    return 3;
  }
}
