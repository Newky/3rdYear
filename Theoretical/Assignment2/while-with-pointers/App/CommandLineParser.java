package App;

import edu.berkeley.sbp.*;
import java.util.*;

public class CommandLineParser extends GenParser {
  /**
   * Parser for While or AM scripts
   */
  protected CodeOrWhileParser codeOrWhileParser;

  /**
   * Parser for command line arguments
   */
  protected Union cmd;
 
  /**
   * Constructor (initialize the parser)
   */
  public CommandLineParser() {
    codeOrWhileParser = new CodeOrWhileParser();

    cmd = new Union("Cmd");
    cmd.add(seq("--inline", codeOrWhileParser.codeOrWhile));
    cmd.add(seq("--load", anythingBut(atom(' '), false)));
    cmd.add(seq("--show-while"));
    cmd.add(seq("--big-step-while"));
    cmd.add(seq("--small-step-while"));
    cmd.add(seq("--compile"));
    cmd.add(seq("--show-am"));
    cmd.add(seq("--big-step-am"));
    cmd.add(seq("--small-step-am"));
  }

  /**
   * Parse the command line arguments
   */
  public List<Command> parse(String[] argArray) throws java.io.IOException, Ambiguous, ParseFailed {
    // Collect all arguments in one string
    StringBuffer args = new StringBuffer();
    for(int i = 0; i < argArray.length; i++) {
      args.append(i > 0 ? " " : "");
      args.append(argArray[i]);
    }

    // Parse
    Forest f = new edu.berkeley.sbp.chr.CharParser(many(cmd)).parse(args.toString());
    Tree t = f.expand1();

    return semCommands(t);
  }

  /**
   * Semantic value for a list of commands
   */
  protected List<Command> semCommands(Tree t) {
    if(t.head().equals("cons")) {
      List<Command> cmds = semCommands(t.child(0));
      Command       cmd  = semCommand(t.child(1));
      cmds.add(cmd);
      return cmds;
    }
    if(t.head().equals("nil")) {
      return new ArrayList<Command>();
    }
    throw new Error("Unknown non-terminal '" + t.head() + "'");
  }

  /**
   * Semantic value for a command
   */
  protected Command semCommand(Tree t) {
    if(t.head().equals("--inline")) {
      return new ScriptCommand(codeOrWhileParser.semCodeOrWhile(t.child(0)));
    }
    else if(t.head().equals("--load")) {
      return new LoadCommand(semString(t.child(0)));
    }
    else {
      return new SimpleCommand((String)t.head());
    }
  }
}
