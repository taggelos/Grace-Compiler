package compiler.assembly;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

import compiler.symboltable.Method_t;
import compiler.symboltable.Quad;
import compiler.symboltable.SymbolTable;
import compiler.symboltable.Variable_t;

public class Assembler {
	private LinkedList<Quad> quads; 
	public Vector<StringBuffer> output = new Vector<StringBuffer>();
	public StringBuffer data = new StringBuffer();
	public SymbolTable st;
	public Method_t current;
	int Methcount = 0;
	boolean isMain = true;
	int label=0;
	int current_bp = 4;
	int current_si = 4;
	boolean si_used = false;
	int args = 0;
	boolean isCallee=false;
	//int next_bp=2;
	
	HashMap<String, Integer> hm = new HashMap<String, Integer>();
	HashMap<Integer, String> LabelMaps = new HashMap<Integer, String>();
	Standards standards = new Standards();
	
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
	
	public int getSize(String input) {
		if(input.contains("\""))
			return input.replaceAll("\" ", "").length();	//String case
		else if(input.contains("\'"))						
			return 1;										//Char const case
		return 4;											//Int case
	}
	
	public void varAllocation(StringBuffer stringBuffer, String name) {
		int offset=0;
		for(Variable_t var : st.contains(name+" ").methodVars) {
			offset += 4;
			hm.put(var.getName().trim(), current_bp);
			current_bp += 4;
		}
		
		if(offset != 0)
			stringBuffer.append("\tsub esp, "+offset+"\n");
	}
	
	private void deallocateArgs(StringBuffer stringBuffer, String name) {
		int offset=4;
		for(Variable_t var : st.contains(name).methodParams) 
			offset += 4;

		stringBuffer.append("\tadd esp, "+offset+"\n");
	}
	
	public String nextLabel() {
		label++;
		return "L"+label;
	}
	
	public String getlastLabel() {
		return "L"+label;
	}

    public Assembler(LinkedList<Quad> quads, SymbolTable symboltable) {
    	this.quads= quads;
    	this.st = symboltable;
	}

