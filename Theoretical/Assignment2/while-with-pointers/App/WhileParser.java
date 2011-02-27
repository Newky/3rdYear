package App;

import edu.berkeley.sbp.*;
import While.Aexp.*;
import While.Bexp.*;
import While.Stm.*;

/**
 * Parser for the While language
 */
public class WhileParser extends GenParser {
  public Union aexp;
  public Union bexp;
  public Union stm;

  /** 
   * The constructor initalizes all the parsers
   */
  public WhileParser() {
    aexp = new Union("Aexp");
    bexp = new Union("Bexp");
    stm  = new Union("Stm");

    aexp.add(seq("(", aexp, ")"));
    aexp.add(seq(aexp, "+", aexp));
    aexp.add(seq(aexp, "*", aexp));
    aexp.add(seq(aexp, "-", aexp));
    aexp.add(seq(aexp, "-", aexp));
    aexp.add(seq("*", aexp));
    aexp.add(seq("&", identifier));
    aexp.add(integer);
    aexp.add(identifier);

    bexp.add(seq("(", bexp, ")"));
    bexp.add(seq(bexp, "&&", bexp));
    bexp.add(seq(bexp, "||", bexp));
    bexp.add(seq("!", bexp));
    bexp.add(seq(aexp, "=", aexp));
    bexp.add(seq(aexp, "<=", aexp));
    bexp.add(seq("true"));
    bexp.add(seq("false"));

    stm.add(seq("{", stm, "}"));
    stm.add(seq(identifier, ":=", aexp));
    stm.add(seq("*", aexp, ":=", aexp));
    stm.add(rightAssoc(stm, ";", stm));
    stm.add(seq("skip"));
    stm.add(seq("if", bexp, "then", stm, "else", stm));
    stm.add(seq("while", bexp, "do", stm));
  }

  /**
   * Attempt to parse a program
   *
   * @param input Program to parse
   * @return AST for the program
   * @throws IOException In case of an I/O error
   * @throws Ambiguous If the program is ambiguous (more than one parse tree)
   * @throws ParseFailed In case of a syntax error
   */
  public Stm parse(String input) throws java.io.IOException, Ambiguous, ParseFailed {
    Forest f = new edu.berkeley.sbp.chr.CharParser(toplevel(stm)).parse(input);
    Tree t = f.expand1();
    Stm a = semStm(t); 
    return a;
  }

  /**
   * Semantic value of a statement
   */
  protected Stm semStm(Tree t) {
    if(t.head().equals(":=")) {
      return new Assign(semString(t.child(0)), semAexp(t.child(1))); 
    } 
    if(t.head().equals("*:=")) {
      return new AssignIndirect(semAexp(t.child(0)), semAexp(t.child(1))); 
    } 
    if(t.head().equals(";")) {
      return new Seq(semStm(t.child(0)), semStm(t.child(1)));
    }
    if(t.head().equals("skip")) {
      return new Skip();
    }
    if(t.head().equals("ifthenelse")) {
      return new If(semBexp(t.child(0)), semStm(t.child(1)), semStm(t.child(2))); 
    } 
    if(t.head().equals("whiledo")) {
      return new While(semBexp(t.child(0)), semStm(t.child(1))); 
    } 
    if(t.head().equals("{}")) {
      return semStm(t.child(0));
    }
    throw new Error("Unknown non-terminal '" + t.head() + "'");
  }

  /**
   * Semantic value of a boolean expression
   */
  protected Bexp semBexp(Tree t) {
    if(t.head().equals("&&")) {
      return new And(semBexp(t.child(0)), semBexp(t.child(1))); 
    } 
    if(t.head().equals("||")) {
      return new Or(semBexp(t.child(0)), semBexp(t.child(1))); 
    } 
    if(t.head().equals("!")) {
      return new Negate(semBexp(t.child(0))); 
    } 
    if(t.head().equals("=")) {
      return new Compare(semAexp(t.child(0)), semAexp(t.child(1))); 
    } 
    if(t.head().equals("<=")) {
      return new LessOrEqual(semAexp(t.child(0)), semAexp(t.child(1))); 
    } 
    if(t.head().equals("true")) {
      return new ConstantBool(true);
    }
    if(t.head().equals("false")) {
      return new ConstantBool(false);
    }
    if(t.head().equals("()")) {
      return semBexp(t.child(0));
    }
    throw new Error("Unknown non-terminal '" + t.head() + "'");
  }

  /**
   * Semantic value for an arithmetic expression
   */
  protected Aexp semAexp(Tree t) {
    if(t.head().equals("+")) {
      return new Add(semAexp(t.child(0)), semAexp(t.child(1))); 
    } 
    if(t.head().equals("*") && t.size() == 2) {
      return new Mult(semAexp(t.child(0)), semAexp(t.child(1))); 
    } 
    if(t.head().equals("*") && t.size() == 1) {
      return new Dereference(semAexp(t.child(0))); 
    } 
    if(t.head().equals("&")) {
      return new AddressOf(semString(t.child(0))); 
    } 
    if(t.head().equals("-")) {
      return new Sub(semAexp(t.child(0)), semAexp(t.child(1))); 
    } 
    if(t.head().equals("integer")) {
      return new ConstantInt(semInteger(t));
    }
    if(t.head().equals("identifier")) {
      return new Variable(semString(t));
    }
    if(t.head().equals("()")) {
      return semAexp(t.child(0));
    }
    throw new Error("Unknown non-terminal '" + t.head() + "'");
  }
}
