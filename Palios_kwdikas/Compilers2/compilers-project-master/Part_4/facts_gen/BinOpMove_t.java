package facts_gen;
import java.io.*;

public class BinOpMove_t extends Var_t {
	
	public int ic;
	public String tempsrc;

	public BinOpMove_t(String meth_name, int ic, String temp, String tempsrc) {
		super(meth_name, temp);
		this.ic = ic;
		this.tempsrc = tempsrc;
	}

	public void printrec(PrintWriter writer) {
		String ret = "binOpMove(" + this.meth_name + ", " + this.ic + ", " + this.temp + ", " + this.tempsrc + ").";
		System.out.println(ret);
		writer.println(ret);
	}

}
