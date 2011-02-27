package While.Stm;

import While.Aexp.*;
import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Assignment
 */
public class Assign extends Stm {
  /**
   * The variable to assign to
   */
  private String x;

  /**
   * The expression to assign
   */
  private Aexp a;

  /**
   * Constructor
   *
   * @param x The variable to assign to
   * @param a The expression to assign
   */
  public Assign(String x, Aexp a) {
    this.x = x;
    this.a = a;
  }

  /**
   * Big-step semantics
   *
   * @param memory Initial memory 
   */
  public void eval(Memory memory) {
    /* EXERCISE */
    memory.store(MemoryLayout.addressOf(x), a.eval(memory));
  }

  /**
   * Small-step semantics
   *
   * @param memory Current memory 
   */
  public Stm reduce(Memory memory) {
    memory.store(MemoryLayout.addressOf(x), a.eval(memory));
    return null;
  }

  /**
   * Compilation to stack code
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    /* EXERCISE */
    return a.compile().append(new AM.Store());
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print(x + " := " + a);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
