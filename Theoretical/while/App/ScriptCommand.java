package App;

import While.Stm.*;
import AM.*;

public class ScriptCommand extends Command {
  /**
   * Script to load
   */
  private Object script;

  /**
   * Constructor
   *
   * @param script Script to load (Stm or Code)
   */
  public ScriptCommand(Object script) {
    this.script = script;
  }

  /**
   * Execute
   */
  public void exec(AppState appState) {
    if(script instanceof Stm) {
      appState.addScript((Stm) script);
    } else if(script instanceof Code) {
      appState.addScript((Code) script);
    } else {
      throw new Error("Unexpected script type");
    }
  }
}
