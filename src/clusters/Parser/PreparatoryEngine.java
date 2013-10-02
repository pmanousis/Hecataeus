package clusters.Parser;

import java.io.IOException;
import java.util.ArrayList;

import clusters.GraphFacades.ClusterableObject;
import clusters.GraphFacades.ClusterableQuery;
import clusters.GraphFacades.ClusterableTable;

public abstract class PreparatoryEngine {
	
	public PreparatoryEngine(){
		inputObjects = new ArrayList<ClusterableObject>();
		inputTables = new ArrayList<ClusterableTable>();
		inputQueries = new ArrayList<ClusterableQuery>();
	}
	
	public enum DistanceFunctionEnum{
		COMMON_NEIGHBORS, PATH, SIMRANK
	}
	
	public abstract void produceDistanceMatrix(DistanceFunctionEnum F) throws IOException;
	
	public void testPreparatoryEngine(){
		System.out.println("Number of Tables: " + numTables + " " + inputTables.size());
		System.out.println("Number of Queries: " + numQueries + " " + inputQueries.size());
		for(ClusterableObject co: inputObjects){
//			System.out.println(co.printClusterableObject());
		}
	}
	
	public ArrayList<ClusterableObject> getInputObjects(){ return inputObjects; }
	public double [][] getDistanceMatrix() { return distanceMatrix; }
	
	protected int numTables = -1;
	protected int numQueries = -1;
	protected int numObjects = -2;
	
	protected ArrayList<ClusterableObject> inputObjects;
	protected ArrayList<ClusterableTable> inputTables;
	protected ArrayList<ClusterableQuery> inputQueries;
	//protected ArrayList<ClusterableViews> inputViews;
	
	protected double distanceMatrix[][] = null;
}
