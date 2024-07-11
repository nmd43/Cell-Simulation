package cellsociety.view.assets;

import cellsociety.config.FileManagement;
import cellsociety.config.LanguageManager;
import cellsociety.view.GamePlay;
import cellsociety.view.Gui;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * A static class that handles the creating and configuration of miscellaneous GUI models.
 */
public class Util {

  public static HBox handleTitle(String title) {
    Text titleInfo = new Text(title);
    HBox top = new HBox(titleInfo);
    top.setAlignment(Pos.TOP_CENTER);
    titleInfo.getStyleClass().add("title-info");
    top.setPadding(new Insets(0, 0, 20, 0));
    return top;
  }

  /**
   * Creates all buttons and returns them in an HBox.
   *
   * @param gamePlay the current GamePlay object
   * @param slider   the slider object of the screen
   * @return an HBox containing all the buttons
   */
  public static HBox handleButtons(GamePlay gamePlay, Slider slider, Grid grid, int[][] stateGrid,
      String gameType) {
    Button resetButton = new Button("Reset");
    Button startButton = new Button("Start");
    Button stopButton = new Button("Stop");
    Button loadNew = new Button("LoadNew");
    Button saveConfig = getSaveButton();
    Button modify = new Button("More");

    Arrays.asList(resetButton, startButton, stopButton, loadNew, saveConfig, modify)
        .forEach(button -> {
          button.setMinWidth(60);
        });

    //Add event handlers to each button
    resetButton.setOnAction(event -> gamePlay.resetGame());
    startButton.setOnAction(event -> gamePlay.updateTimeline(slider.getValue()));
    stopButton.setOnAction(event -> gamePlay.stopSimulation());
    loadNew.setOnAction(event -> gamePlay.loadNewSimulation(gamePlay.getManager()));
    saveConfig.setOnAction(event -> editSaveWindow(gamePlay));
    modify.setOnAction(event -> modifyWindow(gamePlay.getGame().getParamList(), gamePlay, grid));

    HBox buttonBox = new HBox(resetButton, startButton, stopButton, loadNew,
        saveConfig, modify);
    buttonBox.setSpacing(10);
    buttonBox.setAlignment(Pos.CENTER);

    return buttonBox;
  }

  /**
   * A method that configures the slider object of the display screen.
   *
   * @param gridAndButtons a VBox containing the matrix and buttons that the slider will be added
   *                       to
   * @param gamePlay       the current GamePlay object
   * @param slider         the slider object of the screen
   */
  public static void handleSlider(VBox gridAndButtons, GamePlay gamePlay,
      Slider slider, int factor) {
    slider.setMajorTickUnit(5);
    slider.setMinorTickCount(0);
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);
    slider.setMaxWidth(300);
    DecimalFormat formatter = new DecimalFormat("#.#");
    String staticText = LanguageManager.getText("CurrentChangeRate");
    String dynamicValue = formatter.format(slider.getValue());
    String labelText = staticText + dynamicValue;
    Label value = new Label(labelText);
    value.setId("CurrentChangeRate");
    value.setText(labelText);
    value.getStyleClass().add("slider-text");
    slider.getStyleClass().add("slider");

    slider.valueProperty().addListener((observable, oldTime, newTime) -> {
      value.setText(staticText + formatter.format(newTime.doubleValue()));
      if (gamePlay.isRunning()) {
        gamePlay.updateTimeline(newTime.doubleValue());
      }
    });
    VBox root = new VBox(slider, value);
    root.setAlignment(Pos.CENTER);
    root.setSpacing(10);
    root.setPadding(new javafx.geometry.Insets(10));

