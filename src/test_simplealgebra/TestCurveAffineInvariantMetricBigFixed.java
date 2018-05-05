






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
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import test_simplealgebra.TestMandelbrotSet.LrgPrecision;



/**
 * Same test as TestCurveAffineInvariantMetric, but with higher precision exp and ln.
 * 
 * @author tgreen
 *
 */
public class TestCurveAffineInvariantMetricBigFixed extends TestCase {


	
	
	/**
	 * Constant containing the number ten.
	 */
	static final BigInteger TEN = BigInteger.valueOf( 10 );
	
	
	/**
	 * Returns the number <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *       <mrow>
     *         <mi>X</mi>
     *         <mo>+</mo>
     *         <mn>1</mn>
     *       </mrow>
     *   </msup>
     * </mrow>
     * </math>, where X is the input parameter.
	 * 
	 * @param cnt The input parameter.
	 * @return The value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *       <mrow>
     *         <mi>X</mi>
     *         <mo>+</mo>
     *         <mn>1</mn>
     *       </mrow>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	protected static BigInteger calcVal( final int cnt )
	{
		BigInteger ret = TEN;
		for( int i = 0 ; i < cnt ; i++ )
		{
			ret = ret.multiply( TEN );
		}
		return( ret );
	}
	

	
	/**
	 * Constant containing the value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>801</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * Largest possible double is around <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>308</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	static final BigInteger baseVal = calcVal( 800 );
	
	
	/**
	 * Constant containing the square of baseVal, or <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>1602</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	protected static final BigInteger finalBaseValSq = baseVal.multiply( baseVal );
	
	
	/**
	 * Defines a precision of baseVal, or one part in <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>801</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static final class LrgPrecision extends Precision<LrgPrecision>
	{
		@Override
		public BigInteger getVal()
		{
			return( baseVal );
		}
		
		@Override
		public BigInteger getValSquared()
		{
			return( finalBaseValSq );
		}
		
	}
	
	
	/**
	 * A constant defining the large precision.
	 */
	static final LrgPrecision lrgPrecision = new LrgPrecision();
	
	
	
	
	/**
	 * Builds a fixed-point value from a rational number.
	 * 
	 * @param numerator The numerator of the rational number.
	 * @param denominator The denominator of the rational number.
	 * @return The fixed-point value.
	 */
	static BigFixedPointElem<LrgPrecision> buildElem( int numerator , int denominator )
	{
		final BigInteger val = ( BigInteger.valueOf( numerator ) ).multiply( baseVal ).divide( BigInteger.valueOf( denominator ) );
		return( new BigFixedPointElem<LrgPrecision>( val , lrgPrecision ) );
	}	
	

	
	/**
	 * The number of levels to iterate when evaluating.
	 */
	static final int LEVEL = 64;
	
	
	

/**
 * Tests the ability to evaluate a Affine Invariant Metric (AIM) curve
 * generally known from CAGD using Geometric Algebra in 2-D.
 * 
 * The formula for this is from Equation 108 in "Research Issues of Geometry-Based Visual Languages and Some Solutions", Dissertation, Arizona State University, 1999.
 * 
 * Generally, the curve is constructed from the intersection of two lines:
 * 
 * The first line is defined by the point b0, and a direction that rotates from d0 to (b1-b0) as the u parameter goes from zero to one.
 * 
 * The second line is defined by the point b1, and a direction that rotates from (b1-b0) to d1 as the u parameter goes from zero to one.
 * 
 * This produces a curve that interpolates b0 at u=0, and interpolates b1 at u=1.
 * 
 * The curve is also tangent to d0 at u=0, and tangent to d1 at u=1.
 * 
 * 
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
public void testCurveAffineInvariantMetric( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
	
	final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
	
	final TestDimensionTwo td = new TestDimensionTwo();
	
	final HashSet<BigInteger> sca = new HashSet<BigInteger>();
	final HashSet<BigInteger> e0 = new HashSet<BigInteger>();
	final HashSet<BigInteger> e1 = new HashSet<BigInteger>();
	final HashSet<BigInteger> bivec = new HashSet<BigInteger>();
	
	e0.add( BigInteger.ZERO );
	e1.add( BigInteger.ONE );
	
	bivec.add( BigInteger.ZERO );
	bivec.add( BigInteger.ONE );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> b0
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	b0.setVal( e0 ,  buildElem( 33, 100 ) );
	b0.setVal( e1 ,  buildElem( 33, 100 ) );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> b1
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	b1.setVal( e0 ,  buildElem( 55 , 100 ) );
	b1.setVal( e1 ,  buildElem( 35 , 100 ) );
	

	
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> d0
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	d0.setVal( e0 ,  buildElem( 33, 100 ) );
	d0.setVal( e1 ,  buildElem( 80 , 100 ) );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> d1
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	d1.setVal( e0 ,  buildElem( 33, 100 ) );
	d1.setVal( e1 ,  buildElem( -70 , 100 ) );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> ddelta
		= b1.add( b0.negate() );
	

	
	BigFixedPointElem<LrgPrecision> d0l = (BigFixedPointElem<LrgPrecision>)( d0.totalMagnitude() );
	BigFixedPointElem<LrgPrecision> d1l = (BigFixedPointElem<LrgPrecision>)( d1.totalMagnitude() );
	BigFixedPointElem<LrgPrecision> ddeltal = (BigFixedPointElem<LrgPrecision>)( ddelta.totalMagnitude() );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> d0lm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	d0lm.setVal( sca ,  d0l.powR( buildElem( -5 , 10 ) , LEVEL , LEVEL ) );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> d1lm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	d1lm.setVal( sca ,  d1l.powR( buildElem( -5 , 10 ) , LEVEL , LEVEL ) );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> ddeltalm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	ddeltalm.setVal( sca ,  ddeltal.powR( buildElem( -5 , 10 ) , LEVEL , LEVEL ) );
	

	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> t0
		= d0.mult( d0lm );
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> t1
		= d1.mult( d1lm );
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> tdelta
		= ddelta.mult( ddeltalm );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> ln_tdelta_t0
		= ( ( tdelta.invertLeft() ).mult( t0 ) ).ln( LEVEL , LEVEL );
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> ln_tdelta_t1
		= ( ( tdelta.invertLeft() ).mult( t1 ) ).ln( LEVEL , LEVEL );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> im
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	im.setVal( bivec ,  d0l.getFac().identity() );


	
	
	
	final int MAX = 10;
	
	
	for( int cnt = 0 ; cnt <= MAX ; cnt++ )
	{
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> u
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
		u.setVal( sca ,  buildElem( cnt , MAX ) );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> one_minus_u
			= u.getFac().identity().add( u.negate() );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> termA
			= ddelta.mult( ( one_minus_u.mult( ln_tdelta_t0 ) ).exp( LEVEL ) );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> termB
			= ( im.negate().mult( u ).mult( ln_tdelta_t1 ) ).sin( LEVEL );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> termCa
			= ( u.mult( ln_tdelta_t1 ) ).add( ( one_minus_u.mult( ln_tdelta_t0 ) ).negate() );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> termC
			= ( im.negate().mult( termCa ) ).sin( LEVEL );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> p
			= ( termA.mult( termB ).mult( termC.invertLeft() ) ).add( b0 );
		
		
		System.out.print( p.getVal( e0 ).toDouble() );
		System.out.print( "  " );
		System.out.println( p.getVal( e1 ).toDouble() );

		
	}
	


	
}







/**
 * Same test as testCurveAffineInvariantMetric(), but with a slightly simpler formula.
 * 
 * 
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
public void testCurveAffineInvariantMetricRevised( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
	
	final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
	
	final TestDimensionTwo td = new TestDimensionTwo();
	
	final HashSet<BigInteger> sca = new HashSet<BigInteger>();
	final HashSet<BigInteger> e0 = new HashSet<BigInteger>();
	final HashSet<BigInteger> e1 = new HashSet<BigInteger>();
	
	e0.add( BigInteger.ZERO );
	e1.add( BigInteger.ONE );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> b0
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	b0.setVal( e0 ,  buildElem( 33, 100 ) );
	b0.setVal( e1 ,  buildElem( 33, 100 ) );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> b1
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	b1.setVal( e0 ,  buildElem( 55 , 100 ) );
	b1.setVal( e1 ,  buildElem( 35 , 100 ) );
	

	
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> d0
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	d0.setVal( e0 ,  buildElem( 33, 100 ) );
	d0.setVal( e1 ,  buildElem( 80 , 100 ) );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> d1
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	d1.setVal( e0 ,  buildElem( 33, 100 ) );
	d1.setVal( e1 ,  buildElem( -70 , 100 ) );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> ddelta
		= b1.add( b0.negate() );
	

	
	BigFixedPointElem<LrgPrecision> d0l = (BigFixedPointElem<LrgPrecision>)( d0.totalMagnitude() );
	BigFixedPointElem<LrgPrecision> d1l = (BigFixedPointElem<LrgPrecision>)( d1.totalMagnitude() );
	BigFixedPointElem<LrgPrecision> ddeltal = (BigFixedPointElem<LrgPrecision>)( ddelta.totalMagnitude() );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> d0lm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	d0lm.setVal( sca ,  d0l.powR( buildElem( -5 , 10 ) , LEVEL , LEVEL ) );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> d1lm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	d1lm.setVal( sca ,  d1l.powR( buildElem( -5 , 10 ) , LEVEL , LEVEL ) );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> ddeltalm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	ddeltalm.setVal( sca ,  ddeltal.powR( buildElem( -5 , 10 ) , LEVEL , LEVEL ) );
	

	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> t0
		= d0.mult( d0lm );
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> t1
		= d1.mult( d1lm );
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> tdelta
		= ddelta.mult( ddeltalm );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> ln_tdelta_t0
		= ( ( tdelta.invertLeft() ).mult( t0 ) ).ln( LEVEL , LEVEL );
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> ln_tdelta_t1
		= ( ( tdelta.invertLeft() ).mult( t1 ) ).ln( LEVEL , LEVEL );



	
	
	
	final int MAX = 10;
	
	
	for( int cnt = 0 ; cnt <= MAX ; cnt++ )
	{
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> u
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
		u.setVal( sca ,  buildElem( cnt , MAX ) );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> one_minus_u
			= u.getFac().identity().add( u.negate() );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> termA
			= ddelta.mult( ( one_minus_u.mult( ln_tdelta_t0 ) ).exp( LEVEL ) );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> termB
			= u.mult( ln_tdelta_t1 ).sinh( LEVEL );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> termCa
			= ( u.mult( ln_tdelta_t1 ) ).add( ( one_minus_u.mult( ln_tdelta_t0 ) ).negate() );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> termC
			= termCa.sinh( LEVEL );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> p
			= ( termA.mult( termB ).mult( termC.invertLeft() ) ).add( b0 );
		
		
		System.out.print( p.getVal( e0 ).toDouble() );
		System.out.print( "  " );
		System.out.println( p.getVal( e1 ).toDouble() );

		
	}
	


	
}





	
	
	

}


