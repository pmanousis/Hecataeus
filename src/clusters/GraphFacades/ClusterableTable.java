package clusters.GraphFacades;
import clusters.GraphConstructs.HACTable;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;


public class ClusterableTable extends ClusterableObject{
	public ClusterableTable(VisualNode t){
		theObject = t;
	}
	public String printClusterableObject(){
		//return "Table " + getId() + ":" + theObject.getName() + " "; 
	
		return theObject.getName(); 
	}
	public VisualNode getObject(){ return theObject; }
	
	public String tableName(){
		return theObject.getName().trim();
	}
	
	private VisualNode theObject;

}