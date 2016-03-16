







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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.ddx.DirectionalDerivative;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.ga.SymbolicCross;
import simplealgebra.ga.SymbolicDot;
import simplealgebra.ga.SymbolicDotHestenes;
import simplealgebra.ga.SymbolicReverseLeft;
import simplealgebra.ga.SymbolicReverseRight;
import simplealgebra.ga.SymbolicScalar;
import simplealgebra.ga.SymbolicWedge;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicCosine;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicExponential;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicNegate;
import simplealgebra.symbolic.SymbolicOps;
import simplealgebra.symbolic.SymbolicSine;
import simplealgebra.symbolic.SymbolicZero;



/**
 * Tests GA products with zero.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestGaZeroSymbolic extends TestCase 
{


	/**
	 * Node representing the variable to be tested in the expression.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class AElem extends SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the node.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public AElem(GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof AElem )
			{
				return( true );
			}
			
			return( false );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( AElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( AElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		
	}
	
	
	
	/**
	 * Test directional derivative factory.
	 * 
	 * @author tgreen
	 *
	 */
	private static class DDirec extends DirectionalDerivativePartialFactory<DoubleElem,DoubleElemFactory,DoubleElem>
	{

		@Override
		public SymbolicElem<DoubleElem, DoubleElemFactory> getPartial(
				BigInteger basisIndex) {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
	}
	
	
	
	/**
	 * Tests that the wedge product of an elem and zero reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testWedgeZeroA() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = new AElem( se );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d1 = ye.zero();
		
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>();
		
		params.add( d0 );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2 = d1.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , params );
		
		Assert.assertTrue( d2 instanceof SymbolicWedge );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
	
	
	/**
	 * Tests that the wedge product of an elem and zero reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testWedgeZeroB() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = new AElem( se );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d1 = ye.zero();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>();
	
		params.add( d1 );
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , params );
		
		Assert.assertTrue( d2 instanceof SymbolicWedge );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
	

	
	
	/**
	 * Tests that the dot product of an elem and zero reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDotZeroA() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = new AElem( se );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d1 = ye.zero();
		
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>();
		
		params.add( d0 );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2 = d1.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , params );
		
		Assert.assertTrue( d2 instanceof SymbolicDot );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
	
	
	/**
	 * Tests that the dot product of an elem and zero reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDotZeroB() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = new AElem( se );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d1 = ye.zero();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>();
	
		params.add( d1 );
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , params );
		
		Assert.assertTrue( d2 instanceof SymbolicDot );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}

	
	
	
	
	/**
	 * Tests that the "Hestenes" dot product of an elem and zero reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDotHestenesZeroA() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = new AElem( se );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d1 = ye.zero();
		
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>();
		
		params.add( d0 );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2 = d1.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT_HESTENES , params );
		
		Assert.assertTrue( d2 instanceof SymbolicDotHestenes );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
	
	
	/**
	 * Tests that the "Hestenes" dot product of an elem and zero reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDotHestenesZeroB() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = new AElem( se );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d1 = ye.zero();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>();
	
		params.add( d1 );
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT_HESTENES , params );
		
		Assert.assertTrue( d2 instanceof SymbolicDotHestenes );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}

	
	
	
	
	/**
	 * Tests that the scalar product of an elem and zero reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testScalarZeroA() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = new AElem( se );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d1 = ye.zero();
		
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>();
		
		params.add( d0 );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2 = d1.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.SCALAR , params );
		
		Assert.assertTrue( d2 instanceof SymbolicScalar );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
	
	
	/**
	 * Tests that the scalar product of an elem and zero reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testScalarZeroB() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = new AElem( se );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d1 = ye.zero();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>();
	
		params.add( d1 );
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.SCALAR , params );
		
		Assert.assertTrue( d2 instanceof SymbolicScalar );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
	
	
	/**
	 * Tests that the left reverse of identity reduces to identity.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testReverseLeftIdent() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = ye.identity();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>();
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.REVERSE_LEFT , params );
		
		Assert.assertTrue( d2 instanceof SymbolicReverseLeft );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicIdentity );
		
	}
	
	
	
	/**
	 * Tests that the right reverse of identity reduces to identity.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testReverseRightIdent() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = ye.identity();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>();
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.REVERSE_RIGHT , params );
		
		Assert.assertTrue( d2 instanceof SymbolicReverseRight );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicIdentity );
		
	}
	
		
	
	
	/**
	 * Tests that the dot product of a directional derivative and the identity reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDDirecDotIdentity() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dd = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> dl = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( dd );
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>(se);
		
		final DDirec ddirec = new DDirec();
		
		final DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem> direc = 
				new DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem>( se, td, ord, ddirec );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d0 = direc;
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d1 = ye.identity();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>();
	
		params.add( d1 );
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , params );
		
		Assert.assertTrue( d2 instanceof SymbolicDot );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
		
	
	
	/**
	 * Tests that the "Hestenes" dot product of a directional derivative and the identity reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDDirecDotHestenesIdentity() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dd = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> dl = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( dd );
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>(se);
		
		final DDirec ddirec = new DDirec();
		
		final DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem> direc = 
				new DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem>( se, td, ord, ddirec );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d0 = direc;
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d1 = ye.identity();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>();
	
		params.add( d1 );
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT_HESTENES , params );
		
		Assert.assertTrue( d2 instanceof SymbolicDotHestenes );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
		
	
	
	/**
	 * Tests that the wedge product of a directional derivative and the identity reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDDirecWedgeIdentity() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dd = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> dl = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( dd );
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>(se);
		
		final DDirec ddirec = new DDirec();
		
		final DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem> direc = 
				new DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem>( se, td, ord, ddirec );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d0 = direc;
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d1 = ye.identity();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>();
	
		params.add( d1 );
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , params );
		
		Assert.assertTrue( d2 instanceof SymbolicWedge );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
		
	
	
	/**
	 * Tests that the cross product of a directional derivative and the identity reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDDirecCrossIdentity() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dd = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> dl = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( dd );
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>(se);
		
		final DDirec ddirec = new DDirec();
		
		final DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem> direc = 
				new DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem>( se, td, ord, ddirec );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d0 = direc;
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d1 = ye.identity();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>();
	
		params.add( d1 );
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.CROSS , params );
		
		Assert.assertTrue( d2 instanceof SymbolicCross );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
		
	
	
	/**
	 * Tests that the product of a directional derivative and the identity reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDDirecMultIdentity() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dd = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> dl = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( dd );
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>(se);
		
		final DDirec ddirec = new DDirec();
		
		final DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem> direc = 
				new DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem>( se, td, ord, ddirec );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d0 = direc;
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d1 = ye.identity();
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2 = d0.mult( d1 );
		
		Assert.assertTrue( d2 instanceof SymbolicMult );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
	
	
	
	
	
	
	/**
	 * Tests that the scalar product of a directional derivative and the identity reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDDirecScalarIdentity() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dd = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> dl = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( dd );
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>(se);
		
		final DDirec ddirec = new DDirec();
		
		final DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem> direc = 
				new DirectionalDerivative<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory,DoubleElem>( se, td, ord, ddirec );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d0 = direc;
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d1 = ye.identity();
	
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>>();
	
		params.add( d1 );
	
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2 = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.SCALAR , params );
		
		Assert.assertTrue( d2 instanceof SymbolicScalar );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2a = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
	
	
	
	/**
	 * Tests that the exponential of zero reduces to the identity.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testGaExponentialSymbolic() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dd = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> dl = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( dd );
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d0 = ye.zero();
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d1 = d0.exp( 10 );
		
		Assert.assertTrue( d1 instanceof SymbolicExponential );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2a = d1.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicIdentity );
		
	}
	
	
	
	
	/**
	 * Tests that the sine of zero reduces to zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testGaSineSymbolic() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dd = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> dl = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( dd );
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d0 = ye.zero();
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d1 = d0.sin( 10 );
		
		Assert.assertTrue( d1 instanceof SymbolicSine );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2a = d1.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicZero );
		
	}
	
	
	
	
	/**
	 * Tests that the cosine of zero reduces to the identity.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testGaCosineSymbolic() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dd = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> dl = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( dd );
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(dl, td, ord);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> ye = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>(se);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d0 = ye.zero();
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d1 = d0.cos( 10 );
		
		Assert.assertTrue( d1 instanceof SymbolicCosine );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			d2a = d1.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
		
		Assert.assertTrue( d2a instanceof SymbolicIdentity );
		
	}
	
		
	
	
	
	
}




