
//----------------------------------------------------
// The following code was generated by CUP v0.11b 20141202 (SVN rev 60)
//----------------------------------------------------

import java_cup.runtime.*;
import java_cup.runtime.XMLElement;

/** CUP v0.11b 20141202 (SVN rev 60) generated parser.
  */
@SuppressWarnings({"rawtypes"})
public class Parser extends java_cup.runtime.lr_parser {

 public final Class getSymbolContainer() {
    return sym.class;
}

  /** Default constructor. */
  public Parser() {super();}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\033\000\002\002\003\000\002\002\004\000\002\003" +
    "\004\000\002\003\002\000\002\005\003\000\002\005\007" +
    "\000\002\005\011\000\002\005\003\000\002\004\005\000" +
    "\002\004\003\000\002\004\002\000\002\006\006\000\002" +
    "\010\006\000\002\007\005\000\002\007\005\000\002\007" +
    "\005\000\002\007\005\000\002\011\005\000\002\011\005" +
    "\000\002\011\005\000\002\011\005\000\002\011\005\000" +
    "\002\011\004\000\002\011\003\000\002\011\003\000\002" +
    "\011\003\000\002\011\005" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\065\000\016\002\ufffe\005\ufffe\007\ufffe\017\ufffe\024" +
    "\ufffe\025\ufffe\001\002\000\016\002\001\005\013\007\017" +
    "\017\007\024\016\025\011\001\002\000\004\002\006\001" +
    "\002\000\004\002\000\001\002\000\012\007\017\017\007" +
    "\024\016\025\011\001\002\000\022\002\ufffa\005\ufffa\006" +
    "\ufffa\007\ufffa\012\ufffa\017\ufffa\024\ufffa\025\ufffa\001\002" +
    "\000\044\002\uffea\004\uffea\005\uffea\006\uffea\007\uffea\010" +
    "\uffea\012\uffea\013\uffea\014\uffea\015\uffea\016\uffea\017\uffea" +
    "\020\uffea\021\uffea\022\uffea\024\uffea\025\uffea\001\002\000" +
    "\034\002\uffe9\005\uffe9\006\uffe9\007\uffe9\011\064\012\uffe9" +
    "\016\uffe9\017\uffe9\020\uffe9\021\uffe9\022\uffe9\024\uffe9\025" +
    "\uffe9\001\002\000\004\007\043\001\002\000\032\002\ufffd" +
    "\005\ufffd\006\ufffd\007\ufffd\012\ufffd\016\024\017\025\020" +
    "\027\021\026\022\022\024\ufffd\025\ufffd\001\002\000\020" +
    "\002\uffff\005\uffff\007\uffff\012\uffff\017\uffff\024\uffff\025" +
    "\uffff\001\002\000\044\002\uffe8\004\uffe8\005\uffe8\006\uffe8" +
    "\007\035\010\uffe8\012\uffe8\013\uffe8\014\uffe8\015\uffe8\016" +
    "\uffe8\017\uffe8\020\uffe8\021\uffe8\022\uffe8\024\uffe8\025\uffe8" +
    "\001\002\000\012\007\017\017\007\024\016\025\011\001" +
    "\002\000\044\002\uffe9\004\uffe9\005\uffe9\006\uffe9\007\uffe9" +
    "\010\uffe9\012\uffe9\013\uffe9\014\uffe9\015\uffe9\016\uffe9\017" +
    "\uffe9\020\uffe9\021\uffe9\022\uffe9\024\uffe9\025\uffe9\001\002" +
    "\000\016\010\023\016\024\017\025\020\027\021\026\022" +
    "\022\001\002\000\012\007\017\017\007\024\016\025\011" +
    "\001\002\000\044\002\uffe7\004\uffe7\005\uffe7\006\uffe7\007" +
    "\uffe7\010\uffe7\012\uffe7\013\uffe7\014\uffe7\015\uffe7\016\uffe7" +
    "\017\uffe7\020\uffe7\021\uffe7\022\uffe7\024\uffe7\025\uffe7\001" +
    "\002\000\012\007\017\017\007\024\016\025\011\001\002" +
    "\000\012\007\017\017\007\024\016\025\011\001\002\000" +
    "\012\007\017\017\007\024\016\025\011\001\002\000\012" +
    "\007\017\017\007\024\016\025\011\001\002\000\044\002" +
    "\uffee\004\uffee\005\uffee\006\uffee\007\uffee\010\uffee\012\uffee" +
    "\013\uffee\014\uffee\015\uffee\016\uffee\017\uffee\020\uffee\021" +
    "\uffee\022\uffee\024\uffee\025\uffee\001\002\000\044\002\uffed" +
    "\004\uffed\005\uffed\006\uffed\007\uffed\010\uffed\012\uffed\013" +
    "\uffed\014\uffed\015\uffed\016\uffed\017\uffed\020\uffed\021\uffed" +
    "\022\uffed\024\uffed\025\uffed\001\002\000\044\002\uffef\004" +
    "\uffef\005\uffef\006\uffef\007\uffef\010\uffef\012\uffef\013\uffef" +
    "\014\uffef\015\uffef\016\uffef\017\uffef\020\027\021\026\022" +
    "\022\024\uffef\025\uffef\001\002\000\044\002\ufff0\004\ufff0" +
    "\005\ufff0\006\ufff0\007\ufff0\010\ufff0\012\ufff0\013\ufff0\014" +
    "\ufff0\015\ufff0\016\ufff0\017\ufff0\020\027\021\026\022\022" +
    "\024\ufff0\025\ufff0\001\002\000\044\002\uffec\004\uffec\005" +
    "\uffec\006\uffec\007\uffec\010\uffec\012\uffec\013\uffec\014\uffec" +
    "\015\uffec\016\uffec\017\uffec\020\uffec\021\uffec\022\uffec\024" +
    "\uffec\025\uffec\001\002\000\014\007\017\010\ufff7\017\007" +
    "\024\016\025\011\001\002\000\004\010\042\001\002\000" +
    "\020\004\040\010\ufff8\016\024\017\025\020\027\021\026" +
    "\022\022\001\002\000\014\007\017\010\ufff7\017\007\024" +
    "\016\025\011\001\002\000\004\010\ufff9\001\002\000\046" +
    "\002\ufff6\004\ufff6\005\ufff6\006\ufff6\007\ufff6\010\ufff6\011" +
    "\ufff6\012\ufff6\013\ufff6\014\ufff6\015\ufff6\016\ufff6\017\ufff6" +
    "\020\ufff6\021\ufff6\022\ufff6\024\ufff6\025\ufff6\001\002\000" +
    "\012\007\044\017\007\024\016\025\011\001\002\000\012" +
    "\007\044\017\007\024\016\025\011\001\002\000\022\013" +
    "\053\014\055\015\054\016\024\017\025\020\027\021\026" +
    "\022\022\001\002\000\004\010\047\001\002\000\014\005" +
    "\013\007\017\017\007\024\016\025\011\001\002\000\022" +
    "\002\ufffc\005\ufffc\006\051\007\ufffc\012\ufffc\017\ufffc\024" +
    "\ufffc\025\ufffc\001\002\000\014\005\013\007\017\017\007" +
    "\024\016\025\011\001\002\000\022\002\ufffb\005\ufffb\006" +
    "\ufffb\007\ufffb\012\ufffb\017\ufffb\024\ufffb\025\ufffb\001\002" +
    "\000\012\007\017\017\007\024\016\025\011\001\002\000" +
    "\012\007\017\017\007\024\016\025\011\001\002\000\012" +
    "\007\017\017\007\024\016\025\011\001\002\000\016\010" +
    "\ufff2\016\024\017\025\020\027\021\026\022\022\001\002" +
    "\000\016\010\ufff1\016\024\017\025\020\027\021\026\022" +
    "\022\001\002\000\016\010\ufff3\016\024\017\025\020\027" +
    "\021\026\022\022\001\002\000\024\010\023\013\053\014" +
    "\055\015\054\016\024\017\025\020\027\021\026\022\022" +
    "\001\002\000\004\010\063\001\002\000\004\010\ufff4\001" +
    "\002\000\016\005\ufffe\007\ufffe\012\ufffe\017\ufffe\024\ufffe" +
    "\025\ufffe\001\002\000\016\005\013\007\017\012\066\017" +
    "\007\024\016\025\011\001\002\000\022\002\ufff5\005\ufff5" +
    "\006\ufff5\007\ufff5\012\ufff5\017\ufff5\024\ufff5\025\ufff5\001" +
    "\002\000\044\002\uffeb\004\uffeb\005\uffeb\006\uffeb\007\uffeb" +
    "\010\uffeb\012\uffeb\013\uffeb\014\uffeb\015\uffeb\016\uffeb\017" +
    "\uffeb\020\uffeb\021\uffeb\022\uffeb\024\uffeb\025\uffeb\001\002" +
    "" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\065\000\006\002\004\003\003\001\001\000\012\005" +
    "\014\006\011\010\007\011\013\001\001\000\002\001\001" +
    "\000\002\001\001\000\006\006\017\011\066\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\006\006\017\011\020\001\001\000\002\001\001" +
    "\000\002\001\001\000\006\006\017\011\033\001\001\000" +
    "\002\001\001\000\006\006\017\011\032\001\001\000\006" +
    "\006\017\011\031\001\001\000\006\006\017\011\030\001" +
    "\001\000\006\006\017\011\027\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\010\004\035\006\017\011\036\001\001" +
    "\000\002\001\001\000\002\001\001\000\010\004\040\006" +
    "\017\011\036\001\001\000\002\001\001\000\002\001\001" +
    "\000\010\006\017\007\045\011\044\001\001\000\010\006" +
    "\017\007\061\011\060\001\001\000\002\001\001\000\002" +
    "\001\001\000\012\005\047\006\011\010\007\011\013\001" +
    "\001\000\002\001\001\000\012\005\051\006\011\010\007" +
    "\011\013\001\001\000\002\001\001\000\006\006\017\011" +
    "\057\001\001\000\006\006\017\011\056\001\001\000\006" +
    "\006\017\011\055\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\004\003\064\001\001\000\012\005" +
    "\014\006\011\010\007\011\013\001\001\000\002\001\001" +
    "\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$Parser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$Parser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$Parser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


