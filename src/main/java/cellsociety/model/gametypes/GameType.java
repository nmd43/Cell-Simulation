package cellsociety.model.gametypes;

import cellsociety.model.cells.Cell;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * This is a super class for the different types of simulations.  It contains state (cell grid) and
 * behavior (updating the cell grid) that is shared among all simulations.
 */
public abstract class GameType<T extends Cell> {

  private T[][] grid;
  private int numGameStateIterations;
  protected final Random random = new Random();
  private Map<Integer, String> stateMap;
  protected EdgeType edgeType;
  private String arrangement = "";

  public enum EdgeType {
    STANDARD, WRAPPED
  }

  /**
   * Default Constructor
   */
  public GameType() {
  }

  /**
   * Constructor to create grid with initial Cell states
   *
   * @param parserGrid 2D array of cell starting states as integers
   */
  public GameType(int[][] parserGrid) {
    createCellGrid(parserGrid);
  }

  /**
   * This abstract method determines what a cell's next state should be and sets the cell's
   * nextState field appropriately.  It is abstract because the rules and states differ with each
   * simulation.
   *
   * @param cell The cell whose next state will be set
   */
  public abstract void setCellNextState(T cell);

  /**
   * This abstract method updates the parameters of a simulation
   *
   * @param newParams new parameter values
   */
  public abstract void updateParams(Map<String, Double> newParams);

  /**
   * This abstract method gets the current parameters of the simulation
   *
   * @return list of current parameters
   */
  public abstract List<String> getParamList();

  /**
   * THis abstract method gets the total number of states
   *
   * @return total number of states
   */
  public abstract int getTotalStates();

  /**
   * This abstract method creates an empty cell grid
   *
   * @param rows    number of rows
   * @param columns number of columns
   * @return empty cell grid
   */
  protected abstract T[][] createCellGridStructure(int rows, int columns);

  /**
   * This abstract method creates a cell
   *
   * @param row   row position
   * @param col   column position
   * @param state original state of cell
   * @return created cell object
   */
  protected abstract T createCell(int row, int col, int state);

  /**
   * This method creates the Cell grid from the state grid created by the parser class
   *
   * @param parserGrid 2D array of cell starting states as integers
   */
  public void createCellGrid(int[][] parserGrid) {
    int state;
    this.grid = createCellGridStructure(parserGrid.length, parserGrid[0].length);

    for (int currRow = 0; currRow < getGrid().length; currRow++) {
      for (int currCol = 0; currCol < getGrid()[0].length; currCol++) {
        state = parserGrid[currRow][currCol];
        this.setGridCell(currRow, currCol, createCell(currRow, currCol, state));
      }
    }
  }

  /**
   * This method creates an integer array representing the current states of the cells
   *
   * @return an integer array representing the current state of the grid
   */
  public int[][] createStateGrid() {
    int[][] stateGrid = new int[grid.length][grid[0].length];

    for (int currRow = 0; currRow < grid.length; currRow++) {
      for (int currCol = 0; currCol < grid[0].length; currCol++) {
        stateGrid[currRow][currCol] = grid[currRow][currCol].getCurrentState();
      }
    }

    return stateGrid;
  }

  /**
   * Returns a copy of the Cell grid of this game.
   */
  protected T[][] getGrid() {

    T[][] copyGrid = Arrays.copyOf(grid, grid.length);
    for (int i = 0; i < grid.length; i++) {
      copyGrid[i] = Arrays.copyOf(grid[i], grid[i].length);
    }
    return copyGrid;
  }

  /**
   * Sets the grid representing the grid of all cells for this game to a new grid.
   *
   * @param newGrid the new grid to set for the game.
   */
  protected void setGrid(T[][] newGrid) {
    T[][] copyGrid = Arrays.copyOf(newGrid, newGrid.length);
    for (int i = 0; i < newGrid.length; i++) {
      copyGrid[i] = Arrays.copyOf(newGrid[i], newGrid[i].length);
    }
    this.grid = copyGrid;
  }

