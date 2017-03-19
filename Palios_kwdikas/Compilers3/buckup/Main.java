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
                    SpigletVisitor spigletvisit = new SpigletVisitor(symboltable);
                    root.accept(spigletvisit, null);
                    File fp = new File(args[i]);
                    String path = fp.getPath();
                    path = path.substring(0, path.lastIndexOf('.'));
                    PrintWriter out = new PrintWriter(path + ".spg");
                    out.print(spigletvisit.output);
                    out.close();
		            System.out.println("Proccess finished successfully.");
                } 
                catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage() + "\n");
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
