




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


import org.kie.api.runtime.rule.FactHandle;

import simplealgebra.symbolic.DroolsSession;


/**
 * A dependency in a topological sort.
 * Uses Drools ( <A href="http://drools.org">http://drools.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TopoSortDependency {
	
	
	/**
	 * Constructs the dependency.
	 * @param _from The node that the dependency is from.
	 * @param _to The node that the dependency is to.
	 */
	public TopoSortDependency( TopoSortNode _from , TopoSortNode _to )
	{
		from = _from;
		to = _to;
	}

	
	/**
	 * The node that the dependency is from.
	 */
	public TopoSortNode from;
	
	
	/**
	 * The node that the dependency is to.
	 */
	public TopoSortNode to;
	
	
	/**
	 * Handle used to delete the dependency from the session.
	 */
	protected FactHandle fact;


	/**
	 * Sets the handle used to delete the dependency from the session.
	 * @param fact the fact to set
	 */
	public void setFact(FactHandle fact) {
		this.fact = fact;
	}
	
	
	/**
	 * Handles the marking of the dependency for deletion.
	 * @param ds The Drools session.
	 */
	public void handleDependencyMark( DroolsSession ds )
	{
		ds.getElem().delete( fact );
	}

	
}



