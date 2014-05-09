



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


public abstract class Elem<T extends Elem<T,?>, R extends ElemFactory<T,R>> {

	public abstract T add( T b );
	
	public abstract T mult( T b );
	
	public abstract T negate( );
	
	public abstract T invertLeft( ) throws NotInvertibleException;
	
	public abstract T invertRight( ) throws NotInvertibleException;
	
	public abstract T divideBy( int val );
	
	public abstract R getFac();
	
	public void validate() throws RuntimeException
	{
	}
	
	public T handleOptionalOp( Object id , ArrayList<T> args ) throws NotInvertibleException
	{
		throw( new RuntimeException( "Operation Not Supported" ) );
	}
	
	
	/**
	 * Implements the exponential function <math display="inline">
     * <mrow>
     *  <msup>
     *          <mo>e</mo>
     *        <mi>x</mi>
     *  </msup>
     * </mrow>
     * </math>
	 * 
	 * @param numIter
	 * @return
	 */
	public T exp( int numIter )
	{
		if( numIter == 0 )
		{
			final T x0 = getFac().identity();
			final T x1 = (T) this;
			final T x2 = x1.mult( x1 );
			final T x3 = x2.mult( x1 );
			final T ret = x0.add( x1 ).add( x2.divideBy( 2 ) ).add( x3.divideBy( 6 ) );
			return( ret );
		}
		else
		{
			final T x1 = (T) this;
			final T tmp = x1.divideBy( 2 ).exp( numIter - 1 );
			return( tmp.mult( tmp ) );
		}
	}
	
	
	/**
	 * Implements the sine function in units of radians.
	 * 
	 * @param numIter
	 * @return
	 */
	public T sin( int numIter )
	{
		ComplexElem<T,R> tmp = new ComplexElem<T,R>( getFac().zero() , (T) this );
		ComplexElem<T,R> btmp = tmp.exp( numIter );
		return( btmp.getIm() );
	}
	
	/**
	 * Implements the cosine function in units of radians.
	 * 
	 * @param numIter
	 * @return
	 */
	public T cos( int numIter )
	{
		ComplexElem<T,R> tmp = new ComplexElem<T,R>( getFac().zero() , (T) this );
		ComplexElem<T,R> btmp = tmp.exp( numIter );
		return( btmp.getRe() );
	}
	
	/**
	 * Implements the hyperbolic sine function defined by <math display="inline">
     * <mrow>
     *  <mo>sinh(</mo>
     *  <mi>x</mi>
     *  <mo>)</mo>
     *  <mo>=</mo>
     *  <msup>
     *          <mo>e</mo>
     *        <mi>x</mi>
     *  </msup>
     *  <mo>-</mo>
     *  <msup>
     *          <mo>e</mo>
     *      <mrow>
     *        <mo>-</mo>
     *        <mi>x</mi>
     *      </mrow>
     *  </msup>
     * </mrow>
     * </math>
	 * 
	 * @param numIter
	 * @return
	 */
	public T sinh( int numIter )
	{
		final T x = (T) this;
		final T ret = ( x.exp( numIter ) ).add( x.negate().exp( numIter ).negate() ).divideBy( 2 );
		return( ret );
	}
	
	/**
	 * Implements the hyperbolic cosine function defined by <math display="inline">
     * <mrow>
     *  <mo>cosh(</mo>
     *  <mi>x</mi>
     *  <mo>)</mo>
     *  <mo>=</mo>
     *  <msup>
     *          <mo>e</mo>
     *        <mi>x</mi>
     *  </msup>
     *  <mo>+</mo>
     *  <msup>
     *          <mo>e</mo>
     *      <mrow>
     *        <mo>-</mo>
     *        <mi>x</mi>
     *      </mrow>
     *  </msup>
     * </mrow>
     * </math>
     *
	 * 
	 * @param numIter
	 * @return
	 */
	public T cosh( int numIter )
	{
		final T x = (T) this;
		final T ret = ( x.exp( numIter ) ).add( x.negate().exp( numIter ) ).divideBy( 2 );
		return( ret );
	}
	
	
}

