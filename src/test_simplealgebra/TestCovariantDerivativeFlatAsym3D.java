



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

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.ddx.CovariantDerivativeFactory;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.MetricTensorFactory;
import simplealgebra.et.MetricTensorInvertingFactory;
import simplealgebra.et.OrdinaryDerivativeFactory;
import simplealgebra.et.SymbolicTensorResym;
import simplealgebra.et.TemporaryIndexFactory;
import simplealgebra.et.SymbolicTensorResym.ResymType;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicOps;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.symbolic.SymbolicSqrt;







public class TestCovariantDerivativeFlatAsym3D extends TestCase {
	
	
	
	
	private static DoubleElem genFromConstDbl( double in )
	{
		return( new DoubleElem( in ) );
	}
	
	
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
		public EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
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
			
			contravariantIndices.add( "u" );
			
			
			final EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
				elem = new EinsteinTensorElem<String,SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>(fac.getFac(), contravariantIndices, covariantIndices);
			
			for( int cnt = 0 ; cnt < 3 ; cnt++ )
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
	
	
	
	
	private static class CElem extends SymbolicElem<DoubleElem,DoubleElemFactory>
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
		 * @return the col
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
	
	
	
	
	protected static class TestTemporaryIndexFactory extends TemporaryIndexFactory<String>
	{
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
	private class DDirec extends DirectionalDerivativePartialFactory<DoubleElem,DoubleElemFactory,Ordinate>
	{
		/**
		 * Factory for the enclosed type.
		 */
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
			final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
			
			wrtX.add( new Ordinate( de , basisIndex.intValue() ) );
			
			SymbolicElem<DoubleElem,DoubleElemFactory>
			ret =
					new PartialDerivativeOp<DoubleElem,DoubleElemFactory,Ordinate>( se2 , wrtX );
			return( ret );
		}
		
	}
	
	
	
	protected static class SEvalElem extends SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
	{
		
		protected EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
			dval = null;

		public SEvalElem(
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
		public EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public void writeString( PrintStream ps ) {
			Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
			while( it.hasNext() )
			{
				final ArrayList<BigInteger> key = it.next();
				ps.print( "\n" + "** " );
				( dval.getVal( key ) ).writeString( ps );
			}
		}
		
	}
	
	
	
	
	protected static class TestMetricTensorFactory extends MetricTensorInvertingFactory<String, TestDimensionThree, DoubleElem, DoubleElemFactory>
	{

		@Override
		public SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> getMetricTensor(
				boolean icovariantIndices, String index0, String index1) {
			
			final TestDimensionThree td = new TestDimensionThree();
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
			
			
			
			for( int acnt = 0 ; acnt < 3 ; acnt++ )
			{
				
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt ) );
				ab.add( BigInteger.valueOf( acnt ) );
				SymbolicElem<DoubleElem,DoubleElemFactory> as = new SymbolicConst( new DoubleElem( 1.0 ) , de );
				g0.setVal( ab , as );
			}
			

			if( !icovariantIndices )
			{
				g0 = genMatrixInverseLeft( td , seA , g0 );
			}
			
			
			final SEvalElem seval = new SEvalElem( ge , g0 );
			
			
			return( seval );
		}
		
	}
	
	
	
	
	
	private class PrecCompare extends PrecedenceComparator<DoubleElem, DoubleElemFactory>
	{

		public PrecCompare()
		{
		}
		
		@Override
		public boolean parenNeeded(
				SymbolicElem<DoubleElem, DoubleElemFactory> a,
				SymbolicElem<DoubleElem, DoubleElemFactory> b,
				boolean after) {
			
			if( ( a instanceof SymbolicSqrt ) 
					|| ( b instanceof SymbolicSqrt ) || ( a instanceof PartialDerivativeOp )
					|| ( b instanceof PartialDerivativeOp ) )
			{
				return( false );
			}
			
			if( ( a instanceof SymbolicAdd ) && ( b instanceof SymbolicMult ) )
			{
				return( false );
			}
			
			// TODO Auto-generated method stub
			
			return( true );
		}
		
	}
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.OrdinaryDerivative}.
	 */
	public void testCovariantDerivative() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		
		covariantIndices.add( "v" );
		
		
		final TestDimensionThree tdim = new TestDimensionThree();
		
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> se2 =
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory> de2 =
				new EinsteinTensorElemFactory<String, DoubleElem, DoubleElemFactory>(de);
		
		
		final EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2s =
				new EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem,DoubleElemFactory>, SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(se2);
		
		
		final DDirec dd = new DDirec( de2 , de );
		
		
		final OrdinaryDerivativeFactory<String, TestDimensionThree, DoubleElem, DoubleElemFactory, Ordinate> ofacI =
				new OrdinaryDerivativeFactory<String, TestDimensionThree, DoubleElem, DoubleElemFactory, Ordinate>(se2s, tdim, dd, null);
		
		
		
		MetricTensorFactory<String, DoubleElem, DoubleElemFactory> tmt = new TestMetricTensorFactory();
		
		
		

		
		SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> elem
			= new BElem(se2s);
		
		
		
		
		final CovariantDerivativeFactory<String, TestDimensionThree, DoubleElem, DoubleElemFactory, Ordinate> cofac =
			new CovariantDerivativeFactory<String, TestDimensionThree, DoubleElem, DoubleElemFactory, Ordinate>(se2s, elem, 
					"v", new TestTemporaryIndexFactory(), tmt, tdim, dd, null);
		
		
		
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace
				= (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
		
		
		final SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, 
			SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,
			EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			odir = cofac.genTerms( implicitSpace );
		
		
		final SymbolicElem<EinsteinTensorElem<String, SymbolicElem<DoubleElem, DoubleElemFactory>, 
			SymbolicElemFactory<DoubleElem, DoubleElemFactory>>,
			EinsteinTensorElemFactory<String, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>
			odir_asym = new SymbolicTensorResym<String, TestDimensionThree, SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>( 
					odir , 
					odir.getFac().getFac() , SymbolicTensorResym.ResymType.RESYM_ANTISYMMETRIC , tdim );
		
		
		final EinsteinTensorElem<String,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ev 
			= odir_asym.eval( implicitSpace );
		
		
		
		ev.validate();
		
		
		
		
		int kcnt = 0;
		Iterator<ArrayList<BigInteger>> itA = ev.getKeyIterator();
		while( itA.hasNext() )
		{
			kcnt++;
			ArrayList<BigInteger> key = itA.next();
			Assert.assertTrue( key.size() == 2 );
			final BigInteger k0 = key.get( 0 );
			final BigInteger k1 = key.get( 1 );
	//		System.out.print( key.get( 0 ) );
	//		System.out.print( " " );
	//		System.out.print( key.get( 1 ) );
			final SymbolicElem<DoubleElem,DoubleElemFactory> el =
					ev.getVal( key );
			final SymbolicElem<DoubleElem,DoubleElemFactory>
				el2 = el.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null);
	//		System.out.print( " " );
	//		el2.writeString( System.out );
	//		System.out.println( "" );
		}
		
		// Assert.assertTrue( kcnt == 16 );
		
		
	}
	
	
}

