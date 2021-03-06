package While.Stm;

import While.Aexp.*;
import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Skip (do nothing)
 */
public class Skip extends Stm {
  /**
   * Constructor
   */
  public Skip() {
  }

  /**
   * Big-step semantics
   *
   * @param memory Initial memory 
   */
  public void eval(Memory memory) {
  }

  /**
   * Small-step semantics
   *
   * @param memory Current memory 
   */
  public Stm reduce(Memory memory) {
    return null;
  }
  
  /**
   * Compilation to stack code
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return new Code(new AM.Noop());
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print("skip");
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
