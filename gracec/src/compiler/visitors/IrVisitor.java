package compiler.visitors;

import java.util.*;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;
import compiler.symboltable.*;

public class IrVisitor extends DepthFirstAdapter
{   
	SymbolTable st;
	int regcount=1;
	String value;
	String current;
	int count=0;
	String arrayname, arrayindex="0";
	
	public LinkedList<LinkedList<Integer>> trueList;
	public LinkedList<LinkedList<Integer>> falseList;
	
	public Helpers h = new Helpers();
	
	public IrVisitor(SymbolTable st) { 
		this.st = st;
		trueList = new LinkedList<LinkedList<Integer>>();
		falseList = new LinkedList<LinkedList<Integer>>();
	}
	
	public String getRegt(){
		return "$"+Integer.toString(regcount++);
	}
	
	public String getlastreg(){
		return "$"+Integer.toString(regcount-1);
	}
	 
	 public void makeArray(String name, String content){
		 String reg = getRegt();
		 h.genQuad("array", name, content, reg);
	 }
	 
	 public String getType(String var, String meth) {
		 
        String methodvar = st.contains(meth).methContains(var);
        if(methodvar == null && st.contains(meth).from != null) {             // An den brisketai se sunarthsh...
            String fromvar = st.contains(meth).from.methContains(var);
            if(fromvar != null)            
            	methodvar = fromvar;
        }
        return methodvar;

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
        h.printQuads();
        outAProgram(node);
    }
    
    @Override
    public void caseAFunDef(AFunDef node)
    {
        inAFunDef(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
            h.genQuad("unit" , ((AHeader)node.getHeader()).getName().toString(),"-","-");
        }
        {
            List<PLocalDef> copy = new ArrayList<PLocalDef>(node.getLocal());
            for(PLocalDef e : copy)
            {
                e.apply(this);                
            }
        }
        {
        	current = ((AHeader)node.getHeader()).getName().toString();
            List<PStmt> copy = new ArrayList<PStmt>(node.getBlock());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outAFunDef(node);
        h.genQuad("endu" , ((AHeader)node.getHeader()).getName().toString(),"-","-");
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
        inAFunDec(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
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
        String name = null;
        String type;
        if(node.getName() != null)
        {
            node.getName().apply(this);
            name = node.getName().toString();
        }
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExprs());
            int i=0;
            for(PExpr e : copy)
            {
                e.apply(this);
                if(st.contains(name)!=null){ 
	                if(st.contains(name).getParams().get(i++).isRef())
	                	type = "R";
	                else 
	                	type = "V";
	                h.genQuad("par", value, type, "-");
                }
            }
        }
        if(!st.contains(name).get_return_type().equals("nothing")) {
        	h.genQuad("par", "RET", getRegt(), "-");
        }
        h.genQuad("call", "-", "-", node.getName().toString());
        value = getlastreg();
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
        value=node.getIdentifier().toString();
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
        value=node.getStringLiteral().toString();
        outAStringLVal(node);
    }
    
    @Override
    public void caseAIdBracketsLVal(AIdBracketsLVal node)
    {
        inAIdBracketsLVal(node);
        String index = null;
        if(node.getLVal() != null)
        {
            node.getLVal().apply(this);
            
        }
        if(node.getLBr() != null)
        {
            node.getLBr().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
            index= value;
        }
        if(node.getRBr() != null)
        {
            node.getRBr().apply(this);
        }
        
        
        //if(!arrayname.contains("\"")) {
        	arrayname = node.getLVal().toString().split(" ")[0];
        	String type = getType(arrayname+" ", current);
       
        System.err.println(arrayname);
        String[] split = type.split("\\[");
        String s;
        String prev = index;
        int c=count+2;
        if(c < split.length) {
	        for(int k=c; k < split.length; k++) {
	        	s = split[k].replaceAll("[^1234567890]", "");
	        	if(count==0 && k==2)
	        		h.genQuad("*", index, s, getRegt());
	        	else
	        		h.genQuad("*", prev, s, getRegt());
	        	
	        	prev = getlastreg();
	        }
	        count++;
	        h.genQuad("+", arrayindex, getlastreg(), getRegt());
	        arrayindex = getlastreg();
        }
        else {
        	h.genQuad("+", index, arrayindex, getRegt());
        }
        arrayindex = getlastreg();
        outAIdBracketsLVal(node);
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
        trueList.removeLast();
        falseList.removeLast();
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
        	h.backpatchall(trueList.getLast(), h.nextQuad());
            List<PStmt> copy = new ArrayList<PStmt>(node.getThen());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        {
        	trueList.getLast().add(h.nextQuad());
        	h.genQuad("jump2", "-", "-", "*");
        	h.backpatchall(falseList.getLast(), h.nextQuad());
            List<PStmt> copy = new ArrayList<PStmt>(node.getElseSt());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
            h.backpatchall(trueList.getLast(), h.nextQuad());
        }
        trueList.removeLast();
        falseList.removeLast();
        outAIfElseStmt(node);
    }
    
