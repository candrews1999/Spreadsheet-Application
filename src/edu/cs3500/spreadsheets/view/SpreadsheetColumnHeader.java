package edu.cs3500.spreadsheets.view;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellInterface;

/**
 * Used to create a JPanel for the Spreadsheet Column Header.
 */
public class SpreadsheetColumnHeader extends JPanel {
  private static final int CELL_WIDTH = 80;
  private static final int CELL_HEIGHT = 25;
  private WorksheetModel<CellInterface> model;

  public SpreadsheetColumnHeader(WorksheetModel<CellInterface> model) {
    this.model = model;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    setFocusable(false);
    int cols = getWidth() / CELL_WIDTH;
    int height = 0;
    int widthTraveledSoFar = 0;
    for (int i = 1; i < cols; i++) {
      int colWidth = model.getColWidth(CELL_WIDTH, i);
      g2.setPaint(Color.LIGHT_GRAY);
      g2.fillRect(widthTraveledSoFar, height, colWidth, CELL_HEIGHT);
      g2.setPaint(Color.BLACK);
      g2.drawRect(widthTraveledSoFar, height, colWidth, CELL_HEIGHT);
      widthTraveledSoFar += colWidth;
    }
    drawHeaders(g2);
  }

  private void drawHeaders(Graphics2D g2) {
    int cols = getWidth() / CELL_WIDTH;
    // Set header colors
    g2.setFont(new Font(g2.getFont().getName(), Font.BOLD, 16));
    // Fill column header
    int widthTraveledSoFar = 0;
    for (int i = 0; i < cols; i++) {
      int colWidth = model.getColWidth(CELL_WIDTH, i + 1);
      g2.drawString(Coord.colIndexToName(i + 1),
              widthTraveledSoFar + 5,
              (CELL_HEIGHT / 2) + 10);
      widthTraveledSoFar += colWidth;
    }
  }
}
