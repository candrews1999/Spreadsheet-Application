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
import edu.cs3500.spreadsheets.model.cell.Formula;
import edu.cs3500.spreadsheets.model.cell.Reference;
import edu.cs3500.spreadsheets.model.cell.function.Product;
import edu.cs3500.spreadsheets.model.cell.function.Sum;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.sexp.CreateContentsVisitor;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;

import static org.junit.Assert.assertEquals;

/**
 * Test class for CreateContentVisitor.
 */
public class CreateContentsVisitorTest {
  private WorksheetModel<CellInterface> w;
  private CreateContentsVisitor cCV;
  private CreateContentsVisitor visitor;

  @Before
  public void setUp() {
    w = new Worksheet();
    cCV = new CreateContentsVisitor(w, 4, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullConstructor() {
    new CreateContentsVisitor(null, 1, 1);
  }

  // test visitBoolean
  @Test
  public void testVisitBoolean() {
    assertEquals(new Bool(false), cCV.visitBoolean(false));
    assertEquals(new Bool(true), cCV.visitBoolean(true));
  }

  // test visitNumber
  @Test
  public void testVisitNumber() {
    assertEquals(new Dbl(0.01), cCV.visitNumber(0.01));
    assertEquals(new Dbl(10.23455), cCV.visitNumber(10.23455));
  }

  // test visitList
  @Test(expected = IllegalArgumentException.class)
  public void testVisitListNoFunc() {
    cCV.visitSList(
            new ArrayList<>(Arrays.asList(new SSymbol("A1"), new SSymbol("A2"))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitListNoArgs() {
    cCV.visitSList(new ArrayList<>(Collections.singletonList(new SSymbol("SUM"))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitListCycleColRef() {
    w.setCell(1, 1, "= C1");
    assertEquals(new Reference(
            new ArrayList<>(Collections.singletonList(new Coord(3, 1))), w),
            w.getCellAt(1, 1).getContents());
    cCV = new CreateContentsVisitor(w, 1, 3);
    cCV.visitSList(new ArrayList<>(Arrays.asList(new SSymbol("SUM"),
            new SSymbol("A:B"))));
  }

  @Test
  public void testVisitListColRef() {
    ArrayList<Sexp> list = new ArrayList<>(Arrays.asList(new SSymbol("SUM"),
            new SSymbol("A:B")));
    ArrayList<CellContents> after = new ArrayList<>(Collections.singletonList(
                    new ColumnReference(new ArrayList<>(Arrays.asList(1, 2)), w)));
    assertEquals(new Formula(after, new Sum(w)), cCV.visitSList(list));
  }

  @Test
  public void testVisitListValidSimple() {
    w.setCell(1, 1, "5.0");
    ArrayList<Sexp> list = new ArrayList<>(Arrays.asList(new SSymbol("SUM"),
            new SNumber(5), new SSymbol("A1"), new SBoolean(true)));
    ArrayList<CellContents> after = new ArrayList<>(Arrays.asList(new Dbl(5.0),
            new Reference(new ArrayList<>(Collections.singletonList(new Coord(1, 1))), w),
            new Bool(true)));
    assertEquals(new Formula(after, new Sum(w)), cCV.visitSList(list));
  }

  @Test
  public void testVisitListValidMultiRef1() {
    w.setCell(1, 1, "5.0");
    ArrayList<Sexp> list = new ArrayList<>(Arrays.asList(new SSymbol("PRODUCT"),
            new SNumber(5), new SSymbol("A1:A2"), new SBoolean(true)));
    ArrayList<CellContents> after = new ArrayList<>(Arrays.asList(new Dbl(5.0),
            new Reference(new ArrayList<>(Arrays.asList(new Coord(1, 1),
                    new Coord(1, 2))), w),
            new Bool(true)));
    assertEquals(new Formula(after, new Product(w)), cCV.visitSList(list));
  }

  @Test
  public void testVisitListValidMultiRef2() {
    w.setCell(2, 2, "5.0");
    w.setCell(3, 3, "6.5");
    ArrayList<Sexp> list = new ArrayList<>(Arrays.asList(new SSymbol("SUM"),
            new SNumber(5), new SSymbol("B2:C3"), new SBoolean(true)));
    ArrayList<CellContents> after = new ArrayList<>(Arrays.asList(new Dbl(5.0),
            new Reference(new ArrayList<>(Arrays.asList(
                    new Coord(2, 2),
                    new Coord(3, 2),
                    new Coord(2, 3),
                    new Coord(3, 3))), w),
            new Bool(true)));
    assertEquals(new Formula(after, new Sum(w)), cCV.visitSList(list));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitListSimpleCycle() {
    visitor = new CreateContentsVisitor(w, 1, 1);
    visitor.visitSList(new ArrayList<>(
            Arrays.asList(new SSymbol("SUM"), new SSymbol("A1"))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitListComplexCycle1() {
    w.setCell(2, 1, "5");
    visitor = new CreateContentsVisitor(w, 1, 2);
    ArrayList<Sexp> toCheck =
            new ArrayList<>(Arrays.asList(new SSymbol("SUM"), new SNumber(5),
                    new SBoolean(true), new SString("hello"), new SSymbol("A1"),
                    new SList(new SSymbol("A1:A2"))));
    visitor.visitSList(toCheck);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitListComplexCycle2() {
    w.setCell(1, 1, "A2"); // A1 = A2
    w.setCell(2, 1, "5"); // A2 = 5
    visitor = new CreateContentsVisitor(w, 3, 1); // A3
    ArrayList<Sexp> toCheck =
            new ArrayList<>(Arrays.asList(new SSymbol("SUM"), new SSymbol("A1:A3")));
    visitor.visitSList(toCheck);
  }

  // test visitSymbol
  @Test(expected = IllegalArgumentException.class)
  public void testVisitSymbolExtraColon() {
    assertEquals(new Reference(new ArrayList<>(), w), cCV.visitSymbol("A1::C3"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitSymbolFirstIsColon() {
    assertEquals(new Reference(new ArrayList<>(), w), cCV.visitSymbol(":C3:A1"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitSymbolSum() {
    assertEquals(new Reference(new ArrayList<>(), w), cCV.visitSymbol("SUM"));
  }

  @Test
  public void testVisitSymbolSumFirstSmaller() {
    assertEquals(new Reference(new ArrayList<>(Arrays.asList(new Coord(1, 1),
            new Coord(1, 2),
            new Coord(1, 3))), w),
            cCV.visitSymbol("A1:A3"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitSymbolSumSecondSmaller() {
    cCV.visitSymbol("B1:A1");
  }

  @Test
  public void testVisitSymbol() {
    assertEquals(new Reference(new ArrayList<>(Arrays.asList(
            new Coord(1, 1),
            new Coord(2, 1),
            new Coord(1, 2),
            new Coord(2, 2),
            new Coord(1, 3),
            new Coord(2, 3))), w),
            cCV.visitSymbol("A1:B3"));
  }

  @Test
  public void testVisitSymbolSingleColRef() {
    assertEquals(new ColumnReference(new ArrayList<>(Collections.singletonList(1)), w),
            cCV.visitSymbol("A:A"));
  }

  @Test
  public void testVisitSymbolColRef() {
    assertEquals(new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3)), w),
            cCV.visitSymbol("A:C"));
    assertEquals(new ColumnReference(new ArrayList<>(Arrays.asList(1, 2)), w),
            cCV.visitSymbol("A:B"));
    assertEquals(new ColumnReference(new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6)), w),
            cCV.visitSymbol("B:F"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitSymbolFirstColRefLarger() {
    cCV.visitSymbol("C:A");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitSymbolColRefMalformed() {
    cCV.visitSymbol("C1:A");
  }
}