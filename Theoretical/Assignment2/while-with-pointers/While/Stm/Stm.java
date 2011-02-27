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
   * @param memory Initial memory (to be updated in-place)
   */
  abstract public void eval(Memory memory);

  /**
   * Small-step semantics
   *
   * @param memory Current memory (to be updated in-place)
   * @return The next statement to be executed, or null for none
   */
  abstract public Stm reduce(Memory memory); 

  /**
   * Compilation to stack code
   *
   * @return List of abstract machine (AM) instructions
   */
  abstract public Code compile();
}
