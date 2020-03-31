package edu.cs3500.spreadsheets.view;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.ReadOnlyModel;
import edu.cs3500.spreadsheets.model.cell.CellInterface;

/**
 * A graphical view that renders the model as a viewable spreadsheet where each Cell's Value is
 * displayed and if a Value is larger than the box size, the Value is cut off.
 */
public class WorksheetTableView extends JFrame implements WorksheetView {
  private static final int CELL_WIDTH = 80;
  private static final int CELL_HEIGHT = 25;

  /**
   * Constructor for a WorksheetTableView which is the overall Frame which holds the JScrollPanel
   * and SpreadsheetPanel.
   *
   * @param model the model that the view is based on
   */
  public WorksheetTableView(ReadOnlyModel<CellInterface> model) {
    super();
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null!");
    }
    setTitle("BeyondGood");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setSize(12 * CELL_WIDTH + 19, 26 * CELL_HEIGHT + 16);
    SpreadsheetPanel panel = new SpreadsheetPanel(model);
    add(panel);
  }

  @Override
  public void render() {
    this.setVisible(true);
  }

  @Override
  public void addFeatures(Features features) {
    throw new UnsupportedOperationException("Can't add features to visual view.");
  }
}
