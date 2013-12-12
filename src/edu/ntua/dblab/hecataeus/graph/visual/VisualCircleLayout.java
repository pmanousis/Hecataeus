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
import java.util.TreeMap;

import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.TopologicalTravel;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

public class VisualCircleLayout extends AbstractLayout<VisualNode, VisualEdge>{

	protected VisualGraph graph;
	
	private List<VisualNode> nodes;
	private List<VisualNode> queries = new ArrayList<VisualNode>();
	private List<VisualNode> relations = new ArrayList<VisualNode>();
	private List<VisualNode> views = new ArrayList<VisualNode>();
	private static int b = 0;
	protected static int c = 0,  a = 0, rCnt = 0;
	protected List<String> files = new ArrayList<String>();
	protected List<VisualNode> RQV = new ArrayList<VisualNode>();
	
	
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

	
	protected double getSmallRad(List<VisualNode> komboi){
		if(komboi.size()==1){
			return(Math.log(komboi.size()));
		}
		else{
			return(Math.log(Math.pow(komboi.size(),3))+2*komboi.size());
		}
	}
	protected double getQueryRad(int numOfNodes){
		return(Math.log(numOfNodes*numOfNodes*numOfNodes)+2*numOfNodes);
	}
	
	
	protected ArrayList<VisualNode> relationsInCluster(List<VisualNode> nodes){
		ArrayList<VisualNode> relations = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_RELATION){
				
				relations.add(v);
			}
		}
		
		return relations;
	}
	
	protected ArrayList<VisualNode> queriesInCluster(List<VisualNode> nodes){
		ArrayList<VisualNode> queries = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				queries.add(v);
			}
		}
		return queries;
	}
	
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
	
	
	//place queries 
	protected double placeQueries(ArrayList<VisualNode> queriesforR,  double cx, double cy, double qRad, double qAngle, int Q) {
		
		double sAngle = 0.0;
		double Angle = ((2 * Math.PI ) / Q);  
		//qRad = getQueryRad(Q)*1.1;
		for(VisualNode q : queriesforR){
						
			Point2D coord = transform(q);
			sAngle+=Angle;
//			if(Angle*a>Math.toRadians(180)){
//				coord.setLocation(Math.cos((-1)*Angle*a)*qRad+cx, Math.sin((-1)*Angle*a)*qRad+cy);
//
//			}else{
//				coord.setLocation(Math.cos(Angle*a)*qRad+cx, Math.sin(Angle*a)*qRad+cy);
//			}
			coord.setLocation(Math.cos(Angle*a)*qRad+cx, Math.sin(Angle*a)*qRad+cy);

			q.setLocation(coord);
			q.setNodeAngle(Angle*a);
			a++;
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(q, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		
		return sAngle;
		
	}
	
	protected ArrayList<VisualNode> queriesWithViews(ArrayList<VisualNode> queriesInCluster){
		ArrayList<VisualNode> myQ = new ArrayList<VisualNode>();
		for(VisualNode v : queriesInCluster){
			ArrayList<VisualEdge> mye = new ArrayList<VisualEdge>(v.getOutEdges());
			boolean cnt = false;
			for(VisualEdge m: mye){
				if(m.getToNode().getType()== NodeType.NODE_TYPE_RELATION){
					cnt=true;
					break;
				}
			}
			if(!cnt){
				myQ.add(v);
			}
		}
		return myQ;
	}
	
	protected void placeQueriesWithViews(ArrayList<VisualNode> queriesInCluster, double cx, double cy, double myRad){
		myRad = myRad + 80;
		
		
		ArrayList<VisualNode> myQ = new ArrayList<VisualNode>();
		for(VisualNode v : queriesInCluster){
			ArrayList<VisualEdge> mye = new ArrayList<VisualEdge>(v.getOutEdges());
			boolean cnt = false;
			for(VisualEdge m: mye){
				if(m.getToNode().getType()== NodeType.NODE_TYPE_RELATION){
					cnt=true;
					break;
				}
			}
			if(!cnt){
				myQ.add(v);
			}
		}
		int e = 0;
		double Angle = 2*Math.PI/myQ.size();
		for(VisualNode n : myQ){
			Point2D coord = transform(n);
			coord.setLocation(Math.cos(Angle*e)*myRad+cx, Math.sin(Angle*e)*myRad+cy);
	
			n.setLocation(coord);
			n.setNodeAngle(Angle*e);
			e++;
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(n, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		
	}
	
	//place single views
	protected double placeSingleViews(ArrayList<VisualNode> viewsforR,  double cx, double cy, double qRad, int Q) {
		
		double sAngle = 0.0;
		double Angle = ((2 * Math.PI ) / Q);   //+qAngle;
		for(VisualNode q : viewsforR){
						
			Point2D coord = transform(q);
			sAngle+=Angle;
			coord.setLocation(Math.cos(Angle*b)*qRad+cx, Math.sin(Angle*b)*qRad+cy);

			q.setLocation(coord);
			q.setNodeAngle(Angle*b);
			b++;
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(q, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		
		return sAngle;
		
	}
	
	protected ArrayList<VisualNode> getMultipleRViews(ArrayList<VisualNode> viewsInCluster){
		ArrayList<VisualNode> multyViews = new ArrayList<VisualNode>();
		
		for(VisualNode v : viewsInCluster){
			int cnt = 0;
			ArrayList<VisualEdge> edges = new ArrayList<VisualEdge>(v.getOutEdges());
			for(VisualEdge e : edges){
				if(e.getToNode().getType() == NodeType.NODE_TYPE_VIEW){
					multyViews.add(v);
				}
				else if(e.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
					cnt++;
				}
			}
			if(cnt > 1){
				multyViews.add(v);
			}
		}
		return multyViews;
	}
	
	protected Map<Double, VisualNode> sortViewsById(ArrayList<VisualNode> mv){
		TreeMap<Double, VisualNode> idViews = new TreeMap<Double, VisualNode>();
		for(VisualNode v : mv){
			idViews.put(v.ID,v);
		}
		Map<Double, VisualNode>  ReverseOrderedMap = new TreeMap<Double, VisualNode>(idViews);
		return ReverseOrderedMap;
	}
	
	protected Map<Double, VisualNode> viewRad(Map<Double, VisualNode> viewsId, double startRad){
		double cnt = startRad*2;
		Map<Double, VisualNode>  myMap = new TreeMap<Double, VisualNode>();
		
		for(Map.Entry<Double, VisualNode> entry : viewsId.entrySet()){
			myMap.put(cnt, entry.getValue());
			cnt-=10;
		}
		
		return myMap;
	}
	protected double getViewBandSize(ArrayList<VisualNode> mv, double relRad){
		if(mv.size()<1){
			return 0;
		}else{
			TreeMap<Double, ArrayList<VisualNode>> myViews = new TreeMap<Double, ArrayList<VisualNode>>();
			TopologicalTravel tt = new TopologicalTravel(graph);
			//tt.travelLevel();
			myViews = tt.travelLevel();

			double size = myViews.lastKey();
			return relRad/3*size+relRad*1.25;
		}
	}
	
	
	protected void placeMultyViews(ArrayList<VisualNode> mv, double cx, double cy , double l0Rad){
		TreeMap<Double, VisualNode> myViews = new TreeMap<Double, VisualNode>(sortViewsById(mv));
		TreeMap<Double, ArrayList<VisualNode>>  myV = new TreeMap<Double, ArrayList<VisualNode>> ();
		
		if(mv.size()>1){
			TopologicalTravel tt = new TopologicalTravel(graph);
			//tt.travelLevel();
			myV = tt.travelLevel();
		}
		
		
		double Angle = ((2 * Math.PI ) / mv.size());   //+qAngle;
		double vRad;
		for(Map.Entry<Double, ArrayList<VisualNode>> entry : myV.entrySet()){
			for( VisualNode viewN :entry.getValue()){
				
			
			//System.out.println(entry.getKey() + "/" + entry.getValue());
			vRad = entry.getKey()*l0Rad/3+l0Rad*1.25;

			//System.out.println("VRAD  " + vRad);
			Point2D coord = transform(viewN);
			coord.setLocation(Math.cos(Angle*c)*vRad+cx, Math.sin(Angle*c)*vRad+cy);

			viewN.setLocation(coord);
			viewN.setNodeAngle(Angle*c);
			c++;
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexShapeTransformer(new  VisualNodeShape());
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(viewN, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
			}
		}
	}
	//place views
	protected void placeViews(ArrayList<VisualNode> vc, double relationRad, double queryRad, double cx, double cy){
		double viewRad = (queryRad + relationRad)/2;
		double angle = (2*Math.PI)/vc.size();
		int va =0;
		double myAngle = 0;
		double d = 0.0;
		for(VisualNode v : vc){
			ArrayList<VisualNode> myQ = new ArrayList<VisualNode>(getMyQueries(v));
			if(myQ.size()>1){
				for(VisualNode mq : myQ){
					myAngle += mq.getNodeAngle();
				}
				myAngle = myAngle/2 + d;
				d+=0.09;
			}
			else{
				myAngle = angle*va;
			}
			Point2D coord = transform(v);
			//coord.setLocation((Math.cos(angle*va)*viewRad + cx), (Math.sin(angle*va)*viewRad + cy));
			coord.setLocation((Math.cos(myAngle)*viewRad + cx), (Math.sin(myAngle)*viewRad + cy));
			v.setLocation(coord);
			v.setNodeAngle(myAngle);
			va++;
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		
	}
	
	
	
	
	protected ArrayList<VisualNode> getMyQueries(VisualNode v){
		ArrayList<VisualNode> q = new ArrayList<VisualNode>();
		
		ArrayList<VisualEdge> edges = new ArrayList<VisualEdge>(v.getOutEdges());
		for(VisualEdge e : edges){
			if(e.getToNode().getType() == NodeType.NODE_TYPE_QUERY){
				q.add(e.getToNode());
			}
		}
		
		return q;
	}
	//posa q rotane panw apo mia r
	protected ArrayList<VisualNode> outQ(List<VisualNode> nodes){
		int queriesInCluster = 0;
		ArrayList<VisualNode> q = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				int cnt = 0;
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v.getOutEdges());
				for(VisualEdge e : edges){
					if(e.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
						cnt++;
						if(cnt > 1){
							if(!q.contains(v)){
								q.add(v);
							}
						}
					}
				}
			}
		}
		//queriesInCluster = q.size();
		return q;
	}
	
	protected ArrayList<VisualNode> getSortedArray(Map<ArrayList<VisualNode>, Integer> sorted, ArrayList<VisualNode>rc){
		ArrayList<VisualNode> sortedR = new ArrayList<VisualNode>();
		for(Entry e : sorted.entrySet()){
		//	System.out.println("print  " + e.getValue()+"   " +e.getKey());
			ArrayList<VisualNode> temp = ((ArrayList<VisualNode>)e.getKey());
			for(VisualNode node : temp){
				if(!sortedR.contains(node)){
					sortedR.add(node);
				}
			}
		}
		for(VisualNode node1 : rc){
			if(!sortedR.contains(node1)){
				sortedR.add(node1);
			}
		}
		
		return sortedR;
	}
	
	protected ArrayList<VisualNode>getQueriesforR(VisualNode relationNode){
		ArrayList<VisualNode> queriesforR = new ArrayList<VisualNode>();
		for(VisualEdge e : relationNode.getInEdges()){
            if(e.getFromNode().getType() == NodeType.NODE_TYPE_QUERY ){
                    VisualNode q = e.getFromNode();
                    ArrayList<VisualEdge> qEdges = new ArrayList<VisualEdge>(q.getOutEdges());
                    int cnt = 0;
                    for(VisualEdge ed : qEdges){
                            if(ed.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
                                    cnt++;
                            }
                    }
                    if(cnt==1){
                            queriesforR.add(q);
                    }
                    
            }
		}
		return queriesforR;
	}
	//single relation views
	//layer 0
	protected ArrayList<VisualNode>getViewsforR(VisualNode relationNode){
		ArrayList<VisualNode> viewsforR = new ArrayList<VisualNode>();
		
		for(VisualEdge e : relationNode.getInEdges()){
			if(e.getFromNode().getType() == NodeType.NODE_TYPE_QUERY ){
				VisualNode q = e.getFromNode();
				ArrayList<VisualEdge> qEdges = new ArrayList<VisualEdge>(q.getOutEdges());
				int cnt = 0;
				for(VisualEdge ed : qEdges){
					if(ed.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
						cnt++;
					}
				}
				if(cnt==1){
					viewsforR.add(q);
				}
			}
		}
		return viewsforR;
	}
	
	protected void placeRelation(VisualNode r, double qAngle, double sAngle, double relationRad, double cx, double cy, double newAngle){
		Point2D coord = transform(r);
		double rx = 0;
		double ry = 0;
		
		rx = Math.cos(sAngle-(qAngle/2))*relationRad+(cx);
		ry = Math.sin(sAngle-(qAngle/2))*relationRad+(cy);
//		rx = Math.cos(newAngle*rCnt)*relationRad+(cx);
//		ry = Math.sin(newAngle*rCnt)*relationRad+(cy);
		rCnt++;
		coord.setLocation(rx, ry);
		r.setLocation(coord);
		r.setNodeAngle(sAngle-(qAngle/2));
	
		HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(r, HecataeusViewer.getActiveViewer().getPickedVertexState()));
		HecataeusViewer.getActiveViewer().repaint();
	}
	
	
	protected void placeOutQueries(List<VisualNode> nodes, double qRad, double cx, double cy){
		double jqRad = qRad + 40;
		ArrayList<VisualNode>  allR = new ArrayList<VisualNode>();
		for(VisualNode v : outQ(nodes)){
			double c = 0;
			ArrayList<VisualNode> myR = new ArrayList<VisualNode>();
			for(VisualEdge myEdge : v.getOutEdges()){
				if(myEdge.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
					myR.add(myEdge.getToNode());
					allR.add(myEdge.getToNode());
				}
			}
			
			double myAngle = 0.0;
			for(VisualNode rel : myR){
//				if(myAngle + rel.getNodeAngle() < 2*Math.PI){
//					myAngle += rel.getNodeAngle();
//				}else{
//					//myAngle += 2*Math.PI - rel.getNodeAngle();
//					myAngle = Math.PI +(myAngle + rel.getNodeAngle());
//				}
				myAngle += rel.getNodeAngle();
			}
		//	if(allR.contains(myR)){
		//		c += 0.09;
		//	}
			if(myAngle<2*Math.PI){
				myAngle = myAngle/2 + c;
			}
			else{
				myAngle = myAngle + c;
			}
//			myAngle = myAngle/2 + c;
			
			c += 0.09;
//			if(myAngle>Math.toRadians(180)){
//				myAngle = 2*Math.PI-myAngle;
//			}
			Point2D coord = transform(v);
			double jqx = Math.cos(myAngle)*jqRad+(cx);
			double jqy = Math.sin(myAngle)*jqRad+(cy);
			coord.setLocation(jqx, jqy);

			v.setLocation(coord);
			v.setNodeAngle(myAngle);
//			allR.addAll(myR);
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
	}
	
	protected Map<ArrayList<VisualNode>, Integer> getRSimilarity(ArrayList<VisualNode> qc){
		Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>();
		
		for(VisualNode q : qc){
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
	
	protected Map<ArrayList<VisualNode>, Integer> getVSimilarity(ArrayList<VisualNode> vc){
		Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>();
		
		for(VisualNode v : vc){
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
	
	protected void placeRelations(ArrayList<VisualNode>rc, double cx, double cy){
		double smallRad = 1.3*getSmallRad(rc);
		double angle = (2 * Math.PI ) / rc.size();
		int cnt = 0;
		for(VisualNode v : rc){
			Point2D coord = transform(v);
			coord.setLocation(Math.cos(angle*cnt)*smallRad+(cx),Math.sin(angle*cnt)*smallRad+(cy));
			v.setLocation(coord);
			v.setNodeAngle(angle*cnt);
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
			cnt++;
		}
		
	}
	
  protected void drawCircles(List<VisualNode> nodes, double cx, double cy){
		int b = 0;
		ArrayList<VisualNode> rc = new ArrayList<VisualNode>();
		ArrayList<VisualNode> qc = new ArrayList<VisualNode>();
		ArrayList<VisualNode> vc = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_RELATION){
				rc.add(v);
				double smallRad = 1.3*getSmallRad(relationsInCluster(nodes));
				Point2D coord = transform(v);
				double angleA = (2 * Math.PI ) / relationsInCluster(nodes).size();
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
				v.setLocation(coord);
				HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
				HecataeusViewer.getActiveViewer().repaint();
			}else{
				if(v.getType() == NodeType.NODE_TYPE_QUERY){
					qc.add(v);
				}
				else if(v.getType() == NodeType.NODE_TYPE_VIEW){
					vc.add(v);
				}
				double smallRad = getSmallRad(nodes);
				Point2D coord = transform(v);
				double angleA = 0.0;
				if(relationsInCluster(nodes).size() > 1){
					angleA = (2 * Math.PI ) / (nodes.size()-relationsInCluster(nodes).size());
				}else{
					angleA = (2 * Math.PI ) / nodes.size();
				}
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
				v.setLocation(coord);
				HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
				HecataeusViewer.getActiveViewer().repaint();
			}
			b++;
		}
		
		
	}
  
}