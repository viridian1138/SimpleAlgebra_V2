






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




/**
 * Tests generation of absolute value expressions.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestAbsValSymbolic extends TestCase 
{

	
	/**
	 * Provides precedence comparison rules for the test.  Used when generating MathML.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class PrecCompare extends DefaultPrecedenceComparator
	{
		
		
	}
	
	
	
	/**
	 * Tests generation of absolute value expressions.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testAbsValSymbolic() throws NotInvertibleException
	{
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> ye = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>(dl);
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d0 = ye.identity();
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d1 = d0.divideBy( 1234 );
		
		final ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>>
			args = new ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>>();
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d2 = d1.handleOptionalOp( AbsoluteValue.ABSOLUTE_VALUE , args);
		
		Assert.assertTrue( d2 instanceof SymbolicAbsoluteValue );
		
		final PrecCompare prec = new PrecCompare();
		
		// d2.writeMathMLWrapped(prec, System.out);
		
		
	}
	
	

	
}


