




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

import simplealgebra.symbolic.SymbolicElem;


/**
 * Mutator for right-side multiplication.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The mutation type.
 */
public class MultRightMutator<T extends Elem<T,?>> implements Mutator<T> {
	
	/**
	 * Constructs the mutator.
	 * 
	 * @param _elem The elem. by which to multiply.
	 * @param _name The name of the mutation.
	 */
	public MultRightMutator( T _elem , String _name )
	{
		elem = _elem;
		name = _name;
	}

	@Override
	public T mutate(T in) {
		return( in.mult( elem ) );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		if( elem instanceof SymbolicElem )
		{
			return( ( (SymbolicElem) elem ).exposesDerivatives() );
		}
		return( false );
	}

	@Override
	public String writeString() {
		return( "multRight[ " + name + " ]" );
	}

	/**
	 * The elem. by which to multiply.
	 */
	private T elem;
	
	/**
	 * The name of the mutation.
	 */
	private String name;
	
}


