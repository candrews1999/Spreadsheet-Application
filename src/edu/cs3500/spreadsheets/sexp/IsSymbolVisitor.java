package edu.cs3500.spreadsheets.sexp;

import java.util.List;

/**
 * A Visitor that returns if the Sexp is a Symbol.
 */
public class IsSymbolVisitor implements SexpVisitor<Boolean> {
  @Override
  public Boolean visitBoolean(boolean b) {
    return false;
  }

  @Override
  public Boolean visitNumber(double d) {
    return false;
  }

  @Override
  public Boolean visitSList(List<Sexp> l) {
    return false;
  }

  @Override
  public Boolean visitSymbol(String s) {
    return true;
  }

  @Override
  public Boolean visitString(String s) {
    return false;
  }
}