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
    
    public boolean compQuad(Quad q1,Quad q2){
    	if(q1.a.equals(q2.a)  && q1.b.equals(q2.b) && q1.c.equals(q2.c) && q1.d.equals(q2.d) && q1.num==q2.num)
    		return true;
    	return false;
    }
    
    public void remQuad(Quad q){
    	//quads.remove(q);
    	Iterator<Quad> iter = quads.iterator();
    	while (iter.hasNext()) {
    		Quad data=iter.next();
    	    if (compQuad(data,q)) {
    	        iter.remove();
    	        break;
    	    }   
    	}
    }    
    
    public void remQuad(int num){
    	//quads.remove(q);
    	Iterator<Quad> iter = quads.iterator();
    	while (iter.hasNext()) {
    		Quad data=iter.next();
    	    if (data.num==num) {
    	        iter.remove();
    	        break;
    	    }   
    	}
    } 
    
    public void setQuad(Quad q,Quad q2){  
        	Iterator<Quad> iter = quads.iterator();
        	while (iter.hasNext()) {
        		Quad data=iter.next();
        	    if (compQuad(data,q)) {
        	    	data.num=q2.num;
        	    	data.a=q2.a;
        	    	data.b=q2.b;
        	    	data.c=q2.c;
        	    	data.d=q2.d;
        	    	//quads.set(quads.indexOf(data), q2);
        	    	break;
        	    }   
        	}
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