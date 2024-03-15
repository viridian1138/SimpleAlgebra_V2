







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
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.WriteElemCache;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.ga.SymbolicDot;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.tdg.ChangeVectorBasis;
import simplealgebra.tdg.DimensionMismatchException;



/**
 * Tests the ChangeVectorBasis class.  
 * This was inspired by an idea from Ship AI about fundamental operations for 
 * vectors from different dimensions.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestChangeVectorBasis extends TestCase 
{
	
	
	/**
	 * The dimensionality for the elem.
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
	 * Node representing the vector to be tested.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class VectElem extends SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
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
		public VectElem(GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> _fac , 
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
			if( b instanceof VectElem )
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
				ps.print( MatrixElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( MatrixElem.class.getSimpleName() );
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
	 * Node representing the vector to be tested.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class VectElem2 extends SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
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
		public VectElem2(GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> _fac , 
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
			if( b instanceof VectElem2 )
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
				ps.print( MatrixElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( MatrixElem.class.getSimpleName() );
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
	 * Node representing the matrix to be tested.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class MatrixElem extends SymbolicElem<SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory>>
	{
		/**
		 * The result for running eval()
		 */
		protected SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory> el;

		/**
		 * Constructs the node.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public MatrixElem(SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory> _fac , 
				SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory> _el ) {
			super(_fac);
			el = _el;
		}

		@Override
		public SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( el );
		}
		
		@Override
		public SquareMatrixElem<Bdim,DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SquareMatrixElem<Bdim,DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<Bdim,DoubleElem, DoubleElemFactory>>, SquareMatrixElem<Bdim,DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public SquareMatrixElem<Bdim,DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SquareMatrixElem<Bdim,DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<Bdim,DoubleElem, DoubleElemFactory>>, SquareMatrixElem<Bdim,DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof MatrixElem )
			{
				return( true );
			}
			
			return( false );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SquareMatrixElem<Bdim, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<Bdim, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SquareMatrixElem<Bdim, DoubleElem, DoubleElemFactory>, SquareMatrixElemFactory<Bdim, DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<SquareMatrixElem<Bdim, DoubleElem, DoubleElemFactory>,SquareMatrixElemFactory<Bdim, DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				final String els = el.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( MatrixElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( MatrixElem.class.getSimpleName() );
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
	 * Tests the evaluation of a ChangeVectorBasis.
	 * 
	 * @throws NotInvertibleException
	 * @throws DimensionMismatchException 
	 * @throws MultiplicativeDistributionRequiredException 
	 */
	public void testChangeVectorBasis() throws NotInvertibleException, DimensionMismatchException, MultiplicativeDistributionRequiredException
	{
		final Bdim tdB = new Bdim();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<Bdim> ordB = new GeometricAlgebraOrd<Bdim>();
		
		final SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory> seA = 
				new SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory>(dl, tdB);
		
		final GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> seB = 
				new GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>(dl, tdB, ordB);
		
		final SquareMatrixElem<Bdim, DoubleElem, DoubleElemFactory> mA =
				seA.identity();
		
		final GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> geB = 
				seB.zero();
		
		final HashSet<BigInteger> axisA = new HashSet<BigInteger>();
		
		axisA.add(BigInteger.valueOf(7));
		
		final HashSet<BigInteger> axisB = new HashSet<BigInteger>();
		
		axisB.add(BigInteger.valueOf(8));
		
		geB.setVal(axisA, new DoubleElem(3.0) );
		
		geB.setVal(axisB, new DoubleElem(5.0) );
		
		mA.remove(BigInteger.valueOf(7), BigInteger.valueOf(7));
		
		mA.remove(BigInteger.valueOf(8), BigInteger.valueOf(8));
		
		mA.setVal(BigInteger.valueOf(7), BigInteger.valueOf(8), new DoubleElem(7.0));
		
		mA.setVal(BigInteger.valueOf(8), BigInteger.valueOf(7), new DoubleElem(11.0));
		
		final SymbolicElemFactory<SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory>> yeA = 
				new SymbolicElemFactory<SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory>>(seA);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>> yeB = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>(seB);
		
		SymbolicElem<SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory>>
			d0 = new MatrixElem( seA , mA );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
			d1 = new VectElem( seB , geB );
		
		final ChangeVectorBasis<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory>
			d01 = new ChangeVectorBasis<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory>(seB, d1, d0);
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace = (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
		GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>, DoubleElem, DoubleElemFactory> 
			result = d01.eval( implicitSpace );
		
		int elemCount = 0;
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> val : result.getEntrySet() )
		{
			elemCount++;
			Assert.assertTrue( val.getKey().size() == 1 ); /* Vector */
			final BigInteger indx = val.getKey().iterator().next();
			final int idx = indx.intValue();
			Assert.assertTrue( ( idx == 7 ) || ( idx == 8 ) );
			final double vl = val.getValue().getVal();
			if( idx == 7 )
			{
				Assert.assertEquals(35.0, vl, 1E-6);
			}
			else
			{
				Assert.assertEquals(33.0, vl, 1E-6);
			}
		}
		
		Assert.assertTrue( elemCount == 2 );
		
	}
	
	
	
	/**
	 * Tests the evaluation of an inner product between vectors of different bases
	 * 
	 * @throws NotInvertibleException
	 * @throws DimensionMismatchException 
	 * @throws MultiplicativeDistributionRequiredException 
	 */
	public void testInnerProductBetweenBases() throws NotInvertibleException, DimensionMismatchException, MultiplicativeDistributionRequiredException
	{
		final Bdim tdB = new Bdim();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraOrd<Bdim> ordB = new GeometricAlgebraOrd<Bdim>();
		
		final SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory> seA = 
				new SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory>(dl, tdB);
		
		final GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> seB = 
				new GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>(dl, tdB, ordB);
		
		final SquareMatrixElem<Bdim, DoubleElem, DoubleElemFactory> mA =
				seA.identity();
		
		final GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> geB = 
				seB.zero();
		
		final GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory> geC = 
				seB.zero();
		
		final HashSet<BigInteger> axisA = new HashSet<BigInteger>();
		
		axisA.add(BigInteger.valueOf(7));
		
		final HashSet<BigInteger> axisB = new HashSet<BigInteger>();
		
		axisB.add(BigInteger.valueOf(8));
		
		geB.setVal(axisA, new DoubleElem(3.0) );
		
		geB.setVal(axisB, new DoubleElem(5.0) );
		
		geC.setVal(axisB, new DoubleElem(13.0) );
		
		mA.remove(BigInteger.valueOf(7), BigInteger.valueOf(7));
		
		mA.remove(BigInteger.valueOf(8), BigInteger.valueOf(8));
		
		mA.setVal(BigInteger.valueOf(7), BigInteger.valueOf(8), new DoubleElem(7.0));
		
		mA.setVal(BigInteger.valueOf(8), BigInteger.valueOf(7), new DoubleElem(11.0));
		
		final SymbolicElemFactory<SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory>> yeA = 
				new SymbolicElemFactory<SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory>>(seA);
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>> yeB = 
				new SymbolicElemFactory<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>(seB);
		
		SymbolicElem<SquareMatrixElem<Bdim,DoubleElem,DoubleElemFactory>,SquareMatrixElemFactory<Bdim,DoubleElem,DoubleElemFactory>>
			d0 = new MatrixElem( seA , mA );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
			d1 = new VectElem( seB , geB );
		
		SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
			d2 = new VectElem2( seB , geC );
		
		final ChangeVectorBasis<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory>
			d01 = new ChangeVectorBasis<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem, DoubleElemFactory>(seB, d1, d0);
		
		ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>> params = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>>();
		
		params.add( d2 );
		
		final SymbolicElem<GeometricAlgebraMultivectorElem<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<Bdim,GeometricAlgebraOrd<Bdim>,DoubleElem,DoubleElemFactory>>
			d02 = d01.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , params ); /* Inner Product */
		
		Assert.assertTrue( d02 instanceof SymbolicDot );
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace = (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
		GeometricAlgebraMultivectorElem<Bdim, GeometricAlgebraOrd<Bdim>, DoubleElem, DoubleElemFactory> 
			result = d02.eval( implicitSpace );
		
		int elemCount = 0;
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> val : result.getEntrySet() )
		{
			elemCount++;
			Assert.assertTrue( val.getKey().size() == 0 ); /* Scalar */
			final double vl = val.getValue().getVal();
			Assert.assertEquals(429.0, vl, 1E-6);
		}
		
		Assert.assertTrue( elemCount == 1 );
		
	}
	
		
	
	
	
	
}




