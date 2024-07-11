package cellsociety.model.cells;

/**
 * This is a child of the Cell class.  It represents the Game of Life simulation and contains state
 * (alive and dead) and behavior (setting the current state) specific to this simulation.
 */
public class GameOfLifeCell extends Cell {

  public static final int ALIVE = 1;
  public static final int DEAD = 0;

  /**
   * Constructor for GameOfLifeCell
   *
   * @param row   row position
   * @param col   column position
   * @param state original state of cell
   */
  public GameOfLifeCell(int row, int col, int state) {
    super(row, col, state);
  }

  /**
   * Method to set cell's next state to alive
   */
  public void setNextStateAlive() {
    nextState = ALIVE;
  }

  /**
   * Method to set cell's next state to dead
   */
  public void setNextStateDead() {
    nextState = DEAD;
  }
}
