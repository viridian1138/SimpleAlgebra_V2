




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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.algo.*;
import simplealgebra.ddx.DirectionalDerivative;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.symbolic.SymbolicSqrt;
import simplealgebra.symbolic.SymbolicZero;
import simplealgebra.ddx.*;
import simplealgebra.ga.*;
import simplealgebra.et.*;
import test_simplealgebra.TestConnectionCoefficient.TestMetricTensorFactory;










/**
 * Tests the ability to numerically evaluate the differential equation <math display="inline">
 * <mrow>
 *   <msub>
 *           <mi>G</mi>
 *       <mrow>
 *         <mi>u</mi>
 *         <mi>v</mi>
 *       </mrow>
 *   </msub>
 *  <mo>=</mo>
 *  <mn>0</mn>
 * </mrow>
 * </math> where <math display="inline">
 * <mrow>
 *   <msub>
 *           <mi>G</mi>
 *       <mrow>
 *         <mi>u</mi>
 *         <mi>v</mi>
 *       </mrow>
 *   </msub>
 * </mrow>
 * </math> is the Einstein tensor.
 * 
 * 
 * 
 * See http://en.wikipedia.org/wiki/Einstein_tensor
 *
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestGeneralRelativityAdm extends TestCase {
	
	
	/**
	 * DoubleElem representing the constant zero.
	 */
	private static final DoubleElem DOUBLE_ZERO = new DoubleElem( 0.0 );
	
	/**
	 * DoubleElem representing the constant 2.
	 */
	private static final DoubleElem MM = genFromConstDbl( 2.0 );
	
	
	
	/**
	 * Returns a tensor scalar that is equal to the parameter value.
	 * 
	 * @param in The value to be assigned to the scalar.
	 * @return The constructed tensor.
	 */
	private static EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> genFromConst( double in )  // Rank Zero.
	{
		final DoubleElem dd = new DoubleElem( in );
		DoubleElemFactory da = new DoubleElemFactory();
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>
			ret = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( da , new ArrayList() , new ArrayList() );
		ret.setVal( new ArrayList<BigInteger>() , dd );
		return( ret );
	}
	
	
	/**
	 * Random number generator for producing initial conditions.
	 */
	private static final Random rand = new Random( 4455 );
	
	
	/**
	 * Generates a default roughly flat rank 2 metric tensor.
	 * @return The default roughly flat rank 2 metric tensor.
	 */
	private static EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> genDiffAll( )
	{
		final double CV = C.getVal();
		DoubleElemFactory da = new DoubleElemFactory();
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		covariantIndices.add( "u" );
		covariantIndices.add( "v" );
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>
			ret = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( da , contravariantIndices  , covariantIndices );
		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			if( ( acnt % TestDimensionFour.FOUR ) == ( acnt / TestDimensionFour.FOUR ) )
			{
				// final DoubleElem dd = acnt == 0 ? 
				//		new DoubleElem( - ( C.getVal() ) * ( C.getVal() ) ) : new DoubleElem( 1.0 );
				final DoubleElem dd = acnt == 0 ? new DoubleElem( - CV * CV * ( 1.0 + ( 1E-6 * rand.nextDouble() ) ) )
					: new DoubleElem( 1.0 + ( 1E-6 * rand.nextDouble() ) );
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt / TestDimensionFour.FOUR ) );
				ab.add( BigInteger.valueOf( acnt % TestDimensionFour.FOUR ) );
				ret.setVal( ab , dd );
			}
		}
		return( ret );
	}
	
	
	/**
	 * Generates a random tensor of rank 2.
	 * @return The random tensor.
	 */
	private static EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> genDiffEnt( )
	{
		final double CV = C.getVal();
		DoubleElemFactory da = new DoubleElemFactory();
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		covariantIndices.add( "u" );
		covariantIndices.add( "v" );
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>
			ret = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( da , contravariantIndices  , covariantIndices );
		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			if( ( acnt % TestDimensionFour.FOUR ) == ( acnt / TestDimensionFour.FOUR ) )
			{
				final DoubleElem dd = acnt == 0 ? new DoubleElem( - CV * CV * ( 1.0 + ( 0.001 * rand.nextDouble() ) ) )
					: new DoubleElem( 1.0 + ( 0.001 * rand.nextDouble() ) );
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt / TestDimensionFour.FOUR ) );
				ab.add( BigInteger.valueOf( acnt % TestDimensionFour.FOUR ) );
				ret.setVal( ab , dd );
			}
		}
		return( ret );
	}
	
	
	/**
	 * Returns a tensor equal to the input value.
	 * @param in The input value.
	 * @return The generated tensor.
	 */
	private static DoubleElem genFromConstDbl( double in )
	{
		return( new DoubleElem( in ) );
	}
	
	
	
	/**
	 * Arbitrary constant.
	 */
	protected static final DoubleElem C = genFromConstDbl( 0.004 );
	
	
	
	/**
	 * The total measurement size along the X-Axis.
	 */
	protected static final double TOTAL_X_AXIS_SIZE = 0.1;
	
	/**
	 * The total measurement size along the Y-Axis.
	 */
	protected static final double TOTAL_Y_AXIS_SIZE = 0.1;
	
	/**
	 * The total measurement size along the Z-Axis.
	 */
	protected static final double TOTAL_Z_AXIS_SIZE = 0.1;
	
	
	/**
	 * The number of discretizations on the T-Axis over which to iterate.
	 */
	protected static final int NUM_T_ITER = 9;
	
	/**
	 * The number of discretizations on the X-Axis over which to iterate.
	 */
	protected static final int NUM_X_ITER = 12;
	
	/**
	 * The number of discretizations on the Y-Axis over which to iterate.
	 */
	protected static final int NUM_Y_ITER = 12;
	
	/**
	 * The number of discretizations on the Z-Axis over which to iterate.
	 */
	protected static final int NUM_Z_ITER = 12;
	
	
	
	/**
	 * Size of the T-Axis discretization.
	 */
	protected static final DoubleElem T_HH = genFromConstDbl( 0.0025 );
	
	/**
	 * Size of the X-Axis discretization.
	 */
	protected static final DoubleElem X_HH = genFromConstDbl( TOTAL_X_AXIS_SIZE / NUM_X_ITER /* 0.01 */ );
	
	/**
	 * Size of the Y-Axis discretization.
	 */
	protected static final DoubleElem Y_HH = genFromConstDbl( TOTAL_Y_AXIS_SIZE / NUM_Y_ITER /* 0.01 */ );
	
	/**
	 * Size of the Z-Axis discretization.
	 */
	protected static final DoubleElem Z_HH = genFromConstDbl( TOTAL_Z_AXIS_SIZE / NUM_Z_ITER /* 0.01 */ );
	
	/**
	 * Discretization sizes arrayed by coordinate index.
	 */
	protected static final DoubleElem[] HH = { T_HH , X_HH , Y_HH , Z_HH };
	
	
	
	/**
	 * The halfway iteration point in X.
	 */
	protected static final int HALF_X = NUM_X_ITER / 2;
	
	/**
	 * The halfway iteration point in Y.
	 */
	protected static final int HALF_Y = NUM_Y_ITER / 2;
	
	/**
	 * The halfway iteration point in Z.
	 */
	protected static final int HALF_Z = NUM_Z_ITER / 2;
	
	
	/**
	 * The initial condition radius in X.
	 */
	protected static final double RAD_X = NUM_X_ITER / 10.0;
	
	/**
	 * The initial condition radius in Y.
	 */
	protected static final double RAD_Y = NUM_Y_ITER / 10.0;
	
	/**
	 * The initial condition radius in Z.
	 */
	protected static final double RAD_Z = NUM_Z_ITER / 10.0;
	
	
	
	
	
	/**
	 * Temp step size in the T-direction.
	 */
	protected static final int NSTPT = 2;
	
	
	/**
	 * Temp step size in the X-direction.
	 */
	protected static final int NSTPX = 2;
	
	
	/**
	 * Temp step size in the Y-direction.
	 */
	protected static final int NSTPY = 2;
	
	
	/**
	 * Temp step size in the Z-direction.
	 */
	protected static final int NSTPZ = 2;
	

	/**
	 * Indicates whether predictor-corrector should be used while iterating.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	// protected static final boolean USE_PREDICTOR_CORRECTOR = true;
	
	
	
	
	
	/**
	 * Result array over which to iterate.
	 */
	protected static final EinsteinTensorElem[][][][] iterArrayMetric = new EinsteinTensorElem[ NUM_T_ITER ][ NUM_X_ITER ][ NUM_Y_ITER ][ NUM_Z_ITER ];
	

	/**
	 * Result array over which to iterate.
	 */
	protected static final EinsteinTensorElem[][][][] iterArrayConjugateMomentum = new EinsteinTensorElem[ NUM_T_ITER ][ NUM_X_ITER ][ NUM_Y_ITER ][ NUM_Z_ITER ];
	
	
	
	
	
	/**
	 * Discretized index for the T-Axis.
	 */
	protected static final int TV = 0;
	
	/**
	 * Discretized index for the X-Axis.
	 */
	protected static final int XV = 1;
	
	/**
	 * Discretized index for the Y-Axis.
	 */
	protected static final int YV = 2;
	
	/**
	 * Discretized index for the Z-Axis.
	 */
	protected static final int ZV = 3;
	
	
	
	/**
	 * Temporary array in which to generate descent algorithm solutions.
	 * <p>0 = T
	 * <p>1 = X
	 * <p>2 = Y
	 * <p>3 = Z
	 */
	private static EinsteinTensorElem[][][][] tempArrayMetric = new EinsteinTensorElem[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPY * 2 + 1 ][ NSTPZ * 2 + 1 ];
	
	/**
	 * Temporary array in which to generate descent algorithm solutions.
	 * <p>0 = T
	 * <p>1 = X
	 * <p>2 = Y
	 * <p>3 = Z
	 */
	private static EinsteinTensorElem[][][][] tempArrayConjugateMomentum = new EinsteinTensorElem[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPY * 2 + 1 ][ NSTPZ * 2 + 1 ];
	
	
	
	
	
	/**
	 * Given a change calculated by a descent algorithm iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void performIterationUpdateMetric( DoubleElem dbl , ArrayList<BigInteger> tensorIndex )
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArrayMetric[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		if( va == null )
		{
			final DoubleElemFactory de = new DoubleElemFactory();
			final ArrayList<String> contravariantIndices = new ArrayList<String>();
			final ArrayList<String> covariantIndices = new ArrayList<String>();
			covariantIndices.add( "i" );
			covariantIndices.add( "j" );
			final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>
				et = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , contravariantIndices , covariantIndices );
			et.setVal( tensorIndex , dbl );			
			tempArrayMetric[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] = et;
			return;
		}
		va.setVal( tensorIndex, va.getVal( tensorIndex ).add( dbl ) );
	}
	
	
	
	/**
	 * Given a change calculated by a descent algorithm iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void performIterationUpdateConjugateMomentum( DoubleElem dbl , ArrayList<BigInteger> tensorIndex )
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArrayConjugateMomentum[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		if( va == null )
		{
			final DoubleElemFactory de = new DoubleElemFactory();
			final ArrayList<String> contravariantIndices = new ArrayList<String>();
			final ArrayList<String> covariantIndices = new ArrayList<String>();
			contravariantIndices.add( "i" );
			contravariantIndices.add( "j" );
			final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>
				et = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , contravariantIndices , covariantIndices );
			et.setVal( tensorIndex , dbl );
			tempArrayConjugateMomentum[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] = et;
			return;
		}
		va.setVal( tensorIndex, va.getVal( tensorIndex ).add( dbl ) );
	}
	
	
	
	/**
	 * Given a change calculated by a descent algorithm iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void setIterationValueMetric( EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> dbl )
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArrayMetric[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		tempArrayMetric[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] = dbl;
	}
	
	
	
	
	/**
	 * Given a change calculated by a descent algorithm iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void setIterationValueConjugateMomentum( EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> dbl )
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArrayConjugateMomentum[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		tempArrayConjugateMomentum[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] = dbl;
	}
	
	
	
	
	/**
	 * Returns the result of the descent algorithm iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static DoubleElem getUpdateValueMetric( final ArrayList<BigInteger> index )
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArrayMetric[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		if( va != null )
		{
			DoubleElem ret = va.getVal( index );
			if( ret != null )
			{
				return( ret );
			}
			DoubleElemFactory da = new DoubleElemFactory();
			return( da.zero() );
		}
		DoubleElemFactory da = new DoubleElemFactory();
		return( da.zero() );
	}
	
	
	
	/**
	 * Returns the result of the descent algorithm iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static DoubleElem getUpdateValueConjugateMomentum( final ArrayList<BigInteger> index )
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArrayConjugateMomentum[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		if( va != null )
		{
			DoubleElem ret = va.getVal( index );
			if( ret != null )
			{
				return( ret );
			}
			DoubleElemFactory da = new DoubleElemFactory();
			return( da.zero() );
		}
		DoubleElemFactory da = new DoubleElemFactory();
		return( da.zero() );
	}
	
	
	
	/**
	 * Returns the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> getCorrectionValueMetric()
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArrayMetric[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		if( va != null )
		{
			return( va );
		}
		DoubleElemFactory da = new DoubleElemFactory();
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		covariantIndices.add( "u" );
		covariantIndices.add( "v" );
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>
			ret = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( da , contravariantIndices  , covariantIndices );
		return( ret );
	}
	
	
	
	/**
	 * Returns the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> getCorrectionValueConjugateMomentum()
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArrayConjugateMomentum[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		if( va != null )
		{
			return( va );
		}
		DoubleElemFactory da = new DoubleElemFactory();
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		covariantIndices.add( "u" );
		covariantIndices.add( "v" );
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>
			ret = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( da , contravariantIndices  , covariantIndices );
		return( ret );
	}
	
	
	
	/**
	 * Resets the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @param in The value to which to reset.
	 */
	protected static void resetCorrectionValueMetric( final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> in )
	{
		tempArrayMetric[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] = in;
	}
	
	
	/**
	 * Resets the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @param in The value to which to reset.
	 */
	protected static void resetCorrectionValueConjugateMomentum( final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> in )
	{
		tempArrayConjugateMomentum[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] = in;
	}
	
	

	
	
	/**
	 * Input parameter for fillTempArrayInner()
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TempArrayFillInnerParam
	{
		
		
		
		
		
		
		/**
		 * Gets the T-Axis index for the center in the iter array.
		 * 
		 * @return The T-Axis index for the center in the iter array.
		 */
		public int getTcnt() {
			return tcnt;
		}


		/**
		 * Sets the T-Axis index for the center in the iter array.
		 * 
		 * @param tcnt The T-Axis index for the center in the iter array.
		 */
		public void setTcnt(int tcnt) {
			this.tcnt = tcnt;
		}


		/**
		 * Gets the X-Axis index for the center in the iter array.
		 * 
		 * @return The X-Axis index for the center in the iter array.
		 */
		public int getXcnt() {
			return xcnt;
		}


		/**
		 * Sets the X-Axis index for the center in the iter array.
		 * 
		 * @param xcnt The X-Axis index for the center in the iter array.
		 */
		public void setXcnt(int xcnt) {
			this.xcnt = xcnt;
		}


		/**
		 * Gets the Y-Axis index for the center in the iter array.
		 * 
		 * @return The Y-Axis index for the center in the iter array.
		 */
		public int getYcnt() {
			return ycnt;
		}


		/**
		 * Sets the Y-Axis index for the center in the iter array.
		 * 
		 * @param ycnt The Y-Axis index for the center in the iter array.
		 */
		public void setYcnt(int ycnt) {
			this.ycnt = ycnt;
		}


		/**
		 * Gets the Z-Axis index for the center in the iter array.
		 * 
		 * @return The Z-Axis index for the center in the iter array.
		 */
		public int getZcnt() {
			return zcnt;
		}


		/**
		 * Sets the Z-Axis index for the center in the iter array.
		 * 
		 * @param zcnt The Z-Axis index for the center in the iter array.
		 */
		public void setZcnt(int zcnt) {
			this.zcnt = zcnt;
		}


		/**
		 * Gets the T-Axis iteration of the array fill.
		 * 
		 * @return The T-Axis iteration of the array fill.
		 */
		public int getTa() {
			return ta;
		}


		/**
		 * Sets the T-Axis iteration of the array fill.
		 * 
		 * @param ta The T-Axis iteration of the array fill.
		 */
		public void setTa(int ta) {
			this.ta = ta;
		}


		/**
		 * Gets the X-Axis iteration of the array fill.
		 * 
		 * @return The X-Axis iteration of the array fill.
		 */
		public int getXa() {
			return xa;
		}


		/**
		 * Sets the X-Axis iteration of the array fill.
		 * 
		 * @param xa The X-Axis iteration of the array fill.
		 */
		public void setXa(int xa) {
			this.xa = xa;
		}


		/**
		 * Gets the Y-Axis iteration of the array fill.
		 * 
		 * @return The Y-Axis iteration of the array fill.
		 */
		public int getYa() {
			return ya;
		}


		/**
		 * Sets the Y-Axis iteration of the array fill.
		 * 
		 * @param ya The Y-Axis iteration of the array fill.
		 */
		public void setYa(int ya) {
			this.ya = ya;
		}


		/**
		 * Gets the Z-Axis iteration of the array fill.
		 * 
		 * @return The Z-Axis iteration of the array fill.
		 */
		public int getZa() {
			return za;
		}


		/**
		 * Sets the Z-Axis iteration of the array fill.
		 * 
		 * @param za The Z-Axis iteration of the array fill.
		 */
		public void setZa(int za) {
			this.za = za;
		}


		/**
		 * The T-Axis index for the center in the iter array.
		 */
		protected int tcnt;
		
		/**
		 * The X-Axis index for the center in the iter array.
		 */
		protected int xcnt;
		
		/**
		 * The Y-Axis index for the center in the iter array.
		 */
		protected int ycnt;
		
		/**
		 * The Z-Axis index for the center in the iter array.
		 */
		protected int zcnt;
		
		/**
		 * The T-Axis iteration of the array fill.
		 */
		protected int ta;
		
		/**
		 * The X-Axis iteration of the array fill.
		 */
		protected int xa;
		
		/**
		 * The Y-Axis iteration of the array fill.
		 */
		protected int ya;
	
		/**
		 * The Z-Axis iteration of the array fill.
		 */
		protected int za;
		
	
	}
	
	
	
	/**
	 * Fills one element of the temp array with an element from the iter array.
	 * 
	 * @param param Input parameter describing where to get the element.
	 */
	protected static void fillTempArrayInner( TempArrayFillInnerParam param )
	{
		final int tcnt = param.getTcnt();
		final int xcnt = param.getXcnt();
		final int ycnt = param.getYcnt();
		final int zcnt = param.getZcnt();
		
		final int ta = param.getTa();
		final int xa = param.getXa();
		final int ya = param.getYa();
		final int za = param.getZa();
		
		final int tv = tcnt + ta;
		final int xv = xcnt + xa;
		final int yv = ycnt + ya;
		final int zv = zcnt + za;
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> avmet = null;
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> avmom = null;
		if( ( tv >= 0 )  && ( xv >= 0 ) && ( yv >= 0 ) && ( zv >= 0 ) &&
			( tv < NUM_T_ITER ) && ( xv < NUM_X_ITER ) && ( yv < NUM_Y_ITER ) && ( zv < NUM_Z_ITER )  )
		{
			if( ta != NSTPT )
			{
				avmet = iterArrayMetric[ tv ][ xv ][ yv ][ zv ];
				avmom = iterArrayConjugateMomentum[ tv ][ xv ][ yv ][ zv ];
			}
			else
			{
				if( ( xa == 0 ) && ( ya == 0 ) && ( za == 0 ) )
				{
					avmet = iterArrayMetric[ tv ][ xv ][ yv ][ zv ];
					avmom = iterArrayConjugateMomentum[ tv ][ xv ][ yv ][ zv ];
				}
			}
		}
		if( avmet == null )
		{
			avmet = genDiffAll();
		}
		if( avmom == null )
		{
			avmom = genDiffAll();
		}
		tempArrayMetric[ ta + NSTPT ][ xa + NSTPX ][ ya + NSTPY ][ za + NSTPZ ] = avmet;
		tempArrayConjugateMomentum[ ta + NSTPT ][ xa + NSTPX ][ ya + NSTPY ][ za + NSTPZ ] = avmom;
		
	}
	
	
	
	
	/**
	 * Fills the temp array with elements from the iter array.
	 * 
	 * @param tcnt The T-Axis index for the center in the iter array.
	 * @param xcnt The X-Axis index for the center in the iter array.
	 * @param ycnt The Y-Axis index for the center in the iter array.
	 * @param zcnt The Z-Axis index for the center in the iter array.
	 */
	protected static void fillTempArray( final int tcnt , final int xcnt , final int ycnt , final int zcnt )
	{
		final TempArrayFillInnerParam param = new TempArrayFillInnerParam();
		
		param.setTcnt( tcnt );
		param.setXcnt( xcnt );
		param.setYcnt( ycnt );
		param.setZcnt( zcnt );
		
		for( int ta = -NSTPT ; ta < NSTPT ; ta++ )
		{
			param.setTa( ta );
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				param.setXa( xa );
				for( int ya = -NSTPY ; ya < NSTPY + 1 ; ya++ )
				{
					param.setYa( ya );
					for( int za = -NSTPX ; za < NSTPZ + 1 ; za++ )
					{
						param.setZa( za );
						fillTempArrayInner( param );
					}
				}
			}
		}
		
		
		// Overlay initial seed for iterations.
		for( int xa = 0 ; xa < NSTPX * 2 + 1 ; xa++ )
		{
			for( int ya = 0 ; ya < NSTPY * 2 + 1 ; ya++ )
			{
				for( int za = 0 ; za < NSTPZ * 2 + 1 ; za++ )
				{
					tempArrayMetric[ NSTPT * 2 ][ xa ][ ya ][ za ] = tempArrayMetric[ NSTPT * 2 - 1 ][ xa ][ ya ][ za ];
					tempArrayConjugateMomentum[ NSTPT * 2 ][ xa ][ ya ][ za ] = tempArrayConjugateMomentum[ NSTPT * 2 - 1 ][ xa ][ ya ][ za ];
				}
			}
		}
	}
	
	
	
	
	/**
	 * Test array used to verify that the entire temp array has been filled.
	 */
	private static int[][][][] spatialAssertArray = new int[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPY * 2 + 1 ][ NSTPZ * 2 + 1 ];
	
	
	
	/**
	 * Clears the test array used to verify that the entire temp array has been filled.
	 */
	protected static void clearSpatialAssertArray( )
	{
		for( int ta = -NSTPT ; ta < NSTPT + 1 ; ta++ )
		{
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				for( int ya = -NSTPY ; ya < NSTPY + 1 ; ya++ )
				{
					for( int za = -NSTPZ ; za < NSTPZ + 1 ; za++ )
					{
						spatialAssertArray[ ta + NSTPT ][ xa + NSTPX ][ ya + NSTPY ][ za + NSTPZ ] = 0;
					}
				}
			}
		}
	}
	
	
	
	
	


	
