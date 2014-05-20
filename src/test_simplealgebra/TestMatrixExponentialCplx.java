



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
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
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
 * </math> for complex-number matrices.
 * 
 * 
 * Test cases are adapted from http://people.sc.fsu.edu/~jburkardt/m_src/test_matrix_exponential/test_matrix_exponential.html
 * 
 * 
 * @author thorngreen
 *
 */
public class TestMatrixExponentialCplx extends TestCase {
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential1( ) throws NotInvertibleException {
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> fac =
				new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> el
			= new SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( fac , td );
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 1.0 ) , new DoubleElem( 0.0 ) ) );
		
		el.setVal( BigInteger.ZERO , BigInteger.ONE , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( 0.0 ) ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ZERO , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( 0.0 ) ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 2.0 ) , new DoubleElem( 0.0 ) ) );
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> exp = el.exp( 10 );
		
		Assert.assertEquals( 2.718281828459 , exp.get(BigInteger.ZERO , BigInteger.ZERO).getRe().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ZERO , BigInteger.ZERO).getIm().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ZERO , BigInteger.ONE).getRe().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ZERO , BigInteger.ONE).getIm().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ONE , BigInteger.ZERO).getRe().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ONE , BigInteger.ZERO).getIm().getVal() , 1E-6 );
		
		Assert.assertEquals( 7.3890560989 , exp.get(BigInteger.ONE , BigInteger.ONE).getRe().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ONE , BigInteger.ONE).getIm().getVal() , 1E-6 );
		
	}
	
	

	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential2( ) throws NotInvertibleException {
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> fac =
				new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> el
			= new SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( fac , td );
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( 3.0 ) ) );
		
		el.setVal( BigInteger.ZERO , BigInteger.ONE , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( 0.0 ) ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ZERO , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( 0.0 ) ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( -4.0 ) ) );
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> exp = el.exp( 10 );
		
		Assert.assertEquals( -0.98999249 , exp.get(BigInteger.ZERO , BigInteger.ZERO).getRe().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.14112000 , exp.get(BigInteger.ZERO , BigInteger.ZERO).getIm().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ZERO , BigInteger.ONE).getRe().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ZERO , BigInteger.ONE).getIm().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ONE , BigInteger.ZERO).getRe().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ONE , BigInteger.ZERO).getIm().getVal() , 1E-6 );
		
		Assert.assertEquals( -0.6536436208 , exp.get(BigInteger.ONE , BigInteger.ONE).getRe().getVal() , 1E-6 );
		
		Assert.assertEquals( 0.756802495307 , exp.get(BigInteger.ONE , BigInteger.ONE).getIm().getVal() , 1E-6 );
		
	}
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMatrixExponential3( ) throws NotInvertibleException {
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> fac =
				new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> el
			= new SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( fac , td );
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 5.0 ) , new DoubleElem( 6.0 ) ) );
		
		el.setVal( BigInteger.ZERO , BigInteger.ONE , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( 0.0 ) ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ZERO , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( 0.0 ) ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 7.0 ) , new DoubleElem( -8.0 ) ) );
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> exp = el.exp( 10 );
		
		Assert.assertEquals( 142.501905 , exp.get(BigInteger.ZERO , BigInteger.ZERO).getRe().getVal() , 1E-2 );
		
		Assert.assertEquals( -41.468936 , exp.get(BigInteger.ZERO , BigInteger.ZERO).getIm().getVal() , 1E-2 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ZERO , BigInteger.ONE).getRe().getVal() , 1E-2 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ZERO , BigInteger.ONE).getIm().getVal() , 1E-2 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ONE , BigInteger.ZERO).getRe().getVal() , 1E-2 );
		
		Assert.assertEquals( 0.0 , exp.get(BigInteger.ONE , BigInteger.ZERO).getIm().getVal() , 1E-2 );
		
		Assert.assertEquals( -159.560161 , exp.get(BigInteger.ONE , BigInteger.ONE).getRe().getVal() , 1E-2 );
		
		Assert.assertEquals( -1084.963058 , exp.get(BigInteger.ONE , BigInteger.ONE).getIm().getVal() , 1E-2 );
		
	}
	
	
}

