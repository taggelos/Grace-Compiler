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
	
	//nop kai = 
	public boolean revertConds(){ //reverts condition and deletes jump
		boolean myret=false; //if sth changed boolean
		for (int i = 0; i < quads.size()-1; i ++){		
			if (quads.get(i+1).a.equals("jump")){
				switch (quads.get(i).a) {					
				case "<":
					h2.setQuad(quads.get(i), new Quad(quads.get(i).num,">=",quads.get(i).b,quads.get(i).c,quads.get(i+1).d));
					h2.remQuad(quads.get(i+1));
					myret=true;
					break;	
				case ">":
					h2.setQuad(quads.get(i), new Quad(quads.get(i).num,"<=",quads.get(i).b,quads.get(i).c,quads.get(i+1).d));
					h2.remQuad(quads.get(i+1));
					myret=true;
					break;
				case "<=":
					h2.setQuad(quads.get(i), new Quad(quads.get(i).num,">",quads.get(i).b,quads.get(i).c,quads.get(i+1).d));
					h2.remQuad(quads.get(i+1));
					myret=true;
					break;
				case ">=":
					h2.setQuad(quads.get(i), new Quad(quads.get(i).num,"<",quads.get(i).b,quads.get(i).c,quads.get(i+1).d));
					h2.remQuad(quads.get(i+1));
					myret=true;
					break;
				default:
					break;
				}					
			}
		}
		return myret;
	}	
	
	public boolean checkSameAss(String s){ //check if there is same assignment again
		int count=0;
		for (int i = 0; i < quads.size(); i ++){
			if(quads.get(i).a.equals(":=") && quads.get(i).b.equals(s))
				count++;
		}
		//System.err.println(count +s );
		if (count > 1) return true; 
		return false;
	}
	
	public boolean constantAndCopyPropagation(boolean simpleVersion){ //tested perfectly + meta extra oi prakseis+copyprop + oxi g while kalo
		boolean myret=false,simpleV=false; //if sth changed boolean
		for (int i = 0; i < quads.size(); i ++){
			if(quads.get(i).a.equals(":=") && !isInteger(quads.get(i).b)){ //&& isInteger(quads.get(i).d) //(for the other propagation only)
				if(simpleVersion && checkSameAss(quads.get(i).b)) simpleV=true;
				if (!simpleV ){ 
					for (int j = i+1; j < quads.size(); j ++){
							if(quads.get(j).a.equals(":="))
								if (quads.get(i).b.equals(quads.get(j).b) ){ 
									break;
								}
							switch (quads.get(j).a) {
							case "<":				
							case ">":
							case "<=":
							case ">=":
							case "=":
								if(quads.get(j).b.equals(quads.get(i).b) ){
									h2.setQuad(quads.get(j), new Quad(quads.get(j).num,quads.get(j).a,quads.get(i).d,quads.get(j).c,quads.get(j).d));
									myret=true;
								}
								if (quads.get(j).c.equals(quads.get(i).b)){
									h2.setQuad(quads.get(j), new Quad(quads.get(j).num,quads.get(j).a,quads.get(j).b,quads.get(i).d,quads.get(j).d));
									myret=true;
								}
								break;
							case ":=":
								if(quads.get(j).d.equals(quads.get(i).b) ){
									h2.setQuad(quads.get(j), new Quad(quads.get(j).num,quads.get(j).a,quads.get(j).b,quads.get(j).c,quads.get(i).d));
									myret=true;
								}
								break;
							case "*" :
							case "+" :
							case "-" :
							case "/" :
							case "mod" :
								if(quads.get(j).b.equals(quads.get(i).b) ){
									h2.setQuad(quads.get(j), new Quad(quads.get(j).num,quads.get(j).a,quads.get(i).d,quads.get(j).c,quads.get(j).d));
									myret=true;
								}
								if(quads.get(j).c.equals(quads.get(i).b) ){
									h2.setQuad(quads.get(j), new Quad(quads.get(j).num,quads.get(j).a,quads.get(j).b,quads.get(i).d,quads.get(j).d));
									myret=true;
								}
								break;
							default:
								break;						
							}
						
					}
				}
				simpleV=false;
			}
			
		}
		return myret;
	}	
	
	
	public void checkIfUsed(String qa){
		switch (qa) {
		case "<":				
		case ">":
		case "<=":
		case ">=":
		case "=":
			for (int i = 0; i < quads.size(); i ++){
				
			}
			break;
		default:
			break;
		}
		
	}
	
	/*public boolean copyPropagation(){
		boolean myret=false;
		for (int i = 0; i < quads.size(); i ++){
			if(quads.get(i).a.equals(":=")){
				if (!isInteger(quads.get(i).b) && !isInteger(quads.get(i).d)){ 
					for (int j = i+1; j < quads.size(); j ++){
							if(quads.get(j).a.equals(":="))
								if (quads.get(i).b.equals(quads.get(j).b) ){ 
									break;
								}
							switch (quads.get(j).a) {
							case "<":				
							case ">":
							case "<=":
							case ">=":
							case "=":
								if(quads.get(j).b.equals(quads.get(i).b) ){

									System.err.println("asdas " + quads.get(j).b  +"  "+quads.get(i).b);
									h2.setQuad(quads.get(j), new Quad(quads.get(j).num,quads.get(j).a,quads.get(i).d,quads.get(j).c,quads.get(j).d));

									System.err.println("asdas " + quads.get(j).b  +"  "+quads.get(i).b);
									myret=true;
								}
								if (quads.get(j).c.equals(quads.get(i).b)){
									h2.setQuad(quads.get(j), new Quad(quads.get(j).num,quads.get(j).a,quads.get(j).b,quads.get(i).d,quads.get(j).d));
									myret=true;
								}
								break;
							default:
								break;							
						
							}
				}
				
			}
		}
		}
		return myret;
	}*/
		
	public boolean constantFolding(){ //kind of??
		boolean myret=false;
		for (int i = 0; i < quads.size(); i ++){
			if(isInteger(quads.get(i).b)==true && isInteger(quads.get(i).c)==true)
				switch (quads.get(i).a) {
					case "*" :
						int mult = Integer.parseInt(quads.get(i).b.trim()) * Integer.parseInt(quads.get(i).c.trim());//needs float too
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",Integer.toString(mult)));
						myret=true;
						break;
					case "+" :
						int plus = Integer.parseInt(quads.get(i).b.trim()) + Integer.parseInt(quads.get(i).c.trim());//needs float too
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",Integer.toString(plus)));
						myret=true;
						break;
					case "-" :
						int minus = Integer.parseInt(quads.get(i).b.trim()) - Integer.parseInt(quads.get(i).c.trim());//needs float too
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",Integer.toString(minus)));
						myret=true;
						break;
					case "/" :
						System.err.println(quads.get(i).b);
						int div = Integer.parseInt(quads.get(i).b.trim()) / Integer.parseInt(quads.get(i).c.trim());//needs float too
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",Integer.toString(div)));
						myret=true;
						break;
					case "mod" : 
						int mod = Integer.parseInt(quads.get(i).b.trim()) % Integer.parseInt(quads.get(i).c.trim());//needs float too
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",Integer.toString(mod)));
						myret=true;
						break;
					default:
						break;						
				}
		}
		return myret;
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
	
	public void iterateOverJumps(){//while problem?
		int next=0;
		for(Quad q : quads) {
			if (q.a.equals("jump")){
				next=Integer.parseInt(q.d);
			}
			else{
				next++;
			}
			if (q.num+1==next){
				//do sth
			}			
		}
	}
	
	//idio provlhma kai remove unused variables
	public void remJumpsOfNextLine(){ //TODO needs checks g kathe jump mhn xrhsimopoieitai apo allh prin svhstei
		for (int i = 0; i < quads.size()-1; i ++){
			if (quads.get(i+1).equals("jump"))
				switch (quads.get(i).a) {					
				case "<":				
				case ">":
				case "<=":
				case ">=":
				case "=":
					if(quads.get(i).d.trim().equals(quads.get(i+1).d))
					break;
				default:
					break;
				}
		}
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
		/*
		for (HashMap.Entry<String,LinkedList<Integer>> entry : vars.entrySet()) {
		    System.err.println(entry.getKey() + ", " + entry.getValue());
		    if(entry.getValue().get(1)==0){
		    	h2.remQuad(entry.getValue().get(0));
		    }
		}*/
		this.quads=h2.quads;
		//constantFolding(); //???
		//constantAndCopyPropagation();
		//constantAndCopyPropagation(true); //true for simple
		revertConds();
		//constantFolding();
		while(constantAndCopyPropagation(true) || constantFolding()){
		System.err.println("as");
		};
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
		
		int cursum;
		boolean flag=false;
		if(q.a.equals("+")) {
			if(q.b.contains("$")){
				
			}
			if(isInteger(q.b.trim())) {
				
			}
			if(isInteger(q.c.trim())) {
			
			}

			if(q.b.trim().equals("t")){
				//h2.remQuad(q);
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

		h2.genQuad(q.a, q.b, q.c, q.d);
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
