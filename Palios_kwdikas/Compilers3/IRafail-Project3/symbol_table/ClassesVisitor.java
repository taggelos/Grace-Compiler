package symbol_table;

import syntaxtree.*;
import visitor.GJNoArguDepthFirst;
import java.util.*;
import types.*;

/* 
    Visitor gia thn apo8hkeush twn klasewn se lista
*/

public class ClassesVisitor extends GJNoArguDepthFirst<ClassType> {
    public LinkedList<ClassType> classes;        // Lista twn klasewn

    public ClassesVisitor() { 
        classes = new LinkedList<ClassType>(); 
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
        String name = n.f1.accept(this).name;
        ClassType id = new ClassType(name, null);
        classes.add(id);
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
        String id = n.f1.accept(this).name;
        ClassType c = new ClassType(id, null);
        classes.add(c);

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
        String id1 = n.f1.accept(this).name;
        String id2 = n.f3.accept(this).name;
        ClassType c = new ClassType(id1, id2);
        classes.add(c);

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
