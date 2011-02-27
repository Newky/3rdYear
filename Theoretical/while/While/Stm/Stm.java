package While.Stm;

import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Statements
 */
abstract public class Stm implements Printable {
  /**
   * Big-step semantics
   *
   * @param state Initial state (to be updated in-place)
   */
  abstract public void eval(State state);

  /**
   * Small-step semantics
   *
   * @param state Current state (to be updated in-place)
   * @return The next statement to be executed, or null for none
   */
  abstract public Stm reduce(State state); 

  /**
   * Compilation to stack code
   *
   * @return List of abstract machine (AM) instructions
   */
  abstract public Code compile();
}
