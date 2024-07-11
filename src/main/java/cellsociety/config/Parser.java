package cellsociety.config;

import cellsociety.model.cells.Cell;
import cellsociety.model.gametypes.FallingSand;
import cellsociety.model.gametypes.ForagingAnts;
import cellsociety.model.gametypes.GameOfLife;
import cellsociety.model.gametypes.GameType;
import cellsociety.model.gametypes.ModelOfSegregation;
import cellsociety.model.gametypes.Percolation;
import cellsociety.model.gametypes.SpreadingOfFire;
import cellsociety.model.gametypes.WaTorWorld;
import cellsociety.view.assets.StateNamesMap;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.scene.control.Alert;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * A parser class for handling XML related functions.
 */
public class Parser {

  private File xmlFile;
  private int width;
  private int height;
  private final String defaultProbability = "0.5";
  private String typeName;
  private String title;
  private String author;
  private String description;
  private Element root;
  private final Map<String, String> additionalParams = new HashMap<>();
  private GameType currentGame;
  private String arrangement;

  private Document xmlDocument;

  private static final Map<String, Integer> maxStateValues = new HashMap<>();


  static {
    // Initialize the maximum state values for each game type
    maxStateValues.put("percolation", 2);
    maxStateValues.put("watorworld", 2);
    maxStateValues.put("spreadingoffire", 2);
    maxStateValues.put("modelofsegregation", 2);
    maxStateValues.put("gameoflife", 1);
    maxStateValues.put("foragingants", 4);
    maxStateValues.put("fallingsand", 3);
  }

  /**
   * Constructs a Parser object with the given XML file.
   *
   * @param xmlFile the XML file to parse
   */
  public Parser(File xmlFile) {
    try {

      if (xmlFile.length() == 0) {
        throw new InvalidConfigurationException("Empty_configuration_file");
      }
      this.xmlFile = xmlFile;
      xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(this.xmlFile);
      this.root = xmlDocument.getDocumentElement();
      setDefaultLocaleFromLanguageTag(root);
      getBasicInfo();

    } catch (NumberFormatException e) {
      showMessage(Alert.AlertType.ERROR, "Invalid_number_given_in_data");
      System.exit(1);
    } catch (ParserConfigurationException e) {
      showMessage(Alert.AlertType.ERROR, "Invalid_XML_Configuration");
      System.exit(1);
    } catch (SAXException | IOException e) {
      showMessage(Alert.AlertType.ERROR, "Incorrectly_Formatted_XML_Data");
      System.exit(1);
    } catch (InvalidConfigurationException e) {
      e.showErrorDialog();
      System.exit(1);
    }
  }

  public Document getDocument() {
    return this.xmlDocument;
  }

  /**
   * Extracts the language tag from the XML root element and sets the default locale accordingly.
   *
   * @param root the root element of the XML document
   */
  private void setDefaultLocaleFromLanguageTag(Element root) {
    String languageTag = getTextValueOrDefault(root, "language", "");
    if (!languageTag.isEmpty()) {
      Locale locale;
      switch (languageTag.toLowerCase()) {
        case "english":
          locale = Locale.ENGLISH;
          break;
        case "french":
          locale = Locale.FRENCH;
          break;
        case "spanish":
          locale = new Locale("es", "ES");
          break;
        default:
          new InvalidConfigurationException("Language should be English, French, or Spanish. " +
              "Defaulting to English since you entered " + languageTag).showLanguageErrorDialog();
          locale = Locale.ENGLISH; // Default to English
      }
      Locale.setDefault(locale);
    } else {
      new InvalidConfigurationException(
          "Couldn't find language. Defaulting to English.").showLanguageErrorDialog();
      Locale.setDefault(Locale.ENGLISH); // Default to English
    }
  }

  /**
   * Retrieves the type of edge specified in the XML file.
   *
   * @return the type of edge as a String
   */
  public String getEdgeType() {
    String edgeTypeTag = getTextValueOrDefault(root, "edgeType", "standard");
    edgeTypeTag = edgeTypeTag.toLowerCase();
    return edgeTypeTag;

  }

  /**
   * Returns all basic info of the current file.
   */
  public void getBasicInfo() {
    try {
      this.width = Integer.parseInt(getTextValueOrDefault(root, "width", ""));
      this.height = Integer.parseInt(getTextValueOrDefault(root, "height", ""));
      this.typeName = getTextValueOrDefault(root, "gameType", "");
      this.title = getTextValueOrDefault(root, "title", "");
      this.author = getTextValueOrDefault(root, "author", "");
      this.description = getTextValueOrDefault(root, "description", "");
      this.arrangement = getTextValueOrDefault(root, "neighbors", "");

      if (typeName.isEmpty() || title.isEmpty() || author.isEmpty() || description.isEmpty()) {
        throw new InvalidConfigurationException("Missing_simulation_information");
      }
    } catch (InvalidConfigurationException e) {
      e.showErrorDialog();
      System.exit(1);
    }
  }

