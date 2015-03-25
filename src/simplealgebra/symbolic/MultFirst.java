



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
 * Node for transitively determining in Drools ( http://drools.org ) the
 * first node in a tree of multiplications under a root multiplication.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class MultFirst<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	/**
	 * Constructs the node.
	 * 
	 * @param _root The root of the multiplication tree.
	 * @param _first The first node in the multiplication tree.
	 */
	public MultFirst( SymbolicMult<R, S> _root , SymbolicElem<R, S> _first )
	{
		root = _root;
		first = _first;
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
	 * Returns the first node in the multiplication tree.
	 * 
	 * @return The first node in the multiplication tree.
	 */
	public SymbolicElem<R, S> getFirst() {
		return first;
	}


	/**
	 * The root of the multiplication tree.
	 */
	private SymbolicMult<R,S> root;
	
	/**
	 * The first node of the multiplication tree.
	 */
	private SymbolicElem<R,S> first;

}



