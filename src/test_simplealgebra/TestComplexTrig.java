



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
import simplealgebra.NotInvertibleException;

/**
 * Tests for complex-number <math display="inline">
 * <mrow>
 *  <msup>
 *          <mo>e</mo>
 *        <mi>x</mi>
 *  </msup>
 * </mrow>
 * </math>
 * 
 * 
 * @author thorngreen
 *
 */
public class TestComplexTrig extends TestCase {
	
	
	
	/**
	 * Tests several complex-number <math display="inline">
     * <mrow>
     *  <msup>
     *          <mo>e</mo>
     *        <mi>x</mi>
     *  </msup>
     * </mrow>
     * </math> invocations against their known results.
	 */
	public void testComplexTrig() throws NotInvertibleException
	{
		seedTestComplexTrig( 1111 );
		seedTestComplexTrig( 2222 );
		seedTestComplexTrig( 3333 );
		seedTestComplexTrig( 4444 );
		seedTestComplexTrig( 5555 );
		seedTestComplexTrig( 6666 );
		seedTestComplexTrig( 7777 );
		seedTestComplexTrig( 8888 );
		seedTestComplexTrig( 9999 );
		seedTestComplexTrig( 11111 );
		seedTestComplexTrig( 22222 );
		seedTestComplexTrig( 33333 );
		seedTestComplexTrig( 44444 );
		seedTestComplexTrig( 55555 );
		seedTestComplexTrig( 66666 );
		seedTestComplexTrig( 77777 );
		seedTestComplexTrig( 88888 );
		seedTestComplexTrig( 99999 );
	}

	
	/**
	 * Tests a randomly-selected invocation of complex-number <math display="inline">
     * <mrow>
     *  <msup>
     *          <mo>e</mo>
     *        <mi>x</mi>
     *  </msup>
     * </mrow>
     * </math> against its known result.
	 * 
	 * @param seed The random number seed for generating the invocation.
	 * @throws NotInvertibleException
	 */
	private void seedTestComplexTrig( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final double iValRe = 20.0 * ( rand.nextDouble() - 0.5 );
		
		final double iValIm = 20.0 * ( rand.nextDouble() - 0.5 );
		
		final DoubleElem elRe = new DoubleElem( iValRe );
		
		final DoubleElem elIm = new DoubleElem( iValIm );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> el
			= new ComplexElem<DoubleElem,DoubleElemFactory>( elRe , elIm );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> exp = el.exp( 10 );
		
		final double expectedReal = Math.exp( iValRe ) * Math.cos( iValIm );
		
		final double expectedIm = Math.exp( iValRe ) * Math.sin( iValIm );
		
		Assert.assertEquals( expectedReal , exp.getRe().getVal() , 1E-2 );
		
		Assert.assertEquals( expectedIm , exp.getIm().getVal() , 1E-2 );
	}
	
	
}

