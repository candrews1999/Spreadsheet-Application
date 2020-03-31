package edu.cs3500.spreadsheets.model.cell.value;

/**
 * A Visitor that returns the content of a Value in the form of a String with no white spaces.
 */
public class StringNoSpaceVisitor implements ValueVisitor<String> {

  @Override
  public String visitBool(boolean bool) {
    throw new IllegalArgumentException("Can't remove space from a Boolean.");
  }

  @Override
  public String visitDbl(double dbl) {
    throw new IllegalArgumentException("Can't remove space from a Double.");
  }

  @Override
  public String visitStr(String str) {
    if (str == null) {
      throw new IllegalArgumentException("String can't be null.");
    }
    return str.replaceAll("\\s+", "");
  }

  @Override
  public String visitError(String error) {
    throw new IllegalArgumentException(error);
  }
}
