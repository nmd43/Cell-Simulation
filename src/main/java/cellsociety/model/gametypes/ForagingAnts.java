package cellsociety.model.gametypes;

import cellsociety.model.cells.ForagingAntsCell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a child of the GameType class.  It represents the Foraging Ants simulation.  It
 * implements abstract methods and declares helper functions.
 */
public class ForagingAnts extends GameType<ForagingAntsCell> {

  private int currAntsInNest;
  private int antsInNest;
  private int maxPheromoneLevel;
  private int nestRow;
  private int nestCol;
  public static final int TOTAL_STATES = 5;

  private final List<String> params = new ArrayList<>(Arrays.asList("antsInNest",
      "maxPheromoneLevel"));

  /**
   * Constructor for Foraging Ants
   *
   * @param parserGrid 2D array of cell starting states as integers
   */
  public ForagingAnts(int[][] parserGrid, int antsInNest, int maxPheromoneLevel) {
    super(parserGrid);
    findNest();
    this.setNumGameStateIterations(1);
    this.antsInNest = antsInNest;
    currAntsInNest = antsInNest;
    this.maxPheromoneLevel = maxPheromoneLevel;
  }

  /**
   * Determines the cell's next state and sets the cell's nextState field appropriately.
   *
   * @param currCell The cell whose next state will be set
   */
  public void setCellNextState(ForagingAntsCell currCell) {
    if (currCell.getCurrentState() == ForagingAntsCell.NEST) {
      currCell.setNextStateNest(currCell.getHomePheromoneLevel());
      currAntsInNest = moveAntsInNest();
    } else if (currCell.getCurrentState() == ForagingAntsCell.FOOD && currCell.nextStateNull()) {
      currCell.setNextStateFood();
      currCell.setPheromoneLevels(maxPheromoneLevel, currCell.getHomePheromoneLevel());
    } else if (currCell.getCurrentState() == ForagingAntsCell.EMPTY && currCell.nextStateNull()) {
      currCell.setNextStateEmpty();
    } else if (currCell.getCurrentState() == ForagingAntsCell.PHEROMONE
        && currCell.nextStateNull()) {
      decreasePheromones(currCell);
    } else if (currCell.getCurrentState() == ForagingAntsCell.ANT) {
      if (currCell.hasFood()) {
        returnToNest(currCell);
      } else {
        findFood(currCell);
      }
    }
  }

  private void decreasePheromones(ForagingAntsCell currCell) {
    int homeLevel;
    int foodLevel;
    if (currCell.getHomePheromoneLevel() > 0) {
      homeLevel = currCell.getHomePheromoneLevel() - 1;
    } else {
      homeLevel = 0;
    }
    if (currCell.getFoodPheromoneLevel() > 0) {
      foodLevel = currCell.getFoodPheromoneLevel() - 1;
    } else {
      foodLevel = 0;
    }

    if (homeLevel > 0 || foodLevel > 0) {
      currCell.setPheromoneLevels(foodLevel, homeLevel);
      currCell.setNextStatePheromone();
    } else {
      currCell.setNextStateEmpty();
    }
  }

  private int moveAntsInNest() {
    Map<Integer, List<ForagingAntsCell>> availableCells = findNeighborPheromones(
        getGrid()[nestRow][nestCol], true);
    int newAntsInNest = currAntsInNest;
    for (int currAnt = 0; currAnt < currAntsInNest; currAnt++) {
      if (!availableCells.isEmpty()) {
        dropHomePheromones(getGrid()[nestRow][nestCol]);
        int maxLevel = getMaxInMap(availableCells);
        ForagingAntsCell movingCell = getCellWithMaxPheromones(availableCells);
        availableCells.get(maxLevel).remove(movingCell);
        if (availableCells.get(maxLevel).isEmpty()) {
          availableCells.remove(maxLevel);
        }
        int[] orientation = {movingCell.getRowPos() - nestRow, movingCell.getColPos() - nestCol};
        movingCell.setNextStateAnt(false, false, orientation);
        newAntsInNest--;
      }
    }
    return newAntsInNest;
  }

  private void findFood(ForagingAntsCell currCell) {
    Map<Integer, List<ForagingAntsCell>> neighbors = findNeighborPheromones(currCell, true);
    if (!neighbors.isEmpty()) {
      dropHomePheromones(currCell);
      ForagingAntsCell movingCell = moveTowardsFood(currCell);
      if (movingCell.getCurrentState() == ForagingAntsCell.FOOD) {
        movingCell.setNextStateAnt(true, true, currCell.getOrientation());
      } else if (atNest(movingCell.getRowPos(), movingCell.getColPos())) {
        currAntsInNest++;
      } else {
        movingCell.setNextStateAnt(false, false, currCell.getOrientation());
      }
    } else {
      currCell.setNextStateAnt(false, false, currCell.getOrientation());
    }
  }

