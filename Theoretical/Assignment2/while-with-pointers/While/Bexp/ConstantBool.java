package While.Bexp;

import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Constant boolean 
 */
public class ConstantBool extends Bexp {
  /**
   * Value of the boolean 
   */
  private boolean b;
 
  /**
   * Constructor
   *
   * @param b Value of the boolean 
   */
  public ConstantBool(boolean b) {
    this.b = b;
  }

  /**
   * Big-step semantics
   *
   * @return Value of the boolean 
   */
  public boolean eval(Memory memory) {
    return b;
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
   * @return The value of the boolean 
   */
  public boolean value() {
    return b;
  }

  /**
   * Compilation
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return new Code(b ? new AM.True() : new AM.False());
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print(b ? "true" : "false");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
