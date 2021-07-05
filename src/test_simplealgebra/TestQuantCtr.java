



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

import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.ComplexElem.ComplexCmd;
import junit.framework.TestCase;


/**
 * Tests the ability to generate points on a wave.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestQuantCtr extends TestCase {

	public TestQuantCtr(String name) {
		super(name);
	}
	
	
	/**
	 * The start voxel location of the flat initial condition.
	 */
	private static final int XSST = 6;
	
	/**
	 * The amplitude of the initial condition phase oscillations.
	 */
	private static final double DSSX = 10000.0;
	
	/**
	 * The phase rate of the initial conditions in radians over the full NUM_T_ITER.
	 */
	private static final double ERATE = -20.0;
	
	/**
	 * The phase offset of the second initial condition in radians.
	 */
	private static final double EPHASEB = 0.0;
	
	
	protected static final int NUM_T_ITER = 250;
	
	
	
	
	
	
	final static double[] ctrRe = new double[ NUM_T_ITER ];
			
			
	final static double[] ctrIm = new double[ NUM_T_ITER ];
	
	
	
	
	
	
	public void testQuantCtr() throws Throwable
	{
		System.out.println( "Initializing ctr..." );
		final double IMMULT = 20.0;
		double startRe = 1.0;
		double startIm = 0.0;
		final DoubleElemFactory dfac = new DoubleElemFactory();
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		boolean done = false;
		while( !done )
		{
			startRe = startRe * 2.0;
			startIm = startIm * 2.0;
			done = true;
			double prevRe = startRe;
			double prevIm = startIm;
			for( int tv = 0 ; tv < NUM_T_ITER ; tv++ )
			{
				double avRep = IMMULT * DSSX * - Math.sin( ERATE * tv / NUM_T_ITER + EPHASEB ) * ERATE / NUM_T_ITER;
				double avImp = DSSX * Math.cos( ERATE * tv / NUM_T_ITER + EPHASEB ) * ERATE / NUM_T_ITER;
				avImp = -Math.abs( avImp );
				ComplexElem<DoubleElem,DoubleElemFactory> eval = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( avRep ) , new DoubleElem( avImp ) );
				ComplexElem<DoubleElem,DoubleElemFactory> prev = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( prevRe ) , new DoubleElem( prevIm ) );
				ComplexElem<DoubleElem,DoubleElemFactory> prevConj = prev.handleOptionalOp( ComplexCmd.CONJUGATE_LEFT , null );
				ComplexElem<DoubleElem,DoubleElemFactory> prevConjInv = prevConj.invertLeft();
				ComplexElem<DoubleElem,DoubleElemFactory> sdv = prev.add( eval.mult( prevConjInv ) );
				ctrRe[ tv ] = sdv.getRe().getVal();
				ctrIm[ tv ] = sdv.getIm().getVal();
				if( ( prevRe * ctrRe[ tv ] ) + ( prevIm * ctrIm[ tv ] ) <= 0.0 )
				{
					done = false;
				}
				prevRe = ctrRe[ tv ];
				prevIm = ctrIm[ tv ];
			}
		}
		
		
		for( int tv = 0 ; tv < NUM_T_ITER ; tv++ )
		{
			ctrRe[ tv ] = ctrRe[ tv ] * 10.0;
			ctrIm[ tv ] = ctrIm[ tv ] * 10.0;
		}
		
		
		
		System.out.println( "Finished Initializing ctr..." );
		
		System.out.println( "---" );
		for( int tv = 0 ; tv < NUM_T_ITER ; tv++ )
		{
			final double mag = Math.sqrt( ctrRe[ tv ] * ctrRe[ tv ] + ctrIm[ tv ] * ctrIm[ tv ] );
			// System.out.println( "---" );
			// System.out.println( ctrRe[ tv ] );
			// System.out.println( ctrIm[ tv ] );
			System.out.println( tv + " " + mag );
		}
		
	}

}


