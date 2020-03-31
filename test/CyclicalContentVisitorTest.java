import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.ColumnReference;
import edu.cs3500.spreadsheets.model.cell.CyclicalContentVisitor;
import edu.cs3500.spreadsheets.model.cell.Formula;
import edu.cs3500.spreadsheets.model.cell.Reference;
import edu.cs3500.spreadsheets.model.cell.function.Product;
import edu.cs3500.spreadsheets.model.cell.function.Sum;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Str;
import edu.cs3500.spreadsheets.model.cell.value.Value;

import static org.junit.Assert.assertFalse;

/**
 * Test class for CyclicalContentVisitor which is a CellContentVisitor.
 */
public class CyclicalContentVisitorTest {


  private WorksheetModel<CellInterface> w = new Worksheet();
  private CyclicalContentVisitor visitor;
  private CellContents strHi = new Str("hi");
  private CellContents boolT = new Bool(true);
  private CellContents dbl10Half = new Dbl(10.5);
  private CellContents sumForm = new Formula(
          new ArrayList<>(Arrays.asList(strHi, boolT, dbl10Half,
                  new Reference(new ArrayList<>(Collections.singletonList(
                          new Coord(3, 1))), w))),
          new Sum(w));
  private CellContents prodForm = new Formula(
          new ArrayList<>(Arrays.asList(strHi, boolT, dbl10Half,
                  new Reference(new ArrayList<>(Collections.singletonList(
                          new Coord(3, 1))), w), sumForm)),
          new Product(w));

  @Before
  public void setUp() {
    w.setCell(1, 1, "");
    w.setCell(1, 3, "=B1");
    w.setCell(1, 2, "=A1");
  }

  // test visitValue
  @Test
  public void testVisitValue() {
    visitor = new CyclicalContentVisitor(1, 1, w);
    Value str = new Str("hello");
    Value dbl = new Dbl(10.543);
    Value boolF = new Bool(false);
    Value boolT = new Bool(true);
    assertFalse(visitor.visitValue(dbl) || visitor.visitValue(str)
            || visitor.visitValue(boolF) || visitor.visitValue(boolT));
  }

  // test visitReference
  @Test
  public void testVisitRefSingleSameRow() {
    visitor = new CyclicalContentVisitor(1, 3, w);
    assertFalse(visitor.visitReference(
            new ArrayList<>(Collections.singletonList(new Coord(1, 1)))));
  }

  @Test
  public void testVisitRefSingleSameCol() {
    visitor = new CyclicalContentVisitor(3, 1, w);
    assertFalse(visitor.visitReference(
            new ArrayList<>(Collections.singletonList(new Coord(1, 1)))));
  }

  @Test
  public void testVisitRefSingleNullContent() {
    visitor = new CyclicalContentVisitor(2, 3, w);
    assertFalse(visitor.visitReference(
            new ArrayList<>(Collections.singletonList(new Coord(1, 2)))));
  }

  @Test
  public void testVisitRefSingleRecursive() {
    visitor = new CyclicalContentVisitor(3, 3, w);
    assertFalse(visitor.visitReference(
            new ArrayList<>(Collections.singletonList(new Coord(2, 1)))));
  }

  @Test
  public void testVisitRefList() {
    visitor = new CyclicalContentVisitor(3, 3, w);
    ArrayList<Coord> refNotC = new ArrayList<>(Arrays.asList(
            new Coord(1, 1),
            new Coord(1, 2),
            new Coord(2, 1),
            new Coord(3, 1)));
    assertFalse(visitor.visitReference(refNotC));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitRefSingleC() {
    visitor = new CyclicalContentVisitor(1, 1, w);
    assertFalse(visitor.visitReference(
            new ArrayList<>(Collections.singletonList(new Coord(1, 1)))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitRefRecursiveC() {
    visitor = new CyclicalContentVisitor(1, 1, w);
    assertFalse(visitor.visitReference(
            new ArrayList<>(Collections.singletonList(new Coord(3, 1)))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitRefListC() {
    visitor = new CyclicalContentVisitor(1, 3, w);
    ArrayList<Coord> refC = new ArrayList<>(Arrays.asList(new Coord(1, 1),
            new Coord(2, 1),
            new Coord(3, 1)));
    assertFalse(visitor.visitReference(refC));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitRefRecursiveListC() {
    visitor = new CyclicalContentVisitor(1, 4, w);
    w.setCell(1, 4, "false");
    w.setCell(2, 3, "=D1");
    ArrayList<Coord> refC = new ArrayList<>(Arrays.asList(new Coord(1, 1),
            new Coord(2, 1),
            new Coord(3, 2)));
    assertFalse(visitor.visitReference(refC));
  }

  // test visitFormula
  @Test
  public void testVisitFormulaRecursive() {
    visitor = new CyclicalContentVisitor(3, 3, w);
    ArrayList<CellContents> toApply =
            new ArrayList<>(Arrays.asList(strHi, dbl10Half, boolT,
                    new Reference(new ArrayList<>(Collections.singletonList(
                            new Coord(3, 1))), w), sumForm, prodForm));
    assertFalse(visitor.visitFormula(toApply));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitFormulaFirstC() {
    visitor = new CyclicalContentVisitor(1, 1, w);
    visitor.visitFormula(new ArrayList<>(Collections.singletonList(
            new Reference(new ArrayList<>(Collections.singletonList(
                    new Coord(2, 1))), w))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitFormulaLastC() {
    visitor = new CyclicalContentVisitor(1, 2, w);
    visitor.visitFormula(new ArrayList<>(Arrays.asList(strHi, dbl10Half, boolT, new Reference(
            new ArrayList<>(Collections.singletonList(
                    new Coord(3, 1))), w))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitFormulaRecursiveC() {
    visitor = new CyclicalContentVisitor(1, 1, w);
    CellContents prodForm2 = new Formula(
            new ArrayList<>(Arrays.asList(strHi, boolT, dbl10Half, sumForm)), new Product(w));
    ArrayList<CellContents> toApply =
            new ArrayList<>(Arrays.asList(strHi, dbl10Half, boolT, prodForm2));
    assertFalse(visitor.visitFormula(toApply));
  }

  // test visitColumnReference
  @Test(expected = IllegalArgumentException.class)
  public void testVisitRefColCycle() {
    visitor = new CyclicalContentVisitor(5, 3, w);
    ColumnReference aToE = new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)), w);
    CellContents sumForm2 = new Formula(
            new ArrayList<>(Collections.singletonList(aToE)), new Sum(w));
    ArrayList<CellContents> toApply =
            new ArrayList<>(Collections.singletonList(sumForm2));
    visitor.visitFormula(toApply);
  }

  @Test
  public void testVisitRefColNoCycle() {
    visitor = new CyclicalContentVisitor(5, 6, w);
    ColumnReference aToE = new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)), w);
    CellContents sumForm2 = new Formula(
            new ArrayList<>(Collections.singletonList(aToE)), new Sum(w));
    ArrayList<CellContents> toApply =
            new ArrayList<>(Collections.singletonList(sumForm2));
    assertFalse(visitor.visitFormula(toApply));
  }
}