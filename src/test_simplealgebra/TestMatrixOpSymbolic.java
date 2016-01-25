







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


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.SymbolicTranspose;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicNegate;
import simplealgebra.symbolic.SymbolicOps;
import simplealgebra.symbolic.SymbolicZero;



/**
 * Tests symbolic matrix operators.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestMatrixOpSymbolic extends TestCase 
{


	/**
	 * Node representing the variable to be tested in the expression.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class AElem extends SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the node.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public AElem(SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public SquareMatrixElem<TestDimensionFour,DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SquareMatrixElem<TestDimensionFour,DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour,DoubleElem, DoubleElemFactory>>, SquareMatrixElem<TestDimensionFour,DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public SquareMatrixElem<TestDimensionFour,DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SquareMatrixElem<TestDimensionFour,DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<TestDimensionFour,DoubleElem, DoubleElemFactory>>, SquareMatrixElem<TestDimensionFour,DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof AElem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "a( )" );
		}
		
		
	}
	
	
	
	/**
	 * Tests that the transpose of zero reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTransposeZeroA() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			d1 = ye.zero();
		
		ArrayList<SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>>();
		
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			d2 = d1.handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.TRANSPOSE , params );
		
		Assert.assertTrue( d2 instanceof SymbolicTranspose );
		
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
	
	
	
	
	/**
	 * Tests that the transpose of negative identity reduces to negative identity.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testTransposeNegativeIdentity() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl, td);
		
		final SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			d1 = ye.identity().negate();
		
		ArrayList<SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>>();
		
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			d2 = d1.handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.TRANSPOSE , params );
		
		Assert.assertTrue( d2 instanceof SymbolicTranspose );
		
		SymbolicElem<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicNegate );
		
		
		SymbolicNegate<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			sn = (SymbolicNegate<SquareMatrixElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>) d2a;
		
		
		Assert.assertTrue( ( sn.getElem() ) instanceof SymbolicIdentity );
		
		
	}
	
	
	

	
}

