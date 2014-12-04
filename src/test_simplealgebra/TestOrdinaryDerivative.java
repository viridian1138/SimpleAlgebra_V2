



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
import java.util.Iterator;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.*;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
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
 * @author thorngreen
 * 
 */
public class TestOrdinaryDerivative extends TestCase {
	
	
	
	
	private static DoubleElem genFromConstDbl( double in )
	{
		return( new DoubleElem( in ) );
	}
	
	
	
	protected static final DoubleElem C = genFromConstDbl( 0.004 );
	
	
	
	private class AElem extends SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>
	{
		private int col;

		
		public AElem(EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> _fac, int _col) {
			super(_fac);
			col = _col;
		}

		@Override
		public EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
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
		public String writeString() {
			return( "a" + col + "()" );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof AElem )
			{
				return( col == ( (AElem) b ).col );
			}
			return( false );
		}
		
		@Override
		public boolean equals( Object b )
		{
			if( b instanceof AElem )
			{
				return( col == ( (AElem) b ).col );
			}
			return( false );
		}
		
		@Override
		public int hashCode()
		{
			return( col );
		}
		
		/**
		 * @return the col
		 */
		public int getCol() {
			return col;
		}
		
	}
	
	
	private class BElem extends SymbolicElem<EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,EinsteinTensorElemFactory<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
	{

		
		public BElem(EinsteinTensorElemFactory<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>> _fac) {
			super(_fac);
		}

		@Override
		public EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			
			final ArrayList<String> contravariantIndices = new ArrayList<String>();
			
			final ArrayList<String> covariantIndices = new ArrayList<String>();
			
			covariantIndices.add( "v" );
			
			
			final EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
				elem = new EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(fac.getFac(), contravariantIndices, covariantIndices);
			
			for( int cnt = 0 ; cnt < 4 ; cnt++ )
			{
				CElem ce = new CElem( new DoubleElemFactory() , cnt );
				final ArrayList<BigInteger> key = new ArrayList<BigInteger>();
				key.add( BigInteger.valueOf( cnt ) );
				elem.setVal( key , ce );
			}
			
			return( elem );
		}

		@Override
		public EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeString() {
			return( "b()" );
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
	
	
	
	
	private class CElem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{
		private int col;

		
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
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeString() {
			return( "c" + col + "()" );
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
		 * @return the col
		 */
		public int getCol() {
			return col;
		}
		
	}
	
	
	
	private class SymbolicConst extends SymbolicReduction<DoubleElem,DoubleElemFactory>
	{

		public SymbolicConst(DoubleElem _elem, DoubleElemFactory _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public String writeString() {
			return( " " + ( this.getElem().getVal() ) );
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
	
	
	
	
	
	
	private class DDirec extends DirectionalDerivativePartialFactory<DoubleElem,DoubleElemFactory,AElem>
	{
		EinsteinTensorElemFactory<String,DoubleElem, DoubleElemFactory> de;
		DoubleElemFactory se2;
		
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
			final ArrayList<AElem> wrtX = new ArrayList<AElem>();
			
			wrtX.add( new AElem( de , basisIndex.intValue() ) );
			
			SymbolicElem<DoubleElem,DoubleElemFactory>
			ret =
					new PartialDerivativeOp<DoubleElem,DoubleElemFactory,AElem>( se2 , wrtX );
			return( ret );
		}
		
	}
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.OrdinaryDerivative}.
	 */
	public void testOrdinaryDerivative() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		
		covariantIndices.add( "v" );
		
		
		final TestDimensionFour tdim = new TestDimensionFour();
		
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> se2 =
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory> de2 =
				new EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>(de, contravariantIndices, covariantIndices);
		
		
		final EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2s =
				new EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(se2, contravariantIndices, covariantIndices);
		
		
		final DDirec dd = new DDirec( de2 , de );
		
		
		final OrdinaryDerivativeFactory<String, TestDimensionFour, DoubleElem, DoubleElemFactory, AElem> ofac =
				new OrdinaryDerivativeFactory<String, TestDimensionFour, DoubleElem, DoubleElemFactory, AElem>(se2s, tdim, dd);
		
		
		
		
		
		SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> elem
			= new BElem(se2s);
		
		
		final SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, 
			SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,
			EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			odir = ofac.getOrdinaryDerivative( elem , "u" );
		
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace
				= (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
		EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ev 
			= odir.eval( implicitSpace );
		
		
		ev.validate();
		
		
		int kcnt = 0;
		Iterator<ArrayList<BigInteger>> itA = ev.getKeyIterator();
		while( itA.hasNext() )
		{
			kcnt++;
			ArrayList<BigInteger> key = itA.next();
			Assert.assertTrue( key.size() == 2 );
			final int ind0 = key.get( 0 ).intValue();
			final int ind1 = key.get( 1 ).intValue();
			SymbolicElem<DoubleElem,DoubleElemFactory> el = ev.getVal( key );
			SymbolicMult<DoubleElem,DoubleElemFactory> sm =
					(SymbolicMult<DoubleElem,DoubleElemFactory>) el;
			PartialDerivativeOp<DoubleElem,DoubleElemFactory,AElem> po =
					(PartialDerivativeOp<DoubleElem,DoubleElemFactory,AElem>)( sm.getElemA() );
			CElem p1 = (CElem)( sm.getElemB() );
			final ArrayList<AElem> ell = po.getWithRespectTo();
			Assert.assertTrue( ell.size() == 1 );
			AElem wrt = ell.get( 0 );
			Assert.assertTrue( ind0 == wrt.getCol() );
			Assert.assertTrue( ind1 == p1.getCol() );
		}
		
		Assert.assertTrue( kcnt == 16 );
		
		
	}
	
	
}

