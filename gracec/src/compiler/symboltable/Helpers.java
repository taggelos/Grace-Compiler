package compiler.symboltable;

import java.util.*;

public class Helpers {
	public LinkedList<Quad> quads;
	public LinkedList<Integer> trueList;
	public LinkedList<Integer> falseList;
	public Quad lastQuad;
	public Quad firstQuad;
	int curline;
	//int curreg;

    public Helpers() {
    	quads = new LinkedList<Quad>(); 
    	trueList = new LinkedList<Integer>();
    	falseList = new LinkedList<Integer>();
    	firstQuad =null;
    	lastQuad =null;
    	curline =1;
    }
    
    public void genQuad(String a, String b, String c, String d){
    	Quad q= new Quad(curline++,a,b,c,d);
    	if(firstQuad==null) firstQuad=q;
    	quads.add(q);
    	lastQuad = q;
    }
    
	public int nextQuad(){
		//if(lastQuad == firstQuad) return firstQuad;
		return curline;	
	}
	
	public void backpatch(boolean b, int jump) {
		System.out.println("TREU: "+ trueList);
		System.out.println("FALSE: "+ falseList);
		if(b) {
			if(!trueList.isEmpty()) {
				quads.get(trueList.getLast().intValue()-1).d = Integer.toString(jump);
				trueList.removeLast();
			}
		}
		else {
			if(!falseList.isEmpty()) {
				quads.get(falseList.getLast().intValue()-1).d = Integer.toString(jump);
				falseList.removeLast();
			}
		}
			
	}
    
	public void printLast() {
    	System.out.println(lastQuad.printQuad());
    }
	
    public void printQuads() {
    	for (Quad q: quads){
    		System.out.println("[ " + q.printQuad() +" ]" );
    	}
    }
}