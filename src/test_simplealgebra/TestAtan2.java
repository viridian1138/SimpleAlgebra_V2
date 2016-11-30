



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

import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;


/**
 * Tests the ability to calculate arctangeants and other related functions.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestAtan2 extends TestCase {
	
	
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
	private DoubleElem sinhTest( final DoubleElem x , int numIter )
	{
		final DoubleElem ret = ( x.exp( numIter ) ).add( x.negate().exp( numIter ).negate() ).divideBy( 2 );
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
	private DoubleElem coshTest( final DoubleElem x , int numIter )
	{
		final DoubleElem ret = ( x.exp( numIter ) ).add( x.negate().exp( numIter ) ).divideBy( 2 );
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
			final DoubleElem da = new DoubleElem( a );
			final DoubleElem das = da.cos( 20 );
			Assert.assertTrue( Math.abs( as - das.getVal() ) < 1E-3 );
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
			final DoubleElem da = new DoubleElem( a );
			final DoubleElem das = da.sin( 20 );
			Assert.assertTrue( Math.abs( as - das.getVal() ) < 1E-3 );
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
			final DoubleElem da = new DoubleElem( a );
			final DoubleElem das = da.cosh( 20 );
			Assert.assertTrue( Math.abs( as - das.getVal() ) < 1E-3 );
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
			final DoubleElem da = new DoubleElem( a );
			final DoubleElem das = da.exp( 20 );
			Assert.assertTrue( Math.abs( as - das.getVal() ) < 1E-3 );
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
			final DoubleElem da = new DoubleElem( a );
			final DoubleElem das = da.ln( 20 , 20 );
			Assert.assertTrue( Math.abs( as - das.getVal() ) < 1E-3 );
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
			
			final ComplexElem<DoubleElem,DoubleElemFactory> dda =
					new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( dd0 ) , new DoubleElem( dd1 ) );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> expd = dda.exp( 20 );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> d2 = expd.ln(20, 20);
			
			final ComplexElem<DoubleElem,DoubleElemFactory> d3 = d2.exp( 20 );
			
			Assert.assertTrue( Math.abs( expd.getRe().getVal() - d3.getRe().getVal() ) < 1E-5 );
			
			Assert.assertTrue( Math.abs( expd.getIm().getVal() - d3.getIm().getVal() ) < 1E-5 );
		}
	}
	
	
	
	
	
	
	
	/**
	 * Tests the ability to take natural logarithms of complex numbers.
	 * @throws Throwable
	 */
	public void testCplxDLn() throws Throwable
	{
		final Random rand = new Random( 4444 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			
			final double dd0 = 1.75 * ( rand.nextDouble() ) - 0.875;
			final double dd1 = 1.75 * ( rand.nextDouble() ) - 0.875;
			final double dd2 = 1.75 * ( rand.nextDouble() ) - 0.875;
			final double dd3 = 1.75 * ( rand.nextDouble() ) - 0.875;
			
			final ComplexElem<DoubleElem,DoubleElemFactory> dda0 =
					new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( dd0 ) , new DoubleElem( dd1 ) );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> dda1 =
					new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( dd2 ) , new DoubleElem( dd3 ) );
			
			final ComplexElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> dda =
					new ComplexElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( dda0 , dda1 );
			
			final ComplexElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expd = dda.exp( 20 );
			
			final ComplexElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> d2 = expd.ln(20, 20);
			
			final ComplexElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> d3 = d2.exp( 20 );
			
//			System.out.println( expd.getRe().getRe().getVal() );
//			System.out.println( d3.getRe().getRe().getVal() );
//			System.out.println( expd.getRe().getIm().getVal() );
//			System.out.println( d3.getRe().getIm().getVal() );
//			System.out.println( expd.getIm().getRe().getVal() );
//			System.out.println( d3.getIm().getRe().getVal() );
//			System.out.println( expd.getIm().getIm().getVal() );
//			System.out.println( d3.getIm().getIm().getVal() );
			
			Assert.assertTrue( Math.abs( expd.getRe().getRe().getVal() - d3.getRe().getRe().getVal() ) < 1E-5 );
			
			Assert.assertTrue( Math.abs( expd.getRe().getIm().getVal() - d3.getRe().getIm().getVal() ) < 1E-5 );
			
			Assert.assertTrue( Math.abs( expd.getIm().getRe().getVal() - d3.getIm().getRe().getVal() ) < 1E-5 );
			
			Assert.assertTrue( Math.abs( expd.getIm().getIm().getVal() - d3.getIm().getIm().getVal() ) < 1E-5 );
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
			final DoubleElem da = new DoubleElem( a );
			final DoubleElem das = da.sinh( 20 );
			Assert.assertTrue( Math.abs( as - das.getVal() ) < 1E-3 );
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
			final DoubleElem xd = new DoubleElem( x );
			final DoubleElem asin = xd.asin( 20 , 20 );
			Assert.assertTrue( Math.abs( x - ( Math.sin( asin.getVal() ) ) ) < 1E-3 );
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
			final DoubleElem xd = new DoubleElem( x );
			final DoubleElem acos = xd.acos( 20 , 20 );
			Assert.assertTrue( Math.abs( x - ( Math.cos( acos.getVal() ) ) ) < 1E-3 );
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
			
			final DoubleElem d = new DoubleElem( dd );
			
			final DoubleElem acoshd = d.acosh( 20 , 20 );
			
			final DoubleElem d2 = acoshd.cosh( 20 );
			
			final DoubleElem d3 = coshTest( acoshd , 20 );
			
			System.out.println( "*** " + ( d.getVal() ) + " *** " + ( d2.getVal() ) );
			Assert.assertTrue( Math.abs( d.getVal() - d2.getVal() ) < 1E-5 );
			
			Assert.assertTrue( Math.abs( d.getVal() - d3.getVal() ) < 1E-5 );
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
			
			final DoubleElem d = new DoubleElem( dd );
			
			final DoubleElem asinhd = d.asinh( 20 , 20 );
			
			final DoubleElem d2 = asinhd.sinh( 20 );
			
			final DoubleElem d3 = sinhTest( asinhd , 20 );
			
			System.out.println( "*** " + ( d.getVal() ) + " *** " + ( d2.getVal() ) );
			Assert.assertTrue( Math.abs( d.getVal() - d2.getVal() ) < 1E-5 );
			
			Assert.assertTrue( Math.abs( d.getVal() - d3.getVal() ) < 1E-5 );
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
				final DoubleElem xd = new DoubleElem( x );
				final DoubleElem yd = new DoubleElem( y );
				final DoubleElem atan = yd.atan2( xd , 25 , 25 );
				compareAngles( stdAtan , atan.getVal() );
			}
		}
	}
	
	
	
	
}



