



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

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.NotInvertibleException;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.Precision;


/**
 * 
 * Simple Euler-Method-like evaluator test for the equation <math display="inline">
 * <mrow>
 *  <mfrac>
 *    <mrow>
 *      <msup>
 *              <mo>&PartialD;</mo>
 *            <mn>2</mn>
 *      </msup>
 *      <mi>c</mi>
 *    </mrow>
 *    <mrow>
 *      <mo>&PartialD;</mo>
 *      <msup>
 *              <mi>t</mi>
 *            <mn>2</mn>
 *      </msup>
 *    </mrow>
 *  </mfrac>
 *  <mo>=</mo>
 *  <msup>
 *          <mi>c</mi>
 *        <mn>2</mn>
 *  </msup>
 *  <mfrac>
 *    <mrow>
 *      <msup>
 *              <mo>&PartialD;</mo>
 *            <mn>2</mn>
 *      </msup>
 *      <mi>c</mi>
 *    </mrow>
 *    <mrow>
 *      <mo>&PartialD;</mo>
 *      <msup>
 *              <mi>x</mi>
 *            <mn>2</mn>
 *      </msup>
 *    </mrow>
 *  </mfrac>
 * </mrow>
 * </math>
 *
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestCWave extends TestCase {
	
	/**
	 * The number of discretizations on the X-Axis over which to iterate.
	 */
	static final int ARRAY_SZ = 2000;
	
	/**
	 * Size of the X-Axis discretization.
	 */
	static final double DELTA_X = 0.5;
	
	/**
	 * Size of the T-Axis discretization.
	 */
	static final double DELTA_T = 1E-10;
	
	
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
		return( cnt == 0 ? TEN : TEN.multiply( calcVal( cnt - 1 ) ) );
	}
	

	
	/**
	 * Constant containing the value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>291</mn>
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
	static final BigInteger baseVal = calcVal( 290 );
	
	

	
	/**
	 * Constant containing the square of baseVal, or <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>582</mn>
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
     *         <mn>291</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static final class LrgPrecision extends Precision
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
	 * Constant defining the size of the X-Axis discretization in terms of the large precision.
	 */
	static final BigFixedPointElem<LrgPrecision> DELTA_X_L
		= new BigFixedPointElem<LrgPrecision>( DELTA_X , lrgPrecision );
	
	/**
	 * Constant defining the size of the T-Axis discretization in terms of the large precision.
	 */
	static final BigFixedPointElem<LrgPrecision> DELTA_T_L
		= new BigFixedPointElem<LrgPrecision>( DELTA_T , lrgPrecision );
	
	
	/**
	 * Constant defining the square of the T-Axis discretization in terms of the large precision.
	 */
	static final BigFixedPointElem<LrgPrecision> DELTA_T_2_L
		= DELTA_T_L.mult( DELTA_T_L );
	
	
	/**
	 * Computes a square root using the taylor series approximation <math display="inline">
     * <mrow>
     *   <msqrt>
     *     <msup>
     *             <mi>c</mi>
     *           <mn>2</mn>
     *     </msup>
     *     <mo>+</mo>
     *     <mi>d</mi>
     *   </msqrt>
     *   <mo>&asymp;</mo>
     *   <mi>c</mi>
     *   <mo>+</mo>
     *   <mfrac>
     *     <mrow>
     *       <mi>d</mi>
     *     </mrow>
     *     <mrow>
     *       <mn>2</mn>
     *       <mi>c</mi>
     *     </mrow>
     *   </mfrac>
     *   <mo>-</mo>
     *   <mfrac>
     *     <mrow>
     *       <msup>
     *               <mi>d</mi>
     *             <mn>2</mn>
     *       </msup>
     *     </mrow>
     *     <mrow>
     *       <mn>8</mn>
     *       <msup>
     *               <mi>c</mi>
     *             <mn>3</mn>
     *       </msup>
     *     </mrow>
     *   </mfrac>
     *   <mo>+</mo>
     *   <mfrac>
     *     <mrow>
     *       <msup>
     *               <mi>d</mi>
     *             <mn>3</mn>
     *       </msup>
     *     </mrow>
     *     <mrow>
     *       <mn>16</mn>
     *       <msup>
     *               <mi>c</mi>
     *             <mn>5</mn>
     *       </msup>
     *     </mrow>
     *   </mfrac>
     * </mrow>
     * </math>
	 * <P>
	 * <P>See http://en.wikipedia.org/wiki/Methods_of_computing_square_roots
	 * 
	 * @param in The input parameter on which to take the square root.
	 * @param c The input "c" parameter.
	 * @param cSquared The square of the "c" parameter.
	 * @return The approximate square root of "in".
	 * @throws NotInvertibleException
	 */
	protected BigFixedPointElem<LrgPrecision> sqrtC( BigFixedPointElem<LrgPrecision> in , BigFixedPointElem<LrgPrecision> c ,
			BigFixedPointElem<LrgPrecision> cSquared ) throws NotInvertibleException
	{
		final BigFixedPointElem<LrgPrecision> d = in.add( cSquared.negate() );
		
		BigFixedPointElem<LrgPrecision> sum = c;
		
		sum = sum.add( d.mult( c.invertLeft() ).divideBy( 2 ) );
		
		sum = sum.add( d.mult( d ).mult( c.mult( c ).mult( c ).invertLeft() ).divideBy( 8 ).negate() );
		
		sum = sum.add( d.mult( d ).mult( d ).mult( c.mult( c ).mult( c ).mult( c ).mult( c ).invertLeft() ).divideBy( 16 ) );
		
		return( sum );
	}
	
	
	/**
	 * Abstract class defining a function upon an X-Axis array.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static abstract class ArrayFun
	{
		/**
		 * Returns the value at a particular array index.
		 * 
		 * @param index The array index.
		 * @return The value.
		 * @throws NotInvertibleException
		 */
		public abstract BigFixedPointElem<LrgPrecision> getVal( int index ) throws NotInvertibleException;
	}
	
	
	/**
	 * Defines an array function for a pre-defined array.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class IndArrayFun extends ArrayFun
	{
		/**
		 * The pre-defined array.
		 */
		BigFixedPointElem<LrgPrecision> elem[];
		
		/**
		 * Constructs the array function.
		 * 
		 * @param _elem The pre-defined array.
		 */
		public IndArrayFun( BigFixedPointElem<LrgPrecision>[] _elem )
		{
			elem = _elem;
		}
		
		@Override
		public BigFixedPointElem<LrgPrecision> getVal( int index )
		{
			if( index < 0 )
			{
				return( elem[ 0 ] );
			}
			
			if( index >= ARRAY_SZ )
			{
				return( elem[ ARRAY_SZ - 1 ] );
			}
			
			return( elem[ index ] );
		}
		
	}
	
	
	/**
	 * Array function for returning the discretized derivative <math display="inline">
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
     * </math> where "h" is the size of the discretization.
     * <P>
     * <P> (See http://en.wikipedia.org/wiki/Numerical_differentiation)
	 * 
	 * 
	 * @author tgreen
	 *
	 */
	protected static class DerivativeFun extends ArrayFun
	{
		/**
		 * The input array function.
		 */
		ArrayFun fun;
		
		/**
		 * Constructs the derivative function.
		 * 
		 * @param _fun The input function over which to take the derivative.
		 */
		public DerivativeFun( ArrayFun _fun )
		{
			fun = _fun;
		}
		
		@Override
		public BigFixedPointElem<LrgPrecision> getVal( int index ) throws NotInvertibleException
		{
			return( ( fun.getVal( index + 1 ).add( fun.getVal( index - 1 ).negate() ) ).mult( DELTA_X_L.invertLeft() ).divideBy( 2 ) );
		}
		
	}
	
	
	
	/**
	 * Simple Euler-Method-like evaluator test for the equation <math display="inline">
     * <mrow>
     *  <mfrac>
     *    <mrow>
     *      <msup>
     *              <mo>&PartialD;</mo>
     *            <mn>2</mn>
     *      </msup>
     *      <mi>c</mi>
     *    </mrow>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *      <msup>
     *              <mi>t</mi>
     *            <mn>2</mn>
     *      </msup>
     *    </mrow>
     *  </mfrac>
     *  <mo>=</mo>
     *  <msup>
     *          <mi>c</mi>
     *        <mn>2</mn>
     *  </msup>
     *  <mfrac>
     *    <mrow>
     *      <msup>
     *              <mo>&PartialD;</mo>
     *            <mn>2</mn>
     *      </msup>
     *      <mi>c</mi>
     *    </mrow>
     *    <mrow>
     *      <mo>&PartialD;</mo>
     *      <msup>
     *              <mi>x</mi>
     *            <mn>2</mn>
     *      </msup>
     *    </mrow>
     *  </mfrac>
     * </mrow>
     * </math>
	 * 
	 * @throws NotInvertibleException
	 */
	public void testCWave() throws NotInvertibleException
	{
		
		
		
		final BigFixedPointElem<LrgPrecision> c
			= new BigFixedPointElem<LrgPrecision>( 3E8 , lrgPrecision );
		
		
		final BigFixedPointElem<LrgPrecision> cSquared = c.mult( c );
		
		
		Object[] runs = { (BigFixedPointElem<LrgPrecision>[])( new BigFixedPointElem[ ARRAY_SZ ] ) , 
				(BigFixedPointElem<LrgPrecision>[])( new BigFixedPointElem[ ARRAY_SZ ] ) ,
				(BigFixedPointElem<LrgPrecision>[])( new BigFixedPointElem[ ARRAY_SZ ] ) ,
				(BigFixedPointElem<LrgPrecision>[])( new BigFixedPointElem[ ARRAY_SZ ] ) , 
				(BigFixedPointElem<LrgPrecision>[])( new BigFixedPointElem[ ARRAY_SZ ] )
		};
		
		
		BigFixedPointElem<LrgPrecision>[] elemCsqMaster = (BigFixedPointElem<LrgPrecision>[])( new BigFixedPointElem[ ARRAY_SZ ] );
		
		BigFixedPointElem<LrgPrecision>[] elemCsqRun = (BigFixedPointElem<LrgPrecision>[])( new BigFixedPointElem[ ARRAY_SZ ] );
		
		BigFixedPointElem<LrgPrecision>[] elemCMaster = (BigFixedPointElem<LrgPrecision>[])( new BigFixedPointElem[ ARRAY_SZ ] );
		
		BigFixedPointElem<LrgPrecision>[] elemCRun = (BigFixedPointElem<LrgPrecision>[])( runs[ 0 ] );
		
		
		final BigFixedPointElem<LrgPrecision> addSlope
			= new BigFixedPointElem<LrgPrecision>( 10.0 * DELTA_X , lrgPrecision );
		
		
		BigFixedPointElem<LrgPrecision> t_2Ux_Master
			= new BigFixedPointElem<LrgPrecision>( 10.0 * DELTA_X , lrgPrecision );
		
		
		BigFixedPointElem<LrgPrecision> t_2Ux_Run
			= new BigFixedPointElem<LrgPrecision>( 10.0 * DELTA_X , lrgPrecision );
		
		
		
		int cntx;
		for( cntx = 0 ; cntx < ARRAY_SZ ; cntx++ )
		{
			System.out.println( cntx );
			elemCsqMaster[ cntx ] = cSquared.add( t_2Ux_Master.negate() );
			elemCsqRun[ cntx ] = cSquared.add( t_2Ux_Run.negate() );
			t_2Ux_Master = t_2Ux_Master.add( addSlope );
			if( ( cntx >= ( 475 / DELTA_X ) ) && ( cntx <= ( 525 / DELTA_X ) ) )
			{
				t_2Ux_Run = t_2Ux_Run.add( addSlope.negate() );
			}
			else
			{
				t_2Ux_Run = t_2Ux_Run.add( addSlope );
			}
			
			elemCMaster[ cntx ] = sqrtC( elemCsqMaster[ cntx ] , c , cSquared );
			elemCRun[ cntx ] = sqrtC( elemCsqRun[ cntx ] , c , cSquared );
		}
		
		
		elemCsqMaster = null;
		elemCsqRun = null;
		
		
		
		
		for( int cnty = 1 ; cnty < 4 ; cnty++ )
		{
			final BigFixedPointElem<LrgPrecision>[] elemC2 = (BigFixedPointElem<LrgPrecision>[])( runs[ cnty ] );
			for( cntx = 0 ; cntx < ARRAY_SZ ; cntx++ )
			{
				elemC2[ cntx ] = elemCRun[ cntx ];
			}
		}
	
		
		
		
		final ArrayFun derivMaster = new DerivativeFun( new DerivativeFun( new IndArrayFun( elemCMaster ) ) );
		
		
		
		
		
		{
			ArrayFun derivRun = new DerivativeFun( new DerivativeFun( new IndArrayFun( 
					(BigFixedPointElem<LrgPrecision>[])( runs[ 2 ] ) ) ) );
			
			BigFixedPointElem<LrgPrecision>[] elemCOut = (BigFixedPointElem<LrgPrecision>[])( runs[ 4 ] );
			
			BigFixedPointElem<LrgPrecision>[] elemCDt = (BigFixedPointElem<LrgPrecision>[])( runs[ 0 ] );
			
			
			for( cntx = 0 ; cntx < ARRAY_SZ ; cntx++ )
			{
				BigFixedPointElem<LrgPrecision> dXMaster = derivMaster.getVal( cntx );
				BigFixedPointElem<LrgPrecision> dXRun = derivRun.getVal( cntx );
			
				BigFixedPointElem<LrgPrecision> dXmMaster = dXMaster.mult( elemCMaster[ cntx ] ).mult( elemCMaster[ cntx ] );
			
				BigFixedPointElem<LrgPrecision> dXmRun = dXRun.mult( elemCRun[ cntx ] ).mult( elemCRun[ cntx ] );
			
				BigFixedPointElem<LrgPrecision> dXm = dXmRun.add( dXmMaster.negate() );
				
				BigFixedPointElem<LrgPrecision> dOut = ( elemCDt[ cntx ] ).add( dXm.mult( DELTA_T_2_L ) );
			
				elemCOut[ cntx ] = dOut;
			}
			
		}
		
		
		
		for( int cntt = 0 ; cntt < 7 ; cntt++ )
		{
			Object ob = runs[ 0 ];
			for( int cnti = 0 ; cnti < 4 ; cnti++ )
			{
				runs[ cnti ] = runs[ cnti + 1 ];
			}
			runs[ 4 ] = ob;
			
			ArrayFun derivRun = new DerivativeFun( new DerivativeFun( new IndArrayFun( 
					(BigFixedPointElem<LrgPrecision>[])( runs[ 2 ] ) ) ) );
			
			BigFixedPointElem<LrgPrecision>[] elemCOut = (BigFixedPointElem<LrgPrecision>[])( runs[ 4 ] );
			
			BigFixedPointElem<LrgPrecision>[] elemCDt = (BigFixedPointElem<LrgPrecision>[])( runs[ 0 ] );
			
			
			for( cntx = 0 ; cntx < ARRAY_SZ ; cntx++ )
			{
				BigFixedPointElem<LrgPrecision> dXMaster = derivMaster.getVal( cntx );
				BigFixedPointElem<LrgPrecision> dXRun = derivRun.getVal( cntx );
			
				BigFixedPointElem<LrgPrecision> dXmMaster = dXMaster.mult( elemCMaster[ cntx ] ).mult( elemCMaster[ cntx ] );
			
				BigFixedPointElem<LrgPrecision> dXmRun = dXRun.mult( elemCRun[ cntx ] ).mult( elemCRun[ cntx ] );
			
				BigFixedPointElem<LrgPrecision> dXm = dXmRun.add( dXmMaster.negate() );
			
				BigFixedPointElem<LrgPrecision> dOut = ( elemCDt[ cntx ] ).add( dXm.mult( DELTA_T_2_L ) );
				
				elemCOut[ cntx ] = dOut;
			}
			
			
		}
		
		
		
		{
			BigFixedPointElem<LrgPrecision>[] elemCOut = (BigFixedPointElem<LrgPrecision>[])( runs[ 4 ] );
			
			for( cntx = 1 ; cntx < ARRAY_SZ ; cntx++ )
			{
				System.out.println( cntx );
				
				BigFixedPointElem<LrgPrecision> p0 = elemCOut[ cntx - 1 ];
				BigFixedPointElem<LrgPrecision> p1 = elemCOut[ cntx ];
				
				BigFixedPointElem<LrgPrecision> p0s = p0.mult( p0 );
				BigFixedPointElem<LrgPrecision> p1s = p1.mult( p1 );
				
				BigFixedPointElem<LrgPrecision> p = p1s.add( p0s.negate() );
				BigFixedPointElem<LrgPrecision> pm = p.mult( DELTA_X_L.invertLeft() );
				
				Assert.assertTrue( pm != null );
				System.out.println( pm.toDouble() );
			}
			
		}
		
		
		
		
	}

	
	
}


