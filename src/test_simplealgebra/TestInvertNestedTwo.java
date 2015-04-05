



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
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;

/**
 * Tests inverses for a Matrix Algebra <math display="inline">
 * <mrow>
 *   <msub>
 *          <mi>M</mi>
 *        <mn>2</mn>
 *  </msub>
 *  <mfenced open="(" close=")" separators=",">
 *    <mrow>
 *      <msub>
 *              <mi>M</mi>
 *            <mn>2</mn>
 *      </msub>
 *      <mfenced open="(" close=")" separators=",">
 *        <mrow>
 *          <mi>R</mi>
 *        </mrow>
 *      </mfenced>
 *    </mrow>
 *  </mfenced>
 * </mrow>
 * </math>.  For more information see:
 * 
 * http://en.wikipedia.org/wiki/Matrix_ring
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestInvertNestedTwo extends TestCase {
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertLeft()}.
	 */
	public void testInvertLeft() throws NotInvertibleException
	{
		seedTestInvertLeft( 1111 );
		seedTestInvertLeft( 2222 );
		seedTestInvertLeft( 3333 );
		seedTestInvertLeft( 4444 );
		seedTestInvertLeft( 5555 );
		seedTestInvertLeft( 6666 );
		seedTestInvertLeft( 7777 );
		seedTestInvertLeft( 8888 );
		seedTestInvertLeft( 9999 );
	}
	
	
	/**
	 * Generates a random matrix for a Matrix Algebra <math display="inline">
     * <mrow>
     *  <msub>
     *          <mi>M</mi>
     *        <mn>2</mn>
     *  </msub>
     *  <mfenced open="(" close=")" separators=",">
     *    <mrow>
     *      <mi>R</mi>
     *    </mrow>
     *  </mfenced>
     * </mrow>
     * </math>
	 * 
	 * @param rand The random number generator.
	 * @param se The factory for the enclosed type.
	 * @return The random matrix.
	 */
	protected SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> generateMat( final Random rand,
			SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> se )
	{
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> mat = se.zero();
		
		int i;
		int j;
		
		for( i = 0 ; i < TestDimensionTwo.TWO ; i++ )
		{
			for( j = 0 ; j < TestDimensionTwo.TWO ; j++ )
			{
				DoubleElem val = new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 );
				mat.setVal( BigInteger.valueOf(i) , BigInteger.valueOf(j) , val );
			}
		}
		
		mat.validate();
		
		return( mat );
	}


	/**
	 * Verifies that for matrices <math display="inline">
     * <mrow>
     *  <msup>
     *          <mi>A</mi>
     *        <mo>-1L</mo>
     *  </msup>
     *  <mi>A</mi>
     *  <mo>=</mo>
     *  <mi>I</mi>
     * </mrow>
     * </math>
     * where <math display="inline">
     * <mrow>
     *  <mi>I</mi>
     * </mrow>
     * </math> is the identity.
	 * 
	 * @param seed The random number seed for generating the matrix.
	 * @throws NotInvertibleException
	 */
	private void seedTestInvertLeft( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElemFactory<TestDimensionTwo,
			SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>> se2 = 
				new SquareMatrixElemFactory<TestDimensionTwo,
				SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>>(se, td);
		
		final SquareMatrixElem<TestDimensionTwo, 
			SquareMatrixElem<TestDimensionTwo, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionTwo, DoubleElem, DoubleElemFactory>> mat = se2.zero();
		
		int i;
		int j;
		
		for( i = 0 ; i < TestDimensionTwo.TWO ; i++ )
		{
			for( j = 0 ; j < TestDimensionTwo.TWO ; j++ )
			{
				SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> val = generateMat( rand , se );
				mat.setVal( BigInteger.valueOf(i) , BigInteger.valueOf(j) , val );
			}
		}
		
		mat.validate();
		
		final SquareMatrixElem<TestDimensionTwo, 
			SquareMatrixElem<TestDimensionTwo, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionTwo, DoubleElem, DoubleElemFactory>> inv = mat.invertLeft();
		
		final SquareMatrixElem<TestDimensionTwo, 
			SquareMatrixElem<TestDimensionTwo, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionTwo, DoubleElem, DoubleElemFactory>> shouldBeIdent = inv.mult( mat );
		
		
		mat.validate();
		inv.validate();
		shouldBeIdent.validate();
		
		
		
		for( i = 0 ; i < TestDimensionTwo.TWO ; i++ )
		{
			for( j = 0 ; j < TestDimensionTwo.TWO ; j++ )
			{
				
				
				int i2;
				int j2;
				
				for( i2 = 0 ; i2 < TestDimensionTwo.TWO ; i2++ )
				{
					for( j2 = 0 ; j2 < TestDimensionTwo.TWO ; j2++ )
					{
						final double matchVal = ( i == j ) && ( i2 == j2 ) ? 1.0 : 0.0;
						
						Assert.assertEquals( matchVal , 
								shouldBeIdent.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).
								getVal(BigInteger.valueOf(i2) , BigInteger.valueOf(j2) ).getVal() , 1E-7 );
						
					}
				}
			}
		}
		
	}
	
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertRight()}.
	 */
	public void testInvertRight() throws NotInvertibleException
	{
		seedTestInvertRight( 1111 );
		seedTestInvertRight( 2222 );
		seedTestInvertRight( 3333 );
		seedTestInvertRight( 4444 );
		seedTestInvertRight( 5555 );
		seedTestInvertRight( 6666 );
		seedTestInvertRight( 7777 );
		seedTestInvertRight( 8888 );
		seedTestInvertRight( 9999 );
	}

	
	/**
	 * Verifies for matrices that <math display="inline">
     * <mrow>
     *  <mi>A</mi>
     *  <msup>
     *          <mi>A</mi>
     *        <mo>-1R</mo>
     *  </msup>
     *  <mo>=</mo>
     *  <mi>I</mi>
     * </mrow>
     * </math> 
     * where <math display="inline">
     * <mrow>
     *  <mi>I</mi>
     * </mrow>
     * </math> is the identity.
	 * 
	 * @param seed The random number seed for generating the matrix.
	 * @throws NotInvertibleException
	 */
	private void seedTestInvertRight( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElemFactory<TestDimensionTwo,
			SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>> se2 = 
				new SquareMatrixElemFactory<TestDimensionTwo,
				SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>>(se, td);
		
		final SquareMatrixElem<TestDimensionTwo, 
			SquareMatrixElem<TestDimensionTwo, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionTwo, DoubleElem, DoubleElemFactory>> mat = se2.zero();
		
		int i;
		int j;
		
		for( i = 0 ; i < TestDimensionTwo.TWO ; i++ )
		{
			for( j = 0 ; j < TestDimensionTwo.TWO ; j++ )
			{
				SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> val = generateMat( rand , se );
				mat.setVal( BigInteger.valueOf(i) , BigInteger.valueOf(j) , val );
			}
		}
		
		mat.validate();
		
		final SquareMatrixElem<TestDimensionTwo, 
			SquareMatrixElem<TestDimensionTwo, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionTwo, DoubleElem, DoubleElemFactory>> inv = mat.invertRight();
		
		final SquareMatrixElem<TestDimensionTwo, 
			SquareMatrixElem<TestDimensionTwo, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionTwo, DoubleElem, DoubleElemFactory>> shouldBeIdent = mat.mult( inv );
		
		
		mat.validate();
		inv.validate();
		shouldBeIdent.validate();
		
	
		
		for( i = 0 ; i < TestDimensionTwo.TWO ; i++ )
		{
			for( j = 0 ; j < TestDimensionTwo.TWO ; j++ )
			{
				
				
				int i2;
				int j2;
				
				for( i2 = 0 ; i2 < TestDimensionTwo.TWO ; i2++ )
				{
					for( j2 = 0 ; j2 < TestDimensionTwo.TWO ; j2++ )
					{
						final double matchVal = ( i == j ) && ( i2 == j2 ) ? 1.0 : 0.0;
						
						Assert.assertEquals( matchVal , 
								shouldBeIdent.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j)  ).
								getVal(BigInteger.valueOf(i2) , BigInteger.valueOf(j2)  ).getVal() , 1E-7 );
						
					}
				}
			}
		}
		
	}

	
}

