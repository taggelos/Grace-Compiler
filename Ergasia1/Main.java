import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;

import compiler.lexer.Lexer;
import compiler.node.EOF;
import compiler.node.Start;
import compiler.node.Token;
import compiler.parser.*;

public class Main {

    public static void main(String args[]) throws FileNotFoundException {
        FileInputStream fis = null;
        if(args.length < 1){
            System.err.println("Usage: java Main <inputFile1> [<inputFile2>] ...");
            System.exit(1);

        }

        for(int i = 0; i < args.length; i++) {
            try {
                fis = new FileInputStream(args[i]);

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
                    
                    tree.apply(new Printer());
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
            catch(FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            } 
            finally {
                try {
                    if(fis != null)
                        fis.close();
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
