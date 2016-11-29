



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




package simplealgebra.symbolic;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;



/**
 * Node for transitively determining in Drools ( <A href="http://drools.org">http://drools.org</A> ) 
 * adjacent nodes in a tree of multiplications under a root multiplication.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class MultNext<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	/**
	 * Constructs the node.
	 * 
	 * @param _root The root of the multiplication tree.
	 * @param _first The first node in the adjacency pair (not the first node overall).
	 * @param _next The next node in the adjacency pair.
	 */
	public MultNext( SymbolicMult<R, S> _root , SymbolicElem<R, S> _first , SymbolicElem<R, S> _next )
	{
		root = _root;
		first = _first;
		next = _next;
	}
	
	
	
	/**
	 * Returns the root of the multiplication tree.
	 * 
	 * @return The root of the multiplication tree.
	 */
	public SymbolicMult<R, S> getRoot() {
		return root;
	}
	
	/**
	 * Returns the first node in the adjacency pair (not the first node overall).
	 * 
	 * @return The first node in the adjacency pair (not the first node overall).
	 */
	public SymbolicElem<R, S> getFirst() {
		return first;
	}
	
	/**
	 * Returns the next node in the adjacency pair.
	 * 
	 * @return The next node in the adjacency pair.
	 */
	public SymbolicElem<R, S> getNext() {
		return next;
	}



	/**
	 * The root of the multiplication tree.
	 */
	private SymbolicMult<R,S> root;
	
	/**
	 * The first node in the adjacency pair (not the first node overall).
	 */
	private SymbolicElem<R,S> first;
	
	/**
	 * The next node in the adjacency pair.
	 */
	private SymbolicElem<R,S> next;

}



