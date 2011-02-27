package AM;

import State.*;
import PrettyPrinter.*;

/**
 * If the stack contains
 *
 *   |  .  |
 *   |  .  |
 *   |  z  |
 *   |  l  |
 *   |-----|
 *
 * pop off z and l, and update memory location l to be z
 */
public class Store extends Inst {
  /**
   * Constructor
   *
   * @param x Variable to store 
   */
  public Store() {
  }

  /**
   * Execute the instruction (operational semantics)
   *
   * @param c Instructions to execute after this one
   * @param memory Current memory (updated in-place)
   * @param stack Current evaluation stack (updated in-place)
   * @return Remaining instructions to execute
   */
  public Code reduce(Code c, Stack stack, Memory memory) {
    memory.store(stack.popInt(), stack.popInt());
    return c;
  }
  
  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("STORE");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
