package While.Stm;

import While.Aexp.*;
import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Skip (do nothing)
 */
public class Skip extends Stm {
  /**
   * Constructor
   */
  public Skip() {
  }

  /**
   * Big-step semantics
   *
   * @param state Initial state
   */
  public void eval(State state) {
  }

  /**
   * Small-step semantics
   *
   * @param state Current state
   */
  public Stm reduce(State state) {
    return null;
  }
  
  /**
   * Compilation to stack code
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return new Code(new AM.Noop());
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("skip");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
