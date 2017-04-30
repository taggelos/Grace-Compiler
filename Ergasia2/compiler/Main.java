package compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;

import compiler.lexer.Lexer;
import compiler.lexer.LexerException;
import compiler.node.EOF;
import compiler.node.Start;
import compiler.node.Token;
import compiler.parser.*;

public class Main {

    public static void main(String args[]) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fop = null;
        File file;
        if(args.length < 1){
            System.err.println("Usage: java Main <inputFile1> [<inputFile2>] ...");
            System.exit(1);

        }

        for(int i = 0; i < args.length; i++) {
            try {
                fis = new FileInputStream(args[i]);
                file = new File(args[i].replace(".", "_")+"output.txt");
    			fop = new FileOutputStream(file);
    			if (!file.exists()) {
    				file.createNewFile();
    			}
                Parser p = new Parser(
                        new Lexer(
                            new PushbackReader(
                                new InputStreamReader(fis), 1024
                        )
                    )
                );
                try {
                    Start tree = p.parse();
                    
                    Printer pr = new Printer();
                    tree.apply(pr);
                    byte[] contentInBytes = pr.getoutput().toString().getBytes();

        			fop.write(contentInBytes);
        			fop.flush();
        			fop.close();
                } catch (LexerException e) {
            	    System.err.printf("Lexing error: %s\n", e.getMessage());
                } catch (ParserException e) {
                	System.err.printf("Parsing error: %s\n", e.getMessage());
                }
            } catch(FileNotFoundException ex) {
                	System.err.printf("I/O error: %s\n", ex.getMessage());
                	ex.printStackTrace();
            } 
            finally {
                try {
                    if(fis != null && fop!=null)
                        fis.close();
                    	fop.close();
                } 
                catch(IOException ex) {
                	System.err.printf("I/O error: %s\n", ex.getMessage());
                	ex.printStackTrace();
                }
            }
            System.out.println("--------------"+ args[i] +"--------------");
            System.out.println("\t---------------------- Programm Parsed Successfully ----------------------");
        }
        System.exit(0);
    }

}
