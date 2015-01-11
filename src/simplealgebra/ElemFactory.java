



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




package simplealgebra;

import java.util.ArrayList;

import simplealgebra.symbolic.SymbolicElem;

/**
 * Factory for elems.
 * 
 * @author thorngreen
 *
 * @param <T>
 * @param <R>
 */
public abstract class ElemFactory<T extends Elem<T,?>, R extends ElemFactory<T,R>> {

	/**
	 * Returns an instance of the identity elem.
	 * 
	 * @return An instance of the identity elem.
	 */
	public abstract T identity();
	
	/**
	 * Returns an instance of the zero elem.
	 * 
	 * @return An instance of the zero elem.
	 */
	public abstract T zero();
	
	/**
	 * Returns an instance of the negation of the identity elem.
	 * 
	 * @return An instance of the negation of the identity elem.
	 */
	public T negativeIdentity()
	{
		return( identity().negate() );
	}
	
	/**
	 * Handles optional operations that are not supported by all elems.
	 * 
	 * @param id The id for the command describing the operation.
	 * @param args The arguments for the operation.
	 * @return The result of the operation.
	 * @throws NotInvertibleException
	 */
	public SymbolicElem<T, R> handleSymbolicOptionalOp( Object id , ArrayList<SymbolicElem<T, R>> args ) throws NotInvertibleException
	{
		throw( new RuntimeException( "Operation Not Supported" ) );
	}
	
	public abstract boolean isMultCommutative();
	
	/**
	 * Returns whether the multiplication of the enclosed type if commutative, or true
	 * if there is no enclosed type.
	 * 
	 * @return Whether the multiplication of the enclosed type if commutative, or true
	 * if there is no enclosed type.
	 */
	public abstract boolean isNestedMultCommutative();
	
	
}

