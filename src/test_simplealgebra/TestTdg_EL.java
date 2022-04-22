



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

import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.tdg.Tdg_EL_Facade;


/**
 * Tests the ability to generate geometric measurements.
 * Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestTdg_EL extends TestCase {
	
	

	
	
	/**
	 * Tests hypersphere surface volume.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testHypersphereSurfaceVolume( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> sfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> pi = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( Math.PI ) , dfac.zero() );
		
		final Tdg_EL_Facade<DoubleElem,DoubleElemFactory> tdg = new Tdg_EL_Facade<DoubleElem,DoubleElemFactory>( pi );
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			final double rad = rand.nextDouble();
			
			
			final DoubleElem radius = new DoubleElem( rad );
			
	
			final DoubleElem result = tdg.calcHypersphereSurfaceVolume(radius, 20);
			
			/*
			 * Known formula for hypersphere surface volume.
			 */
			final DoubleElem known = new DoubleElem( 2.0 * Math.PI * Math.PI * rad * rad * rad );
			
			
			Assert.assertTrue( Math.abs( result.add( known.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
	}
	
	
	
	
	/**
	 * Tests sphere surface area.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testSphereSurfaceArea( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> sfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> pi = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( Math.PI ) , dfac.zero() );
		
		final Tdg_EL_Facade<DoubleElem,DoubleElemFactory> tdg = new Tdg_EL_Facade<DoubleElem,DoubleElemFactory>( pi );
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			final double rad = rand.nextDouble();
			
			
			final DoubleElem radius = new DoubleElem( rad );
			
	
			final DoubleElem result = tdg.calcSphereSurfaceArea(radius, 20);
			
			/*
			 * Known formula for sphere surface area.
			 */
			final DoubleElem known = new DoubleElem( 4.0 * Math.PI * rad * rad );
			
			
			Assert.assertTrue( Math.abs( result.add( known.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
	}
	
	
	
	
	/**
	 * Tests circle circumfrence.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testCircleCircumfrence( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> sfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> pi = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( Math.PI ) , dfac.zero() );
		
		final Tdg_EL_Facade<DoubleElem,DoubleElemFactory> tdg = new Tdg_EL_Facade<DoubleElem,DoubleElemFactory>( pi );
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			final double rad = rand.nextDouble();
			
			
			final DoubleElem radius = new DoubleElem( rad );
			
	
			final DoubleElem result = tdg.calcCircleCircumfrence(radius, 20);
			
			/*
			 * Known formula for circle circumfrence.
			 */
			final DoubleElem known = new DoubleElem( 2.0 * Math.PI * rad );
			
			
			Assert.assertTrue( Math.abs( result.add( known.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
	}
	
	
	
	
	/**
	 * Tests line segment length.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testLineSegmentLength( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> sfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> pi = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( Math.PI ) , dfac.zero() );
		
		final Tdg_EL_Facade<DoubleElem,DoubleElemFactory> tdg = new Tdg_EL_Facade<DoubleElem,DoubleElemFactory>( pi );
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			final double rad = rand.nextDouble();
			
			
			final DoubleElem radius = new DoubleElem( rad );
			
	
			final DoubleElem result = tdg.calcLineSegmentLength(radius, 20);
			
			/*
			 * Known formula for line segment length.
			 */
			final DoubleElem known = new DoubleElem( 2.0 * rad );
			
			
			Assert.assertTrue( Math.abs( result.add( known.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
	}
	
	
	
	
	/**
	 * Tests circle area.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testCircleArea( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> sfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> pi = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( Math.PI ) , dfac.zero() );
		
		final Tdg_EL_Facade<DoubleElem,DoubleElemFactory> tdg = new Tdg_EL_Facade<DoubleElem,DoubleElemFactory>( pi );
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			final double rad = rand.nextDouble();
			
			
			final DoubleElem radius = new DoubleElem( rad );
			
	
			final DoubleElem result = tdg.calcCircleArea(radius, 20);
			
			/*
			 * Known formula for circle area.
			 */
			final DoubleElem known = new DoubleElem( Math.PI * rad * rad );
			
			
			Assert.assertTrue( Math.abs( result.add( known.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
	}
	
	
	
	
	/**
	 * Tests sphere volume.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testSphereVolume( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> sfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> pi = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( Math.PI ) , dfac.zero() );
		
		final Tdg_EL_Facade<DoubleElem,DoubleElemFactory> tdg = new Tdg_EL_Facade<DoubleElem,DoubleElemFactory>( pi );
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			final double rad = rand.nextDouble();
			
			
			final DoubleElem radius = new DoubleElem( rad );
			
	
			final DoubleElem result = tdg.calcSphereVolume(radius, 20);
			
			/*
			 * Known formula for sphere volume.
			 */
			final DoubleElem known = new DoubleElem( ( 4.0 / 3.0 ) * Math.PI * rad * rad * rad );
			
			
			Assert.assertTrue( Math.abs( result.add( known.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
	}
	
	
	
	
	/**
	 * Tests hypersphere hypervolume.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testHypersphereHypervolume( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> sfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> pi = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( Math.PI ) , dfac.zero() );
		
		final Tdg_EL_Facade<DoubleElem,DoubleElemFactory> tdg = new Tdg_EL_Facade<DoubleElem,DoubleElemFactory>( pi );
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			
			final double rad = rand.nextDouble();
			
			
			final DoubleElem radius = new DoubleElem( rad );
			
	
			final DoubleElem result = tdg.calcHypersphereHypervolume(radius, 20);
			
			/*
			 * Known formula for hypersphere hypervolume.
			 */
			final DoubleElem known = new DoubleElem( ( 1.0 / 2.0 ) * Math.PI * Math.PI * rad * rad * rad * rad );
			
			
			Assert.assertTrue( Math.abs( result.add( known.negate() ).getVal() ) < 0.0001 );
			
			
		}
		
		
	}
	
	
	
	
	
	
}



