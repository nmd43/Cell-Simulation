package cellsociety.view.assets;

import cellsociety.view.Gui;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A JavaFX asset to display all the "about simulation" information in a specified VBox.
 * It functions as a wrapper for a detailed VBox.
 */
public class AboutBox {
  private final String author;
  private final String description;
  private final StackPane box;

  public AboutBox(String author, String description, int factor) {
    this.author = author;
    this.description = description;

    this.box = populateBox(factor);
    box.getStyleClass().add("about-box");
  }

  /**
   * Getter for this class's VBox.
   * @return ^
   */
  public StackPane getAboutBox() {
    return this.box;
  }

  /**
   * Creates text from the "about simulation" information, adds it to the stylesheet, and returns a
   * created, formatted VBox
   * @return the formatted, populated VBox
   */
  private StackPane populateBox(int factor) {
    Text authorInfo = new Text("Author: " + author);
    Text descInfo = new Text("Description: " + description);
    descInfo.setWrappingWidth((Gui.TEXT_WIDTH + 200) / factor);
    Text about = new Text("ABOUT");

    authorInfo.getStyleClass().add("author-info");
    descInfo.getStyleClass().add("desc-info");
    about.getStyleClass().add("about-info");

    return createBox(about, authorInfo, descInfo);
  }

  /**
   * Handles formatting and configuration of the aboutBox VBox
   * @param about "ABOUT"
   * @param authorInfo The author info
   * @param descInfo Description of the simulation
   * @return a VBox with all the "about simulation" information
   */
  private StackPane createBox(Text about, Text authorInfo, Text descInfo) {
    VBox aboutBox = new VBox(20);
    aboutBox.getChildren().addAll(about, authorInfo, descInfo);
    aboutBox.setAlignment(Pos.CENTER_LEFT);

    StackPane layout  = new StackPane(aboutBox);
    layout.setPadding(new javafx.geometry.Insets(20));
    aboutBox.setMaxHeight(Gui.TEXT_HEIGHT - 200);
    return layout;
  }

}
