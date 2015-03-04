



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


/**
 * An elem implements properties similar to those for a noncommutative ring, though there are some important
 * differences:
 * <P>
 * <P>
 * Division by a non-zero integer is a fundamental operation of an elem.  For instance, it is impossible
 * to calculate the mean of multiple elems without the ability to divide by the number of elems in the sum.
 * In several other important cases it is important to apply some sort of fraction.
 * <P>
 * <P>
 * When a derivative operator is "multiplied" by a term "X" the operation is interpreted as applying the
 * derivative to "X".  In such a case some typical axioms of rings do not apply.  For instance, in a typical
 * ring Y * I reduces to Y where I is the identity.  However, if Y is a partial derivative operator then
 * Y * I should be the application of the derivative to the constant I which should be zero.  Hence, the "I"
 * shouldn't be reduced from Y * I in this case. 
 * <P>
 * <P>
 * For noncommutative products, separate right-side and left-side inverses are defined.  The right-side
 * inverse doesn't necessarily equal the left-side inverse.
 * <P>
 * <P> It is possible to compute inverses of elems.  However, the algebra is not generally a division
 * algebra because it is possible to have a non-zero elem, such as a non-zero square matrix, that
 * doesn't have an inverse.
 * <P>
 * <P> If the isMultCommutative() method of the elem's type's factory returns true, then all multiplications 
 * of the elem type commute.  Otherwise, it is possible that multiplications
 * of the elem type do not commute.  
 * <P>
 * <P> See http://en.wikipedia.org/wiki/noncommutative_ring
 * <P>
 * <P>
 * <P> If the isMultAssociative() method of the elem's type's factory returns true, then all multiplications 
 * of the elem type are associative.  Otherwise, it is possible that multiplications
 * of the elem type are not associative. 
 * 
 * @author thorngreen
 * 
 * @param <T> The elem type.
 * @param <R> The factory for the elem type.
 */
public abstract class Elem<T extends Elem<T,?>, R extends ElemFactory<T,R>> {

	/**
	 * Adds the elem passed in the parameter.
	 * 
	 * @param b The elem to be added.
	 * @return The result of the addition.
	 */
	public abstract T add( T b );
	
	/**
	 * Multiplies the elem in the parameter on the right.
	 * 
	 * @param b The elem to be multiplied.
	 * @return The result of the multiplication.
	 */
	public abstract T mult( T b );
	
	/**
	 * Returns the negation of the elem.
	 * 
	 * @return The negation of the elem.
	 */
	public abstract T negate( );
	
	/**
	 * Returns the left-side inverse of the elem satisfying <math display="inline">
     * <mrow>
     *  <msup>
     *          <mi>A</mi>
     *        <mo>-1L</mo>
     *  </msup>
     *  <mi>A</mi>
     *  <mo>=</mo>
     *  <mi>I</mi>
     * </mrow>
     * </math>
	 * 
	 * @return The left-inverse of the elem.
	 * @throws NotInvertibleException
	 */
	public abstract T invertLeft( ) throws NotInvertibleException;
	
	/**
	 * Returns the right-side inverse of the elem satisfying <math display="inline">
     * <mrow>
     *  <mi>A</mi>
     *  <msup>
     *          <mi>A</mi>
     *        <mo>-1R</mo>
     *  </msup>
     *  <mo>=</mo>
     *  <mi>I</mi>
     * </mrow>
     * </math>
	 * 
	 * @return The right-inverse of the elem.
	 * @throws NotInvertibleException
	 */
	public abstract T invertRight( ) throws NotInvertibleException;
	
	/**
	 * Divides the elem. by a non-zero integer.
	 * 
	 * @param val The integer by which to divide.
	 * @return The elem divided by the integer.
	 */
	public abstract T divideBy( int val );
	
	/**
	 * Returns the factory of the elem.
	 * 
	 * @return The factory of the elem.
	 */
	public abstract R getFac();
	
	/**
	 * Validates the structures and properties of the elem.  Throws
	 * a runtime exception if an invalid structure or property is found.
	 * 
	 * @throws RuntimeException
	 */
	public void validate() throws RuntimeException
	{
	}
	
	/**
	 * Implements a command pattern for executing optional
	 * operations that are not implemented by all elems.
	 * For instance, vectors have an outer product but real
	 * numbers do not.  Hence, only vectors would implement an
	 * optional command for an outer product.  A runtime exception
	 * should be thrown upon receipt of a command that is not supported.
	 * <P>
	 * <P> See http://en.wikipedia.org/wiki/command_pattern
	 * 
	 * @param id The identifier of the command to be executed.
	 * @param args The arguments of the command.
	 * @return The result of the command.
	 * @throws NotInvertibleException
	 */
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
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The exponent of the elem.
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
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The sine of the argument.
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
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The cosine of the argument.
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
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The hyperbolic sine of the argument.
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
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The hyperbolic cosine of the argument.
	 */
	public T cosh( int numIter )
	{
		final T x = (T) this;
		final T ret = ( x.exp( numIter ) ).add( x.negate().exp( numIter ) ).divideBy( 2 );
		return( ret );
	}
	
	
}

