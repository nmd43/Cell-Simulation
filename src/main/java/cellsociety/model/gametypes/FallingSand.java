package cellsociety.model.gametypes;


import cellsociety.model.cells.FallingSandCell;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is a child of the GameType class.  It represents the Falling Sand simulation.  It implements
 * abstract methods and declares helper functions.
 */
public class FallingSand extends GameType<FallingSandCell> {

  public static final int TOTAL_STATES = 4;

  /**
   * Constructor for Falling Sand
   *
   * @param parserGrid 2D array of cell starting states as integers
   */
  public FallingSand(int[][] parserGrid) {
    super(parserGrid);
    this.setNumGameStateIterations(1);
  }

  /**
   * Determines the cell's next state and sets the cell's nextState field appropriately.
   * <p>
   *
   * @param currCell The cell whose next state will be set
   */
  public void setCellNextState(FallingSandCell currCell) {

    if (currCell.getCurrentState() == FallingSandCell.EMPTY && currCell.nextStateNull()) {
      currCell.setNextStateEmpty();
    } else if (currCell.getCurrentState() == FallingSandCell.METAL) {
      currCell.setNextStateMetal();
    } else if (currCell.getCurrentState() == FallingSandCell.SAND) {
      moveSand(currCell);
    } else if (currCell.getCurrentState() == FallingSandCell.WATER) {
      moveWater(currCell);
    }
  }

  private void moveSand(FallingSandCell currCell) {
    int currRow = currCell.getRowPos();
    int currCol = currCell.getColPos();
    if (currRow < getGrid().length - 1) {
      FallingSandCell belowCell = getGrid()[currRow + 1][currCol];
      if (belowCell.nextStateEmpty()) {
        belowCell.setNextStateSand();
        currCell.setNextStateEmpty();
      } else if (belowCell.nextStateWater()) {
        belowCell.setNextStateWater();
        currCell.setNextStateEmpty();
      }
    } else {
      currCell.setNextStateSand();
    }

  }

  private void moveWater(FallingSandCell currCell) {
    List<FallingSandCell> emptyNeighbors = getWaterNeighbors(currCell);
    if (!emptyNeighbors.isEmpty()) {
      int randIndex = random.nextInt(emptyNeighbors.size());
      FallingSandCell newCell = emptyNeighbors.get(randIndex);
      newCell.setNextStateWater();
      currCell.setNextStateEmpty();
    } else {
      currCell.setNextStateWater();
    }
  }

  private List<FallingSandCell> getWaterNeighbors(FallingSandCell currCell) {
    FallingSandCell[][] grid = getGrid();
    List<FallingSandCell> neighbors = new ArrayList<>();
    int row = currCell.getRowPos();
    int col = currCell.getColPos();

    if (row < grid.length - 1 && grid[row + 1][col].canBeWater()) {
      neighbors.add(grid[row + 1][col]);
    }

    if (col > 0 && grid[row][col - 1].canBeWater()) {
      neighbors.add(grid[row][col - 1]);
    }

    if (col < grid[0].length - 1 && grid[row][col + 1].canBeWater()) {
      neighbors.add(grid[row][col + 1]);
    }
    return neighbors;
  }

  /**
   * Creates a grid of FallingSandCell objects based on the provided parserGrid. (Empty)
   *
   * @param rows    the rows in the grid
   * @param columns the columns in the grid
   */
  protected FallingSandCell[][] createCellGridStructure(int rows, int columns) {
    return new FallingSandCell[rows][columns];
  }

  /**
   * Creates a new FallingSandCell in the specified row, col position in the grid with the specified
   * state.
   *
   * @param row   the current row
   * @param col   the current column
   * @param state the original cell state
   * @return GameOfLifeCell with set row, column, and state
   */
  protected FallingSandCell createCell(int row, int col, int state) {
    return new FallingSandCell(row, col, state);
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
