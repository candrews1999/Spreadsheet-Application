package edu.cs3500.spreadsheets.model.cell.value;

/**
 * A visitor that evaluates the Value into a Double.
 */
public class NumericValueVisitor implements ValueVisitor<Double> {

  @Override
  public Double visitBool(boolean bool) {
    return 0.0;
  }

  @Override
  public Double visitDbl(double dbl) {
    return dbl;
  }

  @Override
  public Double visitStr(String str) {
    if (str == null) {
      throw new IllegalArgumentException("String can't be null.");
    }
    return 0.0;
  }

  @Override
  public Double visitError(String error) {
    throw new IllegalArgumentException(error);
  }
}
