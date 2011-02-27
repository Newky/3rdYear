package App;

import While.Stm.*;
import AM.*;

public class SimpleCommand extends Command {
  /**
   * Name of the command
   */
  private String cmd;

  /**
   * Constructor
   *
   * @param name Name of the command
   */
  public SimpleCommand(String cmd) {
    this.cmd = cmd;
  }

  /**
   * Execute
   */
  public void exec(AppState appState) {
    if(cmd.equals("--show-while")) {
      System.out.println(appState.script);
    }
    else if(cmd.equals("--big-step-while")) {
      appState.script.eval(appState.memory);
      System.out.println(appState.memory);
    }
    else if(cmd.equals("--small-step-while")) {
      while(appState.script != null) {
        System.out.println("----------------------->");
        System.out.println("m = " + appState.memory);
        System.out.println(appState.script);

        appState.script = appState.script.reduce(appState.memory);
      }
        
      System.out.println("----------------------->");
      System.out.println(appState.memory);

      appState.script = new Skip();
    }
    else if(cmd.equals("--compile")) {
      appState.code = appState.script.compile();
    }
    else if(cmd.equals("--show-am")) {
      System.out.println(appState.code);
    }
    else if(cmd.equals("--big-step-am")) {
      Code c = appState.code;
      while(!c.isEmpty()) {
        Inst i = c.remove(0);
        c = i.reduce(c, appState.stack, appState.memory);
      }

      System.out.println(appState.memory);
    }
    else if(cmd.equals("--small-step-am")) {
      Code c = appState.code;
      while(!c.isEmpty()) {
        System.out.println("----------------------->");
        System.out.println("e = " + appState.stack);
        System.out.println("m = " + appState.memory);
        System.out.println(c);

        Inst i = c.remove(0);
        c = i.reduce(c, appState.stack, appState.memory);
      }

      System.out.println("----------------------->");
      System.out.println(appState.memory);
    }
    else {
      throw new Error("Unsupported command '" + cmd + "'");
    }
  }
}
