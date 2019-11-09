







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

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.CloneThreadCache;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.algo.DescentAlgorithmMultiElem;
import simplealgebra.algo.DescentAlgorithmMultiElemInputParam;
import simplealgebra.algo.DescentAlgorithmMultiElemInputParamCallbacks;
import simplealgebra.algo.NewtonRaphsonMultiElemSimpleBacktrack;
import simplealgebra.algo.NewtonRaphsonSingleElem;
import simplealgebra.algo.SimplificationType;
import simplealgebra.algo.DescentAlgorithmMultiElem.DescentInverseFailedException;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.stelem.Nelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import test_simplealgebra.TestDiracA.StelemDescent;



/**
 * Finds Newtonian Basins.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This is a variant of class TestNewtonianBasins that uses multivariable Newton-Raphson with Jacobian matrices
 * 	with a set of constraint functions over the reals.
 * 
 * Formulas from https://mathcs.clarku.edu/~djoyce/newton/examples.html
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestNewtonianBasinsMult extends TestCase {
	
	
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
	
	
	/**
	 * Builds a fixed-point value from a rational number.
	 * 
	 * @param numerator The numerator of the rational number.
	 * @param denominator The denominator of the rational number.
	 * @return The fixed-point value.
	 */
	static BigFixedPointElem<LrgPrecision> buildElem( BigInteger numerator , BigInteger denominator )
	{
		final BigInteger val = numerator.multiply( baseVal ).divide( denominator );
		return( new BigFixedPointElem<LrgPrecision>( val , lrgPrecision ) );
	}
	
	
	/**
	 * Constant defining the X-center for the Newtonian Basin execution.
	 */
	static final BigFixedPointElem<LrgPrecision> X_CENTER
		= buildElem( BigInteger.valueOf( 0L ) , BigInteger.valueOf( 10000000000000000L ) );
	
	/**
	 * Constant defining the Y-center for the Newtonian Basin execution.
	 */
	static final BigFixedPointElem<LrgPrecision> Y_CENTER
		= buildElem( BigInteger.valueOf( 0L ) , BigInteger.valueOf( 10000000000000000L ) );
	
	/**
	 * The X-Y resolution for the Newtonian Basin iterations.
	 * Note: much higher resolutions should work.
	 */
	static final BigInteger N = BigInteger.valueOf( 60 /* 512 */ );
	
	/**
	 * Constant defining the zoom ratio for the Newtonian Basin execution.  Set by each test.
	 */
	static BigFixedPointElem<LrgPrecision> ZOOM;
	
	/**
	 * The X-Y resolution for the Newtonian Basin iterations.
	 */
	static final BigFixedPointElem<LrgPrecision> Nv
		= buildElem( N , BigInteger.valueOf( 1 ) );
	
	
	
	
	
	static HashSet<BigInteger> genIndexRe()
	{
		HashSet<BigInteger> hs = new HashSet<BigInteger>();
		hs.add( BigInteger.ZERO );
		return( hs );
	}
	
	
	
	
	
	static HashSet<BigInteger> genIndexIm()
	{
		HashSet<BigInteger> hs = new HashSet<BigInteger>();
		hs.add( BigInteger.ONE );
		return( hs );
	}
	
	
	
	static final HashSet<BigInteger> VCT_INDEX_RE = genIndexRe();	
	
	static final HashSet<BigInteger> VCT_INDEX_IM = genIndexIm();
	
	
	
	
	
	/**
	 * Temporary iteration value for Newton-Raphson.
	 */
	private static GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> tempValue = null;
	
	
	
	/**
	 * Given a change calculated by a Newton-Raphson iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void performIterationUpdate( GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> dbl )
	{
		tempValue = tempValue.add( dbl );
	}
	
	
	/**
	 * The iteration cache value.
	 */
	protected static GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> iterationValueCache = null;
	
	
	/**
	 * Places the current iteration value in the cache.
	 */
	protected static void cacheIterationValue()
	{
		iterationValueCache = tempValue;
	}
	
	
	/**
	 * Sets the current iteration value to the value in the cache.
	 */
	protected static void retrieveIterationValue()
	{
		tempValue = iterationValueCache;
	}
	
	
	/**
	 * Returns the result of the Newton-Raphson iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> getUpdateValue()
	{
		return( tempValue );
	}
	
	
	
	protected static void resetIterations()
	{
		intCnt = 0;
	}
	


	/**
	 * The iteration count for descent algorithm iterations.
	 */
	protected static int intCnt = 0;

	
	protected static boolean iterationsDone() {
		intCnt++;
		return( intCnt > 40 );
	}
	
	
	
	
	
	/**
	 * Elem representing the value constrained by the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class BNelemRe extends SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
	{

		/**
		 * Constructs the elem.
		 * @param _fac The input factory.
		 */
		public BNelemRe(BigFixedPointElemFactory<LrgPrecision> _fac) {
			super(_fac);
		}

		@Override
		public BigFixedPointElem<LrgPrecision> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( tempValue.get( VCT_INDEX_RE ) );
		}

		@Override
		public BigFixedPointElem<LrgPrecision> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, BigFixedPointElem<LrgPrecision>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( this.eval(implicitSpace) );
		}

		@Override
		public BigFixedPointElem<LrgPrecision> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public BigFixedPointElem<LrgPrecision> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, BigFixedPointElem<LrgPrecision>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache,
				PrintStream ps) {
			throw( new RuntimeException( "NotSupported" ) );
		}

		
		
	}
	
	
	
	
	
	
	/**
	 * Elem representing the value constrained by the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class BNelemIm extends SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
	{

		/**
		 * Constructs the elem.
		 * @param _fac The input factory.
		 */
		public BNelemIm(BigFixedPointElemFactory<LrgPrecision> _fac) {
			super(_fac);
		}

		@Override
		public BigFixedPointElem<LrgPrecision> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( tempValue.get( VCT_INDEX_IM ) );
		}

		@Override
		public BigFixedPointElem<LrgPrecision> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, BigFixedPointElem<LrgPrecision>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( this.eval(implicitSpace) );
		}

		@Override
		public BigFixedPointElem<LrgPrecision> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public BigFixedPointElem<LrgPrecision> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, BigFixedPointElem<LrgPrecision>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache,
				PrintStream ps) {
			throw( new RuntimeException( "NotSupported" ) );
		}

		
		
	}
	
	
	
	
	
	
	/**
	 * Elem representing a constant.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class BConst extends SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
	{
		
		/**
		 * The constant value.
		 */
		BigFixedPointElem<LrgPrecision> val;

		/**
		 * Constructs the elem.
		 * @param _fac The input factory.
		 */
		public BConst(
				BigFixedPointElem<LrgPrecision> val ,
				BigFixedPointElemFactory<LrgPrecision> _fac) {
			super(_fac);
			this.val = val;
		}

		@Override
		public BigFixedPointElem<LrgPrecision> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( val );
		}

		@Override
		public BigFixedPointElem<LrgPrecision> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, BigFixedPointElem<LrgPrecision>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( this.eval(implicitSpace) );
		}

		@Override
		public BigFixedPointElem<LrgPrecision> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public BigFixedPointElem<LrgPrecision> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, BigFixedPointElem<LrgPrecision>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache,
				PrintStream ps) {
			throw( new RuntimeException( "NotSupported" ) );
		}

		
		
	}
	
	
	
	
	
	
	
	/**
	 * Elem representing the symbolic expression for 
	 * the value constrained by the differential equation.
	 * The partial derivatives of this elem generate
	 * the slopes for producing Newton-Raphson iterations (e.g. the Jacobian slopes),
	 * as opposed to partial derivatives for the underlying differential equation.
	 * 
	 * @author thorngreen
	 *
	 */	
	protected static class CNelemRe extends SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	{

		/**
		 * Constructs the elem.
		 * @param _fac The input factory.
		 */
		public CNelemRe(
				SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_fac);
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( new BNelemRe( fac.getFac() ) );
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			if( withRespectTo.get(0) instanceof CNelemRe )
			{
				return( fac.identity() );
			}
			return( fac.zero() );
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( evalPartialDerivative( withRespectTo , implicitSpace ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
				PrintStream ps) {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
	}
	
	
	
	
	
	
	
	/**
	 * Elem representing the symbolic expression for 
	 * the value constrained by the differential equation.
	 * The partial derivatives of this elem generate
	 * the slopes for producing Newton-Raphson iterations (e.g. the Jacobian slopes),
	 * as opposed to partial derivatives for the underlying differential equation.
	 * 
	 * @author thorngreen
	 *
	 */	
	protected static class CNelemIm extends SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	{

		/**
		 * Constructs the elem.
		 * @param _fac The input factory.
		 */
		public CNelemIm(
				SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_fac);
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( new BNelemIm( fac.getFac() ) );
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			if( withRespectTo.get(0) instanceof CNelemIm )
			{
				return( fac.identity() );
			}
			return( fac.zero() );
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( evalPartialDerivative( withRespectTo , implicitSpace ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
				PrintStream ps) {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Elem representing the symbolic expression for 
	 * a constant.
	 * The partial derivatives of this elem generate
	 * the slopes for producing Newton-Raphson iterations (e.g. the Jacobian slopes),
	 * as opposed to partial derivatives for the underlying differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class CConst extends SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	{
		
		/**
		 * The constant value.
		 */
		BigFixedPointElem<LrgPrecision> val;

		/**
		 * Constructs the elem.
		 * @param _fac The input factory.
		 */
		public CConst(
				BigFixedPointElem<LrgPrecision> val ,
				SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_fac);
			this.val = val;
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( new BConst( val , fac.getFac() ) );
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( fac.zero() );
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( evalPartialDerivative( withRespectTo , implicitSpace ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
				PrintStream ps) {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
	}
	
	

	
	/**
	 * The internal multivariate descent algorithm.
	 * 
	 * @author thorngreen
	 *
	 */
	protected class StelemDescentEnt extends DescentAlgorithmMultiElemInputParamCallbacks<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
	{
		protected SquareMatrixElemFactory<TestDimensionTwo,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> fac;
		
		@Override
		protected void performIterationUpdate(
				GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> iterationOffset) {
			TestNewtonianBasinsMult.performIterationUpdate( iterationOffset );
		}
		
		@Override
		protected void setIterationValue(
				GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> iterationOffset) {
			tempValue = iterationOffset;
		}
		
		@Override
		protected void cacheIterationValue() {
			TestNewtonianBasinsMult.cacheIterationValue();
		}
		
		@Override
		protected void retrieveIterationValue() {
			TestNewtonianBasinsMult.retrieveIterationValue();
		}
		
		@Override protected GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> getIterationValue()
		{
			return( TestNewtonianBasinsMult.getUpdateValue( ) );
		}

		@Override
		protected boolean iterationsDone() {
			return( TestNewtonianBasinsMult.iterationsDone() );
		}
		
		@Override
		protected SimplificationType useSimplification() {
			return( SimplificationType.NONE );
		}
		
		@Override
		protected boolean useCachedEval() {
			return( false );
		}
		
		@Override
		protected int getMaxIterationsBacktrack() {
			return( 400 );
		}
		
		@Override
		protected boolean evalIterationImproved( GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> lastValue ,
				GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> nextValue )
		{
			final BigFixedPointElem<LrgPrecision> lastRe = lastValue.get( VCT_INDEX_RE );
			final BigFixedPointElem<LrgPrecision> lastIm = lastValue.get( VCT_INDEX_IM );
			final BigFixedPointElem<LrgPrecision> nextRe = nextValue.get( VCT_INDEX_RE );
			final BigFixedPointElem<LrgPrecision> nextIm = nextValue.get( VCT_INDEX_IM );
			final BigFixedPointElem<LrgPrecision> distLast = ( lastRe.mult( lastRe ) ).add( lastIm.mult( lastIm ) );
			final BigFixedPointElem<LrgPrecision> distNext = ( nextRe.mult( nextRe ) ).add( nextIm.mult( nextIm ) );
			return( distNext.compareTo( distLast ) < 0 );
		}
		
		@Override
		protected SquareMatrixElem<TestDimensionTwo,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> handleDescentInverseFailed( final SquareMatrixElem<TestDimensionTwo,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> derivativeJacobian , final SquareMatrixElem.NoPivotException ex )
				 throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			return( fac.identity() );
		}
		
		
		public StelemDescentEnt( SquareMatrixElemFactory<TestDimensionTwo,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> fac )
		{
			super();
			this.fac = fac;
		}

		
	};
	
	
	
	
	
	protected DescentAlgorithmMultiElem<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> genDescent(
			final SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> slfac2 ,
			final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>  function ,
			final CNelemRe cnre ,
			final CNelemIm cnim 
			 ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>( );
		
		final DescentAlgorithmMultiElemInputParam<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> saa
			= new DescentAlgorithmMultiElemInputParam<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>();
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> funs = 
				new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>(
						slfac2, td, ord);
		
		funs.setVal( VCT_INDEX_RE , function.getRe() );
		
		funs.setVal( VCT_INDEX_IM , function.getIm() );
		
		
		final HashMap hm = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm;
		
		
		final ArrayList ar = new ArrayList();
		final ArrayList ar0 = new ArrayList();
		final ArrayList ar1 = new ArrayList();
		ar0.add( cnre );
		ar1.add( cnim );
		ar.add( ar0 );
		ar.add( ar1 );
		final ArrayList<ArrayList<? extends Elem<?, ?>>> withRespectTo = (ArrayList<ArrayList<? extends Elem<?, ?>>>)( ar );
		
		
		
		final HashMap<SCacheKey<SymbolicElem<BigFixedPointElem<TestNewtonianBasinsMult.LrgPrecision>,BigFixedPointElemFactory<TestNewtonianBasinsMult.LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<TestNewtonianBasinsMult.LrgPrecision>,BigFixedPointElemFactory<TestNewtonianBasinsMult.LrgPrecision>>>,SymbolicElem<BigFixedPointElem<TestNewtonianBasinsMult.LrgPrecision>,BigFixedPointElemFactory<TestNewtonianBasinsMult.LrgPrecision>>> cache = 
				new HashMap<SCacheKey<SymbolicElem<BigFixedPointElem<TestNewtonianBasinsMult.LrgPrecision>,BigFixedPointElemFactory<TestNewtonianBasinsMult.LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<TestNewtonianBasinsMult.LrgPrecision>,BigFixedPointElemFactory<TestNewtonianBasinsMult.LrgPrecision>>>,SymbolicElem<BigFixedPointElem<TestNewtonianBasinsMult.LrgPrecision>,BigFixedPointElemFactory<TestNewtonianBasinsMult.LrgPrecision>>>();
		
		
		SquareMatrixElemFactory<TestDimensionTwo,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> mfac = new SquareMatrixElemFactory<TestDimensionTwo,BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(slfac2.getFac().getFac(),td);
		
		
		final StelemDescentEnt sa = new StelemDescentEnt( mfac );
		saa.setFunctions( funs );
		saa.setWithRespectTos( withRespectTo );
		saa.setImplicitSpaceFirstLevel( implicitSpaceFirstLevel );
		saa.setSfac( slfac2.getFac() );
		saa.setDim( td );
		saa.setCallbacks( sa );
		
		return( new NewtonRaphsonMultiElemSimpleBacktrack<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( saa , cache ) );
	}
	
	
	
	
	
	/**
	 * Computes the X-Y distance between two complex points.
	 * @param c1 The first point.
	 * @param c2 The second point.
	 * @return The distance.
	 */
	static protected BigFixedPointElem<LrgPrecision> dist(
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c1,
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c2
			)
	{
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c3 = c1.add( c2.negate() );
		final BigFixedPointElem<LrgPrecision> r1 = c3.getRe();
		final BigFixedPointElem<LrgPrecision> r2 = c3.getIm();
		final BigFixedPointElem<LrgPrecision> ret = ( r1.mult( r1 ) ).add( r2.mult( r2 ) );
		return( ret );
	}
	
	
	
	/**
	 * Assigns a color to a particular complex point.
	 * @param updateValue The point to color.
	 * @param basins The list of potential solutions to check.
	 * @param colors The list of colors.
	 * @return The closest color.
	 */
	static protected Color findClosestColor(
			final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> updateValueI ,
			final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> basins ,
			final ArrayList<Color> colors
			)
	{
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> updateValue =
				new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( updateValueI.get(VCT_INDEX_RE) , updateValueI.get(VCT_INDEX_IM) );
		
		Color col = colors.get( 0 );
		BigFixedPointElem<LrgPrecision> dst = dist( updateValue , basins.get( 0 ) );
		for( int cnt = 1 ; cnt < basins.size() ; cnt++ )
		{
			final BigFixedPointElem<LrgPrecision> dst2 = dist( updateValue , basins.get( cnt ) );
			if( dst2.compareTo( dst ) < 0 )
			{
				dst = dst2;
				col = colors.get( cnt );
			}
		}
		final BigFixedPointElem<LrgPrecision> CUTOFF
			= buildElem( BigInteger.valueOf( 1L ) , BigInteger.valueOf( 100L ) );
		if( dst.compareTo( CUTOFF ) > 0 )
		{
			col = Color.BLACK;
		}
		return( col );
	}
	

	
	
	
	/**
	 * Takes an elem to an integer power ( >= 1 ) through multiplications.
	 * @param z The input elem.
	 * @param powr The desired power.
	 * @return The elem taken to the power.
	 */
	static protected ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>  
		mpow( ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>  z , int powr )
	{
		ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>  ret = z;
		for( int cnt = 2 ; cnt <= powr ; cnt++ )
		{
			ret = ret.mult( z );
		}
		return( ret );
	}
	
	
	
	
	/**
	 * Finds the Newtonian Basins of z ^ 4 - 1.
	 * 
	 * Formula from https://mathcs.clarku.edu/~djoyce/newton/examples.html
	 * 
	 * @throws NotInvertibleException
	 */
	public void testNewtonianBasinA() throws NotInvertibleException, Throwable
	{
		ZOOM = buildElem( N , BigInteger.valueOf( 4L ) );
		
		File ofilePpm = new File( "testAm" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> slfac = 
				new SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> slfac2 = 
				new SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( slfac );
		
		final ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> cfac =
				new ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( slfac2 );
		
				
		final CNelemRe cnre = new CNelemRe( slfac );
		
		final CNelemIm cnim = new CNelemIm( slfac );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> z = 
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( cnre , cnim );
		
		
		final HashMap hm2 = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceInitialGuess = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm2;
		
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> basins = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		final ArrayList<Color> colors = new ArrayList<Color>();
		
		
		{
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity() , fac.zero() ) );
			colors.add( Color.RED );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity().negate() , fac.zero() ) );
			colors.add( Color.GREEN );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.zero() , fac.identity() ) );
			colors.add( Color.CYAN );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.zero() , fac.identity().negate() ) );
			colors.add( Color.ORANGE );
			
		}
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> function =
				mpow( z , 4 ).add( cfac.identity().negate() );
		
		final DescentAlgorithmMultiElem<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> stel = genDescent(
				slfac2 , function , cnre , cnim );
		
		
		final BigInteger n2 = N.divide( BigInteger.valueOf( 2 ) );
		final BigFixedPointElem<LrgPrecision> zoomInverse = ZOOM.invertLeft();
		
		for( BigInteger y = BigInteger.ZERO ; y.compareTo( N ) < 0 ; y = y.add( BigInteger.ONE ) )
		{
			
			final BigFixedPointElem<LrgPrecision> yv = buildElem( y.subtract( n2 ) , BigInteger.ONE );
			final BigFixedPointElem<LrgPrecision> yyv = Y_CENTER.add( yv.mult( zoomInverse ).negate() );
			System.out.println( y );
			
			for( BigInteger x = BigInteger.ZERO ; x.compareTo( N ) < 0 ; x = x.add( BigInteger.ONE ) )
			{
				final BigFixedPointElem<LrgPrecision> xv = buildElem( x.subtract( n2 ) , BigInteger.ONE );
				final BigFixedPointElem<LrgPrecision> xxv = X_CENTER.add( xv.mult( zoomInverse ) );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c
					= new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( xxv , yyv );
				
				final TestDimensionTwo td = new TestDimensionTwo();
				
				final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
				
				tempValue = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				tempValue.setVal( VCT_INDEX_RE , c.getRe() );
				
				tempValue.setVal( VCT_INDEX_IM , c.getIm() );
				
				iterationValueCache = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				iterationValueCache.setVal( VCT_INDEX_RE , c.getRe() );
				
				iterationValueCache.setVal( VCT_INDEX_IM , c.getIm() );
				
				resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
					updateValue = getUpdateValue();
				
				// System.out.println( updateValue.getRe().toDouble() + " " + updateValue.getIm().toDouble() );
				
				final Color col =
					findClosestColor( updateValue , basins , colors );
				
				/* System.out.print( x );
				System.out.print( " " );
				System.out.print( y );
				System.out.print( " " );
				System.out.print( col.getRed() );
				System.out.print( " " );
				System.out.print( col.getGreen() );
				System.out.print( " " );
				System.out.println( col.getBlue() ); */
				
				baos.write( col.getRed() );
				baos.write( col.getGreen() );
				baos.write( col.getBlue() );
			}
		}
		
		
		baos.close();
		
	}
	
	
	
	
	/**
	 * Finds the Newtonian Basins of z ^ 3 - 1.
	 * 
	 * Formula from https://mathcs.clarku.edu/~djoyce/newton/examples.html
	 * 
	 * @throws NotInvertibleException
	 */
	public void testNewtonianBasinB() throws NotInvertibleException, Throwable
	{
		ZOOM = buildElem( N , BigInteger.valueOf( 4L ) );
		
		File ofilePpm = new File( "testBm" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> slfac = 
				new SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> slfac2 = 
				new SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( slfac );
		
		final ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> cfac =
				new ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( slfac2 );
		
				
		final CNelemRe cnre = new CNelemRe( slfac );
		
		final CNelemIm cnim = new CNelemIm( slfac );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> z = 
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( cnre , cnim );
		
		
		final HashMap hm2 = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceInitialGuess = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm2;
		
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> basins = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		final ArrayList<Color> colors = new ArrayList<Color>();
		
		
		{
			final BigFixedPointElem<LrgPrecision> V866
				= buildElem( BigInteger.valueOf( 86603L ) , BigInteger.valueOf( 100000L ) );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity() , fac.zero() ) );
			colors.add( Color.RED );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity().negate().divideBy( 2 ) , V866 ) );
			colors.add( Color.GREEN );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity().negate().divideBy( 2 ) , V866.negate() ) );
			colors.add( Color.CYAN );
			
		}
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> function =
				mpow( z , 3 ).add( cfac.identity().negate() );
		
		final DescentAlgorithmMultiElem<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> stel = genDescent(
				slfac2 , function , cnre , cnim );
		
		
		final BigInteger n2 = N.divide( BigInteger.valueOf( 2 ) );
		final BigFixedPointElem<LrgPrecision> zoomInverse = ZOOM.invertLeft();
		
		for( BigInteger y = BigInteger.ZERO ; y.compareTo( N ) < 0 ; y = y.add( BigInteger.ONE ) )
		{
			
			final BigFixedPointElem<LrgPrecision> yv = buildElem( y.subtract( n2 ) , BigInteger.ONE );
			final BigFixedPointElem<LrgPrecision> yyv = Y_CENTER.add( yv.mult( zoomInverse ).negate() );
			System.out.println( y );
			
			for( BigInteger x = BigInteger.ZERO ; x.compareTo( N ) < 0 ; x = x.add( BigInteger.ONE ) )
			{
				final BigFixedPointElem<LrgPrecision> xv = buildElem( x.subtract( n2 ) , BigInteger.ONE );
				final BigFixedPointElem<LrgPrecision> xxv = X_CENTER.add( xv.mult( zoomInverse ) );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c
					= new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( xxv , yyv );
				
				final TestDimensionTwo td = new TestDimensionTwo();
				
				final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
				
				tempValue = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				tempValue.setVal( VCT_INDEX_RE , c.getRe() );
				
				tempValue.setVal( VCT_INDEX_IM , c.getIm() );
				
				iterationValueCache = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				iterationValueCache.setVal( VCT_INDEX_RE , c.getRe() );
				
				iterationValueCache.setVal( VCT_INDEX_IM , c.getIm() );
				
				resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
					updateValue = getUpdateValue();
				
				// System.out.println( updateValue.getRe().toDouble() + " " + updateValue.getIm().toDouble() );
				
				final Color col =
					findClosestColor( updateValue , basins , colors );
				
				/* System.out.print( x );
				System.out.print( " " );
				System.out.print( y );
				System.out.print( " " );
				System.out.print( col.getRed() );
				System.out.print( " " );
				System.out.print( col.getGreen() );
				System.out.print( " " );
				System.out.println( col.getBlue() ); */
				
				baos.write( col.getRed() );
				baos.write( col.getGreen() );
				baos.write( col.getBlue() );
			}
		}
		
		
		baos.close();
		
	}
	
	
	
	
	/**
	 * Finds the Newtonian Basins of z ^ 3 - z.
	 * 
	 * Formula from https://mathcs.clarku.edu/~djoyce/newton/examples.html
	 * 
	 * @throws NotInvertibleException
	 */
	public void testNewtonianBasinC() throws NotInvertibleException, Throwable
	{
		ZOOM = buildElem( N , BigInteger.valueOf( 4L ) );
		
		File ofilePpm = new File( "testCm" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> slfac = 
				new SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> slfac2 = 
				new SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( slfac );
		
		final ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> cfac =
				new ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( slfac2 );
		
				
		final CNelemRe cnre = new CNelemRe( slfac );
		
		final CNelemIm cnim = new CNelemIm( slfac );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> z = 
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( cnre , cnim );
		
		
		final HashMap hm2 = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceInitialGuess = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm2;
		
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> basins = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		final ArrayList<Color> colors = new ArrayList<Color>();
		
		
		{
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity() , fac.zero() ) );
			colors.add( Color.RED );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity().negate() , fac.zero() ) );
			colors.add( Color.GREEN );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.zero() , fac.zero() ) );
			colors.add( Color.CYAN );
			
		}
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> function =
				mpow( z , 3 ).add( z.negate() );
		
		final DescentAlgorithmMultiElem<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> stel = genDescent(
				slfac2 , function , cnre , cnim );
		
		
		final BigInteger n2 = N.divide( BigInteger.valueOf( 2 ) );
		final BigFixedPointElem<LrgPrecision> zoomInverse = ZOOM.invertLeft();
		
		for( BigInteger y = BigInteger.ZERO ; y.compareTo( N ) < 0 ; y = y.add( BigInteger.ONE ) )
		{
			
			final BigFixedPointElem<LrgPrecision> yv = buildElem( y.subtract( n2 ) , BigInteger.ONE );
			final BigFixedPointElem<LrgPrecision> yyv = Y_CENTER.add( yv.mult( zoomInverse ).negate() );
			System.out.println( y );
			
			for( BigInteger x = BigInteger.ZERO ; x.compareTo( N ) < 0 ; x = x.add( BigInteger.ONE ) )
			{
				final BigFixedPointElem<LrgPrecision> xv = buildElem( x.subtract( n2 ) , BigInteger.ONE );
				final BigFixedPointElem<LrgPrecision> xxv = X_CENTER.add( xv.mult( zoomInverse ) );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c
					= new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( xxv , yyv );
				
				final TestDimensionTwo td = new TestDimensionTwo();
				
				final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
				
				tempValue = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				tempValue.setVal( VCT_INDEX_RE , c.getRe() );
				
				tempValue.setVal( VCT_INDEX_IM , c.getIm() );
				
				iterationValueCache = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				iterationValueCache.setVal( VCT_INDEX_RE , c.getRe() );
				
				iterationValueCache.setVal( VCT_INDEX_IM , c.getIm() );
				
				resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
					updateValue = getUpdateValue();
				
				// System.out.println( updateValue.getRe().toDouble() + " " + updateValue.getIm().toDouble() );
				
				final Color col =
					findClosestColor( updateValue , basins , colors );
				
				/* System.out.print( x );
				System.out.print( " " );
				System.out.print( y );
				System.out.print( " " );
				System.out.print( col.getRed() );
				System.out.print( " " );
				System.out.print( col.getGreen() );
				System.out.print( " " );
				System.out.println( col.getBlue() ); */
				
				baos.write( col.getRed() );
				baos.write( col.getGreen() );
				baos.write( col.getBlue() );
			}
		}
		
		
		baos.close();
		
	}
	
	
	
	
	/**
	 * Finds the Newtonian Basins of (z^2-(1+3i)^2)(z^2-(5+i)^2)(z^2-(3-2i)^2).
	 * 
	 * Formula from https://mathcs.clarku.edu/~djoyce/newton/examples.html
	 * 
	 * @throws NotInvertibleException
	 */
	public void testNewtonianBasinD() throws NotInvertibleException, Throwable
	{
		ZOOM = buildElem( N , BigInteger.valueOf( 12L ) );
		
		File ofilePpm = new File( "testDm" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> slfac = 
				new SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> slfac2 = 
				new SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( slfac );
		
		final ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> cfac =
				new ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( slfac2 );
		
				
		final CNelemRe cnre = new CNelemRe( slfac );
		
		final CNelemIm cnim = new CNelemIm( slfac );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> z = 
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( cnre , cnim );
		
		
		final HashMap hm2 = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceInitialGuess = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm2;
		
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> basins = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		final ArrayList<Color> colors = new ArrayList<Color>();
		

		
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> C1 = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( buildElem( BigInteger.valueOf( 1L ) , BigInteger.valueOf( 1L ) ) , 
				buildElem( BigInteger.valueOf( 3L ) , BigInteger.valueOf( 1L ) ) );
		
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> C2 = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( buildElem( BigInteger.valueOf( 5L ) , BigInteger.valueOf( 1L ) ) , 
				buildElem( BigInteger.valueOf( 1L ) , BigInteger.valueOf( 1L ) ) );
		
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> C3 = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( buildElem( BigInteger.valueOf( 3L ) , BigInteger.valueOf( 1L ) ) , 
				buildElem( BigInteger.valueOf( -2L ) , BigInteger.valueOf( 1L ) ) );
		
		
		{
			
			basins.add( C1 );
			colors.add( Color.RED );
			
			basins.add( C1.negate() );
			colors.add( Color.GREEN );
			
			basins.add( C2 );
			colors.add( Color.CYAN );
			
			basins.add( C2.negate() );
			colors.add( Color.ORANGE );
			
			basins.add( C3 );
			colors.add( Color.YELLOW );
			
			basins.add( C3.negate() );
			colors.add( Color.MAGENTA );
			
		}
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> c1A =
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>(
						new CConst( C1.getRe() , slfac ) , new CConst( C1.getIm() , slfac ) );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> c2A =
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>(
						new CConst( C2.getRe() , slfac ) , new CConst( C2.getIm() , slfac ) );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> c3A =
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>(
						new CConst( C3.getRe() , slfac ) , new CConst( C3.getIm() , slfac ) );
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fa = mpow( z , 2 ).add( c1A.mult( c1A ).negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fb = mpow( z , 2 ).add( c2A.mult( c2A ).negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fc = mpow( z , 2 ).add( c3A.mult( c3A ).negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
				function = fa.mult( fb ).mult( fc );
		
		final DescentAlgorithmMultiElem<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> stel = genDescent(
				slfac2 , function , cnre , cnim );
		
		
		final BigInteger n2 = N.divide( BigInteger.valueOf( 2 ) );
		final BigFixedPointElem<LrgPrecision> zoomInverse = ZOOM.invertLeft();
		
		for( BigInteger y = BigInteger.ZERO ; y.compareTo( N ) < 0 ; y = y.add( BigInteger.ONE ) )
		{
			
			final BigFixedPointElem<LrgPrecision> yv = buildElem( y.subtract( n2 ) , BigInteger.ONE );
			final BigFixedPointElem<LrgPrecision> yyv = Y_CENTER.add( yv.mult( zoomInverse ).negate() );
			System.out.println( y );
			
			for( BigInteger x = BigInteger.ZERO ; x.compareTo( N ) < 0 ; x = x.add( BigInteger.ONE ) )
			{
				final BigFixedPointElem<LrgPrecision> xv = buildElem( x.subtract( n2 ) , BigInteger.ONE );
				final BigFixedPointElem<LrgPrecision> xxv = X_CENTER.add( xv.mult( zoomInverse ) );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c
					= new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( xxv , yyv );
				
				final TestDimensionTwo td = new TestDimensionTwo();
				
				final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
				
				tempValue = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				tempValue.setVal( VCT_INDEX_RE , c.getRe() );
				
				tempValue.setVal( VCT_INDEX_IM , c.getIm() );
				
				iterationValueCache = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				iterationValueCache.setVal( VCT_INDEX_RE , c.getRe() );
				
				iterationValueCache.setVal( VCT_INDEX_IM , c.getIm() );
				
				resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
					updateValue = getUpdateValue();
				
				// System.out.println( updateValue.getRe().toDouble() + " " + updateValue.getIm().toDouble() );
				
				final Color col =
					findClosestColor( updateValue , basins , colors );
				
				/* System.out.print( x );
				System.out.print( " " );
				System.out.print( y );
				System.out.print( " " );
				System.out.print( col.getRed() );
				System.out.print( " " );
				System.out.print( col.getGreen() );
				System.out.print( " " );
				System.out.println( col.getBlue() ); */
				
				baos.write( col.getRed() );
				baos.write( col.getGreen() );
				baos.write( col.getBlue() );
			}
		}
		
		
		baos.close();
		
	}
	
	
	
	
	
	
	/**
	 * Finds the Newtonian Basins of ( z ^ 4 - 1 ) ( z ^ 2 - ( 1 + i ) ^ 2 ).
	 * 
	 * Formula from https://mathcs.clarku.edu/~djoyce/newton/examples.html
	 * 
	 * @throws NotInvertibleException
	 */
	public void testNewtonianBasinF() throws NotInvertibleException, Throwable
	{
		ZOOM = buildElem( N , BigInteger.valueOf( 4L ) );
		
		File ofilePpm = new File( "testFm" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> slfac = 
				new SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> slfac2 = 
				new SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( slfac );
		
		final ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> cfac =
				new ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( slfac2 );
		
				
		final CNelemRe cnre = new CNelemRe( slfac );
		
		final CNelemIm cnim = new CNelemIm( slfac );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> z = 
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( cnre , cnim );
		
	
		final HashMap hm2 = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceInitialGuess = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm2;
		
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> basins = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		final ArrayList<Color> colors = new ArrayList<Color>();
		
		
		{
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity() , fac.zero() ) );
			colors.add( Color.RED );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity().negate() , fac.zero() ) );
			colors.add( Color.GREEN );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.zero() , fac.identity() ) );
			colors.add( Color.CYAN );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.zero() , fac.identity().negate() ) );
			colors.add( Color.ORANGE );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity() , fac.identity() ) );
			colors.add( Color.YELLOW );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity().negate() , fac.identity().negate() ) );
			colors.add( Color.MAGENTA );
			
		}
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> OPI =
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>(
						new CConst( fac.identity() , slfac ) , new CConst( fac.identity() , slfac ) );
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fa = mpow( z , 4 ).add( cfac.identity().negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fb = mpow( z , 2 ).add( OPI.mult( OPI ).negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
				function = fa.mult( fb );
		
		final DescentAlgorithmMultiElem<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> stel = genDescent(
				slfac2 , function , cnre , cnim );
		
		
		final BigInteger n2 = N.divide( BigInteger.valueOf( 2 ) );
		final BigFixedPointElem<LrgPrecision> zoomInverse = ZOOM.invertLeft();
		
		for( BigInteger y = BigInteger.ZERO ; y.compareTo( N ) < 0 ; y = y.add( BigInteger.ONE ) )
		{
			
			final BigFixedPointElem<LrgPrecision> yv = buildElem( y.subtract( n2 ) , BigInteger.ONE );
			final BigFixedPointElem<LrgPrecision> yyv = Y_CENTER.add( yv.mult( zoomInverse ).negate() );
			System.out.println( y );
			
			for( BigInteger x = BigInteger.ZERO ; x.compareTo( N ) < 0 ; x = x.add( BigInteger.ONE ) )
			{
				final BigFixedPointElem<LrgPrecision> xv = buildElem( x.subtract( n2 ) , BigInteger.ONE );
				final BigFixedPointElem<LrgPrecision> xxv = X_CENTER.add( xv.mult( zoomInverse ) );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c
					= new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( xxv , yyv );
				
				final TestDimensionTwo td = new TestDimensionTwo();
				
				final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
				
				tempValue = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				tempValue.setVal( VCT_INDEX_RE , c.getRe() );
				
				tempValue.setVal( VCT_INDEX_IM , c.getIm() );
				
				iterationValueCache = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				iterationValueCache.setVal( VCT_INDEX_RE , c.getRe() );
				
				iterationValueCache.setVal( VCT_INDEX_IM , c.getIm() );
				
				resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
					updateValue = getUpdateValue();
				
				// System.out.println( updateValue.getRe().toDouble() + " " + updateValue.getIm().toDouble() );
				
				final Color col =
					findClosestColor( updateValue , basins , colors );
				
				/* System.out.print( x );
				System.out.print( " " );
				System.out.print( y );
				System.out.print( " " );
				System.out.print( col.getRed() );
				System.out.print( " " );
				System.out.print( col.getGreen() );
				System.out.print( " " );
				System.out.println( col.getBlue() ); */
				
				baos.write( col.getRed() );
				baos.write( col.getGreen() );
				baos.write( col.getBlue() );
			}
		}
		
		
		baos.close();
		
	}
	
	
	
	
	/**
	 * Finds the Newtonian Basins of ( z ^ 4 - 1 ) ( z ^ 4 - 16 ).
	 * 
	 * Formula from https://mathcs.clarku.edu/~djoyce/newton/examples.html
	 * 
	 * @throws NotInvertibleException
	 */
	public void testNewtonianBasinH() throws NotInvertibleException, Throwable
	{
		ZOOM = buildElem( N , BigInteger.valueOf( 8L ) );
		
		File ofilePpm = new File( "testHm" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> slfac = 
				new SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> slfac2 = 
				new SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( slfac );
		
		final ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> cfac =
				new ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( slfac2 );
		
				
		final CNelemRe cnre = new CNelemRe( slfac );
		
		final CNelemIm cnim = new CNelemIm( slfac );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> z = 
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( cnre , cnim );
		
		
		final HashMap hm2 = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceInitialGuess = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm2;
		
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> basins = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		final ArrayList<Color> colors = new ArrayList<Color>();
		
		
		{
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity() , fac.zero() ) );
			colors.add( Color.RED );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity().negate() , fac.zero() ) );
			colors.add( Color.GREEN );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.zero() , fac.identity() ) );
			colors.add( Color.CYAN );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.zero() , fac.identity().negate() ) );
			colors.add( Color.ORANGE );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(  buildElem( BigInteger.valueOf( 2L ) , BigInteger.valueOf( 1L ) ) , fac.zero() ) );
			colors.add( Color.YELLOW );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(  buildElem( BigInteger.valueOf( -2L ) , BigInteger.valueOf( 1L ) ) , fac.zero() ) );
			colors.add( Color.MAGENTA );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.zero() , buildElem( BigInteger.valueOf( 2L ) , BigInteger.valueOf( 1L ) ) ) );
			colors.add( Color.LIGHT_GRAY );
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.zero() , buildElem( BigInteger.valueOf( -2L ) , BigInteger.valueOf( 1L ) ) ) );
			colors.add( Color.BLUE );
			
		}
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> SXTN =
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>(
						new CConst( buildElem( BigInteger.valueOf( 16L ) , BigInteger.valueOf( 1L ) ) , slfac ) , new CConst( fac.zero() , slfac ) );
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fa = mpow( z , 4 ).add( cfac.identity().negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fb = mpow( z , 4 ).add( SXTN.negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
				function = fa.mult( fb );
		
		final DescentAlgorithmMultiElem<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> stel = genDescent(
				slfac2 , function , cnre , cnim );
		
		
		final BigInteger n2 = N.divide( BigInteger.valueOf( 2 ) );
		final BigFixedPointElem<LrgPrecision> zoomInverse = ZOOM.invertLeft();
		
		for( BigInteger y = BigInteger.ZERO ; y.compareTo( N ) < 0 ; y = y.add( BigInteger.ONE ) )
		{
			
			final BigFixedPointElem<LrgPrecision> yv = buildElem( y.subtract( n2 ) , BigInteger.ONE );
			final BigFixedPointElem<LrgPrecision> yyv = Y_CENTER.add( yv.mult( zoomInverse ).negate() );
			System.out.println( y );
			
			for( BigInteger x = BigInteger.ZERO ; x.compareTo( N ) < 0 ; x = x.add( BigInteger.ONE ) )
			{
				final BigFixedPointElem<LrgPrecision> xv = buildElem( x.subtract( n2 ) , BigInteger.ONE );
				final BigFixedPointElem<LrgPrecision> xxv = X_CENTER.add( xv.mult( zoomInverse ) );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c
					= new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( xxv , yyv );
				
				final TestDimensionTwo td = new TestDimensionTwo();
				
				final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
				
				tempValue = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				tempValue.setVal( VCT_INDEX_RE , c.getRe() );
				
				tempValue.setVal( VCT_INDEX_IM , c.getIm() );
				
				iterationValueCache = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				iterationValueCache.setVal( VCT_INDEX_RE , c.getRe() );
				
				iterationValueCache.setVal( VCT_INDEX_IM , c.getIm() );
				
				resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
					updateValue = getUpdateValue();
				
				// System.out.println( updateValue.getRe().toDouble() + " " + updateValue.getIm().toDouble() );
				
				final Color col =
					findClosestColor( updateValue , basins , colors );
				
				/* System.out.print( x );
				System.out.print( " " );
				System.out.print( y );
				System.out.print( " " );
				System.out.print( col.getRed() );
				System.out.print( " " );
				System.out.print( col.getGreen() );
				System.out.print( " " );
				System.out.println( col.getBlue() ); */
				
				baos.write( col.getRed() );
				baos.write( col.getGreen() );
				baos.write( col.getBlue() );
			}
		}
		
		
		baos.close();
		
	}
	
	
	
	
	/**
	 * Finds the Newtonian Basins of from https://mathcs.clarku.edu/~djoyce/newton/cube.html
	 * 
	 * See https://mathcs.clarku.edu/~djoyce/newton/examples.html
	 * 
	 * @throws NotInvertibleException
	 */
	public void testNewtonianBasinI() throws NotInvertibleException, Throwable
	{
		ZOOM = buildElem( N , BigInteger.valueOf( 2L ) );
		
		File ofilePpm = new File( "testIm" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> slfac = 
				new SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> slfac2 = 
				new SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( slfac );
		
		final ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> cfac =
				new ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( slfac2 );
		
				
		final CNelemRe cnre = new CNelemRe( slfac );
		
		final CNelemIm cnim = new CNelemIm( slfac );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> z = 
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( cnre , cnim );
		
		
		final HashMap hm2 = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceInitialGuess = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm2;
		
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> basins = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		final ArrayList<Color> colors = new ArrayList<Color>();
		
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> C1 = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( buildElem( BigInteger.valueOf( -1384609L ) , BigInteger.valueOf( 1000000L ) ) , 
				buildElem( BigInteger.valueOf( -923100L ) , BigInteger.valueOf( 1000000L ) ) );
		
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> C2 = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( buildElem( BigInteger.valueOf( 384609L ) , BigInteger.valueOf( 1000000L ) ) , 
				buildElem( BigInteger.valueOf( 923100L ) , BigInteger.valueOf( 1000000L ) ) );
		
		
		{
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity() , fac.zero() ) );
			colors.add( Color.RED );
			
			basins.add( C1 );
			colors.add( Color.GREEN );
			
			basins.add( C2 );
			colors.add( Color.BLUE );
			
		}
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> C1A =
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>(
						new CConst( C1.getRe() , slfac ) , new CConst( C1.getIm() , slfac ) );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> C2A =
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>(
						new CConst( C2.getRe() , slfac ) , new CConst( C2.getIm() , slfac ) );
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fa = z.add( cfac.identity().negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fb = z.add( C1A.negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fc = z.add( C2A.negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
				function = fa.mult( fb ).mult( fc );
		
		final DescentAlgorithmMultiElem<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> stel = genDescent(
				slfac2 , function , cnre , cnim );
		
		
		final BigInteger n2 = N.divide( BigInteger.valueOf( 2 ) );
		final BigFixedPointElem<LrgPrecision> zoomInverse = ZOOM.invertLeft();
		
		for( BigInteger y = BigInteger.ZERO ; y.compareTo( N ) < 0 ; y = y.add( BigInteger.ONE ) )
		{
			
			final BigFixedPointElem<LrgPrecision> yv = buildElem( y.subtract( n2 ) , BigInteger.ONE );
			final BigFixedPointElem<LrgPrecision> yyv = Y_CENTER.add( yv.mult( zoomInverse ).negate() );
			System.out.println( y );
			
			for( BigInteger x = BigInteger.ZERO ; x.compareTo( N ) < 0 ; x = x.add( BigInteger.ONE ) )
			{
				final BigFixedPointElem<LrgPrecision> xv = buildElem( x.subtract( n2 ) , BigInteger.ONE );
				final BigFixedPointElem<LrgPrecision> xxv = X_CENTER.add( xv.mult( zoomInverse ) );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c
					= new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( xxv , yyv );
				
				final TestDimensionTwo td = new TestDimensionTwo();
				
				final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
				
				tempValue = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				tempValue.setVal( VCT_INDEX_RE , c.getRe() );
				
				tempValue.setVal( VCT_INDEX_IM , c.getIm() );
				
				iterationValueCache = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				iterationValueCache.setVal( VCT_INDEX_RE , c.getRe() );
				
				iterationValueCache.setVal( VCT_INDEX_IM , c.getIm() );
				
				resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
					updateValue = getUpdateValue();
				
				// System.out.println( updateValue.getRe().toDouble() + " " + updateValue.getIm().toDouble() );
				
				final Color col =
					findClosestColor( updateValue , basins , colors );
				
				/* System.out.print( x );
				System.out.print( " " );
				System.out.print( y );
				System.out.print( " " );
				System.out.print( col.getRed() );
				System.out.print( " " );
				System.out.print( col.getGreen() );
				System.out.print( " " );
				System.out.println( col.getBlue() ); */
				
				baos.write( col.getRed() );
				baos.write( col.getGreen() );
				baos.write( col.getBlue() );
			}
		}
		
		
		baos.close();
		
	}
	
	
	
	
	/**
	 * Finds the Newtonian Basins of from https://mathcs.clarku.edu/~djoyce/newton/cube2.html
	 * 
	 * See https://mathcs.clarku.edu/~djoyce/newton/examples.html
	 * 
	 * @throws NotInvertibleException
	 */
	public void testNewtonianBasinJ() throws NotInvertibleException, Throwable
	{
		ZOOM = buildElem( N.multiply( BigInteger.valueOf( 6 ) ) , BigInteger.valueOf( 2L ) );
		
		File ofilePpm = new File( "testJm" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> slfac = 
				new SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> slfac2 = 
				new SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( slfac );
		
		final ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> cfac =
				new ComplexElemFactory<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( slfac2 );
		
				
		final CNelemRe cnre = new CNelemRe( slfac );
		
		final CNelemIm cnim = new CNelemIm( slfac );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> z = 
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( cnre , cnim );
		
		
		final HashMap hm2 = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceInitialGuess = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm2;
		
		
		final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> basins = new ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>();
		final ArrayList<Color> colors = new ArrayList<Color>();
		
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> C1 = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( buildElem( BigInteger.valueOf( -1384609L ) , BigInteger.valueOf( 1000000L ) ) , 
				buildElem( BigInteger.valueOf( -900000L ) , BigInteger.valueOf( 1000000L ) ) );
		
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> C2 = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( buildElem( BigInteger.valueOf( 384609L ) , BigInteger.valueOf( 1000000L ) ) , 
				buildElem( BigInteger.valueOf( 930000L ) , BigInteger.valueOf( 1000000L ) ) );
		
		
		{
			
			basins.add( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity() , fac.zero() ) );
			colors.add( Color.RED );
			
			basins.add( C1 );
			colors.add( Color.GREEN );
			
			basins.add( C2 );
			colors.add( Color.BLUE );
			
		}
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> C1A =
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>(
						new CConst( C1.getRe() , slfac ) , new CConst( C1.getIm() , slfac ) );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> C2A =
				new ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>(
						new CConst( C2.getRe() , slfac ) , new CConst( C2.getIm() , slfac ) );
		
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fa = z.add( cfac.identity().negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fb = z.add( C1A.negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fc = z.add( C2A.negate() );
		
		final ComplexElem<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
				function = fa.mult( fb ).mult( fc );
		
		final DescentAlgorithmMultiElem<TestDimensionTwo, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> stel = genDescent(
				slfac2 , function , cnre , cnim );
		
		
		final BigInteger n2 = N.divide( BigInteger.valueOf( 2 ) );
		final BigFixedPointElem<LrgPrecision> zoomInverse = ZOOM.invertLeft();
		
		for( BigInteger y = BigInteger.ZERO ; y.compareTo( N ) < 0 ; y = y.add( BigInteger.ONE ) )
		{
			
			final BigFixedPointElem<LrgPrecision> yv = buildElem( y.subtract( n2 ) , BigInteger.ONE );
			final BigFixedPointElem<LrgPrecision> yyv = Y_CENTER.add( yv.mult( zoomInverse ).negate() );
			System.out.println( y );
			
			for( BigInteger x = BigInteger.ZERO ; x.compareTo( N ) < 0 ; x = x.add( BigInteger.ONE ) )
			{
				final BigFixedPointElem<LrgPrecision> xv = buildElem( x.subtract( n2 ) , BigInteger.ONE );
				final BigFixedPointElem<LrgPrecision> xxv = X_CENTER.add( xv.mult( zoomInverse ) );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c
					= new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( xxv , yyv );
				
				final TestDimensionTwo td = new TestDimensionTwo();
				
				final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
				
				tempValue = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				tempValue.setVal( VCT_INDEX_RE , c.getRe() );
				
				tempValue.setVal( VCT_INDEX_IM , c.getIm() );
				
				iterationValueCache = new GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>(fac,td,ord);
				
				iterationValueCache.setVal( VCT_INDEX_RE , c.getRe() );
				
				iterationValueCache.setVal( VCT_INDEX_IM , c.getIm() );
				
				resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
					updateValue = getUpdateValue();
				
				// System.out.println( updateValue.getRe().toDouble() + " " + updateValue.getIm().toDouble() );
				
				final Color col =
					findClosestColor( updateValue , basins , colors );
				
				/* System.out.print( x );
				System.out.print( " " );
				System.out.print( y );
				System.out.print( " " );
				System.out.print( col.getRed() );
				System.out.print( " " );
				System.out.print( col.getGreen() );
				System.out.print( " " );
				System.out.println( col.getBlue() ); */
				
				baos.write( col.getRed() );
				baos.write( col.getGreen() );
				baos.write( col.getBlue() );
			}
		}
		
		
		baos.close();
		
	}
	
	
	
	

}



