package clusters.GraphFacades;
import clusters.GraphConstructs.HACTable;


public class ClusterableTable extends ClusterableObject{
	public ClusterableTable(HACTable t){
		theObject = t;
	}
	public String printClusterableObject(){
		//return "Table " + getId() + ":" + theObject.getName() + " "; 
		return theObject.getName(); 
	}
	public Object getObject(){ return theObject; }
	
	public String tableName(){
		return theObject.getName().trim();
	}
	
	private HACTable theObject;

	
	
}