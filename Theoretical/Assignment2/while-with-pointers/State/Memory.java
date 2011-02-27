package State;

/**
 * Memory is a (total) function from addresses (integers) to values
 */
public class Memory {
  /**
   * We represent memory internally by a hashtable
   */
  protected java.util.Hashtable<Integer, Integer> hash;

  /**
   * Constructor
   */
  public Memory() {
    hash = new java.util.Hashtable<Integer, Integer>();
  }

  /**
   * The value in the memory at a particular location 
   *
   * @param l Location to read 
   */
  public int fetch(int l) {
    if(!hash.containsKey(l)) {
      System.out.println("Warning: read from '" + l + "' which has not been given a value");
      return 0;
    }
    else {
      return hash.get(l);
    }
  }

  /**
   * Update the memory 
   *
   * @param l Location to write to 
   * @param n Value to write
   */
  public void store(int l, int n) {
    hash.put(l, n);
  }

  /**
   * Pretty-printing
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();

    buffer.append("{");

    java.util.Enumeration<Integer> keys = hash.keys();
    
    boolean isFirst = true;
    while(keys.hasMoreElements()) {
      int key = keys.nextElement();

      if(!isFirst) {
        buffer.append(", ");
      }

      String var = MemoryLayout.reverseLookup(key);
      if(var != null) {
        buffer.append(key + "(" + var + ")=" + fetch(key));
      } else {
        buffer.append(key + "=" + fetch(key));
      }

      isFirst = false;
    }

    buffer.append("}");

    return buffer.toString();
  }
}
