




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
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.AbsoluteValue;
import simplealgebra.AbstractCache;
import simplealgebra.CloneThreadCache;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.Sqrt;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.algo.NewtonRaphsonSingleElem;
import simplealgebra.bigfixedpoint.Precision;
import simplealgebra.constants.CpuInfo;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.store.DbFastArray3D_Param;
import simplealgebra.store.DrFastArray3D_Dbl;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.ga.*;
import simplealgebra.ddx.*;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;



 



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
 * </math> is zero.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * In this test the X-axis has both a real and an imaginary component.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestSchrodingerCdimBN extends TestCase {
	
	
	
	
	
	/**
	 * Constant containing the number ten.
	 */
	static final BigInteger TEN = BigInteger.valueOf( 10 );
	
	
	/**
	 * Returns the number <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *       <mrow>
     *         <mi>X</mi>
     *         <mo>+</mo>
     *         <mn>1</mn>
     *       </mrow>
     *   </msup>
     * </mrow>
     * </math>, where X is the input parameter.
	 * 
	 * @param cnt The input parameter.
	 * @return The value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *       <mrow>
     *         <mi>X</mi>
     *         <mo>+</mo>
     *         <mn>1</mn>
     *       </mrow>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	protected static BigInteger calcVal( final int cnt )
	{
		BigInteger ret = TEN;
		for( int i = 0 ; i < cnt ; i++ )
		{
			ret = ret.multiply( TEN );
		}
		return( ret );
	}
	

	
	/**
	 * Constant containing the value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>801</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * Largest possible double is around <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>308</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	static final BigInteger baseVal = calcVal( 800 );
	
	
	/**
	 * Constant containing the square of baseVal, or <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>1602</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	protected static final BigInteger finalBaseValSq = baseVal.multiply( baseVal );
	
	
	/**
	 * Defines a precision of baseVal, or one part in <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>801</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static final class LrgPrecision extends Precision<LrgPrecision>
	{
		@Override
		public BigInteger getVal()
		{
			return( baseVal );
		}
		
		@Override
		public BigInteger getValSquared()
		{
			return( finalBaseValSq );
		}
		
	}
	
	
	/**
	 * A constant defining the large precision.
	 */
	static final LrgPrecision lrgPrecision = new LrgPrecision();
	
	
	static final BigFixedPointElemFactory<LrgPrecision> de = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );

	
	
	
	/**
	 * The number of CPU Cores.
	 */
	static final int NUM_CPU_CORES = CpuInfo.NUM_CPU_CORES;
	
	/**
	 * Constant representing the number zero.
	 */
	private static final BigFixedPointElem<LrgPrecision> DOUBLE_ZERO = de.zero();
	
	/**
	 * Arbitrary constant.
	 */
	private static final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> MM = genFromConst( 2.0 );
	
	/**
	 * Constant representing the imaginary number.
	 */
	private static final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> II = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( 
			de.zero() , de.identity() );
	
	
	/**
	 * Returns a complex-value scalar that is equal to the parameter value.
	 * 
	 * @param in The value to be assigned to the scalar.
	 * @return The constructed complex number.
	 */
	private static ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> genFromConst( double in )
	{
		return( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( in , lrgPrecision ) , DOUBLE_ZERO ) );
	}
	
	
	/**
	 * Arbitrary constant.
	 */
	protected static final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> HBAR = genFromConst( 0.005 );
	
	
	
	
	/**
	 * The total measurement size along the X-Axis.
	 */
	protected static final double TOTAL_X_AXIS_SIZE = 0.1;
	
	/**
	 * The total measurement size along the XI-Axis.
	 */
	protected static final double TOTAL_XI_AXIS_SIZE = 0.1;
	
	
	/**
	 * The number of discretizations on the T-Axis over which to iterate.
	 */
	protected static final int NUM_T_ITER = 800; // IterConstants.LRG_ITER_T; // 400;
	
	/**
	 * The number of discretizations on the X-Axis over which to iterate (real component of the X-axis)..
	 */
	protected static final int NUM_X_ITER = 400; // 50; // IterConstants.LRG_ITER_X; // 25
	
	/**
	 * The number of discretizations on the XI-Axis over which to iterate (imaginary component of the X-axis)..
	 */
	protected static final int NUM_XI_ITER = 400; // 50; // IterConstants.LRG_ITER_X; // 25
	
	
	
	/**
	 * Size of the T-Axis discretization.
	 */
	protected static final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> T_HH = genFromConst( 0.0025 );
	
	/**
	 * Size of the X-Axis discretization (real component of the X-axis).
	 */
	protected static final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> X_HH = genFromConst( TOTAL_X_AXIS_SIZE / NUM_X_ITER /* 0.01 */ );
	
	/**
	 * Size of the XI-Axis discretization (imaginary component of the X-axis).
	 */
	protected static final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> XI_HH = genFromConst( TOTAL_XI_AXIS_SIZE / NUM_XI_ITER /* 0.01 */ );
	
	/**
	 * Discretization sizes arrayed by coordinate index.
	 */
	protected static final ComplexElem[] HH = { T_HH , X_HH , XI_HH };
	

	
	/**
	 * The halfway iteration point in X (real component of the X-axis).
	 */
	protected static final int HALF_X = NUM_X_ITER / 2;
	
	/**
	 * The halfway iteration point in XI (imaginary component of the X-axis).
	 */
	protected static final int HALF_XI = NUM_XI_ITER / 2;
	
	
	
	/**
	 * The initial condition radius in X (real component of the X-axis).
	 */
	protected static final double RAD_X = NUM_X_ITER / 10.0;
	
	/**
	 * The initial condition radius in XI (imaginary component of the X-axis).
	 */
	protected static final double RAD_XI = NUM_XI_ITER / 10.0;
	
	
	

	
	
	/**
	 * The T-Axis cell size.
	 */
	protected static final int TMULT = 8;
	
	/**
	 * The X-Axis cell size.
	 */
	protected static final int XMULT = 8;
	
	/**
	 * The XI-Axis cell size.
	 */
	protected static final int XIMULT = 8;
	
	
	
	
	
	
	
	/**
	 * Temp step size in the T-direction.
	 */
	protected static final int NSTPT = 1;
	
	
	/**
	 * Temp step size in the X-direction (real component of the X-axis).
	 */
	protected static final int NSTPX = 1;
	
	
	/**
	 * Temp step size in the XI-direction (imaginary component of the X-axis).
	 */
	protected static final int NSTPXI = 1;
	
	
	/**
	 * Indicates whether predictor-corrector should be used while iterating.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	// protected static final boolean USE_PREDICTOR_CORRECTOR = true;
	
	

	
	/**
	 * Indicates whether a form of nonlinear numerical viscosity should be used while iterating.
	 */
	protected static final boolean APPLY_NUMERICAL_VISCOSITY = true;
	
	
	
	
	/**
	 * Discretized index for the T-Axis.
	 */
	protected static final int TV = 0;
	
	/**
	 * Discretized index for the X-Axis (real component of the X-axis).
	 */
	protected static final int XV = 1;
	
	/**
	 * Discretized index for the XI-Axis (imaginary component of the X-axis).
	 */
	protected static final int XIV = 2;
	
	
	
	
	
	protected static class IterationThreadContext
	{
	
	
		/**
		 * Result array of real values over which to iterate.
		 */
		protected DrFastArray3D_Dbl iterArrayRe = null;
		
		/**
		 * Result array of imaginary values over which to iterate.
		 */
		protected DrFastArray3D_Dbl iterArrayIm = null;
		

	
	
	/**
	 * Temporary array of real values in which to generate Newton-Raphson solutions.
	 * <p>0 = T
	 * <p>1 = X
	 * <p>2 = XI
	 */
	private BigFixedPointElem<LrgPrecision>[][][] tempArrayRe = ( BigFixedPointElem<LrgPrecision>[][][])( new BigFixedPointElem[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPXI * 2 + 1 ] );
	
	
	/**
	 * Temporary array of imaginary values in which to generate Newton-Raphson solutions.
	 * <p>0 = T
	 * <p>1 = X
	 * <p>2 = XI
	 */
	private BigFixedPointElem<LrgPrecision>[][][] tempArrayIm = ( BigFixedPointElem<LrgPrecision>[][][])( new BigFixedPointElem[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPXI * 2 + 1 ] );
	
	
	/**
	 * The current X iteration location. Used for calculating potentials.
	 */
	protected int tempXLocn = 0;
	
	/**
	 * The current XI iteration location. Used for calculating potentials.
	 */
	protected int tempXILocn = 0;
	
	/**
	 * The current T iteration location. Used for calculating potentials.
	 */
	protected int tempTLocn = 0;
	
	
	
	
	/**
	 * Given a change calculated by a Newton-Raphson iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected void performIterationUpdate( ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> dbl )
	{
		tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPXI ] = tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPXI ].add( dbl.getRe() );
		tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPXI ] = tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPXI ].add( dbl.getIm() );
	}
	
	
	/**
	 * The real iteration cache value.
	 */
	protected BigFixedPointElem<LrgPrecision> iterationValueCacheRe = de.zero();
	
	
	/**
	 * The imaginary iteration cache value.
	 */
	protected BigFixedPointElem<LrgPrecision> iterationValueCacheIm = de.zero();
	
	
	/**
	 * Places the current iteration value in the cache.
	 */
	protected void cacheIterationValue()
	{
		iterationValueCacheRe = tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPXI ];
		iterationValueCacheIm = tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPXI ];
	}
	
	
	/**
	 * Sets the current iteration value to the value in the cache.
	 */
	protected void retrieveIterationValue()
	{
		tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPXI ] = iterationValueCacheRe;
		tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPXI ] = iterationValueCacheIm;
	}
	
	
	/**
	 * Returns the real component of the result of the Newton-Raphson iterations
	 * from the temp array.
	 * 
	 * @return The real value in the temp array.
	 */
	protected BigFixedPointElem<LrgPrecision> getUpdateValueRe()
	{
		return( tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPXI ] );
	}
	
	
	/**
	 * Returns the imaginary component of the result of the Newton-Raphson iterations
	 * from the temp array.
	 * 
	 * @return The imaginary value in the temp array.
	 */
	protected BigFixedPointElem<LrgPrecision> getUpdateValueIm()
	{
		return( tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPXI ] );
	}
	
	/**
	 * Returns real component of the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @return The real value in the temp array.
	 */
	protected BigFixedPointElem<LrgPrecision> getCorrectionValueRe()
	{
		return( tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ] );
	}
	
	
	/**
	 * Returns the imaginary component of the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @return The imaginary value in the temp array.
	 */
	protected BigFixedPointElem<LrgPrecision> getCorrectionValueIm()
	{
		return( tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ] );
	}
	
	
	/**
	 * Resets the real component of the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @param in The value to which to reset.
	 */
	protected void resetCorrectionValueRe( final BigFixedPointElem<LrgPrecision> in )
	{
		tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ] = in;
	}
	
	
	/**
	 * Resets the imaginery component of the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @param in The value to which to reset.
	 */
	protected void resetCorrectionValueIm( final BigFixedPointElem<LrgPrecision> in )
	{
		tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ] = in;
	}
	
	
	
	/**
	 * Approximate maximum change allowed by nonlinear viscosity.
	 */
	final static double MAX_CHG = 0.05;
	
	/**
	 * Multiplicative inverse of MAX_CHG.
	 */
	final static BigFixedPointElem<LrgPrecision> I_MAX_CHG = new BigFixedPointElem<LrgPrecision>( 1.0 / MAX_CHG , lrgPrecision );
	
	/**
	 * Size of change below which numerical viscosity isn't applied.
	 */
	final static BigFixedPointElem<LrgPrecision> NUMERICAL_VISCOSITY_EXIT_CUTOFF = new BigFixedPointElem<LrgPrecision>( 1E-5 , lrgPrecision );
	
	
	
	
	
	
	/**
	 * Applies a form of nonlinear numerical viscosity.
	 */
	protected void applyNumericViscosityRe() throws NotInvertibleException
	{
		final BigFixedPointElem<LrgPrecision> delt = tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPXI ].add(
			tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ].negate() ) ;
		final ArrayList<BigFixedPointElem<LrgPrecision>>
			args = new ArrayList<BigFixedPointElem<LrgPrecision>>();
		final BigFixedPointElem<LrgPrecision> adelt = delt.handleOptionalOp( AbsoluteValue.ABSOLUTE_VALUE , args);
		if( adelt.compareTo( NUMERICAL_VISCOSITY_EXIT_CUTOFF ) < 0 )
		{
			return;
		}
		final BigFixedPointElem<LrgPrecision> iadelt = adelt.invertLeft();
		final BigFixedPointElem<LrgPrecision> iadiv = ( ( iadelt.mult( iadelt ) ).add( I_MAX_CHG.mult( I_MAX_CHG ) ) ).handleOptionalOp( Sqrt.SQRT , args);
		final BigFixedPointElem<LrgPrecision> adiv = iadiv.invertLeft();
		tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPXI ] =
				tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ].add(
				( delt.compareTo( DOUBLE_ZERO ) > 0 ? adiv : adiv.negate() ) );
	}
	
	
	
	/**
	 * Applies a form of nonlinear numerical viscosity.
	 */
	protected void applyNumericViscosityIm() throws NotInvertibleException
	{
		final BigFixedPointElem<LrgPrecision> delt = tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPXI ].add(
			tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ].negate() );
		final ArrayList<BigFixedPointElem<LrgPrecision>>
			args = new ArrayList<BigFixedPointElem<LrgPrecision>>();
		final BigFixedPointElem<LrgPrecision> adelt = delt.handleOptionalOp( AbsoluteValue.ABSOLUTE_VALUE , args);
		if( adelt.compareTo( NUMERICAL_VISCOSITY_EXIT_CUTOFF ) < 0 )
		{
			return;
		}
		final BigFixedPointElem<LrgPrecision> iadelt = adelt.invertLeft();
		final BigFixedPointElem<LrgPrecision> iadiv = ( ( iadelt.mult( iadelt ) ).add( I_MAX_CHG.mult( I_MAX_CHG ) ) ).handleOptionalOp( Sqrt.SQRT , args);
		final BigFixedPointElem<LrgPrecision> adiv = iadiv.invertLeft();
		tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPXI ] =
				tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ].add(
				( delt.compareTo( DOUBLE_ZERO ) > 0 ? adiv : adiv.negate() ) );
	}
	
	
	
	/**
	 * Applies a form of nonlinear numerical viscosity.
	 */
	protected void applyNumericViscosity()
	{
		try
		{
			applyNumericViscosityRe();
			applyNumericViscosityIm();
		}
		catch( NotInvertibleException ex )
		{
			ex.printStackTrace( System.out );
		}
	}
	
	
	
	
	
	
	
	/**
	 * Applies a predictor-corrector process to the temp array.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	protected void applyPredictorCorrector()
	{
		final BigFixedPointElem<LrgPrecision> slopePrevRe = tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ].add(
				tempArrayRe[ NSTPT * 2 - 2 ][ NSTPX ][ NSTPXI ].negate() );
		final BigFixedPointElem<LrgPrecision> slopePrevIm = tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ].add(
				tempArrayIm[ NSTPT * 2 - 2 ][ NSTPX ][ NSTPXI ].negate() );
		final BigFixedPointElem<LrgPrecision> slopeNewRe = tempArrayRe[ NSTPT * 2 ][ NSTPX ][ NSTPXI ].add(
				tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ].negate() );
		final BigFixedPointElem<LrgPrecision> slopeNewIm = tempArrayIm[ NSTPT * 2 ][ NSTPX ][ NSTPXI ].add(
				tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ].negate() );
		final BigFixedPointElem<LrgPrecision> avgSlopeRe = slopePrevRe.add( slopeNewRe ).divideBy( 2 );
		final BigFixedPointElem<LrgPrecision> avgSlopeIm = slopePrevIm.add( slopeNewIm ).divideBy( 2 );
		tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ] = 
				tempArrayRe[ NSTPT * 2 - 2 ][ NSTPX ][ NSTPXI ].add( avgSlopeRe );
		tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPXI ] = 
				tempArrayIm[ NSTPT * 2 - 2 ][ NSTPX ][ NSTPXI ].add( avgSlopeIm );
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
		 * Gets the XI-Axis index for the center in the iter array.
		 * 
		 * @return The XI-Axis index for the center in the iter array.
		 */
		public int getXIcnt() {
			return xicnt;
		}


		/**
		 * Sets the XI-Axis index for the center in the iter array.
		 * 
		 * @param xicnt The XI-Axis index for the center in the iter array.
		 */
		public void setXIcnt(int xicnt) {
			this.xicnt = xicnt;
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
		 * Gets the XI-Axis iteration of the array fill.
		 * 
		 * @return The XI-Axis iteration of the array fill.
		 */
		public int getXIa() {
			return xia;
		}


		/**
		 * Sets the XI-Axis iteration of the array fill.
		 * 
		 * @param xia The XI-Axis iteration of the array fill.
		 */
		public void setXIa(int xia) {
			this.xia = xia;
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
		 * The XI-Axis index for the center in the iter array.
		 */
		protected int xicnt;
		
		/**
		 * The T-Axis iteration of the array fill.
		 */
		protected int ta;
		
		/**
		 * The X-Axis iteration of the array fill.
		 */
		protected int xa;
		
		/**
		 * The XI-Axis iteration of the array fill.
		 */
		protected int xia;
		
	
	}
	
	
	
	/**
	 * Fills one element of the temp array with an element from the iter array.
	 * 
	 * @param param Input parameter describing where to get the element.
	 */
	protected void fillTempArrayInner( TempArrayFillInnerParam param ) throws Throwable
	{
		final int tcnt = param.getTcnt();
		final int xcnt = param.getXcnt();
		final int xicnt = param.getXIcnt();
		
		final int ta = param.getTa();
		final int xa = param.getXa();
		final int xia = param.getXIa();
		
		final int tv = tcnt + ta;
		final int xv = xcnt + xa;
		final int xiv = xicnt + xia;
		BigFixedPointElem<LrgPrecision> avRe = de.zero();
		BigFixedPointElem<LrgPrecision> avIm = de.zero();
		if( ( tv >= 0 )  && ( xv >= 0 ) && ( xiv >= 0 ) &&
			( tv < NUM_T_ITER ) && ( xv < NUM_X_ITER ) && ( xiv < NUM_XI_ITER )  )
		{
			avRe = new BigFixedPointElem<LrgPrecision>( iterArrayRe.get( tv , xv , xiv ) , lrgPrecision );
			avIm = new BigFixedPointElem<LrgPrecision>( iterArrayIm.get( tv , xv , xiv ) , lrgPrecision );
		}
		tempArrayRe[ ta + NSTPT ][ xa + NSTPX ][ xia + NSTPXI ] = avRe;
		tempArrayIm[ ta + NSTPT ][ xa + NSTPX ][ xia + NSTPXI ] = avIm;
		
	}
	
	

	/**
	 * Overlays an initial seed value into the temp array that serves as the starting point for Newton-Raphson iterations.
	 */
	protected void overlayInitialSeedForIterations()
	{
		// Overlay initial seed for iterations.
		for( int xa = 0 ; xa < NSTPX * 2 + 1 ; xa++ )
		{
			for( int xia = 0 ; xia < NSTPXI * 2 + 1 ; xia++ )
			{
				tempArrayRe[ NSTPT * 2 ][ xa ][ xia ] = tempArrayRe[ NSTPT * 2 - 1 ][ xa ][ xia ];
				tempArrayIm[ NSTPT * 2 ][ xa ][ xia ] = tempArrayIm[ NSTPT * 2 - 1 ][ xa ][ xia ];
			}
		}
	}
	
	
	
	
	/**
	 * Fills the temp array with elements from the iter array.
	 * 
	 * @param tcnt The T-Axis index for the center in the iter array.
	 * @param xcnt The X-Axis index for the center in the iter array.
	 * @param xicnt The XI-Axis index for the center in the iter array.
	 */
	protected void fillTempArray( final int tcnt , final int xcnt , final int xicnt ) throws Throwable
	{
		final TempArrayFillInnerParam param = new TempArrayFillInnerParam();
		
		param.setTcnt( tcnt );
		param.setXcnt( xcnt );
		param.setXIcnt( xicnt );
		
		for( int ta = -NSTPT ; ta < NSTPT + 1 ; ta++ )
		{
			param.setTa( ta );
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				param.setXa( xa );
				for( int xia = -NSTPXI ; xia < NSTPXI + 1 ; xia++ )
				{
					param.setXIa( xia );
					fillTempArrayInner( param ); 
				}
			}
		}
		
		
		overlayInitialSeedForIterations();
				
	}
	
	
	
	
	
	
	
	
	/**
	 * Fills the temp array with elements from the iter array, assuming a shift by one in Y.
	 * This should be faster because a temp array shift is much faster than refreshing from the file store.
	 * 
	 * @param tcnt The T-Axis index for the center in the iter array.
	 * @param xcnt The X-Axis index for the center in the iter array.
	 * @param xicnt The XI-Axis index for the center in the iter array.
	 */
	protected void fillTempArrayShiftXIup( final int tcnt , final int xcnt , final int xicnt ) throws Throwable
	{
		for( int ta = 0 ; ta < 2 * NSTPT + 1 ; ta++ )
		{
			for( int xa = 0 ; xa < 2 * NSTPX + 1 ; xa++ )
			{
				for( int xia = 0 ; xia < 2 * NSTPXI ; xia++ )
				{
					tempArrayRe[ ta ][ xa ][ xia ] = tempArrayRe[ ta ][ xa ][ xia + 1 ]; 
					tempArrayIm[ ta ][ xa ][ xia ] = tempArrayIm[ ta ][ xa ][ xia + 1 ];
				}
			}
		}
		
		final TempArrayFillInnerParam param = new TempArrayFillInnerParam();
		
		param.setTcnt( tcnt );
		param.setXcnt( xcnt );
		param.setXIcnt( xicnt );
		param.setXIa( NSTPXI );
		
		for( int ta = -NSTPT ; ta < NSTPT ; ta++ )
		{
			param.setTa( ta );
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				param.setXa( xa );
				fillTempArrayInner( param ); 
			}
		}
		
		
		overlayInitialSeedForIterations();
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Fills the temp array with elements from the iter array, assuming a shift by one in Y.
	 * This should be faster because a temp array shift is much faster than refreshing from the file store.
	 * 
	 * @param tcnt The T-Axis index for the center in the iter array.
	 * @param xcnt The X-Axis index for the center in the iter array.
	 * @param xicnt The XI-Axis index for the center in the iter array.
	 */
	protected void fillTempArrayShiftXIdown( final int tcnt , final int xcnt , final int xicnt ) throws Throwable
	{
		for( int ta = 0 ; ta < 2 * NSTPT + 1 ; ta++ )
		{
			for( int xa = 0 ; xa < 2 * NSTPX + 1 ; xa++ )
			{
				for( int xia = 2 * NSTPXI ; xia > 0 ; xia-- )
				{
					tempArrayRe[ ta ][ xa ][ xia ] = tempArrayRe[ ta ][ xa ][ xia - 1 ]; 
					tempArrayIm[ ta ][ xa ][ xia ] = tempArrayIm[ ta ][ xa ][ xia - 1 ]; 
				}
			}
		}
		
		final TempArrayFillInnerParam param = new TempArrayFillInnerParam();
		
		param.setTcnt( tcnt );
		param.setXcnt( xcnt );
		param.setXIcnt( xicnt );
		param.setXIa( -NSTPXI );
		
		for( int ta = -NSTPT ; ta < NSTPT ; ta++ )
		{
			param.setTa( ta );
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				param.setXa( xa );
				fillTempArrayInner( param ); 
			}
		}
		
		
		overlayInitialSeedForIterations();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Fills the temp array with elements from the iter array, assuming a shift by one in X.
	 * This should be faster because a temp array shift is much faster than refreshing from the file store.
	 * 
	 * @param tcnt The T-Axis index for the center in the iter array.
	 * @param xcnt The X-Axis index for the center in the iter array.
	 * @param xicnt The XI-Axis index for the center in the iter array.
	 */
	protected void fillTempArrayShiftXup( final int tcnt , final int xcnt , final int xicnt ) throws Throwable
	{
		for( int ta = 0 ; ta < 2 * NSTPT + 1 ; ta++ )
		{
			for( int xa = 0 ; xa < 2 * NSTPX ; xa++ )
			{
				for( int xia = 0 ; xia < 2 * NSTPXI + 1 ; xia++ )
				{
					tempArrayRe[ ta ][ xa ][ xia ] = tempArrayRe[ ta ][ xa + 1 ][ xia ]; 
					tempArrayIm[ ta ][ xa ][ xia ] = tempArrayIm[ ta ][ xa + 1 ][ xia ]; 
				}
			}
		}
		
		final TempArrayFillInnerParam param = new TempArrayFillInnerParam();
		
		param.setTcnt( tcnt );
		param.setXcnt( xcnt );
		param.setXIcnt( xicnt );
		param.setXa( NSTPX );
		
		for( int ta = -NSTPT ; ta < NSTPT ; ta++ )
		{
			param.setTa( ta );
			for( int xia = -NSTPXI ; xia < NSTPXI + 1 ; xia++ )
			{
				param.setXIa( xia );
				fillTempArrayInner( param ); 
			}
		}
		
		
		overlayInitialSeedForIterations();
		
	}
	
	
	
	
	
	
	/**
	 * Test array used to verify that the entire temp array has been filled.
	 */
	private int[][][] spatialAssertArray = new int[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPXI * 2 + 1 ];
	
	
	/**
	 * Clears the test array used to verify that the entire temp array has been filled.
	 */
	protected void clearSpatialAssertArray( )
	{
		for( int ta = -NSTPT ; ta < NSTPT + 1 ; ta++ )
		{
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				for( int xia = -NSTPXI ; xia < NSTPXI + 1 ; xia++ )
				{
					spatialAssertArray[ ta + NSTPT ][ xa + NSTPX ][ xia + NSTPXI ] = 0;
				}
			}
		}
	}
	
	
	
	
	
	} // IterationThreadContext
	
	
	
	

	
	

	
	/**
	 * In itializes the iteration thread contexts.
	 * @return An array of initialized iteration thread contexts.
	 */
	protected static TestSchrodingerCdimBN.IterationThreadContext[] initIterationThreadContext()
	{
		final TestSchrodingerCdimBN.IterationThreadContext[] ret = new TestSchrodingerCdimBN.IterationThreadContext[ NUM_CPU_CORES ];
		for( int cnt = 0 ; cnt < NUM_CPU_CORES ; cnt++ )
		{
			ret[ cnt ] = new TestSchrodingerCdimBN.IterationThreadContext();
		}
		return( ret );
	}
	
	
	
	
	
	/**
	 * The array of iteration thread contexts for multi-threading.
	 */
	protected static TestSchrodingerCdimBN.IterationThreadContext[] iterationThreadContexts = initIterationThreadContext();
	
	
	
	
	
	
	
	/**
	 * Cache for symbolic constants.
	 * 
	 * @author tgreen
	 *
	 */
	private static class SymbolicConstCache
	{
		
		/**
		 * Map representing the cache.
		 */
		protected static HashMap<ArrayList<BigInteger>,SymbolicConst> map = new HashMap<ArrayList<BigInteger>,SymbolicConst>();
		
		/**
		 * Returns a cached SymbolicConst representing a ComplexElem.
		 * 
		 * @param in The ComplexElem to be represented.
		 * @param _fac The factory for ComplexElem instances.
		 * @return The cached SymbolicConst.
		 */
		public static SymbolicConst get(  ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> in , ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> _fac )
		{
			final ArrayList<BigInteger> key = new ArrayList<BigInteger>();
			key.add( in.getRe().getPrecVal() );
			key.add( in.getIm().getPrecVal() );
			SymbolicConst cnst = map.get( key );
			if( cnst == null )
			{
				cnst = new SymbolicConst( in , _fac );
				map.put( key , cnst );
			}
			return( cnst );
		}
		
		/**
		 * Clears the cache.
		 */
		public static void clearCache()
		{
			map.clear();
		}
		
	}
	
	
	
	/**
	 * Defines a directional derivative for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class DDirec extends DirectionalDerivativePartialFactory<
		SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>, 
		SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
		Ordinate>
	{
		/**
		 * Factory for building the value of the derivative of an ordinate.
		 */
		ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> de;
		
		/**
		 * Factory for the enclosed type.
		 */
		SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> se2;
		
		/**
		 * Constructs the directional derivative factory.
		 * 
		 * @param _de Factory for building the value of the derivative of an ordinate.
		 * @param _se2 Factory for the enclosed type.
		 */
		public DDirec( 
				final ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _de ,
				final SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> _se2 )
		{
			de = _de;
			se2 = _se2;
		}

		@Override
		public SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, 
			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> getPartial(
				BigInteger basisIndex) 
		{
			if( basisIndex.equals( BigInteger.ZERO ) )
			{
				final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
			
				wrtX.add( new Ordinate( de , 1 + basisIndex.intValue() ) );
			
				return( new PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
						SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,Ordinate>( se2 , wrtX ) );
			}
			else
			{
				// XI isn't an actual axis in the sense that X is, hence fudge the code here so that the correct partial is used.
				
				SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, 
					SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>
					se3 = new SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, 
					SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>( se2 );
				
				final SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> se = new SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( de );
				
				final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
				
				wrtX.add( new Ordinate( de , 1 + basisIndex.intValue() ) );
				
				PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
					SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,Ordinate> p0 =
							new PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
							SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,Ordinate>( se2 , wrtX );
				
				final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, 
				SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>
					cmul = ( new StelemReduction3L( new StelemReduction2L( SymbolicConstCache.get( 
							II , de ) , se ) , se2 )
							);
				
				return( cmul.mult( p0 ).negate() );
				
				
				
			}
		}


	};
	
	
	
	/**
	 * Node representing an ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class Ordinate extends SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
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
		public Ordinate(ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> _fac, int _col) {
			super(_fac);
			col = _col;
		}

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		

		
		@Override
		public Ordinate cloneThread( final BigInteger threadIndex )
		{
			return( this );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( Ordinate.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( Ordinate.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( col );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> b )
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
	private static class SymbolicConst extends SymbolicReduction<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public SymbolicConst(ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> _elem, ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> b )
		{
			if( b instanceof SymbolicConst )
			{
				final boolean eqr = getElem().getRe().compareTo( ( (SymbolicConst) b ).getElem().getRe() ) == 0;
				final boolean eqi = getElem().getIm().compareTo( ( (SymbolicConst) b ).getElem().getIm() ) == 0;
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
	private static class StelemReduction2L extends SymbolicReduction<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public StelemReduction2L(SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> _elem, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> b )
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
	private static class StelemReduction3L extends SymbolicReduction<
		SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
		SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public StelemReduction3L(
				SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> _elem, 
				SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<
				SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
				SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> b )
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
	private static class CoeffNode
	{
		/**
		 * The numerator.
		 */
		private SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> numer;
		
		/**
		 * The denominator.
		 */
		private SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> denom;
		
		/**
		 * Constructs the coefficient.
		 * 
		 * @param _numer The numerator.
		 * @param _denom The denominator.
		 */
		public CoeffNode( SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> _numer , SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> _denom )
		{
			numer = _numer;
			denom = _denom;
		}
		
		/**
		 * Gets the numerator.
		 * 
		 * @return The numerator.
		 */
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> getNumer() {
			return numer;
		}
		
		/**
		 * Gets the denominator.
		 * 
		 * @return The denominator.
		 */
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> getDenom() {
			return denom;
		}
		
	}
	
	
	
	
	

	
	/**
	 * Elem representing the discretized equivalent 
	 * of the complex number potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class BVelem extends Nelem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,Ordinate>
	{

		/**
		 * The thread context in which to evaluate.
		 */
		protected TestSchrodingerCdimBN.IterationThreadContext threadContext;
		
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public BVelem(ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> _fac, int _threadIndex ) {
			super(_fac, new HashMap<Ordinate, BigInteger>() );
			threadContext = iterationThreadContexts[ _threadIndex ];
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
		public ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
//			final double YHALF = NUM_Y_ITER / 2.0;
//			// final BigFixedPointElem<LrgPrecision> re = new BigFixedPointElem<LrgPrecision>( ( vSlope * ( threadContext.tempYLocn - YHALF ) * TOTAL_Y_AXIS_SIZE / NUM_Y_ITER ) + vStart );
//			// System.out.println( re.getVal() );
//			final BigFixedPointElem<LrgPrecision> re = ( threadContext.tempYLocn > YHALF ) ? new BigFixedPointElem<LrgPrecision>( 1E+10 ) : new BigFixedPointElem<LrgPrecision>( 0.0 );
//			// System.out.println( re.getVal() );
//			return( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( re , new BigFixedPointElem<LrgPrecision>( 0.0 ) ) );
			
			
			final double XIHALF = NUM_XI_ITER / 2.0;
			final double u = ( threadContext.tempXLocn - XIHALF ) / XIHALF;
			final double uua = Math.pow( Math.abs( u ) , 0.001 );
			final double uu = u < 0.0 ? -uua : uua;
			final double uz = ( uu + 1.0 ) / 2.0;
			final double expn = ( 1.0 - uz ) * ( Math.log( 0.1 ) ) + ( uz ) * ( Math.log( 1E+10 ) );
			// final BigFixedPointElem<LrgPrecision> re = new BigFixedPointElem<LrgPrecision>( ( vSlope * ( threadContext.tempYLocn - YHALF ) * TOTAL_Y_AXIS_SIZE / NUM_Y_ITER ) + vStart );
			// System.out.println( re.getVal() );
			final BigFixedPointElem<LrgPrecision> re = new BigFixedPointElem<LrgPrecision>( Math.exp( expn ) , lrgPrecision );
			// System.out.println( re.getVal() );
			return( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( re , de.zero() ) );
			
		}
		
		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}
		
		/**
		 * Copies the BVelem for threading.
		 * 
		 * @param in The BVelem to be copied.
		 * @param threadIndex The thread index.
		 */
		public BVelem( final BVelem in , final BigInteger threadIndex )
		{
			super( in , threadIndex );
			final int threadInd = threadIndex.intValue();
			threadContext = iterationThreadContexts[ threadInd ];
		}
		
		
		@Override
		public BVelem cloneThread( final BigInteger threadIndex )
		{
			return( new BVelem( this , threadIndex ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( BVelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( BVelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> b )
		{
			if( b instanceof BVelem )
			{
				return( true );
			}
			return( false );
		}
		
		
	}
	


	
	
	
	

	
	
	/**
	 * Elem representing an altering value.
	 * 
	 * @author thorngreen
	 *
	 */
//	private static class BAelem extends Nelem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,Ordinate>
//	{
//		
//		/**
//		 * Constructs the elem.
//		 * 
//		 * @param _fac The factory for the enclosed type.
//		 */
//		public BAelem(ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> _fac, int _threadIndex ) {
//			super(_fac, new HashMap<Ordinate, BigInteger>() );
//			threadI = _threadIndex;
//		}
//		
//		
//		/**
//		 * Column indices in the discretized space.
//		 */
//		protected final int[] cols = new int[ 4 ];
//		
//		/**
//		 * Assertion booleans used to verify that all
//		 * column indices have been initialized.
//		 */
//		protected final boolean[] assertCols = new boolean[ 4 ];
//		
//		/**
//		 * Thread index.
//		 */
//		protected int threadI;
//		
//
//		@Override
//		public ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
//				throws NotInvertibleException,
//				MultiplicativeDistributionRequiredException {
//			
//			TestSchrodingerCdimBN.IterationThreadContext threadContext = iterationThreadContexts[ threadI ];
//			
//			if( threadContext == null )
//			{
//				System.out.println( "Thread Context Fail" );
//				throw( new RuntimeException( "Thread Context Fail." ) );
//			}
//			
//			try
//			{
//				if( ( threadContext.tempXLocn % 2 == 0 ) && ( threadContext.tempYLocn % 2 == 0 ) && ( threadContext.tempZLocn % 2 == 0 ) && ( threadContext.tempTLocn % 2 == 0 ) )
//				{
//					final double d1 = Math.sqrt( X_HH.getRe().getVal() * X_HH.getRe().getVal() + Y_HH.getRe().getVal() * Y_HH.getRe().getVal() + Z_HH.getRe().getVal() * Z_HH.getRe().getVal() );
//					
//					final long rseed = ( 2748833L * threadContext.tempXLocn ) + ( 2749247L * threadContext.tempYLocn ) + ( 2749679L * threadContext.tempZLocn ) + ( 2750159L * threadContext.tempTLocn ) + 54321L;
//					final Random rand = new Random( rseed );
//					rand.nextInt();
//			
//					final double dx = ( 0.0 + threadContext.tempXLocn - HALF_X ) / RAD_X;
//					final double dy = ( 0.0 + threadContext.tempYLocn - HALF_Y ) / RAD_Y;
//					final double dz = ( 0.0 + threadContext.tempZLocn - HALF_Z ) / RAD_Z;
//					double dval = 0.0;
//					if( dx * dx + dy * dy + dz * dz < 1.0 )
//					{
//						dval = 10000.0 * ( d1 * d1 );
//					}
//			
//					final double tempRe = threadContext.tempArrayRe[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ];
//					final double tempIm = threadContext.tempArrayIm[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ];
//					final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> tmp =
//							new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( tempRe ) , new BigFixedPointElem<LrgPrecision>( tempIm ) );
//					final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
//						ra1 = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 0.0 ) , new BigFixedPointElem<LrgPrecision>( 2.0 * Math.PI * ( rand.nextDouble() ) ) );
//					
//					final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
//						magTp = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( dval - Math.sqrt( tempRe * tempRe + tempIm * tempIm ) ) , new BigFixedPointElem<LrgPrecision>( 0.0 ) );
//					
//					ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> rb1 = tmp.add( magTp.mult( ra1.exp( 15 ) ) );
//					BigFixedPointElem<LrgPrecision> d1a = (BigFixedPointElem<LrgPrecision>)( rb1.totalMagnitude() );
//					ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
//						ret = rb1;
//					
//					for( int count = 0 ; count < 10 ; count++ )
//					{
//						final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
//							ra2 = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 0.0 ) , new BigFixedPointElem<LrgPrecision>( 2.0 * Math.PI * ( rand.nextDouble() ) ) );
//						final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> rb2 = tmp.add( magTp.mult( ra2.exp( 15 ) ) );
//						final BigFixedPointElem<LrgPrecision> d2a = (BigFixedPointElem<LrgPrecision>)( rb2.totalMagnitude() );
//						if( Math.abs( d2a.getVal() - dval ) < Math.abs( d1a.getVal() - dval ) )
//						{
//							ret = rb2;
//							rb1 = rb2;
//							d1a = d2a;
//						}
//					}
//					return( ret );
//				}
//			}
//			catch( Throwable ex )
//			{
//				ex.printStackTrace( System.out );
//				throw( new RuntimeException( ex ) );
//			}
//			
//			return( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 0.0 ) , new BigFixedPointElem<LrgPrecision>( 0.0 ) ) );
//		}
//		
//		
//		@Override
//		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalCached(
//				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
//				HashMap<SCacheKey<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
//				throws NotInvertibleException,
//				MultiplicativeDistributionRequiredException {
//			return( eval( implicitSpace ) );
//		}
//		
//		/**
//		 * Copies the BAelem for threading.
//		 * 
//		 * @param in The BAelem to be copied.
//		 * @param threadIndex The thread index.
//		 */
//		public BAelem( final BAelem in , final BigInteger threadIndex )
//		{
//			super( in , threadIndex );
//			final int threadInd = threadIndex.intValue();
//			this.threadI = threadInd;
//		}
//		
//		
//		@Override
//		public BAelem cloneThread( final BigInteger threadIndex )
//		{
//			return( new BAelem( this , threadIndex ) );
//		}
//
//		@Override
//		public String writeDesc(
//				WriteElemCache<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
//				PrintStream ps) {
//			String st = cache.get( this );
//			if( st == null )
//			{
//				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>)( cache.getInnerCache() ) , ps);
//				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
//				st = cache.getIncrementVal();
//				cache.put(this, st);
//				ps.print( BAelem.class.getSimpleName() );
//				ps.print( " " );
//				ps.print( st );
//				ps.print( " = new " );
//				ps.print( BAelem.class.getSimpleName() );
//				ps.print( "( " );
//				ps.print( sta );
//				ps.println( " );" );
//			}
//			return( st );
//		}
//		
//		@Override
//		public boolean symbolicEquals( SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> b )
//		{
//			if( b instanceof BAelem )
//			{
//				return( true );
//			}
//			return( false );
//		}
//		
//		
//	}
//	
//	
	
	
	
	

	
	/**
	 * Elem representing the symbolic expression for 
	 * the discretized equivalent
	 * of the complex number potential.
	 * The partial derivatives of this elem generate
	 * the slopes for producing Newton-Raphson iterations (e.g. the Jacobian slopes),
	 * as opposed to partial derivatives for the underlying differential equation.
	 * 
	 * @author thorngreen
	 *
	 */	
	private static class CVelem extends Nelem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,
		SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,Ordinate>
	{

		/**
		 * The thread index for the elem.
		 */
		protected int threadIndex;
		
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public CVelem(SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> _fac ) {
			super(_fac, new HashMap<Ordinate, BigInteger>() );
			threadIndex = 0;
		}

		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( new BVelem( fac.getFac() , threadIndex ) );
		}
		
		
		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			final SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> key =
					new SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>( this , implicitSpace );
			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> iret = cache.get( key );
			if( iret != null )
			{
				return( iret );
			}
			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> ret = eval( implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		
		
		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
			if( withRespectTo.size() > 1 )
			{
				return( fac.zero() );
			}
			Iterator<? extends Elem<?,?>> it = withRespectTo.iterator();
			CNelem wrt = (CNelem)( it.next() );
			// final boolean cond = this.symbolicEquals( wrt );
			// return( cond ? fac.identity() : fac.zero() );
			return( fac.zero() );
		}
		
		
		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> evalPartialDerivativeCached(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
			final SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> key =
					new SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( this , implicitSpace , withRespectTo );
			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> iret = cache.get( key );
			if( iret != null ) 
			{
				return( iret );
			}
			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> ret = evalPartialDerivative( withRespectTo , implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		
		/**
		 * Copies the CVelem for threading.
		 * 
		 * @param in The CVelem to be copied.
		 * @param _threadIndex The thread index.
		 */
		public CVelem( final CVelem in , final BigInteger _threadIndex )
		{
			super( in , _threadIndex );
			final int threadInd = _threadIndex.intValue();
			threadIndex = threadInd;
		}
		
		
		@Override
		public CVelem cloneThread( final BigInteger threadIndex )
		{
			return( new CVelem( this , threadIndex ) );
		}
		

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( CVelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( CVelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		
		@Override
		public boolean symbolicEquals( 
				SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> b )
		{
			if( b instanceof CVelem )
			{
				return( true );
			}
			return( false );
		}
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Elem representing the symbolic expression for 
	 * an altering value.
	 * The partial derivatives of this elem generate
	 * the slopes for producing Newton-Raphson iterations (e.g. the Jacobian slopes),
	 * as opposed to partial derivatives for the underlying differential equation.
	 * 
	 * @author thorngreen
	 *
	 */	
//	private static class CAelem extends Nelem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,
//		SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,Ordinate>
//	{
//
//		/**
//		 * The thread index for the elem.
//		 */
//		protected int threadIndex;
//		
//		/**
//		 * Constructs the elem.
//		 * 
//		 * @param _fac The factory for the enclosed type.
//		 */
//		public CAelem(SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> _fac ) {
//			super(_fac, new HashMap<Ordinate, BigInteger>() );
//			threadIndex = 0;
//		}
//
//		@Override
//		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
//				throws NotInvertibleException,
//				MultiplicativeDistributionRequiredException {
//			return( new BAelem( fac.getFac() , threadIndex ) );
//		}
//		
//		
//		@Override
//		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> evalCached(
//				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
//				HashMap<SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache)
//				throws NotInvertibleException,
//				MultiplicativeDistributionRequiredException {
//			final SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> key =
//					new SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>( this , implicitSpace );
//			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> iret = cache.get( key );
//			if( iret != null )
//			{
//				return( iret );
//			}
//			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> ret = eval( implicitSpace );
//			cache.put(key, ret);
//			return( ret );
//		}
//		
//		
//		@Override
//		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
//			if( withRespectTo.size() > 1 )
//			{
//				return( fac.zero() );
//			}
//			Iterator<? extends Elem<?,?>> it = withRespectTo.iterator();
//			CNelem wrt = (CNelem)( it.next() );
//			// final boolean cond = this.symbolicEquals( wrt );
//			// return( cond ? fac.identity() : fac.zero() );
//			return( fac.zero() );
//		}
//		
//		
//		@Override
//		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> evalPartialDerivativeCached(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace,
//				HashMap<SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
//			final SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> key =
//					new SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( this , implicitSpace , withRespectTo );
//			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> iret = cache.get( key );
//			if( iret != null ) 
//			{
//				return( iret );
//			}
//			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> ret = evalPartialDerivative( withRespectTo , implicitSpace );
//			cache.put(key, ret);
//			return( ret );
//		}
//		
//		/**
//		 * Copies the CAelem for threading.
//		 * 
//		 * @param in The CAelem to be copied.
//		 * @param _threadIndex The thread index.
//		 */
//		public CAelem( final CAelem in , final BigInteger _threadIndex )
//		{
//			super( in , _threadIndex );
//			final int threadInd = _threadIndex.intValue();
//			threadIndex = threadInd;
//		}
//		
//		
//		@Override
//		public CAelem cloneThread( final BigInteger threadIndex )
//		{
//			return( new CAelem( this , threadIndex ) );
//		}
//		
//
//		@Override
//		public String writeDesc(
//				WriteElemCache<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache,
//				PrintStream ps) {
//			String st = cache.get( this );
//			if( st == null )
//			{
//				final String sta = fac.writeDesc( (WriteElemCache<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>)( cache.getInnerCache() ) , ps);
//				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
//				st = cache.getIncrementVal();
//				cache.put(this, st);
//				ps.print( CAelem.class.getSimpleName() );
//				ps.print( " " );
//				ps.print( st );
//				ps.print( " = new " );
//				ps.print( CAelem.class.getSimpleName() );
//				ps.print( "( " );
//				ps.print( sta );
//				ps.println( " );" );
//			}
//			return( st );
//		}
//		
//		
//		@Override
//		public boolean symbolicEquals( 
//				SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> b )
//		{
//			if( b instanceof CAelem )
//			{
//				return( true );
//			}
//			return( false );
//		}
//		
//	}
//	
//	
	
	
	
	
	
	
	/**
	 * Elem representing the discretized equivalent 
	 * of the complex number constrained by the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class BNelem extends Nelem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,Ordinate>
	{

		/**
		 * The thread context in which to evaluate.
		 */
		protected TestSchrodingerCdimBN.IterationThreadContext threadContext;
		
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
		 */
		public BNelem(ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> _fac, HashMap<Ordinate, BigInteger> _coord, int _threadIndex ) {
			super(_fac, _coord);
			threadContext = iterationThreadContexts[ _threadIndex ];
		}
		
		
		/**
		 * Column indices in the discretized space.
		 */
		protected final int[] cols = new int[ 3 ];
		
		/**
		 * Assertion booleans used to verify that all
		 * column indices have been initialized.
		 */
		protected final boolean[] assertCols = new boolean[ 3 ];
		

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			cols[ 0 ] = 0;
			cols[ 1 ] = 0;
			cols[ 2 ] = 0;
			assertCols[ 0 ] = false;
			assertCols[ 1 ] = false;
			assertCols[ 2 ] = false;
			Assert.assertTrue( coord.keySet().size() == 3 );
			for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
			{
				Ordinate keyCoord = ii.getKey();
				BigInteger coordVal = ii.getValue();
				final int offset = keyCoord.getCol() == 2 ? NSTPXI : keyCoord.getCol() == 1 ? NSTPX : NSTPT;
				cols[ keyCoord.getCol() ] = coordVal.intValue() + offset;
				assertCols[ keyCoord.getCol() ] = true;
			}
			( threadContext.spatialAssertArray[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ] )++;
			Assert.assertTrue( assertCols[ 0 ] );
			Assert.assertTrue( assertCols[ 1 ] );
			Assert.assertTrue( assertCols[ 2 ] );
			final BigFixedPointElem<LrgPrecision> re = threadContext.tempArrayRe[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ];
			final BigFixedPointElem<LrgPrecision> im = threadContext.tempArrayIm[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ];
			return( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( re , im ) );
		}
		
		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}
		

		/**
		 * Copies the BNelem for threading.
		 * 
		 * @param in The BNelem to be copied.
		 * @param threadIndex The thread index.
		 */
		public BNelem( final BNelem in , final BigInteger threadIndex )
		{
			super( in , threadIndex );
			final int threadInd = threadIndex.intValue();
			threadContext = iterationThreadContexts[ threadInd ];
		}
		
		
		@Override
		public BNelem cloneThread( final BigInteger threadIndex )
		{
			return( new BNelem( this , threadIndex ) );
		}

		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				final String strr = cache.getIncrementVal();
				ps.print( "final HashMap<Ordinate, BigInteger> " );
				ps.print( strr );
				ps.println( " = new HashMap<Ordinate, BigInteger>();" );
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					final String stai = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( ii.getValue() , ps );
					final String sl = ii.getKey().writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
					ps.print( strr );
					ps.print( ".put( " );
					ps.print( sl );
					ps.print( " , " );
					ps.print( stai );
					ps.println( " );" );
				}
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( BNelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( BNelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( strr );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> b )
		{
			if( b instanceof BNelem )
			{
				BNelem bn = (BNelem) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					Ordinate key = ii.getKey();
					BigInteger ka = ii.getValue();
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
		
		@Override
		public boolean equals( Object b )
		{
			if( b instanceof BNelem )
			{
				BNelem bn = (BNelem) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					Ordinate key = ii.getKey();
					BigInteger ka = ii.getValue();
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
		
		
		@Override 
		public int hashCode()
		{
			int sum = 0;
			for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
			{
				Ordinate key = ii.getKey();
				BigInteger ka = ii.getValue();
				sum += key.hashCode();
				sum += ka.hashCode();
			}
			return( sum );
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
	private static class CNelem extends Nelem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,
		SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,Ordinate>
	{

		/**
		 * The thread index for the elem.
		 */
		protected int threadIndex;
		
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
		 */
		public CNelem(SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> _fac, HashMap<Ordinate, BigInteger> _coord) {
			super(_fac, _coord);
			threadIndex = 0;
		}

		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( new BNelem( fac.getFac() , coord , threadIndex ) );
		}
		
		
		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			final SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> key =
					new SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>( this , implicitSpace );
			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> iret = cache.get( key );
			if( iret != null )
			{
				return( iret );
			}
			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> ret = eval( implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		
		
		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
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
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> evalPartialDerivativeCached(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
			final SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> key =
					new SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( this , implicitSpace , withRespectTo );
			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> iret = cache.get( key );
			if( iret != null ) 
			{
				return( iret );
			}
			final SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> ret = evalPartialDerivative( withRespectTo , implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		
		/**
		 * Copies the CNelem for threading.
		 * 
		 * @param in The CNelem to be copied.
		 * @param _threadIndex The thread index.
		 */
		public CNelem( final CNelem in , final BigInteger _threadIndex )
		{
			super( in , _threadIndex );
			final int threadInd = _threadIndex.intValue();
			threadIndex = threadInd;
		}
		
		
		@Override
		public CNelem cloneThread( final BigInteger threadIndex )
		{
			return( new CNelem( this , threadIndex ) );
		}
		

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				final String strr = cache.getIncrementVal();
				ps.print( "final HashMap<Ordinate, BigInteger> " );
				ps.print( strr );
				ps.println( " = new HashMap<Ordinate, BigInteger>();" );
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					final String stai = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( ii.getValue() , ps );
					final String sl = ii.getKey().writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
					ps.print( strr );
					ps.print( ".put( " );
					ps.print( sl );
					ps.print( " , " );
					ps.print( stai );
					ps.println( " );" );
				}
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( CNelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( CNelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( strr );
				ps.println( " );" );
			}
			return( st );
		}
		
		
		@Override
		public boolean symbolicEquals( 
				SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> b )
		{
			if( b instanceof CNelem )
			{
				CNelem bn = (CNelem) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					Ordinate key = ii.getKey();
					BigInteger ka = ii.getValue();
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
	private class AStelem extends Stelem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,
		SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,Ordinate>
	{
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public AStelem(SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, 
				SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> _fac) {
			super(_fac);
		}

		@Override
		public AStelem cloneInstance() {
			AStelem cl = new AStelem( fac );
			for( Entry<Ordinate,BigInteger> ii : partialMap.entrySet() )
			{
				Ordinate key = ii.getKey();
				cl.partialMap.put(key, ii.getValue() );
			}
			return( cl );
		}

		@Override
		public SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, 
			SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			
			HashMap<Ordinate,Ordinate> imp = (HashMap<Ordinate,Ordinate>) implicitSpace;
			
			
			HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesA = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
			
			
			{
				CoeffNode cf = new CoeffNode( fac.getFac().identity() , fac.getFac().identity() );
				HashMap<Ordinate, BigInteger> key = new HashMap<Ordinate, BigInteger>();
				for( final Ordinate ae : imp.keySet() )
				{
					BigInteger valA = BigInteger.valueOf( imp.get( ae ).getCol() );
					key.put( ae , valA );
				}
				spacesA.put( key , cf );
			}
			
			
			{
				for( Entry<Ordinate,BigInteger> ii : partialMap.entrySet() )
				{
					HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesB = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
					final Ordinate ae = ii.getKey();
					final BigInteger numDerivs = ii.getValue();
					applyDerivativeAction( spacesA , ae , numDerivs.intValue() , (ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>)(HH[ ae.getCol() ]) , spacesB );
					spacesA = spacesB;
				}
			}
			
			
			
			SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, 
				SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> ret = fac.zero();
			
			
			{
				for( final Entry<HashMap<Ordinate, BigInteger>, CoeffNode> ii : spacesA.entrySet() )
				{
					HashMap<Ordinate, BigInteger> spaceAe = ii.getKey();
					CoeffNode coeff = ii.getValue();
					final CNelem an0 = 
							new CNelem( fac.getFac() , spaceAe );
					SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>
						an1 = an0.mult( 
								new StelemReduction2L( coeff.getNumer() , fac.getFac() ) );
					SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
						an2 = an1.mult( 
								( new StelemReduction2L( coeff.getDenom() , fac.getFac() ) ).invertLeft() );
					ret = ret.add( an2 );
				}
			}
			
			
			return( ret );
		}
		
		
		@Override
		public SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>, SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			final SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> key =
					new SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>( this , implicitSpace );
			final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> iret = cache.get( key );
			if( iret != null )
			{
				return( iret );
			}
			final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> ret =
					eval( implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		
		
		/**
		 * Copies the AStelem for threading.
		 * @param in The AStelem to be copied.
		 * @param threadIndex The thread index.
		 */
		protected AStelem( AStelem in , final BigInteger threadIndex )
		{
			super( in , threadIndex );
		}
		
		
		
		@Override
		public AStelem cloneThread( final BigInteger threadIndex )
		{
			return( new AStelem( this , threadIndex ) );
		}
		

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				final String sta = fac.writeDesc( (WriteElemCache< SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> , SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> >>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( AStelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( AStelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
				for( Entry<Ordinate,BigInteger> ii : partialMap.entrySet() )
				{
					Ordinate key = ii.getKey();
					BigInteger val = ii.getValue();
					final String stav = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( val , ps );
					final String stak = key.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
					ps.print( st );
					ps.print( ".partialMap.put( " );
					ps.print( stak );
					ps.print( " , " );
					ps.print( stav );
					ps.println( " );" );
				}
			}
			return( st );
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
				Ordinate node , final int numDerivatives , ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hh ,
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			if( numDerivatives > 3 )
			{
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesMid = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
				applyDerivativeAction(implicitSpacesIn, node, 3, hh, implicitSpacesMid);
				applyDerivativeAction(implicitSpacesMid, node, numDerivatives-3, hh, implicitSpacesOut);
				return;
			}
			
			for( final Entry<HashMap<Ordinate, BigInteger>,CoeffNode> ii : implicitSpacesIn.entrySet() )
			{
				final HashMap<Ordinate, BigInteger> implicitSpace = ii.getKey();
				final CoeffNode coeffNodeIn = ii.getValue();
				
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
					
				default:
					{
						throw( new RuntimeException( "Internal Error" ) );
					}
					
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
				Ordinate node , ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hh ,
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
			
			for( final Entry<Ordinate,BigInteger> ii : implicitSpace.entrySet() )
			{
				Ordinate ae = ii.getKey();
				final BigInteger valAe = ii.getValue();
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
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( genFromConst( 2.0 ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( genFromConst( 2.0 ) ), hh.getFac() ) ) );
			
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
				Ordinate node , ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hh ,
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
			
			for( final Entry<Ordinate,BigInteger> ii : implicitSpace.entrySet() )
			{
				Ordinate ae = ii.getKey();
				final BigInteger valAe = ii.getValue();
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
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh ) , hh.getFac() ) ) );
			final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate().mult( SymbolicConstCache.get( genFromConst( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh ) , hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh ) , hh.getFac() ) ) );
			
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
				Ordinate node , ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hh ,
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutM2 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutP2 = new HashMap<Ordinate, BigInteger>();
			
			for( final Entry<Ordinate,BigInteger> ii : implicitSpace.entrySet() )
			{
				Ordinate ae = ii.getKey();
				final BigInteger valAe = ii.getValue();
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
			
			final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().mult( SymbolicConstCache.get( genFromConst( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( genFromConst( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( SymbolicConstCache.get( genFromConst( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( genFromConst( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( genFromConst( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( genFromConst( 2.0 ) ) ) ), hh.getFac() ) ) );
			
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
			
			if( ( prev.getDenom().eval( null ).getRe().compareTo( node.getDenom().eval( null ).getRe() ) == 0 ) &&
				( prev.getDenom().eval( null ).getIm().compareTo( node.getDenom().eval( null ).getIm() ) == 0 ) )
			{
				SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> outN = node.getNumer().add( prev.getNumer() );
				CoeffNode nxt = new CoeffNode( outN , prev.getDenom() );
				implicitSpacesOut.put( implicitSpace , nxt );
				return;
			}
			
			
			SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> outDenom = prev.getDenom().mult( node.getDenom() );
			
			SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> outNumer = ( node.getDenom().mult( prev.getNumer() ) ).add( prev.getDenom().mult( node.getNumer() ) );
			
			CoeffNode nxt = new CoeffNode( outNumer , outDenom );
			
			implicitSpacesOut.put( implicitSpace , nxt );
		}
		
		
	}
	
	
	
	
	
	
	

	
	
	
	
	/**
	 * An elem defining a potential that is evaluated over the discretized space of the test.
	 * 
	 * @author thorngreen
	 *
	 */
	private class AVtelem extends SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
		SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>
	{
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public AVtelem(SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, 
				SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> _fac) {
			super(_fac);
		}

		@Override
		public SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, 
			SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {	
			return( new CVelem( fac.getFac() ) );
		}
		
		
		@Override
		public SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>, SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			final SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> key =
					new SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>( this , implicitSpace );
			final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> iret = cache.get( key );
			if( iret != null )
			{
				return( iret );
			}
			final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> ret =
					eval( implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		
		
		
		/**
		 * Copies the AVtelem for threading.
		 * @param in The AVtelem to be copied.
		 * @param threadIndex The thread index.
		 */
		protected AVtelem( AVtelem in , final BigInteger threadIndex )
		{
			super( in.getFac().getFac().cloneThread(threadIndex) );
		}
		
		
		
		@Override
		public AVtelem cloneThread( final BigInteger threadIndex )
		{
			return( new AVtelem( this , threadIndex ) );
		}

		
		

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				final String sta = fac.writeDesc( (WriteElemCache< SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> , SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> >>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( AVtelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( AVtelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}

		@Override
		public SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			ArrayList<Ordinate> wrt = (ArrayList<Ordinate>) withRespectTo;
			if( wrt.size() > 1 )
			{
				return( fac.zero() );
			}
			Iterator<Ordinate> it = wrt.iterator();
			Ordinate wr = it.next();
			
			if( wr.getCol() == 2  )
			{
				throw( new RuntimeException( "Not Supported..." ) );
			}
			
			return( fac.zero() );
			
		} 

		@Override
		public SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>, SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "Not Supported" ) );
		}
				
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * An elem defining an altering value.
	 * 
	 * @author thorngreen
	 *
	 */
//	private class AAtelem extends SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
//		SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>
//	{
//		/**
//		 * Constructs the elem.
//		 * 
//		 * @param _fac The factory for the enclosed type.
//		 */
//		public AAtelem(SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, 
//				SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> _fac) {
//			super(_fac);
//		}
//
//		@Override
//		public SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, 
//			SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> eval(
//				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
//				throws NotInvertibleException,
//				MultiplicativeDistributionRequiredException {	
//			return( new CAelem( fac.getFac() ) );
//		}
//		
//		
//		@Override
//		public SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> evalCached(
//				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
//				HashMap<SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>, SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache)
//				throws NotInvertibleException,
//				MultiplicativeDistributionRequiredException {
//			final SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> key =
//					new SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>( this , implicitSpace );
//			final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> iret = cache.get( key );
//			if( iret != null )
//			{
//				return( iret );
//			}
//			final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> ret =
//					eval( implicitSpace );
//			cache.put(key, ret);
//			return( ret );
//		}
//		
//		
//		
//		/**
//		 * Copies the AAtelem for threading.
//		 * @param in The AAtelem to be copied.
//		 * @param threadIndex The thread index.
//		 */
//		protected AAtelem( AAtelem in , final BigInteger threadIndex )
//		{
//			super( in.getFac().getFac().cloneThread(threadIndex) );
//		}
//		
//		
//		
//		@Override
//		public AAtelem cloneThread( final BigInteger threadIndex )
//		{
//			return( new AAtelem( this , threadIndex ) );
//		}
//
//		
//		
//
//		@Override
//		public String writeDesc(
//				WriteElemCache<SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>> cache,
//				PrintStream ps) {
//			String st = cache.get( this );
//			if( st == null )
//			{
//				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
//				final String sta = fac.writeDesc( (WriteElemCache< SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> , SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> >>)( cache.getInnerCache() ) , ps);
//				st = cache.getIncrementVal();
//				cache.put(this, st);
//				ps.print( AAtelem.class.getSimpleName() );
//				ps.print( " " );
//				ps.print( st );
//				ps.print( " = new " );
//				ps.print( AAtelem.class.getSimpleName() );
//				ps.print( "( " );
//				ps.print( sta );
//				ps.println( " );" );
//			}
//			return( st );
//		}
//
//		@Override
//		public SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> evalPartialDerivative(
//				ArrayList<? extends Elem<?, ?>> withRespectTo,
//				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
//				throws NotInvertibleException,
//				MultiplicativeDistributionRequiredException {
//			
//			ArrayList<Ordinate> wrt = (ArrayList<Ordinate>) withRespectTo;
//			if( wrt.size() > 1 )
//			{
//				return( fac.zero() );
//			}
//			Iterator<Ordinate> it = wrt.iterator();
//			Ordinate wr = it.next();
//			
//			if( wr.getCol() == 2  )
//			{
//				throw( new RuntimeException( "Not Supported..." ) );
//			}
//			
//			return( fac.zero() );
//			
//		} 
//
//		@Override
//		public SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> evalPartialDerivativeCached(
//				ArrayList<? extends Elem<?, ?>> withRespectTo,
//				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
//				HashMap<SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>>, SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache)
//				throws NotInvertibleException,
//				MultiplicativeDistributionRequiredException {
//			throw( new RuntimeException( "Not Supported" ) );
//		}
//				
//		
//	}
//	
//	
	
	
	
	
	
	
	
	
	/**
	 * Newton-Raphson evaluator for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class StelemNewton extends NewtonRaphsonSingleElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
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
				SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> _function,
				ArrayList<? extends Elem<?, ?>> _withRespectTo, 
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel,
				final int threadIndex)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			super(_function, _withRespectTo, implicitSpaceFirstLevel, null);
			// System.out.println( "**" );
			// System.out.println( this.partialEval.writeString() );
			threadContext = TestSchrodingerCdimBN.iterationThreadContexts[ threadIndex ];
		}
		
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
				SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> _function,
				ArrayList<? extends Elem<?, ?>> _withRespectTo, 
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			this(_function,_withRespectTo,implicitSpaceFirstLevel,0);
		}
		
		/**
		 * The thread context for the iterations.
		 */
		protected TestSchrodingerCdimBN.IterationThreadContext threadContext;
		
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
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			intCnt = 0;
			return( super.eval(implicitSpaceInitialGuess) );
		}
		
		@Override
		protected void performIterationUpdate( ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> iterationOffset )
		{
			threadContext.performIterationUpdate( iterationOffset );
		}
		
		@Override
		protected void cacheIterationValue()
		{
			threadContext.cacheIterationValue();
		}
		
		@Override
		protected void retrieveIterationValue()
		{
			threadContext.retrieveIterationValue();
		}
		
		/**
		 * Copies an instance for cloneThread();
		 * 
		 * @param in The instance to copy.
		 * @param threadIndex The index of the thread for which to clone.
		 */
		protected StelemNewton( final StelemNewton in , final BigInteger threadIndex )
		{
			super( in , threadIndex );
			final int threadInd = threadIndex.intValue();
			threadContext = TestSchrodingerCdimBN.iterationThreadContexts[ threadInd ];
		}
		
		@Override
		public StelemNewton cloneThread( final BigInteger threadIndex )
		{
			return( new StelemNewton( this , threadIndex ) );
		}
		
		@Override
		public NewtonRaphsonSingleElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cloneThreadCached(
				CloneThreadCache<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache,
				CloneThreadCache<?, ?> cacheImplicit, BigInteger threadIndex) {
			throw( new RuntimeException( "Not Supported" ) );
		}
		
		@Override
		protected boolean evalIterationImproved( ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> lastValue , ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> nextValue )
		{
			final BigFixedPointElem<LrgPrecision> distLast = ( ( lastValue.getRe() ).mult( lastValue.getRe() ) ).add( ( lastValue.getIm() ).mult( lastValue.getIm() ) );
			final BigFixedPointElem<LrgPrecision> distNext = ( ( nextValue.getRe() ).mult( nextValue.getRe() ) ).add( ( nextValue.getIm() ).mult( nextValue.getIm() ) );
			return( distNext.compareTo( distLast ) < 0 );
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
	protected BigFixedPointElem<LrgPrecision> expectationValue( final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> in ) throws Throwable
	{
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> args = 
				new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> conj = 
				in.handleOptionalOp( ComplexElem.ComplexCmd.CONJUGATE_LEFT , args );
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> mult = conj.mult( in );
		
		return( mult.getRe() );
	}
	

	
	
	/**
	 * Initializes the iter array.
	 * 
	 * @param d1 The dimensional size to be used for the initialization.
	 */
	protected void initIterArray( final double d1 ) throws Throwable
	{
		System.out.println( "Setting Initial Conditions..." );
		final TestSchrodingerCdimBN.IterationThreadContext iterContext = iterationThreadContexts[ 0 ];
		final DrFastArray3D_Dbl iterArrayRe = iterContext.iterArrayRe;
		final DrFastArray3D_Dbl iterArrayIm = iterContext.iterArrayIm;
		long atm = System.currentTimeMillis();
		long atm2 = System.currentTimeMillis();
		for( int tcnt = 0 ; tcnt < 2 * NSTPT ; tcnt++ )
		{
			System.out.println( "Initial - " + tcnt );
			for( long acnt = 0 ; acnt < ( (long) NUM_X_ITER ) * NUM_XI_ITER ; acnt++ )
			{
				atm2 = System.currentTimeMillis();
				if( atm2 - atm >= IterConstants.INIT_UPDATE_DELAY )
				{
					System.out.println( ">> " + acnt );
					atm = atm2;
				}
				
				long ac = acnt;
				final int xi = (int)( ac % NUM_XI_ITER );
				ac = ac / NUM_XI_ITER;
				final int x = (int)( ac % NUM_X_ITER );
				final double dx = ( x - HALF_X ) / RAD_X;
				final double dxi = ( xi - HALF_XI ) / RAD_XI;
				if( ( dx * dx < 1.0 ) && ( xi == HALF_XI ) )
				{
					iterArrayRe.set( tcnt , x , xi , 10000.0 * ( d1 * d1 ) );
				}
				else
				{
					iterArrayRe.set( tcnt , x , xi , 0.0 );
				}
				iterArrayIm.set( tcnt , x , xi , 0.0 );
			}
		}
		System.out.println( "Initial Conditions Set..." );
	}
	
	
	
	
	/**
	 * Increments through the discretized space with cache-locality.
	 * 
	 * This documentation should be viewed using Firefox version 33.1.1 or above.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class IncrementManager
	{
		
		/**
		 * The thread context for the iterations.
		 */
		protected TestSchrodingerCdimBN.IterationThreadContext threadContext;
		
		/**
		 * The current discretized X-coordinate.
		 */
		protected int xcnt = 0;
		
		/**
		 * The current discretized Y-coordinate.
		 */
		protected int xicnt = 0;
		
		/**
		 * The XI-coordinate of the start of the swatch.
		 */
		protected int xistrt = 0;
		
		/**
		 * The X-coordinate of the start of the swatch.
		 */
		protected int xstrt = 0;
		
		/**
		 * The number of XI-coordinates to count down to the boundary a standard-size swatch.
		 */
		protected int xidn = XIMULT - 1;
		
		/**
		 * The number of X-coordinates to count down to the boundary a standard-size swatch.
		 */
		protected int xdn = XMULT - 1;
		
		/**
		 * Indicates that the current increment is only in the X-direction.
		 */
		protected boolean xMoveOnly = false;
		
		/**
		 * Indicates that the current increment is only in the XI-direction.
		 */
		protected boolean xiMoveOnly = false;
		
		/**
		 * Indicates whether the XI-Axis increment is up or down.
		 */
		protected boolean xiMoveUp = true;
		
		
		/**
		 * Constructs the increment manager.
		 * 
		 * @param threadIndex The thread index of the increments.
		 */
		public IncrementManager( final int threadIndex )
		{
			threadContext = iterationThreadContexts[ threadIndex ];
		}
		
		
		/**
		 * Returns whether the iterations are done.
		 * @return True iff. the iterations are complete.
		 */
		public boolean isDone()
		{
			return( xstrt >= NUM_X_ITER );
		}
		
		
		/**
		 * Increments to the next swatch.
		 */
		public void handleSwatchIncrementSingle()
		{
			xiMoveUp = true;
			if( ( xistrt + ( XIMULT - 1 ) ) < ( NUM_XI_ITER - 1 ) )
			{
				xicnt = xistrt + XIMULT;
				xistrt = xicnt;
				xidn = XIMULT - 1;
				xcnt = xstrt;
				xdn = XMULT - 1;
				}
				else
				{
					xicnt = 0;
					xistrt = 0;
					xidn = XIMULT - 1;
					xcnt = xstrt + XMULT;
					xstrt = xcnt;
					xdn = XMULT - 1;
				}
		}
		
		
		
		/**
		 * Increments to the next thread-swatch.
		 */
		protected void handleSwatchIncrement()
		{
			for( int cnt = 0 ; cnt < NUM_CPU_CORES ; cnt++ )
			{
				handleSwatchIncrementSingle();
			}
		}
		
		
		
		/**
		 * Handles the increment from the X-Axis.  At the point the increment
		 * reaches a swatch boundary, other axes are potentially incremented.
		 */
		protected void handleIncrementXa()
		{
			
			if( ( xdn > 0 ) && ( xcnt < ( NUM_X_ITER - 1 ) ) )
			{
				xMoveOnly = true;
				xcnt++;
				xdn--;
				xiMoveUp = !xiMoveUp;
				xidn = XIMULT - 1;
			}
			else
			{
				handleSwatchIncrement();
			}
			
		}
		
		
		/**
		 * Handles the base increment from the Y-Axis.  At the point the increment
		 * reaches a swatch boundary, other axes are potentially incremented.
		 */
		public void handleIncrementXIa()
		{
			xiMoveOnly = false;
			xMoveOnly = false;
			if( xiMoveUp )
			{
				if( ( xidn > 0 ) && ( xicnt < ( NUM_XI_ITER - 1 ) ) )
				{
					xiMoveOnly = true;
					xicnt++;
					xidn--;
				}
				else
				{
					handleIncrementXa();
				}
			}
			else
			{
				if( ( xidn > 0 ) && ( xicnt > xistrt ) )
				{
					xiMoveOnly = true;
					xicnt--;
					xidn--;
				}
				else
				{
					handleIncrementXa();
				}
			}
		}
		
		
		
		
		/**
		 * Performs the temp array fill for the most recently calculated increment.  Selects a
		 * cache-efficient algorithm for performing the fill.
		 * 
		 * @param tval The current T-Axis iteration value.
		 * @throws Throwable
		 */
		public void performTempArrayFill( final int tval ) throws Throwable
		{
			if( xiMoveOnly )
			{
				if( xiMoveUp )
				{
					threadContext.fillTempArrayShiftXIup( tval , xcnt , xicnt );
				}
				else
				{
					threadContext.fillTempArrayShiftXIdown( tval , xcnt , xicnt );
				}
			}
			else
			{
				if( xMoveOnly )
				{
					threadContext.fillTempArrayShiftXup( tval , xcnt , xicnt );
				}
				else
				{
					threadContext.fillTempArray( tval , xcnt , xicnt );
				}
			}
		}
		
		
		
		/**
		 * Restarts the inctrements upon a new T-Axis iteration.
		 */
		public void restartIncrements()
		{
			xcnt = 0;
			xicnt = 0;
			xistrt = 0;
			xstrt = 0;
			xidn = XIMULT - 1;
			xdn = XMULT - 1;
			xiMoveUp = true;
			xMoveOnly = false;
			xiMoveOnly = false;
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
		 * Gets the current discretized XI-coordinate.
		 * 
		 * @return The current discretized XI-coordinate.
		 */
		public int getXIcnt() {
			return xicnt;
		}
		

		
	}

	
	/**
	 * Initializes the IncrementManager instances.
	 * 
	 * @return The initialized IncrementManager instances.
	 */
	protected static IncrementManager[] initIncrementManager()
	{
		IncrementManager[] ima = new IncrementManager[ NUM_CPU_CORES ];
		for( int cnt = 0 ; cnt < NUM_CPU_CORES ; cnt++ )
		{
			final IncrementManager im = new IncrementManager( cnt );
			ima[ cnt ] = im;
		}
		return( ima );
	}
	
	
	
	/**
	 * Instances of the IncrementManager used by the performIterationT() method.
	 */
	protected static final IncrementManager[] ims = initIncrementManager();
	
	
	
	/**
	 * Performs descent iterations for one value of T.
	 * 
	 * @param tval The value of T over which to iterate.
	 * @param newtons The descent algorithms to use for the iterations.
	 * @param implicitSpace2 The implicit space over which to iterate.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected void performIterationT( final int tval , final StelemNewton[] newtons , final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 ) 
			throws NotInvertibleException, MultiplicativeDistributionRequiredException, Throwable
	{
		final int numCores = CpuInfo.NUM_CPU_CORES;
		final Runnable[] runn = new Runnable[ numCores ];
		final boolean[] b = CpuInfo.createBool( false );
		for( int ccnt = 0 ; ccnt < NUM_CPU_CORES ; ccnt++ )
		{
			final int core = ccnt;
			final IncrementManager im = ims[ core ];
			final StelemNewton newton = newtons[ core ];
			final TestSchrodingerCdimBN.IterationThreadContext threadContext = iterationThreadContexts[ core ];
			runn[ core ] = new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						//double tmpCorrectionValue = 0.0;
						im.restartIncrements();
						for( int acnt = 0 ; acnt < core ; acnt++ )
						{
							im.handleSwatchIncrementSingle();
						}
						
						long atm = System.currentTimeMillis();
						long atm2 = System.currentTimeMillis();
						while( !( im.isDone() ) )
						{
			
							atm2 = System.currentTimeMillis();
							if( atm2 - atm >= IterConstants.ITER_UPDATE_DELAY )
							{
								System.out.println( ">> " + tval + " / " + im.getXcnt() + " / " + im.getXIcnt() );
								atm = atm2;
							}
			
			
							im.performTempArrayFill( tval );
							threadContext.tempXLocn = im.getXcnt();
							threadContext.tempXILocn = im.getXIcnt();
							threadContext.tempTLocn = tval;
			
			
							threadContext.clearSpatialAssertArray();
	
			
							final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> ival =
									new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(
											threadContext.getUpdateValueRe() , 
											threadContext.getUpdateValueIm() );
			
			
		
			
							ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> err = newton.eval( implicitSpace2 );
							
							if( APPLY_NUMERICAL_VISCOSITY )
							{
								threadContext.applyNumericViscosity();
							}
			
			
							//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
							//{
							//	tmpCorrectionValue = threadContext.getCorrectionValue();
							//	threadContext.applyPredictorCorrector();
							//	
							//
							//	err = newton.eval( implicitSpace2 );
							//
							// if( APPLY_NUMERICAL_VISCOSITY )
							// {
							//	threadContext.applyNumericViscosity();
							// }
							//}
	
	
							final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> val =
									new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(
											threadContext.getUpdateValueRe() , 
											threadContext.getUpdateValueIm() );
									
							
							if( ( im.getXcnt() == HALF_X ) && ( im.getXIcnt() == HALF_XI ) )
							{
								System.out.println( "******************" );
								System.out.println( " ( " + im.getXcnt() + " , " + im.getXIcnt() + " ) " );
								System.out.println( expectationValue( ival ).toDouble() );
								System.out.println( expectationValue( val ).toDouble() );
								System.out.println( "## " + ( expectationValue( err ).toDouble() ) );
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
			
			
							Assert.assertTrue( Math.abs( Math.sqrt( expectationValue( err ).toDouble() ) ) < ( 0.01 * Math.abs( Math.sqrt( expectationValue( val ).toDouble() ) ) + 0.01 ) );
							
							//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
							//{
							//	threadContext.resetCorrectionValue( tmpCorrectionValue );
							//}
		
							threadContext.iterArrayRe.set( tval + NSTPT , im.getXcnt() , im.getXIcnt() , val.getRe().toDouble() );
							threadContext.iterArrayIm.set( tval + NSTPT , im.getXcnt() , im.getXIcnt() , val.getIm().toDouble() );
			
			
			
							im.handleIncrementXIa();
							
						}
					}
					catch( Error ex  ) { ex.printStackTrace( System.out ); }
					catch( Throwable ex ) { ex.printStackTrace( System.out ); }
						
					synchronized( this )
					{
						b[ core ] = true;
						this.notify();
					}			
				}
			};		
		}
		
		CpuInfo.start( runn );
		CpuInfo.wait( runn , b );
		
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
	 */	
	public void testStelemSimple() throws Throwable
	{
		String databaseLocationRe = DatabasePathForTest.FILESPACE_PATH + "mydbRe";
		String databaseLocationIm = DatabasePathForTest.FILESPACE_PATH + "mydbIm";
		
		
		
		final DbFastArray3D_Param dparam = new DbFastArray3D_Param();
		dparam.setTmult( TMULT );
		dparam.setXmult( XMULT );
		dparam.setYmult( XIMULT );
		dparam.setTmax( NUM_T_ITER );
		dparam.setXmax( NUM_X_ITER );
		dparam.setYmax( NUM_XI_ITER );
		
		for( int hcnt = 0 ; hcnt < NUM_CPU_CORES ; hcnt++ )
		{
			TestSchrodingerCdimBN.iterationThreadContexts[ hcnt ].iterArrayRe = new DrFastArray3D_Dbl( dparam , databaseLocationRe );
			TestSchrodingerCdimBN.iterationThreadContexts[ hcnt ].iterArrayIm = new DrFastArray3D_Dbl( dparam , databaseLocationIm );
		}
		
		final Random rand = new Random( 3344 );
		
		final double d1 = Math.sqrt( X_HH.getRe().toDouble() * X_HH.getRe().toDouble() + XI_HH.getRe().toDouble() * XI_HH.getRe().toDouble() );
		
		final TestDimensionTwo tdim = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		
		
		initIterArray( d1 );
		
		
		
		final ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> de2 = new ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( de );
		
		final SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> se = new SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( de2 );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> se2 =
				new SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( se );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>( se2 );
		
		
		final AStelem as = new AStelem( se2 );
		
		final ArrayList<Ordinate> wrtT = new ArrayList<Ordinate>();
		
		wrtT.add( new Ordinate( de2 , TV ) );
		
		// final ArrayList<AElem> wrtX = new ArrayList<AElem>();
		
		// wrtX.add( new AElem( de , 1 ) );
		
		
		final GeometricAlgebraMultivectorElemFactory<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
			ge =
			new GeometricAlgebraMultivectorElemFactory<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>( se3 , tdim , ord );
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
				g0 = new GeometricAlgebraMultivectorElem<
					TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
					SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
					( as , se3 , tdim , ord );
		
		
		final DDirec ddirec = new DDirec(de2, se2);
		
		final DirectionalDerivative<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>, 
			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>, 
			Ordinate>
			del =
			new DirectionalDerivative<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>, 
			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>, 
			Ordinate>( 
					ge , 
					tdim , ord ,
					ddirec );
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
				del0 = del.eval( null );
		
		
		
		final PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,Ordinate> pa0T 
			= new PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
					SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,Ordinate>( se2 , wrtT );
		
		// final PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
		//	SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,AElem> pa0X 
		//	= new PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
		//			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,AElem>( se2 , wrtX );
	
		
		final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> m0T
			= pa0T.mult( as ); 
		
		// SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
		//	SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> m0X
		//	= pa0X.mult( as ); 
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
				gxx0 = del0.mult( g0 );
		
		final ArrayList<GeometricAlgebraMultivectorElem<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>> args0 
				= new ArrayList<GeometricAlgebraMultivectorElem<
					TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
					SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>>();
		args0.add( gxx0 );
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
				gxx1 = del0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , args0 );
		
		
		//SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
		//	SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> m1X
		//	= pa0X.mult( m0X );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> gtt0
			= m0T.mult( 
					( new StelemReduction3L( new StelemReduction2L( SymbolicConstCache.get( II.mult( HBAR ) , de2 ) , se ) , se2 )
							) ).negate();
		
		
		// SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
		//	SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> m1
		//	= m1X.add( gtt0 );
		
		final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> vslope0
			= as.mult( new AVtelem( se2 ) );
	
	
//		final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
//			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> vslope1
//			= new AAtelem( se2 );
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
				gtt = new GeometricAlgebraMultivectorElem<
					TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
					SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
					( gtt0 , se3 , tdim , ord );
		
//		final GeometricAlgebraMultivectorElem<
//			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
//			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
//			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
//				vslopeV = new GeometricAlgebraMultivectorElem<
//					TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
//					SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
//					SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
//					( vslope0.add( vslope1 ) , se3 , tdim , ord );
		
		final GeometricAlgebraMultivectorElem<
		TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
		SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
		SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
			vslopeV = new GeometricAlgebraMultivectorElem<
				TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
				SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
				SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
				( vslope0 , se3 , tdim , ord );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> gxxMult
			=  
				( new StelemReduction3L( new StelemReduction2L( SymbolicConstCache.get( HBAR.mult( HBAR 
							).mult( ( MM.mult( genFromConst( 2.0 ) ) ).invertLeft() ).negate() , de2 ) , se ) , se2 )
						);
		
		
		final GeometricAlgebraMultivectorElem<
		TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
		SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
		SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
			gxxMultV = new GeometricAlgebraMultivectorElem<
					TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
					SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>( gxxMult , se3 , tdim , ord );
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>, 
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>>>
				mg1 = ( gxx1.mult( gxxMultV ) ).add( vslopeV ).add( gtt );
		
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> m1
			= mg1.get( new HashSet<BigInteger>() );
		
		
		
		
		
		
		
		final HashMap<Ordinate,Ordinate> implicitSpace0 = new HashMap<Ordinate,Ordinate>();
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 = implicitSpace0;
		
		implicitSpace0.put( new Ordinate( de2 , TV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , XV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , XIV ) , new Ordinate( de2 , 0 ) );
		
		final SymbolicElem<
			SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> s0 = m1.eval( implicitSpace2 );
		
		// String s = s0.writeString();
		
		// System.out.println( s );
		
		
		final ArrayList<Elem<?, ?>> wrt3 = new ArrayList<Elem<?, ?>>();
		{
			final HashMap<Ordinate, BigInteger> coord = new HashMap<Ordinate, BigInteger>();
			coord.put( new Ordinate( de2 , TV ) , BigInteger.valueOf( NSTPT ) );
			coord.put( new Ordinate( de2 , XV ) , BigInteger.valueOf( 0 ) );
			coord.put( new Ordinate( de2 , XIV ) , BigInteger.valueOf( 0 ) );
			wrt3.add( new CNelem( se , coord ) );
		}
		
		
		
		final StelemNewton newton0 = new StelemNewton( s0 , wrt3 , implicitSpace2 );
		final StelemNewton[] newtons = new StelemNewton[ NUM_CPU_CORES ];
		newtons[ 0 ] = newton0;
		for( int hcnt = 1 ; hcnt < NUM_CPU_CORES ; hcnt++ )
		{
			newtons[ hcnt ] = newton0.cloneThread( BigInteger.valueOf( hcnt ) );
		}
		
		
		for( int tval = 1 ; tval < ( NUM_T_ITER - NSTPT ) ; tval++ )
		{
			performIterationT( tval , newtons , implicitSpace2 );	
		}
		
		
		// System.out.println( "==============================" ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// System.out.println( TestSchrodingerCdimBN.iterationThreadContexts[ 0 ].iterArray[ NUM_T_ITER - 1 ][ HALF-X ][ HALF_Y ][ HALF_Z ] ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// Assert.assertTrue( Math.abs( val - ( -1.450868 ) ) < 0.01 ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		
		for( int hcnt = 0 ; hcnt < NUM_CPU_CORES ; hcnt++ )
		{
			TestSchrodingerCdimBN.iterationThreadContexts[ hcnt ].iterArrayRe.close();
			TestSchrodingerCdimBN.iterationThreadContexts[ hcnt ].iterArrayRe.close();
		}
		
		
		
	}
	

	
}


