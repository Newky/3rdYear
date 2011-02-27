package App;

public abstract class Command {
  /**
   * Execute the command
   */
  public abstract void exec(AppState appState);
}
