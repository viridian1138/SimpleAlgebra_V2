






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
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicOps;


/**
 * Verifies that <math display="inline">
 * <mrow>
 * <msup>
 *        <mi>a</mi>
 *        <mo>-1L</mo>
 *  </msup>
 *  <mi>a</mi>
 *  </mrow>
 * </math> reduces to the identity for a non-associative elem.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestInvertLeftSymbolicNonAssoc extends TestCase 
{
	
	
	/**
	 * Symbolic elem for <math display="inline">
     * <mrow>
     *  <mi>a</mi>
     * </mrow>
     * </math> to be used in the test.
	 * 
	 * @author tgreen
	 *
	 */
	private static class AElem extends SymbolicElem<EinsteinTensorElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public AElem(EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public EinsteinTensorElem<TestDimensionFour,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public EinsteinTensorElem<TestDimensionFour, DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<EinsteinTensorElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>, EinsteinTensorElem<TestDimensionFour, DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public EinsteinTensorElem<TestDimensionFour,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public EinsteinTensorElem<TestDimensionFour, DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<EinsteinTensorElem<TestDimensionFour, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<TestDimensionFour, DoubleElem, DoubleElemFactory>>, EinsteinTensorElem<TestDimensionFour, DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<EinsteinTensorElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> b )
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
	 * Verifies that <math display="inline">
     * <mrow>
     * <msup>
     *        <mi>a</mi>
     *        <mo>-1L</mo>
     *  </msup>
     *  <mi>a</mi>
     *  </mrow>
     * </math> reduces to the identity for a non-associative elem.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testInvertRightElems() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory> se = 
				new EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>(dl);
		
		final SymbolicElemFactory<EinsteinTensorElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<EinsteinTensorElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>(se);
		
		final SymbolicElem<EinsteinTensorElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			ten = new AElem( se );
		
		
		
		final SymbolicElem<EinsteinTensorElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
			inv = ten.invertLeft();
		
	
		
		final SymbolicElem<EinsteinTensorElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
				mul = inv.mult( ten );
		final SymbolicElem<EinsteinTensorElem<TestDimensionFour,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<TestDimensionFour,DoubleElem,DoubleElemFactory>>
				muls = mul.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null );
		
		
		Assert.assertTrue( muls instanceof SymbolicIdentity );

				
	}
	
	

	
}


