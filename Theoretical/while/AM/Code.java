package AM;

import State.*;
import PrettyPrinter.*;

/**
 * "Code" is an abbreviation for a sequence of instructions
 */
public class Code extends java.util.ArrayList<Inst> implements Printable {
  /**
   * Constructor for an empty list
   */
  public Code() {
    super();
  }

  /**
   * Constructor for a list based on another list
   */
  public Code(Code c) {
    super(c);
  }

  /**
   * Constructor for a list with a single instruction
   *
   * @param inst The instruction
   */
  public Code(Inst inst) {
    super();
    add(inst);
  }

  /**
   * New list constructed by appending another list
   *
   * @param code Code to append
   */
  public Code append(Code code) {
    Code result = new Code(this);
    result.addAll(code);
    return result;
  }

  /**
   * New list constructed by appending a single instruction 
   *
   * @param inst Instruction to add
   */
  public Code append(Inst inst) {
    Code result = new Code(this);
    result.add(inst);
    return result;
  }

  /**
   * Pretty-printing
   */
  public void pretty(PrettyPrinter pp) {
    boolean isFirst = true;

    for(Inst i : this) {
      if(!isFirst) {
        pp.println(":");
      }

      pp.print(i);
      isFirst = false;
    }
  }

  /**
   * Unparsing
   */
  public String toString() {
    return PrettyPrinter.render(this);
  }
}