/**
 * Returns the sum of the squares of all of the tensor elements.
 * 	
 * @param in The input tensor.
 * @return The sum of the squares of the tensor values.
 */
private double calcMagnitudeSq( EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> in )
{
	double db = 0.0;
	Iterator<ArrayList<BigInteger>> it = in.getKeyIterator();
	while( it.hasNext() )
	{
		final ArrayList<BigInteger> elem = it.next();
		final DoubleElem dbl = in.getVal( elem );
		final double vl = dbl.getVal();
		System.out.print( elem + " " + vl + " " );
		db += vl * vl;
	}
	return( db );
}




/**
 * Returns the square of the magnitude of a DoubleElem.
 * 
 * @param in The DoubleElem.
 * @return The square of the magnitude of the DoubleElem.
 */
private double calcMagnitudeSq( DoubleElem in )
{
	double db = in.getVal();
	return( db * db );
}




/**
 * Returns whether two tensors are exactly equal.
 * 
 * @param a Tensor to be checked for equality.
 * @param b Tensor to be checked for equality.
 * @return True iff. the two tensors are exactly equal.
 */
private boolean calcEq( EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> a , EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> b )
{
	Iterator<ArrayList<BigInteger>> it = a.getKeyIterator();
	while( it.hasNext() )
	{
		final ArrayList<BigInteger> elem = it.next();
		final DoubleElem dbla = a.getVal( elem );
		final DoubleElem dblb = b.getVal( elem );
		if( ( dbla == null ) || ( dblb == null ) )
		{
			return( false );
		}
		if( dbla.getVal() != dblb.getVal() )
		{
			return( false );
		}
	}
	it = b.getKeyIterator();
	while( it.hasNext() )
	{
		final ArrayList<BigInteger> elem = it.next();
		final DoubleElem dbla = a.getVal( elem );
		final DoubleElem dblb = b.getVal( elem );
		if( ( dbla == null ) || ( dblb == null ) )
		{
			return( false );
		}
		if( dbla.getVal() != dblb.getVal() )
		{
			return( false );
		}
	}
	
	return( true );
}
	


/**
 * Defines a directional derivative for the test.
 * 
 * @author thorngreen
 *
 */
private class DDirec4 extends DirectionalDerivativePartialFactory<
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	Ordinate>
{
	/**
	 * Factory for building the value of the derivative of an ordinate.
	 */
	EinsteinTensorElemFactory<String,DoubleElem, DoubleElemFactory> de;
	
	/**
	 * Factory for the enclosed type.
	 */
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2;
	
	/**
	 * Constructs the directional derivative factory.
	 * 
	 * @param _de Factory for building the value of the derivative of an ordinate.
	 * @param _se2 Factory for the enclosed type.
	 */
	public DDirec4( 
			final EinsteinTensorElemFactory<String,DoubleElem, DoubleElemFactory> _de ,
			final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _se2 )
	{
		de = _de;
		se2 = _se2;
	}

	@Override
	public SymbolicElem<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			getPartial( BigInteger basisIndex )
	{
		final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
		
		wrtX.add( new Ordinate( de , basisIndex.intValue() ) );
		
		SymbolicElem<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
			ret =
					new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate>( se2 , wrtX );
		
		if( basisIndex.equals( BigInteger.ZERO ) )
		{
			try
			{
				final DoubleElemFactory de2 = new DoubleElemFactory();
				final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de2 );
				final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
				final DoubleElem cinv = C.invertLeft();
				final SymbolicElem<
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
					cmul = ( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( 
							cinv , de2 ) , seA ) , se2A )
							);
				ret = cmul.mult( ret );
			}
			catch( NotInvertibleException ex )
			{
				Assert.assertNull( ex );
			}
		}
			
		
		return( ret );
	}

};




/**
 * Defines a directional derivative for the test.
 * 
 * @author thorngreen
 *
 */
private class DDirec3 extends DirectionalDerivativePartialFactory<
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	Ordinate>
{
	/**
	 * Factory for building the value of the derivative of an ordinate.
	 */
	EinsteinTensorElemFactory<String,DoubleElem, DoubleElemFactory> de;
	
	/**
	 * Factory for the enclosed type.
	 */
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2;
	
	/**
	 * Constructs the directional derivative factory.
	 * 
	 * @param _de Factory for building the value of the derivative of an ordinate.
	 * @param _se2 Factory for the enclosed type.
	 */
	public DDirec3( 
			final EinsteinTensorElemFactory<String,DoubleElem, DoubleElemFactory> _de ,
			final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _se2 )
	{
		de = _de;
		se2 = _se2;
	}

	@Override
	public SymbolicElem<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			getPartial( BigInteger basisIndex )
	{
		final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
		
		wrtX.add( new Ordinate( de , basisIndex.intValue() ) );
		
		SymbolicElem<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
			ret =
					new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate>( se2 , wrtX );
		
		if( basisIndex.equals( BigInteger.ZERO ) )
		{
			final SymbolicElem<
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
				zer = new SymbolicZero<
						SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
						SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2 );
			ret = zer;
		}
			
		
		return( ret );
	}

};




/**
 * Node representing an ordinate of the coordinate space.
 * 
 * @author thorngreen
 *
 */
private class Ordinate extends SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>
{
	/**
	 * The number of the ordinate.
	 */
	private int col;

	/**
	 * Constructs the node.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _col The number of the ordinate.
	 */
	public Ordinate(EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> _fac, int _col) {
		super(_fac);
		col = _col;
	}

	@Override
	public EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	@Override
	public EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "a" + col + "()" );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof Ordinate )
		{
			return( col == ( (Ordinate) b ).col );
		}
		return( false );
	}
	
	@Override
	public boolean equals( Object b )
	{
		if( b instanceof Ordinate )
		{
			return( col == ( (Ordinate) b ).col );
		}
		return( false );
	}
	
	@Override
	public int hashCode()
	{
		return( col );
	}
	
	/**
	 * Returns the number of the ordinate.
	 * 
	 * @return The number of the ordinate.
	 */
	public int getCol() {
		return col;
	}
	
}


/**
 * A symbolic elem representing a constant value
 * at the base descent algorithm evaluation level.
 * 
 * @author thorngreen
 *
 */
