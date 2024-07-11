package cellsociety.model.cells;

/**
 * This is a child of the Cell class.  It represents the Model of Segregation simulation and
 * contains state (empty, group A, and group B) and behavior (setting the current state) specific to
 * this simulation.
 */
public class ModelOfSegregationCell extends Cell {

  public static final int EMPTY = 0;
  public static final int GROUP_A = 1;
  public static final int GROUP_B = 2;

  /**
   * Constructor for ModelOfSegregationCell
   *
   * @param row   row position
   * @param col   column position
   * @param state original state of cell
   */
  public ModelOfSegregationCell(int row, int col, int state) {
    super(row, col, state);
  }

  /**
   * Method to set cell's next state to empty
   */
  public void setNextStateEmpty() {
    nextState = EMPTY;
  }

  /**
   * Method to set cell's next state to group A
   */
  public void setNextStateGroupA() {
    nextState = GROUP_A;
  }

  /**
   * Method to set cell's next state to group B
   */
  public void setNextStateGroupB() {
    nextState = GROUP_B;
  }

  /**
   * Method to set cell's next state as its current state
   */
  public void setNextStateSame() {
    nextState = currentState;
  }

  /**
   * Method to get cell's opposite group
   *
   * @return the int corresponding to the opposite group or -1 if the cell is empty
   */
  public int getOppositeGroup() {
    if (currentState == GROUP_A) {
      return GROUP_B;
    } else if (currentState == GROUP_B) {
      return GROUP_A;
    }
    return -1;
  }
}
