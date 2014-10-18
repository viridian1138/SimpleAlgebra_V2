






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
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicInvertLeft;
import simplealgebra.symbolic.SymbolicInvertRight;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicNegate;



public class TestInvertRightSymbolic extends TestCase 
{
	
	final static int NUM_DIM = 4;
	
	
	private class AElem extends SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
	{
		private BigInteger row;
		private BigInteger col;

		
		public AElem(SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> _fac , BigInteger _row , BigInteger _col ) {
			super(_fac);
			row = _row;
			col = _col;
		}

		@Override
		public SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> eval( HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<Elem<?, ?>> withRespectTo , HashMap<Elem<?,?>,Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		/**
		 * @return the row
		 */
		public BigInteger getRow() {
			return row;
		}

		/**
		 * @return the col
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
		public String writeString( ) {
			return( "a( " + row + " , " + col + " )" );
		}
		
	}
	
	
	private void verifyNoInvertLeft( 
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
			verifyNoInvertLeft( add.getElemA() );
			verifyNoInvertLeft( add.getElemB() );
			return;
		}
		
		
		if( in instanceof SymbolicNegate )
		{
			SymbolicNegate<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> neg = (SymbolicNegate<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>) in;
			verifyNoInvertLeft( neg.getElem() );
			return;
		}
		
		
		if( in instanceof SymbolicMult )
		{
			SymbolicMult<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> add = (SymbolicMult<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>) in;
			verifyNoInvertLeft( add.getElemA() );
			verifyNoInvertLeft( add.getElemB() );
			return;
		}
		
		
		if( in instanceof SymbolicInvertRight )
		{
			SymbolicInvertRight<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> inv = (SymbolicInvertRight<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>) in;
			verifyNoInvertLeft( inv.getElem() );
			return;
		}
		
		
		if( in instanceof SymbolicInvertLeft )
		{
			Assert.assertTrue( false );
		}
		
		
		throw( new RuntimeException( "Not Recognized" ) );
		
		
	}
	
	
	
	
	
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
			inv = mat.invertRight();
		
		
		
		
		for( i = 0 ; i < NUM_DIM ; i++ )
		{
			for( j = 0 ; j < NUM_DIM ; j++ )
			{
				SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
					el = inv.get( BigInteger.valueOf(i) , BigInteger.valueOf(j) );
				verifyNoInvertLeft( el );
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