  /**
   * Retrieves the state colors specified in the XML file.
   *
   * @return a map containing state-color pairs, or null if no colors are specified
   */
  public Map<String, String> getStateColors() {
    //it's default
    if (root.getElementsByTagName("colors").getLength() == 0) {
      return null;
    }
    Element colors = (Element) this.root.getElementsByTagName("colors").item(0);
    NamedNodeMap states = colors.getAttributes();
    Map<String, String> map = new HashMap<>();

    //Find each color value pairing and add it to the map
    for (int i = 0; i < states.getLength(); i++) {
      Node attribute = states.item(i);
      map.put(attribute.getNodeName().trim().toLowerCase(),
          attribute.getNodeValue().trim().toLowerCase());
    }
    return map;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public String getTypeName() {
    return this.typeName;
  }

  public String getTitle() {
    return this.title;
  }

  public String getAuthor() {
    return this.author;
  }

  public String getDescription() {
    return this.description;
  }

  public String getArrangement() {
    return arrangement;
  }

  public int[][] getCells() {
    try {
      return getCellsHelper();
    } catch (InvalidConfigurationException e) {
      e.showErrorDialog();
      System.exit(1);
      return null;
    }
  }

  /**
   * Helper method to retrieve cell states from the XML file and initialize the grid accordingly. If
   * randomization is enabled, it calls the randomization method to assign states based on
   * proportions. Otherwise, it uses cell state values from the XML file.
   *
   * @return a 2D array representing the grid of cell states
   * @throws InvalidConfigurationException if there is an error in the configuration or cell states
   */
  private int[][] getCellsHelper() throws InvalidConfigurationException {
    int[][] grid = new int[width][height];
    NodeList cells = this.root.getElementsByTagName("cell");
    NodeList randomizeNodes = root.getElementsByTagName("randomize");
    if (randomizeNodes.getLength() > 0) {
      Element randomizeElement = (Element) randomizeNodes.item(0);
      String randomizeValue = randomizeElement.getAttribute("value");
      if (randomizeValue.equalsIgnoreCase("yes")) {
        // Randomize cell state values based on proportions
        RandomizeXml.randomizeCellStates(cells, grid, randomizeElement);
      }
    } else {
      // Use cell state values from the XML file
      for (int i = 0; i < cells.getLength(); i++) {
        Element current = (Element) cells.item(i);
        int row = Integer.parseInt(current.getAttribute("row"));
        int col = Integer.parseInt(current.getAttribute("column"));
        int state = Integer.parseInt(current.getAttribute("state"));

        int maxStateValue = maxStateValues.get(typeName);

        if (state < 0 || state > maxStateValue) {
          throw new InvalidConfigurationException("Invalid_cell_state_value_for_game_type");
        }
        if (row < 0 || row >= this.width || col < 0 || col >= this.height) {
          throw new InvalidConfigurationException(
              "invalidCellLocation", row, col);
        }
        // Populate the grid with these states
        grid[row][col] = state;
      }
    }
    return grid;
  }

  /**
   * Retrieves the GameType specified in the XML file.
   *
   * @return a GameType object corresponding to the specified game type along with its parameters
   */
  public GameType<Cell> getGameTypeFromName() {
    typeName = typeName.replaceAll("\\s", "").toLowerCase();
    try {
      switch (typeName) {
        case "fallingsand":
          currentGame = new FallingSand(getCells());
          break;
        case "foragingants":
          int antsInNest = Integer.parseInt(
              getTextValueOrDefault(root, "antsInNest", "10"));
          int maxPheromoneLevel = Integer.parseInt(
              getTextValueOrDefault(root, "maxPheromoneLevel", "500"));
          additionalParams.put("antsInNest", "" + antsInNest);
          additionalParams.put("maxPheromoneLevel", "" + maxPheromoneLevel);
          currentGame = new ForagingAnts(getCells(), antsInNest, maxPheromoneLevel);
          currentGame.setStateMap(StateNamesMap.ants);
          break;
        case "percolation":
          currentGame = new Percolation(getCells());
          currentGame.setStateMap(StateNamesMap.percolation);
          break;

        case "watorworld":
          int repAgeLimit = Integer.parseInt(
              getTextValueOrDefault(root, "reproductionAgeLimit", "5"));
          int startingEnergy = Integer.parseInt(getTextValueOrDefault(root, "startingEnergy", "3"));
          int fishEnergy = Integer.parseInt(getTextValueOrDefault(root, "fishEnergy", "1"));
          additionalParams.put("repAgeLimit", "" + repAgeLimit);
          additionalParams.put("startingEnergy", "" + startingEnergy);
          additionalParams.put("fishEnergy", "" + fishEnergy);
          currentGame = new WaTorWorld(getCells(), repAgeLimit, startingEnergy, fishEnergy);
          currentGame.setStateMap(StateNamesMap.wator);
          break;

        case "spreadingoffire":
          double probCatch = Double.parseDouble(getTextValueOrDefault(root,
              "probCatch", defaultProbability));
          double probFillTree = Double.parseDouble(
              getTextValueOrDefault(root, "probFillTree", defaultProbability));
          additionalParams.put("probCatch", "" + probCatch);
          additionalParams.put("probFillTree", "" + probFillTree);
          currentGame = new SpreadingOfFire(getCells(), probCatch, probFillTree);
          currentGame.setStateMap(StateNamesMap.spreadingOfFire);
          break;

        case "modelofsegregation":
          double neighborPercentage = Double.parseDouble(getTextValueOrDefault(root,
              "neighborPercentage", defaultProbability));
          additionalParams.put("neighborPercentage", "" + neighborPercentage);
          currentGame = new ModelOfSegregation(getCells(), neighborPercentage);
          currentGame.setStateMap(StateNamesMap.segregation);
          break;

        case "gameoflife":
          currentGame = new GameOfLife(getCells());
          currentGame.setStateMap(StateNamesMap.gameOfLife);
          break;

        default:
          throw new InvalidConfigurationException("Invalid_game_type");
      }
      for (HashMap.Entry<String, String> e : additionalParams.entrySet()) {
        if (Double.parseDouble(e.getValue()) < 0) {
          throw new InvalidConfigurationException(
              "negativeParameterValue",
              e.getKey()
          );
        }
      }
    } catch (InvalidConfigurationException e) {
      e.showErrorDialog();
      System.exit(1);
    }
    return currentGame;
  }

  /**
   * Retrieves the text content of the specified tag name from the given XML element. If the tag is
   * found and has non-empty text content, returns the trimmed text content. Otherwise, returns the
   * default value.
   *
   * @param e            the XML element to search for the tag
   * @param tagName      the name of the tag to retrieve text content from
   * @param defaultValue the default value to return if the tag is not found or has empty text
   *                     content
   * @return the text content of the specified tag or the default value
   */
  private String getTextValueOrDefault(Element e, String tagName, String defaultValue) {
    NodeList nodeList = e.getElementsByTagName(tagName);
    if (nodeList.getLength() > 0) {
      String textContent = nodeList.item(0).getTextContent().trim();
      if (!textContent.isEmpty()) {
        return textContent;
      }
    }
    return defaultValue;
  }

  // display given message to user using the given type of Alert dialog box
  void showMessage(Alert.AlertType type, String message) {
    message = LanguageManager.getText(message);
    new Alert(type, message).showAndWait();
  }

  /**
   * Creates a new XML configuration file based on the current state.
   *
   * @return the newly created XML document
   */
  public Document createConfigFile(String newTitle, String newAuthor, String newDesc) {
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document newFile = builder.newDocument();

      //create the root
      Element root = newFile.createElement("simulation");
      newFile.appendChild(root);

      //Add the new edited info, if the user put it in
      if (!newTitle.isEmpty()) {
        this.title = newTitle;
      }
      if (!newAuthor.isEmpty()) {
        this.author = newAuthor;
      }
      if (!newDesc.isEmpty()) {
        this.description = newDesc;
      }

      //create all the basic info tags
      createAndAppend(newFile, "width", root, "" + this.width);
      createAndAppend(newFile, "height", root, "" + this.height);
      createAndAppend(newFile, "gameType", root, this.typeName);
      createAndAppend(newFile, "title", root, this.title);
      createAndAppend(newFile, "author", root, this.author);
      createAndAppend(newFile, "description", root, this.description);

      //additional param tags
      for (HashMap.Entry<String, String> e : additionalParams.entrySet()) {
        createAndAppend(newFile, e.getKey(), root, e.getValue());
      }
      int[][] newGrid = currentGame.createStateGrid();
      //add new wrapper element for grid
      Element cellRoot = createAndAppend(newFile, "grid", root, null);
      //populate document with all the cells
      root.appendChild(cellRoot);
      populateCellsToFile(newGrid, cellRoot, newFile);

      return newFile;
    } catch (ParserConfigurationException e) {
      showMessage(Alert.AlertType.ERROR, "Invalid_XML_Configuration");
      System.exit(1);
      return null;
    }
  }


  /**
   * Creates a new Element with the specified tagName, sets its text content to the given content,
   * appends it to the specified root Element, and returns the newly created Element.
   *
   * @param file    the Document to which the new Element belongs
   * @param tagName the tag name of the new Element
   * @param root    the root Element to which the new Element will be appended
   * @param content the text content of the new Element
   * @return the newly created Element
   */
  private Element createAndAppend(Document file, String tagName, Element root, String content) {
    Element child = file.createElement(tagName);
    child.setTextContent(content);
    root.appendChild(child);
    return child;
  }

  /**
   * Populates the XML document with cell elements representing the grid.
   *
   * @param grid     the grid containing cell states
   * @param cellRoot the root Element for cell elements
   * @param file     the XML Document
   */
  private void populateCellsToFile(int[][] grid, Element cellRoot, Document file) {
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[0].length; col++) {
        Element cell = createAndAppend(file, "cell", cellRoot, null);
        cell.setAttribute("row", "" + row);
        cell.setAttribute("column", "" + col);
        cell.setAttribute("state", "" + grid[row][col]);
      }
    }
  }
}
