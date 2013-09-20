package clusters.GraphFacades;
import clusters.GraphConstructs.HACTable;


public class ClusterableTable extends ClusterableObject{
	public ClusterableTable(HACTable t){
		theObject = t;
	}
	public String printClusterableObject(){
		return "Table " + getId() + ":" + theObject.getName() + " "; 
	}
	public Object getObject(){ return theObject; }
	
	
	private HACTable theObject;
	
}