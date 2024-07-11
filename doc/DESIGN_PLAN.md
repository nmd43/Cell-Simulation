# Cell Simulation Design Plan

### Names: Nikita, Yasha, Jordan

## Simulation Models Implemented

* **Conway’s Game of Life**: A classic cellular automaton where cells on a grid live, die, or
  reproduce based on their neighbors.
* **Spreading of Fire**: This simulation models a forest fire, where trees catch fire and burn based
  on neighboring cells. It’s used to study percolation and critical phenomena, as well as fire
  spread in different forest densities.
* **Schelling’s Model of Segregation**: A model that demonstrates how individual preferences can
  lead to large-scale segregation. It simulates agents of different types moving based on
  neighborhood composition, revealing patterns of segregation.
* **Wa-Tor World**: A predator-prey ecosystem simulation. It models fish and sharks, with the
  dynamics of birth, death, and movement, providing insights into population dynamics and ecological
  systems.
* **Percolation**: Simulates fluid flow through a porous material.
* **Falling Sand**: Mimics the physical behaviors of granular materials by simulating how individual
  particles interact with each other and their environment such as gravity, water, etc.
* **Foraging Ants**: Models the behavior of ants searching for food, demonstrating how individual ants follow simple rules such as pheromone deposition and trail following to find and collect food efficiently

## Core Functionality:

* When a simulation is loaded from a configuration file, the state of the simulation should be
  displayed in Grid View where adjacent cells are adjacent in the visualization. Cell states should
  be mapped to colors so that all cells in the same state are the same color and cells in different
  states are different colors. When the simulation state changes, the Grid View should be updated to
  reflect the changes.
* When a simulation has been loaded, the simulator should show or make easily accessible the
  simulation’s descriptive information, including its type, name, author, description, state colors,
  and parameter values.
* Have a drop-down menu for a user to load a new simulation from a simulation configuration file.
  When the user invokes this feature, they may select a new simulation configuration from their
  project resources. Any current simulation is stopped and removed, and the new simulation is loaded
  in its initial state.
* When the user presses the start button, the simulation should execute steps at a rate determined
  by the current configuration until stopped.
* When the user presses the pause button, the simulation should stop advancing steps. The simulation
  state from when the pause button was pressed should be displayed to the user in the simulation
  view.
* Provide a slider to speed up and slow down the simulation. When the user speeds up/slows downs the
  simulation, the rate at which simulation steps occur should increase/decrease.
* Provide a mechanism to save the current state of the simulation. When the user invokes this save
  mechanism, the current state of the simulation should be saved as an XML
  configuration file matching the XML configuration specification. When the user loads this XML
  file, the simulation should return to the exact state it was in when the file was saved.
* When a user is saving a simulation state, before writing the configuration file, allow the user to
  add or alter information such as a title, author, description, and save location.
* Provide a reset button. When the user presses the reset button, the simulation should return to
  its initial state.

## Additional features:
* Different edge types that can be configured: toroidal, finite
* Display dynamic aggregate graphs of how different populations are changing over time
* Allow users to run multiple simulations at the same time, so they can compare the results side by side.
* Different neighbor types: Von Neumann, Moore
* Different grid topologies