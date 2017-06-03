package compiler.visitors;

import java.util.*;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;
import compiler.symboltable.*;

public class FunctionVisitor extends DepthFirstAdapter
{
	public static LinkedList<Method_t> methods;        // Lista twn klasewn
    public LinkedList<String> errors;
    public LinkedList<Method_t> fun_decs;
    public LinkedList<String> bools;
    Method_t from = null;
    Method_t current = null;
    boolean ret;
    
    HashMap<String, String> hm = new HashMap<String, String>();

    
    static String[] smethodnames = {
  		  "puti ", 
  		  "putc ", 
  		  "puts ", 
  		  "geti ", 
  		  "getc ", 
  		  "gets ", 
  		  "abs ",
  		  "ord ",
  		  "chr ",
  		  "strlen ",
  		  "strcmp ",
  		  "strcpy ",
  		  "strcat ",
  		  "writeString "
  		};
    static String[] smethodreturns = {
  		  "nothing", 
  		  "nothing", 
  		  "nothing",  
  		  "int", 
  		  "char", 
  		  "nothing", 
  		  "int",
  		  "int",
  		  "char",
  		  "int",
  		  "int",
  		  "nothing",
  		  "nothing",
  		  "nothing"
  		};
    static String[] smethodparams = {
  		  "int", 
  		  "char", 
  		  "ref char[]",  
  		  "", 
  		  "", 
  		  "int,ref char[]", 
  		  "int",
  		  "char",
  		  "int",
  		  "ref char[]",
  		  "ref char[],ref char[]",
  		  "ref char[],ref char[]",
  		  "ref char[],ref char[]",
  		  "ref char[]"
  		};
    

    public FunctionVisitor() { 
    	methods = new LinkedList<Method_t>(); 
    	errors = new LinkedList<String>(); 
    	fun_decs = new LinkedList<Method_t>(); 
    	bools = new LinkedList<String>(); 
    }
    
    public Variable_t getType(String var, Method_t meth) {
  
        String methodvar = meth.methContains(var);
        if(methodvar == null && meth.from != null) {             // An den brisketai se sunarthsh...
            String fromvar = meth.from.methContains(var);
            if(fromvar != null)            // An den brisketai oute sto from...  (+ oxi sto hm?)
                //errors.add("Undeclared Variable " + var);
            	methodvar = fromvar;
        }
        return new Variable_t(methodvar, var);

    } 
    
