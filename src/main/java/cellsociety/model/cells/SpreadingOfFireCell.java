package cellsociety.model.cells;

/**
 * This is a child of the Cell class.  It represents the Spreading of Fire simulation and contains
 * state (burning, tree, and empty) and behavior (setting the current state) specific to this
 * simulation.
 */
public class SpreadingOfFireCell extends Cell {

  public static final int EMPTY = 0;
  public static final int TREE = 1;
  public static final int BURNING = 2;

  /**
   * Constructor for SpreadingOfFireCell
   *
   * @param row   row position
   * @param col   column position
   * @param state original state to be set
   */
  public SpreadingOfFireCell(int row, int col, int state) {
    super(row, col, state);
  }

  /**
   * Method to set cell's next state to empty
   */
  public void setNextStateEmpty() {
    nextState = EMPTY;
  }

  /**
   * Method to set cell's next state to tree
   */
  public void setNextStateTree() {
    nextState = TREE;
  }

  /**
   * Method to set cell's next state to burning
   */
  public void setNextStateBurning() {
    nextState = BURNING;
  }

}
