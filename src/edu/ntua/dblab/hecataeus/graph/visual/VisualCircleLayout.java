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

import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

public class VisualCircleLayout extends AbstractLayout<VisualNode, VisualEdge>{

	protected VisualGraph graph;
	
	private List<VisualNode> nodes;
	private List<VisualNode> queries = new ArrayList<VisualNode>();
	private List<VisualNode> relations = new ArrayList<VisualNode>();
	private List<VisualNode> views = new ArrayList<VisualNode>();
	
	private static int a = 0;
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
		
		VisualFileColor vfc = new VisualFileColor();
		vfc.setFileNames(files);
		
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
	
	public List<String> getFileNames(){
		if(files!=null){
			return files;
		}
		else{
			return null;
		}
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
		double Angle = ((2 * Math.PI ) / Q);   //+qAngle;
		qRad = getQueryRad(Q)*1.1;
		for(VisualNode q : queriesforR){
						
			Point2D coord = transform(q);
			sAngle+=Angle;
			coord.setLocation(Math.cos(Angle*a)*qRad+cx, Math.sin(Angle*a)*qRad+cy);

			q.setLocation(coord);
			q.setNodeAngle(Angle*a);
			a++;
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(q, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		
		return sAngle;
		
	}
	
	//place views
	protected void placeViews(ArrayList<VisualNode> vc, double relationRad, double queryRad, double cx, double cy){
		double viewRad = (queryRad + relationRad)/2;
		double angle = (2*Math.PI)/vc.size();
		int va =0;
		for(VisualNode v : vc){
			Point2D coord = transform(v);
			coord.setLocation((Math.cos(angle*va)*viewRad + cx), (Math.sin(angle*va)*viewRad + cy));
			v.setLocation(coord);
			v.setNodeAngle(angle);
			va++;
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		
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
	
	protected void placeRelation(VisualNode r, double qAngle, double sAngle, double relationRad, double cx, double cy){
		Point2D coord = transform(r);
		double rx = 0;
		double ry = 0;
		rx = Math.cos(sAngle-(qAngle/2))*relationRad+(cx);
		ry = Math.sin(sAngle-(qAngle/2))*relationRad+(cy);
		coord.setLocation(rx, ry);
		r.setLocation(coord);
		r.setNodeAngle(sAngle-(qAngle/2));
	
		HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(r, HecataeusViewer.getActiveViewer().getPickedVertexState()));
		HecataeusViewer.getActiveViewer().repaint();
	}
	
	
	protected void placeOutQueries(List<VisualNode> nodes, double qRad, double cx, double cy){
		double jqRad = qRad + 40;
		double c = 0;
		for(VisualNode v : outQ(nodes)){
			ArrayList<VisualNode> myR = new ArrayList<VisualNode>();
			for(VisualEdge myEdge : v.getOutEdges()){
				if(myEdge.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
					myR.add(myEdge.getToNode());
				}
			}
			double myAngle = 0.0;
			for(VisualNode rel : myR){
				myAngle += rel.getNodeAngle();
			}
			myAngle = myAngle/2 + c;
			c += 0.09;
			
			Point2D coord = transform(v);
			double jqx = Math.cos(myAngle)*jqRad+(cx);
			double jqy = Math.sin(myAngle)*jqRad+(cy);
			coord.setLocation(jqx, jqy);

			v.setLocation(coord);
			v.setNodeAngle(myAngle);
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
}
