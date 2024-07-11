package cellsociety.model.gametypes;

import cellsociety.model.cells.ModelOfSegregationCell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This is a child of the GameType class.  It represents the Model of Segregation simulation.  It
 * implements abstract methods and declares helper functions.
 */

public class ModelOfSegregation extends GameType<ModelOfSegregationCell> {

  private double neighborPercentage;

  private final List<String> params = new ArrayList<>(Arrays.asList("neighborPercentage"));
  public static final int TOTAL_STATES = 3;

  /**
   * Constructor for ModelOfSegregation
   *
   * @param parserGrid         2D array of cell starting states as integers
   * @param neighborPercentage percentage of neighbors that must be of the same group as a decimal
   */
  public ModelOfSegregation(int[][] parserGrid, double neighborPercentage) {
    super(parserGrid);
    this.neighborPercentage = neighborPercentage;
    setNumGameStateIterations(1);
  }

  /**
   * Determines the cell's next state and sets the nextState fields appropriately.  The rules are as
   * follows:
   * <p>
   * Each round consists of agents checking their neighborhood to see if the fraction of neighbors F
   * that matches their group (ignoring empty spaces) is greater than or equal to
   * neighborPercentage.  If F < neighborPercentage then the agent will choose to relocate to a
   * vacant spot where F>= neighborPercentage
   *
   * @param currCell The cell who is currently being analyzed to determine next states
   */
  public void setCellNextState(ModelOfSegregationCell currCell) {
    if (currCell.getCurrentState() != ModelOfSegregationCell.EMPTY) {
      if (!neighborsMeetThreshold(currCell)) {
        findNewLocation(currCell);
        //currCell.setNextStateEmpty();
      } else {
        currCell.setNextStateSame();
      }
    } else if (currCell.isAvailable(ModelOfSegregationCell.EMPTY)) {
      currCell.setNextStateEmpty();
    }
  }

  /**
   * Calculates the number of neighbors in the specified group for a given ModelOfSegregationCell.
   *
   * @param currCell the current ModelOfSegregationCell
   * @param group    the group to count neighbors for
   * @return the number of neighbors in the specified group
   */
  private int getNumNeighborsInGroup(ModelOfSegregationCell currCell, int group) {
    int count = 0;

    count += countUpAndDownNeighbors(currCell, group);
    count += countDiagonalNeighbors(currCell, group);

    return count;
  }

  /**
   * Determines whether the neighbors of the given ModelOfSegregationCell meet the threshold for
   * similarity.
   *
   * @param currCell the current ModelOfSegregationCell
   * @return true if the neighbors meet the threshold for similarity, false otherwise
   */
  private boolean neighborsMeetThreshold(ModelOfSegregationCell currCell) {
    double sameGroup = getNumNeighborsInGroup(currCell, currCell.getCurrentState());
    double diffGroup = getNumNeighborsInGroup(currCell, currCell.getOppositeGroup());
    double similarity = (sameGroup / (sameGroup + diffGroup));

    return similarity >= neighborPercentage;
  }

  /**
   * Finds a new location for the moving cell based on available empty cells in the grid.
   *
   * @param movingCell the cell to be moved
   */
  private void findNewLocation(ModelOfSegregationCell movingCell) {
    List<ModelOfSegregationCell> availableCells = new ArrayList<>();
    ModelOfSegregationCell[][] grid = this.getGrid();

    for (ModelOfSegregationCell[] currRow : grid) {
      for (ModelOfSegregationCell currCell : currRow) {
        if (currCell.isAvailable(ModelOfSegregationCell.EMPTY)) {
          availableCells.add(currCell);
        }
      }
    }

    if (!availableCells.isEmpty()) {
      // Shuffle the list to achieve randomness
      Collections.shuffle(availableCells);

      // Move the agent to the randomly selected empty cell (the first one after shuffling)
      ModelOfSegregationCell randomAvailableCell = availableCells.get(0);
      if (movingCell.getCurrentState() == ModelOfSegregationCell.GROUP_A) {
        randomAvailableCell.setNextStateGroupA();
      } else {
        randomAvailableCell.setNextStateGroupB();
      }
      movingCell.setNextStateEmpty();
    }
  }

  /**
   * Creates a grid of ModelOfSegregationCell objects based on the provided parserGrid. (Empty)
   *
   * @param rows    the rows in the grid
   * @param columns the columns in the grid
   */
  protected ModelOfSegregationCell[][] createCellGridStructure(int rows, int columns) {
    return new ModelOfSegregationCell[rows][columns];
  }

  /**
   * Creates a new ModelOfSegregationCell in the specified row, col position in the grid with the
   * specified state.
   *
   * @param row   the current row
   * @param col   the current column
   * @param state the original state
   * @return ModelOfSegregationCell with set row, column, and state
   */
  protected ModelOfSegregationCell createCell(int row, int col, int state) {
    return new ModelOfSegregationCell(row, col, state);
  }

  public void updateParams(Map<String, Double> newParams) {
    for (Map.Entry<String, Double> e : newParams.entrySet()) {
      if (e.getKey().equals("neighborPercentage")) {
        this.neighborPercentage = e.getValue();
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
