package AM;

import State.*;
import PrettyPrinter.*;

/**
 * Superclass for all the abstract machine (AM) instructors
 */
public abstract class Inst implements Printable {
  /**
   * Execute the instruction (operational semantics)
   *
   * @param c Instructions to execute after this one
   * @param state Current state (updated in-place)
   * @param stack Current evaluation stack (updated in-place)
   * @return Remaining instructions to execute
   */
  public abstract Code reduce(Code c, Stack stack, State state);
}
