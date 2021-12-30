



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
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.*;
import simplealgebra.tdg.Tdg;
import simplealgebra.tdg.Tdg_Facade;
import simplealgebra.tdg.VertexCollection;
import simplealgebra.tdg.VertexLoop2D;
import simplealgebra.tdg.VertexUnorderedCollection;


/**
 * Tests the ability to generate geometric measurements.
 * 
 * Adapted from https://github.com/superwills/gtp/blob/master/gtp/geometry/Icosahedron.cpp
 * 
 * Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestTdgIcosahedron extends TestCase {
	
	
	
	
	protected double magnitude_3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p )
	{
		double sum = 0.0;
		
		for( DoubleElem e : p.getValueSet() )
		{
			sum += ( e.getVal() * e.getVal() );
		}
		
		return( Math.sqrt( sum ) );
	}
	
	
	
	
	protected double magnitude_4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p )
	{
		double sum = 0.0;
		
		for( DoubleElem e : p.getValueSet() )
		{
			sum += ( e.getVal() * e.getVal() );
		}
		
		return( Math.sqrt( sum ) );
	}
	
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
		initVect_3D( double d0, double d1, double d2,
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se )
	{
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> bv = se.zero();
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			bv.setVal( hs , new DoubleElem( d0 ) );
		}
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			bv.setVal( hs , new DoubleElem( d1 ) );
		}
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.valueOf( 2 ) );
			bv.setVal( hs , new DoubleElem( d2 ) );
		}
		
		return( bv );
	}
	
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
		initVect_4D( double d0, double d1, double d2,
				GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se )
	{
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> bv = se.zero();
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			bv.setVal( hs , new DoubleElem( d0 ) );
		}
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			bv.setVal( hs , new DoubleElem( d1 ) );
		}
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.valueOf( 2 ) );
			bv.setVal( hs , new DoubleElem( d2 ) );
		}
		
		return( bv );
	}
	
	

	
	
	
	VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> 
		buildTriangle3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1,
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2,
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3 )
	{
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] vects =
				(GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
		
		vects[ 0 ] = p1;
		
		vects[ 1 ] = p2;
		
		vects[ 2 ] = p3;
		
		VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			ret = new VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( vects  );
		
		return( ret );
	}
	
	

	
	
	
	VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> 
		buildTriangle4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1,
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2,
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 )
	{
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] vects =
				(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
		
		vects[ 0 ] = p1;
		
		vects[ 1 ] = p2;
		
		vects[ 2 ] = p3;
		
		VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( vects  );
		
		return( ret );
	}

	
	
	
	
	
	/**
	 * Tests the volume of a icosahedron in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testIcosahedronVolume_3D( ) throws Throwable
	{
		final int NVERT = 12;
		final int NFACE = 20;
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		final Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>();
		
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
			topVertices = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
	
	
	
		final ArrayList<VertexCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
			topSubs = new ArrayList<VertexCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
		
		
		
		final double t = ( 1.0 + Math.sqrt( 5.0 ) ) / 2.0;
	
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] vertices =
				(GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( 
						new GeometricAlgebraMultivectorElem[ NVERT ] );
		
		
		for( int i = 0 ; i < 4 ; i++ )
		{
			vertices[ i ] = initVect_3D( 0.0 , ((i&2)!=0)?-1:1 , ((i&1)!=0)?-t:t, se  );
		}
		
		for( int i = 4 ; i < 8 ; i++ )
		{
			vertices[ i ] = initVect_3D( ((i&2)!=0)?-1:1 , ((i&1)!=0)?-t:t, 0.0 , se  );
		}
		
		for( int i = 8 ; i < 12 ; i++ )
		{
			vertices[ i ] = initVect_3D(  ((i&1)!=0)?-t:t, 0.0 ,    ((i&2)!=0)?-1:1   , se  );
		}
		
		
		
		
		
		

		topSubs.add( buildTriangle3D( vertices[0] , vertices[2] , vertices[8] ) );
		topSubs.add( buildTriangle3D( vertices[0] , vertices[8] , vertices[4] ) );
		topSubs.add( buildTriangle3D( vertices[0] , vertices[4] , vertices[6] ) );
		topSubs.add( buildTriangle3D( vertices[0] , vertices[6] , vertices[9] ) );
		topSubs.add( buildTriangle3D( vertices[0] , vertices[9] , vertices[2] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[2] , vertices[7] , vertices[5] ) );
		topSubs.add( buildTriangle3D( vertices[2] , vertices[5] , vertices[8] ) );
		topSubs.add( buildTriangle3D( vertices[2] , vertices[9] , vertices[7] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[8] , vertices[5] , vertices[10] ) );
		topSubs.add( buildTriangle3D( vertices[8] , vertices[10] , vertices[4] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[10] , vertices[5] , vertices[3] ) );
		topSubs.add( buildTriangle3D( vertices[10] , vertices[3] , vertices[1] ) );
		topSubs.add( buildTriangle3D( vertices[10] , vertices[1] , vertices[4] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[1] , vertices[6] , vertices[4] ) );
		topSubs.add( buildTriangle3D( vertices[1] , vertices[3] , vertices[11] ) );
		topSubs.add( buildTriangle3D( vertices[1] , vertices[11] , vertices[6] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[6] , vertices[11] , vertices[9] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[11] , vertices[3] , vertices[7] ) );
		topSubs.add( buildTriangle3D( vertices[11] , vertices[7] , vertices[9] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[3] , vertices[5] , vertices[7] ) );
		
		
		
		
		
		
		
		VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> icosahedron 
			= new VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( topSubs , topVertices );
		
		
		
		final DoubleElem r = icosahedron.calcM1(3, se, 20, 20);
		
		
		final double edgeLength = facade.calcLineSegmentLength( vertices[0], vertices[2], 20, 20).getVal();
		final double edgeLengthCubed = edgeLength * edgeLength * edgeLength;
	
		final double icoMult = 5.0 * ( 3.0 + Math.sqrt( 5.0 ) ) / 12.0;
		final double chk = icoMult * edgeLengthCubed; // icosahedron volume check
		
		
		Assert.assertTrue( Math.abs( r.getVal() - chk ) < 0.00001 );
		
	}

	
	
	
	
	
	/**
	 * Tests the volume of a icosahedron in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testIcosahedronVolume_4D( ) throws Throwable
	{
		final int NVERT = 12;
		final int NFACE = 20;
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		final Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
		
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> 
			topVertices = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
	
	
	
		final ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> 
			topSubs = new ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		
		
		
		final double t = ( 1.0 + Math.sqrt( 5.0 ) ) / 2.0;
	
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] vertices =
				(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( 
						new GeometricAlgebraMultivectorElem[ NVERT ] );
		
		
		for( int i = 0 ; i < 4 ; i++ )
		{
			vertices[ i ] = initVect_4D( 0.0 , ((i&2)!=0)?-1:1 , ((i&1)!=0)?-t:t, se  );
		}
		
		for( int i = 4 ; i < 8 ; i++ )
		{
			vertices[ i ] = initVect_4D( ((i&2)!=0)?-1:1 , ((i&1)!=0)?-t:t, 0.0 , se  );
		}
		
		for( int i = 8 ; i < 12 ; i++ )
		{
			vertices[ i ] = initVect_4D(  ((i&1)!=0)?-t:t, 0.0 ,    ((i&2)!=0)?-1:1   , se  );
		}
		
		
		
		
		
		

		topSubs.add( buildTriangle4D( vertices[0] , vertices[2] , vertices[8] ) );
		topSubs.add( buildTriangle4D( vertices[0] , vertices[8] , vertices[4] ) );
		topSubs.add( buildTriangle4D( vertices[0] , vertices[4] , vertices[6] ) );
		topSubs.add( buildTriangle4D( vertices[0] , vertices[6] , vertices[9] ) );
		topSubs.add( buildTriangle4D( vertices[0] , vertices[9] , vertices[2] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[2] , vertices[7] , vertices[5] ) );
		topSubs.add( buildTriangle4D( vertices[2] , vertices[5] , vertices[8] ) );
		topSubs.add( buildTriangle4D( vertices[2] , vertices[9] , vertices[7] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[8] , vertices[5] , vertices[10] ) );
		topSubs.add( buildTriangle4D( vertices[8] , vertices[10] , vertices[4] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[10] , vertices[5] , vertices[3] ) );
		topSubs.add( buildTriangle4D( vertices[10] , vertices[3] , vertices[1] ) );
		topSubs.add( buildTriangle4D( vertices[10] , vertices[1] , vertices[4] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[1] , vertices[6] , vertices[4] ) );
		topSubs.add( buildTriangle4D( vertices[1] , vertices[3] , vertices[11] ) );
		topSubs.add( buildTriangle4D( vertices[1] , vertices[11] , vertices[6] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[6] , vertices[11] , vertices[9] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[11] , vertices[3] , vertices[7] ) );
		topSubs.add( buildTriangle4D( vertices[11] , vertices[7] , vertices[9] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[3] , vertices[5] , vertices[7] ) );
		
		
		
		
		
		
		
		VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> icosahedron 
			= new VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( topSubs , topVertices );
		
		
		
		final DoubleElem r = icosahedron.calcM1(3, se, 20, 20);
		
		
		final double edgeLength = facade.calcLineSegmentLength( vertices[0], vertices[2], 20, 20).getVal();
		final double edgeLengthCubed = edgeLength * edgeLength * edgeLength;
	
		final double icoMult = 5.0 * ( 3.0 + Math.sqrt( 5.0 ) ) / 12.0;
		final double chk = icoMult * edgeLengthCubed; // icosahedron volume check
		
		
		Assert.assertTrue( Math.abs( r.getVal() - chk ) < 0.00001 );
		
	}

	
	
	
	
	
	/**
	 * Tests the surface area of a icosahedron in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testIcosahedronSurfaceArea_3D( ) throws Throwable
	{
		final int NVERT = 12;
		final int NFACE = 20;
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		final Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>();
		
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
			topVertices = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
	
	
	
		final ArrayList<VertexCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
			topSubs = new ArrayList<VertexCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
		
		
		
		final double t = ( 1.0 + Math.sqrt( 5.0 ) ) / 2.0;
	
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] vertices =
				(GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( 
						new GeometricAlgebraMultivectorElem[ NVERT ] );
		
		
		for( int i = 0 ; i < 4 ; i++ )
		{
			vertices[ i ] = initVect_3D( 0.0 , ((i&2)!=0)?-1:1 , ((i&1)!=0)?-t:t, se  );
		}
		
		for( int i = 4 ; i < 8 ; i++ )
		{
			vertices[ i ] = initVect_3D( ((i&2)!=0)?-1:1 , ((i&1)!=0)?-t:t, 0.0 , se  );
		}
		
		for( int i = 8 ; i < 12 ; i++ )
		{
			vertices[ i ] = initVect_3D(  ((i&1)!=0)?-t:t, 0.0 ,    ((i&2)!=0)?-1:1   , se  );
		}
		
		
		
		
		
		

		topSubs.add( buildTriangle3D( vertices[0] , vertices[2] , vertices[8] ) );
		topSubs.add( buildTriangle3D( vertices[0] , vertices[8] , vertices[4] ) );
		topSubs.add( buildTriangle3D( vertices[0] , vertices[4] , vertices[6] ) );
		topSubs.add( buildTriangle3D( vertices[0] , vertices[6] , vertices[9] ) );
		topSubs.add( buildTriangle3D( vertices[0] , vertices[9] , vertices[2] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[2] , vertices[7] , vertices[5] ) );
		topSubs.add( buildTriangle3D( vertices[2] , vertices[5] , vertices[8] ) );
		topSubs.add( buildTriangle3D( vertices[2] , vertices[9] , vertices[7] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[8] , vertices[5] , vertices[10] ) );
		topSubs.add( buildTriangle3D( vertices[8] , vertices[10] , vertices[4] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[10] , vertices[5] , vertices[3] ) );
		topSubs.add( buildTriangle3D( vertices[10] , vertices[3] , vertices[1] ) );
		topSubs.add( buildTriangle3D( vertices[10] , vertices[1] , vertices[4] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[1] , vertices[6] , vertices[4] ) );
		topSubs.add( buildTriangle3D( vertices[1] , vertices[3] , vertices[11] ) );
		topSubs.add( buildTriangle3D( vertices[1] , vertices[11] , vertices[6] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[6] , vertices[11] , vertices[9] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[11] , vertices[3] , vertices[7] ) );
		topSubs.add( buildTriangle3D( vertices[11] , vertices[7] , vertices[9] ) );
		
		
		topSubs.add( buildTriangle3D( vertices[3] , vertices[5] , vertices[7] ) );
		
		
		
		
		
		
		
		VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> icosahedron 
			= new VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( topSubs , topVertices );
		
		
		
		final DoubleElem r = icosahedron.calcM2(3, se, 20, 20);
		
		
		final double edgeLength = facade.calcLineSegmentLength( vertices[0], vertices[2], 20, 20).getVal();
		final double edgeLengthSquared = edgeLength * edgeLength;
	
		final double icoMult = 5.0 * Math.sqrt( 3.0 );
		final double chk = icoMult * edgeLengthSquared; // icosahedron surface area check
		
		
		Assert.assertTrue( Math.abs( r.getVal() - chk ) < 0.00001 );
		
	}

	
	
	
	
	
	/**
	 * Tests the surface area of a icosahedron in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testIcosahedronSurfaceArea_4D( ) throws Throwable
	{
		final int NVERT = 12;
		final int NFACE = 20;
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		final Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
		
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> 
			topVertices = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
	
	
	
		final ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> 
			topSubs = new ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		
		
		
		final double t = ( 1.0 + Math.sqrt( 5.0 ) ) / 2.0;
	
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] vertices =
				(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( 
						new GeometricAlgebraMultivectorElem[ NVERT ] );
		
		
		for( int i = 0 ; i < 4 ; i++ )
		{
			vertices[ i ] = initVect_4D( 0.0 , ((i&2)!=0)?-1:1 , ((i&1)!=0)?-t:t, se  );
		}
		
		for( int i = 4 ; i < 8 ; i++ )
		{
			vertices[ i ] = initVect_4D( ((i&2)!=0)?-1:1 , ((i&1)!=0)?-t:t, 0.0 , se  );
		}
		
		for( int i = 8 ; i < 12 ; i++ )
		{
			vertices[ i ] = initVect_4D(  ((i&1)!=0)?-t:t, 0.0 ,    ((i&2)!=0)?-1:1   , se  );
		}
		
		
		
		
		
		

		topSubs.add( buildTriangle4D( vertices[0] , vertices[2] , vertices[8] ) );
		topSubs.add( buildTriangle4D( vertices[0] , vertices[8] , vertices[4] ) );
		topSubs.add( buildTriangle4D( vertices[0] , vertices[4] , vertices[6] ) );
		topSubs.add( buildTriangle4D( vertices[0] , vertices[6] , vertices[9] ) );
		topSubs.add( buildTriangle4D( vertices[0] , vertices[9] , vertices[2] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[2] , vertices[7] , vertices[5] ) );
		topSubs.add( buildTriangle4D( vertices[2] , vertices[5] , vertices[8] ) );
		topSubs.add( buildTriangle4D( vertices[2] , vertices[9] , vertices[7] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[8] , vertices[5] , vertices[10] ) );
		topSubs.add( buildTriangle4D( vertices[8] , vertices[10] , vertices[4] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[10] , vertices[5] , vertices[3] ) );
		topSubs.add( buildTriangle4D( vertices[10] , vertices[3] , vertices[1] ) );
		topSubs.add( buildTriangle4D( vertices[10] , vertices[1] , vertices[4] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[1] , vertices[6] , vertices[4] ) );
		topSubs.add( buildTriangle4D( vertices[1] , vertices[3] , vertices[11] ) );
		topSubs.add( buildTriangle4D( vertices[1] , vertices[11] , vertices[6] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[6] , vertices[11] , vertices[9] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[11] , vertices[3] , vertices[7] ) );
		topSubs.add( buildTriangle4D( vertices[11] , vertices[7] , vertices[9] ) );
		
		
		topSubs.add( buildTriangle4D( vertices[3] , vertices[5] , vertices[7] ) );
		
		
		
		
		
		
		
		VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> icosahedron 
			= new VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( topSubs , topVertices );
		
		
		
		final DoubleElem r = icosahedron.calcM2(3, se, 20, 20);
		
		
		final double edgeLength = facade.calcLineSegmentLength( vertices[0], vertices[2], 20, 20).getVal();
		final double edgeLengthSquared = edgeLength * edgeLength;
	
		final double icoMult = 5.0 * Math.sqrt( 3.0 );
		final double chk = icoMult * edgeLengthSquared; // icosahedron surface area check
		
		
		Assert.assertTrue( Math.abs( r.getVal() - chk ) < 0.00001 );
		
	}

	
	
	
	
	
}



