package edu.cs3500.spreadsheets.view;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellInterface;

/**
 * Used to create a JPanel for the Spreadsheet Row Header.
 */
public class SpreadsheetRowHeader extends JPanel {
  private static final int CELL_WIDTH = 80;
  private static final int CELL_HEIGHT = 25;
  private WorksheetModel<CellInterface> model;

  public SpreadsheetRowHeader(WorksheetModel<CellInterface> model) {
    this.model = model;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    setFocusable(false);
    int rows = getHeight() / CELL_HEIGHT;
    int width = 0;
    int heightTraveledSoFar = 0;
    for (int i = 1; i < rows; i++) {
      int rowHeight = model.getRowHeight(CELL_HEIGHT, i);
      g2.setPaint(Color.LIGHT_GRAY);
      g2.fillRect(width, heightTraveledSoFar, CELL_WIDTH, rowHeight);
      g2.setPaint(Color.BLACK);
      g2.drawRect(width, heightTraveledSoFar, CELL_WIDTH, rowHeight);
      heightTraveledSoFar += rowHeight;
    }
    drawHeaders(g2);
  }

  private void drawHeaders(Graphics2D g2) {
    int rows = getHeight() / CELL_HEIGHT;
    // Set header colors
    g2.setFont(new Font(g2.getFont().getName(), Font.BOLD, 16));
    int heightTraveledSoFar = 0;
    for (int i = 0; i < rows; i++) {
      int rowHeight = model.getRowHeight(CELL_HEIGHT, i + 1);
      g2.drawString(i + "",
              5,
              heightTraveledSoFar - 2);
      heightTraveledSoFar += rowHeight;
    }
  }
}
