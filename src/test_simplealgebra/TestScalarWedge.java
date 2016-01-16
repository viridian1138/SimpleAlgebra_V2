



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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.*;


/**
 * Tests the wedge product between a vector and a scalar.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestScalarWedge extends TestCase {
	
	
	
	/**
	 * Tests the wedge product between a vector and a scalar.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testScalarWedgeA( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mvA = se.zero();
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mvB = se.zero();
		
		final HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		
		final double s = rand.nextDouble();
		
		
		final double[] v = { rand.nextDouble() , rand.nextDouble() , rand.nextDouble() };
		
		
		for( int cnt = 0 ; cnt < TestDimensionThree.THREE ; cnt++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cnt ) );
			mvA.setVal( key , new DoubleElem( v[ cnt ] ) );
		}
		
		
		mvB.setVal( keyA , new DoubleElem( s ) );
		
		
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
		
		args.add( mvB );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> 
			prod = mvA.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		
		
		int acnt = 0;
		
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> e : prod.getEntrySet() )
		{
			Assert.assertTrue( e.getKey().size() == 1 );
			final BigInteger ka = e.getKey().iterator().next();
			final double dA = ( v[ ka.intValue() ] ) * s;
			final double dB = e.getValue().getVal();
			Assert.assertTrue( Math.abs( dA - dB ) < 1E-5 );
			acnt++;
		}
		
		Assert.assertTrue( acnt == 3 );
		
		
	}
	
	
	
	
	/**
	 * Tests the wedge product between a vector and a scalar.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testScalarWedgeB( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mvA = se.zero();
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mvB = se.zero();
		
		final HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		
		final double s = rand.nextDouble();
		
		
		final double[] v = { rand.nextDouble() , rand.nextDouble() , rand.nextDouble() };
		
		
		for( int cnt = 0 ; cnt < TestDimensionThree.THREE ; cnt++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cnt ) );
			mvA.setVal( key , new DoubleElem( v[ cnt ] ) );
		}
		
		
		mvB.setVal( keyA , new DoubleElem( s ) );
		
		
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
		
		args.add( mvA );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> 
			prod = mvB.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		
		
		int acnt = 0;
		
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> e : prod.getEntrySet() )
		{
			Assert.assertTrue( e.getKey().size() == 1 );
			final BigInteger ka = e.getKey().iterator().next();
			final double dA = ( v[ ka.intValue() ] ) * s;
			final double dB = e.getValue().getVal();
			Assert.assertTrue( Math.abs( dA - dB ) < 1E-5 );
			acnt++;
		}
		
		Assert.assertTrue( acnt == 3 );
		
		
	}
	
	
	
	
}