  /**
   * Setter for the game's grid. Takes in a row and column position to insert the new Cell.
   *
   * @param row  int position of the row of the cell
   * @param col  int position of the column of the cell
   * @param cell the new cell to be inserted
   */
  protected void setGridCell(int row, int col, T cell) {
    this.grid[row][col] = cell;
  }

  /**
   * Resets the game grid to its initial state.
   *
   * @param parserGrid 2D array of cell starting states as integers
   */
  public void resetGame(int[][] parserGrid) {
    createCellGrid(parserGrid);
  }

  /**
   * Sets the number of iterations for this game.
   *
   * @param iterations the # of iterations
   */
  protected void setNumGameStateIterations(int iterations) {
    this.numGameStateIterations = iterations;
  }

  /**
   * This method acts as a synchronous update to the game state.  It first iterates through the grid
   * to determine every cell's next state.  It then iterates through the grid again to update every
   * cell's current state.
   */
  public int[][] updateGameState() {
    for (int currItr = 0; currItr < numGameStateIterations; currItr++) {
      for (int currRow = grid.length - 1; currRow >= 0; currRow--) {
        for (int currCol = 0; currCol < grid[0].length; currCol++) {
          setCellNextState(grid[currRow][currCol]);
        }
      }

      for (T[] cells : grid) {
        for (T currCell : cells) {
          currCell.switchState();
        }
      }
    }

    return createStateGrid();
  }

  /**
   * Method to set the edge type of the grid
   *
   * @param edgeType string representing the edge type
   */
  public void setEdgeType(String edgeType) {
    if (Objects.equals(edgeType, "wrapped")) {
      this.edgeType = EdgeType.WRAPPED;
    } else {
      this.edgeType = EdgeType.STANDARD;
    }
  }

  /**
   * This method checks the state of the neighbors up, down, left, and right.  It accounts for cells
   * on the edges of the grid.
   *
   * @param currCell The cell whose neighbors are being checked
   * @param state    The state to check for
   * @return The number of neighbors in the specified state
   */
  public int countUpAndDownNeighbors(T currCell, int state) {
    if (edgeType == EdgeType.WRAPPED) {
      return countUpAndDownNeighborsWrapped(currCell, state);
    }
    int row = currCell.getRowPos();
    int col = currCell.getColPos();
    int count = 0;

    if (row > 0) {
      if (grid[row - 1][col].getCurrentState() == state) {
        count++;
      }
    }

    if (row < grid.length - 1) {
      if (grid[row + 1][col].getCurrentState() == state) {
        count++;
      }
    }

    if (col > 0) {
      if (grid[row][col - 1].getCurrentState() == state) {
        count++;
      }
    }

    if (col < grid[0].length - 1) {
      if (grid[row][col + 1].getCurrentState() == state) {
        count++;
      }
    }

    return count;
  }

  /**
   * This method checks the state of the neighbors up, down, left, and right.  When a cell is on the
   * edge, the edge is assumed to wrap around
   *
   * @param currCell The cell whose neighbors are being checked
   * @param state    The state to check for
   * @return The number of neighbors in the specified state
   */
  public int countUpAndDownNeighborsWrapped(T currCell, int state) {
    int row = currCell.getRowPos();
    int col = currCell.getColPos();
    int count = 0;
    int numRows = grid.length;
    int numCols = grid[0].length;

    // Check neighbors in the row above
    int aboveRow = (row - 1 + numRows) % numRows; // Wrap around if row < 0
    if (grid[aboveRow][col].getCurrentState() == state) {
      count++;
    }

    // Check neighbors in the row below
    int belowRow = (row + 1) % numRows; // Wrap around if row exceeds grid height
    if (grid[belowRow][col].getCurrentState() == state) {
      count++;
    }

    // Check neighbors in the same row (left and right)
    if (grid[row][(col - 1 + numCols) % numCols].getCurrentState() == state) {
      count++; // Wrap around if col < 0
    }
    if (grid[row][(col + 1) % numCols].getCurrentState() == state) {
      count++; // Wrap around if col exceeds grid width
    }

    return count;
  }