    //Layout CSS
    gridAndButtons.getChildren().add(root);
    gridAndButtons.setAlignment(Pos.CENTER);
    gridAndButtons.setMaxWidth(Gui.GUI_GRID_WIDTH + 600);
    gridAndButtons.setSpacing(8);
  }

  /**
   * Creates and displays a dialogue box that lets you input specific information to edit on a saved
   * configuration file.
   *
   * @param gamePlay
   */
  public static void editSaveWindow(GamePlay gamePlay) {
    Stage popup = new Stage();
    popup.setTitle("EditConfigSave");

    Label titleLabel = new Label("Title");
    TextField titleField = new TextField();

    Label authorLabel = new Label("Author");
    TextField authorField = new TextField();

    Label descLabel = new Label("Description");
    TextField descField = new TextField();

    Button okButton = getSaveButton();
    okButton.setOnAction(e -> {
      String title = titleField.getText();
      String author = authorField.getText();
      String desc = descField.getText();
      gamePlay.getManager().saveFile(title, author, desc, gamePlay);

      popup.close();
    });

    VBox popupRoot = new VBox(10, titleLabel, titleField, authorLabel, authorField,
        descLabel, descField, okButton);
    popupRoot.setPadding(new Insets(10));

    Scene popupScene = new Scene(popupRoot, 250, 300);
    popup.setScene(popupScene);
    popup.show();
  }

  /**
   * Configures and displays a modification window for the simulation
   *
   * @param params   The string of params to edit
   * @param gamePlay The current simulation's timeline
   * @param grid     the grid of the simulation
   */
  public static void modifyWindow(List<String> params, GamePlay gamePlay, Grid grid) {
    Stage popup = new Stage();
    popup.setTitle("Modify Current Simulation");

    VBox popupRoot = new VBox(10);
    popupRoot.setAlignment(Pos.CENTER); // Set alignment to center

    //All sims will have this regardless
    Button gridBorders = new Button("Add Borders");
    gridBorders.setOnAction(e -> {
      grid.setBorderStatus();
      if (grid.getBordersStatus()) {
        gridBorders.setText("Remove Borders");
      } else {
        gridBorders.setText("Add Borders");
      }
      grid.setBorders();
    });
    //Same with this button
    Button stats = new Button("Statistics");
    stats.setOnAction(event -> histogramWindow(gamePlay.getGame()
        .getPopulationStatistics(), gamePlay));

    setParamFields(params, popupRoot, popup, gamePlay);

    popupRoot.getChildren().addAll(gridBorders, stats);
    popupRoot.setPadding(new Insets(10));
    Scene popupScene = new Scene(popupRoot, 250, 300);
    popup.setScene(popupScene);
    popup.show();
  }

  /**
   * Helper that initializes labels and fields for additional params in the modify simulation
   * window
   *
   * @param params    The string of params to edit
   * @param popupRoot The root of the window
   * @param popup     The stage of the window
   * @param gamePlay  The current simulation's timeline
   */
  private static void setParamFields(List<String> params, VBox popupRoot, Stage popup,
      GamePlay gamePlay) {
    Map<String, Double> labels = new HashMap<>();
    List<TextField> fields = new ArrayList<>();

    if (params != null) {
      for (String param : params) {
        Label current = new Label(param + ":");
        TextField currentField = new TextField();
        currentField.setPrefWidth(100); // Set a preferred width for the text field
        popupRoot.getChildren().addAll(current, currentField);
        fields.add(currentField);
      }
    }
    Button okButton = getSaveButton();
    okButton.setOnAction(e -> {
      if (params != null) {
        for (int i = 0; i < params.size(); i++) {
          String val = fields.get(i).getText().trim();
          if (!val.isEmpty()) {
            labels.put(params.get(i), Double.valueOf(val));
          }
        }
        gamePlay.getGame().updateParams(labels);
      }
      popup.close();
    });
    popupRoot.getChildren().add(okButton);
  }

  /**
   * Sets up the scene for a new loaded file.
   *
   * @param manager The File Manager for this current simulation(s) run.
   * @return a Scene for all of the simulations now loaded.
   */
  public static Scene configureNewSimulation(FileManagement manager) {
    GamePlay[] simulations = manager.getGamePlays();
    if (simulations == null) {
      return null;
    }
    HBox sims = new HBox();
    Screen screen = Screen.getPrimary();
    double screenWidth = screen.getBounds().getWidth();
    double screenHeight = screen.getBounds().getHeight();

    for (GamePlay simulation : simulations) {
      sims.getChildren().add(simulation.getRoot());
    }
    sims.setSpacing(150);
    sims.setAlignment(Pos.CENTER_LEFT);
    Scene mainScreen = new Scene(sims, screenWidth - 100, screenHeight - 100, Color.BEIGE);
    return mainScreen;
  }

  public static void histogramWindow(Map<Integer, Integer> stats, GamePlay gamePlay) {
    Stage popup = new Stage();

    CategoryAxis x = new CategoryAxis();
    NumberAxis y = new NumberAxis();

    BarChart<String, Number> graph = new BarChart<>(x, y);
    graph.setTitle("Histogram of Current Population Data");
    x.setLabel("State");
    y.setLabel("Amount of Cells");

    XYChart.Series<String, Number> data = new XYChart.Series<>();
    Map<Integer, String> stateMap = gamePlay.getGame().getStateMap();

    for (Map.Entry<Integer, Integer> e : stats.entrySet()) {
      String category = stateMap.get(e.getKey());
      data.getData().add(new XYChart.Data<>(category, e.getValue()));
    }

    graph.getData().add(data);

    Scene scene = new Scene(graph, 600, 400);
    popup.setScene(scene);
    popup.setTitle("Histogram");
    popup.show();
  }

  private static Button getSaveButton() {
    return new Button("Save");
  }

}
