package edu.cs3500.spreadsheets.view;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyModel;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellInterface;

/**
 * A CellPanel that draws all Cell Values in a WorksheetModel.
 */
public class CellPanel extends JPanel {
  private final WorksheetModel<CellInterface> model;
  private static final int CELL_WIDTH = 80;
  private static final int CELL_HEIGHT = 25;
  private Coord clickedCoord;

  /**
   * A CellPanel constructor that takes in the WorksheetModel whose Cells need to be drawn.
   *
   * @param model the WorksheetModel to draw
   */
  public CellPanel(ReadOnlyModel<CellInterface> model) {
    this.model = model;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    int cols = getWidth() / CELL_WIDTH;
    int rows = getHeight() / CELL_HEIGHT;

    // Draw col lines
    g2.setPaint(Color.BLACK);
    int widthTraveledSoFar = 0;
    for (int i = 1; i < cols; i++) {
      int colWidth = model.getColWidth(CELL_WIDTH, i);
      widthTraveledSoFar += colWidth;
      g2.drawLine(widthTraveledSoFar, 0, widthTraveledSoFar, getHeight());
    }

    // Draw row lines
    int heightTraveledSoFar = 0;
    for (int i = 1; i < rows; i++) {
      int rowHeight = model.getRowHeight(CELL_HEIGHT, i);
      heightTraveledSoFar += rowHeight;
      g2.drawLine(0, heightTraveledSoFar, getWidth(), heightTraveledSoFar);
    }

    // Draw cells
    this.drawFilledEvals(g2);

    // Draw currently clicked
    if (clickedCoord != null) {
      int clickedCol = model.getLeftXOfCol(CELL_WIDTH, clickedCoord.col);
      int clickedRow = model.getUpperYOfRow(CELL_HEIGHT, clickedCoord.row);
      g2.setPaint(Color.RED);
      g2.drawRect(clickedCol, clickedRow, model.getColWidth(CELL_WIDTH, clickedCoord.col),
              model.getRowHeight(CELL_HEIGHT, clickedCoord.row));
      g2.setPaint(Color.BLACK);
    }
  }

  private void drawFilledEvals(Graphics2D g2) {
    // Draw filled Cell's evaluations
    g2.setFont(new Font(g2.getFont().getName(), Font.PLAIN, 14));
    String evalStr;
    for (CellInterface c : model.getAllCells()) {
      //get how big each evaluation String is
      Coord cellCoord = c.getCoord();
      int col = cellCoord.col;
      int row = cellCoord.row;
      try {
        evalStr = c.evaluate().toString();
      } catch (IllegalArgumentException e) {
        evalStr = e.getMessage();
      }
      int evalStrWidth = g2.getFontMetrics().stringWidth(evalStr);
      // Draw String if fits, else truncate
      int colWidth = model.getColWidth(CELL_WIDTH, col);
      int xCoordForCol = model.getLeftXOfCol(CELL_WIDTH, col);
      int yCoordForRow = model.getUpperYOfRow(CELL_HEIGHT, row) - 2 + CELL_HEIGHT;
      if (evalStrWidth < colWidth) {
        g2.drawString(evalStr,
                xCoordForCol + 3,
                yCoordForRow);
      } else {
        String dotString = evalStr + "...";
        StringBuilder dotStringBuilder = new StringBuilder(dotString);
        // while the string is larger than cell width, remove one letter from end
        while (g2.getFontMetrics().stringWidth(dotStringBuilder.toString()) >= colWidth) {
          dotStringBuilder.deleteCharAt(dotStringBuilder.length() - 4);
        }
        g2.drawString(dotStringBuilder.toString(),
                xCoordForCol + 2,
                yCoordForRow);
      }
    }
  }

  void setClickedCoord(Coord clicked) {
    this.clickedCoord = clicked;
  }
}
