



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
import simplealgebra.DoubleElem;


/**
 * Tests the ability to calculate arctangeants.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestAtan2 extends TestCase {
	
	
	/**
	 * Tests whether two angles are equivalent.
	 * @param a1 The first angle to compare in units of radians.
	 * @param a2 The second angle to compare in units of radians.
	 */
	protected void compareAngles( double a1 , double a2 )
	{
		final double twop = 2.0 * Math.PI;
		while( ( a2 - a1 ) > twop )
		{
			a2 -= twop;
		}
		while( ( a1 - a2 ) > twop )
		{
			a2 += twop;
		}
		Assert.assertTrue( Math.abs( a1 - a2 ) < 1E-3 );
	}

	
	/**
	 * Tests the ability to calculate arctangents.
	 * @throws Throwable
	 */
	public void testAtan2() throws Throwable
	{
		final Random rand = new Random( 4444 );
		for( int cnt = 0 ; cnt < 10000 ; cnt++ )
		{
			System.out.println( cnt );
			final double x = 10.0 * ( rand.nextDouble() ) - 5.0;
			final double y = 10.0 * ( rand.nextDouble() ) - 5.0;
			final double stdAtan = Math.atan2( y , x );
			final DoubleElem xd = new DoubleElem( x );
			final DoubleElem yd = new DoubleElem( y );
			final DoubleElem atan = yd.atan2( xd , 20 , 20 );
		}
	}
	
	
	
	
}



