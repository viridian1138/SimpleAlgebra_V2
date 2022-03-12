




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





package simplealgebra.tolerant;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.PrimitiveRandom;

/**
 * Abstract base class for factories generating tolerant results.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type of the derivative.
 * @param <S> Factory for the enclosed type of the derivative.
 * @param <T> The factory for generating tolerant results.
 */
public abstract class TolerantResultFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>,T extends TolerantResultFactory<R,S,T>>
{

	/**
	 * Returns a tolerant result upon encountering an exception from invertLeft()
	 * 
	 * @param in The value that generated the exception.
	 * @param ex The exception that was generated.
	 * @return The tolerant result.
	 */
	public abstract R getTolerantInvertLeft( R in , Exception ex ) throws NotInvertibleException;

	/**
	 * Returns a tolerant result upon encountering an exception from invertRight()
	 * 
	 * @param in The value that generated the exception.
	 * @param ex The exception that was generated.
	 * @return The tolerant result.
	 */
	public abstract R getTolerantInvertRight( R in , Exception ex ) throws NotInvertibleException;;

	/**
	 * Returns a tolerant result upon encountering an exception from divideBy()
	 * 
	 * @param in The value that generated the exception.
	 * @param val The integer to divide by.
	 * @param ex The exception that was generated.
	 * @return The tolerant result.
	 */
	public abstract R getTolerantDivideBy( R in , BigInteger val , Exception ex );

	/**
	 * Returns a tolerant result upon encountering an exception from totalMagnitude().
	 * 
	 * @param in The value that generated the exception.
	 * @param ex The exception that was generated.
	 * @return The tolerant result.
	 */
	public abstract Elem<?,?> getTolerantTotalMagnitude( R in , Exception ex );

	/**
	 * Returns a tolerant result upon encountering an exception from add().
	 * 
	 * @param a First argument of the add that generated the exception.
	 * @param b Second argument of the add that generated the exception.
	 * @param ex The exception that was generated.
	 * @return The tolerant result.
	 */
	public abstract R getTolerantAdd( R a , R b , Exception ex );

	/**
	 * Returns a tolerant result upon encountering an exception from mult().
	 * 
	 * @param a First argument of the multiplication that generated the exception.
	 * @param b Second argument of the multiplication that generated the exception.
	 * @param ex The exception that was generated.
	 * @return The tolerant result.
	 */
	public abstract R getTolerantMult( R a , R b , Exception ex );

	/**
	 * Returns a tolerant result upon encountering an exception from negate().
	 * 
	 * @param a The value that generated the exception.
	 * @param ex The exception that was generated.
	 * @return The tolerant result.
	 */
	public abstract R getTolerantNegate( R a , Exception ex );
	
	/**
	 * Returns a tolerant result upon encountering an exception from an optional op.
	 * 
	 * @param value The value that generated the exception.
	 * @param id The ID of the optional op.
	 * @param args The arguments of the optional op.
	 * @param ex The exception that was generated.
	 * @return The tolerant result.
	 * @throws NotInvertibleException
	 */
	public abstract R getTolerantOptionalOp( R value , Object id , ArrayList<R> args , Exception ex ) throws NotInvertibleException;
	
	/**
	 * Returns a tolerant result upon encountering an exception from mutate().
	 * 
	 * @param value The value that generated the exception.
	 * @param mutr The mutator that generated the exception.
	 * @param ex The exception that was generated.
	 * @return The tolerant result.
	 * @throws NotInvertibleException
	 */
	public abstract R getTolerantMutate( R value , Mutator<R> mutr , Exception ex ) throws NotInvertibleException;
	
	/**
	 * Returns a tolerant result upon encountering an exception from random().
	 * 
	 * @param value The value that generated the exception.
	 * @param in The PrimitiveRandom that generated the exception.
	 * @param ex The exception that was generated.
	 * @return The tolerant result.
	 */
	public abstract R getTolerantRandom( R value , PrimitiveRandom in , Exception ex );
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public T cloneThread( final BigInteger threadIndex )
	{
		// Subclasses of TolerantResultFactory are presumed to be immutable.  Override this method to implement a TolerantResultFactory that is not immutable.
		return( (T) this );
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
	public String writeDesc( WriteTolerantResultFactoryCache cache , PrintStream ps )
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

