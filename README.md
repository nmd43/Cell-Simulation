# Cell Simulation

## Contributors: Nikita, Yasha, Jordan

This project implements a cellular automata simulator for 7 different models in 3 languages. This
program allows users to view different cellular automata simulations through a single
platform. It has an intuitive user interface, where users can choose which simulation
model they would like to explore, engage with features such as learning about the simulation’s
features, authors, etc. The simulation can also change speed, be paused, and downloaded.

### Timeline

* Start Date: Jan 25, 2024

* Finish Date: Feb 13, 2024

* Hours Spent: 70

### Running the Program

* Main class: Main.java in the /view folder.

* Data files needed: All data files are organized by game type in the /data directory. You can use
  any file in each directory you want to run the specified simulation.

* Testing data files:
    * cell27_emptyData_default.xml : Has empty parameters that are replaced with default values.
    * cell28_invalidData_gameType.xml : Has an invalid Game Type value.
    * cell29_invalidState.xml : Has an invalid state value for a cell. The (row, col) values are
      dynamic but do not impact the language of the error message.
    * cell30_invalidCellBounds.xml : Has a cell that is out of grid bounds.
    * cell31_badFormat.xml : The XML file has missing tags leading to a poor format.
    * cell31_emptyFile.xml : The XML file is empty.
    * cell31_nonXml.txt : The users are not provided the option to select a file that isn't saved as
      an XML.
    * cell38_customColors.xml : There is an option to customize the cell colors by state in the XML.
    * All games have test1 in Spanish, test2 in French, and test3 in an unrecognized language.
    * All games have test3 with wrapped/toroidal edges

* Key/Mouse inputs:
    * Key inputs include typing new values to change parameters in the "More" button, as well as
      changing
      simulation information in the edit save dialogue box.
    * You can use your mouse to click any of the buttons on the Gui (reset, start, stop, load new,
      save, more (modifications)).
    * You can also click on any cell in the grid, and it will change the state of that cell
      dynamically.
      It is updated real time so that surrounded cells are impacted by the next iteration.
    * You need to use your mouse for things involving files like file choosing, directory save, etc.

### Notes/Assumptions

* The design goals and outline can be found under doc/DESIGN.md
* All implemented features and functionality can be found under doc/DESIGN_PLAN.md

* Noteworthy Features:
    * Each concurrent simulation has different timelines and displays the simulation info for each.
      This was so users had the opportunity to pause either at their leisure instead of them all
      depending on each other.
    * Borders and timeline can be changed with or without having to stop the simulation.

    

