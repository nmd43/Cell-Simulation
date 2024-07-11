package cellsociety.config;

import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;

/**
 * Custom exception class for handling invalid configurations.
 */
public class InvalidConfigurationException extends Exception {

  private Object[] args;

  /**
   * Constructs an InvalidConfigurationException with the specified error message.
   *
   * @param message the error message
   */
  public InvalidConfigurationException(String message) {
    super(message);
  }

  /**
   * Constructs an InvalidConfigurationException with the specified error message and arguments.
   *
   * @param message the error message
   * @param args    the arguments to be included in the error message
   */
  public InvalidConfigurationException(String message, Object... args) {
    super(message);
    this.args = Arrays.copyOf(args, args.length);
  }


  // Method to display the error message as a pop-up dialog
  public void showErrorDialog() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("translations");
    String errorMessage = resourceBundle.getString(getMessage());
    if (args != null) {
      errorMessage = String.format(errorMessage, args);
    }
    Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage);
    alert.setHeaderText(null);
    alert.showAndWait();
  }

  // Displays the error message related to language issues as a pop-up dialog.
  public void showLanguageErrorDialog() {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setHeaderText(null);
    alert.setContentText(getMessage());
    alert.showAndWait();
  }

}
