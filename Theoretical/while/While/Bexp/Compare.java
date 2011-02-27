package While.Bexp;

import While.Aexp.*;
import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Is one arithmetic expression equal to another? 
 */
public class Compare extends Bexp {
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
  public Compare(Aexp a1, Aexp a2) {
    this.a1 = a1;
    this.a2 = a2;
  }

  /**
   * Big-step semantics 
   *
   * @return Value of the expression
   */
  public boolean eval(State state) {
    return a1.eval(state) == a2.eval(state); 
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
  public Bexp reduce(State state) {
    if(!a1.isValue())
      return new Compare(a1.reduce(state), a2);
    else if(!a2.isValue())
      return new Compare(a1, a2.reduce(state));
    else 
      return new ConstantBool(a1.value() == a2.value());  
  }

  /**
   * Compilation
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return a2.compile().append(a1.compile()).append(new AM.Eq());
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.binOp(a1, "=", a2);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
