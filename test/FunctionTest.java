import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.ColumnReference;
import edu.cs3500.spreadsheets.model.cell.function.Compare;
import edu.cs3500.spreadsheets.model.cell.function.Function;
import edu.cs3500.spreadsheets.model.cell.function.Product;
import edu.cs3500.spreadsheets.model.cell.function.RemoveSpace;
import edu.cs3500.spreadsheets.model.cell.function.Sum;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.ErrorVal;
import edu.cs3500.spreadsheets.model.cell.value.Str;
import edu.cs3500.spreadsheets.model.cell.value.Value;

import static org.junit.Assert.assertEquals;

/**
 * Test class for testing different Functions.
 */
public class FunctionTest {

  private WorksheetModel<CellInterface> w = new Worksheet();
  private CellContents dbl5 = new Dbl(5.0);
  private CellContents dbl10Half = new Dbl(10.5);
  private CellContents dbl12Half = new Dbl(12.5);
  private CellContents dblNeg5 = new Dbl(-5.0);
  private CellContents strNum = new Str("5");
  private CellContents strOneSpace = new Str("hello i");
  private CellContents strEmpty = new Str("");
  private CellContents boolT = new Bool(true);
  private CellContents boolF = new Bool(false);
  private CellContents error = new ErrorVal("hi");
  private ArrayList<CellContents> allDbl;
  private ArrayList<CellContents> noNums;
  private ArrayList<CellContents> mix;
  private Function sum = new Sum(w);
  private Function product = new Product(w);
  private Function compare = new Compare();
  private Function remove = new RemoveSpace();

  @Before
  public void setUp() {
    allDbl = new ArrayList<>(Arrays.asList(dbl5, dbl10Half, dbl12Half, dblNeg5));
    noNums = new ArrayList<>(Arrays.asList(strNum, strEmpty, boolT, boolF));
    mix = new ArrayList<>(allDbl);
    mix.addAll(noNums);
  }

