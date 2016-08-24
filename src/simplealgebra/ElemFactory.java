



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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import simplealgebra.symbolic.SymbolicElem;

/**
 * Factory for elems.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The elem type.
 * @param <R> The factory for the elem type.
 */
public abstract class ElemFactory<T extends Elem<T,?>, R extends ElemFactory<T,R>> {

	/**
	 * Returns an instance of the identity elem satisfying <math display="inline">
     * <mrow>
     *  <mi>A</mi>
     *  <mi>I</mi>
     *  <mo>=</mo>
     *  <mi>I</mi>
     *  <mi>A</mi>
     *  <mo>=</mo>
     *  <mi>A</mi>
     * </mrow>
     * </math>
	 * 
	 * @return An instance of the identity elem.
	 */
	public abstract T identity();
	
	/**
	 * Returns an instance of the zero elem satisfying <math display="inline">
     * <mrow>
     *  <mi>A</mi>
     *  <mi>Z</mi>
     *  <mo>=</mo>
     *  <mi>Z</mi>
     *  <mi>A</mi>
     *  <mo>=</mo>
     *  <mi>Z</mi>
     * </mrow>
     * </math>  and   <math display="inline">
     * <mrow>
     *  <mi>A</mi>
     *  <mo>+</mo>
     *  <mi>Z</mi>
     *  <mo>=</mo>
     *  <mi>Z</mi>
     *  <mo>+</mo>
     *  <mi>A</mi>
     *  <mo>=</mo>
     *  <mi>A</mi>
     * </mrow>
     * </math>
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
	 * Returns the equivalent of a zero total magnitude for the elem type.
	 * @return The equivalent of a zero total magnitude for the elem type.
	 */
	public abstract Elem<?,?> totalMagnitudeZero();
	
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
	
	/**
	 * Returns whether the multiplication of the elem type is commutative.
	 * 
	 * @return Whether the multiplication of the elem type is commutative.
	 */
	public abstract boolean isMultCommutative();
	
	/**
	 * Returns whether the multiplication of the enclosed type if commutative, or true
	 * if there is no enclosed type.
	 * 
	 * @return Whether the multiplication of the enclosed type if commutative, or true
	 * if there is no enclosed type.
	 */
	public abstract boolean isNestedMultCommutative();
	
	
	/**
	 * Returns whether the multiplication of the elem type is associative.
	 * 
	 * @return Whether the multiplication of the elem type is associative.
	 */
	public abstract boolean isMultAssociative();
	
	/**
	 * Returns whether the multiplication of the enclosed type if associative, or true
	 * if there is no enclosed type.
	 * 
	 * @return Whether the multiplication of the enclosed type if associative, or true
	 * if there is no enclosed type.
	 */
	public abstract boolean isNestedMultAssociative();
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public abstract R cloneThread( final BigInteger threadIndex );
	 
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @param cache The instance cache for the cloning.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public abstract R cloneThreadCached( final BigInteger threadIndex , final CloneThreadCache<T,R> cache );
	
	
	/**
	 * Returns the new instance of the write elem cache.
	 * 
	 * @return The new instance of the cache.
	 */
	public final WriteElemCache<T,R> generateWriteElemCache()
	{
		return( new WriteElemCache<T,R>() );
	}
	
	/**
	 * Writes a description of the instance to the output stream.
	 * 
	 * @param cache Instance cache from which to cache objects.
	 * @param ps Stream to write the description.
	 * @return String describing the id of the object.
	 */
	public abstract String writeDesc( WriteElemCache<T,R> cache , PrintStream ps );


	/**
	 * Writes the type of the elem.
	 * 
	 * @param ps The stream to which to write the type.
	 */
	public abstract void writeElemTypeString( PrintStream ps );

	/**
	 * Writes the type of the factory.
	 * 
	 * @param ps The stream to which to write the type.
	 */
	public abstract void writeElemFactoryTypeString( PrintStream ps );
	
	
	/**
	 * Returns possible approximations for a unit logarithm.
	 * @return An iterator of possible approximations for a unit logarithm.
	 */
	public Iterator<T> getApproxLnUnit()
	{
		return( new Iterator<T>()
		{
			/**
			 * The current index of the iterations.
			 */
			protected int index = -1;
			
			@Override
			public boolean hasNext() {
				return( ( index + 1 ) <= 4 );
			}

			@Override
			public T next() {
				index++;
				switch( index )
				{
					case 0:
						return( zero() );
						
					case 1:
						return( ( identity().add( identity() ).add( identity() ) ) );
						
					case 2:
						return( ( identity().add( identity() ).add( identity() ) ).negate() );
						
					case 3:
						return( ( identity().add( identity() ).add( identity() ) ).divideBy( 2 ) );
						
					case 4:
						return( ( identity().add( identity() ).add( identity() ) ).negate().divideBy( 2 ) );
						
				}
				throw( new RuntimeException( "Inconsistent" ) );
			}

			@Override
			public void remove() {
				throw( new RuntimeException( "Not Supported" ) );
			}
			
		} );
	}

	
	
}


