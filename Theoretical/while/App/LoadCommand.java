package App;

import java.io.*;
import edu.berkeley.sbp.*;
import While.Stm.*;
import AM.*;

public class LoadCommand extends Command {
  /**
   * File to load
   */
  private String filename;

  /**
   * Constructor
   *
   * @param filename File to load
   */
  public LoadCommand(String filename) {
    this.filename = filename;
  }

  /**
   * Execute
   */
  public void exec(AppState appState) {
    CodeOrWhileParser codeOrWhileParser = new CodeOrWhileParser();

    try {
      String         currentLine;
      StringBuffer   input = new StringBuffer();
	    BufferedReader br    = new BufferedReader(new FileReader(filename));

      while ((currentLine = br.readLine()) != null) 
        input.append(currentLine); 
      
      Object script = codeOrWhileParser.parse(input.toString());
      
      if(script instanceof Stm) {
        appState.addScript((Stm) script);
      }
      else if(script instanceof Code) {
        appState.addScript((Code) script);
      }
      else {
        throw new Error("Unexpected return from codeOrWhileParser");
      }
    } catch (FileNotFoundException e) {
      System.out.println(e);
    } catch (IOException e) {
      System.out.println(e);
    } catch (Ambiguous e) {
      System.out.println(e);
    } catch (ParseFailed e) {
      System.out.println(e);
    }
  }
}
