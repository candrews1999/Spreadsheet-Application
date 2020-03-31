import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellContents;
import edu.cs3500.spreadsheets.model.cell.CellInterface;
import edu.cs3500.spreadsheets.model.cell.Formula;
import edu.cs3500.spreadsheets.model.cell.Reference;
import edu.cs3500.spreadsheets.model.cell.function.Compare;
import edu.cs3500.spreadsheets.model.cell.function.Function;
import edu.cs3500.spreadsheets.model.cell.function.Product;
import edu.cs3500.spreadsheets.model.cell.function.RemoveSpace;
import edu.cs3500.spreadsheets.model.cell.function.Sum;
import edu.cs3500.spreadsheets.model.cell.value.Bool;
import edu.cs3500.spreadsheets.model.cell.value.Dbl;
import edu.cs3500.spreadsheets.model.cell.value.Str;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for Formula which is a CellContent.
 */
public class FormulaTest {

  private WorksheetModel<CellInterface> w = new Worksheet();
  private Function sum = new Sum(w);
  private Function product = new Product(w);
  private Function compare = new Compare();
  private Function remove = new RemoveSpace();
  private CellContents dbl5 = new Dbl(5.0);
  private CellContents dbl10Half = new Dbl(10.5);
  private CellContents dbl12Half = new Dbl(12.5);
  private CellContents dblNeg5 = new Dbl(-5.0);
  private CellContents strNum = new Str("5");
  private CellContents strEmpty = new Str("");
  private CellContents strSpaces = new Str("hello i have a lot of spaces   ");
  private CellContents boolT = new Bool(true);
  private CellContents boolF = new Bool(false);
  private CellContents sumTwo =
          new Formula(new ArrayList<>(Arrays.asList(dbl5, dbl10Half)), sum);
  private CellContents refArea =
          new Reference(new ArrayList<>(Arrays.asList(new Coord(1, 1),
                  new Coord(2, 1),
                  new Coord(2, 1))), w);
  private List<CellContents> multApply =
          new ArrayList<>(Arrays.asList(refArea, strNum, boolF, sumTwo));
  private Formula sumNums;
  private Formula productNums;
  private Formula compareNums;
  private Formula removeSpaces;

  @Before
  public void setUp() {
    w.setCell(1, 1, "5.0");
    w.setCell(1, 2, "5");
    w.setCell(1, 2, "false");
    ArrayList<CellContents> mix = new ArrayList<>(
            Arrays.asList(dbl5, dbl10Half, dbl12Half, dblNeg5, strNum, strEmpty, boolT, boolF));
    sum = new Sum(w);
    sumNums = new Formula(mix, sum);
    productNums = new Formula(mix, product);
    compareNums = new Formula(new ArrayList<>(Arrays.asList(dbl5, dbl10Half)), compare);
    removeSpaces = new Formula(Collections.singletonList(strSpaces), remove);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoCells() {
    new Formula(null, sum);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoFunc() {
    new Formula(new ArrayList<>(), null);
  }

  @Test
  public void testSum() {
    assertEquals(new Dbl(23), sumNums.evaluate());
  }

  @Test
  public void testProduct() {
    assertEquals(new Dbl(-3281.25), productNums.evaluate());
  }

  @Test
  public void testSumProductMultCells() {
    assertEquals(new Dbl(20.5), new Formula(multApply, sum).evaluate());
    assertEquals(new Dbl(77.5), new Formula(multApply, product).evaluate());
  }

  @Test
  public void testCompare() {
    assertEquals(new Bool(true), compareNums.evaluate());
  }

  @Test
  public void testRemove() {
    assertEquals(new Str("helloihavealotofspaces"), removeSpaces.evaluate());
  }

  @Test
  public void testEquals() {
    assertNotEquals(new Formula(new ArrayList<>(Arrays.asList(dbl5, dbl10Half)), sum), compareNums);
    assertEquals(new Formula(new ArrayList<>(
            Arrays.asList(dbl5, dbl10Half)), compare), compareNums);
  }

  @Test
  public void testHash() {
    assertNotEquals(new Formula(
                    new ArrayList<>(Arrays.asList(dbl5, dbl10Half)), sum).hashCode(),
            compareNums.hashCode());
    assertEquals(new Formula(
                    new ArrayList<>(Arrays.asList(dbl5, dbl10Half)), compare).hashCode(),
            compareNums.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("Sum: [" + String.format("%f", 5.0) + ", "
            + String.format("%f", 10.5) + "]", sumTwo.toString());
  }
}