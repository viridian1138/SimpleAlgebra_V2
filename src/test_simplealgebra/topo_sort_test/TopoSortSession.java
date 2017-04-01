




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





package test_simplealgebra.topo_sort_test;



import java.util.ArrayList;



/**
 * Session for topological sort.
 * Uses Drools ( <A href="http://drools.org">http://drools.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TopoSortSession {

	
	/**
	 * Constructs the session.
	 */
	public TopoSortSession() {
	}
	
	
	/**
	 * Handles the selection of a node as the next node in the sort.
	 * @param node The selected node.
	 * @return The selected node.
	 */
	public TopoSortNode handleNodeSelect( TopoSortNode node )
	{
		sortedElements.add( node );
		return( node );
	}
	
	
	/**
	 * Gets the list of sorted elements.
	 * @return The list of sorted elements.
	 */
	public ArrayList<TopoSortNode> getSortedElements()
	{
		return( sortedElements );
	}
	
	
	/**
	 * The list of sorted elements.
	 */
	protected ArrayList<TopoSortNode> sortedElements = new ArrayList<TopoSortNode>();
	

}