	public void create() {
		init();
		for(Quad q : quads) {
    		System.out.println(hm);
    		
    		Set<Integer> keys = LabelMaps.keySet();  //get all keys
    		for(Integer i: keys)
    		{
    	        //System.err.println(pair.getKey() + " = " + pair.getValue());
    	        if(i.equals(q.num)) {
    	        	//System.err.println(">>>>>"+pair.getValue());
    	        	output.elementAt(Methcount).append(LabelMaps.get(i)+":\n");
    	        	//it.remove();
    	        	break;
    	        }
    	    }
    		
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
		//System.err.println(standards.code);
		System.err.println(LabelMaps);
		for(StringBuffer sb : output)
			System.out.println(sb.toString());
		
		Set<String> keys = standards.code.keySet();  //get all keys
		for(String i: keys)
		{
		    System.out.println(i+":");
		    System.out.println(standards.code.get(i));
		}
		System.out.println(data.toString());
	}

	private void init() {
		output.addElement(new StringBuffer());
		output.elementAt(Methcount).append(".intel_syntax noprefix\n");
		output.elementAt(Methcount).append(".text\n");
		output.elementAt(Methcount).append("\t.global main\n");
		data.append(".data\n");
	}

	private void caseUnit(Quad q) {	
		//output.append("_"+q.b + "\tproc near\n");
		//output.append("\tpush ebp\n" + "\tmov bp, sp\n" + "\tsub sp,8\n");
		args = 0;
		if(isMain)
			output.elementAt(Methcount).append("main:\n");
		else {
			Methcount++;
			output.addElement(new StringBuffer());
			output.elementAt(Methcount).append("grace_"+q.b+":\n");
		}
		
		isMain = false;
		output.elementAt(Methcount).append("\tpush ebp\n" + "\tmov ebp, esp\n");
		varAllocation(output.elementAt(Methcount), q.b.trim());
		output.elementAt(Methcount).append("\n");
		current = st.contains(q.b);
	}
	
	private void caseEndu(Quad q) {	
		//output.append("_"+q.b + "\tmov sp, bp\n");
		//output.append("\tpop ebp\n" + "\tmov bp, sp\n" + "\tsub sp,8\n");
		output.elementAt(Methcount).append("\n");
		String epilogue = "endof_"+current.getName();
		output.elementAt(Methcount).append(epilogue+":\n");
		if(si_used) {
			output.elementAt(Methcount).append("\tpop esi\n");
			si_used = false;
		}
		
		output.elementAt(Methcount).append("\tmov esp, ebp\n\tpop ebp\n\tret\n");
		Methcount--;
	}

	private void caseAss(Quad q) {
		int off;
		String name = q.b.replaceAll("\\[", "").replaceAll("]", "").trim();
		String val = q.d.replaceAll("\\[", "").replaceAll("]", "").trim();
		if(!hm.containsKey(val)) {
			if(isInteger(val))
				output.elementAt(Methcount).append("\tmov eax, "+val+"\n");
		}
		else {
			off=hm.get(val);
			output.elementAt(Methcount).append("\tmov eax, DWORD PTR [ebp - "+off+"]\n");
		}
		//System.err.println(q.b);
		//if(!hm.containsKey(name)) {
		//	hm.put(name, current_bp);
		//	current_bp += 4;
		//}
		//System.err.println(name+" -- "+hm.get(name));
		off=hm.get(name);
		output.elementAt(Methcount).append("\tmov DWORD PTR [ebp - "+off+"], eax\n");
	}
	
	private void caseArray(Quad q) {
		// TODO Auto-generated method stub
		
		String a, b, c;
		int size;
		
		Variable_t var = getType(q.b+" ", current);
		//System.err.println("--->"+var.getType());
		if(var.getType().contains("int"))
			size = 4;
		else
			size = 1;
		

		a="DWORD PTR [ebp-"+hm.get(q.b).toString()+"]";
		
		c = q.d.replaceAll("\\[", "").replaceAll("]", "").trim();
		//System.err.println(">>> "+c);
		if(!hm.containsKey(c)) {
			hm.put(c.trim(), current_bp);
			current_bp += 4;
		}
			
		if(isInteger(q.c))
			b=q.c;
		else 
			b="DWORD PTR [ebp-"+hm.get(q.c.trim().replaceAll("\\[", "").replaceAll("]", "")).toString()+"]";
			
		output.elementAt(Methcount).append("\tmov eax, "+b+"\n");
		output.elementAt(Methcount).append("\tmov ecx, "+size+"\n");
		output.elementAt(Methcount).append("\timul ecx\n");
		output.elementAt(Methcount).append("\tlea ecx, "+a+"\n");
		output.elementAt(Methcount).append("\tadd eax, ecx\n");
		//System.err.println(hm);
		output.elementAt(Methcount).append("\tmov DWORD PTR [ebp-"+hm.get(c).toString()+"], "+"eax\n");
	}
	
	private void caseJump(Quad q) {
		String lb;
		if(LabelMaps.containsKey(Integer.valueOf(q.d)))
			lb = LabelMaps.get(Integer.valueOf(q.d));
		else {
			lb = nextLabel();
			LabelMaps.put(Integer.valueOf(q.d), lb);
		}
		output.elementAt(Methcount).append("\tjmp "+ lb +"\n");
	}

	private void casePar(Quad q) {
		// TODO Auto-generated method stub
		args++;
		String a, b;
		int offset=4;
		
		//if(!st.contains(q.b).methodParams.isEmpty())
			//output.elementAt(Methcount).append("\tpush esi\n");
			//si_used = true;
		
		if(q.c.equals("V")) {
			if(isInteger(q.b))
				a=q.b;
			else {
				a="DWORD PTR [ebp-"+hm.get(q.b.trim().replaceAll("\\[", "").replaceAll("]", "")).toString()+"]";
				//offset += args*4;
				//a = "DWORD PTR [ebp + "+ offset+"]";
				//output.elementAt(Methcount).append("\tmov esi, "+a+"\n");
				//a = "DWORD PTR [esi - "+ current_si+"]";
				//current_si += 4;
			}
			
			output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
			output.elementAt(Methcount).append("\tpush eax\n");
			//output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
			//output.elementAt(Methcount).append("\tpush eax\n");
			
		}
		else if(q.c.equals("R")) {
			if(isInteger(q.b))
				a=q.b;
			else {
				//a="DWORD PTR [ebp-"+hm.get(q.b).toString()+"]";
				offset += args*4;
				a = "DWORD PTR [ebp + "+ offset+"]";
				//output.elementAt(Methcount).append("\tmov esi, "+a+"\n");
				//a = "DWORD PTR [esi - "+ current_si+"]";
				//current_si += 4;
			}
			
			output.elementAt(Methcount).append("\tlea si, "+a+"\n");
			output.elementAt(Methcount).append("\tpush esi\n");
			//output.elementAt(Methcount).append("\tlea esi, "+a+"\n");
			//output.elementAt(Methcount).append("\tpush esi\n");
		}
		else if(q.b.equals("RET")) {
			
		}
	}
	
	private void caseCall(Quad q) {
		// TODO Auto-generated method stub
		
		output.elementAt(Methcount).append("\tcall grace_"+q.d+"\n");
		deallocateArgs(output.elementAt(Methcount), q.d);
		for(String name : standards.smethodnames) {
			if(name.equals(q.d)) {
				standards.create(name);
				if(name.equals("puti "))
					data.append("\tint_fmt: .asciz  \"%d\"");
				break;
			}
		}
	}

	private void caseCompare(Quad q) {
		String a,b;
		if(isInteger(q.b.trim()))
			a=q.b;
		else
			a="DWORD PTR [ebp-"+hm.get(q.b.trim()).toString()+"]";
		if(isInteger(q.c))
			b=q.c;
		else
			b="DWORD PTR [ebp-"+hm.get(q.b.trim()).toString()+"]";
		
		output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
		output.elementAt(Methcount).append("\tmov edx, "+b+"\n");
		output.elementAt(Methcount).append("\tcmp eax, edx\n");
		
		String lb;
		if(LabelMaps.containsKey(Integer.valueOf(q.d)))
			lb = LabelMaps.get(Integer.valueOf(q.d));
		else {
			lb = nextLabel();
			LabelMaps.put(Integer.valueOf(q.d), lb);
		}
		
		if(q.a.equals(">"))
			output.elementAt(Methcount).append("\tjg "+ lb +"\n");
		else if(q.a.equals("<"))
			output.elementAt(Methcount).append("\tjl "+ lb +"\n");
		else if(q.a.equals(">="))
			output.elementAt(Methcount).append("\tjge "+ lb +"\n");
		else if(q.a.equals("<="))
			output.elementAt(Methcount).append("\tjle "+ lb +"\n");
		else if(q.a.equals("="))
			output.elementAt(Methcount).append("\tjz "+ lb +"\n");
		else if(q.a.equals("#")) 
			output.elementAt(Methcount).append("\tjnz "+ lb +"\n");
	}
	
	private void caseOper(Quad q) {
		String a,b;
		if(isInteger(q.b.trim()))
			a=q.b;
		else
			a="DWORD PTR [ebp-"+hm.get(q.b.trim()).toString()+"]";
		if(isInteger(q.c))
			b=q.c;
		else
			b="DWORD PTR [ebp-"+hm.get(q.b.trim()).toString()+"]";
		
		if(!hm.containsKey(q.d)) {
			hm.put(q.d.trim(), current_bp);
			current_bp += 4;
		}
		
		if(q.a.equals("+")) {
				
			output.elementAt(Methcount).append("\tmov ebx, "+a+"\n");
			output.elementAt(Methcount).append("\tmov ecx, "+b+"\n");
			output.elementAt(Methcount).append("\tmov eax, ebx\n");
			output.elementAt(Methcount).append("\tadd eax, ecx\n");	
			
		}
		else if(q.a.equals("-")) {
			
			output.elementAt(Methcount).append("\tmov ebx, "+a+"\n");
			output.elementAt(Methcount).append("\tmov ecx, "+b+"\n");
			output.elementAt(Methcount).append("\tmov eax, ebx\n");
			output.elementAt(Methcount).append("\tsub eax, ecx\n");
			
		}
		else if(q.a.equals("*")) {
							
			output.elementAt(Methcount).append("\tmov ebx, "+a+"\n");
			output.elementAt(Methcount).append("\tmov ecx, "+b+"\n");
			output.elementAt(Methcount).append("\tmov eax, ebx\n");
			output.elementAt(Methcount).append("\timul eax, ecx\n");			
		}
		else if(q.a.equals("/")) {
			
			output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
			output.elementAt(Methcount).append("\tcdq\n");
			output.elementAt(Methcount).append("\tmov ebx, "+b+"\n");
			output.elementAt(Methcount).append("\tidiv ebx, ecx\n");	// eax -> div, edx -> mod		
		}
		else if(q.a.equals("mod")) {
			
			output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
			output.elementAt(Methcount).append("\tcdq\n");
			output.elementAt(Methcount).append("\tmov ebx, "+b+"\n");
			output.elementAt(Methcount).append("\tidiv ebx, ecx\n");	// eax -> div, edx -> mod
			output.elementAt(Methcount).append("\tmov eax, edx\n");
		}
		output.elementAt(Methcount).append("\tmov DWORD PTR [ebp-"+hm.get(q.d)+"], eax\n");
		
	}
	
	private void caseReturn(Quad q) {
		String epilogue = "endof_"+current.getName();
		output.elementAt(Methcount).append("\tjmp "+ epilogue +"\n");
	}
	
}
