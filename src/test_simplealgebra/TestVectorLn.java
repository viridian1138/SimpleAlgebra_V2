



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
 * Tests the ability to take an approximate natural logarithm of a vector.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestVectorLn extends TestCase {
	
	
	
	/**
	 * Tests the ability to take an approximate natural logarithm of a vector.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testVectorLnA( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mvA = se.zero();
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mvB = se.zero();
		
		
		
		final double[] v0 = { rand.nextDouble() , rand.nextDouble() , rand.nextDouble() };
		
		
		
		for( int cnt = 0 ; cnt < TestDimensionThree.THREE ; cnt++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cnt ) );
			mvA.setVal( key , new DoubleElem( v0[ cnt ] ) );
		}
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> 
			ln = mvA.ln(20, 20);
		
		
		// System.out.println( ( (DoubleElem)( ln.exp( 20 ).add( mvA.negate() ).totalMagnitude() ) ).getVal() );
		
		
		Assert.assertTrue( ln != null );
		
		
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
		
		// System.out.println( d.exp( 20 ).getVal() );
		
		// System.out.println( ln.getVal() );
		
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
	
	
	
	
	
}



