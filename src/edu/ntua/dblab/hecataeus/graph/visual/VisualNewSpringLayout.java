package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.map.LazyMap;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

public class VisualNewSpringLayout<V,E> extends AbstractLayout<V,E> implements IterativeContext {
	
	protected double stretch = 0.70;
    protected Transformer<E, Integer> lengthFunction;
    protected int repulsion_range_sq = 100 * 100;
    protected double force_multiplier = 2;

    protected Map<V, SpringVertexData> springVertexData =
    	LazyMap.decorate(new HashMap<V, SpringVertexData>(),
    			new Factory<SpringVertexData>() {
					public SpringVertexData create() {
						return new SpringVertexData();
					}});
    
    
    @SuppressWarnings("unchecked")
    public VisualNewSpringLayout(Graph<V,E> g) {
        this(g, new ConstantTransformer(30));
    }
    
    
    /**
     * Constructor for a SpringLayout for a raw graph with associated component.
     *
     * @param g the {@code Graph} to lay out
     * @param length_function provides a length for each edge
     */
    public VisualNewSpringLayout(Graph<V,E> g, Transformer<E, Integer> length_function)
    {
        super(g);
        this.lengthFunction = length_function;
    }

    /**
     * Returns the current value for the stretch parameter.
     * @see #setStretch(double)
     */
    public double getStretch() {
        return stretch;
    }

	@Override
	public void setSize(Dimension size) {
		if(initialized == false)
			setInitializer(new RandomLocationTransformer<V>(size));
		super.setSize(size);
	}

    public void setStretch(double stretch) {
        this.stretch = stretch;
    }
    public int getRepulsionRange() {
        return (int)(Math.sqrt(repulsion_range_sq));
    }

    public void setRepulsionRange(int range) {
        this.repulsion_range_sq = range * range;
    }
    public double getForceMultiplier() {
        return force_multiplier;
    }


    public void setForceMultiplier(double force) {
        this.force_multiplier = force;
    }

    public void initialize() {
    	 step();
    
    	
     	
    	
    }

    public void step() {
    	try {
    		for(V v : getGraph().getVertices()) {
    			SpringVertexData svd = springVertexData.get(v);
    			if (svd == null) {
    				continue;
    			}
    			svd.dx /= 4;
    			svd.dy /= 4;
    			svd.edgedx = svd.edgedy = 0;
    			svd.repulsiondx = svd.repulsiondy = 0;
    		}
    	} catch(ConcurrentModificationException cme) {
    		step();
    	}

    	relaxEdges();
    	calculateRepulsion();
    	moveNodes();
    }

    protected void relaxEdges() {
    	try {
    		for(E e : getGraph().getEdges()) {
    		    Pair<V> endpoints = getGraph().getEndpoints(e);
    			V v1 = endpoints.getFirst();
    			V v2 = endpoints.getSecond();

    			Point2D p1 = transform(v1);
    			Point2D p2 = transform(v2);
    			if(p1 == null || p2 == null) continue;
    			double vx = p1.getX() - p2.getX();
    			double vy = p1.getY() - p2.getY();
    			double len = Math.sqrt(vx * vx + vy * vy);

    			double desiredLen = lengthFunction.transform(e);

    			// round from zero, if needed [zero would be Bad.].
    			len = (len == 0) ? .0001 : len;

    			double f = force_multiplier * (desiredLen - len) / len;

    			f = f * Math.pow(stretch, (getGraph().degree(v1) + getGraph().degree(v2) - 2));

    			// the actual movement distance 'dx' is the force multiplied by the
    			// distance to go.
    			double dx = f * vx;
    			double dy = f * vy;
    			SpringVertexData v1D, v2D;
    			v1D = springVertexData.get(v1);
    			v2D = springVertexData.get(v2);
    			
    			v1D.edgedx += dx;
    			v1D.edgedy += dy;
    			v2D.edgedx += -dx;
    			v2D.edgedy += -dy;
    		}
    	} catch(ConcurrentModificationException cme) {
    		relaxEdges();
    	}
    }

    protected void calculateRepulsion() {
        try {
        for (V v : getGraph().getVertices()) {
            if (isLocked(v)) continue;

            SpringVertexData svd = springVertexData.get(v);
            if(svd == null) continue;
            double dx = 0, dy = 0;

            for (V v2 : getGraph().getVertices()) {
                if (v == v2) continue;
                Point2D p = transform(v);
                Point2D p2 = transform(v2);
                if(p == null || p2 == null) continue;
                double vx = p.getX() - p2.getX();
                double vy = p.getY() - p2.getY();
                double distanceSq = p.distanceSq(p2);
                if (distanceSq == 0) {
                    dx += Math.random();
                    dy += Math.random();
                } else if (distanceSq < repulsion_range_sq) {
                    double factor = 1;
                    dx += factor * vx / distanceSq;
                    dy += factor * vy / distanceSq;
                }
            }
            double dlen = dx * dx + dy * dy;
            if (dlen > 0) {
                dlen = Math.sqrt(dlen) / 2;
                svd.repulsiondx += dx / dlen;
                svd.repulsiondy += dy / dlen;
            }
        }
        } catch(ConcurrentModificationException cme) {
            calculateRepulsion();
        }
    }

    protected void moveNodes()
    {
        synchronized (getSize()) {
            try {
                for (V v : getGraph().getVertices()) {
                    if (isLocked(v)) continue;
                    SpringVertexData vd = springVertexData.get(v);
                    if(vd == null) continue;
                    Point2D xyd = transform(v);

                    vd.dx += vd.repulsiondx + vd.edgedx;
                    vd.dy += vd.repulsiondy + vd.edgedy;

                    // keeps nodes from moving any faster than 5 per time unit
                    xyd.setLocation(xyd.getX()+Math.max(-5, Math.min(5, vd.dx)),
                    		xyd.getY()+Math.max(-5, Math.min(5, vd.dy)));

                    Dimension d = getSize();
                    int width = d.width;
                    int height = d.height;

                    if (xyd.getX() < 0) {
                        xyd.setLocation(0, xyd.getY());
                    } else if (xyd.getX() > width) {
                        xyd.setLocation(width, xyd.getY());
                    }
                    if (xyd.getY() < 0) {
                        xyd.setLocation(xyd.getX(), 0);
                    } else if (xyd.getY() > height) {
                        xyd.setLocation(xyd.getX(), height);
                    }

                }
            } catch(ConcurrentModificationException cme) {
                moveNodes();
            }
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


    public class SpringDimensionChecker extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            setSize(e.getComponent().getSize());
        }
    }

    public boolean isIncremental() {
        return true;
    }

    public boolean done() {
        return false;
    }

	public void reset() {
		initialize();
	}
}
