package edu.cs3500.spreadsheets.model.cell.value;

/**
 * An abstracted function object for processing any {@link Value}.
 *
 * @param <R> the return type of this function
 */
public interface ValueVisitor<R> {

  /**
   * Visits a Bool Value.
   *
   * @param bool the value
   * @return the desired result
   */
  R visitBool(boolean bool);

  /**
   * Visits a Dbl Value.
   *
   * @param dbl the value
   * @return the desired result
   */
  R visitDbl(double dbl);

  /**
   * Visits a Str Value.
   *
   * @param str the value
   * @return the desired result
   * @throws IllegalArgumentException if the String is null
   */
  R visitStr(String str);

  /**
   * Visits an ErrorVal  Value.
   *
   * @param error the value
   * @return the desired result
   */
  R visitError(String error);
}
