



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
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.ga.Ord;
import simplealgebra.ga.SpacetimeAlgebraOrd;
import simplealgebra.samp.*;
import java.util.*;


/**
 * Tests the generation of simple fits.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestSimpleFits extends TestCase {
	
	
	
	/**
	 * The number of dimensions.
	 */
	static final int FIFTY_INT = 50;
	
	/**
	 * The number of dimensions.
	 */
	static final BigInteger FIFTY = BigInteger.valueOf( FIFTY_INT );
	
	
	
	/**
	 * Test class for fifty-dimensional problems.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TestDimensionFifty extends NumDimensions {

		/**
		 * Constructs the NumDimensions.
		 */
		public TestDimensionFifty() {
			// Does Nothing
		}

		@Override
		public BigInteger getVal() {
			return( FIFTY );
		}

	}
	
	
	
	
	/**
	 * Generates a random vector of X-Axis DoubleElems.
	 * 
	 * @param seed The random number seed from which to generate the vector.
	 * @return The resulting vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,DoubleElem,DoubleElemFactory> generateNoisedLinearXData( final long seed )
	{
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFifty td = new TestDimensionFifty();
		
		final GeometricAlgebraOrd<TestDimensionFifty> ord = new GeometricAlgebraOrd<TestDimensionFifty>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		
		for( int cnt = 0 ; cnt < FIFTY_INT ; cnt++ )
		{
			final double x = 75.0 + 8.0 * cnt + 0.3 * ( rand.nextDouble() - 0.5 );
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.valueOf( cnt ) );
			mv.setVal( ind , new DoubleElem( x ) );
		}
		
		
		return( mv );
	}
	
	
	
	/**
	 * Generates a random vector of Y-Axis DoubleElems.
	 * 
	 * @param seed The random number seed from which to generate the vector.
	 * @return The resulting vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,DoubleElem,DoubleElemFactory> generateNoisedLinearYData( final long seed )
	{
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFifty td = new TestDimensionFifty();
		
		final GeometricAlgebraOrd<TestDimensionFifty> ord = new GeometricAlgebraOrd<TestDimensionFifty>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,DoubleElem,DoubleElemFactory> mv = se.zero();
		
		
		for( int cnt = 0 ; cnt < FIFTY_INT ; cnt++ )
		{
			final double y = 225.0 + 66.6 * cnt + 0.3 * ( rand.nextDouble() - 0.5 );
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.valueOf( cnt ) );
			mv.setVal( ind , new DoubleElem( y ) );
		}
		
		
		return( mv );
	}
	
	
	
	/**
	 * Generates a random vector of X-Axis vector positions.
	 * 
	 * @param seed The random number seed from which to generate the vector.
	 * @return The resulting vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>,
		GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> generateNoisedLinearXVectData( final long seed )
	{
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionThree tdA = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ordA = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> seA = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, tdA, ordA);
		
		final TestDimensionFifty tdB = new TestDimensionFifty();
		
		final GeometricAlgebraOrd<TestDimensionFifty> ordB = new GeometricAlgebraOrd<TestDimensionFifty>();
		
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>,
			GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> seB =
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>,
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>(seA, tdB, ordB);
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>,
			GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> mv = seB.zero();
		
		
		for( int cnt = 0 ; cnt < FIFTY_INT ; cnt++ )
		{
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.valueOf( cnt ) );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				mvA = seA.zero();
			
			{
				final double x = 75.0 + 8.0 * cnt + 0.3 * ( rand.nextDouble() - 0.5 );
				final HashSet<BigInteger> indA = new HashSet<BigInteger>();
				indA.add( BigInteger.ZERO );
				mvA.setVal( indA , new DoubleElem( x ) );
			}
			
			mv.setVal( ind , mvA );
		}
		
		
		return( mv );
	}
	
	
	
	
	/**
	 * Generates a random vector of Y-Axis vector positions.
	 * 
	 * @param seed The random number seed from which to generate the vector.
	 * @return The resulting vector.
	 */
