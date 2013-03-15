package edu.ntua.dblab.hecataeus;

import java.awt.GridBagConstraints;

/**
 * <p>Class that sets gridBag Constraints  for the layout of components in JDialog
 *
 *@author George Papastefanatos
 *	<br> National Technical University of Athens
 */
public final class HecataeusGridBagConstraints extends GridBagConstraints{

	private static final long serialVersionUID = 1L;

	public void reset(int gx, int gy,
			int gw, int gh, int wx, int wy) {
		this.gridx = gx;
		this.gridy = gy;
		this.gridwidth = gw;
		this.gridheight = gh;
		this.weightx = wx;
		this.weighty = wy;
	}
}