private class SymbolicConst extends SymbolicReduction<DoubleElem,DoubleElemFactory>
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The constant to be represented.
	 * @param _fac The factory for the constant.
	 */
	public SymbolicConst(DoubleElem _elem, DoubleElemFactory _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( " " + ( this.getElem().getVal() ) );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
	{
		if( b instanceof SymbolicConst )
		{
			final boolean eq = ( getElem().getVal() ) == ( ( (SymbolicConst) b ).getElem().getVal() );
			return( eq );
		}
		return( false );
	}
	
}
	
	

/**
 * An elem representing a symbolic constant at the level of mapping 
 * discretized approximations of the underlying differential
 * equation into expressions (e.g. Jacobian slopes)
 * for descent algorithm evaluations.
 * 
 * @author thorngreen
 *
 */
private class StelemReduction2L extends SymbolicReduction<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The constant to be represented.
	 * @param _fac The factory for the constant.
	 */
	public StelemReduction2L( SymbolicElem<DoubleElem,DoubleElemFactory> _elem, 
			SymbolicElemFactory<DoubleElem,DoubleElemFactory> _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "reduce2L( " );
		getElem().writeString( ps );
		ps.print( " )" );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof StelemReduction2L )
		{
			return( getElem().symbolicEquals( ( (StelemReduction2L) b ).getElem() ) );
		}
		return( false );
	}
	
}



/**
 * An elem representing a symbolic constant at the level
 * of mapping the underlying differential equation into
 * its discretized approximation.
 * 
 * @author thorngreen
 *
 */
private class StelemReduction3L extends SymbolicReduction<
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The constant to be represented.
	 * @param _fac The factory for the constant.
	 */
	public StelemReduction3L(
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _elem, 
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "reduce3L( " );
		getElem().writeString( ps );
		ps.print( " )" );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> b )
	{
		if( b instanceof StelemReduction3L )
		{
			return( getElem().symbolicEquals( ( (StelemReduction3L) b ).getElem() ) );
		}
		return( false );
	}
	
}
	




/**
 * A constant coefficient with a numerator and a denominator.
 * 	
 * @author thorngreen
 *
 */
private class CoeffNode
{
	/**
	 * The numerator.
	 */
	private SymbolicElem<DoubleElem,DoubleElemFactory> numer;
	
	/**
	 * The denominator.
	 */
	private SymbolicElem<DoubleElem,DoubleElemFactory> denom;
	
	/**
	 * Constructs the coefficient.
	 * 
	 * @param _numer The numerator.
	 * @param _denom The denominator.
	 */
	public CoeffNode( SymbolicElem<DoubleElem,DoubleElemFactory> _numer , SymbolicElem<DoubleElem,DoubleElemFactory> _denom )
	{
		numer = _numer;
		denom = _denom;
	}
	
	/**
	 * Gets the numerator.
	 * 
	 * @return The numerator.
	 */
	public SymbolicElem<DoubleElem,DoubleElemFactory> getNumer() {
		return numer;
	}
	
	/**
	 * Gets the denominator.
	 * 
	 * @return The denominator.
	 */
	public SymbolicElem<DoubleElem,DoubleElemFactory> getDenom() {
		return denom;
	}
	
}






	/**
	 * Elem representing the discretized equivalent
	 * of one component of the tensor constrained by the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private class BNelemMetric extends Nelem<DoubleElem,DoubleElemFactory,Ordinate>
	{
		/**
		 * The index of the component of the tensor.
		 */
		protected ArrayList<BigInteger> index;
		

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
		 * @param _index The index of the component of the tensor.
		 */
		public BNelemMetric(DoubleElemFactory _fac, HashMap<Ordinate, BigInteger> _coord , ArrayList<BigInteger> _index ) {
			super(_fac, _coord);
			index = _index;
		}
		
		
		/**
		 * Column indices in the discretized space.
		 */
		protected final int[] cols = new int[ 4 ];
		
		/**
		 * Assertion booleans used to verify that all
		 * column indices have been initialized.
		 */
		protected final boolean[] assertCols = new boolean[ 4 ];
		

		@Override
		public DoubleElem eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			cols[ 0 ] = 0;
			cols[ 1 ] = 0;
			cols[ 2 ] = 0;
			cols[ 3 ] = 0;
			assertCols[ 0 ] = false;
			assertCols[ 1 ] = false;
			assertCols[ 2 ] = false;
			assertCols[ 3 ] = false;
			Assert.assertTrue( coord.keySet().size() == 4 );
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate keyCoord = it.next();
				BigInteger coordVal = coord.get( keyCoord );
				final int offset = keyCoord.getCol() == 3 ? NSTPZ : keyCoord.getCol() == 2 ? NSTPY : keyCoord.getCol() == 1 ? NSTPX : NSTPT;
				cols[ keyCoord.getCol() ] = coordVal.intValue() + offset;
				assertCols[ keyCoord.getCol() ] = true;
			}
			( spatialAssertArray[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ] )++;
			Assert.assertTrue( assertCols[ 0 ] );
			Assert.assertTrue( assertCols[ 1 ] );
			Assert.assertTrue( assertCols[ 2 ] );
			Assert.assertTrue( assertCols[ 3 ] );
			DoubleElem ret = null;
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> val = 
					TestGeneralRelativityAdm.tempArrayMetric[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ];
			if( val == null )
			{
				ret = fac.zero();
			}
			else
			{
				DoubleElem valA = val.getVal( index );
				if( valA == null )
				{
					ret = fac.zero();
				}
				else
				{
					ret = valA;
				}
			}
			return( ret );
		}

		@Override
		public void writeString( PrintStream ps ) {
			String s0 = "bn";
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate key = it.next();
				BigInteger val = coord.get( key );
				s0 = s0 + "[";
				s0 = s0 + key.getCol();
				s0 = s0 + ",";
				s0 = s0 + val.intValue();
				s0 = s0 + "]";
			}
			s0 = s0 + "{";
			Iterator<BigInteger> ita = index.iterator();
			while( ita.hasNext() )
			{
				BigInteger b = ita.next();
				s0 = s0 + b + ",";
			}
			s0 = s0 + "}()";
			ps.print( s0 );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof BNelemMetric )
			{
				BNelemMetric bn = (BNelemMetric) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				Iterator<Ordinate> it = coord.keySet().iterator();
				while( it.hasNext() )
				{
					Ordinate key = it.next();
					BigInteger ka = coord.get( key );
					BigInteger kb = bn.coord.get( key );
					if( ( ka == null ) || ( kb == null ) )
					{
						return( false );
					}
					if( !( ka.equals( kb ) ) )
					{
						return( false );
					}
				}
				return( true );
			}
			return( false );
		}
		
	}
	
	

	
	



	/**
	 * Elem representing the discretized equivalent
	 * of one component of the tensor constrained by the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private class BNelemConjugateMomentum extends Nelem<DoubleElem,DoubleElemFactory,Ordinate>
	{
		/**
		 * The index of the component of the tensor.
		 */
		protected ArrayList<BigInteger> index;
		

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
		 * @param _index The index of the component of the tensor.
		 */
		public BNelemConjugateMomentum(DoubleElemFactory _fac, HashMap<Ordinate, BigInteger> _coord , ArrayList<BigInteger> _index ) {
			super(_fac, _coord);
			index = _index;
		}
		
		
		/**
		 * Column indices in the discretized space.
		 */
		protected final int[] cols = new int[ 4 ];
		
		/**
		 * Assertion booleans used to verify that all
		 * column indices have been initialized.
		 */
		protected final boolean[] assertCols = new boolean[ 4 ];
		

		@Override
		public DoubleElem eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			cols[ 0 ] = 0;
			cols[ 1 ] = 0;
			cols[ 2 ] = 0;
			cols[ 3 ] = 0;
			assertCols[ 0 ] = false;
			assertCols[ 1 ] = false;
			assertCols[ 2 ] = false;
			assertCols[ 3 ] = false;
			Assert.assertTrue( coord.keySet().size() == 4 );
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate keyCoord = it.next();
				BigInteger coordVal = coord.get( keyCoord );
				final int offset = keyCoord.getCol() == 3 ? NSTPZ : keyCoord.getCol() == 2 ? NSTPY : keyCoord.getCol() == 1 ? NSTPX : NSTPT;
				cols[ keyCoord.getCol() ] = coordVal.intValue() + offset;
				assertCols[ keyCoord.getCol() ] = true;
			}
			( spatialAssertArray[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ] )++;
			Assert.assertTrue( assertCols[ 0 ] );
			Assert.assertTrue( assertCols[ 1 ] );
			Assert.assertTrue( assertCols[ 2 ] );
			Assert.assertTrue( assertCols[ 3 ] );
			DoubleElem ret = null;
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> val = 
					TestGeneralRelativityAdm.tempArrayConjugateMomentum[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ];
			if( val == null )
			{
				ret = fac.zero();
			}
			else
			{
				DoubleElem valA = val.getVal( index );
				if( valA == null )
				{
					ret = fac.zero();
				}
				else
				{
					ret = valA;
				}
			}
			return( ret );
		}

		@Override
		public void writeString( PrintStream ps ) {
			String s0 = "bn";
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate key = it.next();
				BigInteger val = coord.get( key );
				s0 = s0 + "[";
				s0 = s0 + key.getCol();
				s0 = s0 + ",";
				s0 = s0 + val.intValue();
				s0 = s0 + "]";
			}
			s0 = s0 + "{";
			Iterator<BigInteger> ita = index.iterator();
			while( ita.hasNext() )
			{
				BigInteger b = ita.next();
				s0 = s0 + b + ",";
			}
			s0 = s0 + "}()";
			ps.print( s0 );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof BNelemConjugateMomentum )
			{
				BNelemConjugateMomentum bn = (BNelemConjugateMomentum) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				Iterator<Ordinate> it = coord.keySet().iterator();
				while( it.hasNext() )
				{
					Ordinate key = it.next();
					BigInteger ka = coord.get( key );
					BigInteger kb = bn.coord.get( key );
					if( ( ka == null ) || ( kb == null ) )
					{
						return( false );
					}
					if( !( ka.equals( kb ) ) )
					{
						return( false );
					}
				}
				return( true );
			}
			return( false );
		}
		
	}
	
		
	
	
	
	

/**
 * Elem representing the symbolic expression for 
 * the discretized equivalent
 * of one component of the tensor constrained by the differential equation.
 * The partial derivatives of this elem generate
 * the slopes for producing descent algorithm iterations (e.g. the Jacobian slopes),
 * as opposed to partial derivatives for the underlying differential equation.
 * 
 * @author thorngreen
 *
 */	
private class CNelemMetric extends Nelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
	SymbolicElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
{
	/**
	 * The index of the component of the tensor.
	 */
	protected ArrayList<BigInteger> index;

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
	 * @param _index The index of the component of the tensor.
	 */
	public CNelemMetric(SymbolicElemFactory<DoubleElem,DoubleElemFactory> _fac, 
			HashMap<Ordinate, BigInteger> _coord , ArrayList<BigInteger> _index ) {
		super(_fac, _coord);
		index = _index;
	}

	@Override
	public SymbolicElem<DoubleElem,DoubleElemFactory> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		return( new BNelemMetric( fac.getFac() , coord , index ) );
	}
	
	
	@Override
	public SymbolicElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
		if( withRespectTo.size() > 1 )
		{
			return( fac.zero() );
		}
		Iterator<? extends Elem<?,?>> it = withRespectTo.iterator();
		CNelemMetric wrt = (CNelemMetric)( it.next() );
		// final boolean cond = this.symbolicDEval( wrt );
		final boolean cond = this.symbolicEquals( wrt );
		return( cond ? fac.identity() : fac.zero() );
	}
	

	@Override
	public void writeString( PrintStream ps ) {
		String s0 = "cn";
		Iterator<Ordinate> it = coord.keySet().iterator();
		while( it.hasNext() )
		{
			Ordinate key = it.next();
			BigInteger val = coord.get( key );
			s0 = s0 + "[";
			s0 = s0 + key.getCol();
			s0 = s0 + ",";
			s0 = s0 + val.intValue();
			s0 = s0 + "]";
		}
		s0 = s0 + "()";
		ps.print( s0 );
	}
	
	
//	
//	protected boolean symbolicDEval( 
//			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
//	{
//		if( b instanceof CNelem )
//		{
//			CNelem bn = (CNelem) b;
//			if( coord.keySet().size() != bn.coord.keySet().size() )
//			{
//				return( false );
//			}
//			Iterator<Ordinate> it = coord.keySet().iterator();
//			while( it.hasNext() )
//			{
//				Ordinate key = it.next();
//				BigInteger ka = coord.get( key );
//				BigInteger kb = bn.coord.get( key );
//				if( ( ka == null ) || ( kb == null ) )
//				{
//					return( false );
//				}
//				if( !( ka.equals( kb ) ) )
//				{
//					return( false );
//				}
//			}
//			return( true );
//		}
//		return( false );
//	}
//	
	
	
	@Override
	public boolean symbolicEquals( 
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof CNelemMetric )
		{
			CNelemMetric bn = (CNelemMetric) b;
			if( !( index.equals( bn.index ) ) )
			{
				return( false );
			}
			if( coord.keySet().size() != bn.coord.keySet().size() )
			{
				return( false );
			}
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate key = it.next();
				BigInteger ka = coord.get( key );
				BigInteger kb = bn.coord.get( key );
				if( ( ka == null ) || ( kb == null ) )
				{
					return( false );
				}
				if( !( ka.equals( kb ) ) )
				{
					return( false );
				}
			}
			return( true );
		}
		return( false );
	}
	
}
	
	




/**
* Elem representing the symbolic expression for 
* the discretized equivalent
* of one component of the tensor constrained by the differential equation.
* The partial derivatives of this elem generate
* the slopes for producing descent algorithm iterations (e.g. the Jacobian slopes),
* as opposed to partial derivatives for the underlying differential equation.
* 
* @author thorngreen
*
*/	
private class CNelemConjugateMomentum extends Nelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
SymbolicElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
{
/**
 * The index of the component of the tensor.
 */
protected ArrayList<BigInteger> index;

/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
 * @param _index The index of the component of the tensor.
 */
public CNelemConjugateMomentum(SymbolicElemFactory<DoubleElem,DoubleElemFactory> _fac, 
		HashMap<Ordinate, BigInteger> _coord , ArrayList<BigInteger> _index ) {
	super(_fac, _coord);
	index = _index;
}

@Override
public SymbolicElem<DoubleElem,DoubleElemFactory> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	return( new BNelemConjugateMomentum( fac.getFac() , coord , index ) );
}


