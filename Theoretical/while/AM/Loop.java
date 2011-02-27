package AM;

import State.*;
import PrettyPrinter.*;

/**
 * Execute a bunch of instructions as long as another bunch 
 * of instructions leave 'true' on top of the stack
 */
public class Loop extends Inst {
  /**
   * Instructions to compute the loop guard 
   */
  private Code c1;

  /**
   * Instructions to execute as long as the guard is true 
   */
  private Code c2;
  
  /**
   * Constructor
   *
   * @param c1 Instructions to compute the loop guard 
   * @param c2 Instructions to execute as long as the guard is true 
   */
  public Loop(Code c1, Code c2) {
    this.c1 = c1;
    this.c2 = c2;
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
    return c1.append(new Branch(c2.append(this), new Code(new Noop())));
  }
    
  /**
   * Pretty-printing
   *
   * LOOP
   * (
   *   c1
   * ,
   *   c2
   * )
   */
  public void pretty(PrettyPrinter pp) {
    pp.println("LOOP").print("(");
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
