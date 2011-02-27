package AM;

import State.*;
import PrettyPrinter.*;

/**
 * Push the boolean "True" onto the stack 
 */
public class True extends Inst {
  /**
   * Constructor
   */
  public True() {
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
    stack.push(true);
    return c;
  }
  
  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("TRUE");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
