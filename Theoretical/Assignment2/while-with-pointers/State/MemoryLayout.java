package State;

/**
 * Mapping from variable names to memory addresses
 */
public class MemoryLayout {
  /**
   * The global (singleton) memory layout object
   */
  static protected java.util.Hashtable<String, Integer> hash;

  /**
   * The next available address (we build the layout lazily)
   */
  static protected int nextAddress;

  /** 
   * Get the address of a variable
   *
   * We build the layout lazily
   */
  public static int addressOf(String x) {
    if(hash == null) {
      hash = new java.util.Hashtable<String, Integer>();
    }

    if(!hash.containsKey(x)) {
      hash.put(x, nextAddress++);
    }

    return hash.get(x);
  }

  /**
   * Reverse lookup: what is the name of the variable that points to an address?
   */
  public static String reverseLookup(int l) {
    if (hash == null)
      return null;

    java.util.Enumeration<String> keys = hash.keys();
    while(keys.hasMoreElements()) {
      String key = keys.nextElement();
      if(hash.get(key) == l)
        return key;
    }

    return null;
  }
}
