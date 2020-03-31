package edu.cs3500.spreadsheets.model.cell.function;

import java.util.List;

import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.NumericValueVisitor;
import edu.cs3500.spreadsheets.model.cell.value.IsDblVisitor;
import edu.cs3500.spreadsheets.model.cell.value.Value;

/**
 * A Function object that compares CellContents whose evaluations are numbers.
 */
public class Compare implements Function {
  @Override
  public Value apply(List<CellContents> contents) {
    if (contents.size() != 2) {
      throw new IllegalArgumentException("Can only compare over two arguments.");
    }
    Value firstContent = contents.get(0).evaluate();
    Value secondContent = contents.get(1).evaluate();
    if (firstContent.accept(new IsDblVisitor()) && secondContent.accept(new IsDblVisitor())) {
      double diff = firstContent.accept(new NumericValueVisitor())
              - secondContent.accept(new NumericValueVisitor());
      return new Bool(diff < 0.0);
    } else {
      throw new IllegalArgumentException("Both cell contents need to contain numeric values.");
    }
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    return other instanceof Compare;
  }

  @Override
  public String toString() {
    return "Compare";
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
