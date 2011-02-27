package State;

public class Stack {
  /**
   * We use a Java stack internally
   */
  private java.util.Stack<Object> stack;

  /**
   * Constructor
   */
  public Stack() {
    this.stack = new java.util.Stack<Object>();
  }

  /**
   * Push an integer
   *
   * @param n Integer to push
   */
  public void push(int n) {
    stack.push(new Integer(n));
  }

  /**
   * Push a boolean
   *
   * @param b Boolean to push
   */
  public void push(boolean b) {
    stack.push(new Boolean(b));
  }

  /**
   * Pop an integer
   *
   * @return Integer at the top of the stack
   */
  public int popInt() {
    Integer top = (Integer) stack.pop();
    return top;
  }

  /**
   * Pop a boolean
   *
   * @return Boolean at the top of the stack
   */
  public boolean popBool() {
    Boolean top = (Boolean) stack.pop();
    return top;
  }

  /**
   * Unparsing
   */
  public String toString() {
    return stack.toString();
  }
}

