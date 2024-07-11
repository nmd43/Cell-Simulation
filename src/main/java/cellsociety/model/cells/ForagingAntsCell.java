package cellsociety.model.cells;

import java.util.Arrays;

/**
 * This is a child of the Cell class.  It represents the Foraging Ants simulation and contains state
 * (food, ant, nest, and empty) and behavior (setting the current state) specific to this
 * simulation.
 */
public class ForagingAntsCell extends Cell {

  public static final int EMPTY = 0;
  public static final int NEST = 1;
  public static final int FOOD = 2;
  public static final int ANT = 3;
  public static final int PHEROMONE = 4;
  private boolean hasFood;
  private boolean atFoodSource;
  private int foodPheromoneLevel;
  private int homePheromoneLevel;
  private final int[] orientation = new int[2];

  /**
   * Constructor for ForagingAntsCell
   *
   * @param row   row position
   * @param col   column position
   * @param state original state of cell
   */
  public ForagingAntsCell(int row, int col, int state) {
    super(row, col, state);
    hasFood = false;
    atFoodSource = false;
    foodPheromoneLevel = 0;
    homePheromoneLevel = 0;
    orientation[0] = 0;
    orientation[1] = 1;
  }

  /**
   * Method to set next state to empty
   */
  public void setNextStateEmpty() {
    nextState = EMPTY;
    hasFood = false;
    atFoodSource = false;
    foodPheromoneLevel = 0;
    homePheromoneLevel = 0;
  }

  /**
   * Method to set next state to nest
   */
  public void setNextStateNest(int homePheromoneLevel) {
    nextState = NEST;
    this.homePheromoneLevel = homePheromoneLevel;
  }

  /**
   * Method to set next state to food
   */
  public void setNextStateFood() {
    nextState = FOOD;
  }

  /**
   * Method to set next state to ant
   *
   * @param hasFood      True if ant has food, false if not
   * @param atFoodSource True if ant is at food source, false if not
   * @param orientation  array representing ant's orientation
   */
  public void setNextStateAnt(boolean hasFood, boolean atFoodSource, int[] orientation) {
    nextState = ANT;
    this.hasFood = hasFood;
    this.atFoodSource = atFoodSource;
    this.orientation[0] = orientation[0];
    this.orientation[1] = orientation[1];
  }

  /**
   * Method to set next state to food pheromone
   */
  public void setNextStatePheromone() {
    nextState = PHEROMONE;
  }

  /**
   * Method to get if ant is holding food
   *
   * @return True if ant has food, false if not
   */
  public boolean hasFood() {
    return hasFood;
  }

  /**
   * Method to get if ant is at food source
   *
   * @return True if ant is at food source, false if not
   */
  public boolean atFoodSource() {
    return atFoodSource;
  }


  /**
   * Method to check if a cell's next state is occupied by an ant
   *
   * @return True if occupied by ant, false if not
   */
  public boolean nextStateNotAnt() {
    return (nextState != ANT);
  }

  /**
   * Method to get ant's orientation
   *
   * @return Array representing ant's orientation
   */
  public int[] getOrientation() {
    return Arrays.copyOf(orientation, orientation.length);
  }

  /**
   * Method to get food pheromone level
   *
   * @return int representing food pheromone level
   */
  public int getFoodPheromoneLevel() {
    return foodPheromoneLevel;
  }

  /**
   * Method to get home pheromone level
   *
   * @return int representing home pheromone level
   */
  public int getHomePheromoneLevel() {
    return homePheromoneLevel;
  }

  /**
   * Method to set pheromone levels
   *
   * @param homeLevel level of home pheromones
   * @param foodLevel level of food pheromones
   */
  public void setPheromoneLevels(int foodLevel, int homeLevel) {
    homePheromoneLevel = homeLevel;
    foodPheromoneLevel = foodLevel;
  }

  /**
   * Method to set ant's orientation
   *
   * @param row row direction
   * @param col column direction
   */
  public void setOrientation(int row, int col) {
    orientation[0] = row;
    orientation[1] = col;
  }
}