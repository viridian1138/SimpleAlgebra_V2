



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


/**
 * Tests the generation of simple fits.
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
		
		
		for( int cnt = 0 ; cnt < 50 ; cnt++ )
		{
			final double x = 75.0 + 8.0 * cnt + 0.3 * ( rand.nextDouble() - 0.5 );
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.valueOf( cnt ) );
			mv.setVal( ind , new DoubleElem( x ) );
		}
		
		
		return( mv );
	}
	
	
	
	
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
		
		
		for( int cnt = 0 ; cnt < 50 ; cnt++ )
		{
			final double y = 225.0 + 66.6 * cnt + 0.3 * ( rand.nextDouble() - 0.5 );
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.valueOf( cnt ) );
			mv.setVal( ind , new DoubleElem( y ) );
		}
		
		
		return( mv );
	}
	
	
	
	
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
		
		
		
		System.out.println( "mn X ---" );
		
		System.out.println( lin.getMeanX().getVal() );
		
		System.out.println( quad1.getMeanX().getVal() );
		
		System.out.println( quad2.getMeanX().getVal() );
		
		System.out.println( tri1.getMeanX().getVal() );
		
		
		
		System.out.println( "mn Y ---" );
		
		System.out.println( lin.getMeanY().getVal() );
		
		System.out.println( quad1.getMeanY().getVal() );
		
		System.out.println( quad2.getMeanY().getVal() );
		
		System.out.println( tri1.getMeanY().getVal() );
		
		
		
		System.out.println( "mn slp ---" );
		
		System.out.println( lin.getSlope().getVal() );
		
		System.out.println( quad1.getSlope().getVal() );
		
		System.out.println( quad2.getSlopes().get( 0 ).getVal() );
		
		System.out.println( tri1.getSlopes().get( 0 ).getVal() );
		
		
		
		System.out.println( "mn acc ---" );
		
		System.out.println( quad1.getAcc().getVal() );
		
		System.out.println( quad2.getSlopes().get( 1 ).getVal() );
		
		System.out.println( tri1.getSlopes().get( 1 ).getVal() );
		
		
		
		System.out.println( "mn 3 ---" );
		
		System.out.println( tri1.getSlopes().get( 2 ).getVal() );
		
		
	}
	
	

	
}



