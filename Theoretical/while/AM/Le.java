package AM;

import State.*;
import PrettyPrinter.*;

/**
 * Replace the top two integers on the stack with true if the first
 * is equal to or less than the second, or false otherwise 
 */
public class Le extends Inst {
  /**
   * Constructor
   */
  public Le() {
  }

  /**
   * Execute the instruction (operational semantics)
   *
   * @param c Instructions to execute after this one
   * @param state Current state (updated in-place)
   * @param stack Current evaluation stack (updated in-place)
   * @return Remaining instructions to execute
   */
  public Code reduce(Code c, Stack stack, State state) {
    stack.push(stack.popInt() <= stack.popInt());
    return c;
  }
  
  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("LE");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