  /** Scan to get the next Symbol. */
  public java_cup.runtime.Symbol scan()
    throws java.lang.Exception
    {
 return s.next_token(); 
    }


    // Connect this parser to a scanner!
    Scanner s;
    Parser(Scanner s){ this.s = s; }


/** Cup generated class to encapsulate user supplied action code.*/
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
class CUP$Parser$actions {
  private final Parser parser;

  /** Constructor */
  CUP$Parser$actions(Parser parser) {
    this.parser = parser;
  }

  /** Method 0 with the actual generated action code for actions 0 to 300. */
  public final java_cup.runtime.Symbol CUP$Parser$do_action_part00000000(
    int                        CUP$Parser$act_num,
    java_cup.runtime.lr_parser CUP$Parser$parser,
    java.util.Stack            CUP$Parser$stack,
    int                        CUP$Parser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$Parser$result;

      /* select the action based on the action number */
      switch (CUP$Parser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // program ::= statement_list 
            {
              String RESULT =null;
		int slleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int slright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String sl = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 System.out.println("The S-Expression output:\n" + sl); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("program",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= program EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		String start_val = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		RESULT = start_val;
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$Parser$parser.done_parsing();
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // statement_list ::= statement_list statement 
            {
              String RESULT =null;
		int slleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int slright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		String sl = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = (sl != null)? new String(sl + "\n" + s) : s; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("statement_list",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // statement_list ::= 
            {
              String RESULT =null;
		 /* do nothing */ 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("statement_list",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // statement ::= expression 
            {
              String RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = e; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("statement",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // statement ::= IF LPAREN condition RPAREN statement 
            {
              String RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = (s != null)? new String("\n\t(if (" + c + ") " + s + ")\n\t") : new String("\n\t(if (" + c + "))\n"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("statement",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-4)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // statement ::= IF LPAREN condition RPAREN statement ELSE statement 
            {
              String RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-4)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-4)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-4)).value;
		int s1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int s1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String s1 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int s2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int s2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String s2 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 String t1 = (s1 == null)?"":s1; String t2 = (s2 == null)?"":s2; 
                                                                                       RESULT = (t2.matches("(\r\n|[\r\n]|\r\n\t|[\r\n\t])*.*if(.*(\r\n|[\r\n]|\r\n\t|[\r\n\t])*)*")) ? 
                                                                                       new String("\n\t(if (" + c + ") " + t1 + "\t\t" + t2 + ")") : new String("\n\t(if (" + c + ") " + t1 + "\n\t\t" + t2 + ")"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("statement",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-6)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // statement ::= func_declaration 
            {
              String RESULT =null;
		int fdleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fdright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String fd = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = fd; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("statement",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // identifier_list ::= expression COMMA identifier_list 
            {
              String RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int illeft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int ilright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String il = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = (il != null)? new String(e + " " + il) : e; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("identifier_list",2, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // identifier_list ::= expression 
            {
              String RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = e; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("identifier_list",2, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // identifier_list ::= 
            {
              String RESULT =null;
		 /* do nothing */ 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("identifier_list",2, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // function ::= IDENTIFIER LPAREN identifier_list RPAREN 
            {
              String RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int illeft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int ilright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		String il = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = (il != null)? new String("(" + i + " " + il + ")") : new String("(" + i + ")"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("function",4, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // func_declaration ::= function LBRAC statement_list RBRAC 
            {
              String RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int slleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int slright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		String sl = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = (sl != null)? new String("(define " + f + " " + sl + ")") : new String("(define " + f + ")"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("func_declaration",6, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // condition ::= LPAREN condition RPAREN 
            {
              String RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new String("(" + c + ")"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("condition",5, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // condition ::= expression EQUAL expression 
            {
              String RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String e1 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e2 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new String("= " + e1 + " " + e2); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("condition",5, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // condition ::= expression GREATERTHAN expression 
            {
              String RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String e1 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e2 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new String("> " + e1 + " " + e2); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("condition",5, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // condition ::= expression LESSTHAN expression 
            {
              String RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String e1 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e2 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new String("< " + e1 + " " + e2); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("condition",5, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // expression ::= expression PLUS expression 
            {
              String RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String e1 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e2 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new String("(+ " + e1 + " " + e2 + ")"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expression",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // expression ::= expression MINUS expression 
            {
              String RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String e1 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e2 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new String("(- " + e1 + " " + e2 + ")"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expression",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // expression ::= expression TIMES expression 
            {
              String RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String e1 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e2 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new String("(* " + e1 + " " + e2 + ")"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expression",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 20: // expression ::= expression DIV expression 
            {
              String RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String e1 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e2 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new String("(/ " + e1 + " " + e2 + ")"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expression",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 21: // expression ::= expression MOD expression 
            {
              String RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String e1 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e2 = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new String("(modulo " + e1 + " " + e2 + ")"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expression",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 22: // expression ::= MINUS expression 
            {
              String RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new String("(-" + e + ")"); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expression",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 23: // expression ::= NUMBER 
            {
              String RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Integer n = (Integer)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new String(Integer.toString(n)); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expression",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 24: // expression ::= function 
            {
              String RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = f; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expression",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 25: // expression ::= IDENTIFIER 
            {
              String RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = i; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expression",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 26: // expression ::= LPAREN expression RPAREN 
            {
              String RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = e; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expression",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number "+CUP$Parser$act_num+"found in internal parse table");

        }
    } /* end of method */

  /** Method splitting the generated action code into several parts. */
  public final java_cup.runtime.Symbol CUP$Parser$do_action(
    int                        CUP$Parser$act_num,
    java_cup.runtime.lr_parser CUP$Parser$parser,
    java.util.Stack            CUP$Parser$stack,
    int                        CUP$Parser$top)
    throws java.lang.Exception
    {
              return CUP$Parser$do_action_part00000000(
                               CUP$Parser$act_num,
                               CUP$Parser$parser,
                               CUP$Parser$stack,
                               CUP$Parser$top);
    }
}

}
