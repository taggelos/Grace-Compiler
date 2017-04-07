package compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;

import compiler.lexer.Lexer;
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
                /*PushbackReader reader = new PushbackReader(new InputStreamReader(fis));   
                Lexer lexer = new Lexer(reader);

                while(true) {
                    try {
                        Token t = lexer.next();

                        if (t instanceof EOF)
                        break;
                        System.out.println(t.toString());
                    } 
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                } */
                Parser p = new Parser(
                        new Lexer(
                            new PushbackReader(
                                new InputStreamReader(fis), 1024
                        )
                    )
                );
                try {
                    Start tree = p.parse();
                    System.out.println(tree.toString());
                    
                    Printer pr = new Printer();
                    tree.apply(pr);
                    byte[] contentInBytes = pr.getoutput().toString().getBytes();

        			fop.write(contentInBytes);
        			fop.flush();
        			fop.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
            catch(FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            } 
            finally {
                try {
                    if(fis != null && fop!=null)
                        fis.close();
                    	fop.close();
                } 
                catch(IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            System.out.println("--------------"+ args[i] +"--------------");
        }
        System.exit(0);
    }

}