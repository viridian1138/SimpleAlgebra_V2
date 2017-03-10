



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
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;


/**
 * Tests the ability to alter a random function of a complex phase.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestCplxDegrade extends TestCase {

	
	/**
	 * The original starting magnitude.
	 */
	final static double ORIG_MAG = 10.0;
	
	
	/**
	 * The number of samples to consider.
	 */
	final static int MAX_SMPL = 1000;
	
	
	
	/**
	 * Generates one instance in which a complex phase is randomly altered.
	 * @param randSeed Random number seeding value.
	 * @return One instance in which the complex phase is randomly altered.
	 */
	protected static ComplexElem<DoubleElem,DoubleElemFactory> genDegradeResult( final long randSeed )
	{
		
		final Random rand = new Random( randSeed );
		
		ComplexElem<DoubleElem,DoubleElemFactory> cplx = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -ORIG_MAG ) , new DoubleElem( 0.0 ) );
		
		
		for( int cnt = 0 ; cnt < 16 ; cnt++ )
		{
			final DoubleElem magSq = (DoubleElem)( cplx.totalMagnitude() );
			
			final DoubleElem mag = new DoubleElem( Math.sqrt( magSq.getVal() ) );
			
			// Assume that the phase can't be controlled, and hence is selected randomly.
			final double phaseA = ( 2.0 * Math.PI ) * ( rand.nextDouble() );
			
			// Assume that the phase can't be controlled, and hence is selected randomly.
			final double phaseB = ( 2.0 * Math.PI ) * ( rand.nextDouble() );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> angA = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( phaseA ) );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> angB = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( phaseB ) );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> rotA = angA.exp( 20 );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> rotB = angB.exp( 20 );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> mult = new ComplexElem<DoubleElem,DoubleElemFactory>( mag , new DoubleElem( 0.0 ) );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> cplx2A = cplx.add( mult.mult( rotA ) );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> cplx2B = cplx.add( mult.mult( rotB ) );
			
			// "Collapse" each resulting phase and compare the results.
			if( ( (DoubleElem)( cplx2A.totalMagnitude() ) ).getVal() < ( (DoubleElem)( cplx2B.totalMagnitude() ) ).getVal() )
			{
				cplx = cplx2A;
			}
			else
			{
				cplx = cplx2B;
			}
			
		}
		
		return( cplx );
	}
	
	

	
	/**
	 * Tests the ability to alter a random function of a complex phase.
	 * 
	 * @throws Throwable
	 */
	public void testCplxDegrade() throws Throwable
	{
		int acnt = 0;
		
		for( int cnt = 0 ; cnt < MAX_SMPL ; cnt++ )
		{
			final ComplexElem<DoubleElem,DoubleElemFactory> cplx = genDegradeResult( 1000 * cnt + 77 );
			final DoubleElem magSq = (DoubleElem)( cplx.totalMagnitude() );
			
			// System.out.println( magSq.getVal() );
			
			if( magSq.getVal() < ORIG_MAG * ORIG_MAG )
			{
				acnt++;
			}
			
		}
		
		
		Assert.assertTrue( acnt >= ( MAX_SMPL / 2 ) );
		
	}
	
	

	
}



