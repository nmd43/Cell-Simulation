package cellsociety.model.cells;

/**
 * This is a super class for the different types of simulation cells.  It contains shared state
 * (positioning and state) and behavior (switching next to current state) for all cells.
 */
public class Cell {

  private final int rowPos;
  private final int colPos;
  protected int currentState;
  protected int nextState;

  /**
   * Default Constructor
   */
  public Cell() {
    rowPos = -1;
    colPos = -1;
    currentState = -1;
    nextState = -1;
  }

  /**
   * Constructor to create a Cell
   *
   * @param row   The cell's row index in the grid
   * @param col   The cell's column index in the grid
   * @param state The cell's initial state
   */
  public Cell(int row, int col, int state) {
    rowPos = row;
    colPos = col;
    currentState = state;
    nextState = -1;
  }

  /**
   * This method sets the current state equal to the next state and resets the next state to -1
   */
  public void switchState() {
    if (nextState != -1) {
      currentState = nextState;
      nextState = -1;
    }
  }

  /**
   * This is a getter method for the current state
   *
   * @return int representing current state
   */
  public int getCurrentState() {
    return currentState;
  }

  /**
   * This is a getter method for the cell's row
   *
   * @return int representing the cell's row index in the grid
   */
  public int getRowPos() {
    return rowPos;
  }

  /**
   * This is a getter method for the cell's column
   *
   * @return int representing the cell's column index in the grid
   */
  public int getColPos() {
    return colPos;
  }

  /**
   * Method to determine if a cell is available to be occupied
   *
   * @return boolean corresponding to if cell is available
   */
  public boolean isAvailable(int state) {
    return (currentState == state && (nextState == -1 || nextState == state));
  }

  /**
   * Method to check if next state has been set
   *
   * @return True if next state has not been set, false if next state has been set
   */
  public boolean nextStateNull() {
    return (nextState == -1);
  }

  /**
   * Method to alter cell's current state for non-cell classes
   *
   * @param newState integer representation of new state to be set
   */
  public void manuallySetState(int newState) {
    this.currentState = newState;
  }
}
