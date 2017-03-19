package symbol_table;

import syntaxtree.*;
import visitor.GJNoArguDepthFirst;
import java.util.LinkedList;
import my_type.*;

/* 
    Visitor gia thn apo8hkeush twn klasewn se lista
*/

public class FirstVisitor extends GJNoArguDepthFirst<ClassType> {
    private LinkedList<ClassType> classes;        // Lista twn klasewn

    public FirstVisitor() { 
        classes = new LinkedList<ClassType>(); 
    }

    public LinkedList<ClassType> getClassList() { 
        return classes; 
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "ClassType"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
    public ClassType visit(MainClass n) throws Exception {
        ClassType id = new ClassType(n.f1.accept(this).getName(), null);
        classes.addLast(id);
        return null;
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
    public ClassType visit(ClassDeclaration n) throws Exception {
        String id = n.f1.accept(this).getName();
        for(int i = 0; i < classes.size(); i++) {
            if(classes.get(i).getName().equals(id))
                throw new Exception("Class " + id + " already exists!");
        }
        classes.addLast(new ClassType(id, null));
        return null;
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
    public ClassType visit(ClassExtendsDeclaration n) throws Exception {
        String id1 = n.f1.accept(this).getName();
        String id2 = n.f3.accept(this).getName();
        
        for(int i = 0; i < classes.size(); i++) {
            if(classes.get(i).getName().equals(id1))
                throw new Exception("Class " + id1 + " already exists!");
        }

        boolean flag = false;
        for(int j = 0; j < classes.size(); j++) {
            if(classes.get(j).getName().equals(id2))
                flag = true;
        }
        if(!flag)
            throw new Exception("Class extended before declared!");

        classes.addLast(new ClassType(id1, id2));
        return null;
    }

    /**
    * f0 -> <IDENTIFIER>
    */
    public ClassType visit(Identifier n) throws Exception {
        ClassType id = new ClassType(n.f0.toString(), null);
        return id;
    }

}
