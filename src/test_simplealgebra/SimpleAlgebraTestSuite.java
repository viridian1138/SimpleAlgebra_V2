



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

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Runs standard tests for SimpleAlgebra.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class SimpleAlgebraTestSuite extends TestSuite {

	
	/**
	 * Builds a suite of standard tests for SimpleAlgebra.
	 * 
	 * @return The suite of standard tests.
	 */
	public static Test suite()
	{
		final TestSuite s = new TestSuite();
		s.addTestSuite( TestInvertSimple.class );
		s.addTestSuite( TestInvertNestedLeft.class );
		s.addTestSuite( TestInvertNestedRight.class );
		s.addTestSuite( TestPhasorExample.class );
		s.addTestSuite( TestDiracBraKetNotation.class );
		s.addTestSuite( TestPhasorWithUncertainty.class );
		s.addTestSuite( TestMultivectorInvert.class );
		s.addTestSuite( TestQuaternionInvert.class );
		s.addTestSuite( TestSpacetimeAlgebraInvert.class );
		s.addTestSuite( TestInvertNestedOne.class );
		s.addTestSuite( TestInvertNestedTwo.class );
		s.addTestSuite( TestInvertLeftSymbolic.class );
		s.addTestSuite( TestInvertRightSymbolic.class );
		s.addTestSuite( TestInvertNestedLeftNTwo.class );
		s.addTestSuite( TestInvertNestedRightNTwo.class );
		s.addTestSuite( TestScalarTrig.class );
		s.addTestSuite( TestComplexTrig.class );
		s.addTestSuite( TestMatrixExponential.class );
		s.addTestSuite( TestMatrixExponentialCplx.class );
		s.addTestSuite( TestMultivectorExponential.class );
		s.addTestSuite( TestDoubleNegateSymbolic.class );
		s.addTestSuite( TestDoubleInvertLeft.class );
		s.addTestSuite( TestDoubleInvertRight.class );
		s.addTestSuite( TestAddSimplifySymbolic.class );
		s.addTestSuite( TestSimpMultSymbolic.class );
		s.addTestSuite( TestInvOverMultSymbolic.class );
		s.addTestSuite( TestInvOverNegMultSymbolic.class );
		s.addTestSuite( TestMetricDeterminant.class );
		s.addTestSuite( TestDesResSymbolic.class );
		s.addTestSuite( TestGaugeSymbolic.class );
		s.addTestSuite( TestStelemA.class );
		s.addTestSuite( TestTensorBasics.class );
		s.addTestSuite( TestOrdinaryDerivative.class );
		s.addTestSuite( TestConnectionCoefficient.class );
		s.addTestSuite( TestCovariantDerivative.class );
		s.addTestSuite( TestCovariantDerivativeFlat.class );
		s.addTestSuite( TestRiemannTensor.class );
		s.addTestSuite( TestRicciTensor.class );
		s.addTestSuite( TestRicciScalar.class );
		s.addTestSuite( TestEinsteinTensor.class );
		s.addTestSuite( TestEinsteinTensor_5D.class );
		s.addTestSuite( TestInvertMatrixBasic.class );
		s.addTestSuite( TestPseudoscalar.class );
		s.addTestSuite( TestTensorNotAssociative.class );
		s.addTestSuite( TestInvertLeftSymbolicNonAssoc.class );
		s.addTestSuite( TestInvertRightSymbolicNonAssoc.class );
		s.addTestSuite( TestInvertLeftSymbolicAssoc.class );
		s.addTestSuite( TestInvertRightSymbolicAssoc.class );
		s.addTestSuite( TestNestedNotAssociative.class );
		s.addTestSuite( TestInvertSparse.class );
		s.addTestSuite( TestInvertIdentitySymbolic.class );
		s.addTestSuite( TestSimpleOrdInverses.class );
		s.addTestSuite( TestDoubleDivideBySymbolic.class );
		s.addTestSuite( TestMultNegIdentitySymbolic.class );
		s.addTestSuite( TestBaseDbArrayBasics.class );
		s.addTestSuite( TestBaseDbArrayIndependence.class );
		s.addTestSuite( TestDbElemDbl.class );
		s.addTestSuite( TestScalarWedge.class );
		s.addTestSuite( TestScalarDot.class );
		s.addTestSuite( TestVectorCross.class );
		s.addTestSuite( TestVectorDot.class );
		s.addTestSuite( TestBivectorWedge.class );
		s.addTestSuite( TestGaZeroSymbolic.class );
		s.addTestSuite( TestMatrixOpSymbolic.class );
		s.addTestSuite( TestComplexOpSymbolic.class );
		s.addTestSuite( TestSqrtSymbolic.class );
		s.addTestSuite( TestAbsoluteValueSymbolic.class );
		s.addTestSuite( TestAbsValSymbolic.class );
		s.addTestSuite( TestReductionSymbolic.class );
		return( s );
	}
	
	
	/**
	 * Runs standard tests for SimpleAlgebra.
	 * 
	 * @param in Unused input parameters.
	 */
	public static void main( String[] in )
	{
		TestRunner run = new TestRunner();
		run.doRun( suite() );
	}


}

