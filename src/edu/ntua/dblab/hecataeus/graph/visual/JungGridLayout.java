package edu.ntua.dblab.hecataeus.graph.visual;





import java.awt.Dimension;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

public class JungGridLayout extends JFrame {

Graph graph;
StaticLayout layout;
VisualizationViewer vv;

public static void main(String[] args) {
    JungGridLayout g = new JungGridLayout(25,5,5);
}

public JungGridLayout(int numNodes, int numRows, int numColumns) {
    graph = new SparseMultigraph();
    layout = new StaticLayout(graph);

    //distance between the nodes
    int distX=100;
    int distY=100;

    //idea is to add the vertices and change and the position of each vertex to a coordinate in a grid
    for (int n=0;n<numNodes;n++) {
        graph.addVertex(String.valueOf(n));
    }

    int operatingNode = 0;

    for (int i=0;i<numRows;i++) {
        for (int j=0;j<numColumns;j++) {
            layout.setLocation(String.valueOf(operatingNode++), i*distX, j*distY);
        }
    }        

    createVisualization();
    createFrame();
}

public void createFrame() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setVisible(true);
}

public void createVisualization() {
    vv = new VisualizationViewer(layout, new Dimension(800, 600));

    //zooming and transforming
    GraphZoomScrollPane zoomPane = new GraphZoomScrollPane(vv);
    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
    vv.setGraphMouse(graphMouse);

    this.getContentPane().add(zoomPane);
}
}