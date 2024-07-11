package cellsociety.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * A class responsible for managing language translations.
 */
public class LanguageManager {

  private static ResourceBundle resourceBundle;

  // Static block to initialize the resource bundle
  static {
    try {
      resourceBundle = ResourceBundle.getBundle("translations");
    } catch (MissingResourceException e) {
      System.err.println("Error loading resource bundle: translations");
      e.printStackTrace();
    }
  }

  /**
   * Updates the language of the specified JavaFX node and its children.
   *
   * @param node the JavaFX node whose language is to be updated
   */
  public static void updateLanguage(Node node) {
    if (node instanceof Button) {
      ((Button) node).setText(getText(node.getId()));
    } else if (node instanceof Label) {
      ((Label) node).setText(getText(node.getId()));
    } else if (node instanceof TextField) {
      ((TextField) node).setPromptText(getText(node.getId()));
    }

    if (node instanceof Parent) {
      for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
        updateLanguage(child);
      }
    }
  }

  /**
   * Retrieves the translated text corresponding to the given key.
   *
   * @param key the key for the translated text
   * @return the translated text corresponding to the given key
   */
  public static String getText(String key) {
    try {
      String sanitizedKey = key.replaceAll("\\s+", "_"); // Replace spaces with underscores
      return resourceBundle.getString(sanitizedKey);
    } catch (Exception e) {
      // not for the user, only for debugging
      String sanitizedKey = key.replaceAll("\\s+", "_"); // Replace spaces with underscores
      e.printStackTrace();
      System.err.println("Error getting text for key '" + sanitizedKey + "': " + e.getMessage());
      return ""; // Or any other default value you prefer
    }
  }
}