  private void dropFoodPheromones(ForagingAntsCell currCell) {
    int level;

    if (currCell.atFoodSource()) {
      level = maxPheromoneLevel;
    } else {
      Map<Integer, List<ForagingAntsCell>> neighbors = findNeighborPheromones(currCell, true);
      level = getMaxInMap(neighbors) - 2;
    }

    dropPheromones(currCell, level, true);
  }

  private void dropHomePheromones(ForagingAntsCell currCell) {
    int level;

    if (atNest(currCell.getRowPos(), currCell.getColPos())) {
      level = maxPheromoneLevel;
      currCell.setNextStateNest(level);
    } else {
      Map<Integer, List<ForagingAntsCell>> neighbors = findNeighborPheromones(currCell, false);
      level = getMaxInMap(neighbors) - 2;

      dropPheromones(currCell, level, false);
    }
  }

  private void dropPheromones(ForagingAntsCell currCell, int level, boolean foodPheromones) {
    if (level > 0) {
      if (foodPheromones) {
        currCell.setPheromoneLevels(level, currCell.getHomePheromoneLevel());
      } else {
        currCell.setPheromoneLevels(currCell.getFoodPheromoneLevel(), level);
      }
      if (currCell.nextStateNotAnt()) {
        currCell.setNextStatePheromone();
      }
    } else if (currCell.getFoodPheromoneLevel() > 0 || currCell.getHomePheromoneLevel() > 0) {
      currCell.setPheromoneLevels(currCell.getFoodPheromoneLevel(),
          currCell.getHomePheromoneLevel());
      if (currCell.nextStateNotAnt()) {
        currCell.setNextStatePheromone();
      }
    } else if (currCell.nextStateNotAnt()) {
      currCell.setNextStateEmpty();
    }
  }

  private void returnToNest(ForagingAntsCell currCell) {
    if (currCell.atFoodSource()) {
      changeOrientation(currCell, true);
    }

    Map<Integer, List<ForagingAntsCell>> neighbors = findNeighborPheromones(currCell, false);
    ForagingAntsCell maxCell = getCellWithMaxPheromones(neighbors);
    if (maxCell != null && maxCell.getHomePheromoneLevel() > 0) {
      dropFoodPheromones(currCell);
      ForagingAntsCell movingCell = moveTowardsNest(currCell);
      if (atNest(movingCell.getRowPos(), movingCell.getColPos())) {
        currAntsInNest++;
      } else {
        movingCell.setNextStateAnt(true, false, currCell.getOrientation());
      }
    } else {
      currCell.setNextStateAnt(true, currCell.atFoodSource(), currCell.getOrientation());
    }
  }

  private ForagingAntsCell moveTowardsNest(ForagingAntsCell currCell) {
    int currRow = currCell.getRowPos();
    int currCol = currCell.getColPos();
    int rowOffset = currCell.getOrientation()[0];
    int colOffset = currCell.getOrientation()[1];

    ForagingAntsCell movingCell;
    Map<Integer, List<ForagingAntsCell>> forwardCells = checkForwardCells(currRow, currCol,
        rowOffset, colOffset, false);
    forwardCells.remove(0);
    movingCell = getCellWithMaxPheromones(forwardCells);
    if (movingCell == null) {

      Map<Integer, List<ForagingAntsCell>> neighborCells = findNeighborPheromones(currCell,
          false);
      neighborCells.remove(0);
      movingCell = getCellWithMaxPheromones(neighborCells);
    }

    if (movingCell != null) {
      rowOffset = movingCell.getRowPos() - currRow;
      colOffset = movingCell.getColPos() - currCol;
      currCell.setOrientation(rowOffset, colOffset);
    }

    return movingCell;
  }

  public ForagingAntsCell moveTowardsFood(ForagingAntsCell currCell) {
    int currRow = currCell.getRowPos();
    int currCol = currCell.getColPos();
    int rowOffset = currCell.getOrientation()[0];
    int colOffset = currCell.getOrientation()[1];

    Map<Integer, List<ForagingAntsCell>> forwardCells = checkForwardCells(currRow, currCol,
        rowOffset, colOffset, true);

    ForagingAntsCell movingCell = getCellWithMaxPheromones(forwardCells);
    if (movingCell == null) {
      Map<Integer, List<ForagingAntsCell>> neighborCells = findNeighborPheromones(currCell,
          true);
      movingCell = getCellWithMaxPheromones(neighborCells);
    }

    if (movingCell != null) {
      rowOffset = movingCell.getRowPos() - currRow;
      colOffset = movingCell.getColPos() - currCol;
      currCell.setOrientation(rowOffset, colOffset);
    }

    return movingCell;
  }

