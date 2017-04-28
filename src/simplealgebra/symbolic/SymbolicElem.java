




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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.PrimitiveRandom;

/**
 * A symbolic elem.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public abstract class SymbolicElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends Elem<SymbolicElem<R,S>, SymbolicElemFactory<R,S>> {

	/**
	 * Evaluates the symbolic expression.
	 * 
	 * @param implicitSpace The implicit space over which to evaluate the expression.
	 * @return The result of the evaluation.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	abstract public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	

	/**
	 * Evaluates the symbolic expression.
	 * 
	 * @param implicitSpace The implicit space over which to evaluate the expression.
	 * @param cache The evaluation cache.
	 * @return The result of the evaluation.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	abstract public R evalCached( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace,
			HashMap<SCacheKey<R,S>,R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	/**
	 * Evaluates the partial derivative of the symbolic expression.
	 * 
	 * @param withRespectTo The variable over which to evaluate the derivative.
	 * @param implicitSpace The implicit space over which to evaluate the expression.
	 * @return The result of the evaluation.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	abstract public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	/**
	 * Evaluates the partial derivative of the symbolic expression.
	 * 
	 * @param withRespectTo The variable over which to evaluate the derivative.
	 * @param implicitSpace The implicit space over which to evaluate the expression.
	 * @param cache The evaluation cache.
	 * @return The result of the evaluation.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	abstract public R evalPartialDerivativeCached( ArrayList<? extends Elem<?,?>> withRespectTo , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R,S>,R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	
	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicElem( S _fac )
	{
		fac = _fac;
	}

	
	@Override
	public SymbolicElem<R, S> add(SymbolicElem<R, S> b) {
		// This simplification has a parallel implementation in the "Add Zero B" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( b.isSymbolicZero() ? this : new SymbolicAdd<R,S>( this , b , fac ) );
	}

	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		// This simplification has a parallel implementation in the "MultRightMutatorType Zero B" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( b.isSymbolicZero() ? b : new SymbolicMult<R,S>( this , b , fac ) );
	}

	@Override
	public SymbolicElem<R, S> negate() {
		return( new SymbolicNegate<R,S>( this , fac ) );
	}

	@Override
	public SymbolicElem<R, S> invertLeft() throws NotInvertibleException {
		return( new SymbolicInvertLeft<R,S>( this , fac ) );
	}
	
	@Override
	public SymbolicElem<R, S> invertRight() throws NotInvertibleException {
		return( new SymbolicInvertRight<R,S>( this , fac ) );
	}

	@Override
	public SymbolicElem<R, S> divideBy(BigInteger val) {
		return( new SymbolicDivideBy<R,S>( this , fac , val ) );
	}
	
	@Override
	public SymbolicElem<R, S>  exp( int numIter ) {
		return( new SymbolicExponential<R,S>( this , fac , numIter ) );
	}
	
	@Override
	public SymbolicElem<R, S>  sin( int numIter ) {
		return( new SymbolicSine<R,S>( this , fac , numIter ) );
	}
	
	@Override
	public SymbolicElem<R, S>  cos( int numIter ) {
		return( new SymbolicCosine<R,S>( this , fac , numIter ) );
	}
	
	@Override
	public SymbolicElem<R, S> random( PrimitiveRandom in ) {
		return( new SymbolicRandom<R,S>( this , fac , in ) );
	}
	
	@Override
	public Elem<?,?> totalMagnitude()
	{
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	
	/**
	 * Expands the exponential function <math display="inline">
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
	public SymbolicElem<R,S> expandExp( int numIter )
	{
		return( super.exp(numIter) );
	}
	
	
	/**
	 * Expands the sine function in units of radians.
	 * 
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The sine of the argument.
	 */
	public SymbolicElem<R,S> expandSin( int numIter )
	{
		return( super.sin(numIter) );
	}
	
	
	/**
	 * Expands the cosine function in units of radians.
	 * 
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The cosine of the argument.
	 */
	public SymbolicElem<R,S> expandCos( int numIter )
	{
		return( super.cos(numIter) );
	}
	
	
	/**
	 * Inserts the elem into a Drools ( <A href="http://drools.org">http://drools.org</A> ) session.
	 * @param ds The session.
	 * @return This elem.
	 */
	public SymbolicElem<R,S> insSym( DroolsSession ds )
	{
		ds.insert( this );
		return( this );
	}

	
	@Override
	public SymbolicElemFactory<R, S> getFac() {
		return( new SymbolicElemFactory<R,S>( fac ) );
	}
	
	/**
	 * Returns true iff. the elem is a symbolic zero.
	 * 
	 * @return True iff. the elem is a symbolic zero.
	 */
	public boolean isSymbolicZero()
	{
		return( false );
	}
	
	/**
	 * Returns true iff. the elem is a symbolic identity.
	 * 
	 * @return True iff. the elem is a symbolic identity.
	 */
	public boolean isSymbolicIdentity()
	{
		return( false );
	}
	
	
	/**
	 * Returns true if the elem exposes derivatives to elems by which it is multiplied.
	 * 
	 * @return True if the elem exposes derivatives to elems by which it is multiplied.
	 */
	public boolean exposesDerivatives()
	{
		return( false );
	}
	
	
	/**
	 * Returns whether the partial derivative of the elem is zero.
	 * 
	 * @return True if the partial derivative of the elem is zero.
	 */
	public boolean isPartialDerivativeZero()
	{
		return( false );
	}
	
	
	
	@Override
	public SymbolicElem<R, S> handleOptionalOp( Object id , ArrayList<SymbolicElem<R, S>> args ) throws NotInvertibleException
	{
		
		
		final ArrayList<SymbolicElem<R, S>> args2 = new ArrayList<SymbolicElem<R, S>>();
		
		args2.add( this );
		
		if( args != null )
		{
			for( final SymbolicElem<R, S> ii : args )
			{
				args2.add( ii );
			}
		}
		
		return( getFac().getFac().handleSymbolicOptionalOp(id, args2) );
	}
	
	
	/**
	 * Mode determining the extent to which the elem will be reduced to determine if it is a constant.
	 * 
	 * @author thorngreen
	 *
	 */
	public static enum EVAL_MODE{ APPROX , SIMPLIFY , SIMPLIFY2 };
	
	
	/**
	 * Returns approximately whether the elem can be determined to be a symbolic constant.
	 * 
	 * @param mode Mode determining the extent to which the elem will be reduced to determine if it is a constant.
	 * @return  True if the elem can be determined to be a symbolic constant.
	 * @throws NotInvertibleException 
	 */
	public boolean evalSymbolicConstant( EVAL_MODE mode )
	{
		switch( mode )
		{
			case APPROX:
			{
				return( evalSymbolicConstantApprox() );
			}
			
			case SIMPLIFY:
			{
				//try
				{
					return( distributeSimplify().evalSymbolicConstantApprox() );
				}
				//catch( NotInvertibleException ex )
				//{
				//	return( false );
				//}
			}
			
			case SIMPLIFY2:
			{
				// try
				{
					return( this.distributeSimplify2().evalSymbolicConstantApprox() );
				}
				// catch( NotInvertibleException ex )
				//{
				//	return( false );
				//}
			}
		}
		
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	
	
	@Override
	public boolean evalSymbolicZeroApprox( EVAL_MODE mode )
	{
		switch( mode )
		{
			case APPROX:
			{
				return( isSymbolicZero() );
			}
			
			case SIMPLIFY:
			{
				return( distributeSimplify().isSymbolicZero() );
			}
			
			case SIMPLIFY2:
			{
				return( this.distributeSimplify2().isSymbolicZero() );
			}
		}
		
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	
	
	@Override
	public boolean evalSymbolicIdentityApprox( EVAL_MODE mode )
	{
		switch( mode )
		{
			case APPROX:
			{
				return( isSymbolicIdentity() );
			}
			
			case SIMPLIFY:
			{
				return( distributeSimplify().isSymbolicIdentity() );
			}
			
			case SIMPLIFY2:
			{
				return( this.distributeSimplify2().isSymbolicIdentity() );
			}
		}
		
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	
	
	/**
	 * Returns approximately whether the elem can be determined to be a symbolic constant.
	 * 
	 * @return True if the elem can be determined to be a symbolic constant.
	 */
	public boolean evalSymbolicConstantApprox()
	{
		return( false );
	}
	

	
	/**
	 * Returns whether this expression is equal to the one in the parameter, simplifying both sides first.
	 * 
	 * @param b The expression to be compared.
	 * @return True if the expressions are found to be equal, false otherwise.
	 */
	public boolean extSymbolicEquals( SymbolicElem<R, S> b ) throws NotInvertibleException
	{
		SymbolicElem<R, S> aa = this.distributeSimplify();
		SymbolicElem<R, S> bb = b.distributeSimplify();
		boolean ret = aa.symbolicEquals( bb );
		if( ret )
		{
//			System.out.println( "AAAAA" );
		}
		return( ret );
	}
	
	
	/**
	 * Returns whether this expression is equal to the one in the parameter.
	 * 
	 * @param b The expression to be compared.
	 * @return True if the expressions are found to be equal, false otherwise.
	 */
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		throw( new RuntimeException( "Not Supported " + this ) );
	}
	

	
	
	/**
	 * The factory for the enclosed type.
	 */
	protected S fac;
	
	
}


