



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
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicZero;



/**
 * Tests basic matrix inverses.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestInvertMatrixBasic extends TestCase {
	
	
	
	
	
	
	
	/**
	 * Test method for the inverse of a simple identity symbolic matrix
	 */
	public void testInvertIdentityLeft() throws NotInvertibleException
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
		
		Assert.assertTrue( matI != null );
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				SymbolicElem<DoubleElem,DoubleElemFactory> el = matI.getVal( BigInteger.valueOf(row) , 
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
	 * Test method for the inverse of a simple diagonal matrix
	 */
	public void testInvertDiagonalLeft() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final int numDim = 150;
		
		final NumDimensions td = new NumDimensions()
		{

			@Override
			public BigInteger getVal() {
				return( BigInteger.valueOf( numDim ) );
			}
			
		};
		
		
		final Random rand = new Random( 5555 );
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> mat
			= new SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory>(de, td);
		
		
		for( int cnt = 0 ; cnt < numDim ; cnt++ )
		{
			final BigInteger index = BigInteger.valueOf( cnt );
			mat.setVal(index, index, new DoubleElem( rand.nextDouble() ) );
		}
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> matI
			= mat.invertLeft();
		
		Assert.assertTrue( matI != null );
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> matI2
			= matI.mult( mat );
		
		
		Assert.assertTrue( matI2 != null );
		
		
		for( int row = 0 ; row < numDim ; row++ )
		{
			for( int col = 0 ; col < numDim ; col++ )
			{
				DoubleElem el = matI2.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				if( row == col )
				{
					Assert.assertTrue( Math.abs( 1.0 - el.getVal() ) < 0.0001 );
				}
				else
				{
					Assert.assertTrue( Math.abs( 0.0 - el.getVal() ) < 0.0001 );
				}
			}
		}
		
		
	}
	
	
	
	

	/**
	 * Test method for the inverse of a simple tridiagonal matrix
	 */
	public void testInvertTridiagonalLeft() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final int numDim = 150;
		
		final NumDimensions td = new NumDimensions()
		{

			@Override
			public BigInteger getVal() {
				return( BigInteger.valueOf( numDim ) );
			}
			
		};
		
		
		final Random rand = new Random( 5555 );
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> mat
			= new SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory>(de, td);
		
		
		for( int cnt = 0 ; cnt < numDim ; cnt++ )
		{
			final BigInteger index = BigInteger.valueOf( cnt );
			mat.setVal(index, index, new DoubleElem( rand.nextDouble() ) );
			if( cnt > 0 )
			{
				final BigInteger index2 = BigInteger.valueOf( cnt );
				mat.setVal(index, index2, new DoubleElem( rand.nextDouble() ) );
				mat.setVal(index2, index, new DoubleElem( rand.nextDouble() ) );
			}
		}
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> matI
			= mat.invertLeft();
		
		Assert.assertTrue( matI != null );
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> matI2
			= matI.mult( mat );
		
		
		Assert.assertTrue( matI2 != null );
		
		
		for( int row = 0 ; row < numDim ; row++ )
		{
			for( int col = 0 ; col < numDim ; col++ )
			{
				DoubleElem el = matI2.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				if( row == col )
				{
					Assert.assertTrue( Math.abs( 1.0 - el.getVal() ) < 0.0001 );
				}
				else
				{
					Assert.assertTrue( Math.abs( 0.0 - el.getVal() ) < 0.0001 );
				}
			}
		}
		
		
	}
	
	
	
	
	
	/**
	 * Test method for the inverse of a simple block diagonal matrix
	 */
	public void testInvertBlockDiagonalLeft() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory> me =
				new SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>(de, td);
		
		
		final Random rand = new Random( 5555 );
		
		
		SquareMatrixElem<NumDimensions, SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> mat
			= new SquareMatrixElem<NumDimensions, SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>(me, td);
		
		
		for( int cnt = 0 ; cnt < 4 ; cnt++ )
		{
			final BigInteger index = BigInteger.valueOf( cnt );
			final SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> an =
					new SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>(de, td);
			for( int row = 0 ; row < 4 ; row++ )
			{
				for( int col = 0 ; col < 4 ; col++ )
				{
					an.setVal(BigInteger.valueOf(row), BigInteger.valueOf(col), new DoubleElem( rand.nextDouble() ) );
				}
			}
			mat.setVal( index, index, an);
		}
		
		
		SquareMatrixElem<NumDimensions, SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> matI
			= mat.invertLeft();
		
		Assert.assertTrue( matI != null );
		
		
		SquareMatrixElem<NumDimensions, SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> matI2
			= matI.mult( mat );
		
		
		Assert.assertTrue( matI2 != null );
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> elA = matI2.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				for( int row2 = 0 ; row2 < 4 ; row2++ )
				{
					for( int col2 = 0 ; col2 < 4 ; col2++ )
					{
						DoubleElem el = elA.getVal( BigInteger.valueOf(row2) , 
								BigInteger.valueOf(col2) );
						if( ( row == col ) && ( row2 == col2 ) )
						{
							Assert.assertTrue( Math.abs( 1.0 - el.getVal() ) < 0.0001 );
						}
						else
						{
							Assert.assertTrue( Math.abs( 0.0 - el.getVal() ) < 0.0001 );
						}
					}
				}
			}
		}
		
		
	}
	
	
	
	
	/**
	 * Test method for the inverse of a simple upper triangular matrix
	 */
	public void testInvertUpperTriangularLeft() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		final Random rand = new Random( 5555 );
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> mat
			= new SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>(de, td);
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				if( ( row + ( 3 - col ) ) < 4 )
				{
					mat.setVal(BigInteger.valueOf(row), BigInteger.valueOf(col), new DoubleElem( rand.nextDouble() ) );
				}
			}
		}
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> matI
			= mat.invertLeft();
		
		Assert.assertTrue( matI != null );
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> matI2
			= matI.mult( mat );
		
		
		Assert.assertTrue( matI2 != null );
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				DoubleElem el = matI2.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				if( row == col )
				{
					Assert.assertTrue( Math.abs( 1.0 - el.getVal() ) < 0.0001 );
				}
				else
				{
					Assert.assertTrue( Math.abs( 0.0 - el.getVal() ) < 0.0001 );
				}
			}
		}
		
		
	}
	
	
	
	
	
	/**
	 * Test method for the inverse of a simple lower triangular matrix
	 */
	public void testInvertLowerTriangularLeft() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		final Random rand = new Random( 5555 );
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> mat
			= new SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>(de, td);
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				if( ( ( 3 - row ) + col ) < 4 )
				{
					mat.setVal(BigInteger.valueOf(row), BigInteger.valueOf(col), new DoubleElem( rand.nextDouble() ) );
				}
			}
		}
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> matI
			= mat.invertLeft();
		
		Assert.assertTrue( matI != null );
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> matI2
			= matI.mult( mat );
		
		
		Assert.assertTrue( matI2 != null );
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				DoubleElem el = matI2.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				if( row == col )
				{
					Assert.assertTrue( Math.abs( 1.0 - el.getVal() ) < 0.0001 );
				}
				else
				{
					Assert.assertTrue( Math.abs( 0.0 - el.getVal() ) < 0.0001 );
				}
			}
		}
		
		
	}
	
	
	
	
	
	/**
	 * Test method for the inverse of a simple identity symbolic matrix
	 */
	public void testInvertIdentityRight() throws NotInvertibleException
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
		
		Assert.assertTrue( matI != null );
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				SymbolicElem<DoubleElem,DoubleElemFactory> el = matI.getVal( BigInteger.valueOf(row) , 
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
	 * Test method for the inverse of a simple diagonal matrix
	 */
	public void testInvertDiagonalRight() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final int numDim = 150;
		
		final NumDimensions td = new NumDimensions()
		{

			@Override
			public BigInteger getVal() {
				return( BigInteger.valueOf( numDim ) );
			}
			
		};
		
		
		final Random rand = new Random( 5555 );
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> mat
			= new SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory>(de, td);
		
		
		for( int cnt = 0 ; cnt < numDim ; cnt++ )
		{
			final BigInteger index = BigInteger.valueOf( cnt );
			mat.setVal(index, index, new DoubleElem( rand.nextDouble() ) );
		}
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> matI
			= mat.invertRight();
		
		Assert.assertTrue( matI != null );
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> matI2
			= mat.mult( matI );
		
		
		Assert.assertTrue( matI2 != null );
		
		
		for( int row = 0 ; row < numDim ; row++ )
		{
			for( int col = 0 ; col < numDim ; col++ )
			{
				DoubleElem el = matI2.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				if( row == col )
				{
					Assert.assertTrue( Math.abs( 1.0 - el.getVal() ) < 0.0001 );
				}
				else
				{
					Assert.assertTrue( Math.abs( 0.0 - el.getVal() ) < 0.0001 );
				}
			}
		}
		
		
	}
	
	
	
	
	
	/**
	 * Test method for the inverse of a simple tridiagonal matrix
	 */
	public void testInvertTridiagonalRight() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final int numDim = 150;
		
		final NumDimensions td = new NumDimensions()
		{

			@Override
			public BigInteger getVal() {
				return( BigInteger.valueOf( numDim ) );
			}
			
		};
		
		
		final Random rand = new Random( 5555 );
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> mat
			= new SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory>(de, td);
		
		
		for( int cnt = 0 ; cnt < numDim ; cnt++ )
		{
			final BigInteger index = BigInteger.valueOf( cnt );
			mat.setVal(index, index, new DoubleElem( rand.nextDouble() ) );
			if( cnt > 0 )
			{
				final BigInteger index2 = BigInteger.valueOf( cnt );
				mat.setVal(index, index2, new DoubleElem( rand.nextDouble() ) );
				mat.setVal(index2, index, new DoubleElem( rand.nextDouble() ) );
			}
		}
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> matI
			= mat.invertRight();
		
		Assert.assertTrue( matI != null );
		
		
		SquareMatrixElem<NumDimensions, DoubleElem, DoubleElemFactory> matI2
			= mat.mult( matI );
		
		
		Assert.assertTrue( matI2 != null );
		
		
		for( int row = 0 ; row < numDim ; row++ )
		{
			for( int col = 0 ; col < numDim ; col++ )
			{
				DoubleElem el = matI2.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				if( row == col )
				{
					Assert.assertTrue( Math.abs( 1.0 - el.getVal() ) < 0.0001 );
				}
				else
				{
					Assert.assertTrue( Math.abs( 0.0 - el.getVal() ) < 0.0001 );
				}
			}
		}
		
		
	}
	
	
	
	
	
	

	/**
	 * Test method for the inverse of a simple block diagonal matrix
	 */
	public void testInvertBlockDiagonalRight() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory> me =
				new SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>(de, td);
		
		
		final Random rand = new Random( 5555 );
		
		
		SquareMatrixElem<NumDimensions, SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> mat
			= new SquareMatrixElem<NumDimensions, SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>(me, td);
		
		
		for( int cnt = 0 ; cnt < 4 ; cnt++ )
		{
			final BigInteger index = BigInteger.valueOf( cnt );
			final SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> an =
					new SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>(de, td);
			for( int row = 0 ; row < 4 ; row++ )
			{
				for( int col = 0 ; col < 4 ; col++ )
				{
					an.setVal(BigInteger.valueOf(row), BigInteger.valueOf(col), new DoubleElem( rand.nextDouble() ) );
				}
			}
			mat.setVal( index, index, an);
		}
		
		
		SquareMatrixElem<NumDimensions, SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> matI
			= mat.invertRight();
		
		Assert.assertTrue( matI != null );
		
		
		SquareMatrixElem<NumDimensions, SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>> matI2
			= mat.mult( matI );
		
		
		Assert.assertTrue( matI2 != null );
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> elA = matI2.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				for( int row2 = 0 ; row2 < 4 ; row2++ )
				{
					for( int col2 = 0 ; col2 < 4 ; col2++ )
					{
						DoubleElem el = elA.getVal( BigInteger.valueOf(row2) , 
								BigInteger.valueOf(col2) );
						if( ( row == col ) && ( row2 == col2 ) )
						{
							Assert.assertTrue( Math.abs( 1.0 - el.getVal() ) < 0.0001 );
						}
						else
						{
							Assert.assertTrue( Math.abs( 0.0 - el.getVal() ) < 0.0001 );
						}
					}
				}
			}
		}
		
		
	}
	
	
	
	
	
	
	/**
	 * Test method for the inverse of a simple upper triangular matrix
	 */
	public void testInvertUpperTriangularRight() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		final Random rand = new Random( 5555 );
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> mat
			= new SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>(de, td);
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				if( ( row + ( 3 - col ) ) < 4 )
				{
					mat.setVal(BigInteger.valueOf(row), BigInteger.valueOf(col), new DoubleElem( rand.nextDouble() ) );
				}
			}
		}
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> matI
			= mat.invertRight();
		
		Assert.assertTrue( matI != null );
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> matI2
			= mat.mult( matI );
		
		
		Assert.assertTrue( matI2 != null );
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				DoubleElem el = matI2.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				if( row == col )
				{
					Assert.assertTrue( Math.abs( 1.0 - el.getVal() ) < 0.0001 );
				}
				else
				{
					Assert.assertTrue( Math.abs( 0.0 - el.getVal() ) < 0.0001 );
				}
			}
		}
		
		
	}
	
	
	
	
	
	/**
	 * Test method for the inverse of a simple lower triangular matrix
	 */
	public void testInvertLowerTriangularRight() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		final Random rand = new Random( 5555 );
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> mat
			= new SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory>(de, td);
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				if( ( ( 3 - row ) + col ) < 4 )
				{
					mat.setVal(BigInteger.valueOf(row), BigInteger.valueOf(col), new DoubleElem( rand.nextDouble() ) );
				}
			}
		}
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> matI
			= mat.invertRight();
		
		Assert.assertTrue( matI != null );
		
		
		SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> matI2
			= mat.mult( matI );
		
		
		Assert.assertTrue( matI2 != null );
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				DoubleElem el = matI2.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				if( row == col )
				{
					Assert.assertTrue( Math.abs( 1.0 - el.getVal() ) < 0.0001 );
				}
				else
				{
					Assert.assertTrue( Math.abs( 0.0 - el.getVal() ) < 0.0001 );
				}
			}
		}
		
		
	}
	
	

	
	
}


