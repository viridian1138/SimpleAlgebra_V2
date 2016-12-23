



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


/**
 * Rule engine node for the sorting test.  Used to represent a symbolic refactoring in Drools ( <A href="http://drools.org">http://drools.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The comparable type of the sort nodes.
 */
public class SortReng<R extends Comparable<?> > {
	
	/**
	 * Constructs the node.
	 * 
	 * @param _strt The starting point of the refactoring.
	 * @param _end The result of the refactoring.
	 */
	public SortReng( SortNode<R> _strt , SortNode<R> _end )
	{
		strt = _strt;
		end = _end;
	}
	
	/**
	 * Gets the starting point of the refactoring.
	 * 
	 * @return The starting point of the refactoring.
	 */
	public SortNode<R> getStrt() {
		return strt;
	}
	
	/**
	 * Gets the result of the refactoring.
	 * 
	 * @return The result of the refactoring.
	 */
	public SortNode<R> getEnd() {
		return end;
	}
	
	/**
	 * The starting point of the refactoring.
	 */
	private SortNode<R> strt;
	
	/**
	 * The result of the refactoring.
	 */
	private SortNode<R> end;

}