  private Map<Integer, List<ForagingAntsCell>> checkForwardCells(int currRow, int currCol,
      int rowOffset,
      int colOffset, boolean foodPheromones) {
    Map<Integer, List<ForagingAntsCell>> forwardCells = new HashMap<>();
    if (rowOffset == 0 && (currCol + colOffset) < getGrid()[0].length
        && (currCol + colOffset) >= 0) {
      sameRowOrientation(currRow, currCol, colOffset, forwardCells, foodPheromones);

    } else if (colOffset == 0 && (currRow + rowOffset) >= 0
        && (currRow + rowOffset) < getGrid().length) {
      sameColOrientation(currRow, currCol, rowOffset, forwardCells, foodPheromones);
    } else {
      diagonalOrientation(currRow, currCol, rowOffset, colOffset, forwardCells, foodPheromones);
    }
    return forwardCells;
  }

  private void sameRowOrientation(int currRow, int currCol, int colOffset,
      Map<Integer, List<ForagingAntsCell>> forwardCells, boolean foodPheromones) {
    checkPheromoneLevel(getGrid()[currRow][currCol + colOffset], forwardCells, foodPheromones);

    if (currRow - 1 >= 0) {
      checkPheromoneLevel(getGrid()[currRow - 1][currCol + colOffset], forwardCells,
          foodPheromones);
    }

    if (currRow + 1 < getGrid().length) {
      checkPheromoneLevel(getGrid()[currRow + 1][currCol + colOffset], forwardCells,
          foodPheromones);
    }
  }

  private void sameColOrientation(int currRow, int currCol, int rowOffset,
      Map<Integer, List<ForagingAntsCell>> forwardCells, boolean foodPheromones) {
    checkPheromoneLevel(getGrid()[currRow + rowOffset][currCol], forwardCells, foodPheromones);

    if (currCol - 1 >= 0) {
      checkPheromoneLevel(getGrid()[currRow + rowOffset][currCol - 1], forwardCells,
          foodPheromones);
    }

    if (currCol + 1 < getGrid()[0].length) {
      checkPheromoneLevel(getGrid()[currRow + rowOffset][currCol + 1], forwardCells,
          foodPheromones);
    }
  }

  private void diagonalOrientation(int currRow, int currCol, int rowOffset, int colOffset,
      Map<Integer, List<ForagingAntsCell>> forwardCells, boolean foodPheromones) {
    if ((currRow + rowOffset) >= 0 && (currRow + rowOffset) < getGrid().length
        && (currCol + colOffset) >= 0 && (currCol + colOffset) < getGrid()[0].length) {

      checkPheromoneLevel(getGrid()[currRow + rowOffset][currCol + colOffset], forwardCells,
          foodPheromones);

      checkPheromoneLevel(getGrid()[currRow][currCol + colOffset], forwardCells,
          foodPheromones);

      checkPheromoneLevel(getGrid()[currRow + rowOffset][currCol], forwardCells,
          foodPheromones);
    }
  }

  private void findNest() {
    for (ForagingAntsCell[] cellRow : getGrid()) {
      for (ForagingAntsCell cell : cellRow) {
        if (cell.getCurrentState() == ForagingAntsCell.NEST) {
          nestRow = cell.getRowPos();
          nestCol = cell.getColPos();
        }
      }
    }
  }

  private boolean atNest(int row, int col) {
    return (row == nestRow && col == nestCol);
  }

  private void changeOrientation(ForagingAntsCell currCell, boolean foodPheromones) {
    ForagingAntsCell maxNeighbor;
    Map<Integer, List<ForagingAntsCell>> neighbors = findNeighborPheromones(currCell,
        foodPheromones);
    maxNeighbor = getCellWithMaxPheromones(neighbors);
    if (maxNeighbor != null) {
      int rowDir = maxNeighbor.getRowPos() - currCell.getRowPos();
      int colDir = maxNeighbor.getColPos() - currCell.getColPos();
      currCell.setOrientation(rowDir, colDir);
    }
  }

  private ForagingAntsCell getCellWithMaxPheromones(
      Map<Integer, List<ForagingAntsCell>> cells) {
    if (!cells.isEmpty()) {
      int maxPheromones = getMaxInMap(cells);
      List<ForagingAntsCell> maxCells = cells.get(maxPheromones);
      int randInt = random.nextInt(maxCells.size());
      return maxCells.get(randInt);
    }
    return null;
  }

