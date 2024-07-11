package cellsociety.model.cells;

/**
 * This is a child of the Cell class.  It represents the Wa-Tor World simulation and contains state
 * (empty, water, fish, shark) and behavior (setting the current state) specific to this
 * simulation.
 */
public class WaTorWorldCell extends Cell {

  public static final int EMPTY = 0;
  public static final int FISH = 1;
  public static final int SHARK = 2;
  private int reproductionAge;
  private int energy;

  /**
   * Constructor for WatorWorldCell
   *
   * @param row   row position
   * @param col   column position
   * @param state original state of cell
   */
  public WaTorWorldCell(int row, int col, int state) {
    super(row, col, state);
  }

  /**
   * Method to set next state to empty
   */
  public void setNextStateEmpty() {
    reproductionAge = -1;
    energy = -1;
    nextState = EMPTY;
  }

  /**
   * Method to set next state to fish
   */
  public void setNextStateFish(int reproductionAge) {
    this.reproductionAge = reproductionAge;
    energy = -1;
    nextState = FISH;
  }

  /**
   * Method to set next state to shark
   */
  public void setNextStateShark(int reproductionAge, int energy) {
    this.reproductionAge = reproductionAge;
    this.energy = energy;
    nextState = SHARK;
  }

  /**
   * Getter method for reproduction age
   *
   * @return int representing reproducing years accumulated
   */
  public int getReproductionAge() {
    return reproductionAge;
  }

  /**
   * Getter method for energy
   *
   * @return the shark's energy as an int
   */
  public int getEnergy() {
    return energy;
  }

  /**
   * Method to set reproduction age to zero for after reproducing
   */
  public void resetReproductionAge() {
    reproductionAge = 0;
  }

  /**
   * Setting energy to default for initialization purposes
   *
   * @param startingEnergy the amount of energy sharks are spawned with
   */
  public void resetStartingEnergy(int startingEnergy) {
    energy = startingEnergy;
  }

  /**
   * Method to check if the fish has been eaten
   *
   * @return True if fish is alive, false if fish has been eaten
   */
  public boolean fishAlive() {
    return (currentState == FISH && nextState != SHARK);
  }


}
