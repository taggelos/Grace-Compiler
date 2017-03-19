package types;

public class VariableType extends MyType {
	public String type;
	public int id;
	public String var_TEMP;

	public VariableType(String type, String name) {
	    super(name);
	    this.type = type;
	    this.var_TEMP = null;
	}
}