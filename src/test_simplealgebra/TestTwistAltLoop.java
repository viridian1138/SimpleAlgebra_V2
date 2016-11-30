



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

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;


    /**
     * 
     * Populates a vector of coordinates from the isolines of 
     * a bilinear patch with multiple twists so that for
	 * the surface B(x, y) one can get the result <math display="inline">
     * <mrow>
     *  <mfrac>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *    </mrow>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *      <mi>u</mi>
     *    </mrow>
     *  </mfrac>
     *  <mfrac>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *    </mrow>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *      <mi>v</mi>
     *    </mrow>
     *  </mfrac>
     *  <mi>B</mi>
     *  <mo>&ne;</mo>
     *  <mfrac>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *    </mrow>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *      <mi>v</mi>
     *    </mrow>
     *  </mfrac>
     *  <mfrac>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *    </mrow>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *      <mi>u</mi>
     *    </mrow>
     *  </mfrac>
     *  <mi>B</mi>
     * </mrow>
     * </math>.  Hence, it is possible to yield a surface for which, <math display="inline">
     * <mrow>
     *  <mo>&nabla;</mo>
     *  <mo>&bull;</mo>
     *  <mo>(</mo>
     *  <mo>&nabla;</mo>
     *  <mo>&times;</mo>
     *  <mi>B</mi>
     *  <mo>)</mo>
     *  <mo>&ne;</mo>
     *  <mn>0</mn>
     * </mrow>
     * </math>.
	 * 
	 *   Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
     * 
     * This documentation should be viewed using Firefox version 33.1.1 or above.
     * 
     * @author thorngreen
     *
     */
public class TestTwistAltLoop extends TestCase {
	
	
	/**
	 * The number of points in each quadrant of the isoline.
	 */
	static final int CMAX = 160;
	
	
	/**
	 * The dimensionality for the result vector.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class Adim extends NumDimensions
	{
		/**
		 * The number of dimensions.
		 */
		BigInteger dim;
		
		/**
		 * Constructs the dimension.
		 */
		public Adim( )
		{
			dim = BigInteger.valueOf( 2 * CMAX );
		}

