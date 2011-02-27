package While.Bexp;

import While.Aexp.*;
import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Is one arithmetic expression less than or equal to another? 
 */
public class LessOrEqual extends Bexp {
  /**
   * Left operand
   */
  private Aexp a1;
  
  /**
   * Right operand
   */
  private Aexp a2;

  /**
   * Constructor
   *
   * @param a1 Left operand
   * @param a2 Right operand
   */
  public LessOrEqual(Aexp a1, Aexp a2) {
    this.a1 = a1;
    this.a2 = a2;
  }

  /**
   * Big-step semantics 
   *
   * @return Value of the expression
   */
  public boolean eval(Memory memory) {
    return a1.eval(memory) <= a2.eval(memory); 
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
    if(!a1.isValue())
      return new LessOrEqual(a1.reduce(memory), a2);
    else if(!a2.isValue())
      return new LessOrEqual(a1, a2.reduce(memory));
    else 
      return new ConstantBool(a1.value() <= a2.value());  
  }

  /**
   * Compilation
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return a2.compile().append(a1.compile()).append(new AM.Le());
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.binOp(a1, "<=", a2);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
