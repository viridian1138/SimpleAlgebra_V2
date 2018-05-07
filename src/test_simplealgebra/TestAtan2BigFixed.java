



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
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;


/**
 * Same test as TestAtan2, but with higher precision exp and ln.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestAtan2BigFixed extends TestCase {
	
	
	
	
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
	 * The number of levels to iterate when evaluating.
	 */
	static final int LEVEL = 64;
	
	
	
	/**
	 * Tests whether two angles are equivalent.
	 * @param a1 The first angle to compare in units of radians.
	 * @param a2 The second angle to compare in units of radians.
	 */
	protected void compareAngles( double a1 , double a2 )
	{
		final double twop = 2.0 * Math.PI;
		while( ( a2 - a1 ) > twop )
		{
			a2 -= twop;
		}
		while( ( a1 - a2 ) > twop )
		{
			a2 += twop;
		}
		Assert.assertTrue( ( Math.abs( a1 - a2 ) < 1E-3 ) || 
			( ( twop - Math.abs( a1 - a2 ) ) < 1E-3 ) );
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
	private BigFixedPointElem<LrgPrecision> sinhTest( final BigFixedPointElem<LrgPrecision> x , int numIter )
	{
		final BigFixedPointElem<LrgPrecision> ret = ( x.exp( numIter ) ).add( x.negate().exp( numIter ).negate() ).divideBy( 2 );
		return( ret );
	}
	
	
	
	
	/**
	 * Implements the hyperbolic cosine function defined by <math display="inline">
     * <mrow>
     *  <mo>sinh(</mo>
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
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The hyperbolic cosine of the argument.
	 */
	private BigFixedPointElem<LrgPrecision> coshTest( final BigFixedPointElem<LrgPrecision> x , int numIter )
	{
		final BigFixedPointElem<LrgPrecision> ret = ( x.exp( numIter ) ).add( x.negate().exp( numIter ) ).divideBy( 2 );
		return( ret );
	}
	
	
		
	
	
	/**
	 * Tests the ability to calculate cosines
	 * @throws Throwable
	 */
	public void testCosine() throws Throwable
	{
		final Random rand = new Random( 6666 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			final double a = 20.0 * ( rand.nextDouble() ) - 10.0;
			final double as = Math.cos( a );
			final BigFixedPointElem<LrgPrecision> da = new BigFixedPointElem<LrgPrecision>( a , lrgPrecision );
			final BigFixedPointElem<LrgPrecision> das = da.cos( LEVEL );
			Assert.assertTrue( Math.abs( as - das.toDouble() ) < 1E-3 );
		}
	}
	


	
	
	
	/**
	 * Tests the ability to calculate sines
	 * @throws Throwable
	 */
	public void testSine() throws Throwable
	{
		final Random rand = new Random( 5555 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			final double a = 20.0 * ( rand.nextDouble() ) - 10.0;
			final double as = Math.sin( a );
			final BigFixedPointElem<LrgPrecision> da = new BigFixedPointElem<LrgPrecision>( a , lrgPrecision );
			final BigFixedPointElem<LrgPrecision> das = da.sin( LEVEL );
			Assert.assertTrue( Math.abs( as - das.toDouble() ) < 1E-3 );
		}
	}
	
	
	
	/**
	 * Tests the ability to calculate hyperbolic cosines
	 * @throws Throwable
	 */
	public void testCosh() throws Throwable
	{
		final Random rand = new Random( 6666 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			final double a = 20.0 * ( rand.nextDouble() ) - 10.0;
			final double as = Math.cosh( a );
			final BigFixedPointElem<LrgPrecision> da = new BigFixedPointElem<LrgPrecision>( a , lrgPrecision );
			final BigFixedPointElem<LrgPrecision> das = da.cosh( LEVEL );
			Assert.assertTrue( Math.abs( as - das.toDouble() ) < 1E-3 );
		}
	}
	
	
	
	
	
	/**
	 * Tests the ability to calculate natural exponents.
	 * @throws Throwable
	 */
	public void testExp() throws Throwable
	{
		final Random rand = new Random( 5555 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			final double a = 20.0 * ( rand.nextDouble() ) - 10.0;
			final double as = Math.exp( a );
			final BigFixedPointElem<LrgPrecision> da = new BigFixedPointElem<LrgPrecision>( a , lrgPrecision );
			final BigFixedPointElem<LrgPrecision> das = da.exp( LEVEL );
			Assert.assertTrue( Math.abs( as - das.toDouble() ) < 1E-3 );
		}
	}
	
	
	
	/**
	 * Tests the ability to calculate natural logarithms.
	 * @throws Throwable
	 */
	public void testLn() throws Throwable
	{
		final Random rand = new Random( 5555 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			final double a = 20.0 * ( rand.nextDouble() );
			final double as = Math.log( a );
			final BigFixedPointElem<LrgPrecision> da = new BigFixedPointElem<LrgPrecision>( a , lrgPrecision );
			final BigFixedPointElem<LrgPrecision> das = da.ln( LEVEL , LEVEL );
			Assert.assertTrue( Math.abs( as - das.toDouble() ) < 1E-3 );
		}
	}
	
	
	
	
	/**
	 * Tests the ability to take natural logarithms of complex numbers.
	 * @throws Throwable
	 */
	public void testCplxLn() throws Throwable
	{
		final Random rand = new Random( 4444 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			
			final double dd0 = 7.0 * ( rand.nextDouble() ) - 3.5;
			final double dd1 = 10.0 * ( rand.nextDouble() ) - 5.0;
			
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> dda =
					new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( dd0 , lrgPrecision ) , new BigFixedPointElem<LrgPrecision>( dd1 , lrgPrecision ) );
			
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> expd = dda.exp( LEVEL );
			
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> d2 = expd.ln(LEVEL, LEVEL);
			
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> d3 = d2.exp( LEVEL );
			
			Assert.assertTrue( Math.abs( expd.getRe().toDouble() - d3.getRe().toDouble() ) < 1E-5 );
			
			Assert.assertTrue( Math.abs( expd.getIm().toDouble() - d3.getIm().toDouble() ) < 1E-5 );
		}
	}
	
	
	
	
	
	
	
	/**
	 * Tests the ability to take natural logarithms of complex numbers.
	 * @throws Throwable
	 */
	public void testCplxDLn() throws Throwable
	{
		final Random rand = new Random( 4444 );
		for( int cnt = 0 ; cnt < 8000 ; cnt++ )
		{
			System.out.println( cnt );
			
			final double dd0 = 1.75 * ( rand.nextDouble() ) - 0.875;
			final double dd1 = 1.75 * ( rand.nextDouble() ) - 0.875;
			final double dd2 = 1.75 * ( rand.nextDouble() ) - 0.875;
			final double dd3 = 1.75 * ( rand.nextDouble() ) - 0.875;
			
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> dda0 =
					new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( dd0 , lrgPrecision ) , new BigFixedPointElem<LrgPrecision>( dd1 , lrgPrecision ) );
			
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> dda1 =
					new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( dd2 , lrgPrecision ) , new BigFixedPointElem<LrgPrecision>( dd3 , lrgPrecision ) );
			
			final ComplexElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> dda =
					new ComplexElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( dda0 , dda1 );
			
			final ComplexElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> expd = dda.exp( LEVEL );
			
			final ComplexElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> d2 = expd.ln(LEVEL, LEVEL);
			
			final ComplexElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> d3 = d2.exp( LEVEL );
			
