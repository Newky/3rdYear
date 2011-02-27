package While.Aexp;

import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Variable 
 */
public class Variable extends Aexp {
  /**
   * Name of the variable
   */
  private String x;

  /**
   * Constructor
   *
   * @param x Name of the variable
   */
  public Variable(String x) {
    this.x = x;
  }

  /**
   * Big-step semantics
   *
   * @return Value of the variable in the state
   */
  public int eval(State state) {
    return state.fetch(x);
  }

  /**
   * Is this expression a value? 
   *
   * @return False
   */
  public boolean isValue() {
    return false;
  }

  /**
   * Small-step semantics
   *
   * @return Value of the variable in the state
   */
  public Aexp reduce(State state) {
    return new ConstantInt(state.fetch(x));
  }

  /**
   * Compilation
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return new Code(new AM.Fetch(x));
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print(x);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
