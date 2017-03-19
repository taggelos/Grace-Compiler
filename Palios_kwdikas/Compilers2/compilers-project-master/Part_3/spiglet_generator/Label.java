package spiglet_generator;

public class Label {
	private int cnt;

	public Label() { this.cnt = 1; }

	public String new_label() { return new String("L" + this.cnt++); }

	public String new_Class_label(String str) { return new String(str + "_vTable"); }

}