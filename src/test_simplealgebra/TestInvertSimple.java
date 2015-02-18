



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
 * <msub>
 *         <mi>M</mi>
 *       <mn>4</mn>
 * </msub>
 * <mo>(</mo>
 * <mi>R</mi>
 * <mo>)</mo>
 * </mrow>
 * </math>.  For more information see:
 * 
 * http://en.wikipedia.org/wiki/Matrix_ring
 * 
 * 
 * @author thorngreen
 *
 */
public class TestInvertSimple extends TestCase {
	
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
		invertLeftEmpty();
	}
	
	
	/**
	 * Verifies that a left-invert of an empty matrix produces the correct exception.
	 */
	private void invertLeftEmpty()
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = se.zero();
		
		try
		{
			mat.invertLeft();
		}
		catch( NotInvertibleException ex )
		{
			return;
		}
		
		Assert.assertTrue( false );
	}


	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertLeft()}.
	 */
	private void seedTestInvertLeft( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
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
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> inv = mat.invertLeft();
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> shouldBeIdentA = mat.mult( inv );
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mat );
		
		for( i = 0 ; i < 4 ; i++ )
		{
			for( j = 0 ; j < 4 ; j++ )
			{
				final double matchVal = ( i == j ) ? 1.0 : 0.0;
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentA.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-10 );
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentB.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-10 );
				
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
		invertRightEmpty();
	}
	
	
	/**
	 * Verifies that a left-invert of an empty matrix produces the correct exception.
	 */
	private void invertRightEmpty()
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = se.zero();
		
		try
		{
			mat.invertRight();
		}
		catch( NotInvertibleException ex )
		{
			return;
		}
		
		Assert.assertTrue( false );
	}

	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertRight()}.
	 */
	private void seedTestInvertRight( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
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
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> inv = mat.invertRight();
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> shouldBeIdentA = mat.mult( inv );
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mat );
		
		for( i = 0 ; i < 4 ; i++ )
		{
			for( j = 0 ; j < 4 ; j++ )
			{
				final double matchVal = ( i == j ) ? 1.0 : 0.0;
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentA.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-10 );
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentB.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-10 );
				
			}
		}
		
	}
	
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertRight()}.
	 */
	public void testInvertSimilarity() throws NotInvertibleException
	{
		seedTestInvertSimilarity( 1111 );
		seedTestInvertSimilarity( 2222 );
		seedTestInvertSimilarity( 3333 );
		seedTestInvertSimilarity( 4444 );
		seedTestInvertSimilarity( 5555 );
		seedTestInvertSimilarity( 6666 );
		seedTestInvertSimilarity( 7777 );
		seedTestInvertSimilarity( 8888 );
		seedTestInvertSimilarity( 9999 );
	}
	
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertRight()}.
	 */
	private void seedTestInvertSimilarity( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
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
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> invR = mat.invertRight();
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> invL = mat.invertLeft();
		
		
		
		for( i = 0 ; i < 4 ; i++ )
		{
			for( j = 0 ; j < 4 ; j++ )
			{
				final double matchVal = invL.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal();
				
				Assert.assertEquals( matchVal , 
						invR.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-10 );
				
			}
		}
		
	}
	

	
}