    public static Method_t CheckCall(String call_name, Method_t meth) {
    	if(meth.getName().equals(call_name))
			return meth;
		for(Method_t m : meth.methodList) {
			////System.out.println(m.getName() +" - "+ call_name);
			if(m.getName().equals(call_name))
				return m;
		}
		for(int i = 0; i<smethodnames.length; i++){
			String[] spl=null;
			if(call_name.equals(smethodnames[i])) {
				Method_t smeth = new Method_t(smethodreturns[i], smethodnames[i]);
				if(!smethodparams[i].isEmpty()) {
					spl = smethodparams[i].split(",");
					Variable_t t1 =null;
					for(int j=0; j< spl.length; j++){					
						if(spl[j].split(" ")[0].equals("ref")){
							t1 = new Variable_t(spl[j].split(" ")[1], "arg"+j);
							t1.setRef();							
						}
						else{
							t1 = new Variable_t(spl[j].split(" ")[0], "arg"+j);
						}
						smeth.addParam(t1);
					}
					
				}
				
				//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				smeth.printMethod();
				methods.add(smeth);
				//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				return smeth;
			}
		}
    	
    	return null;
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node); 
        node.getPProgram().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }

    @Override
    public void caseAProgram(AProgram node)
    {
        inAProgram(node);
        if(node.getFunDef() != null)
        {
            node.getFunDef().apply(this);
        } 
        Set set = hm.entrySet();
        Iterator i = set.iterator();
        
        // Display elements
        while(i.hasNext()) {
           Map.Entry me = (Map.Entry)i.next();
           //System.out.print(me.getKey() + ": ");
           //System.out.println(me.getValue());
        }
        outAProgram(node);        
        if(!methods.getLast().methodParams.isEmpty()){
        	errors.add("Function "+ methods.getLast().getName() + "shall not have arguments.");
        }
        if(!methods.getLast().get_return_type().equals("nothing")){
        	errors.add("Function "+methods.getLast().getName() + "shall return \"nothing\".");
        }
        for(String e : errors)
        	System.err.println(e);
    }

    @Override
    public void caseAFunDef(AFunDef node)
    {
        Method_t NewMethod = null;
        
        inAFunDef(node);
        if(node.getHeader() != null) {
            AHeader header = (AHeader) node.getHeader();
            String return_type, name;
            
            return_type = header.getReturnT().toString();
            name = header.getName().toString();
            int error_count = errors.size();
            
            for(Method_t m : methods)
            	if(m.getName().equals(name)) 
					errors.add("Method " + name + " already exists!");
            
            if(errors.size() == error_count) {
            	NewMethod = new Method_t(return_type.replaceAll(" ",""), name);
            	NewMethod.addFrom(from);
            	if(from != null)
            	from.addMethod(NewMethod);
            	from = NewMethod;
            	
            	for(PFparDef par : header.getPars()) {
            		for(TIdentifier id : ((AFparDef) par).getNames()) {
            			Variable_t NewVar = new Variable_t(((AFparDef) par).getTypes().toString().replaceAll(" ", ""), id.toString());
            			if(((AFparDef) par).getRef() != null)
            				NewVar.setRef();
            			if(!NewMethod.addParam(NewVar))
            				errors.add("Param " + id.toString() + " already exists!");
            		}
            	}
            }
            node.getHeader().apply(this);
            for(Method_t meth: fun_decs){
            	if(meth.getName().equals(name)){
            		if(meth.methodParams.size()!=NewMethod.methodParams.size()){
            			errors.add("Function Definition "+ meth.getName() + "does not match Function Declaration.");
            		}
            		else{
            			int count=0;
            			for(Variable_t var: meth.methodParams){
            				if(!var.getType().equals(NewMethod.methodParams.get(count).getType())){
            					errors.add("Function Definition of Parameter with type "+ var.getType() + " does not match type "+NewMethod.methodParams.get(count).getType()+".");
            				}
            				count++;
            			}
            		}
            	}
            }
        }
        {
        	current = NewMethod;
            List<PLocalDef> copy = new ArrayList<PLocalDef>(node.getLocal());
            for(PLocalDef e : copy) {
                ////System.out.println("out of loop -- "  +e);
                if(e instanceof AVarLocalDef){
                    AVarLocalDef a= (AVarLocalDef) e;
                    AVarDef n = (AVarDef) a.getVarDef();
                    for(TIdentifier id : n.getName()) {
                    	if(!NewMethod.addVar(new Variable_t(n.getType().toString().replaceAll(" ", ""), id.toString()))){
                            errors.add("Variable " + id + " already exists!");
                        }
                    }
                    ////System.out.println("inside AVarDef -- " + n);                       
                }
                else if(e instanceof AFunLocalDef){
                	
                }
                e.apply(this);
             }      
        }
        {
        	ret = false;
        	current = NewMethod;
            List<PStmt> copy = new ArrayList<PStmt>(node.getBlock());
            for(PStmt e : copy)
            {
                e.apply(this);
                if(e instanceof AReturnstmtStmt) {
                	////System.out.println("BLOCK_RETURN: "+e);
                	//System.out.println(((AReturnstmtStmt) e).getReturnexpr()+"<---------");
                	if(((AReturnstmtStmt) e).getReturnexpr() != null) ret = true;
                }
            }
            if(!current.get_return_type().equals("nothing") && !ret) {
            	errors.add("Expecting return statment for method "+current.getName()+".");
        	}
            from = NewMethod.from;
        }
        NewMethod.printMethod();
        methods.add(NewMethod);
        outAFunDef(node);
    }

    @Override
    public void caseAHeader(AHeader node)
    {
        inAHeader(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        {
            List<PFparDef> copy = new ArrayList<PFparDef>(node.getPars());
            for(PFparDef e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getReturnT() != null)
        {
            node.getReturnT().apply(this);
        }
        outAHeader(node);
    }

    @Override
    public void caseAFparDef(AFparDef node)
    {
        inAFparDef(node);
        if(node.getRef() != null)
        {
            node.getRef().apply(this);
        }
        {
            List<TIdentifier> copy = new ArrayList<TIdentifier>(node.getNames());
            for(TIdentifier e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getTypes() != null)
        {
            node.getTypes().apply(this);
        }
        outAFparDef(node);
    }

    @Override
    public void caseAFunLocalDef(AFunLocalDef node)
    {
        inAFunLocalDef(node);
        if(node.getFunDef() != null)
        {
            node.getFunDef().apply(this);
        }
        outAFunLocalDef(node);
    }

    @Override
    public void caseADecLocalDef(ADecLocalDef node)
    {
        inADecLocalDef(node);
        if(node.getFunDec() != null)
        {
            node.getFunDec().apply(this);
        }
        outADecLocalDef(node);
    }

    @Override
    public void caseAVarLocalDef(AVarLocalDef node)
    {
        inAVarLocalDef(node);
        if(node.getVarDef() != null)
        {
            node.getVarDef().apply(this);
        }
        outAVarLocalDef(node);
    }

    @Override
    public void caseAFunDec(AFunDec node)
    {
    	Method_t NewMethod =null;
        inAFunDec(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }        
        AHeader header = (AHeader) node.getHeader();
        String return_type, name;
        
        return_type = header.getReturnT().toString();
        name = header.getName().toString();
        int error_count = errors.size();
        
        for(Method_t m : methods)
			if(m.getName().equals(name)) 
				errors.add("Method " + name + " already exists!");
        
        if(errors.size() == error_count) {
        	NewMethod = new Method_t(return_type.replaceAll(" ",""), name);        	
        	for(PFparDef par : header.getPars()) {
        		for(TIdentifier id : ((AFparDef) par).getNames()) {
        			if(!NewMethod.addParam(new Variable_t(((AFparDef) par).getTypes().toString().replaceAll(" ", ""), id.toString())))
        				errors.add("Param " + id.toString() + " already exists!");
        		}
        	}
        }
        for(Method_t meth: methods){
        	//System.out.println(meth.getName()+ "345345435");
        	if(current.getName().equals(name)){
        		errors.add("Method Already defined!");
        		return;
        	}
        }
        for(Method_t fun: fun_decs){
        	if(fun.getName().equals(name)){
        		errors.add("Method Already declared!");
        		return;
        	}
        }
        fun_decs.add(NewMethod);
        NewMethod.printMethod();
        ////System.out.println(NewMethod.getName()+ "435545453!!");
        outAFunDec(node);
    }

    @Override
    public void caseAIntDataTypes(AIntDataTypes node)
    {
        inAIntDataTypes(node);
        if(node.getInt() != null)
        {
            node.getInt().apply(this);
        }
        outAIntDataTypes(node);
    }

    @Override
    public void caseACharDataTypes(ACharDataTypes node)
    {
        inACharDataTypes(node);
        if(node.getChar() != null)
        {
            node.getChar().apply(this);
        }
        outACharDataTypes(node);
    }

    @Override
    public void caseABracketsArrayTypes(ABracketsArrayTypes node)
    {
        inABracketsArrayTypes(node);
        if(node.getLBr() != null)
        {
            node.getLBr().apply(this);
        }
        if(node.getIntegers() != null)
        {
            node.getIntegers().apply(this);
        }
        if(node.getRBr() != null)
        {
            node.getRBr().apply(this);
        }
        outABracketsArrayTypes(node);
    }

    @Override
    public void caseASimpleTypes(ASimpleTypes node)
    {
        inASimpleTypes(node);
        if(node.getDataTypes() != null)
        {
            node.getDataTypes().apply(this);
        }
        outASimpleTypes(node);
    }

    @Override
    public void caseAArrayTypes(AArrayTypes node)
    {
        inAArrayTypes(node);
        if(node.getDataTypes() != null)
        {
            node.getDataTypes().apply(this);
        }
        {
            List<PArrayTypes> copy = new ArrayList<PArrayTypes>(node.getArrayTypes());
            for(PArrayTypes e : copy)
            {
                e.apply(this);
            }
        }
        outAArrayTypes(node);
    }

    @Override
    public void caseASimpleReturnType(ASimpleReturnType node)
    {
        inASimpleReturnType(node);
        if(node.getDataTypes() != null)
        {
            node.getDataTypes().apply(this);
        }
        outASimpleReturnType(node);
    }

    @Override
    public void caseANoneReturnType(ANoneReturnType node)
    {
        inANoneReturnType(node);
        if(node.getNothing() != null)
        {
            node.getNothing().apply(this);
        }
        outANoneReturnType(node);
    }

    @Override
    public void caseAVarDef(AVarDef node)
    {
        inAVarDef(node);
        {
            List<TIdentifier> copy = new ArrayList<TIdentifier>(node.getName());
            for(TIdentifier e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        outAVarDef(node);
    }

    @Override
    public void caseAFunCal(AFunCal node)
    {
        inAFunCal(node);
        Method_t m = null;
        String name = null;
        if(node.getName() != null)
        {
        	name = node.getName().toString();
        	//System.out.println("\tMETHOD IS: "+ current.getName());
        	//System.out.println("\tCALL IS: "+ name);
        	m = CheckCall(name, current);
        	
        	if(m == null) {
        		errors.add("Method " + name + " doesn't exist!");
        		return;
        	}
        	
            node.getName().apply(this);
        }
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExprs());
            if(copy.size() != m.methodParams.size()) {
            	errors.add("Invalid number of parameters for method " + name);
            	return;
            }
            int count = 0;
            String val;
            for(PExpr e : copy) {
            	//System.out.println("-------------------------------------------");
                e.apply(this);
                val = e.toString();
                val = val.replaceAll("- ", "");
                val = val.replaceAll("\\+ ", "");
            	//System.out.println("EXPR--: "+ e.toString());
            	if(!hm.isEmpty()) {
            		if(!hm.containsKey(val))
            			errors.add("Invalid parameter of method " + name+"."+val);
            		//else if(hm.get(e.toString()) == null)
            		//		errors.add("Invalid parameter type.");
            		else if(hm.get(val) == null || !hm.get(val).equals(m.methodParams.get(count).getType().replaceAll("[0123456789 ]", "")))
            			errors.add("Invalid parameter type " + hm.get(val) + ". Expecting " + m.methodParams.get(count).getType()+".");
            	}
            	count++;
            }
        }
        hm.put(node.toString(), m.get_return_type());
        outAFunCal(node);
    }

    @Override
    public void caseAIdLVal(AIdLVal node)
    {
        inAIdLVal(node);
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        Variable_t t = getType(node.toString(), current); 
        if(t.getType() == null) {
			errors.add("Variable " + node.toString() + "not declared in method " + current.getName()+".");
			return;
		}
        ////System.out.println("LVAL ID: "+node.toString());
        ////System.out.println("ADDING: "+node.toString()+" "+ t.getType().replaceAll("[1234567890 ]", ""));
        hm.put(node.toString(), t.getType().replaceAll("[1234567890 ]", ""));
        outAIdLVal(node);
    }

    @Override
    public void caseAStringLVal(AStringLVal node)
    {
        inAStringLVal(node);
        if(node.getStringLiteral() != null)
        {
            node.getStringLiteral().apply(this);
        }
        hm.put(node.toString(), "char[]");
        outAStringLVal(node);
    }

    @Override
    public void caseAIdBracketsLVal(AIdBracketsLVal node)
    {
        inAIdBracketsLVal(node);
        if(node.getLVal() != null)
        {
            node.getLVal().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
            if(!(node.getExpr() instanceof AIntExpr)) {
            	if(!hm.get(node.getExpr().toString()).equals("int")) {
            		errors.add("Invalid array input. Integer expected.");
            		return;
            	}
            }
        }
        int i = 0, j=0;
        String index = node.getExpr().toString();
        String name = node.getLVal().toString()+"[ "+index+"] ";
        for(int z=0; z<name.length(); z++) {
        	if(name.charAt(z) == '[')
        		i++;
        }
        for(int z=0; z<index.length(); z++) {
        	if(index.charAt(z) == '[')
        		i--;
        }
        
        Variable_t t = getType(node.toString().split(" ")[0]+" ", current);
        
    	if(t.getType() == null) {
			//errors.add("Variable " + node.toString().split(" ")[0] + "not declared in method " + current.getName()+".");
			return;
		}
    	//System.err.println(current.getName());
        String type= t.getType();
        type = type.replaceAll("[0123456789 ]", "");
        
        for(int z=0; z<type.length(); z++) {
        	if(type.charAt(z) == '[')
        		j++;
        }
        if(i>j)
        	errors.add("Invalid array type. "+type +" expected.");
        
        for(j=0; j<i; j++) {
        	type = type.replaceFirst("\\[", "");
        	type = type.replaceFirst("]", "");
        	//errors.add(type);
        }

        //errors.add(name+" -- "+ type+" i: "+i);
        hm.put(name, type);
        outAIdBracketsLVal(node);
    }

    @Override
    public void caseAIfHeader(AIfHeader node)
    {
        inAIfHeader(node);
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        outAIfHeader(node);
    }

    @Override
    public void caseANoElseIfTrail(ANoElseIfTrail node)
    {
        inANoElseIfTrail(node);
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getThen());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outANoElseIfTrail(node);
    }

    @Override
    public void caseAWithElseIfTrail(AWithElseIfTrail node)
    {
        inAWithElseIfTrail(node);
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getThen());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getElseSt());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outAWithElseIfTrail(node);
    }

    @Override
    public void caseAAndExprExpr(AAndExprExpr node)
    {
        inAAndExprExpr(node);
        String type = "";
        Variable_t t = null;
        String val;
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
            /*val = node.getLeft().toString();
            for(String b: bools){
            	
            	}*/
         }
          
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        ////System.out.println(node.getLeft() + " -- " + node.getRight());
        bools.add(node.toString());
        //errors.add(bools.toString());
        outAAndExprExpr(node);
    }

    @Override
    public void caseAOrExprExpr(AOrExprExpr node)
    {
        inAOrExprExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAOrExprExpr(node);
    }

    @Override
    public void caseANotExprExpr(ANotExprExpr node)
    {
        inANotExprExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outANotExprExpr(node);
    }

    @Override
    public void caseAParExpr(AParExpr node)
    {
        inAParExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outAParExpr(node);
    }

    @Override
    public void caseALessThanExpr(ALessThanExpr node)
    {
        inALessThanExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        String typel = null,typer = null;
        Variable_t varl = null, varr = null;
        
        varl = getType(node.getLeft().toString(), current);
        varr = getType(node.getRight().toString(), current);
        
        if(varl.getType() == null)
        	typel = hm.get(node.getLeft().toString());
        else
        	typel = varl.getType();
        
        if(varr.getType() == null)
        	typer = hm.get(node.getRight().toString());
        else
        	typer = varr.getType();
        if(typel!=null && typer!=null && !typel.equals(typer)){
        	errors.add("Less-Than Expression of "+ node.getLeft().toString() +
        			" does not match type \""+ typel +"\". Type \""+typer+"\" found.");
        	return;
        }
        bools.add(node.toString());
        outALessThanExpr(node);
    }

    @Override
    public void caseAGreaterThanExpr(AGreaterThanExpr node)
    {
        inAGreaterThanExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        String typel = null,typer = null;
        Variable_t varl = null, varr = null;
        
        varl = getType(node.getLeft().toString(), current);
        varr = getType(node.getRight().toString(), current);
        
        if(varl.getType() == null)
        	typel = hm.get(node.getLeft().toString());
        else
        	typel = varl.getType();
        
        if(varr.getType() == null)
        	typer = hm.get(node.getRight().toString());
        else
        	typer = varr.getType();

        if(typel!=null && typer!=null && !typel.equals(typer)){
        	errors.add("Less-Than Expression of "+ node.getLeft().toString() +" does not match type \""+ typel +"\". Type \""+typer+"\" found.");
        	return;
        }
        bools.add(node.toString());
        outAGreaterThanExpr(node);
    }

    @Override
    public void caseAGreaterEqualThanExpr(AGreaterEqualThanExpr node)
    {
        inAGreaterEqualThanExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        String typel = null,typer = null;
        Variable_t varl = null, varr = null;
        
        varl = getType(node.getLeft().toString(), current);
        varr = getType(node.getRight().toString(), current);
        
        if(varl.getType() == null)
        	typel = hm.get(node.getLeft().toString());
        else
        	typel = varl.getType();
        
        if(varr.getType() == null)
        	typer = hm.get(node.getRight().toString());
        else
        	typer = varr.getType();

        if(typel!=null && typer!=null && !typel.equals(typer)){
        	errors.add("Less-Than Expression of "+ node.getLeft().toString() +" does not match type \""+ typel +"\". Type \""+typer+"\" found.");
        	return;
        }
        bools.add(node.toString());
        outAGreaterEqualThanExpr(node);
    }

    @Override
    public void caseALessEqualThanExpr(ALessEqualThanExpr node)
    {
        inALessEqualThanExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        String typel = null,typer = null;
        Variable_t varl = null, varr = null;
        
        varl = getType(node.getLeft().toString(), current);
        varr = getType(node.getRight().toString(), current);
        
        if(varl.getType() == null)
        	typel = hm.get(node.getLeft().toString());
        else
        	typel = varl.getType();
        
        if(varr.getType() == null)
        	typer = hm.get(node.getRight().toString());
        else
        	typer = varr.getType();

        if(typel!=null && typer!=null && !typel.equals(typer)){
        	errors.add("Less-Than Expression of "+ node.getLeft().toString() +" does not match type \""+ typel +"\". Type \""+typer+"\" found.");
        	return;
        }
        bools.add(node.toString());
        outALessEqualThanExpr(node);
    }

    @Override
    public void caseAEqualExpr(AEqualExpr node)
    {
        inAEqualExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        String typel = null,typer = null;
        Variable_t varl = null, varr = null;
        
        varl = getType(node.getLeft().toString(), current);
        varr = getType(node.getRight().toString(), current);
        
        if(varl.getType() == null)
        	typel = hm.get(node.getLeft().toString());
        else
        	typel = varl.getType();
        
        if(varr.getType() == null)
        	typer = hm.get(node.getRight().toString());
        else
        	typer = varr.getType();

        if(typel!=null && typer!=null && !typel.equals(typer)){
        	errors.add("Less-Than Expression of "+ node.getLeft().toString() +" does not match type \""+ typel +"\". Type \""+typer+"\" found.");
        	return;
        }
        bools.add(node.toString());
        outAEqualExpr(node);
    }

    @Override
    public void caseANotEqualExpr(ANotEqualExpr node)
    {
        inANotEqualExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        String typel = null,typer = null;
        Variable_t varl = null, varr = null;
        
        varl = getType(node.getLeft().toString(), current);
        varr = getType(node.getRight().toString(), current);
        
        if(varl.getType() == null)
        	typel = hm.get(node.getLeft().toString());
        else
        	typel = varl.getType();
        
        if(varr.getType() == null)
        	typer = hm.get(node.getRight().toString());
        else
        	typer = varr.getType();

        if(typel!=null && typer!=null && !typel.equals(typer)){
        	errors.add("Less-Than Expression of "+ node.getLeft().toString() +" does not match type \""+ typel +"\". Type \""+typer+"\" found.");
        	return;
        }
        bools.add(node.toString());
        outANotEqualExpr(node);
    }
    
    @Override
    public void caseAAddExpr(AAddExpr node)
    {
        inAAddExpr(node);
        String type1 = "int", type2= "int";
        Variable_t t = null;
        String val;
        if(node.getExpr1() != null) {
            node.getExpr1().apply(this);
            val = node.getExpr1().toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //errors.add(val);
            if(!hm.containsKey(val)) {
            	if(node.getExpr1() instanceof ALValExpr) {
            		ALValExpr lval = (ALValExpr) node.getExpr1();
            		if(lval.getLVal() instanceof AIdBracketsLVal) {
            			////System.out.println("--->"+lval.getLVal());
            			val = val.split(" ")[0]+" ";
            			////System.out.println("------>"+val);
            		}
            		//System.out.println(val + " Method: " + current.getName());
            		t = getType(val, current);
            		//System.out.println(t.getType());
            		if(t.getType() == null) {
            			errors.add("Variable " + val + "not declared in method " + current.getName()+".");
            			type1 = null;
            			return;
            		}
            		else if(!t.getType().equals("int")) {
            			errors.add("Invalid add expression type.");
            			type1 = null;
            			return;
            		}
            		
            	}
            	else if(node.getExpr1() instanceof AIntExpr) {
            		////System.out.println(node.getExpr1());
            		type1 = "int";
            	}
            	else {
            		
            		errors.add("Invalid add expression type.");
            		return;
            	}
            }
            else
            	type1 = hm.get(val);
        }
        if(node.getExpr2() != null) {
        	node.getExpr2().apply(this);
            val = node.getExpr2().toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //errors.add(val);
            if(!hm.containsKey(val)) {
            	if(node.getExpr2() instanceof ALValExpr) {
            		ALValExpr lval = (ALValExpr) node.getExpr2();
            		if(lval.getLVal() instanceof AIdBracketsLVal) {
            			////System.out.println("--->"+lval.getLVal());
            			val = val.split(" ")[0]+" ";
            			////System.out.println("------>"+val);
            			
            		}
            		//System.out.println(val + " Method: " + current.getName());
            		t = getType(val, current);
            		errors.add(t.getType());
            		if(t.getType() == null) {
            			errors.add("Variable " + val + "not declared in method " + current.getName()+".");
            			type2 = null;
            			return;
            		}
            		else if(!t.getType().equals("int")) {
            			errors.add("Invalid add expression type.");
            			type2 = null;
            			return;
            		}
            		
            	}
            	else if(node.getExpr2() instanceof AIntExpr) {
            		////System.out.println(node.getExpr2());
            		type2 = "int";
            	}
            	else {
            		errors.add("Invalid add expression type.");
            		type2 = null;
            	}
            	
            }
            else
            	type2 = hm.get(val);
        }
        
        if(type1.equals("int") && type2.equals("int")) {
	        //System.out.println(node.getExpr1() + " -- " + node.getExpr2());
	        //errors.add(node.toString() +" withtype " + type2);
	        hm.put(node.toString(), type2);
	        //System.out.println("NODE: "+node.toString());
        }
        else {
        	errors.add("Invalid add expression types "+type1+ " and " + type2+".");
        }
        outAAddExpr(node);
    }

    @Override
    public void caseASubExpr(ASubExpr node)
    {
        inASubExpr(node);
        String type1 = "int", type2= "int";
        Variable_t t = null;
        String val;
        if(node.getExpr1() != null) {
            node.getExpr1().apply(this);
            val = node.getExpr1().toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //errors.add(val);
            if(!hm.containsKey(val)) {
            	if(node.getExpr1() instanceof ALValExpr) {
            		ALValExpr lval = (ALValExpr) node.getExpr1();
            		if(lval.getLVal() instanceof AIdBracketsLVal) {
            			////System.out.println("--->"+lval.getLVal());
            			val = val.split(" ")[0]+" ";
            			////System.out.println("------>"+val);
            		}
            		//System.out.println(val + " Method: " + current.getName());
            		t = getType(val, current);
            		//System.out.println(t.getType());
            		if(t.getType() == null) {
            			errors.add("Variable " + val + "not declared in method " + current.getName()+".");
            			type1 = null;
            			return;
            		}
            		else if(!t.getType().equals("int")) {
            			errors.add("Invalid sub expression type.");
            			type1 = null;
            			return;
            		}
            		
            	}
            	else if(node.getExpr1() instanceof AIntExpr) {
            		////System.out.println(node.getExpr1());
            		type1 = "int";
            	}
            	else {
            		
            		errors.add("Invalid sub expression type.");
            		return;
            	}
            }
            else
            	type1 = hm.get(val);
        }
        if(node.getExpr2() != null) {
        	node.getExpr2().apply(this);
            val = node.getExpr2().toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //errors.add(val);
            if(!hm.containsKey(val)) {
            	if(node.getExpr2() instanceof ALValExpr) {
            		ALValExpr lval = (ALValExpr) node.getExpr2();
            		if(lval.getLVal() instanceof AIdBracketsLVal) {
            			////System.out.println("--->"+lval.getLVal());
            			val = val.split(" ")[0]+" ";
            			////System.out.println("------>"+val);
            			
            		}
            		//System.out.println(val + " Method: " + current.getName());
            		t = getType(val, current);
            		errors.add(t.getType());
            		if(t.getType() == null) {
            			errors.add("Variable " + val + "not declared in method " + current.getName()+".");
            			type2 = null;
            			return;
            		}
            		else if(!t.getType().equals("int")) {
            			errors.add("Invalid sub expression type.");
            			type2 = null;
            			return;
            		}
            		
            	}
            	else if(node.getExpr2() instanceof AIntExpr) {
            		////System.out.println(node.getExpr2());
            		type2 = "int";
            	}
            	else {
            		errors.add("Invalid sub expression type.");
            		type2 = null;
            	}
            	
            }
            else
            	type2 = hm.get(val);
        }
        System.err.println(hm);
        if(type1.equals("int") && type2.equals("int")) {
	        //System.out.println(node.getExpr1() + " -- " + node.getExpr2());
	        //errors.add(node.toString() +" withtype " + type2);
	        hm.put(node.toString(), type2);
	        //System.out.println("NODE: "+node.toString());
        }
        else {
        	errors.add("Invalid sub expression types "+type1+ " and " + type2+".");
        }
        outASubExpr(node);
    }

    @Override
    public void caseAMultExpr(AMultExpr node)
    {
        inAMultExpr(node);
        String type1 = "int", type2= "int";
        Variable_t t = null;
        String val;
        if(node.getExpr1() != null) {
            node.getExpr1().apply(this);
            val = node.getExpr1().toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //errors.add(val);
            if(!hm.containsKey(val)) {
            	if(node.getExpr1() instanceof ALValExpr) {
            		ALValExpr lval = (ALValExpr) node.getExpr1();
            		if(lval.getLVal() instanceof AIdBracketsLVal) {
            			////System.out.println("--->"+lval.getLVal());
            			val = val.split(" ")[0]+" ";
            			////System.out.println("------>"+val);
            		}
            		//System.out.println(val + " Method: " + current.getName());
            		t = getType(val, current);
            		//System.out.println(t.getType());
            		if(t.getType() == null) {
            			errors.add("Variable " + val + "not declared in method " + current.getName()+".");
            			type1 = null;
            			return;
            		}
            		else if(!t.getType().equals("int")) {
            			errors.add("Invalid mult expression type.");
            			type1 = null;
            			return;
            		}
            		
            	}
            	else if(node.getExpr1() instanceof AIntExpr) {
            		////System.out.println(node.getExpr1());
            		type1 = "int";
            	}
            	else {
            		
            		errors.add("Invalid mult expression type.");
            		return;
            	}
            }
            else
            	type1 = hm.get(val);
        }
        if(node.getExpr2() != null) {
        	node.getExpr2().apply(this);
            val = node.getExpr2().toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //errors.add(val);
            if(!hm.containsKey(val)) {
            	if(node.getExpr2() instanceof ALValExpr) {
            		ALValExpr lval = (ALValExpr) node.getExpr2();
            		if(lval.getLVal() instanceof AIdBracketsLVal) {
            			////System.out.println("--->"+lval.getLVal());
            			val = val.split(" ")[0]+" ";
            			////System.out.println("------>"+val);
            			
            		}
            		//System.out.println(val + " Method: " + current.getName());
            		t = getType(val, current);
            		errors.add(t.getType());
            		if(t.getType() == null) {
            			errors.add("Variable " + val + "not declared in method " + current.getName()+".");
            			type2 = null;
            			return;
            		}
            		else if(!t.getType().equals("int")) {
            			errors.add("Invalid mult expression type.");
            			type2 = null;
            			return;
            		}
            		
            	}
            	else if(node.getExpr2() instanceof AIntExpr) {
            		////System.out.println(node.getExpr2());
            		type2 = "int";
            	}
            	else {
            		errors.add("Invalid mult expression type.");
            		type2 = null;
            	}
            	
            }
            else
            	type2 = hm.get(val);
        }
        
        if(type1.equals("int") && type2.equals("int")) {
	        //System.out.println(node.getExpr1() + " -- " + node.getExpr2());
	        //errors.add(node.toString() +" withtype " + type2);
	        hm.put(node.toString(), type2);
	        //System.out.println("NODE: "+node.toString());
        }
        else {
        	errors.add("Invalid mult expression types "+type1+ " and " + type2+".");
        }
        outAMultExpr(node);
    }

    @Override
    public void caseAModExpr(AModExpr node)
    {
        inAModExpr(node);
        String type1 = "int", type2= "int";
        Variable_t t = null;
        String val;
        if(node.getExpr1() != null) {
            node.getExpr1().apply(this);
            val = node.getExpr1().toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //errors.add(val);
            if(!hm.containsKey(val)) {
            	if(node.getExpr1() instanceof ALValExpr) {
            		ALValExpr lval = (ALValExpr) node.getExpr1();
            		if(lval.getLVal() instanceof AIdBracketsLVal) {
            			////System.out.println("--->"+lval.getLVal());
            			val = val.split(" ")[0]+" ";
            			////System.out.println("------>"+val);
            		}
            		//System.out.println(val + " Method: " + current.getName());
            		t = getType(val, current);
            		//System.out.println(t.getType());
            		if(t.getType() == null) {
            			errors.add("Variable " + val + "not declared in method " + current.getName()+".");
            			type1 = null;
            			return;
            		}
            		else if(!t.getType().equals("int")) {
            			errors.add("Invalid mod expression type.");
            			type1 = null;
            			return;
            		}
            		
            	}
            	else if(node.getExpr1() instanceof AIntExpr) {
            		////System.out.println(node.getExpr1());
            		type1 = "int";
            	}
            	else {
            		
            		errors.add("Invalid mod expression type.");
            		return;
            	}
            }
            else
            	type1 = hm.get(val);
        }
        if(node.getExpr2() != null) {
        	node.getExpr2().apply(this);
            val = node.getExpr2().toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //errors.add(val);
            if(!hm.containsKey(val)) {
            	if(node.getExpr2() instanceof ALValExpr) {
            		ALValExpr lval = (ALValExpr) node.getExpr2();
            		if(lval.getLVal() instanceof AIdBracketsLVal) {
            			////System.out.println("--->"+lval.getLVal());
            			val = val.split(" ")[0]+" ";
            			////System.out.println("------>"+val);
            			
            		}
            		//System.out.println(val + " Method: " + current.getName());
            		t = getType(val, current);
            		errors.add(t.getType());
            		if(t.getType() == null) {
            			errors.add("Variable " + val + "not declared in method " + current.getName()+".");
            			type2 = null;
            			return;
            		}
            		else if(!t.getType().equals("int")) {
            			errors.add("Invalid mod expression type.");
            			type2 = null;
            			return;
            		}
            		
            	}
            	else if(node.getExpr2() instanceof AIntExpr) {
            		////System.out.println(node.getExpr2());
            		type2 = "int";
            	}
            	else {
            		errors.add("Invalid mod expression type.");
            		type2 = null;
            	}
            	
            }
            else
            	type2 = hm.get(val);
        }
        
        if(type1.equals("int") && type2.equals("int")) {
	        //System.out.println(node.getExpr1() + " -- " + node.getExpr2());
	        //errors.add(node.toString() +" withtype " + type2);
	        hm.put(node.toString(), type2);
	        //System.out.println("NODE: "+node.toString());
        }
        else {
        	errors.add("Invalid mod expression types "+type1+ " and " + type2+".");
        }
        outAModExpr(node);
    }

    @Override
    public void caseADivExpr(ADivExpr node)
    {
        inADivExpr(node);
        String type1 = "int", type2= "int";
        Variable_t t = null;
        String val;
        if(node.getExpr1() != null) {
            node.getExpr1().apply(this);
            val = node.getExpr1().toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //errors.add(val);
            if(!hm.containsKey(val)) {
            	if(node.getExpr1() instanceof ALValExpr) {
            		ALValExpr lval = (ALValExpr) node.getExpr1();
            		if(lval.getLVal() instanceof AIdBracketsLVal) {
            			////System.out.println("--->"+lval.getLVal());
            			val = val.split(" ")[0]+" ";
            			////System.out.println("------>"+val);
            		}
            		//System.out.println(val + " Method: " + current.getName());
            		t = getType(val, current);
            		//System.out.println(t.getType());
            		if(t.getType() == null) {
            			errors.add("Variable " + val + "not declared in method " + current.getName()+".");
            			type1 = null;
            			return;
            		}
            		else if(!t.getType().equals("int")) {
            			errors.add("Invalid div expression type.");
            			type1 = null;
            			return;
            		}
            		
            	}
            	else if(node.getExpr1() instanceof AIntExpr) {
            		////System.out.println(node.getExpr1());
            		type1 = "int";
            	}
            	else {
            		
            		errors.add("Invalid div expression type.");
            		return;
            	}
            }
            else
            	type1 = hm.get(val);
        }
        if(node.getExpr2() != null) {
        	node.getExpr2().apply(this);
            val = node.getExpr2().toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //errors.add(val);
            if(!hm.containsKey(val)) {
            	if(node.getExpr2() instanceof ALValExpr) {
            		ALValExpr lval = (ALValExpr) node.getExpr2();
            		if(lval.getLVal() instanceof AIdBracketsLVal) {
            			////System.out.println("--->"+lval.getLVal());
            			val = val.split(" ")[0]+" ";
            			////System.out.println("------>"+val);
            			
            		}
            		//System.out.println(val + " Method: " + current.getName());
            		t = getType(val, current);
            		errors.add(t.getType());
            		if(t.getType() == null) {
            			errors.add("Variable " + val + "not declared in method " + current.getName()+".");
            			type2 = null;
            			return;
            		}
            		else if(!t.getType().equals("int")) {
            			errors.add("Invalid div expression type.");
            			type2 = null;
            			return;
            		}
            		
            	}
            	else if(node.getExpr2() instanceof AIntExpr) {
            		////System.out.println(node.getExpr2());
            		type2 = "int";
            	}
            	else {
            		errors.add("Invalid div expression type.");
            		type2 = null;
            	}
            	
            }
            else
            	type2 = hm.get(val);
        }
        
        if(type1.equals("int") && type2.equals("int")) {
	        //System.out.println(node.getExpr1() + " -- " + node.getExpr2());
	        //errors.add(node.toString() +" withtype " + type2);
	        hm.put(node.toString(), type2);
	        //System.out.println("NODE: "+node.toString());
        }
        else {
        	errors.add("Invalid div expression types "+type1+ " and " + type2+".");
        }
        outADivExpr(node);
    }

    @Override
    public void caseAPlusOrMinusExpr(APlusOrMinusExpr node)
    {
        inAPlusOrMinusExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outAPlusOrMinusExpr(node);
    }

    @Override
    public void caseAIntExpr(AIntExpr node)
    {
        inAIntExpr(node);
        if(node.getIntegers() != null)
        {
            node.getIntegers().apply(this);
        }
        hm.put(node.toString(), "int");
        outAIntExpr(node);
    }

    @Override
    public void caseACharExpr(ACharExpr node)
    {
        inACharExpr(node);
        if(node.getCharConst() != null)
        {
            node.getCharConst().apply(this);
        }
        hm.put(node.toString(), "char");
        outACharExpr(node);
    }

    @Override
    public void caseALValExpr(ALValExpr node)
    {
        inALValExpr(node);
        if(node.getLVal() != null)
        {
            node.getLVal().apply(this);
        }
        outALValExpr(node);
    }

    @Override
    public void caseAFunCalExpr(AFunCalExpr node)
    {
        inAFunCalExpr(node);
        if(node.getFunCal() != null)
        {
            node.getFunCal().apply(this);
        }
        
        outAFunCalExpr(node);
    }

    @Override
    public void caseAReturnstmtExpr(AReturnstmtExpr node)
    {
        inAReturnstmtExpr(node);
        if(node.getReturnexpr() != null)
        {
            node.getReturnexpr().apply(this);
        }
        outAReturnstmtExpr(node);
    }

    @Override
    public void caseASemiStmt(ASemiStmt node)
    {
        inASemiStmt(node);
        outASemiStmt(node);
    }

    @Override
    public void caseAAssignmentStmt(AAssignmentStmt node)
    {
        inAAssignmentStmt(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
            
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        String typel = null,typer = null;
        Variable_t varl = null, varr = null;
        
        varl = getType(node.getLeft().toString(), current);
        varr = getType(node.getRight().toString(), current);
        
        if(varl.getType() == null)
        	typel = hm.get(node.getLeft().toString());
        else
        	typel = varl.getType();
        
        if(varr.getType() == null)
        	typer = hm.get(node.getRight().toString());
        else
        	typer = varr.getType();

        if(typel!=null && typer!=null && !typel.equals(typer)){
        	errors.add("Assignment of "+ node.getLeft().toString().replaceAll(" ","") +" does not match type \""+ typel +"\". Type \""+typer+"\" found.");
        }
        outAAssignmentStmt(node);
    }

    @Override
    public void caseAIfstmtStmt(AIfstmtStmt node)
    {
        inAIfstmtStmt(node);
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        if(node.getThen() != null)
        {
            node.getThen().apply(this);
        }
        outAIfstmtStmt(node);
    }

    @Override
    public void caseAIfElseStmt(AIfElseStmt node)
    {
        inAIfElseStmt(node);
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getThen());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getElseSt());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outAIfElseStmt(node);
    }

    @Override
    public void caseAWhilestmtStmt(AWhilestmtStmt node)
    {
        inAWhilestmtStmt(node);
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getBody());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outAWhilestmtStmt(node);
    }

    @Override
    public void caseAFunCalStmt(AFunCalStmt node)
    {
        inAFunCalStmt(node);
        if(node.getFunCal() != null)
        {
            node.getFunCal().apply(this);
        }
        outAFunCalStmt(node);
    }

    @Override
    public void caseAReturnstmtStmt(AReturnstmtStmt node)
    {
        inAReturnstmtStmt(node);
        if(node.getReturnexpr() != null)
        {
        	ret = true;
        	String type;
        	String val;
            node.getReturnexpr().apply(this);
            val = node.toString();
            val = val.replaceAll("- ", "");
            val = val.replaceAll("\\+ ", "");
            //System.out.println("RETURN "+node.toString());
            Variable_t t = getType(node.toString(), current); 
            t.printVar();
            //System.out.println("HM::: "+hm);
            if(t.getType() == null ) {
            	/* if(!current.get_return_type().equals("nothing")){
    	    		errors.add("Return statement is null! Required "+current.get_return_type()+ " in method " + current.getName());
    				return;	
    			} */
            	if(hm.containsKey(val)) {
            		type = hm.get(val);
            		//System.out.println(type+"<----");
            		if(!type.equals(current.get_return_type())) {
            			errors.add("Return statement " + val +" is type of \""+type+"\"! Required \""+current.get_return_type()+ "\" in method " + current.getName());
            			return;
            		}
            	}
            	else {
    	    		errors.add("Unknown return expression."+val);
            	}
    		}   
            else if(!t.getType().equals(current.get_return_type())){
            	errors.add("Return statement " + val +" is type of \""+t.getType()+"\"! Required \""+current.get_return_type()+ "\" in method " + current.getName());
    			return;
            }
        }
        
        outAReturnstmtStmt(node);
    }

    @Override
    public void caseABlockStmt(ABlockStmt node)
    {
        inABlockStmt(node);
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getBlock());
            for(PStmt e : copy)
            {
                e.apply(this);                
            }
        }
        outABlockStmt(node);
    }
}