  // test invalid apply arguments for RemoveSpace class
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveEmpty() {
    remove.apply(new ArrayList<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveTooMany() {
    remove.apply(new ArrayList<>(Arrays.asList(strNum, strOneSpace)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNotStr() {
    remove.apply(new ArrayList<>(Collections.singletonList(boolT)));
  }

  // test invalid apply arguments for Compare class
  @Test(expected = IllegalArgumentException.class)
  public void testCompareEmpty() {
    compare.apply(new ArrayList<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompareTooMany() {
    compare.apply(new ArrayList<>(Arrays.asList(dbl5, dbl10Half, dblNeg5)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompareTooFew() {
    compare.apply(new ArrayList<>(Collections.singletonList(dblNeg5)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompareNotNum() {
    compare.apply(new ArrayList<>(Arrays.asList(dbl5, strEmpty)));
  }

  // test Sum and Product classes
  @Test
  public void testAllDbl() {
    assertEquals(new Dbl(-3281.25), product.apply(allDbl));
    assertEquals(new Dbl(23.0), sum.apply(allDbl));
  }

  @Test
  public void testMixValue() {
    assertEquals(new Dbl(-3281.25), product.apply(mix));
    assertEquals(new Dbl(23.0), sum.apply(mix));
  }

  @Test
  public void testSameResult() {
    assertEquals(product.apply(allDbl), product.apply(mix));
    assertEquals(sum.apply(allDbl), sum.apply(mix));
  }

  @Test
  public void testNoDbl() {
    assertEquals(new Dbl(0.0), product.apply(noNums));
    assertEquals(new Dbl(0.0), sum.apply(noNums));
  }

  @Test
  public void testMixOrder() {
    Collections.shuffle(mix);
    assertEquals(new Dbl(-3281.25), product.apply(mix));
    assertEquals(new Dbl(23.0), sum.apply(mix));
  }

  @Test
  public void testColRefAllNums() {
    w.setCell(1, 1, "5.25");
    w.setCell(300, 1, "5.5");
    w.setCell(1, 2, "-5.25");
    w.setCell(298, 2, "-5.5");
    assertEquals(new Dbl(0.0), sum.apply(new ArrayList<>(Collections.singletonList(
            new ColumnReference(new ArrayList<>(Arrays.asList(1, 2)), w)))));
    assertEquals(new Dbl(833.765625), product.apply(new ArrayList<>(Collections.singletonList(
            new ColumnReference(new ArrayList<>(Arrays.asList(1, 2)), w)))));
  }

  @Test
  public void testColRefMix() {
    w.setCell(1, 1, "5.25");
    w.setCell(300, 1, "5.5");
    w.setCell(1, 2, "-5.25");
    w.setCell(298, 2, "-5.5");
    w.setCell(5, 1, "HELLO!");
    assertEquals(new Dbl(0.0), sum.apply(new ArrayList<>(Collections.singletonList(
            new ColumnReference(new ArrayList<>(Arrays.asList(1, 2)), w)))));
    assertEquals(new Dbl(833.765625), product.apply(new ArrayList<>(Collections.singletonList(
            new ColumnReference(new ArrayList<>(Arrays.asList(1, 2)), w)))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testColRefErrorSum() {
    w.setCell(1, 1, "=A1");
    sum.apply(new ArrayList<>(Collections.singletonList(new ColumnReference(
            new ArrayList<>(Arrays.asList(1, 2)), w))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testColRefErrorProduct() {
    w.setCell(1, 1, "=A1");
    product.apply(new ArrayList<>(Collections.singletonList(new ColumnReference(
            new ArrayList<>(Arrays.asList(1, 2)), w))));
  }

  @Test
  public void testEmptyList() {
    assertEquals(new Dbl(0.0), product.apply(new ArrayList<>()));
    assertEquals(new Dbl(0.0), sum.apply(new ArrayList<>()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testErrorSum() {
    sum.apply(new ArrayList<>(Collections.singletonList(error)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testErrorProduct() {
    product.apply(new ArrayList<>(Collections.singletonList(error)));
  }

  @Test
  public void testToStringProduct() {
    assertEquals("Product", product.toString());
  }

  @Test
  public void testToStringSum() {
    assertEquals("Sum", sum.toString());
  }

  // test RemoveSpace class
  @Test
  public void testNoSpace() {
    assertEquals(strNum, remove.apply(new ArrayList<>(Collections.singletonList(strNum))));
  }

  @Test
  public void testNoString() {
    Value strEmpty = new Str("");
    assertEquals(strEmpty, remove.apply(new ArrayList<>(Collections.singletonList(strEmpty))));
  }

  @Test
  public void testOneSpace() {
    assertEquals(new Str("helloi"),
            remove.apply(new ArrayList<>(Collections.singletonList(strOneSpace))));
  }

  @Test
  public void testColRefSpaces() {
    w.setCell(1, 1, "HELLO I");
    assertEquals(new Str("HELLOI"),
            remove.apply(new ArrayList<>(Collections.singletonList(
                    new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3)), w)))));
  }

  @Test
  public void testMultipleSpaces() {
    Value strTwoSpace = new Str("hello i am");
    Value strEndSpace = new Str("hello i am     ");
    Value result = new Str("helloiam");
    assertEquals(result,
            remove.apply(new ArrayList<>(Collections.singletonList(strTwoSpace))));
    assertEquals(result,
            remove.apply(new ArrayList<>(Collections.singletonList(strEndSpace))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testErrorRemove() {
    product.apply(new ArrayList<>(Collections.singletonList(error)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testColRefNotString() {
    w.setCell(1, 1, "5");
    remove.apply(new ArrayList<>(Collections.singletonList(
            new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3)), w))));
  }

  @Test
  public void testToStringRemove() {
    assertEquals("RemoveSpace", remove.toString());
  }

  // test Compare class
  @Test
  public void testTrue1() {
    assertEquals(new Bool(true),
            compare.apply(new ArrayList<>(Arrays.asList(dbl5, dbl10Half))));
  }

  @Test
  public void testTrue2() {
    assertEquals(new Bool(true),
            compare.apply(new ArrayList<>(Arrays.asList(dbl10Half, dbl12Half))));
  }

  @Test
  public void testFalse() {
    assertEquals(new Bool(false),
            compare.apply(new ArrayList<>(Arrays.asList(dbl12Half, dbl10Half))));
  }

  @Test
  public void testFalseSameNum() {
    assertEquals(new Bool(false),
            compare.apply(new ArrayList<>(Arrays.asList(dbl5, dbl5))));
  }

  @Test
  public void testColRefNumberTrue() {
    w.setCell(1, 1, "1");
    assertEquals(new Bool(true),
            compare.apply(new ArrayList<>(Arrays.asList(
                    new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3)), w), dbl5))));
  }

  @Test
  public void testColRefNumberFalse() {
    w.setCell(1, 1, "10");
    assertEquals(new Bool(false),
            compare.apply(new ArrayList<>(Arrays.asList(
                    new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3)), w), dbl5))));
  }

  @Test
  public void testNegative() {
    assertEquals(new Bool(false),
            compare.apply(new ArrayList<>(Arrays.asList(dbl5, dblNeg5))));
    assertEquals(new Bool(true),
            compare.apply(new ArrayList<>(Arrays.asList(dblNeg5, dbl5))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testErrorCompare() {
    compare.apply(new ArrayList<>(Collections.singletonList(error)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testColRefNotNumber() {
    w.setCell(1, 1, "HELLO!");
    compare.apply(new ArrayList<>(Arrays.asList(
            new ColumnReference(new ArrayList<>(Arrays.asList(1, 2, 3)), w), dbl5)));
  }

  @Test
  public void testToStringCompare() {
    assertEquals("Compare", compare.toString());
  }
}