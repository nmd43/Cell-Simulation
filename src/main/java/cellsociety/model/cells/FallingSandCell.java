package cellsociety.model.cells;

/**
 * This is a child of the Cell class.  It represents the Falling Sand simulation and contains state
 * (empty, sand, metal, and water) and behavior (setting the current state) specific to this
 * simulation.
 */
public class FallingSandCell extends Cell {

  public static final int EMPTY = 0;
  public static final int SAND = 1;
  public static final int METAL = 2;
  public static final int WATER = 3;

  /**
   * Constructor for foraging ants cell
   *
   * @param row   row of cell
   * @param col   column of cell
   * @param state original state of cell
   */
  public FallingSandCell(int row, int col, int state) {
    super(row, col, state);
  }

  /**
   * Method to set next state to empty
   */
  public void setNextStateEmpty() {
    nextState = EMPTY;
  }

  /**
   * Method to set next state to sand
   */
  public void setNextStateSand() {
    nextState = SAND;
  }

  /**
   * Method to set next state to metal
   */
  public void setNextStateMetal() {
    nextState = METAL;
  }

  /**
   * Method to set next state to water
   */
  public void setNextStateWater() {
    nextState = WATER;
  }

  /**
   * Method to determine if a cell can become water
   *
   * @return True if cell can become water, false if not
   */
  public boolean canBeWater() {
    if (currentState != METAL) {
      return (nextState != WATER);
    }
    return false;
  }

  /**
   * Method to determine if next state is empty
   *
   * @return True if next state empty, false if not
   */
  public boolean nextStateEmpty() {
    return (nextState == EMPTY);
  }

  /**
   * Method to determine if next state is water
   *
   * @return True if next state water, false if not
   */
  public boolean nextStateWater() {
    return (nextState == WATER);
  }
}
