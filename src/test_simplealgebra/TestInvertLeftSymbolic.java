






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
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.AbstractCache;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicInvertLeft;
import simplealgebra.symbolic.SymbolicInvertRight;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicNegate;

import java.io.*;


/**
 * Performs a left invert, and then verifies that the left invert contains no right invert terms.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestInvertLeftSymbolic extends TestCase 
{
	
	/**
	 * The number of dimensions for the matrix.
	 */
	final static int NUM_DIM = TestDimensionFour.FOUR;
	
	
	/**
	 * Symbolic elem for <math display="inline">
     * <mrow>
     *  <mi>a</mi>
     * </mrow>
     * </math> to be used in the test.
	 * 
	 * @author tgreen
	 *
	 */
	private static class AElem extends SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
	{
		/**
		 * The row of the elem.
		 */
		private BigInteger row;
		
		/**
		 * The column of the elem.
		 */
		private BigInteger col;

		
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _row The row of the elem.
		 * @param _col The column of the elem.
		 */
		public AElem(SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> _fac , BigInteger _row , BigInteger _col ) {
			super(_fac);
			row = _row;
			col = _col;
		}

		@Override
		public SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>, SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>, SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		/**
		 * Gets the row of the elem.
		 * 
		 * @return The row of the elem.
		 */
		public BigInteger getRow() {
			return row;
		}

		/**
		 * Gets the column of the elem.
		 * 
		 * @return The column of the elem.
		 */
		public BigInteger getCol() {
			return col;
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof AElem )
			{
				return( ( row.equals( ((AElem) b).getRow() ) ) && ( col.equals( ((AElem) b).getCol() ) ) );
			}
			
			return( false );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				final String sta = fac.writeDesc( (WriteElemCache<SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				String starow = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( row , ps );
				String stacol = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( col , ps );
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( AElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( AElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( starow );
				ps.print( " , " );
				ps.print( stacol );
				ps.println( " );" );
			}
			return( st );
		}

		
	}
	
	
	/**
	 * Verifies that there are no right invert terms in the expression.  Asserts if a right invert is found.
	 * 
	 * @param in The expression to check.
	 */
	private void verifyNoInvertRight( 
			SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> in )
	{
		if( in instanceof AElem )
		{
			return;
		}
		
		if( in instanceof SymbolicIdentity )
		{
			return;
		}
		
		if( in instanceof SymbolicAdd )
		{
			SymbolicAdd<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> add = (SymbolicAdd<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>) in;
			verifyNoInvertRight( add.getElemA() );
			verifyNoInvertRight( add.getElemB() );
			return;
		}
		
		
		if( in instanceof SymbolicNegate )
		{
			SymbolicNegate<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> neg = (SymbolicNegate<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>) in;
			verifyNoInvertRight( neg.getElem() );
			return;
		}
		
		
		if( in instanceof SymbolicMult )
		{
			SymbolicMult<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> add = (SymbolicMult<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>) in;
			verifyNoInvertRight( add.getElemA() );
			verifyNoInvertRight( add.getElemB() );
			return;
		}
		
		
		if( in instanceof SymbolicInvertLeft )
		{
			SymbolicInvertLeft<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> inv = (SymbolicInvertLeft<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>) in;
			verifyNoInvertRight( inv.getElem() );
			return;
		}
		
		
		if( in instanceof SymbolicInvertRight )
		{
			Assert.assertTrue( false );
		}
		
		
		throw( new RuntimeException( "Not Recognized" ) );
		
		
	}
	
	
	
	
	/**
	 * Performs a left invert, and then verifies that the left invert contains no right invert terms.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testInvertRightElems() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>(se);
		
		final SquareMatrixElemFactory<TestDimensionFour,
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>> se2 = 
				new SquareMatrixElemFactory<TestDimensionFour,
				SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>>(ye, td);
		
		final SquareMatrixElem<TestDimensionFour,
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>>
			mat = se2.zero();
		
		
		
		int i;
		int j;
		
		for( i = 0 ; i < NUM_DIM ; i++ )
		{
			for( j = 0 ; j < NUM_DIM ; j++ )
			{
				AElem val = new AElem( se , BigInteger.valueOf(i) , BigInteger.valueOf(j) );
				mat.setVal( BigInteger.valueOf(i) , BigInteger.valueOf(j) , val );
			}
		}
		
		
		
		final SquareMatrixElem<TestDimensionFour,
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>>
			inv = mat.invertLeft();
		
		
		
		
		for( i = 0 ; i < NUM_DIM ; i++ )
		{
			for( j = 0 ; j < NUM_DIM ; j++ )
			{
				SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
					el = inv.get( BigInteger.valueOf(i) , BigInteger.valueOf(j) );
				verifyNoInvertRight( el );
			}
		}
		
		
//		final SquareMatrixElem<TestDimensionFour,
//			SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>>
//				shouldBeIdent = inv.mult( mat );
//		for( i = 0 ; i < NUM_DIM ; i++ )
//		{
//			for( j = 0 ; j < NUM_DIM ; j++ )
//			{
//				SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
//					el = shouldBeIdent.get( BigInteger.valueOf(i) , BigInteger.valueOf(j) );
//				el = el.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null );
//				System.out.println( el.writeString() );
//			}
//		}
		
		
	}
	
	

	
}


