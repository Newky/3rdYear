package While.Stm;

import While.Aexp.*;
import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Assignment
 */
public class Assign extends Stm {
  /**
   * The variable to assign to
   */
  private String x;

  /**
   * The expression to assign
   */
  private Aexp a;

  /**
   * Constructor
   *
   * @param x The variable to assign to
   * @param a The expression to assign
   */
  public Assign(String x, Aexp a) {
    this.x = x;
    this.a = a;
  }

  /**
   * Big-step semantics
   *
   * @param state Initial state
   */
  public void eval(State state) {
    state.store(x, a.eval(state));
  }

  /**
   * Small-step semantics
   *
   * @param state Current state
   */
  public Stm reduce(State state) {
    state.store(x, a.eval(state));
    return null;
  }

  /**
   * Compilation to stack code
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return a.compile().append(new AM.Store(x));
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print(x + " := " + a);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
