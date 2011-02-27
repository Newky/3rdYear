package While.Aexp;

import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Constant integer 
 */
public class ConstantInt extends Aexp {
  /**
   * Value of the integer
   */
  private int n;
 
  /**
   * Constructor
   *
   * @param n Value of the integer
   */
  public ConstantInt(int n) {
    this.n = n;
  }

  /**
   * Big-step semantics
   *
   * @return Value of the integer
   */
  public int eval(State state) {
    return n;
  }

  /**
   * Is this expression a value?
   *
   * @return True
   */
  public boolean isValue() {
    return true;
  }

  /**
   * Value of the expression
   *
   * @return The value of the integer
   */
  public int value() {
    return n;
  }

  /**
   * Compilation
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return new Code(new AM.Push(n));
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print(n);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
