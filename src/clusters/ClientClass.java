package clusters;

public class ClientClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Complete this
		String introLine = new String("This does hierarchical agglomerative clustering; must decide the input!!!");
		System.out.println(introLine);
		HAggloEngine engine = new HAggloEngine(); 
		//System.out.println("Size is " + engine.getInputSize());
		//engine.printResultsConsole();
			   
		engine.executeParser();
		engine.buildFirstSolution();
		engine.execute();
	}

}
