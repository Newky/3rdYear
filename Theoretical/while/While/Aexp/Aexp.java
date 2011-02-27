package While.Aexp;

import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Arithmetic expression
 */
abstract public class Aexp implements Printable {
  /**
   * Big-step semantics
   *
   * @return Value of the expression
   */
  abstract public int eval(State state);

  /**
   * Is this expression a value?
   *
   * @return True if the expression is a value, False otherwise
   */
  abstract public boolean isValue();
 
  /**
   * Small-step semantics
   *
   * This should be implemented in all classes except those
   * representing values.
   *
   * @return The expression after taking a single reduction step
   */
  public Aexp reduce(State state) {
    throw new Error("Already a value");
  }

  /**
   * The value of the expression
   *
   * This should be implemented in all classses representing values
   *
   * @return The value of the expression
   */
  public int value() {
    throw new Error("Not a value");
  }

  /**
   * Compilation to stack code
   *
   * @return List of abstract machine (AM) instructions
   */
  abstract public Code compile();
}
