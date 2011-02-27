package While.Stm;

import While.Aexp.*;
import State.*;
import AM.*;
import PrettyPrinter.*;

/**
 * Sequential composition 
 */
public class Seq extends Stm {
  /**
   * First statement to execute
   */
  private Stm s1;

  /**
   * Second statement to execute
   */
  private Stm s2;

  /**
   * Constructor
   *
   * @param s1 First statement to execute
   * @param s2 Second statement to execute
   */
  public Seq(Stm s1, Stm s2) {
    this.s1 = s1;
    this.s2 = s2;
  }

  /**
   * Big-step semantics
   *
   * @param memory Initial memory 
   */
  public void eval(Memory memory) {
    s1.eval(memory);
    s2.eval(memory);
  }

  /**
   * Small-step semantics
   *
   * @param memory Current memory 
   */
  public Stm reduce(Memory memory) {
    Stm after_s1 = s1.reduce(memory);
    if(after_s1 != null)
      return new Seq(after_s1, s2);
    else
      return s2;
  }

  /**
   * Compilation to stack code
   *
   * @return List of abstract machine (AM) instructions
   */
  public Code compile() {
    return s1.compile().append(s2.compile());
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    pp.print(s1).println(";").print(s2);
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
