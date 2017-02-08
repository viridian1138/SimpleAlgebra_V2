



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
 * Tests the ability to produce rotations to generate an OpenSCAD caduceus coil form.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestGenToroidalCaduceus extends TestCase {
	
	
	/**
	 * The radius of the outer shell of the coil.
	 */
	public static final DoubleElem OUTER_SHELL_RADIUS = new DoubleElem( 100.0 );
	
	
	public static final DoubleElem INNER_TORUS_RADIUS = new DoubleElem( 0.125 * ( OUTER_SHELL_RADIUS.getVal() ) );
	
	
	/**
	 * The radius of the inner shell of the coil.
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
	public static final int NUM_WINDINGS = 10;
	
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
	public static final int MAX_ROT_DIVISIONS = 20;
	
	
	
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
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> genSpinA(
			final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac ,
			final DoubleElem rval )
	{
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			spin = fac.zero();
		final HashSet<BigInteger> xv = new HashSet<BigInteger>();
		xv.add( BigInteger.ZERO );
		xv.add( BigInteger.ONE );
		spin.setVal( xv ,  rval );
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			vec = spin.exp( 20 );
		return( vec );
	}
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> genSpinA2(
			final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac ,
			final DoubleElem rval )
	{
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			spin = fac.zero();
		final HashSet<BigInteger> xv = new HashSet<BigInteger>();
		xv.add( BigInteger.ZERO );
		xv.add( BigInteger.ONE );
		spin.setVal( xv ,  rval );
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			vec = spin.exp( 20 );
		spin.setVal( xv ,  new DoubleElem( 1.0 ) );
		vec = vec.mult( spin );
		return( vec );
	}
	
	
	
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
	 * Tests the ability to produce rotations to generate an OpenSCAD caduceus coil form.
	 */
	public void testGenToroidalCaduceus() throws Throwable
	{
		
		final DoubleElemFactory d0 = new DoubleElemFactory();
		final TestDimensionThree td = new TestDimensionThree();
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac =
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(d0, td, ord);
		
		
		final FileOutputStream fo = new FileOutputStream( "toroidal_caduceus.scad" );
		
		final PrintStream ps = new PrintStream( fo );
		
		
		
		ps.println( "" );
		ps.println( "difference() { " );
		ps.println( "{" );
		ps.println( "union()" );
		ps.println( "{" );
		
		
		for( int j = 0 ; j < MAX_ROT_DIVISIONS ; j++ )
		{
			final DoubleElem rvalA = new DoubleElem( 2.0 * Math.PI * j / MAX_ROT_DIVISIONS );
			final DoubleElem rvalB = new DoubleElem( 2.0 * Math.PI * ( j + 1 ) / MAX_ROT_DIVISIONS );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				svA = genSpinA( fac , rvalA );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				svB = genSpinA( fac , rvalB );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				svAdot = genSpinA2( fac , rvalA );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				svBdot = genSpinA2( fac , rvalB );
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
						tvA = svA.mult( xvU );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvAdot = svAdot.mult( xvU );
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
						tvB = svB.mult( xvU );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvBdot = svBdot.mult( xvU );
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
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				svA = genSpinA( fac , rvalA );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				svB = genSpinA( fac , rvalB );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				svAdot = genSpinA2( fac , rvalA );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				svBdot = genSpinA2( fac , rvalB );
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
						tvA = svA.mult( xvU );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvAdot = svAdot.mult( xvU );
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
						tvB = svB.mult( xvU );
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
						tvBdot = svBdot.mult( xvU );
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
				final DoubleElem rvalA = new DoubleElem( 2.0 * Math.PI * ( (double) j ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
				final DoubleElem rvalB =  new DoubleElem( 2.0 * Math.PI * ( (double) ( j + 1 ) ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
				
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					svA = genSpinA( fac , rvalA );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					svB = genSpinA( fac , rvalB );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					svAdot = genSpinA2( fac , rvalA );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					svBdot = genSpinA2( fac , rvalB );
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					xvRad = genXval( fac , OUTER_SHELL_RADIUS  );
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					xvU = genXval( fac , new DoubleElem( 1.0 ) );
				
				
				final DoubleElem rvalC0 = new DoubleElem( 2.0 * Math.PI * j / MAX_ROT_DIVISIONS );
				final DoubleElem rvalC1 = new DoubleElem( 2.0 * Math.PI * ( j + 1 ) / MAX_ROT_DIVISIONS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvA = svA.mult( xvU );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvB = svB.mult( xvU );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvAdot = svAdot.mult( xvU );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvBdot = svBdot.mult( xvU );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtA = tvA.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtB = tvB.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvC0 = genSpinB( tvAdot , fac , rvalC0 ).mult( tvA );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvC1 = genSpinB( tvBdot , fac , rvalC1 ).mult( tvB );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtC0 = tvC0.mult( genVal( fac , INNER_TORUS_RADIUS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtC1 = tvC1.mult( genVal( fac , INNER_TORUS_RADIUS ) );
				
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt0 = strtA.add( strtC0 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt1 = strtB.add( strtC1 );
				
				ps.println( "hull() { " );
				
				ps.print( "translate( " ); writePoint( false , pt0 , ps ); ps.println( " ) sphere( r=" + ( WINDING_GROOVE_RADIUS.getVal() ) + " ); " );
				ps.print( "translate( " ); writePoint( false , pt1 , ps ); ps.println( " ) sphere( r=" + ( WINDING_GROOVE_RADIUS.getVal() ) + " ); " );
				
				ps.println( "}" ); // Hull
			}
		}
		
		
		
		
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				yv = genYval( fac , OUTER_SHELL_RADIUS  );
			
			for( int j = 0 ; j < MAX_ROT_DIVISIONS * NUM_WINDINGS ; j++ )
			{
				final double u0 = ( (double) j ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS );
				final double u1 = ( (double) ( j + 1 ) ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS );
				final DoubleElem rvalA = new DoubleElem( 2.0 * Math.PI * ( (double) j ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
				final DoubleElem rvalB =  new DoubleElem( 2.0 * Math.PI * ( (double) ( j + 1 ) ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS ) );
				
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					svA = genSpinA( fac , rvalA );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					svB = genSpinA( fac , rvalB );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					svAdot = genSpinA2( fac , rvalA );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					svBdot = genSpinA2( fac , rvalB );
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					xvRad = genXval( fac , OUTER_SHELL_RADIUS  );
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					xvU = genXval( fac , new DoubleElem( 1.0 ) );
				
				
				final DoubleElem rvalD0 = new DoubleElem( -2.0 * Math.PI * j / MAX_ROT_DIVISIONS );
				final DoubleElem rvalD1 = new DoubleElem( -2.0 * Math.PI * ( j + 1 ) / MAX_ROT_DIVISIONS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvA = svA.mult( xvU );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvB = svB.mult( xvU );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvAdot = svAdot.mult( xvU );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvBdot = svBdot.mult( xvU );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtA = tvA.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtB = tvB.mult( genVal( fac , OUTER_SHELL_RADIUS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvD0 = genSpinB( tvAdot , fac , rvalD0 ).mult( tvA );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					tvD1 = genSpinB( tvBdot , fac , rvalD1 ).mult( tvB );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtD0 = tvD0.mult( genVal( fac , INNER_TORUS_RADIUS ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strtD1 = tvD1.mult( genVal( fac , INNER_TORUS_RADIUS ) );
				
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt0 = strtA.add( strtD0 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt1 = strtB.add( strtD1 );
				
				ps.println( "hull() { " );
				
				ps.print( "translate( " ); writePoint( false , pt0 , ps ); ps.println( " ) sphere( r=" + ( WINDING_GROOVE_RADIUS.getVal() ) + " ); " );
				ps.print( "translate( " ); writePoint( false , pt1 , ps ); ps.println( " ) sphere( r=" + ( WINDING_GROOVE_RADIUS.getVal() ) + " ); " );
				
				ps.println( "}" ); // Hull
			}
		}
			
		
		
		
		ps.println( "}" );
		ps.println( "}" );
		
		ps.println( "}" ); // DiffA
		
		
		
		ps.close();
		
	}
	
	
	
}



