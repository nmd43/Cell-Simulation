package cellsociety.view.assets;

import cellsociety.view.GamePlay;
import cellsociety.view.Gui;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A wrapper class for a grid that visually represents all cells at any point in time for a specific
 * simulation.
 */
public class Grid {

  private final GridPane matrix;
  private final int gridWidth;
  private final int gridHeight;

  private boolean borders = false;
  public static final Map<String, Color> colors = new HashMap<>();

  static {
    colors.put("red", Color.RED);
    colors.put("orange", Color.ORANGE);
    colors.put("yellow", Color.YELLOW);
    colors.put("green", Color.GREEN);
    colors.put("blue", Color.BLUE);
    colors.put("purple", Color.PURPLE);
    colors.put("pink", Color.PINK);
    colors.put("white", Color.WHITE);
    colors.put("black", Color.BLACK);
    colors.put("default", Color.GRAY);
  }

  public static final Map<Integer, String> numbers = new HashMap<>();

  static {
    numbers.put(0, "zero");
    numbers.put(1, "one");
    numbers.put(2, "two");
    numbers.put(3, "three");
    numbers.put(4, "four");
    numbers.put(5, "five");
  }

  private final Map<String, String> customColors;

  /**
   * Creates the grid representation of the simulation.
   *
   * @param stateGrid The initial state of the grid.
   * @param gameType  The type of the simulation.
   */
  public Grid(int[][] stateGrid, String gameType, GamePlay gamePlay, int gridWidth, int gridHeight,
      Map<String, String> colors) {
    this.customColors = colors;
    this.matrix = new GridPane();
    matrix.setPadding(new Insets(10));
    matrix.setHgap(3);
    matrix.setVgap(3);
    this.gridWidth = gridWidth;
    this.gridHeight = gridHeight;

    // create grid
    for (int row = 0; row < gridWidth; row++) {
      for (int col = 0; col < gridHeight; col++) {
        Rectangle rectangle = new Rectangle(Gui.GUI_GRID_WIDTH / gridWidth,
            Gui.GUI_GRID_WIDTH / gridHeight);
        setRectangleFill(rectangle, stateGrid[row][col], gameType);

        StackPane rectPane = new StackPane(rectangle);
        rectPane.setOnMouseClicked(event -> handleCellClick(rectPane, gamePlay));
        matrix.add(rectPane, col, row);
      }
    }
    matrix.setAlignment(Pos.CENTER);
    matrix.getStyleClass().add("matrix");
  }

  /**
   * Updates the grid with the new state of the simulation.
   *
   * @param stateGrid The updated state of the grid.
   * @param gameType  The type of the simulation.
   */
  public void updateGrid(int[][] stateGrid, String gameType) {
    for (int row = 0; row < gridWidth; row++) {
      for (int col = 0; col < gridHeight; col++) {
        Node cell = getCell(matrix, row, col);
        updateIndividualCell(cell, row, col, stateGrid, gameType);
      }
    }
  }

  private void updateIndividualCell(Node cell, int row, int col, int[][] stateGrid,
      String gameType) {
    if (cell instanceof StackPane) {
      ObservableList<Node> children = ((StackPane) cell).getChildren();
      for (Node node : children) {
        if (node instanceof Rectangle rectangle) {
          setRectangleFill(rectangle, stateGrid[row][col], gameType);
        }
      }
    }
  }

  public void setBorders() {
    for (Node cell : matrix.getChildren()) {
      if (borders) {
        cell.setStyle("-fx-border-color: rgba(52, 20, 20, 0.93); -fx-border-width: 2;");
      } else {
        cell.setStyle("-fx-border-width: 0;");
      }
    }
  }

  /**
   * Sets the fill color of the rectangle based on the state and type of the simulation.
   *
   * @param rectangle The rectangle representing a cell in the grid.
   * @param state     The state of the cell.
   * @param gameType  The type of the simulation.
   */
  private void setRectangleFill(Rectangle rectangle, int state, String gameType) {
    if (customColors == null) {
      switch (gameType) {
        case "fallingsand":
          fallingSandFill(rectangle, state);
          break;
        case "foragingants":
          foragingAntsFill(rectangle, state);
          break;
        case "spreadingoffire":
          spreadingOfFireFill(rectangle, state);
          break;
        case "percolation":
          percolationFill(rectangle, state);
          break;
        case "watorworld":
          watorWorldFill(rectangle, state);
          break;
        default:
          defaultFill(rectangle, state);
      }
    } else {
      String numState = numbers.get(state);
      String color = customColors.getOrDefault(numState, "default");
      Color fill = colors.get(color);
      rectangle.setFill(fill);
    }
  }

