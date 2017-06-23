package compiler.visitors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import compiler.symboltable.Helpers;
import compiler.symboltable.Method_t;
import compiler.symboltable.Quad;
import compiler.symboltable.SymbolTable;
import compiler.symboltable.Variable_t;

public class IrVisitor2 {
	private LinkedList<Quad> quads;
	private HashMap<String, LinkedList<Integer>> vars = new HashMap<String, LinkedList<Integer>>();
	public Helpers h2 = new Helpers();
	public Vector<StringBuffer> output = new Vector<StringBuffer>();
	public StringBuffer data = new StringBuffer();
	public SymbolTable st;
	

	HashMap<String, String> myhm = new HashMap<String, String>();
	
	
	public Variable_t getType(String var, Method_t meth) {
		  
        String methodvar = meth.methContains(var);
        Method_t from;
        if(methodvar == null) {
        	from = meth.from;
        	while(from != null) {             // An den brisketai se sunarthsh...
	            String fromvar = from.methContains(var);
	            if(fromvar != null) {            // An den brisketai oute sto from...  
	            	methodvar = fromvar;
	            	break;
	            }
	            from = from.from;
	        }
        }
        return new Variable_t(methodvar, var);

    } 
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s.trim()); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
	

    public IrVisitor2(LinkedList<Quad> quads, SymbolTable symboltable) {
    	this.quads= quads;
    	this.st = symboltable;
	}

	public void create() {
		init();
		for(Quad q : quads) {
			
    		switch (q.a) {
	    		case "unit":
					caseUnit(q);
					break;
					
				case "endu":
					caseEndu(q);
					break;
					
				case "jump":
					caseJump(q);
					//if(!LabelMaps.containsKey(q.d))
						
					break;
					
				case "array":
					caseArray(q);
					break;	
					
				case "par":
					casePar(q);
					break;	
					
				case "call":
					caseCall(q);
					break;
					
				case ":=":
					caseAss(q);
					break;	
					
				case "<" :
				case "<=" :
				case ">" :
				case ">=" :
				case "=" :
					caseCompare(q);
					
					break;	
				
				case "*" :
				case "+" :
				case "-" :
				case "/" :
				case "mod" :
					caseOper(q);
					break;
					
				case "ret":
					caseReturn(q);
					break;	
	
				default:
					break;
			}  
    	}
		for (HashMap.Entry<String,LinkedList<Integer>> entry : vars.entrySet()) {
		    System.err.println(entry.getKey() + ", " + entry.getValue());
		    if(entry.getValue().get(1)==0){
		    	h2.remQuad(entry.getValue().get(0));
		    }
		}
		h2.printQuads();		
	}

	private void init() {
		System.out.println();
		System.out.println("----------------------------------");
		System.out.println();
	}
	
	private void caseOper(Quad q) {	
		if(!isInteger(q.b.trim())){
			if(vars.containsKey(q.b.trim())){
				LinkedList<Integer> ml= new LinkedList<Integer>();
				ml.add(q.num);			
				ml.add(1);
				vars.put(q.b.trim(),ml);
			}
		}
		if(!isInteger(q.c.trim())){
			if(vars.containsKey(q.c.trim())){
				LinkedList<Integer> ml= new LinkedList<Integer>();
				ml.add(q.num);			
				ml.add(1);
				vars.put(q.c.trim(),ml);
			}
		}
		h2.genQuad(q.a, q.b, q.c, q.d);
		//h2.remQuad(q);
		int cursum;
		boolean flag=false;
		if(q.a.equals("+")) {
			if(q.b.contains("$")){
				
			}
			if(isInteger(q.b.trim())) {
				//myhm.put(key, value)	
			}
			if(isInteger(q.c.trim())) {
			
			}

			if(q.b.trim().equals("t")){
				h2.remQuad(q);
				flag=true;
			}
		}
		else if(q.a.equals("-")) {				
			
		}
		else if(q.a.equals("*")) {
									
		}
		else if(q.a.equals("/")) {
				
		}
		else if(q.a.equals("mod")) {			
			
		}

		//h2.genQuad(q.a, q.b, q.c, q.d);
	}
	
	private void caseAss(Quad q) {
		if(!isInteger(q.b.trim())){ //&& vars.get(q.b.trim()).isEmpty()) {
			LinkedList<Integer> ml= new LinkedList<Integer>();
			ml.add(q.num);			
			ml.add(0);
			vars.put(q.b.trim(), ml);
		}
		if(!isInteger(q.c.trim())){
			if(vars.containsKey(q.c.trim())){
				LinkedList<Integer> ml= new LinkedList<Integer>();
				ml.add(q.num);			
				ml.add(1);
				vars.put(q.c.trim(),ml);
			}
		}
		
		
		h2.genQuad(q.a, q.b, q.c, q.d);		
		
	}
	
	private void caseCompare(Quad q) {
		if(!isInteger(q.b.trim())){
			if(vars.containsKey(q.b.trim())){
				LinkedList<Integer> ml= new LinkedList<Integer>();
				ml.add(q.num);			
				ml.add(1);
				vars.put(q.b.trim(),ml);
			}
		}
		if(!isInteger(q.c.trim())){
			if(vars.containsKey(q.c.trim())){
				LinkedList<Integer> ml= new LinkedList<Integer>();
				ml.add(q.num);			
				ml.add(1);
				vars.put(q.c.trim(),ml);
			}
		}
		h2.genQuad(q.a, q.b, q.c, q.d);
	}
	
	private void caseUnit(Quad q) {	
		h2.genQuad(q.a, q.b, q.c, q.d);
	}
	
	private void caseEndu(Quad q) {	
		h2.genQuad(q.a, q.b, q.c, q.d);
		
	}
	
	
	private void caseArray(Quad q) {
		// TODO Auto-generated method stub
		h2.genQuad(q.a, q.b, q.c, q.d);

	}
	
	private void caseJump(Quad q) {
		h2.genQuad(q.a, q.b, q.c, q.d);
	}

	private void casePar(Quad q) {
		h2.genQuad(q.a, q.b, q.c, q.d);
	}
	
	private void caseCall(Quad q) {
		h2.genQuad(q.a, q.b, q.c, q.d);	
		
	}	
	
	private void caseReturn(Quad q) {
		h2.genQuad(q.a, q.b, q.c, q.d);
	}
	
}
