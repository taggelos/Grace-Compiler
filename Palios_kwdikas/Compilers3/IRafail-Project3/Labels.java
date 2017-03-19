public class Labels {
	private int count;

	public Labels() { 
		this.count = 0; 
	}

	public String create_label() { 
		this.count += 1;
		return new String("L" + this.count); 
	}

	public String create_class_label(String str) { 
		return new String(str + "_vTable"); 
	}

}