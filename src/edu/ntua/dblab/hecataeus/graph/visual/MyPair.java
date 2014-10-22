package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;

public class MyPair {

	private Point2D first;
	private Point2D second;
	
	
	public MyPair(Point2D f, Point2D s) {
		first = f;
		second = s;
	}
	
	protected Point2D getFirstPoint(){
		return first;
	}
	
	protected Point2D getSecondPoint(){
		return second;
	}
}
