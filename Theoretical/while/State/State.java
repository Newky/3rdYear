package State;

public class State {
  /**
   * State is represented internally by a hashtable
   */
  protected java.util.Hashtable<String, Integer> hash;

  /**
   * Constructor
   */
  public State() {
    hash = new java.util.Hashtable<String, Integer>();
  }

  /**
   * Read a variable from the store
   *
   * @param x Name of the variable
   */
  public int fetch(String x) {
    if(!hash.containsKey(x)) {
      System.out.println("Warning: read from '" + x + "' which has not been given a value");
      return 0;
    }
    else {
      return hash.get(x);
    }
  }

  /**
   * Write a variable to the store
   *
   * @param x Name of the variable
   * @param n Value to write
   */
  public void store(String x, int n) {
    hash.put(x, new Integer(n));
  }

  /**
   * Pretty-printing
   */
  public String toString() {
    return hash.toString();
  }
}