		/**
		 * Gets the number of dimensions.
		 * @return The number of dimensions.
		 */
		@Override
		public BigInteger getVal() {
			return( dim );
		}
		
	};
	
	
	
	/**
	 * Evaluates a Bezier curve blending between two values.
	 * @param b0 First blend value.
	 * @param b1 Second blend value.
	 * @param u0 First barycentric parameter.
	 * @param u1 Second barycentric parameter.
	 * @return The evaluated value.
	 */
	protected static double evalCurveBez( final double b0 , final double b1 , final double u0 , final double u1 )
	{
		final double b10 = u0 * b0 + u1 * b0;
		final double b11 = u0 * b0 + u1 * b1;
		final double b12 = u0 * b1 + u1 * b1;
		
		final double b20 = u0 * b10 + u1 * b11;
		final double b21 = u0 * b11 + u1 * b12;
		
		final double b30 = u0 * b20 + u1 * b21;
		
		return( b30 );
	}
	
	
	
	/**
	 * Evaluates a Bezier curve blending across (u,v) parameters.
	 * @param b0 First blend value.
	 * @param b1 Second blend value.
	 * @param u Blend "u" parameter.
	 * @param v Blend "v" parameter.
	 * @return The evaluated value.
	 */
	final static double evalCurveBezUv( final double b0 , final double b1 , double u , double v )
	{
		if( u < 0.0 )
		{
			u = 0.0;
		}
		
		if( v < 0.0 )
		{
			v = 0.0;
		}
		
		return( evalCurveBez( b0 , b1 , u / ( u + v ) , v / ( u + v) ) );
	}
	
	
	
	/**
	 * Evaluates a bilinear patch with multiple twists so that for
	 * the surface B(x, y) one can get the result <math display="inline">
     * <mrow>
     *  <mfrac>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *    </mrow>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *      <mi>u</mi>
     *    </mrow>
     *  </mfrac>
     *  <mfrac>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *    </mrow>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *      <mi>v</mi>
     *    </mrow>
     *  </mfrac>
     *  <mi>B</mi>
     *  <mo>&ne;</mo>
     *  <mfrac>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *    </mrow>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *      <mi>v</mi>
     *    </mrow>
     *  </mfrac>
     *  <mfrac>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *    </mrow>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *      <mi>u</mi>
     *    </mrow>
     *  </mfrac>
     *  <mi>B</mi>
     * </mrow>
     * </math>.  Hence, it is possible to yield a surface for which, <math display="inline">
     * <mrow>
     *  <mo>&nabla;</mo>
     *  <mo>&bull;</mo>
     *  <mo>(</mo>
     *  <mo>&nabla;</mo>
     *  <mo>&times;</mo>
     *  <mi>B</mi>
     *  <mo>)</mo>
     *  <mo>&ne;</mo>
     *  <mn>0</mn>
     * </mrow>
     * </math>.
	 * 
	 * @param u The "u" parameter of the patch.
	 * @param v The "v" parameter of the patch.
	 * @param b00 The b00 value of the patch.
	 * @param b01 The b01 value of the patch.
	 * @param b10 The b10 value of the patch.
	 * @param b11u The b11 value of the patch along "u".
	 * @param b11v The b11 value of the patch along "v".
	 * @return The evaluated value.
	 */
	static final double evalSurfTwist( final double u , final double v , 
			final double b00 , final double b01 , final double b10 , 
			final double b11u , final double b11v )
	{
		final double b11 = evalCurveBezUv( b11u , b11v , u , v );
		
		final double b0 = ( 1 - u ) * b00 + u * b01;
		final double b1 = ( 1 - u ) * b10 + u * b11;
		
		return( ( 1 - v ) * b0 + v * b1 );		
	}
	
	
	
	/**
	 * Returns a isoline value from a bilinear patch with multiple twists.
	 * @param ang The angle at which to look for the cutoff.
	 * @param cutoff The isoline cutoff value.
	 * @param b00 The b00 value of the patch.
	 * @param b01 The b01 value of the patch.
	 * @param b10 The b10 value of the patch.
	 * @param b11u The b11 value of the patch along "u".
	 * @param b11v The b11 value of the patch along "v".
	 * @return The evaluated coordinate.
	 */
	static final double[] evalSurfTwistRad( final double ang , final double cutoff , 
			final double b00 , final double b01 , final double b10 , 
			final double b11u , final double b11v )
	{
		double a1 = 1E-6;
		double a2 = 1E+6;
		
		double a = 1.0;
		
		final double ua = Math.cos( ang );
		final double va = Math.sin( ang );
		
		for( int cnt = 0 ; cnt < 35 ; cnt++ )
		{
			final double eval = evalSurfTwist( a * ua , a * va , 
					b00 , b01 , b10 , b11u , b11v );
			if( eval == cutoff )
			{
				final double[] ret = { a * ua , a * va , a };
				return( ret );
			}
			
			if( eval > cutoff )
			{
				a2 = a;
				a = ( a1 + a ) / 2.0;
			}
			else
			{
				a1 = a;
				a = ( a2 + a ) / 2.0;
			}
			
		}
		
		final double[] ret = { a * ua , a * va , a };
		return( ret );
	}
	
	
	
	/**
	 * Builds a vector representing the coordinate.
	 * @param fac Factory for building DoubleElem instances.
	 * @param d0 The "u" coordinate.
	 * @param d1 The "v" coordinate.
	 * @return The generated vector.
	 */
	protected static GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>
		genVect2( GeometricAlgebraMultivectorElemFactory<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory> fac ,
			double d0 , double d1 )
	{
		final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>
			vect = fac.zero();
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			vect.setVal( hs , new DoubleElem( d0 ) );
		}
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			vect.setVal( hs , new DoubleElem( d1 ) );
		}
		
		
		return( vect );
	}

	
	
	/**
	 * Runs the test.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTwistAltLoop() throws NotInvertibleException
	{
		
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final DoubleElemFactory dd = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>(dd, td, ord);
		
		final Adim adim = new Adim();
		
		final GeometricAlgebraOrd<Adim> orda = new GeometricAlgebraOrd<Adim>();
		
		final GeometricAlgebraMultivectorElemFactory<Adim, GeometricAlgebraOrd<Adim>, GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>> sea = 
				new GeometricAlgebraMultivectorElemFactory<Adim, GeometricAlgebraOrd<Adim>, GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>>(se, adim, orda);
		
		
		final GeometricAlgebraMultivectorElem<Adim, GeometricAlgebraOrd<Adim>, GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>> vect = 
				sea.zero();
		
		
		
		final double kvaa = 1.0;
		
		final double kvb = 0.82;
		
		
		final double cutoff = 0.5;
		
		final double rang = Math.PI / 2.0;
		
		
		
		BigInteger vcnt = BigInteger.ZERO;
		
		
		
		{
			final double b00 = 0.0;
			final double b01 = kvaa; 
			final double b10 = kvaa;
			final double b11u = kvaa - kvb; 
			final double b11v = kvaa + kvb;
			
			
			Assert.assertTrue( Math.abs( b10 - b01 ) < 1E-6 );
			
			
			for( int cnt = 0 ; cnt < CMAX ; cnt++ )
			{
				final double ua = ( (double) cnt ) / ( CMAX - 1 );
				
				final double ang = ( 1.0 - ua ) * ( -0.4999 * rang ) + ( ua ) * ( 1.4999 * rang );
				
				final double[] db = evalSurfTwistRad( ang , cutoff , 
						b00 , b01 , b10 , b11u , b11v );
				
				
				System.out.println( ( - db[ 0 ] ) + " " + ( db[ 1 ] ) + " " + ( db[ 2 ]) );
				
				final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>
					v = genVect2( se , - db[ 0 ] , db[ 1 ] );
				
				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
				hs.add( vcnt );
				vect.setVal( hs , v );
				vcnt = vcnt.add( BigInteger.ONE );
				
			}
			
		}
		

		
		
		
		// #############################################################
		
		
		// System.out.println( "///" );
		
		
		{
			final double b00 = 0.0;
			final double b01 = kvaa;
			final double b10 = kvaa; 
			final double b11u = kvaa + kvb; 
			final double b11v = kvaa - kvb;
			
			
			Assert.assertTrue( Math.abs( b10 - b01 ) < 1E-6 );
			
			
			for( int cnt = 0 ; cnt < CMAX ; cnt++ )
			{
				final double ua = ( (double) cnt ) / ( CMAX - 1 );
				
				final double ang = ( 1.0 - ua ) * ( -0.4999 * rang ) + ( ua ) * ( 1.4999 * rang );
				
				final double[] db = evalSurfTwistRad( ang , cutoff , 
						b00 , b01 , b10 , b11u , b11v );
				
				
				System.out.println( ( db[ 0 ] ) + " " + ( - db[ 1 ] ) + " " + ( db[ 2 ]) );
				
				final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory>
					v = genVect2( se , db[ 0 ] , - db[ 1 ] );
				
				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
				hs.add( vcnt );
				vect.setVal( hs , v );
				vcnt = vcnt.add( BigInteger.ONE );
				
			}
			
		}
		
		
		

		
		
		
		
		
	}
	
	
	

	
}




