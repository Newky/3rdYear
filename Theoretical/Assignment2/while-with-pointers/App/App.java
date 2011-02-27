package App;

import edu.berkeley.sbp.*;
import java.util.*;
import While.Stm.*;

/**
 * Main application class
 */
public class App {
  public static void main(String[] args) {
    CommandLineParser cmdParser = new CommandLineParser();
    AppState appState = new AppState();
    List<Command> cmds;

    try  {
      cmds = cmdParser.parse(args);
    } catch (Ambiguous e) {
      System.out.println(e);
      return;
    } catch (ParseFailed e) {
      System.out.println(e);

      System.out.println(
          "You must provide one or (probably) more of the following arguments.\n"
        + "\n"
        + "GENERAL OPTIONS:\n"
        + "  --inline <inline script>\n"
        + "      Add <inline script> to the While or AM script\n"
        + "  --load <filename>\n"
        + "      Add the the script in <filename> to the While or AM script\n"
        + "\n"
        + "OPTIONS FOR \"WHILE\" SCRIPTS:\n"
        + "  --show-while\n"
        + "      Pretty-print the script\n"
        + "  --big-step-while\n"
        + "      Interpret the script and show final state\n"
        + "  --small-step-while\n"
        + "      Reduce the script until completion, showing intermediate states\n"
        + "  --compile\n"
        + "      Compile the script to abstract machine code\n"
        + "\n"
        + "OPTIONS FOR \"AM\" (ABSTRACT MACHINE) SCRIPTS:\n"
        + "  --show-am\n"
        + "      Pretty-print the AM script\n"
        + "  --big-step-am\n"
        + "      Run the script through the abstract machine and show final state\n"
        + "  --small-step-am\n"
        + "      Run the script through the abstract machine, showing intermediate states\n"
        + "\n"
        + "Example\n"
        + "\n"
        + "  --inline n := 5 --load examples/fac.while --show-while --compile --small-step-am\n"
        );
      return;
    } catch (java.io.IOException e) {
      System.out.println(e);
      return;
    }
   
    for(Command cmd : cmds) {
      cmd.exec(appState);
    }
  }
}
