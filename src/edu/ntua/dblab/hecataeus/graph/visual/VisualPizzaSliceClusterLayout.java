package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.HecataeusClusterMap;
import edu.ntua.dblab.hecataeus.HecataeusViewer;

public class VisualPizzaSliceClusterLayout extends VisualCircleLayout{

    
    protected VisualGraph graph;
    protected double endC;
    private List<VisualNode> queries;
    private List<VisualNode> relations;
    private List<VisualNode> views;
    private ClusterSet cs;
    private static int clusterId = 0;
    private double edgelenngthforGraph = 0;
    private VisualTotalClusters clusterList;
    protected List<String> files;
    private List<VisualNode> RQV;
    protected VisualCircleLayout vcl;
    
    protected VisualPizzaSliceClusterLayout(VisualGraph g, double endC) {
        super(g);
        this.graph = g;
        this.endC = endC;
        vcl = new VisualCircleLayout(this.graph);
        
        queries = new ArrayList<VisualNode>(vcl.getQueries());
        relations = new ArrayList<VisualNode>(vcl.getRelations());
        views = new ArrayList<VisualNode>(vcl.getViews());
        
        RQV = new ArrayList<VisualNode>(vcl.RQV);
        files = new ArrayList<String>(vcl.files);
        
    }

     private void circles(List<VisualNode> nodes, double cx, double cy){
            vcl.a = 0; vcl.rCnt=0; vcl.c = 0;
            int b = 0, relwithoutQ = 0;
            ArrayList<VisualNode> rc = new ArrayList<VisualNode>();
            ArrayList<VisualNode> qc = new ArrayList<VisualNode>();
            ArrayList<VisualNode> vc = new ArrayList<VisualNode>();
            
            rc.addAll(relationsInCluster(nodes));
            qc.addAll(queriesInCluster(nodes));
            vc.addAll(viewsInCluster(nodes));
            
            int singleQinCl = nodes.size() - rc.size() - outQ(nodes).size() - vc.size() - queriesWithViews(qc).size();
            Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>(getRSimilarity(qc));
            Map<ArrayList<VisualNode>, Integer> viewSet = new HashMap<ArrayList<VisualNode>, Integer>(getVSimilarity(vc));
            if(relationsInCluster(nodes).size()>3){
                Map<ArrayList<VisualNode>, Integer> sorted = sortByComparator(set);
                Map<ArrayList<VisualNode>, Integer> sortedViews = sortByComparator(viewSet);
                ArrayList<VisualNode> sortedR = new ArrayList<VisualNode>(getSortedArray(sorted, rc));
                ArrayList<VisualNode> sortedV = new ArrayList<VisualNode>(getSortedArray(sortedViews, vc));
                rc.clear();vc.clear();
                rc.addAll(sortedR);vc.addAll(sortedV);
                
            }
     
            double relationRad = 1.9*getSmallRad(rc);

            double qRad = getQueryRad(nodes.size() - rc.size()- vc.size());
            int Q = singleQinCl;
            double qAngle = 0;
            double sAngle = 0;
            double newAngle = 2*Math.PI/rc.size();
            ArrayList<VisualNode> multyV = new ArrayList<VisualNode>(vc);//getmultyViews
            double viewBand = getViewBandSize(multyV, relationRad);
            if(qRad <= viewBand){
                qRad = viewBand+40;
            }
            if(qRad <= relationRad){
                qRad = relationRad+40;
            }
            for(VisualNode r : rc){
                ArrayList<VisualNode> queriesforR = new ArrayList<VisualNode>(getQueriesforR(r));
                ArrayList<VisualNode> viewsforR = new ArrayList<VisualNode>(getViewsforR(r));
                qAngle = placeQueries(queriesforR, cx, cy, qRad, qAngle, Q);
                sAngle += qAngle;
                placeRelation(r, qAngle, sAngle, relationRad, cx, cy, newAngle);
            }
            placeOutQueries(nodes, qRad, cx, cy);
            placeQueriesWithViews(qc, cx, cy, qRad);
            placeMultyViews(multyV, cx, cy, relationRad+10);
            clusterId++;
            VisualCluster cluster = new VisualCluster(qRad, rc, vc, qc, cx, cy, clusterId);
            
            clusterList.addCluster(cluster);
            cluster.printInClusterEdges();
            edgelenngthforGraph += cluster.getLineLength();
        }
    
