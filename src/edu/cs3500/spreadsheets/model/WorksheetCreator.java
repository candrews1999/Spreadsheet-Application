package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.cell.CellInterface;

/**
 * Factory class for creating a Worksheet.
 */
public class WorksheetCreator
        implements WorksheetReader.WorksheetBuilder<WorksheetModel<CellInterface>> {
  private WorksheetModel<CellInterface> model;

  /**
   * Constructor for a WorksheetCreator which initializes the model.
   */
  public WorksheetCreator() {
    this.model = new Worksheet();
  }

  @Override
  public WorksheetReader.WorksheetBuilder<WorksheetModel<CellInterface>>
      createCell(int col, int row, String contents) {
    model.setCell(row, col, contents);
    return this;
  }

  @Override
  public WorksheetReader.WorksheetBuilder<WorksheetModel<CellInterface>>
      changeColSize(int col, int size) {
    model.addResizedCol(80, col, size);
    return this;
  }

  @Override
  public WorksheetReader.WorksheetBuilder<WorksheetModel<CellInterface>>
      changeRowSize(int row, int size) {
    model.addResizedRow(20, row, size);
    return this;
  }

  @Override
  public WorksheetModel<CellInterface> createWorksheet() {
    return model;
  }
}
