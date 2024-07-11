package cellsociety.view;

import cellsociety.view.assets.AboutBox;
import cellsociety.view.assets.Grid;
import cellsociety.view.assets.Util;
import java.util.List;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class Gui {

  Scene scene;
  GridPane matrix;
  Grid grid;
  public static final int GUI_GRID_WIDTH = 200;
  public static final int TEXT_HEIGHT = 200;
  private final int gridWidth;
  private final int gridHeight;
  public static final int TEXT_WIDTH = 800;
  public static final String STYLESHEET = "style.css";
  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.";
  public static final String DEFAULT_RESOURCE_FOLDER =
      "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/");
  private final BorderPane root;

  public Gui(int[][] grid, List<String> params, GamePlay gamePlay, int factor,
      Map<String, String> colors) {
    this.gridWidth = grid.length;
    this.gridHeight = grid[0].length;

    //Create the about box
    StackPane aboutBox = (new AboutBox(params.get(1), params.get(2), factor)).getAboutBox();
    //Handle creating the title
    HBox top = Util.handleTitle(params.get(0));

    BorderPane root = makeRoot(grid, params.get(3), gamePlay, factor, colors);
    root.setTop(top);
    root.setCenter(aboutBox);
    //Create the scene with movable bounds
    Screen screen = Screen.getPrimary();
    double screenWidth = screen.getBounds().getWidth();
    double screenHeight = screen.getBounds().getHeight();

    root.maxWidth((screenWidth - 100));
    root.maxHeight((screenHeight - 100) / factor);

    root.setId("root");
    setIdsForNodes(root);

    root.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
    this.root = root;
  }

  /**
   * Wrapper for the update grid function on the Grid object.
   *
   * @param stateGrid
   * @param gameType
   */
  public void updateGrid(int[][] stateGrid, String gameType) {
    this.grid.updateGrid(stateGrid, gameType);
  }

  /**
   * Function to create the root.
   */
  private BorderPane makeRoot(int[][] grid, String gameType, GamePlay gamePlay,
      int factor, Map<String, String> colors) {
    //Set the buttons and slider
    Slider slider = new Slider(0.1, 5, 2);

    //Make the grid, buttons, and slider
    this.grid = new Grid(grid, gameType, gamePlay, gridWidth, gridHeight, colors);
    matrix = this.grid.getGrid();
    HBox buttonBox = Util.handleButtons(gamePlay, slider, this.grid, grid, gameType);
    VBox gridAndButtons = new VBox(matrix, buttonBox);
    gridAndButtons.setAlignment(Pos.CENTER);
    Util.handleSlider(gridAndButtons, gamePlay, slider, factor);

    //Create the root of the scene
    BorderPane root = new BorderPane();
    root.setBottom(gridAndButtons);

    return root;
  }

  private void setIdsForNodes(Node node) {
    if (node instanceof Button) {
      String buttonText = ((Button) node).getText();
      (node).setId(buttonText);
    } else if (node instanceof TextField) {
      String promptText = ((TextField) node).getPromptText();
      (node).setId(promptText);
    } else if (node instanceof Label label) {
      if (label.getId() == null || label.getId().isEmpty()) {
        String labelText = ((Label) node).getText();
        (node).setId(labelText);
      }
    } else if (node instanceof Parent parent) {
      for (Node child : parent.getChildrenUnmodifiable()) {
        setIdsForNodes(child);
      }
    }
  }

  public BorderPane getRoot() {
    return this.root;
  }

  /**
   * @return The scene object representing the GUI.
   */
  public Scene getScene() {
    return this.scene;
  }
}

