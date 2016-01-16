



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
 * Tests the dot product between two vectors.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestVectorDot extends TestCase {
	
	
	
	/**
	 * Tests the dot product between two vectors.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testVectorDotA( ) throws Throwable
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
		
		final double[] v1 = { rand.nextDouble() , rand.nextDouble() , rand.nextDouble() };
		
		final double vc =
				v0[ 0 ] * v1[ 0 ] + v0[ 1 ] * v1[ 1 ] + v0[ 2 ] * v1[ 2 ];
		
		
		for( int cnt = 0 ; cnt < TestDimensionThree.THREE ; cnt++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cnt ) );
			mvA.setVal( key , new DoubleElem( v0[ cnt ] ) );
			mvB.setVal( key , new DoubleElem( v1[ cnt ] ) );
		}
		
		
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
		
		args.add( mvB );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> 
			prod = mvA.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , args );
		
		
		int acnt = 0;
		
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> e : prod.getEntrySet() )
		{
			Assert.assertTrue( e.getKey().size() == 0 );
			final double dB = e.getValue().getVal();
			Assert.assertTrue( Math.abs( vc - dB ) < 1E-5 );
			acnt++;
		}
		
		Assert.assertTrue( acnt == 1 );
		
		
	}
	
	
	
	
	
	/**
	 * Tests the dot product between two vectors.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testVectorDotHestenesA( ) throws Throwable
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
		
		final double[] v1 = { rand.nextDouble() , rand.nextDouble() , rand.nextDouble() };
		
		final double vc =
				v0[ 0 ] * v1[ 0 ] + v0[ 1 ] * v1[ 1 ] + v0[ 2 ] * v1[ 2 ];
		
		
		for( int cnt = 0 ; cnt < TestDimensionThree.THREE ; cnt++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cnt ) );
			mvA.setVal( key , new DoubleElem( v0[ cnt ] ) );
			mvB.setVal( key , new DoubleElem( v1[ cnt ] ) );
		}
		
		
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
		
		args.add( mvB );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> 
			prod = mvA.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT_HESTENES , args );
		
		
		int acnt = 0;
		
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> e : prod.getEntrySet() )
		{
			Assert.assertTrue( e.getKey().size() == 0 );
			final double dB = e.getValue().getVal();
			Assert.assertTrue( Math.abs( vc - dB ) < 1E-5 );
			acnt++;
		}
		
		Assert.assertTrue( acnt == 1 );
		
		
	}
	
		
	
	
	
	
	
}