    protected void CirclingCusters(){
        clusterList = new VisualTotalClusters();
        clusterList.clearList();
        Dimension d = getSize();
        double w = d.getWidth();
        double h = d.getHeight();
        List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
        ArrayList<ArrayList<VisualNode>> vertices = new ArrayList<ArrayList<VisualNode>>();
        for(Cluster cl : clusters){
            vertices.add(cl.getNode());
        }
        ArrayList<ArrayList<VisualNode>> sortedV = new ArrayList<ArrayList<VisualNode>>();
        Collections.sort(vertices, new ListComparator());
        sortedV.addAll(vertices);
        
        double myRad = 1.0;
        
        ArrayList<ArrayList<ArrayList<VisualNode>>> sublistofClusters = new ArrayList<ArrayList<ArrayList<VisualNode>>>();
        while((int) Math.pow(2, myRad)<sortedV.size()){
            ArrayList<ArrayList<VisualNode>> tmpVl = new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, myRad-1), (int) Math.pow(2, myRad)));
            sublistofClusters.add(tmpVl);
            myRad++;
        }
        ArrayList<ArrayList<VisualNode>> tmpVl=new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, myRad-1), sortedV.size()));
        if(!tmpVl.isEmpty()){
            sublistofClusters.add(tmpVl);
        }
        sublistofClusters.add(0, new ArrayList<ArrayList<VisualNode>>(sortedV.subList(0, 1)));

        double bigCircleRad = 0.0;
        double bigClusterRad = 0.0;
        double tempCircleRad = 0.0;
        
        for(ArrayList<ArrayList<VisualNode>> listaC: sublistofClusters){
            ArrayList<ArrayList<VisualNode>> tmp ;
            if (sublistofClusters.indexOf(listaC)!=sublistofClusters.size()-1){
                tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)+1));
            }
            else{
                tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)));    
            }

            
            double periferia=0;
            for(ArrayList<VisualNode> lista : listaC)
            {
                periferia+=getSmallRad(lista);
            }
            
            tempCircleRad=bigCircleRad+periferia/Math.PI;
            if(tempCircleRad<bigCircleRad+getSmallRad(listaC.get(listaC.size()-1)))
            {
                tempCircleRad=bigCircleRad+getSmallRad(listaC.get(listaC.size()-1));
            }
            if(tempCircleRad<periferia)
            {
                bigCircleRad=periferia/0.90;
            }
            else
            {
                bigCircleRad=tempCircleRad;
            }
            
            double angle = 0.0, sum = 0.0;
            for(ArrayList<VisualNode> lista : listaC){
                List<VisualNode> nodes = new ArrayList<VisualNode>();
                Collections.sort(lista, new CustomComparator());
                nodes.addAll(lista);
                double temp =   (2*bigCircleRad*bigCircleRad - getSmallRad(nodes)*getSmallRad(nodes)*0.9)/(2*bigCircleRad*bigCircleRad );
                if(Math.abs(temp)>1){
                    temp = 1;
                }
                angle = (Math.acos(temp))*1.2;
                double cx = Math.cos((sum+angle/2)/2*Math.PI) * bigCircleRad*1.8 ;// 1.8 is used for white space borders
                double cy =    Math.sin((sum+angle/2)/2*Math.PI) * bigCircleRad*1.8 ;
                sum+=angle;
                circles(nodes, cx, cy);
            }
        }
        HecataeusViewer.getActiveViewer().repaint();
    }
    
    @Override
    public void initialize() {
        HAggloEngine engine = new HAggloEngine(this.graph);
        VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);
        engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
        engine.buildFirstSolution();
        cs = engine.execute(endC);
        CirclingCusters();
        HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(HecataeusViewer.getActiveViewer().getPickedVertexState()));
		HecataeusViewer.getActiveViewer().repaint();
		HecataeusViewer.hecMap.createMap();
    }

    @Override
    public void reset() {
        initialize();
    }

}