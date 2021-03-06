package While.Stm;

import While.Bexp.*;
import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Conditional 
 */
public class If extends Stm {
  /**
   * Condition
   */
  private Bexp b;

  /**
   * Statement to execute if condition is true 
   */
  private Stm s1;

  /**
   * Statement to execute if condition is false 
   */
  private Stm s2;

  /**
   * Constructor
   *
   * @param b Condition
   * @param s1 Statement to execute if condition is true 
   * @param s2 Statement to execute if condition is false 
   */
  public If(Bexp b, Stm s1, Stm s2) {
    this.b  = b;
    this.s1 = s1;
    this.s2 = s2;
  }

  /**
   * Big-step semantics
   *
   * @param memory Initial memory 
   */
  public void eval(Memory memory) {
    if(b.eval(memory)) 
      s1.eval(memory);
    else
      s2.eval(memory);
  }

  /**
   * Small-step semantics
   *
   * @param memory Current memory
   */
  public Stm reduce(Memory memory) {
    if(b.eval(memory))
      return s1;
    else
      return s2;
  }

  /**
   * Compilation to stack code
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return b.compile().append(new Branch(s1.compile(), s2.compile()));
  }

  /**
   * Pretty-printing
   *
   * { if condition then
   *     s1
   *   else
   *     s2
   * }
   */ 
  public void pretty(PrettyPrinter pp) {
    pp.print("{ if ").print(b).print(" then");
    pp.pushIndent("    ").print(s1).popIndent();
    pp.print("  else");
    pp.pushIndent("    ").print(s2).popIndent();
    pp.print("}");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
