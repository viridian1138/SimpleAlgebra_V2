



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



public class MultNext<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	public MultNext( SymbolicMult<R, S> _root , SymbolicElem<R, S> _first , SymbolicElem<R, S> _next )
	{
		root = _root;
		first = _first;
		next = _next;
	}
	
	
	
	/**
	 * @return the root
	 */
	public SymbolicMult<R, S> getRoot() {
		return root;
	}
	/**
	 * @return the first
	 */
	public SymbolicElem<R, S> getFirst() {
		return first;
	}
	/**
	 * @return the next
	 */
	public SymbolicElem<R, S> getNext() {
		return next;
	}



	private SymbolicMult<R,S> root;
	private SymbolicElem<R,S> first;
	private SymbolicElem<R,S> next;

}



