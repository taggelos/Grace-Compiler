package compiler.assembly;


import java.util.LinkedList;
import compiler.symboltable.Quad;

public class Assembler {
	private LinkedList<Quad> quads; 
	public StringBuffer output = new StringBuffer();

    public Assembler(LinkedList<Quad> quads) {
    	this.quads= quads;
	}

	public void create() {
		for(Quad q : quads) {
    		//System.out.println(q.printQuad());
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
		System.out.println(output.toString());
	}

	
	private void caseAss(Quad q) {
		// TODO Auto-generated method stub
		output.append("\tjmp label("+ q.d +")\n");
		
	}

	private void caseJump(Quad q) {
		// TODO Auto-generated method stub
		output.append("\tjmp label("+ q.d +")\n");
		
	}

	private void caseUnit(Quad q) {	
		output.append("_"+q.b + "\tproc near\n");
		output.append("\tpush ebp\n" + "\tmov bp, sp\n" + "\tsub sp,8\n");
		
		
	}
	
	private void caseEndu(Quad q) {	
		output.append("_"+q.b + "\tmov sp, bp\n");
		//output.append("\tpop ebp\n" + "\tmov bp, sp\n" + "\tsub sp,8\n");
		System.out.println(output.toString());		
	}
	
	private void caseOper(Quad q) {
		// TODO Auto-generated method stub

		q.printQuad2();
		
	}

	
	
	
	
}
