package cellsociety.view.assets;

import java.util.HashMap;
import java.util.Map;

public class StateNamesMap {

  static String empty = "empty";
  public static final Map<Integer, String> gameOfLife = new HashMap<>();

  static {
    gameOfLife.put(0, "dead");
    gameOfLife.put(1, "alive");
  }

  public static final Map<Integer, String> spreadingOfFire = new HashMap<>();

  static {
    spreadingOfFire.put(0, empty);
    spreadingOfFire.put(1, "tree");
    spreadingOfFire.put(2, "burning");
  }

  public static final Map<Integer, String> percolation = new HashMap<>();

  static {
    percolation.put(0, empty);
    percolation.put(1, "blocked");
    percolation.put(2, "water");
  }

  public static final Map<Integer, String> segregation = new HashMap<>();

  static {
    segregation.put(0, empty);
    segregation.put(1, "group a");
    segregation.put(2, "group b");
  }

  public static final Map<Integer, String> wator = new HashMap<>();

  static {
    wator.put(0, empty);
    wator.put(1, "fish");
    wator.put(2, "shark");
  }

  public static final Map<Integer, String> ants = new HashMap<>();

  static {
    ants.put(0, empty);
    ants.put(1, "nest");
    ants.put(2, "food");
    ants.put(3, "ant");
    ants.put(4, "pheremone");
  }
}
