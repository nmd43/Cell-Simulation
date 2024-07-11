package cellsociety.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Utility class for randomizing cell states based on proportions defined in XML.
 */
public class RandomizeXml {

  private static Map<Integer, Double> stateProportions;

  /**
   * Randomizes the cell states based on proportions defined in the given XML element.
   *
   * @param cells            NodeList containing cell elements
   * @param grid             2D array representing the grid
   * @param randomizeElement Element containing state proportions
   */
  public static void randomizeCellStates(NodeList cells, int[][] grid, Element randomizeElement) {
    if (stateProportions == null) {
      stateProportions = parseStateProportions(randomizeElement);
    }

    int totalCells = grid.length * grid[0].length;
    int[] numCellsByState = calculateNumCellsByState(stateProportions, totalCells);
    List<Integer> states = new ArrayList<>(stateProportions.keySet());
    randomizeRemainingCells(numCellsByState, grid, states);

    // Assign states to cells based on randomized proportions
    List<CellPosition> cellPositions = generateCellPositions(grid);
    Collections.shuffle(cellPositions);

    int cellIndex = 0;
    for (CellPosition cellPosition : cellPositions) {
      int row = cellPosition.row;
      int col = cellPosition.column;
      int state = states.get(cellIndex);
      grid[row][col] = state;
      numCellsByState[state]--;

      if (numCellsByState[state] == 0) {
        cellIndex++;
      }
    }
  }

  /**
   * Calculates the number of cells for each state based on proportions.
   *
   * @param stateProportions Map containing state proportions
   * @param totalCells       Total number of cells
   * @return Array containing the number of cells for each state
   */
  private static int[] calculateNumCellsByState(Map<Integer, Double> stateProportions,
      int totalCells) {
    int[] numCellsByState = new int[stateProportions.size()];
    int remainingCells = totalCells;

    // Calculate number of cells for each state based on proportions
    for (Map.Entry<Integer, Double> entry : stateProportions.entrySet()) {
      int state = entry.getKey();
      double proportion = entry.getValue();
      int numCells = (int) Math.round(proportion * totalCells);
      numCellsByState[state] = numCells;
      remainingCells -= numCells;
    }

    // Distribute remaining cells to states with non-zero proportions
    while (remainingCells > 0) {
      for (int i = 0; i < numCellsByState.length; i++) {
        if (numCellsByState[i] > 0) {
          numCellsByState[i]++;
          remainingCells--;
          if (remainingCells == 0) {
            break;
          }
        }
      }
    }

    return numCellsByState;
  }

  /**
   * Randomly assigns remaining cells to states.
   *
   * @param numCellsByState Array containing the number of cells for each state
   * @param grid            2D array representing the grid
   * @param states          List containing state values
   */
  private static void randomizeRemainingCells(int[] numCellsByState, int[][] grid,
      List<Integer> states) {
    Random random = new Random();

    // Shuffle the states list to ensure randomness
    Collections.shuffle(states);

    // Distribute remaining cells randomly across the grid
    int remainingEmptyCells = countEmptyCells(grid);
    for (int state : states) {
      while (numCellsByState[state] > 0 && remainingEmptyCells > 0) {
        // Randomly select an empty cell and assign the state
        int randomRow = random.nextInt(grid.length);
        int randomCol = random.nextInt(grid[0].length);
        if (grid[randomRow][randomCol] == -1) {
          grid[randomRow][randomCol] = state;
          numCellsByState[state]--;
          remainingEmptyCells--;
        }
      }
    }
  }

  /**
   * Counts the number of empty cells in the grid.
   *
   * @param grid 2D array representing the grid
   * @return Number of empty cells
   */
  private static int countEmptyCells(int[][] grid) {
    int count = 0;
    for (int[] row : grid) {
      for (int cell : row) {
        if (cell == -1) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Generates a list of cell positions for the grid.
   *
   * @param grid 2D array representing the grid
   * @return List of cell positions
   */
  private static List<CellPosition> generateCellPositions(int[][] grid) {
    List<CellPosition> cellPositions = new ArrayList<>();
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        cellPositions.add(new CellPosition(row, col));
      }
    }
    return cellPositions;
  }

  private static class CellPosition {

    int row;
    int column;

    CellPosition(int row, int column) {
      this.row = row;
      this.column = column;
    }
  }

  /**
   * Parses state proportions from the given XML element.
   *
   * @param randomizeElement Element containing state proportions
   * @return Map containing state proportions
   */
  private static Map<Integer, Double> parseStateProportions(Element randomizeElement) {
    Map<Integer, Double> stateProportions = new HashMap<>();
    NodeList stateNodes = randomizeElement.getElementsByTagName("state");
    double totalProportion = 0.0;
    for (int i = 0; i < stateNodes.getLength(); i++) {
      Element stateElement = (Element) stateNodes.item(i);
      int stateValue = Integer.parseInt(stateElement.getAttribute("value"));
      double proportion = Double.parseDouble(stateElement.getAttribute("proportion"));
      stateProportions.put(stateValue, proportion);
      totalProportion += proportion;
    }

    // Normalize proportions to ensure they sum up to 1.0
    for (int state : stateProportions.keySet()) {
      stateProportions.put(state, stateProportions.get(state) / totalProportion);
    }
    return stateProportions;
  }
}