  private void fallingSandFill(Rectangle rectangle, int state) {
    if (state == 0) {
      rectangle.setFill(Color.WHITE);
    } else if (state == 1) {
      rectangle.setFill(Color.ORANGE);
    } else if (state == 2) {
      rectangle.setFill(Color.GREY);
    } else if (state == 3) {
      rectangle.setFill(Color.BLUE);
    }
  }

  private void foragingAntsFill(Rectangle rectangle, int state) {
    if (state == 0) {
      rectangle.setFill(Color.WHITE);
    } else if (state == 1) {
      rectangle.setFill(Color.ORANGE);
    } else if (state == 2) {
      rectangle.setFill(Color.BLUE);
    } else if (state == 3) {
      rectangle.setFill(Color.BLACK);
    } else if (state == 4) {
      rectangle.setFill(Color.YELLOW);
    }
  }

  private void spreadingOfFireFill(Rectangle rectangle, int state) {
    if (state == 0) {
      rectangle.setFill(Color.WHITE);
    } else if (state == 1) {
      rectangle.setFill(Color.GREEN);
    } else if (state == 2) {
      rectangle.setFill(Color.RED);
    }
  }

  private void percolationFill(Rectangle rectangle, int state) {
    if (state == 0) {
      rectangle.setFill(Color.WHITE);
    } else if (state == 1) {
      rectangle.setFill(Color.BLACK);
    } else if (state == 2) {
      rectangle.setFill(Color.BLUE);
    }
  }

  private void watorWorldFill(Rectangle rectangle, int state) {
    if (state == 0) {
      rectangle.setFill(Color.WHITE);
    } else if (state == 1) {
      rectangle.setFill(Color.ORANGE);
    } else if (state == 2) {
      rectangle.setFill(Color.GRAY);
    }
  }

  private void defaultFill(Rectangle rectangle, int state) {
    if (state == 0) {
      rectangle.setFill(Color.WHITE);
    } else if (state == 1) {
      rectangle.setFill(Color.BLUE);
    } else if (state == 2) {
      rectangle.setFill(Color.RED);
    }
  }

  /**
   * Retrieves the cell node from the grid based on its row and column indices.
   *
   * @param matrix The grid pane containing the cells.
   * @param row    The row index of the cell.
   * @param col    The column index of the cell.
   * @return The node representing the cell in the grid.
   */
  private Node getCell(GridPane matrix, int row, int col) {
    for (Node cell : matrix.getChildren()) {
      if (GridPane.getRowIndex(cell) == row && GridPane.getColumnIndex(cell) == col) {
        return cell;
      }
    }
    return null;
  }

  /**
   * Dynamically updates the state of a cell when a user clicks it on the grid.
   *
   * @param cell     the Cell to update
   * @param gamePlay the current gamePlay
   */
  private void handleCellClick(StackPane cell, GamePlay gamePlay) {
    int targetRow = -1;
    int targetCol = -1;

    for (int row = 0; row < gridWidth; row++) {
      for (int col = 0; col < gridHeight; col++) {
        StackPane current = (StackPane) findNodeInMatrix(col, row);
        if (current != null && current == cell) {
          targetRow = row;
          targetCol = col;
          break;
        }
      }
    }
    //Increment the cell by 1 state
    gamePlay.getGame().incrementCell(targetRow, targetCol, gamePlay.getGame().getTotalStates());
  }

  /**
   * Finds a Node in this Grid's GridPane based on an input column and row value.
   *
   * @param col
   * @param row
   * @return
   */
  private Node findNodeInMatrix(int col, int row) {
    for (Node node : matrix.getChildren()) {
      if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
        return node;
      }
    }
    return null;
  }

  /**
   * Returns the GridPane object for this Grid.
   *
   * @return ^
   */
  public GridPane getGrid() {
    return matrix;
  }

  /**
   * Puts a border frame around each cell in the grid.
   */
  public void setBorderStatus() {
    borders = !borders;
  }

  public boolean getBordersStatus() {
    return borders;
  }
}