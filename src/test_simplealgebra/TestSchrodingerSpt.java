




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
import simplealgebra.algo.NewtonRaphsonSingleElem;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.ga.*;
import simplealgebra.ddx.*;






/**
 * 
 * Tests the ability to numerically evaluate a formulation of the differential equation <math display="inline">
 * <mrow>
 *   <mo>-</mo>
 *   <mfrac>
 *     <mrow>
 *       <msup>
 *               <mi>&hbar;</mi>
 *             <mn>2</mn>
 *       </msup>
 *     </mrow>
 *     <mrow>
 *       <mn>2</mn>
 *       <mi>m</mi>
 *     </mrow>
 *   </mfrac>
 *   <msup>
 *           <mo>&nabla;</mo>
 *         <mn>2</mn>
 *   </msup>
 *   <mi>&Psi;</mi>
 *   <mo>+</mo>
 *   <mi>V</mi>
 *   <mi>&Psi;</mi>
 *   <mo>=</mo>
 *   <mi>i</mi>
 *   <mi>&hbar;</mi>
 *   <mfrac>
 *     <mrow>
 *       <mo>&PartialD;</mo>
 *     </mrow>
 *     <mrow>
 *       <mo>&PartialD;</mo>
 *       <mi>t</mi>
 *     </mrow>
 *   </mfrac>
 *   <mi>&Psi;</mi>
 * </mrow>
 * </math>
 * 
 * 
 * in 4-D where <math display="inline">
 * <mrow>
 *  <mi>m</mi>
 * </mrow>
 * </math> and <math display="inline">
 * <mrow>
 *  <mi>&hbar;</mi>
 * </mrow>
 * </math> are arbitrary constants and <math display="inline">
 * <mrow>
 *  <mi>V</mi>
 * </mrow>
 * </math> is zero.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestSchrodingerSpt extends TestCase {
	
	
	/**
	 * Constant representing the number zero.
	 */
	private static final DoubleElem DOUBLE_ZERO = new DoubleElem( 0.0 );
	
	/**
	 * Arbitrary constant.
	 */
	private static final ComplexElem<DoubleElem,DoubleElemFactory> MM = genFromConst( 2.0 );
	
	/**
	 * Constant representing the imaginary number.
	 */
	private static final ComplexElem<DoubleElem,DoubleElemFactory> II = new ComplexElem<DoubleElem,DoubleElemFactory>( 
			new DoubleElem( 0.0 ) , new DoubleElem( 1.0 ) );
	
	
	/**
	 * Returns a complex-value scalar that is equal to the parameter value.
	 * 
	 * @param in The value to be assigned to the scalar.
	 * @return The constructed complex number.
	 */
	private static ComplexElem<DoubleElem,DoubleElemFactory> genFromConst( double in )
	{
		return( new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( in ) , DOUBLE_ZERO ) );
	}
	
	
	/**
	 * Arbitrary constant.
	 */
	protected static final ComplexElem<DoubleElem,DoubleElemFactory> C = genFromConst( 0.004 );
	
	/**
	 * Arbitrary constant.
	 */
	protected static final ComplexElem<DoubleElem,DoubleElemFactory> HBAR = genFromConst( 0.005 );
	
	
	
	/**
	 * Size of the T-Axis discretization.
	 */
	protected static final ComplexElem<DoubleElem,DoubleElemFactory> T_HH = genFromConst( 0.0025 );
	
	/**
	 * Size of the X-Axis discretization.
	 */
	protected static final ComplexElem<DoubleElem,DoubleElemFactory> X_HH = genFromConst( 0.01 );
	
	/**
	 * Size of the Y-Axis discretization.
	 */
	protected static final ComplexElem<DoubleElem,DoubleElemFactory> Y_HH = genFromConst( 0.01 );
	
	/**
	 * Size of the Z-Axis discretization.
	 */
	protected static final ComplexElem<DoubleElem,DoubleElemFactory> Z_HH = genFromConst( 0.01 );
	
	/**
	 * Discretization sizes arrayed by coordinate index.
	 */
	protected static final ComplexElem[] HH = { T_HH , X_HH , Y_HH , Z_HH };
	
	
	
	
	
	/**
	 * The number of discretizations on the T-Axis over which to iterate.
	 */
	protected static final int NUM_T_ITER = 400;
	
	/**
	 * The number of discretizations on the X-Axis over which to iterate.
	 */
	protected static final int NUM_X_ITER = 25;
	
	/**
	 * The number of discretizations on the Y-Axis over which to iterate.
	 */
	protected static final int NUM_Y_ITER = 10;
	
	/**
	 * The number of discretizations on the Z-Axis over which to iterate.
	 */
	protected static final int NUM_Z_ITER = 10;
	
	
	
	
	/**
	 * Temp step size in the T-direction.
	 */
	protected static final int NSTPT = 1;
	
	
	/**
	 * Temp step size in the X-direction.
	 */
	protected static final int NSTPX = 1;
	
	
	/**
	 * Temp step size in the Y-direction.
	 */
	protected static final int NSTPY = 1;
	
	
	/**
	 * Temp step size in the Z-direction.
	 */
	protected static final int NSTPZ = 1;
	
	
	/**
	 * Indicates whether predictor-corrector should be used while iterating.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	protected static final boolean USE_PREDICTOR_CORRECTOR = true;
	
	
	
	
	/**
	 * Result array of real values over which to iterate.
	 */
	protected static double[][][][] iterArrayRe = new double[ NUM_T_ITER ][ NUM_X_ITER ][ NUM_Y_ITER ][ NUM_Z_ITER ];
	
	/**
	 * Result array of imaginary values over which to iterate.
	 */
	protected static double[][][][] iterArrayIm = new double[ NUM_T_ITER ][ NUM_X_ITER ][ NUM_Y_ITER ][ NUM_Z_ITER ];
	
	
	
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
	 * Temporary array of real values in which to generate Newton-Raphson solutions.
	 * <p>0 = T
	 * <p>1 = X
	 * <p>2 = Y
	 * <p>3 = Z
	 */
	private static double[][][][] tempArrayRe = new double[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPY * 2 + 1 ][ NSTPZ * 2 + 1 ];
	
	
	/**
	 * Temporary array of imaginary values in which to generate Newton-Raphson solutions.
	 * <p>0 = T
	 * <p>1 = X
	 * <p>2 = Y
	 * <p>3 = Z
	 */
	private static double[][][][] tempArrayIm = new double[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPY * 2 + 1 ][ NSTPZ * 2 + 1 ];
	
	
	
	
	/**
	 * Given a change calculated by a Newton-Raphson iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void performIterationUpdate( ComplexElem<DoubleElem, DoubleElemFactory> dbl )
	{
		tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] += dbl.getRe().getVal();
		tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] += dbl.getIm().getVal();
	}
	
	/**
	 * Returns the real component of the result of the Newton-Raphson iterations
	 * from the temp array.
	 * 
	 * @return The real value in the temp array.
	 */
	protected static double getUpdateValueRe()
	{
		return( tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
	}
	
	
	/**
	 * Returns the imaginary component of the result of the Newton-Raphson iterations
	 * from the temp array.
	 * 
	 * @return The imaginary value in the temp array.
	 */
	protected static double getUpdateValueIm()
	{
		return( tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
	}
	
	
	/**
	 * Returns real component of the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @return The real value in the temp array.
	 */
	protected static double getCorrectionValueRe()
	{
		return( tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
	}
	
	
	/**
	 * Returns the imaginary component of the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @return The imaginary value in the temp array.
	 */
	protected static double getCorrectionValueIm()
	{
		return( tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
	}
	
	
	/**
	 * Applies a predictor-corrector process to the temp array.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	protected static void applyPredictorCorrector()
	{
		final double slopePrevRe = tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ]
				- tempArrayRe[ NSTPT * 2 - 2 ][ NSTPX ][ NSTPY ][ NSTPZ ];
		final double slopePrevIm = tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ]
				- tempArrayIm[ NSTPT * 2 - 2 ][ NSTPX ][ NSTPY ][ NSTPZ ];
		final double slopeNewRe = tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ]
				- tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ];
		final double slopeNewIm = tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ]
				- tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ];
		final double avgSlopeRe = ( slopePrevRe + slopeNewRe ) / 2.0;
		final double avgSlopeIm = ( slopePrevIm + slopeNewIm ) / 2.0;
		tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] = 
				tempArrayRe[ NSTPT * 2 - 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] + avgSlopeRe;
		tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] = 
				tempArrayIm[ NSTPT * 2 - 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] + avgSlopeIm;
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
		 * @param tcnt The X-Axis index for the center in the iter array.
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
		 * @param tcnt The Y-Axis index for the center in the iter array.
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
		 * @param tcnt The Z-Axis index for the center in the iter array.
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
		 * @param ta The X-Axis iteration of the array fill.
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
		 * @param ta The Y-Axis iteration of the array fill.
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
		 * @param ta The Z-Axis iteration of the array fill.
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
		double avRe = 0.0;
		double avIm = 0.0;
		if( ( tv >= 0 )  && ( xv >= 0 ) && ( yv >= 0 ) && ( zv >= 0 ) &&
			( tv < NUM_T_ITER ) && ( xv < NUM_X_ITER ) && ( yv < NUM_Y_ITER ) && ( zv < NUM_Z_ITER )  )
		{
			avRe = iterArrayRe[ tv ][ xv ][ yv ][ zv ];
			avIm = iterArrayIm[ tv ][ xv ][ yv ][ zv ];
		}
		tempArrayRe[ ta + NSTPT ][ xa + NSTPX ][ ya + NSTPY ][ za + NSTPZ ] = avRe;
		tempArrayIm[ ta + NSTPT ][ xa + NSTPX ][ ya + NSTPY ][ za + NSTPZ ] = avIm;
		
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
		
		for( int ta = -NSTPT ; ta < NSTPT + 1 ; ta++ )
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
	 * Defines a directional derivative for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	private class DDirec extends DirectionalDerivativePartialFactory<
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, 
		SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		Ordinate>
	{
		/**
		 * Factory for building the value of the derivative of an ordinate.
		 */
		ComplexElemFactory<DoubleElem, DoubleElemFactory> de;
		
		/**
		 * Factory for the enclosed type.
		 */
		SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se2;
		
		/**
		 * Constructs the directional derivative factory.
		 * 
		 * @param _de Factory for building the value of the derivative of an ordinate.
		 * @param _se2 Factory for the enclosed type.
		 */
		public DDirec( 
				final ComplexElemFactory<DoubleElem, DoubleElemFactory> _de ,
				final SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _se2 )
		{
			de = _de;
			se2 = _se2;
		}

		@Override
		public SymbolicElem<
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> getPartial( BigInteger basisIndex )
		{
			final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
			
			wrtX.add( new Ordinate( de , basisIndex.intValue() ) );
			
			SymbolicElem<
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
				ret = new PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
					SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,Ordinate>( se2 , wrtX );
				
			if( basisIndex.equals( BigInteger.ZERO ) )
			{
				try
				{
					final DoubleElemFactory de1 = new DoubleElemFactory();
					final ComplexElemFactory<DoubleElem,DoubleElemFactory> de2 = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de1 );
					final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> seA = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( de2 );
					final SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se2A = new SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( seA );
					final ComplexElem<DoubleElem,DoubleElemFactory> cinv = C.invertLeft();
					final SymbolicElem<
					SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
					SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
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
	 * Node representing an ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private class Ordinate extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
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
		public Ordinate(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac, int _col) {
			super(_fac);
			col = _col;
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "a" + col + "()" );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
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
	 * at the base Newton-Raphson evaluation level.
	 * 
	 * @author thorngreen
	 *
	 */
	private class SymbolicConst extends SymbolicReduction<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public SymbolicConst(ComplexElem<DoubleElem,DoubleElemFactory> _elem, ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "const( " + ( getElem().getRe().getVal() ) + " , " + ( getElem().getIm().getVal() ) + " )" );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof SymbolicConst )
			{
				final boolean eqr = getElem().getRe().getVal() == ( (SymbolicConst) b ).getElem().getRe().getVal();
				final boolean eqi = getElem().getIm().getVal() == ( (SymbolicConst) b ).getElem().getIm().getVal();
				return( eqr && eqi );
			}
			return( false );
		}
		
	}
	
	
	
	/**
	 * An elem representing a symbolic constant at the level of mapping 
	 * discretized approximations of the underlying differential
	 * equation into expressions (e.g. Jacobian slopes)
	 * for Newton-Raphson evaluations.
	 * 
	 * @author thorngreen
	 *
	 */
	private class StelemReduction2L extends SymbolicReduction<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public StelemReduction2L(SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _elem, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "reduce2L( " );
			getElem().writeString( ps );
			ps.print( " )" );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> b )
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
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public StelemReduction3L(
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _elem, 
				SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _fac) {
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
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
				SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> b )
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
		private SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> numer;
		
		/**
		 * The denominator.
		 */
		private SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> denom;
		
		/**
		 * Constructs the coefficient.
		 * 
		 * @param _numer The numerator.
		 * @param _denom The denominator.
		 */
		public CoeffNode( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _numer , SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _denom )
		{
			numer = _numer;
			denom = _denom;
		}
		
		/**
		 * Gets the numerator.
		 * 
		 * @return The numerator.
		 */
		public SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> getNumer() {
			return numer;
		}
		
		/**
		 * Gets the denominator.
		 * 
		 * @return The denominator.
		 */
		public SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> getDenom() {
			return denom;
		}
		
	}
	
	
	
	
	/**
	 * Elem representing the discretized equivalent 
	 * of the complex number constrained by the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private class BNelem extends Nelem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
		 */
		public BNelem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac, HashMap<Ordinate, BigInteger> _coord) {
			super(_fac, _coord);
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
		public ComplexElem<DoubleElem,DoubleElemFactory> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
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
			final DoubleElem re = new DoubleElem( TestSchrodingerSpt.tempArrayRe[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ] );
			final DoubleElem im = new DoubleElem( TestSchrodingerSpt.tempArrayIm[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ] );
			return( new ComplexElem<DoubleElem,DoubleElemFactory>( re , im ) );
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
			s0 = s0 + "()";
			ps.print( s0 );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof BNelem )
			{
				BNelem bn = (BNelem) b;
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
	 * of the complex number constrained by the differential equation.
	 * The partial derivatives of this elem generate
	 * the slopes for producing Newton-Raphson iterations (e.g. the Jacobian slopes),
	 * as opposed to partial derivatives for the underlying differential equation.
	 * 
	 * @author thorngreen
	 *
	 */	
	private class CNelem extends Nelem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
		 */
		public CNelem(SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _fac, HashMap<Ordinate, BigInteger> _coord) {
			super(_fac, _coord);
		}

		@Override
		public SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( new BNelem( fac.getFac() , coord ) );
		}
		
		
		@Override
		public SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
			if( withRespectTo.size() > 1 )
			{
				return( fac.zero() );
			}
			Iterator<? extends Elem<?,?>> it = withRespectTo.iterator();
			CNelem wrt = (CNelem)( it.next() );
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
		
		
		@Override
		public boolean symbolicEquals( 
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> b )
		{
			if( b instanceof CNelem )
			{
				CNelem bn = (CNelem) b;
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
	private class AStelem extends Stelem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate>
	{
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public AStelem(SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, 
				SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _fac) {
			super(_fac);
		}

		@Override
		public AStelem cloneInstance() {
			AStelem cl = new AStelem( fac );
			Iterator<Ordinate> it = partialMap.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate key = it.next();
				cl.partialMap.put(key, partialMap.get(key) );
			}
			return( cl );
		}

		@Override
		public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, 
			SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> eval(
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
					applyDerivativeAction( spacesA , ae , numDerivs.intValue() , (ComplexElem<DoubleElem,DoubleElemFactory>)(HH[ ae.getCol() ]) , spacesB );
					spacesA = spacesB;
				}
			}
			
			
			
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, 
				SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> ret = fac.zero();
			
			
			{
				Iterator<HashMap<Ordinate, BigInteger>> it = spacesA.keySet().iterator();
				while( it.hasNext() )
				{
					HashMap<Ordinate, BigInteger> spaceAe = it.next();
					CoeffNode coeff = spacesA.get( spaceAe );
					final CNelem an0 = 
							new CNelem( fac.getFac() , spaceAe );
					SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
						an1 = an0.mult( 
								new StelemReduction2L( coeff.getNumer() , fac.getFac() ) );
					SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> 
						an2 = an1.mult( 
								( new StelemReduction2L( coeff.getDenom() , fac.getFac() ) ).invertLeft() );
					ret = ret.add( an2 );
				}
			}
			
			
			return( ret );
		}

		@Override
		public void writeString( PrintStream ps ) {
			throw( new RuntimeException( "NotSupported" ) );
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
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
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
				Ordinate node , final int numDerivatives , ComplexElem<DoubleElem,DoubleElemFactory> hh ,
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
								node , hh , implicitSpacesOut );
					}
					break;
					
				case 2:
					{
						applyDerivativeAction2( 
								implicitSpace , coeffNodeIn ,
								node , hh , implicitSpacesOut );
					}
					break;
					
				case 3:
					{
						applyDerivativeAction3( 
							implicitSpace , coeffNodeIn ,
							node , hh , implicitSpacesOut );
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
				Ordinate node , ComplexElem<DoubleElem,DoubleElemFactory> hh ,
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
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( genFromConst( 2.0 ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( genFromConst( 2.0 ) ), hh.getFac() ) ) );
			
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
				Ordinate node , ComplexElem<DoubleElem,DoubleElemFactory> hh ,
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
			
			final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ) , hh.getFac() ) ) );
			final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate().mult( new SymbolicConst( genFromConst( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ) , hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ) , hh.getFac() ) ) );
			
			applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
			applyAdd( implicitSpace , coeffNodeOut , implicitSpacesOut );
			applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
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
				Ordinate node , ComplexElem<DoubleElem,DoubleElemFactory> hh ,
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
			
			final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().mult( new SymbolicConst( genFromConst( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConst( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( new SymbolicConst( genFromConst( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConst( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConst( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConst( 2.0 ) ) ) ), hh.getFac() ) ) );
			
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
			
			if( ( prev.getDenom().eval( null ).getRe().getVal() == node.getDenom().eval( null ).getRe().getVal() ) &&
				( prev.getDenom().eval( null ).getIm().getVal() == node.getDenom().eval( null ).getIm().getVal() ) )
			{
				SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> outN = node.getNumer().add( prev.getNumer() );
				CoeffNode nxt = new CoeffNode( outN , prev.getDenom() );
				implicitSpacesOut.put( implicitSpace , nxt );
				return;
			}
			
			
			SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> outDenom = prev.getDenom().mult( node.getDenom() );
			
			SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> outNumer = ( node.getDenom().mult( prev.getNumer() ) ).add( prev.getDenom().mult( node.getNumer() ) );
			
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
	protected class StelemNewton extends NewtonRaphsonSingleElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the evaluator.
		 * 
		 * @param _function The function over which to evaluate Netwon-Raphson.
		 * @param _withRespectTo The variable over which to evaluate the derivative of the function.
		 * @param implicitSpaceFirstLevel The initial implicit space over which to take the function and its derivative.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		public StelemNewton(
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> _function,
				ArrayList<? extends Elem<?, ?>> _withRespectTo, 
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			super(_function, _withRespectTo, implicitSpaceFirstLevel);
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
		public ComplexElem<DoubleElem, DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			intCnt = 0;
			return( super.eval(implicitSpaceInitialGuess) );
		}
		
		@Override
		protected void performIterationUpdate( ComplexElem<DoubleElem, DoubleElemFactory> iterationOffset )
		{
			TestSchrodingerSpt.performIterationUpdate( iterationOffset );
		}
		
	}
	
	
	
	
	/**
	 * Calculates the expectation value given the wave value.
	 * 
	 * See http://en.wikipedia.org/wiki/expectation_value_(quantum_mechanics)
	 * 
	 * @param in The input wave value.
	 * @return The calculated expectation value.
	 */
	protected double expectationValue( final ComplexElem<DoubleElem,DoubleElemFactory> in ) throws Throwable
	{
		final ArrayList<ComplexElem<DoubleElem,DoubleElemFactory>> args = 
				new ArrayList<ComplexElem<DoubleElem,DoubleElemFactory>>();
		
		final ComplexElem<DoubleElem,DoubleElemFactory> conj = 
				in.handleOptionalOp( ComplexElem.ComplexCmd.CONJUGATE_LEFT , args );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> mult = conj.mult( in );
		
		return( mult.getRe().getVal() );
	}
	
	
	
	
	/**
	 * Performs descent iterations for one value of T.
	 * 
	 * @param tval The value of T over which to iterate.
	 * @param newton The descent algorithm to use for the iterations.
	 * @param implicitSpace2 The implicit space over which to iterate.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected void performIterationT( final int tval , final StelemNewton newton , final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 ) 
			throws Throwable
	{
		for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
		{
			for( int ycnt = 0 ; ycnt < NUM_Y_ITER ; ycnt++ )
			{
				for( int zcnt = 0 ; zcnt < NUM_Z_ITER ; zcnt++ )
				{
					iterArrayRe[ tval + 1 ][ xcnt ][ ycnt ][ zcnt ] = iterArrayRe[ tval ][ xcnt ][ ycnt ][ zcnt ];
					iterArrayIm[ tval + 1 ][ xcnt ][ ycnt ][ zcnt ] = iterArrayIm[ tval ][ xcnt ][ ycnt ][ zcnt ];
				}
			}
		}
		
		for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
		{
			for( int ycnt = 0 ; ycnt < NUM_Y_ITER ; ycnt++ )
			{
				for( int zcnt = 0 ; zcnt < NUM_Z_ITER ; zcnt++ )
				{
					fillTempArray( tval , xcnt , ycnt , zcnt );
					clearSpatialAssertArray();
	
			
					final ComplexElem<DoubleElem,DoubleElemFactory> ival =
							new ComplexElem<DoubleElem,DoubleElemFactory>(
									new DoubleElem( TestSchrodingerSpt.getUpdateValueRe() ) , 
									new DoubleElem( TestSchrodingerSpt.getUpdateValueIm() ) );
		
			
					ComplexElem<DoubleElem, DoubleElemFactory> err = newton.eval( implicitSpace2 );
					
					
					if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
					{
						applyPredictorCorrector();
						
						err = newton.eval( implicitSpace2 );
					}
	
	
					final ComplexElem<DoubleElem,DoubleElemFactory> val =
							new ComplexElem<DoubleElem,DoubleElemFactory>(
									new DoubleElem( TestSchrodingerSpt.getUpdateValueRe() ) , 
									new DoubleElem( TestSchrodingerSpt.getUpdateValueIm() ) );
			
					
					if( ( xcnt == 12 ) && ( ycnt == 5 ) && ( zcnt == 5 ) )
					{
						System.out.println( "******************" );
						System.out.println( " ( " + xcnt + " , " + ycnt + " , " + zcnt + " ) " );
						System.out.println( expectationValue( ival ) );
						System.out.println( expectationValue( val ) );
						System.out.println( "## " + ( expectationValue( err ) ) );
					}
					
					
					Assert.assertTrue( spatialAssertArray[ 0 ][ 0 ][ 0 ][ 0 ] == 0 );
					
					Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 1 ] > 0 );
					
					Assert.assertTrue( spatialAssertArray[ 2 ][ 1 ][ 1 ][ 1 ] > 0 );
					Assert.assertTrue( spatialAssertArray[ 1 ][ 2 ][ 1 ][ 1 ] > 0 );
					Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 2 ][ 1 ] > 0 );
					Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 2 ] > 0 );
					
					Assert.assertTrue( spatialAssertArray[ 0 ][ 1 ][ 1 ][ 1 ] > 0 );
					Assert.assertTrue( spatialAssertArray[ 1 ][ 0 ][ 1 ][ 1 ] > 0 );
					Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 0 ][ 1 ] > 0 );
					Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 0 ] > 0 );
			
			
					Assert.assertTrue( Math.abs( Math.sqrt( expectationValue( err ) ) ) < ( 0.01 * Math.abs( Math.sqrt( expectationValue( val ) ) ) + 0.01 ) );
			
					if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
					{
						iterArrayRe[ tval ][ xcnt ][ ycnt ][ zcnt ] =
							getCorrectionValueRe();	
						iterArrayIm[ tval ][ xcnt ][ ycnt ][ zcnt ] =
							getCorrectionValueIm();
					}
		
					iterArrayRe[ tval + 1 ][ xcnt ][ ycnt ][ zcnt ] = val.getRe().getVal();
					iterArrayIm[ tval + 1 ][ xcnt ][ ycnt ][ zcnt ] = val.getIm().getVal();
				}
				
			}
					
		}
	}
	
	
	
	
	/**
	 * Initializes the iter array.
	 * 
	 * @param d1 The dimensional size to be used for the initialization.
	 */
	protected void initIterArray( final double d1 )
	{
		for( int tcnt = 0 ; tcnt < 2 ; tcnt++ )
		{
			// for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
			// {
			//	iterArray[ tcnt ][ xcnt ] = rand.nextDouble();
			// }
			iterArrayRe[ tcnt ][ 12 ][ 5 ][ 5 ] = 10000.0 * ( d1 * d1 );
		}
	}
	
	
	
	
	/**
	 * 
	 * Tests the ability to numerically evaluate the differential equation <math display="inline">
	 * <mrow>
	 *   <mo>-</mo>
	 *   <mfrac>
	 *     <mrow>
	 *       <msup>
	 *               <mi>&hbar;</mi>
	 *             <mn>2</mn>
	 *       </msup>
	 *     </mrow>
	 *     <mrow>
	 *       <mn>2</mn>
	 *       <mi>m</mi>
	 *     </mrow>
	 *   </mfrac>
	 *   <msup>
	 *           <mo>&nabla;</mo>
	 *         <mn>2</mn>
	 *   </msup>
	 *   <mi>&Psi;</mi>
	 *   <mo>+</mo>
	 *   <mi>V</mi>
	 *   <mi>&Psi;</mi>
	 *   <mo>=</mo>
	 *   <mi>i</mi>
	 *   <mi>&hbar;</mi>
	 *   <mfrac>
	 *     <mrow>
	 *       <mo>&PartialD;</mo>
	 *     </mrow>
	 *     <mrow>
	 *       <mo>&PartialD;</mo>
	 *       <mi>t</mi>
	 *     </mrow>
	 *   </mfrac>
	 *   <mi>&Psi;</mi>
	 * </mrow>
	 * </math>
	 * 
	 * 
	 * where <math display="inline">
	 * <mrow>
	 *  <mi>m</mi>
	 * </mrow>
	 * </math> and <math display="inline">
	 * <mrow>
	 *  <mi>&hbar;</mi>
	 * </mrow>
	 * </math> are arbitrary constants and <math display="inline">
	 * <mrow>
	 *  <mi>V</mi>
	 * </mrow>
	 * </math> is zero.
	 * 
	 * 
	 * @author thorngreen
	 *
	 */	
	public void testStelemSimple() throws Throwable
	{
		final Random rand = new Random( 3344 );
		
		final double d1 = Math.sqrt( X_HH.getRe().getVal() * X_HH.getRe().getVal() + Y_HH.getRe().getVal() * Y_HH.getRe().getVal() + Z_HH.getRe().getVal() * Z_HH.getRe().getVal() );
		
		final TestDimensionFour tdim = new TestDimensionFour();
		
		final SpacetimeAlgebraOrd<TestDimensionFour> ord = new SpacetimeAlgebraOrd<TestDimensionFour>();
		
		
		
		initIterArray( d1 );
		
		
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> de2 = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( se );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>( se2 );
		
		
		final AStelem as = new AStelem( se2 );
		
		final ArrayList<Ordinate> wrtT = new ArrayList<Ordinate>();
		
		wrtT.add( new Ordinate( de2 , TV ) );
		
		// final ArrayList<AElem> wrtX = new ArrayList<AElem>();
		
		// wrtX.add( new AElem( de , 1 ) );
		
		
		final GeometricAlgebraMultivectorElemFactory<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
			ge =
			new GeometricAlgebraMultivectorElemFactory<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>( se3 , tdim , ord );
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
				g0 = new GeometricAlgebraMultivectorElem<
					TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
					SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
					( se3 , tdim , ord );
		
		g0.setVal( new HashSet<BigInteger>() , as );
		
		
		final DDirec ddirec = new DDirec(de2, se2);
		
		final DirectionalDerivative<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, 
			Ordinate>
			del =
			new DirectionalDerivative<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, 
			Ordinate>( 
					ge , 
					tdim , ord ,
					ddirec );
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
				del0 = del.eval( null );
		
		
		
		final PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,Ordinate> pa0T 
			= new PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
					SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,Ordinate>( se2 , wrtT );
		
		// final PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		//	SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,AElem> pa0X 
		//	= new PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		//			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,AElem>( se2 , wrtX );
	
		
		final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> m0T
			= pa0T.mult( as ); 
		
		// SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		//	SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> m0X
		//	= pa0X.mult( as ); 
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
				gxx0 = del0.mult( g0 );
		
		final ArrayList<GeometricAlgebraMultivectorElem<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>> args0 
				= new ArrayList<GeometricAlgebraMultivectorElem<
					TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
					SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>();
		args0.add( gxx0 );
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
				gxx1 = del0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , args0 );
		
		
		//SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		//	SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> m1X
		//	= pa0X.mult( m0X );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> gtt0
			= m0T.mult( 
					( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( II.mult( HBAR ) , de2 ) , se ) , se2 )
							) ).negate();
		
		
		// SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		//	SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> m1
		//	= m1X.add( gtt0 );
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
				gtt = new GeometricAlgebraMultivectorElem<
					TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
					SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
					( se3 , tdim , ord );
		
		gtt.setVal( new HashSet<BigInteger>() , gtt0 );
		
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> gxxMult
			=  
				( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( HBAR.mult( HBAR 
							).mult( ( MM.mult( genFromConst( 2.0 ) ) ).invertLeft() ).negate() , de2 ) , se ) , se2 )
						);
		
		
		final GeometricAlgebraMultivectorElem<
		TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
		SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
		SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
			gxxMultV = new GeometricAlgebraMultivectorElem<
					TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
					SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>( se3 , tdim , ord );
		
		
		gxxMultV.setVal( new HashSet<BigInteger>() , gxxMult );
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
				mg1 = ( gxx1.mult( gxxMultV ) ).add( gtt );
		
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> m1
			= mg1.get( new HashSet<BigInteger>() );
		
		
		
		
		
		
		
		final HashMap<Ordinate,Ordinate> implicitSpace0 = new HashMap<Ordinate,Ordinate>();
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 = implicitSpace0;
		
		implicitSpace0.put( new Ordinate( de2 , TV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , XV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , YV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , ZV ) , new Ordinate( de2 , 0 ) );
		
		final SymbolicElem<
			SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> s0 = m1.eval( implicitSpace2 );
		
		// String s = s0.writeString();
		
		// System.out.println( s );
		
		
		final ArrayList<Elem<?, ?>> wrt3 = new ArrayList<Elem<?, ?>>();
		{
			final HashMap<Ordinate, BigInteger> coord = new HashMap<Ordinate, BigInteger>();
			coord.put( new Ordinate( de2 , TV ) , BigInteger.valueOf( 1 ) );
			coord.put( new Ordinate( de2 , XV ) , BigInteger.valueOf( 0 ) );
			coord.put( new Ordinate( de2 , YV ) , BigInteger.valueOf( 0 ) );
			coord.put( new Ordinate( de2 , ZV ) , BigInteger.valueOf( 0 ) );
			wrt3.add( new CNelem( se , coord ) );
		}
		
		
		
		StelemNewton newton = new StelemNewton( s0 , wrt3 , implicitSpace2 );
		
		
		for( int tval = 1 ; tval < ( NUM_T_ITER - 1 ) ; tval++ )
		{
			performIterationT( tval , newton , implicitSpace2 );
		}
		
		
		// System.out.println( "==============================" ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// System.out.println( iterArray[ NUM_T_ITER - 1 ][ 10 ][ 5 ][ 5 ] ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// Assert.assertTrue( Math.abs( val - ( -1.450868 ) ) < 0.01 ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
	}
	

	
}


