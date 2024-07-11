# Cell Society Design Final
### TEAM NUMBER: 6
### NAMES: Jordan, Nikita, Yasha


## Team Roles and Responsibilities

 * Team Member #1: Nikita 
   * Language Manager
   * XML Parsing/ File creation
   * Toroidal Edges
   * Error checking

 * Team Member #2: Jordan
   * Back-End (Simulation back-end implementation, model classes)

 * Team Member #3: Yasha
   * Front-end (GUI, GUI assets)
   * XML Parsing
   * File Management



## Design goals
* We wanted our design to be easy to understand, follow the Liskov principle, and follow the View
Model separation principle. As a result, we decided to split our modules into config, models, and view.
Our config modules were focused on error handling, XML parsing/creation, and file management (retrieving/
downloading files). Our models contained the backend files of our cell and gametype classes, and these
classes internally only interact with each other. Finally, our view module contains anything JavaFX
and frontend related and operates with the backend through very specific classes so that users cannot
edit the backend unintentionally. 
* Furthermore, our cell and gametype classes follow the Liskov principle because each subclass can be easily
  (and are) replaced with the superclass type. In our GamePlay wrapper class, which handles connection of
the frontend to the backend and handles the timeline, we utilize a GameType<Cell> object, which is the 
general form of any gametype and cell object.

#### What Features are Easy to Add
* It is easy to add a new game. Simply create a subclass of the Cell class, which handles basic properties
of any game cell, and implement whatever additional methods are needed for this particular cell (if any).
* Then, implement a subclass of the GameType class, and implement all abstract methods. Add additional methods
if necessary.
* Assets like new edit windows, about boxes, and grids are also easy to add on the front end. Our module
called assets within view contains a variety of front-end widgets that help in the creation of a front-end, 
and you can use these without having to implement them every single time.

## High-level Design

#### Core Classes
* /view/Main: runs the program.
* /models/gametypes/GameType: the abstract superclass for all gametype subclasses. These handle all basic
functionalities and provide abstract method signatures that every subclass must implement for effortless
integration to the front-end.
* /models/cells/Cell: The superclass for all gametype cells. Provides basic functionality necessary to
run a bare-bones game, and you can customize the cell by adding more methods as necessary.
* /view/Gui: the wrapper class for all front-end widgets and display.
* /view/GamePlay: the wrapper class that connects the front-end to the back-end and initializes the 
timeline for the game.

## Assumptions that Affect the Design

#### Features Affected by Assumptions
* In designing our project we assumed that users' primary language is English.  This affected how we
wrote the simulation descriptions and how we handled unspecified language exceptions.
* We assumed that the users' purpose in utilizing our program is primarily for entertainment.  We thought
of the simulations as being analogous to games as opposed to scientific models.  For these reasons we
chose to make our graphical interface aesthetic and engaging.  We wanted users to primarily feel entertained
watching the simulations as opposed to academically educated.  
* In implementing the simulation back-end, we made some assumptions about state transitions.  In simulations 
where cells would "move", we assumed that the moving cells knew to each choose unique
positions, so as not to occupy the same space.  In Wa-Tor World, we assumed that in every transition
the sharks moved before the fish.  In this way, it was determined which fish were eaten or alive before
altering the fish next states.


## Significant differences from Original Plan
With a few exceptions, we feel that our final design was closely modeled after our original plan.  The
time and intention we put into planning the design of and interactions between our classes definitely helped
our development velocity.
* State representation:  We initially planned to use enums to represent the different states for each simulation.
However, once we began programming we realized that enums were not conducive to the class interactions
we needed to implement.  We decided that the best course of alternative action would be to use integers
to represent states.  To maintain the immutable nature of an enum, we made the state integers constant variables
in the cell subclasses.
* Downloads Class:  We initially planned to create a downloads class to handle saving the XML configurations.
However, in practice we felt that much of the functionality required to save the simulation was already
implemented in the Parser class.  To follow the principle of not repeating code we decided it would be best
practice to build on top of our existing code instead of creating a new class.
* Class Organization: When we first planned our class structure, we were not aware of the View Model separation
principle.  We also did not have the exact details of the additional features we would be implementing in the
change stage of the project.  As a result, our final project contains some additional classes and is organized
in a view, model, config structure.  We felt that these additional classes were necessary to implement the additional
functionalities and maintain strong design.


## New Features HowTo

#### Easy to Add Features
* It is easy to add a new game. Simply create a subclass of the Cell class, which handles basic properties
  of any game cell, and implement whatever additional methods are needed for this particular cell (if any).
* Then, implement a subclass of the GameType class, and implement all abstract methods. Add additional methods
  if necessary.
* Assets like new edit windows, about boxes, and grids are also easy to add on the front end. Our module
  called assets within view contains a variety of front-end widgets that help in the creation of a front-end,
  and you can use these without having to implement them every single time.

#### Other Features not yet Done
* CELL-35: Our team was unable to implement different shapes for the cells since the GameType and Grid classes assumed a
a square/rectangular cell type to showcase neighbors as well as GUI aspects
* CELL-39: Our team did not implement different shapes within the cells due to time constraints

