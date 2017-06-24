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
            	File fold = new File(args[i]);
            	//System.err.println(new File(fold.getParent()+"/../ir/", fold.getName()));
                fis = new FileInputStream(args[i]);
                file = new File(fold.getParent()+"/../ir/", fold.getName().replace(".", "_")+"_ir.txt");
    			
    			
                
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
                    
                    SymbolTable symboltable = new SymbolTable(FunctionVisitor.methods);
                    //symboltable.printST();
                    
                    if(fv.errors.isEmpty()){
                    	fop = new FileOutputStream(file);
                    	if (!file.exists()) {
            				file.createNewFile();
            			}
                    	IrVisitor iv = new IrVisitor(symboltable);
                    	tree.apply(iv);
                    	for(Quad q : iv.h.quads) {
                    		fop.write((q.printQuad()+'\n').getBytes());
                    	}
                    	Assembler as = new Assembler(iv.h.quads, symboltable);
                    	as.create();
                    	fop.flush();
            			fop.close();
                    }
                    
        			
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
                    	//fop.close();
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
