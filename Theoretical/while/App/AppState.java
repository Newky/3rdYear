package App;

import While.Stm.*;
import State.*;
import AM.*;

public class AppState {
  /**
   * Current state of the script
   */
  public Stm script;

  /**
   * Current compiled script
   */
  public Code code;

  /**
   * Current state of the store
   */
  public State state;

  /**
   * Current state of the evaluation stack
   */
  public Stack stack;

  /**
   * Constructor
   */
  public AppState() {
    script = new Skip();
    code   = new Code();
    state  = new State();
    stack  = new Stack();
  }

  /**
   * Add a statement to the while script
   *
   * @param stm Statement to add
   */
  public void addScript(Stm script) {
    if(this.script == null || this.script instanceof Skip) 
      this.script = script;
    else
      this.script = new Seq(this.script, script);
  }

  /**
   * Add instructions the AM script
   *
   * @param code Instructions to add
   */
  public void addScript(Code code) {
    if(code == null || code.size() == 0) 
      this.code = code;
    else
      this.code.addAll(code);
  }
}
