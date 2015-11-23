



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
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ddx.*;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.et.*;
import simplealgebra.symbolic.*;




/**
 * Implements a factory for the tensor <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>v</mi>
 *  </msub>
 * </mrow>
 * </math> where <math display="inline">
 * <mrow>
 *  <mi>v</mi>
 * </mrow>
 * </math> is a tensor index.  This produces a rank-one tensor
 * with a set of partial derivative operators.  The name of the
 * particular index to be used is passed into the class as a
 * parameter.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestOrdinaryDerivative extends TestCase {
	
	
	
	/**
	 * Generates a DoubleElem from a primitive double value.
	 * 
	 * @param in The input primitive double value.
	 * @return The output DoubleElem.
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
	 * Node representing an ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private class Ordinate extends SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>
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
		public void writeString( PrintStream ps ) {
			ps.print( "a" + col + "()" );
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
	private class BElem extends SymbolicElem<EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,EinsteinTensorElemFactory<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
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
		public void writeString( PrintStream ps ) {
			ps.print( "b()" );
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
	private class CElem extends SymbolicElem<DoubleElem,DoubleElemFactory>
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
		public void writeString( PrintStream ps ) {
			ps.print( "c" + col + "()" );
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
	private class SymbolicConst extends SymbolicReduction<DoubleElem,DoubleElemFactory>
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
		public void writeString( PrintStream ps ) {
			ps.print( " " + ( this.getElem().getVal() ) );
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
	 * Defines a directional derivative for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	private class DDirec extends DirectionalDerivativePartialFactory<DoubleElem,DoubleElemFactory,Ordinate>
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
	 * Test method for tensor ordinary derivatives.
	 */
	public void testOrdinaryDerivative() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		
		contravariantIndices.add( "v" );
		
		
		final TestDimensionFour tdim = new TestDimensionFour();
		
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> se2 =
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory> de2 =
				new EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>(de);
		
		
		final EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2s =
				new EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(se2);
		
		
		final DDirec dd = new DDirec( de2 , de );
		
		
		final OrdinaryDerivativeFactory<String, TestDimensionFour, DoubleElem, DoubleElemFactory, Ordinate> ofac =
				new OrdinaryDerivativeFactory<String, TestDimensionFour, DoubleElem, DoubleElemFactory, Ordinate>(se2s, tdim, dd, null);
		
		
		
		
		
		SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> elem
			= new BElem(se2s);
		
		
		
		final SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, 
			SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,
			EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			odir = ofac.getOrdinaryDerivative( elem , "v" );
		
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace
				= (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
		EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ev 
			= odir.eval( implicitSpace );
		
		
		ev.validate();
		
		
		
		
		
		int kcnt = 0;
		for( final Entry<ArrayList<BigInteger>, SymbolicElem<DoubleElem, DoubleElemFactory>> ii : ev.getEntrySet() )
		{
			kcnt++;
			ArrayList<BigInteger> key = ii.getKey();
			Assert.assertTrue( key.size() == 2 );
			final int ind0 = key.get( 0 ).intValue();
			final int ind1 = key.get( 1 ).intValue();
			SymbolicElem<DoubleElem,DoubleElemFactory> el = ii.getValue();
			SymbolicMult<DoubleElem,DoubleElemFactory> sm =
					(SymbolicMult<DoubleElem,DoubleElemFactory>) el;
			PartialDerivativeOp<DoubleElem,DoubleElemFactory,Ordinate> po =
					(PartialDerivativeOp<DoubleElem,DoubleElemFactory,Ordinate>)( sm.getElemA() );
			CElem p1 = (CElem)( sm.getElemB() );
			final ArrayList<Ordinate> ell = po.getWithRespectTo();
			Assert.assertTrue( ell.size() == 1 );
			Ordinate wrt = ell.get( 0 );
			Assert.assertTrue( ind0 == wrt.getCol() );
			Assert.assertTrue( ind1 == p1.getCol() );
		}
		
		Assert.assertTrue( kcnt == 16 );
		
		
	}
	
	
}

