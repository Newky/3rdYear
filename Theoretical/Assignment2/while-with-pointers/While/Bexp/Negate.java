package While.Bexp;

import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Negate a boolean expression 
 */
public class Negate extends Bexp {
  /**
   * Operand
   */
  private Bexp b;
  
  /**
   * Constructor
   *
   * @param b Operand
   */
  public Negate(Bexp b) {
    this.b = b;
  }

  /**
   * Big-step semantics 
   *
   * @return Value of the expression
   */
  public boolean eval(Memory memory) {
    return !b.eval(memory); 
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
   * @return The expression after taking a single reduction step
   */
  public Bexp reduce(Memory memory) {
    if(!b.isValue())
      return new Negate(b.reduce(memory));
    else 
      return new ConstantBool(!b.value());  
  }

  /**
   * Compilation
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return b.compile().append(new AM.Neg());
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.unaryOp("!", b);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