  private int getMaxInMap(Map<Integer, List<ForagingAntsCell>> cells) {
    return cells.keySet().stream().mapToInt(Integer::intValue)
        .max().orElse(Integer.MIN_VALUE);
  }

  private Map<Integer, List<ForagingAntsCell>> findNeighborPheromones(
      ForagingAntsCell currCell,
      boolean foodPheromones) {

    Map<Integer, List<ForagingAntsCell>> neighborCells = findPerpendicularPheromones(
        currCell, foodPheromones);
    Map<Integer, List<ForagingAntsCell>> diagonalCells = findDiagonalPheromones(currCell,
        foodPheromones);

    for (int level : diagonalCells.keySet()) {
      if (neighborCells.containsKey(level)) {
        for (ForagingAntsCell cell : diagonalCells.get(level)) {
          neighborCells.get(level).add(cell);
        }
      } else {
        neighborCells.put(level, diagonalCells.get(level));
      }
    }

    return neighborCells;
  }

  private void checkPheromoneLevel(ForagingAntsCell currCell,
      Map<Integer, List<ForagingAntsCell>> map, boolean foodPheromones) {
    int currPheromoneLevel;

    if (foodPheromones) {
      currPheromoneLevel = currCell.getFoodPheromoneLevel();
    } else {
      currPheromoneLevel = currCell.getHomePheromoneLevel();
    }

    if (currCell.nextStateNotAnt()) {
      map.putIfAbsent(currPheromoneLevel, new ArrayList<>());
      map.get(currPheromoneLevel).add(currCell);
    }
  }

  private Map<Integer, List<ForagingAntsCell>> findPerpendicularPheromones(
      ForagingAntsCell currCell,
      boolean foodPheromones) {
    int row = currCell.getRowPos();
    int col = currCell.getColPos();
    Map<Integer, List<ForagingAntsCell>> perpendicularCells = new HashMap<>();

    if (row > 0) {
      checkPheromoneLevel(getGrid()[row - 1][col], perpendicularCells, foodPheromones);

    }

    if (row < getGrid().length - 1) {
      checkPheromoneLevel(getGrid()[row + 1][col], perpendicularCells, foodPheromones);
    }

    if (col > 0) {
      checkPheromoneLevel(getGrid()[row][col - 1], perpendicularCells, foodPheromones);
    }

    if (col < getGrid()[0].length - 1) {
      checkPheromoneLevel(getGrid()[row][col + 1], perpendicularCells, foodPheromones);
    }

    return perpendicularCells;
  }

  private Map<Integer, List<ForagingAntsCell>> findDiagonalPheromones(ForagingAntsCell currCell,
      boolean foodPheromones) {
    int row = currCell.getRowPos();
    int col = currCell.getColPos();
    Map<Integer, List<ForagingAntsCell>> diagonalCells = new HashMap<>();

    if (row > 0 && col > 0) {
      checkPheromoneLevel(getGrid()[row - 1][col - 1], diagonalCells, foodPheromones);
    }

    if (row > 0 && col < getGrid()[0].length - 1) {
      checkPheromoneLevel(getGrid()[row - 1][col + 1], diagonalCells, foodPheromones);
    }

    if (row < getGrid().length - 1 && col > 0) {
      checkPheromoneLevel(getGrid()[row + 1][col - 1], diagonalCells, foodPheromones);
    }

    if (row < getGrid().length - 1 && col < getGrid()[0].length - 1) {
      checkPheromoneLevel(getGrid()[row + 1][col + 1], diagonalCells, foodPheromones);
    }

    return diagonalCells;
  }

  /**
   * Creates a grid of ForagingAntsCell objects based on the provided parserGrid. (Empty)
   *
   * @param rows    the rows in the grid
   * @param columns the columns in the grid
   */
  protected ForagingAntsCell[][] createCellGridStructure(int rows, int columns) {
    currAntsInNest = antsInNest;
    return new ForagingAntsCell[rows][columns];
  }

  /**
   * Creates a new ForagingAntsCell in the specified row, col position in the grid with the
   * specified state.
   *
   * @param row   the current row
   * @param col   the current column
   * @param state the original cell state
   * @return GameOfLifeCell with set row, column, and state
   */
  protected ForagingAntsCell createCell(int row, int col, int state) {
    return new ForagingAntsCell(row, col, state);
  }

  public void updateParams(Map<String, Double> newParams) {
    for (Map.Entry<String, Double> e : newParams.entrySet()) {
      if (e.getKey().equals("antsInNest")) {
        this.antsInNest = (int) e.getValue().doubleValue();
      } else if (e.getKey().equals("maxPheromoneLevel")) {
        this.maxPheromoneLevel = (int) e.getValue().doubleValue();
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
