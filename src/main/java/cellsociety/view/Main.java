package cellsociety.view;

import cellsociety.config.FileManagement;
import cellsociety.config.LanguageManager;
import cellsociety.view.assets.Util;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  /**
   * @see Application#start(Stage)
   */
  @Override
  public void start(Stage primaryStage) {
    FileManagement manager = new FileManagement();
    manager.retrieveFile(primaryStage);

    Scene screen = Util.configureNewSimulation(manager);

    if (screen == null) {
      primaryStage.close();
    } else {
      LanguageManager.updateLanguage(screen.getRoot());
      primaryStage.setScene(screen);
      primaryStage.show();
    }
  }

  /**
   * Start the program, give complete control to JavaFX.
   * <p>
   * Default version of main() is actually included within JavaFX, so this is not technically
   * necessary!
   */
  public static void main(String[] args) {
    launch(args);
  }
}
