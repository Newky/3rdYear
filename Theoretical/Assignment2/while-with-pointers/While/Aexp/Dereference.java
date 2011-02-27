package While.Aexp;

import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Dereferencing (*e) 
 */
public class Dereference extends Aexp {
  /**
   * Expression that will return the location in memory to dereference
   */
  private Aexp a;

  /**
   * Constructor
   *
   * @param a Expression that will return the location in memory to dereference 
   */
  public Dereference(Aexp a) {
    this.a = a;
  }

  /**
   * Big-step semantics
   *
   * @return Value of the variable in the state
   */
  public int eval(Memory memory) {
    /* EXERCISE */
    return memory.fetch(a.eval(memory));
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
  public Aexp reduce(Memory memory) {
    /* EXERCISE */
    return new ConstantInt(memory.fetch(a.eval(memory)));
  }

  /**
   * Compilation
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    /* EXERCISE */
    // Remove the next line when you implement this method; it's just there
    // to keep the compiler happy:
    return null;
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.unaryOp("*", a);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
