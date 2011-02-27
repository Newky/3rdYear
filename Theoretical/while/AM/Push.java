package AM;

import State.*;
import PrettyPrinter.*;

/**
 * Push a value onto the stack
 */
public class Push extends Inst {
  /**
   * Value to push
   */
  private int n;

  /**
   * Constructor
   *
   * @param n Value to push
   */
  public Push(int n) {
    this.n = n;
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
    stack.push(n);
    return c;
  }
  
  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("PUSH " + n);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
