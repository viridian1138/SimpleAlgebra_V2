



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




package test_simplealgebra;

import java.math.BigInteger;
import java.util.ArrayList;

import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;


/**
 * Tests the ability to evaluate a Fourier transform after changing the derivatives of a function.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestChangeDerivative extends TestCase {
	
	
	
	
	/**
	 * Constant containing the number ten.
	 */
	static final BigInteger TEN = BigInteger.valueOf( 10 );
	
	
	/**
	 * Returns the number <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *       <mrow>
     *         <mi>X</mi>
     *         <mo>+</mo>
     *         <mn>1</mn>
     *       </mrow>
     *   </msup>
     * </mrow>
     * </math>, where X is the input parameter.
	 * 
	 * @param cnt The input parameter.
	 * @return The value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *       <mrow>
     *         <mi>X</mi>
     *         <mo>+</mo>
     *         <mn>1</mn>
     *       </mrow>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	protected static BigInteger calcVal( final int cnt )
	{
		BigInteger ret = TEN;
		for( int i = 0 ; i < cnt ; i++ )
		{
			ret = ret.multiply( TEN );
		}
		return( ret );
	}
	

	
	/**
	 * Constant containing the value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>801</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * Largest possible double is around <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>308</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	static final BigInteger baseVal = calcVal( 800 );
	
	
	/**
	 * Constant containing the square of baseVal, or <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>1602</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	protected static final BigInteger finalBaseValSq = baseVal.multiply( baseVal );
	
	
	/**
	 * Defines a precision of baseVal, or one part in <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>801</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static final class LrgPrecision extends Precision<LrgPrecision>
	{
		@Override
		public BigInteger getVal()
		{
			return( baseVal );
		}
		
		@Override
		public BigInteger getValSquared()
		{
			return( finalBaseValSq );
		}
		
	}
	
	
	/**
	 * A constant defining the large precision.
	 */
	static final LrgPrecision lrgPrecision = new LrgPrecision();
	
	
	
	

	
	/**
	 * A simple approximation for Pi (3.14159...)
	 */
	static final BigFixedPointElem<LrgPrecision> PI_N = new BigFixedPointElem<LrgPrecision>( Math.PI , lrgPrecision );
	
	
	
	
	
	/**
	 * Builds a fixed-point value from a rational number.
	 * 
	 * @param numerator The numerator of the rational number.
	 * @param denominator The denominator of the rational number.
	 * @return The fixed-point value.
	 */
	static BigFixedPointElem<LrgPrecision> buildElem( int numerator , int denominator )
	{
		final BigInteger val = ( BigInteger.valueOf( numerator ) ).multiply( baseVal ).divide( BigInteger.valueOf( denominator ) );
		return( new BigFixedPointElem<LrgPrecision>( val , lrgPrecision ) );
	}	
	
	
	
	/**
	 * Constant for zero.
	 */
	static final BigFixedPointElem<LrgPrecision> ZERO_N = buildElem( 0 , 1 );
	
	
	
	/**
	 * Constant for the identity value.
	 */
	static final BigFixedPointElem<LrgPrecision> IDENTITY_N = buildElem( 1 , 1 );
	
	
	
	
	/**
	 * A simple approximation for Pi (3.14159...)
	 */
	static final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> PI_C = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( PI_N , ZERO_N );
	
	
	
	/**
	 * Constant for zero.
	 */
	static final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> ZERO_C = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( ZERO_N , ZERO_N );
	
	
	
	/**
	 * Constant for the imaginary value.
	 */
	static final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> II_C = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( ZERO_N , IDENTITY_N );
	
	
	
	/**
	 * The number of iterations to use when calculating exponential approximations.
	 */
	static final int EXP_ITER_NUM = 30;
	
	
	
	/**
	 * The size of the initial array from which to calculate derivatives.
	 */
	static final int INITIAL_ARRAY_SIZE = 100;
	
	
	/**
	 * The number of sets of Fourier coefficients.
	 */
	static final int COEFF_SIZE = INITIAL_ARRAY_SIZE / 2;
	
	
	
	
	/**
	 * Generates an ArrayList containing a single exponential decay.
	 * @return An ArrayList containing a single exponential decay.
	 */
	static ArrayList<BigFixedPointElem<LrgPrecision>> generateEvaluationArrayCrvA()
	{
		System.out.println( "Entering Generate Evaluation Array" );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> ret = new ArrayList<BigFixedPointElem<LrgPrecision>>();
		ret.ensureCapacity(INITIAL_ARRAY_SIZE);
		
		final BigFixedPointElem<LrgPrecision> rateA = ( buildElem( -1 , 10 ) ).mult( PI_N );
		
		for( int count = 0 ; count < INITIAL_ARRAY_SIZE ; count++ )
		{
			final BigFixedPointElem<LrgPrecision> parameter = ( buildElem( count , 1 ) ).mult( rateA );
			
			final BigFixedPointElem<LrgPrecision> valA = parameter.exp( EXP_ITER_NUM );
			
			ret.add( valA );
		}
		
		return( ret );
	}
	
	
	
	
	/**
	 * Generates an ArrayList containing a single exponential decay.  Decays twice as fast as "...CrvA()".
	 * @return An ArrayList containing a single exponential decay.
	 */
	static ArrayList<BigFixedPointElem<LrgPrecision>> generateEvaluationArrayCrvB()
	{
		System.out.println( "Entering Generate Evaluation Array" );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> ret = new ArrayList<BigFixedPointElem<LrgPrecision>>();
		ret.ensureCapacity(INITIAL_ARRAY_SIZE);
		
		final BigFixedPointElem<LrgPrecision> rateA = ( buildElem( -2 , 10 ) ).mult( PI_N );
		
		for( int count = 0 ; count < INITIAL_ARRAY_SIZE ; count++ )
		{
			final BigFixedPointElem<LrgPrecision> parameter = ( buildElem( count , 1 ) ).mult( rateA );
			
			final BigFixedPointElem<LrgPrecision> valA = parameter.exp( EXP_ITER_NUM );
			
			ret.add( valA );
		}
		
		return( ret );
	}
	
	
	
	
	/**
	 * Generates an ArrayList containing a superposition of two exponential decays.
	 * @return An ArrayList containing a superposition of two exponential decays.
	 */
	static ArrayList<BigFixedPointElem<LrgPrecision>> generateEvaluationArrayCrvC()
	{
		System.out.println( "Entering Generate Evaluation Array" );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> ret = new ArrayList<BigFixedPointElem<LrgPrecision>>();
		ret.ensureCapacity(INITIAL_ARRAY_SIZE);
		
		final BigFixedPointElem<LrgPrecision> rateA = ( buildElem( -1 , 10 ) ).mult( PI_N );
		
		final BigFixedPointElem<LrgPrecision> rateB = ( buildElem( -2 , 10 ) ).mult( PI_N );
		
		final BigFixedPointElem<LrgPrecision> bmult = ( buildElem( 80000000 , 10 ) );
		
		for( int count = 0 ; count < INITIAL_ARRAY_SIZE ; count++ )
		{
			final BigFixedPointElem<LrgPrecision> parameterA = ( buildElem( count , 1 ) ).mult( rateA );
			
			final BigFixedPointElem<LrgPrecision> parameterB = ( buildElem( count , 1 ) ).mult( rateB );
			
			final BigFixedPointElem<LrgPrecision> valA = parameterA.exp( EXP_ITER_NUM );
			
			final BigFixedPointElem<LrgPrecision> valB = parameterB.exp( EXP_ITER_NUM );
			
			final BigFixedPointElem<LrgPrecision> retVal = valA.add( valB.mult( bmult ) );
			
			ret.add( retVal );
		}
		
		return( ret );
	}
	
	
	
	
	/**
	 * Generates an ArrayList containing a constant function.
	 * @return An ArrayList containing a constant function.
	 */
	static ArrayList<BigFixedPointElem<LrgPrecision>> generateEvaluationArrayFlat()
	{
		System.out.println( "Entering Generate Evaluation Array" );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> ret = new ArrayList<BigFixedPointElem<LrgPrecision>>();
		ret.ensureCapacity(INITIAL_ARRAY_SIZE);
		
		final BigFixedPointElem<LrgPrecision> valA = ( buildElem( -1 , 10 ) );
		
		for( int count = 0 ; count < INITIAL_ARRAY_SIZE ; count++ )
		{
			ret.add( valA );
		}
		
		return( ret );
	}
	
	
	
	
	/**
	 * Computes one derivative of the values in the input ArrayList.  The Delta-X between consecutive values is assumed to be unity.
	 * @param in The ArrayList of values over which to evaluate the derivative.
	 * @return The ArrayList of the computed derivative.
	 */
	static ArrayList<BigFixedPointElem<LrgPrecision>> calcSimpleDerivative( final ArrayList<BigFixedPointElem<LrgPrecision>> in )
	{
		System.out.println( "Entering Calc Simple Derivative " + ( in.size() ) );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> ret = new ArrayList<BigFixedPointElem<LrgPrecision>>();
		final int LEN = in.size();
		ret.ensureCapacity( LEN - 1 );
		
		for( int count = 0 ; count < ( LEN - 1 ) ; count++ )
		{
			final BigFixedPointElem<LrgPrecision> prev = in.get( count );
			
			final BigFixedPointElem<LrgPrecision> next = in.get( count+1 );
			
			final BigFixedPointElem<LrgPrecision> valA = next.add( prev.negate() );
			
			ret.add( valA );
		}
		
		return( ret );
	}
	
	
	
	
	/**
	 * Returns a value for the middle of the input ArrayList.
	 * @param in The input ArrayList.
	 * @return The calculated middle value.
	 */
	static BigFixedPointElem<LrgPrecision> calcMidValue( final ArrayList<BigFixedPointElem<LrgPrecision>> in )
	{
		final int LEN = in.size();
		
		final boolean EVEN = (LEN % 2 ) == 0;
		
		final int DIVTWO = LEN / 2;
		
		if( EVEN )
		{
			final BigFixedPointElem<LrgPrecision> sum = ( in.get( DIVTWO - 1 ) ).add( in.get( DIVTWO ) );
			final BigFixedPointElem<LrgPrecision> ret = sum.divideBy( 2 );
			return( ret );
		}
		else
		{
			final BigFixedPointElem<LrgPrecision> ret = in.get( DIVTWO );
			return( ret );
		}
		
	}
	
	
	
	
	/**
	 * Returns a changed version of the input derivatives.
	 * @param in The ArrayList of input derivatives.
	 * @return The ArrayList of changed derivatives.
	 */
	static ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> applyDerivativeChange( final ArrayList<BigFixedPointElem<LrgPrecision>> in )
	{
		System.out.println( "Entering Apply Derivative Change " + ( in.size() ) );
		
		final int LEN = in.size();
		
		if( LEN == 0 )
		{
			throw( new RuntimeException( "No Elements Available" ) );
		}
		
		final  ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>  ret = new  ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		ret.ensureCapacity(LEN);
		
		/*
		 * Actually shorthand for ( i ) / ( -1 )
		 */
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> nextMult = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( ZERO_N , IDENTITY_N.negate() );
		
		
		ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> multChange = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( IDENTITY_N , ZERO_N );
		
		
		for( int count = 0 ; count < LEN ; count++ )
		{
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> val = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( in.get(count) , ZERO_N );
			
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> valA = val.mult( multChange );
			
			ret.add( valA );
			
			multChange = multChange.mult( nextMult );
		}
		
		return( ret );
	}
	
	
	
	
	/**
	 * Computes numerical derivatives of the values in the input ArrayList.  The Delta-X between consecutive values is assumed to be unity.
	 * @param in The ArrayList of values over which to evaluate the derivatives.
	 * @return The ArrayList of computed derivatives indexed by derivative number.
	 */
	static ArrayList<BigFixedPointElem<LrgPrecision>> calculateDerivatives( final ArrayList<BigFixedPointElem<LrgPrecision>> in )
	{
		System.out.println( "Entering Calculate Derivatives " + ( in.size() ) );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> ret = new ArrayList<BigFixedPointElem<LrgPrecision>>();
		ret.ensureCapacity(INITIAL_ARRAY_SIZE);
		
		ArrayList<BigFixedPointElem<LrgPrecision>> current = in;
		
		for( int count = 0 ; count < INITIAL_ARRAY_SIZE ; count++ )
		{
			final BigFixedPointElem<LrgPrecision> valA = calcMidValue( current );
			
			// System.out.println( valA.toDouble() );
			
			ret.add( valA );
			
			current = calcSimpleDerivative( current );
		}
		
		return( ret );
	}
	
	
	
	
	
	
	/**
	 * Returns an ArrayList of Taylor coefficients given an input ArrayList of derivatives.
	 * @param derivatives The input ArrayList of derivatives.
	 * @return The generated ArrayList of Taylor coefficients.
	 */
	static ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> buildTaylorCoeffs( final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> derivatives )
	{
		System.out.println( "Entering Build Taylor Coeffs " + ( derivatives.size() ) );
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> ret = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		ret.ensureCapacity(INITIAL_ARRAY_SIZE);
		
		BigInteger factorial = BigInteger.ONE;
		
		for( int count = 0 ; count < INITIAL_ARRAY_SIZE ; count++ )
		{
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> derivative = derivatives.get(count);
			
			if( count > 0 )
			{
				final BigInteger countVal = BigInteger.valueOf( count );
				
				factorial = factorial.multiply( countVal );
			}
			
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> coeff = derivative.divideBy( factorial );
			
			// System.out.print( coeff.getRe().toDouble() );
			
			// System.out.print( " " );
			
			// System.out.println( coeff.getIm().toDouble() );
			
			ret.add( coeff );
			
		}
		
		return( ret );
	}
	
	
	
	
	/**
	 * Evaluates a Taylor series.
	 * @param coeffs ArrayList of input Taylor coefficients.
	 * @param x The parameter value at which to evaluate the Taylor series.
	 * @return The result of evaluating the Taylor series.
	 */
	static ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> evalTaylorSeries( final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> coeffs , ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> x )
	{
		System.out.println( "Entering Eval Taylor Series " + ( x.getRe().toDouble() ) );
		
		ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> sum = ZERO_C;
		
		ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> xpow = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( IDENTITY_N , ZERO_N );
		
		
		for( int count = 0 ; count < INITIAL_ARRAY_SIZE ; count++ )
		{
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> coeff = coeffs.get(count);
			
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> term = xpow.mult( coeff );
			
			sum = sum.add( term );
			
			xpow = xpow.mult( x );
			
		}
		
		
		return( sum );
		
	}
	
	
	
	
	
	
	/**
	 * Calculates Taylor coefficients after changing the derivatives of the input function.
	 * @param evalArr ArrayList of input function values over which to calculate the derivatives.
	 * @return The generated ArrayList of Taylor coefficients.
	 */
	static ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> performTaylorCoeffGeneration( final ArrayList<BigFixedPointElem<LrgPrecision>> evalArr )
	{
		System.out.println( "Entering Perform Taylor Coeff Generation " + ( evalArr.size() ) );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> derivatives = calculateDerivatives( evalArr );
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> changeDerivatives = applyDerivativeChange( derivatives );
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> taylorCoeffs = buildTaylorCoeffs( changeDerivatives );
	
		return( taylorCoeffs );
	}
	
	
	
	
	
	
	/**
	 * Calculates Fourier coefficients after changing the derivatives of the input function.
	 * @param evalArr ArrayList of input function values over which to calculate the derivatives.
	 * @param coeff1 Output ArrayList to receive "cosine" Fourier coefficients.
	 * @param coeff2 Output ArrayList to receive "sine" Fourier coefficients.
	 * @throws Throwable
	 */
	static void evalFourierCoeffs( final ArrayList<BigFixedPointElem<LrgPrecision>> evalArr , final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> coeff1 , final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> coeff2 ) throws Throwable
	{
		System.out.println( "Entering Eval Fourier Coeffs " );
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> taylorCoeffs = performTaylorCoeffGeneration( evalArr );
		
		coeff1.ensureCapacity(COEFF_SIZE);
		
		coeff2.ensureCapacity(COEFF_SIZE);
		
		
		
		
		coeff1.add( ZERO_C );
		coeff2.add( ZERO_C );
		for( int count2 = 1 ; count2 < COEFF_SIZE ; count2++ )
		{
			coeff1.add( ZERO_C );
			coeff2.add( ZERO_C );
		}
		
		
		final int ITER_SIZE = 400;
		
		final int DIVISOR_SIZE = 2 * ITER_SIZE / INITIAL_ARRAY_SIZE;
		
		
		for( int count = -ITER_SIZE ; count <= ITER_SIZE ; count++ )
		{
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> evalCount = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( buildElem( count , 1 ) , ZERO_N );
			
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> val = evalTaylorSeries( taylorCoeffs , evalCount.divideBy( DIVISOR_SIZE ) );
			
			for( int count2 = 1 ; count2 < COEFF_SIZE ; count2++ )
			{
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> evalCount2 = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( buildElem( count2 , 1 ) , ZERO_N );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> theta = PI_C.mult( evalCount ).mult( evalCount2 ).divideBy( ITER_SIZE );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> wave = ( II_C.mult( theta ) ).exp( EXP_ITER_NUM );
				
				final BigFixedPointElem<LrgPrecision> wave_re_n = wave.getRe();
				
				final BigFixedPointElem<LrgPrecision> wave_im_n = wave.getIm();
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> wave_re_c = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(wave_re_n, ZERO_N);
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> wave_im_c = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(wave_im_n, ZERO_N);
				
				coeff1.set(count2, coeff1.get(count2).add( wave_re_c.mult( val ) ) );
				
				coeff2.set(count2, coeff2.get(count2).add( wave_im_c.mult( val ) ) );
			}
			
		}
		
		
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> taylorConstantCoeff = taylorCoeffs.get( 0 );
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> XMULT = ( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( IDENTITY_N.divideBy(DIVISOR_SIZE) , ZERO_N ) ).mult( taylorConstantCoeff.invertLeft() );
		
		
		/*
		 * Apply the delta-x part of the numerical integration with a single multiplication at the end.
		 */
		for( int count2 = 1 ; count2 < COEFF_SIZE ; count2++ )
		{
			coeff1.set(count2, coeff1.get(count2).mult( XMULT ) );
			
			coeff2.set(count2, coeff2.get(count2).mult( XMULT ) );
		}
		
		
		
	}
	
	
	
	

	
	/**
	 * Tests the ability to evaluate a Fourier transform after changing the derivatives of a function.
	 * @param evalArr The function values over which to test.
	 * @throws Throwable
	 */
	public void runTest( final ArrayList<BigFixedPointElem<LrgPrecision>> evalArr ) throws Throwable
	{
		System.out.println( "Entering Run Test" );
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> coeff1 = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> coeff2 = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		
		evalFourierCoeffs( evalArr , coeff1 , coeff2 );
		
		System.out.println( "Fourier Coeffs Complete" );
		
		for( int count2 = 1 ; count2 < COEFF_SIZE ; count2++ )
		{
			final BigFixedPointElem<LrgPrecision> re_1 = coeff1.get(count2).getRe();

			final BigFixedPointElem<LrgPrecision> im_1 = coeff1.get(count2).getIm();
			
			final BigFixedPointElem<LrgPrecision> re_2 = coeff2.get(count2).getRe();

			final BigFixedPointElem<LrgPrecision> im_2 = coeff2.get(count2).getIm();
			
			final BigFixedPointElem<LrgPrecision> sum_sqr = ( re_1.mult( re_1 ) ).add( im_1.mult( im_1 ) ).add( re_2.mult( re_2 ) ).add( im_2.mult( im_2 ) );
			
			// BigFixedPointElem<LrgPrecision> sumv = sum_sqr.powR( buildElem(1 , 2) , EXP_ITER_NUM , EXP_ITER_NUM );
			
			final double vl = sum_sqr.toDouble();
			
			System.out.println( count2 );
			
			System.out.println( vl );
			
			// System.out.println( sum_sqr.getPrecVal() );
		}
		
	}
	
	
	
	

	
	/**
	 * Tests the ability to evaluate a Fourier transform after changing the derivatives of a function.for a single exponential decay.
	 * @throws Throwable
	 */
	public void testChangeDerivativeCrvA() throws Throwable
	{
		System.out.println( "Entering Test Change Derivative A" );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> evalArr = generateEvaluationArrayCrvA();
		
		runTest( evalArr );
		
	}
	
	
	
	

	
	/**
	 * Tests the ability to evaluate a Fourier transform after changing the derivatives of a function.for a single exponential decay.  Decays twice as fast as "...CrvA()".
	 * @throws Throwable
	 */
	public void testChangeDerivativeCrvB() throws Throwable
	{
		System.out.println( "Entering Test Change Derivative B" );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> evalArr = generateEvaluationArrayCrvB();
		
		runTest( evalArr );
		
	}
	
	
	
	

	
	/**
	 * Tests the ability to evaluate a Fourier transform after changing the derivatives of a function.for a superposition of two exponential decays.
	 * @throws Throwable
	 */
	public void testChangeDerivativeCrvC() throws Throwable
	{
		System.out.println( "Entering Test Change Derivative C" );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> evalArr = generateEvaluationArrayCrvC();
		
		runTest( evalArr );
		
	}
	
	
	
	

	
	/**
	 * Tests the ability to evaluate a Fourier transform after changing the derivatives of a function.for a constant function.
	 * @throws Throwable
	 */
	public void testChangeDerivativeFlat() throws Throwable
	{
		System.out.println( "Entering Test Change Derivative Flat" );
		
		final ArrayList<BigFixedPointElem<LrgPrecision>> evalArr = generateEvaluationArrayFlat();
		
		runTest( evalArr );
		
	}
	
	
	
	
}



