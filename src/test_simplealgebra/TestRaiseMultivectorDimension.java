







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
import java.util.HashSet;
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.WriteElemCache;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.ga.SymbolicDot;
import simplealgebra.ga.SymbolicWedge;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.tdg.DimensionMismatchException;
import simplealgebra.tdg.RaiseMultivectorDimension;



/**
 * Tests the RaiseMultivectorDimension class.  
 * This was inspired by an idea from Ship AI about performing products on
 * vectors from different dimensions. Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestRaiseMultivectorDimension extends TestCase 
{
	
	
	/**
	 * The dimensionality for the higher-dimension elem.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class Bdim extends NumDimensions
	{
		
		/**
		 * Constructs the dimension.
		 */
		public Bdim( )
		{
			/* Empty */
		}

		/**
		 * Gets the number of dimensions.
		 * @return The number of dimensions.
		 */
		@Override
		public BigInteger getVal() {
			return( BigInteger.valueOf( 11 ) );
		}
		
	};


	/**
	 * Node representing the variable to be tested for the lower-dimension elem.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class AElem extends SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
	{
		/**
		 * The result for running eval()
		 */
		protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> el;

		/**
		 * Constructs the node.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public AElem(GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> _fac , 
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> _el ) {
			super(_fac);
			el = _el;
		}

		@Override
		public GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( el );
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
				final String els = el.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( AElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( AElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( els );
				ps.println( " );" );
			}
			return( st );
		}
		
		
	}	


	/**
	 * Node representing the variable to be tested for the higher-dimension elem.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class BElem extends SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
	{
		/**
		 * The result for running eval()
		 */
		protected GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> el;

		/**
		 * Constructs the node.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public BElem(GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> _fac , 
				GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> _el ) {
			super(_fac);
			el = _el;
		}

		@Override
		public GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( el );
		}
		
		@Override
		public GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<Bdim, GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<Bdim, GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof BElem )
			{
				return( true );
			}
			
			return( false );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<Bdim, GeometricAlgebraOrd<Bdim>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<Bdim, GeometricAlgebraOrd<Bdim>, DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>, DoubleElem, DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim, GeometricAlgebraOrd<Bdim>, DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				final String els = el.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( BElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( BElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( els );
				ps.println( " );" );
			}
			return( st );
		}
		
		
	}
	
	
	
	/**
	 * Tests that the wedge product of two multivectors in different dimensions.
	 * 
	 * @throws NotInvertibleException
	 * @throws DimensionMismatchException 
	 * @throws MultiplicativeDistributionRequiredException 
	 */
	public void testWedgeAcrossDimensions() throws NotInvertibleException, DimensionMismatchException, MultiplicativeDistributionRequiredException
	{
		final TestDimensionFour tdA = new TestDimensionFour();
		
		final Bdim tdB = new Bdim();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ordA = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraOrd<Bdim> ordB = new GeometricAlgebraOrd<Bdim>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> seA = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, tdA, ordA);
		
		final GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> seB = 
				new GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>(dl, tdB, ordB);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> geA = 
				seA.zero();
		
		final GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> geB = 
				seB.zero();
		
		final HashSet<BigInteger> axisA = new HashSet<BigInteger>();
		
		axisA.add(BigInteger.ONE);
		
		final HashSet<BigInteger> axisB = new HashSet<BigInteger>();
		
		axisB.add(BigInteger.valueOf(7));
		
		geA.setVal(axisA, new DoubleElem(2.0) );
		
		geB.setVal(axisA, new DoubleElem(2.0) );
		
		geB.setVal(axisB, new DoubleElem(7.0) );
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> yeA = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(seA);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>> yeB = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>(seB);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = new AElem( seA , geA );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
			d1 = new BElem( seB , geB );
		
		final RaiseMultivectorDimension<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory, TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>>
			d01 = new RaiseMultivectorDimension<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory, TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>>(seB, d0);
		
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>>();
		
		params.add( d01 );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
			d2 = d1.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , params );
		
		Assert.assertTrue( d2 instanceof SymbolicWedge );
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace = (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
		GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>, DoubleElem, DoubleElemFactory> 
			result = d2.eval( implicitSpace );
		
		int elemCount = 0;
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> val : result.getEntrySet() )
		{
			elemCount++;
			Assert.assertTrue( Math.abs( val.getValue().getVal() - (-14.0) ) < 1E-6 );
			Assert.assertTrue( val.getKey().size() == 2 ); /* Bivector */
			Assert.assertTrue( val.getKey().contains( BigInteger.ONE ) );
			Assert.assertTrue( val.getKey().contains( BigInteger.valueOf(7) ) );
		}
		
		Assert.assertTrue( elemCount == 1 );
		
	}
	
	
	
	/**
	 * Tests that the dot product of two multivectors in different dimensions.
	 * 
	 * @throws NotInvertibleException
	 * @throws DimensionMismatchException 
	 * @throws MultiplicativeDistributionRequiredException 
	 */
	public void testDotAcrossDimensions() throws NotInvertibleException, DimensionMismatchException, MultiplicativeDistributionRequiredException
	{
		final TestDimensionFour tdA = new TestDimensionFour();
		
		final Bdim tdB = new Bdim();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<TestDimensionFour> ordA = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final GeometricAlgebraOrd<Bdim> ordB = new GeometricAlgebraOrd<Bdim>();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> seA = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, tdA, ordA);
		
		final GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> seB = 
				new GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>(dl, tdB, ordB);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> geA = 
				seA.zero();
		
		final GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> geB = 
				seB.zero();
		
		final HashSet<BigInteger> axisA = new HashSet<BigInteger>();
		
		axisA.add(BigInteger.ONE);
		
		final HashSet<BigInteger> axisB = new HashSet<BigInteger>();
		
		axisB.add(BigInteger.valueOf(7));
		
		geA.setVal(axisA, new DoubleElem(2.0) );
		
		geB.setVal(axisA, new DoubleElem(2.0) );
		
		geB.setVal(axisB, new DoubleElem(7.0) );
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> yeA = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>(seA);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>> yeB = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>(seB);
		
		SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
			d0 = new AElem( seA , geA );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
			d1 = new BElem( seB , geB );
		
		final RaiseMultivectorDimension<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory, TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>>
			d01 = new RaiseMultivectorDimension<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory, TestDimensionFour, GeometricAlgebraOrd<TestDimensionFour>>(seB, d0);
		
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>>();
		
		params.add( d01 );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
			d2 = d1.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , params );
		
		Assert.assertTrue( d2 instanceof SymbolicDot );
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace = (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
		GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>, DoubleElem, DoubleElemFactory> 
			result = d2.eval( implicitSpace );
		
		int elemCount = 0;
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> val : result.getEntrySet() )
		{
			elemCount++;
			Assert.assertTrue( Math.abs( val.getValue().getVal() - 4.0 ) < 1E-6 );
			Assert.assertTrue( val.getKey().size() == 0 ); /* Scalar */
		}
		
		Assert.assertTrue( elemCount == 1 );
		
	}
	
		
	
	
	
	
}




