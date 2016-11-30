






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
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;
import simplealgebra.constants.StandardConstants_SI_Units;
import simplealgebra.constants.StandardConstants_SI_Units_BigFixed;
import simplealgebra.meas.ValueWithUncertaintyElem;



/**
 * Verifies the set of SI constants.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * @author thorngreen
 *
 */
public class TestStandardConstantsSI extends TestCase 
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
	 * Returns whether the absolute value of one parameter is less than or equal to another parameter.
	 * 
	 * @param a First value to be compared.
	 * @param b Second value to be compared.
	 * @return True iff. abs( a ) <= b.
	 */
	protected static boolean isAbsoluteLessThan( BigFixedPointElem<LrgPrecision> a , BigFixedPointElem<LrgPrecision> b )
	{
		boolean ret = ( a.getPrecVal().abs() ).compareTo( b.getPrecVal() ) <= 0;
		return( ret );
	}
	
	
	
	/**
	 * Returns whether a value matches within a tolerance.
	 * 
	 * @param a The value to compare.
	 * @param b The value to be tested against.
	 * @param c The tolerance.
	 * @return True iff. abs( a - b ) <= c
	 */
	protected static boolean isAbsoluteLessThan( BigFixedPointElem<LrgPrecision> a , double b , double c )
	{
		BigFixedPointElem<LrgPrecision> bb = new BigFixedPointElem<LrgPrecision>( b, lrgPrecision );
		BigFixedPointElem<LrgPrecision> cc = new BigFixedPointElem<LrgPrecision>( c, lrgPrecision );
		return( isAbsoluteLessThan( a.add( bb.negate() ) , cc ) );
	}
	
	
	
	
	/**
	 * Runs the test for DoubleElem instances.
	 */
	public void testStandardConstantsSI_Dbl()
	{
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hh = StandardConstants_SI_Units.H;
		
		Assert.assertEquals( hh.getValue().getVal() , 6.62E-34 , 0.01E-34 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hhbar = StandardConstants_SI_Units.HBAR;
		
		Assert.assertEquals( hhbar.getValue().getVal() , 1.05E-34 , 0.01E-34 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hc = StandardConstants_SI_Units.C;
		
		Assert.assertEquals( hc.getValue().getVal() , 3.0E+8 , 0.1E+8 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hg = StandardConstants_SI_Units.G;
		
		Assert.assertEquals( hg.getValue().getVal() , 6.67E-11 , 0.01E-11 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hk = StandardConstants_SI_Units.K;
		
		Assert.assertEquals( hk.getValue().getVal() , 1.38E-23 , 0.01E-23 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> he = StandardConstants_SI_Units.Q_E;
		
		Assert.assertEquals( he.getValue().getVal() , 1.60E-19 , 0.01E-19 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hme = StandardConstants_SI_Units.M_E;
		
		Assert.assertEquals( hme.getValue().getVal() , 9.1E-31 , 0.1E-31 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hmp = StandardConstants_SI_Units.M_P;
		
		Assert.assertEquals( hmp.getValue().getVal() , 1.67E-27 , 0.01E-27 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hPI = StandardConstants_SI_Units.PI;
		
		Assert.assertEquals( hPI.getValue().getVal() , 3.14 , 0.01 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hE = StandardConstants_SI_Units.E;
		
		Assert.assertEquals( hE.getValue().getVal() , 2.7 , 0.1 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hmu_0 = StandardConstants_SI_Units.MU_0;
		
		Assert.assertEquals( hmu_0.getValue().getVal() , 12.566E-7 , 0.001E-7 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hep_0 = StandardConstants_SI_Units.EPSILON_0;
		
		Assert.assertEquals( hep_0.getValue().getVal() , 8.85E-12 , 0.01E-12 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hcoul = StandardConstants_SI_Units.COUL;
		
		Assert.assertEquals( hcoul.getValue().getVal() , 8.99E+9 , 0.01E+9 );
		
		
	}
	
	
	/**
	 * Runs the test for BigFixedPointElem instances.
	 */
	public void testStandardConstantsSI_BigFixed()
	{
	
		final StandardConstants_SI_Units_BigFixed<LrgPrecision> cnst = new StandardConstants_SI_Units_BigFixed<LrgPrecision>( lrgPrecision );
		
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hh = cnst.getH();
		
		Assert.assertTrue( isAbsoluteLessThan( hh.getValue() , 6.62E-34 , 0.01E-34 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hhbar = cnst.getHbar();
		
		Assert.assertTrue( isAbsoluteLessThan( hhbar.getValue() , 1.05E-34 , 0.01E-34 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hc = cnst.getC();
		
		Assert.assertTrue( isAbsoluteLessThan( hc.getValue() , 3.0E+8 , 0.1E+8 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hg = cnst.getG();
		
		Assert.assertTrue( isAbsoluteLessThan( hg.getValue() , 6.67E-11 , 0.01E-11 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hk = cnst.getK();
		
		Assert.assertTrue( isAbsoluteLessThan( hk.getValue() , 1.38E-23 , 0.01E-23 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> he = cnst.getQ_E();
		
		Assert.assertTrue( isAbsoluteLessThan( he.getValue() , 1.60E-19 , 0.01E-19 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hme = cnst.getM_E();
		
		Assert.assertTrue( isAbsoluteLessThan( hme.getValue() , 9.1E-31 , 0.1E-31 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hmp = cnst.getM_P();
		
		Assert.assertTrue( isAbsoluteLessThan( hmp.getValue() , 1.67E-27 , 0.01E-27 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hPI = cnst.getPi();
		
		Assert.assertTrue( isAbsoluteLessThan( hPI.getValue() , 3.14 , 0.01 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hE = cnst.getE();
		
		Assert.assertTrue( isAbsoluteLessThan( hE.getValue() , 2.7 , 0.1 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hmu_0 = cnst.getMu_0();
		
		Assert.assertTrue( isAbsoluteLessThan( hmu_0.getValue() , 12.566E-7 , 0.001E-7 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hep_0 = cnst.getEpsilon_0();
		
		Assert.assertTrue( isAbsoluteLessThan( hep_0.getValue() , 8.85E-12 , 0.01E-12 ) );
		
		final ValueWithUncertaintyElem<BigFixedPointElem<LrgPrecision>,BigFixedPointElemFactory<LrgPrecision>> hcoul = cnst.getCoul();
		
		Assert.assertTrue( isAbsoluteLessThan( hcoul.getValue() , 8.99E+9 , 0.01E+9 ) );
		
		
	}
	
	
}


