# Breakout Abstractions Lab Discussion
### NAMES: Yasha, Jordan, Nikita
### Block

This superclass's purpose as an abstraction:
```java
 public class Block {
     public int something ()
//This would be a superclass representing all types of blocks (exploding, multiple lives, moving, etc) 
 }
 //Getters/setters for X/Y positions, height, width, color/fill
//these are not abstract methods.
```

### Subclasses
* The exploding subclass would have a method to destroy the neighboring blocks
* The multiple lives subclass would have an instance variable representing the number of lives and a method to decrement the number of lives.
* The moving subclass would have information about the speed and direction of movement

### Affect on Game/Level class

* Methods handling collisions with the ball – it would be the same method for any type of Block
* Keeping record of all blocks–you just have to determine which objects in the scene are type of (Block) instead of all the individual variations of blocks
* Lets you make variations of blocks very easily that still operate like a general block does

### Power-up 

This superclass's purpose as an abstraction:
```java
 public class PowerUp {
     public int something ()
     // This is a superclass representing all different types of power-ups (including poisons/power-downs)
  //Getters and setters for positions, velocity, speed, size, etc  
 }
```

### Subclasses
* The speed-up class would have variables and methods for increasing the ball speed
* The wide-paddle class would have variables for the new paddle width and a method to increase the size
* The safety-net class would alter the ball’s behavior to bounce off of the bottom edge of the screen


### Affect on Game/Level class

* You can still have variation in your powerups while easily being able to keep track of power-ups/downs in the scene by just checking if they are of the superclass type


### Others?
