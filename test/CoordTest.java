import org.junit.Test;

import edu.cs3500.spreadsheets.model.Coord;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Coord class. Only testing the methods we added to Coord.
 */
public class CoordTest {

  @Test
  public void testStringToCoord() {
    assertEquals(new Coord(1, 1), Coord.stringToCoord("A1"));
    assertEquals(new Coord(1, 3), Coord.stringToCoord("A3"));
    assertEquals(new Coord(26, 256), Coord.stringToCoord("Z256"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStringEmpty() {
    Coord.stringToCoord("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStringNull() {
    Coord.stringToCoord(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStringToCoordInvalid() {
    Coord.stringToCoord("A123D");
  }
}