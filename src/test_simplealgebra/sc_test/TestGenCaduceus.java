



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
public class TestGenCaduceus extends TestCase {
	
	
	/**
	 * The length pf the coil.
	 */
	public static final DoubleElem SHELL_LENGTH = new DoubleElem( 100.0 );
	
	/**
	 * The radius of the outer shell of the coil.
	 */
	public static final DoubleElem OUTER_SHELL_RADIUS = new DoubleElem( 0.25 * ( SHELL_LENGTH.getVal() ) );
	
	/**
	 * The radius of the inner shell of the coil.
	 */
	public static final DoubleElem INNER_SHELL_RADIUS = new DoubleElem( 0.20 * ( SHELL_LENGTH.getVal() ) );

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
	public static final int MAX_ROT_DIVISIONS = 360;
	
	
	
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
			
			ps.print( v.get( hs ).getVal() );
		}
		
		ps.print( " , " );
		
		{
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			
			ps.print( v.get( hs ).getVal() );
		}
		
		ps.print( " , " );
		
		{
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.valueOf( 2 ) );
			
			ps.print( v.get( hs ).getVal() );
		}
		
		ps.println( " ] " );
		
	}
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> genSpin(
			final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac ,
			final DoubleElem rval )
	{
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			spin = fac.zero();
		final HashSet<BigInteger> xv = new HashSet<BigInteger>();
		xv.add( BigInteger.ONE );
		xv.add( BigInteger.valueOf( 2 ) );
		spin.setVal( xv ,  rval );
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
	

	
	
	/**
	 * Tests the ability to produce rotations to generate an OpenSCAD caduceus coil form.
	 */
	public void testGenCaduceus() throws Throwable
	{
		
		final DoubleElemFactory d0 = new DoubleElemFactory();
		final TestDimensionThree td = new TestDimensionThree();
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac =
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(d0, td, ord);
		
		
		final FileOutputStream fo = new FileOutputStream( "caduceus.scad" );
		
		final PrintStream ps = new PrintStream( fo );
		
		
		
		ps.println( "" );
		ps.println( "difference() { " );
		ps.println( "{" );
		
		
		ps.println( "hull() { " );
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				strt = genXval( fac , SHELL_LENGTH.divideBy( 2 ) );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				yv = genYval( fac , OUTER_SHELL_RADIUS  );
			
			
			for( int i = 0 ; i < MAX_ROT_DIVISIONS ; i++ )
			{
				final DoubleElem rval = new DoubleElem( 2.0 * Math.PI * i / MAX_ROT_DIVISIONS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					sv = genSpin( fac , rval );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt = strt.add( sv.mult( yv ) );
				ps.print( "translate( " ); writePoint( false , pt , ps ); ps.println( " ) sphere( r=" + ( HULL_SPHERE_RADIUS.getVal() ) + " ); " );
			}
			
		}
		
		
		
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				strt = genXval( fac , SHELL_LENGTH.divideBy( 2 ).negate() );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				yv = genYval( fac , OUTER_SHELL_RADIUS  );
			
			
			for( int i = 0 ; i < MAX_ROT_DIVISIONS ; i++ )
			{
				final DoubleElem rval = new DoubleElem( 2.0 * Math.PI * i / MAX_ROT_DIVISIONS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					sv = genSpin( fac , rval );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt = strt.add( sv.mult( yv ) );
				ps.print( "translate( " ); writePoint( false , pt , ps ); ps.println( " ) sphere( r=" + ( HULL_SPHERE_RADIUS.getVal() ) + " ); " );
			}
			
			
		}
		ps.println( "}" ); // Hull
		
		
		
		
		ps.println( "}" );
		ps.println( "{" );
		
		
		ps.println( "hull() { " );
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				strt = genXval( fac , SHELL_LENGTH );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				yv = genYval( fac , INNER_SHELL_RADIUS  );
			
			
			for( int i = 0 ; i < MAX_ROT_DIVISIONS ; i++ )
			{
				final DoubleElem rval = new DoubleElem( 2.0 * Math.PI * i / MAX_ROT_DIVISIONS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					sv = genSpin( fac , rval );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt = strt.add( sv.mult( yv ) );
				ps.print( "translate( " ); writePoint( false , pt , ps ); ps.println( " ) sphere( r=" + ( HULL_SPHERE_RADIUS.getVal() ) + " ); " );
			}
			
		}
		
		
		
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				strt = genXval( fac , SHELL_LENGTH.negate() );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				yv = genYval( fac , INNER_SHELL_RADIUS  );
			
			
			for( int i = 0 ; i < MAX_ROT_DIVISIONS ; i++ )
			{
				final DoubleElem rval = new DoubleElem( 2.0 * Math.PI * i / MAX_ROT_DIVISIONS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					sv = genSpin( fac , rval );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt = strt.add( sv.mult( yv ) );
				ps.print( "translate( " ); writePoint( false , pt , ps ); ps.println( " ) sphere( r=" + ( HULL_SPHERE_RADIUS.getVal() ) + " ); " );
			}
			
			
		}
		ps.println( "}" ); // Hull
		
		
		
		
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				yv = genYval( fac , OUTER_SHELL_RADIUS  );
			
			for( int j = 0 ; j < MAX_ROT_DIVISIONS * NUM_WINDINGS ; j++ )
			{
				final double u0 = ( (double) j ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS );
				final double u1 = ( (double) ( j + 1 ) ) / ( MAX_ROT_DIVISIONS * NUM_WINDINGS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strt00 = genXval( fac , new DoubleElem( ( SHELL_LENGTH.getVal() ) * ( ( 1.0 - u0 ) * ( -1 ) + u0 * ( 1 ) ) ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strt01 = genXval( fac , new DoubleElem( ( SHELL_LENGTH.getVal() ) * ( ( 1.0 - u1 ) * ( -1 ) + u1 * ( 1 ) ) ) );
				final DoubleElem rval0 = new DoubleElem( 2.0 * Math.PI * j / MAX_ROT_DIVISIONS );
				final DoubleElem rval1 = new DoubleElem( 2.0 * Math.PI * ( j + 1 ) / MAX_ROT_DIVISIONS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					sv0 = genSpin( fac , rval0 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					sv1 = genSpin( fac , rval1 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt0 = strt00.add( sv0.mult( yv ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt1 = strt01.add( sv1.mult( yv ) );
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
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strt00 = genXval( fac , new DoubleElem( ( SHELL_LENGTH.getVal() ) * ( ( 1.0 - u0 ) * ( -1 ) + u0 * ( 1 ) ) ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					strt01 = genXval( fac , new DoubleElem( ( SHELL_LENGTH.getVal() ) * ( ( 1.0 - u1 ) * ( -1 ) + u1 * ( 1 ) ) ) );
				final DoubleElem rval0 = new DoubleElem( - 2.0 * Math.PI * j / MAX_ROT_DIVISIONS );
				final DoubleElem rval1 = new DoubleElem( - 2.0 * Math.PI * ( j + 1 ) / MAX_ROT_DIVISIONS );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					sv0 = genSpin( fac , rval0 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					sv1 = genSpin( fac , rval1 );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt0 = strt00.add( sv0.mult( yv ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					pt1 = strt01.add( sv1.mult( yv ) );
				ps.println( "hull() { " );
				
				ps.print( "translate( " ); writePoint( false , pt0 , ps ); ps.println( " ) sphere( r=" + ( WINDING_GROOVE_RADIUS.getVal() ) + " ); " );
				ps.print( "translate( " ); writePoint( false , pt1 , ps ); ps.println( " ) sphere( r=" + ( WINDING_GROOVE_RADIUS.getVal() ) + " ); " );
				
				ps.println( "}" ); // Hull
			}
		}
			
		
		
		
		ps.println( "}" );
		
		ps.println( "}" ); // DiffA
		
		
		
		ps.close();
		
	}
	
	
	
}



