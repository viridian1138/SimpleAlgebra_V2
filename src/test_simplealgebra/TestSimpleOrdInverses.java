



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


/**
 * Tests the generation of elems that have different left/right inverses, and inverses thereof.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestSimpleOrdInverses extends TestCase {
	
	

	
	/**
	 * Generates a random multivector that has different left/right inverses.
	 * 
	 * @param seed The random number seed.
	 * @return The random multivector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> generateNoisedOrdVect( final long seed )
	{
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final TestInvOrd<TestDimensionTwo> ord = new TestInvOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			mv.setVal( hs , new DoubleElem( rand.nextDouble() ) );
		}
		
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			mv.setVal( hs , new DoubleElem( rand.nextDouble() ) );
		}
		
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			mv.setVal( hs , new DoubleElem( rand.nextDouble() ) );
		}
		
		
		return( mv );
	}
	
	
	
	
	
	/**
	 * Tests inverses in cases where the left inverse is different from the right inverse.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testSimpleOrdInverses() throws NotInvertibleException
	{
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			xv = generateNoisedOrdVect( 334455 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			xiL = xv.invertLeft();
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			xiR = xv.invertRight();
		
		
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			final double iL = xiL.get( hs ).getVal();
			final double iR = xiR.get( hs ).getVal();
			Assert.assertTrue( Math.abs( iL - iR )  > 1E-7 );
		}
		
		
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			final double iL = xiL.get( hs ).getVal();
			final double iR = xiR.get( hs ).getVal();
			Assert.assertTrue( Math.abs( iL - iR )  > 1E-7 );
		}
		
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			final double iL = xiL.get( hs ).getVal();
			final double iR = xiR.get( hs ).getVal();
			Assert.assertTrue( Math.abs( iL - iR )  > 1E-7 );
		}
		
		
		
		
	
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			Rxv = xiR.mult( xv );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			xvR = xv.mult( xiR );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			Lxv = xiL.mult( xv );
	
	
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			xvL = xv.mult( xiL );
		
		
		
		// Verify that using the left inverse on the left works, but using the left inverse on the right doesn't.
		// Also verify that using the right inverse on the right works, but using the right inverse on the left doesn't.
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			final double Rxvv = Rxv.get( hs ).getVal();
			final double xvRv = xvR.get( hs ).getVal();
			final double Lxvv = Lxv.get( hs ).getVal();
			final double xvLv = xvL.get( hs ).getVal();
			
			Assert.assertTrue( Math.abs( Rxvv ) > 1E-6 );
			Assert.assertTrue( Math.abs( xvRv - 1.0 ) < 1E-6 );
			Assert.assertTrue( Math.abs( Lxvv - 1.0 ) < 1E-6 );
			Assert.assertTrue( Math.abs( xvLv ) > 1E-6 );
		}
		
		
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			// final double Rxvv = Rxv.get( hs ).getVal();
			final double xvRv = xvR.get( hs ).getVal();
			final double Lxvv = Lxv.get( hs ).getVal();
			// final double xvLv = xvL.get( hs ).getVal();
			
			Assert.assertTrue( Math.abs( xvRv ) < 1E-6 );
			Assert.assertTrue( Math.abs( Lxvv ) < 1E-6 );
		}
		
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			// final double Rxvv = Rxv.get( hs ).getVal();
			final double xvRv = xvR.get( hs ).getVal();
			final double Lxvv = Lxv.get( hs ).getVal();
			// final double xvLv = xvL.get( hs ).getVal();
			
			Assert.assertTrue( Math.abs( xvRv ) < 1E-6 );
			Assert.assertTrue( Math.abs( Lxvv ) < 1E-6 );
		}
		
		
		
		
	}
	
	
	
	
	/**
	 * Tests verifies by counterexample that the inv ord is not associative.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testNotAssociative() throws NotInvertibleException
	{
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			xv = generateNoisedOrdVect( 334455 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			yv = generateNoisedOrdVect( 554433 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			zv = generateNoisedOrdVect( 555555 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			a1 = ( xv.mult( yv ) ).mult( zv );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,TestInvOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>
			a2 = xv.mult( yv.mult( zv ) );
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			final double i1 = a1.get( hs ).getVal();
			final double i2 = a2.get( hs ).getVal();
			Assert.assertTrue( Math.abs( i1 - i2 )  > 1E-7 );
		}
		
		
	}
	
	

	
}




