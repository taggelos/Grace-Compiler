package facts_gen;
import java.io.*;

public class Instruction_t extends dl_t {

	public int ic;
	public String instr;

	public Instruction_t(String meth_name, int ic, String instr) {
		super(meth_name);
		this.ic = ic;
		this.instr = instr;
	}

	public void printrec(PrintWriter writer) {
		String ret = "instruction(" + this.meth_name + ", " + this.ic + ", " + this.instr + ").";
		System.out.println(ret);
		writer.println(ret);
	}

}
