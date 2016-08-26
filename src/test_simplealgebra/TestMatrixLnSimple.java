



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
import simplealgebra.SquareMatrixElemFactory;


/**
 * Tests matrix logarithms (and related operations) for some simple test cases.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestMatrixLnSimple extends TestCase {
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#ln(int,int)}.
	 */
	public void testMatrixLnSimpleDiagonalMat( ) throws Throwable {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>( fac , td );
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new DoubleElem( 1.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new DoubleElem( 2.0 ) );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> exp = el.exp( 20 );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> ln = exp.ln(20, 20);
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> lnexp = ln.exp(20);
		
		Assert.assertEquals( lnexp.getVal(BigInteger.ZERO , BigInteger.ZERO).getVal() , exp.getVal(BigInteger.ZERO , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( lnexp.getVal(BigInteger.ZERO , BigInteger.ONE).getVal() , exp.getVal(BigInteger.ZERO , BigInteger.ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( lnexp.getVal(BigInteger.ONE , BigInteger.ZERO).getVal() , exp.getVal(BigInteger.ONE , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( lnexp.getVal(BigInteger.ONE , BigInteger.ONE).getVal() , exp.getVal(BigInteger.ONE , BigInteger.ONE).getVal() , 1E-4 );
		
	}
	
	
	
	/**
	 * Test method for a simple square-root of the identity matrix.
	 */
	public void testSimpleMatrixSquareRoot( ) throws Throwable {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>( fac , td );
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> mfac
			= new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>( fac , td );
		
		el.setVal( BigInteger.ZERO , BigInteger.ZERO , new DoubleElem( 3.0 ) );
		
		el.setVal( BigInteger.ONE , BigInteger.ONE , new DoubleElem( 5.0 ) );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> sqrt = el.powL( mfac.identity().divideBy( 2 ) , 20 , 20 );
		
		Assert.assertEquals( sqrt.getVal(BigInteger.ZERO , BigInteger.ZERO).getVal() , Math.sqrt( 3.0 ) , 1E-4 );
		
		Assert.assertEquals( sqrt.getVal(BigInteger.ZERO , BigInteger.ONE).getVal() , 0.0 , 1E-4 );
		
		Assert.assertEquals( sqrt.getVal(BigInteger.ONE , BigInteger.ZERO).getVal() , 0.0 , 1E-4 );
		
		Assert.assertEquals( sqrt.getVal(BigInteger.ONE , BigInteger.ONE).getVal() , Math.sqrt( 5.0 ) , 1E-4 );
		
	}
	
	

	
	
	
	/**
	 * Test the ability to take the square root of the negative identity matrix.
	 */
	public void testMatrixSquareRootNegativeIdentity( ) throws Throwable {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> mfac
			= new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>( fac , td );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> el
			= mfac.identity().negate();
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> sqrt = el.powL( mfac.identity().divideBy( 2 ) , 20 , 20 );
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> sqrtSq = sqrt.mult( sqrt );
		
		Assert.assertEquals( sqrtSq.getVal(BigInteger.ZERO , BigInteger.ZERO).getVal() , el.getVal(BigInteger.ZERO , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( sqrtSq.getVal(BigInteger.ZERO , BigInteger.ONE).getVal() , el.getVal(BigInteger.ZERO , BigInteger.ONE).getVal() , 1E-4 );
		
		Assert.assertEquals( sqrtSq.getVal(BigInteger.ONE , BigInteger.ZERO).getVal() , el.getVal(BigInteger.ONE , BigInteger.ZERO).getVal() , 1E-4 );
		
		Assert.assertEquals( sqrtSq.getVal(BigInteger.ONE , BigInteger.ONE).getVal() , el.getVal(BigInteger.ONE , BigInteger.ONE).getVal() , 1E-4 );
		
	}
	
	
	
	
	
	
}


