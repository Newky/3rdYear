package AM;

import State.*;
import PrettyPrinter.*;

/**
 * Pop an address l off the stack, and push the value in memory at location l
 * back onto the stack
 */
public class Fetch extends Inst {
  /**
   * Constructor
   */
  public Fetch() {
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
    stack.push(memory.fetch(stack.popInt())); 
    return c;
  }
  
  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("FETCH");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
