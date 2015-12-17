






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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicOps;
import simplealgebra.symbolic.SymbolicZero;


import simplealgebra.et.*;
import simplealgebra.*;
import java.io.*;


/**
 * Test for generating the determinant of the metric tensor.
 * 
 * http://physics.stackexchange.com/questions/33950/what-is-the-equation-of-the-gravitational-potential-in-general-relativity
 *  
 * http://en.wikipedia.org/wiki/Metric_tensor_%28general_relativity%29
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestMetricDeterminant extends TestCase 
{

	
	/**
	 * Elem representing the square of the speed of light.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class CSquaredElem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public CSquaredElem(DoubleElemFactory _fac ) {
			super(_fac);
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof CSquaredElem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public boolean isPartialDerivativeZero()
		{
			return( true );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "cSquared( )" );
		}

		
	}
	
	
	
	/**
	 * Elem representing the potential term.
	 * 
	 * http://physics.stackexchange.com/questions/33950/what-is-the-equation-of-the-gravitational-potential-in-general-relativity
	 *  
	 * http://en.wikipedia.org/wiki/Metric_tensor_%28general_relativity%29
	 * 
	 * @author thorngreen
	 *
	 */
	private static class T_2UxElem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public T_2UxElem(DoubleElemFactory _fac ) {
			super(_fac);
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof T_2UxElem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "T_2Ux( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<DoubleElem,DoubleElemFactory> pc , PrintStream ps )
		{
			ps.print( "<mn>2</mn><mo>&InvisibleTimes;</mo><mi>U</mi><mfenced><mi>x</mi></mfenced>" );
		}
		
	}
	
	
	
	
	
	/**
	 * Test for generating the determinant of the metric tensor.
	 * 
	 * http://physics.stackexchange.com/questions/33950/what-is-the-equation-of-the-gravitational-potential-in-general-relativity
	 *  
	 * http://en.wikipedia.org/wiki/Metric_tensor_%28general_relativity%29
	 * 
	 * @throws NotInvertibleException
	 */
	public void testMetricDeterminant() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			cSquared = new CSquaredElem( dl );
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			t_2Ux = new T_2UxElem( dl );
		
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> ye = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>(dl);
		
		
		SimpleCurveMetricTensorFactory<Object, DoubleElem,DoubleElemFactory>
			scfac = new SimpleCurveMetricTensorFactory<Object, DoubleElem,DoubleElemFactory>( ye , cSquared , t_2Ux );
		
		
		final Object z0 = new Object()
		{
			
		};
		
		
		final Object z1 = new Object()
		{
			
		};
		
		
		final RankTwoDeterminantFactory<Object, TestDimensionFour, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>> detF =
				new RankTwoDeterminantFactory<Object, TestDimensionFour, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( td );
		
		
		EinsteinTensorElem<Object, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>> 
			metric = scfac.getMetricTensor( true, z0, z1, td.getVal() );
		
		
		SymbolicElem<DoubleElem,DoubleElemFactory> det
			= detF.getDeterminantComponent( metric ).negate();
		
		
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d2 = det.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null);
		
		// System.out.println( d2.writeString() );
		
		
		
		d2 = d2.add( cSquared.negate() );
		
		d2 = d2.add( t_2Ux );
		
		
		
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d3 = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
	
		// System.out.println( d3.writeString() );
		
		
		
		Assert.assertTrue( d3 instanceof SymbolicZero );
		
	}
	
	
	
	
	
}


