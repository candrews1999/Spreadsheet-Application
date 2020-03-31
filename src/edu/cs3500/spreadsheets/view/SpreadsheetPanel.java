package edu.cs3500.spreadsheets.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;

import edu.cs3500.spreadsheets.model.ReadOnlyModel;
import edu.cs3500.spreadsheets.model.cell.CellInterface;

/**
 * A SpreadsheetPanel which visualizes all Cells in a WorksheetModel and has row and column
 * headers.
 */
public class SpreadsheetPanel extends JPanel {
  private static final int CELL_WIDTH = 80;
  private static final int CELL_HEIGHT = 25;
  private static final int SCROLLBAR_NONLONG_CONSTANT = 15;
  private CellPanel cellPanel;
  private JPanel colHeaderPanel;
  private JPanel rowHeaderPanel;

  /**
   * A SpreadsheetPanel constructor that takes in the model to visualize.
   *
   * @param model the model to be drawn
   */
  public SpreadsheetPanel(ReadOnlyModel<CellInterface> model) {
    super();
    int cols = Math.max(model.numCols(), 12);
    int rows = Math.max(model.numRows(), 26);
    setLayout(new BorderLayout());
    setSize(12 * CELL_WIDTH + 19, 26 * CELL_HEIGHT + 16);

    // Setup CellPanel
    cellPanel = new CellPanel(model);
    cellPanel.setPreferredSize(
            new Dimension(CELL_WIDTH * cols, CELL_HEIGHT * rows));

    // Setup JScrollPane
    JScrollPane scrollPane = new JScrollPane(cellPanel);
    scrollPane.setPreferredSize(new Dimension(12 * CELL_WIDTH, 26 * CELL_HEIGHT));

    // Setup JScrollBars
    JScrollBar hBar = scrollPane.getHorizontalScrollBar();
    JScrollBar vBar = scrollPane.getVerticalScrollBar();
    hBar.setPreferredSize(new Dimension(CELL_WIDTH * cols, SCROLLBAR_NONLONG_CONSTANT));
    vBar.setPreferredSize(new Dimension(SCROLLBAR_NONLONG_CONSTANT, CELL_HEIGHT * rows));
    hBar.setUnitIncrement(CELL_WIDTH);
    vBar.setUnitIncrement(CELL_HEIGHT);
    add(scrollPane, BorderLayout.CENTER);

    // Setup headers
    JPanel rowHead = new SpreadsheetRowHeader(model);
    JPanel colHead = new SpreadsheetColumnHeader(model);
    this.colHeaderPanel = colHead;
    this.rowHeaderPanel = rowHead;
    colHead.setPreferredSize(new Dimension(cols * CELL_WIDTH, CELL_HEIGHT));
    rowHead.setPreferredSize(new Dimension(CELL_WIDTH, rows * CELL_HEIGHT));
    scrollPane.setColumnHeaderView(colHead);
    scrollPane.setRowHeaderView(rowHead);

    // Resize listeners
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        Dimension larger = new Dimension(cellPanel.getWidth() + CELL_WIDTH,
                cellPanel.getHeight() + CELL_HEIGHT);
        cellPanel.setPreferredSize(larger);
        Dimension largerColHeader = new Dimension(
                colHead.getWidth() + CELL_WIDTH,
                colHead.getHeight());
        colHead.setPreferredSize(largerColHeader);
        Dimension largerRowHeader = new Dimension(
                rowHead.getWidth(),
                rowHead.getHeight() + CELL_HEIGHT);
        rowHead.setPreferredSize(largerRowHeader);
      }
    });

    // Scrollbar listeners
    hBar.addAdjustmentListener(e -> {
      if ((hBar.getValue() + hBar.getModel().getExtent()) == hBar.getMaximum()) {
        Dimension larger = new Dimension(cellPanel.getWidth() + CELL_WIDTH,
                cellPanel.getHeight());
        cellPanel.setPreferredSize(larger);
        Dimension largerColHeader = new Dimension(
                colHead.getWidth() + CELL_WIDTH,
                colHead.getHeight());
        colHead.setPreferredSize(largerColHeader);
      }
    });
    vBar.addAdjustmentListener(e -> {
      if ((vBar.getValue() + vBar.getModel().getExtent()) == vBar.getMaximum()) {
        Dimension larger = new Dimension(cellPanel.getWidth(),
                cellPanel.getHeight() + CELL_HEIGHT);
        cellPanel.setPreferredSize(larger);
        Dimension largerRowHeader = new Dimension(
                rowHead.getWidth(),
                rowHead.getHeight() + CELL_HEIGHT);
        rowHead.setPreferredSize(largerRowHeader);
      }
    });
  }

  public CellPanel getCellPanel() {
    return cellPanel;
  }

  public JPanel getColHeaderPanel() {
    return colHeaderPanel;
  }

  public JPanel getRowHeaderPanel() {
    return rowHeaderPanel;
  }
}
