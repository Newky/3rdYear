package PrettyPrinter;

import java.util.Stack;

public class PrettyPrinter {
  /**
   * Output buffer
   */
  private StringBuffer buffer;

  /**
   * Indentation stack
   *
   * All strings on the indentation stack will be output (from bottom to top)
   * whenever a new line is started.
   */
  private Stack<String> indentStack;

  /**
   * Are we at the start of a new line?
   */
  private boolean atStartOfLine;

  /**
   * Are we in the context of a (boolean or unary) operator?
   */
  private boolean isOperand;

  /**
   * Constructor
   */
  public PrettyPrinter() {
    buffer        = new StringBuffer();
    indentStack   = new Stack<String>();
    atStartOfLine = true;
    isOperand     = false;
  }
  
  /**
   * Print a string
   *
   * @param s String to print
   * @return The pretty-printer
   */
  public PrettyPrinter print(String s) {
    if(atStartOfLine) {
      for(String ind : indentStack) {
        buffer.append(ind);
      }
      atStartOfLine = false;
    }

    buffer.append(s);

    if(s.endsWith("\n")) {
      atStartOfLine = true;
    }

    return this;
  }

  /**
   * Print a string and a linebreak
   *
   * @param s String to print
   * @return The pretty-printer
   */
  public PrettyPrinter println(String s) {
    return print(s + "\n");
  }

  /**
   * Print an integer
   *
   * @param n Integer to print
   * @return The pretty-printer
   */
  public PrettyPrinter print(int n) {
    return print("" + n);
  }

  /**
   * Print any printable node
   *
   * @param p Node to print
   * @return The pretty-printer
   */
  public PrettyPrinter print(Printable p) {
    p.pretty(this);
    return this;
  }

  /**
   * Print a binary operator
   *
   * @param p1 Left operand
   * @param op Operator
   * @param p2 Right operand
   * @return The pretty-printer
   */
  public PrettyPrinter binOp(Printable p1, String op, Printable p2) {
    if(isOperand) {
      print("(").print(p1).print(" " + op + " ").print(p2).print(")");
    } else {
      isOperand = true;
      print(p1).print(" " + op + " ").print(p2);
      isOperand = false;
    }
      
    return this;
  }

  /**
   * Print a unary operator
   *
   * @param op Operator
   * @param p Operand
   * @return The pretty-printer
   */
  public PrettyPrinter unaryOp(String op, Printable p) {
    if(isOperand) {
      print(op).print(p);
    } else {
      isOperand = true;
      print(op).print(p);
      isOperand = false;
    }

    return this;
  }

  /**
   * Increase indentation with the specified string
   *
   * @param indent Identation to add to the stack
   * @return The pretty-printer
   */
  public PrettyPrinter pushIndent(String indent) {
    indentStack.push(indent);
    return println("");
  }

  /**
   * Remove the last added indentation
   *
   * @return The pretty-printer
   */
  public PrettyPrinter popIndent() {
    indentStack.pop();
    return println("");
  }

  /**
   * Return current state of the buffer
   */
  public String toString() {
    return buffer.toString();
  }

  /**
   * Render a node
   */
  public static String render(Printable p) {
    PrettyPrinter pp = new PrettyPrinter();
    p.pretty(pp);
    return pp.toString();
  }
}
