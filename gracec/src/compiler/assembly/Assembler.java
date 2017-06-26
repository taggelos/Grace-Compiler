package compiler.assembly;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
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
	int bp = 4;
	int current_si = 4;
	boolean si_used = false;
	int args = 0;
	boolean isCallee=false;
	int str=0;
	StringBuffer update=new StringBuffer();
	int current_scope=0;
	int scope;
	int current_par = 8;
	
	HashMap<String, Integer> current_bp = new HashMap<String, Integer>();
	public HashMap<String,HashMap<String, Integer>> paramhm = new HashMap<String, HashMap<String, Integer>>();
	HashMap<String, Integer> scopes = new HashMap<String, Integer>();
	public Vector<HashMap<String, Integer>> hm = new Vector<HashMap<String, Integer>>();
	HashMap<String, String> datahm = new HashMap<String, String>();
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
	
	public String nextLabel() {
		label++;
		return "L"+label;
	}
	
	public String getlastLabel() {
		return "L"+label;
	}
	
	public String nextdata() {
		str++;
		return "string"+str;
	}
	
	private boolean isStandard(String name) {
		if(name.equals("puti") || name.equals("puts") || name.equals("gets") || name.equals("getc") || name.equals("strlen"))
			return true;
		return false;
	}
	
	public void varAllocation(StringBuffer stringBuffer, String name) {
		int offset = 0;
		int type;
		int count;
		int total = 0;
		String[] sp;
		for(Variable_t var : st.contains(name+" ").methodVars) {
			count = 1;
			type=1;
			offset=0;
			sp = null;
			if(var.getType().contains("int")) {
				type = 4;
			}
			sp = var.getType().split("\\[");
			
			for(String s : sp) {
				s=s.replaceAll("]", "");
				if(isInteger(s)){
					count *= Integer.valueOf(s);
				}
			}
			offset = type*count;
			total += offset;
			System.err.println(">>>>>>"+current_bp);
			current_bp.put(name, current_bp.get(current.getName().trim())+4);
			hm.elementAt(Methcount).put(var.getName().trim(), current_bp.get(name));
			bp += offset;
		}
		
		if(offset != 0)
			stringBuffer.append("\tsub esp, "+total+"\n");
	}
	
	public void parAllocation(StringBuffer stringBuffer, String name) {
		int offset = 0;
		int type;
		int count;
		String[] sp;
		LinkedList<Variable_t> list = st.contains(name+" ").methodParams;
		ListIterator<Variable_t> it = list.listIterator(list.size());
		while(it.hasPrevious()) {
			Variable_t var = it.previous();
			count = 1;
			type=1;
			offset=0;
			sp = null;
			if(var.getType().contains("int")) {
				type = 4;
			}
			sp = var.getType().split("\\[");
			
			for(String s : sp) {
				s=s.replaceAll("]", "");
				if(isInteger(s)){
					count *= Integer.valueOf(s);
				}
			}
			offset = type*count;
			System.err.println(paramhm.get(current.getName().trim()));
			paramhm.get(current.getName().trim()).put(var.getName().trim(), current_par);
			current_par += offset;
		}
		
	}
	
	private void deallocateArgs(StringBuffer stringBuffer, String name) {
		int offset=4;
		for(Variable_t var : st.contains(name).methodParams) 
			offset += 4;
		stringBuffer.append("\tadd esp, "+offset+"\n");
	}
	
	private void updateAL(StringBuffer stringBuffer, Method_t meth) {
		
		System.err.println(meth.getName());
		if(isStandard(meth.getName().trim())) {
			if(meth.get_return_type().equals("nothing"))
				output.elementAt(Methcount).append("\tsub esp, 4\n");
			output.elementAt(Methcount).append("\tpush ebp\n");
		}
		else if(scopes.get(meth.getName()) > scopes.get(current.getName())) {
			if(meth.get_return_type().equals("nothing"))
				output.elementAt(Methcount).append("\tsub esp, 4\n");
			output.elementAt(Methcount).append("\tpush ebp\n");
		}
		else if(scopes.get(meth.getName()) == scopes.get(current.getName())) {
			if(meth.get_return_type().equals("nothing"))
				output.elementAt(Methcount).append("\tsub esp, 4\n");
			output.elementAt(Methcount).append("\tpush DWORD PTR [ebp + 8]\n");
		}
		else {
			si_used = true;
			for(int i=scopes.get(current.getName()); i<scopes.get(meth.getName()); i++)
				output.elementAt(Methcount).append("\tmov esi, DWORD PTR [ebp + 8]\n");
			output.elementAt(Methcount).append("\tpush esi\n");
		}
	}
	
	public String find(int i, String name) {
		while(i>=0) {
			if(hm.elementAt(i).containsKey(name.trim()))
				return hm.elementAt(i).get(name.trim()).toString();
			i--;
		}
		return null;
	}
	
	public String find2(String name, boolean isOp) {
		String a = null;
		boolean flag=false;
		System.err.println("--->"+hm.elementAt(Methcount));
		if(!hm.elementAt(Methcount).containsKey(name.trim().replaceAll("\\[", "").replaceAll("]", ""))) {
			for(Variable_t var : current.methodParams) {
				if(var.getName().equals(name.replaceAll("\\[", "").replaceAll("]", ""))) {  //mov esi, DWORD PTR [ebp + 16] mov eax, DWORD PTR [esi]
					if(isOp) {
						a="DWORD PTR [esi]";
						output.elementAt(Methcount).append("\tmov esi, DWORD PTR [ebp + 16]\n");
					}
					else
						a="DWORD PTR [ebp + "+paramhm.get(current.getName().trim()).get(var.getName().trim())+"]";
						
					flag = true;
					break;
				}
			}
		
			if(!flag) {
				output.elementAt(Methcount).append("\tpush esi\n\tmov esi, DWORD PTR [ebp + 8]\n");
				a="DWORD PTR [esi - "+hm.elementAt(Methcount-1).get(name.trim().replaceAll("\\[", "").replaceAll("]", "")).toString()+"]";
				si_used = true;
			}
			flag = false;
		}
		else
			a="DWORD PTR [ebp - "+hm.elementAt(Methcount).get(name.trim().replaceAll("\\[", "").replaceAll("]", "")).toString()+"]";
		
		return a;
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
    	        if(i.equals(q.num)) {
    	        	output.elementAt(Methcount).append(LabelMaps.get(i)+":\n");
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
		System.out.println(data.toString()+"\n");
	}

	private void init() {
		output.addElement(new StringBuffer());
		hm.addElement(new HashMap<String, Integer>());
		paramhm.put("main", new HashMap<String, Integer>());
		output.elementAt(Methcount).append(".intel_syntax noprefix\n");
		output.elementAt(Methcount).append(".text\n");
		output.elementAt(Methcount).append("\t.global main\n");
		data.append(".data\n");
	}

	private void caseUnit(Quad q) {	
		bp = 4;
		current = st.contains(q.b);
		
		current_par=16;
		args=0;
		
		if(isMain) {
			output.elementAt(Methcount).append("main:\n");
			current_bp.put("main", 0);
		}
		else {
			current_bp.put(current.getName().trim(), 0);
			Methcount++;
			output.addElement(new StringBuffer());
			hm.addElement(new HashMap<String, Integer>());
			paramhm.put(q.b.trim(), new HashMap<String, Integer>());
			output.elementAt(Methcount).append("grace_"+q.b+":\n");
		}
		
		output.elementAt(Methcount).append("\tpush ebp\n" + "\tmov ebp, esp\n");
		parAllocation(output.elementAt(Methcount), q.b.trim());
		varAllocation(output.elementAt(Methcount), q.b.trim());
		
		output.elementAt(Methcount).append("\n");
		
		isMain = false;
		
		current_scope++;
		scopes.put(q.b, current_scope);
	}
	
	private void caseEndu(Quad q) {	
		output.elementAt(Methcount).append("\n");
		String epilogue;
		if(Methcount==0)
			epilogue = "endof_main";
		else
			epilogue = "endof_"+current.getName();
			
		output.elementAt(Methcount).append(epilogue+":\n");
		if(si_used) {
			output.elementAt(Methcount).append("\tpop esi\n");
			si_used = false;
		}
		
		output.elementAt(Methcount).append("\tmov esp, ebp\n\tpop ebp\n\tret\n");
		Methcount--;
		current = current.from;
		current_scope--;
	}

	private void caseAss(Quad q) {
		boolean flag=false;
		String name = q.b.replaceAll("\\[", "").replaceAll("]", "").trim();
		String val = q.d.replaceAll("\\[", "").replaceAll("]", "").trim();
		
		String wtf=null;
		
		System.err.println(q.printQuad());
		
		if(!isInteger(q.b.replaceAll("\\[", "").replaceAll("]", ""))) {
			
			
			for(Variable_t var : st.contains(current.getName()).methodParams) {
				if(var.getName().equals(q.b.replaceAll("\\[", "").replaceAll("]", ""))) {
					if(var.isRef()) {
						name = find2(q.b.replaceAll("\\[", "").replaceAll("]", ""), false);
						wtf = "\tmov esi, "+name+"\n"; 
						name = "DWORD PTR [esi]";
						flag=true;
						break;
					}
						
				}
			}
			if(!flag)
				name=find2(q.b.replaceAll("\\[", "").replaceAll("]", ""), false);
		}
		
		if(!isInteger(q.d.replaceAll("\\[", "").replaceAll("]", ""))) {
			if(q.d.equals("$$")) {
				if(!isInteger(q.b.replaceAll("\\[", "").replaceAll("]", "").trim())) {
					output.elementAt(Methcount).append("\tmov eax, "+name+"\n");
					name = "eax";
				}
				output.elementAt(Methcount).append("\tmov esi, DWORD PTR [ebp + 12]\n\tmov DWORD PTR [esi], "+name+"\n");
				return;
			}
			else if(val.contains("\"")) {
				if(!datahm.containsKey(val)) {
					datahm.put(val, nextdata());
					data.append("\t"+datahm.get(val)+": .asciz "+val+"\n");
				}
				output.elementAt(Methcount).append("\tmov eax, OFFSET FLAT:"+datahm.get(val)+"\n");
				return;
			}
			else {
				val=find2(q.d.replaceAll("\\[", "").replaceAll("]", ""), false);
				output.elementAt(Methcount).append("\tmov eax, "+val+"\n");
				val = "eax";
			}
		}
		
		if(wtf != null) {
			output.elementAt(Methcount).append(wtf);
		}
		output.elementAt(Methcount).append("\tmov "+name+", "+val+"\n");
		
	}
	
	private void caseArray(Quad q) {
		// TODO Auto-generated method stub
		String a, b, c;
		int size;
		
		Variable_t var = getType(q.b+" ", current);
		if(var.getType().contains("int"))
			size = 4;
		else
			size = 1;
		
		a="DWORD PTR [ebp-"+hm.elementAt(Methcount).get(q.b).toString()+"]";
		
		c = q.d.replaceAll("\\[", "").replaceAll("]", "").trim();
		if(!hm.elementAt(Methcount).containsKey(c)) {
			current_bp.put(current.getName(), current_bp.get(current.getName().trim())+4);
			hm.elementAt(Methcount).put(c.trim(), current_bp.get(current.getName()));
			bp += 4;
		}
			
		if(isInteger(q.c))
			b=q.c;
		else 
			b="DWORD PTR [ebp-"+hm.elementAt(Methcount).get(q.c.trim().replaceAll("\\[", "").replaceAll("]", "")).toString()+"]";
			
		output.elementAt(Methcount).append("\tmov eax, "+b+"\n");
		output.elementAt(Methcount).append("\tmov ecx, "+size+"\n");
		output.elementAt(Methcount).append("\timul ecx\n");
		output.elementAt(Methcount).append("\tlea ecx, "+a+"\n");
		output.elementAt(Methcount).append("\tadd eax, ecx\n");
		output.elementAt(Methcount).append("\tmov DWORD PTR [ebp-"+hm.elementAt(Methcount).get(c).toString()+"], "+"eax\n");
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
		
		String a = null, b;
		int offset=4;
		//system.err.println(paramhm);
		boolean flag = false;
		if(q.c.equals("V")) {
			if(isInteger(q.b))
				a=q.b;
			else {	
				//system.err.println("--->"+Methcount);
				if(!hm.elementAt(Methcount).containsKey(q.b.trim().replaceAll("\\[", "").replaceAll("]", ""))) {
					
					for(Variable_t var : current.methodParams) {
						if(var.getName().equals(q.b.replaceAll("\\[", "").replaceAll("]", ""))) {
							a="DWORD PTR [ebp + "+(16+((args++)*4))+"]";
							flag = true;
							break;
						}
					}
				
					if(!flag) {
						output.elementAt(Methcount).append("\tpush esi\n\tmov esi, DWORD PTR [ebp + 8]\n");
						a="DWORD PTR [esi - "+hm.elementAt(Methcount-1).get(q.b.trim().replaceAll("\\[", "").replaceAll("]", "")).toString()+"]\n";
						si_used = true;
					}
					flag = false;
				}
				else
					a="DWORD PTR [ebp - "+hm.elementAt(Methcount).get(q.b.trim().replaceAll("\\[", "").replaceAll("]", "")).toString()+"]";
				
			}
			
			update.append("\tmov eax, "+a+"\n\tpush eax\n");
		}
		else if(q.c.equals("R")) {
			if(isInteger(q.b))
				a=q.b;
			else if(q.b.contains("\"")) {
				if(!datahm.containsKey(q.b)) {
					datahm.put(q.b, nextdata());
					data.append("\t"+datahm.get(q.b)+": .asciz "+q.b+"\n");
					
				}
				a="OFFSET FLAT:"+datahm.get(q.b);
				update.append("\tmov eax, "+a+"\n\tpush eax\n");
			}
			else {
				//a="DWORD PTR [ebp-"+hm.get(q.b).toString()+"]";
				////offset += args*4;
				////a = "DWORD PTR [ebp + "+ offset+"]";
				//output.elementAt(Methcount).append("\tmov esi, "+a+"\n");
				//a = "DWORD PTR [esi - "+ current_si+"]";
				//current_si += 4;
				
				a = find2(q.b, false);
				
				output.elementAt(Methcount).append("\tlea esi, "+a+"\n");
				output.elementAt(Methcount).append("\tpush esi\n");
			}
		}
		else if(q.b.equals("RET")) {
			if(!hm.elementAt(Methcount).containsKey(q.c)) {
				current_bp.put(current.getName().trim(), current_bp.get(current.getName().trim())+4);
				hm.elementAt(Methcount).put(q.c.trim(), current_bp.get(current.getName().trim()));
				bp += 4;
			}
			//system.err.println("----->>>"+hm.elementAt(Methcount));
			output.elementAt(Methcount).append("\tlea esi, DWORD PTR [ebp - "+hm.elementAt(Methcount).get(q.c.trim())+"]\n");
			output.elementAt(Methcount).append("\tpush esi\n");
		}
	}
	
	private void caseCall(Quad q) {
		// TODO Auto-generated method stub
		args=0;
		
		output.elementAt(Methcount).append(update);
		update.delete(0, update.length());
		updateAL(output.elementAt(Methcount), st.contains(q.d));
		

		output.elementAt(Methcount).append("\tcall grace_"+q.d+"\n");
		deallocateArgs(output.elementAt(Methcount), q.d);
		for(String name : standards.smethodnames) {
			if(name.equals(q.d)) {
				standards.create(name);
				if(name.equals("puti ")) {
					if(!datahm.containsKey(".asciz  \"%d\"")) {
						data.append("\tint_fmt: .asciz  \"%d\"");
						datahm.put(".asciz  \"%d\"", "int_fmt");
					}
				}
				else if(name.equals("strlen ")) {
					if(!datahm.containsKey("strln_strings")) {
						data.append("\tint_fmt: .asciz  \"%d\n\"\n\tchar_fmt: .asciz  \"%c\n\"\n\tchars_fmt: .asciz  \"%s\n\"\n");
						datahm.put("\tint_fmt: .asciz  \"%d\n\"\n\tchar_fmt: .asciz  \"%c\n\"\n\tchars_fmt: .asciz  \"%s\n\"\n", "strln_strings");
					}
				}
				break;
			}
		}
	}

	private void caseCompare(Quad q) {
		String a,b;
		if(isInteger(q.b.trim()))
			a=q.b;
		else
			a=find2(q.b, false);
		if(isInteger(q.c))
			b=q.c;
		else
			b=find2(q.c, false);
		
		String lb = nextLabel();
		LabelMaps.put(Integer.valueOf(q.num), lb);
		
		output.elementAt(Methcount).append("\t"+lb+":\n");
		output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
		output.elementAt(Methcount).append("\tmov edx, "+b+"\n");
		output.elementAt(Methcount).append("\tcmp eax, edx\n");
		
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
		String a = null,b = null;
		boolean flag=false;
		String aa = null;
		if(isInteger(q.b.trim()))
			a=q.b;
		else {
			a = find2(q.b, true);
		}
		
		if(isInteger(q.c))
			b=q.c;
		else {
			b=find2(q.c, true);
		}
		
		//system.err.println(current_bp);
		if(!hm.elementAt(Methcount).containsKey(q.d)) {
			current_bp.put(current.getName(), current_bp.get(current.getName().trim())+4);
			hm.elementAt(Methcount).put(q.d.trim(), current_bp.get(current.getName()));
			bp += 4;
		}
		
		if(q.a.equals("+")) {
			output.elementAt(Methcount).append("\tmov ecx, "+b+"\n");
			output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
			output.elementAt(Methcount).append("\tadd eax, ecx\n");	
		}
		else if(q.a.equals("-")) {
			output.elementAt(Methcount).append("\tmov ecx, "+b+"\n");
			output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
			output.elementAt(Methcount).append("\tsub eax, ecx\n");
			
		}
		else if(q.a.equals("*")) {
							
			output.elementAt(Methcount).append("\tmov ecx, "+b+"\n");
			output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
			output.elementAt(Methcount).append("\timul eax, ecx\n");			
		}
		else if(q.a.equals("/")) {
			
			output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
			output.elementAt(Methcount).append("\tcdq\n");
			output.elementAt(Methcount).append("\tmov ebx, "+b+"\n");
			output.elementAt(Methcount).append("\tidiv ebx\n");	// eax -> div, edx -> mod		
		}
		else if(q.a.equals("mod")) {
			
			output.elementAt(Methcount).append("\tmov eax, "+a+"\n");
			output.elementAt(Methcount).append("\tcdq\n");
			output.elementAt(Methcount).append("\tmov ebx, "+b+"\n");
			output.elementAt(Methcount).append("\tidiv ebx\n");	// eax -> div, edx -> mod
			output.elementAt(Methcount).append("\tmov eax, edx\n");
		}
		output.elementAt(Methcount).append("\tmov DWORD PTR [ebp-"+hm.elementAt(Methcount).get(q.d)+"], eax\n");
		
	}
	
	private void caseReturn(Quad q) {
		String epilogue = "endof_"+current.getName();
		output.elementAt(Methcount).append("\tjmp "+ epilogue +"\n");
	}
	
}
