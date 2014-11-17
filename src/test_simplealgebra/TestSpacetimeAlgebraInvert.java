



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
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.SpacetimeAlgebraOrd;



/**
 * @author thorngreen
 *
 */
public class TestSpacetimeAlgebraInvert extends TestCase {
	
	
	
	private void validateIsUnit( GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdent )
	{
		final int max = 1 << 5;
		
		int i;
		int j;
		for( i = 0 ; i < max ; i++ )
		{
			HashSet<BigInteger> key = new HashSet<BigInteger>();
			for( j = 0 ; j < 5 ; j++ )
			{
				final int bit = 1 << j;
				final boolean bitOn = ( i & bit ) != 0;
				if( bitOn )
				{
					key.add( BigInteger.valueOf( j ) );
				}
			}
			
			final double matchVal = ( key.size() == 0 ) ? 1.0 : 0.0;
			
//			try {
			Assert.assertEquals( matchVal , 
					shouldBeIdent.getVal( key ).getVal() , 1E-5 );
//			} catch( Throwable ex )
//			{
//				System.out.println( "*****" );
//				java.util.Iterator<BigInteger> ii = key.iterator();
//				while( ii.hasNext() )
//				{
//					System.out.println( ii.next() );
//				}
//				System.out.println( shouldBeIdent.getVal( key ).getVal() );
//			}
			
		}
		
	}
	
	
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertLeft()}.
	 */
	public void testInvertLeft() throws NotInvertibleException
	{
		seedTestInvertLeft( 1111 );
		seedTestInvertLeft( 2222 );
		seedTestInvertLeft( 3333 );
		seedTestInvertLeft( 4444 );
		seedTestInvertLeft( 5555 );
		seedTestInvertLeft( 6666 );
		seedTestInvertLeft( 7777 );
		seedTestInvertLeft( 8888 );
		seedTestInvertLeft( 9999 );
	}


	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertLeft()}.
	 */
	private void seedTestInvertLeft( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final SpacetimeAlgebraOrd<TestDimensionFive> ord = new SpacetimeAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		final int max = 1 << 5;
		
		int i;
		int j;
		
		for( i = 0 ; i < max ; i++ )
		{
			HashSet<BigInteger> key = new HashSet<BigInteger>();
			for( j = 0 ; j < 5 ; j++ )
			{
				final int bit = 1 << j;
				final boolean bitOn = ( i & bit ) != 0;
				if( bitOn )
				{
					key.add( BigInteger.valueOf( j ) );
				}
			}
			mv.setVal(key, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		}
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsUnit( shouldBeIdentA );
		
		validateIsUnit( shouldBeIdentB );
		
		
	}
	
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertRight()}.
	 */
	public void testInvertRight() throws NotInvertibleException
	{
		seedTestInvertRight( 1111 );
		seedTestInvertRight( 2222 );
		seedTestInvertRight( 3333 );
		seedTestInvertRight( 4444 );
		seedTestInvertRight( 5555 );
		seedTestInvertRight( 6666 );
		seedTestInvertRight( 7777 );
		seedTestInvertRight( 8888 );
		seedTestInvertRight( 9999 );
	}

	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertRight()}.
	 */
	private void seedTestInvertRight( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final SpacetimeAlgebraOrd<TestDimensionFive> ord = new SpacetimeAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		final int max = 1 << 5;
		
		int i;
		int j;
		
		for( i = 0 ; i < max ; i++ )
		{
			HashSet<BigInteger> key = new HashSet<BigInteger>();
			for( j = 0 ; j < 5 ; j++ )
			{
				final int bit = 1 << j;
				final boolean bitOn = ( i & bit ) != 0;
				if( bitOn )
				{
					key.add( BigInteger.valueOf( j ) );
				}
			}
			mv.setVal(key, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		}
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertRight();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsUnit( shouldBeIdentA );
		
		validateIsUnit( shouldBeIdentB );
		
		
	}
	
	
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertLeft()}.
	 */
	public void testSimpleInvert() throws NotInvertibleException
	{
		seedTestSimpleInvert( 1111 );
		seedTestSimpleInvert( 2222 );
		seedTestSimpleInvert( 3333 );
		seedTestSimpleInvert( 4444 );
		seedTestSimpleInvert( 5555 );
		seedTestSimpleInvert( 6666 );
		seedTestSimpleInvert( 7777 );
		seedTestSimpleInvert( 8888 );
		seedTestSimpleInvert( 9999 );
	}
	
	
	
	
	private void seedTestSimpleInvert( final long seed ) throws NotInvertibleException
	{
		seedTestSimpleInvertVect( seed );
		seedTestSimpleInvertBivec( seed );
		seedTestSimpleInvertTrivec( seed );
		seedTestSimpleInvertQuadvec( seed );
		seedTestSimpleInvertQuint( seed );
	}
	
	
	
	
	private void seedTestSimpleInvertVect( final long seed ) throws NotInvertibleException
	{
		Random rand = new Random( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final SpacetimeAlgebraOrd<TestDimensionFive> ord = new SpacetimeAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		keyA.add( BigInteger.valueOf( 1 ) );
		
		mv.setVal(keyA, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		HashSet<BigInteger> keyB = new HashSet<BigInteger>();
		
		keyB.add( BigInteger.valueOf( 2 ) );
		
		mv.setVal(keyB, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsUnit( shouldBeIdentA );
	
		validateIsUnit( shouldBeIdentB );
		
	}
	
	
	
	
	private void seedTestSimpleInvertBivec( final long seed ) throws NotInvertibleException
	{
		Random rand = new Random( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final SpacetimeAlgebraOrd<TestDimensionFive> ord = new SpacetimeAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		keyA.add( BigInteger.valueOf( 1 ) );
		keyA.add( BigInteger.valueOf( 2 ) );
		
		mv.setVal(keyA, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		HashSet<BigInteger> keyB = new HashSet<BigInteger>();
		
		keyB.add( BigInteger.valueOf( 2 ) );
		keyB.add( BigInteger.valueOf( 3 ) );
		
		mv.setVal(keyB, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsUnit( shouldBeIdentA );
	
		validateIsUnit( shouldBeIdentB );
		
	}
	
	
	
	private void seedTestSimpleInvertTrivec( final long seed ) throws NotInvertibleException
	{
		Random rand = new Random( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final SpacetimeAlgebraOrd<TestDimensionFive> ord = new SpacetimeAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		keyA.add( BigInteger.valueOf( 1 ) );
		keyA.add( BigInteger.valueOf( 2 ) );
		keyA.add( BigInteger.valueOf( 3 ) );
		
		mv.setVal(keyA, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		HashSet<BigInteger> keyB = new HashSet<BigInteger>();
		
		keyB.add( BigInteger.valueOf( 2 ) );
		keyB.add( BigInteger.valueOf( 3 ) );
		keyB.add( BigInteger.valueOf( 4 ) );
		
		mv.setVal(keyB, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsUnit( shouldBeIdentA );
	
		validateIsUnit( shouldBeIdentB );
		
	}
	
	
	
	private void seedTestSimpleInvertQuadvec( final long seed ) throws NotInvertibleException
	{
		Random rand = new Random( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final SpacetimeAlgebraOrd<TestDimensionFive> ord = new SpacetimeAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		keyA.add( BigInteger.valueOf( 1 ) );
		keyA.add( BigInteger.valueOf( 2 ) );
		keyA.add( BigInteger.valueOf( 3 ) );
		keyA.add( BigInteger.valueOf( 4 ) );
		
		mv.setVal(keyA, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		HashSet<BigInteger> keyB = new HashSet<BigInteger>();
		
		keyB.add( BigInteger.valueOf( 2 ) );
		keyB.add( BigInteger.valueOf( 3 ) );
		keyB.add( BigInteger.valueOf( 4 ) );
		keyB.add( BigInteger.valueOf( 5 ) );
		
		mv.setVal(keyB, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsUnit( shouldBeIdentA );
	
		validateIsUnit( shouldBeIdentB );
		
	}
	
	
	
	private void seedTestSimpleInvertQuint( final long seed ) throws NotInvertibleException
	{
		Random rand = new Random( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final SpacetimeAlgebraOrd<TestDimensionFive> ord = new SpacetimeAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		keyA.add( BigInteger.valueOf( 1 ) );
		keyA.add( BigInteger.valueOf( 2 ) );
		keyA.add( BigInteger.valueOf( 3 ) );
		keyA.add( BigInteger.valueOf( 4 ) );
		keyA.add( BigInteger.valueOf( 5 ) );
		
		mv.setVal(keyA, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,SpacetimeAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsUnit( shouldBeIdentA );
	
		validateIsUnit( shouldBeIdentB );
		
	}
	
	
	
	
	
}



