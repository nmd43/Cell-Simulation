package cellsociety.model.gametypes;

import cellsociety.model.cells.PercolationCell;
import java.util.List;
import java.util.Map;

/**
 * This is a child of the GameType class.  It represents the Percolation simulation.  It implements
 * abstract methods and declares helper functions.
 */
public class Percolation extends GameType<PercolationCell> {

  public static final int TOTAL_STATES = 3;

  /**
   * Constructor for Game of Life
   *
   * @param parserGrid 2D array of cell starting states as integers
   */
  public Percolation(int[][] parserGrid) {
    super(parserGrid);
    setNumGameStateIterations(1);
  }

  /**
   * Determines the cell's next state and sets the nextState fields appropriately.  The rules are as
   * follows:
   * <p>
   * If a cell contains water, it will always contain water
   * <p>
   * If a cell is blocked, it will always be blocked
   * <p>
   * If a cell has a neighbor with water, it will be filled with water
   *
   * @param currCell The cell who is currently being analyzed to determine next states
   */
  public void setCellNextState(PercolationCell currCell) {
    if (currCell.getCurrentState() == PercolationCell.BLOCKED) {
      currCell.setNextStateBlocked();
    } else if (currCell.getCurrentState() == PercolationCell.WATER) {
      currCell.setNextStateWater();
    } else if (currCell.getCurrentState() == PercolationCell.EMPTY) {
      if (getNumNeighborsWater(currCell) > 0) {
        currCell.setNextStateWater();
      } else {
        currCell.setNextStateEmpty();
      }
    }
  }

  /**
   * Calculates the number of water neighbors for a given PercolationCell.
   *
   * @param currCell the current PercolationCell
   * @return the number of water neighbors
   */
  private int getNumNeighborsWater(PercolationCell currCell) {
    int count = 0;

    count += countUpAndDownNeighbors(currCell, PercolationCell.WATER);
    count += countDiagonalNeighbors(currCell, PercolationCell.WATER);

    return count;
  }

  /**
   * Creates a grid of PercolationCell objects based on the provided parserGrid. (Empty)
   *
   * @param rows    the rows in the grid
   * @param columns the columns in the grid
   */
  protected PercolationCell[][] createCellGridStructure(int rows, int columns) {
    return new PercolationCell[rows][columns];
  }

  /**
   * Creates a new PercolationCell in the specified row, col position in the grid with the specified
   * state.
   *
   * @param row   The current row
   * @param col   The current column
   * @param state The current state
   * @return PercolationCell with set row, column, and state
   */
  protected PercolationCell createCell(int row, int col, int state) {
    return new PercolationCell(row, col, state);
  }

  public void updateParams(Map<String, Double> newParams) {
    return;
  }

  public List<String> getParamList() {
    return null;
  }

  public int getTotalStates() {
    return TOTAL_STATES;
  }
}
