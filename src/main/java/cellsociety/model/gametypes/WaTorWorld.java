package cellsociety.model.gametypes;

import cellsociety.model.cells.WaTorWorldCell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This is a child of the GameType class.  It represents the Wa-Tor World simulation.  It implements
 * abstract methods and declares helper functions.
 * <p>
 * Currently, working on bug where states are updated inaccurately and fish do not see accurate
 * current states
 */

public class WaTorWorld extends GameType<WaTorWorldCell> {

  private int reproductionAgeLimit;
  private int startingEnergy;
  private int fishEnergy;
  private boolean sharksHaveMoved;
  public static final int TOTAL_STATES = 3;
  private final List<String> params = new ArrayList<>(Arrays.asList("reproductionAgeLimit",
      "startingEnergy", "fishEnergy"));

  /**
   * Constructor for WaTorWorld
   *
   * @param parserGrid 2D array of cell starting states as integers
   */
  public WaTorWorld(int[][] parserGrid, int reproductionAgeLimit, int startingEnergy,
      int fishEnergy) {
    super(parserGrid);
    this.reproductionAgeLimit = reproductionAgeLimit;
    this.startingEnergy = startingEnergy;
    this.fishEnergy = fishEnergy;
    setNumGameStateIterations(2);
    sharksHaveMoved = false;
  }


  /**
   * Determines the cell's next state and sets the nextState fields appropriately.  The rules are as
   * follows:
   * <p>
   * For the fish:
   * <p>
   * At each chronon, a fish moves randomly to one of the adjacent unoccupied squares. If there are
   * no free squares, no movement takes place.
   * <p>
   * Once a fish has survived a certain number of chronons it may reproduce. This is done as it
   * moves to a neighbouring square, leaving behind a new fish in its old position. Its reproduction
   * time is also reset to zero.
   * <p>
   * For the sharks:
   * <p>
   * At each chronon, a shark moves randomly to an adjacent square occupied by a fish. If there is
   * none, the shark moves to a random adjacent unoccupied square. If there are no free squares, no
   * movement takes place.
   * <p>
   * At each chronon, each shark is deprived of a unit of energy. Upon reaching zero energy, a shark
   * dies.
   * <p>
   * If a shark moves to a square occupied by a fish, it eats the fish and earns a certain amount of
   * energy.
   * <p>
   * Once a shark has survived a certain number of chronons it may reproduce in exactly the same way
   * as the fish.
   *
   * @param currCell The cell who is currently being analyzed to determine next states
   */
  public void setCellNextState(WaTorWorldCell currCell) {
    if (sharksHaveMoved) {
      fishMovement(currCell);
    } else {
      sharkMovement(currCell);
    }
    setSharkMovementStatus(currCell);
  }

  /**
   * Performs movement for a shark in the WaTorWorld simulation.
   *
   * @param currCell the current WaTorWorldCell
   */
  private void sharkMovement(WaTorWorldCell currCell) {
    if (currCell.getCurrentState() == WaTorWorldCell.SHARK) {
      int fishNeighbors = countUpAndDownNeighbors(currCell, WaTorWorldCell.FISH);
      int sharkNeighbors = countUpAndDownNeighbors(currCell, WaTorWorldCell.SHARK);

      if (currCell.getEnergy() == 0) {
        currCell.setNextStateEmpty();
      } else if (fishNeighbors > 0) {
        eatFish(currCell);
      } else if (surrounded(currCell, sharkNeighbors)) {
        currCell.setNextStateShark(currCell.getReproductionAge() + 1,
            currCell.getEnergy() - 1);
      } else {
        moveRandomShark(currCell);
      }
    } else if (currCell.getCurrentState() == WaTorWorldCell.EMPTY && currCell.nextStateNull()) {
      currCell.setNextStateEmpty();
    } else if (currCell.getCurrentState() == WaTorWorldCell.FISH && currCell.nextStateNull()) {
      currCell.setNextStateFish(currCell.getReproductionAge());
    }
  }

