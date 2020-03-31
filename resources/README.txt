CHANGED
- Added a new custom JPanel class that holds a CellPanel, headers, and scrolling functionality
  - Previously we only had a custom JPanel class that drew Cells and we added headers and scrolling inside our visual view
  - Did this so that we could avoid duplicate code when reusing our custom JPanel in our editable visual view
- Added a delete Cell method in our model
  - Added this functionality so that a user can completely remove a Cell from the model
  - Previously deleting a Cell was just setting the Cell's original String to ""
  - The method will take the Cell out of the model's getAllCells list and thus free up memory
- Added addFeatures to our view interface which allows certain views to interact with a controller and make changes to a model through the controller
  - The previous views from assignment 6 throw UnsupportedOperationException
- Added a ReadOnlyModel which allows a user to access data from the model (ex: getCellAt, getAllCells) but doesn't allow the user to make any changes
  - setCell and deleteCell do nothing in the ReadOnlyModel

FEATURES/CONTROLLER
- Added a Features interface (controller) and implemented it with WorksheetController
  - Methods in the interface are confirmChange, deleteCell, and saveModel
- Our WorksheetController only takes in a WorksheetModel (editable) and has a setView method which takes in a view, adds features to it, and renders it
  - We choose to have the controller only take in a model because all of our listeners are in our view and the controller doesn't need to know the specifics of the actions that occur
  - We allow the view and controller to interact with the addFeatures method in the view which handles communication between view and controller
  - This makes it so that the controller only gets specific information from the view about changes to the model the user wants to make

EXTRA CREDIT
- Added ability to use keyboard arrows to move currently clicked Cell after a Cell has already been selected by the mouse
- Added ability to use delete button to delete a Cell
- Added an upload button which allows a user to upload a file if the file is compatible
  - The file will open in a new window since this is what Excel does if a user opens a file
- Added a save button which allows a user to choose what they want to name their file and save it