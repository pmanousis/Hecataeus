package clusters.Parser;

public abstract class NodeDistanceFunction {

	// TODO Add graph as a parameter; requires to "build" the HACGraph class
	public NodeDistanceFunction(){
		distanceMatrix = null;
	}
	public abstract double[][] computeAllDistances(int adjMatrix[][], int numObjects);
	
	public double [][] getDistanceMatrix(){ return distanceMatrix; }
	
	protected double distanceMatrix[][] = null;
}
