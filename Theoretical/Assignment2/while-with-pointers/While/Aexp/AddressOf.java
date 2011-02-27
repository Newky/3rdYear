package While.Aexp;

import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Address of a variable (&amp;x) 
 */
public class AddressOf extends Aexp {
  /**
   * Name of the variable
   */
  private String x;

  /**
   * Constructor
   *
   * @param x Name of the variable
   */
  public AddressOf(String x) {
    this.x = x;
  }

  /**
   * Big-step semantics
   *
   * @return Value of the variable in the state
   */
  public int eval(Memory memory) {
  	return MemoryLayout.addressOf(x);	
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
  	return new ConstantInt(MemoryLayout.addressOf(x));	
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
    pp.print("&" + x);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
