package compiler;

import java.io.PushbackReader;

import lexer.Lexer;
import node.EOF;
import node.Token;

import java.io.InputStreamReader;

public class Main {

        public static void main(String args[]) {
                PushbackReader reader = new PushbackReader(new InputStreamReader(System.in));   
                Lexer lexer = new Lexer(reader);

                while(true) {
                        try {
                                Token t = lexer.next();

                        if (t instanceof EOF)
                                break;
                        System.out.println(t.toString());
                        } catch (Exception e)
                        {
                                System.err.println(e.getMessage());
                        }
                }

                System.exit(0);
        }

}