protected GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,
	GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> generateNoisedLinearYVectData( final long seed )
{
	final Random rand = new Random();
	
	rand.setSeed( seed );
	
	final TestDimensionThree tdA = new TestDimensionThree();
	
	final GeometricAlgebraOrd<TestDimensionThree> ordA = new GeometricAlgebraOrd<TestDimensionThree>();
	
	final DoubleElemFactory dl = new DoubleElemFactory();
	
	final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> seA = 
			new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, tdA, ordA);
	
	final TestDimensionFifty tdB = new TestDimensionFifty();
	
	final GeometricAlgebraOrd<TestDimensionFifty> ordB = new GeometricAlgebraOrd<TestDimensionFifty>();
	
	
	final GeometricAlgebraMultivectorElemFactory<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>,
		GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> seB =
			new GeometricAlgebraMultivectorElemFactory<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>,
			GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>(seA, tdB, ordB);
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>,
		GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> mv = seB.zero();
	
	
	for( int cnt = 0 ; cnt < FIFTY_INT ; cnt++ )
	{
		final HashSet<BigInteger> ind = new HashSet<BigInteger>();
		ind.add( BigInteger.valueOf( cnt ) );
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			mvA = seA.zero();
		
		{	
			final double yx = 250.0 + 2.6 * cnt + 0.3 * ( rand.nextDouble() - 0.5 );
			final double yy = 225.0 + 66.6 * cnt + 0.3 * ( rand.nextDouble() - 0.5 );
			final HashSet<BigInteger> indAx = new HashSet<BigInteger>();
			final HashSet<BigInteger> indAy = new HashSet<BigInteger>();
			indAx.add( BigInteger.ZERO );
			indAy.add( BigInteger.ONE );
			mvA.setVal( indAx , new DoubleElem( yx ) );
			mvA.setVal( indAy , new DoubleElem( yy ) );
		}
		
		mv.setVal( ind , mvA );
	}
	
	
	return( mv );
}
	
	
	
	/**
	 * Tests scalar X-Y fits.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testSimpleLinearFits() throws NotInvertibleException
	{
		
		final GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,DoubleElem,DoubleElemFactory>
			xv = generateNoisedLinearXData( 334455 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,DoubleElem,DoubleElemFactory>
			yv = generateNoisedLinearYData( 445566 );
		
		
		final LinearFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, DoubleElem, DoubleElemFactory> lin
			= new LinearFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, DoubleElem, DoubleElemFactory>( xv , yv );
		
		
		final QuadraticFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, DoubleElem, DoubleElemFactory> quad1
			= new QuadraticFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, DoubleElem, DoubleElemFactory>( xv , yv );
		
		
		final NPowerFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, DoubleElem, DoubleElemFactory> quad2
			= new NPowerFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, DoubleElem, DoubleElemFactory>( 2 , xv , yv );
		
		
		final NPowerFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, DoubleElem, DoubleElemFactory> tri1
			= new NPowerFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, DoubleElem, DoubleElemFactory>( 3 , xv , yv );
		
		
		
		lin.generateFit();
		
		quad1.generateFit();
		
		quad2.generateFit();
		
		tri1.generateFit();
		
		
		
		// System.out.println( "mn X ---" );
		
		Assert.assertEquals( 75.0 + 8.0 * 24.5 , lin.getMeanX().getVal() , 0.5 );
		
		Assert.assertEquals( 75.0 + 8.0 * 24.5 , quad1.getMeanX().getVal() , 0.5 );
		
		Assert.assertEquals( 75.0 + 8.0 * 24.5 , quad2.getMeanX().getVal() , 0.5 );
		
		Assert.assertEquals( 75.0 + 8.0 * 24.5 , tri1.getMeanX().getVal() , 0.5 );
		
		
		
		// System.out.println( "mn Y ---" );
		
		Assert.assertEquals( 225.0 + 66.6 * 24.5 , lin.getMeanY().getVal() , 0.5 );
		
		Assert.assertEquals( 225.0 + 66.6 * 24.5 , quad1.getMeanY().getVal() , 0.5 );
		
		Assert.assertEquals( 225.0 + 66.6 * 24.5 , quad2.getMeanY().getVal() , 0.5 );
		
		Assert.assertEquals( 225.0 + 66.6 * 24.5 , tri1.getMeanY().getVal() , 0.5 );
		
		
		
		// System.out.println( "mn slp ---" );
		
		Assert.assertEquals( 66.0 / 8.0 , lin.getSlope().getVal() , 0.5 );
		
		Assert.assertEquals( 66.0 / 8.0 , quad1.getSlope().getVal() , 0.5 );
		
		Assert.assertEquals( 66.0 / 8.0 , quad2.getSlopes().get( 0 ).getVal() , 0.5 );
		
		Assert.assertEquals( 66.0 / 8.0 , tri1.getSlopes().get( 0 ).getVal() , 0.5 );
		
		
		
		// System.out.println( "mn acc ---" );
		
		Assert.assertEquals( 0.0 , quad1.getAcc().getVal() , 0.5 );
		
		Assert.assertEquals( 0.0 , quad2.getSlopes().get( 1 ).getVal() , 0.5 );
		
		Assert.assertEquals( 0.0 , tri1.getSlopes().get( 1 ).getVal() , 0.5 );
		
		
		
		// System.out.println( "mn 3 ---" );
		
		Assert.assertEquals( 0.0 , tri1.getSlopes().get( 2 ).getVal() , 0.5 );
		
		
	}
	
	
	
	
	/**
	 * Tests vector X-Y fits.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testSimpleLinearFitsVect() throws NotInvertibleException
	{
		
		final GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>,
			GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>
				xv = generateNoisedLinearXVectData( 334455 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFifty,GeometricAlgebraOrd<TestDimensionFifty>,
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>,
			GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>
				yv = generateNoisedLinearYVectData( 445566 );
		
		
		final LinearFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, 
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>, 
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> lin
			= new LinearFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, 
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>, 
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>( xv , yv );
		
		
		final QuadraticFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, 
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>, 
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> quad1
			= new QuadraticFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, 
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>, 
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>( xv , yv );
		
		
		final NPowerFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, 
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>, 
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> quad2
			= new NPowerFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, 
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>, 
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>( 2 , xv , yv );
		
		
		final NPowerFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, 
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>, 
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> tri1
			= new NPowerFit<TestDimensionFifty, GeometricAlgebraOrd<TestDimensionFifty>, 
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>, 
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>( 3 , xv , yv );
		
		
		
		lin.generateFit();
		
		quad1.generateFit();
		
		quad2.generateFit();
		
		tri1.generateFit();
		
		
		
		
		{
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.ZERO );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> linx = lin.getMeanX();
			Assert.assertEquals( 75.0 + 8.0 * 24.5 , linx.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> quad1x = quad1.getMeanX();
			Assert.assertEquals( 75.0 + 8.0 * 24.5 , quad1x.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> quad2x = quad2.getMeanX();
			Assert.assertEquals( 75.0 + 8.0 * 24.5 , quad2x.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> tri1x = tri1.getMeanX();
			Assert.assertEquals( 75.0 + 8.0 * 24.5 , tri1x.getVal( ind ).getVal() , 0.5 );
		}
		
		
		
		
		
		{
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.ONE );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> liny = lin.getMeanY();
			Assert.assertEquals( 225.0 + 66.6 * 24.5 , liny.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> quad1y = quad1.getMeanY();
			Assert.assertEquals( 225.0 + 66.6 * 24.5 , quad1y.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> quad2y = quad2.getMeanY();
			Assert.assertEquals( 225.0 + 66.6 * 24.5 , quad2y.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> tri1y = tri1.getMeanY();
			Assert.assertEquals( 225.0 + 66.6 * 24.5 , tri1y.getVal( ind ).getVal() , 0.5 );
		}
		
		
		
		
		{
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.ZERO );
			ind.add( BigInteger.ONE );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lins = lin.getSlope();
			Assert.assertEquals( - 66.0 / 8.0 , lins.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> quad1s = quad1.getSlope();
			Assert.assertEquals( - 66.0 / 8.0 , quad1s.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> quad2s = quad2.getSlopes().get( 0 );
			Assert.assertEquals( - 66.0 / 8.0 , quad2s.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> tri1s = tri1.getSlopes().get( 0 );
			Assert.assertEquals( - 66.0 / 8.0 , tri1s.getVal( ind ).getVal() , 0.5 );
		}
		
		
		
		
		
		{
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.ONE );
						
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> quad1s = quad1.getAcc();
			Assert.assertEquals( 0.0 , quad1s.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> quad2s = quad2.getSlopes().get( 1 );
			Assert.assertEquals( 0.0 , quad2s.getVal( ind ).getVal() , 0.5 );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> tri1s = tri1.getSlopes().get( 1 );
			Assert.assertEquals( 0.0 , tri1s.getVal( ind ).getVal() , 0.5 );
		}
		
		
		
		
		
		{
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.ZERO );
			ind.add( BigInteger.ONE );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> tri1s = tri1.getSlopes().get( 2 );
			Assert.assertEquals( 0.0 , tri1s.getVal( ind ).getVal() , 0.5 );
		}
		
		
		
		
		
	}
	
	

	
}



