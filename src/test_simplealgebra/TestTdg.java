



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


/**
 * Tests the ability to generate geometric measurements.
 * Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestTdg extends TestCase {
	
	
	
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
	
	
	
	
	
	/**
	 * Tests line segment lengths in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testLineSegmentLength_4D( ) throws Throwable
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
			
		
			final DoubleElem r = facade.calcLineSegmentLength(p1, p2, 20, 20);
			
	
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> del = p2.add( p1.negate() );
			
			DoubleElem sum = getMagnitude4D( del , dl );
			
			
			Assert.assertTrue( Math.abs( r.add( sum.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests line segment lengths in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testLineSegmentLength_3D( ) throws Throwable
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
			
		
			final DoubleElem r = facade.calcLineSegmentLength(p1, p2, 20, 20);
			
	
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> del = p2.add( p1.negate() );
			
			DoubleElem sum = getMagnitude3D( del , dl );
			
			
			Assert.assertTrue( Math.abs( r.add( sum.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests line segment lengths in 2-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testLineSegmentLength_2D( ) throws Throwable
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
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2 = createRandomVector2D(rand, se);
			
		
			final DoubleElem r = facade.calcLineSegmentLength(p1, p2, 20, 20);
			
	
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> del = p2.add( p1.negate() );
			
			DoubleElem sum = getMagnitude2D( del , dl );
			
			
			Assert.assertTrue( Math.abs( r.add( sum.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests line segment lengths in 1-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testLineSegmentLength_1D( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionOne td = new TestDimensionOne();
		
		final GeometricAlgebraOrd<TestDimensionOne> ord = new GeometricAlgebraOrd<TestDimensionOne>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory>();
	
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory> p1 = createRandomVector1D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory> p2 = createRandomVector1D(rand, se);
			
		
			final DoubleElem r = facade.calcLineSegmentLength(p1, p2, 20, 20);
			
	
			final GeometricAlgebraMultivectorElem<TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>,DoubleElem,DoubleElemFactory> del = p2.add( p1.negate() );
			
			DoubleElem sum = getMagnitude1D( del , dl );
			
			
			Assert.assertTrue( Math.abs( r.add( sum.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
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
			
		
			
			final DoubleElem r = facade.calcTriangleArea(p1, p2, p3, 20, 20);
			
	
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
			
		
			
			final DoubleElem r = facade.calcTriangleArea(p1, p2, p3, 20, 20);
			
	
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
			
		
			
			final DoubleElem r = facade.calcTriangleArea(p1, p2, p3, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ).divideBy( 2 ); // base * height / 2
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests triangle perimeter in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTrianglePerimeter_4D( ) throws Throwable
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
			
		
			
			final DoubleElem r = facade.calcTrianglePerimeter(p1, p2, p3, 20, 20);
			
	
			final DoubleElem chk = ( facade.calcLineSegmentLength(p1, p2, 20, 20) ).add( facade.calcLineSegmentLength(p2, p3, 20, 20) ).add( facade.calcLineSegmentLength(p3, p1, 20, 20) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests triangle perimeter in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTrianglePerimeter_3D( ) throws Throwable
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
			
		
			
			final DoubleElem r = facade.calcTrianglePerimeter(p1, p2, p3, 20, 20);
			
	
			final DoubleElem chk = ( facade.calcLineSegmentLength(p1, p2, 20, 20) ).add( facade.calcLineSegmentLength(p2, p3, 20, 20) ).add( facade.calcLineSegmentLength(p3, p1, 20, 20) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests triangle perimeter in 2-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTrianglePerimeter_2D( ) throws Throwable
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
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2 = createRandomVector2D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3 = createRandomVector2D(rand, se);
			
		
			
			final DoubleElem r = facade.calcTrianglePerimeter(p1, p2, p3, 20, 20);
			
	
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
			
		
			
			final DoubleElem r = facade.calcParalellogramArea(p1, p2, p3, 20, 20);
			
	
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
			
		
			
			final DoubleElem r = facade.calcParalellogramArea(p1, p2, p3, 20, 20);
			
	
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
			
		
			
			final DoubleElem r = facade.calcParalellogramArea(p1, p2, p3, 20, 20);
			
	
			final DoubleElem chk = p2v.mult( p3v ); // base * height
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests paralellogram perimeter in 4-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testParalellogramPerimeter_4D( ) throws Throwable
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
			
		
			
			final DoubleElem r = facade.calcParalellogramPerimeter(p1, p2, p3, 20, 20);
			
	
			final DoubleElem chk = ( ( facade.calcLineSegmentLength(p1, p2, 20, 20) ).add( facade.calcLineSegmentLength(p1, p3, 20, 20) ) ).mult( new DoubleElem( 2.0 ) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests paralellogram perimeter in 3-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testParalellogramPerimeter_3D( ) throws Throwable
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
			
		
			
			final DoubleElem r = facade.calcParalellogramPerimeter(p1, p2, p3, 20, 20);
			
	
			final DoubleElem chk = ( ( facade.calcLineSegmentLength(p1, p2, 20, 20) ).add( facade.calcLineSegmentLength(p1, p3, 20, 20) ) ).mult( new DoubleElem( 2.0 ) );
			
			
			Assert.assertTrue( Math.abs( r.add( chk.negate() ).getVal() ) < 0.00001 );
			
			
		}
		
		
	}
	
	
	
	
	
	/**
	 * Tests paralellogram perimeter in 2-D.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testParalellogramPerimeter_2D( ) throws Throwable
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
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p2 = createRandomVector2D(rand, se);
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p3 = createRandomVector2D(rand, se);
			
		
			
			final DoubleElem r = facade.calcParalellogramPerimeter(p1, p2, p3, 20, 20);
			
	
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
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4del = se.zero();
			DoubleElem p4v = buildDel4D( rand , axes , p4del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4 = p1.add( p4del );
			
		
			
			final DoubleElem r = facade.calcTetrahedronVolume(p1, p2, p3, p4, 20, 20);
			
	
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
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p4del = se.zero();
			DoubleElem p4v = buildDel3D( rand , axes , p4del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p4 = p1.add( p4del );
			
		
			
			final DoubleElem r = facade.calcTetrahedronVolume(p1, p2, p3, p4, 20, 20);
			
	
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
			
		
			
			final DoubleElem r = facade.calcTetrahedronSurfaceArea(p1, p2, p3, p4, 20, 20);
			
	
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
			
		
			
			final DoubleElem r = facade.calcTetrahedronSurfaceArea(p1, p2, p3, p4, 20, 20);
			
	
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
		
		final Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
	
		
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
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel4D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4del = se.zero();
			DoubleElem p4v = buildDel4D( rand , axes , p4del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4 = p1.add( p4del );
			
		
			
			final DoubleElem r = facade.calcOrthogonalHexahedronVolume(p1, p2, p3, p4, 20, 20);
			
	
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
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>();
	
		
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
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel3D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p4del = se.zero();
			DoubleElem p4v = buildDel3D( rand , axes , p4del );
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> p4 = p1.add( p4del );
			
		
			
			final DoubleElem r = facade.calcOrthogonalHexahedronVolume(p1, p2, p3, p4, 20, 20);
			
	
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
			
		
			
			final DoubleElem r = facade.calcOrthogonalHexahedronSurfaceArea(p1, p2, p3, p4, 20, 20);
			
	
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
			
		
			
			final DoubleElem r = facade.calcOrthogonalHexahedronSurfaceArea(p1, p2, p3, p4, 20, 20);
			
	
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
		
		final Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
	
		
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
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p2 = p1.add( p2del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3del = se.zero();
			DoubleElem p3v = buildDel4D( rand , axes , p3del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p3 = p1.add( p3del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4del = se.zero();
			DoubleElem p4v = buildDel4D( rand , axes , p4del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p4 = p1.add( p4del );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p5del = se.zero();
			DoubleElem p5v = buildDel4D( rand , axes , p5del );
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p5 = p1.add( p5del );
			
		
			
			final DoubleElem r = facade.calcTesseractHyperVolume(p1, p2, p3, p4, p5, 20, 20);
			
	
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
		
		final Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> facade = new Tdg_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
		
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
			
		
			
			final DoubleElem r = facade.calcTesseractSurfaceVolume(p1, p2, p3, p4, p5, 20, 20);
			
	
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
	
	
	
	
	
	
	
	
	
	
	
}



