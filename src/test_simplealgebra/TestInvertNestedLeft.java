



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
import java.util.*;

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
 *           <mi>M</mi>
 *         <mn>4</mn>
 *   </msub>
 *   <mo>(</mo>
 *   <msub>
 *           <mi>M</mi>
 *         <mn>4</mn>
 *   </msub>
 *   
 *     <mrow>
 *       <mo>(</mo>
 *       <mi>R</mi>
 *       <mo>)</mo>
 *       <mo>)</mo>
 *     </mrow>
 *   
 * </mrow>
 * </math>
 * .  For more information see:
 * 
 * http://en.wikipedia.org/wiki/Matrix_ring
 * 
 * 
 * @author thorngreen
 *
 */
public class TestInvertNestedLeft extends TestCase {
	
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
	 * Generates a random matrix for a Matrix Algebra M_4(R).
	 * 
	 * @param rand The random number generator.
	 * @param se The factory for the enclosed type.
	 * @return The random matrix.
	 */
	protected SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> generateMat( final Random rand,
			SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se )
	{
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = se.zero();
		
		int i;
		int j;
		
		for( i = 0 ; i < 4 ; i++ )
		{
			for( j = 0 ; j < 4 ; j++ )
			{
				DoubleElem val = new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 );
				mat.setVal( BigInteger.valueOf(i) , BigInteger.valueOf(j) , val );
			}
		}
		
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
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElemFactory<TestDimensionFour,
			SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> se2 = 
				new SquareMatrixElemFactory<TestDimensionFour,
				SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>(se, td);
		
		final SquareMatrixElem<TestDimensionFour, 
			SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> mat = se2.zero();
		
		int i;
		int j;
		
		for( i = 0 ; i < 4 ; i++ )
		{
			for( j = 0 ; j < 4 ; j++ )
			{
				SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> val = generateMat( rand , se );
				mat.setVal( BigInteger.valueOf(i) , BigInteger.valueOf(j) , val );
			}
		}
		
		final SquareMatrixElem<TestDimensionFour, 
			SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> inv = mat.invertLeft();
		
		final SquareMatrixElem<TestDimensionFour, 
			SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> shouldBeIdent = inv.mult( mat );
		
		
		for( i = 0 ; i < 4 ; i++ )
		{
			for( j = 0 ; j < 4 ; j++ )
			{
				
				
				int i2;
				int j2;
				
				for( i2 = 0 ; i2 < 4 ; i2++ )
				{
					for( j2 = 0 ; j2 < 4 ; j2++ )
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
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertLeftRevCoeff()}.
	 */
	public void testInvertLeftRevCoeff() throws NotInvertibleException
	{
		seedTestInvertLeftRevCoeff( 1111 );
		seedTestInvertLeftRevCoeff( 2222 );
		seedTestInvertLeftRevCoeff( 3333 );
		seedTestInvertLeftRevCoeff( 4444 );
		seedTestInvertLeftRevCoeff( 5555 );
		seedTestInvertLeftRevCoeff( 6666 );
		seedTestInvertLeftRevCoeff( 7777 );
		seedTestInvertLeftRevCoeff( 8888 );
		seedTestInvertLeftRevCoeff( 9999 );
	}

	
	/**
	 * Verifies matrix reversed-coefficient left-inverses.
	 * 
	 * @param seed The random number seed for generating the matrix.
	 * @throws NotInvertibleException
	 */
	private void seedTestInvertLeftRevCoeff( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElemFactory<TestDimensionFour,
			SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> se2 = 
				new SquareMatrixElemFactory<TestDimensionFour,
				SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>(se, td);
		
		final SquareMatrixElem<TestDimensionFour, 
			SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> mat = se2.zero();
		
		int i;
		int j;
		
		for( i = 0 ; i < 4 ; i++ )
		{
			for( j = 0 ; j < 4 ; j++ )
			{
				SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> val = generateMat( rand , se );
				mat.setVal( BigInteger.valueOf(i) , BigInteger.valueOf(j) , val );
			}
		}
		
		final SquareMatrixElem<TestDimensionFour, 
			SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> inv = 
				mat.handleOptionalOp(SquareMatrixElem.SquareMatrixCmd.INVERT_LEFT_REV_COEFF, null);
		
		
		ArrayList<SquareMatrixElem<TestDimensionFour, 
			SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>> args =
				new ArrayList<SquareMatrixElem<TestDimensionFour, 
				SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, 
				SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>>();
		
		args.add( mat );
		
		final SquareMatrixElem<TestDimensionFour, 
			SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, 
			SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> shouldBeIdent = 
				inv.handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.MULT_REV_COEFF , args);
		
		
		for( i = 0 ; i < 4 ; i++ )
		{
			for( j = 0 ; j < 4 ; j++ )
			{
				
				
				int i2;
				int j2;
				
				for( i2 = 0 ; i2 < 4 ; i2++ )
				{
					for( j2 = 0 ; j2 < 4 ; j2++ )
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

