package attribute;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;


public class Attribute {
	public String name;
	public int tempNo;
	public HashMap<String,Integer> tempMap;

	public Attribute(String name){
		this.name = name;
	}


	public static void printMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}