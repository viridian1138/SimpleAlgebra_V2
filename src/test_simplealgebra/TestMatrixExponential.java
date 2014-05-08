



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
	public void testMatrixExponentialA( ) throws NotInvertibleException {
		
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
	
	
}

