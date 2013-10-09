package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class VisualDebianCircleLayout extends VisualCircleLayout{

	protected VisualGraph graph;
	private List<VisualNode> queries;
	private List<VisualNode> relations;
	private List<VisualNode> views;
	private ClusterSet cs;
	
	
	protected List<String> files;
	private List<VisualNode> RQV;
	protected VisualCircleLayout vcl;
	
	
	protected VisualDebianCircleLayout(VisualGraph g) {
		super(g);
		this.graph = g;
		
		vcl = new VisualCircleLayout(this.graph);
		
		queries = new ArrayList<VisualNode>(vcl.queries);
		relations = new ArrayList<VisualNode>(vcl.relations);
		views = new ArrayList<VisualNode>(vcl.views);
		
		RQV = new ArrayList<VisualNode>(vcl.RQV);
		files = new ArrayList<String>(vcl.files);
		
	}
	
	private void circles(List<VisualNode> nodes, double cx, double cy){
		int b = 0;
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_RELATION){
				
				double smallRad = getSmallRad(relationsInCluster(nodes));
				Point2D coord = transform(v);
				double angleA = (2 * Math.PI ) / relationsInCluster(nodes).size();
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
				HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
				HecataeusViewer.vv.repaint();
			}else{
				double smallRad = getSmallRad(nodes);
				Point2D coord = transform(v);
				double angleA = 0.0;
				if(relationsInCluster(nodes).size() > 1){
					angleA = (2 * Math.PI ) / (nodes.size()-relationsInCluster(nodes).size());
				}else{
					angleA = (2 * Math.PI ) / nodes.size();
				}
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
				HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
				HecataeusViewer.vv.repaint();
			}
			b++;
		}
		
	}
	
	
	
	private void debianClusters(){
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		ArrayList<ArrayList<VisualNode>> vertices = new ArrayList<ArrayList<VisualNode>>();       //lista me ta clusters 
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}
		double myRad = 10.0;
		//taksinomei tin lista --> prwta ta relations meta ta upoloipa k briskei aktina
		for(ArrayList<VisualNode> lista : vertices){
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
		}
		
		int a = 0;double angle = 0.0;
		for(ArrayList<VisualNode> lista : vertices){
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
			angle = (2 * Math.PI)/ lista.size();

			
			double cx = Math.cos(angle) * myRad+lista.size() ;
			double cy = Math.sin(angle) * myRad+lista.size();

			System.out.println("   angle  "+ angle + "  cx  " + cx + "   cy  " +cy);
			circles(nodes, cx, cy);
			
			myRad+=getSmallRad(lista)*2.5;
			a++;
		}
	}
	
	
	@Override
	public void initialize() {
		HAggloEngine engine = new HAggloEngine(this.graph);
		VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);		
		engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
		engine.buildFirstSolution();
		cs = engine.execute(1);
		debianClusters();
		
	}

	@Override
	public void reset() {
		initialize();
	}

}
