






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


import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.constants.StandardConstants_SI_Units;
import simplealgebra.meas.ValueWithUncertaintyElem;



/**
 * Verifies the set of SI constants.
 * 
 * @author thorngreen
 *
 */
public class TestStandardConstantsSI extends TestCase 
{

	
	
	/**
	 * Runs the test.
	 */
	public void testStandardConstantsSI()
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
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hmu_0 = StandardConstants_SI_Units.mu_0;
		
		Assert.assertEquals( hmu_0.getValue().getVal() , 12.566E-7 , 0.001E-7 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hep_0 = StandardConstants_SI_Units.epsilon_0;
		
		Assert.assertEquals( hep_0.getValue().getVal() , 8.85E-12 , 0.01E-12 );
		
		final ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> hcoul = StandardConstants_SI_Units.Coul;
		
		Assert.assertEquals( hcoul.getValue().getVal() , 8.99E+9 , 0.01E+9 );
		
		
	}
	
	
}


