package clusters;
/**
 * Class responsible for keeping all the data of the problem
 * 1. the input = a set of clusterable objects
 * 2. the output = a set of clusters
 * 3. the distance calculations -- typically the adjacency matrix
 * 4. the algorithms that perform the clustering
 */

import java.util.ArrayList;
import java.util.Iterator;
//import java.util.Collections;
//import java.util.Enumeration;

import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import clusters.GraphConstructs.HACNode;
import clusters.GraphFacades.ClusterableNode;
import clusters.GraphFacades.ClusterableObject;
import clusters.HACAlgorithms.HACAlgorithm;
import clusters.HACAlgorithms.SimpleHAC_AvgLink;
import clusters.Parser.Parser;
import clusters.Parser.PreparatoryEngine;
/**
 * @author pvassil
 *
 */
public class HAggloEngine {
	public  HAggloEngine(){
		inputObjects = new ArrayList<ClusterableObject>();
		solutions = new ArrayList<ClusterSet>();
		// TODO Build a factory for algo's
		algorithm = new SimpleHAC_AvgLink();
		
		//this.testConstructor();
	}
	public double[][] getinputObjectsDistances(){ return inputObjectsDistances; }
	public int getInputSize(){return inputObjects.size();}
	
	/*
	 * The main job of the Engine is to execute a Hierarchical Agglomerative Clustering Algo.
	 * It does so in two parts: (a) by picking an appropriate algo at the constructor and 
	 * (b) by invoking its execute function here.
	 */
	public ClusterSet execute(int numC){
		ClusterSet cs;
//		cs = new ClusterSet(algorithm.execute(inputObjects, solutions, inputObjectsDistances, numC).getId());
		cs = algorithm.execute(inputObjects, solutions, inputObjectsDistances, numC);
		return cs;
	}
	
	/*
	 * Introduced to provide a quick example without parsing
	 */
	public void testConstructor(){
/*		
 * Still: this worked too!!
 * 		inputObjects.add(new ClusterableTable(new HACTable("R1")));
		inputObjects.add(new ClusterableTable(new HACTable("R2")));
		inputObjects.add(new ClusterableTable(new HACTable("R3")));
		inputObjects.add(new ClusterableTable(new HACTable("R4")));
		inputObjects.add(new ClusterableTable(new HACTable("R5")));
*/
		inputObjects.add(new ClusterableNode(new HACNode("R1")));
		inputObjects.add(new ClusterableNode(new HACNode("R2")));
		inputObjects.add(new ClusterableNode(new HACNode("R3")));
		inputObjects.add(new ClusterableNode(new HACNode("R4")));
		inputObjects.add(new ClusterableNode(new HACNode("R5")));


		
		ClusterSet orgSolution = new ClusterSet(0);
		Iterator<ClusterableObject> it = inputObjects.iterator();
		int clustId = 0;
		while(it.hasNext()){
			ClusterableObject co = it.next();
			
			co.setId(clustId);
			Cluster c = new Cluster(clustId);
			
			c.getExtension().add(co);
			orgSolution.getClusters().add(c);
			clustId++;
		}
		orgSolution.createDistances(clustId);
		System.out.println("Num of Original clusters: " + orgSolution.getClusters().size()+  "\n");
		solutions.add(orgSolution);
		System.out.println("Num of Solutions: " + solutions.size()+ "\n");
	}
	
	public void printResultsConsole(){
		Iterator<ClusterSet> it = solutions.iterator();
		while(it.hasNext()){
			System.out.println(it.next().getCSDescriptionString());
		}
	}
	
	public void executeParser(){
		Parser prsr = new Parser();
		String fileName = prsr.chooseFile();
		prsr.parseFile(fileName);
		int numNodes = prsr.produceFacetedObjects();
		System.out.println("Preprocessing gave " + numNodes + " nodes overall");
		prsr.testPreparatoryEngine();
		prsr.produceAdjMatrix();
		prsr.produceDistanceMatrix(PreparatoryEngine.DistanceFunctionEnum.COMMON_NEIGHBORS);
		
		inputObjectsDistances = prsr.getDistanceMatrix();
		inputObjects = prsr.getInputObjects();
	}

	
	public void buildFirstSolution(){
		ClusterSet orgSolution = new ClusterSet(0);
		Iterator<ClusterableObject> it = inputObjects.iterator();
		int clustId = 0;
		while(it.hasNext()){
			ClusterableObject co = it.next();
			
			co.setId(clustId);
			Cluster c = new Cluster(clustId);
			
			c.getExtension().add(co);
			orgSolution.getClusters().add(c);
			clustId++;
		}
		orgSolution.createDistances(clustId);
		System.out.println("Num of Original clusters: " + orgSolution.getClusters().size()+  "\n");
		solutions.add(orgSolution);
		System.out.println("Num of Solutions: " + solutions.size()+ "\n");
	}
	
//	private int numObjects = 0;
//	private int numSolutions = 0;
	private ArrayList<ClusterableObject> inputObjects;
	private ArrayList<ClusterSet> solutions;
	private HACAlgorithm algorithm;
	private double inputObjectsDistances[][] = null; //{{0,0,1,1,0.67}, {0,0,1,1,0.67}, {1,1,0,0,0.67}, {1,1,0,0,0.67}, {0.67,0.67,0.67,0.67,0}};

}