  /**
   * Performs movement for a fish in the WaTorWorld simulation.
   *
   * @param currCell the current WaTorWorldCell
   */
  private void fishMovement(WaTorWorldCell currCell) {
    if (currCell.getCurrentState() == WaTorWorldCell.FISH) {
      int occupiedNeighbors = 0;
      occupiedNeighbors += countUpAndDownNeighbors(currCell, WaTorWorldCell.FISH);
      occupiedNeighbors += countUpAndDownNeighbors(currCell, WaTorWorldCell.SHARK);

      if (surrounded(currCell, occupiedNeighbors)) {
        currCell.setNextStateFish(currCell.getReproductionAge() + 1);
      } else {
        moveRandomFish(currCell);
      }
    } else if (currCell.getCurrentState() == WaTorWorldCell.EMPTY && currCell.nextStateNull()) {
      currCell.setNextStateEmpty();
    } else if (currCell.getCurrentState() == WaTorWorldCell.SHARK) {
      currCell.setNextStateShark(currCell.getReproductionAge(), currCell.getEnergy());
    }
  }

  /**
   * Moves the shark to a random empty neighbor or updates its state.
   *
   * @param currCell the current WaTorWorldCell
   */
  private void moveRandomShark(WaTorWorldCell currCell) {
    List<WaTorWorldCell> emptyCells = getEmptyNeighbors(currCell);
    int size = emptyCells.size();

    if (size == 0) {
      currCell.setNextStateShark(currCell.getReproductionAge() + 1,
          currCell.getEnergy() + 1);
    } else {
      int randomCellIndex = random.nextInt(size);
      emptyCells.get(randomCellIndex)
          .setNextStateShark(currCell.getReproductionAge() + 1,
              currCell.getEnergy() - 1);

      if (currCell.getReproductionAge() >= reproductionAgeLimit) {
        emptyCells.get(randomCellIndex).resetReproductionAge();
        currCell.setNextStateShark(0, startingEnergy);
      } else {
        currCell.setNextStateEmpty();
      }
    }
  }

  /**
   * Moves the fish to a random empty neighbor or updates its state.
   *
   * @param currCell the current WaTorWorldCell
   */
  private void moveRandomFish(WaTorWorldCell currCell) {
    List<WaTorWorldCell> emptyCells = getEmptyNeighbors(currCell);
    int size = emptyCells.size();

    if (size == 0) {
      currCell.setNextStateFish(currCell.getReproductionAge() + 1);
    } else {
      int randomCellIndex = random.nextInt(size);
      emptyCells.get(randomCellIndex).setNextStateFish(currCell.getReproductionAge() + 1);

      if (currCell.getReproductionAge() >= reproductionAgeLimit) {
        emptyCells.get(randomCellIndex).resetReproductionAge();
        currCell.setNextStateFish(0);
      } else {
        currCell.setNextStateEmpty();
      }
    }
  }

  /**
   * Eats a fish neighboring the shark cell.
   *
   * @param currCell the current WaTorWorldCell representing the shark
   */
  private void eatFish(WaTorWorldCell currCell) {
    List<WaTorWorldCell> fishCells = getFishNeighbors(currCell);
    int size = fishCells.size();
    if (size == 0) {
      currCell.setNextStateShark(currCell.getReproductionAge() + 1,
          currCell.getEnergy() - 1);
    } else {
      int randomCellIndex = random.nextInt(size);
      fishCells.get(randomCellIndex)
          .setNextStateShark(currCell.getReproductionAge() + 1,
              currCell.getEnergy() + fishEnergy);
      currCell.setNextStateEmpty();
    }
  }

  /**
   * Retrieves neighboring fish cells.
   *
   * @param currCell the current WaTorWorldCell
   * @return a list of neighboring fish cells
   */
  private List<WaTorWorldCell> getFishNeighbors(WaTorWorldCell currCell) {
    if (edgeType == EdgeType.WRAPPED) {
      return getFishNeighborsWrapped(currCell);
    }
    WaTorWorldCell[][] grid = this.getGrid();
    List<WaTorWorldCell> fishNeighbors = new ArrayList<>();

    int row = currCell.getRowPos();
    int col = currCell.getColPos();

    if (row > 0 && grid[row - 1][col].fishAlive()) {
      fishNeighbors.add(grid[row - 1][col]);
    }

    if (row < grid.length - 1 && grid[row + 1][col].fishAlive()) {
      fishNeighbors.add(grid[row + 1][col]);
    }

    if (col > 0 && grid[row][col - 1].fishAlive()) {
      fishNeighbors.add(grid[row][col - 1]);
    }

    if (col < grid[0].length - 1 && grid[row][col + 1].fishAlive()) {
      fishNeighbors.add(grid[row][col + 1]);
    }
    return fishNeighbors;
  }

