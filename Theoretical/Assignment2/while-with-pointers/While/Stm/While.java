package While.Stm;

import While.Bexp.*;
import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * While-loop 
 */
public class While extends Stm {
  /**
   * Condition
   */
  private Bexp b;

  /**
   * Statement to execute while condition is true 
   */
  private Stm s;

  /**
   * Constructor
   *
   * @param b Condition
   * @param s Statement to execute while condition is true 
   */
  public While(Bexp b, Stm s) {
    this.b = b;
    this.s = s;
  }

  /**
   * Big-step semantics
   *
   * @param memory Initial memory 
   */
  public void eval(Memory memory) {
    if(b.eval(memory)) {
      s.eval(memory);
      this.eval(memory);
    }
  }

  /**
   * Small-step semantics
   *
   * @param memory Current memory 
   */
  public Stm reduce(Memory memory) {
    return new If(b, new Seq(s, this), new Skip());
  }
  
  /**
   * Compilation to stack code
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return new Code(new AM.Loop(b.compile(), s.compile()));
  }

  /**
   * Pretty-printing
   *
   * { while condition do
   *     s1
   * }
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("{ while ").print(b).print(" do");
    pp.pushIndent("    ").print(s).popIndent();
    pp.print("}");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
