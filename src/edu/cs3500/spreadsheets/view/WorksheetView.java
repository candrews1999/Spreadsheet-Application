package edu.cs3500.spreadsheets.view;

import java.io.IOException;

import edu.cs3500.spreadsheets.controller.Features;

/**
 * A view interface for a worksheet.
 */
public interface WorksheetView {
  /**
   * Renders a Worksheet model in some manner.
   *
   * @throws IOException if the rendering fails
   */
  void render() throws IOException;

  /**
   * Adds features to the view so that it can communicate with the controller.
   *
   * @param features the features to add
   */
  void addFeatures(Features features);
}