  private List<WaTorWorldCell> getFishNeighborsWrapped(WaTorWorldCell currCell) {
    WaTorWorldCell[][] grid = this.getGrid();
    List<WaTorWorldCell> fishNeighbors = new ArrayList<>();

    int row = currCell.getRowPos();
    int col = currCell.getColPos();
    int numRows = grid.length;
    int numCols = grid[0].length;

    // Check north neighbor
    int northRow = (row - 1 + numRows) % numRows;
    if (grid[northRow][col].fishAlive()) {
      fishNeighbors.add(grid[northRow][col]);
    }

    // Check south neighbor
    int southRow = (row + 1) % numRows;
    if (grid[southRow][col].fishAlive()) {
      fishNeighbors.add(grid[southRow][col]);
    }

    // Check west neighbor
    int westCol = (col - 1 + numCols) % numCols;
    if (grid[row][westCol].fishAlive()) {
      fishNeighbors.add(grid[row][westCol]);
    }

    // Check east neighbor
    int eastCol = (col + 1) % numCols;
    if (grid[row][eastCol].fishAlive()) {
      fishNeighbors.add(grid[row][eastCol]);
    }

    return fishNeighbors;
  }


  /**
   * Retrieves neighboring empty cells.
   *
   * @param currCell the current WaTorWorldCell
   * @return a list of neighboring empty cells
   */
  private List<WaTorWorldCell> getEmptyNeighbors(WaTorWorldCell currCell) {
    if (edgeType == EdgeType.WRAPPED) {
      return getEmptyNeighborsWrapped(currCell);
    }
    WaTorWorldCell[][] grid = this.getGrid();
    List<WaTorWorldCell> emptyNeighbors = new ArrayList<>();

    int row = currCell.getRowPos();
    int col = currCell.getColPos();

    if (row > 0 && grid[row - 1][col].isAvailable(WaTorWorldCell.EMPTY)) {
      emptyNeighbors.add(grid[row - 1][col]);
    }

    if (row < grid.length - 1 && grid[row + 1][col].isAvailable(WaTorWorldCell.EMPTY)) {
      emptyNeighbors.add(grid[row + 1][col]);
    }

    if (col > 0 && grid[row][col - 1].isAvailable(WaTorWorldCell.EMPTY)) {
      emptyNeighbors.add(grid[row][col - 1]);
    }

    if (col < grid[0].length - 1 && grid[row][col + 1].isAvailable(WaTorWorldCell.EMPTY)) {
      emptyNeighbors.add(grid[row][col + 1]);
    }

    return emptyNeighbors;
  }

  private List<WaTorWorldCell> getEmptyNeighborsWrapped(WaTorWorldCell currCell) {
    WaTorWorldCell[][] grid = this.getGrid();
    List<WaTorWorldCell> emptyNeighbors = new ArrayList<>();

    int numRows = grid.length;
    int numCols = grid[0].length;

    int row = currCell.getRowPos();
    int col = currCell.getColPos();

    // Calculate row indices for neighbors with toroidal edges
    int rowUp = (row - 1 + numRows) % numRows;
    int rowDown = (row + 1) % numRows;

    // Calculate column indices for neighbors with toroidal edges
    int colLeft = (col - 1 + numCols) % numCols;
    int colRight = (col + 1) % numCols;

    // Check for empty neighbors
    if (grid[rowUp][col].isAvailable(WaTorWorldCell.EMPTY)) {
      emptyNeighbors.add(grid[rowUp][col]);
    }
    if (grid[rowDown][col].isAvailable(WaTorWorldCell.EMPTY)) {
      emptyNeighbors.add(grid[rowDown][col]);
    }
    if (grid[row][colLeft].isAvailable(WaTorWorldCell.EMPTY)) {
      emptyNeighbors.add(grid[row][colLeft]);
    }
    if (grid[row][colRight].isAvailable(WaTorWorldCell.EMPTY)) {
      emptyNeighbors.add(grid[row][colRight]);
    }

    return emptyNeighbors;
  }

