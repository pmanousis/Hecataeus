package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.map.LazyMap;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

public class VisualDagLayout<V, E> extends SpringLayout<V,E> {

	private Map<V,Number> minLevels = new HashMap<V,Number>();
	// Simpler than the "pair" technique.
	static int graphHeight;
	static int numRoots;
	final double SPACEFACTOR = 1.3;
	// How much space do we allow for additional floating at the bottom.
	final double LEVELATTRACTIONRATE = 0.8;
	
	final double MSV_THRESHOLD = 10.0;
	double meanSquareVel;
	boolean stoppingIncrements = false;
	int incrementsLeft;
	final int COOL_DOWN_INCREMENTS = 200;

	
	 protected Map<V, SpringVertexData> springVertexData = LazyMap.decorate(new HashMap<V, SpringVertexData>(),new Factory<SpringVertexData>() {
		public SpringVertexData create() {
			return new SpringVertexData();
		}});
	
	public VisualDagLayout(Graph<V,E> g) {
		super(g);
	}
	/**
	 * setRoot calculates the level of each vertex in the graph. Level 0 is
	 * allocated to any vertex with no successors. Level n+1 is allocated to
	 * any vertex whose successors' maximum level is n.
	 */
	public void setRoot(Graph<V,E> g) {
		numRoots = 0;
		for(V v : g.getVertices()) {
			Collection<V> successors = getGraph().getSuccessors(v);
			if (successors.size() == 0) {
				setRoot(v);
				numRoots++;
			}
		}
	}

	/**
	 * Set vertex v to be level 0.
	 */
	public void setRoot(V v) {
		minLevels.put(v, new Integer(0));
		// set all the levels.
		propagateMinimumLevel(v);
	}

	/**
	 * A recursive method for allocating the level for each vertex. Ensures
	 * that all predecessors of v have a level which is at least one greater
	 * than the level of v.
	 *
	 * @param v
	 */
	public void propagateMinimumLevel(V v) {
		int level = minLevels.get(v).intValue();
		for(V child : getGraph().getPredecessors(v)) {
			int oldLevel, newLevel;
			Number o = minLevels.get(child);
			if (o != null)
				oldLevel = o.intValue();
			else
				oldLevel = 0;
			newLevel = Math.max(oldLevel, level + 1);
			minLevels.put(child, new Integer(newLevel));

			if (newLevel > graphHeight)
				graphHeight = newLevel;
			propagateMinimumLevel(child);
		}
	}

	/**
	 * Sets random locations for a vertex within the dimensions of the space.
	 * This overrides the method in AbstractLayout
	 *
	 * @param coord
	 * @param d
	 */
	private void initializeLocation(
		V v,
		Point2D coord,
		Dimension d) {

		int level = minLevels.get(v).intValue();
		int minY = (int) (level * d.getHeight() / (graphHeight * SPACEFACTOR));
		double x = Math.random() * d.getWidth();
		double y = Math.random() * (d.getHeight() - minY) + minY;
		coord.setLocation(x,y);
	}

	@Override
	public void setSize(Dimension size) {
		super.setSize(size);
		for(V v : getGraph().getVertices()) {
			initializeLocation(v,transform(v),getSize());
		}
	}

	/**
	 * Had to override this one as well, to ensure that setRoot() is called.
	 */
	@Override
	public void initialize() {
		super.initialize();
		setRoot(getGraph());
	}

