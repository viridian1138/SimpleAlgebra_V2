



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
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.prec.DefaultPrecedenceComparator;


/**
 * Tests matrix logarithms (and related operations) for some simple test cases.
 * Also includes some GA logarithm tests.
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
		
		Assert.assertTrue( ( (DoubleElem)( ( lnexp.add( exp.negate() ) ).totalMagnitude() ) ).getVal() < 1E-4 );
		
	}
	
	
	
	/**
	 * Test method for a simple square-root of a diagonal matrix.
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
		
		Assert.assertTrue( ( (DoubleElem)( ( sqrtSq.add( el.negate() ) ).totalMagnitude() ) ).getVal() < 1E-4 );
		
	}
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#ln(int,int)}.
	 */
	public void testGaLnSimpleScalar( ) throws Throwable {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> el
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td, ord );
		
		el.setVal( new HashSet<BigInteger>() , new DoubleElem( 2.0 ) );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> exp = el.exp( 20 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> ln = exp.ln(20, 20);
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> lnexp = ln.exp(20);
		
		Assert.assertTrue( ( (DoubleElem)( ( lnexp.add( exp.negate() ) ).totalMagnitude() ) ).getVal() < 1E-4 );
		
	}
	
	
	
	
	/**
	 * Test method for a simple square-root of a GA scalar.
	 */
	public void testSimpleGaSquareRoot( ) throws Throwable {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> el
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td, ord );
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> gfac
			= new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td, ord );
		
		el.setVal( new HashSet<BigInteger>() , new DoubleElem( 3.0 ) );
		
		GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> sqrt = el.powL( gfac.identity().divideBy( 2 ) , 20 , 20 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> elv
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td, ord );
		
		elv.setVal( new HashSet<BigInteger>() , new DoubleElem( Math.sqrt( 3.0 ) ) );
		
		Assert.assertTrue( ( (DoubleElem)( ( sqrt.add( elv.negate() ) ).totalMagnitude() ) ).getVal() < 1E-4 );
		
	}
	
	
	
	/**
	 * Test the ability to take the square root of the negative GA scalar.
	 */
	public void testGaSquareRootNegativeIdentity( ) throws Throwable {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> gfac
			= new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td, ord );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> el
			= gfac.identity().negate();
		
		GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> ln 
			= el.ln( 20 , 20 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> sqrt = el.powL( gfac.identity().divideBy( 2 ) , 20 , 20 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> sqrtSq = sqrt.mult( sqrt );
		
		Assert.assertTrue( ( (DoubleElem)( ( sqrtSq.add( el.negate() ) ).totalMagnitude() ) ).getVal() < 1E-4 );
		
	}
	
	
	
	
	/**
	 * Tests the ability to take an approximate natural logarithm of a 2-D vector.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testVectorLnA( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mvA = se.zero();
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mvB = se.zero();
		
		
		
		final double[] v0 = { rand.nextDouble() , rand.nextDouble() };
		
		
		
		for( int cnt = 0 ; cnt < TestDimensionTwo.TWO ; cnt++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cnt ) );
			mvA.setVal( key , new DoubleElem( v0[ cnt ] ) );
		}
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> 
			ln = mvA.ln(20, 20);
		
		
		// System.out.println( ( (DoubleElem)( ln.exp( 20 ).add( mvA.negate() ).totalMagnitude() ) ).getVal() );
		
		
		Assert.assertTrue( ln != null );
		
		
	}
	
	
	
	
}


