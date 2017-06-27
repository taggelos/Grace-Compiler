package compiler;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.Set;

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
        FileOutputStream fop = null,fop2=null,fop3=null,fop4=null;
        File file,fileOpt,fileAssembly,fileAssemblyOpt;
        if(args.length < 1){
            System.err.println("Usage: java Main <inputFile1> [<inputFile2>] ...");
            System.exit(1);

        }

        for(int i = 0; i < args.length; i++) {
            try {
            	File fold = new File(args[i]);
                fis = new FileInputStream(args[i]);
                file = new File(fold.getParent()+"/../ir/", fold.getName().replace(".", "_")+"_ir.txt");
                fileOpt = new File(fold.getParent()+"/../iropt/", fold.getName().replace(".", "_")+"_irOpt.txt");
                fileAssembly = new File(fold.getParent()+"/../assemblycode/", fold.getName().replace(".", "_")+".s");
                fileAssemblyOpt = new File(fold.getParent()+"/../assemblycodeopt/", fold.getName().replace(".", "_")+"_Opt.s");

                
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

                    if(fv.errors.isEmpty()){
                    	fop = new FileOutputStream(file);
                    	fop2 = new FileOutputStream(fileOpt);
                    	fop3 = new FileOutputStream(fileAssembly);
                    	fop4 = new FileOutputStream(fileAssemblyOpt);
                    	if (!file.exists()) {
            				file.createNewFile();
            			}
                    	if (!fileOpt.exists()) {
                    		fileOpt.createNewFile();
            			}
                    	if (!fileAssembly.exists()) {
                    		fileAssembly.createNewFile();
            			}
                    	if (!fileAssemblyOpt.exists()) {
                    		fileAssemblyOpt.createNewFile();
            			}
                    	
                    	IrVisitor iv = new IrVisitor(symboltable);
                    	tree.apply(iv);
                    	IrVisitor2 iv2 = new IrVisitor2(iv.h.quads,symboltable);
                    	iv2.create();
                    	for(Quad q : iv.h.quads) {
                    		fop.write((q.printQuad()+'\n').getBytes());
                    	}
                    	for(Quad q : iv2.h2.quads) {
                    		fop2.write((q.printQuad()+'\n').getBytes());
                    	}
                    	
                    	Assembler as = new Assembler(iv.h.quads, symboltable);
                    	as.create();
                    	
                    	for(StringBuffer sb : as.output)
                    		fop3.write(sb.toString().getBytes());
                		
                		Set<String> keys = as.standards.code.keySet();
                		for(String i1: keys)
                		{
                			fop3.write((i1+":").toString().getBytes());
                			fop3.write(as.standards.code.get(i1).toString().getBytes());
                		}
                		fop3.write(("\n"+as.data+"\n").toString().getBytes());
                    	
        
                    	
                    	Assembler as2 = new Assembler(iv2.h2.quads, symboltable);
                    	as2.create();
                    	
                    	for(StringBuffer sb : as2.output)
                    		fop4.write(sb.toString().getBytes());
                		
                		Set<String> keys2 = as2.standards.code.keySet();
                		for(String i1: keys2)
                		{
                			fop4.write((i1+":").toString().getBytes());
                			fop4.write(as2.standards.code.get(i1).toString().getBytes());
                		}
                		fop4.write(("\n"+as2.data+"\n").toString().getBytes());
                  
                    	fop.flush();
            			fop.close();
                    	fop2.flush();
            			fop2.close();
            			fop3.flush();
            			fop3.close();
            			fop4.flush();
            			fop4.close();
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
                    if(fis != null && fop!=null && fop2!=null)
                        fis.close();
	                	//fop.close();
	                	//fop2.close();
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
