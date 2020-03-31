package edu.cs3500.spreadsheets.model.cell.value;

/**
 * A visitor that checks if the Value is a Dbl.
 */
public class IsDblVisitor implements ValueVisitor<Boolean> {

  @Override
  public Boolean visitBool(boolean bool) {
    return false;
  }

  @Override
  public Boolean visitDbl(double dbl) {
    return true;
  }

  @Override
  public Boolean visitStr(String str) {
    if (str == null) {
      throw new IllegalArgumentException("String can't be null.");
    }
    return false;
  }

  @Override
  public Boolean visitError(String error) {
    throw new IllegalArgumentException(error);
  }
}