//			System.out.println( expd.getRe().getRe().toDouble() );
//			System.out.println( d3.getRe().getRe().toDouble() );
//			System.out.println( expd.getRe().getIm().toDouble() );
//			System.out.println( d3.getRe().getIm().toDouble() );
//			System.out.println( expd.getIm().getRe().toDouble() );
//			System.out.println( d3.getIm().getRe().toDouble() );
//			System.out.println( expd.getIm().getIm().toDouble() );
//			System.out.println( d3.getIm().getIm().toDouble() );
			
			
			Assert.assertTrue( Math.abs( expd.getRe().getRe().toDouble() - d3.getRe().getRe().toDouble() ) < 1E-5 );
			
			Assert.assertTrue( Math.abs( expd.getRe().getIm().toDouble() - d3.getRe().getIm().toDouble() ) < 1E-5 );
			
		    Assert.assertTrue( Math.abs( expd.getIm().getRe().toDouble() - d3.getIm().getRe().toDouble() ) < 1E-5 );
			
		    Assert.assertTrue( Math.abs( expd.getIm().getIm().toDouble() - d3.getIm().getIm().toDouble() ) < 1E-5 );
			
		}
	}
	
	
	
	
	
	
	
	/**
	 * Tests the ability to calculate hyperbolic sines
	 * @throws Throwable
	 */
	public void testSinh() throws Throwable
	{
		final Random rand = new Random( 6666 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			final double a = 20.0 * ( rand.nextDouble() ) - 10.0;
			final double as = Math.sinh( a );
			final BigFixedPointElem<LrgPrecision> da = new BigFixedPointElem<LrgPrecision>( a , lrgPrecision );
			final BigFixedPointElem<LrgPrecision> das = da.sinh( LEVEL );
			Assert.assertTrue( Math.abs( as - das.toDouble() ) < 1E-3 );
		}
	}
	
	
	
	
	/**
	 * Tests the ability to calculate arcsines.
	 * @throws Throwable
	 */
	public void testAsin() throws Throwable
	{
		final Random rand = new Random( 4444 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			final double x = 2.0 * ( rand.nextDouble() ) - 1.0;
			final BigFixedPointElem<LrgPrecision> xd = new BigFixedPointElem<LrgPrecision>( x , lrgPrecision );
			final BigFixedPointElem<LrgPrecision> asin = xd.asin( LEVEL , LEVEL );
			Assert.assertTrue( Math.abs( x - ( Math.sin( asin.toDouble() ) ) ) < 1E-3 );
		}
	}
	
	
	

	/**
	 * Tests the ability to calculate arccosines.
	 * @throws Throwable
	 */
	public void testAcos() throws Throwable
	{
		final Random rand = new Random( 4444 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			final double x = 2.0 * ( rand.nextDouble() ) - 1.0;
			final BigFixedPointElem<LrgPrecision> xd = new BigFixedPointElem<LrgPrecision>( x , lrgPrecision );
			final BigFixedPointElem<LrgPrecision> acos = xd.acos( LEVEL , LEVEL );
			Assert.assertTrue( Math.abs( x - ( Math.cos( acos.toDouble() ) ) ) < 1E-3 );
		}
	}
	
	
	
	
	
	/**
	 * Tests the ability to calculate inverse hyperbolic cosines.
	 * @throws Throwable
	 */
	public void testAcosh() throws Throwable
	{
		final Random rand = new Random( 4444 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			
			final Double dd = 5.0 * ( rand.nextDouble() ) + 1.0;
			
			System.out.println( dd );
			
			final BigFixedPointElem<LrgPrecision> d = new BigFixedPointElem<LrgPrecision>( dd , lrgPrecision );
			
			final BigFixedPointElem<LrgPrecision> acoshd = d.acosh( LEVEL , LEVEL );
			
			final BigFixedPointElem<LrgPrecision> d2 = acoshd.cosh( LEVEL );
			
			final BigFixedPointElem<LrgPrecision> d3 = coshTest( acoshd , LEVEL );
			
			System.out.println( "*** " + ( d.toDouble() ) + " *** " + ( d2.toDouble() ) );
			Assert.assertTrue( Math.abs( d.toDouble() - d2.toDouble() ) < 1E-5 );
			
			Assert.assertTrue( Math.abs( d.toDouble() - d3.toDouble() ) < 1E-5 );
		}
	}
	
	
	
	
	
	/**
	 * Tests the ability to calculate inverse hyperbolic sines.
	 * @throws Throwable
	 */
	public void testAsinh() throws Throwable
	{
		final Random rand = new Random( 4444 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			
			final Double dd = 10.0 * ( rand.nextDouble() ) - 5.0;
			
			System.out.println( dd );
			
			final BigFixedPointElem<LrgPrecision> d = new BigFixedPointElem<LrgPrecision>( dd , lrgPrecision );
			
			final BigFixedPointElem<LrgPrecision> asinhd = d.asinh( LEVEL , LEVEL );
			
			final BigFixedPointElem<LrgPrecision> d2 = asinhd.sinh( LEVEL );
			
			final BigFixedPointElem<LrgPrecision> d3 = sinhTest( asinhd , LEVEL );
			
			System.out.println( "*** " + ( d.toDouble() ) + " *** " + ( d2.toDouble() ) );
			Assert.assertTrue( Math.abs( d.toDouble() - d2.toDouble() ) < 1E-5 );
			
			Assert.assertTrue( Math.abs( d.toDouble() - d3.toDouble() ) < 1E-5 );
		}
	}
	
	
	
	
	/**
	 * Tests the ability to calculate arctangents.
	 * @throws Throwable
	 */
	public void testAtan2() throws Throwable
	{
		final Random rand = new Random( 4444 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			final double x = 10.0 * ( rand.nextDouble() ) - 5.0;
			final double y = 10.0 * ( rand.nextDouble() ) - 5.0;
			if( Math.sqrt( x * x + y * y ) > 1E-2 ) // Skip degenerate cases near the origin
			{
				final double stdAtan = Math.atan2( y , x );
				final BigFixedPointElem<LrgPrecision> xd = new BigFixedPointElem<LrgPrecision>( x , lrgPrecision );
				final BigFixedPointElem<LrgPrecision> yd = new BigFixedPointElem<LrgPrecision>( y , lrgPrecision );
				final BigFixedPointElem<LrgPrecision> atan = yd.atan2( xd , 25 , 25 );
				compareAngles( stdAtan , atan.toDouble() );
			}
		}
	}
	
	
	
	
}



