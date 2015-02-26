



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
 * 
 * @author thorngreen
 *
 */
public class TestCWave extends TestCase {
	
	
	static final int ARRAY_SZ = 2000;
	
	
	static final double DELTA_X = 0.5;
	
	
	static final double DELTA_T = 1E-10;
	
	
	
	static final BigInteger TEN = BigInteger.valueOf( 10 );
	
	
	
	protected static BigInteger calcVal( final int cnt )
	{
		return( cnt == 0 ? TEN : TEN.multiply( calcVal( cnt - 1 ) ) );
	}
	
	
	// Largest possible double is around E+308
	static final BigInteger baseVal = calcVal( 290 );
	
	

	
	
	protected static final BigInteger finalBaseValSq = baseVal.multiply( baseVal );
	
	
	
	protected static final class LrgPrecision extends Precision
	{
		public BigInteger getVal()
		{
			return( baseVal );
		}
		
		public BigInteger getValSquared()
		{
			return( finalBaseValSq );
		}
	}
	
	
	
	static final LrgPrecision lrgPrecision = new LrgPrecision();
	
	
	
	static final BigFixedPointElem<LrgPrecision> DELTA_X_L
		= new BigFixedPointElem<LrgPrecision>( DELTA_X , lrgPrecision );
	
	
	static final BigFixedPointElem<LrgPrecision> DELTA_T_L
		= new BigFixedPointElem<LrgPrecision>( DELTA_T , lrgPrecision );
	
	
	static final BigFixedPointElem<LrgPrecision> DELTA_T_2_L
		= DELTA_T_L.mult( DELTA_T_L );
	
	
	
	protected BigFixedPointElem<LrgPrecision> sqrtC( BigFixedPointElem<LrgPrecision> in , BigFixedPointElem<LrgPrecision> c ,
			BigFixedPointElem<LrgPrecision> cSquared ) throws NotInvertibleException
	{
		final BigFixedPointElem<LrgPrecision> d = in.add( cSquared.negate() );
		
		BigFixedPointElem<LrgPrecision> sum = c;
		
		sum = sum.add( d.mult( c.invertLeft() ).divideBy( 2 ) );
		
		sum = sum.add( d.mult( d ).mult( c.mult( c ).mult( c ).invertLeft() ).divideBy( 8 ) );
		
		sum = sum.add( d.mult( d ).mult( d ).mult( c.mult( c ).mult( c ).mult( c ).mult( c ).invertLeft() ).divideBy( 16 ) );
		
		return( sum );
	}
	
	
	protected static abstract class ArrayFun
	{
		public abstract BigFixedPointElem<LrgPrecision> getVal( int index ) throws NotInvertibleException;
	}
	
	
	protected static class IndArrayFun extends ArrayFun
	{
		BigFixedPointElem<LrgPrecision> elem[];
		
		public IndArrayFun( BigFixedPointElem<LrgPrecision>[] _elem )
		{
			elem = _elem;
		}
		
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


