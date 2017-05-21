package compiler.symboltable;

public class Variable_t extends MyType {
	private String type;

	public Variable_t(String type, String name) {
	    super(name);
	    this.type = type;
	}

	public String getType() {
        return this.type;
    }

    public void printVar() {
    	if(this.getName() == null)
    		System.out.print(this.type + " null");
    	else
    		System.out.print(this.type + " " + this.getName());
	}
}