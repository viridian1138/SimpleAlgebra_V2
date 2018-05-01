






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
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;



/**
 * Tests the ability to evaluate a Affine Invariant Metric (AIM) curve
 * generally known from CAGD using Geometric Algebra in 2-D.
 * 
 * @author tgreen
 *
 */
public class TestCurveAffineInvariantMetric extends TestCase {




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
 * 
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
public void testCurveAffineInvariantMetric( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
	
	final DoubleElemFactory fac = new DoubleElemFactory();
	
	final TestDimensionTwo td = new TestDimensionTwo();
	
	final HashSet<BigInteger> sca = new HashSet<BigInteger>();
	final HashSet<BigInteger> e0 = new HashSet<BigInteger>();
	final HashSet<BigInteger> e1 = new HashSet<BigInteger>();
	final HashSet<BigInteger> bivec = new HashSet<BigInteger>();
	
	e0.add( BigInteger.ZERO );
	e1.add( BigInteger.ONE );
	
	bivec.add( BigInteger.ZERO );
	bivec.add( BigInteger.ONE );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> b0
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	b0.setVal( e0 ,  new DoubleElem( 0.33 ) );
	b0.setVal( e1 ,  new DoubleElem( 0.33 ) );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> b1
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	b1.setVal( e0 ,  new DoubleElem( 0.55 ) );
	b1.setVal( e1 ,  new DoubleElem( 0.35 ) );
	

	
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> d0
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	d0.setVal( e0 ,  new DoubleElem( 0.33 ) );
	d0.setVal( e1 ,  new DoubleElem( 0.80 ) );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> d1
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	d1.setVal( e0 ,  new DoubleElem( 0.33 ) );
	d1.setVal( e1 ,  new DoubleElem( -0.70 ) );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> ddelta
		= b1.add( b0.negate() );
	

	
	DoubleElem d0l = (DoubleElem)( d0.totalMagnitude() );
	DoubleElem d1l = (DoubleElem)( d1.totalMagnitude() );
	DoubleElem ddeltal = (DoubleElem)( ddelta.totalMagnitude() );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> d0lm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	d0lm.setVal( sca ,  d0l.powR( new DoubleElem( -0.5 ), 20 , 20 ) );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> d1lm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	d1lm.setVal( sca ,  d1l.powR( new DoubleElem( -0.5 ), 20 , 20 ) );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> ddeltalm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	ddeltalm.setVal( sca ,  ddeltal.powR( new DoubleElem( -0.5 ), 20 , 20 ) );
	

	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> t0
		= d0.mult( d0lm );
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> t1
		= d1.mult( d1lm );
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> tdelta
		= ddelta.mult( ddeltalm );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> ln_tdelta_t0
		= ( ( tdelta.invertLeft() ).mult( t0 ) ).ln( 20 , 20 );
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> ln_tdelta_t1
		= ( ( tdelta.invertLeft() ).mult( t1 ) ).ln( 20 , 20 );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> im
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	im.setVal( bivec ,  d0l.getFac().identity() );


	
	
	
	final int MAX = 10;
	
	
	for( int cnt = 0 ; cnt <= MAX ; cnt++ )
	{
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> u
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
		u.setVal( sca ,  new DoubleElem( (double) cnt / MAX ) );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> one_minus_u
			= u.getFac().identity().add( u.negate() );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> termA
			= ddelta.mult( ( one_minus_u.mult( ln_tdelta_t0 ) ).exp( 20 ) );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> termB
			= ( im.negate().mult( u ).mult( ln_tdelta_t1 ) ).sin( 20 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> termCa
			= ( u.mult( ln_tdelta_t1 ) ).add( ( one_minus_u.mult( ln_tdelta_t0 ) ).negate() );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> termC
			= ( im.negate().mult( termCa ) ).sin( 20 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p
			= ( termA.mult( termB ).mult( termC.invertLeft() ) ).add( b0 );
		
		
		System.out.print( p.getVal( e0 ).getVal() );
		System.out.print( "  " );
		System.out.println( p.getVal( e1 ).getVal() );

		
	}
	


	
}




	
	
	

}


