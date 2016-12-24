




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





package test_simplealgebra.sort_test;


import simplealgebra.symbolic.DroolsSession;


/**
 * Serves as a placeholder for a SortNode as part of TestSort.
 * Uses Drools ( <A href="http://drools.org">http://drools.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The comparable type of the sort nodes.
 */
public class SortPlaceholder<R extends Comparable<?>>
{

	/**
	 * Constructs the placeholder.
	 * 
	 * @param _elem The enclosed elem.
	 */
	public SortPlaceholder( SortNode<R> _elem )
	{
		elem = _elem;
	}
	
	/**
	 * Constructs the placeholder for use in a Drools ( <A href="http://drools.org">http://drools.org</A> ) session.
	 * 
	 * @param _elem The enclosed elem.
	 * @param ds The Drools session.
	 */
	public SortPlaceholder( SortNode<R> _elem , DroolsSession ds )
	{
		this( _elem );
		ds.insert( this );
	}
	
	
	
	/**
	 * Gets the enclosed elem.
	 * 
	 * @return The enclosed elem.
	 */
	public SortNode<R> getElem() {
		return elem;
	}
	
	
	/**
	 * Sets the enclosed elem.
	 * 
	 * @param _elem The enclosed elem.
	 */
	public void setElem( SortNode<R> _elem ) {
		elem  = _elem;
	}


	/**
	 * The enclosed elem.
	 */
	private SortNode<R> elem;

}


