package AM;

import State.*;
import PrettyPrinter.*;

/**
 * Pop a boolean off the stack and branch accordingly 
 */
public class Branch extends Inst {
  /**
   * Instructions to execute in the true branch
   */
  private Code c1;

  /**
   * Instructions to execute in the false branch
   */
  private Code c2;
  
  /**
   * Constructor
   *
   * @param c1 Instructions to execute in the true branch
   * @param c2 Instructions to execute in the false branch
   */
  public Branch(Code c1, Code c2) {
    this.c1 = c1;
    this.c2 = c2;
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
    boolean top = stack.popBool();
    if(top)
      return c1.append(c);
    else
      return c2.append(c);
  }
  
  /**
   * Pretty-printing
   *
   * BRANCH
   * (
   *   c1
   * ,
   *   c2
   * )
   */
  public void pretty(PrettyPrinter pp) {
    pp.println("BRANCH").print("(");
    pp.pushIndent("  ").print(c1).popIndent();
    pp.print(",");
    pp.pushIndent("  ").print(c2).popIndent();
    pp.print(")");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
