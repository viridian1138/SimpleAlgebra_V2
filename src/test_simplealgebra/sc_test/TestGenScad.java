



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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import test_simplealgebra.TestDimensionThree;



/**
 * Tests the ability to load an old VRML model from the 1990s and calculate an equivalent OpenSCAD model for 3-D printing.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestGenScad extends TestCase {
	
	
	/**
	 * Set of points from the original VRML model.
	 */
	protected HashMap<Integer,OrigPoint> origPts = new HashMap<Integer,OrigPoint>();
	
	/**
	 * Set of edges from the original VRML model.
	 */
	protected HashMap<OrigEdge,OrigEdge> origEdge = new HashMap<OrigEdge,OrigEdge>();
	
	/**
	 * Set of directed edges from the original VRML model.
	 */
	protected HashMap<OrigDirEdge,OrigDirEdge> origDirEdge = new HashMap<OrigDirEdge,OrigDirEdge>();
	
	/**
	 * Set of triangular faces from the original VRML model.
	 */
	protected HashMap<OrigTriangularFace,OrigTriangularFace> origTriangularFace = new HashMap<OrigTriangularFace,OrigTriangularFace>();
	
	/**
	 * Set of new pointds for the OpenSCAD model.
	 */
	protected HashSet<NewPoint> newPts = new HashSet<NewPoint>();
	
	

	/**
	 * Constructs the test.
	 */
	public TestGenScad() {
	}
	
	
	
	/**
	 * Handles the processing of an original VRML non-directed edge.
	 * 
	 * @param p0 The first point of the edge.
	 * @param p1 The second point of the edge.
	 * @return The equivalent edge.
	 */
	protected OrigEdge processEdge( OrigPoint p0 , OrigPoint p1 )
	{
		OrigEdge edge = new OrigEdge( p0 , p1 );
		if( origEdge.get( edge ) != null )
		{
			edge = origEdge.get( edge );
		}
		else
		{
			origEdge.put( edge , edge );
		}
		return( edge );
	}
	
	
	
	/**
	 * Handles the processing of an original VRML directed edge.
	 * 
	 * @param p0 The first point of the directed edge.
	 * @param p1 The second point of the directed edge.
	 * @param eedge The equivalent non-directed edge.
	 * @return The equivalent directed edge.
	 */
	protected OrigDirEdge processDirEdge( OrigPoint p0 , OrigPoint p1 , OrigEdge eedge )
	{
		OrigDirEdge edge = new OrigDirEdge( p0.equals( eedge.getP0() ) , eedge );
		if( origDirEdge.get( edge ) != null )
		{
			edge = origDirEdge.get( edge );
		}
		else
		{
			origDirEdge.put( edge , edge );
		}
		return( edge );
	}
	
	
	/**
	 * Handles the processing of an original VRML triangular face.
	 * 
	 * @param arr Array or directed edges in the face.
	 * @return The generated face.
	 */
	protected OrigTriangularFace processOrigFace( ArrayList<OrigDirEdge> arr )
	{
		OrigTriangularFace of = new OrigTriangularFace( arr );
		if( origTriangularFace.get( of ) != null )
		{
			of = origTriangularFace.get( of );
		}
		else
		{
			origTriangularFace.put( of , of );
		}
		return( of );
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
	
	
	
	
	/**
	 * Tests the ability to load an old VRML model from the 1990s and calculate an equivalent OpenSCAD model for 3-D printing.
	 * 
	 * @throws Throwable
	 */
	public void testScad() throws Throwable
	{
		final DoubleElemFactory d0 = new DoubleElemFactory();
		final TestDimensionThree td = new TestDimensionThree();
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> fac =
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(d0, td, ord);
		
		InputStream is = getClass().getResourceAsStream( "ksurfa.wrl" );
		LineNumberReader li = new LineNumberReader( new InputStreamReader( is ) );
		
		boolean initialPoint = false;
		boolean finalPoint = false;
		int pointIndex = 0;
		
		String line = li.readLine();
		while( ( line != null ) && !finalPoint )
		{
			if( !initialPoint )
			{
				if( line.indexOf( "Coordinate3" ) >= 0 )
				{
					initialPoint = true;
					
					StringTokenizer st = new StringTokenizer( line , " ," );
					
					for( int i = 0 ; i < 6 ; i++ )
					{
						String s = st.nextToken();
					}
					
					String s0 = st.nextToken();
					String s1 = st.nextToken();
					String s2 = st.nextToken();
				
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> vec = fac.zero();
				
					{
						final HashSet<BigInteger> hs = new HashSet<BigInteger>();
						hs.add( BigInteger.ZERO );
						vec.setVal( hs , new DoubleElem( 100.0 * Double.parseDouble( s0 ) ) );
					}
				
					{
						final HashSet<BigInteger> hs = new HashSet<BigInteger>();
						hs.add( BigInteger.ONE );
						vec.setVal( hs , new DoubleElem( 100.0 * Double.parseDouble( s1 ) ) );
					}
				
					{
						final HashSet<BigInteger> hs = new HashSet<BigInteger>();
						hs.add( BigInteger.valueOf( 2 ) );
						vec.setVal( hs , new DoubleElem( 100.0 * Double.parseDouble( s2 ) ) );
					}
				
					OrigPoint orig = new OrigPoint( pointIndex , vec );
				
					origPts.put( pointIndex , orig );
				
					pointIndex++;
				}
			}
			else
			{
				if( line.indexOf( "}" ) >= 0 )
				{
					finalPoint = true;
				}
				else
				{
					StringTokenizer st = new StringTokenizer( line , " ," );
					String s0 = st.nextToken();
					String s1 = st.nextToken();
					String s2 = st.nextToken();
				
					final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> vec = fac.zero();
				
					{
						final HashSet<BigInteger> hs = new HashSet<BigInteger>();
						hs.add( BigInteger.ZERO );
						vec.setVal( hs , new DoubleElem( 100.0 * Double.parseDouble( s0 ) ) );
					}
				
					{
						final HashSet<BigInteger> hs = new HashSet<BigInteger>();
						hs.add( BigInteger.ONE );
						vec.setVal( hs , new DoubleElem( 100.0 * Double.parseDouble( s1 ) ) );
					}
				
					{
						final HashSet<BigInteger> hs = new HashSet<BigInteger>();
						hs.add( BigInteger.valueOf( 2 ) );
						vec.setVal( hs , new DoubleElem( 100.0 * Double.parseDouble( s2 ) ) );
					}
				
					OrigPoint orig = new OrigPoint( pointIndex , vec );
				
					origPts.put( pointIndex , orig );
				
					pointIndex++;
				}
			}
			
			line = li.readLine();
		}
		
		
		{
			ArrayList<OrigDirEdge> arr = new ArrayList<OrigDirEdge>();
			StringTokenizer st = new StringTokenizer( line , " ," );
			
			for( int i = 0 ; i < 4 ; i++ )
			{
				String s = st.nextToken();
			}
			
			String s0 = st.nextToken();
			String s1 = st.nextToken();
			String s2 = st.nextToken();
			
			OrigPoint p0 = origPts.get( Integer.valueOf( s0 ) );
			
			OrigPoint p1 = origPts.get( Integer.valueOf( s1 ) );
			
			OrigPoint p2 = origPts.get( Integer.valueOf( s2 ) );
			
			
			{
				OrigEdge edge = processEdge( p0 , p1 );
				OrigDirEdge eedge = processDirEdge( p0 , p1 , edge );
				arr.add( eedge );
			}
			
			
			{
				OrigEdge edge = processEdge( p1 , p2 );
				OrigDirEdge eedge = processDirEdge( p1 , p2 , edge );
				arr.add( eedge );
			}
			
			
			{
				OrigEdge edge = processEdge( p2 , p0 );
				OrigDirEdge eedge = processDirEdge( p2 , p0 , edge );
				arr.add( eedge );
			}
			
			processOrigFace( arr );
			
		}
		
		
		
		line = li.readLine();
		
		boolean finalEdge = false;
		
		while( ( line != null ) && !finalEdge )
		{
			if( line.indexOf( "}" ) >= 0 )
			{
				finalEdge = true;
			}
			else
			{
				ArrayList<OrigDirEdge> arr = new ArrayList<OrigDirEdge>();
				StringTokenizer st = new StringTokenizer( line , " ," );
				
				String s0 = st.nextToken();
				String s1 = st.nextToken();
				String s2 = st.nextToken();
				
				OrigPoint p0 = origPts.get( Integer.valueOf( s0 ) );
				
				OrigPoint p1 = origPts.get( Integer.valueOf( s1 ) );
				
				OrigPoint p2 = origPts.get( Integer.valueOf( s2 ) );
				
				
				{
					OrigEdge edge = processEdge( p0 , p1 );
					OrigDirEdge eedge = processDirEdge( p0 , p1 , edge );
					arr.add( eedge );
				}
				
				
				{
					OrigEdge edge = processEdge( p1 , p2 );
					OrigDirEdge eedge = processDirEdge( p1 , p2 , edge );
					arr.add( eedge );
				}
				
				
				{
					OrigEdge edge = processEdge( p2 , p0 );
					OrigDirEdge eedge = processDirEdge( p2 , p0 , edge );
					arr.add( eedge );
				}
				
				processOrigFace( arr );
				
			}
			
			line = li.readLine();
			
		}
		
		
		// ps.println( origPts.keySet().size() );
		// ps.println( origEdge.keySet().size() );
		// ps.println( origDirEdge.keySet().size() );
		// ps.println( origTriangularFace.keySet().size() );
		
	
		li.close();
		
		
		FileOutputStream fo = new FileOutputStream( "ksurf.scad" );
		
		PrintStream ps = new PrintStream( fo );
		
		
		int acnt = 0;
		
		
		for( OrigTriangularFace i : origTriangularFace.values() )
		{
			OrigDirEdge e0 = i.getDirEdge( 0 );
			OrigDirEdge e1 = i.getDirEdge( 1 );
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				n0 = e0.getP0().calcNormal();
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				nv0 = n0.divideBy( 1 );
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				n1 = e0.getP1().calcNormal();
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				nv1 = n1.divideBy( 1 );
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				n2 = e1.getP1().calcNormal();
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
				nv2 = n2.divideBy( 1 );
			 

			
			NewPoint[] np0 = NewPoint.genNewPoint( e0.getP0() , nv0 );
			NewPoint[] np1 = NewPoint.genNewPoint( e0.getP1() , nv1 );
			NewPoint[] np2 = NewPoint.genNewPoint( e1.getP1() , nv2 );
			
			newPts.add( np0[ 0 ] );
			newPts.add( np0[ 1 ] );
			
			newPts.add( np1[ 0 ] );
			newPts.add( np1[ 1 ] );
			
			newPts.add( np2[ 0 ] );
			newPts.add( np2[ 1 ] );
				
			ps.println( "" );
			ps.println( "hull() { " );
			ps.print( "translate( " ); writePoint( false , np0[ 0 ].getLocn() , ps ); ps.println( " ) sphere( r=0.0001 ); " );
				
			ps.print( "translate( " ); writePoint( false , np1[ 0 ].getLocn() , ps );  ps.println( " ) sphere( r=0.0001 ); " );
			
			ps.print( "translate( " ); writePoint( false , np2[ 0 ].getLocn() , ps );  ps.println( " ) sphere( r=0.0001 ); " );
			
			ps.print( "translate( " ); writePoint( false , np0[ 1 ].getLocn() , ps );  ps.println( " ) sphere( r=0.0001 ); " );
			
			ps.print( "translate( " ); writePoint( false , np1[ 1 ].getLocn() , ps );  ps.println( " ) sphere( r=0.0001 ); " );
			
			ps.print( "translate( " ); writePoint( false , np2[ 1 ].getLocn() , ps );  ps.println( " ) sphere( r=0.0001 ); " );
			ps.println( "}" );
			
			
					
		}
		
		
		
		ps.close();
		
		
		
	}
	
	

}




