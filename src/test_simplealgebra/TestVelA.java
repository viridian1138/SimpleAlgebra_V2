




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
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.AbstractCache;
import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.algo.NewtonRaphsonSingleElemCacheFinal;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;
import simplealgebra.bigfixedpoint.WritePrecisionCache;
import simplealgebra.constants.StandardConstants_SI_Units_BigFixed;
import simplealgebra.prec.DefaultPrecedenceComparator;
import simplealgebra.stelem.Nelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;




/**
 * Tests the feasibility of using BigFixedPointElem.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 *
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestVelA extends TestCase {
	
	
	
	
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
     *         <mn>8001</mn>
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
	static final BigInteger baseVal = calcVal( 8000 );
	
	
	/**
	 * Constant containing the square of baseVal, or <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>16002</mn>
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
     *         <mn>8001</mn>
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
	 * Constants in SI units.
	 */
	static final StandardConstants_SI_Units_BigFixed<LrgPrecision> CNST = new StandardConstants_SI_Units_BigFixed<LrgPrecision>( lrgPrecision );
	
	
	
	
	
	/**
	 * Temporary value in which to generate Newton-Raphson solutions.
	 */
	private static BigFixedPointElem<LrgPrecision> tempValue = new BigFixedPointElem<LrgPrecision>( 0.0 , lrgPrecision );
	
	
	
	/**
	 * Given a change calculated by a Newton-Raphson iteration,
	 * applies the change to the temp value.
	 * 
	 * @param dbl The change to apply to the temp value.
	 */
	protected static void performIterationUpdate( BigFixedPointElem<LrgPrecision> dbl )
	{
		tempValue = dbl;
	}
	
	
	/**
	 * The iteration cache value.
	 */
	protected static BigFixedPointElem<LrgPrecision> iterationValueCache = new BigFixedPointElem<LrgPrecision>( 0.0 , lrgPrecision );
	
	
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
	 * from the temp value.
	 * 
	 * @return The temp value.
	 */
	protected static BigFixedPointElem<LrgPrecision> getUpdateValue()
	{
		return( tempValue );
	}
	
	
	
	
	
	/**
	 * Node representing an ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class Ordinate extends SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
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
		public Ordinate(BigFixedPointElemFactory<LrgPrecision> _fac, int _col) {
			super(_fac);
			col = _col;
		}

		@Override
		public BigFixedPointElem<LrgPrecision> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public BigFixedPointElem<LrgPrecision> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, BigFixedPointElem<LrgPrecision>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public BigFixedPointElem<LrgPrecision> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public BigFixedPointElem<LrgPrecision> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, BigFixedPointElem<LrgPrecision>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>)( cache.getInnerCache() ) , ps);
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
		public boolean symbolicEquals( SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> b )
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
	private static class SymbolicConst extends SymbolicReduction<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public SymbolicConst(BigFixedPointElem<LrgPrecision> _elem, BigFixedPointElemFactory<LrgPrecision> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> b )
		{
			if( b instanceof SymbolicConst )
			{
				return( getElem().getPrecVal().equals( ( (SymbolicConst) b ).getElem().getPrecVal() ) );
			}
			return( false );
		}
		
	}
	
	
	
	/**
	 * An elem representing a symbolic constant at the level of  
	 * the original base expression.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class StelemReduction2L extends SymbolicReduction<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public StelemReduction2L(SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _elem, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> b )
		{
			if( b instanceof StelemReduction2L )
			{
				return( getElem().symbolicEquals( ( (StelemReduction2L) b ).getElem() ) );
			}
			return( false );
		}
		
	}
	
	
	
	/**
	 * Elem representing the value constrained by the equation in the Newton-Raphson iterations.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class BNelem extends Nelem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>,Ordinate>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public BNelem(BigFixedPointElemFactory<LrgPrecision> _fac ) {
			super(_fac, new HashMap<Ordinate,BigInteger>() );
		}
		

		@Override
		public BigFixedPointElem<LrgPrecision> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( TestVelA.tempValue );
		}
		
		@Override
		public BigFixedPointElem<LrgPrecision> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, BigFixedPointElem<LrgPrecision>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( BNelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( BNelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> b )
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


		
	}
	
	
	
	/**
	 * Elem representing the value constrained
	 * by the equation at the level of the original equation.
	 * 
	 * @author thorngreen
	 *
	 */	
	private static class CNelem extends Nelem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,
		SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,Ordinate>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public CNelem(SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_fac, new  HashMap<Ordinate, BigInteger>() );
		}

		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( new BNelem( fac.getFac() ) );
		}
		
		
		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			final SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> key =
					new SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>( this , implicitSpace );
			final SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> iret = cache.get( key );
			if( iret != null )
			{
				return( iret );
			}
			final SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> ret = eval( implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		
		
		@Override
		public SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
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
		public SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> evalPartialDerivativeCached(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> cache ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
			final SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> key =
					new SCacheKey<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>( this , implicitSpace , withRespectTo );
			final SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> iret = cache.get( key );
			if( iret != null ) 
			{
				return( iret );
			}
			final SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> ret = evalPartialDerivative( withRespectTo , implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( CNelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( CNelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		
		@Override
		public boolean symbolicEquals( 
				SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> b )
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
	 * Newton-Raphson evaluator for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class StelemNewton extends NewtonRaphsonSingleElemCacheFinal<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
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
				SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>> _function,
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
			return( intCnt > 20 );
		}
		
		@Override
		protected void performIterationUpdate( BigFixedPointElem<LrgPrecision> iterationOffset )
		{
			TestVelA.performIterationUpdate( iterationOffset );
		}
		
		@Override
		protected void cacheIterationValue()
		{
			TestVelA.cacheIterationValue();
		}
		
		@Override
		protected void retrieveIterationValue()
		{
			TestVelA.retrieveIterationValue();
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
		public NewtonRaphsonSingleElemCacheFinal<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> cloneThreadCached(
				CloneThreadCache<SymbolicElem<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>, SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>, SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>> cache,
				CloneThreadCache<?, ?> cacheImplicit, BigInteger threadIndex) {
			throw( new RuntimeException( "Not Supported" ) );
		}
		
	}
	
	
	
	
	/**
	 * Returns a symbolic evaluation of a quartic expression.
	 * @param w Angular frequency.
	 * @param a Constant coefficient.
	 * @param ax Linear coefficient.
	 * @param ax2 Quadratic coefficient.
	 * @param ax3 Cubic coefficient.
	 * @param ax4 Quartic coefficient.
	 * @param x Term over which to evaluate the quartic.
	 * @return The evaluated quartic expression.
	 */
	protected static  SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
		handleEval(  SymbolicElem<
				SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> w ,  SymbolicElem<
				SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> a ,  SymbolicElem<
				SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> ax ,  SymbolicElem<
				SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> ax2,  SymbolicElem<
				SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> ax3 ,  SymbolicElem<
				SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> ax4 ,  SymbolicElem<
				SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> x )
	{
		 final SymbolicElem<
			SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
		 	x2 = x.mult( x );
		 final SymbolicElem<
			SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
		 	x3 = x2.mult( x );
		 final SymbolicElem<
			SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
		 	x4 = x3.mult( x );
		 final SymbolicElem<
			SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
		 	del = a.add( ax.mult( x ) ).add( ax2.mult( x2 ) ).add( ax3.mult( x3 ) ).add( ax4.mult( x4 ) );
		 return( del );
	}
	
	
	
	
	/**
	 * Returns a symbolic evaluation of the expression.
	 * @param w Angular frequency.
	 * @param xc Term over which to evaluate.
	 * @param C The speed of light.
	 * @param M_E The mass of the electron.
	 * @param HBAR The angular form of Planck's constant.
	 * @param se2 Factory for generating elems.
	 * @return The evaluated expression.
	 * @throws NotInvertibleException
	 */
	protected static  SymbolicElem<
	SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	handleEval2(  SymbolicElem<
			SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> w ,  SymbolicElem<
			SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> xc ,  SymbolicElem<
			SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> C ,  SymbolicElem<
			SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> M_E,  SymbolicElem<
			SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> HBAR , SymbolicElemFactory<
			SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> se2  ) throws NotInvertibleException
{
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	w2 = w.mult( w );
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	w3 = w2.mult( w );
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	w4 = w3.mult( w );
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	c2 = C.mult( C );
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	c3 = c2.mult( C );
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	c4 = c3.mult( C );
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	h2 = HBAR.mult( HBAR );
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	h3 = h2.mult( HBAR );
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	h4 = h3.mult( HBAR );
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	a = w4.mult( h2 ).mult( ( c2 ).invertRight() );
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	ax = se2.zero();
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	ax2 = ( h2.mult( w2 ) ).add( M_E.mult( M_E ).mult( c4 ) ).add( h2.mult( w2 ) );
	 
	 final StelemReduction2L TWO = 
		new StelemReduction2L( new SymbolicConst( new BigFixedPointElem<LrgPrecision>( 2.0 , lrgPrecision ) , se2.getFac().getFac()) , se2.getFac());
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	ax3 = h2.mult( TWO ).mult( w ).mult( C ).negate();
	 
	 final SymbolicElem<
		SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>
	 	ax4 = h2.mult( c2 ).negate();
	 
	 return( handleEval( w , a , ax , ax2 , ax3 , ax4 , w.mult( ( xc.mult( C ) ).invertRight( ) ) ) );
}
	
	
	
	
	/**
	 * Tests the feasibility of using BigFixedPointElem.
	 *
	 *
	 */	
	public void testVelSimple() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		DefaultPrecedenceComparator pc = new DefaultPrecedenceComparator();
		
		BigFixedPointElemFactory<LrgPrecision> de = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> se = new SymbolicElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( de );
		
		SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> se2 =
				new SymbolicElemFactory<SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>>( se );
		
		
		
//		final ArrayList<Ordinate> wrtT = new ArrayList<Ordinate>();
//		
//		wrtT.add( new Ordinate( de , 0 ) );
//		
//		final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
//		
//		wrtX.add( new Ordinate( de , 1 ) );
//		
		
		
		
		final HashMap<Ordinate,Ordinate> implicitSpace0 = new HashMap<Ordinate,Ordinate>();
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 = implicitSpace0;
		
//		implicitSpace0.put( new Ordinate( de , 0 ) , new Ordinate( de , 0 ) );
//		implicitSpace0.put( new Ordinate( de , 1 ) , new Ordinate( de , 0 ) );
//		
		
		
		
		
		final StelemReduction2L E = 
				new StelemReduction2L( new SymbolicConst( CNST.getQ_E().getValue() , de) , se);
		
		
		final StelemReduction2L C = 
				new StelemReduction2L( new SymbolicConst( CNST.getC().getValue() , de) , se);
		
		
		final StelemReduction2L M_E = 
				new StelemReduction2L( new SymbolicConst( CNST.getM_E().getValue() , de) , se);
		
		
		final StelemReduction2L HBAR = 
				new StelemReduction2L( new SymbolicConst( CNST.getHbar().getValue() , de) , se);
		
		
		final StelemReduction2L H = 
				new StelemReduction2L( new SymbolicConst( CNST.getH().getValue() , de) , se);
		
		
		
		final CNelem xc = new CNelem( se );
		
		
		
		for( double ww = 10.0 ; ww < 20.0 ; ww += 0.15 )
		{
		
			final BigFixedPointElem<LrgPrecision> wwv = new BigFixedPointElem<LrgPrecision>( ww , lrgPrecision );
			
			final StelemReduction2L w = 
					new StelemReduction2L( new SymbolicConst( wwv , de) , se);
		
			final SymbolicElem<
				SymbolicElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>,SymbolicElemFactory<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>> s0 = 
				handleEval2( w , xc , C , M_E , HBAR , se2 );
		
		

		
			// s0.writeString( System.out );
		
		
		
			final ArrayList<Elem<?, ?>> wrt3 = new ArrayList<Elem<?, ?>>();
			{
				// final HashMap<Ordinate, BigInteger> coord = new HashMap<Ordinate, BigInteger>();
				// coord.put( new Ordinate( de , 0 ) , BigInteger.valueOf( 1 ) );
				// coord.put( new Ordinate( de , 1 ) , BigInteger.valueOf( 0 ) );
				// wrt3.add( new CNelem( se , coord ) );
				wrt3.add( new CNelem( se ) );
			}
			
			
			tempValue = new BigFixedPointElem<LrgPrecision>( 1000.0 , lrgPrecision );
			
			iterationValueCache = new BigFixedPointElem<LrgPrecision>( 1000.0 , lrgPrecision );;
		
		
			final BigFixedPointElem<LrgPrecision> ival = TestVelA.getUpdateValue();
		
			// System.out.println( ival );
		
		
			StelemNewton newton = new StelemNewton( s0 , wrt3 , implicitSpace2 );
		
		
			BigFixedPointElem<LrgPrecision> err = newton.eval( implicitSpace2 );
			
		
			final BigFixedPointElem<LrgPrecision> val = TestVelA.getUpdateValue();
		
		
			// Assert.assertEquals( 100.0 , dbl.getVal() , 1E-4 );
		
		
			System.out.println( "***" );
			System.out.println( ww );
			val.writeMathML( pc , System.out );
			System.out.println( "" );
			err.writeMathML( pc , System.out );
			System.out.println( "" );
		

		
			// Assert.assertTrue( Math.abs( val - ( -1.450868 ) ) < 0.01 );
		
			// Assert.assertTrue( Math.abs( err.getVal() ) < 0.01 );
			
		}
		
		
	}
	

	
}


