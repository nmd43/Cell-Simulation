package cellsociety.model.gametypes;

import cellsociety.model.cells.SpreadingOfFireCell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This is a child of the GameType class.  It represents the Spreading of Fire simulation.  It
 * implements abstract methods and declares helper functions.
 */
public class SpreadingOfFire extends GameType<SpreadingOfFireCell> {

  private double probCatch;
  private double probGrow;
  public static final int TOTAL_STATES = 3;

  private final List<String> params = new ArrayList<>(Arrays.asList("probCatch", "probFillTree"));

  /**
   * Constructor for Spreading of Fire
   *
   * @param parserGrid 2D array of cell starting states as integers
   * @param probCatch  probability of a tree catching fire as a decimal
   * @param probGrow   probability of a tree growing as a decimal
   */
  public SpreadingOfFire(int[][] parserGrid, double probCatch, double probGrow) {
    super(parserGrid);
    this.probCatch = probCatch;
    this.probGrow = probGrow;
    setNumGameStateIterations(1);
  }

  /**
   * Determines the cell's next state and sets the cell's nextState field appropriately.  The rules
   * are as follows:
   * <p>
   * A burning cell turns into an empty cell A tree will burn if at least one neighbor is burning A
   * tree ignites with probability probCatch even if no neighbor is burning An empty space fills
   * with a tree with probability probGrow
   *
   * @param currCell The cell whose next state will be set
   */
  public void setCellNextState(SpreadingOfFireCell currCell) {
    int numNeighborsAlive = getNumNeighborsBurning(currCell);

    if (currCell.getCurrentState() == SpreadingOfFireCell.BURNING) {
      currCell.setNextStateEmpty();
    } else if (currCell.getCurrentState() == SpreadingOfFireCell.TREE) {
      if (numNeighborsAlive >= 1 || determineRandomOutcome(probCatch)) {
        currCell.setNextStateBurning();
      } else {
        currCell.setNextStateTree();
      }
    } else if (currCell.getCurrentState() == SpreadingOfFireCell.EMPTY) {
      if (determineRandomOutcome(probGrow)) {
        currCell.setNextStateTree();
      } else {
        currCell.setNextStateEmpty();
      }
    }
  }

  /**
   * Calculates the number of burning neighbors for a given cell.
   *
   * @param currCell the current cell
   * @return the number of burning neighbors
   */
  private int getNumNeighborsBurning(SpreadingOfFireCell currCell) {
    int count = 0;
    count += countUpAndDownNeighbors(currCell, SpreadingOfFireCell.BURNING);
    return count;
  }

  /**
   * Creates a grid of SpreadingOfFireCell objects based on the provided parserGrid. (Empty)
   *
   * @param rows    the rows in the grid
   * @param columns the columns in the grid
   */
  protected SpreadingOfFireCell[][] createCellGridStructure(int rows, int columns) {
    return new SpreadingOfFireCell[rows][columns];
  }

  /**
   * Creates a new SpreadingOfFireCell in the specified row, col position in the grid with the
   * specified state.
   *
   * @param row   The current row
   * @param col   The current column
   * @param state The original state
   * @return SpreadingOfFireCell with set row, column, and state
   */
  protected SpreadingOfFireCell createCell(int row, int col, int state) {
    return new SpreadingOfFireCell(row, col, state);
  }

  /**
   * Gets the list of unique parameters for this simulation.
   */
  public List<String> getParamList() {
    return params;
  }

  public void updateParams(Map<String, Double> newParams) {
    for (Map.Entry<String, Double> e : newParams.entrySet()) {
      if (e.getKey().equals("probFillTree")) {
        this.probGrow = e.getValue();
      } else if (e.getKey().equals("probCatch")) {
        this.probCatch = e.getValue();
      }
    }
  }

  public int getTotalStates() {
    return TOTAL_STATES;
  }
}
