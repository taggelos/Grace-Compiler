package compiler.assembly;


import java.util.HashMap;
import java.util.LinkedList;
import compiler.symboltable.Quad;

public class Assembler {
	private LinkedList<Quad> quads; 
	public StringBuffer output = new StringBuffer();
	public StringBuffer data = new StringBuffer();
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
			case ":=":
				caseAss(q);
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
		output.append(data);
		System.out.println(output.toString());
	}

	private void init() {
		output.append(".intel_syntax noprefix\n");
		output.append(".text\n");
		output.append("\t.global main\n");
		data.append(".data\n");
	}
	
	private void caseAss(Quad q) {
		
	}

	private void caseJump(Quad q) {
		// TODO Auto-generated method stub
		output.append("\tjmp "+ nextLabel() +"\n");
		
	}

	private void caseUnit(Quad q) {	
		//output.append("_"+q.b + "\tproc near\n");
		//output.append("\tpush ebp\n" + "\tmov bp, sp\n" + "\tsub sp,8\n");
		if(isMain)
			output.append("main:\n");
		else
			output.append(q.b+":\n");
		
		isMain = false;
		output.append("\tpush ebp\n" + "\tmov ebp, esp\n");
	}
	
	private void caseEndu(Quad q) {	
		//output.append("_"+q.b + "\tmov sp, bp\n");
		//output.append("\tpop ebp\n" + "\tmov bp, sp\n" + "\tsub sp,8\n");
		output.append("\tmove esp, ebp\n\tpop ebp\n\tret\n");
	}
	
	private void caseOper(Quad q) {
		if(q.a.equals("+")) {
			String a,b;
			int c;
			if(isInteger(q.b))
				a=q.b;
			else
				a=hm.get(q.b).toString();
			if(isInteger(q.c))
				b=q.c;
			else
				b=hm.get(q.c).toString();
			if(hm.containsKey(q.d))
				c=hm.get(q.d);
			else {
				hm.put(q.d, current_bp);
				c=hm.get(q.d);
				current_bp += 2;
			}
				
			output.append("\tmove ebx, "+a+"\n");
			output.append("\tmove ecx, "+b+"\n");
			output.append("\tmove eax, ebx\n");
			output.append("\tadd eax, ecx\n");
			output.append("\tmov word ptr [ebp - "+c+"], eax\n");
			
		}
		else if(q.a.equals("-")) {
			
		}
		else if(q.a.equals("*")) {
			
		}
		else if(q.a.equals("/")) {
			
		}
		else if(q.a.equals("mod")) {
			
		}
		
	}
	
}
