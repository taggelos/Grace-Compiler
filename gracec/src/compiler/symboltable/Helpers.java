package compiler.symboltable;

import java.util.*;

public class Helpers {
	public LinkedList<Quad> quads;
	public Quad lastQuad;
	public Quad firstQuad;
	int curline;
	//int curreg;

    public Helpers() {
    	quads = new LinkedList<Quad>(); 
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
	
	public void backpatch(LinkedList<Integer> list, int jump) {
		//while(!list.isEmpty()) {
		if(!list.isEmpty()) {
			quads.get(list.getLast().intValue()-1).d = Integer.toString(jump);
			list.removeLast();
		}
	}
	
	public void backpatchall(LinkedList<Integer> list, int jump) {
		while(!list.isEmpty()) {
		//if(!list.isEmpty()) {
			quads.get(list.getLast().intValue()-1).d = Integer.toString(jump);
			list.removeLast();
		}
	}
    
	public void printLast() {
    	System.out.println(lastQuad.printQuad());
    }
	
    public void printQuads() {
    	for (Quad q: quads){
    		System.out.println(q.printQuad());
    	}
    }
}