    @Override
    public void caseAIfHeader(AIfHeader node)
    {
        inAIfHeader(node);
        trueList.addLast(new LinkedList<>());
        falseList.addLast(new LinkedList<>());
        h.backpatch(trueList.getLast(), h.nextQuad());
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
        	h.backpatchall(trueList.getLast(), h.nextQuad());
            List<PStmt> copy = new ArrayList<PStmt>(node.getThen());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
            h.backpatchall(falseList.getLast(), h.nextQuad());
        }
        outANoElseIfTrail(node);
    }
    
    @Override
    public void caseAWithElseIfTrail(AWithElseIfTrail node)
    {
        inAWithElseIfTrail(node);
        {
        	h.backpatchall(trueList.getLast(), h.nextQuad());
            List<PStmt> copy = new ArrayList<PStmt>(node.getThen());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        {
        	trueList.getLast().add(h.nextQuad());
        	h.genQuad("jump", "-", "-", "*");
        	h.backpatchall(falseList.getLast(), h.nextQuad());
            List<PStmt> copy = new ArrayList<PStmt>(node.getElseSt());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
            h.backpatchall(trueList.getLast(), h.nextQuad());
        }
        outAWithElseIfTrail(node);
    }
    
    @Override
    public void caseAWhilestmtStmt(AWhilestmtStmt node)
    {
        inAWhilestmtStmt(node);
        trueList.addLast(new LinkedList<>());
        falseList.addLast(new LinkedList<>());
        String jump = null;
        if(node.getCond() != null)
        {
        	jump = Integer.toString(h.nextQuad());
            node.getCond().apply(this);
        }
        {

            h.backpatchall(trueList.getLast(), h.nextQuad());
            List<PStmt> copy = new ArrayList<PStmt>(node.getBody());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        	
        	h.genQuad("jump", "-", "-", jump);
        	h.backpatchall(falseList.getLast(), h.nextQuad());
        }
        trueList.removeLast();
        falseList.removeLast();
        outAWhilestmtStmt(node);
    }
    
    @Override
    public void caseAAndExprExpr(AAndExprExpr node)
    {
        inAAndExprExpr(node);
        int counter = trueList.getLast().size();
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        int counter2 = trueList.getLast().size();
        for(int i=0; i<(counter2-counter); i++)
        	h.backpatch(trueList.getLast(), h.nextQuad());
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAAndExprExpr(node);
    }
    
    @Override
    public void caseAOrExprExpr(AOrExprExpr node)
    {
        inAOrExprExpr(node);
        int counter = falseList.getLast().size();
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        int counter2 = falseList.getLast().size();
        for(int i=0; i<(counter2-counter); i++)
        	h.backpatch(falseList.getLast(), h.nextQuad());
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
            Integer temp=null;
            temp = falseList.getLast().getLast();
            falseList.getLast().removeLast();
            falseList.getLast().add(trueList.getLast().getLast());
            trueList.getLast().removeLast();
            trueList.getLast().add(temp);
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
        String left = null, right = null;
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
            left = value;
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
            right = value;
        }
        trueList.getLast().add(h.nextQuad());
        h.genQuad("<", left, right, "*");
        
        falseList.getLast().add(h.nextQuad());
        h.genQuad("jump", "-", "-", "*");
        
        outALessThanExpr(node);
    }
    
    @Override
    public void caseAGreaterThanExpr(AGreaterThanExpr node)
    {
        inAGreaterThanExpr(node);
        String left = null, right = null;
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
            left = value;
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
            right = value;
        }
        trueList.getLast().add(h.nextQuad());
        h.genQuad(">", left, right, "*");
        falseList.getLast().add(h.nextQuad());
        h.genQuad("jump", "-", "-", "*");
        
        outAGreaterThanExpr(node);
    }
    
    @Override
    public void caseAGreaterEqualThanExpr(AGreaterEqualThanExpr node)
    {
        inAGreaterEqualThanExpr(node);
        String left = null, right = null;
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
            left = value;
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
            right = value;
        }
        trueList.getLast().add(h.nextQuad());
        h.genQuad(">=", left, right, "*");
        falseList.getLast().add(h.nextQuad());
        h.genQuad("jump", "-", "-", "*");
       
        outAGreaterEqualThanExpr(node);
    }
    
    @Override
    public void caseALessEqualThanExpr(ALessEqualThanExpr node)
    {
        inALessEqualThanExpr(node);
        String left = null, right = null;
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
            left = value;
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
            right = value;
        }
        trueList.getLast().add(h.nextQuad());
        h.genQuad("<=", left, right, "*");
        falseList.getLast().add(h.nextQuad());
        h.genQuad("jump", "-", "-", "*");
        
        outALessEqualThanExpr(node);
    }
    
    @Override
    public void caseAEqualExpr(AEqualExpr node)
    {
        inAEqualExpr(node);
        String left = null, right = null;
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
            left = value;
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
            right = value;
        }
        trueList.getLast().add(h.nextQuad());
        h.genQuad("=", left, right, "*");
        falseList.getLast().add(h.nextQuad());
        h.genQuad("jump", "-", "-", "*");
        
        outAEqualExpr(node);
    }
    
    @Override
    public void caseANotEqualExpr(ANotEqualExpr node)
    {
        inANotEqualExpr(node);
        String left = null, right = null;
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
            left = value;
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
            right = value;
        }
        trueList.getLast().add(h.nextQuad());
        h.genQuad("#", left, right, "*");
        falseList.getLast().add(h.nextQuad());
       
        h.genQuad("jump", "-", "-", "*");
        
        outANotEqualExpr(node);
    }
    
    @Override
    public void caseAAddExpr(AAddExpr node)
    {
        inAAddExpr(node);
        String left = node.getExpr1().toString(), right = node.getExpr2().toString();

        if(node.getExpr1() != null)
        {
            node.getExpr1().apply(this);
            left = value;
        }
        if(node.getExpr2() != null)
        {
            node.getExpr2().apply(this);
            right = value;
        }  
        
        String reg= getRegt();
        h.genQuad("+" , left, right, reg);
        value = getlastreg();
        outAAddExpr(node);
    }
    
    @Override
    public void caseASubExpr(ASubExpr node)
    {
        inASubExpr(node);
        String left = node.getExpr1().toString(), right = node.getExpr2().toString();

        if(node.getExpr1() != null)
        {
            node.getExpr1().apply(this);
            left = value;
        }
        if(node.getExpr2() != null)
        {
            node.getExpr2().apply(this);
            right = value;
            if(node.getExpr2().toString().contains("-"))
            	right = getlastreg();
        }
        String reg= getRegt();
        h.genQuad("-" , left, right, reg);
        value = getlastreg();
        outASubExpr(node);
    }
    
    @Override
    public void caseAMultExpr(AMultExpr node)
    {
        inAMultExpr(node);
        String left = node.getExpr1().toString(), right = node.getExpr2().toString();

        if(node.getExpr1() != null)
        {
            node.getExpr1().apply(this);
            left = value;
        }
        if(node.getExpr2() != null)
        {
            node.getExpr2().apply(this);
            right = value;
        }  
        
        String reg= getRegt();
        h.genQuad("*" , left, right, reg);
        value = getlastreg();
        outAMultExpr(node);
    }
    
    @Override
    public void caseAModExpr(AModExpr node)
    {
        inAModExpr(node);
        String left = node.getExpr1().toString(), right = node.getExpr2().toString();

        if(node.getExpr1() != null)
        {
            node.getExpr1().apply(this);
            left = value;
        }
        if(node.getExpr2() != null)
        {
            node.getExpr2().apply(this);
            right = value;
        }  
        
        String reg= getRegt();
        h.genQuad("mod" , left, right, reg);
        value = getlastreg();
        outAModExpr(node);
    }
    
    @Override
    public void caseADivExpr(ADivExpr node)
    {
        inADivExpr(node);
        String left = node.getExpr1().toString(), right = node.getExpr2().toString();

        if(node.getExpr1() != null)
        {
            node.getExpr1().apply(this);
            left = value;
        }
        if(node.getExpr2() != null)
        {
            node.getExpr2().apply(this);
            right = value;
        }  
        
        String reg= getRegt();
        h.genQuad("/" , left, right, reg);
        value = getlastreg();
        outADivExpr(node);
    }
    
    @Override
    public void caseAPlusOrMinusExpr(APlusOrMinusExpr node)
    {
        inAPlusOrMinusExpr(node);
        if(node.getPlusOrMinus() != null)
        {
            node.getPlusOrMinus().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getPlusOrMinus().toString().contains("-")) {
        	h.genQuad("-", "0", value, getRegt());
        	value = getlastreg();
        }
        outAPlusOrMinusExpr(node);
    }
    
    @Override
    public void caseAMinusPlusOrMinus(AMinusPlusOrMinus node)
    {
        inAMinusPlusOrMinus(node);
        if(node.getMinus() != null)
        {
            node.getMinus().apply(this);
        }
        
        outAMinusPlusOrMinus(node);
    }
    
    @Override
    public void caseAIntExpr(AIntExpr node)
    {
        inAIntExpr(node);
        if(node.getIntegers() != null)
        {
            node.getIntegers().apply(this);
        }
        value = node.getIntegers().toString();
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
        value = node.getCharConst().toString();
        outACharExpr(node);
    }
    
    @Override
    public void caseALValExpr(ALValExpr node)
    {
        inALValExpr(node);
        String prev = arrayindex;
        arrayindex = "0";
        //count = 0;
        if(node.getLVal() != null)
        {
            node.getLVal().apply(this);
        }
        if(node.getLVal() instanceof AIdBracketsLVal) {
        	makeArray(arrayname, arrayindex);
        	value = "["+getlastreg()+"]";
        }
        arrayindex = prev;
        
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
        String left = node.getLeft().toString(), right = node.getRight().toString();

        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
            if(node.getLeft() instanceof AIdBracketsLVal) {
            	makeArray(arrayname, arrayindex);
            	value = "["+getlastreg()+"]";
            }
            count = 0;
            arrayindex = "0";
            left = value;
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
            right = value;
            if(node.getRight() instanceof ALValExpr) {
        		ALValExpr lval = (ALValExpr) node.getRight();
            }
            else if(node.getRight() instanceof AFunCalExpr) {
            	right = value;
            }
        }       
        h.genQuad(":=" , left,"-", right);
        outAAssignmentStmt(node);
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
        String ret;
        if(node.getReturnexpr() != null)
        {
            node.getReturnexpr().apply(this);
            ret=node.getReturnexpr().toString();
            if(node.getReturnexpr() instanceof ALValExpr) {
        		ALValExpr lval = (ALValExpr) node.getReturnexpr();
        		if(lval.getLVal() instanceof AIdBracketsLVal) {
        			ret = "["+getlastreg()+"]";
        		}
            }
            else if(!(node.getReturnexpr() instanceof AIntExpr)) {
            	ret = getlastreg();
            }

            h.genQuad(":=" ,ret,"-","$$");
        }
        h.genQuad("ret" ,"-","-","-");
        
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
