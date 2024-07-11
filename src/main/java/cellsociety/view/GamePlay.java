package cellsociety.view;

import cellsociety.config.FileManagement;
import cellsociety.config.Parser;
import cellsociety.model.cells.Cell;
import cellsociety.model.gametypes.GameType;
import cellsociety.view.assets.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GamePlay {

  private final Timeline timeline;
  private final GameType<Cell> game;
  private final Parser parser;
  private final Gui design;
  private final Stage stage;
  private final FileManagement manager;

  private final Scene scene;

  /**
   * Constructs a GamePlay instance with the provided configuration file, stage, and file manager.
   *
   * @param configFile   The configuration file defining the initial state of the simulation.
   * @param primaryStage The primary JavaFX stage to display the simulation GUI.
   * @param manager      The file manager responsible for handling file operations.
   * @param factor       The ratio by which GUI simulations should be resized depending on how many
   *                     simulations are allowed to be run at once
   */
  public GamePlay(File configFile, Stage primaryStage, FileManagement manager, int factor) {
    this.stage = primaryStage;
    this.manager = manager;
    parser = new Parser(configFile);
    game = parser.getGameTypeFromName();
    game.setEdgeType(parser.getEdgeType());
    game.setArrangement(parser.getArrangement());
    List<String> params = new ArrayList<>(Arrays.asList(parser.getTitle(),  parser.getAuthor(),
        parser.getDescription(), parser.getTypeName()));
    design = new Gui(parser.getCells(), params,
        this, factor, parser.getStateColors());

    this.scene = design.getScene();

    timeline = new Timeline();
    //default timeline
    KeyFrame keyFrame = new KeyFrame(Duration.seconds(0), event -> {
      int[][] updatedStates = game.updateGameState();
      design.updateGrid(updatedStates, parser.getTypeName());
    });
    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();

    resetGame();
  }

  /**
   * @return The parser object used for parsing configuration files.
   */
  public Parser getParser() {
    return this.parser;
  }

  /**
   * Returns the scene of this simulation
   */
  public Scene getScene() {
    return scene;
  }

  public BorderPane getRoot() {
    return design.getRoot();
  }

  /**
   * @return The file manager object used for file operations.
   */
  public FileManagement getManager() {
    return this.manager;
  }

  /**
   * @return The game logic object representing the current game state and rules.
   */
  public GameType<Cell> getGame() {
    return this.game;
  }

  /**
   * Sets up a timeline for updating the game state and GUI design at regular intervals.
   *
   * @param seconds The time interval in seconds between each game update.
   */
  public void updateTimeline(double seconds) {
    timeline.stop();
    timeline.getKeyFrames().clear();
    KeyFrame newSpeed = new KeyFrame(Duration.seconds(seconds), event -> {
      int[][] updatedStates = game.updateGameState();
      design.updateGrid(updatedStates, parser.getTypeName());
    });
    timeline.getKeyFrames().add(newSpeed);
    timeline.play();
  }

  /**
   * Pauses the game simulation by stopping the timeline.
   */
  public void stopSimulation() {
    if (timeline != null) {
      timeline.pause();
    }
  }

  /**
   * Resets the game to its initial state and updates the GUI accordingly.
   */
  public void resetGame() {
    // Reset the game to its initial state
    game.resetGame(parser.getCells());

    // Update the GUI to reflect the reset state
    design.updateGrid(parser.getCells(), parser.getTypeName());
  }

  /**
   * Stops the current simulation and loads a new configuration file for a new simulation.
   */
  public void loadNewSimulation(FileManagement manager) {
    stopSimulation();
    manager.retrieveFile(stage);

    Scene mainScreen = Util.configureNewSimulation(manager);

    stage.setScene(mainScreen);
    stage.show();
  }

  public boolean isRunning() {
    return this.timeline.getStatus() == Status.RUNNING;
  }
}
