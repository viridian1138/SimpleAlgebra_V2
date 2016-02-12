






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


import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.AbsoluteValue;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.prec.DefaultPrecedenceComparator;
import simplealgebra.symbolic.SymbolicAbsoluteValue;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;




/**
 * Tests generation of symbolic reduction expressions.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestReductionSymbolic extends TestCase 
{

	
	/**
	 * Provides precedence comparison rules for the test.  Used when generating MathML.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class PrecCompare extends DefaultPrecedenceComparator<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
	{
		
		
	}
	
	
	
	/**
	 * Tests generation of symbolic reduction expressions.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testReductionSymbolic() throws NotInvertibleException
	{
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> ye = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>(dl);
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ye2 = 
				new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(ye);
		
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
			d0 = ye2.identity();
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d1 = ye.identity();
		
		final SymbolicReduction<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
			d2 = new SymbolicReduction<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( d1 , ye );
		
		final SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
			d3 = d0.add( d2 );
		
		final PrecCompare prec = new PrecCompare();
		
		// d3.writeMathMLWrapped(prec, System.out);
		
		
	}
	
	

	
}


