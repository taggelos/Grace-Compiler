import syntaxtree.*;
import visitor.*;
import symbol_table.*;
import java.io.*;

public class Main {
    public static void main (String [] args){
        if(args.length < 1){
            System.err.println("Usage: java Main <inputFile1> [<inputFile2>] ...");
            System.exit(1);
        }
        FileInputStream fis = null;

        for(int i = 0; i < args.length; i++) {
            try {
                fis = new FileInputStream(args[i]);
                MiniJavaParser parser = new MiniJavaParser(fis);
                Goal root = parser.Goal();
                System.out.println("Program " + args[i] + " parsed successfully.");
                ClassesVisitor classesvisit = new ClassesVisitor();
                try {
                    root.accept(classesvisit);
                    MethodsVisitor methodsvisit = new MethodsVisitor(classesvisit.classes);
                    root.accept(methodsvisit);
                    SymbolTable symboltable = methodsvisit.st;
                    TypeCheckVisitor typecheckvisit = new TypeCheckVisitor(symboltable);
                    root.accept(typecheckvisit, null);
                    System.out.println("Type Check Completed Successfully!\n");
                } 
                catch (Exception ex) {
                    System.out.println("Type Check Error: " + ex.getMessage() + "\n");
                }
            } 
            catch(ParseException ex) {
                System.err.println(ex.getMessage());
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
        }
    }
}
