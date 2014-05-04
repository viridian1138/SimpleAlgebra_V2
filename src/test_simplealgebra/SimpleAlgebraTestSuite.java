



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
 * @author thorngreen
 *
 */
public class SimpleAlgebraTestSuite extends TestSuite {

	
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
		s.addTestSuite( TestInvertNestedOne.class );
		s.addTestSuite( TestInvertNestedTwo.class );
		s.addTestSuite( TestInvertLeftSymbolic.class );
		s.addTestSuite( TestInvertRightSymbolic.class );
		s.addTestSuite( TestInvertNestedLeftNTwo.class );
		s.addTestSuite( TestInvertNestedRightNTwo.class );
		return( s );
	}
	
	
	public static void main( String[] in )
	{
		TestRunner run = new TestRunner();
		run.doRun( suite() );
	}


}

