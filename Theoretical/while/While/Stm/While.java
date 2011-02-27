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
   * @param state Initial state
   */
  public void eval(State state) {
    if(b.eval(state)) {
      s.eval(state);
      this.eval(state);
    }
  }

  /**
   * Small-step semantics
   *
   * @param state Current state
   */
  public Stm reduce(State state) {
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
