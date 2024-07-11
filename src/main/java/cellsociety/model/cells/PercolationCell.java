package cellsociety.model.cells;

/**
 * This is a child of the Cell class.  It represents the Percolation simulation and contains state
 * (empty, blocked, water) and behavior (setting the current state) specific to this simulation.
 */
public class PercolationCell extends Cell {

  public static final int EMPTY = 0;
  public static final int BLOCKED = 1;
  public static final int WATER = 2;

  /**
   * Constructor for PercolationCell
   *
   * @param row   row position
   * @param col   column position
   * @param state original state to be set
   */
  public PercolationCell(int row, int col, int state) {
    super(row, col, state);
  }

  /**
   * Method to set cell's next state to empty
   */
  public void setNextStateEmpty() {
    nextState = EMPTY;
  }

  /**
   * Method to set cell's next state to water
   */
  public void setNextStateWater() {
    nextState = WATER;
  }

  /**
   * Method to set cell's next state to blocked
   */
  public void setNextStateBlocked() {
    nextState = BLOCKED;
  }


}
