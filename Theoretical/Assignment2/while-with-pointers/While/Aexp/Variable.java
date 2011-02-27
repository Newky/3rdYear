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
  public int eval(Memory memory) {
    /* EXERCISE */
    // Remove the next line when you implement this method; it's just there
    // to keep the compiler happy:
    return memory.fetch(MemoryLayout.addressOf(x));
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
    return new ConstantInt(memory.fetch(MemoryLayout.addressOf(x)));
  }

  /**
   * Compilation
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    /* EXERCISE */
    return new Code(new AM.Fetch());
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