@Override
public SymbolicElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
	if( withRespectTo.size() > 1 )
	{
		return( fac.zero() );
	}
	Iterator<? extends Elem<?,?>> it = withRespectTo.iterator();
	CNelemConjugateMomentum wrt = (CNelemConjugateMomentum)( it.next() );
	// final boolean cond = this.symbolicDEval( wrt );
	final boolean cond = this.symbolicEquals( wrt );
	return( cond ? fac.identity() : fac.zero() );
}


@Override
public void writeString( PrintStream ps ) {
	String s0 = "cn";
	Iterator<Ordinate> it = coord.keySet().iterator();
	while( it.hasNext() )
	{
		Ordinate key = it.next();
		BigInteger val = coord.get( key );
		s0 = s0 + "[";
		s0 = s0 + key.getCol();
		s0 = s0 + ",";
		s0 = s0 + val.intValue();
		s0 = s0 + "]";
	}
	s0 = s0 + "()";
	ps.print( s0 );
}


//
//protected boolean symbolicDEval( 
//		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
//{
//	if( b instanceof CNelem )
//	{
//		CNelem bn = (CNelem) b;
//		if( coord.keySet().size() != bn.coord.keySet().size() )
//		{
//			return( false );
//		}
//		Iterator<Ordinate> it = coord.keySet().iterator();
//		while( it.hasNext() )
//		{
//			Ordinate key = it.next();
//			BigInteger ka = coord.get( key );
//			BigInteger kb = bn.coord.get( key );
//			if( ( ka == null ) || ( kb == null ) )
//			{
//				return( false );
//			}
//			if( !( ka.equals( kb ) ) )
//			{
//				return( false );
//			}
//		}
//		return( true );
//	}
//	return( false );
//}
//


@Override
public boolean symbolicEquals( 
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
{
	if( b instanceof CNelemConjugateMomentum )
	{
		CNelemConjugateMomentum bn = (CNelemConjugateMomentum) b;
		if( !( index.equals( bn.index ) ) )
		{
			return( false );
		}
		if( coord.keySet().size() != bn.coord.keySet().size() )
		{
			return( false );
		}
		Iterator<Ordinate> it = coord.keySet().iterator();
		while( it.hasNext() )
		{
			Ordinate key = it.next();
			BigInteger ka = coord.get( key );
			BigInteger kb = bn.coord.get( key );
			if( ( ka == null ) || ( kb == null ) )
			{
				return( false );
			}
			if( !( ka.equals( kb ) ) )
			{
				return( false );
			}
		}
		return( true );
	}
	return( false );
}

}







/**
 * An elem defining a partial derivative that is evaluated over the discretized space of the test.
 * 
 * @author thorngreen
 *
 */	
private class AStelemMetric extends Stelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
	SymbolicElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
{	

/**
 * The index of the tensor component represented by the elem.
 */
protected ArrayList<BigInteger> index;


/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _index The index of the tensor component represented by the elem.
 */
public AStelemMetric( SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac,
		ArrayList<BigInteger> _index ) {
	super(_fac);
	index = _index;
}

@Override
public AStelemMetric cloneInstance() {
	AStelemMetric cl = new AStelemMetric( fac , index );
	Iterator<Ordinate> it = partialMap.keySet().iterator();
	while( it.hasNext() )
	{
		Ordinate key = it.next();
		cl.partialMap.put(key, partialMap.get(key) );
	}
	return( cl );
}

@Override
public SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> eval(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	
	
	HashMap<Ordinate,Ordinate> imp = (HashMap<Ordinate,Ordinate>) implicitSpace;
	
	
	HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesA = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
	
	
	{
		CoeffNode cf = new CoeffNode( fac.getFac().identity() , fac.getFac().identity() );
		HashMap<Ordinate, BigInteger> key = new HashMap<Ordinate, BigInteger>();
		Iterator<Ordinate> it = imp.keySet().iterator();
		while( it.hasNext() )
		{
			Ordinate ae = it.next();
			BigInteger valA = BigInteger.valueOf( imp.get( ae ).getCol() );
			key.put( ae , valA );
		}
		spacesA.put( key , cf );
	}
	
	
	{
		Iterator<Ordinate> it = partialMap.keySet().iterator();
		while( it.hasNext() )
		{
			HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesB = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
			final Ordinate ae = it.next();
			final BigInteger numDerivs = partialMap.get( ae );
			/* final boolean tmpChk = ( ae.getCol() == 0 ) && ( numDerivs.intValue() > 1 );
			if( tmpChk )
			{
				System.out.print( "** " + ( ae.getCol() ) );
				System.out.print(  " >> " + ( numDerivs.intValue() ) + " // " + index );
			} */
			applyDerivativeAction( spacesA , ae , numDerivs.intValue() , HH[ ae.getCol() ] , spacesB );
			spacesA = spacesB;
		}
	}
	
	
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ret = fac.zero();
	
	
	{
		Iterator<HashMap<Ordinate, BigInteger>> it = spacesA.keySet().iterator();
		while( it.hasNext() )
		{
			HashMap<Ordinate, BigInteger> spaceAe = it.next();
			CoeffNode coeff = spacesA.get( spaceAe );
			final CNelemMetric an0 = 
					new CNelemMetric( fac.getFac() , spaceAe , index );
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
				an1 = an0.mult( 
						new StelemReduction2L( coeff.getNumer() , fac.getFac() ) );
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> 
				an2 = an1.mult( 
						( new StelemReduction2L( coeff.getDenom() , fac.getFac() ) ).invertLeft() );
			ret = ret.add( an2 );
		}
	}  
	
	
	return( ret );
}

@Override
public void writeString( PrintStream ps ) {
	ps.print( "AstelemMetric" );
}


        /**
         * Applies a discretized approximation of a derivative using the following rules where "N"
		 * is the number of derivatives and "h" is the size of the discretization:
		 * <P>
		 * <P> N = 0 -- Zero derivatives have been taken so simply return the original value of  the elem.
		 * <P>
		 * <P> N = 1 -- Use the first derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <mn>2</mn>
         *       <mi>h</mi>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://en.wikipedia.org/wiki/Numerical_differentiation)
		 * <P>
		 * <P> N = 2 -- Use the second derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <mn>4</mn>
         *       <msup>
         *               <mi>h</mi>
         *             <mn>2</mn>
         *       </msup>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://en.wikipedia.org/wiki/Second_derivative)
		 * <P>
		 * <P> N = 3 -- Use the third derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mo>-</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <mn>2</mn>
         *       <msup>
         *               <mi>h</mi>
         *             <mn>3</mn>
         *       </msup>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://www.geometrictools.com/Documentation/FiniteDifferences.pdf)
		 * <P>
		 * <P> N > 3 -- Combine results from smaller values of N to build an approximation of a higher-order derivative.
         * 
         * @param implicitSpacesIn The input implicit space containing the discretized approximation function.
         * @param node The ordinate over which to take the derivative.
         * @param numDerivatives The number of derivatives to apply.
         * @param hh The size of the discretization.
         * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
         * @throws MultiplicativeDistributionRequiredException 
         * @throws NotInvertibleException 
         */
protected void applyDerivativeAction( HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesIn , 
		Ordinate node , final int numDerivatives , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	if( numDerivatives > 3 )
	{
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesMid = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
		applyDerivativeAction(implicitSpacesIn, node, 3, hh, implicitSpacesMid);
		applyDerivativeAction(implicitSpacesMid, node, numDerivatives-3, hh, implicitSpacesOut);
	}
	
	Iterator<HashMap<Ordinate, BigInteger>> it = implicitSpacesIn.keySet().iterator();
	while( it.hasNext() )
	{
		final HashMap<Ordinate, BigInteger> implicitSpace = it.next();
		final CoeffNode coeffNodeIn = implicitSpacesIn.get( implicitSpace );
		
		switch( numDerivatives )
		{
		
		case 0:
			{
				applyAdd( implicitSpace , coeffNodeIn , implicitSpacesOut );
			}
			break;
		
		case 1:
			{
				applyDerivativeAction1( 
						implicitSpace , coeffNodeIn ,
						node , hh ,
						implicitSpacesOut );
			}
			break;
			
		case 2:
			{
				applyDerivativeAction2( 
						implicitSpace , coeffNodeIn ,
						node , hh ,
						implicitSpacesOut );
			}
			break;
			
		case 3:
			{
				applyDerivativeAction3( 
					implicitSpace , coeffNodeIn ,
					node , hh ,
					implicitSpacesOut );
			}
			break;
			
		}
	}
}





/**
 * Applies a discretized approximation of a first derivative.
 * 
 * @param implicitSpace The input implicit space containing the discretized approximation function.
 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
 * @param node The ordinate over which to take the derivative.
 * @param hh The size of the discretization.
 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void applyDerivativeAction1( 
		final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn ,
		Ordinate node , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
	
	Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
	while( itA.hasNext() )
	{
		Ordinate ae = itA.next();
		final BigInteger valAe = implicitSpace.get( ae );
		if( node.symbolicEquals( ae ) )
		{
			final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
			final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
			implicitSpaceOutM1.put( ae , valAeM1 );
			implicitSpaceOutP1.put( ae , valAeP1 );
		}
		else
		{
			implicitSpaceOutM1.put( ae , valAe );
			implicitSpaceOutP1.put( ae , valAe );
		}
	}
	
	final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( genFromConstDbl( 2.0 ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( genFromConstDbl( 2.0 ) ), hh.getFac() ) ) );
	
	applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
	applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
}




/**
 * Applies a discretized approximation of a second derivative.
 * 
 * @param implicitSpace The input implicit space containing the discretized approximation function.
 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
 * @param node The ordinate over which to take the derivative.
 * @param hh The size of the discretization.
 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void applyDerivativeAction2( 
		final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn ,
		Ordinate node , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM2 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP2 = new HashMap<Ordinate, BigInteger>();
	
	Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
	while( itA.hasNext() )
	{
		Ordinate ae = itA.next();
		final BigInteger valAe = implicitSpace.get( ae );
		if( node.symbolicEquals( ae ) )
		{
			final BigInteger valAeM2 = valAe.subtract( BigInteger.valueOf( 2 ) );
			final BigInteger valAeP2 = valAe.add( BigInteger.valueOf( 2 ) );
			implicitSpaceOutM2.put( ae , valAeM2 );
			implicitSpaceOutP2.put( ae , valAeP2 );
		}
		else
		{
			implicitSpaceOutM2.put( ae , valAe );
			implicitSpaceOutP2.put( ae , valAe );
		}
	}
	
	final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ).mult( genFromConstDbl( 4.0 ) ) , hh.getFac() ) ) );
	final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ).mult( genFromConstDbl( 2.0 ) ) , hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ).mult( genFromConstDbl( 4.0 ) ) , hh.getFac() ) ) );
	
	applyAdd( implicitSpaceOutM2 , coeffNodeOutM2 , implicitSpacesOut );
	applyAdd( implicitSpace , coeffNodeOut , implicitSpacesOut );
	applyAdd( implicitSpaceOutP2 , coeffNodeOutP2 , implicitSpacesOut );
}






/**
 * Applies a discretized approximation of a third derivative.
 * 
 * @param implicitSpace The input implicit space containing the discretized approximation function.
 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
 * @param node The ordinate over which to take the derivative.
 * @param hh The size of the discretization.
 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void applyDerivativeAction3( 
		final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn ,
		Ordinate node , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM2 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP2 = new HashMap<Ordinate, BigInteger>();
	
	Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
	while( itA.hasNext() )
	{
		Ordinate ae = itA.next();
		final BigInteger valAe = implicitSpace.get( ae );
		if( node.symbolicEquals( ae ) )
		{
			final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
			final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
			final BigInteger valAeM2 = valAe.subtract( BigInteger.valueOf( 2 ) );
			final BigInteger valAeP2 = valAe.add( BigInteger.valueOf( 2 ) );
			implicitSpaceOutM1.put( ae , valAeM1 );
			implicitSpaceOutP1.put( ae , valAeP1 );
			implicitSpaceOutM2.put( ae , valAeM2 );
			implicitSpaceOutP2.put( ae , valAeP2 );
		}
		else
		{
			implicitSpaceOutM1.put( ae , valAe );
			implicitSpaceOutP1.put( ae , valAe );
			implicitSpaceOutM2.put( ae , valAe );
			implicitSpaceOutP2.put( ae , valAe );
		}
	}
	
	final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().mult( new SymbolicConst( genFromConstDbl( 2.0 ) , hh.getFac() ) ) , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( new SymbolicConst( genFromConstDbl( 2.0 ) , hh.getFac() ) ) , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	
	applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
	applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
	applyAdd( implicitSpaceOutM2 , coeffNodeOutM2 , implicitSpacesOut );
	applyAdd( implicitSpaceOutP2 , coeffNodeOutP2 , implicitSpacesOut );
}





/**
 * Adds a coefficient times the input implicit space to the output implicit space.
 * 
 * @param implicitSpace The input implicit space.
 * @param node The coefficient.
 * @param implicitSpacesOut The output implicit space.
 * @throws MultiplicativeDistributionRequiredException 
 * @throws NotInvertibleException 
 */
protected void applyAdd( 
		HashMap<Ordinate, BigInteger> implicitSpace , CoeffNode node ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	CoeffNode prev = implicitSpacesOut.get( implicitSpace );
	
	if( prev == null )
	{
		implicitSpacesOut.put( implicitSpace , node );
		return;
	}
	
	if( ( prev.getDenom().eval( null ).getVal() == node.getDenom().eval( null ).getVal() ) &&
		( prev.getDenom().eval( null ).getVal() == node.getDenom().eval( null ).getVal() ) ) 
	{
		SymbolicElem<DoubleElem,DoubleElemFactory> outN = node.getNumer().add( prev.getNumer() );
		CoeffNode nxt = new CoeffNode( outN , prev.getDenom() );
		implicitSpacesOut.put( implicitSpace , nxt );
		return;
	}
	
	
	SymbolicElem<DoubleElem,DoubleElemFactory> outDenom = prev.getDenom().mult( node.getDenom() );
	
	SymbolicElem<DoubleElem,DoubleElemFactory> outNumer = ( node.getDenom().mult( prev.getNumer() ) ).add( prev.getDenom().mult( node.getNumer() ) );
	
	CoeffNode nxt = new CoeffNode( outNumer , outDenom );
	
	implicitSpacesOut.put( implicitSpace , nxt );
}



}








/**
 * An elem defining a partial derivative that is evaluated over the discretized space of the test.
 * 
 * @author thorngreen
 *
 */	
