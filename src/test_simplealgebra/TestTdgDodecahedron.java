



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
 * Some code adapted from https://www.cplusplus.com/forum/general/280339/
 * 
 * Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestTdgDodecahedron extends TestCase {
	
	
	
	
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
		rotXY_3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p ,
				double angle ,
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se ,
				int numIterExp )
	{
		final HashSet<BigInteger> hsx = new HashSet<BigInteger>();
		hsx.add( BigInteger.ZERO );
		
		final HashSet<BigInteger> hsy = new HashSet<BigInteger>();
		hsy.add( BigInteger.ONE );
		
		final HashSet<BigInteger> hsz = new HashSet<BigInteger>();
		hsz.add( BigInteger.valueOf( 2 ) );
		
		final double px = p.getVal( hsx ).getVal();
		
		final double py = p.getVal( hsy ).getVal();
		
		final double pz = p.getVal( hsz ).getVal();
		
		final double c = Math.cos( angle );
		final double s = Math.sin( angle );
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> ret =
				initVect_3D( px * c - py * s , px * s + py * c , pz , se );
		
		return( ret );
	}
	
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
		rotXY_4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p ,
				double angle ,
				GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se ,
				int numIterExp )
	{
		final HashSet<BigInteger> hsx = new HashSet<BigInteger>();
		hsx.add( BigInteger.ZERO );
		
		final HashSet<BigInteger> hsy = new HashSet<BigInteger>();
		hsy.add( BigInteger.ONE );
		
		final HashSet<BigInteger> hsz = new HashSet<BigInteger>();
		hsz.add( BigInteger.valueOf( 2 ) );
		
		final double px = p.getVal( hsx ).getVal();
		
		final double py = p.getVal( hsy ).getVal();
		
		final double pz = p.getVal( hsz ).getVal();
		
		final double c = Math.cos( angle );
		final double s = Math.sin( angle );
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ret =
				initVect_4D( px * c - py * s , px * s + py * c , pz , se );
		
		return( ret );
	}
	
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
		rotYZ_3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p ,
				double angle ,
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se ,
				int numIterExp )
	{
		final HashSet<BigInteger> hsx = new HashSet<BigInteger>();
		hsx.add( BigInteger.ZERO );
		
		final HashSet<BigInteger> hsy = new HashSet<BigInteger>();
		hsy.add( BigInteger.ONE );
		
		final HashSet<BigInteger> hsz = new HashSet<BigInteger>();
		hsz.add( BigInteger.valueOf( 2 ) );
		
		final double px = p.getVal( hsx ).getVal();
		
		final double py = p.getVal( hsy ).getVal();
		
		final double pz = p.getVal( hsz ).getVal();
		
		final double c = Math.cos( angle );
		final double s = Math.sin( angle );
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> ret =
				initVect_3D( px , py * c - pz * s , py * s + pz * c , se );
		
		return( ret );
	}
	
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
		rotYZ_4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p ,
				double angle ,
				GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se ,
				int numIterExp )
	{
		final HashSet<BigInteger> hsx = new HashSet<BigInteger>();
		hsx.add( BigInteger.ZERO );
		
		final HashSet<BigInteger> hsy = new HashSet<BigInteger>();
		hsy.add( BigInteger.ONE );
		
		final HashSet<BigInteger> hsz = new HashSet<BigInteger>();
		hsz.add( BigInteger.valueOf( 2 ) );
		
		final double px = p.getVal( hsx ).getVal();
		
		final double py = p.getVal( hsy ).getVal();
		
		final double pz = p.getVal( hsz ).getVal();
		
		final double c = Math.cos( angle );
		final double s = Math.sin( angle );
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ret =
				initVect_4D( px , py * c - pz * s , py * s + pz * c , se );
		
		return( ret );
	}
	
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
		unit_3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p ,
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se )
	{
		final double mag = magnitude_3D( p );
		final DoubleElem multv = new DoubleElem( 1.0 / mag );
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> bv = se.zero();
		final HashSet<BigInteger> hs = new HashSet<BigInteger>();
		bv.setVal( hs , multv );
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> ret =
				p.mult( bv );
		return( ret );
	}
	
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
		unit_4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p ,
				GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se )
	{
		final double mag = magnitude_4D( p );
		final DoubleElem multv = new DoubleElem( 1.0 / mag );
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> bv = se.zero();
		final HashSet<BigInteger> hs = new HashSet<BigInteger>();
		bv.setVal( hs , multv );
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ret =
				p.mult( bv );
		return( ret );
	}
	
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
		reflectInPlane_3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p ,
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> norm ,
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> ref ,
				GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se )
	{
		norm = unit_3D( norm , se );
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, DoubleElem, DoubleElemFactory>
			prod = ( p.add( ref.negate() ) ).mult( norm );
		DoubleElem multv = prod.getVal( new HashSet<BigInteger>() );
		multv = multv.mult( new DoubleElem( 2.0 ) );
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> nv = se.zero();
		nv.setVal( new HashSet<BigInteger>() , multv );
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> normMult =
				nv.mult( norm );
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> ret =
				p.add( normMult.negate() );
				
		return( ret );
	}
	
	
	
	
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
		reflectInPlane_4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p ,
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> norm ,
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ref ,
				GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se )
	{
		norm = unit_4D( norm , se );
		GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>
			prod = ( p.add( ref.negate() ) ).mult( norm );
		DoubleElem multv = prod.getVal( new HashSet<BigInteger>() );
		multv = multv.mult( new DoubleElem( 2.0 ) );
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> nv = se.zero();
		nv.setVal( new HashSet<BigInteger>() , multv );
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> normMult =
				nv.mult( norm );
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ret =
				p.add( normMult.negate() );
				
		return( ret );
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
		buildPentagon3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1,
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2,
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3,
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p4,
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p5 )
	{
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] vects =
				(GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 5 ] );
		
		vects[ 0 ] = p1;
		
		vects[ 1 ] = p2;
		
		vects[ 2 ] = p3;
		
		vects[ 3 ] = p4;
		
		vects[ 4 ] = p5;
		
		VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			ret = new VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( vects  );
		
		return( ret );
	}
	
	

	
	
	
	VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> 
		buildPentagon4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1,
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2,
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3,
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4,
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p5 )
	{
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] vects =
				(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 5 ] );
		
		vects[ 0 ] = p1;
		
		vects[ 1 ] = p2;
		
		vects[ 2 ] = p3;
		
		vects[ 3 ] = p4;
		
		vects[ 4 ] = p5;
		
		VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( vects  );
		
		return( ret );
	}

	
	
	
	
	
	/**
	 * Tests the volume of a dodecahedron in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDodecahedronVolume_3D( ) throws Throwable
	{
		final int NVERT = 20;
		final int NFACE = 12;
		
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
		
		
		
		final double L = 4.0 / ( Math.sqrt( 3.0 ) * ( 1.0 + Math.sqrt( 5.0 ) ) );
		final double d = 0.5 * L / Math.sin( 0.2 * Math.PI );
		final double ztop = Math.sqrt( 1.0 - d * d );
	
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] vertices =
				(GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( 
						new GeometricAlgebraMultivectorElem[ NVERT ] );
		

		vertices[0] = initVect_3D( 0.0 , -d , ztop  , se );
		
		for( int i = 1 ; i <= 4 ; i++ )
		{
			vertices[ i ] = rotXY_3D( vertices[0] , 0.4 * Math.PI * i , se, 20 );
		}
		
		vertices[ 5 ] = rotYZ_3D( vertices[ 0 ] , 2.0 * Math.asin( 0.5 * L ) , se , 20 );
				
		vertices[ 10 ] = reflectInPlane_3D(
				vertices[1],
				vertices[0].add( vertices[5].negate() ),
				vertices[0].add( vertices[5] ).divideBy( 2 ),
				se
				);
		
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> tmp = vertices[ 2 ];
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> tmp2 = se.zero();
			
			{
				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
				hs.add( BigInteger.ZERO );
				tmp2.setVal( hs , tmp.getVal( hs ) );
			}
			
			{
				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
				hs.add( BigInteger.ONE );
				tmp2.setVal( hs , tmp.getVal( hs ).negate() );
			}
			
			{
				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
				hs.add( BigInteger.valueOf( 2 ) );
				tmp2.setVal( hs , tmp.getVal( hs ).negate() );
			}
			
			vertices[ 15 ] = tmp2;
		}
		
		
		for( int t = 1 ; t < 4 ; t++ )
		{
			for( int i = 1 ; i <= 4 ; i++ )
			{
				vertices[ 5 * t + i ] = rotXY_3D( vertices[5*t] , 0.4 * Math.PI * i , se , 20 );
			}
		}
		
		
		for( int cnt = 0 ; cnt < NVERT ; cnt++ )
		{
			topVertices.add( vertices[ cnt ] );
		}
		
		
		
		
		int p = 0;
		topSubs.add( buildPentagon3D( vertices[0] , vertices[1] , vertices[2] , vertices[3] , vertices[4] ) );
		
		
		for( p = 1 ; p <= 5 ; p++ )
		{
			int off1 = (p-1)%5;
			int off2 = p%5;
			topSubs.add( buildPentagon3D( vertices[off1] , vertices[5+off1] , vertices[10+off1] , vertices[5+off2] , vertices[off2] ) );
		}
		
		
		for( p = 6 ; p <= 10 ; p++ )
		{
			int off1 = (p-6)%5;
			int off2 = (p-2)%5;
			topSubs.add( buildPentagon3D( vertices[5+off1] , vertices[10+off2] , vertices[15+off2] , vertices[15+off1] , vertices[10+off1] ) );
		}
		
		p = 11;
		topSubs.add( buildPentagon3D( vertices[19] , vertices[18] , vertices[17] , vertices[16] , vertices[15] ) );
		
		
		
		VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> dodecahedron 
			= new VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( topSubs , topVertices );
		
		
		
		final DoubleElem r = dodecahedron.calcM1(3, se, 20, 20);
		
		
		final double vertexLength = facade.calcLineSegmentLength( vertices[0], vertices[1], 20, 20).getVal();
		final double vertexLengthCubed = vertexLength * vertexLength * vertexLength;
	
		final double dodecMult = ( 15 + 7 * Math.sqrt( 5.0 ) ) / 4.0;
		final double chk = dodecMult * vertexLengthCubed; // dodecahedron volume check
		
		Assert.assertTrue( Math.abs( r.getVal() - chk ) < 0.00001 );
		
	}

	
	
	
	
	
	/**
	 * Tests the volume of a dodecahedron in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDodecahedronVolume_4D( ) throws Throwable
	{
		final int NVERT = 20;
		final int NFACE = 12;
		
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
		
		
		
		final double L = 4.0 / ( Math.sqrt( 3.0 ) * ( 1.0 + Math.sqrt( 5.0 ) ) );
		final double d = 0.5 * L / Math.sin( 0.2 * Math.PI );
		final double ztop = Math.sqrt( 1.0 - d * d );
	
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] vertices =
				(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( 
						new GeometricAlgebraMultivectorElem[ NVERT ] );
		

		vertices[0] = initVect_4D( 0.0 , -d , ztop  , se );
		
		for( int i = 1 ; i <= 4 ; i++ )
		{
			vertices[ i ] = rotXY_4D( vertices[0] , 0.4 * Math.PI * i , se, 20 );
		}
		
		vertices[ 5 ] = rotYZ_4D( vertices[ 0 ] , 2.0 * Math.asin( 0.5 * L ) , se , 20 );
				
		vertices[ 10 ] = reflectInPlane_4D(
				vertices[1],
				vertices[0].add( vertices[5].negate() ),
				vertices[0].add( vertices[5] ).divideBy( 2 ),
				se
				);
		
		{
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> tmp = vertices[ 2 ];
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> tmp2 = se.zero();
			
			{
				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
				hs.add( BigInteger.ZERO );
				tmp2.setVal( hs , tmp.getVal( hs ) );
			}
			
			{
				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
				hs.add( BigInteger.ONE );
				tmp2.setVal( hs , tmp.getVal( hs ).negate() );
			}
			
			{
				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
				hs.add( BigInteger.valueOf( 2 ) );
				tmp2.setVal( hs , tmp.getVal( hs ).negate() );
			}
			
			vertices[ 15 ] = tmp2;
		}
		
		
		for( int t = 1 ; t < 4 ; t++ )
		{
			for( int i = 1 ; i <= 4 ; i++ )
			{
				vertices[ 5 * t + i ] = rotXY_4D( vertices[5*t] , 0.4 * Math.PI * i , se , 20 );
			}
		}
		
		
		for( int cnt = 0 ; cnt < NVERT ; cnt++ )
		{
			topVertices.add( vertices[ cnt ] );
		}
		
		
		
		
		int p = 0;
		topSubs.add( buildPentagon4D( vertices[0] , vertices[1] , vertices[2] , vertices[3] , vertices[4] ) );
		
		
		for( p = 1 ; p <= 5 ; p++ )
		{
			int off1 = (p-1)%5;
			int off2 = p%5;
			topSubs.add( buildPentagon4D( vertices[off1] , vertices[5+off1] , vertices[10+off1] , vertices[5+off2] , vertices[off2] ) );
		}
		
		
		for( p = 6 ; p <= 10 ; p++ )
		{
			int off1 = (p-6)%5;
			int off2 = (p-2)%5;
			topSubs.add( buildPentagon4D( vertices[5+off1] , vertices[10+off2] , vertices[15+off2] , vertices[15+off1] , vertices[10+off1] ) );
		}
		
		p = 11;
		topSubs.add( buildPentagon4D( vertices[19] , vertices[18] , vertices[17] , vertices[16] , vertices[15] ) );
		
		
		
		VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> dodecahedron 
			= new VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( topSubs , topVertices );
		
		
		
		final DoubleElem r = dodecahedron.calcM1(3, se, 20, 20);
		
		
		final double vertexLength = facade.calcLineSegmentLength( vertices[0], vertices[1], 20, 20).getVal();
		final double vertexLengthCubed = vertexLength * vertexLength * vertexLength;
	
		final double dodecMult = ( 15 + 7 * Math.sqrt( 5.0 ) ) / 4.0;
		final double chk = dodecMult * vertexLengthCubed; // dodecahedron volume check
		
		Assert.assertTrue( Math.abs( r.getVal() - chk ) < 0.00001 );
		
	}

	
	
}



