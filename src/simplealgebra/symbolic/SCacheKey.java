




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

import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;

/**
 * Cache key for the evaluation of a symbolic elem.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SCacheKey<R extends Elem<R,?>, S extends ElemFactory<R,S>> {

	/**
	 * The evaluated node.
	 */
	protected SymbolicElem<R,S> elem;
	
	/**
	 * The implicit space for the evaluation of the node.
	 */
	protected HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace;
	
	
	/**
	 * Constructs the cache key.
	 * 
	 * @param _elem The evaluated node.
	 * @param _implicitSpace The implicit space for the evaluation of the node.
	 */
	public SCacheKey( SymbolicElem<R,S> _elem , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> _implicitSpace )
	{
		elem = _elem;
		implicitSpace = _implicitSpace;
	}
	
	
	@Override
	public int hashCode()
	{
		return( implicitSpace != null ? elem.hashCode() + implicitSpace.hashCode() : elem.hashCode() );
	}
	
	
	@Override
	public boolean equals( Object in )
	{
		if( in instanceof SCacheKey )
		{
			SCacheKey<R,S> iin = (SCacheKey<R,S>) in;
			return( implicitSpace != null ? 
					( elem.equals( iin.elem ) ) && ( implicitSpace.equals( iin.implicitSpace ) ) :
						elem.equals( iin.elem ) );
		}
		
		return( false );
	}
	
	
}