private class AStelemConjugateMomentum extends Stelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
	SymbolicElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
{	

/**
 * The index of the tensor component represented by the elem.
 */
protected ArrayList<BigInteger> index;


/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _index The index of the tensor component represented by the elem.
 */
public AStelemConjugateMomentum( SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac,
		ArrayList<BigInteger> _index ) {
	super(_fac);
	index = _index;
}

@Override
public AStelemConjugateMomentum cloneInstance() {
	AStelemConjugateMomentum cl = new AStelemConjugateMomentum( fac , index );
	Iterator<Ordinate> it = partialMap.keySet().iterator();
	while( it.hasNext() )
	{
		Ordinate key = it.next();
		cl.partialMap.put(key, partialMap.get(key) );
	}
	return( cl );
}

@Override
public SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> eval(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	
	
	HashMap<Ordinate,Ordinate> imp = (HashMap<Ordinate,Ordinate>) implicitSpace;
	
	
	HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesA = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
	
	
	{
		CoeffNode cf = new CoeffNode( fac.getFac().identity() , fac.getFac().identity() );
		HashMap<Ordinate, BigInteger> key = new HashMap<Ordinate, BigInteger>();
		Iterator<Ordinate> it = imp.keySet().iterator();
		while( it.hasNext() )
		{
			Ordinate ae = it.next();
			BigInteger valA = BigInteger.valueOf( imp.get( ae ).getCol() );
			key.put( ae , valA );
		}
		spacesA.put( key , cf );
	}
	
	
	{
		Iterator<Ordinate> it = partialMap.keySet().iterator();
		while( it.hasNext() )
		{
			HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesB = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
			final Ordinate ae = it.next();
			final BigInteger numDerivs = partialMap.get( ae );
			/* final boolean tmpChk = ( ae.getCol() == 0 ) && ( numDerivs.intValue() > 1 );
			if( tmpChk )
			{
				System.out.print( "** " + ( ae.getCol() ) );
				System.out.print(  " >> " + ( numDerivs.intValue() ) + " // " + index );
			} */
			applyDerivativeAction( spacesA , ae , numDerivs.intValue() , HH[ ae.getCol() ] , spacesB );
			spacesA = spacesB;
		}
	}
	
	
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ret = fac.zero();
	
	
	{
		Iterator<HashMap<Ordinate, BigInteger>> it = spacesA.keySet().iterator();
		while( it.hasNext() )
		{
			HashMap<Ordinate, BigInteger> spaceAe = it.next();
			CoeffNode coeff = spacesA.get( spaceAe );
			final CNelemConjugateMomentum an0 = 
					new CNelemConjugateMomentum( fac.getFac() , spaceAe , index );
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
				an1 = an0.mult( 
						new StelemReduction2L( coeff.getNumer() , fac.getFac() ) );
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> 
				an2 = an1.mult( 
						( new StelemReduction2L( coeff.getDenom() , fac.getFac() ) ).invertLeft() );
			ret = ret.add( an2 );
		}
	}  
	
	
	return( ret );
}

@Override
public void writeString( PrintStream ps ) {
	ps.print( "AstelemConjugateMomentum" );
}


        /**
         * Applies a discretized approximation of a derivative using the following rules where "N"
		 * is the number of derivatives and "h" is the size of the discretization:
		 * <P>
		 * <P> N = 0 -- Zero derivatives have been taken so simply return the original value of  the elem.
		 * <P>
		 * <P> N = 1 -- Use the first derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <mn>2</mn>
         *       <mi>h</mi>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://en.wikipedia.org/wiki/Numerical_differentiation)
		 * <P>
		 * <P> N = 2 -- Use the second derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <mn>4</mn>
         *       <msup>
         *               <mi>h</mi>
         *             <mn>2</mn>
         *       </msup>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://en.wikipedia.org/wiki/Second_derivative)
		 * <P>
		 * <P> N = 3 -- Use the third derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mo>-</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <mn>2</mn>
         *       <msup>
         *               <mi>h</mi>
         *             <mn>3</mn>
         *       </msup>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://www.geometrictools.com/Documentation/FiniteDifferences.pdf)
		 * <P>
		 * <P> N > 3 -- Combine results from smaller values of N to build an approximation of a higher-order derivative.
         * 
         * @param implicitSpacesIn The input implicit space containing the discretized approximation function.
         * @param node The ordinate over which to take the derivative.
         * @param numDerivatives The number of derivatives to apply.
         * @param hh The size of the discretization.
         * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
         * @throws MultiplicativeDistributionRequiredException 
         * @throws NotInvertibleException 
         */
protected void applyDerivativeAction( HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesIn , 
		Ordinate node , final int numDerivatives , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	if( numDerivatives > 3 )
	{
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesMid = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
		applyDerivativeAction(implicitSpacesIn, node, 3, hh, implicitSpacesMid);
		applyDerivativeAction(implicitSpacesMid, node, numDerivatives-3, hh, implicitSpacesOut);
	}
	
	Iterator<HashMap<Ordinate, BigInteger>> it = implicitSpacesIn.keySet().iterator();
	while( it.hasNext() )
	{
		final HashMap<Ordinate, BigInteger> implicitSpace = it.next();
		final CoeffNode coeffNodeIn = implicitSpacesIn.get( implicitSpace );
		
		switch( numDerivatives )
		{
		
		case 0:
			{
				applyAdd( implicitSpace , coeffNodeIn , implicitSpacesOut );
			}
			break;
		
		case 1:
			{
				applyDerivativeAction1( 
						implicitSpace , coeffNodeIn ,
						node , hh ,
						implicitSpacesOut );
			}
			break;
			
		case 2:
			{
				applyDerivativeAction2( 
						implicitSpace , coeffNodeIn ,
						node , hh ,
						implicitSpacesOut );
			}
			break;
			
		case 3:
			{
				applyDerivativeAction3( 
					implicitSpace , coeffNodeIn ,
					node , hh ,
					implicitSpacesOut );
			}
			break;
			
		}
	}
}





/**
 * Applies a discretized approximation of a first derivative.
 * 
 * @param implicitSpace The input implicit space containing the discretized approximation function.
 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
 * @param node The ordinate over which to take the derivative.
 * @param hh The size of the discretization.
 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void applyDerivativeAction1( 
		final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn ,
		Ordinate node , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
	
	Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
	while( itA.hasNext() )
	{
		Ordinate ae = itA.next();
		final BigInteger valAe = implicitSpace.get( ae );
		if( node.symbolicEquals( ae ) )
		{
			final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
			final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
			implicitSpaceOutM1.put( ae , valAeM1 );
			implicitSpaceOutP1.put( ae , valAeP1 );
		}
		else
		{
			implicitSpaceOutM1.put( ae , valAe );
			implicitSpaceOutP1.put( ae , valAe );
		}
	}
	
	final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( genFromConstDbl( 2.0 ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( genFromConstDbl( 2.0 ) ), hh.getFac() ) ) );
	
	applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
	applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
}




/**
 * Applies a discretized approximation of a second derivative.
 * 
 * @param implicitSpace The input implicit space containing the discretized approximation function.
 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
 * @param node The ordinate over which to take the derivative.
 * @param hh The size of the discretization.
 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void applyDerivativeAction2( 
		final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn ,
		Ordinate node , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM2 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP2 = new HashMap<Ordinate, BigInteger>();
	
	Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
	while( itA.hasNext() )
	{
		Ordinate ae = itA.next();
		final BigInteger valAe = implicitSpace.get( ae );
		if( node.symbolicEquals( ae ) )
		{
			final BigInteger valAeM2 = valAe.subtract( BigInteger.valueOf( 2 ) );
			final BigInteger valAeP2 = valAe.add( BigInteger.valueOf( 2 ) );
			implicitSpaceOutM2.put( ae , valAeM2 );
			implicitSpaceOutP2.put( ae , valAeP2 );
		}
		else
		{
			implicitSpaceOutM2.put( ae , valAe );
			implicitSpaceOutP2.put( ae , valAe );
		}
	}
	
	final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ).mult( genFromConstDbl( 4.0 ) ) , hh.getFac() ) ) );
	final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ).mult( genFromConstDbl( 2.0 ) ) , hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ).mult( genFromConstDbl( 4.0 ) ) , hh.getFac() ) ) );
	
	applyAdd( implicitSpaceOutM2 , coeffNodeOutM2 , implicitSpacesOut );
	applyAdd( implicitSpace , coeffNodeOut , implicitSpacesOut );
	applyAdd( implicitSpaceOutP2 , coeffNodeOutP2 , implicitSpacesOut );
}






/**
 * Applies a discretized approximation of a third derivative.
 * 
 * @param implicitSpace The input implicit space containing the discretized approximation function.
 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
 * @param node The ordinate over which to take the derivative.
 * @param hh The size of the discretization.
 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void applyDerivativeAction3( 
		final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn ,
		Ordinate node , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM2 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP2 = new HashMap<Ordinate, BigInteger>();
	
	Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
	while( itA.hasNext() )
	{
		Ordinate ae = itA.next();
		final BigInteger valAe = implicitSpace.get( ae );
		if( node.symbolicEquals( ae ) )
		{
			final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
			final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
			final BigInteger valAeM2 = valAe.subtract( BigInteger.valueOf( 2 ) );
			final BigInteger valAeP2 = valAe.add( BigInteger.valueOf( 2 ) );
			implicitSpaceOutM1.put( ae , valAeM1 );
			implicitSpaceOutP1.put( ae , valAeP1 );
			implicitSpaceOutM2.put( ae , valAeM2 );
			implicitSpaceOutP2.put( ae , valAeP2 );
		}
		else
		{
			implicitSpaceOutM1.put( ae , valAe );
			implicitSpaceOutP1.put( ae , valAe );
			implicitSpaceOutM2.put( ae , valAe );
			implicitSpaceOutP2.put( ae , valAe );
		}
	}
	
	final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().mult( new SymbolicConst( genFromConstDbl( 2.0 ) , hh.getFac() ) ) , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( new SymbolicConst( genFromConstDbl( 2.0 ) , hh.getFac() ) ) , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	
	applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
	applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
	applyAdd( implicitSpaceOutM2 , coeffNodeOutM2 , implicitSpacesOut );
	applyAdd( implicitSpaceOutP2 , coeffNodeOutP2 , implicitSpacesOut );
}





/**
 * Adds a coefficient times the input implicit space to the output implicit space.
 * 
 * @param implicitSpace The input implicit space.
 * @param node The coefficient.
 * @param implicitSpacesOut The output implicit space.
 * @throws MultiplicativeDistributionRequiredException 
 * @throws NotInvertibleException 
 */
protected void applyAdd( 
		HashMap<Ordinate, BigInteger> implicitSpace , CoeffNode node ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	CoeffNode prev = implicitSpacesOut.get( implicitSpace );
	
	if( prev == null )
	{
		implicitSpacesOut.put( implicitSpace , node );
		return;
	}
	
	if( ( prev.getDenom().eval( null ).getVal() == node.getDenom().eval( null ).getVal() ) &&
		( prev.getDenom().eval( null ).getVal() == node.getDenom().eval( null ).getVal() ) ) 
	{
		SymbolicElem<DoubleElem,DoubleElemFactory> outN = node.getNumer().add( prev.getNumer() );
		CoeffNode nxt = new CoeffNode( outN , prev.getDenom() );
		implicitSpacesOut.put( implicitSpace , nxt );
		return;
	}
	
	
	SymbolicElem<DoubleElem,DoubleElemFactory> outDenom = prev.getDenom().mult( node.getDenom() );
	
	SymbolicElem<DoubleElem,DoubleElemFactory> outNumer = ( node.getDenom().mult( prev.getNumer() ) ).add( prev.getDenom().mult( node.getNumer() ) );
	
	CoeffNode nxt = new CoeffNode( outNumer , outDenom );
	
	implicitSpacesOut.put( implicitSpace , nxt );
}



}
	
	


	
	
	/**
	 * Newton-Raphson evaluator for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected class StelemNewtonMetric extends NewtonRaphsonSingleElem<DoubleElem,DoubleElemFactory>
	{
		
		/**
		 * The tensor index at which to perform the eval.
		 */
		protected ArrayList<BigInteger> tensorIndex;
		
		
		/**
		 * Constructs the evaluator.
		 * 
		 * @param _function The function over which to evaluate Netwon-Raphson.
		 * @param _withRespectTo The variable over which to evaluate the derivative of the function.
		 * @param implicitSpaceFirstLevel The initial implicit space over which to take the function and its derivative.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		public StelemNewtonMetric(
				final DescentAlgorithmMultiElemRemapTensorParam<String,DoubleElem,DoubleElemFactory> param ,
				ArrayList<BigInteger> _tensorIndex)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			super( param.getFunctions().getVal( _tensorIndex ), param.getWithRespectTosI().get( _tensorIndex ) , 
					param.getImplicitSpaceFirstLevel() );
			tensorIndex = _tensorIndex;
			// System.out.println( "**" );
			// System.out.println( this.partialEval.writeString() );
		}
		
		/**
		 * The iteration count for Newton-Raphson iterations.
		 */
		protected int intCnt = 0;

		@Override
		protected boolean iterationsDone() {
			intCnt++;
			return( intCnt > 20 );
		}
		
		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			intCnt = 0;
			return( super.eval(implicitSpaceInitialGuess) );
		}
		
		@Override
		protected void performIterationUpdate( DoubleElem iterationOffset )
		{
			TestGeneralRelativityAdm.performIterationUpdateMetric( iterationOffset , tensorIndex );
		}
		
		/**
		 * Copies an instance for cloneThread();
		 * 
		 * @param in The instance to copy.
		 * @param threadIndex The index of the thread for which to clone.
		 */
		protected StelemNewtonMetric( final StelemNewtonMetric in , final BigInteger threadIndex )
		{
			super( in , threadIndex );
		}
		
		@Override
		public StelemNewtonMetric cloneThread( final BigInteger threadIndex )
		{
			throw( new RuntimeException( "Not Supported" ) );
		}
		
	}
	
	
	
	
	
	/**
	 * Newton-Raphson evaluator for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected class StelemNewtonConjugateMomentum extends NewtonRaphsonSingleElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * The tensor index at which to perform the eval.
		 */
		protected ArrayList<BigInteger> tensorIndex;
		
		
		/**
		 * Constructs the evaluator.
		 * 
		 * @param _function The function over which to evaluate Netwon-Raphson.
		 * @param _withRespectTo The variable over which to evaluate the derivative of the function.
		 * @param implicitSpaceFirstLevel The initial implicit space over which to take the function and its derivative.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		public StelemNewtonConjugateMomentum(
				final DescentAlgorithmMultiElemRemapTensorParam<String,DoubleElem,DoubleElemFactory> param , 
				ArrayList<BigInteger> _tensorIndex)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			super( param.getFunctions().getVal( _tensorIndex ) , param.getWithRespectTosI().get( _tensorIndex ) , 
					param.getImplicitSpaceFirstLevel() );
			tensorIndex = _tensorIndex;
			// System.out.println( "**" );
			// System.out.println( this.partialEval.writeString() );
		}
		
		/**
		 * The iteration count for Newton-Raphson iterations.
		 */
		protected int intCnt = 0;

		@Override
		protected boolean iterationsDone() {
			intCnt++;
			return( intCnt > 20 );
		}
		
		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			intCnt = 0;
			return( super.eval(implicitSpaceInitialGuess) );
		}
		
		@Override
		protected void performIterationUpdate( DoubleElem iterationOffset )
		{
			TestGeneralRelativityAdm.performIterationUpdateConjugateMomentum( iterationOffset , tensorIndex );
		}
		
		/**
		 * Copies an instance for cloneThread();
		 * 
		 * @param in The instance to copy.
		 * @param threadIndex The index of the thread for which to clone.
		 */
		protected StelemNewtonConjugateMomentum( final StelemNewtonConjugateMomentum in , final BigInteger threadIndex )
		{
			super( in , threadIndex );
		}
		
		@Override
		public StelemNewtonConjugateMomentum cloneThread( final BigInteger threadIndex )
		{
			throw( new RuntimeException( "Not Supported" ) );
		}
		
	}
	
	
	
	

