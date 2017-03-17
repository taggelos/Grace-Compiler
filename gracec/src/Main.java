import simpleAdder.intepreter.Interpreter;
import simpleAdder.parser.*;
import simpleAdder.lexer.*;
import simpleAdder.node.*;

import java.io.*;

public class Main {
	public static void main(String[] args) {
		
			try {
				/* Form our AST */
				// Lexer lexer = new Lexer (new PushbackReader(new
				// FileReader(args[0]), 1024));
				PushbackReader reader = new PushbackReader(new InputStreamReader(System.in));
				Lexer lexer = new Lexer(reader);
				
				Parser parser = new Parser(lexer);
				Start ast = parser.parse();

				/* Get our Interpreter going. */
				Interpreter interp = new Interpreter();
				ast.apply(interp);
			} catch (Exception e) {
				System.out.println(e);
			}
		} 
	}
