package compiler;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;

import compiler.assembly.Assembler;
import compiler.lexer.Lexer;
import compiler.node.Start;
import compiler.parser.*;
import compiler.symboltable.Quad;
import compiler.symboltable.SymbolTable;
import compiler.visitors.*;

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
                file = new File(args[i].replace(".", "_")+"_ir.txt");
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
                    System.out.println(tree.toString());
                    
                    FunctionVisitor fv = new FunctionVisitor();
                    tree.apply(fv);
                    
                    SymbolTable symboltable = new SymbolTable(fv.methods);
                    //symboltable.printST();
                    
                    if(fv.errors.isEmpty()){
                    	IrVisitor iv = new IrVisitor(symboltable);
                    	tree.apply(iv);
                    	for(Quad q : iv.h.quads) {
                    		fop.write((q.printQuad()+'\n').getBytes());
                    	}
                    	//Assembler as = new Assembler(iv.h.quads);
                    	//as.create();
                    }
                    
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
