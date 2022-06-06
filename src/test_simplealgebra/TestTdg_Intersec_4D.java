



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
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.tdg.Tdg_Intersec;


/**
 * Tests the ability to generate geometric intersections.
 * Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestTdg_Intersec_4D extends TestCase {
	
	
	
	/**
	 * Generates a random vector.
	 * @param rand Input random number generator.
	 * @param fac Factory for generating multivectors.
	 * @return The random vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genRandomVector(
			Random rand ,
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> fac )
	{
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> vct = fac.zero();
		for( int i = 0 ; i < TestDimensionFour.FOUR ; i++ )
		{
			final double d = 2.0 * ( rand.nextDouble() ) - 1.0;
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( i ) );
			vct.setVal(key, new DoubleElem( d ) );
		}
		return( vct );
	}
	
	
	
	/**
	 * Generates a random unit vector.
	 * @param rand Input random number generator.
	 * @param fac Factory for generating multivectors.
	 * @return The random unit vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genRandomUnitVector(
			Random rand ,
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> fac )
	{
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> vct = genRandomVector(rand, fac);
		return( makeVecUnit( vct , fac ) );
	}
	

	
	/**
	 * Returns a vector scaled to a unit vector.
	 * @param vct The input vector to be scaled.
	 * @param fac Input factory for generating multivectors.
	 * @return Unit version of the input vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> makeVecUnit(
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> vct ,
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> fac )
	{
		double dv = 0.0;
		for( final HashSet<BigInteger> hs : vct.getKeySet() )
		{
			final DoubleElem dvv = vct.get( hs );
			final double dvvv = dvv.getVal();
			dv += dvvv * dvvv;
		}
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> div = fac.zero();
		div.setVal( new HashSet<BigInteger>() , new DoubleElem( 1.0 / Math.sqrt( dv ) ) );
		return( vct.mult( div ) );
	}
	
	
	
	/**
	 * Returns a bivector scaled to a unit bivector.
	 * @param vct The input bivector to be scaled.
	 * @param fac Input factory for generating multivectors.
	 * @return Unit version of the input bivector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> makeBivectorUnit(
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> vct ,
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> fac )
	{
		double dv = 0.0;
		for( final HashSet<BigInteger> hs : vct.getKeySet() )
		{
			final DoubleElem dvv = vct.get( hs );
			final double dvvv = dvv.getVal();
			dv += dvvv * dvvv;
		}
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> div = fac.zero();
		div.setVal( new HashSet<BigInteger>() , new DoubleElem( 1.0 / Math.sqrt( dv ) ) );
		return( vct.mult( div ) );
	}
	
	
	
	/**
	 * Scales a multivector by a value.
	 * @param vct The input multivector.
	 * @param fac Factory for generating multivectors.
	 * @param val The value by which to scale.
	 * @return The scaled multivector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> scaleVal(
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> vct ,
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> fac ,
			double val )
	{
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> scv = fac.zero();
		scv.setVal( new HashSet<BigInteger>() , new DoubleElem( val ) );
		return( vct.mult( scv ) );
	}
	
	
	
	/**
	 * Tests simple plane surface-surface intersections (SSI) in 4-D.
	 * 
	 * @throws Throwable
	 */
	public void testInitial( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		
		final Tdg_Intersec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> tdg = new Tdg_Intersec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
		
		
		
		
		final BigInteger ONE = BigInteger.ONE;
		
		final BigInteger TWO = BigInteger.valueOf( 2 );
		
		final BigInteger THREE = BigInteger.valueOf( 3 );
		
		
		
		
		final HashSet<BigInteger> bivecA = new HashSet<BigInteger>();
		bivecA.add( ONE );
		bivecA.add( TWO );
		
		
		
		
		final HashSet<BigInteger> bivecB = new HashSet<BigInteger>();
		bivecB.add( TWO );
		bivecB.add( THREE );
		
		
		
		
		
		HashSet<BigInteger> direcONE = new HashSet<BigInteger>();
		direcONE.add( ONE );
		
		
		
		HashSet<BigInteger> direcTWO = new HashSet<BigInteger>();
		direcTWO.add( TWO );
		
		
		
		HashSet<BigInteger> direcTHREE = new HashSet<BigInteger>();
		direcTHREE.add( THREE );
	
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ma = se.zero();
		
		ma.setVal(bivecA, new DoubleElem(1.0) );
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> maa = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		maa.add( ma );
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mb = se.zero();
		
		mb.setVal(bivecB, new DoubleElem(1.0) );
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> mba = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		mba.add( mb );
		
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b0 = se.zero();
		
		b0.setVal( direcONE , new DoubleElem( 0.5 + rand.nextDouble() ) );
		b0.setVal( direcTWO , new DoubleElem( 0.5 + rand.nextDouble() ) );
		
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b1 = se.zero();
		
		b1.setVal( direcTWO , new DoubleElem( 0.5 + rand.nextDouble() ) );
		b1.setVal( direcTHREE , new DoubleElem( 0.5 + rand.nextDouble() ) );
		
		
		
		
		
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> result = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		
		
		result = tdg.genPlaneIntersec(b0, ma, b1, mb, 20, 20);
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> rDirec = result.get( 0 );
		
		
		for( final HashSet<BigInteger> hs : rDirec.getKeySet() )
		{
			final DoubleElem d = rDirec.get( hs );
			final double dd = Math.abs( d.getVal() );
			if( hs.equals( direcTWO ) )
			{
				Assert.assertTrue( dd > 1E-6  );
			}
			else
			{
				Assert.assertTrue( dd < 1E-6  );
			}
		}
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> rPnt = result.get( 1 );
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ar = 
				( rPnt.add( b0.negate() ) ).handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, maa);
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> br = 
				( rPnt.add( b1.negate() ) ).handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, mba);
		
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : ar.getKeySet() )
		{
			final DoubleElem d = ar.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : br.getKeySet() )
		{
			final DoubleElem d = br.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		
		
	}
	
	
	
	/**
	 * Gets a BigInteger for a random unused direction.
	 * @param rand Input random number generator.
	 * @param hsrc Set of directions that are still available.
	 * @param lim Limit on number of dimensions over which to generate random numbers.
	 * @return The unused direction.
	 */
	BigInteger getVl( Random rand , HashSet<BigInteger> hsrc , int lim )
	{
		boolean done = false;
		while( !done )
		{
			BigInteger v = BigInteger.valueOf( rand.nextInt( lim ) );
			if( hsrc.contains( v ) )
			{
				hsrc.remove( v );
				return( v );
			}
		}
		return( null );
	}
	
	
	
	/**
	 * Tests simple plane surface-surface intersections (SSI) in 4-D.
	 * 
	 * @throws Throwable
	 */
	public void testInitialB( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		
		final Tdg_Intersec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> tdg = new Tdg_Intersec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
		
		
		for( int ccnt = 0 ; ccnt < 100 ; ++ccnt )
		{
		
		
			
		HashSet<BigInteger> hsrc = new HashSet<BigInteger>();
		for( BigInteger i = BigInteger.ZERO ; i.compareTo( td.getVal() ) < 0 ; i = i.add( BigInteger.ONE ) )
		{
			hsrc.add( i );
		}
		
		
		final BigInteger ONE = getVl( rand , hsrc , TestDimensionFour.FOUR );
		
		final BigInteger TWO = getVl( rand , hsrc , TestDimensionFour.FOUR );
		
		final BigInteger THREE = getVl( rand , hsrc , TestDimensionFour.FOUR );
		
		
		
		
		final HashSet<BigInteger> bivecA = new HashSet<BigInteger>();
		bivecA.add( ONE );
		bivecA.add( TWO );
		
		
		
		
		final HashSet<BigInteger> bivecB = new HashSet<BigInteger>();
		bivecB.add( TWO );
		bivecB.add( THREE );
		
		
		
		
		
		HashSet<BigInteger> direcONE = new HashSet<BigInteger>();
		direcONE.add( ONE );
		
		
		
		HashSet<BigInteger> direcTWO = new HashSet<BigInteger>();
		direcTWO.add( TWO );
		
		
		
		HashSet<BigInteger> direcTHREE = new HashSet<BigInteger>();
		direcTHREE.add( THREE );
	
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ma = se.zero();
		
		ma.setVal(bivecA, new DoubleElem(1.0) );
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> maa = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		maa.add( ma );
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mb = se.zero();
		
		mb.setVal(bivecB, new DoubleElem(1.0) );
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> mba = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		mba.add( mb );
		
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b0 = se.zero();
		
		b0.setVal( direcONE , new DoubleElem( 0.5 + rand.nextDouble() ) );
		b0.setVal( direcTWO , new DoubleElem( 0.5 + rand.nextDouble() ) );
		
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b1 = se.zero();
		
		b1.setVal( direcTWO , new DoubleElem( 0.5 + rand.nextDouble() ) );
		b1.setVal( direcTHREE , new DoubleElem( 0.5 + rand.nextDouble() ) );
		
		
		
		
		
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> result = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		
		
		result = tdg.genPlaneIntersec(b0, ma, b1, mb, 20, 20);
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> rDirec = result.get( 0 );
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> direcWedgeA =
				rDirec.handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, maa);
		

		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> direcWedgeB =
				rDirec.handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, mba);
		
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : direcWedgeA.getKeySet() )
		{
			final DoubleElem d = direcWedgeA.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : direcWedgeB.getKeySet() )
		{
			final DoubleElem d = direcWedgeB.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> rPnt = result.get( 1 );
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ar = 
				( rPnt.add( b0.negate() ) ).handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, maa);
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> br = 
				( rPnt.add( b1.negate() ) ).handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, mba);
		
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : ar.getKeySet() )
		{
			final DoubleElem d = ar.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : br.getKeySet() )
		{
			final DoubleElem d = br.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		
		
		}
		
	}
	
	
	
	/**
	 * Tests simple line-line intersections in 4-D.
	 * 
	 * @throws Throwable
	 */
	public void testLineIntersec( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		
		final Tdg_Intersec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> tdg = new Tdg_Intersec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
		
		
		for( int ccnt = 0 ; ccnt < 100 ; ++ccnt )
		{
			
			
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			dirONE = genRandomUnitVector( rand , se );
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			adirONE = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		adirONE.add( dirONE );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			dirTWO = genRandomUnitVector( rand , se );
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			adirTWO = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		adirTWO.add( dirTWO );
		
			
		
		
		/*
		 * Put the starting points b0 and b1 in the plane defined by the directions.
		 */
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b0 = se.zero();
		
		b0 = b0.add( scaleVal( dirONE , se , 2.0 * rand.nextDouble() - 1.0 ) );
		b0 = b0.add( scaleVal( dirTWO , se , 2.0 * rand.nextDouble() - 1.0 ) );
		
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b1 = se.zero();
		
		b1 = b1.add( scaleVal( dirONE , se , 2.0 * rand.nextDouble() - 1.0 ) );
		b1 = b1.add( scaleVal( dirTWO , se , 2.0 * rand.nextDouble() - 1.0 ) );
		
		
		
		
		

		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> rDirec 
			= tdg.genLnIntersec(b0, dirONE, b1, dirTWO);
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> direcWedgeA =
				( rDirec.add( b0.negate() ) ).handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, adirONE);
		

		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> direcWedgeB =
				( rDirec.add( b1.negate() ) ).handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, adirTWO);
		
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : direcWedgeB.getKeySet() )
		{
			final DoubleElem d = direcWedgeB.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : direcWedgeA.getKeySet() )
		{
			final DoubleElem d = direcWedgeA.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
	
		
		
		
		}
		
	}
	
	
	
	/**
	 * Tests simple plane surface-surface intersections (SSI) in 4-D.
	 * 
	 * @throws Throwable
	 */
	public void testInitialC( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		
		final Tdg_Intersec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> tdg = new Tdg_Intersec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
		
		
		for( int ccnt = 0 ; ccnt < 100 ; ++ccnt )
		{
			
			
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			dirONE = genRandomUnitVector( rand , se );
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			adirONE = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		adirONE.add( dirONE );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			dirTWO = genRandomUnitVector( rand , se );
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			adirTWO = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		adirTWO.add( dirTWO );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			dirTHREE = genRandomUnitVector( rand , se );
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			adirTHREE = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		adirTHREE.add( dirTHREE );
			
		
		

	
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ma0
			= dirONE.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , adirTWO);
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ma 
			= makeBivectorUnit( ma0 , se );
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> maa = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		maa.add( ma );
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mb0
			= dirTWO.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , adirTHREE);
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mb 
			= makeBivectorUnit( mb0 , se );
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> mba = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		mba.add( mb );
		
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b0 = se.zero();
		
		b0 = b0.add( scaleVal( dirONE , se , 0.5 + rand.nextDouble() ) );
		b0 = b0.add( scaleVal( dirTWO , se , 0.5 + rand.nextDouble() ) );
		
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b1 = se.zero();
		
		b1 = b1.add( scaleVal( dirTWO , se , 0.5 + rand.nextDouble() ) );
		b1 = b1.add( scaleVal( dirTHREE , se , 0.5 + rand.nextDouble() ) );
		
		
		
		
		
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> result = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		
		
		result = tdg.genPlaneIntersec(b0, ma, b1, mb, 20, 20);
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> rDirec = result.get( 0 );
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> direcWedgeA =
				rDirec.handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, maa);
		

		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> direcWedgeB =
				rDirec.handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, mba);
		
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : direcWedgeB.getKeySet() )
		{
			final DoubleElem d = direcWedgeB.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : direcWedgeA.getKeySet() )
		{
			final DoubleElem d = direcWedgeA.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> rPnt = result.get( 1 );
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> ar = 
				( rPnt.add( b0.negate() ) ).handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, maa);
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> br = 
				( rPnt.add( b1.negate() ) ).handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE, mba);
		
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : ar.getKeySet() )
		{
			final DoubleElem d = ar.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		// Assert all zero
		for( final HashSet<BigInteger> hs : br.getKeySet() )
		{
			final DoubleElem d = br.get( hs );
			final double dd = Math.abs( d.getVal() );
			Assert.assertTrue( dd < 1E-6  );
		}
		
		
		
		
		}
		
	}
	
	
	
	
	
	

}




