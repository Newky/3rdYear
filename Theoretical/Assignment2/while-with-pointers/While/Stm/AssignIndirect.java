package While.Stm;

import While.Aexp.*;
import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Indirect assignment (*x = a)
 */
public class AssignIndirect extends Stm {
  /**
   * Expression that will return the location in memory to write to 
   */
  private Aexp a1;

  /**
   * The expression to assign
   */
  private Aexp a2;

  /**
   * Constructor
   *
   * @param a1 Expression that will return the location in memory to write to 
   * @param a2 The expression to assign
   */
  public AssignIndirect(Aexp a1, Aexp a2) {
    this.a1 = a1;
    this.a2 = a2;
  }

  /**
   * Big-step semantics
   *
   * @param memory Initial memory 
   */
  public void eval(Memory memory) {
    /* EXERCISE */
    memory.store(a1.eval(), a2.eval());
  }

  /**
   * Small-step semantics
   *
   * @param memory Current memory 
   */
  public Stm reduce(Memory memory) {
    /* EXERCISE */
    // Remove the next line when you implement this method; it's just there
    // to keep the compiler happy:
    return null;
  }

  /**
   * Compilation to stack code
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
    pp.unaryOp("*", a1).print(" := ").print(a2);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
