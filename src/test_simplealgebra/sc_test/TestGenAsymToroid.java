



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




package test_simplealgebra.sc_test;

import java.io.FileOutputStream;
import java.io.PrintStream;
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
import test_simplealgebra.TestDimensionThree;


/**
 * Tests the ability to produce rotations to generate an OpenSCAD form for an asymmetric toroid.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestGenAsymToroid extends TestCase {
	
	
	/**
	 * The radius of the larger circle of the toroid.
	 */
	public static final DoubleElem OUTER_SHELL_RADIUS = new DoubleElem( 50.0 );
	
	/**
	 * The radius of the smaller circle of the toroid.
	 */
	public static final DoubleElem INNER_TORUS_RADIUS = new DoubleElem( 0.125 * ( OUTER_SHELL_RADIUS.getVal() ) );
	
	/**
	 * The radius of the hollow inner shell of the coil.
	 */
	public static final DoubleElem INNER_SHELL_RADIUS = new DoubleElem( 0.04 * ( OUTER_SHELL_RADIUS.getVal() ) );

	/**
	 * The radius of small spheres used to build convex hulls.
	 */
	public static final DoubleElem HULL_SPHERE_RADIUS = new DoubleElem( 0.0001 );
	
	/**
	 * The radius of the winding pulled from the outside of the coil.
	 */
	public static final DoubleElem WINDING_GROOVE_RADIUS = new DoubleElem( 1.5 );
	
	/**
	 * The number of windings for the coil.
	 */
	public static final int NUM_WINDINGS = 10; // 20;
	
	/**
	 * The constant PI.
	 */
	public static final DoubleElem PI = new DoubleElem( Math.PI );
	
	/**
	 * The constant 2.0.
	 */
	public static final DoubleElem TWO = new DoubleElem( Math.PI );
	
	/**
	 * The number of vertices around which to rotate 360 degrees.
	 */
	public static final int MAX_ROT_DIVISIONS = 20; // 40;
	
	/**
	 * The radius the making the windings partially recessed.
	 */
	public static final DoubleElem INNER_TORUS_RADIUS_RECESSED = new DoubleElem( INNER_TORUS_RADIUS.getVal() - 0.4 * ( WINDING_GROOVE_RADIUS.getVal() ) );
	
	
	
	
	/**
	 * Returns the position of the curve center point in a normalized device coordinate system.
	 * @param fac Factory for creating multivectors.
	 * @param ua The parameter from zero to one around the shape.
	 * @return The position.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
		genCenterCurvePoint( GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac , 
				double ua )
	{
		while( ua > 1.0 )
		{
			ua -= 1.0;
		}
		
		while( ua < 0.0 )
		{
			ua += 1.0;
		}
		
		final DoubleElem rval = new DoubleElem( 2.0 * Math.PI * ua );
		
		double ub = ua < 0.5 ? 2.0 * ua : 1.0 - ( 2.0 * ( ua - 0.5 ) );
		
		double b30 = 0.0;
		
		if( ua < 0.5 )
		{
			final double b0 = 0.8;
			final double b1 = 0.7;
			final double b2 = 1.3;
			final double b3 = 0.7;
		
			final double b10 = ( 1 - ub ) * b0 + ub * b1;
			final double b11 = ( 1 - ub ) * b1 + ub * b2;
			final double b12 = ( 1 - ub ) * b2 + ub * b3;
		
			final double b20 = ( 1 - ub ) * b10 + ub * b11;
			final double b21 = ( 1 - ub ) * b11 + ub * b12;
		
			b30 = ( 1 - ub ) * b20 + ub * b21;
		}
		else
		{
			final double b0 = 0.8;
			final double b1 = 0.7;
			final double b2 = 0.8;
			final double b3 = 0.7;
		
			final double b10 = ( 1 - ub ) * b0 + ub * b1;
			final double b11 = ( 1 - ub ) * b1 + ub * b2;
			final double b12 = ( 1 - ub ) * b2 + ub * b3;
		
			final double b20 = ( 1 - ub ) * b10 + ub * b11;
			final double b21 = ( 1 - ub ) * b11 + ub * b12;
		
			b30 = ( 1 - ub ) * b20 + ub * b21;
		}
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			spin = fac.zero();
		final HashSet<BigInteger> xv = new HashSet<BigInteger>();
		xv.add( BigInteger.ZERO );
		xv.add( BigInteger.ONE );
		spin.setVal( xv ,  rval );
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			vec = spin.exp( 20 );
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			xvU = genXval( fac , new DoubleElem( b30 ) );
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			tvA = vec.mult( xvU );
		
		return( tvA );		
	}
	
	
	
	/**
	 * Returns the numerical derivative of the curve center point.
	 * @param fac Factory for creating multivectors.
	 * @param ua The parameter from zero to one around the shape.
	 * @param The delta for the numerical derivative.
	 * @return The derivative.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
		genCenterCurveDerivative( GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac , 
				double ua , double delta )
	{
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			pl = genCenterCurvePoint( fac , ua + delta );
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			ne = genCenterCurvePoint( fac , ua - delta );
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			xv = genVal( fac , new DoubleElem( 1.0 / ( 2.0 * delta ) ) );
		return( ( pl.add( ne.negate() ) ).mult( xv ) );
	}
	
	
	/**
	 * Returns the numerical unit derivative of the curve center point.
	 * @param fac Factory for creating multivectors.
	 * @param ua The parameter from zero to one around the shape.
	 * @param The delta for the numerical derivative.
	 * @return The derivative.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
		genUnitCenterCurveDerivative( GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac , 
			double ua , double delta )
	{
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			dv = genCenterCurveDerivative( fac , ua , delta );
		final DoubleElem d = (DoubleElem)( dv.totalMagnitude() );
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			xv = genVal( fac , new DoubleElem( 1.0 / ( Math.sqrt( d.getVal() ) ) ) );
		return( dv.mult( xv ) );
	}
	
	
	
	/**
	 * Writes a point in OpenSCAD format.
	 * 
	 * @param prev Whether there was a previous point requiring a separating comma.
	 * @param v The coordinates of the point to be written.
	 * @param ps The stream to which to write the point.
	 */
	protected void writePoint( boolean prev , 
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> v ,
			PrintStream ps )
	{
		
		if( prev )
		{
			ps.print( " , " );
		}
		
		ps.print( " [ " );
		
		{
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			
			ps.print( v.getVal( hs ).getVal() );
		}
		
		ps.print( " , " );
		
		{
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			
			ps.print( v.getVal( hs ).getVal() );
		}
		
		ps.print( " , " );
		
		{
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.valueOf( 2 ) );
			
			ps.print( v.getVal( hs ).getVal() );
		}
		
		ps.println( " ] " );
		
	}
	
	
	
	/**
	 * Generates a spinor.
	 * @param tvDot Unit vector perpendicular to the spin bivector.
	 * @param fac Multivector factory.
	 * @param rvalD The radian angle of the spin.
	 * @return The generated spinor.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> genSpinB(
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> tvDot ,
			final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac ,
			final DoubleElem rvalD )
	{
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			spin = tvDot.mult( genIm( fac , rvalD ) );
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			vec = spin.exp( 20 );
		return( vec );
	}
	
	
	
	/**
	 * Generates a vector along the X-Axis.
	 * @param fac Multivector factory.
	 * @param rval The value of the vector.
	 * @return The generated vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> genXval(
			final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac ,
			final DoubleElem rval )
	{
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			cv = fac.zero();
		final HashSet<BigInteger> xv = new HashSet<BigInteger>();
		xv.add( BigInteger.ZERO );
		cv.setVal( xv ,  rval );
		return( cv );
	}
	
	
	
	/**
	 * Generates a vector along the Y-Axis.
	 * @param fac Multivector factory.
	 * @param rval The value of the vector.
	 * @return The generated vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> genYval(
			final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac ,
			final DoubleElem rval )
	{
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			cv = fac.zero();
		final HashSet<BigInteger> xv = new HashSet<BigInteger>();
		xv.add( BigInteger.ONE );
		cv.setVal( xv ,  rval );
		return( cv );
	}
	
	
	/**
	 * Generates a scalar.
	 * @param fac Multivector factory.
	 * @param rval The value of the scalar.
	 * @return The generated scalar.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> genVal(
			final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac ,
			final DoubleElem rval )
	{
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			cv = fac.zero();
		final HashSet<BigInteger> xv = new HashSet<BigInteger>();
		cv.setVal( xv ,  rval );
		return( cv );
	}
	
	
	
	/**
	 * Generates a trivector.
	 * @param fac The multivector factory.
	 * @param rval The value of the trivector.
	 * @return The generated trivector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> genIm(
			final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac ,
			final DoubleElem rval )
	{
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			cv = fac.zero();
		final HashSet<BigInteger> xv = new HashSet<BigInteger>();
		xv.add( BigInteger.ZERO );
		xv.add( BigInteger.ONE );
		xv.add( BigInteger.valueOf( 2 ) );
		cv.setVal( xv ,  rval );
		return( cv );
	}
	

	
	
	/**
	 * Tests the ability to produce rotations to generate an OpenSCAD form for an asymmetric toroid.
	 */
	public void testGenToroidalCaduceus() throws Throwable
	{
		
		final DoubleElemFactory d0 = new DoubleElemFactory();
		final TestDimensionThree td = new TestDimensionThree();
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac =
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(d0, td, ord);
		
		
		final FileOutputStream fo = new FileOutputStream( "asym_toroid.scad" );
		
		final PrintStream ps = new PrintStream( fo );
		
		
		double[] parms = new double[ MAX_ROT_DIVISIONS * NUM_WINDINGS + 1 ];
		
		
		double dsum = 0.0;
		for( int j = 0 ; j < MAX_ROT_DIVISIONS * NUM_WINDINGS ; j++ )
		{
			final double u0 = ( (double) j ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS );
			final double u1 = ( (double) ( j + 1 ) ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				tvA = this.genCenterCurvePoint( fac , u0 );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				tvB =  this.genCenterCurvePoint( fac , u1 );
			final DoubleElem db = (DoubleElem)( ( tvA.add( tvB.negate() ) ).totalMagnitude() );
			parms[ j ] = dsum;
			dsum += db.getVal();
		}
		parms[ MAX_ROT_DIVISIONS * NUM_WINDINGS ] = dsum;
		
		
		
		ps.println( "" );
		ps.println( "difference() { " );
		ps.println( "{" );
		ps.println( "union()" );
		ps.println( "{" );
		
		
		for( int j = 0 ; j < MAX_ROT_DIVISIONS ; j++ )
		{
			final DoubleElem rvalA = new DoubleElem( 2.0 * Math.PI * j / MAX_ROT_DIVISIONS );
			final DoubleElem rvalB = new DoubleElem( 2.0 * Math.PI * ( j + 1 ) / MAX_ROT_DIVISIONS );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				xvRad = genXval( fac , OUTER_SHELL_RADIUS  );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				xvU = genXval( fac , new DoubleElem( 1.0 ) );
			ps.println( "hull() { " );
			{
				
	
				for( int i = 0 ; i < MAX_ROT_DIVISIONS ; i++ )
				{
					final DoubleElem rvalC = new DoubleElem( 2.0 * Math.PI * i / MAX_ROT_DIVISIONS );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvA = this.genCenterCurvePoint( fac , ( (double) j ) / MAX_ROT_DIVISIONS );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvAdot = this.genUnitCenterCurveDerivative(fac, ( (double) j ) / MAX_ROT_DIVISIONS , 1.0  / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						strtA = tvA.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvC = genSpinB( tvAdot , fac , rvalC ).mult( tvA );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						strtC = tvC.mult( genVal( fac , INNER_TORUS_RADIUS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						pt = strtA.add( strtC );
					ps.print( "translate( " ); writePoint( false , pt , ps ); ps.println( " ) sphere( r=" + ( HULL_SPHERE_RADIUS.getVal() ) + " ); " );
				}
			
			}
		
		
		
			{
			
			
				for( int i = 0 ; i < MAX_ROT_DIVISIONS ; i++ )
				{
					final DoubleElem rvalD = new DoubleElem( 2.0 * Math.PI * i / MAX_ROT_DIVISIONS );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvB = this.genCenterCurvePoint( fac , ( (double) ( j + 1 ) ) / MAX_ROT_DIVISIONS );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvBdot = this.genUnitCenterCurveDerivative(fac, ( (double) ( j + 1 ) ) / MAX_ROT_DIVISIONS , 1.0  / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						strtB = tvB.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvD = genSpinB( tvBdot , fac , rvalD ).mult( tvB );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						strtD = tvD.mult( genVal( fac , INNER_TORUS_RADIUS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						pt = strtB.add( strtD );
					ps.print( "translate( " ); writePoint( false , pt , ps ); ps.println( " ) sphere( r=" + ( HULL_SPHERE_RADIUS.getVal() ) + " ); " );
				}
			
			
			}
			ps.println( "}" ); // Hull
		}
		
		
		
		
		ps.println( "}" );
		ps.println( "}" );
		ps.println( "{" );
		ps.println( "union()" );
		ps.println( "{" );
		
		
		for( int j = 0 ; j < MAX_ROT_DIVISIONS ; j++ )
		{
			final DoubleElem rvalA = new DoubleElem( 2.0 * Math.PI * j / MAX_ROT_DIVISIONS );
			final DoubleElem rvalB = new DoubleElem( 2.0 * Math.PI * ( j + 1 ) / MAX_ROT_DIVISIONS );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				xvRad = genXval( fac , OUTER_SHELL_RADIUS  );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				xvU = genXval( fac , new DoubleElem( 1.0 ) );
			ps.println( "hull() { " );
			{
				
	
				for( int i = 0 ; i < MAX_ROT_DIVISIONS ; i++ )
				{
					final DoubleElem rvalC = new DoubleElem( 2.0 * Math.PI * i / MAX_ROT_DIVISIONS );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvA = this.genCenterCurvePoint( fac , ( (double) j ) / MAX_ROT_DIVISIONS );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvAdot = this.genUnitCenterCurveDerivative( fac , ( (double) j ) / MAX_ROT_DIVISIONS , 1.0  / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						strtA = tvA.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvC = genSpinB( tvAdot , fac , rvalC ).mult( tvA );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						strtC = tvC.mult( genVal( fac , INNER_SHELL_RADIUS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						pt = strtA.add( strtC );
					ps.print( "translate( " ); writePoint( false , pt , ps ); ps.println( " ) sphere( r=" + ( HULL_SPHERE_RADIUS.getVal() ) + " ); " );
				}
			
			}
		
		
		
			{
			
			
				for( int i = 0 ; i < MAX_ROT_DIVISIONS ; i++ )
				{
					final DoubleElem rvalD = new DoubleElem( 2.0 * Math.PI * i / MAX_ROT_DIVISIONS );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvB = this.genCenterCurvePoint( fac , ( (double) ( j + 1 ) ) / MAX_ROT_DIVISIONS );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvBdot = this.genUnitCenterCurveDerivative( fac , ( (double) ( j + 1 ) ) / MAX_ROT_DIVISIONS , 1.0  / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						strtB = tvB.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvD = genSpinB( tvBdot , fac , rvalD ).mult( tvB );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						strtD = tvD.mult( genVal( fac , INNER_SHELL_RADIUS ) );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						pt = strtB.add( strtD );
					ps.print( "translate( " ); writePoint( false , pt , ps ); ps.println( " ) sphere( r=" + ( HULL_SPHERE_RADIUS.getVal() ) + " ); " );
				}
			
			
			}
			ps.println( "}" ); // Hull
		}
		
		
		
		
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				yv = genYval( fac , OUTER_SHELL_RADIUS  );
			
			for( int j = 0 ; j < MAX_ROT_DIVISIONS * NUM_WINDINGS ; j++ )
			{
				final double u0 = ( (double) j ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS );
				final double u1 = ( (double) ( j + 1 ) ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS );
				
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					xvRad = genXval( fac , OUTER_SHELL_RADIUS  );
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					xvU = genXval( fac , new DoubleElem( 1.0 ) );
				
				
				final DoubleElem rvalC0 = new DoubleElem( 2.0 * Math.PI * ( parms[ j ] / dsum ) *  NUM_WINDINGS );
				final DoubleElem rvalC1 = new DoubleElem( 2.0 * Math.PI * ( parms[ j + 1 ] / dsum ) * NUM_WINDINGS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvA = this.genCenterCurvePoint( fac , u0 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvB =  this.genCenterCurvePoint( fac , u1 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvAdot = this.genUnitCenterCurveDerivative( fac , u0 , 1.0  / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvBdot = this.genUnitCenterCurveDerivative( fac , u1 , 1.0  / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtA = tvA.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtB = tvB.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvC0 = genSpinB( tvAdot , fac , rvalC0 ).mult( tvA );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvC1 = genSpinB( tvBdot , fac , rvalC1 ).mult( tvB );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtC0 = tvC0.mult( genVal( fac , INNER_TORUS_RADIUS_RECESSED ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtC1 = tvC1.mult( genVal( fac , INNER_TORUS_RADIUS_RECESSED ) );
				
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt0 = strtA.add( strtC0 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt1 = strtB.add( strtC1 );
				
				ps.println( "hull() { " );
				
				ps.print( "translate( " ); writePoint( false , pt0 , ps ); ps.println( " ) sphere( r=" + ( WINDING_GROOVE_RADIUS.getVal() ) + " ); " );
				ps.print( "translate( " ); writePoint( false , pt1 , ps ); ps.println( " ) sphere( r=" + ( WINDING_GROOVE_RADIUS.getVal() ) + " ); " );
				
				ps.println( "}" ); 
			}
		}
		
		
		
		
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				yv = genYval( fac , OUTER_SHELL_RADIUS  );
			
			for( int j = 0 ; j < MAX_ROT_DIVISIONS * NUM_WINDINGS ; j++ )
			{
				final double u0 = ( (double) j ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS );
				final double u1 = ( (double) ( j + 1 ) ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS );
				
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					xvRad = genXval( fac , OUTER_SHELL_RADIUS  );
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					xvU = genXval( fac , new DoubleElem( 1.0 ) );
				
				
				final DoubleElem rvalC0 = new DoubleElem( - 2.0 * Math.PI * ( parms[ j ] / dsum ) *  NUM_WINDINGS );
				final DoubleElem rvalC1 = new DoubleElem( - 2.0 * Math.PI * ( parms[ j + 1 ] / dsum ) * NUM_WINDINGS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvA = this.genCenterCurvePoint( fac , u0 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvB =  this.genCenterCurvePoint( fac , u1 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvAdot = this.genUnitCenterCurveDerivative( fac , u0 , 1.0  / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvBdot = this.genUnitCenterCurveDerivative( fac , u1 , 1.0  / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtA = tvA.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtB = tvB.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvC0 = genSpinB( tvAdot , fac , rvalC0 ).mult( tvA );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvC1 = genSpinB( tvBdot , fac , rvalC1 ).mult( tvB );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtC0 = tvC0.mult( genVal( fac , INNER_TORUS_RADIUS_RECESSED ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtC1 = tvC1.mult( genVal( fac , INNER_TORUS_RADIUS_RECESSED ) );
				
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt0 = strtA.add( strtC0 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt1 = strtB.add( strtC1 );
				
				ps.println( "hull() { " );
				
				ps.print( "translate( " ); writePoint( false , pt0 , ps ); ps.println( " ) sphere( r=" + ( WINDING_GROOVE_RADIUS.getVal() ) + " ); " );
				ps.print( "translate( " ); writePoint( false , pt1 , ps ); ps.println( " ) sphere( r=" + ( WINDING_GROOVE_RADIUS.getVal() ) + " ); " );
				
				ps.println( "}" ); 
			}
		}
		
		
		
		ps.println( "}" );
		ps.println( "}" );
		
		ps.println( "}" ); // DiffA
		
		
		
		ps.close();
		
	}
	
	
	
}



