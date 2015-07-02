



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
 * Runs standard persistence tests for SimpleAlgebra.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class PersistenceTestSuite extends TestSuite {

	
	/**
	 * Builds a suite of standard tests for SimpleAlgebra.
	 * 
	 * @return The suite of standard tests.
	 */
	public static Test suite()
	{
		final TestSuite s = new TestSuite();
		s.addTestSuite( TestBaseDbArrayBasics.class );
		s.addTestSuite( TestBaseDbArrayIndependence.class );
		s.addTestSuite( TestDbElemDbl.class );
		s.addTestSuite( TestDbElemTensor.class );
		s.addTestSuite( TestDbElemMultivector.class );
		s.addTestSuite( TestDbElemBasics.class );
		s.addTestSuite( TestDbElemSym.class );
		s.addTestSuite( TestDbElemSymbolicTensorResym.class );
		s.addTestSuite( TestQueryIterable.class );
		s.addTestSuite( TestDaqHgDbl.class );
		s.addTestSuite( TestDaqHgCompound.class );
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

