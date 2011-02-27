package AM;

import State.*;
import PrettyPrinter.*;

/**
 * Push the current value of the given variable onto the stack 
 */
public class Fetch extends Inst {
  /**
   * Variable to fetch
   */
  private String x;

  /**
   * Constructor
   *
   * @param x Variable to fetch 
   */
  public Fetch(String x) {
    this.x = x;
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
    stack.push(state.fetch(x));
    return c;
  }
  
  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("FETCH " + x);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
