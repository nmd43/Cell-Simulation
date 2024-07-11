package cellsociety.config;

import cellsociety.view.GamePlay;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;


/**
 * Handles file management operations such as retrieving and saving files.
 */
public class FileManagement {

  private static ResourceBundle resourceBundle;

  // default to start in the data folder to make it easy on the user to find
  public static final String DATA_FILE_FOLDER = System.getProperty("user.dir") + "/data";

  // kind of data files to look for
  public static final String DATA_FILE_EXTENSION = "*.xml";
  private static final FileChooser FILE_CHOOSER = makeChooser(DATA_FILE_EXTENSION);
  private GamePlay[] gamePlays;

  public static final int MAX_FILES = 2;

  static {
    // Load resource bundle
    resourceBundle = ResourceBundle.getBundle("translations");
  }

  public static void setResourceBundle(ResourceBundle bundle) {
    resourceBundle = bundle;
  }

  /**
   * Retrieves a file using a FileChooser dialog.
   *
   * @param primaryStage the primary stage of the JavaFX application
   */
  public void retrieveFile(Stage primaryStage) {
    List<File> files = FILE_CHOOSER.showOpenMultipleDialog(primaryStage);
    try {
      if (files != null) {
        if (files.size() <= MAX_FILES) {
          gamePlays = new GamePlay[files.size()];
          for (int i = 0; i < files.size(); i++) {
            gamePlays[i] = new GamePlay(files.get(i), primaryStage, this, files.size());
          }
        } else {
          showMessage(AlertType.ERROR, "TooManyFiles");
          retrieveFile(primaryStage);
        }
      }
    } catch (Exception e) {
      // Show an error message or handle the case where the user doesn't select exactly two files
      e.printStackTrace();
    }
  }

  public GamePlay[] getGamePlays() {
    return Arrays.copyOf(gamePlays, gamePlays.length);
  }

  /**
   * Displays a message to the user using an Alert dialog box.
   *
   * @param type    the type of the Alert dialog box
   * @param message the message to be displayed
   */

  public static void showMessage(AlertType type, String message) {
    message = resourceBundle.getString(message);
    new Alert(type, message + " " + MAX_FILES).showAndWait();
  }

  /**
   * Creates a FileChooser with sensible default settings.
   *
   * @param extensionAccepted the accepted file extension
   * @return a FileChooser instance with default settings
   */
  private static FileChooser makeChooser(String extensionAccepted) {
    FileChooser result = new FileChooser();
    result.setTitle("Open Data File");
    // pick a reasonable place to start searching for files
    result.setInitialDirectory(new File(DATA_FILE_FOLDER));
    result.getExtensionFilters()
        .setAll(new FileChooser.ExtensionFilter("Data Files", extensionAccepted));
    return result;
  }

  /**
   * Saves the current configuration to a file.
   */
  public void saveFile(String title, String author, String desc, GamePlay gamePlay) {
    try {
      Parser currentParser = gamePlay.getParser();
      Document newFile = currentParser.createConfigFile(title, author, desc);

      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(newFile);

      //Make a file chooser dialog box
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Configuration File");

      fileChooser.setInitialFileName(currentParser.getTitle() + ".xml");
      FileChooser.ExtensionFilter extFilter = new FileChooser
          .ExtensionFilter("XML files (*.xml)", "*.xml");
      fileChooser.getExtensionFilters().add(extFilter);

      File selectedFile = fileChooser.showSaveDialog(null);
      if (selectedFile != null) {
        StreamResult result = new StreamResult(selectedFile);
        transformer.transform(source, result);

        showMessage(AlertType.INFORMATION,
            "Successfully saved file. Please check the selected location to find it.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
