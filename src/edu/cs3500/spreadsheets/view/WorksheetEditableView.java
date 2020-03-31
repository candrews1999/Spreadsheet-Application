package edu.cs3500.spreadsheets.view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyModel;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CellInterface;

/**
 * A graphical view that renders the model as a spreadsheet and allows the user to edit Cells by
 * clicking a Cell and using the text box to change a Cell's contents.
 */
public class WorksheetEditableView extends JFrame implements WorksheetView {
  private static final int CELL_WIDTH = 80;
  private static final int CELL_HEIGHT = 25;
  private SpreadsheetPanel panel;
  private JTextField input;
  private JButton confirm;
  private JButton cancel;
  private JButton load;
  private JButton save;
  private Coord clickedCoord;
  private WorksheetModel<CellInterface> model;

  /**
   * Constructor for a WorksheetEditableView which is the overall frame that holds the visualized
   * spreadsheet of the given model and allows a user to edit Cell contents.
   *
   * @param model the model the view is based on
   */
  public WorksheetEditableView(ReadOnlyModel<CellInterface> model) {
    super();
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null!");
    }
    this.model = model;

    // Setup frame
    setTitle("BeyondGood");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setSize(12 * CELL_WIDTH + 19, 26 * CELL_HEIGHT + 16);
    setFocusable(true);
    setEnabled(true);

    // Setup the visual view
    panel = new SpreadsheetPanel(model);
    panel.setFocusable(true);
    panel.requestFocus();
    add(panel, BorderLayout.CENTER);

    // Setup editing panel
    JPanel editCellPanel = new JPanel();
    editCellPanel.setLayout(new FlowLayout());
    add(editCellPanel, BorderLayout.PAGE_START);

    // Add buttons and text box to editing panel
    confirm = new JButton("confirm");
    editCellPanel.add(confirm);
    cancel = new JButton("cancel");
    editCellPanel.add(cancel);
    input = new JTextField("", 60);
    editCellPanel.add(input);

    // Setup save and upload panel
    JPanel extraButtons = new JPanel();
    extraButtons.setLayout(new FlowLayout());
    add(extraButtons, BorderLayout.PAGE_END);

