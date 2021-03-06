




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
import simplealgebra.AbstractCache;
import simplealgebra.CloneThreadCache;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteElemCache.IntVal;
import simplealgebra.algo.*;
import simplealgebra.ddx.DirectionalDerivative;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.ddx.*;
import simplealgebra.ga.*;
import simplealgebra.et.*;









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
 * </math> is the Einstein tensor in 5-D with a compactified 5th dimension "W".
 * 
 * The compactification is a 1-dimensional circle as used in
 * Kaluza-Klein theory.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * TODO: Implement the modified metric tensor terms in the Kaluza-Klein theory.
 * 
 * 
 * See http://en.wikipedia.org/wiki/Einstein_tensor
 *
 * For details on compactification and Kaluza-Klein see http://mafija.fmf.uni-lj.si/seminar/files/2011_2012/KaluzaKlein_theory.pdf
 *
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestGeneralRelativityA_5D_Cd extends TestCase {
	
	
	/**
	 * DoubleElem representing the constant zero.
	 */
	private static final DoubleElem DOUBLE_ZERO = new DoubleElem( 0.0 );
	
	/**
	 * DoubleElem representing the constant 2.
	 */
	private static final DoubleElem MM = genFromConstDbl( 2.0 );
	
	/**
	 * The number of elements in the rank two tensor.
	 */
	private static final int SQ_TENSOR_SZ = ( TestDimensionFive.FIVE ) * ( TestDimensionFive.FIVE );
	
	/**
	 * Random number multiplier size.
	 */
	private static final double RAND_SIZE = 1E-19;
	
	
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
		for( int acnt = 0 ; acnt < SQ_TENSOR_SZ ; acnt++ )
		{
			if( ( acnt % TestDimensionFive.FIVE ) == ( acnt / TestDimensionFive.FIVE ) )
			{
				// final DoubleElem dd = acnt == 0 ? 
				//		new DoubleElem( - ( C.getVal() ) * ( C.getVal() ) ) : new DoubleElem( 1.0 );
				final DoubleElem dd = acnt == 0 ? new DoubleElem( - CV * CV * ( 1.0 + ( RAND_SIZE * rand.nextDouble() ) ) )
					: new DoubleElem( 1.0 + ( RAND_SIZE * rand.nextDouble() ) );
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt / TestDimensionFive.FIVE ) );
				ab.add( BigInteger.valueOf( acnt % TestDimensionFive.FIVE ) );
				ret.setVal( ab , dd );
			}
			else
			{
				final DoubleElem dd = new DoubleElem( RAND_SIZE * rand.nextDouble() );
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt / TestDimensionFive.FIVE ) );
				ab.add( BigInteger.valueOf( acnt % TestDimensionFive.FIVE ) );
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
		for( int acnt = 0 ; acnt < SQ_TENSOR_SZ ; acnt++ )
		{
			if( ( acnt % TestDimensionFive.FIVE ) == ( acnt / TestDimensionFive.FIVE ) )
			{
				final DoubleElem dd = acnt == 0 ? new DoubleElem( - CV * CV * ( 1.0 + ( 0.001 * rand.nextDouble() ) ) )
					: new DoubleElem( 1.0 + ( 0.001 * rand.nextDouble() ) );
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt / TestDimensionFive.FIVE ) );
				ab.add( BigInteger.valueOf( acnt % TestDimensionFive.FIVE ) );
				ret.setVal( ab , dd );
			}
			else
			{
				final DoubleElem dd = new DoubleElem( RAND_SIZE * rand.nextDouble() );
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt / TestDimensionFive.FIVE ) );
				ab.add( BigInteger.valueOf( acnt % TestDimensionFive.FIVE ) );
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
	 * The total measurement size along the W-Axis.
	 * This dimension is smaller because it is a simulated compactified dimension.
	 */
	protected static final double TOTAL_W_AXIS_SIZE = 0.05;
	
	
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
	 * The number of discretizations on the W-Axis over which to iterate.
	 * This dimension is smaller because it is a simulated compactified dimension.
	 */
	protected static final int NUM_W_ITER = 6;
	
	
	
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
	 * Size of the W-Axis discretization.
	 */
	protected static final DoubleElem W_HH = genFromConstDbl( TOTAL_W_AXIS_SIZE / NUM_W_ITER /* 0.01 */ );
	
	/**
	 * Discretization sizes arrayed by coordinate index.
	 */
	protected static final DoubleElem[] HH = { T_HH , X_HH , Y_HH , Z_HH , W_HH };
	
	
	
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
	 * The halfway iteration point in W.
	 */
	protected static final int HALF_W = NUM_W_ITER / 2;
	
	
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
	 * The initial condition radius in W.
	 */
	protected static final double RAD_W = NUM_W_ITER / 10.0;
	
	
	
	
	
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
	 * Temp step size in the W-direction.
	 */
	protected static final int NSTPW = 2;
	

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
	 * Result array over which to iterate.
	 */
	protected static final EinsteinTensorElem[][][][][] iterArray = new EinsteinTensorElem[ NUM_T_ITER ][ NUM_X_ITER ][ NUM_Y_ITER ][ NUM_Z_ITER ][ NUM_W_ITER ];
	
	
	
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
	 * Discretized index for the W-Axis.
	 */
	protected static final int WV = 4;
	
	
	
	/**
	 * Temporary array in which to generate descent algorithm solutions.
	 * <p>0 = T
	 * <p>1 = X
	 * <p>2 = Y
	 * <p>3 = Z
	 * <p>4 = W
	 */
	private static EinsteinTensorElem[][][][][] tempArray = new EinsteinTensorElem[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPY * 2 + 1 ][ NSTPZ * 2 + 1 ][ NSTPW * 2 + 1 ];
	
	
	
	
	
	/**
	 * Given a change calculated by a descent algorithm iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void performIterationUpdate( EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> dbl )
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] );
		if( va == null )
		{
			tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] = dbl;
			return;
		}
		tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] = va.add( dbl );
	}
	
	
	/**
	 * Given a change calculated by a descent algorithm iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void setIterationValue( EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> dbl )
	{
		tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] = dbl;
	}
	
	
	/**
	 * The iteration cache value.
	 */
	protected static EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> iterationValueCache = null;
	
	
	/**
	 * Places the current iteration value in the cache.
	 */
	protected static void cacheIterationValue()
	{
		iterationValueCache = 
			(EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] );
	}
	
	
	/**
	 * Sets the current iteration value to the value in the cache.
	 */
	protected static void retrieveIterationValue()
	{
		tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] = iterationValueCache;
	}
	
	
	
	/**
	 * Returns the result of the descent algorithm iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> getUpdateValue()
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] );
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
	protected static EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> getCorrectionValue()
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> va
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] );
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
	protected static void resetCorrectionValue( final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> in )
	{
		tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] = in;
	}
	
	
	/**
	 * Approximate maximum change allowed by nonlinear viscosity.
	 */
	final static double MAX_CHG = 0.05;
	
	/**
	 * Multiplicative inverse of MAX_CHG.
	 */
	final static double I_MAX_CHG = 1.0 / MAX_CHG;
	
	/**
	 * Size of change below which numerical viscosity isn't applied.
	 */
	final static double NUMERICAL_VISCOSITY_EXIT_CUTOFF = 1E-5;
	
	
	
	
	
	/**
	 * Applies a form of nonlinear numerical viscosity.
	 */
	protected static DoubleElem applyNumericViscosityVal( ArrayList<BigInteger> index )
	{
		final double delt = ( (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] ) ).getVal( index ).getVal()
			- ( (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] ) ).getVal( index ).getVal();
		final double adelt = Math.abs( delt );
		if( adelt < NUMERICAL_VISCOSITY_EXIT_CUTOFF )
		{
			return( ( (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] ) ).getVal( index ) );
		}
		final double iadelt = 1.0 / adelt;
		final double iadiv = Math.sqrt( iadelt * iadelt + I_MAX_CHG * I_MAX_CHG );
		final double adiv = 1.0 / iadiv;
		return( new DoubleElem(
				( (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] ) ).getVal( index ).getVal() +
				( delt > 0.0 ? adiv : -adiv ) ) );
	}
	
	
	
	/**
	 * Applies a form of nonlinear numerical viscosity.
	 */
	protected static void applyNumericViscosity()
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> vam
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] );
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> vl = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>(
				vam.getFac().getFac(), 
				vam.getContravariantIndices(), vam.getCovariantIndices());
		for( ArrayList<BigInteger> index : vam.getKeySet() )
		{
			vl.setVal( index , applyNumericViscosityVal( index ) );
		}
		
		tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] = vl;
		
	}
	
	
	
	
	
	/**
	 * Applies a predictor-corrector process to the temp array.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	protected static void applyPredictorCorrector()
	{
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> vam2
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 - 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] );
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> vam1
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] );
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> vam
			= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] );
		if( vam2 == null )
		{
			DoubleElemFactory da = new DoubleElemFactory();
			final ArrayList<String> contravariantIndices = new ArrayList<String>();
			final ArrayList<String> covariantIndices = new ArrayList<String>();
			covariantIndices.add( "u" );
			covariantIndices.add( "v" );
			vam2 = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( da , contravariantIndices  , covariantIndices );
		}
		if( vam1 == null )
		{
			DoubleElemFactory da = new DoubleElemFactory();
			final ArrayList<String> contravariantIndices = new ArrayList<String>();
			final ArrayList<String> covariantIndices = new ArrayList<String>();
			covariantIndices.add( "u" );
			covariantIndices.add( "v" );
			vam1 = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( da , contravariantIndices  , covariantIndices );
		}
		if( vam == null )
		{
			DoubleElemFactory da = new DoubleElemFactory();
			final ArrayList<String> contravariantIndices = new ArrayList<String>();
			final ArrayList<String> covariantIndices = new ArrayList<String>();
			covariantIndices.add( "u" );
			covariantIndices.add( "v" );
			vam = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( da , contravariantIndices  , covariantIndices );
		}
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> slopePrev 
			= vam1.add( vam2.negate() );
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> slopeNew 
			= vam.add( vam1.negate() );
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> avgSlope 
			= ( slopePrev.add( slopeNew ) ).divideBy( BigInteger.valueOf( 2 ) );
		tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ][ NSTPW ] = 
				vam2.add( avgSlope );
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
		 * Gets the W-Axis index for the center in the iter array.
		 * 
		 * @return The W-Axis index for the center in the iter array.
		 */
		public int getWcnt() {
			return wcnt;
		}


		/**
		 * Sets the W-Axis index for the center in the iter array.
		 * 
		 * @param wcnt The W-Axis index for the center in the iter array.
		 */
		public void setWcnt(int wcnt) {
			this.wcnt = wcnt;
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
		 * Gets the W-Axis iteration of the array fill.
		 * 
		 * @return The W-Axis iteration of the array fill.
		 */
		public int getWa() {
			return wa;
		}


		/**
		 * Sets the W-Axis iteration of the array fill.
		 * 
		 * @param wa The W-Axis iteration of the array fill.
		 */
		public void setWa(int wa) {
			this.wa = wa;
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
		 * The W-Axis index for the center in the iter array.
		 */
		protected int wcnt;
		
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
		
		/**
		 * The W-Axis iteration of the array fill.
		 */
		protected int wa;
		
	
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
		final int wcnt = param.getWcnt();
		
		final int ta = param.getTa();
		final int xa = param.getXa();
		final int ya = param.getYa();
		final int za = param.getZa();
		final int wa = param.getWa();
		
		final int tv = tcnt + ta;
		final int xv = xcnt + xa;
		final int yv = ycnt + ya;
		final int zv = zcnt + za;
		int wv = wcnt + wa;
		
		// Wrap compactified dimension in circle by iterating around NUM_W_ITER.
		while( wv < 0 )
		{
			wv += NUM_W_ITER;
		}
		// Wrap compactified dimension in circle by iterating around NUM_W_ITER.
		while( wv >= NUM_W_ITER )
		{
			wv -= NUM_W_ITER;
		}
		
		EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> av = null;
		if( ( tv >= 0 )  && ( xv >= 0 ) && ( yv >= 0 ) && ( zv >= 0 ) &&
			( tv < NUM_T_ITER ) && ( xv < NUM_X_ITER ) && ( yv < NUM_Y_ITER ) && ( zv < NUM_Z_ITER ) )
		{
//			if( ta != NSTPT )
//			{
				av = iterArray[ tv ][ xv ][ yv ][ zv ][ wv ];
//			}
//			else
//			{
//				if( ( xa == 0 ) && ( ya == 0 ) && ( za == 0 ) && ( wa == 0 ) )
//				{
//					av = iterArray[ tv ][ xv ][ yv ][ zv ][ wv ];
//				}
//			}
		}
		if( av == null )
		{
			av = genDiffAll();
		}
		tempArray[ ta + NSTPT ][ xa + NSTPX ][ ya + NSTPY ][ za + NSTPZ ][ wa + NSTPW ] = av;
		
	}
	
	
	
	
	/**
	 * Fills the temp array with elements from the iter array.
	 * 
	 * @param tcnt The T-Axis index for the center in the iter array.
	 * @param xcnt The X-Axis index for the center in the iter array.
	 * @param ycnt The Y-Axis index for the center in the iter array.
	 * @param zcnt The Z-Axis index for the center in the iter array.
	 * @param wcnt The W-Axis index for the center in the iter array.
	 */
	protected static void fillTempArray( final int tcnt , final int xcnt , final int ycnt , final int zcnt , final int wcnt )
	{
		final TempArrayFillInnerParam param = new TempArrayFillInnerParam();
		
		param.setTcnt( tcnt );
		param.setXcnt( xcnt );
		param.setYcnt( ycnt );
		param.setZcnt( zcnt );
		param.setWcnt( wcnt );
		
		for( int ta = -NSTPT ; ta < NSTPT ; ta++ )
		{
			param.setTa( ta );
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				param.setXa( xa );
				for( int ya = -NSTPY ; ya < NSTPY + 1 ; ya++ )
				{
					param.setYa( ya );
					for( int za = -NSTPZ ; za < NSTPZ + 1 ; za++ )
					{
						param.setZa( za );
						for( int wa = -NSTPW ; wa < NSTPW + 1 ; wa++ )
						{
							param.setWa( wa );
							fillTempArrayInner( param );
						}
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
					for( int wa = 0 ; wa < NSTPW * 2 + 1 ; wa++ )
					{
						final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> vam
							= (EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>)( 
									tempArray[ NSTPT * 2 - 1 ][ xa ][ ya ][ za ][ wa ] );
						final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> vl = 
							new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>(
									vam.getFac().getFac(), 
									vam.getContravariantIndices(), vam.getCovariantIndices());
						for( final Entry<ArrayList<BigInteger>, DoubleElem> i : vam.getEntrySet() )
						{
							vl.setVal( i.getKey() , i.getValue() );
						}
						tempArray[ NSTPT * 2 ][ xa ][ ya ][ za ][wa ] = vl;
					}
				}
			}
		}
	}
	
	
	
	
	/**
	 * Test array used to verify that the entire temp array has been filled.
	 */
	private static int[][][][][] spatialAssertArray = new int[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPY * 2 + 1 ][ NSTPZ * 2 + 1 ][ NSTPW * 2 + 1 ];
	
	
	
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
						for( int wa = -NSTPW ; wa < NSTPW + 1 ; wa++ )
						{
							spatialAssertArray[ ta + NSTPT ][ xa + NSTPX ][ ya + NSTPY ][ za + NSTPZ ][ wa + NSTPW ] = 0;
						}
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
	for( final DoubleElem dbl : in.getValueSet() )
	{
		final double vl = dbl.getVal();
		// System.out.print( elem + " " + vl + " " );
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
	for( final ArrayList<BigInteger> elem : a.getKeySet() )
	{
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
	for( final ArrayList<BigInteger> elem : b.getKeySet() )
	{
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
	protected static HashMap<Double,SymbolicConst> map = new HashMap<Double,SymbolicConst>();
	
	/**
	 * Returns a cached SymbolicConst representing a DoubleElem.
	 * 
	 * @param in The DoubleElem to be represented.
	 * @param _fac The factory for DoubleElem instances.
	 * @return The cached SymbolicConst.
	 */
	public static SymbolicConst get(  DoubleElem in , DoubleElemFactory _fac )
	{
		SymbolicConst cnst = map.get( in.getVal() );
		if( cnst == null )
		{
			cnst = new SymbolicConst( in , _fac );
			map.put( in.getVal() , cnst );
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
	public DDirec( 
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
					cmul = ( new StelemReduction3L( new StelemReduction2L( SymbolicConstCache.get( 
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
private static class Ordinate extends SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>
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
	public EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
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
	public EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> cache,
			PrintStream ps) {
		String st = cache.get( this );
		if( st == null )
		{
			final String sta = fac.writeDesc( (WriteElemCache<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>,EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
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
private static class SymbolicConst extends SymbolicReduction<DoubleElem,DoubleElemFactory>
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
private static class StelemReduction2L extends SymbolicReduction<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
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
private static class StelemReduction3L extends SymbolicReduction<
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
private static class CoeffNode
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
	private static class BNelem extends Nelem<DoubleElem,DoubleElemFactory,Ordinate>
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
		public BNelem(DoubleElemFactory _fac, HashMap<Ordinate, BigInteger> _coord , ArrayList<BigInteger> _index ) {
			super(_fac, _coord);
			index = _index;
		}
		
		
		/**
		 * Column indices in the discretized space.
		 */
		protected final int[] cols = new int[ 5 ];
		
		/**
		 * Assertion booleans used to verify that all
		 * column indices have been initialized.
		 */
		protected final boolean[] assertCols = new boolean[ 5 ];
		

		@Override
		public DoubleElem eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			cols[ 0 ] = 0;
			cols[ 1 ] = 0;
			cols[ 2 ] = 0;
			cols[ 3 ] = 0;
			cols[ 4 ] = 0;
			assertCols[ 0 ] = false;
			assertCols[ 1 ] = false;
			assertCols[ 2 ] = false;
			assertCols[ 3 ] = false;
			assertCols[ 4 ] = false;
			Assert.assertTrue( coord.keySet().size() == 5 );
			for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
			{
				Ordinate keyCoord = ii.getKey();
				BigInteger coordVal = ii.getValue();
				final int offset =  keyCoord.getCol() == 4 ? NSTPW : keyCoord.getCol() == 3 ? NSTPZ : keyCoord.getCol() == 2 ? NSTPY : keyCoord.getCol() == 1 ? NSTPX : NSTPT;
				cols[ keyCoord.getCol() ] = coordVal.intValue() + offset;
				assertCols[ keyCoord.getCol() ] = true;
			}
			( spatialAssertArray[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ][ cols[ 4 ] ] )++;
			Assert.assertTrue( assertCols[ 0 ] );
			Assert.assertTrue( assertCols[ 1 ] );
			Assert.assertTrue( assertCols[ 2 ] );
			Assert.assertTrue( assertCols[ 3 ] );
			Assert.assertTrue( assertCols[ 4 ] );
			DoubleElem ret = null;
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> val = 
					TestGeneralRelativityA_5D_Cd.tempArray[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ][ cols[ 4 ] ];
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
		public DoubleElem evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem, DoubleElemFactory>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				final String strr = cache.getIncrementVal();
				ps.print( "final HashMap<Ordinate, BigInteger> " );
				ps.print( strr );
				ps.println( " = new HashMap<Ordinate, BigInteger>();" );
				final String stri = cache.getIncrementVal();
				ps.print( "final ArrayList<BigInteger> " );
				ps.print( stri );
				ps.println( " = new ArrayList<BigInteger>();" );
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
				for( BigInteger ii : index )
				{
					final String stai = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( ii , ps );
					ps.print( stri );
					ps.print( ".add( " );
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
				ps.print( " , " );
				ps.print( stri );
				ps.println( " );" );
			}
			return( st );
		}
		
		/**
		 * Compares the index in this node to the index in the input node.
		 * @param in The node with the index to compare.
		 * @return True iff. the indices are equal, false otherwise.
		 */
		protected boolean symbolicCompareIndex( final BNelem in )
		{
			return( index.equals( in.index ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
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
				return( symbolicCompareIndex( bn ) );
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
 * of one component of the tensor constrained by the differential equation.
 * The partial derivatives of this elem generate
 * the slopes for producing descent algorithm iterations (e.g. the Jacobian slopes),
 * as opposed to partial derivatives for the underlying differential equation.
 * 
 * @author thorngreen
 *
 */	
private static class CNelem extends Nelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
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
	public CNelem(SymbolicElemFactory<DoubleElem,DoubleElemFactory> _fac, 
			HashMap<Ordinate, BigInteger> _coord , ArrayList<BigInteger> _index ) {
		super(_fac, _coord);
		index = _index;
	}

	@Override
	public SymbolicElem<DoubleElem,DoubleElemFactory> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		return( new BNelem( fac.getFac() , coord , index ) );
	}
	
	
	@Override
	public SymbolicElem<DoubleElem, DoubleElemFactory> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> key =
				new SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>( this , implicitSpace );
		final SymbolicElem<DoubleElem, DoubleElemFactory> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final SymbolicElem<DoubleElem, DoubleElemFactory> ret = eval( implicitSpace );
		cache.put(key, ret);
		return( ret );
	}
	
	
	@Override
	public SymbolicElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
		if( withRespectTo.size() > 1 )
		{
			return( fac.zero() );
		}
		Iterator<? extends Elem<?,?>> it = withRespectTo.iterator();
		CNelem wrt = (CNelem)( it.next() );
		// final boolean cond = this.symbolicDEval( wrt );
		final boolean cond = this.symbolicEquals( wrt );
		return( cond ? fac.identity() : fac.zero() );
	}
	
	
	@Override
	public SymbolicElem<DoubleElem,DoubleElemFactory> evalPartialDerivativeCached(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace,
			HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> cache ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
		final SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> key =
				new SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>( this , implicitSpace , withRespectTo );
		final SymbolicElem<DoubleElem, DoubleElemFactory> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final SymbolicElem<DoubleElem, DoubleElemFactory> ret = evalPartialDerivative( withRespectTo , implicitSpace );
		cache.put(key, ret);
		return( ret );
	}
	

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> cache,
			PrintStream ps) {
		String st = cache.get( this );
		if( st == null )
		{
			final String sta = fac.writeDesc( (WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
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
//          for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
//			{
//				Ordinate key = ii.getKey();
//				BigInteger ka = ii.getValue();
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
	
	
	/**
	 * Compares the index in this node to the index in the input node.
	 * @param in The node with the index to compare.
	 * @return True iff. the indices are equal, false otherwise.
	 */
	protected boolean symbolicCompareIndex( final CNelem in )
	{
		return( index.equals( in.index ) );
	}
	
	
	@Override
	public boolean symbolicEquals( 
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof CNelem )
		{
			CNelem bn = (CNelem) b;
			if( !( index.equals( bn.index ) ) )
			{
				return( false );
			}
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
			return( symbolicCompareIndex( bn ) );
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
private static class AStelem extends Stelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
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
public AStelem( SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac,
		ArrayList<BigInteger> _index ) {
	super(_fac);
	index = _index;
}

@Override
public AStelem cloneInstance() {
	AStelem cl = new AStelem( fac , index );
	for( Entry<Ordinate,BigInteger> ii : partialMap.entrySet() )
	{
		Ordinate key = ii.getKey();
		cl.partialMap.put(key, ii.getValue() );
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
		for( final Entry<HashMap<Ordinate, BigInteger>, CoeffNode> ii : spacesA.entrySet() )
		{
			HashMap<Ordinate, BigInteger> spaceAe = ii.getKey();
			CoeffNode coeff = ii.getValue();
			final CNelem an0 = 
					new CNelem( fac.getFac() , spaceAe , index );
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
public SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalCached(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	final SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> key =
			new SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>( this , implicitSpace );
	final SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> iret = cache.get( key );
	if( iret != null )
	{
		return( iret );
	}
	final SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> ret =
			eval( implicitSpace );
	cache.put(key, ret);
	return( ret );
}


@Override
public String writeDesc(
		WriteElemCache<SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> cache,
		PrintStream ps) {
	String st = cache.get( this );
	if( st == null )
	{
		cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
		final String sta = fac.writeDesc( (WriteElemCache< SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> , SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> >)( cache.getInnerCache() ) , ps);
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
		for( final Entry<Ordinate,BigInteger> ii : partialMap.entrySet() )
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
		for( final BigInteger ii : index )
		{
			final String stav = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( ii , ps );
			ps.print( st );
			ps.print( ".index.add( " );
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
		Ordinate node , DoubleElem hh ,
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
			if( ae.getCol() != TV )
			{
				final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
				final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
				implicitSpaceOutM1.put( ae , valAeM1 );
				implicitSpaceOutP1.put( ae , valAeP1 );
			}
			else
			{
				final BigInteger valAeM1 = valAe;
				final BigInteger valAeP1 = valAe.add( BigInteger.valueOf( 2 ) );
				implicitSpaceOutM1.put( ae , valAeM1 );
				implicitSpaceOutP1.put( ae , valAeP1 );
			}
		}
		else
		{
			implicitSpaceOutM1.put( ae , valAe );
			implicitSpaceOutP1.put( ae , valAe );
		}
	}
	
	final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( genFromConstDbl( 2.0 ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( genFromConstDbl( 2.0 ) ), hh.getFac() ) ) );
	
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
	
	for( final Entry<Ordinate,BigInteger> ii : implicitSpace.entrySet() )
	{
		Ordinate ae = ii.getKey();
		final BigInteger valAe = ii.getValue();
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
			coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh ).mult( genFromConstDbl( 4.0 ) ) , hh.getFac() ) ) );
	final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh ).mult( genFromConstDbl( 2.0 ) ) , hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh ).mult( genFromConstDbl( 4.0 ) ) , hh.getFac() ) ) );
	
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
	
	final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().mult( SymbolicConstCache.get( genFromConstDbl( 2.0 ) , hh.getFac() ) ) , 
			coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( SymbolicConstCache.get( genFromConstDbl( 2.0 ) , hh.getFac() ) ) , 
			coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	
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
	 * descent algorithm evaluator for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class StelemDescent extends DescentAlgorithmMultiElemRemapTensor<String,DoubleElem,DoubleElemFactory>
	// protected static class StelemDescent extends DescentAlgorithmMultiElemRemapTensorDiag<String,DoubleElem,DoubleElemFactory> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	{

		
		/**
		 * Constructs the evaluator.
		 * 
		 * @param param Input parameters for the remap.
		 * @param cache Cache to be used for symbolic evals if useCachedEval() returns true.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		public StelemDescent(
				final DescentAlgorithmMultiElemRemapTensorParam<String,DoubleElem,DoubleElemFactory> param,
				final HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> cache )
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			super( param , cache );
			
			// System.out.println( "**" );
			// System.out.println( this.partialEval.writeString() );
		}

		/**
		 * The iteration count for descent algorithm iterations.
		 */
		protected int intCnt = 0;

		@Override
		protected boolean iterationsDone() {
			intCnt++;
			return( intCnt > 20 );
		}
		
		@Override
		public EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			intCnt = 0;
			
			// System.out.println( this.partialEval.writeString() ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			return( super.eval(implicitSpaceInitialGuess) );
		}
		
		@Override
		protected void performIterationUpdate( EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> iterationOffset )
		{
			TestGeneralRelativityA_5D_Cd.performIterationUpdate( iterationOffset );
		}
		
		@Override
		protected void setIterationValue( EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> iterationOffset )
		{
			TestGeneralRelativityA_5D_Cd.setIterationValue( iterationOffset );
		}
		
		@Override
		protected void cacheIterationValue()
		{
			TestGeneralRelativityA_5D_Cd.cacheIterationValue();
		}
		
		@Override
		protected void retrieveIterationValue()
		{
			TestGeneralRelativityA_5D_Cd.retrieveIterationValue();
		}
		
		@Override
		protected EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> getIterationValue( )
		{
			return( TestGeneralRelativityA_5D_Cd.getUpdateValue( ) );
		}
		
		
		
		/**
		 * Returns whether convergence-wise the new function value should be accepted as an improvement over the old function value.
		 * 
		 * @param lastValue The old function value.
		 * @param nextValue The new function value.
		 * @return True iff. the new function value should be accepted as an improvement over the old function value.
		 */
		protected boolean evalIterationImproved(
				GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim>, DoubleElem, DoubleElemFactory> lastValue,
				GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim>, DoubleElem, DoubleElemFactory> nextValue ) 
		{
			DoubleElem lastTotal = new DoubleElem( 0.0 );
			DoubleElem nextTotal = new DoubleElem( 0.0 );
			
			for( final DoubleElem v : lastValue.getValueSet() )
			{
				lastTotal = lastTotal.add( v.mult( v ) );
			}
			
			for( final DoubleElem v : nextValue.getValueSet() )
			{
				nextTotal = nextTotal.add( v.mult( v ) );
			}
			
			final double ntv = nextTotal.getVal();
			final double ptv = lastTotal.getVal();
			// System.out.println( ( ntv - ptv ) + " --- " + ntv + " --- " + ptv );
			// if( ntv < ptv )
			// {
			//	return( true );
			// }
			// if( ( ntv - ptv ) < ( 1E-7 * ptv ) )
			// {
			//	return( true );
			// }
			// return( false );
			return( ntv < ptv );
		}
		
		
		@Override
		public SimplificationType useSimplification()
		{
			return( SimplificationType.NONE );
		}
		
		@Override
		public boolean useCachedEval()
		{
			return( true );
		}
		
		@Override
		protected int getMaxIterationsBacktrack()
		{
			return( 400 );
		}
	
		
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		@Override
//		protected EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> 
//			evalPartialDerivative() throws NotInvertibleException, MultiplicativeDistributionRequiredException
//		{
//			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>
//				ev = super.evalPartialDerivative();
//			for( int acnt = 0 ; acnt < SQ_TENSOR_SZ ; acnt++ )
//			{
//				final DoubleElem dd = new DoubleElem( RAND_SIZE * ( 2.0 * rand.nextDouble() ) );
//				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
//				if( ( acnt & 1 ) != 0 ) hs.add( BigInteger.ZERO );
//				if( ( acnt & 2 ) != 0 ) hs.add( BigInteger.ONE );
//				if( ( acnt & 4 ) != 0 ) hs.add( BigInteger.valueOf( 2 ) );
//				if( ( acnt & 8 ) != 0 ) hs.add( BigInteger.valueOf( 3 ) );
//				DoubleElem dda = ev.getVal( hs );
//				ev.setVal( hs , dd.add( dda ) );
//			}
//			return( ev );
//		}
//		
		
		/**
		 * The internal multivariate descent algorithm.
		 * 
		 * @author thorngreen
		 *
		 */
		protected class StelemDescentEnt extends DescentAlgorithmMultiElemInputParamCallbacks<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, DoubleElem, DoubleElemFactory>
		{

			@Override
			protected void performIterationUpdate(
					GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim>, DoubleElem, DoubleElemFactory> iterationOffset) {
				StelemDescent.this.performIterationUpdateInternal( iterationOffset );
			}
			
			@Override
			protected void setIterationValue(
					GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim>, DoubleElem, DoubleElemFactory> iterationOffset) {
				StelemDescent.this.setIterationValueInternal( iterationOffset );
			}
			
			@Override
			protected void cacheIterationValue() {
				StelemDescent.this.cacheIterationValue();
			}
			
			@Override
			protected void retrieveIterationValue() {
				StelemDescent.this.retrieveIterationValue();
			}
			
			@Override
			protected GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim>, DoubleElem, DoubleElemFactory> getIterationValue() 
			{
				return( StelemDescent.this.getIterationValueInternal( ) );
			}

			@Override
			protected boolean iterationsDone() {
				return( StelemDescent.this.iterationsDone() );
			}
			
			@Override
			protected SimplificationType useSimplification()
			{
				return( StelemDescent.this.useSimplification() );
			}
			
			@Override
			protected boolean useCachedEval()
			{
				return( StelemDescent.this.useCachedEval() );
			}
			
			@Override
			protected int getMaxIterationsBacktrack() {
				return( StelemDescent.this.getMaxIterationsBacktrack() );
			}
			
			@Override
			protected boolean evalIterationImproved(
					GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim>, DoubleElem, DoubleElemFactory> lastValue,
					GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim>, DoubleElem, DoubleElemFactory> nextValue ) 
			{
				return( StelemDescent.this.evalIterationImproved(lastValue, nextValue) );
			}
			
			
			public StelemDescentEnt()
			{
				super();
			}
			
			
			
		};
		

		@Override
		protected DescentAlgorithmMultiElem<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, DoubleElem, DoubleElemFactory> genDescent(
				final GenDescentParam param )
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			final DescentAlgorithmMultiElemInputParam<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, DoubleElem, DoubleElemFactory> saa =
					new DescentAlgorithmMultiElemInputParam<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, DoubleElem, DoubleElemFactory>();
			
			final StelemDescentEnt sa = new StelemDescentEnt();
			saa.setFunctions( param.getFunctions() );
			saa.setWithRespectTos( param.getWithRespectTos() );
			saa.setImplicitSpaceFirstLevel( param.getImplicitSpaceFirstLevel() );
			saa.setSfac( param.getSfac() );
			saa.setDim( param.getDim() );
			saa.setCallbacks( sa );
			
			return( new NewtonRaphsonMultiElemSimpleBacktrackCacheFinal<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, DoubleElem, DoubleElemFactory>( saa , param.getCache() ) );
		}

		@Override
		protected StelemDescentEnt genCallbacks() {
			return( new StelemDescentEnt() );
		}

		@Override
		public StelemDescent cloneThread(
				BigInteger threadIndex) {
			throw( new RuntimeException( "Not Supported" ) );
		}

		@Override
		public StelemDescent cloneThreadCached(
				CloneThreadCache<GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, GeometricAlgebraMultivectorElemFactory<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> cache,
				CloneThreadCache<?, ?> cacheImplicit, BigInteger threadIndex) {
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
protected static class VEvalElem extends SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
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
		
		for( final Entry<ArrayList<BigInteger>, SymbolicElem<DoubleElem, DoubleElemFactory>> ii : dval.getEntrySet() )
		{
			final ArrayList<BigInteger> key = ii.getKey();
			ret.setVal( key , ii.getValue().eval( implicitSpace ) );
		}
		
		return( ret );
	}
	
	@Override
	public EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>
			key = new SCacheKey<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>( this , implicitSpace );
		final EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>
			iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		
		EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> ret = 
				new EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>( new DoubleElemFactory() , dval.getContravariantIndices() , dval.getCovariantIndices() );
		
		for( final Entry<ArrayList<BigInteger>, SymbolicElem<DoubleElem, DoubleElemFactory>> ii : dval.getEntrySet() )
		{
			final ArrayList<BigInteger> keyA = ii.getKey();
			ret.setVal( keyA , ii.getValue().eval( implicitSpace ) );
		}
		
		cache.put(key, ret);
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
		
		for( final Entry<ArrayList<BigInteger>, SymbolicElem<DoubleElem, DoubleElemFactory>> ii : dval.getEntrySet() )
		{
			final ArrayList<BigInteger> key = ii.getKey();
			ret.setVal( key , ii.getValue().evalPartialDerivative( withRespectTo , implicitSpace ) );
		}
		
		return( ret );
	}
	
	@Override
	public EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		
		final SCacheKey<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>
		key = new SCacheKey<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>( this , implicitSpace , withRespectTo );
		final EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>
		iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
	
		final EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> ret = evalPartialDerivative( withRespectTo , implicitSpace );
		cache.put(key, ret);
		return( ret );
	}

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> cache,
			PrintStream ps) {
		String st = cache.get( this );
		if( st == null )
		{
			final String sta = fac.writeDesc( (WriteElemCache<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
			final String dvals = dval.writeDesc( (WriteElemCache<EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal(); // #####################################
			cache.put(this, st);
			ps.print( VEvalElem.class.getSimpleName() );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( VEvalElem.class.getSimpleName() );
			ps.print( "( " );
			ps.print( sta );
			ps.print( " , " );
			ps.print( dvals );
			ps.println( " );" );
		}
		return( st );
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
protected static class VEvalElem2 extends SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
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
	
	for( final Entry<ArrayList<BigInteger>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ii : dval.getEntrySet() )
	{
		final ArrayList<BigInteger> key = ii.getKey();
		retA.setVal( key , ii.getValue().eval( implicitSpace ) );
	}
	
	return( new VEvalElem( vefac , retA ) );
}

@Override
public SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>> evalCached(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	final SCacheKey<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>
		key = new SCacheKey<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>( this , implicitSpace );
	final SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>
		iret = cache.get( key );
	if( iret != null )
	{
		return( iret );
	}
	
	EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> retA = 
			new EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( sefac , dval.getContravariantIndices() , dval.getCovariantIndices() );
	
	final HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> 
		cache2 = new HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>>();
	
	for( final Entry<ArrayList<BigInteger>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ii : dval.getEntrySet() )
	{
		final ArrayList<BigInteger> keyA = ii.getKey();
		retA.setVal( keyA , ii.getValue().evalCached( implicitSpace , cache2 ) );
	}
	
	final SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>
		ret = new VEvalElem( vefac , retA );
	cache.put(key, ret);
	return( ret );
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
	
	for( final Entry<ArrayList<BigInteger>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ii : dval.getEntrySet() )
	{
		final ArrayList<BigInteger> key = ii.getKey();
		retA.setVal( key , ii.getValue().evalPartialDerivative( withRespectTo , implicitSpace ) );
	}
	
	return( new VEvalElem( vefac , retA ) );
}

@Override
public SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>> evalPartialDerivativeCached(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	
	final SCacheKey<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>
		key = new SCacheKey<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>( this , implicitSpace , withRespectTo );
	final SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>
	iret = cache.get( key );
	if( iret != null )
	{
		return( iret );
	}	
	
	EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> retA = 
			new EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( sefac , dval.getContravariantIndices() , dval.getCovariantIndices() );
	
	final HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> 
		cache2 = new HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>>();
	
	for( final Entry<ArrayList<BigInteger>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ii : dval.getEntrySet() )
	{
		final ArrayList<BigInteger> keyA = ii.getKey();
		retA.setVal( keyA , ii.getValue().evalPartialDerivativeCached( withRespectTo , implicitSpace , cache2 ) );
	}
	
	final SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>> ret = new VEvalElem( vefac , retA );
	cache.put(key, ret);
	return( ret );
}

@Override
public String writeDesc(
		WriteElemCache<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>> cache,
		PrintStream ps) {
	String st = cache.get( this );
	if( st == null )
	{
		final String sta = fac.writeDesc( (WriteElemCache<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>)( cache.getInnerCache() ) , ps);
		final String dvals = dval.writeDesc( (WriteElemCache<EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>)( cache.getInnerCache() ) , ps);
		st = cache.getIncrementVal(); // #####################################
		cache.put(this, st);
		ps.print( VEvalElem2.class.getSimpleName() );
		ps.print( " " );
		ps.print( st );
		ps.print( " = new " );
		ps.print( VEvalElem2.class.getSimpleName() );
		ps.print( "( " );
		ps.print( sta );
		ps.print( " , " );
		ps.print( dvals );
		ps.println( " );" );
	}
	return( st );
}


}





/**
 * An elem for evaluating the underlying differential equation into
 * its discretized approximation.
 * 
 * @author thorngreen
 *
 */
protected static class VEvalElem3 extends SymbolicElem<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
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

for( final Entry<ArrayList<BigInteger>, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> ii : dval.getEntrySet() )
{
	final ArrayList<BigInteger> key = ii.getKey();
	retA.setVal( key , ii.getValue().eval( implicitSpace ) );
}

return( new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA ) );
}

@Override
public SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> evalCached(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>, SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	final SCacheKey<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>
		key = new SCacheKey<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>( this , implicitSpace );
	final SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>
		iret = cache.get(key);
	if( iret != null )
	{
		return( iret );
	}
	
	final HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
		cache2 = new HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>();

	EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> retA = 
			new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( sefac , dval.getContravariantIndices() , dval.getCovariantIndices() );

	for( final Entry<ArrayList<BigInteger>, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> ii : dval.getEntrySet() )
	{
		final ArrayList<BigInteger> keyA = ii.getKey();
		retA.setVal( keyA , ii.getValue().evalCached( implicitSpace , cache2 ) );
	}

	final SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>
		ret = new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA );
	cache.put(key, ret);
	return( ret );
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

for( final Entry<ArrayList<BigInteger>, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> ii : dval.getEntrySet() )
{
	final ArrayList<BigInteger> key = ii.getKey();
	retA.setVal( key , ii.getValue().evalPartialDerivative( withRespectTo , implicitSpace ) );
}

return( new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA ) );
}

@Override
public SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> evalPartialDerivativeCached(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>, SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	
	final SCacheKey<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>
		key = new SCacheKey<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>( this , implicitSpace , withRespectTo );
	final SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>
		iret = cache.get(key);
	if( iret != null )
	{
		return( iret );
	}
	
	EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> retA = 
			new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( sefac , dval.getContravariantIndices() , dval.getCovariantIndices() );

	final HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
		cache2 = new HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>();
	
	for( final Entry<ArrayList<BigInteger>, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> ii : dval.getEntrySet() )
	{
		final ArrayList<BigInteger> keyA = ii.getKey();
		retA.setVal( keyA , ii.getValue().evalPartialDerivativeCached( withRespectTo , implicitSpace , cache2 ) );
	}

	final SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> 
				ret = new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA );
	cache.put(key, ret);
	return( ret );
}


@Override
public String writeDesc(
		WriteElemCache<SymbolicElem<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>> cache,
		PrintStream ps) {
	String st = cache.get( this );
	if( st == null )
	{
		final String sta = fac.writeDesc( (WriteElemCache<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>>)( cache.getInnerCache() ) , ps);
		final String dvals = dval.writeDesc( (WriteElemCache<EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>,EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>>)( cache.getInnerCache() ) , ps);
		st = cache.getIncrementVal(); // #####################################
		cache.put(this, st);
		ps.print( VEvalElem3.class.getSimpleName() );
		ps.print( " " );
		ps.print( st );
		ps.print( " = new " );
		ps.print( VEvalElem3.class.getSimpleName() );
		ps.print( "( " );
		ps.print( sta );
		ps.print( " , " );
		ps.print( dvals );
		ps.println( " );" );
	}
	return( st );
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
protected static class SymbolicMetricTensor extends SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>	
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
public EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> evalCached(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>, EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>> cache)
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
public EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> evalPartialDerivativeCached(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>, EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	throw( new RuntimeException( "NotSupported" ) );
}

@Override
public String writeDesc(
		WriteElemCache<SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>, SymbolicElemFactory<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>> cache,
		PrintStream ps) {
	String st = cache.get( this );
	if( st == null )
	{
		final String sta = fac.writeDesc( (WriteElemCache<EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>,EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>>)( cache.getInnerCache() ) , ps);
		final String dvals = dval.writeDesc( (WriteElemCache<EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>,EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>>)( cache.getInnerCache() ) , ps);
		st = cache.getIncrementVal();
		cache.put(this, st);
		ps.print( SymbolicMetricTensor.class.getSimpleName() );
		ps.print( " " );
		ps.print( st );
		ps.print( " = new " );
		ps.print( SymbolicMetricTensor.class.getSimpleName() );
		ps.print( "( " );
		ps.print( sta );
		ps.print( " , " );
		ps.print( dvals );
		ps.println( " );" );
	}
	return( st );
}


}





/**
 * Factory for generating an instance of the metric tensor.
 * 
 * @author thorngreen
 *
 */
protected static class TestMetricTensorFactory extends MetricTensorInvertingFactory<String, TestDimensionFive,
	SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
	>
{

	@Override
	public SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>> getMetricTensor(
			boolean icovariantIndices, String index0, String index1) 
	{	
		final TestDimensionFive td = new TestDimensionFive();
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		if( icovariantIndices ) covariantIndices.add( index0 ); else contravariantIndices.add( index0 );
		if( icovariantIndices ) covariantIndices.add( index1 ); else contravariantIndices.add( index1 );
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> de2 = new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		// final SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		// final SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>> se2 =
		//		new SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		// final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>> se3 =
		//		new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
				new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A );
	
		
		
		EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			g0 = new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				( se3A , contravariantIndices , covariantIndices );

		for( int acnt = 0 ; acnt < SQ_TENSOR_SZ ; acnt++ )
		{
			if( /* ( acnt % TestDimensionFive.FIVE ) == ( acnt / TestDimensionFive.FIVE ) */ true )
			{
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt / TestDimensionFive.FIVE ) );
				ab.add( BigInteger.valueOf( acnt % TestDimensionFive.FIVE ) );
				final AStelem as = new AStelem( se2A  , ab );
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
	public TestMetricTensorFactory cloneThread( final BigInteger threadIndex )
	{
		return( this );
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
	for( int tcnt = 0 ; tcnt < 2 * NSTPT ; tcnt++ )
	{
		System.out.println( "Initial - " + tcnt );
		for( long acnt = 0 ; acnt < ( (long) NUM_X_ITER ) * NUM_Y_ITER * NUM_Z_ITER * NUM_W_ITER ; acnt++ )
		{
			atm2 = System.currentTimeMillis();
			if( atm2 - atm >= IterConstants.INIT_UPDATE_DELAY )
			{
				System.out.println( ">> " + acnt );
				atm = atm2;
			}
			
			long ac = acnt;
			final int w = (int)( ac % NUM_W_ITER );
			ac = ac / NUM_W_ITER;
			final int z = (int)( ac % NUM_Z_ITER );
			ac = ac / NUM_Z_ITER;
			final int y = (int)( ac % NUM_Y_ITER );
			ac = ac / NUM_Y_ITER;
			final int x = (int)( ac % NUM_X_ITER );
			final double dx = ( x - HALF_X ) / RAD_X;
			final double dy = ( y - HALF_Y ) / RAD_Y;
			final double dz = ( z - HALF_Z ) / RAD_Z;
			final double dw = ( w - HALF_W ) / RAD_W;
			if( dx * dx + dy * dy + dz * dz + dw * dw < 1.0 )
			{
				iterArray[ tcnt ][ x ][ y ][ z ][ w ] = genDiffEnt();
			}
			else
			{
				iterArray[ tcnt ][ x ][ y ][ z ][ w ] = genDiffAll();
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
					for( int wcnt = 0 ; wcnt < 2 ; wcnt++ )
					{
						tempArray[ tcnt ][ xcnt ][ ycnt ][ zcnt ][ wcnt ] = genDiffAll( );
					}
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
protected static class IncrementManager
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
	 * The current discretized W-coordinate.
	 */
	protected int wcnt = 0;
	
	
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
	 * Handles the base increment from the W-Axis.  At the point the increment
	 * reaches a swatch boundary, other axes are potentially incremented.
	 */
	public void handleIncrementWa()
	{
		if( wcnt < ( NUM_W_ITER - 1 ) )
		{
			wcnt++;
		}
		else
		{
			handleIncrementZa();
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
		wcnt = 0;
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
	
	
	
	/**
	 * Gets the current discretized W-coordinate.
	 * 
	 * @return The current discretized W-coordinate.
	 */
	public int getWcnt() {
		return wcnt;
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
protected void performIterationT( final int tval , final StelemDescent descent , final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 ) 
		throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	//EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> tmpCorrectionValue = null;
	im.restartIncrements();
	for( long acnt = 0 ; acnt < ( ( (long) NUM_X_ITER ) * NUM_Y_ITER * NUM_Z_ITER * NUM_W_ITER ) ; acnt++ )
	{
		
		System.out.println( "iter... " + im.getXcnt() + " " + im.getYcnt() + " " + im.getZcnt() + " " + im.getWcnt() );
				
		fillTempArray( tval , im.getXcnt() , im.getYcnt() , im.getZcnt() , im.getWcnt() );
		clearSpatialAssertArray();

		
		final EinsteinTensorElem<String,DoubleElem, DoubleElemFactory> 
			ival = TestGeneralRelativityA_5D_Cd.getUpdateValue();
		
		
	
		
		EinsteinTensorElem<String,DoubleElem, DoubleElemFactory> err = descent.eval( implicitSpace2 );
		
		if( APPLY_NUMERICAL_VISCOSITY )
		{
			applyNumericViscosity();
		}
				
		
		
		//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
		//{
		//	tmpCorrectionValue = getCorrectionValue();
		//	applyPredictorCorrector();
		//
		//			
		//	err = descent.eval( implicitSpace2 );
		//
		//  if( APPLY_NUMERICAL_VISCOSITY )
		// {
		//	applyNumericViscosity();
		// }
		//}


		final EinsteinTensorElem<String,DoubleElem, DoubleElemFactory> 
			val = TestGeneralRelativityA_5D_Cd.getUpdateValue();
				
				
		
		if( ( im.getXcnt() == HALF_X ) && ( im.getYcnt() == HALF_Y ) && ( im.getZcnt() == HALF_Z ) && ( im.getWcnt() == HALF_W ) )
		{
			System.out.println( "******************" );
			System.out.println( " ( " + im.getXcnt() + " , " + im.getYcnt() + " , " + im.getZcnt() + " , " + im.getWcnt() + " ) " );
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
		
		
		System.out.println( "***  " + im.getXcnt() + "  " + im.getYcnt() + "  " + im.getZcnt() + " " + im.getWcnt() );
		System.out.println( calcMagnitudeSq( val ) );
		System.out.println( calcMagnitudeSq( err ) );
		// Assert.assertTrue( Math.abs( Math.sqrt( calcMagnitudeSq( err ) ) ) < ( 0.01 * Math.abs( Math.sqrt( calcMagnitudeSq( val ) ) ) + 0.01 ) );
		
		//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
		//{
		//	resetCorrectionValue( tmpCorrectionValue );
		//}
	
		iterArray[ tval + NSTPT ][ im.getXcnt() ][ im.getYcnt() ][ im.getZcnt() ][ im.getWcnt() ] = val;
			
		
		
		im.handleIncrementWa();
				
	}
	
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
	    final double cmx = Math.min( X_HH.getVal() , Math.min( Y_HH.getVal() , Math.min( Z_HH.getVal() , W_HH.getVal() ) ) ) / ( T_HH.getVal() );
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
		
		final double d1 = Math.sqrt( calcMagnitudeSq( X_HH ) + calcMagnitudeSq( Y_HH ) + calcMagnitudeSq( Z_HH ) + calcMagnitudeSq( W_HH ) );
		
		final TestDimensionFive tdim = new TestDimensionFive();
		
		// final SpacetimeAlgebraOrd<TestDimensionFive> ord = new SpacetimeAlgebraOrd<TestDimensionFive>();
		
		
		
		initIterArray();
		
		
		initTempArray();
		
		

		
		
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
		
		
		// final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>> se3 =
		//		new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		
		
		
		final ArrayList<Ordinate> wrtT = new ArrayList<Ordinate>();
		
		wrtT.add( new Ordinate( de2 , TV ) );
		
		// final ArrayList<AElem> wrtX = new ArrayList<AElem>();
		
		// wrtX.add( new AElem( de , 1 ) );
		
		
		final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
			new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A );
		
		
		
		
		final DDirec ddirec = new DDirec(de2, se2A);
		
		
		
		final OrdinaryDerivativeFactory<String,TestDimensionFive,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			Ordinate> odf =
			new OrdinaryDerivativeFactory<String,TestDimensionFive,
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				Ordinate>( ge , tdim , ddirec , null );
		
		
		
		MetricTensorFactory<String, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
			metricZ = new TestMetricTensorFactory();
		
		EinsteinTensorFactory<String,TestDimensionFive,
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				Ordinate> 
				et = new EinsteinTensorFactory<String,TestDimensionFive,
					SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					Ordinate>
					( ge , metricZ , new TestTemporaryIndexFactory() , odf );
		
		
		System.out.println( "Reached #1..." );
		
		
		HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>, EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>> 
			cache3A = new HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>, EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>>();
		
		
		final EinsteinTensorElem<
			String,
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				curv = et.getEinsteinTensor( "u" , "v" ).evalCached( null /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */ , cache3A );
	
		
		cache3A = null;
		
		
		System.out.println( "Reached #2..." );
		
		
		final EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				mg1 = curv;
		
		
		VEvalElem3 m1 = new VEvalElem3( se2 , se2A , de2 , mg1 );
		
		
		System.out.println( "Reached #3..." );
		
		
		// System.out.println( m1.writeString() );
		
		
		
		
		final HashMap<Ordinate,Ordinate> implicitSpace0 = new HashMap<Ordinate,Ordinate>();
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 = implicitSpace0;
		
		implicitSpace0.put( new Ordinate( de2 , TV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , XV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , YV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , ZV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , WV ) , new Ordinate( de2 , 0 ) );
		
		
		System.out.println( "Reached #4..." );
		
		
		HashMap<SCacheKey<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>, SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>> 
			cache2A = new HashMap<SCacheKey<SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>, SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>>>();
		
		
		final SymbolicElem<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> 
			s0 = m1.evalCached( implicitSpace2 , cache2A );
		
		
		cache2A = null;
		
		
		System.out.println( "Reached #5..." );
		
		
		final EinsteinTensorElem<String, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
			s00 = ( (VEvalElem2)( s0 ) ).dval;
		
		
		System.out.println( "Reached #6..." );
		
		
		// String s = s0.writeString();
		
		// System.out.println( s );
		
		
		final HashMap<ArrayList<BigInteger>, ArrayList<? extends Elem<?, ?>>> wrt3 = new HashMap<ArrayList<BigInteger>, ArrayList<? extends Elem<?, ?>>>();
		{
			for( int acnt = 0 ; acnt < SQ_TENSOR_SZ ; acnt++ )
			{
				// if( ( acnt % TestDimensionFive.FIVE ) == ( acnt / TestDimensionFive.FIVE ) )
				// {
					final HashMap<Ordinate, BigInteger> coord = new HashMap<Ordinate, BigInteger>();
					coord.put( new Ordinate( de2 , TV ) , BigInteger.valueOf( NSTPT ) );
					coord.put( new Ordinate( de2 , XV ) , BigInteger.valueOf( 0 ) );
					coord.put( new Ordinate( de2 , YV ) , BigInteger.valueOf( 0 ) );
					coord.put( new Ordinate( de2 , ZV ) , BigInteger.valueOf( 0 ) );
					coord.put( new Ordinate( de2 , WV ) , BigInteger.valueOf( 0 ) );
				
					final ArrayList<BigInteger> index = new ArrayList<BigInteger>();
					index.add( BigInteger.valueOf( acnt / TestDimensionFive.FIVE ) );
					index.add( BigInteger.valueOf( acnt % TestDimensionFive.FIVE ) );
				
					ArrayList<CNelem> ce = new ArrayList<CNelem>();
					ce.add( new CNelem( seA , coord , index ) );
				
					wrt3.put( index , ce );
				// }
			}
		}
		
		
		// System.out.println( "S7 I" ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// System.exit( 0 );
		
		
		System.out.println( "Reached #7..." );
		
		
		HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> cache =
				new HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>>();
		
		
		final DescentAlgorithmMultiElemRemapTensorParam<String, DoubleElem, DoubleElemFactory>
			param = new DescentAlgorithmMultiElemRemapTensorParam<String, DoubleElem, DoubleElemFactory>();
		
		
		param.setFunctions( s00 );
		param.setWithRespectTosI( wrt3 );
		param.setImplicitSpaceFirstLevel( implicitSpace2 );
		param.setSfac( se2A );
		param.setContravariantIndices( contravariantIndices );
		param.setCovariantIndices( covariantIndices );
		
		
		StelemDescent descent = new StelemDescent( param , cache );
		
		
		
		cache = null;
		
		
		// System.out.println( "S9 I" ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// System.exit( 0 );
		
		
		System.out.println( "Reached #9..." );
		
		
		try
		{
			for( int tval = 1 ; tval < ( NUM_T_ITER - NSTPT ) ; tval++ )
			{
				performIterationT( tval , descent , implicitSpace2 );
			}
		}
		catch( DescentAlgorithmMultiElemRemapTensor.TensorDescentInverseFailedException ex )
		{
			final IntVal cacheVal = new IntVal();
			final WriteEinListCache cacheA = new WriteEinListCache( cacheVal );
			final WriteBigIntegerCache wb = new WriteBigIntegerCache( cacheVal );
			cacheA.writeDesc( ex.getElemNum() , wb , System.out );
		}
		
		
		// System.out.println( "==============================" ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// System.out.println( iterArray[ NUM_T_ITER - 1 ][ HALF_X ][ HALF_Y ][ HALF_Z ] ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// Assert.assertTrue( Math.abs( val - ( -1.450868 ) ) < 0.01 ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
	}
	
	

	
}


