







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


import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;

import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.NotInvertibleException;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;
import simplealgebra.constants.CpuInfo;
import simplealgebra.store.DrFastArray2D_Dbl;



/**
 * Test for accurate generation of the Mandelbrot set.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * See http://en.wikipedia.org/wiki/Mandelbrot_set
 * 
 * The example calculated was derived from http://lodev.org/cgtutor/juliamandelbrot.html
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestMandelbrotSet_Ncore extends TestCase {
	
	
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
	 * The maximum number of iterations used to determine the escape time.
	 */
	static final BigInteger MAX_ESCAPE_ITERATIONS = BigInteger.valueOf( 1000 );
	

	
	/**
	 * Constant defining the square of the escape magnitude for stopping the Mandelbrot execution.
	 */
	static final BigFixedPointElem<LrgPrecision> ESCAPE_MAGNITUDE_SQ
		= buildElem( BigInteger.valueOf( 4 ) , BigInteger.ONE );
	
	
	
	/**
	 * Constant defining the X-center for the Mandelbrot execution.
	 */
	static final BigFixedPointElem<LrgPrecision> X_CENTER
		= buildElem( BigInteger.valueOf( -7431533999637661L ) , BigInteger.valueOf( 10000000000000000L ) );
	
	/**
	 * Constant defining the Y-center for the Mandelbrot execution.
	 */
	static final BigFixedPointElem<LrgPrecision> Y_CENTER
		= buildElem( BigInteger.valueOf( -1394057861346605L ) , BigInteger.valueOf( 10000000000000000L ) );
	
	/**
	 * Constant defining the zoom ratio for the Mandelbrot execution.
	 */
	static final BigFixedPointElem<LrgPrecision> ZOOM
		= buildElem( BigInteger.valueOf( 1779803945297549L ) , BigInteger.valueOf( 1000000000000L ) );
	
	
	/**
	 * The X-Y resolution for the Mandelbrot iterations.
	 */
	static final int N_INT = 512;
	
	
	/**
	 * The X-Y resolution for the Mandelbrot iterations.
	 */
	static final BigInteger N = BigInteger.valueOf( N_INT );
	
	/**
	 * The X-Y resolution for the Mandelbrot iterations.
	 */
	static final BigFixedPointElem<LrgPrecision> Nv
		= buildElem( N , BigInteger.valueOf( 1 ) );
	
	
	
	
	/**
	 * Performs a Mandelbrot iteration z <-- z * z + c
	 *
	 * 
	 * @param z The "z" value.
	 * @param c The "c" value.
	 * @return The result of the iteration.
	 */
	static ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
		calcMaldelbrotIteration( ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> z ,
				ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c )
	{
		return( z.mult( z ).add( c ) );
	}
	
	
	/**
	 * Calculates the complex-number magnitude squared for the purpose of determining the escape time.
	 * 
	 * @param z The complex number input.
	 * @return The magnitude squared.
	 */
	static BigFixedPointElem<LrgPrecision> calcMagnitudeSq( ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> z )
	{
		final BigFixedPointElem<LrgPrecision> re = z.getRe();
		final BigFixedPointElem<LrgPrecision> im = z.getIm();
		return( ( re.mult( re ) ).add( im.mult( im ) ) );
	}

	
	
	/**
	 * Returns true if the input value has escaped.
	 * 
	 * @param z The input value.
	 * @return True if the value has escaped.
	 */
	static boolean isEscaped( ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> z )
	{
		BigFixedPointElem<LrgPrecision> magSq = calcMagnitudeSq( z );
		return( ( magSq.getPrecVal().compareTo( ESCAPE_MAGNITUDE_SQ.getPrecVal() ) ) >= 0 );
	}
	
	
	
	/**
	 * Calculates the escape time at the input value.
	 * 
	 * @param c The input value to test.
	 * @return The number of iterations before escape, or MAX_ESCAPE_ITERATIONS if escape was not reached.
	 */
	static BigInteger calcEscapeTime( final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c )
	{
		ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> z = c;
		BigInteger escapeTime = BigInteger.ZERO;
		while( !( isEscaped( z ) ) && ( ( escapeTime.compareTo( MAX_ESCAPE_ITERATIONS ) ) < 0 ) )
		{
			z = calcMaldelbrotIteration( z , c );
			escapeTime = escapeTime.add( BigInteger.ONE );
		}
		return( escapeTime );
	}
	
	
	
	
	protected static final String filePath = "outRaw5.raw";
	
	
	/**
	 * The X-Axis cell size.
	 */
	protected static final int XMULT = 8;
	
	/**
	 * The Y-Axis cell size.
	 */
	protected static final int YMULT = 8;
	

	
	/**
	 * Test for accurate generation of the Mandelbrot set.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testMandelbrot() throws NotInvertibleException, Throwable
	{
		final int numCores = CpuInfo.NUM_CPU_CORES;
		final Runnable[] runn = new Runnable[ numCores ];
		final DataOutputStream dout = new DataOutputStream( new BufferedOutputStream( new FileOutputStream( filePath ) ) );
		final long[] tempOutput = new long[ N_INT ];
		
		
		final BigInteger n2 = N.divide( BigInteger.valueOf( 2 ) );
		final BigFixedPointElem<LrgPrecision> zoomInverse = ZOOM.invertLeft();
		
		for( int y = 0 ; y < N_INT; y++ )
		{
			final int yy = y;
			final BigFixedPointElem<LrgPrecision> yv = buildElem( ( BigInteger.valueOf( y ) ).subtract( n2 ) , BigInteger.ONE );
			final BigFixedPointElem<LrgPrecision> yyv = Y_CENTER.add( yv.mult( zoomInverse ) );
			final boolean[] b = CpuInfo.createBool( false );
			
			for( int ccnt = 0 ; ccnt < numCores ; ccnt++ )
			{
				final int core = ccnt;
				runn[ core ] = new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							for( int x = core ; x < N_INT ; x = x + numCores )
							{
								final BigFixedPointElem<LrgPrecision> xv = buildElem( ( BigInteger.valueOf( x ) ).subtract( n2 ) , BigInteger.ONE );
								final BigFixedPointElem<LrgPrecision> xxv = X_CENTER.add( xv.mult( zoomInverse ) );
							
								final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> c
									= new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( xxv , yyv );
							
								final BigInteger escapeTime = calcEscapeTime( c );
							
								System.out.print( x );
								System.out.print( " " );
								System.out.print( yy );
								System.out.print( " " );
								System.out.println( escapeTime );
							
								tempOutput[ x ] =  escapeTime.longValue();
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
			
			
			for( int cnt = 0 ; cnt < N_INT ; cnt++ )
			{
				dout.writeLong( tempOutput[ cnt ] );
			}
			
			
		}
		
		
		for( int cnt = 0 ; cnt < numCores ; cnt++ )
		{
			dout.close();
		}
		
	}
	
	
	

}



