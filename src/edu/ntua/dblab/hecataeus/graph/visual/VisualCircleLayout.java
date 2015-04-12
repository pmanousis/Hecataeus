package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import clusters.EngineConstructs.Cluster;
import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.TopologicalTravel;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

/**
 * @author eva
 * places every node inside a cluster in the circle it belongs
 */

public class VisualCircleLayout extends AbstractLayout<VisualNode, VisualEdge>{

	protected VisualGraph graph;
	private List<VisualNode> nodes;
	private List<VisualNode> queries = new ArrayList<VisualNode>();
	private List<VisualNode> relations = new ArrayList<VisualNode>();
	private List<VisualNode> views = new ArrayList<VisualNode>();
	private int singleQueriesCounter = 0;
	protected List<String> files = new ArrayList<String>();
	protected List<VisualNode> RQV = new ArrayList<VisualNode>();
	private List<List<VisualNode>> groups = new ArrayList<List<VisualNode>>();

	/**
	 * places nodes in lists with respect to their type
	 */
	public VisualCircleLayout(VisualGraph g) {
		super(g);
		this.graph = g;
		nodes = new ArrayList<VisualNode>((Collection<? extends VisualNode>) g.getVertices());
		for(VisualNode v : nodes){
			if(v.getType().getCategory() == NodeCategory.MODULE ){
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v._inEdges);
				for(VisualEdge e : edges){
					if(e.getType() == EdgeType.EDGE_TYPE_CONTAINS){
						if(files.contains(e.getFromNode().getName())==false){
							files.add(e.getFromNode().getName());
						}
					}
				}
			}
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				getQueries().add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_RELATION){
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v._inEdges);
				for(VisualEdge e : edges){
					if(e.getType() == EdgeType.EDGE_TYPE_USES){
						if(getRelations().contains(v)==false){
							getRelations().add(v);
						}
					}
				}
			}
			else if(v.getType() == NodeType.NODE_TYPE_VIEW){
				getViews().add(v);
			}
		}
		for(VisualNode r : getRelations()){
			for(int i =0; i < r.getInEdges().size(); i++){
				if(r.getInEdges().get(i).getType()== EdgeType.EDGE_TYPE_USES){
					if(RQV.contains(r) == false){
						RQV.add(r);
					}
				}
			}
		}
		for(VisualNode q: getQueries()){
			RQV.add(q);
		}
		for(VisualNode v:getViews()){
			if(RQV.contains(v)==false){
				RQV.add(v);
			}
		}
	}

	/**
	 * calculates the radius of a circle for a number of nodes
	 * @param number of nodes
	 * @return radius of the cicle for these nodes
	 */
	protected double getSmallRad(List<VisualNode> komboi){
		if(komboi.size()==1)
		{
			return(0);
		}
		return(1.4 * 3 * (Math.log(komboi.size()) + komboi.size()));
	}
	
	protected double getQueryRad(int numOfNodes){
		return(3 * (Math.log(numOfNodes) + numOfNodes));
	}
	
	/**
	 * @param nodes
	 * @return nodes that are of type relation
	 */
	protected ArrayList<VisualNode> relationsInCluster(List<VisualNode> nodes){
		ArrayList<VisualNode> relations = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_RELATION){
				
				relations.add(v);
			}
		}
		return relations;
	}

	/**
	 * @author pmanousi
	 * @param nodes The nodes of the cluster
	 * @return The queries that use more than one relations, or the queries that use views.
	 */
	protected ArrayList<VisualNode> multiInputOrViewUsingQueriesInCluster(List<VisualNode> nodes)
	{
		ArrayList<VisualNode> queries = new ArrayList<VisualNode>();
		for(VisualNode v : nodes)
		{
			if(v.getType() == NodeType.NODE_TYPE_QUERY)
			{
				if(v.getNumberOfUsesEdges() > 1)
				{	// multiInput
					queries.add(v);
					continue;
				}
				for(VisualEdge e: v.getOutEdges())
				{
					if(e.getToNode().getType() == NodeType.NODE_TYPE_VIEW)
					{	// view usage
						queries.add(v);
						break;
					}
				}
			}
		}
		return queries;
	}
	
	/**
	 * @param nodes
	 * @return nodes that are of type view
	 */
	protected ArrayList<VisualNode> viewsInCluster(List<VisualNode> nodes){
		ArrayList<VisualNode> views = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_VIEW){
				views.add(v);
			}
		}
		return views;
	}
	
	@Override
	public void initialize() {
	}

	@Override
	public void reset() {
	}


	protected List<VisualNode> getRelations() {
		return relations;
	}


	protected void setRelations(List<VisualNode> relations) {
		this.relations = relations;
	}


	protected List<VisualNode> getQueries() {
		return queries;
	}


	protected void setQueries(List<VisualNode> queries) {
		this.queries = queries;
	}


	protected List<VisualNode> getViews() {
		return views;
	}


	protected void setViews(List<VisualNode> views) {
		this.views = views;
	}
	
	/**
	 * @param unsortedMap
	 * @return sorted map by value
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map<ArrayList<VisualNode>, Integer> sortByComparator(Map<ArrayList<VisualNode>, Integer> unsortedMap) {
		List list = new LinkedList(unsortedMap.entrySet());
		Collections.sort(list, new Comparator() {
		public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
	 * Places the nodes to:
	 * a) A circle that contains the queries of the cluster, that use only one relation
	 * b) A circle that has the relations of the cluster, based on the placement of the single relation queries
	 * c) A circular disc that has the views that use the relations of the cluster, this disk is between the relations and the single relation queries
	 * d) A circle made of "cloud" like points that has the queries that use either (i) views or (ii) more than one relations
	 * @author pmanousi
	 * @param nodes The nodes of a cluster
	 * @param clusterCenter The center of the cluster circle
	 */
	protected void circles(List<VisualNode> nodes, Point2D clusterCenter){
		relations.clear();
		views.clear();
		queries.clear();
		groups.clear();
        ArrayList<VisualNode> singleRelationQueries = new ArrayList<VisualNode>();
        relations.addAll(relationsInCluster(nodes));
        for(VisualNode relation: relations)
        {
        	singleRelationQueries.addAll(getSingleTableQueriesOfRelation(relation));
        }
        queries.addAll(multiInputOrViewUsingQueriesInCluster(nodes));
        views.addAll(viewsInCluster(nodes));
        Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>(getRSimilarity(queries));
        Map<ArrayList<VisualNode>, Integer> Vset = new HashMap<ArrayList<VisualNode>, Integer>(getVSimilarity(views));
        if(relationsInCluster(nodes).size()>3)
        {
        	Map<ArrayList<VisualNode>, Integer> sorted = sortByComparator(set);
        	Map<ArrayList<VisualNode>, Integer> Vsorted = sortByComparator(Vset);
            ArrayList<VisualNode> sortedR = new ArrayList<VisualNode>(getSortedArray(sorted, relations));
            ArrayList<VisualNode> sortedV = new ArrayList<VisualNode>(getSortedArray(Vsorted, views));
            relations.clear();
            views.clear();
            relations.addAll(sortedR);
            views.addAll(sortedV);
        }
        double relationRad = 1.9*getSmallRad(relations);
        double singleRelationQueriesRad = getQueryRad(singleRelationQueries.size());
        double maxViewRad = getMaxViewRadius(views, relationRad);
        if(singleRelationQueriesRad < maxViewRad + (relationRad * 0.5)){
        	singleRelationQueriesRad = maxViewRad + (relationRad * 0.5);
        }
        if(singleRelationQueriesRad < relationRad * 2){
        	singleRelationQueriesRad = relationRad * 2;
        }
        double relationAngle = 0;
        for(VisualNode r : relations)
        {
			if(getSingleTableQueriesOfRelation(r).size() > 0)
			{
				placeQueries(getSingleTableQueriesOfRelation(r), clusterCenter, singleRelationQueriesRad, singleRelationQueries.size());
				relationAngle = (getSingleTableQueriesOfRelation(r).get(getSingleTableQueriesOfRelation(r).size()-1).getNodeAngle() + getSingleTableQueriesOfRelation(r).get(0).getNodeAngle()) / 2.0;	// Barycentered placement of the relation node, based on the single table queries.
			}
        	placeRelation(r, relationAngle, relationRad, clusterCenter, getSingleTableQueriesOfRelation(r).size());
        }
        if(views.size() > 0)
        {
        	placeMultyViews(views, relationRad, clusterCenter);
        }
        if(queries.size() > 0)
        {
        	placeOutQueries(queries, singleRelationQueriesRad, clusterCenter);
        }
	}
	
	/**
	 * @param views
	 * @param clusterCenter
	 * @param l0Rad
	 */
	protected void placeMultyViews(List<VisualNode> views, double l0Rad, Point2D clusterCenter)
	{
		List<Point2D> usedPoints = new ArrayList<Point2D>();
		TopologicalTravel tt = new TopologicalTravel(graph);
		double vRad;
		for(Entry<Integer, List<VisualNode>> entry : tt.viewStratificationLevels().entrySet())
		{
			for(VisualNode stratifiedView : entry.getValue())
			{
				double angle = 0;
				int angleCounter = 0;
				for(VisualEdge e : stratifiedView.getOutEdges())
				{
					if(e.getToNode().getType().getCategory() == NodeCategory.MODULE)
					{
						angle += e.getToNode().getNodeAngle();
						angleCounter++;
					}
				}
				angle /= angleCounter;
				vRad = (entry.getKey() + 1.5) * l0Rad ;
				Point2D coord = transform(stratifiedView);
				coord.setLocation(Math.cos(angle)*vRad+clusterCenter.getX(), Math.sin(angle)*vRad+clusterCenter.getY());
				while(usedPoints.contains(coord) == true)
				{	// here I should change sth a little bit.
					coord.setLocation(addSomeNoise(coord, vRad*0.2));	// this should get some random X and Y modification
				}
				usedPoints.add(coord);
				stratifiedView.setLocation(coord);
				stratifiedView.setNodeAngle(angle);
			}
		}
	}

	/**
	 * Places the single table queries to a circle. This circle is guiding the placement of the relations in a later step.
	 * @author pmanousi
	 * @param queriesForThisRelation The queries that refer only to a specific relation (single table queries) in this cluster 
	 * @param clusterCenter The center of the cluster that these queries are part of
	 * @param qRad The radius of the single table queries circle in this cluster
	 * @param numberOfSingleRelationQueriesInTheCluster The number of single table queries of the whole cluster 
	 */
	protected void placeQueries(ArrayList<VisualNode> queriesForThisRelation,  Point2D clusterCenter, double qRad, int numberOfSingleRelationQueriesInTheCluster)
	{
		if(numberOfSingleRelationQueriesInTheCluster != 0)
		{
			double Angle = ((2 * Math.PI ) / numberOfSingleRelationQueriesInTheCluster);  
			for(VisualNode q : queriesForThisRelation)
			{
				Point2D coord = transform(q);
				coord.setLocation(Math.cos(Angle*singleQueriesCounter)*qRad+clusterCenter.getX(), Math.sin(Angle*singleQueriesCounter)*qRad+clusterCenter.getY());
				q.setLocation(coord);
				q.setNodeAngle(Angle*singleQueriesCounter);
				singleQueriesCounter++;
			}
		}
	}

	/**
	 * Places a relation to the internal circle of the relations, in a cluster. If the relation has no single table queries, then it is placed a little bit (20%) more towards the center of the cluster. 
	 * @author pmanousi
	 * @param relation The relation to be placed
	 * @param relationAnglePossition The angle that this relation has from the center of the cluster
	 * @param relationRad The radius of the relation circle in this cluster
	 * @param clusterCenter The center of the cluster that this relation is part of
	 * @param numberOfQueriesOnlyForThisRelation The number queries that refer to only this relation (single table queries) in order to modify the radius
	 */
	protected void placeRelation(VisualNode relation, double relationAnglePossition, double relationRad, Point2D clusterCenter, int numberOfQueriesOnlyForThisRelation)
	{
		Point2D coord = transform(relation);
		if(numberOfQueriesOnlyForThisRelation != 0)
		{
			coord.setLocation(Math.cos(relationAnglePossition)*relationRad+clusterCenter.getX(), Math.sin(relationAnglePossition)*relationRad+clusterCenter.getY());
		}
		else
		{
			coord.setLocation(Math.cos(relationAnglePossition)*relationRad*0.8+clusterCenter.getX(), Math.sin(relationAnglePossition)*relationRad*0.8+clusterCenter.getY());
		}
		relation.setLocation(coord);
		relation.setNodeAngle(relationAnglePossition);
	}

	protected double getMaxViewRadius(List<VisualNode> mv, double relRad)
	{
		if(mv.size()>1)
		{
			TopologicalTravel tt = new TopologicalTravel(graph);
			return(relRad * (tt.viewStratificationLevels().lastKey() + 1.5));
		}
		return(0);
	}

	protected ArrayList<VisualNode> getSortedArray(Map<ArrayList<VisualNode>, Integer> sorted, List<VisualNode> relations2){
		ArrayList<VisualNode> sortedR = new ArrayList<VisualNode>();
		for(Entry<ArrayList<VisualNode>, Integer> e : sorted.entrySet())
		{
			for(VisualNode node : e.getKey())
			{
				if(!sortedR.contains(node))
				{
					sortedR.add(node);
				}
			}
		}
		for(VisualNode node1 : relations2)
		{
			if(!sortedR.contains(node1))
			{
				sortedR.add(node1);
			}
		}
		return sortedR;
	}
	
	/**
	 * Gives the queries that ask only one table, the relationNode table.
	 * @author pmanousi
	 * @param relationNode The relation whose the single table queries we are searching  
	 * @return The queries that use only the relationNode relation
	 */
	protected ArrayList<VisualNode>getSingleTableQueriesOfRelation(VisualNode relationNode)
	{
		ArrayList<VisualNode> queriesforR = new ArrayList<VisualNode>();
		for(VisualEdge e : relationNode.getInEdges())
		{
            if(e.getFromNode().getType() == NodeType.NODE_TYPE_QUERY && e.getFromNode().getNumberOfUsesEdges() == 1)
            {
                queriesforR.add(e.getFromNode());
            }
		}
		return queriesforR;
	}

	/**
	 * Places the queries that use more than one relations, or they use a view. We have created groups of the queries that ask the same relations/views, and we place them in a "cloud" like area in this outer circle. 
	 * @author pmanousi
	 * @param nodes The queries that use more than one relations (or they use a view)
	 * @param qRad The radius of the single table queries
	 * @param clusterCenter The center of the cluster that these queries are part of
	 */
	protected void placeOutQueries(List<VisualNode> nodes, double qRad, Point2D clusterCenter)
	{
		List<Point2D> usedCoordinates = new ArrayList<Point2D>();
		double jqRad = qRad * 1.4;
		for(VisualNode v : queries)
		{
			double myAngle = 0.0;
			int numberOfRelationsForThisQuery = 0;
			for(VisualEdge myEdge : v.getOutEdges())
			{
				if(myEdge.getToNode().getType() == NodeType.NODE_TYPE_RELATION || myEdge.getToNode().getType() == NodeType.NODE_TYPE_VIEW)
				{
					myAngle += myEdge.getToNode().getNodeAngle();
					numberOfRelationsForThisQuery++;
				}
			}
			myAngle /= numberOfRelationsForThisQuery;
			Point2D coord = transform(v);
			coord.setLocation(Math.cos(myAngle)*jqRad+clusterCenter.getX(), Math.sin(myAngle)*jqRad+clusterCenter.getY());
			double counter = jqRad / 33.0;
			while(usedCoordinates.contains(coord) == true)
			{
				coord.setLocation(Math.cos(myAngle)*(jqRad+counter)+clusterCenter.getX(), Math.sin(myAngle)*(jqRad+counter)+clusterCenter.getY());
				counter += jqRad / 33.0;
			}
			v.setLocation(coord);
			usedCoordinates.add(v.getLocation());
			v.setNodeAngle(myAngle);
			boolean createGroupForThisNode = true;
			for(List<VisualNode> vnl: groups)
			{
				if(vnl != null && vnl.get(vnl.size()-1).getNodeAngle() == v.getNodeAngle())
				{	// already heaving a group for this angle 
					vnl.add(v);
					createGroupForThisNode = false;
					break;
				}
			}
			if(createGroupForThisNode)
			{	// we didn't have any group for this angle, so we create one
				List<VisualNode> group = new ArrayList<VisualNode>();
				group.add(v);
				groups.add(group);
			}
		}
		boolean finished = false;
		while(finished == false)
		{	// while new groups are created and have not been checked
			finished = true;
			List<VisualNode> newGroup = new ArrayList<VisualNode>();
			for(List<VisualNode> vnl: groups)
			{	// I am not sure if I should check only the last of the groups but never mind
				VisualNode firstNodeOfGroup = vnl.get(0);
				for(VisualNode v : vnl)
				{
					if(theyHaveSameProviders(v, firstNodeOfGroup) == false)
					{
						newGroup.add(v);
						continue;
					}
				}
				vnl.removeAll(newGroup);
			}
			if(newGroup.size()>0)
			{
				finished = false;
				groups.add(newGroup);
			}
		}
		double groupsAngle = 2 * Math.PI / groups.size();
		int counter = 0;
		for(List<VisualNode> vnl : groups)
		{
			Point2D groupPosition = new Point2D.Double(Math.cos(groupsAngle * counter) * jqRad + clusterCenter.getX(), Math.sin(groupsAngle * counter) * jqRad + clusterCenter.getY());
			for(VisualNode v : vnl)
			{	// place them in a "cloud"
				Point2D coord = transform(v);
				coord.setLocation(addSomeNoise(groupPosition, qRad*0.2));	// this should get some random X and Y modification
				v.setLocation(coord);
				v.setNodeAngle(groupsAngle * counter);	// this is unneeded
			}
			counter++;
		}
	}
	
	/**
	 * Adds some "noise" to the current possition of a node (in order to create a "cloud" for the nodes.
	 * @author pmanousi
	 * @param currentPossition The possition that this node has up to now
	 * @param spreadFactor A radius saying how far the node should be placed (now I have it to 20% of the radius of the single table queries
	 * @return The new possition for this node
	 */
	private Point2D addSomeNoise(Point2D currentPossition, Double spreadFactor)
	{
		Point2D toReturn = new Point2D.Double();
		Random randomGenerator = new Random();
		toReturn.setLocation(currentPossition.getX() + (spreadFactor) * randomGenerator.nextDouble() - spreadFactor / 2, currentPossition.getY() + (spreadFactor) * randomGenerator.nextDouble() - spreadFactor / 2);
		return toReturn;
	}
	
	/**
	 * Takes two nodes and searches if they have the same providers (relations or views or other queries -nested-).
	 * @author pmanousi
	 * @param v The node that we want to see if it has the same providers with the first one in the list of its group.
	 * @param fvn The first node in the list of its group.
	 * @return True if the two nodes have the exast same providers or falst otherwise.
	 */
	private boolean theyHaveSameProviders(VisualNode v, VisualNode fvn)
	{
		if(v == fvn)
		{
			return(true);
		}
		int howManyTimesToFindTrue = 0;
		if(v.getNumberOfUsesEdges() != fvn.getNumberOfUsesEdges())
		{
			return(false);
		}
		for(VisualEdge efvn : fvn.getOutEdges())
		{
			for(VisualEdge ev : v.getOutEdges())
			{
				if(ev.getToNode() == efvn.getToNode())
				{
					howManyTimesToFindTrue++;
					continue;
				}
			}
		}
		if(howManyTimesToFindTrue == v.getNumberOfUsesEdges())
		{
			return(true);
		}
		return(false);
	}
	
	protected Map<ArrayList<VisualNode>, Integer> getRSimilarity(List<VisualNode> queries2){
		Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>();
		
		for(VisualNode q : queries2){
			ArrayList<VisualEdge> qEdges = new ArrayList<VisualEdge>(q.getOutEdges());
			int toRelation = 0;
			for(VisualEdge ed : qEdges){
				if(ed.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
					toRelation++;
				}
			}
			if(toRelation > 1){
				ArrayList<VisualNode> QtR = new ArrayList<VisualNode>();
				for(VisualEdge ed : qEdges){
					if(ed.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
						QtR.add(ed.getToNode());
					}
				}
				if(set.containsKey(QtR)){
					set.put(QtR, set.get(QtR)+1);
				}else{
					set.put(QtR, 1);
				}
			}
		}
		return set;
	}
	
	protected double getMaxRadius(List<VisualNode> nodes)
	{
		relations.clear();
		views.clear();
		queries.clear();
		groups.clear();
        ArrayList<VisualNode> singleRelationQueries = new ArrayList<VisualNode>();
        relations.addAll(relationsInCluster(nodes));
        for(VisualNode relation: relations)
        {
        	singleRelationQueries.addAll(getSingleTableQueriesOfRelation(relation));
        }
        queries.addAll(multiInputOrViewUsingQueriesInCluster(nodes));
        views.addAll(viewsInCluster(nodes));
        Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>(getRSimilarity(queries));
        Map<ArrayList<VisualNode>, Integer> Vset = new HashMap<ArrayList<VisualNode>, Integer>(getVSimilarity(views));
        if(relationsInCluster(nodes).size()>3)
        {
        	Map<ArrayList<VisualNode>, Integer> sorted = sortByComparator(set);
        	Map<ArrayList<VisualNode>, Integer> Vsorted = sortByComparator(Vset);
            ArrayList<VisualNode> sortedR = new ArrayList<VisualNode>(getSortedArray(sorted, relations));
            ArrayList<VisualNode> sortedV = new ArrayList<VisualNode>(getSortedArray(Vsorted, views));
            relations.clear();
            views.clear();
            relations.addAll(sortedR);
            views.addAll(sortedV);
        }
        double relationRad = 1.9*getSmallRad(relations);
        double singleRelationQueriesRad = getQueryRad(singleRelationQueries.size());
        double maxViewRad = getMaxViewRadius(views, relationRad);
        if(singleRelationQueriesRad < maxViewRad + (relationRad * 0.5)){
        	singleRelationQueriesRad = maxViewRad + (relationRad * 0.5);
        }
        if(singleRelationQueriesRad < relationRad * 2){
        	singleRelationQueriesRad = relationRad * 2;
        }
		return(singleRelationQueriesRad * 2.4);
	}
	
	protected Map<ArrayList<VisualNode>, Integer> getVSimilarity(List<VisualNode> views2){
		Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>();
		
		for(VisualNode v : views2){
			ArrayList<VisualEdge> vEdges = new ArrayList<VisualEdge>(v.getOutEdges());
			int toRelation = 0;
			for(VisualEdge ed : vEdges){
				if(ed.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
					toRelation++;
				}
			}
			if(toRelation > 1){
				ArrayList<VisualNode> VtR = new ArrayList<VisualNode>();
				for(VisualEdge ed : vEdges){
					if(ed.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
						VtR.add(ed.getFromNode());
					}
				}
				if(set.containsKey(VtR)){
					set.put(VtR, set.get(VtR)+1);
				}else{
					set.put(VtR, 1);
				}
			}
		}
		return set;
	}
	
	/**
	 * Given a list of clusters, it sorts them, according their size, it creates small lists of 2^i segments and returns them to the caller function.
	 * @author pmanousi
	 * @param clusters The clusters of the graph.
	 * @return A sorted list that contains 2^i clusters in its i list-node.
	 */
	protected ArrayList<ArrayList<Cluster>> createTwoToISegments(List<Cluster> clusters)
	{
		Collections.sort(clusters, new ClusterComparator());
		ArrayList<ArrayList<Cluster>> listOfClusters = new ArrayList<ArrayList<Cluster>>();
		int segmentCounter = 0;	// from now we will take 2^conCircle clusters
		while(Math.pow(2, segmentCounter) < clusters.size())
		{
			double upperLimit = Math.pow(2, segmentCounter + 1);
			if(upperLimit > clusters.size())
			{
				upperLimit = clusters.size() + 1;
			}
			listOfClusters.add(new ArrayList<Cluster>(clusters.subList((int) (Math.pow(2, segmentCounter) - 1) /* whatever^0 = 1, so I perform a slide to take 0 from list of clusters*/, (int) (upperLimit - 1))));	// sublist does not take the upper limit
			segmentCounter++;
		}
		return(listOfClusters);
	}
}