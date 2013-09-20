package clusters.GraphFacades;

public abstract class ClusterableObject {
	
	public Object getObject(){
		return theObject;
	}
	public int getId(){ return id;}
	public void setId(int n){ id=n; }
	public abstract String printClusterableObject();
	
	private Object theObject;
	private int id = -1;
}