  /**
   * This method checks the state of the diagonal neighbors.  It accounts for cells on the edges of
   * the grid.
   *
   * @param currCell The cell whose neighbors are being checked
   * @param state    The state to check for
   * @return The number of neighbors in the specified state
   */
  public int countDiagonalNeighbors(T currCell, int state) {

    if (arrangement.equals("vonneumann")) {
      return 0;
    } else if (edgeType == EdgeType.WRAPPED) {
      return countDiagonalNeighborsWrapped(currCell, state);
    }
    return countDiagonalNeighborsHelp(currCell, state);
  }

  /**
   * This method checks the state of diagonal neighbors.  When a cell is on the edge, the edge is
   * assumed to wrap around
   *
   * @param currCell The cell whose neighbors are being checked
   * @param state    The state to check for
   * @return The number of neighbors in the specified state
   */
  public int countDiagonalNeighborsWrapped(T currCell, int state) {
    if (arrangement.equals("vonneumann")) {
      return 0;
    }
    int row = currCell.getRowPos();
    int col = currCell.getColPos();
    int count = 0;
    int numRows = grid.length;
    int numCols = grid[0].length;

    // Check diagonal neighbors above
    int aboveRow = (row - 1 + numRows) % numRows; // Wrap around if row < 0
    if (col > 0 && grid[aboveRow][(col - 1 + numCols) % numCols].getCurrentState() == state) {
      count++;
    }
    if (col < numCols - 1 && grid[aboveRow][(col + 1) % numCols].getCurrentState() == state) {
      count++;
    }

    // Check diagonal neighbors below
    int belowRow = (row + 1) % numRows; // Wrap around if row exceeds grid height
    if (col > 0 && grid[belowRow][(col - 1 + numCols) % numCols].getCurrentState() == state) {
      count++;
    }
    if (col < numCols - 1 && grid[belowRow][(col + 1) % numCols].getCurrentState() == state) {
      count++;
    }

    return count;
  }

  /**
   * Method to randomly determine the outcome of an event
   *
   * @param probability the probability event takes place as a decimal
   * @return true if event takes place, false if it does not
   */
  public boolean determineRandomOutcome(double probability) {
    return (random.nextDouble() < probability);
  }

  /**
   * Method to calculate population statistics
   *
   * @return map with state (key) and population (value)
   */
  public Map<Integer, Integer> getPopulationStatistics() {
    Map<Integer, Integer> stats = new HashMap<>();
    for (T[] currRow : grid) {
      for (T cell : currRow) {
        int currState = cell.getCurrentState();
        stats.putIfAbsent(currState, 0);
        stats.put(currState, stats.get(currState) + 1);
      }
    }
    return stats;
  }

  public void incrementCell(int row, int col, int totalStates) {
    grid[row][col].manuallySetState((grid[row][col].getCurrentState() + 1) % totalStates);
  }

  /**
   * Method to set state map
   *
   * @param states map of states
   */
  public void setStateMap(Map<Integer, String> states) {
    this.stateMap = states;
  }

  /**
   * method to get state map
   *
   * @return map of states
   */
  public Map<Integer, String> getStateMap() {
    return stateMap;
  }

  /**
   * Method to set arrangement
   *
   * @param newArrangement String representing new arrangement
   */
  public void setArrangement(String newArrangement) {
    this.arrangement = newArrangement.replaceAll("\\s", "").toLowerCase();
  }

  private int countDiagonalNeighborsHelp(T currCell, int state) {
    int row = currCell.getRowPos();
    int col = currCell.getColPos();
    int count = 0;

    if (row > 0) {

      if (col > 0 && grid[row - 1][col - 1].getCurrentState() == state) {
        count++;
      }

      if (col < grid[0].length - 1 && grid[row - 1][col + 1].getCurrentState() == state) {
        count++;
      }
    }

    if (row < grid.length - 1) {

      if (col > 0 && grid[row + 1][col - 1].getCurrentState() == state) {
        count++;
      }

      if (col < grid[0].length - 1 && grid[row + 1][col + 1].getCurrentState() == state) {
        count++;
      }
    }

    return count;
  }
}
