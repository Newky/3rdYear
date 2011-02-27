package App;

import edu.berkeley.sbp.*;

/**
 * Parser for While *or* AM scripts
 */
public class CodeOrWhileParser extends GenParser {
  /**
   * Parser for while *or* AM scripts
   */
  public Union codeOrWhile;

  /**
   * Parser for While
   */
  protected WhileParser whileParser;

  /**
   * Parser for Code
   */
  protected CodeParser codeParser;

  /**
   * Construct the parser
   */
  public CodeOrWhileParser() {
    whileParser = new WhileParser();
    codeParser  = new CodeParser();

    codeOrWhile = new Union("AM_or_While");
    codeOrWhile.add(Sequence.create("AM", new Element[] { codeParser.code }, null));
    codeOrWhile.add(Sequence.create("While", new Element[] { whileParser.stm }, null));
  }

  /**
   * Attempt to parse a program
   *
   * @param input Program to parse
   * @return AST for the program (either an instance of Stm.Stm or AM.Code)
   * @throws IOException In case of an I/O error
   * @throws Ambiguous If the program is ambiguous (more than one parse tree)
   * @throws ParseFailed In case of a syntax error
   */
  public Object parse(String input) throws java.io.IOException, Ambiguous, ParseFailed {
    Forest f = new edu.berkeley.sbp.chr.CharParser(toplevel(codeOrWhile)).parse(input);
    Tree t = f.expand1();
    return semCodeOrWhile(t);  
  }

  /**
   * Semantic value 
   */
  public Object semCodeOrWhile(Tree t) {
    if(t.head().equals("AM")) {
      return codeParser.semCode(t.child(0));
    } else if(t.head().equals("While")) {
      return whileParser.semStm(t.child(0)); 
    } else {
      throw new Error("Unknown non-terminal '" + t.head() + "'");
    }
  }
}
