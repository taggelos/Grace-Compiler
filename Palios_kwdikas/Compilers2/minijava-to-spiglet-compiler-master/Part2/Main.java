import syntaxtree.*;
import visitor.*;
import java.io.*;
import java.util.Vector;
import symboltable.*;


public class Main {
    public static void main (String [] args){
        FileInputStream fis = null;
        for(int i=0; i< args.length; i++){
            try{
                // System.out.println("Checking file: "+args[i]);
                fis = new FileInputStream(args[i]);
                MiniJavaParser parser = new MiniJavaParser(fis);
                Goal root = parser.Goal();
                // System.err.println("Program "+args[i]+" parsed successfully.");
                try{
                    SymbolTable symbolTable = new SymbolTable();
                    ClassColector firstVisitor = new ClassColector(symbolTable.classList);
                    root.accept(firstVisitor);
                    
                    SymbolTableBuilder secondVisitor = new SymbolTableBuilder(symbolTable);
                    root.accept(secondVisitor);
                    

                    // symbolTable.printClasses();

                    LoweringVisitor thirdVisitor = new LoweringVisitor(symbolTable);
                    root.accept(thirdVisitor,null);
                    

                    String finalCode = thirdVisitor.getFinalCode();
                    // System.out.println(finalCode);
                    String filename = args[i].substring(0, args[i].lastIndexOf('.'));
                    filename += ".spg";

                    PrintWriter writer = new PrintWriter(filename, "UTF-8");
                    writer.println(finalCode);
                    writer.close();
                    
                    // System.err.println("Program "+args[i]+" checked without any type errors.\n\n");
                } catch(Exception ex){
                    // System.err.println("Program "+args[i]+" isn't semantically correct.\n\t"+ex.getMessage()+"\n\n");
                    System.err.println("Error: "+ex.getMessage());
                }
            }
            catch(ParseException ex){
                System.out.println(ex.getMessage());
            }
            catch(FileNotFoundException ex){
                System.err.println(ex.getMessage());
            }
            finally{
                try{
                    if(fis != null) fis.close();
                }
                catch(IOException ex){
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
}