/**
 * An elem for performing evaluations
 * at the base descent algorithm level.
 * 
 * @author thorngreen
 *
 */
protected class VEvalElem extends SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>	
{
	
	/**
	 * The tensor to be evaluated by the elem.
	 */
	protected EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
		dval = null;
	

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _dval The tensor to be evaluated by the elem.
	 */
	public VEvalElem(
			EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory> _fac,
			EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _dval ) {
		super(_fac);
		dval = _dval;
	}

	@Override
	public EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> ret = 
				new EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>( new DoubleElemFactory() , dval.getContravariantIndices() , dval.getCovariantIndices() );
		
		Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
		while( it.hasNext() )
		{
			final ArrayList<BigInteger> key = it.next();
			ret.setVal( key , dval.getVal( key ).eval( implicitSpace ) );
		}
		
		return( ret );
	}

	@Override
	public EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> ret = 
				new EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>( new DoubleElemFactory() , dval.getContravariantIndices() , dval.getCovariantIndices() );
		
		Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
		while( it.hasNext() )
		{
			final ArrayList<BigInteger> key = it.next();
			ret.setVal( key , dval.getVal( key ).evalPartialDerivative( withRespectTo , implicitSpace ) );
		}
		
		return( ret );
	}

	@Override
	public void writeString( PrintStream ps ) {
		Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
		while( it.hasNext() )
		{
			final ArrayList<BigInteger> key = it.next();
			ps.print( "\n" + "** " );
			( dval.getVal( key ) ).writeString( ps );
		}
	}
	
}





/**
 * An elem for evaluating 
 * discretized approximations of the underlying differential
 * equation into expressions (e.g. Jacobian slopes)
 * for descent algorithm evaluations.
 * 
 * @author thorngreen
 *
 */
protected class VEvalElem2 extends SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>	
{

/**
 * The tensor to be evaluated by the elem.
 */
protected EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>  
	dval = null;

/**
 * Factory for components at the base descent algorithm level.
 */
protected SymbolicElemFactory<DoubleElem,DoubleElemFactory> sefac;

/**
 * Factory for evaluated tensors at the base descent algorithm level.
 */
protected EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory> vefac;



/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _sefac Factory for components at the base descent algorithm level.
 * @param _vefac Factory for evaluated tensors at the base descent algorithm level.
 * @param _dval The tensor to be evaluated by the elem.
 */
public VEvalElem2(
		SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>,EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>> _fac,
		SymbolicElemFactory<DoubleElem,DoubleElemFactory> _sefac ,
		EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory> _vefac ,
		EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> _dval ) {
	super(_fac);
	sefac = _sefac;
	vefac = _vefac;
	dval = _dval;
}

@Override
public SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> eval(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> retA = 
			new EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( sefac , dval.getContravariantIndices() , dval.getCovariantIndices() );
	
	Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final ArrayList<BigInteger> key = it.next();
		retA.setVal( key , dval.getVal( key ).eval( implicitSpace ) );
	}
	
	return( new VEvalElem( vefac , retA ) );
}

@Override
public SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> evalPartialDerivative(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> retA = 
			new EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( sefac , dval.getContravariantIndices() , dval.getCovariantIndices() );
	
	Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final ArrayList<BigInteger> key = it.next();
		retA.setVal( key , dval.getVal( key ).evalPartialDerivative( withRespectTo , implicitSpace ) );
	}
	
	return( new VEvalElem( vefac , retA ) );
}

@Override
public void writeString( PrintStream ps ) {
	throw( new RuntimeException( "Not Supported" ) );
}

}





/**
 * An elem for evaluating the underlying differential equation into
 * its discretized approximation.
 * 
 * @author thorngreen
 *
 */
protected class VEvalElem3 extends SymbolicElem<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>>	
{

/**
 * The tensor to be evaluated by the elem.
 */
protected EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>   
dval = null;

/**
 * Factory for components at the level of discretized approximations.
 */
protected SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> sefac;

/**
 * Factory for evaluated tensors at the base descent algorithm level.
 */
protected EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory> vefac;



/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _sefac Factory for components at the level of discretized approximations.
 * @param _vefac Factory for evaluated tensors at the base descent algorithm level.
 * @param _dval The tensor to be evaluated by the elem.
 */
public VEvalElem3(
		SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
		EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
		EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>> _fac,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _sefac ,
	EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory> _vefac ,
	EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>> _dval ) {
super(_fac);
sefac = _sefac;
vefac = _vefac;
dval = _dval;
}

@Override
public SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>> eval(
	HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
	throws NotInvertibleException,
	MultiplicativeDistributionRequiredException {
EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> retA = 
		new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( sefac , dval.getContravariantIndices() , dval.getCovariantIndices() );

Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
while( it.hasNext() )
{
	final ArrayList<BigInteger> key = it.next();
	retA.setVal( key , dval.getVal( key ).eval( implicitSpace ) );
}

return( new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA ) );
}

@Override
public SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
	EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>> evalPartialDerivative(
	ArrayList<? extends Elem<?, ?>> withRespectTo,
	HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
	throws NotInvertibleException,
	MultiplicativeDistributionRequiredException {
	EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> retA = 
			new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( sefac , dval.getContravariantIndices() , dval.getCovariantIndices() );

Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
while( it.hasNext() )
{
	final ArrayList<BigInteger> key = it.next();
	retA.setVal( key , dval.getVal( key ).evalPartialDerivative( withRespectTo , implicitSpace ) );
}

return( new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA ) );
}

@Override
public void writeString( PrintStream ps ) {
	Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final ArrayList<BigInteger> key = it.next();
		ps.print( "\n" + "** " );
		( dval.getVal( key ) ).writeString( ps );
	}
}


}
	



/**
 * Generates temporary indices for use in tensor arithmetic.
 * 
 * @author thorngreen
 *
 */
protected static class TestTemporaryIndexFactory extends TemporaryIndexFactory<String>
{
	/**
	 * The current temporary index.
	 */
	protected static int tempIndex = 0;

	@Override
	public String getTemp() {
		String ret = "temp" + tempIndex;
		tempIndex++;
		return( ret );
	}
	
}





/**
 * An symbolic elem for a tensor.  Used to represent the metric tensor.
 * 
 * @author tgreen
 *
 */
protected class SymbolicMetricTensor extends SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>	
{

/**
 * The tensor to be represented by the symbolic elem.
 */
protected EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
	dval = null;


/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _dval The tensor to be represented by the symbolic elem.
 */
public SymbolicMetricTensor(
		EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> _fac,
		EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
		_dval ) {
	super( _fac );
	dval = _dval;
}

@Override
public EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
	eval(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	return( dval );
}

@Override
public EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
	evalPartialDerivative(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	throw( new RuntimeException( "NotSupported" ) );
}

@Override
public void writeString( PrintStream ps ) {
	Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final ArrayList<BigInteger> key = it.next();
		ps.print( "\n" + "** " ); 
		( dval.getVal( key ) ).writeString( ps );
	}
}

}




/**
 * Factory for generating an instance of the metric tensor.
 * 
 * @author thorngreen
 *
 */
protected class TestConjugateMomentumTensorFactory4 extends MetricTensorInvertingFactory<String, TestDimensionFour,
	SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
	>
{

	@Override
	public SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>> getMetricTensor(
			boolean icovariantIndices, String index0, String index1) 
	{	
		final TestDimensionFour td = new TestDimensionFour();
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		if( icovariantIndices ) covariantIndices.add( index0 ); else contravariantIndices.add( index0 );
		if( icovariantIndices ) covariantIndices.add( index1 ); else contravariantIndices.add( index1 );
		
		final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> de2 = new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
				new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A );
	
		
		
		EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			g0 = new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				( se3A , contravariantIndices , covariantIndices );

		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
			ab.add( BigInteger.valueOf( acnt / TestDimensionFour.FOUR ) );
			ab.add( BigInteger.valueOf( acnt % TestDimensionFour.FOUR ) );
			final AStelemConjugateMomentum as = new AStelemConjugateMomentum( se2A  , ab );
			g0.setVal( ab , as );
		}
		

		if( !icovariantIndices )
		{
			g0 = genMatrixInverseLeft( td , se3A , g0 );
		}
		
		
		final SymbolicMetricTensor seval = new SymbolicMetricTensor( ge , g0 );
		
		
		return( seval );
		
	}
	
	
	@Override
	public TestConjugateMomentumTensorFactory4 cloneThread( final BigInteger threadIndex )
	{
		return( this );
	}

	
}







/**
 * Factory for generating an instance of the metric tensor.
 * 
 * @author thorngreen
 *
 */
protected class TestMetricTensorFactory4 extends MetricTensorInvertingFactory<String, TestDimensionFour,
	SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
	>
{

	@Override
	public SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>> getMetricTensor(
			boolean icovariantIndices, String index0, String index1) 
	{	
		final TestDimensionFour td = new TestDimensionFour();
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		if( icovariantIndices ) covariantIndices.add( index0 ); else contravariantIndices.add( index0 );
		if( icovariantIndices ) covariantIndices.add( index1 ); else contravariantIndices.add( index1 );
		
		final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> de2 = new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
				new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A );
	
		
		
		EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			g0 = new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				( se3A , contravariantIndices , covariantIndices );

		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
			ab.add( BigInteger.valueOf( acnt / TestDimensionFour.FOUR ) );
			ab.add( BigInteger.valueOf( acnt % TestDimensionFour.FOUR ) );
			final AStelemMetric as = new AStelemMetric( se2A  , ab );
			g0.setVal( ab , as );
		}
		

		if( !icovariantIndices )
		{
			g0 = genMatrixInverseLeft( td , se3A , g0 );
		}
		
		
		final SymbolicMetricTensor seval = new SymbolicMetricTensor( ge , g0 );
		
		
		return( seval );
		
	}
	
	
	@Override
	public TestMetricTensorFactory4 cloneThread( final BigInteger threadIndex )
	{
		return( this );
	}

	
}






/**
 * Factory for generating an instance of the metric tensor.
 * 
 * @author thorngreen
 *
 */
protected class TestMetricTensorFactory3 extends MetricTensorInvertingFactory<String, TestDimensionFour,
	SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
	>
{

	@Override
	public SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>> getMetricTensor(
			boolean icovariantIndices, String index0, String index1) 
	{	
		final TestDimensionFour td = new TestDimensionFour();
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		if( icovariantIndices ) covariantIndices.add( index0 ); else contravariantIndices.add( index0 );
		if( icovariantIndices ) covariantIndices.add( index1 ); else contravariantIndices.add( index1 );
		
		final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> de2 = new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
				new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A );
	
		
		
		EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			g0 = new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				( se3A , contravariantIndices , covariantIndices );

		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			if( ( ( acnt % TestDimensionFour.FOUR ) > 0 ) && ( ( acnt / TestDimensionFour.FOUR ) > 0 ) )
			{
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt / TestDimensionFour.FOUR ) );
				ab.add( BigInteger.valueOf( acnt % TestDimensionFour.FOUR ) );
				final AStelemMetric as = new AStelemMetric( se2A  , ab );
				g0.setVal( ab , as );
			}
		}
		

		if( !icovariantIndices )
		{
			g0 = genMatrixInverseLeft( td , se3A , g0 );
		}
		
		
		final SymbolicMetricTensor seval = new SymbolicMetricTensor( ge , g0 );
		
		
		return( seval );
		
	}
	
	
	@Override
	public TestMetricTensorFactory3 cloneThread( final BigInteger threadIndex )
	{
		return( this );
	}

	
}






protected class ShiftVectorFactory3
{
	

	public SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>> getShiftVector(
			boolean icovariantIndices, String index0) 
	{	
		final TestDimensionFour td = new TestDimensionFour();
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		if( icovariantIndices ) covariantIndices.add( index0 ); else contravariantIndices.add( index0 );
		
		final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> de2 = new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
				new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A );
	
		
		
		EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			n = new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				( se3A , contravariantIndices , covariantIndices );

		for( int acnt = 1 ; acnt < 4 ; acnt++ )
		{
			final ArrayList<BigInteger> aba = new ArrayList<BigInteger>();
			final ArrayList<BigInteger> abb = new ArrayList<BigInteger>();
			aba.add( BigInteger.valueOf( 0 ) );
			aba.add( BigInteger.valueOf( acnt ) );
			abb.add( BigInteger.valueOf( acnt ) );
			final AStelemMetric as = new AStelemMetric( se2A  , aba );
			n.setVal( abb , as );
		}
		

		if( !icovariantIndices )
		{
			// g0 = genMatrixInverseLeft( td , se3A , g0 ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		}
		
		
		final SymbolicMetricTensor seval = new SymbolicMetricTensor( ge , n );
		
		
		return( seval );
		
	}
	
}







protected class LapseScalarFactory
{
	

	public SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>> getShiftVector(
			) 
	{	
		final TestDimensionFour td = new TestDimensionFour();
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		
		
		final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> de2 = new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
				new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A );
	
		
		
		EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			n = new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				( se3A , contravariantIndices , covariantIndices );

		
		
		final ArrayList<BigInteger> aba = new ArrayList<BigInteger>();
		final ArrayList<BigInteger> abb = new ArrayList<BigInteger>();
		aba.add( BigInteger.valueOf( 0 ) );
		aba.add( BigInteger.valueOf( 0 ) );
		final AStelemMetric as0 = new AStelemMetric( se2A  , aba );
		
		
		
