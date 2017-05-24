package compiler.symboltable;

import java.util.*;

public class Helpers {
	public LinkedList<Quad> quads;
	//public LinkedList<String> regs;
	public Quad lastQuad;
	public Quad firstQuad;
	int curline;
	//int curreg;

    public Helpers() {
    	quads = new LinkedList<Quad>(); 
    	//regs = new LinkedList<String>();
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
    
	public void printLast() {
    	System.out.println(lastQuad.printQuad());
    }
	
    public void printQuads() {
    	for (Quad q: quads){
    		System.out.println("[ " + q.printQuad() +" ]" );
    	}
    }
}