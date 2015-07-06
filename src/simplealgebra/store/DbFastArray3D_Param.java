


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
 * Input parameter for DbFastArray_3D
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 */
public class DbFastArray3D_Param {
	
	/**
	 * The graph to be used in the array.
	 */
	HyperGraph graph;
	
	int tmult;
	
	int xmult;
	
	int ymult;
	
	int tmax;
	
	int xmax;
	
	int ymax;

	
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

	public int getTmult() {
		return tmult;
	}

	public void setTmult(int tmult) {
		this.tmult = tmult;
	}

	public int getXmult() {
		return xmult;
	}

	public void setXmult(int xmult) {
		this.xmult = xmult;
	}

	public int getYmult() {
		return ymult;
	}

	public void setYmult(int ymult) {
		this.ymult = ymult;
	}

	public int getTmax() {
		return tmax;
	}

	public void setTmax(int tmax) {
		this.tmax = tmax;
	}

	public int getXmax() {
		return xmax;
	}

	public void setXmax(int xmax) {
		this.xmax = xmax;
	}

	public int getYmax() {
		return ymax;
	}

	public void setYmax(int ymax) {
		this.ymax = ymax;
	}
	
	
	
}