		try
		{
			final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, 
				SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, 
				SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
				asa = as0.negate().invertLeft();
			
			
			final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, 
				SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, 
				SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
				as = new SymbolicSqrt<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, 
						SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, 
						SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>( asa , se2A );
			
		
			n.setVal( abb , as );
	
		
		
			final SymbolicMetricTensor seval = new SymbolicMetricTensor( ge , n );
		
		
			return( seval );
		}
		catch( NotInvertibleException ex )
		{
			throw( new RuntimeException( "Failed" ) );
		}
		
	}
	
}






/**
 * Initializes the iter array.
 */
protected void initIterArray()
{
	System.out.println( "Setting Initial Conditions..." );
	long atm = System.currentTimeMillis();
	long atm2 = System.currentTimeMillis();
	for( int tcnt = 0 ; tcnt < 2 ; tcnt++ )
	{
		System.out.println( "Initial - " + tcnt );
		for( long acnt = 0 ; acnt < ( (long) NUM_X_ITER ) * NUM_Y_ITER * NUM_Z_ITER ; acnt++ )
		{
			atm2 = System.currentTimeMillis();
			if( atm2 - atm >= 1000 )
			{
				System.out.println( ">> " + acnt );
				atm = atm2;
			}
			
			long ac = acnt;
			final int z = (int)( ac % NUM_Z_ITER );
			ac = ac / NUM_Z_ITER;
			final int y = (int)( ac % NUM_Y_ITER );
			ac = ac / NUM_Y_ITER;
			final int x = (int)( ac % NUM_X_ITER );
			final double dx = ( x - HALF_X ) / RAD_X;
			final double dy = ( y - HALF_Y ) / RAD_Y;
			final double dz = ( z - HALF_Z ) / RAD_Z;
			if( dx * dx + dy * dy + dz * dz < 1.0 )
			{
				iterArrayMetric[ tcnt ][ x ][ y ][ z ] = genDiffEnt();
				iterArrayConjugateMomentum[ tcnt ][ x ][ y ][ z ] = genDiffEnt();
			}
			else
			{
				iterArrayMetric[ tcnt ][ x ][ y ][ z ] = genDiffAll();
				iterArrayConjugateMomentum[ tcnt ][ x ][ y ][ z ] = genDiffAll();
			}
		}
	}
	System.out.println( "Initial Conditions Set..." );
}





/**
 * Initializes the temp array.
 */
protected void initTempArray()
{
	for( int tcnt = 0 ; tcnt < 2 ; tcnt++ )
	{
		for( int xcnt = 0 ; xcnt < 2 ; xcnt++ )
		{
			for( int ycnt = 0 ; ycnt < 2 ; ycnt++ )
			{
				for( int zcnt = 0 ; zcnt < 2 ; zcnt++ )
				{
					tempArrayMetric[ tcnt ][ xcnt ][ ycnt ][ zcnt ] = genDiffAll( );
					tempArrayConjugateMomentum[ tcnt ][ xcnt ][ ycnt ][ zcnt ] = genDiffAll( );
				}
			}
		}
	}
}



/**
 * Increments through the discretized space with cache-locality.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
protected class IncrementManager
{
	/**
	 * The current discretized X-coordinate.
	 */
	protected int xcnt = 0;
	
	/**
	 * The current discretized Y-coordinate.
	 */
	protected int ycnt = 0;
	
	/**
	 * The current discretized Z-coordinate.
	 */
	protected int zcnt = 0;
	
	
	/**
	 * Handles the base increment from the X-Axis.  At the point the increment
	 * reaches a swatch boundary, other axes are potentially incremented.
	 */
	public void handleIncrementXa()
	{
		ycnt = 0;
		xcnt++;
	}
	
	
	/**
	 * Handles the base increment from the Y-Axis.  At the point the increment
	 * reaches a swatch boundary, other axes are potentially incremented.
	 */
	public void handleIncrementYa()
	{
		zcnt = 0;
		if( ycnt < ( NUM_Y_ITER - 1 ) )
		{
			ycnt++;
		}
		else
		{
			handleIncrementXa();
		}
	}
	
	
	/**
	 * Handles the base increment from the Z-Axis.  At the point the increment
	 * reaches a swatch boundary, other axes are potentially incremented.
	 */
	public void handleIncrementZa()
	{
		if( zcnt < ( NUM_Z_ITER - 1 ) )
		{
			zcnt++;
		}
		else
		{
			handleIncrementYa();
		}
	}
	
	
	/**
	 * Restarts the inctrements upon a new T-Axis iteration.
	 */
	public void restartIncrements()
	{
		xcnt = 0;
		ycnt = 0;
		zcnt = 0;
	}
	
	
	
	/**
	 * Gets the current discretized X-coordinate.
	 * 
	 * @return The current discretized X-coordinate.
	 */
	public int getXcnt() {
		return xcnt;
	}



	/**
	 * Gets the current discretized Y-coordinate.
	 * 
	 * @return The current discretized Y-coordinate.
	 */
	public int getYcnt() {
		return ycnt;
	}



	/**
	 * Gets the current discretized Z-coordinate.
	 * 
	 * @return The current discretized Z-coordinate.
	 */
	public int getZcnt() {
		return zcnt;
	}
	

	
}



/**
 * Instance of the IncrementManager used by the performIterationT() method.
 */
protected final IncrementManager im = new IncrementManager();



/**
 * Performs descent iterations for one value of T.
 * 
 * @param tval The value of T over which to iterate.
 * @param descent The descent algorithm to use for the iterations.
 * @param implicitSpace2 The implicit space over which to iterate.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void performIterationT( final int tval , final StelemNewtonMetric[] descentMetric ,
		final StelemNewtonConjugateMomentum[] descentConjugateMomentum ,
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 ) 
		throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	//EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> tmpCorrectionValue = null;
	im.restartIncrements();
	for( long acnt = 0 ; acnt < ( ( (long) NUM_X_ITER ) * NUM_Y_ITER * NUM_Z_ITER ) ; acnt++ )
	{
		
		System.out.println( "iter... " + im.getXcnt() + " " + im.getYcnt() + " " + im.getZcnt() );
		
		
		for( int bcnt = 0 ; bcnt < 9 ; bcnt++ )
		{
			final ArrayList<BigInteger> index = new ArrayList<BigInteger>();
			index.add( BigInteger.valueOf( ( bcnt / 3 ) + 1 ) );
			index.add( BigInteger.valueOf( ( bcnt % 3 ) + 1 ) );
			
			fillTempArray( tval , im.getXcnt() , im.getYcnt() , im.getZcnt() );
			clearSpatialAssertArray();

			
			final DoubleElem 
				ival = TestGeneralRelativityAdm.getUpdateValueMetric( index );
			
			
			
			DoubleElem err = descentMetric[ bcnt ].eval( implicitSpace2 );
					
					
			//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
			//{
			//	tmpCorrectionValue = getCorrectionValue();
			//	applyPredictorCorrector();
			//			
			//	err = descent.eval( implicitSpace2 );
			//}


			final DoubleElem 
				val = TestGeneralRelativityAdm.getUpdateValueMetric( index );
					
					
			
			if( ( im.getXcnt() == HALF_X ) && ( im.getYcnt() == HALF_Y ) && ( im.getZcnt() == HALF_Z ) )
			{
				System.out.println( "******************" );
				System.out.println( " ( " + im.getXcnt() + " , " + im.getYcnt() + " , " + im.getZcnt() + " ) " );
				System.out.println( Math.sqrt( calcMagnitudeSq( ival ) ) );
				System.out.println( Math.sqrt( calcMagnitudeSq( val ) ) );
				System.out.println( "## " + ( Math.sqrt( calcMagnitudeSq( err ) ) ) );
			}
					
					
			/* Assert.assertTrue( spatialAssertArray[ 0 ][ 0 ][ 0 ][ 0 ] == 0 );
					
			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 1 ] > 0 );
					
			Assert.assertTrue( spatialAssertArray[ 2 ][ 1 ][ 1 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 2 ][ 1 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 2 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 2 ] > 0 );
					
			Assert.assertTrue( spatialAssertArray[ 0 ][ 1 ][ 1 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 0 ][ 1 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 0 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 0 ] > 0 ); */
					
			// for( int xc = 0 ; xc < 2 * NSTPX - 1 ; xc++ )
			// {
			//	for( int yc = 0 ; yc < 2 * NSTPY - 1 ; yc++ )
			//	{
			//		for( int zc = 0 ; zc < 2 * NSTPZ - 1 ; zc++ )
			//		{
			//			if( ( xc == NSTPX ) && ( yc == NSTPY ) && ( zc == NSTPZ ) )
			//			{
			//				Assert.assertTrue( spatialAssertArray[ NSTPT * 2 ][ xc ][ yc ][ zc ] > 0 );
			//			}
			//			else
			//			{
			//				Assert.assertTrue( spatialAssertArray[ NSTPT * 2 ][ xc ][ yc ][ zc ] == 0 );
			//			}
			//		}
			//	}
			// }
			
			
			System.out.println( "***  " + im.getXcnt() + "  " + im.getYcnt() + "  " + im.getZcnt() );
			System.out.println( calcMagnitudeSq( val ) );
			System.out.println( calcMagnitudeSq( err ) );
			// Assert.assertTrue( Math.abs( Math.sqrt( calcMagnitudeSq( err ) ) ) < ( 0.01 * Math.abs( Math.sqrt( calcMagnitudeSq( val ) ) ) + 0.01 ) );
			
			//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
			//{
			//	resetCorrectionValue( tmpCorrectionValue );
			//}
		
			( iterArrayMetric[ tval + 1 ][ im.getXcnt() ][ im.getYcnt() ][ im.getZcnt() ] ).setVal( index , val );
				
			
		}
		
		
		
		for( int bcnt = 0 ; bcnt < 16 ; bcnt++ )
		{
			final ArrayList<BigInteger> index = new ArrayList<BigInteger>();
			index.add( BigInteger.valueOf( bcnt / TestDimensionFour.FOUR ) );
			index.add( BigInteger.valueOf( bcnt % TestDimensionFour.FOUR ) );
			
			fillTempArray( tval , im.getXcnt() , im.getYcnt() , im.getZcnt() );
			clearSpatialAssertArray();

			
			final DoubleElem 
				ival = TestGeneralRelativityAdm.getUpdateValueConjugateMomentum( index );
			
			
			
			DoubleElem err = descentConjugateMomentum[ bcnt ].eval( implicitSpace2 );
					
					
			//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
			//{
			//	tmpCorrectionValue = getCorrectionValue();
			//	applyPredictorCorrector();
			//			
			//	err = descent.eval( implicitSpace2 );
			//}


			final DoubleElem 
				val = TestGeneralRelativityAdm.getUpdateValueConjugateMomentum( index );
					
					
			
			if( ( im.getXcnt() == HALF_X ) && ( im.getYcnt() == HALF_Y ) && ( im.getZcnt() == HALF_Z ) )
			{
				System.out.println( "******************" );
				System.out.println( " ( " + im.getXcnt() + " , " + im.getYcnt() + " , " + im.getZcnt() + " ) " );
				System.out.println( Math.sqrt( calcMagnitudeSq( ival ) ) );
				System.out.println( Math.sqrt( calcMagnitudeSq( val ) ) );
				System.out.println( "## " + ( Math.sqrt( calcMagnitudeSq( err ) ) ) );
			}
					
					
			/* Assert.assertTrue( spatialAssertArray[ 0 ][ 0 ][ 0 ][ 0 ] == 0 );
					
			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 1 ] > 0 );
					
			Assert.assertTrue( spatialAssertArray[ 2 ][ 1 ][ 1 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 2 ][ 1 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 2 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 2 ] > 0 );
					
			Assert.assertTrue( spatialAssertArray[ 0 ][ 1 ][ 1 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 0 ][ 1 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 0 ][ 1 ] > 0 );
			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 0 ] > 0 ); */
					
			// for( int xc = 0 ; xc < 2 * NSTPX - 1 ; xc++ )
			// {
			//	for( int yc = 0 ; yc < 2 * NSTPY - 1 ; yc++ )
			//	{
			//		for( int zc = 0 ; zc < 2 * NSTPZ - 1 ; zc++ )
			//		{
			//			if( ( xc == NSTPX ) && ( yc == NSTPY ) && ( zc == NSTPZ ) )
			//			{
			//				Assert.assertTrue( spatialAssertArray[ NSTPT * 2 ][ xc ][ yc ][ zc ] > 0 );
			//			}
			//			else
			//			{
			//				Assert.assertTrue( spatialAssertArray[ NSTPT * 2 ][ xc ][ yc ][ zc ] == 0 );
			//			}
			//		}
			//	}
			// }
			
			
			System.out.println( "***  " + im.getXcnt() + "  " + im.getYcnt() + "  " + im.getZcnt() );
			System.out.println( calcMagnitudeSq( val ) );
			System.out.println( calcMagnitudeSq( err ) );
			// Assert.assertTrue( Math.abs( Math.sqrt( calcMagnitudeSq( err ) ) ) < ( 0.01 * Math.abs( Math.sqrt( calcMagnitudeSq( val ) ) ) + 0.01 ) );
			
			//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
			//{
			//	resetCorrectionValue( tmpCorrectionValue );
			//}
		
			( iterArrayConjugateMomentum[ tval + 1 ][ im.getXcnt() ][ im.getYcnt() ][ im.getZcnt() ] ).setVal( index , val );
				
			
		}
		
		
		
		im.handleIncrementZa();
				
	}
	
}





protected static abstract class CovParamGen
{
	public abstract CovariantDerivativeFactoryParam<String,TestDimensionFour,
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	Ordinate>
		gen();
}



	

/**
 * Tests the ability to numerically evaluate the differential equation <math display="inline">
 * <mrow>
 *   <msub>
 *           <mi>G</mi>
 *       <mrow>
 *         <mi>u</mi>
 *         <mi>v</mi>
 *       </mrow>
 *   </msub>
 *  <mo>=</mo>
 *  <mn>0</mn>
 * </mrow>
 * </math> where <math display="inline">
 * <mrow>
 *   <msub>
 *           <mi>G</mi>
 *       <mrow>
 *         <mi>u</mi>
 *         <mi>v</mi>
 *       </mrow>
 *   </msub>
 * </mrow>
 * </math> is the Einstein tensor.
 * 
 * 
 * 
 * See http://en.wikipedia.org/wiki/Einstein_tensor
 *
 *
 */
