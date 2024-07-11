package cellsociety.model.gametypes;

import cellsociety.model.cells.GameOfLifeCell;
import java.util.List;
import java.util.Map;

/**
 * This is a child of the GameType class.  It represents the Game of Life simulation.  It implements
 * abstract methods and declares helper functions.
 */
public class GameOfLife extends GameType<GameOfLifeCell> {
  public static final int TOTAL_STATES = 2;

  /**
   * Constructor for Game of Life
   *
   * @param parserGrid 2D array of cell starting states as integers
   */
  public GameOfLife(int[][] parserGrid) {
    super(parserGrid);
    this.setNumGameStateIterations(1);
  }

  /**
   * Determines the cell's next state and sets the cell's nextState field appropriately.  The rules
   * are as follows:
   * <p>
   * Any live cell with fewer than two live neighbors dies, as if by underpopulation. Any live cell
   * with two or three live neighbors lives on to the next generation. Any live cell with more than
   * three live neighbors dies, as if by overpopulation. Any dead cell with exactly three live
   * neighbors becomes a live cell, as if by reproduction.
   *
   * @param currCell The cell whose next state will be set
   */
  public void setCellNextState(GameOfLifeCell currCell) {
    int numNeighborsAlive = getNumNeighborsAlive(currCell);

    if (currCell.getCurrentState() == GameOfLifeCell.ALIVE) {
      if (numNeighborsAlive < 2) {
        currCell.setNextStateDead();
      } else if (numNeighborsAlive == 2 || numNeighborsAlive == 3) {
        currCell.setNextStateAlive();
      } else {
        currCell.setNextStateDead();
      }
    } else if (currCell.getCurrentState() == GameOfLifeCell.DEAD) {
      if (numNeighborsAlive == 3) {
        currCell.setNextStateAlive();
      } else {
        currCell.setNextStateDead();
      }
    }
  }

  /**
   * Calculates the number of alive neighbors for a given GameOfLifeCell.
   *
   * @param currCell the current GameOfLifeCell
   * @return the number of alive neighbors
   */
  private int getNumNeighborsAlive(GameOfLifeCell currCell) {
    int count = 0;

    count += countUpAndDownNeighbors(currCell, GameOfLifeCell.ALIVE);
    count += countDiagonalNeighbors(currCell, GameOfLifeCell.ALIVE);

    return count;
  }

  /**
   * Creates a grid of GameOfLifeCell objects based on the provided parserGrid. (Empty)
   *
   * @param rows    the rows in the grid
   * @param columns the columns in the grid
   */
  protected GameOfLifeCell[][] createCellGridStructure(int rows, int columns) {
    return new GameOfLifeCell[rows][columns];
  }

  /**
   * Creates a new GameOfLifeCell in the specified row, col position in the grid with the specified
   * state.
   *
   * @param row   the current row
   * @param col   the current column
   * @param state the original cell state
   * @return GameOfLifeCell with set row, column, and state
   */
  protected GameOfLifeCell createCell(int row, int col, int state) {
    return new GameOfLifeCell(row, col, state);
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
