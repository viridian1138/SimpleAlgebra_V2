



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

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicZero;



/**
 * Tests basic matrix inverses.
 * 
 * @author thorngreen
 * 
 */
public class TestInvertSymmetric extends TestCase {
	
	
	
	
	
	
	
	/**
	 * Test method for the inverse of a simple diagonal symbolic matrix
	 */
	public void testInvertDiagonalLeft() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> se =
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		
		SquareMatrixElem<TestDimensionFour, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>> mat
			= new SquareMatrixElem<TestDimensionFour, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(se, td);
		
		
		for( int cnt = 0 ; cnt < 4 ; cnt++ )
		{
			final BigInteger index = BigInteger.valueOf( cnt );
			mat.setVal(index, index, new SymbolicIdentity<DoubleElem, DoubleElemFactory>( de ) );
		}
		
		
		SquareMatrixElem<TestDimensionFour, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>> matI
			= mat.invertLeft();
		
		Assert.assertTrue( mat != null );
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				SymbolicElem<DoubleElem,DoubleElemFactory> el = mat.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				if( row == col )
				{
					Assert.assertTrue( el instanceof SymbolicIdentity );
				}
				else
				{
					Assert.assertTrue( el instanceof SymbolicZero );
				}
			}
		}
		
		
	}
	
	
	
	
	
	/**
	 * Test method for the inverse of a simple diagonal symbolic matrix
	 */
	public void testInvertDiagonalRight() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> se =
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		
		SquareMatrixElem<TestDimensionFour, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>> mat
			= new SquareMatrixElem<TestDimensionFour, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(se, td);
		
		
		for( int cnt = 0 ; cnt < 4 ; cnt++ )
		{
			final BigInteger index = BigInteger.valueOf( cnt );
			mat.setVal(index, index, new SymbolicIdentity<DoubleElem, DoubleElemFactory>( de ) );
		}
		
		
		SquareMatrixElem<TestDimensionFour, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>> matI
			= mat.invertRight();
		
		Assert.assertTrue( mat != null );
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				SymbolicElem<DoubleElem,DoubleElemFactory> el = mat.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				if( row == col )
				{
					Assert.assertTrue( el instanceof SymbolicIdentity );
				}
				else
				{
					Assert.assertTrue( el instanceof SymbolicZero );
				}
			}
		}
		
		
	}
	
	

	
	
}


