




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





package simplealgebra.ddx;

import java.io.PrintStream;
import java.math.BigInteger;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.symbolic.SymbolicElem;

/**
 * Factory for mapping a directional derivative (usually represented as <math display="inline">
 * <mrow>
 *  <mo>&nabla;</mo>
 * </mrow>
 * </math>) into its set of constituent partial derivatives.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type of the derivative.
 * @param <S> Factory for the enclosed type of the derivative.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public abstract class DirectionalDerivativePartialFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>>
{

	/**
	 * Returns the partial derivative for a particular index value.
	 * 
	 * @param basisIndex The input index value.
	 * @return A partial derivative expression corresponding to the index.
	 */
	public abstract SymbolicElem<R,S> getPartial( BigInteger basisIndex );
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public DirectionalDerivativePartialFactory<R,S,K> cloneThread( final BigInteger threadIndex )
	{
		throw( new RuntimeException( "Not Supported.  Override cloneThread() method to add support." ) );
	}
	
	
	/**
	 * Writes the type of the factory.
	 * 
	 * @param ps The type to which to write the factory.
	 */
	public void writeTypeString( PrintStream ps )
	{
		ps.print( this.getClass().getSimpleName() );
	}
	
	
	/**
	 * Writes a description of the instance to the output stream.
	 * 
	 * @param cache Instance cache from which to cache objects.
	 * @param ps Stream to write the description.
	 * @return String describing the id of the object.
	 */
	public String writeDesc( WriteDirectionalDerivativePartialFactoryCache cache , PrintStream ps )
	{
		String st = (String) cache.getFac( this );
		if( st == null )
		{
			st = cache.getIncrementVal();
			cache.putFac( this, st );
			writeTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			writeTypeString( ps );
			ps.println( "();" );
		}
		return( st );
	}

	
}

