



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
import simplealgebra.NotInvertibleException;

/**
 * Tests for scalar <math display="inline">
 * <mrow>
 *  <msup>
 *          <mo>e</mo>
 *        <mi>x</mi>
 *  </msup>
 * </mrow>
 * </math>, sin( x ) and cos( x )
 * 
 *   Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestScalarTrig extends TestCase {
	
	
	
	/**
	 * Tests for scalar <math display="inline">
	 * <mrow>
	 *  <msup>
	 *          <mo>e</mo>
	 *        <mi>x</mi>
	 *  </msup>
	 * </mrow>
	 * </math>, sin( x ) and cos( x )
	 *
	 */
	public void testScalarTrig() throws NotInvertibleException
	{
		seedTestScalarTrig( 1111 );
		seedTestScalarTrig( 2222 );
		seedTestScalarTrig( 3333 );
		seedTestScalarTrig( 4444 );
		seedTestScalarTrig( 5555 );
		seedTestScalarTrig( 6666 );
		seedTestScalarTrig( 7777 );
		seedTestScalarTrig( 8888 );
		seedTestScalarTrig( 9999 );
		seedTestScalarTrig( 11111 );
		seedTestScalarTrig( 22222 );
		seedTestScalarTrig( 33333 );
		seedTestScalarTrig( 44444 );
		seedTestScalarTrig( 55555 );
		seedTestScalarTrig( 66666 );
		seedTestScalarTrig( 77777 );
		seedTestScalarTrig( 88888 );
		seedTestScalarTrig( 99999 );
	}

	
	/**
	 * Tests for scalar <math display="inline">
	 * <mrow>
	 *  <msup>
	 *          <mo>e</mo>
	 *        <mi>x</mi>
	 *  </msup>
	 * </mrow>
	 * </math>, sin( x ) and cos( x )
	 * 
	 * @param seed Random number seed for generating a random "x".
	 * @throws NotInvertibleException
	 */
	private void seedTestScalarTrig( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final double iVal = 20.0 * ( rand.nextDouble() - 0.5 );
		
		final DoubleElem el = new DoubleElem( iVal );
		
		final DoubleElem exp = el.exp( 10 );
		
		final DoubleElem sin = el.sin( 10 );
		
		final DoubleElem cos = el.cos( 10 );
		
		Assert.assertEquals( Math.exp( iVal ) , exp.getVal() , 1E-2 );
		
		Assert.assertEquals( Math.sin( iVal ) , sin.getVal() , 1E-5 );
		
		Assert.assertEquals( Math.cos( iVal ) , cos.getVal() , 1E-5 );
	}
	
	
}

