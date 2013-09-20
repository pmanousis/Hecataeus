package clusters.GraphConstructs;

public class HACNode {

	public  HACNode(){
		name = "NONAME";
	}
	public  HACNode(String n){
		name = n;
	}
	
	public String getName(){ return name; }

	protected String name;
}
