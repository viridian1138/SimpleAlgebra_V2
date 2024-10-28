



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




package simplealgebra.algo;

import java.math.BigInteger;
import java.util.HashMap;

import simplealgebra.ComplexElem;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;

/**
 * Generator for factorials and gamma functions extended to Elems.
 * 
 * Factorials for arbitrary elems are calculated using the gamma function.
 * 
 * See http://en.wikipedia.org/wiki/Factorial
 * 
 * See http://en.wikipedia.org/wiki/Gamma_function
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed elem type.
 * @param <S> The factory for the enclosed elem type.
 */
public abstract class ExtendedFactorial<R extends Elem<R,?>, S extends ElemFactory<R,S>>
{

	/**
	 * The factory for the enclosed elem type.
	 */
	protected S fac;
	
	/**
	 * An approximate value for pi, e.g. 3.14159.  Better approximations produce better results.
	 */
	protected R pi;
	
	/**
	 * The number of iterations for the factorial approximation.
	 */
	protected int numIterFactorial;
	
	
	
	/**
	 * Converts a BigInteger to the enclosed elem type.
	 * @param val The BigInteger to be converted.
	 * @return The value in the enclosed elem type
	 * @throws NotInvertibleException
	 */
	protected R convert( BigInteger val ) throws NotInvertibleException
	{
		R ret = ( fac.identity().divideBy( val ) ).invertLeft();
		return( ret );
	}
	
	
	/**
	 * Initializes the extended factorial instance.
	 * 
	 * @param _pi An approximate value for pi, e.g. 3.14159.  Better approximations produce better results.
	 * @param numIterFactorial The number of iterations for the factorial approximation.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public void init( R _pi, int _numIterFactorial ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		pi = _pi;
		numIterFactorial = _numIterFactorial;
	}
	
	
	/**
	 * Returns whether the "real" part of a number is positive beyond a particular epsilon
	 * @param vv The number to be checked.
	 * @return True if the "real" part of a number is positive beyond a particular epsilon, false otherwise.
	 */
	protected abstract boolean beyondPositiveEpsilon( R vv );
	
	
	/**
	 * Calculates the natural log of the input integer in the type of the enclosed elem
	 * @param n The integer from which to take the log
	 * @param numIterExp Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn Number of iterations to build the underlying logarithm approximation.
	 * @return The natural log of the number
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected R calcLnNval( int n , int numIterExp, int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		R nvar = convert( BigInteger.valueOf(n) );
		return( nvar.ln(numIterExp, numIterLn) );
	}
	
	
	/**
	 * Returns a series approximation of a factorial for cases where the "real" part of a number is positive beyond a particular epsilon
	 * 
	 * @param vv The elem for which to take the factorial
	 * @param numIterExp Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn Number of iterations to build the underlying logarithm approximation.
	 * @return The approximate factorial
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected R coreInterpFactorialSeries( R vv, int numIterExp, int numIterLn  ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		// Algorithm adapted from an answer on a web Q and A site
		// It is similar to the Gauss rewrite of the Euler product formula for Gamma
		
		int n = numIterFactorial;
		
		R total = fac.identity();
		
		for( int i = 1 ; i < n ; i++ )
		{
			// System.out.println( i );
			R ivar = convert( BigInteger.valueOf(i) );
			R numer = ivar;
			R denom = vv.add( ivar );
			total = total.mult( numer ).mult( denom.invertLeft() );
		}
		
		
		R eterm = vv.mult( calcLnNval(n, numIterExp, numIterLn) );
		total = total.mult( eterm.exp(numIterExp) );
		
		return( total );
	}
	
	
	/**
	 * Calculates a gamma function
	 * 
	 * @param vv The value over which to calculate the gamma function
	 * @param numIterExp Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn Number of iterations to build the underlying logarithm approximation.
	 * @return The gamma function
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R extendedGamma( R vv, int numIterExp, int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		
		if( beyondPositiveEpsilon( vv ) )
		{
			return( coreInterpFactorialSeries( vv.add( fac.identity().negate() )  , numIterExp, numIterLn ) );
		}
		else
		{
			R vv2 = fac.identity().add( vv.negate() );
			R numer = pi;
			R gammaMultVal = coreInterpFactorialSeries( vv2.add( fac.identity().negate() )  , numIterExp, numIterLn );
			R sinArg = pi.mult( vv );
			R denom = ( sinArg.sin( numIterExp ) ).mult( gammaMultVal );
			return( numer.mult( denom.invertLeft() ) );
		}
	}
	
	
	/**
	 * Calculates a factorial
	 * 
	 * @param vv The value over which to calculate the factorial
	 * @param numIterExp Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn Number of iterations to build the underlying logarithm approximation.
	 * @return The factorial
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R extendedFactorial( R vv, int numIterExp, int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( this.extendedGamma( vv.add( fac.identity() ) , numIterExp, numIterLn) );
	}
	
	
	/**
	 * Calculates a binomial coefficient.
	 * @param n The set parameter of the binomial coefficient.
	 * @param k The subset parameter of the binomial coefficient.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn  Number of iterations to build the underlying logarithm approximation.
	 * @return The binomial coefficient.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R extendedBinomialCoefficient( R n, R k, int numIterExp, int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final R numer = extendedFactorial( n, numIterExp, numIterLn );
		
		final R denom1 = extendedFactorial( n.add( k.negate() ), numIterExp, numIterLn );
		final R denom2 = extendedFactorial( k, numIterExp, numIterLn );
		final R denom = denom1.mult( denom2 );
		final R ret = numer.mult( denom.invertLeft() );
		return( ret );
	}
	
	
	
	
	
	/**
	 * Constructor
	 * 
	 * @param _fac The factory for the enclosed type
	 */
	public ExtendedFactorial( S _fac ) {
		fac = _fac;
	}
	
	
	
	

}
