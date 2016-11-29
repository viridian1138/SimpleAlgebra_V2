



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
 * Node for building in Drools ( <A href="http://drools.org">http://drools.org</A> ) the transitive closure
 * of all additions in the same addition tree.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class AddRoot<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	/**
	 * Constructs the node in the transitive closure.
	 * 
	 * @param _elemA One end of the relation in the transitive closure.
	 * @param _elemB The other end of the relation in the transitive closure.
	 */
	public AddRoot( SymbolicElem<R, S> _elemA , SymbolicAdd<R, S> _elemB )
	{
		elemA = _elemA;
		elemB = _elemB;
	}
	
	/**
	 * Gets one end of the relation in the transitive closure.
	 * 
	 * @return One end of the relation in the transitive closure.
	 */
	public SymbolicElem<R, S> getElemA() {
		return elemA;
	}
	/**
	 * Gets the other end of the relation in the transitive closure.
	 * 
	 * @return The other end of the relation in the transitive closure.
	 */
	public SymbolicAdd<R, S> getElemB() {
		return elemB;
	}
	
	/**
	 * One end of the relation in the transitive closure.
	 */
	private SymbolicElem<R,S> elemA;
	
	/**
	 * The other end of the relation in the transitive closure.
	 */
	private SymbolicAdd<R,S> elemB;

}



