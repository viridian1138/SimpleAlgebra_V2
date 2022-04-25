






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

import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;
import simplealgebra.constants.StandardConstants_SI_Units_BigFixed;
import simplealgebra.meas.ValueWithUncertaintyElem;
import simplealgebra.tdg.Tdg_EL;



/**
 * Tests the ability to calculate the measurement of an equidistant locus of points in higher dimensions.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * @author thorngreen
 *
 */
public class TestTdg_EL2 extends TestCase 
{

	
	
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
	 * Tests the ability to calculate the measurement of an equidistant locus of points in higher dimensions.
	 */
	public void testTdg_EL() throws Throwable
	{
	
		System.out.println( "Started..." );
		
		final StandardConstants_SI_Units_BigFixed<LrgPrecision> cnst = new StandardConstants_SI_Units_BigFixed<LrgPrecision>( lrgPrecision );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> pi_i = cnst.getPi();
		
		System.out.println( "Got PI..." );
		
		final BigFixedPointElem<LrgPrecision> pi_l = pi_i.getValue();
		
		final ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>
			pi = new ComplexElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( pi_l , pi_l.getFac().zero() );
		
		final Tdg_EL<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> tdg = 
				new Tdg_EL<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>>( pi );
		
		final BigFixedPointElem<LrgPrecision> radius = pi_l.getFac().identity().add( pi_l.getFac().identity() );
		
		final BigInteger numDimensions = BigInteger.valueOf( 6 );
		
		System.out.println( "Running M1" );
		
		BigFixedPointElem<LrgPrecision> m1 = tdg.calcM1(numDimensions, radius, 40);
		
		System.out.println( "Running M2" );
		
		BigFixedPointElem<LrgPrecision> m2 = tdg.calcM2(numDimensions, radius, 40);
		
		System.out.println( "Printing" );
		
		System.out.println( m1.toDouble() );
		
		System.out.println( m2.toDouble() );
		
	}
	
	
	
	/**
	 * Tests the ability to calculate the measurement of an equidistant locus of points in higher dimensions.
	 */
	public void testTdg_E2() throws Throwable
	{
	
		System.out.println( "Started..." );
		
		final DoubleElem pi_l = new DoubleElem( Math.PI );
		
		final ComplexElem<DoubleElem,DoubleElemFactory>
			pi = new ComplexElem<DoubleElem,DoubleElemFactory>( pi_l , pi_l.getFac().zero() );
		
		final Tdg_EL<DoubleElem,DoubleElemFactory> tdg = 
				new Tdg_EL<DoubleElem,DoubleElemFactory>( pi );
		
		final DoubleElem radius = pi_l.getFac().identity().add( pi_l.getFac().identity() );
		
		final BigInteger numDimensions = BigInteger.valueOf( 7 );
		
		System.out.println( "Running M1" );
		
		DoubleElem m1 = tdg.calcM1(numDimensions, radius, 20);
		
		System.out.println( "Running M2" );
		
		DoubleElem m2 = tdg.calcM2(numDimensions, radius, 20);
		
		System.out.println( "Printing" );
		
		System.out.println( m1.getVal() );
		
		System.out.println( m2.getVal() );
		
	}
	
	
	
	/**
	 * Tests the ability to calculate the measurement of an equidistant locus of points in higher dimensions.
	 */
	public void testTdg_E3() throws Throwable
	{
	
		System.out.println( "Started..." );
		
		final DoubleElem pi_l = new DoubleElem( Math.PI );
		
		final ComplexElem<DoubleElem,DoubleElemFactory>
			pi = new ComplexElem<DoubleElem,DoubleElemFactory>( pi_l , pi_l.getFac().zero() );
		
		final Tdg_EL<DoubleElem,DoubleElemFactory> tdg = 
				new Tdg_EL<DoubleElem,DoubleElemFactory>( pi );
		
		final DoubleElem radius = pi_l.getFac().identity().add( pi_l.getFac().identity() );
		
		final BigInteger numDimensions = BigInteger.valueOf( 15 );
		
		System.out.println( "Running M1" );
		
		DoubleElem m1 = tdg.calcM1(numDimensions, radius, 20);
		
		System.out.println( "Running M2" );
		
		DoubleElem m2 = tdg.calcM2(numDimensions, radius, 20);
		
		System.out.println( "Printing" );
		
		System.out.println( m1.getVal() );
		
		System.out.println( m2.getVal() );
		
	}
	
	
	
}


