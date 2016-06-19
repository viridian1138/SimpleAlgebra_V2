


//$$strtCprt
/**
* Simple Algebra 
* 
* Copyright (C) 2014 Thornton Green
* 
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
* published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program; if not, 
* see <http://www.gnu.org/licenses>.
* Additional permission under GNU GPL version 3 section 7
*
*/
//$$endCprt





package simplealgebra.store;

import org.hypergraphdb.HyperGraph;


/**
 * Input parameter for DbFastArray_4D
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 */
public class DbFastArray4D_Param {
	
	/**
	 * The graph to be used in the array.
	 */
	HyperGraph graph;
	
	/**
	 * The size of each cell along the T-axis.
	 */
	int tmult;
	
	/**
	 * The size of each cell along the X-axis.
	 */
	int xmult;
	
	/**
	 * The size of each cell along the Y-axis.
	 */
	int ymult;
	
	/**
	 * The size of each cell along the Z-axis.
	 */
	int zmult;
	
	/**
	 * The size of the array along the T-axis.
	 */
	int tmax;
	
	/**
	 * The size of the array along the X-axis.
	 */
	int xmax;
	
	/**
	 * The size of the array along the Y-axis.
	 */
	int ymax;
	
	/**
	 * The size of the array along the Z-axis.
	 */
	int zmax;

	
	/**
	 * Gets the graph to be used in the array.
	 * 
	 * @return The graph to be used in the array.
	 */
	public HyperGraph getGraph() {
		return graph;
	}

	/**
	 * Sets the graph to be used in the array.
	 * 
	 * @param graph The graph to be used in the array.
	 */
	public void setGraph(HyperGraph graph) {
		this.graph = graph;
	}

	/**
	 * Gets the size of each cell along the T-axis.
	 * @return The size of each cell along the T-axis.
	 */
	public int getTmult() {
		return tmult;
	}

	/**
	 * Sets the size of each cell along the T-axis.
	 * @param tmult The size of each cell along the T-axis.
	 */
	public void setTmult(int tmult) {
		this.tmult = tmult;
	}

	/**
	 * Gets the size of each cell along the X-axis.
	 * @return The size of each cell along the X-axis.
	 */
	public int getXmult() {
		return xmult;
	}

	/**
	 * Sets the size of each cell along the X-axis.
	 * @param xmult The size of each cell along the X-axis.
	 */
	public void setXmult(int xmult) {
		this.xmult = xmult;
	}

	/**
	 * Gets the size of each cell along the Y-axis.
	 * @return The size of each cell along the Y-axis.
	 */
	public int getYmult() {
		return ymult;
	}

	/**
	 * Sets the size of each cell along the Y-axis.
	 * @param ymult The size of each cell along the Y-axis.
	 */
	public void setYmult(int ymult) {
		this.ymult = ymult;
	}

	/**
	 * Gets the size of each cell along the Z-axis.
	 * @return The size of each cell along the Z-axis.
	 */
	public int getZmult() {
		return zmult;
	}

	/**
	 * Sets the size of each cell along the Z-axis.
	 * @param zmult The size of each cell along the Z-axis.
	 */
	public void setZmult(int zmult) {
		this.zmult = zmult;
	}

	/**
	 * Gets the size of the array along the T-axis.
	 * @return The size of the array along the T-axis.
	 */
	public int getTmax() {
		return tmax;
	}

	/**
	 * Sets the size of the array along the T-axis.
	 * @param tmax The size of the array along the T-axis.
	 */
	public void setTmax(int tmax) {
		this.tmax = tmax;
	}

	/**
	 * Gets the size of the array along the X-axis.
	 * @return The size of the array along the X-axis.
	 */
	public int getXmax() {
		return xmax;
	}

	/**
	 * Sets the size of the array along the X-axis.
	 * @param xmax The size of the array along the X-axis.
	 */
	public void setXmax(int xmax) {
		this.xmax = xmax;
	}

	/**
	 * Gets the size of the array along the Y-axis.
	 * @return The size of the array along the Y-axis.
	 */
	public int getYmax() {
		return ymax;
	}

	/**
	 * Sets the size of the array along the Y-axis.
	 * @param ymax The size of the array along the Y-axis.
	 */
	public void setYmax(int ymax) {
		this.ymax = ymax;
	}

	/**
	 * Gets the size of the array along the Z-axis.
	 * @return The size of the array along the Z-axis.
	 */
	public int getZmax() {
		return zmax;
	}

	/**
	 * Sets the size of the array along the Z-axis.
	 * @param zmax The size of the array along the Z-axis.
	 */
	public void setZmax(int zmax) {
		this.zmax = zmax;
	}

	
	
	
	
}


