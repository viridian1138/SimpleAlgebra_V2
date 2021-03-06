



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
import java.util.HashSet;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;


/**
 * Tests the ability to take an approximate natural logarithm of a vector.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestCplxLn extends TestCase {
	
	
	
	
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
	private DoubleElem coshTest( final DoubleElem x , int numIter )
	{
		final DoubleElem ret = ( x.exp( numIter ) ).add( x.negate().exp( numIter ) ).divideBy( 2 );
		return( ret );
	}
	
	
	
	
	
	/**
	 * Tests the ability to take an approximate natural logarithm of a scalar
	 * 
	 * @throws NotInvertibleException
	 */
	public void testScalarLnA( ) throws Throwable
	{
		
		DoubleElem d = new DoubleElem( 10.0 );
		
		final DoubleElem ln = d.ln( 20 , 20 );
		
		Assert.assertTrue( ln != null );
		
		Assert.assertTrue( Math.abs( ln.getVal() - Math.log( 10.0 ) ) < 1E-5 );
		
		
	}
	
	
	
	/**
	 * Tests the ability to take an approximate natural logarithm of a negative value
	 * 
	 * @throws Throwable
	 */
	public void testNegativeLnA() throws Throwable
	{
		final ComplexElem<DoubleElem,DoubleElemFactory> cplx = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -10.0 ) , new DoubleElem( 0.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> ln = cplx.ln(20, 20);
		
		Assert.assertTrue( Math.abs( ln.getRe().getVal() - Math.log( 10.0 ) ) < 1E-5 );
		
		Assert.assertTrue( Math.abs( ln.getIm().getVal() - Math.PI ) < 1E-5 );
		
	}
	
	
	/**
	 * Tests a simple use of a power function.  Tests <math display="inline">
     * <mrow>
     *  <msup>
     *          <mn>2</mn>
     *        <mn>3</mn>
     *  </msup>
     *  <mo>=</mo>
     *  <mn>8</mn>
     * </mrow>
     * </math>
	 * 
	 * @throws Throwable
	 */
	public void testSimplePowerFunction() throws Throwable 
	{
		final DoubleElem a = new DoubleElem( 2.0 );
		
		final DoubleElem b = new DoubleElem( 3.0 );
		
		final DoubleElem pow = a.powR(b, 20, 20);
		
		Assert.assertTrue( Math.abs( pow.getVal() - 8.0 ) < 1E-5 );
		
	}
	
	
	
	/**
	 * Tests that using the power function to take the square root of -1 produces the imaginary number.
	 * 
	 * @throws Throwable
	 */
	public void testPowNegative() throws Throwable
	{
		final ComplexElem<DoubleElem,DoubleElemFactory> a = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -1.0 ) , new DoubleElem( 0.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> b = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.5 ) , new DoubleElem( 0.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> pow = a.powR(b, 20, 20);
		
		Assert.assertTrue( Math.abs( pow.getRe().getVal() - 0.0 ) < 1E-5 );
		
		Assert.assertTrue( Math.abs( pow.getIm().getVal() - 1.0 ) < 1E-5 );
		
	}
	
	
	
	/**
	 * Tests the arcsin of a positive value.
	 * 
	 * @throws Throwable
	 */
	public void testAsinPositive() throws Throwable
	{
		final DoubleElem d = new DoubleElem( 0.7 );
		
		final DoubleElem asind = d.asin( 20 , 20 );
		
		final DoubleElem d2 = asind.sin( 20 );
		
		final double d3 = Math.sin( asind.getVal() );
		
		Assert.assertTrue( Math.abs( d.getVal() - d2.getVal() ) < 1E-5 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d3 ) < 1E-5 );
		
	}
	
	
	

	/**
	 * Tests the arcsin of a negative value.
	 * 
	 * @throws Throwable
	 */
	public void testAsinNegative() throws Throwable
	{
		final DoubleElem d = new DoubleElem( -0.7 );
		
		final DoubleElem asind = d.asin( 20 , 20 );
		
		final DoubleElem d2 = asind.sin( 20 );
		
		final double d3 = Math.sin( asind.getVal() );
		
		Assert.assertTrue( Math.abs( d.getVal() - d2.getVal() ) < 1E-5 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d3 ) < 1E-5 );
		
	}
	
	
	

	
	/**
	 * Tests the arccosine of a positive value.
	 * 
	 * @throws Throwable
	 */
	public void testAcosPositive() throws Throwable
	{
		final DoubleElem d = new DoubleElem( 0.7 );
		
		final DoubleElem acosd = d.acos( 20 , 20 );
		
		final DoubleElem d2 = acosd.cos( 20 );
		
		final double d3 = Math.cos( acosd.getVal() );
		
		Assert.assertTrue( Math.abs( d.getVal() - d2.getVal() ) < 1E-5 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d3 ) < 1E-5 );
		
	}
	
	
	

	/**
	 * Tests the arccosine of a negative value.
	 * 
	 * @throws Throwable
	 */
	public void testAcosNegative() throws Throwable
	{
		final DoubleElem d = new DoubleElem( -0.7 );
		
		final DoubleElem acosd = d.acos( 20 , 20 );
		
		final DoubleElem d2 = acosd.cos( 20 );
		
		final double d3 = Math.cos( acosd.getVal() );
		
		Assert.assertTrue( Math.abs( d.getVal() - d2.getVal() ) < 1E-5 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d3 ) < 1E-5 );
		
	}
	
	
	
	
	
	/**
	 * Tests the hyperbolic sine of a positive value.
	 * 
	 * @throws Throwable
	 */
	public void testSinhPositive() throws Throwable
	{
		final DoubleElem d = new DoubleElem( 5.0 );
		
		final DoubleElem sinh = d.sinh( 20 );
		
		final DoubleElem sinh2 = sinhTest( d , 20 );
		
		Assert.assertTrue( Math.abs( sinh.getVal() - sinh2.getVal() ) < 1E-5 );
		
	}
	
	
	
	
	/**
	 * Tests the hyperbolic sine of a negative value.
	 * 
	 * @throws Throwable
	 */
	public void testSinhNegative() throws Throwable
	{
		final DoubleElem d = new DoubleElem( -5.0 );
		
		final DoubleElem sinh = d.sinh( 20 );
		
		final DoubleElem sinh2 = sinhTest( d , 20 );
		
		Assert.assertTrue( Math.abs( sinh.getVal() - sinh2.getVal() ) < 1E-5 );
		
	}
	
	
	
	
	
	/**
	 * Tests the hyperbolic cosine of a positive value.
	 * 
	 * @throws Throwable
	 */
	public void testCoshPositive() throws Throwable
	{
		final DoubleElem d = new DoubleElem( 5.0 );
		
		final DoubleElem cosh = d.cosh( 20 );
		
		final DoubleElem cosh2 = coshTest( d , 20 );
		
		Assert.assertTrue( Math.abs( cosh.getVal() - cosh2.getVal() ) < 1E-5 );
		
	}
	
	
	
	/**
	 * Tests the hyperbolic cosine of a negative value.
	 * 
	 * @throws Throwable
	 */
	public void testCoshNegative() throws Throwable
	{
		final DoubleElem d = new DoubleElem( -5.0 );
		
		final DoubleElem cosh = d.cosh( 20 );
		
		final DoubleElem cosh2 = coshTest( d , 20 );
		
		Assert.assertTrue( Math.abs( cosh.getVal() - cosh2.getVal() ) < 1E-5 );
		
	}
	
	
	
	
	/**
	 * Tests the arcsinh of a positive value.
	 * 
	 * @throws Throwable
	 */
	public void testAsinhPositive() throws Throwable
	{
		final DoubleElem d = new DoubleElem( 5.0 );
		
		final DoubleElem asinhd = d.asinh( 20 , 20 );
		
		final DoubleElem d2 = asinhd.sinh( 20 );
		
		final DoubleElem d3 = sinhTest( asinhd , 20 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d2.getVal() ) < 1E-5 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d3.getVal() ) < 1E-5 );
		
	}
	
	
	
	
	/**
	 * Tests the arcsinh of a negative value.
	 * 
	 * @throws Throwable
	 */
	public void testAsinhNegative() throws Throwable
	{
		final DoubleElem d = new DoubleElem( -5.0 );
		
		final DoubleElem asinhd = d.asinh( 20 , 20 );
		
		final DoubleElem d2 = asinhd.sinh( 20 );
		
		final DoubleElem d3 = sinhTest( asinhd , 20 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d2.getVal() ) < 1E-5 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d3.getVal() ) < 1E-5 );
		
	}
	
	
	
	
	/**
	 * Tests the arccosh of a positive value.
	 * 
	 * @throws Throwable
	 */
	public void testAcoshPositiveA() throws Throwable
	{
		final DoubleElem d = new DoubleElem( 5.0 );
		
		final DoubleElem acoshd = d.acosh( 20 , 20 );
		
		final DoubleElem d2 = acoshd.cosh( 20 );
		
		final DoubleElem d3 = coshTest( acoshd , 20 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d2.getVal() ) < 1E-5 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d3.getVal() ) < 1E-5 );
		
	}
	
	
	
	/**
	 * Tests the arccosh of a positive value.
	 * 
	 * @throws Throwable
	 */
	public void testAcoshPositiveB() throws Throwable
	{
		final DoubleElem d = new DoubleElem( 3.0 );
		
		final DoubleElem acoshd = d.acosh( 20 , 20 );
		
		final DoubleElem d2 = acoshd.cosh( 20 );
		
		final DoubleElem d3 = coshTest( acoshd , 20 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d2.getVal() ) < 1E-5 );
		
		Assert.assertTrue( Math.abs( d.getVal() - d3.getVal() ) < 1E-5 );
		
	}
	
	
	
	/**
	 * Verifies that nop 1 with negate=true is similar to division.
	 * 
	 * @throws Throwable
	 */
	public void nopOneLikeDivision() throws Throwable
	{
		final DoubleElem d0 = new DoubleElem( 3.0 );
		
		final DoubleElem d1 = new DoubleElem( 5.0 );
		
		final DoubleElem d2 = d0.nop(d1, 1, true, 20, 20);
		
		Assert.assertTrue( Math.abs( d2.getVal() - ( 3.0 / 5.0) ) < 1E-5 );
		
	}
	
	
	
	/**
	 * Verifies that nop 1 with negate=false is similar to multiplication.
	 * 
	 * @throws Throwable
	 */
	public void nopOneLikeMultiplication() throws Throwable
	{
		final DoubleElem d0 = new DoubleElem( 3.0 );
		
		final DoubleElem d1 = new DoubleElem( 5.0 );
		
		final DoubleElem d2 = d0.nop(d1, 1, false, 20, 20);
		
		Assert.assertTrue( Math.abs( d2.getVal() - ( 3.0 * 5.0) ) < 1E-5 );
		
	}
	
	
	
	/**
	 * Verifies that nop 0 with negate=true is similar to subtraction.
	 * 
	 * @throws Throwable
	 */
	public void nopOneLikeSubtraction() throws Throwable
	{
		final DoubleElem d0 = new DoubleElem( 3.0 );
		
		final DoubleElem d1 = new DoubleElem( 5.0 );
		
		final DoubleElem d2 = d0.nop(d1, 0, true, 20, 20);
		
		Assert.assertTrue( Math.abs( d2.getVal() - ( 3.0 - 5.0) ) < 1E-5 );
		
	}
	
	
	
	/**
	 * Verifies that nop 0 with negate=false is similar to addition.
	 * 
	 * @throws Throwable
	 */
	public void nopOneLikeAddition() throws Throwable
	{
		final DoubleElem d0 = new DoubleElem( 3.0 );
		
		final DoubleElem d1 = new DoubleElem( 5.0 );
		
		final DoubleElem d2 = d0.nop(d1, 0, false, 20, 20);
		
		Assert.assertTrue( Math.abs( d2.getVal() - ( 3.0 + 5.0) ) < 1E-5 );
		
	}
	
	
	
	
	
	
	
}



