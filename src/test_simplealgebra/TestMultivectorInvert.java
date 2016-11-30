



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
import simplealgebra.ga.*;


/**
 * Tests the ability to invert a Geometric Algebra multivector in 5-D.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestMultivectorInvert extends TestCase {
	
	
	/**
	 * Validates that a particular multivector is, to within a constant,
	 * equal to the identity multivector.  Asserts if the condition is not met.
	 * 
	 * @param shouldBeIdent The multivector to be tested against the identity.
	 */
	private void validateIsIdentity( GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdent )
	{
		final int max = 1 << TestDimensionFive.FIVE;
		
		int i;
		int j;
		for( i = 0 ; i < max ; i++ )
		{
			HashSet<BigInteger> key = new HashSet<BigInteger>();
			for( j = 0 ; j < TestDimensionFive.FIVE ; j++ )
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
	 * Test method for {@link simplealgebra.ga.GeometricAlgebraMultivectorElem#invertLeft()}.
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
	 * Validates that a random multivector times its left-inverse is equal to the identity.
	 * 
	 * @param seed The random number seed from which to generate the random multivector.
	 * @throws NotInvertibleException
	 */
	private void seedTestInvertLeft( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		final int max = 1 << TestDimensionFive.FIVE;
		
		int i;
		int j;
		
		for( i = 0 ; i < max ; i++ )
		{
			HashSet<BigInteger> key = new HashSet<BigInteger>();
			for( j = 0 ; j < TestDimensionFive.FIVE ; j++ )
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
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsIdentity( shouldBeIdentA );
		
		validateIsIdentity( shouldBeIdentB );
		
		
	}
	
	
	/**
	 * Test method for {@link simplealgebra.ga.GeometricAlgebraMultivectorElem#invertRight()}.
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
	 * Validates that a random multivector times its right-inverse is equal to the identity.
	 * 
	 * @param seed The random number seed from which to generate the random multivector.
	 * @throws NotInvertibleException
	 */
	private void seedTestInvertRight( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		final int max = 1 << TestDimensionFive.FIVE;
		
		int i;
		int j;
		
		for( i = 0 ; i < max ; i++ )
		{
			HashSet<BigInteger> key = new HashSet<BigInteger>();
			for( j = 0 ; j < TestDimensionFive.FIVE ; j++ )
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
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertRight();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsIdentity( shouldBeIdentA );
		
		validateIsIdentity( shouldBeIdentB );
		
		
	}
	
	
	
	/**
	 * Test method for {@link simplealgebra.ga.GeometricAlgebraMultivectorElem#invertLeft()}.
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
	
	
	
	/**
	 * Tests a series of inverses of randomly-generated blades.
	 * 
	 * @param seed The random number seed from which to generate the random blades.
	 * @throws NotInvertibleException
	 */
	private void seedTestSimpleInvert( final long seed ) throws NotInvertibleException
	{
		seedTestSimpleInvertVect( seed );
		seedTestSimpleInvertBivec( seed );
		seedTestSimpleInvertTrivec( seed );
		seedTestSimpleInvertQuadvec( seed );
		seedTestSimpleInvertQuint( seed );
	}
	
	
	
	/**
	 * Validates that a random vector times its inverse is equal to the identity.
	 * 
	 * @param seed The random number seed from which to generate the random vector.
	 * @throws NotInvertibleException
	 */
	private void seedTestSimpleInvertVect( final long seed ) throws NotInvertibleException
	{
		Random rand = new Random( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		keyA.add( BigInteger.valueOf( 1 ) );
		
		mv.setVal(keyA, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		HashSet<BigInteger> keyB = new HashSet<BigInteger>();
		
		keyB.add( BigInteger.valueOf( 2 ) );
		
		mv.setVal(keyB, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsIdentity( shouldBeIdentA );
	
		validateIsIdentity( shouldBeIdentB );
		
	}
	
	
	
	/**
	 * Validates that a random bivector times its inverse is equal to the identity.
	 * 
	 * @param seed The random number seed from which to generate the random bivector.
	 * @throws NotInvertibleException
	 */
	private void seedTestSimpleInvertBivec( final long seed ) throws NotInvertibleException
	{
		Random rand = new Random( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		keyA.add( BigInteger.valueOf( 1 ) );
		keyA.add( BigInteger.valueOf( 2 ) );
		
		mv.setVal(keyA, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		HashSet<BigInteger> keyB = new HashSet<BigInteger>();
		
		keyB.add( BigInteger.valueOf( 2 ) );
		keyB.add( BigInteger.valueOf( 3 ) );
		
		mv.setVal(keyB, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsIdentity( shouldBeIdentA );
	
		validateIsIdentity( shouldBeIdentB );
		
	}
	
	
	/**
	 * Validates that a random trivector times its inverse is equal to the identity.
	 * 
	 * @param seed The random number seed from which to generate the random trivector.
	 * @throws NotInvertibleException
	 */
	private void seedTestSimpleInvertTrivec( final long seed ) throws NotInvertibleException
	{
		Random rand = new Random( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
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
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsIdentity( shouldBeIdentA );
	
		validateIsIdentity( shouldBeIdentB );
		
	}
	
	
	/**
	 * Validates that a random quad-vector times its inverse is equal to the identity.
	 * 
	 * @param seed The random number seed from which to generate the random quad-vector.
	 * @throws NotInvertibleException
	 */
	private void seedTestSimpleInvertQuadvec( final long seed ) throws NotInvertibleException
	{
		Random rand = new Random( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
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
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsIdentity( shouldBeIdentA );
	
		validateIsIdentity( shouldBeIdentB );
		
	}
	
	
	/**
	 * Validates that a random quint-vector times its inverse is equal to the identity.
	 * 
	 * @param seed The random number seed from which to generate the random quint-vector.
	 * @throws NotInvertibleException
	 */
	private void seedTestSimpleInvertQuint( final long seed ) throws NotInvertibleException
	{
		Random rand = new Random( seed );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		keyA.add( BigInteger.valueOf( 1 ) );
		keyA.add( BigInteger.valueOf( 2 ) );
		keyA.add( BigInteger.valueOf( 3 ) );
		keyA.add( BigInteger.valueOf( 4 ) );
		keyA.add( BigInteger.valueOf( 5 ) );
		
		mv.setVal(keyA, new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 ) );
		
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> inv = mv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentA = mv.mult( inv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mv );
		
		
		validateIsIdentity( shouldBeIdentA );
	
		validateIsIdentity( shouldBeIdentB );
		
	}
	
	
	
	
	
}



