






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
 * A description of how to assign precedence for converting to infix notation.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed elem type.
 * @param <S> The factory for the enclosed elem type.
 */
public abstract class PrecedenceComparator<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends Object {
	
	
	/**
	 * Returns true iff. a parenthesis is required to convert to infix.
	 * @param a The parent node in the elem tree.
	 * @param b The child node in the elem tree.
	 * @param after Whether the child is written after the parent in infix notation.
	 * @return Whether infix notation requires a parenthesis for the child.
	 */
	public abstract boolean parenNeeded( SymbolicElem<R,S> a , SymbolicElem<R,S> b , boolean after );

	
}


