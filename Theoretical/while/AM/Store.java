package AM;

import State.*;
import PrettyPrinter.*;

/**
 * Pop the top integer off the stack and write it to the store 
 */
public class Store extends Inst {
  /**
   * Variable to store 
   */
  private String x;

  /**
   * Constructor
   *
   * @param x Variable to store 
   */
  public Store(String x) {
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
    state.store(x, stack.popInt());
    return c;
  }
  
  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("STORE " + x);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
