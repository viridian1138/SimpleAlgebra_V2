



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


/**
 * Tests the ability to generate transformation matrices.
 * Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestTdg_TransformMatrixGenPerspec extends TestCase {
	
	
	/**
	 * Twenty-Six Dimensions
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class DimTwentySix extends NumDimensions
	{
		/**
		 * The number of dimensions.
		 */
		BigInteger dim;
		
		/**
		 * Constructs the dimension.
		 */
		public DimTwentySix( )
		{
			dim = BigInteger.valueOf(26);
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
	public final void testRotationMatrix2D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrix2D(angle, 20);
		
		
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
	 * Tests the ability to generate rotation matrices in 2-D
	 * @throws Throwable
	 */
	public final void testRotationMatrix2D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
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
	 * Tests the ability to generate rotation matrices on the XY plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixXY_3D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrixXY_3D(angle, 20);
		
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = cosd;
			imat[ 1 ][ 0 ] = sind;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = -sind;
			imat[ 1 ][ 1 ] = cosd;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
			imat[ 3 ][ 2 ] = 0.0;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
		
		
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
	 * Tests the ability to generate rotation matrices on the XY plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixXY_3D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrix(BigInteger.ZERO, BigInteger.ONE, angle, 20);
		
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = cosd;
			imat[ 1 ][ 0 ] = sind;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = -sind;
			imat[ 1 ][ 1 ] = cosd;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
			imat[ 3 ][ 2 ] = 0.0;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
		
		
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
	 * Tests the ability to generate rotation matrices on the YZ plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixYZ_3D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrixYZ_3D(angle, 20);
		
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = cosd;
			imat[ 2 ][ 1 ] = sind;
			imat[ 3 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = -0.0;
			imat[ 1 ][ 2 ] = -sind;
			imat[ 2 ][ 2 ] = cosd;
			imat[ 3 ][ 2 ] = 0.0;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
		
		
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
	 * Tests the ability to generate rotation matrices on the YZ plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixYZ_3D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrix(BigInteger.ONE, BigInteger.valueOf(2), angle, 20);
		
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = cosd;
			imat[ 2 ][ 1 ] = sind;
			imat[ 3 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = -0.0;
			imat[ 1 ][ 2 ] = -sind;
			imat[ 2 ][ 2 ] = cosd;
			imat[ 3 ][ 2 ] = 0.0;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
		
		
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
	 * Tests the ability to generate rotation matrices on the XZ plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixXZ_3D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrixXZ_3D(angle, 20);
		
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: the transformation on https://open.gl/transformations has the sine angle negated
			imat[ 0 ][ 0 ] = cosd;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = sind;
			imat[ 3 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = -sind;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = cosd;
			imat[ 3 ][ 2 ] = 0.0;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
		
		
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
	 * Tests the ability to generate rotation matrices on the XZ plane in 3-D
	 * @throws Throwable
	 */
	public final void testRotationMatrixXZ_3D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.rotationMatrix(BigInteger.ZERO, BigInteger.valueOf(2), angle, 20);
		
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			final double cosd = Math.cos(dangle);
			final double sind = Math.sin(dangle);
		
		
			// NOTE: the transformation on https://open.gl/transformations has the sine angle negated
			imat[ 0 ][ 0 ] = cosd;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = sind;
			imat[ 3 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = -sind;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = cosd;
			imat[ 3 ][ 2 ] = 0.0;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
		
		
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
	 * Tests the ability to generate rotation matrices in 4-D
	 * @throws Throwable
	 */
	public final void testRotationMatrix_4D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFive,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFive,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<TestDimensionFive,DoubleElem,DoubleElemFactory> matA = mg.rotationMatrix(BigInteger.ONE, BigInteger.valueOf(3), angle, 20);
			
			final SquareMatrixElem<TestDimensionFive,DoubleElem,DoubleElemFactory> matB = mg.rotationMatrix(BigInteger.ONE, BigInteger.valueOf(3), angle.negate(), 20);
			
			
			final SquareMatrixElem<TestDimensionFive,DoubleElem,DoubleElemFactory> iden = matA.mult(matB);
		
		
			for( int row = 0 ; row < 5 ; row++ )
			{
				for( int col = 0 ; col < 5 ; col++ )
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
		
		final DimTwentySix td = new DimTwentySix();
		
		final GeometricAlgebraOrd<DimTwentySix> ord = new GeometricAlgebraOrd<DimTwentySix>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<DimTwentySix,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<DimTwentySix,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double dangle = ( rand.nextDouble() ) * ( 2.0 * Math.PI );
		
			final DoubleElem angle = new DoubleElem( dangle );
		
		
			final SquareMatrixElem<DimTwentySix,DoubleElem,DoubleElemFactory> matA = mg.rotationMatrix(BigInteger.ONE, BigInteger.valueOf(15), angle, 20);
			
			final SquareMatrixElem<DimTwentySix,DoubleElem,DoubleElemFactory> matB = mg.rotationMatrix(BigInteger.ONE, BigInteger.valueOf(15), angle.negate(), 20);
			
			
			final SquareMatrixElem<DimTwentySix,DoubleElem,DoubleElemFactory> iden = matA.mult(matB);
		
		
			for( int row = 0 ; row < 26 ; row++ )
			{
				for( int col = 0 ; col < 26 ; col++ )
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
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.identityMatrix2D();
		
		
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
	 * Tests the ability to generate identity matrices in 2-D
	 */
	public final void testIdentityMatrix2D_2() 
	{
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
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
	 * Tests the ability to generate identity matrices in 3-D
	 */
	public final void testIdentityMatrix3D()  throws Throwable
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.identityMatrix3D();
		
		
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
	 * Tests the ability to generate identity matrices in 3-D
	 */
	public final void testIdentityMatrix3D_2() 
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
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
	 * Tests the ability to generate identity matrices in 4-D
	 */
	public final void testIdentityMatrix4D() 
	{
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFive,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFive,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(se, me);
		
		final SquareMatrixElem<TestDimensionFive,DoubleElem,DoubleElemFactory> mat = mg.identityMatrix();
		
		
		for( int row = 0 ; row < 5 ; row++ )
		{
			for( int col = 0 ; col < 5 ; col++ )
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
		final DimTwentySix td = new DimTwentySix();
		
		final GeometricAlgebraOrd<DimTwentySix> ord = new GeometricAlgebraOrd<DimTwentySix>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<DimTwentySix,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<DimTwentySix,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>(se, me);
		
		final SquareMatrixElem<DimTwentySix,DoubleElem,DoubleElemFactory> mat = mg.identityMatrix();
		
		
		for( int row = 0 ; row < 26 ; row++ )
		{
			for( int col = 0 ; col < 26 ; col++ )
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
	public final void testScalingMatrix2D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double yscal = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xscale = new DoubleElem( xscal );
			final DoubleElem yscale = new DoubleElem( yscal );
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.scalingMatrix2D(xscale, yscale);
		
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
	 * Tests the ability to generate scaling matrices in 2-D
	 * @throws Throwable
	 */
	public final void testScalingMatrix2D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double yscal = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xscale = new DoubleElem( xscal );
			final DoubleElem yscale = new DoubleElem( yscal );
			
			final BigInteger xAxis = BigInteger.ZERO;
			final BigInteger yAxis = BigInteger.ONE;
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> scale = se.zero();
			
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
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.scalingMatrix(scale, false);
		
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
	 * Tests the ability to generate scaling matrices in 3-D
	 * @throws Throwable
	 */
	public final void testScalingMatrix3D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double yscal = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double zscal = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xscale = new DoubleElem( xscal );
			final DoubleElem yscale = new DoubleElem( yscal );
			final DoubleElem zscale = new DoubleElem( zscal );
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.scalingMatrix3D(xscale, yscale, zscale);
		
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
			imat[ 3 ][ 3 ] = 1.0;
		
		
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
	 * Tests the ability to generate scaling matrices in 3-D
	 * @throws Throwable
	 */
	public final void testScalingMatrix3D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
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
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> scale = se.zero();
			
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
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.scalingMatrix(scale, false);
		
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
			imat[ 3 ][ 3 ] = 1.0;
		
		
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
	 * Tests the ability to generate scaling matrices in 4-D
	 * @throws Throwable
	 */
	public final void testScalingMatrix4D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFive,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFive,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(se, me);
		
		
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
			
			final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> scale = se.zero();
			
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
		
		
			final SquareMatrixElem<TestDimensionFive,DoubleElem,DoubleElemFactory> mat = mg.scalingMatrix(scale, false);
		
			final double imat[][] = new double[ 5 ][ 5 ];
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = xscal;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = 0.0;
			imat[ 4 ][ 0 ] = 0.0;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = yscal;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = 0.0;
			imat[ 4 ][ 1 ] = 0.0;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = zscal;
			imat[ 3 ][ 2 ] = 0.0;
			imat[ 4 ][ 2 ] = 0.0;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = tscal;
			imat[ 4 ][ 3 ] = 0.0;
		
		
			imat[ 0 ][ 4 ] = 0.0;
			imat[ 1 ][ 4 ] = 0.0;
			imat[ 2 ][ 4 ] = 0.0;
			imat[ 3 ][ 4 ] = 0.0;
			imat[ 4 ][ 4 ] = 1.0;
		
		
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
		
		final DimTwentySix td = new DimTwentySix();
		
		final GeometricAlgebraOrd<DimTwentySix> ord = new GeometricAlgebraOrd<DimTwentySix>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<DimTwentySix,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<DimTwentySix,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double ascal = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem ascale = new DoubleElem( ascal );
			
			final BigInteger axis = BigInteger.valueOf( rand.nextInt( 25 ) );
			
			final GeometricAlgebraMultivectorElem<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> scale = se.zero();
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( axis );
				scale.setVal(idx, ascale);
			}
		
		
			final SquareMatrixElem<DimTwentySix,DoubleElem,DoubleElemFactory> mat = mg.scalingMatrix(scale,false);
		
			final double imat[][] = new double[ 26 ][ 26 ];
			
			
			for( int row = 0 ; row < 26 ; row++ )
			{
				for( int col = 0 ; col < 26 ; col++ )
				{
					imat[row][col] = ( row == col ? 1.0 : 0.0 );
				}
			}
			
			imat[ axis.intValue() ][ axis.intValue() ] = ascal;
		
		
			for( int row = 0 ; row < 26 ; row++ )
			{
				for( int col = 0 ; col < 26 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	
	/**
	 * Tests the ability to generate translation matrices in 2-D
	 * @throws Throwable
	 */
	public final void testTranslationMatrix2D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xtrans = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double ytrans = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xtransl = new DoubleElem( xtrans );
			final DoubleElem ytransl = new DoubleElem( ytrans );
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.translationMatrix2D(xtransl, ytransl);
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = xtrans;
					
					
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = ytrans;
					
					
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
	 * Tests the ability to generate translation matrices in 2-D
	 * @throws Throwable
	 */
	public final void testTranslationMatrix2D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xtrans = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double ytrans = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xtransl = new DoubleElem( xtrans );
			final DoubleElem ytransl = new DoubleElem( ytrans );
			
			final BigInteger xAxis = BigInteger.ZERO;
			final BigInteger yAxis = BigInteger.ONE;
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> transl = se.zero();
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( xAxis );
				transl.setVal(idx, xtransl);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( yAxis );
				transl.setVal(idx, ytransl);
			}
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.translationMatrix(transl);
		
			final double imat[][] = new double[ 3 ][ 3 ];
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = xtrans;
					
					
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = ytrans;
					
					
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
	 * Tests the ability to generate translation matrices in 3-D
	 * @throws Throwable
	 */
	public final void testTranslationMatrix3D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xtrans = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double ytrans = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double ztrans = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xtransl = new DoubleElem( xtrans );
			final DoubleElem ytransl = new DoubleElem( ytrans );
			final DoubleElem ztransl = new DoubleElem( ztrans );
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.translationMatrix3D(xtransl, ytransl, ztransl);
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = xtrans;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = ytrans;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
			imat[ 3 ][ 2 ] = ztrans;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
		
		
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
	 * Tests the ability to generate trsnslation matrices in 3-D
	 * @throws Throwable
	 */
	public final void testTranslationMatrix3D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xtrans = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double ytrans = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double ztrans = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xtransl = new DoubleElem( xtrans );
			final DoubleElem ytransl = new DoubleElem( ytrans );
			final DoubleElem ztransl = new DoubleElem( ztrans );
			
			final BigInteger xAxis = BigInteger.ZERO;
			final BigInteger yAxis = BigInteger.ONE;
			final BigInteger zAxis = BigInteger.valueOf(2);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> transl = se.zero();
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( xAxis );
				transl.setVal(idx, xtransl);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( yAxis );
				transl.setVal(idx, ytransl);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( zAxis );
				transl.setVal(idx, ztransl);
			}
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.translationMatrix(transl);
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = xtrans;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = ytrans;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
			imat[ 3 ][ 2 ] = ztrans;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
		
		
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
	 * Tests the ability to generate translation matrices in 4-D
	 * @throws Throwable
	 */
	public final void testTranslationMatrix4D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFive,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFive,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double xtrans = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double ytrans = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double ztrans = 2.0 * ( rand.nextDouble() ) - 1.0;
			final double ttrans = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem xtransl = new DoubleElem( xtrans );
			final DoubleElem ytransl = new DoubleElem( ytrans );
			final DoubleElem ztransl = new DoubleElem( ztrans );
			final DoubleElem ttransl = new DoubleElem( ttrans );
			
			final BigInteger xAxis = BigInteger.ZERO;
			final BigInteger yAxis = BigInteger.ONE;
			final BigInteger zAxis = BigInteger.valueOf(2);
			final BigInteger tAxis = BigInteger.valueOf(3);
			
			final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> transl = se.zero();
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( xAxis );
				transl.setVal(idx, xtransl);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( yAxis );
				transl.setVal(idx, ytransl);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( zAxis );
				transl.setVal(idx, ztransl);
			}
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( tAxis );
				transl.setVal(idx, ttransl);
			}
		
		
			final SquareMatrixElem<TestDimensionFive,DoubleElem,DoubleElemFactory> mat = mg.translationMatrix(transl);
		
			final double imat[][] = new double[ 5 ][ 5 ];
		
		
			// NOTE: matrix to validate against adapted from https://open.gl/transformations 
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = 0.0;
			imat[ 4 ][ 0 ] = xtrans;
		
		
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = 0.0;
			imat[ 4 ][ 1 ] = ytrans;
		
		
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
			imat[ 3 ][ 2 ] = 0.0;
			imat[ 4 ][ 2 ] = ztrans;
		
		
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
			imat[ 4 ][ 3 ] = ttrans;
		
		
			imat[ 0 ][ 4 ] = 0.0;
			imat[ 1 ][ 4 ] = 0.0;
			imat[ 2 ][ 4 ] = 0.0;
			imat[ 3 ][ 4 ] = 0.0;
			imat[ 4 ][ 4 ] = 1.0;
		
		
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
	 * Tests the ability to generate translation matrices in 25-D
	 * @throws Throwable
	 */
	public final void testTranslationMatrix25D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DimTwentySix td = new DimTwentySix();
		
		final GeometricAlgebraOrd<DimTwentySix> ord = new GeometricAlgebraOrd<DimTwentySix>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<DimTwentySix,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<DimTwentySix,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>(se, me);
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double atrans = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem atransl = new DoubleElem( atrans );
			
			final BigInteger axis = BigInteger.valueOf( rand.nextInt( 25 ) );
			
			final GeometricAlgebraMultivectorElem<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> transl = se.zero();
			
			{
				final HashSet<BigInteger> idx = new HashSet<BigInteger>();
				idx.add( axis );
				transl.setVal(idx, atransl);
			}
		
		
			final SquareMatrixElem<DimTwentySix,DoubleElem,DoubleElemFactory> mat = mg.translationMatrix(transl);
		
			final double imat[][] = new double[ 26 ][ 26 ];
			
			
			for( int row = 0 ; row < 26 ; row++ )
			{
				for( int col = 0 ; col < 26 ; col++ )
				{
					imat[row][col] = ( row == col ? 1.0 : 0.0 );
				}
			}
			
			imat[ 25 ][ axis.intValue() ] = atrans;
		
		
			for( int row = 0 ; row < 26 ; row++ )
			{
				for( int col = 0 ; col < 26 ; col++ )
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
	public final void testShearingMatrix2D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		final int gdim = td.getVal().intValue() - 1;
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( gdim ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( gdim ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, DoubleElem, DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
			
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.shearingMatrix2D(hm);
		
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
	 * Tests the ability to generate shearing matrices in 2-D
	 * @throws Throwable
	 */
	public final void testShearingMatrix2D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionThree,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(se, me);
		
		final int gdim = td.getVal().intValue() - 1;
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
			
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( gdim ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( gdim ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, DoubleElem, DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
			
		
		
			final SquareMatrixElem<TestDimensionThree,DoubleElem,DoubleElemFactory> mat = mg.shearingMatrix(hm);
		
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
	public final void testShearingMatrix3D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec_Facade<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		final int gdim = td.getVal().intValue() - 1;
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
		
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( gdim ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( gdim ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
			
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.shearingMatrix3D(hm);
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = 0.0;
					
					
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = 0.0;
					
					
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
			imat[ 3 ][ 2 ] = 0.0;
			
			
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
			
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
	 * Tests the ability to generate shearing matrices in 3-D
	 * @throws Throwable
	 */
	public final void testShearingMatrix3D_2()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(se, me);
		
		final int gdim = td.getVal().intValue() - 1;
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
			
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( gdim ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( gdim ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
			
		
		
			final SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> mat = mg.shearingMatrix(hm);
		
			final double imat[][] = new double[ 4 ][ 4 ];
		
		
			imat[ 0 ][ 0 ] = 1.0;
			imat[ 1 ][ 0 ] = 0.0;
			imat[ 2 ][ 0 ] = 0.0;
			imat[ 3 ][ 0 ] = 0.0;
					
					
			imat[ 0 ][ 1 ] = 0.0;
			imat[ 1 ][ 1 ] = 1.0;
			imat[ 2 ][ 1 ] = 0.0;
			imat[ 3 ][ 1 ] = 0.0;
					
					
			imat[ 0 ][ 2 ] = 0.0;
			imat[ 1 ][ 2 ] = 0.0;
			imat[ 2 ][ 2 ] = 1.0;
			imat[ 3 ][ 2 ] = 0.0;
			
			
			imat[ 0 ][ 3 ] = 0.0;
			imat[ 1 ][ 3 ] = 0.0;
			imat[ 2 ][ 3 ] = 0.0;
			imat[ 3 ][ 3 ] = 1.0;
			
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
	 * Tests the ability to generate shearing matrices in 4-D
	 * @throws Throwable
	 */
	public final void testShearingMatrix4D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<TestDimensionFive,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<TestDimensionFive,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(se, me);
		
		final int gdim = td.getVal().intValue() - 1;
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
			
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( gdim ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( gdim ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionFive, GeometricAlgebraOrd<TestDimensionFive>, DoubleElem, DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
			
		
		
			final SquareMatrixElem<TestDimensionFive,DoubleElem,DoubleElemFactory> mat = mg.shearingMatrix(hm);
		
			final double imat[][] = new double[ 5 ][ 5 ];
		
		
			for( int row = 0 ; row < 5 ; row++ )
			{
				for( int col = 0 ; col < 5 ; col++ )
				{
					imat[row][col] = ( row == col ? 1.0 : 0.0 );
				}
			}
			
			imat[ rowa.intValue() ][ cola.intValue() ] = sher;
		
		
			for( int row = 0 ; row < 5 ; row++ )
			{
				for( int col = 0 ; col < 5 ; col++ )
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
	public final void testShearingMatrix25D()  throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final DimTwentySix td = new DimTwentySix();
		
		final GeometricAlgebraOrd<DimTwentySix> ord = new GeometricAlgebraOrd<DimTwentySix>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SquareMatrixElemFactory<DimTwentySix,DoubleElem,DoubleElemFactory> me = 
				new SquareMatrixElemFactory<DimTwentySix,DoubleElem,DoubleElemFactory>(dl, td);
		
		final Tdg_TransformMatrixGenPerspec<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory> mg =
				new Tdg_TransformMatrixGenPerspec<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>(se, me);
		
		final int gdim = td.getVal().intValue() - 1;
		
		
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final double sher = 2.0 * ( rand.nextDouble() ) - 1.0;
			
			final DoubleElem shear = new DoubleElem( sher );
			
			final BigInteger rowa = BigInteger.valueOf( rand.nextInt( gdim ) );
			
			BigInteger cola = rowa;
			
			while( cola.equals( rowa ) )
			{
				cola = BigInteger.valueOf( rand.nextInt( gdim ) );
			}
			
			
			
			final GeometricAlgebraMultivectorElem<DimTwentySix, GeometricAlgebraOrd<DimTwentySix>, DoubleElem, DoubleElemFactory> vect = se.zero();
			
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			
			hs.add( cola );
			
			vect.setVal(hs, shear);
			
			final HashMap<BigInteger,GeometricAlgebraMultivectorElem<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>> hm = new HashMap<BigInteger,GeometricAlgebraMultivectorElem<DimTwentySix,GeometricAlgebraOrd<DimTwentySix>,DoubleElem,DoubleElemFactory>>();
			
			hm.put(rowa, vect);
			
		
		
			final SquareMatrixElem<DimTwentySix,DoubleElem,DoubleElemFactory> mat = mg.shearingMatrix(hm);
		
			final double imat[][] = new double[ 26 ][ 26 ];
		
		
			for( int row = 0 ; row < 26 ; row++ )
			{
				for( int col = 0 ; col < 26 ; col++ )
				{
					imat[row][col] = ( row == col ? 1.0 : 0.0 );
				}
			}
			
			imat[ rowa.intValue() ][ cola.intValue() ] = sher;
		
		
			for( int row = 0 ; row < 26 ; row++ )
			{
				for( int col = 0 ; col < 26 ; col++ )
				{
					DoubleElem d = mat.getVal( BigInteger.valueOf(row) , BigInteger.valueOf(col) );
					// System.out.println( ( d.getVal() ) + "   " + ( imat[row][col] ) );
					Assert.assertTrue( Math.abs( d.getVal() - imat[row][col] ) < 1E-5 );
				}
			}
		}
		
	}
	
	

	
} /* Test Class */



