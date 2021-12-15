



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
 * Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestTdgVertex extends TestCase {
	
	
	
	/**
	 * Creates a random vector.
	 * @param rand Random number generator.
	 * @param se The Factory for producing multivectors.
	 * @return The random vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> createRandomVector4D(
			Random rand,
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se
			)
	{
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> a = se.zero();
		
		for( int cntA = 0 ; cntA < TestDimensionFour.FOUR ; cntA++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cntA ) );
			a.setVal( key , new DoubleElem( rand.nextDouble() ) );
		}
		
		return( a );
	}
	
	
	
	/**
	 * Creates a random vector.
	 * @param rand Random number generator.
	 * @param se The Factory for producing multivectors.
	 * @return The random vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> createRandomVector3D(
			Random rand,
			GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se
			)
	{
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> a = se.zero();
		
		for( int cntA = 0 ; cntA < TestDimensionThree.THREE ; cntA++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cntA ) );
			a.setVal( key , new DoubleElem( rand.nextDouble() ) );
		}
		
		return( a );
	}
	
	
	
	/**
	 * Creates a random vector.
	 * @param rand Random number generator.
	 * @param se The Factory for producing multivectors.
	 * @return The random vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> createRandomVector2D(
			Random rand,
			GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se
			)
	{
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> a = se.zero();
		
		for( int cntA = 0 ; cntA < TestDimensionTwo.TWO ; cntA++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cntA ) );
			a.setVal( key , new DoubleElem( rand.nextDouble() ) );
		}
		
		return( a );
	}
	
	
	
	/**
	 * Creates a random vector.
	 * @param rand Random number generator.
	 * @param se The Factory for producing multivectors.
	 * @return The random vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory> createRandomVector1D(
			Random rand,
			GeometricAlgebraMultivectorElemFactory<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory> se
			)
	{
		final GeometricAlgebraMultivectorElem<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory> a = se.zero();
		
		for( int cntA = 0 ; cntA < TestDimensionOne.ONE ; cntA++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cntA ) );
			a.setVal( key , new DoubleElem( rand.nextDouble() ) );
		}
		
		return( a );
	}
	
	
	
	
	protected DoubleElem getMagnitude1D( GeometricAlgebraMultivectorElem<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory> in , DoubleElemFactory dl )
	{
		DoubleElem sum = dl.zero();
		
		for( DoubleElem el : in.getValueSet() )
		{
			sum = sum.add( el.mult( el ) );
		}
		
		
		sum = new DoubleElem( Math.sqrt( sum.getVal() ) );
		
		return( sum );
	}
	
	
	
	
	protected DoubleElem getMagnitude2D( GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> in , DoubleElemFactory dl )
	{
		DoubleElem sum = dl.zero();
		
		for( DoubleElem el : in.getValueSet() )
		{
			sum = sum.add( el.mult( el ) );
		}
		
		
		sum = new DoubleElem( Math.sqrt( sum.getVal() ) );
		
		return( sum );
	}
	
	
	
	
	protected DoubleElem getMagnitude3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> in , DoubleElemFactory dl )
	{
		DoubleElem sum = dl.zero();
		
		for( DoubleElem el : in.getValueSet() )
		{
			sum = sum.add( el.mult( el ) );
		}
		
		
		sum = new DoubleElem( Math.sqrt( sum.getVal() ) );
		
		return( sum );
	}
	
	
	
	
	protected DoubleElem getMagnitude4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> in , DoubleElemFactory dl )
	{
		DoubleElem sum = dl.zero();
		
		for( DoubleElem el : in.getValueSet() )
		{
			sum = sum.add( el.mult( el ) );
		}
		
		
		sum = new DoubleElem( Math.sqrt( sum.getVal() ) );
		
		return( sum );
	}
	
	
	
	
	protected DoubleElem buildDel4D( final Random rand , final HashSet<BigInteger> axes , final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2del  )
	{
		BigInteger d2 = BigInteger.valueOf( rand.nextInt( TestDimensionFour.FOUR ) );
		while( axes.contains( d2 ) )
		{
			d2 = BigInteger.valueOf( rand.nextInt( TestDimensionFour.FOUR ) );
		}
		DoubleElem d2v = new DoubleElem( 2.0 * rand.nextDouble() - 1.0 );
		
		HashSet<BigInteger> p2delhs = new HashSet<BigInteger>();
		p2delhs.add( d2 );
		p2del.setVal( p2delhs ,  d2v );
		
		for( final BigInteger basev : axes )
		{
			HashSet<BigInteger> basehs = new HashSet<BigInteger>();
			basehs.add( basev );
			p2del.setVal( basehs ,  new DoubleElem( 2.0 * rand.nextDouble() - 1.0 ) );
		}
		
		axes.add( d2 );
		return( new DoubleElem( Math.abs( d2v.getVal() ) ) );
	}
	
	
	
	
	protected DoubleElem buildDel3D( final Random rand , final HashSet<BigInteger> axes , final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2del  )
	{
		BigInteger d2 = BigInteger.valueOf( rand.nextInt( TestDimensionThree.THREE ) );
		while( axes.contains( d2 ) )
		{
			d2 = BigInteger.valueOf( rand.nextInt( TestDimensionThree.THREE ) );
		}
		DoubleElem d2v = new DoubleElem( 2.0 * rand.nextDouble() - 1.0 );
		
		HashSet<BigInteger> p2delhs = new HashSet<BigInteger>();
		p2delhs.add( d2 );
		p2del.setVal( p2delhs ,  d2v );
		
		for( final BigInteger basev : axes )
		{
			HashSet<BigInteger> basehs = new HashSet<BigInteger>();
			basehs.add( basev );
			p2del.setVal( basehs ,  new DoubleElem( 2.0 * rand.nextDouble() - 1.0 ) );
		}
		
		axes.add( d2 );
		return( new DoubleElem( Math.abs( d2v.getVal() ) ) );
	}
	
	
	
	
	protected DoubleElem buildDel2D( final Random rand , final HashSet<BigInteger> axes , final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2del  )
	{
		BigInteger d2 = BigInteger.valueOf( rand.nextInt( TestDimensionTwo.TWO ) );
		while( axes.contains( d2 ) )
		{
			d2 = BigInteger.valueOf( rand.nextInt( TestDimensionTwo.TWO ) );
		}
		DoubleElem d2v = new DoubleElem( 2.0 * rand.nextDouble() - 1.0 );
		
		HashSet<BigInteger> p2delhs = new HashSet<BigInteger>();
		p2delhs.add( d2 );
		p2del.setVal( p2delhs ,  d2v );
		
		for( final BigInteger basev : axes )
		{
			HashSet<BigInteger> basehs = new HashSet<BigInteger>();
			basehs.add( basev );
			p2del.setVal( basehs ,  new DoubleElem( 2.0 * rand.nextDouble() - 1.0 ) );
		}
		
		axes.add( d2 );
		return( new DoubleElem( Math.abs( d2v.getVal() ) ) );
	}
	
	
	
	VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> 
		buildParallellogram4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> cntr,
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels )
	{
		if( dels.length != 2 )
		{
			throw( new RuntimeException( "Illegal Argument Exception" ) );
		}
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] vects =
				(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 4 ] );
		
		vects[ 0 ] = cntr;
		
		vects[ 1 ] = vects[ 0 ].add( dels[ 0 ] );
		
		vects[ 2 ] = vects[ 1 ].add( dels[ 1 ] );
		
		vects[ 3 ] = vects[ 2 ].add( dels[ 0 ].negate() );
		
		VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( vects  );
		
		return( ret );
	}
	
	
	
	VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> 
		buildParallellogram3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> cntr,
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] dels )
	{
		if( dels.length != 2 )
		{
			throw( new RuntimeException( "Illegal Argument Exception" ) );
		}
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] vects =
				(GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 4 ] );
		
		vects[ 0 ] = cntr;
		
		vects[ 1 ] = vects[ 0 ].add( dels[ 0 ] );
		
		vects[ 2 ] = vects[ 1 ].add( dels[ 1 ] );
		
		vects[ 3 ] = vects[ 2 ].add( dels[ 0 ].negate() );
		
		VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			ret = new VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( vects  );
		
		return( ret );
	}
	
	
	
	VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> 
		buildHexahedron4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> cntr,
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels )
	{
		if( dels.length != 3 )
		{
			throw( new RuntimeException( "Illegal Argument Exception" ) );
		}
		
		ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> subs =
				new ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> vertices = 
				new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
			
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> cntr2A = cntr;
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> cntr2B = cntr;
		
		vertices.add( cntr2A );
		
		for( int cntA = 0 ; cntA < dels.length ; cntA++ )
		{
			cntr2B = cntr2B.add( dels[ cntA ] );
			vertices.add( cntr2A.add( dels[ cntA ] ) );
		}
		
		vertices.add( cntr2B );
		
		for( int cntA = 0 ; cntA < dels.length ; cntA++ )
		{
			vertices.add( cntr2B.add( dels[ cntA ].negate() ) );
		}
		
		for( int cntA = 0 ; cntA < dels.length ; cntA++ )
		{
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels2A =
					(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 2 ] );
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels2B =
					(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 2 ] );
			int idx = 0;
			for( int cntB = 0 ; cntB < dels.length ; cntB++ )
			{
				if( cntA != cntB )
				{
					dels2A[ idx ] = dels[ cntB ];
					dels2B[ idx ] = dels[ cntB ].negate();
					idx++;
				}
			}
			subs.add( buildParallellogram4D( cntr2A , dels2A ) );
			subs.add( buildParallellogram4D( cntr2B , dels2B ) );
			
		}
		
		if( vertices.size() != 8 )
		{
			throw( new RuntimeException( "Failed" ) );
		}
		
		if( subs.size() != 6 )
		{
			throw( new RuntimeException( "Failed" ) );
		}
		
		VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( subs, vertices );
		
		return( ret );
	}
	
	
	
	VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> 
		buildHexahedron3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> cntr,
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] dels )
	{
		if( dels.length != 3 )
		{
			throw( new RuntimeException( "Illegal Argument Exception" ) );
		}
		
		ArrayList<VertexCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> subs =
				new ArrayList<VertexCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> vertices = 
				new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> cntr2A = cntr;
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> cntr2B = cntr;
		
		vertices.add( cntr2A );
		
		for( int cntA = 0 ; cntA < dels.length ; cntA++ )
		{
			cntr2B = cntr2B.add( dels[ cntA ] );
			vertices.add( cntr2A.add( dels[ cntA ] ) );
		}
		
		vertices.add( cntr2B );
		
		for( int cntA = 0 ; cntA < dels.length ; cntA++ )
		{
			vertices.add( cntr2B.add( dels[ cntA ].negate() ) );
		}
		
		for( int cntA = 0 ; cntA < dels.length ; cntA++ )
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] dels2A =
					(GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 2 ] );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] dels2B =
					(GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 2 ] );
			int idx = 0;
			for( int cntB = 0 ; cntB < dels.length ; cntB++ )
			{
				if( cntA != cntB )
				{
					dels2A[ idx ] = dels[ cntB ];
					dels2B[ idx ] = dels[ cntB ].negate();
					idx++;
				}
			}
			subs.add( buildParallellogram3D( cntr2A , dels2A ) );
			subs.add( buildParallellogram3D( cntr2B , dels2B ) );
			
		}
		
		if( vertices.size() != 8 )
		{
			throw( new RuntimeException( "Failed" ) );
		}
		
		if( subs.size() != 6 )
		{
			throw( new RuntimeException( "Failed" ) );
		}
		
		VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			ret = new VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( subs, vertices );
		
		return( ret );
	}
	
	
	
	
	VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> 
		buildTesseract4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> cntr,
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels )
	{
		if( dels.length != 4 )
		{
			throw( new RuntimeException( "Illegal Argument Exception" ) );
		}
		
		ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> subs =
				new ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> vertices = 
				new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
			
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> cntr2A = cntr;
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> cntr2B = cntr;
		
		for( int cntA = 0 ; cntA < dels.length ; cntA++ )
		{
			cntr2B = cntr2B.add( dels[ cntA ] );
		}
		
		for( int cntA = 0 ; cntA < 16 ; cntA++ )
		{
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> vct = cntr;
			
			if( ( cntA & 1 ) != 0 )
			{
				vct = vct.add( dels[ 0 ] );
			}
			
			if( ( cntA & 2 ) != 0 )
			{
				vct = vct.add( dels[ 1 ] );
			}
			
			if( ( cntA & 4 ) != 0 )
			{
				vct = vct.add( dels[ 2 ] );
			}
			
			if( ( cntA & 8 ) != 0 )
			{
				vct = vct.add( dels[ 3 ] );
			}
			
			vertices.add( vct );
		}
		
		for( int cntA = 0 ; cntA < dels.length ; cntA++ )
		{
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels2A =
					(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels2B =
					(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			int idx = 0;
			for( int cntB = 0 ; cntB < dels.length ; cntB++ )
			{
				if( cntA != cntB )
				{
					dels2A[ idx ] = dels[ cntB ];
					dels2B[ idx ] = dels[ cntB ].negate();
					idx++;
				}
			}
			subs.add( buildHexahedron4D( cntr2A , dels2A ) );
			subs.add( buildHexahedron4D( cntr2B , dels2B ) );
			
		}
		
		if( vertices.size() != 16 )
		{
			throw( new RuntimeException( "Failed" ) );
		}
		
		if( subs.size() != 8 )
		{
			throw( new RuntimeException( "Failed" ) );
		}
		
		VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( subs, vertices );
		
		return( ret );
	}
	
	
	
	
	
	/**
	 * Tests triangle areas in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTriangleArea_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel4D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel4D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			
			
			loop[ 0 ] = p1;
			
			loop[ 1 ] = p2;
			
			loop[ 2 ] = p3;
			
			
			VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM1(2, se, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ).divideBy( 2 ); // base * height / 2
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests triangle areas in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTriangleArea_3D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1 = createRandomVector3D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel3D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel3D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			
			
			loop[ 0 ] = p1;
			
			loop[ 1 ] = p2;
			
			loop[ 2 ] = p3;
			
			
			VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM1(2, se, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ).divideBy( 2 ); // base * height / 2
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests triangle areas in 2-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTriangleArea_2D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p1 = createRandomVector2D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel2D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel2D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			
			
			loop[ 0 ] = p1;
			
			loop[ 1 ] = p2;
			
			loop[ 2 ] = p3;
			
			
			VertexLoop2D<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM1(2, se, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ).divideBy( 2 ); // base * height / 2
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests triangle circumfrence in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTriangleCircumfrence_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel4D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel4D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			
			
			loop[ 0 ] = p1;
			
			loop[ 1 ] = p2;
			
			loop[ 2 ] = p3;
			
			
			VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM2(2, se, 20, 20);
			
	
			final DoubleElem chk = ( facade.calcLineSegmentLength(p1, p2, 20, 20) ).add( facade.calcLineSegmentLength(p2, p3, 20, 20) ).add( facade.calcLineSegmentLength(p3, p1, 20, 20) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests triangle circumfrence in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTriangleCircumfrence_3D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1 = createRandomVector3D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel3D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel3D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			
			
			loop[ 0 ] = p1;
			
			loop[ 1 ] = p2;
			
			loop[ 2 ] = p3;
			
			
			VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM2(2, se, 20, 20);
			
	
			final DoubleElem chk = ( facade.calcLineSegmentLength(p1, p2, 20, 20) ).add( facade.calcLineSegmentLength(p2, p3, 20, 20) ).add( facade.calcLineSegmentLength(p3, p1, 20, 20) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests triangle circumfrence in 2-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTriangleCircumfrence_2D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p1 = createRandomVector2D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel2D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel2D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			
			
			loop[ 0 ] = p1;
			
			loop[ 1 ] = p2;
			
			loop[ 2 ] = p3;
			
			
			VertexLoop2D<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM2(2, se, 20, 20);
			
	
			final DoubleElem chk = ( facade.calcLineSegmentLength(p1, p2, 20, 20) ).add( facade.calcLineSegmentLength(p2, p3, 20, 20) ).add( facade.calcLineSegmentLength(p3, p1, 20, 20) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests paralellogram areas in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testParallellogramArea_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel4D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel4D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 4 ] );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p = p1;
			
			loop[ 0 ] = p;
			
			p = p.add( p2del );
			
			loop[ 1 ] = p;
			
			p = p.add( p3del );
			
			loop[ 2 ] = p;
			
			p = p.add( p2del.negate() );
			
			loop[ 3 ] = p;
			
			
			VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM1(2, se, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ); // base * height
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests paralellogram areas in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testParallellogramArea_3D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1 = createRandomVector3D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel3D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel3D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 4 ] );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p = p1;
			
			loop[ 0 ] = p;
			
			p = p.add( p2del );
			
			loop[ 1 ] = p;
			
			p = p.add( p3del );
			
			loop[ 2 ] = p;
			
			p = p.add( p2del.negate() );
			
			loop[ 3 ] = p;
			
			
			VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM1(2, se, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ); // base * height
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests paralellogram areas in 2-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testParallellogramArea_2D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p1 = createRandomVector2D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel2D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel2D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 4 ] );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p = p1;
			
			loop[ 0 ] = p;
			
			p = p.add( p2del );
			
			loop[ 1 ] = p;
			
			p = p.add( p3del );
			
			loop[ 2 ] = p;
			
			p = p.add( p2del.negate() );
			
			loop[ 3 ] = p;
			
			
			VertexLoop2D<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM1(2, se, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ); // base * height
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests paralellogram circumfrence in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testParalellogramCircumfrence_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel4D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel4D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 4 ] );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p = p1;
			
			loop[ 0 ] = p;
			
			p = p.add( p2del );
			
			loop[ 1 ] = p;
			
			p = p.add( p3del );
			
			loop[ 2 ] = p;
			
			p = p.add( p2del.negate() );
			
			loop[ 3 ] = p;
			
			
			VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM2(2, se, 20, 20);
			
	
			final DoubleElem chk = ( ( facade.calcLineSegmentLength(p1, p2, 20, 20) ).add( facade.calcLineSegmentLength(p1, p3, 20, 20) ) ).mult( new DoubleElem( 2.0 ) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests paralellogram circumfrence in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testParalellogramCircumfrence_3D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1 = createRandomVector3D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel3D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel3D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 4 ] );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p = p1;
			
			loop[ 0 ] = p;
			
			p = p.add( p2del );
			
			loop[ 1 ] = p;
			
			p = p.add( p3del );
			
			loop[ 2 ] = p;
			
			p = p.add( p2del.negate() );
			
			loop[ 3 ] = p;
			
			
			VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM2(2, se, 20, 20);
			
	
			final DoubleElem chk = ( ( facade.calcLineSegmentLength(p1, p2, 20, 20) ).add( facade.calcLineSegmentLength(p1, p3, 20, 20) ) ).mult( new DoubleElem( 2.0 ) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests paralellogram circumfrence in 2-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testParalellogramCircumfrence_2D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p1 = createRandomVector2D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel2D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel2D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 4 ] );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p = p1;
			
			loop[ 0 ] = p;
			
			p = p.add( p2del );
			
			loop[ 1 ] = p;
			
			p = p.add( p3del );
			
			loop[ 2 ] = p;
			
			p = p.add( p2del.negate() );
			
			loop[ 3 ] = p;
			
			
			VertexLoop2D<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM2(2, se, 20, 20);
			
	
			final DoubleElem chk = ( ( facade.calcLineSegmentLength(p1, p2, 20, 20) ).add( facade.calcLineSegmentLength(p1, p3, 20, 20) ) ).mult( new DoubleElem( 2.0 ) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests tetrahedron volumes in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTetrahedronVolume_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel4D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel4D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4del = se.zero();
			DoubleElem p4v = buildDel4D( rand , axes , p4del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4 = p1.add( p4del );
			
			
			ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> lst =
				new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
			
			lst.add( p1 );
			lst.add( p2 );
			lst.add( p3 );
			lst.add( p4 );
			
		
			ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ar =
					new ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
			
			
			
			for( int cntB = 0 ; cntB < lst.size() ; cntB++ )
			{
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] loopA
					= (GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
				int idx = 0;
				
				for( int cntC = 0 ; cntC < lst.size() ; cntC++ )
				{
					if( cntC != cntB )
					{
						loopA[ idx ] = lst.get( cntC );
						idx++;
					}
				}
				
				VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> lpA 
					= new VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( loopA );
				ar.add( lpA );
			}
			
			
	
			
			VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> lpB 
				= new VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( ar , lst );
			
			
			
			
			final DoubleElem r = lpB.calcM1(3, se, 20, 20);
			
	
			final DoubleElem baseArea = p2v.mult( p3v ).divideBy( 2 ); // base * height / 2
			final DoubleElem chk = baseArea.mult( p4v ).divideBy( 3 ); // baseArea * height / 3
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests tetrahedron volumes in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTetrahedronVolume_3D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1 = createRandomVector3D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel3D( rand , axes , p2del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel3D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p4del = se.zero();
			DoubleElem p4v = buildDel3D( rand , axes , p4del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p4 = p1.add( p4del );
			
			
			ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> lst =
				new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			
			lst.add( p1 );
			lst.add( p2 );
			lst.add( p3 );
			lst.add( p4 );
			
		
			ArrayList<VertexCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> ar =
					new ArrayList<VertexCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			
			
			
			for( int cntB = 0 ; cntB < lst.size() ; cntB++ )
			{
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] loopA
					= (GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
				int idx = 0;
				
				for( int cntC = 0 ; cntC < lst.size() ; cntC++ )
				{
					if( cntC != cntB )
					{
						loopA[ idx ] = lst.get( cntC );
						idx++;
					}
				}
				
				VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lpA 
					= new VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( loopA );
				ar.add( lpA );
			}
			
			
	
			
			VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lpB 
				= new VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( ar , lst );
			
			
			
			
			final DoubleElem r = lpB.calcM1(3, se, 20, 20);
			
	
			final DoubleElem baseArea = p2v.mult( p3v ).divideBy( 2 ); // base * height / 2
			final DoubleElem chk = baseArea.mult( p4v ).divideBy( 3 ); // baseArea * height / 3
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests tetrahedron surface area in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTetrahedronSurfaceArea_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2 = createRandomVector4D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 = createRandomVector4D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4 = createRandomVector4D(rand, se);
			
			
			ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> lst =
				new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
			
			lst.add( p1 );
			lst.add( p2 );
			lst.add( p3 );
			lst.add( p4 );
			
		
			ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ar =
					new ArrayList<VertexCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
			
			
			
			for( int cntB = 0 ; cntB < lst.size() ; cntB++ )
			{
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] loopA
					= (GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
				int idx = 0;
				
				for( int cntC = 0 ; cntC < lst.size() ; cntC++ )
				{
					if( cntC != cntB )
					{
						loopA[ idx ] = lst.get( cntC );
						idx++;
					}
				}
				
				VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> lpA 
					= new VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( loopA );
				ar.add( lpA );
			}
			
			
	
			
			VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> lpB 
				= new VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( ar , lst );
			
			
			
			
			final DoubleElem r = lpB.calcM2(3, se, 20, 20);
			
	
			final DoubleElem chk = ( facade.calcTriangleArea(p1, p2, p3, 20, 20) ).add( facade.calcTriangleArea(p1, p2, p4, 20, 20) ).add( facade.calcTriangleArea(p1, p3, p4, 20, 20) ).add( facade.calcTriangleArea(p2, p3, p4, 20, 20) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests tetrahedron surface area in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTetrahedronSurfaceArea_3D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1 = createRandomVector3D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2 = createRandomVector3D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3 = createRandomVector3D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p4 = createRandomVector3D(rand, se);
			
			
			ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> lst =
				new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			
			lst.add( p1 );
			lst.add( p2 );
			lst.add( p3 );
			lst.add( p4 );
			
		
			ArrayList<VertexCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> ar =
					new ArrayList<VertexCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			
			
			
			for( int cntB = 0 ; cntB < lst.size() ; cntB++ )
			{
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] loopA
					= (GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
				int idx = 0;
				
				for( int cntC = 0 ; cntC < lst.size() ; cntC++ )
				{
					if( cntC != cntB )
					{
						loopA[ idx ] = lst.get( cntC );
						idx++;
					}
				}
				
				VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lpA 
					= new VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( loopA );
				ar.add( lpA );
			}
			
			
	
			
			VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lpB 
				= new VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( ar , lst );
			
			
			
			
			final DoubleElem r = lpB.calcM2(3, se, 20, 20);
			
	
			final DoubleElem chk = ( facade.calcTriangleArea(p1, p2, p3, 20, 20) ).add( facade.calcTriangleArea(p1, p2, p4, 20, 20) ).add( facade.calcTriangleArea(p1, p3, p4, 20, 20) ).add( facade.calcTriangleArea(p2, p3, p4, 20, 20) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests hexahedron volumes in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testHexahedronVolume_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			/*
			 * The hexahedron doesn't need to be entirely orthogonal as long as it has parallel planes.
			 */
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel4D( rand , axes , p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel4D( rand , axes , p3del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4del = se.zero();
			DoubleElem p4v = buildDel4D( rand , axes , p4del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels =
					(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			
			dels[ 0 ] = p2del;
			dels[ 1 ] = p3del;
			dels[ 2 ] = p4del;
			
			
			VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> hx = 
					buildHexahedron4D( p1, dels );
		
			
			final DoubleElem r = hx.calcM1(3, se, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ).mult( p4v ); // length * width * height
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests hexahedron volumes in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testHexahedronVolume_3D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);;
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			/*
			 * The hexahedron doesn't need to be entirely orthogonal as long as it has parallel planes.
			 */
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1 = createRandomVector3D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel3D( rand , axes , p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel3D( rand , axes , p3del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p4del = se.zero();
			DoubleElem p4v = buildDel3D( rand , axes , p4del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] dels =
					(GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			
			dels[ 0 ] = p2del;
			dels[ 1 ] = p3del;
			dels[ 2 ] = p4del;
			
			
			VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> hx = 
					buildHexahedron3D( p1, dels );
		
			
			final DoubleElem r = hx.calcM1(3, se, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ).mult( p4v ); // length * width * height
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests hexahedron surface area in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testHexahedronSurfaceArea_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			/*
			 * The hexahedron doesn't need to be entirely orthogonal as long as it has parallel planes.
			 */
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2 = createRandomVector4D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 = createRandomVector4D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4 = createRandomVector4D(rand, se);
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels =
					(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			
			dels[ 0 ] = p2.add( p1.negate() );
			dels[ 1 ] = p3.add( p1.negate() );
			dels[ 2 ] = p4.add( p1.negate() );
			
			
			VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> hx = 
					buildHexahedron4D( p1, dels );
		
			
			final DoubleElem r = hx.calcM2(3, se, 20, 20);
			
	
			final DoubleElem chk = ( facade.calcParalellogramArea(p1, p2, p3, 20, 20) ).add( facade.calcParalellogramArea(p1, p2, p4, 20, 20) ).add( facade.calcParalellogramArea(p1, p3, p4, 20, 20) ).mult( new DoubleElem( 2.0 ) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests hexahedron surface area in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testHexahedronSurfaceArea_3D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			/*
			 * The hexahedron doesn't need to be entirely orthogonal as long as it has parallel planes.
			 */
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1 = createRandomVector3D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2 = createRandomVector3D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3 = createRandomVector3D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p4 = createRandomVector3D(rand, se);
			
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] dels =
					(GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			
			dels[ 0 ] = p2.add( p1.negate() );
			dels[ 1 ] = p3.add( p1.negate() );
			dels[ 2 ] = p4.add( p1.negate() );
			
			
			VertexUnorderedCollection<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> hx = 
					buildHexahedron3D( p1, dels );
		
			
			final DoubleElem r = hx.calcM2(3, se, 20, 20);
			
	
			final DoubleElem chk = ( facade.calcParalellogramArea(p1, p2, p3, 20, 20) ).add( facade.calcParalellogramArea(p1, p2, p4, 20, 20) ).add( facade.calcParalellogramArea(p1, p3, p4, 20, 20) ).mult( new DoubleElem( 2.0 ) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests tesseract hypervolumes in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTesseractHyperVolume_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			/*
			 * Doesn't need to be a tesseract as long as it has parallel hyperplanes.
			 */
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			DoubleElem p2v = buildDel4D( rand , axes , p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel4D( rand , axes , p3del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4del = se.zero();
			DoubleElem p4v = buildDel4D( rand , axes , p4del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p5del = se.zero();
			DoubleElem p5v = buildDel4D( rand , axes , p5del );
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels =
					(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 4 ] );
			
			dels[ 0 ] = p2del;
			dels[ 1 ] = p3del;
			dels[ 2 ] = p4del;
			dels[ 3 ] = p5del;
			
			
			VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> hx = 
					buildTesseract4D( p1, dels );
		
			
			final DoubleElem r = hx.calcM1(4, se, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ).mult( p4v ).mult( p5v );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests tesseract surface volume in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTesseractSurfaceVolume_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> tdg = new Tdg<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			/*
			 * Doesn't need to be a tesseract as long as it has parallel hyperplanes.
			 */
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2 = createRandomVector4D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 = createRandomVector4D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4 = createRandomVector4D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p5 = createRandomVector4D(rand, se);
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] dels =
					(GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ 4 ] );
			
			dels[ 0 ] = p2.add( p1.negate() );
			dels[ 1 ] = p3.add( p1.negate() );
			dels[ 2 ] = p4.add( p1.negate() );
			dels[ 3 ] = p5.add( p1.negate() );
			
			
			VertexUnorderedCollection<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> hx = 
					buildTesseract4D( p1, dels );
		
			
			final DoubleElem r = hx.calcM2(4, se, 20, 20);
			
	
			DoubleElem chk = dl.zero();
			
			GeometricAlgebraMultivectorElem[] vectsII = {p2, p3, p4, p5};
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] vectsI = (GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])(vectsII);
			for( int cntB = 0 ; cntB < vectsI.length ; cntB++ )
			{
				int idx = 0;
				GeometricAlgebraMultivectorElem[] vects2 = new GeometricAlgebraMultivectorElem[ vectsI.length - 1 ];
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] vects = (GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])(vects2);
				for( int cntC = 0 ; cntC < vectsI.length ; cntC++ )
				{
					if( cntC != cntB )
					{
						vects[ idx ] = vectsI[ cntC ];
						idx++;
					}
				}
				chk = chk.add( tdg.calcM1_Pa(p1, vects, 3, 20, 20) ); // Volume of externally facing hexahedron.
			}
			
			
			chk = chk.mult( new DoubleElem( 2.0 ) );
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests convex polygon areas in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testPolygonArea_4D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		final Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
		
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p1 = createRandomVector4D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			buildDel4D( rand , axes , p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			buildDel4D( rand , axes , p3del );
			
			
			final int numVertices = rand.nextInt( 7 ) + 3;
			
			
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ numVertices ] );
			
			
			DoubleElem chk = new DoubleElem( 0.0 );
			
			
			for( int cnt = 0 ; cnt < numVertices ; cnt++ )
			{
				final double rot = 2.0 * Math.PI * cnt / numVertices;
				final DoubleElem da = new DoubleElem( Math.cos( rot ) );
				final DoubleElem db = new DoubleElem( Math.sin( rot ) );
				final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> dav = se.zero();
				final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> dbv = se.zero();
				dav.setVal( new HashSet<BigInteger>() , da);
				dbv.setVal( new HashSet<BigInteger>() , db);
				final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> dv = ( dav.mult( p2del ) ).add( dbv.mult( p3del ) ).add( p1 );
				loop[ cnt ] = dv;
				if( cnt > 0 )
				{
					chk = chk.add( facade.calcTriangleArea(p1, loop[cnt-1], loop[cnt], 20, 20) );
				}
			}
			
			chk = chk.add( facade.calcTriangleArea(p1, loop[numVertices-1], loop[0], 20, 20) );
			
			
			VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM1(2, se, 20, 20);
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests convex polygon areas in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testPolygonArea_3D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		final Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>();
		
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p1 = createRandomVector3D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			buildDel3D( rand , axes , p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			buildDel3D( rand , axes , p3del );
			
			
			final int numVertices = rand.nextInt( 7 ) + 3;
			
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ numVertices ] );
			
			
			DoubleElem chk = new DoubleElem( 0.0 );
			
			
			for( int cnt = 0 ; cnt < numVertices ; cnt++ )
			{
				final double rot = 2.0 * Math.PI * cnt / numVertices;
				final DoubleElem da = new DoubleElem( Math.cos( rot ) );
				final DoubleElem db = new DoubleElem( Math.sin( rot ) );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> dav = se.zero();
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> dbv = se.zero();
				dav.setVal( new HashSet<BigInteger>() , da);
				dbv.setVal( new HashSet<BigInteger>() , db);
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> dv = ( dav.mult( p2del ) ).add( dbv.mult( p3del ) ).add( p1 );
				loop[ cnt ] = dv;
				if( cnt > 0 )
				{
					chk = chk.add( facade.calcTriangleArea(p1, loop[cnt-1], loop[cnt], 20, 20) );
				}
			}
			
			chk = chk.add( facade.calcTriangleArea(p1, loop[numVertices-1], loop[0], 20, 20) );
			
			
			VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM1(2, se, 20, 20);
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests convex polygon areas in 2-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testPolygonArea_2D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		final Tdg_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>();
		
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p1 = createRandomVector2D(rand, se);
			
			final HashSet<BigInteger> axes = new HashSet<BigInteger>();
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2del = se.zero();
			buildDel2D( rand , axes , p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			buildDel2D( rand , axes , p3del );
			
			
			final int numVertices = rand.nextInt( 7 ) + 3;
			
			
			GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>[] loop
				= (GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>[])( new GeometricAlgebraMultivectorElem[ numVertices ] );
			
			
			DoubleElem chk = new DoubleElem( 0.0 );
			
			
			for( int cnt = 0 ; cnt < numVertices ; cnt++ )
			{
				final double rot = 2.0 * Math.PI * cnt / numVertices;
				final DoubleElem da = new DoubleElem( Math.cos( rot ) );
				final DoubleElem db = new DoubleElem( Math.sin( rot ) );
				final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> dav = se.zero();
				final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> dbv = se.zero();
				dav.setVal( new HashSet<BigInteger>() , da);
				dbv.setVal( new HashSet<BigInteger>() , db);
				final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> dv = ( dav.mult( p2del ) ).add( dbv.mult( p3del ) ).add( p1 );
				loop[ cnt ] = dv;
				if( cnt > 0 )
				{
					chk = chk.add( facade.calcTriangleArea(p1, loop[cnt-1], loop[cnt], 20, 20) );
				}
			}
			
			chk = chk.add( facade.calcTriangleArea(p1, loop[numVertices-1], loop[0], 20, 20) );
			
			
			VertexLoop2D<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> lp 
				= new VertexLoop2D<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( loop );
			
			
			final DoubleElem r = lp.calcM1(2, se, 20, 20);
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
}



