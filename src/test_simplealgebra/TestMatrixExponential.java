



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

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;

/**
 * Tests for the matrix exponential <math display="inline">
 * <mrow>
 *  <msup>
 *          <mo>e</mo>
 *        <mi>x</mi>
 *  </msup>
 * </mrow>
 * </math> for real-number matrices.
 * 
 * 
 * Test cases are adapted from http://people.sc.fsu.edu/~jburkardt/m_src/test_matrix_exponential/test_matrix_exponential.html
 * 
 * 
 * @author thorngreen
 *
 */
public class TestMatrixExponential extends TestCase {
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential1( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>( fac , td );
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new DoubleElem( 1.0 ) );
		
		el.setVal( BigInteger.ZERO , BigInteger.ONE , new DoubleElem( 0.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ZERO , new DoubleElem( 0.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new DoubleElem( 2.0 ) );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		Assert.assertEquals( 2.718281828 , exp.get(BigInteger.ZERO , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ZERO , BigInteger.ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ONE , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 7.3890560989 , exp.get(BigInteger.ONE , BigInteger.ONE).getVal() , 1E-4 );
		
	}

	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential2( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>( fac , td );
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new DoubleElem( 1.0 ) );
		
		el.setVal( BigInteger.ZERO , BigInteger.ONE , new DoubleElem( 3.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ZERO , new DoubleElem( 3.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new DoubleElem( 2.0 ) );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		Assert.assertEquals( 39.322809708 , exp.get(BigInteger.ZERO , BigInteger.ZERO).getVal() , 1E-2 );
		
		Assert.assertEquals( 46.166301438 , exp.get(BigInteger.ZERO , BigInteger.ONE).getVal() , 1E-2 );
		
		Assert.assertEquals( 46.166301438 , exp.get(BigInteger.ONE , BigInteger.ZERO).getVal() , 1E-2 );
		
		Assert.assertEquals( 54.711576854 , exp.get(BigInteger.ONE , BigInteger.ONE).getVal() , 1E-2 );
		
	}
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential3( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>( fac , td );
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new DoubleElem( 0.0 ) );
		
		el.setVal( BigInteger.ZERO , BigInteger.ONE , new DoubleElem( 1.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ZERO , new DoubleElem( -39.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new DoubleElem( -40.0 ) );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		Assert.assertEquals( 0.37756048 , exp.get(BigInteger.ZERO , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.00968104 , exp.get(BigInteger.ZERO , BigInteger.ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( -0.37756048 , exp.get(BigInteger.ONE , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( -0.00968104 , exp.get(BigInteger.ONE , BigInteger.ONE).getVal() , 1E-4 );
		
	}
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential4( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>( fac , td );
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new DoubleElem( -49.0 ) );
		
		el.setVal( BigInteger.ZERO , BigInteger.ONE , new DoubleElem( 24.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ZERO , new DoubleElem( -64.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new DoubleElem( 31.0 ) );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		Assert.assertEquals( -0.735758758 , exp.get(BigInteger.ZERO , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.55181909965 , exp.get(BigInteger.ZERO , BigInteger.ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( -1.471517599088 , exp.get(BigInteger.ONE , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 1.1036382407 , exp.get(BigInteger.ONE , BigInteger.ONE).getVal() , 1E-4 );
		
	}
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential5( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>( fac , td );
		
		final BigInteger ZERO = BigInteger.ZERO;
		
		final BigInteger ONE = BigInteger.ONE;
		
		final BigInteger TWO = BigInteger.valueOf( 2 );
		
		final BigInteger THREE = BigInteger.valueOf( 3 );
		
		
		el.setVal( ZERO , ONE , new DoubleElem( 6.0 ) );
		
		el.setVal( ONE , TWO , new DoubleElem( 6.0 ) );
		
		el.setVal( TWO , THREE , new DoubleElem( 6.0 ) );
		
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		
		
		Assert.assertEquals( 1.0 , exp.getVal(ZERO , ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 6.0 , exp.getVal(ZERO , ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( 18.0 , exp.getVal(ZERO , TWO).getVal() , 1E-4 );
		
		Assert.assertEquals( 36.0 , exp.getVal(ZERO , THREE).getVal() , 1E-4 );
		
		
		
		
		
		Assert.assertEquals( 0.0 , exp.getVal(ONE , ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 1.0 , exp.getVal(ONE , ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( 6.0 , exp.getVal(ONE , TWO).getVal() , 1E-4 );
		
		Assert.assertEquals( 18.0 , exp.getVal(ONE , THREE).getVal() , 1E-4 );
		
		
		
		
		
		Assert.assertEquals( 0.0 , exp.getVal(TWO , ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.0 , exp.getVal(TWO , ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( 1.0 , exp.getVal(TWO , TWO).getVal() , 1E-4 );
		
		Assert.assertEquals( 6.0 , exp.getVal(TWO , THREE).getVal() , 1E-4 );
		
		
		
		
		
		
		Assert.assertEquals( 0.0 , exp.getVal(THREE , ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.0 , exp.getVal(THREE , ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.0 , exp.getVal(THREE , TWO).getVal() , 1E-4 );
		
		Assert.assertEquals( 1.0 , exp.getVal(THREE , THREE).getVal() , 1E-4 );	
		
		
	}
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential6( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>( fac , td );
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new DoubleElem( 1.0 ) );
		
		el.setVal( BigInteger.ZERO , BigInteger.ONE , new DoubleElem( 1.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ZERO , new DoubleElem( 0.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new DoubleElem( 1.0 ) );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		Assert.assertEquals( 2.718281828 , exp.getVal(BigInteger.ZERO , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 2.718281828 , exp.getVal(BigInteger.ZERO , BigInteger.ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.0 , exp.getVal(BigInteger.ONE , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 2.718281828 , exp.getVal(BigInteger.ONE , BigInteger.ONE).getVal() , 1E-4 );
		
	}
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential7( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>( fac , td );
		
		final double EPS = 1E-8;
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new DoubleElem( 1.0 + EPS ) );
		
		el.setVal( BigInteger.ZERO , BigInteger.ONE , new DoubleElem( 1.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ZERO , new DoubleElem( 0.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new DoubleElem( 1.0 - EPS ) );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		Assert.assertEquals( 2.718281828 , exp.getVal(BigInteger.ZERO , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 2.718281828 , exp.getVal(BigInteger.ZERO , BigInteger.ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.0 , exp.getVal(BigInteger.ONE , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 2.718281828 , exp.getVal(BigInteger.ONE , BigInteger.ONE).getVal() , 1E-4 );
		
	}
	
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential8( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory>( fac , td );
		
		final BigInteger ZERO = BigInteger.ZERO;
		
		final BigInteger ONE = BigInteger.ONE;
		
		final BigInteger TWO = BigInteger.valueOf( 2 );
		
		
		
		el.setVal( ZERO , ZERO , new DoubleElem( 21.0 ) );
		
		el.setVal( ZERO , ONE , new DoubleElem( 17.0 ) );
		
		el.setVal( ZERO , TWO , new DoubleElem( 6.0 ) );
		
		
		
		el.setVal( ONE , ZERO , new DoubleElem( -5.0 ) );
		
		el.setVal( ONE , ONE , new DoubleElem( -1.0 ) );
		
		el.setVal( ONE , TWO , new DoubleElem( -6.0 ) );
		
		
		
		el.setVal( TWO , ZERO , new DoubleElem( 4.0 ) );
		
		el.setVal( TWO , ONE , new DoubleElem( 4.0 ) );
		
		el.setVal( TWO , TWO , new DoubleElem( 16.0 ) );
		
		
		
		
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		
		
		final double EXP16 = Math.exp( 16.0 );
		final double EXP4 = Math.exp( 4.0 );
		
		
		
		Assert.assertEquals( 0.25 * ( 13.0 * EXP16 - EXP4 ) , exp.getVal(ZERO , ZERO).getVal() , 1E+3 );
		
		Assert.assertEquals( 0.25 * ( 13.0 * EXP16 - 5.0 * EXP4 ) , exp.getVal(ZERO , ONE).getVal() , 1E+3 );
		
		Assert.assertEquals( 0.25 * ( 2.0 * EXP16 - 2.0 * EXP4 ) , exp.getVal(ZERO , TWO).getVal() , 1E+3 );
		
		
		
		
		
		Assert.assertEquals( 0.25 * ( -9.0 * EXP16 - EXP4 ) , exp.getVal(ONE , ZERO).getVal() , 1E+3 );
		
		Assert.assertEquals( 0.25 * ( -9.0 * EXP16 + 5.0 * EXP4 ) , exp.getVal(ONE , ONE).getVal() , 1E+3 );
		
		Assert.assertEquals( 0.25 * ( -2.0 * EXP16 + 2.0 * EXP4 ) , exp.getVal(ONE , TWO).getVal() , 1E+3 );
		
		
		
		
		
		Assert.assertEquals( 0.25 * ( 16.0 * EXP16 ) , exp.getVal(TWO , ZERO).getVal() , 1E+3 );
		
		Assert.assertEquals( 0.25 * ( 16.0 * EXP16 ) , exp.getVal(TWO , ONE).getVal() , 1E+3 );
		
		Assert.assertEquals( 0.25 * ( 4.0 * EXP16 ) , exp.getVal(TWO , TWO).getVal() , 1E+3 );
		
		
		
		
	}
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential9( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>( fac , td );
		
		final BigInteger ZERO = BigInteger.ZERO;
		
		final BigInteger ONE = BigInteger.ONE;
		
		final BigInteger TWO = BigInteger.valueOf( 2 );
		
		final BigInteger THREE = BigInteger.valueOf( 3 );
		
		
		
		
		el.setVal( ZERO , ZERO , new DoubleElem( 1.0 ) );
		
		el.setVal( ZERO , ONE , new DoubleElem( 2.0 ) );
		
		el.setVal( ZERO , TWO , new DoubleElem( 2.0 ) );
		
		el.setVal( ZERO , THREE , new DoubleElem( 2.0 ) );
		
		
		
		
		el.setVal( ONE , ZERO , new DoubleElem( 3.0 ) );
		
		el.setVal( ONE , ONE , new DoubleElem( 1.0 ) );
		
		el.setVal( ONE , TWO , new DoubleElem( 1.0 ) );
		
		el.setVal( ONE , THREE , new DoubleElem( 2.0 ) );
		
		
		
		el.setVal( TWO , ZERO , new DoubleElem( 3.0 ) );
		
		el.setVal( TWO , ONE , new DoubleElem( 2.0 ) );
		
		el.setVal( TWO , TWO , new DoubleElem( 1.0 ) );
		
		el.setVal( TWO , THREE , new DoubleElem( 2.0 ) );
		
		
		
		el.setVal( THREE , ZERO , new DoubleElem( 3.0 ) );
		
		el.setVal( THREE , ONE , new DoubleElem( 3.0 ) );
		
		el.setVal( THREE , TWO , new DoubleElem( 3.0 ) );
		
		el.setVal( THREE , THREE , new DoubleElem( 1.0 ) );
		
		
		
		
		
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		
		
		
		
		Assert.assertEquals( 740.7038 , exp.getVal(ZERO , ZERO).getVal() , 1E-2 );
		
		Assert.assertEquals( 610.8500 , exp.getVal(ZERO , ONE).getVal() , 1E-2 );
		
		Assert.assertEquals( 542.2743 , exp.getVal(ZERO , TWO).getVal() , 1E-2 );
		
		Assert.assertEquals( 549.1753 , exp.getVal(ZERO , THREE).getVal() , 1E-2 );
		
		
		
		
		
		Assert.assertEquals( 731.2510 , exp.getVal(ONE , ZERO).getVal() , 1E-2 );
		
		Assert.assertEquals( 603.5524 , exp.getVal(ONE , ONE).getVal() , 1E-2 );
		
		Assert.assertEquals( 535.0884 , exp.getVal(ONE , TWO).getVal() , 1E-2 );
		
		Assert.assertEquals( 542.2743 , exp.getVal(ONE , THREE).getVal() , 1E-2 );
		
		
		
		
		
		Assert.assertEquals( 823.7630 , exp.getVal(TWO , ZERO).getVal() , 1E-2 );
		
		Assert.assertEquals( 679.4257 , exp.getVal(TWO , ONE).getVal() , 1E-2 );
		
		Assert.assertEquals( 603.5524 , exp.getVal(TWO , TWO).getVal() , 1E-2 );
		
		Assert.assertEquals( 610.8500 , exp.getVal(TWO , THREE).getVal() , 1E-2 );
		
		
		
		
		
		
		Assert.assertEquals( 998.4355 , exp.getVal(THREE , ZERO).getVal() , 1E-2 );
		
		Assert.assertEquals( 823.7630 , exp.getVal(THREE , ONE).getVal() , 1E-2 );
		
		Assert.assertEquals( 731.2510 , exp.getVal(THREE , TWO).getVal() , 1E-2 );
		
		Assert.assertEquals( 740.7038 , exp.getVal(THREE , THREE).getVal() , 1E-2 );	
		
		
	}
	
	
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential10( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory>( fac , td );
		
		final BigInteger ZERO = BigInteger.ZERO;
		
		final BigInteger ONE = BigInteger.ONE;
		
		final BigInteger TWO = BigInteger.valueOf( 2 );
		
		
		
		el.setVal( ZERO , ZERO , new DoubleElem( 4.0 ) );
		
		el.setVal( ZERO , ONE , new DoubleElem( 2.0 ) );
		
		el.setVal( ZERO , TWO , new DoubleElem( 0.0 ) );
		
		
		
		el.setVal( ONE , ZERO , new DoubleElem( 1.0 ) );
		
		el.setVal( ONE , ONE , new DoubleElem( 4.0 ) );
		
		el.setVal( ONE , TWO , new DoubleElem( 1.0 ) );
		
		
		
		el.setVal( TWO , ZERO , new DoubleElem( 1.0 ) );
		
		el.setVal( TWO , ONE , new DoubleElem( 1.0 ) );
		
		el.setVal( TWO , TWO , new DoubleElem( 4.0 ) );
		
		
		
		
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		
		
		
		
		Assert.assertEquals( 147.866622 , exp.getVal(ZERO , ZERO).getVal() , 1E-2 );
		
		Assert.assertEquals( 183.765138 , exp.getVal(ZERO , ONE).getVal() , 1E-2 );
		
		Assert.assertEquals( 71.797032 , exp.getVal(ZERO , TWO).getVal() , 1E-2 );
		
		
		
		
		
		Assert.assertEquals( 127.7810855 , exp.getVal(ONE , ZERO).getVal() , 1E-2 );
		
		Assert.assertEquals( 183.765138 , exp.getVal(ONE , ONE).getVal() , 1E-2 );
		
		Assert.assertEquals( 91.8825693 , exp.getVal(ONE , TWO).getVal() , 1E-2 );
		
		
		
		
		
		Assert.assertEquals( 127.7810855 , exp.getVal(TWO , ZERO).getVal() , 1E-2 );
		
		Assert.assertEquals( 163.679601 , exp.getVal(TWO , ONE).getVal() , 1E-2 );
		
		Assert.assertEquals( 111.968106 , exp.getVal(TWO , TWO).getVal() , 1E-2 );
		
		
		
		
	}
	
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential11( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory>( fac , td );
		
		final BigInteger ZERO = BigInteger.ZERO;
		
		final BigInteger ONE = BigInteger.ONE;
		
		final BigInteger TWO = BigInteger.valueOf( 2 );
		
		
		
		el.setVal( ZERO , ZERO , new DoubleElem( 29.879421 ) );
		
		el.setVal( ZERO , ONE , new DoubleElem( 0.781575 ) );
		
		el.setVal( ZERO , TWO , new DoubleElem( -2.289519 ) );
		
		
		
		el.setVal( ONE , ZERO , new DoubleElem( 0.781575 ) );
		
		el.setVal( ONE , ONE , new DoubleElem( 25.726569 ) );
		
		el.setVal( ONE , TWO , new DoubleElem( 8.68073782 ) );
		
		
		
		el.setVal( TWO , ZERO , new DoubleElem( -2.289519 ) );
		
		el.setVal( TWO , ONE , new DoubleElem( 8.68073782 ) );
		
		el.setVal( TWO , TWO , new DoubleElem( 34.3940092 ) );
		
		
		
		
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		
		
		
		
		Assert.assertEquals( 5.496313E+15 , exp.getVal(ZERO , ZERO).getVal() , 1E+14 );
		
		Assert.assertEquals( -1.823188E+16 , exp.getVal(ZERO , ONE).getVal() , 1E+14 );
		
		Assert.assertEquals( -3.047577E+16 , exp.getVal(ZERO , TWO).getVal() , 1E+14 );
		
		
		
		
		
		Assert.assertEquals( -1.823188E+16 , exp.getVal(ONE , ZERO).getVal() , 1E+14 );
		
		Assert.assertEquals( 6.0605228E+16 , exp.getVal(ONE , ONE).getVal() , 1E+14 );
		
		Assert.assertEquals( 1.0129184E+17 , exp.getVal(ONE , TWO).getVal() , 1E+14 );
		
		
		
		
		
		Assert.assertEquals( -3.047577E+16 , exp.getVal(TWO , ZERO).getVal() , 1E+14 );
		
		Assert.assertEquals( 1.0129184E+17 , exp.getVal(TWO , ONE).getVal() , 1E+14 );
		
		Assert.assertEquals( 1.692294411E+17 , exp.getVal(TWO , TWO).getVal() , 1E+14 );
		
		
		
		
	}
	
	
	
	
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential12( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory>( fac , td );
		
		final BigInteger ZERO = BigInteger.ZERO;
		
		final BigInteger ONE = BigInteger.ONE;
		
		final BigInteger TWO = BigInteger.valueOf( 2 );
		
		
		
		el.setVal( ZERO , ZERO , new DoubleElem( -131.0 ) );
		
		el.setVal( ZERO , ONE , new DoubleElem( 19.0 ) );
		
		el.setVal( ZERO , TWO , new DoubleElem( 18.0 ) );
		
		
		
		el.setVal( ONE , ZERO , new DoubleElem( -390.0 ) );
		
		el.setVal( ONE , ONE , new DoubleElem( 56.0 ) );
		
		el.setVal( ONE , TWO , new DoubleElem( 54.0 ) );
		
		
		
		el.setVal( TWO , ZERO , new DoubleElem( -387.0 ) );
		
		el.setVal( TWO , ONE , new DoubleElem( 57.0 ) );
		
		el.setVal( TWO , TWO , new DoubleElem( 52.0 ) );
		
		
		
		
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		
		
		
		
		Assert.assertEquals( -1.509644 , exp.getVal(ZERO , ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.367879 , exp.getVal(ZERO , ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.135335 , exp.getVal(ZERO , TWO).getVal() , 1E-4 );
		
		
		
		
		
		Assert.assertEquals( -5.632570 , exp.getVal(ONE , ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 1.471517 , exp.getVal(ONE , ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.406005 , exp.getVal(ONE , TWO).getVal() , 1E-4 );
		
		
		
		
		
		Assert.assertEquals( -4.934938 , exp.getVal(TWO , ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( 1.103638 , exp.getVal(TWO , ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( 0.541341 , exp.getVal(TWO , TWO).getVal() , 1E-4 );
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
}


