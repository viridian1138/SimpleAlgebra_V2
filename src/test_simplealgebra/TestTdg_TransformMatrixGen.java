



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
import java.util.HashSet;
import java.util.Random;
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.*;
import simplealgebra.tdg.*;
import test_simplealgebra.TestTdg_TransformMatrixGenPerspec.DimTwentySix;


/**
 * Tests the ability to generate transformation matrices.
 * Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestTdg_TransformMatrixGen extends TestCase {
	
	
	/**
	 * Twenty-Five Dimensions
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class DimTwentyFive extends NumDimensions
	{
		/**
		 * The number of dimensions.
		 */
		BigInteger dim;
		
		/**
		 * Constructs the dimension.
		 */
		public DimTwentyFive( )
		{
			dim = BigInteger.valueOf(25);
		}

		/**
		 * Gets the number of dimensions.
		 * @return The number of dimensions.
		 */
		@Override
		public BigInteger getVal() {
			return( dim );
		}
		
	};

	
	/**
	 * Tests the ability to generate rotation matrices in 2-D
	 * @throws Throwable
	 */
	public final void testRotationMatrix2D() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrix2D(angle, 20);
		
		
			final double imat[][] = new double[ 2 ][ 2 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = cosd;
			imat[ 1 ][ 0 ] = sind;
		
		
			imat[ 0 ][ 1 ] = -sind;
			imat[ 1 ][ 1 ] = cosd;
		
		
			for( int row = 0 ; row < 2 ; row++ )
			{
				for( int col = 0 ; col < 2 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate rotation matrices in 2-D
	 * @throws Throwable
	 */
	public final void testRotationMatrix2D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrix(BigInteger.ZERO, BigInteger.ONE, angle, 20);
		
		
			final double imat[][] = new double[ 2 ][ 2 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = cosd;
			imat[ 1 ][ 0 ] = sind;
		
		
			imat[ 0 ][ 1 ] = -sind;
			imat[ 1 ][ 1 ] = cosd;
		
		
			for( int row = 0 ; row < 2 ; row++ )
			{
				for( int col = 0 ; col < 2 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate rotation matrices on the XY plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixXY_3D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrixXY_3D(angle, 20);
		
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = cosd;
			imat[ 1 ][ 0 ] = sind;
			imat[ 2 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = -sind;
			imat[ 1 ][ 1 ] = cosd;
			imat[ 2 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
		
		
			for( int row = 0 ; row < 3 ; row++ )
			{
				for( int col = 0 ; col < 3 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate rotation matrices on the XY plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixXY_3D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrix(BigInteger.ZERO, BigInteger.ONE, angle, 20);
		
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = cosd;
			imat[ 1 ][ 0 ] = sind;
			imat[ 2 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = -sind;
			imat[ 1 ][ 1 ] = cosd;
			imat[ 2 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
		
		
			for( int row = 0 ; row < 3 ; row++ )
			{
				for( int col = 0 ; col < 3 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate rotation matrices on the YZ plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixYZ_3D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrixYZ_3D(angle, 20);
		
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = cosd;
			imat[ 2 ][ 1 ] = sind;
		
		
			imat[ 0 ][ 2 ] = -0.0;
			imat[ 1 ][ 2 ] = -sind;
			imat[ 2 ][ 2 ] = cosd;
		
		
			for( int row = 0 ; row < 3 ; row++ )
			{
				for( int col = 0 ; col < 3 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate rotation matrices on the YZ plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixYZ_3D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrix(BigInteger.ONE, BigInteger.valueOf(2), angle, 20);
		
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = cosd;
			imat[ 2 ][ 1 ] = sind;
		
		
			imat[ 0 ][ 2 ] = -0.0;
			imat[ 1 ][ 2 ] = -sind;
			imat[ 2 ][ 2 ] = cosd;
		
		
			for( int row = 0 ; row < 3 ; row++ )
			{
				for( int col = 0 ; col < 3 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate rotation matrices on the XZ plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixXZ_3D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrixXZ_3D(angle, 20);
		
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: the transformation on https://open.gl/transformations has the sine angle negated
			imat[ 0 ][ 0 ] = cosd;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = sind;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = -sind;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = cosd;
		
		
			for( int row = 0 ; row < 3 ; row++ )
			{
				for( int col = 0 ; col < 3 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate rotation matrices on the XZ plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixXZ_3D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrix(BigInteger.ZERO, BigInteger.valueOf(2), angle, 20);
		
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: the transformation on https://open.gl/transformations has the sine angle negated
			imat[ 0 ][ 0 ] = cosd;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = sind;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = -sind;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = cosd;
		
		
			for( int row = 0 ; row < 3 ; row++ )
			{
				for( int col = 0 ; col < 3 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate rotation matrices in 4-D
	 * @throws Throwable
	 */
	public final void testRotationMatrix_4D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> matA = mg.rotationMatrix(BigInteger.ONE, BigInteger.valueOf(3), angle, 20);
			
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> matB = mg.rotationMatrix(BigInteger.ONE, BigInteger.valueOf(3), angle.negate(), 20);
			
			
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> iden = matA.mult(matB);
		
		
			for( int row = 0 ; row < 4 ; row++ )
			{
				for( int col = 0 ; col < 4 ; col++ )
				{
					DoubleElem d = iden.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					double dv = ( row == col ) ? 1.0 : 0.0;
					Assert.assertTrue( Math.abs( d.getVal() - dv ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate rotation matrices in 25-D
	 * @throws Throwable
	 */
	public final void testRotationMatrix_25D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DimTwentyFive td = new DimTwentyFive();
		
		final GeometricAlgebraOrd<DimTwentyFive> ord = new GeometricAlgebraOrd<DimTwentyFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<DimTwentyFive,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<DimTwentyFive,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<DimTwentyFive,DoubleElem,DoubleElemFactory> matA = mg.rotationMatrix(BigInteger.ONE, BigInteger.valueOf(15), angle, 20);
			
			final SquareMatrixElem<DimTwentyFive,DoubleElem,DoubleElemFactory> matB = mg.rotationMatrix(BigInteger.ONE, BigInteger.valueOf(15), angle.negate(), 20);
			
			
			final SquareMatrixElem<DimTwentyFive,DoubleElem,DoubleElemFactory> iden = matA.mult(matB);
		
		
			for( int row = 0 ; row < 25 ; row++ )
			{
				for( int col = 0 ; col < 25 ; col++ )
				{
					DoubleElem d = iden.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					double dv = ( row == col ) ? 1.0 : 0.0;
					Assert.assertTrue( Math.abs( d.getVal() - dv ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate identity matrices in 2-D
	 */
	public final void testIdentityMatrix2D()  throws Throwable
	{
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(se, me);
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> mat = mg.identityMatrix2D();
		
		
		for( int row = 0 ; row < 2 ; row++ )
		{
			for( int col = 0 ; col < 2 ; col++ )
			{
				DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
				// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
				double dv = ( row == col ) ? 1.0 : 0.0;
				Assert.assertTrue( Math.abs( d.getVal() - dv ) < 1E-5 );
			}
		}
	}

	
	/**
	 * Tests the ability to generate identity matrices in 2-D
	 */
	public final void testIdentityMatrix2D_2() 
	{
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(se, me);
		
		final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> mat = mg.identityMatrix();
		
		
		for( int row = 0 ; row < 2 ; row++ )
		{
			for( int col = 0 ; col < 2 ; col++ )
			{
				DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
				// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
				double dv = ( row == col ) ? 1.0 : 0.0;
				Assert.assertTrue( Math.abs( d.getVal() - dv ) < 1E-5 );
			}
		}
	}
	
	

	
	
	/**
	 * Tests the ability to generate identity matrices in 3-D
	 */
	public final void testIdentityMatrix3D()  throws Throwable
	{
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.identityMatrix3D();
		
		
		for( int row = 0 ; row < 3 ; row++ )
		{
			for( int col = 0 ; col < 3 ; col++ )
			{
				DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
				// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
				double dv = ( row == col ) ? 1.0 : 0.0;
				Assert.assertTrue( Math.abs( d.getVal() - dv ) < 1E-5 );
			}
		}
	}

	
	/**
	 * Tests the ability to generate identity matrices in 3-D
	 */
	public final void testIdentityMatrix3D_2() 
	{
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.identityMatrix();
		
		
		for( int row = 0 ; row < 3 ; row++ )
		{
			for( int col = 0 ; col < 3 ; col++ )
			{
				DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
				// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
				double dv = ( row == col ) ? 1.0 : 0.0;
				Assert.assertTrue( Math.abs( d.getVal() - dv ) < 1E-5 );
			}
		}
	}
	
	

	
	/**
	 * Tests the ability to generate identity matrices in 4-D
	 */
	public final void testIdentityMatrix4D() 
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.identityMatrix();
		
		
		for( int row = 0 ; row < 4 ; row++ )
		{
			for( int col = 0 ; col < 4 ; col++ )
			{
				DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
				// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
				double dv = ( row == col ) ? 1.0 : 0.0;
				Assert.assertTrue( Math.abs( d.getVal() - dv ) < 1E-5 );
			}
		}
	}
	
	

	
	/**
	 * Tests the ability to generate identity matrices in 25-D
	 */
	public final void testIdentityMatrix25D() 
	{
		final DimTwentyFive td = new DimTwentyFive();
		
		final GeometricAlgebraOrd<DimTwentyFive> ord = new GeometricAlgebraOrd<DimTwentyFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<DimTwentyFive,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<DimTwentyFive,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory>(se, me);
		
		final SquareMatrixElem<DimTwentyFive,DoubleElem,DoubleElemFactory> mat = mg.identityMatrix();
		
		
		for( int row = 0 ; row < 25 ; row++ )
		{
			for( int col = 0 ; col < 25 ; col++ )
			{
				DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
				// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
				double dv = ( row == col ) ? 1.0 : 0.0;
				Assert.assertTrue( Math.abs( d.getVal() - dv ) < 1E-5 );
			}
		}
	}

	
	/**
	 * Tests the ability to generate scaling matrices in 2-D
	 * @throws Throwable
	 */
	public final void testScalingMatrix2D() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double yscal = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xscale = new DoubleElem( xscal );
			final DoubleElem yscale = new DoubleElem( yscal );
		
		
			final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> mat = mg.scalingMatrix2D(xscale, yscale);
		
		
			final double imat[][] = new double[ 2 ][ 2 ];
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = xscal;
			imat[ 1 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = yscal;
		
		
			for( int row = 0 ; row < 2 ; row++ )
			{
				for( int col = 0 ; col < 2 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate scaling matrices in 2-D
	 * @throws Throwable
	 */
	public final void testScalingMatrix2D_2() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double yscal = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xscale = new DoubleElem( xscal );
			final DoubleElem yscale = new DoubleElem( yscal );
			
			final BigInteger xAxis = BigInteger.ZERO;
			final BigInteger yAxis = BigInteger.ONE;
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory> scale = se.zero();
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( xAxis );
				scale.setVal(idx, xscale);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( yAxis );
				scale.setVal(idx, yscale);
			}
		
		
			final SquareMatrixElem<TestDimensionTwo, DoubleElem, DoubleElemFactory> mat = mg.scalingMatrix(scale, false);
		
		
			final double imat[][] = new double[ 2 ][ 2 ];
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = xscal;
			imat[ 1 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = yscal;
		
		
			for( int row = 0 ; row < 2 ; row++ )
			{
				for( int col = 0 ; col < 2 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate scaling matrices in 3-D
	 * @throws Throwable
	 */
	public final void testScalingMatrix3D() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double yscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double zscal = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xscale = new DoubleElem( xscal );
			final DoubleElem yscale = new DoubleElem( yscal );
			final DoubleElem zscale = new DoubleElem( zscal );
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.scalingMatrix3D(xscale, yscale, zscale);
		
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = xscal;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = yscal;
			imat[ 2 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = zscal;
		
		
			for( int row = 0 ; row < 3 ; row++ )
			{
				for( int col = 0 ; col < 3 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate scaling matrices in 3-D
	 * @throws Throwable
	 */
	public final void testScalingMatrix3D_2() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double yscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double zscal = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xscale = new DoubleElem( xscal );
			final DoubleElem yscale = new DoubleElem( yscal );
			final DoubleElem zscale = new DoubleElem( zscal );
			
			final BigInteger xAxis = BigInteger.ZERO;
			final BigInteger yAxis = BigInteger.ONE;
			final BigInteger zAxis = BigInteger.valueOf(2);
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, DoubleElem, DoubleElemFactory> scale = se.zero();
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( xAxis );
				scale.setVal(idx, xscale);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( yAxis );
				scale.setVal(idx, yscale);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( zAxis );
				scale.setVal(idx, zscale);
			}
		
		
			final SquareMatrixElem<TestDimensionThree, DoubleElem, DoubleElemFactory> mat = mg.scalingMatrix(scale, false);
		
		
			final double imat[][] = new double[ 3 ][ 3 ];
			
			
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = xscal;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = yscal;
			imat[ 2 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = zscal;
		
		
			for( int row = 0 ; row < 3 ; row++ )
			{
				for( int col = 0 ; col < 3 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate scaling matrices in 4-D
	 * @throws Throwable
	 */
	public final void testScalingMatrix4D() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double yscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double zscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double tscal = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xscale = new DoubleElem( xscal );
			final DoubleElem yscale = new DoubleElem( yscal );
			final DoubleElem zscale = new DoubleElem( zscal );
			final DoubleElem tscale = new DoubleElem( tscal );
			
			final BigInteger xAxis = BigInteger.ZERO;
			final BigInteger yAxis = BigInteger.ONE;
			final BigInteger zAxis = BigInteger.valueOf(2);
			final BigInteger tAxis = BigInteger.valueOf(3);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> scale = se.zero();
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( xAxis );
				scale.setVal(idx, xscale);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( yAxis );
				scale.setVal(idx, yscale);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( zAxis );
				scale.setVal(idx, zscale);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( tAxis );
				scale.setVal(idx, tscale);
			}
		
		
			final SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> mat = mg.scalingMatrix(scale, false);
		
		
			final double imat[][] = new double[ 4 ][ 4 ];
			
			
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = xscal;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = yscal;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = zscal;
			imat[ 3 ][ 2 ] = 0.0;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = tscal;
		
		
			for( int row = 0 ; row < 4 ; row++ )
			{
				for( int col = 0 ; col < 4 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate scaling matrices in 25-D
	 * @throws Throwable
	 */
	public final void testScalingMatrix25D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DimTwentyFive td = new DimTwentyFive();
		
		final GeometricAlgebraOrd<DimTwentyFive> ord = new GeometricAlgebraOrd<DimTwentyFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<DimTwentyFive,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<DimTwentyFive,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double ascal = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem ascale = new DoubleElem( ascal );
			
			final BigInteger axis = BigInteger.valueOf( rand.nextInt( 25 ) );
			
			final GeometricAlgebraMultivectorElem<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory> scale = se.zero();
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( axis );
				scale.setVal(idx, ascale);
			}
		
		
			final SquareMatrixElem<DimTwentyFive,DoubleElem,DoubleElemFactory> mat = mg.scalingMatrix(scale,false);
		
			final double imat[][] = new double[ 25 ][ 25 ];
			
			
			for( int row = 0 ; row < 25 ; row++ )
			{
				for( int col = 0 ; col < 25 ; col++ )
				{
					imat[row][col] = ( row == col ? 1.0 : 0.0 );
				}
			}
			
			imat[ axis.intValue() ][ axis.intValue() ] = ascal;
		
		
			for( int row = 0 ; row < 25 ; row++ )
			{
				for( int col = 0 ; col < 25 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}
	
	
	
	
	
	


	
	/**
	 * Tests the ability to generate shearing matrices in 2-D
	 * @throws Throwable
	 */
	public final void testShearingMatrix2D() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen_Facade<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
			
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
		
			
		
			final SquareMatrixElem<TestDimensionTwo,DoubleElem,DoubleElemFactory> mat = mg.shearingMatrix2D(hm);
		
		
			final double imat[][] = new double[ 2 ][ 2 ];
		
		
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			
			imat[ rowa.intValue() ][ cola.intValue() ] = sher;
		
		
			for( int row = 0 ; row < 2 ; row++ )
			{
				for( int col = 0 ; col < 2 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate shearing matrices in 2-D
	 * @throws Throwable
	 */
	public final void testShearingMatrix2D_2() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionTwo,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
			
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
			
		
		
			final SquareMatrixElem<TestDimensionTwo, DoubleElem, DoubleElemFactory> mat = mg.shearingMatrix(hm);
		
		
			final double imat[][] = new double[ 2 ][ 2 ];
		
		
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			
			imat[ rowa.intValue() ][ cola.intValue() ] = sher;
		
		
			for( int row = 0 ; row < 2 ; row++ )
			{
				for( int col = 0 ; col < 2 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}
	
	
	
	
	
	


	
	/**
	 * Tests the ability to generate shearing matrices in 3-D
	 * @throws Throwable
	 */
	public final void testShearingMatrix3D() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
			
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
		
			
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.shearingMatrix3D(hm);
		
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
			
			imat[ rowa.intValue() ][ cola.intValue() ] = sher;
		
		
			for( int row = 0 ; row < 3 ; row++ )
			{
				for( int col = 0 ; col < 3 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	/**
	 * Tests the ability to generate shearing matrices in 3-D
	 * @throws Throwable
	 */
	public final void testShearingMatrix3D_2() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
			
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
			
		
		
			final SquareMatrixElem<TestDimensionThree, DoubleElem, DoubleElemFactory> mat = mg.shearingMatrix(hm);
		
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
			
			imat[ rowa.intValue() ][ cola.intValue() ] = sher;
		
		
			for( int row = 0 ; row < 3 ; row++ )
			{
				for( int col = 0 ; col < 3 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}
	
	
	
	
	
	


	
	/**
	 * Tests the ability to generate shearing matrices in 4-D
	 * @throws Throwable
	 */
	public final void testShearingMatrix4D() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
			
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
			
		
		
			final SquareMatrixElem<TestDimensionFour, DoubleElem, DoubleElemFactory> mat = mg.shearingMatrix(hm);
		
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			for( int row = 0 ; row < 4 ; row++ )
			{
				for( int col = 0 ; col < 4 ; col++ )
				{
					imat[row][col] = ( row == col ? 1.0 : 0.0 );
				}
			}
			
			imat[ rowa.intValue() ][ cola.intValue() ] = sher;
		
		
			for( int row = 0 ; row < 4 ; row++ )
			{
				for( int col = 0 ; col < 4 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}
	
	
	
	
	
	


	
	/**
	 * Tests the ability to generate shearing matrices in 25-D
	 * @throws Throwable
	 */
	public final void testShearingMatrix25D() throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DimTwentyFive td = new DimTwentyFive();
		
		final GeometricAlgebraOrd<DimTwentyFive> ord = new GeometricAlgebraOrd<DimTwentyFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<DimTwentyFive,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<DimTwentyFive,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGen<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGen<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
			
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( td.getVal().intValue() ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<DimTwentyFive,GeometricAlgebraOrd<DimTwentyFive>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
			
		
		
			final SquareMatrixElem<DimTwentyFive, DoubleElem, DoubleElemFactory> mat = mg.shearingMatrix(hm);
		
		
			final double imat[][] = new double[ 25 ][ 25 ];
		
		
			for( int row = 0 ; row < 25 ; row++ )
			{
				for( int col = 0 ; col < 25 ; col++ )
				{
					imat[row][col] = ( row == col ? 1.0 : 0.0 );
				}
			}
			
			imat[ rowa.intValue() ][ cola.intValue() ] = sher;
		
		
			for( int row = 0 ; row < 25 ; row++ )
			{
				for( int col = 0 ; col < 25 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}

	
	
	
} /* Test Class */



