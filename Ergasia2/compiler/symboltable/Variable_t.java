package compiler.symboltable;

public class Variable_t extends MyType {
	private String type;
	private boolean isRef;

	public Variable_t(String type, String name) {
	    super(name);
	    this.type = type;
	    isRef = false;
	}

	public String getType() {
        return this.type;
    }
	
	public boolean isRef() {
        return this.isRef;
    }

    public void setRef() {
    	this.isRef = true;
    }

    public void printVar() {
    	if(this.getName() == null)
    		System.out.print(this.type + " null");
    	else
    		System.out.print(this.type + " " + this.getName()+ " REF: "+ this.isRef);
	}
}