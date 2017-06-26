package compiler.assembly;

import java.util.HashMap;
import java.util.LinkedList;
import compiler.assembly.Assembler;

public class Standards {
	HashMap<String, StringBuffer> code;
	
	static String[] smethodnames = {
	  		  "puti ", 
	  		  "putc ", 
	  		  "puts ", 
	  		  "geti ", 
	  		  "getc ", 
	  		  "gets ", 
	  		  "abs ",
	  		  "ord ",
	  		  "chr ",
	  		  "strlen ",
	  		  "strcmp ",
	  		  "strcpy ",
	  		  "strcat "
	  		};
	
    public Standards() {
    	code = new HashMap<String, StringBuffer>();
    	
	}
    
    public void create(String name) {
    	if(name.equals("puti "))
    		createputi();
    	else if(name.equals("gets ")) 
    		creategets();
    	else if(name.equals("puts "))
    		createputs();
    	else if(name.equals("putc "))
    		createputs();
    	else if(name.equals("strlen "))
    		createstrlen();
    }
    
    public void createstrlen() {
    	StringBuffer lines = new StringBuffer();
    	lines.append("\tpush ebp\n\tmov ebp, esp\n\tmov eax , DWORD PTR [ebp + 16]\n\tpush eax\n\tcall strlen\n\tadd esp, 4\n\tmov edx, DWORD ptr [ebp+12]\n\tmov DWORD PTR [edx], eax\n\tmov esp, ebp\n\tpop ebp\n\tret\n");
    	code.put("grace_strlen", lines);
	}

	public void createputc() {
		StringBuffer lines = new StringBuffer();
    	lines.append("\tpush ebp\n\tmov ebp, esp\n\tpush eax\n\tmov eax, OFFSET FLAT:char_fmt\n\tpush eax\n\tcall printf\n\tadd esp, 8\n\n\tmov esp, ebp\n\tpop ebp\n\tret\n");
    	code.put("grace_putc", lines);
	}

	public void createputs() {
    	StringBuffer lines = new StringBuffer();
    	lines.append("\tpush ebp\n\tmov ebp, esp\n\t\n\tmov eax, DWORD PTR [ebp + 8]\n\tpush eax\n\tcall printf\n\tadd esp, 8\n\t\n\tmov esp, ebp\n\tpop ebp\n\tret\n\t");
    	code.put("grace_puts", lines);
	}

	public void createputi() {
    	StringBuffer lines = new StringBuffer();
    	lines.append("\tpush ebp\n\tmov ebp, esp\n\t\n\tpush eax\n\tmov eax, OFFSET FLAT:int_fmt\n\tpush eax\n\tcall printf\n\tadd esp, 8\n\t\n\tmov esp, ebp\n\tpop ebp\n\tret\n\t");
    	code.put("grace_puti", lines);

    }
    
    public void creategets() {
    	StringBuffer lines = new StringBuffer();
    	lines.append("\tpush ebp\n\tmov ebp, esp\n\t\n\t\n\tmov eax, DWORD PTR stdin\n\tpush eax\n\t\n\t\n\tmov eax, DWORD PTR [ebp + 8]\n\tpush eax\n\t\n\t\n\tmov eax, DWORD PTR [ebp + 12]\n\tpush eax\n\t\n\t\n\tcall fgets\n\tadd esp, 12\n\t\n\t\n\tmov eax, 10 # Carriage return\n\tpush eax\n\t\n\t\n\tmov eax, DWORD PTR [ebp + 12]\n\tpush eax\n\tcall strchr\n\tadd esp, 8\n\t\n\t\n\tcmp eax, 0\n\tje grace_gets_no_newline\n\t\n\t\n\tmov BYTE PTR [eax], 0\n\tgrace_gets_no_newline:\n\t\n\t\n\tmov esp, ebp\n\tpop ebp\n\tret\n\t");
    	code.put("grace_gets", lines);
    }
}