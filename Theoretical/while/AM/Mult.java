package AM;

import State.*;
import PrettyPrinter.*;

/**
 * Replace the top two integers on the stack with their product 
 */
public class Mult extends Inst {
  /**
   * Constructor
   */
  public Mult() {
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
    stack.push(stack.popInt() * stack.popInt());
    return c;
  }
  
  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("MULT");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
