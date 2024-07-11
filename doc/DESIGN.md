# Cell Simulation Design

### NAMES: Nikita, Yasha, Jordan

## Design goals

* The design follows the Liskov principle and View Model separation principle.
  Our config modules were focused on error handling, XML parsing/creation, and file management (
  retrieving/ downloading files). Our model contained the backend files of our cell and gametype
  classes, and these classes internally only interact with each other. Finally, our view module
  contains anything JavaFX and frontend related and operates with the backend through very specific
  classes so that users cannot edit the backend unintentionally.

## High-level Design

#### Core Classes

* /view/Main: runs the program.
* /models/gametypes/GameType: the abstract superclass for all gametype subclasses. These handle all
  basic functionalities and provide abstract method signatures that every subclass must implement
  for
  effortless integration to the front-end.
* /models/cells/Cell: The superclass for all gametype cells. Provides basic functionality necessary
  to run a bare-bones game, and you can customize the cell by adding more methods as necessary.
* /view/Gui: the wrapper class for all front-end widgets and display.
* /view/GamePlay: the wrapper class that connects the front-end to the back-end and initializes the
  timeline for the game.

#### Easy to Add Features

* It is easy to add a new game. Simply create a subclass of the Cell class, which handles basic
  properties of any game cell, and implement whatever additional methods are needed for this
  particular cell (
  if any).
* Then, implement a subclass of the GameType class, and implement all abstract methods. Add
  additional methods if necessary.
* Assets like new edit windows, about boxes, and grids are also easy to add on the front end. Our
  module called assets within view contains a variety of front-end widgets that help in the creation
  of a front-end, and you can use these without having to implement them every single time.


