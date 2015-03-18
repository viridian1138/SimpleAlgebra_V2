



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
import java.util.HashSet;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;

/**
 * Tests inverses for sparse matrices.
 * 
 * 
 * @author thorngreen
 *
 */
public class TestInvertSparse extends TestCase {
	
	
	
	/**
	 * The number of dimensions.
	 */
	static final int FIFTY_INT = 50;
	
	/**
	 * The number of dimensions.
	 */
	static final BigInteger FIFTY = BigInteger.valueOf( FIFTY_INT );
	
	
	
	/**
	 * Test class for fifty-dimensional problems.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TestDimensionFifty extends NumDimensions {

		/**
		 * Constructs the NumDimensions.
		 */
		public TestDimensionFifty() {
			// Does Nothing
		}

		@Override
		public BigInteger getVal() {
			return( FIFTY );
		}

	}
	
	
	
	/**
	 * Generates a random sparse matrix for a Matrix Algebra M_50(R).
	 * 
	 * @param rand The random number generator.
	 * @param se The factory for the enclosed type.
	 * @return The random matrix.
	 */
	protected SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> generateMat( final Random rand,
			SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory> se )
	{
		final BigInteger FIFTY = BigInteger.valueOf( FIFTY_INT );
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> mat = se.zero();
		
		final HashSet<BigInteger> rows = new HashSet<BigInteger>();
		final HashSet<BigInteger> cols = new HashSet<BigInteger>();
		
		int cnt;
		for( cnt = 0 ; cnt < FIFTY_INT ; cnt++ )
		{
			rows.add( BigInteger.valueOf( cnt ) );
			cols.add( BigInteger.valueOf( cnt ) );
		}
		
		while( ( rows.size() > 0 ) || ( cols.size() > 0 ) )
		{
			BigInteger row = null;
			BigInteger col = null;
			
			if( rand.nextBoolean() )
			{
				row = BigInteger.valueOf( rand.nextInt( 2 ) );
				col = BigInteger.ZERO;
			}
			else
			{
				col = BigInteger.valueOf( rand.nextInt( 2 ) );
				row = BigInteger.ZERO;
			}
			
			while( ( row.compareTo( FIFTY ) < 0 ) && ( col.compareTo( FIFTY ) < 0 ) )
			{
				DoubleElem val = new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 );
				// DoubleElem val2 = new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 );
				mat.setVal( row , col , val );
				// mat.setVal( col , row , val2 );
				rows.remove( row );
				cols.remove( col );
				// rows.remove( col );
				// cols.remove( row );
				row = row.add( BigInteger.ONE );
				col = col.add( BigInteger.ONE );
			}
		}
		
		return( mat );
	}
	
	
	
	
	
	
	
	/**
	 * Generates a random sparse matrix for a Matrix Algebra M_50(R).
	 * 
	 * @param rand The random number generator.
	 * @param se The factory for the enclosed type.
	 * @return The random matrix.
	 */
	protected SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> generateMatRev( final Random rand,
			SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory> se )
	{	
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> mat = se.zero();
		
		final HashSet<BigInteger> rows = new HashSet<BigInteger>();
		final HashSet<BigInteger> cols = new HashSet<BigInteger>();
		
		int cnt;
		for( cnt = 0 ; cnt < FIFTY_INT ; cnt++ )
		{
			rows.add( BigInteger.valueOf( cnt ) );
			cols.add( BigInteger.valueOf( cnt ) );
		}
		
		while( ( rows.size() > 0 ) || ( cols.size() > 0 ) )
		{
			BigInteger row = null;
			BigInteger col = null;
			
			if( rand.nextBoolean() )
			{
				row = BigInteger.valueOf( rand.nextInt( 2 ) );
				col = BigInteger.ZERO;
			}
			else
			{
				col = BigInteger.valueOf( rand.nextInt( 2 ) );
				row = BigInteger.ZERO;
			}
			
			while( ( row.compareTo( FIFTY ) < 0 ) && ( col.compareTo( FIFTY ) < 0 ) )
			{
				DoubleElem val = new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 );
				// DoubleElem val2 = new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 );
				mat.setVal( col , row , val );
				// mat.setVal( col , row , val2 );
				rows.remove( row );
				cols.remove( col );
				// rows.remove( col );
				// cols.remove( row );
				row = row.add( BigInteger.ONE );
				col = col.add( BigInteger.ONE );
			}
		}
		
		return( mat );
	}
	
	
	
	
	/**
	 * Generates a random diagonal matrix for a Matrix Algebra M_50(R) with the rows offset.
	 * 
	 * @param rand The random number generator.
	 * @param se The factory for the enclosed type.
	 * @return The random matrix.
	 */
	protected SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> generateOffsetDiagonalMat( final Random rand,
			SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory> se )
	{
		final BigInteger FIFTY = BigInteger.valueOf( FIFTY_INT );
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> mat = se.zero();
		
		final HashSet<BigInteger> rows = new HashSet<BigInteger>();
		final HashSet<BigInteger> cols = new HashSet<BigInteger>();
		
		int cnt;
		for( cnt = 0 ; cnt < FIFTY_INT ; cnt++ )
		{
			rows.add( BigInteger.valueOf( cnt ) );
			cols.add( BigInteger.valueOf( cnt ) );
		}
		
		BigInteger row = null;
		BigInteger col = null;
		
		if( rand.nextBoolean() )
		{
			row = BigInteger.valueOf( rand.nextInt( 50 ) );
			col = BigInteger.ZERO;
		}
		else
		{
			col = BigInteger.valueOf( rand.nextInt( 50 ) );
			row = BigInteger.ZERO;
		}
		
		while( ( rows.size() > 0 ) || ( cols.size() > 0 ) )
		{
			DoubleElem val = new DoubleElem( 2.0 * ( rand.nextDouble() ) - 1.0 );
			mat.setVal( row , col , val );
			
			rows.remove( row );
			cols.remove( col );
			
			row = row.add( BigInteger.ONE );
			col = col.add( BigInteger.ONE );
			
			if( row.compareTo( FIFTY ) >= 0 )
			{
				row = BigInteger.ZERO;
			}
			
			if( col.compareTo( FIFTY ) >= 0 )
			{
				col = BigInteger.ZERO;
			}
			
		}
		
		return( mat );
	}
	
	
	

	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertLeft()} with sparse matrices.
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
		seedTestInvertLeft( 4321 );
		seedTestInvertLeft( 5432 );
		seedTestInvertLeft( 6543 );
	}
	
	
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertLeft()} with sparse diagonal row-offset matrices.
	 */
	public void testInvertLeftDiagonal() throws NotInvertibleException
	{
		seedTestInvertLeftDiagonal( 1111 );
		seedTestInvertLeftDiagonal( 2222 );
		seedTestInvertLeftDiagonal( 3333 );
		seedTestInvertLeftDiagonal( 4444 );
		seedTestInvertLeftDiagonal( 5555 );
		seedTestInvertLeftDiagonal( 6666 );
		seedTestInvertLeftDiagonal( 7777 );
		seedTestInvertLeftDiagonal( 8888 );
		seedTestInvertLeftDiagonal( 9999 );
		seedTestInvertLeftDiagonal( 4321 );
		seedTestInvertLeftDiagonal( 5432 );
		seedTestInvertLeftDiagonal( 6543 );
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
		
		final TestDimensionFifty td = new TestDimensionFifty();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> mat = generateMat( rand , se );
		
		int i;
		int j;
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> inv = mat.invertLeft();
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> shouldBeIdentA = mat.mult( inv );
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mat );
		
		for( i = 0 ; i < FIFTY_INT ; i++ )
		{
			for( j = 0 ; j < FIFTY_INT ; j++ )
			{
				final double matchVal = ( i == j ) ? 1.0 : 0.0;
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentA.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-9 );
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentB.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-9 );
				
			}
		}
		
	}
	
	
	/**
	 * Verifies that for row-offset diagonal matrices <math display="inline">
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
	private void seedTestInvertLeftDiagonal( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFifty td = new TestDimensionFifty();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> mat = generateOffsetDiagonalMat( rand , se );
		
		int i;
		int j;
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> inv = mat.invertLeft();
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> shouldBeIdentA = mat.mult( inv );
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mat );
		
		for( i = 0 ; i < FIFTY_INT ; i++ )
		{
			for( j = 0 ; j < FIFTY_INT ; j++ )
			{
				final double matchVal = ( i == j ) ? 1.0 : 0.0;
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentA.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-9 );
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentB.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-9 );
				
			}
		}
		
	}
	
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertRight()} with sparse matrices.
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
		seedTestInvertRight( 4321 );
		seedTestInvertRight( 5432 );
		seedTestInvertRight( 6543 );
	}
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertRight()} with sparse diagonal row-offset matrices.
	 */
	public void testInvertRightDiagonal() throws NotInvertibleException
	{
		seedTestInvertRightDiagonal( 1111 );
		seedTestInvertRightDiagonal( 2222 );
		seedTestInvertRightDiagonal( 3333 );
		seedTestInvertRightDiagonal( 4444 );
		seedTestInvertRightDiagonal( 5555 );
		seedTestInvertRightDiagonal( 6666 );
		seedTestInvertRightDiagonal( 7777 );
		seedTestInvertRightDiagonal( 8888 );
		seedTestInvertRightDiagonal( 9999 );
		seedTestInvertRightDiagonal( 4321 );
		seedTestInvertRightDiagonal( 5432 );
		seedTestInvertRightDiagonal( 6543 );
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
     * </math> is the identity.  Also verifies that the reverse-order holds for commutative nested elems.
	 * 
	 * @param seed The random number seed for generating the matrix.
	 * @throws NotInvertibleException
	 */
	private void seedTestInvertRight( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFifty td = new TestDimensionFifty();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> mat = generateMatRev( rand , se );
		
		int i;
		int j;
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> inv = mat.invertRight();
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> shouldBeIdentA = mat.mult( inv );
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mat );
		
		for( i = 0 ; i < FIFTY_INT ; i++ )
		{
			for( j = 0 ; j < FIFTY_INT ; j++ )
			{
				final double matchVal = ( i == j ) ? 1.0 : 0.0;
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentA.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-9 );
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentB.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-9 );
				
			}
		}
		
	}
	
	
	
	
	
	/**
	 * Verifies that for row-offset diagonal matrices <math display="inline">
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
     * </math> is the identity.  Also verifies that the reverse-order holds for commutative nested elems.
	 * 
	 * @param seed The random number seed for generating the matrix.
	 * @throws NotInvertibleException
	 */
	private void seedTestInvertRightDiagonal( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFifty td = new TestDimensionFifty();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> mat = generateOffsetDiagonalMat( rand , se );
		
		int i;
		int j;
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> inv = mat.invertRight();
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> shouldBeIdentA = mat.mult( inv );
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> shouldBeIdentB = inv.mult( mat );
		
		for( i = 0 ; i < FIFTY_INT ; i++ )
		{
			for( j = 0 ; j < FIFTY_INT ; j++ )
			{
				final double matchVal = ( i == j ) ? 1.0 : 0.0;
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentA.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-9 );
				
				Assert.assertEquals( matchVal , 
						shouldBeIdentB.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-9 );
				
			}
		}
		
	}
	
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.SquareMatrixElem#invertRight()} with sparse matrices.
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
		seedTestInvertSimilarity( 4321 );
		seedTestInvertSimilarity( 5432 );
		seedTestInvertSimilarity( 6543 );
	}
	
	
	
	
	
	/**
	 * Verifies that the matrix right-inverse and the left-inverse are identical for commutative nested elems.
	 * 
	 * @param seed The random number seed for generating the matrix.
	 * @throws NotInvertibleException
	 */
	private void seedTestInvertSimilarity( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final TestDimensionFifty td = new TestDimensionFifty();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFifty,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> mat = generateMat( rand , se );
		
		int i;
		int j;
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> invR = mat.invertRight();
		
		final SquareMatrixElem<TestDimensionFifty,DoubleElem,DoubleElemFactory> invL = mat.invertLeft();
		
		
		
		for( i = 0 ; i < FIFTY_INT ; i++ )
		{
			for( j = 0 ; j < FIFTY_INT ; j++ )
			{
				final double matchVal = invL.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal();
				
				Assert.assertEquals( matchVal , 
						invR.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getVal() , 1E-9 );
				
			}
		}
		
	}
	

	
}

