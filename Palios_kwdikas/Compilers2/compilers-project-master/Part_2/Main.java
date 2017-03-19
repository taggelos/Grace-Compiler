import syntaxtree.*;
import visitor.*;
import symbol_table.*;
import type_check.*;
import java.io.*;

public class Main {
    public static void main (String [] args){
        if (args.length < 1){
            System.err.println("Usage: java Main <inputFile1> [<inputFile2>] ...");
            System.exit(1);
        }
        FileInputStream fis = null;
        int i = -1;
        while (++i < args.length) {
            try {
                fis = new FileInputStream(args[i]);
                MiniJavaParser parser = new MiniJavaParser(fis);
                Goal root = parser.Goal();
                System.out.println("_______________________________________________________________________________");
                System.out.println("Program " + args[i] + " parsed successfully.");
                FirstVisitor firstvisit = new FirstVisitor();
                try {
                    root.accept(firstvisit);
                    SecondVisitor secondvisit = new SecondVisitor(firstvisit.getClassList());
                    root.accept(secondvisit);
                    SymbolTable ST = secondvisit.getSymbolTable();
                    ST.printST(); 		// printing the symbol table
                    TypeCheckVisitor TCV = new TypeCheckVisitor(ST);
                    root.accept(TCV, null);
                    System.out.println("\nType Check Completed Successfully!\n");
                } catch (Exception ex) {
                    System.out.println("\n\nType Check Error: " + ex.getMessage() + "\n\n");
                }
            } catch(ParseException ex) {
                System.err.println(ex.getMessage());
            } catch(FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            } finally {
                try {
                    if (fis != null)
                        fis.close();
                } catch(IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
}