public void testStelemSimple() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
	    final double cmx = Math.min( X_HH.getVal() , Math.min( Y_HH.getVal() , Z_HH.getVal() ) ) / ( T_HH.getVal() );
	    final double cmxRatio = cmx / C.getVal();
	    if( cmxRatio < 1.0 )
	    {
		    System.out.println( "WARNING: cmxRatio " + cmxRatio );
	    }
	    else
	    {
		    System.out.println( "cmxRatio " + cmxRatio );
	    }
	
		final Random rand = new Random( 3344 );
		
		final double d1 = Math.sqrt( calcMagnitudeSq( X_HH ) + calcMagnitudeSq( Y_HH ) + calcMagnitudeSq( Z_HH ) );
		
		final TestDimensionFour tdim = new TestDimensionFour();
		
		final SpacetimeAlgebraOrd<TestDimensionFour> ord = new SpacetimeAlgebraOrd<TestDimensionFour>();
		
		
		
		initIterArray();
		
		
		initTempArray();
		
		

		
		final TestTemporaryIndexFactory ttf = new TestTemporaryIndexFactory();
		
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		covariantIndices.add( "u" );
		covariantIndices.add( "v" );
		
		final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> de2 = new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		
		
		
		final ArrayList<Ordinate> wrtT = new ArrayList<Ordinate>();
		
		wrtT.add( new Ordinate( de2 , TV ) );
		
		// final ArrayList<AElem> wrtX = new ArrayList<AElem>();
		
		// wrtX.add( new AElem( de , 1 ) );
		
		
		final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
			new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A );
		
		
		
		
		final DDirec3 ddirec3 = new DDirec3(de2, se2A);
		
		final DDirec4 ddirec4 = new DDirec4(de2, se2A);
		
		
		
		final OrdinaryDerivativeFactory<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			Ordinate> odf3 =
			new OrdinaryDerivativeFactory<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				Ordinate>( ge , tdim , ddirec3 , null );
		
		final OrdinaryDerivativeFactory<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			Ordinate> odf4 =
			new OrdinaryDerivativeFactory<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				Ordinate>( ge , tdim , ddirec4 , null );
		
		
		
		
		final ShiftVectorFactory3 shiftVect3 = new ShiftVectorFactory3();
		
		final LapseScalarFactory lapseScalar = new LapseScalarFactory();
		
		
		final MetricTensorFactory<String, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
			metricZ3 = new TestMetricTensorFactory3();
		
		final MetricTensorFactory<String, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
			metricZ4 = new TestMetricTensorFactory4();
		
		final MetricTensorFactory<String, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
			conjugateMomentumZ4 = new TestConjugateMomentumTensorFactory4();
		
		
	
		
		
		
		final CovParamGen cp3 = new CovParamGen()
		{
			public CovariantDerivativeFactoryParam<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				Ordinate> gen()
				{
				final CovariantDerivativeFactoryParam<String,TestDimensionFour,
					SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					Ordinate> param3 =
					new CovariantDerivativeFactoryParam<String,TestDimensionFour,
						SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
						SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
						Ordinate>( );
					param3.setCoordVecFac( null );  /* !!!!!!!!!!!!!1 Uveca !!!!!!!!!! */
					param3.setDfac( ddirec3 );
					param3.setDim( tdim );
					param3.setFac( ge );
					param3.setMetric( metricZ3 );
					param3.setTemp( ttf );
					return( param3 );
				}
		};
		
		
		
		final CovParamGen cp4 = new CovParamGen()
		{
			public CovariantDerivativeFactoryParam<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				Ordinate> gen()
				{
				final CovariantDerivativeFactoryParam<String,TestDimensionFour,
					SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					Ordinate> param4 =
					new CovariantDerivativeFactoryParam<String,TestDimensionFour,
						SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
						SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
						Ordinate>( );
					param4.setCoordVecFac( null );  /* !!!!!!!!!!!!!1 Uveca !!!!!!!!!! */
					param4.setDfac( ddirec4 );
					param4.setDim( tdim );
					param4.setFac( ge );
					param4.setMetric( metricZ4 );
					param4.setTemp( ttf );
					return( param4 );
				}
		};
		
		
		
		
		final CovariantDerivativeFactory<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			Ordinate> covar3 =
			new CovariantDerivativeFactory<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				Ordinate>( cp3.gen() ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
		final CovariantDerivativeFactory<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			Ordinate> covar4 =
			new CovariantDerivativeFactory<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				Ordinate>( cp4.gen() ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		
		
		final SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>
			Ni =  shiftVect3.getShiftVector( true , "i" );
		
		final SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>
			Nj =  shiftVect3.getShiftVector( true , "j" );
		
		final SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>
			conjMomentum =  conjugateMomentumZ4.getMetricTensor( true , "i" , "j" );
		
		final SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>
			conjMomentumTransf =  conjugateMomentumZ4.getMetricTensor( false , "i" , "j" );
		
		final SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>
			metrZ3 =  metricZ3.getMetricTensor( true , "i" , "j" );
		
		
		
		
		final EinsteinTensorElem<
			String,
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				conjMomentumEt = conjMomentum.eval( null /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!! */ );
		
		final EinsteinTensorElem<
			String,
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				conjMomentumTransfEt = conjMomentumTransf.eval( null /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!! */ );
		
		final EinsteinTensorElem<
			String,
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				metrZ3Et = metrZ3.eval( null /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!! */ );
		
		
		
		final SquareMatrixElem<
			TestDimensionFour,
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			matrix = new SquareMatrixElem<
					TestDimensionFour,
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>(se3A, tdim);
		
		conjMomentumEt.rankTwoTensorToSquareMatrix( matrix );
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
			detConjMomentum = matrix.determinant();
		
		
		final EinsteinTensorElem<
			String,
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				detConjMomentumEt = new EinsteinTensorElem<
					String,
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>(se3A, new ArrayList<String>() , new ArrayList<String>() );
		
		detConjMomentumEt.setVal( new ArrayList<BigInteger>() , detConjMomentum );
		
		
		final EinsteinTensorElem<
			String,
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				tn2mult = detConjMomentumEt.mult( metrZ3Et ).divideBy( 2 );
		
		
		final EinsteinTensorElem<
			String,
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				parenTermA = conjMomentumTransfEt.add( tn2mult.negate() );
				
				// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		
		
		final CovariantDerivativeFactoryParam<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			Ordinate> covNi_j = cp3.gen();
		
		final CovariantDerivativeFactoryParam<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			Ordinate> covNj_i = cp3.gen();
		
		covNi_j.setTensorWithRespectTo( Ni );
		covNj_i.setTensorWithRespectTo( Nj );
		covNi_j.setDerivativeIndex( "j" );
		covNj_i.setDerivativeIndex( "i" );
		
		
		final CovariantDerivativeFactory<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			Ordinate>
			covNi_ja = new CovariantDerivativeFactory<String,TestDimensionFour,
					SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					Ordinate>( covNi_j );
		
		
		final CovariantDerivativeFactory<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			Ordinate>
			covNj_ia = new CovariantDerivativeFactory<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				Ordinate>( covNj_i );
		
				
		
		EinsteinTensorFactory<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				Ordinate> 
				etConjugateMomentum = new EinsteinTensorFactory<String,TestDimensionFour,
					SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					Ordinate>
					( ge , metricZ4 , ttf , odf4 ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		
		
		System.out.println( "Reached #1..." );
		
		
		
		final EinsteinTensorElem<
			String,
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				curvMetric = 
					( covNi_ja.eval( null /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */ ) ).add( 
							covNj_ia.eval( null /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */ ) );
					// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		final EinsteinTensorElem<
			String,
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				curvConjugateMomentum = etConjugateMomentum.getEinsteinTensor( "u" , "v" ).eval( null /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */ );
	
		

		
		
		
		
		System.out.println( "Reached #2..." );
		
		
		final EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				mg1Metric = curvMetric;
		
		final EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				mg1ConjugateMomentum = curvConjugateMomentum;
		
		
		VEvalElem3 m1Metric = new VEvalElem3( se2 , se2A , de2 , mg1Metric );
		
		VEvalElem3 m1ConjugateMomentum = new VEvalElem3( se2 , se2A , de2 , mg1ConjugateMomentum );
		
		
		System.out.println( "Reached #3..." );
		
		
		// System.out.println( m1.writeString() );
		
		
		
		
		final HashMap<Ordinate,Ordinate> implicitSpace0 = new HashMap<Ordinate,Ordinate>();
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 = implicitSpace0;
		
		implicitSpace0.put( new Ordinate( de2 , TV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , XV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , YV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , ZV ) , new Ordinate( de2 , 0 ) );
		
		
		System.out.println( "Reached #4..." );
		
		
		final SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> 
			s0Metric = m1Metric.eval( implicitSpace2 );
		
		final SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> 
			s0ConjugateMomentum = m1ConjugateMomentum.eval( implicitSpace2 );
		
		
		
		System.out.println( "Reached #5..." );
		
		
		final EinsteinTensorElem<String, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
			s00Metric = ( (VEvalElem2)( s0Metric ) ).dval;
		
		final EinsteinTensorElem<String, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
			s00ConjugateMomentum = ( (VEvalElem2)( s0ConjugateMomentum ) ).dval;
		
		
		System.out.println( "Reached #6..." );
		
		
		// String s = s0.writeString();
		
		// System.out.println( s );
		
		
		final HashMap<ArrayList<BigInteger>, ArrayList<? extends Elem<?, ?>>> wrt3Metric = new HashMap<ArrayList<BigInteger>, ArrayList<? extends Elem<?, ?>>>();
		{
			for( int acnt = 0 ; acnt < 9 ; acnt++ )
			{
				final HashMap<Ordinate, BigInteger> coord = new HashMap<Ordinate, BigInteger>();
				coord.put( new Ordinate( de2 , TV ) , BigInteger.valueOf( 1 ) );
				coord.put( new Ordinate( de2 , XV ) , BigInteger.valueOf( 0 ) );
				coord.put( new Ordinate( de2 , YV ) , BigInteger.valueOf( 0 ) );
				coord.put( new Ordinate( de2 , ZV ) , BigInteger.valueOf( 0 ) );
				
				final ArrayList<BigInteger> index = new ArrayList<BigInteger>();
				index.add( BigInteger.valueOf( ( acnt / 3 ) + 1 ) );
				index.add( BigInteger.valueOf( ( acnt % 3 ) + 1 ) );
				
				ArrayList<CNelemMetric> ce = new ArrayList<CNelemMetric>();
				ce.add( new CNelemMetric( seA , coord , index ) );
				
				wrt3Metric.put( index , ce );
			}
		}
		
		
		
		
		final HashMap<ArrayList<BigInteger>, ArrayList<? extends Elem<?, ?>>> wrt3ConjugateMomentum = new HashMap<ArrayList<BigInteger>, ArrayList<? extends Elem<?, ?>>>();
		{
			for( int acnt = 0 ; acnt < 16 ; acnt++ )
			{
				final HashMap<Ordinate, BigInteger> coord = new HashMap<Ordinate, BigInteger>();
				coord.put( new Ordinate( de2 , TV ) , BigInteger.valueOf( 1 ) );
				coord.put( new Ordinate( de2 , XV ) , BigInteger.valueOf( 0 ) );
				coord.put( new Ordinate( de2 , YV ) , BigInteger.valueOf( 0 ) );
				coord.put( new Ordinate( de2 , ZV ) , BigInteger.valueOf( 0 ) );
				
				final ArrayList<BigInteger> index = new ArrayList<BigInteger>();
				index.add( BigInteger.valueOf( acnt / TestDimensionFour.FOUR ) );
				index.add( BigInteger.valueOf( acnt % TestDimensionFour.FOUR ) );
				
				ArrayList<CNelemConjugateMomentum> ce = new ArrayList<CNelemConjugateMomentum>();
				ce.add( new CNelemConjugateMomentum( seA , coord , index ) );
				
				wrt3ConjugateMomentum.put( index , ce );
			}
		}
		
		
		
		System.out.println( "Reached #7..." );
		
		
		final DescentAlgorithmMultiElemRemapTensorParam<String, DoubleElem, DoubleElemFactory>
			paramMetric = new DescentAlgorithmMultiElemRemapTensorParam<String, DoubleElem, DoubleElemFactory>();
		
		final DescentAlgorithmMultiElemRemapTensorParam<String, DoubleElem, DoubleElemFactory>
			paramConjugateMomentum = new DescentAlgorithmMultiElemRemapTensorParam<String, DoubleElem, DoubleElemFactory>();
		
		
		paramMetric.setFunctions( s00Metric );
		paramMetric.setWithRespectTosI( wrt3Metric );
		paramMetric.setImplicitSpaceFirstLevel( implicitSpace2 );
		paramMetric.setSfac( se2A );
		paramMetric.setContravariantIndices( contravariantIndices );
		paramMetric.setCovariantIndices( covariantIndices );
		
		paramConjugateMomentum.setFunctions( s00ConjugateMomentum );
		paramConjugateMomentum.setWithRespectTosI( wrt3ConjugateMomentum );
		paramConjugateMomentum.setImplicitSpaceFirstLevel( implicitSpace2 );
		paramConjugateMomentum.setSfac( se2A );
		paramConjugateMomentum.setContravariantIndices( contravariantIndices );
		paramConjugateMomentum.setCovariantIndices( covariantIndices );
		
		
		StelemNewtonMetric[] descentMetric = new StelemNewtonMetric[ 9 ];
		
		StelemNewtonConjugateMomentum[] descentConjugateMomentum = new StelemNewtonConjugateMomentum[ 16 ];
		
		
		for( int acnt = 0 ; acnt < 9 ; acnt++ )
		{
			final ArrayList<BigInteger> index = new ArrayList<BigInteger>();
			index.add( BigInteger.valueOf( ( acnt / 3 ) + 1 ) );
			index.add( BigInteger.valueOf( ( acnt % 3 ) + 1 ) );
			descentMetric[ acnt ] = new StelemNewtonMetric( paramMetric , index );
		}
		
		
		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			final ArrayList<BigInteger> index = new ArrayList<BigInteger>();
			index.add( BigInteger.valueOf( acnt / TestDimensionFour.FOUR ) );
			index.add( BigInteger.valueOf( acnt % TestDimensionFour.FOUR ) );
			descentConjugateMomentum[ acnt ] = new StelemNewtonConjugateMomentum( paramConjugateMomentum  , index );
		}
		
		
		for( int tval = 1 ; tval < ( NUM_T_ITER - 1 ) ; tval++ )
		{
			performIterationT( tval , descentMetric , descentConjugateMomentum , implicitSpace2 );
		}
		
		// System.out.println( "==============================" ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// System.out.println( iterArray[ NUM_T_ITER - 1 ][ HALF_X ][ HALF_Y ][ HALF_Z ] ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// Assert.assertTrue( Math.abs( val - ( -1.450868 ) ) < 0.01 ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
	}
	
	

	
}


