package compiler.symboltable;

import java.util.*;

public class Quad {
	public int num;
    public String a;
    public String b;
    public String c;
    public String d;

    public Quad(int num,String a, String b, String c, String d) {
        this.num=num;
    	this.a=a;
        this.b=b;
        this.c=c;
        this.d=d;       
    }
    
    @Override
	public String toString() {
    	return Integer.toString(num)+ ": " + a + ", " + b + ", " + c +", "+ d+"\n";
	}

	public String printQuad() {
    	return Integer.toString(num)+ ": " + a + ", " + b + ", " + c +", "+ d ;
    }
    
    public void printQuad2() {
    	System.out.println(Integer.toString(num)+ ": " +a + ", " + b + ", " + c +", "+ d);
    }
}