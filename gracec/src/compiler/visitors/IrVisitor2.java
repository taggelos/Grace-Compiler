package compiler.visitors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import compiler.symboltable.Helpers;
import compiler.symboltable.Quad;
import compiler.symboltable.SymbolTable;

public class IrVisitor2 {
	private LinkedList<Quad> quads;
	public Helpers h2 = new Helpers();
	public Vector<StringBuffer> output = new Vector<StringBuffer>();
	public StringBuffer data = new StringBuffer();
	public SymbolTable st;
	public LinkedList<LinkedList<Quad>> blocks;	
	
	HashMap<String, String> myhm = new HashMap<String, String>();
	
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
				case "=":
					h2.setQuad(quads.get(i), new Quad(quads.get(i).num,"#",quads.get(i).b,quads.get(i).c,quads.get(i+1).d));
					h2.remQuad(quads.get(i+1));
					myret=true;
					break;
				case "#":
					h2.setQuad(quads.get(i), new Quad(quads.get(i).num,"=",quads.get(i).b,quads.get(i).c,quads.get(i+1).d));
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
	
	public boolean constantAndCopyPropagation(boolean simpleVersion){ 
		boolean myret=false,simpleV=false; //if sth changed boolean
		for (int i = 0; i < quads.size(); i ++){
			if(quads.get(i).a.equals(":=") && !isInteger(quads.get(i).b)){ //&& isInteger(quads.get(i).d) //(for one propagation only)
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
									h2.setQuad(quads.get(i), new Quad(quads.get(i).num,"_","-","-","-"));
									myret=true;
								}
								if (quads.get(j).c.equals(quads.get(i).b)){
									h2.setQuad(quads.get(j), new Quad(quads.get(j).num,quads.get(j).a,quads.get(j).b,quads.get(i).d,quads.get(j).d));
									h2.setQuad(quads.get(i), new Quad(quads.get(i).num,"_","-","-","-"));
									myret=true;
								}
								break;
							case ":=":
								if(quads.get(j).d.equals(quads.get(i).b) ){
									h2.setQuad(quads.get(j), new Quad(quads.get(j).num,quads.get(j).a,quads.get(j).b,quads.get(j).c,quads.get(i).d));
									h2.setQuad(quads.get(i), new Quad(quads.get(i).num,"_","-","-","-"));
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
									h2.setQuad(quads.get(i), new Quad(quads.get(i).num,"_","-","-","-"));
									myret=true;
								}
								if(quads.get(j).c.equals(quads.get(i).b) ){
									h2.setQuad(quads.get(j), new Quad(quads.get(j).num,quads.get(j).a,quads.get(j).b,quads.get(i).d,quads.get(j).d));
									h2.setQuad(quads.get(i), new Quad(quads.get(i).num,"_","-","-","-"));
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
		
	public boolean constantFolding(){ 
		boolean myret=false;
		for (int i = 0; i < quads.size(); i ++){
			if(isInteger(quads.get(i).b) && isInteger(quads.get(i).c))
				switch (quads.get(i).a) {
					case "*" :
						int mult = Integer.parseInt(quads.get(i).b.trim()) * Integer.parseInt(quads.get(i).c.trim());
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",Integer.toString(mult)));
						myret=true;
						break;
					case "+" :
						int plus = Integer.parseInt(quads.get(i).b.trim()) + Integer.parseInt(quads.get(i).c.trim());
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",Integer.toString(plus)));
						myret=true;
						break;
					case "-" :
						int minus = Integer.parseInt(quads.get(i).b.trim()) - Integer.parseInt(quads.get(i).c.trim());
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",Integer.toString(minus)));
						myret=true;
						break;
					case "/" :
						System.err.println(quads.get(i).b);
						int div = Integer.parseInt(quads.get(i).b.trim()) / Integer.parseInt(quads.get(i).c.trim());
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",Integer.toString(div)));
						myret=true;
						break;
					case "mod" : 
						int mod = Integer.parseInt(quads.get(i).b.trim()) % Integer.parseInt(quads.get(i).c.trim());
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",Integer.toString(mod)));
						myret=true;
						break;
					default:
						break;						
				}
		}
		return myret;
	}
	
	public boolean algebraicSimplification(){
		boolean myret=false;
		for (int i = 0; i < quads.size(); i ++){
			if(quads.get(i).a.equals(":=") && quads.get(i).b.equals(quads.get(i).d)){
				 h2.setQuad(quads.get(i), new Quad(quads.get(i).num,"_","-","-","-"));
				 myret=true;
				 continue;
			}
			Integer a = null;
			String b = null;
			if(isInteger(quads.get(i).b)){
				a=Integer.parseInt(quads.get(i).b.trim()) ;
				b= quads.get(i).c;
			}
			else if (isInteger(quads.get(i).c))	{
				a=Integer.parseInt(quads.get(i).c.trim()) ;
				b= quads.get(i).b;
				switch (quads.get(i).a) {
				case "/" :
				case "mod" :
					if(a==1){	
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",b));
						myret=true;
					}
					break;
				case "-" :
					if(a==0){	
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",b));
						myret=true;
					}
					break;
					default:
						break;	
				}				
			}
			if (a!=null){
			switch (quads.get(i).a) {
				case "+" :
					if(a==0){	
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",b));
						myret=true;
					}
					break;
				case "*" :
					if(a==1){	
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-",b));
						myret=true;
					}
					else if(a==0){
						h2.setQuad(quads.get(i), new Quad(quads.get(i).num,":=",quads.get(i).d,"-","0"));
						myret=true;
					}
					break;
				default:
					break;	
			}
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
	
	public void findBlocks(){
		 blocks = new LinkedList<>();
		 for (int i=0; i<quads.size(); ) {
		  LinkedList<Quad> block = new LinkedList<>();
		  while (true) {
		   Quad quad = quads.get(i);
		   block.add(quad);
		   i++;
		   if (jump(quad) || i==quads.size() || isLabel(quads.get(i), quads)) {
		    break;
		   }
		  }
		  blocks.add(block);
		}
	}

	boolean jump(Quad quad) {
	 switch (quad.a) {
	  case ">":
	  case "<":
	  case "<=":
	  case ">=":
	  case "=":
	  case "#":
	  case "jump":
	  case "ret":
	  case "endu":
	   return true;
	  default:
	   return false;
	 }
	}

	boolean isLabel(Quad quad, List<Quad> quads) {
	if (quad.a.equals("unit")) {
		   return true;
		  }
	 for (Quad q : quads) {
	  switch (q.a) {
	   case ">":
	   case "<":
	   case "<=":
	   case ">=":
	   case "=":
	   case "#":
	   case "jump":
	    if (q.d.equals(String.valueOf(quad.num)))
	     return true;
	  }
	 }
	 return false;
	}
	
	public void iterateOverRealJumps(){
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

    public IrVisitor2(LinkedList<Quad> quads, SymbolTable symboltable) {
    	this.quads= quads;
    	this.st = symboltable;
	}

	public void create() {
		init();
		for(Quad q : quads) {
			h2.genQuad(q.a, q.b, q.c, q.d);
		}		
		this.quads=h2.quads;
		//constantAndCopyPropagation(true); //true for simple version
		revertConds();		
		findBlocks();
		//constantFolding();
		while(constantAndCopyPropagation(true) || constantFolding() || algebraicSimplification()){
			//System.err.println("one more time");
		};
		h2.printQuads();
		
	}

	private void init() {
		System.out.println();
		System.out.println("----------------------------------");
		System.out.println();
	}
}
