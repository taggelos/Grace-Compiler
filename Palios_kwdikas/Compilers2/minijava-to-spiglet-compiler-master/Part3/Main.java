import syntaxtree.*;
import visitor.*;
import java.io.*;
import java.util.Vector;
import java.io.File;
import org.deri.iris.Configuration;
import org.deri.iris.KnowledgeBase;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.compiler.Parser;
import org.deri.iris.optimisations.magicsets.MagicSets;
import org.deri.iris.storage.IRelation;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    public static void main (String [] args){
        FileInputStream fis = null;
        for(int i=0; i< args.length; i++){
            try{
                // System.out.println("Checking file: "+args[i]);
                fis = new FileInputStream(args[i]);
                SpigletParser parser = new SpigletParser(fis);
                Goal root = parser.Goal();
                try{
                    
                    SpigletVisitor spigletVisitor = new SpigletVisitor();
                    root.accept(spigletVisitor,null);

                    String initialFilename = args[i].substring(0, args[i].lastIndexOf('.'));
                    String fileDirectory = "./"+initialFilename;
                    File dir = new File("./"+initialFilename);
                    dir.mkdir();
                    String factsDirectory = fileDirectory+"/facts";
                    dir = new File(factsDirectory);
                    dir.mkdir();

                    String instructionFile = factsDirectory+"/"+"instruction.iris";
                    PrintWriter writer = new PrintWriter(instructionFile, "UTF-8");
                    writer.print(spigletVisitor.getInstructions());
                    writer.close();

                    String varFile = factsDirectory+"/"+"var.iris";
                    writer = new PrintWriter(varFile, "UTF-8");
                    writer.print(spigletVisitor.getVars());
                    writer.close();

                    String nextFile = factsDirectory+"/"+"next.iris";
                    writer = new PrintWriter(nextFile, "UTF-8");
                    writer.print(spigletVisitor.getNexts());
                    writer.close();

                    String varMoveFile = factsDirectory+"/"+"varMove.iris";
                    writer = new PrintWriter(varMoveFile, "UTF-8");
                    writer.print(spigletVisitor.getVarMoves());
                    writer.close();

                    String constMoveFile = factsDirectory+"/"+"constMove.iris";
                    writer = new PrintWriter(constMoveFile, "UTF-8");
                    writer.print(spigletVisitor.getConstMoves());
                    writer.close();
                    
                    String varUseFile = factsDirectory+"/"+"varUse.iris";
                    writer = new PrintWriter(varUseFile, "UTF-8");
                    writer.print(spigletVisitor.getVarUses());
                    writer.close();

                    String varDefFile = factsDirectory+"/"+"varDef.iris";
                    writer = new PrintWriter(varDefFile, "UTF-8");
                    writer.print(spigletVisitor.getVarDefs());
                    writer.close();

                    // spigletVisitor.printInstructions();
                    // spigletVisitor.printVars();
                    // spigletVisitor.printVarUses();
                    // spigletVisitor.printVarDefs();
                    // spigletVisitor.printVarMoves();
                    // spigletVisitor.printConstMoves();
                    // spigletVisitor.printNexts();

                    ////////////////////////////////////////////////////////////////////////////

                    Parser iris_parser = new Parser();

                    final String projectDirectory = fileDirectory;
                    Map<IPredicate, IRelation> factMap = new HashMap<>();

                    /** The following loop -- given a project directory -- will list and read parse all fact files in its "/facts"
                     *  subdirectory. This allows you to have multiple .iris files with your program facts. For instance you can
                     *  have one file for each relation's facts as our examples show.
                     */
                    final File factsDirectory1 = new File(projectDirectory + "/facts");
                    if (factsDirectory1.isDirectory()) {
                        for (final File fileEntry : factsDirectory1.listFiles()) {

                            if (fileEntry.isDirectory())
                                System.out.println("Omitting directory " + fileEntry.getPath());

                            else {
                                Reader factsReader = new FileReader(fileEntry);
                                iris_parser.parse(factsReader);

                                // Retrieve the facts and put all of them in factMap
                                factMap.putAll(iris_parser.getFacts());
                            }
                        }
                    }
                    else {
                        System.err.println("Invalid facts directory path");
                        System.exit(-1);
                    }

                    File rulesFile = new File(projectDirectory + "/../rules.iris");
                    Reader rulesReader = new FileReader(rulesFile);

                    File queriesFile = new File(projectDirectory + "/../queries.iris");
                    Reader queriesReader = new FileReader(queriesFile);

                    // Parse rules file.
                    iris_parser.parse(rulesReader);
                    // Retrieve the rules from the parsed file.
                    List<IRule> rules = iris_parser.getRules();

                    // Parse queries file.
                    iris_parser.parse(queriesReader);
                    // Retrieve the queries from the parsed file.
                    List<IQuery> queries = iris_parser.getQueries();

                    // Create a default configuration.
                    Configuration configuration = new Configuration();

                    // Enable Magic Sets together with rule filtering.
                    configuration.programOptmimisers.add(new MagicSets());

                    // Create the knowledge base.
                    IKnowledgeBase knowledgeBase = new KnowledgeBase(factMap, rules, configuration);

                    // Evaluate all queries over the knowledge base.
                    for (IQuery query : queries) {
                        List<IVariable> variableBindings = new ArrayList<>();
                        IRelation relation = knowledgeBase.execute(query, variableBindings);

                        // Output the variables.
                        System.out.println("\n" + query.toString() + "\n" + variableBindings);

                        // Output each tuple in the relation, where the term at position i
                        // corresponds to the variable at position i in the variable
                        // bindings list.
                        for (i = 0; i < relation.size(); i++) {
                            System.out.println(relation.get(i));
                        }
                    }
                    ////////////////////////////////////////////////////////////////////////////











                } catch(Exception ex){
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

