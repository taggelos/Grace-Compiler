package compiler.assembly;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import compiler.symboltable.Quad;

public class Assembler {
	private LinkedList<Quad> quads; 
	public Vector<StringBuffer> output = new Vector<StringBuffer>();
	public StringBuffer data = new StringBuffer();
	int Methcount = 0;
	boolean isMain = true;
	int label=0;
	int current_bp=2;
	//int next_bp=2;
	
	HashMap<String, Integer> hm = new HashMap<String, Integer>();
	
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
	
	public String nextLabel() {
		label++;
		return "L"+label;
	}
	
	public String getlastLabel() {
		return "L"+label;
	}

    public Assembler(LinkedList<Quad> quads) {
    	this.quads= quads;
	}

	public void create() {
		init();
		for(Quad q : quads) {
    		System.out.println(hm);
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
	
				default:
					break;
			}    	
    	}	
		//output.elementAt(Methcount).append(data);
		//System.out.println("LEN: "+output.);
		for(StringBuffer sb : output)
			System.out.println(sb.toString());
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
		
		
		
		if(isMain)
			output.elementAt(Methcount).append("main:\n");
		else {
			Methcount++;
			output.addElement(new StringBuffer());
			output.elementAt(Methcount).append(q.b+":\n");
		}
		
		isMain = false;
		output.elementAt(Methcount).append("\tpush ebp\n" + "\tmov ebp, esp\n");
	}
	
	private void caseEndu(Quad q) {	
		//output.append("_"+q.b + "\tmov sp, bp\n");
		//output.append("\tpop ebp\n" + "\tmov bp, sp\n" + "\tsub sp,8\n");
		output.elementAt(Methcount).append("\tmove esp, ebp\n\tpop ebp\n\tret\n");
		Methcount--;
	}
	

	private void caseAss(Quad q) {
		int off;
		if(!hm.containsKey(q.d)) {
			if(isInteger(q.d))
				output.elementAt(Methcount).append("\tmov ax, "+q.d+"\n");
		}
		else {
			off=hm.get(q.d);
			output.elementAt(Methcount).append("\tmov ax, word ptr [ebp - "+off+"]\n");
		}
		System.err.println(q.b);
		if(!hm.containsKey(q.b)) {
			hm.put(q.b, current_bp);
			current_bp += 2;
		}
		
		off=hm.get(q.b);
		output.elementAt(Methcount).append("\tmov word ptr [ebp - "+off+"], eax\n");
	}
	
	private void caseJump(Quad q) {
		// TODO Auto-generated method stub
		output.elementAt(Methcount).append("\tjmp "+ nextLabel() +"\n");
		
	}

	private void casePar(Quad q) {
		// TODO Auto-generated method stub
		
	}
	
	private void caseCall(Quad q) {
		// TODO Auto-generated method stub
		
	}

	private void caseCompare(Quad q) {
		// TODO Auto-generated method stub
		
	}
	
	private void caseOper(Quad q) {
		if(q.a.equals("+")) {
			String a,b;
			if(isInteger(q.b))
				a=q.b;
			else
				a=hm.get(q.b).toString();
			if(isInteger(q.c))
				b=q.c;
			else
				b=hm.get(q.c).toString();
				
			output.elementAt(Methcount).append("\tmove ebx, "+a+"\n");
			output.elementAt(Methcount).append("\tmove ecx, "+b+"\n");
			output.elementAt(Methcount).append("\tmove eax, ebx\n");
			output.elementAt(Methcount).append("\tadd eax, ecx\n");
			
			if(!hm.containsKey(q.d)) {
				hm.put(q.d, current_bp);
				current_bp += 2;
			}
		}
		else if(q.a.equals("-")) {
			String a,b;
			if(isInteger(q.b))
				a=q.b;
			else
				a=hm.get(q.b).toString();
			if(isInteger(q.c))
				b=q.c;
			else
				b=hm.get(q.c).toString();
				
			output.elementAt(Methcount).append("\tmove ebx, "+a+"\n");
			output.elementAt(Methcount).append("\tmove ecx, "+b+"\n");
			output.elementAt(Methcount).append("\tmove eax, ebx\n");
			output.elementAt(Methcount).append("\tsub eax, ecx\n");
			
			if(!hm.containsKey(q.d)) {
				hm.put(q.d, current_bp);
				current_bp += 2;
			}
		}
		else if(q.a.equals("*")) {
			String a,b;
			if(isInteger(q.b))
				a=q.b;
			else
				a=hm.get(q.b).toString();
			if(isInteger(q.c))
				b=q.c;
			else
				b=hm.get(q.c).toString();
				
			output.elementAt(Methcount).append("\tmove ebx, "+a+"\n");
			output.elementAt(Methcount).append("\tmove ecx, "+b+"\n");
			output.elementAt(Methcount).append("\tmove eax, ebx\n");
			output.elementAt(Methcount).append("\timul eax, ecx\n");			
		}
		else if(q.a.equals("/")) {
			String a,b;
			if(isInteger(q.b))
				a=q.b;
			else
				a=hm.get(q.b).toString();
			if(isInteger(q.c))
				b=q.c;
			else
				b=hm.get(q.c).toString();
				
			output.elementAt(Methcount).append("\tmove eax, "+a+"\n");
			output.elementAt(Methcount).append("\tcdq\n");
			output.elementAt(Methcount).append("\tmove ebx, "+b+"\n");
			output.elementAt(Methcount).append("\tidiv ebx, ecx\n");	// eax -> div, edx -> mod		
		}
		else if(q.a.equals("mod")) {
			String a,b;
			if(isInteger(q.b))
				a=q.b;
			else
				a=hm.get(q.b).toString();
			if(isInteger(q.c))
				b=q.c;
			else
				b=hm.get(q.c).toString();
				
			output.elementAt(Methcount).append("\tmove eax, "+a+"\n");
			output.elementAt(Methcount).append("\tcdq\n");
			output.elementAt(Methcount).append("\tmove ebx, "+b+"\n");
			output.elementAt(Methcount).append("\tidiv ebx, ecx\n");	// eax -> div, edx -> mod
			output.elementAt(Methcount).append("\tmove eax, edx\n");
		}
		
	}
	
}
