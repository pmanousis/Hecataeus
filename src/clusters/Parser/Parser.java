package clusters.Parser;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import clusters.GraphFacades.ClusterableObject;
import clusters.GraphFacades.ClusterableQuery;
import clusters.GraphFacades.ClusterableTable;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
public class Parser extends PreparatoryEngine {

	private VisualGraph graph;
	
	public Parser(VisualGraph g){
		this.tableNames = null;
		
		this.adjMatrixFromInput = null;
		

		
		this.adjMatrix = null;
		this.graph = g;
	}
	
	public String chooseFile(){
		String fileName = "";
	 	JFileChooser chooser = new JFileChooser();
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	try {
				fileName = chooser.getSelectedFile().getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("The chosen file did not work properly");
				e.printStackTrace();
			}
	    			//.getName(); 
	    	System.out.println("You chose to open this file: " + fileName);
	    }
		return fileName;
	}
	
	/*
	 * Method construct importObjectsDistances produces the list of objects and their distances from the available input
	 * Input:
	 * ---------- 
	 * pick a filename
	 * Future: Hecataeus' existing collections of Tables, Views, Queries
	 * 
	 * iNTERIM:
	 * ----------
	 * Detect tables, queries, views and create the appropriate facet objects
	 * Correctly construct and parse the adjacency matrix
	 * 
	 * Produce (??? MUST DECIDE ???):
	 * ----------------------
	 * ... based on the adj. matrix, the distance matrix ...
	 * int totalNumObjects;
	 * private double inputObjectsDistances[totalNumObjects][totalNumObjects], POPULATED, partOf Engine 
	 * private ArrayList<ClusterableObject> inputObjects, POPULATED, partOf Engine
	 * ... taking care to: 
	 * the objects inside the inputObjects must be of the correct subclass of ClusterableObject -- view, query, table
	 * ... and possibly ...
	 * private ArrayList<ClusterableTable> inputTables;
	 * private ArrayList<ClusterableViews> inputViews;
	 * private ArrayList<ClusterableQueries> inputQueries;
	 */
	
	
	/*
	 * Breakdown of steps:
	 * DONE one method to produce Arraylists of tables, queries views from the file; just for here.
	 * DONE the same method that produces the arraylists of the facet objects, to be used everywhere
	 * one method that parses and represents in m.m. the adjacency matrix
	 * one method that computes the distance matrix 
	 * 
	 * one and three are for the file-specific implementation
	 * 2 and four are generic + they are (a) part of the parser's output and (b) organically parts of the engine
	 * 
	 */

	
	public void parseFile(String fileName){


		int adjMatrixRowsCounted = 0;int adjMatrixRowsCountedTV = 0;int adjMatrixRowsCountedVQ = 0;
		Boolean AMCreated = false;

		Scanner inputStream = null;
		try{
			inputStream =
					new Scanner(new FileInputStream(fileName));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File " + fileName + " was not found or could not be opened.");
			System.exit(0);
		}

		while (inputStream.hasNextLine( ))
		{
			if ((numTables != -1)&&(numQueries != -1)&&(AMCreated == false)){
				adjMatrixFromInput = new int[numTables+numViews+numQueries][numTables+numViews+numQueries];

				
				//System.out.println("AOUAOA " + numTables + " " + numQueries);
				AMCreated = true;
			}

			String line = inputStream.nextLine( );
			line = line.trim();
			if (line.startsWith("%") || line.trim().isEmpty() || line.startsWith("\n")){	
				continue;				//do nothing for comments, empty lines
			}
			else {
				if (line.startsWith("TABLES")){
					String parts[] = line.split("\\s");
					if (parts.length != 3){
						System.out.println("TABLES not syntaxed correctly");
						System.exit(0);	
					}
					numTables = Integer.parseInt(parts[2]);
					System.out.println(numTables);
				}

				else{ 
					if (line.startsWith("TableNames")){
						String dummySplit[] = line.split("=");
						if (dummySplit.length != 2){
							System.out.println("TableNames not syntaxed correctly");
							System.exit(0);	
						}

						tableNames = dummySplit[1].split(",");

						if(tableNames.length != numTables){
							System.out.println("#TABLES and #TableName strings are not syntaxed consistently");
							System.exit(0);	
						}
						for (String s: tableNames){
							s = s.replace(",","");
							s = s.trim();
							System.out.println(s);	
						}

					}

						else{
							if(line.startsWith("VIEWS")){
								String dummySplit[] = line.split("=");
								String parts[] = line.split("\\s");
								if (parts.length != 3){
									System.out.println("VIEWS not syntaxed correctly");
									System.exit(0);	
								}
								viewNames = dummySplit[1].split(",");

								numViews = Integer.parseInt(parts[2]);
								System.out.println(numViews);
							}
							
							else if(line.startsWith("ViewNames")){
								String dummySplit[] = line.split("=");
								if (dummySplit.length != 2){
									System.out.println("ViewNames not syntaxed correctly");
									System.exit(0);	
								}

								viewNames = dummySplit[1].split(",");

								if(viewNames.length != numViews){
									System.out.println("#Views and #ViewName strings are not syntaxed consistently");
									System.exit(0);	
								}
								for (String s: viewNames){
									s = s.replace(",","");
									s = s.trim();
									System.out.println(s);	
								}
							}
							
							else if (line.startsWith("QUERIES")){
								String dummySplit[] = line.split("=");
								String parts[] = line.split("\\s");
								if (parts.length != 3){
									System.out.println("QUERIES not syntaxed correctly");
									System.exit(0);	
								}
								queryNames = dummySplit[1].split(",");

								numQueries = Integer.parseInt(parts[2]);
								System.out.println(numQueries);
							}
							else if(line.startsWith("QueryNames")){
								String dummySplit[] = line.split("=");
								if (dummySplit.length != 2){
									System.out.println("QueryNames not syntaxed correctly");
									System.exit(0);	
								}

								queryNames = dummySplit[1].split(",");

								if(queryNames.length != numQueries){
									System.out.println("#QUERIES and #QueryName strings are not syntaxed consistently");
									System.exit(0);	
								}
								for (String s: queryNames){
									s = s.replace(",","");
									s = s.trim();
									System.out.println(s);	
								}
							}
							else{
								if (AMCreated == false){
									System.out.println("Exiting due to unexpected syntax of the file");
									System.out.println(line);
									System.exit(0);
								}
								String cells[] = line.split(",");
								int j = 0;
								for (String s: cells){
									s = s.trim();
									adjMatrixFromInput[adjMatrixRowsCounted][j] = Integer.parseInt(s);
									j++;
								}
								//							for (int x = 0; x< numQueries; x++)
								//								System.out.print(adjMatrixFromInput[adjMatrixRowsCounted][x] + "\t");
								//							System.out.println();
								adjMatrixRowsCounted++;
							}

						}
					}
				}
			}//end while



			inputStream.close( );

		}

	
	
	public int produceFacetedObjects(){
		int idCounter = 0;
		for (String s: tableNames){
//			HACTable t = new HACTable(s);
//			System.out.println(s);
			ClusterableTable ct = new ClusterableTable(graph.findVertexByName(s.trim(),NodeType.NODE_TYPE_RELATION));
//			System.out.println(graph.findVertexByName(s.trim()));
			ct.setId(idCounter);
			inputTables.add(ct);
			idCounter++;
		}
		for (String s: queryNames){
//			System.out.println(s);
			ClusterableQuery ct = new ClusterableQuery(graph.findVertexByName(s.trim(),NodeType.NODE_TYPE_QUERY));
//			System.out.println(graph.findVertexByName(s.trim()));
			ct.setId(idCounter);
			inputQueries.add(ct);
			idCounter++;
		}
//		for (int i=0; i < numQueries; i++){
//			
//			HACQuery q = new HACQuery("Q"+idCounter);		//can try + i instead, but now it's clear
//			ClusterableQuery cq = new ClusterableQuery(q);
//			cq.setId(idCounter);
//			inputQueries.add(cq);
//			idCounter++;
//		}
		
		inputObjects.addAll(inputTables);
		inputObjects.addAll(inputQueries);
		inputObjects.addAll(inputViews);
		
		return idCounter;
	}
	
	/*
	 * THe adjMatrixFromInput has one row per table. So, it is numTables x numQueries matrix.
	 * We need to convert it to a matrix which is numObjects x numObjects.
	 * The numbering is as follows:
	 * -- tables have the same numbering as in the file
	 * -- queries have a numbering that also includes the tables. 
	 * E.g., if we have 3 tables and 5 queries, the second query is the 5th object and has id = 4  
	 * and a position 4 in (a) the inputObjects list and (b) the distance matrix.
	 */
	public void produceAdjMatrix(){
//		numObjects = numR + numC;
//		adjMatrix = new int[numObjects][numObjects];
//		
//		for (int i =0; i <numR; i++){
//			for (int j = 0; j < numC; j++){
//				adjMatrix[i][j+numR] = adjMfI[i][j];
//				adjMatrix[j+numR][i] = adjMfI[i][j];	//undirected
//			}
//		}
		
		numObjects = numTables + numQueries + numViews;
		adjMatrix = new int[numObjects][numObjects];
		adjMatrix = adjMatrixFromInput;
		/****************** i eva ta ekane comment ***************/
//		for (int i =0; i <numTables; i++){
//			for (int j = 0; j < numQueries; j++){
//					adjMatrix[i][j+numTables] = adjMatrixFromInput[i][j];
//					adjMatrix[j+numTables][i] = adjMatrixFromInput[i][j];	//undirected
//				}
//		}
//		
		
		
		
//		numObjects = numTables + numQueries;
//		adjMatrix = new int[numObjects][numObjects];
//		for (int i =0; i <numTables; i++){
//			for (int j = 0; j < numQueries; j++){
//				adjMatrix[i][j+numTables] = adjMatrixFromInput[i][j];
//				adjMatrix[j+numTables][i] = adjMatrixFromInput[i][j];	//undirected
//			}
//		}
//	
//		// The following is for reporting purposes
//		for (int i =0; i <numObjects; i++){
//			ClusterableObject co = inputObjects.get(i);
////			System.out.printf("Obj. %3s",co.getId() +"|");
//			for (int j = 0; j < numObjects; j++){
////				System.out.print(adjMatrix[i][j] + " ");
//			}
////			System.out.println();
//		}
	}

	public void produceDistanceMatrix(DistanceFunctionEnum df) throws IOException{
		distanceMatrix = new double [numObjects][numObjects];
		NodeDistanceFunction ndf = null;
		
		if (df == DistanceFunctionEnum.COMMON_NEIGHBORS){
			ndf = new CommonNeighborsDistance();
			distanceMatrix = ndf.computeAllDistances(adjMatrix, numObjects);
		}
		else{
			System.out.println("You are most welcome to build the required distance function :) ");
			System.exit(0);
		}
		
//		FileWriter fw = new FileWriter("/home/eva/distMatrix.ascii");
//		System.out.println("\n DISTANCE MATRIX");
		
//		for (int i = 0; i < numObjects; i++){
//			fw.write(i +"| ");
//			for (int j = 0; j < numObjects; j++){
//				fw.write(String.valueOf( distanceMatrix[i][j]) + " ");
//			}
//			fw.write("\n");
//				
//		}
//		fw.close();
	}
	private String viewNames[] = null;
	private String queryNames[] = null;
	private String tableNames[] = null;
	private int adjMatrix[][] = null;
	int adjMatrixFromInput[][] = null;
}
