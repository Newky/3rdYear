package App;

import edu.berkeley.sbp.*;

/**
 * Common functionality to many parsers
 */
public class GenParser {
  /**
   * Parser for whitespace (supports C style comments)
   */
  protected Union WS;

  /**
   * Parser for integers
   */
  protected Union integer;

  /**
   * Parser for identifiers
   */
  protected Union identifier;

  /**
   * Parser for any character
   */
  protected Atom anyChar;

  /**
   * Parser for any string (useful only in combination, of course)
   */
  protected Union anything;
  
  /**
   * Like anything, but of length at least 1 
   */
  protected Union anything1;

  /**
   * C-style comment
   */
  protected Union comment;

  /**
   * Parser for a single character
   */
  protected Atom atom(char c) {
    return new edu.berkeley.sbp.chr.CharAtom(c);
  }

  /**
   * Parser for any character in the range
   *
   * @param c1 Start of the range (inclusive)
   * @param c2 End of the range (inclusive)
   */
  protected Atom atom(char c1, char c2) {
    return new edu.berkeley.sbp.chr.CharAtom(c1, c2);
  }
  
  /**
   * Like anything, but exclude an element
   *
   * @param allowEmpty Allow the empty string
   */
  protected Element anythingBut(Element exclude, boolean allowEmpty) {
    Union ex = new Union("", Sequence.create("", new Element[] { anything, exclude, anything }, null));
    return aAndNotB(allowEmpty ? anything : anything1, ex); 
  }

  /**
   * Like kleene-star, but allowing white-space between the elements
   */
  public Union many(Element e) {
    Union many = new Union("many");

    many.add(Sequence.create("nil", new Element[] {}, null));
    many.add(Sequence.create("cons", new Element[] { many, WS, e }, new boolean[] { false, true, false }));

    return many;
  }

  /**
   * Parser for a keyword
   *
   * @param s Keyword to parse
   */
  protected Element kw(String s) {
    Element[] elements = new Element[s.length()];

    for(int i = 0; i < s.length(); i++) {
      elements[i] = atom(s.charAt(i));
    }

    return new Union(s, Sequence.create(s, elements, null)); 
  }

  /**
   * The conjunction of an element and the negation of another
   *
   * @param a The "positive" element
   * @param b The "negative" element
   */
  protected Element aAndNotB(Element a, Element b) {
    Sequence aSeq = Sequence.create(a);
    Sequence bSeq = Sequence.create(b);
    return new Union("aAndNotB", aSeq.andnot(bSeq));
  }
  
  /**
   * Parser for a right-associative binary operator
   *
   * @param left Left operand
   * @param op Operator
   * @param right Right operand
   */
  protected Element rightAssoc(Element left, String op, Element right) {
    Union result = new Union(op);

    Element[] elements = new Element[] { aAndNotB(left, result), WS, kw(op), WS, right };
    boolean[] ignore   = new boolean[] { false, true, true, true, false };

    result.add(Sequence.create(op, elements, ignore));
    return result;
  }

  /**
   * Parser for a left-associative binary operator
   *
   * @param left Left operand
   * @param op Operator
   * @param right Right operand
   */
  protected Element leftAssoc(Element left, String op, Element right) {
    Union result = new Union(op);

    Element[] elements = new Element[] { left, WS, kw(op), WS, aAndNotB(right, result) };
    boolean[] ignore   = new boolean[] { false, true, true, true, false };

    result.add(Sequence.create(op, elements, ignore));
    return result;
  }

  /**
   * Parser for a sequence of keywords and parsers, interspersed with whitespace
   *
   * A concatenation of all the strings is used as the name of the sequence 
   *
   * @param objects Array of objects (which must be String or Element)
   */
  protected Element seq(Object... objects) {
    int numElements = objects.length * 2 - 1;

    Element[] elements = new Element[numElements];
    boolean[] ignore   = new boolean[numElements];
    
    String name = ""; 

    for(int i = 0; i < objects.length; i++) {
      Object obj = objects[i];

      if(i > 0) {
        elements[i * 2 - 1] = WS;
        ignore[i * 2 - 1] = true;
      }
     
      if(obj instanceof String) {
        String str      = (String) obj;
        name           += (String) str;
        ignore[i * 2]   = true;
        elements[i * 2] = kw(str);
      }
      else if(obj instanceof Element) {
        Element e       = (Element) obj;
        ignore[i * 2]   = false;
        elements[i * 2] = e;
      }
      else {
        throw new Error("Invalid object in seq");
      }
    }

    return new Union(name, Sequence.create(name, elements, ignore));
  }

  /**
   * Parser for an element surrounded by whitespace
   *
   * @param e Element 
   */
  protected Union toplevel(Element e) {
    return new Union("", Sequence.create(new Element[] { WS, e, WS }, 1));
  }

  /**
   * Semantic value of an integer
   *
   * @param t Parse tree (returned by the integer parser)
   * @return Value of the integer
   */
  protected int semInteger(Tree t) {
    if(t.size() == 2) {
      int i = semInteger(t.child(0));
      int j = Integer.parseInt((String)t.child(1).head());
      return i * 10 + j;
    } 
    if(t.size() == 1) {
      return Integer.parseInt((String)t.child(0).head());
    } 
    throw new Error("Not a valid parse tree for an integer");
  }
 
  /**
   * Semantic value of a string
   *
   * @param t Parse tree (returned by string or identifier)
   * @return Value of the string
   */
  protected String semString(Tree t) {
    if(t.size() == 2) {
      return t.child(0) + semString(t.child(1));
    }
    if(t.size() == 1) {
      return (String)t.child(0).head();
    }
    throw new Error("Not a valid parse tree for an identifier (" + t.size() + " children)");
  }

  /**
   * Constructor (initializes some parsers)
   */
  public GenParser() {
    // "Anything"
    anyChar = atom('\u0000', '\uFFFF');

    anything = new Union("");
    anything.add(Sequence.create("", new Element[] {}, null));
    anything.add(Sequence.create("", new Element[] { anyChar, anything }, null));

    anything1 = new Union("");
    anything1.add(Sequence.create("", new Element[] { anyChar }, null));
    anything1.add(Sequence.create("", new Element[] { anyChar, anything1 }, null));
  
    // Comments 
    Element endOfComment = new Union("", Sequence.create("", new Element[] { atom('*'), atom('/') }, null));

    comment = new Union("Comment");
    comment.add(Sequence.create("", new Element[] { atom('/'), atom('*'), anythingBut(endOfComment, true), atom('*'), atom('/') }, null));

    // Whitespace
    WS = new Union("WS");
    WS.add(Sequence.create("", new Element[] {}, null));
    WS.add(Sequence.create("", new Element[] { atom(' '), WS }, null));
    WS.add(Sequence.create("", new Element[] { atom('\t'), WS }, null));
    WS.add(Sequence.create("", new Element[] { comment, WS }, null));
    
    // Integers
    integer = new Union("Integer");
    integer.add(Sequence.create("integer", new Element[] { atom('0', '9') }, null));
    integer.add(Sequence.create("integer", new Element[] { integer, atom('0', '9') }, null));
    
    // Identifiers
    Union idChar = new Union("IdChar");
    idChar.add(atom('a', 'z'));
    idChar.add(atom('A', 'Z'));
    idChar.add(atom('\''));

    identifier = new Union("Identifier");
    identifier.add(Sequence.create("identifier", new Element[] { idChar }, null));
    identifier.add(Sequence.create("identifier", new Element[] { idChar, identifier }, null));
  }
}
