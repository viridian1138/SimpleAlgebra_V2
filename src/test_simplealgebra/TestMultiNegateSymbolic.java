






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
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicNegate;



/**
 * Tests symbolic chains of multiple negations.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestMultiNegateSymbolic extends TestCase 
{
	
	
	/**
	 * Tests that multiple negation of the identity yields the identity.
	 * 
	 * @param max The number of negations pairs to chain.
	 * @throws NotInvertibleException
	 */
	public void testMultiNegateElemsB( int max ) throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			d0 = ye.identity();
		
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			d2 = d0;
		
		for( int cnt = 0 ; cnt < max ; cnt++ )
		{
			SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
				d1 = new SymbolicNegate<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>( d2, se );
			
			d2 = new SymbolicNegate<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>( d1 , se );
		}
				
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			d3 = d2.distributeSimplify();
		
		Assert.assertTrue( d3 instanceof SymbolicIdentity );
		
	}
	
	
	/**
	 * Tests that multiple negation of the identity yields the identity.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testMultiNegateElems() throws NotInvertibleException
	{
		testMultiNegateElemsB( 1 );
		testMultiNegateElemsB( 2 );
		testMultiNegateElemsB( 3 );
		testMultiNegateElemsB( 4 );
		testMultiNegateElemsB( 5 );
		testMultiNegateElemsB( 6 );
		testMultiNegateElemsB( 7 );
		testMultiNegateElemsB( 8 );
		testMultiNegateElemsB( 9 );
		testMultiNegateElemsB( 10 );
		testMultiNegateElemsB( 11 );
		testMultiNegateElemsB( 12 );
	}
	

	
}


