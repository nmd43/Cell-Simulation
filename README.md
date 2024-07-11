# Cell Society
## 6
## Jordan, Nikita, Yasha


This project implements a cellular automata simulator.

### Timeline

 * Start Date: Jan 25, 2024

 * Finish Date: Feb 13, 2024

 * Hours Spent: Collectively, probably 60+



### Attributions

 * Resources used for learning (including AI assistance)
   * ChatGPT
   * StackOverflow
   * YouTube
 
 * Resources used directly (including AI assistance)
   * ChatGPT
   * StackOverflow
   * YouTube


### Running the Program

 * Main class: Main.java in the /view folder.

 * Data files needed: All data files are organized by game type in the /data directory. You can use 
any file in each directory you want to run the specified simulation.

 * Interesting data files:
   * cell27_emptyData_default.xml : Has empty parameters that are replaced with default values.
   * cell27_emptyData_error.xml : Has missing descriptive data (i.e. authors)
   * cell28_invalidData_gameType.xml : Has an invalid Game Type value.
   * cell28_invalidData_param.xml : Has a negative parameter value. The param values are dynamic but do not impact the
   language of the error message.
   * cell29_invalidState.xml : Has an invalid state value for a cell. The (row, col) values are dynamic but do not
   impact the language of the error message.
   * cell30_invalidCellBounds.xml : Has a cell that is out of grid bounds.
   * cell31_badFormat.xml : The XML file has missing tags leading to a poor format.
   * cell31_emptyFile.xml : The XML file is empty.
   * cell31_nonXml.txt : The users are not provided the option to select a file that isn't saved as an XML.
   * cell38_customColors.xml : There is an option to customize the cell colors by state in the XML.
   * All games have test1 in Spanish, test2 in French, and test3 in an unrecognized language.
   * All games have test3 with wrapped/toroidal edges

 * Key/Mouse inputs:
   * Key inputs include typing new values to change parameters in the "More" button, as well as changing
   simulation information in the edit save dialogue box.
   * You can use your mouse to click any of the buttons on the Gui (reset, start, stop, load new, 
   save, more (modifications)).
   * You can also click on any cell in the grid, and it will change the state of that cell dynamically.
   It is updated real time so that surrounded cells are impacted by the next iteration.
   * You need to use your mouse for things involving files like file choosing, directory save, etc.


### Notes/Assumptions

 * Assumptions or Simplifications:
   * For the changes, we assumed that multiple concurrent simulations meant 2 or more. Our program works
   to display two simulations at once.
   * Users are familiar with cellular automata – we have no splash screen with an introduction.
   * Pop-up window title and content could be in English. Error messages and buttons would be translated.

 * Known Bugs:
   * No known bugs

 * Features implemented:
   * In the Cell Society - Basic guidelines, we implemented all core features and all extensions.
   * In the Cell Society change, we implemented the following:
     * Simulations
       * CELL-26A, CELL-26B
     * XML Configurations
       * CELL-27,28,29,30,31
     * XML Configuration Initialization
       * CELL 32-A
     * Grid Topology
       * CELL-33A
     * Cell Neighbors
       * CELL-34A
     * Graphical User Interface
       * CELL-36,37,38
     * Different Views
       * CELL-40A
     * Dynamic Updates
       * CELL-41A, 41B, 41C

 * Features unimplemented:
   * Cell Shape (Cell-35A, 35B, or 35X), and the Extension (CELL-39)

 * Noteworthy Features:
   * Each concurrent simulation has different timelines and displays the simulation info for each.
   This was so users had the opportunity to pause either at their leisure instead of them all depending
   on each other. Also, this was important because a user might want to run simulations of different 
   game types.
   * Borders and timeline can be changed with or without having to stop the simulation.



### Assignment Impressions
* We definitely found this to be a very challenging assignment. We planned pretty well, and much of 
our design didn't change much other than when variations started to be added (and this was mainly for 
refactoring purposes). Along that note, variations were challenging, but more doable than expected because
of our implemented project design.
* The concept of cellular automata took a bit of getting used to, and we had to do a lot of reading up
on each game to understand what was going on.
* This was a much more complicated GUI than the previous project. In particular, it was difficult getting
things to stay in the correct places. We got a lot of experience in this assignment with trying to 
work with JavaFX and CSS in conjunction.