    // Add buttons to panel
    load = new JButton("upload");
    extraButtons.add(load);
    save = new JButton("save");
    extraButtons.add(save);
  }

  @Override
  public void render() {
    this.setVisible(true);
  }

  @Override
  public void addFeatures(Features features) {
    // Button listeners
    confirm.addActionListener(e -> {
      if (clickedCoord != null) {
        features.confirmChange(clickedCoord, input.getText());
      }
      this.resetFocus();
      panel.repaint();
    });
    cancel.addActionListener(e -> {
      if (clickedCoord != null) {
        input.setText(model.getCellAt(clickedCoord.row, clickedCoord.col).getOriginal());
      }
      this.resetFocus();
    });
    load.addActionListener(e -> {
      JFileChooser file = new JFileChooser();
      int result = file.showOpenDialog(null);
      if (result == JFileChooser.APPROVE_OPTION) {
        File selected = file.getSelectedFile();
        try {
          features.loadModel(selected.toString());
        } catch (IOException | IllegalStateException exceptions) {
          JOptionPane.showMessageDialog(this, "Invalid file!");
        }
      }
    });
    save.addActionListener(e -> {
      JFileChooser file = new JFileChooser();
      int result = file.showSaveDialog(null);
      if (result == JFileChooser.APPROVE_OPTION) {
        File selected = file.getSelectedFile();
        try {
          features.saveModel(selected.toString());
        } catch (IOException exception) {
          JOptionPane.showMessageDialog(this, "Save not successful!");
        }
      }
    });

    // Mouse listener for inner Cell Panel for cell selection
    CellPanel innerPanel = panel.getCellPanel();

    innerPanel.addMouseListener(new MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        int x = e.getX();
        int y = e.getY();
        findCellClicked(x, y);
      }

      private void findCellClicked(int x, int y) {
        int col = findCol(x);
        int row = findRow(y);

        clickedCoord = new Coord(col, row);
        innerPanel.setClickedCoord(clickedCoord);
        innerPanel.repaint();
        input.setText(model.getCellAt(clickedCoord.row, clickedCoord.col).getOriginal());
      }

      private int findCol(int x) {
        int colDiv = 1;
        int widthTraveledSoFar = 0;
        while (widthTraveledSoFar + model.getColWidth(CELL_WIDTH, colDiv) < x) {
          widthTraveledSoFar += model.getColWidth(CELL_WIDTH, colDiv);
          colDiv++;
        }
        return colDiv;
      }

      private int findRow(int y) {
        int rowDiv = 1;
        int heightTraveledSoFar = 0;
        while (heightTraveledSoFar + model.getRowHeight(CELL_HEIGHT, rowDiv) < y) {
          heightTraveledSoFar += model.getRowHeight(CELL_HEIGHT, rowDiv);
          rowDiv++;
        }
        return rowDiv;
      }
    });

    // Mouse Listener for ColHeaderPanel for resizing columns
    JPanel colHeaderPanel = panel.getColHeaderPanel();
    colHeaderPanel.addMouseListener(new MouseAdapter() {

      int initialX = 0;
      Integer colDivClicked;

      @Override
      public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        initialX = e.getX();
        colDivClicked = findColDividerClicked(initialX);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        // dealing with column resizing
        int x = e.getX();
        if (colDivClicked != null) {
          int diffInX = x - initialX;
          int colWidth = model.getColWidth(CELL_WIDTH, colDivClicked);
          // make diffX the perfect number to bring width back to original cell width if user drags
          // col line too far left
          if (diffInX <= (-1 * colWidth) + CELL_WIDTH) {
            diffInX = CELL_WIDTH - colWidth;
          }
          features.resizeCol(CELL_WIDTH, colDivClicked, colWidth + diffInX);
          innerPanel.repaint();
          panel.repaint();
        }
        initialX = 0;
      }

      private Integer findColDividerClicked(int x) {
        int colDivClicked = 0;
        int widthTraveledSoFar = 0;
        while (widthTraveledSoFar < x) {
          colDivClicked++;
          int colWidth = model.getColWidth(CELL_WIDTH, colDivClicked);
          widthTraveledSoFar += colWidth;
          if (widthTraveledSoFar >= x - 3 && widthTraveledSoFar <= x + 3) {
            return colDivClicked;
          }
        }
        return null;
      }
    });

    // Mouse Listener for RowHeaderPanel for resizing rows
    JPanel rowHeaderPanel = panel.getRowHeaderPanel();
    rowHeaderPanel.addMouseListener(new MouseAdapter() {

      int initialY = 0;
      Integer rowDivClicked;

      @Override
      public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        initialY = e.getY();
        rowDivClicked = findRowDividerClicked(initialY);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        // dealing with row resizing
        int y = e.getY();
        if (rowDivClicked != null) {
          int diffInY = y - initialY;
          int rowHeight = model.getRowHeight(CELL_HEIGHT, rowDivClicked);
          //make diffY the perfect number to bring width back to original cell width if user dragged
          //row line too far left
          if (diffInY <= (-1 * rowHeight) + CELL_HEIGHT) {
            diffInY = CELL_HEIGHT - rowHeight;
          }
          features.resizeRow(CELL_HEIGHT, rowDivClicked, rowHeight + diffInY);
          innerPanel.repaint();
          panel.repaint();
        }
        initialY = 0;
      }

      private Integer findRowDividerClicked(int y) {
        int rowDivClicked = 0;
        int heightTraveledSoFar = 0;
        while (heightTraveledSoFar < y) {
          rowDivClicked++;
          int rowHeight = model.getRowHeight(CELL_HEIGHT, rowDivClicked);
          heightTraveledSoFar += rowHeight;
          if (heightTraveledSoFar >= y - 3 && heightTraveledSoFar <= y + 3) {
            return rowDivClicked;
          }
        }
        return null;
      }
    });

    // Keyboard listener
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        super.keyTyped(e);
        if (clickedCoord != null) {
          switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
              features.deleteCell(clickedCoord);
              input.setText("");
              innerPanel.repaint();
              break;
            case KeyEvent.VK_UP:
              if (clickedCoord.row > 1) {
                clickedCoord = new Coord(clickedCoord.col, clickedCoord.row - 1);
                input.setText(model.getCellAt(clickedCoord.row, clickedCoord.col).getOriginal());
                innerPanel.setClickedCoord(clickedCoord);
                innerPanel.repaint();
              }
              break;
            case KeyEvent.VK_DOWN:
              clickedCoord = new Coord(clickedCoord.col, clickedCoord.row + 1);
              input.setText(model.getCellAt(clickedCoord.row, clickedCoord.col).getOriginal());
              innerPanel.setClickedCoord(clickedCoord);
              innerPanel.repaint();
              break;
            case KeyEvent.VK_LEFT:
              if (clickedCoord.col > 1) {
                clickedCoord = new Coord(clickedCoord.col - 1, clickedCoord.row);
                input.setText(model.getCellAt(clickedCoord.row, clickedCoord.col).getOriginal());
                innerPanel.setClickedCoord(clickedCoord);
                innerPanel.repaint();
              }
              break;
            case KeyEvent.VK_RIGHT:
              clickedCoord = new Coord(clickedCoord.col + 1, clickedCoord.row);
              input.setText(model.getCellAt(clickedCoord.row, clickedCoord.col).getOriginal());
              innerPanel.setClickedCoord(clickedCoord);
              innerPanel.repaint();
              break;
            default:
              // do nothing
          }
        }
      }
    });
  }

  private void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }
}
