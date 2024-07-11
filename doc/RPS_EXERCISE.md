# Rock Paper Scissors Lab Discussion
#### Names and NetIDs


### High Level Design Goals
* We wanted to make it easy to create new weapons and support an arbitrary number of weapons depending on what the programmer
wants. 
* We wanted to make relationships between killed//killer weapons easy and accessible.


### CRC Card Classes
* Weapon - abstract class, knows victim(s), knows killer(s), knows status, knows id potentially
  * has the abstract isKiller(Weapon weapon) and isVictim(Weapon weapon) methods
* Subclasses inherit the superclass Weapon (such as Rock, Paper, and Scissors)
  * Each subclass must implement the isKiller and isVictim methods
* Each Weapon class interacts with another Weapon class
* All classes interact with a GamePlay class that handles actual RPS playing
* There is a Player class, and each player has a chosen weapon and a score.

### Use Cases

* A new game is started with five players, their scores are reset to 0.
 ```java
List<Player> currentPlayers;
GamePlay round = new GamePlay(int playerAmount);
Player player = new Player(int score);
//Class that represents the current round gameplay
round.addPlayer(player);
 ```

* A player chooses his RPS "weapon" with which he wants to play for this round.
 ```java
player.setWeapon(Weapon weapon);
currentPlayers.add(player);
 ```

* Given three players' choices, one player wins the round, and their scores are updated.
 ```java
//some method that collects all player's weapons and conducts victim/killer interactions
//returns, if any, the player that won
GamePlay.determineWeaponInteractions(currentPlayers);
//player has an attribute that tells you if they're alive or not
for (Player p: currentPlayers)
    if (p.isAlive()) p.setScore(int someScore);
 ```

* A new choice is added to an existing game and its relationship to all the other choices is updated.
 ```java
//in the Game class, you have a master list of all the weapons
 Weapon newChoice = new Weapon(List<Weapon> killers, List<Weapon> allWeapons);
//constructor updates these relationships
 ```

* A new game is added to the system, with its own relationships for its all its "weapons".
 ```java
 // this implies you have already created a list of new weapons with updated killer/victim lists.
//potentially just a config data file, or a map
GamePlay newGame = new GamePlay(String xmlPathName);
//or
GamePlay newGame = new GamePlay(HashMap<Weapon, Weapon> mapping);
 ```