	/**
	 * Override the moveNodes() method from SpringLayout. The only change we
	 * need to make is to make sure that nodes don't float higher than the minY
	 * coordinate, as calculated by their minimumLevel.
	 */
	@Override
	protected void moveNodes() {
		// Dimension d = currentSize;
		double oldMSV = meanSquareVel;
		meanSquareVel = 0;
		
		synchronized (getSize()) {

			for(V v : getGraph().getVertices()) {
				if (isLocked(v))
					continue;
				SpringVertexData vd = springVertexData.get(v);
				Point2D xyd = transform(v);

				int width = getSize().width;
				int height = getSize().height;

				// (JY addition: three lines are new)
				int level =
					minLevels.get(v).intValue();
				int minY = (int) (level * height / (graphHeight * SPACEFACTOR));
				int maxY =
					level == 0
						? (int) (height / (graphHeight * SPACEFACTOR * 2))
						: height;

				// JY added 2* - double the sideways repulsion.
//				vd.dx += 2 * vd.repulsiondx + vd.edgedx;
//				vd.dy += vd.repulsiondy + vd.edgedy;

				// JY Addition: Attract the vertex towards it's minimumLevel
				// height.
				double delta = xyd.getY() - minY;
//				VD.DY -= DELTA * LEVELATTRACTIONRATE;
//				IF (LEVEL == 0)
//					VD.DY -= DELTA * LEVELATTRACTIONRATE;
				// twice as much at the top.

				// JY addition:
//				meanSquareVel += (vd.dx * vd.dx + vd.dy * vd.dy);

				// keeps nodes from moving any faster than 5 per time unit
//				xyd.setLocation(xyd.getX()+Math.max(-5, Math.min(5, vd.dx)) , xyd.getY()+Math.max(-5, Math.min(5, vd.dy)) );

				if (xyd.getX() < 0) {
					xyd.setLocation(0, xyd.getY());
				} else if (xyd.getX() > width) {
					xyd.setLocation(width, xyd.getY());
				}

				// (JY addition: These two lines replaced 0 with minY)
				if (xyd.getY() < minY) {
					xyd.setLocation(xyd.getX(), minY);
					// (JY addition: replace height with maxY)
				} else if (xyd.getY() > maxY) {
					xyd.setLocation(xyd.getX(), maxY);
				}

				// (JY addition: if there's only one root, anchor it in the
				// middle-top of the screen)
				if (numRoots == 1 && level == 0) {
					xyd.setLocation(width/2, xyd.getY());
				}
			}
		}
		//System.out.println("MeanSquareAccel="+meanSquareVel);
		if (!stoppingIncrements
			&& Math.abs(meanSquareVel - oldMSV) < MSV_THRESHOLD) {
			stoppingIncrements = true;
			incrementsLeft = COOL_DOWN_INCREMENTS;
		} else if (
			stoppingIncrements
				&& Math.abs(meanSquareVel - oldMSV) <= MSV_THRESHOLD) {
			incrementsLeft--;
			if (incrementsLeft <= 0)
				incrementsLeft = 0;
		}
	}

	/**
	 * Override incrementsAreDone so that we can eventually stop.
	 */
	@Override
	public boolean done() {
		if (stoppingIncrements && incrementsLeft == 0)
			return true;
		else
			return false;
	}

	/**
	 * Override forceMove so that if someone moves a node, we can re-layout
	 * everything.
	 */
	@Override
	public void setLocation(V picked, double x, double y) {
		Point2D coord = transform(picked);
		coord.setLocation(x,y);
		stoppingIncrements = false;
	}

	/**
	 * Override forceMove so that if someone moves a node, we can re-layout
	 * everything.
	 */
	@Override
	public void setLocation(V picked, Point2D p) {
		Point2D coord = transform(picked);
		coord.setLocation(p);
		stoppingIncrements = false;
	}

	/**
	 * Overridden relaxEdges. This one reduces the effect of edges between
	 * greatly different levels.
	 *
	 */
	@Override
	protected void relaxEdges() {
		for(E e : getGraph().getEdges()) {
		    Pair<V> endpoints = getGraph().getEndpoints(e);
			V v1 = endpoints.getFirst();
			V v2 = endpoints.getSecond();

			Point2D p1 = transform(v1);
			Point2D p2 = transform(v2);
			double vx = p1.getX() - p2.getX();
			double vy = p1.getY() - p2.getY();
			double len = Math.sqrt(vx * vx + vy * vy);

			// JY addition.
			int level1 =
				minLevels.get(v1).intValue();
			int level2 =
				minLevels.get(v2).intValue();

			// desiredLen *= Math.pow( 1.1, (v1.degree() + v2.degree()) );
//          double desiredLen = getLength(e);
			double desiredLen = lengthFunction.transform(e);

			// round from zero, if needed [zero would be Bad.].
			len = (len == 0) ? .0001 : len;

			// force factor: optimal length minus actual length,
			// is made smaller as the current actual length gets larger.
			// why?

			// System.out.println("Desired : " + getLength( e ));
			double f = force_multiplier * (desiredLen - len) / len;

			f = f * Math.pow(stretch / 100.0,
					(getGraph().degree(v1) + getGraph().degree(v2) -2));

			// JY addition. If this is an edge which stretches a long way,
			// don't be so concerned about it.
			if (level1 != level2)
				f = f / Math.pow(Math.abs(level2 - level1), 1.5);

			// f= Math.min( 0, f );

			// the actual movement distance 'dx' is the force multiplied by the
			// distance to go.
			double dx = f * vx;
			double dy = f * vy;
			SpringVertexData v1D, v2D;
			v1D = springVertexData.get(v1);
			v2D = springVertexData.get(v2);

//			SpringEdgeData<E> sed = getSpringEdgeData(e);
//			sed.f = f;

//			v1D.edgedx += dx;
//			v1D.edgedy += dy;
//			v2D.edgedx += -dx;
//			v2D.edgedy += -dy;
		}
	}
	
	
    protected static class SpringVertexData {
        protected double edgedx;
        protected double edgedy;
        protected double repulsiondx;
        protected double repulsiondy;

        /** movement speed, x */
        protected double dx;

        /** movement speed, y */
        protected double dy;
    }
	
}
