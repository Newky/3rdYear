package App;

import edu.berkeley.sbp.*;
import AM.*;

/**
 * Parser for AM code
 */
public class CodeParser extends GenParser {
  public Union inst;
  public Union code;

  /**
   * Construct the parsers
   */
  public CodeParser() {
    inst = new Union("Inst");
    code = new Union("Code");

    inst.add(seq("PUSH", integer));
    inst.add(seq("ADD"));
    inst.add(seq("MULT"));
    inst.add(seq("SUB"));
    inst.add(seq("TRUE"));
    inst.add(seq("FALSE"));
    inst.add(seq("EQ"));
    inst.add(seq("LE"));
    inst.add(seq("AND"));
    inst.add(seq("NEG"));
    inst.add(seq("FETCH", identifier));
    inst.add(seq("STORE", identifier));
    inst.add(seq("NOOP"));
    inst.add(seq("BRANCH", "(", code, ",", code, ")"));
    inst.add(seq("LOOP", "(", code, ",", code, ")"));

    code.add(seq(inst));
    code.add(seq(code, ":", inst));
  }

  /**
   * Semantic value for a list of AM instructions 
   */
  protected Code semCode(Tree t) {
    if(t.head().equals(":")) {
      Code c = semCode(t.child(0));
      Inst i = semInst(t.child(1));
      c.add(i);
      return c;
    } 
    if(t.head().equals("")) {
      return new Code(semInst(t.child(0)));
    }
    throw new Error("Unknown non-terminal '" + t.head() + "'");
  }

  protected Inst semInst(Tree t) {
    if(t.head().equals("PUSH")) {
      return new Push(semInteger(t.child(0)));
    }
    if(t.head().equals("ADD")) {
      return new Add();
    }
    if(t.head().equals("MULT")) {
      return new Mult();
    }
    if(t.head().equals("SUB")) {
      return new Sub();
    }
    if(t.head().equals("TRUE")) {
      return new True();
    }
    if(t.head().equals("FALSE")) {
      return new False();
    }
    if(t.head().equals("EQ")) {
      return new Eq();
    }
    if(t.head().equals("LE")) {
      return new Le();
    }
    if(t.head().equals("AND")) {
      return new And();
    }
    if(t.head().equals("NEG")) {
      return new Neg();
    }
    if(t.head().equals("FETCH")) {
      return new Fetch(semString(t.child(0)));
    }
    if(t.head().equals("STORE")) {
      return new Store(semString(t.child(0)));
    }
    if(t.head().equals("NOOP")) {
      return new Noop();
    }
    if(t.head().equals("BRANCH(,)")) {
      return new Branch(semCode(t.child(0)), semCode(t.child(1)));
    }
    if(t.head().equals("LOOP(,)")) {
      return new Loop(semCode(t.child(0)), semCode(t.child(1)));
    }
    throw new Error("Unknown non-terminal '" + t.head() + "'");
  }
}
