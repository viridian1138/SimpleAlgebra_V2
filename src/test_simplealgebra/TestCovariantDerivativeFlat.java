



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
import java.util.Iterator;
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.ddx.CovariantDerivativeFactory;
import simplealgebra.ddx.CovariantDerivativeFactoryParam;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.MetricTensorFactory;
import simplealgebra.et.MetricTensorInvertingFactory;
import simplealgebra.et.OrdinaryDerivativeFactory;
import simplealgebra.et.TemporaryIndexFactory;
import simplealgebra.prec.DefaultPrecedenceComparator;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.symbolic.SymbolicSqrt;







/**
 * Test verifying that the covariant derivative over a flat plane is
 * the equivalent of an ordinary derivative.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestCovariantDerivativeFlat extends TestCase {
	
	
	
	/**
	 * Returns a DoubleElem with the value from the parameter.
	 * @param in The input parameter.
	 * @return The generated DoubleElem.
	 */
	private static DoubleElem genFromConstDbl( double in )
	{
		return( new DoubleElem( in ) );
	}
	
	
	/**
	 * Arbitrary constant.
	 */
	protected static final DoubleElem C = genFromConstDbl( 0.004 );
	
	
	/**
	 * Cache for symbolic constants.
	 * 
	 * @author tgreen
	 *
	 */
	private static class SymbolicConstCache
	{
		
		/**
		 * Map representing the cache.
		 */
		protected static HashMap<Double,SymbolicConst> map = new HashMap<Double,SymbolicConst>();
		
		/**
		 * Returns a cached SymbolicConst representing a DoubleElem.
		 * 
		 * @param in The DoubleElem to be represented.
		 * @param _fac The factory for DoubleElem instances.
		 * @return The cached SymbolicConst.
		 */
		public static SymbolicConst get(  DoubleElem in , DoubleElemFactory _fac )
		{
			SymbolicConst cnst = map.get( in.getVal() );
			if( cnst == null )
			{
				cnst = new SymbolicConst( in , _fac );
				map.put( in.getVal() , cnst );
			}
			return( cnst );
		}
		
		/**
		 * Clears the cache.
		 */
		public static void clearCache()
		{
			map.clear();
		}
		
	}
	
	
	/**
	 * Node representing an ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class Ordinate extends SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>
	{
		/**
		 * The number of the ordinate.
		 */
		private int col;

		/**
		 * Constructs the node.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _col The number of the ordinate.
		 */
		public Ordinate(EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> _fac, int _col) {
			super(_fac);
			col = _col;
		}

		@Override
		public EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>, EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>,EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory> >)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( Ordinate.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( Ordinate.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( col );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof Ordinate )
			{
				return( col == ( (Ordinate) b ).col );
			}
			return( false );
		}
		
		@Override
		public boolean equals( Object b )
		{
			if( b instanceof Ordinate )
			{
				return( col == ( (Ordinate) b ).col );
			}
			return( false );
		}
		
		@Override
		public int hashCode()
		{
			return( col );
		}
		
		/**
		 * Returns the number of the ordinate.
		 * 
		 * @return The number of the ordinate.
		 */
		public int getCol() {
			return col;
		}
		
	}
	
	
	/**
	 * The symbolic elem for the tensor upon which to apply the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class BElem extends SymbolicElem<EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,EinsteinTensorElemFactory<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public BElem(EinsteinTensorElemFactory<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>> _fac) {
			super(_fac);
		}

		@Override
		public EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			
			final ArrayList<String> contravariantIndices = new ArrayList<String>();
			
			final ArrayList<String> covariantIndices = new ArrayList<String>();
			
			contravariantIndices.add( "u" );
			
			
			final EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
				elem = new EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(fac.getFac(), contravariantIndices, covariantIndices);
			
			for( int cnt = 0 ; cnt < TestDimensionFour.FOUR ; cnt++ )
			{
				CElem ce = new CElem( new DoubleElemFactory() , cnt );
				final ArrayList<BigInteger> key = new ArrayList<BigInteger>();
				key.add( BigInteger.valueOf( cnt ) );
				elem.setVal( key , ce );
			}
			
			return( elem );
		}
		
		@Override
		public EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			final SCacheKey<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> key =
					new SCacheKey<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>( this , implicitSpace );
			final EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> iret = cache.get( key );
			if( iret != null )
			{
				return( iret );
			}
			final EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> ret =
					eval( implicitSpace );
			cache.put(key, ret);
			return( ret );
		}

		@Override
		public EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> >)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( BElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( BElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,EinsteinTensorElemFactory<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> b )
		{
			if( b instanceof BElem )
			{
				return( true );
			}
			return( false );
		}
		
		@Override
		public boolean equals( Object b )
		{
			if( b instanceof BElem )
			{
				return( true );
			}
			return( false );
		}
		
		@Override
		public int hashCode()
		{
			return( 5 );
		}
		
	}
	
	
	
	/**
	 * Symbolic elem for one component of the tensor upon which to apply the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class CElem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{
		/**
		 * The index of the component.
		 */
		private int col;

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _col The index of the component.
		 */
		public CElem(DoubleElemFactory _fac, int _col) {
			super(_fac);
			col = _col;
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
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
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
		public String writeDesc(
				WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem,DoubleElemFactory >)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( CElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( CElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( col );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof CElem )
			{
				return( col == ( (CElem) b ).col );
			}
			return( false );
		}
		
		@Override
		public boolean equals( Object b )
		{
			if( b instanceof CElem )
			{
				return( col == ( (CElem) b ).col );
			}
			return( false );
		}
		
		@Override
		public int hashCode()
		{
			return( col );
		}
		
		/**
		 * Gets the index of the component.
		 * 
		 * @return The index of the component.
		 */
		public int getCol() {
			return col;
		}
		
	}
	
	
	/**
	 * A symbolic elem representing a constant value.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class SymbolicConst extends SymbolicReduction<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public SymbolicConst(DoubleElem _elem, DoubleElemFactory _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof SymbolicConst )
			{
				final boolean eq = ( getElem().getVal() ) == ( ( (SymbolicConst) b ).getElem().getVal() );
				return( eq );
			}
			return( false );
		}
		
	}
	
	
	
	/**
	 * Generates temporary indices for use in tensor arithmetic.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TestTemporaryIndexFactory extends TemporaryIndexFactory<String>
	{
		/**
		 * The current temporary index.
		 */
		protected static int tempIndex = 0;

		@Override
		public String getTemp() {
			String ret = "temp" + tempIndex;
			tempIndex++;
			return( ret );
		}
		
	}
	
	
	
	/**
	 * Defines a directional derivative for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class DDirec extends DirectionalDerivativePartialFactory<DoubleElem,DoubleElemFactory,Ordinate>
	{
		/**
		 * Factory for building the value of the derivative of an ordinate.
		 */
		EinsteinTensorElemFactory<String,DoubleElem, DoubleElemFactory> de;
		
		/**
		 * Factory for the enclosed type.
		 */
		DoubleElemFactory se2;
		
		/**
		 * Constructs the directional derivative factory.
		 * 
		 * @param _de Factory for building the value of the derivative of an ordinate.
		 * @param _se2 Factory for the enclosed type.
		 */
		public DDirec( 
				final EinsteinTensorElemFactory<String,DoubleElem, DoubleElemFactory> _de ,
				final DoubleElemFactory _se2 )
		{
			de = _de;
			se2 = _se2;
		}

		@Override
		public SymbolicElem<DoubleElem, DoubleElemFactory> getPartial(
				BigInteger basisIndex) {
			final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
			
			wrtX.add( new Ordinate( de , basisIndex.intValue() ) );
			
			SymbolicElem<DoubleElem,DoubleElemFactory>
			ret =
					new PartialDerivativeOp<DoubleElem,DoubleElemFactory,Ordinate>( se2 , wrtX );
			return( ret );
		}
		
	}
	
	
	
	/**
	 * An symbolic elem for a tensor.  Used to represent the metric tensor.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class SymbolicMetricTensor extends SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
	{
		
		/**
		 * The tensor to be represented by the symbolic elem.
		 */
		protected EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
			dval = null;

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _dval The tensor to be represented by the symbolic elem.
		 */
		public SymbolicMetricTensor(
				EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> _fac,
				EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> _dval ) {
			super(_fac);
			dval = _dval;
		}

		@Override
		public EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( dval );
		}
		
		@Override
		public EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( dval );
		}

		@Override
		public EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> >)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( BElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( BElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
	}
	
	
	
	/**
	 * Factory for generating an instance of the metric tensor.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TestMetricTensorFactory extends MetricTensorInvertingFactory<String, TestDimensionFour, DoubleElem, DoubleElemFactory>
	{

		@Override
		public SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> getMetricTensor(
				boolean icovariantIndices, String index0, String index1) {
			
			final TestDimensionFour td = new TestDimensionFour();
			final DoubleElemFactory de = new DoubleElemFactory();
			
			final ArrayList<String> contravariantIndices = new ArrayList<String>();
			final ArrayList<String> covariantIndices = new ArrayList<String>();
			if( icovariantIndices ) covariantIndices.add( index0 ); else contravariantIndices.add( index0 );
			if( icovariantIndices ) covariantIndices.add( index1 ); else contravariantIndices.add( index1 );
			
			
			final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
			
			
			EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> ge
				= new EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(seA);
			
			
			
			
			EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> g0 =
						new EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>( 
									seA , contravariantIndices , covariantIndices );
			
			
			
			for( int acnt = 0 ; acnt < TestDimensionFour.FOUR ; acnt++ )
			{
				
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt ) );
				ab.add( BigInteger.valueOf( acnt ) );
				SymbolicElem<DoubleElem,DoubleElemFactory> as = SymbolicConstCache.get( acnt == 0 ? C : new DoubleElem( 1.0 ) , de );
						// acnt == 0 ? new CElem( de , -2 ) : SymbolicConstCache.get( new DoubleElem( 1.0 ) , de );
						// new CElem( de , -acnt );
				g0.setVal( ab , as );
			}
			

			if( !icovariantIndices )
			{
				g0 = genMatrixInverseLeft( td , seA , g0 );
			}
			
			
			final SymbolicMetricTensor seval = new SymbolicMetricTensor( ge , g0 );
			
			
			return( seval );
		}
		
		
		@Override
		public TestMetricTensorFactory cloneThread( final BigInteger threadIndex )
		{
			return( this );
		}
		
		
	}
	
	
	
	
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
	 * Test verifying that the covariant derivative over a flat plane is
     * the equivalent of an ordinary derivative.
	 */
	public void testCovariantDerivative() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		// final ArrayList<String> contravariantIndices = new ArrayList<String>();
		
		// final ArrayList<String> covariantIndices = new ArrayList<String>();
		
		// covariantIndices.add( "v" );
		
		
		final TestDimensionFour tdim = new TestDimensionFour();
		
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> se2 =
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory> de2 =
				new EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>(de);
		
		
		final EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2s =
				new EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(se2);
		
		
		final DDirec dd = new DDirec( de2 , de );
		
		
		// final OrdinaryDerivativeFactory<String, TestDimensionFour, DoubleElem, DoubleElemFactory, Ordinate> ofacI =
		//		new OrdinaryDerivativeFactory<String, TestDimensionFour, DoubleElem, DoubleElemFactory, Ordinate>(se2s, tdim, dd, null);
		
		
		
		MetricTensorFactory<String, DoubleElem, DoubleElemFactory> tmt = new TestMetricTensorFactory();
		
		
		

		
		SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> elem
			= new BElem(se2s);
		
		
		final CovariantDerivativeFactoryParam<String, TestDimensionFour, DoubleElem, DoubleElemFactory, Ordinate>
			param = new CovariantDerivativeFactoryParam<String, TestDimensionFour, DoubleElem, DoubleElemFactory, Ordinate>();
	
	
	
		param.setFac( se2s );
		param.setTensorWithRespectTo( elem );
		param.setDerivativeIndex( "v" );
		
		// This is allowed to be null because the space of the derivative is the same as the tensor to which it is applied.
		param.setCoordVecFac( null );
		
		param.setTemp( new TestTemporaryIndexFactory() );
		param.setMetric( tmt );
		param.setDim( tdim );
		param.setDfac( dd );
		param.setRemap( null );
		
		
		final CovariantDerivativeFactory<String, TestDimensionFour, DoubleElem, DoubleElemFactory, Ordinate> cofac =
			new CovariantDerivativeFactory<String, TestDimensionFour, DoubleElem, DoubleElemFactory, Ordinate>( param );
		
		
		
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace
				= (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
		
		
		final SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, 
			SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,
			EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			odir = cofac.genTerms( implicitSpace );
		
		
		EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ev 
			= odir.eval( implicitSpace );
		
		
		
		ev.validate();
		
		
		
		
		int kcnt = 0;
		for( final Entry<ArrayList<BigInteger>, SymbolicElem<DoubleElem, DoubleElemFactory>> ii : ev.getEntrySet() )
		{
			kcnt++;
			ArrayList<BigInteger> key = ii.getKey();
			Assert.assertTrue( key.size() == 2 );
			final BigInteger k0 = key.get( 0 );
			final BigInteger k1 = key.get( 1 );
	//		System.out.print( key.get( 0 ) );
	//		System.out.print( " " );
	//		System.out.print( key.get( 1 ) );
			final SymbolicElem<DoubleElem,DoubleElemFactory> el =
					ii.getValue();
			final SymbolicElem<DoubleElem,DoubleElemFactory>
				el2 = el.distributeSimplify2();
	//		System.out.print( " " );
	//		el2.writeString( System.out );
	//		System.out.println( "" );
			Assert.assertTrue( el2 instanceof SymbolicMult );
			final SymbolicMult el2a = (SymbolicMult) el2;
			Assert.assertTrue( el2a.getElemA() instanceof PartialDerivativeOp );
			final PartialDerivativeOp el2aa = (PartialDerivativeOp)( el2a.getElemA() );
			Assert.assertTrue( el2a.getElemB() instanceof CElem );
			final CElem el2ab = (CElem)( el2a.getElemB() );
			Assert.assertTrue( k1.equals( BigInteger.valueOf( el2ab.getCol() ) ) );
			final ArrayList<Elem<?,?>> wrt = el2aa.getWithRespectTo();
			Assert.assertTrue( wrt.size() == 1 );
			final Elem<?,?> wrta = wrt.get( 0 );
			Assert.assertTrue( wrta instanceof Ordinate );
			final Ordinate wrtaa = (Ordinate)( wrta );
			Assert.assertTrue( k0.equals( BigInteger.valueOf( wrtaa.getCol() ) ) );
		}
		
		Assert.assertTrue( kcnt == 16 );
		
		
	}
	
	
}