  /**
   * Sets the status of shark movement based on the current cell's position.
   *
   * @param currCell the current WaTorWorldCell
   */
  private void setSharkMovementStatus(WaTorWorldCell currCell) {
    WaTorWorldCell[][] grid = this.getGrid();
    if (currCell.getRowPos() == grid.length - 1
        && currCell.getColPos() == grid[0].length - 1) {
      sharksHaveMoved = !sharksHaveMoved;
    }
  }

  /**
   * Determines if a cell is surrounded by occupied neighbors.
   *
   * @param currCell          the current WaTorWorldCell
   * @param occupiedNeighbors the number of occupied neighbors
   * @return true if the cell is surrounded, false otherwise
   */
  private boolean surrounded(WaTorWorldCell currCell, int occupiedNeighbors) {
    int numNeighbors = 0;

    numNeighbors += countUpAndDownNeighbors(currCell, WaTorWorldCell.SHARK);
    numNeighbors += countUpAndDownNeighbors(currCell, WaTorWorldCell.FISH);
    numNeighbors += countUpAndDownNeighbors(currCell, WaTorWorldCell.EMPTY);

    return (occupiedNeighbors == numNeighbors);
  }

  /**
   * Creates a grid of WaTorWorldCell objects based on the provided parserGrid.
   *
   * @param parserGrid the 2D array representing cell states
   */
  @Override
  public void createCellGrid(int[][] parserGrid) {
    int state;
    setGrid(createCellGridStructure(parserGrid.length, parserGrid[0].length));
    WaTorWorldCell[][] grid = getGrid();

    for (int currRow = 0; currRow < grid.length; currRow++) {
      for (int currCol = 0; currCol < grid[0].length; currCol++) {
        state = parserGrid[currRow][currCol];
        setGridCell(currRow, currCol, createCell(currRow, currCol, state));
        grid = getGrid();
        if (grid[currRow][currCol].getCurrentState() == WaTorWorldCell.FISH) {
          (grid[currRow][currCol]).resetReproductionAge();
        } else if (grid[currRow][currCol].getCurrentState() == WaTorWorldCell.SHARK) {
          (grid[currRow][currCol]).resetReproductionAge();
          (grid[currRow][currCol]).resetStartingEnergy(startingEnergy);
        }
      }
    }
  }

  /**
   * Creates a grid of WaTorWorldCell objects based on the provided parserGrid. (Empty)
   *
   * @param rows    the rows in the grid
   * @param columns the columns in the grid
   */
  protected WaTorWorldCell[][] createCellGridStructure(int rows, int columns) {
    return new WaTorWorldCell[rows][columns];
  }

  /**
   * Creates a new WaTorWorldCell in the specified row, col position in the grid with the specified
   * state.
   *
   * @param row   the current row in the grid
   * @param col   the current column in the grid
   * @param state the original state to set
   * @return A WaTorWorld Cell with set row, column, and state
   */
  protected WaTorWorldCell createCell(int row, int col, int state) {
    return new WaTorWorldCell(row, col, state);
  }

  public void updateParams(Map<String, Double> newParams) {
    for (Map.Entry<String, Double> e : newParams.entrySet()) {
      if (e.getKey().equals("reproductionAgeLimit")) {
        this.reproductionAgeLimit = (int) e.getValue().doubleValue();
      } else if (e.getKey().equals("startingEnergy")) {
        this.startingEnergy = (int) e.getValue().doubleValue();
      } else if (e.getKey().equals("fishEnergy")) {
        this.fishEnergy = (int) e.getValue().doubleValue();
      }
    }
  }

  public List<String> getParamList() {
    return params;
  }

  public int getTotalStates() {
    return TOTAL_STATES;
  }
}
