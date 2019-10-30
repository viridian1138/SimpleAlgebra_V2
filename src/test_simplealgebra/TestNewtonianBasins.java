







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
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.CloneThreadCache;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.algo.NewtonRaphsonSingleElem;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;
import simplealgebra.stelem.Nelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;



/**
 * Finds Newtonian Basins.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * Formulas from https://mathcs.clarku.edu/~djoyce/newton/examples.html
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestNewtonianBasins extends TestCase {
	
	
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
	
	
	
	
	
	
	
	/**
	 * Temporary iteration value for Newton-Raphson.
	 */
	private static ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> tempValue = null;
	
	
	
	/**
	 * Given a change calculated by a Newton-Raphson iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void performIterationUpdate( ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> dbl )
	{
		tempValue = tempValue.add( dbl );
	}
	
	
	/**
	 * The iteration cache value.
	 */
	protected static ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> iterationValueCache = null;
	
	
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
	protected static ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> getUpdateValue()
	{
		return( tempValue );
	}
	
	
	
	
	
	
	/**
	 * Elem representing the value constrained by the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class BNelem extends SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	{

		/**
		 * Constructs the elem.
		 * @param _fac The input factory.
		 */
		public BNelem(
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_fac);
		}

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( tempValue );
		}

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( this.eval(implicitSpace) );
		}

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
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
	private static class BConst extends SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	{
		
		/**
		 * The constant value.
		 */
		ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> val;

		/**
		 * Constructs the elem.
		 * @param _fac The input factory.
		 */
		public BConst(
				ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> val ,
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_fac);
			this.val = val;
		}

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( val );
		}

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( this.eval(implicitSpace) );
		}

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
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
	private static class CNelem extends SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>
	{

		/**
		 * Constructs the elem.
		 * @param _fac The input factory.
		 */
		public CNelem(
				SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> _fac) {
			super(_fac);
		}

		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( new BNelem( fac.getFac() ) );
		}

		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}

		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( fac.identity() );
		}

		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( evalPartialDerivative( withRespectTo , implicitSpace ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache,
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
	private static class CConst extends SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>
	{
		
		/**
		 * The constant value.
		 */
		ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> val;

		/**
		 * Constructs the elem.
		 * @param _fac The input factory.
		 */
		public CConst(
				ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> val ,
				SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> _fac) {
			super(_fac);
			this.val = val;
		}

		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( new BConst( val , fac.getFac() ) );
		}

		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}

		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( fac.zero() );
		}

		@Override
		public SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache)
				throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			return( evalPartialDerivative( withRespectTo , implicitSpace ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>>> cache,
				PrintStream ps) {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
	}
	
	

	
	
	
	
	
	
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
				SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> _function,
				ArrayList<? extends Elem<?, ?>> _withRespectTo, 
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			super(_function, _withRespectTo, implicitSpaceFirstLevel, null);
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
			return( intCnt > 40 );
		}
		
		/**
		 * Resets the iteration count.
		 */
		public void resetIterations()
		{
			intCnt = 0;
		}
		
		@Override
		protected void performIterationUpdate( ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> iterationOffset )
		{
			TestNewtonianBasins.performIterationUpdate( iterationOffset );
		}
		
		@Override
		protected void cacheIterationValue()
		{
			TestNewtonianBasins.cacheIterationValue();
		}
		
		@Override
		protected void retrieveIterationValue()
		{
			TestNewtonianBasins.retrieveIterationValue();
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
		}
		
		@Override
		public StelemNewton cloneThread( final BigInteger threadIndex )
		{
			throw( new RuntimeException( "Not Supported" ) );
		}

		@Override
		public NewtonRaphsonSingleElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> cloneThreadCached(
				CloneThreadCache<SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>, ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>> cache,
				CloneThreadCache<?, ?> cacheImplicit, BigInteger threadIndex) {
			throw( new RuntimeException( "Not Supported" ) );
		}
		
		@Override
		protected ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> handleDescentInverseFailed( ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> derivative , NotInvertibleException ex )
				throws NotInvertibleException, MultiplicativeDistributionRequiredException
			{
				return( this.function.getFac().getFac().getFac().identity() );
			}
		
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
			final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> updateValue ,
			final ArrayList<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> basins ,
			final ArrayList<Color> colors
			)
	{
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
	static protected SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>  
		mpow( SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>  z , int powr )
	{
		SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>  ret = z;
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
		
		File ofilePpm = new File( "testA" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> cfac = new 
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> sfac = 
				new SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( cfac );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> sfac2 =
				new SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( sfac );
		
		final CNelem z = new CNelem( sfac );
		
		
		final ArrayList ar = new ArrayList();
		ar.add( z );
		final ArrayList<? extends Elem<?, ?>> withRespectTo = (ArrayList<? extends Elem<?, ?>>)( ar );
		
		
		final HashMap hm = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm;
		
		
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
		
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> function =
				mpow( z , 4 ).add( sfac2.identity().negate() );
		
		final StelemNewton stel = new StelemNewton( function , withRespectTo , implicitSpaceFirstLevel );
		
		
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
				
				tempValue = c;
				
				iterationValueCache = c;
				
				stel.resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
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
		
		File ofilePpm = new File( "testB" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> cfac = new 
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> sfac = 
				new SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( cfac );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> sfac2 =
				new SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( sfac );
		
		final CNelem z = new CNelem( sfac );
		
		
		final ArrayList ar = new ArrayList();
		ar.add( z );
		final ArrayList<? extends Elem<?, ?>> withRespectTo = (ArrayList<? extends Elem<?, ?>>)( ar );
		
		
		final HashMap hm = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm;
		
		
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
		
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> function =
				mpow( z , 3 ).add( sfac2.identity().negate() );
		
		final StelemNewton stel = new StelemNewton( function , withRespectTo , implicitSpaceFirstLevel );
		
		
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
				
				tempValue = c;
				
				iterationValueCache = c;
				
				stel.resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
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
		
		File ofilePpm = new File( "testC" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> cfac = new 
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> sfac = 
				new SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( cfac );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> sfac2 =
				new SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( sfac );
		
		final CNelem z = new CNelem( sfac );
		
		
		final ArrayList ar = new ArrayList();
		ar.add( z );
		final ArrayList<? extends Elem<?, ?>> withRespectTo = (ArrayList<? extends Elem<?, ?>>)( ar );
		
		
		final HashMap hm = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm;
		
		
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
		
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> function =
				mpow( z , 3 ).add( z.negate() );
		
		final StelemNewton stel = new StelemNewton( function , withRespectTo , implicitSpaceFirstLevel );
		
		
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
				
				tempValue = c;
				
				iterationValueCache = c;
				
				stel.resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
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
		
		File ofilePpm = new File( "testD" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> cfac = new 
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> sfac = 
				new SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( cfac );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> sfac2 =
				new SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( sfac );
		
		final CNelem z = new CNelem( sfac );
		
		
		final ArrayList ar = new ArrayList();
		ar.add( z );
		final ArrayList<? extends Elem<?, ?>> withRespectTo = (ArrayList<? extends Elem<?, ?>>)( ar );
		
		
		final HashMap hm = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm;
		
		
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
		
		
		final CConst c1A = new CConst( C1 , sfac );
		
		final CConst c2A = new CConst( C2 , sfac );
		
		final CConst c3A = new CConst( C3 , sfac );
		
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fa = mpow( z , 2 ).add( c1A.mult( c1A ).negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fb = mpow( z , 2 ).add( c2A.mult( c2A ).negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fc = mpow( z , 2 ).add( c3A.mult( c3A ).negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
				function = fa.mult( fb ).mult( fc );
		
		final StelemNewton stel = new StelemNewton( function , withRespectTo , implicitSpaceFirstLevel );
		
		
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
				
				tempValue = c;
				
				iterationValueCache = c;
				
				stel.resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
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
		
		File ofilePpm = new File( "testF" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> cfac = new 
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> sfac = 
				new SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( cfac );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> sfac2 =
				new SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( sfac );
		
		final CNelem z = new CNelem( sfac );
		
		
		final ArrayList ar = new ArrayList();
		ar.add( z );
		final ArrayList<? extends Elem<?, ?>> withRespectTo = (ArrayList<? extends Elem<?, ?>>)( ar );
		
		
		final HashMap hm = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm;
		
		
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
		
		
		final CConst OPI = new CConst( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac.identity() , fac.identity() ) , sfac );
		
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fa = mpow( z , 4 ).add( sfac2.identity().negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fb = mpow( z , 2 ).add( OPI.mult( OPI ).negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
				function = fa.mult( fb );
		
		final StelemNewton stel = new StelemNewton( function , withRespectTo , implicitSpaceFirstLevel );
		
		
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
				
				tempValue = c;
				
				iterationValueCache = c;
				
				stel.resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
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
		
		File ofilePpm = new File( "testH" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> cfac = new 
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> sfac = 
				new SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( cfac );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> sfac2 =
				new SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( sfac );
		
		final CNelem z = new CNelem( sfac );
		
		
		final ArrayList ar = new ArrayList();
		ar.add( z );
		final ArrayList<? extends Elem<?, ?>> withRespectTo = (ArrayList<? extends Elem<?, ?>>)( ar );
		
		
		final HashMap hm = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm;
		
		
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
		
		
		final CConst SXTN = new CConst( new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( buildElem( BigInteger.valueOf( 16L ) , BigInteger.valueOf( 1L ) ) , fac.zero() ) , sfac );
		
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fa = mpow( z , 4 ).add( sfac2.identity().negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fb = mpow( z , 4 ).add( SXTN.negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
				function = fa.mult( fb );
		
		final StelemNewton stel = new StelemNewton( function , withRespectTo , implicitSpaceFirstLevel );
		
		
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
				
				tempValue = c;
				
				iterationValueCache = c;
				
				stel.resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
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
		
		File ofilePpm = new File( "testI" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> cfac = new 
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> sfac = 
				new SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( cfac );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> sfac2 =
				new SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( sfac );
		
		final CNelem z = new CNelem( sfac );
		
		
		final ArrayList ar = new ArrayList();
		ar.add( z );
		final ArrayList<? extends Elem<?, ?>> withRespectTo = (ArrayList<? extends Elem<?, ?>>)( ar );
		
		
		final HashMap hm = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm;
		
		
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
		
		
		final CConst C1A = new CConst( C1 , sfac );
		
		final CConst C2A = new CConst( C2 , sfac );
		
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fa = z.add( sfac2.identity().negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fb = z.add( C1A.negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fc = z.add( C2A.negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
				function = fa.mult( fb ).mult( fc );
		
		final StelemNewton stel = new StelemNewton( function , withRespectTo , implicitSpaceFirstLevel );
		
		
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
				
				tempValue = c;
				
				iterationValueCache = c;
				
				stel.resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
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
		
		File ofilePpm = new File( "testJ" + ".ppm" );
		FileOutputStream fo = new FileOutputStream( ofilePpm );
		BufferedOutputStream baos = new BufferedOutputStream( fo );
		
		PrintStream ps = new PrintStream( baos );
		ps.println( "P6" );
		ps.println( "" + ( N ) + " " + ( N ) );
		ps.println( "255" );
		ps.flush();
		ps = null;
		
		final BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		final ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> cfac = new 
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( fac );
		
		final SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> sfac = 
				new SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( cfac );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> sfac2 =
				new SymbolicElemFactory<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>>( sfac );
		
		final CNelem z = new CNelem( sfac );
		
		
		final ArrayList ar = new ArrayList();
		ar.add( z );
		final ArrayList<? extends Elem<?, ?>> withRespectTo = (ArrayList<? extends Elem<?, ?>>)( ar );
		
		
		final HashMap hm = new HashMap();
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>) hm;
		
		
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
		
		
		final CConst C1A = new CConst( C1 , sfac );
		
		final CConst C2A = new CConst( C2 , sfac );
		
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fa = z.add( sfac2.identity().negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fb = z.add( C1A.negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
			fc = z.add( C2A.negate() );
		
		final SymbolicElem<SymbolicElem<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>,SymbolicElemFactory<ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>> 
				function = fa.mult( fb ).mult( fc );
		
		final StelemNewton stel = new StelemNewton( function , withRespectTo , implicitSpaceFirstLevel );
		
		
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
				
				tempValue = c;
				
				iterationValueCache = c;
				
				stel.resetIterations();
				
				stel.eval( implicitSpaceInitialGuess );
				
				final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
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



