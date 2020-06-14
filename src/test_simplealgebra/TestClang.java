



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
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.clang.Clang;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicAbsoluteValue;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;


/**
 * Tests the ability to convert some symbolic expressions to Clang (C Language) native code
 * for execution-time performance optimization.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestClang extends TestCase {
	
	
	
	/**
	 * Simple class for a changing double value.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class Bob_Dbl extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructor.
		 * @param _fac Input factory.
		 */
		public Bob_Dbl(DoubleElemFactory _fac) {
			super(_fac);
		}

		@Override
		public DoubleElem eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( elem );
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
		public DoubleElem evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
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
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		
		/**
		 * Sets the enclosed elem.
		 * 
		 * @param _elem The enclosed elem.
		 */
		public void setElem( DoubleElem _elem ) {
			elem  = _elem;
		}
		

		/**
		 * The enclosed elem.
		 */
		private DoubleElem elem;
		
	}
	
	
	
	/**
	 * Simple class for a changing complex value.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class Bob_Cplx_Dbl extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructor.
		 * @param _fac Input factory.
		 */
		public Bob_Cplx_Dbl(
				ComplexElemFactory<DoubleElem, DoubleElemFactory> _fac) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( elem );
		}

		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		
		/**
		 * Sets the enclosed elem.
		 * 
		 * @param _elem The enclosed elem.
		 */
		public void setElem( ComplexElem<DoubleElem,DoubleElemFactory> _elem ) {
			elem  = _elem;
		}
		
		
		/**
		 * The enclosed elem.
		 */
		private ComplexElem<DoubleElem,DoubleElemFactory> elem;
		
	}
	
	
	
	/**
	 * Tests evaluations of a simple polynomial.
	 * @throws Throwable
	 */
	public void testSimplePolynomialDbl() throws Throwable
	{
		Random rand = new Random( 6543 );
		final DoubleElemFactory dfac = new DoubleElemFactory();
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> sfac = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		final Bob_Dbl bob = new Bob_Dbl( dfac );
		final SymbolicElem<DoubleElem,DoubleElemFactory> bob_2 = bob.mult( bob );
		final SymbolicElem<DoubleElem,DoubleElemFactory> bob_3 = bob.mult( bob_2 );
		final SymbolicElem<DoubleElem,DoubleElemFactory> bob_4 = bob.mult( bob_3 );
		final SymbolicElem<DoubleElem,DoubleElemFactory> un =
				bob.add( bob_2.negate() ).add( bob_3 ).add( bob_4.negate() );
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> gen = Clang.clang_Dbl( un );
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			bob.setElem( new DoubleElem( 2.0 * rand.nextDouble() - 1.0 ) );
			final DoubleElem d = gen.eval( null );
			final DoubleElem d_truth = un.eval( null );
			Assert.assertTrue( Math.abs( d.getVal() - d_truth.getVal() ) < 1E-6 );
		}
	}
	
	
	
	/**
	 * Tests evaluations of the absolute value function.
	 * @throws Throwable
	 */
	public void testAbsoluteValueDbl() throws Throwable
	{
		Random rand = new Random( 6543 );
		final DoubleElemFactory dfac = new DoubleElemFactory();
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> sfac = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		final Bob_Dbl bob = new Bob_Dbl( dfac );
		final SymbolicElem<DoubleElem,DoubleElemFactory> un =
				new SymbolicAbsoluteValue<DoubleElem,DoubleElemFactory>(bob, dfac);
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> gen = Clang.clang_Dbl( un );
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			bob.setElem( new DoubleElem( 2.0 * rand.nextDouble() - 1.0 ) );
			final DoubleElem d = gen.eval( null );
			final DoubleElem d_truth = un.eval( null );
			Assert.assertTrue( Math.abs( d.getVal() - d_truth.getVal() ) < 1E-6 );
		}
	}
	
	
	
	/**
	 * Tests evaluations of a simple polynomial.
	 * @throws Throwable
	 */
	public void testSimplePolynomialDbl_Cplx() throws Throwable
	{
		Random rand = new Random( 6543 );
		final DoubleElemFactory dfac = new DoubleElemFactory();
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = 
				new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		final Bob_Cplx_Dbl bob = new Bob_Cplx_Dbl( cfac );
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> bob_2 = bob.mult( bob );
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> bob_3 = bob.mult( bob_2 );
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> bob_4 = bob.mult( bob_3 );
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> un =
				bob.add( bob_2.negate() ).add( bob_3 ).add( bob_4.negate() );
		
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> gen = Clang.clang_Cplx_Dbl( un );
		for( int cnt = 0 ; cnt < 100 ; cnt++ )
		{
			final DoubleElem dre = new DoubleElem( 2.0 * rand.nextDouble() - 1.0 );
			final DoubleElem dim = new DoubleElem( 2.0 * rand.nextDouble() - 1.0 );
			final ComplexElem<DoubleElem,DoubleElemFactory> cpval = new ComplexElem<DoubleElem,DoubleElemFactory>( dre , dim );
			bob.setElem( cpval );
			final ComplexElem<DoubleElem,DoubleElemFactory> cplx = gen.eval( null );
			final ComplexElem<DoubleElem,DoubleElemFactory> cplx_truth = un.eval( null );
			Assert.assertTrue( Math.abs( cplx.getRe().getVal() - cplx_truth.getRe().getVal() ) < 1E-6 );
			Assert.assertTrue( Math.abs( cplx.getIm().getVal() - cplx_truth.getIm().getVal() ) < 1E-6 );
		}
	}
	
	

	
	/**
	 * Tests the ability to convert a simple DoubleElem expression to Clang.
	 * @throws Throwable
	 */
	public void testClangDbl() throws Throwable
	{
		final DoubleElemFactory dfac = new DoubleElemFactory();
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> sfac = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		final SymbolicElem<DoubleElem,DoubleElemFactory> un 
			= sfac.negativeIdentity().divideBy( 3 );
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> gen = Clang.clang_Dbl( un );
		final DoubleElem d = gen.eval( null );
		final DoubleElem d_truth = un.eval( null );
		Assert.assertTrue( Math.abs( d.getVal() - d_truth.getVal() ) < 1E-6 );
	}
	
	
	
	
	/**
	 * Tests the ability to convert a constant with exponential notation to Clang
	 * @throws Throwable
	 */
	public void testClangExponent() throws Throwable
	{
		final DoubleElemFactory dfac = new DoubleElemFactory();
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> sfac = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		final SymbolicElem<DoubleElem,DoubleElemFactory> un 
			= new SymbolicReduction<DoubleElem,DoubleElemFactory>( new DoubleElem( 1E-8 ) , dfac  );
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> gen = Clang.clang_Dbl( un );
		final DoubleElem d = gen.eval( null );
		final DoubleElem d_truth = un.eval( null );
		Assert.assertTrue( Math.abs( d.getVal() - d_truth.getVal() ) < 1E-10 );
	}
	
	
	
	
	/**
	 * Tests the ability to convert a simple ComplexElem expression to Clang.
	 * @throws Throwable
	 */
	public void testClangCplx() throws Throwable
	{
		final DoubleElemFactory dfac = new DoubleElemFactory();
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = 
				new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> un 
			= sfac.negativeIdentity().divideBy( 3 );
		
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> gen = Clang.clang_Cplx_Dbl( un );
		final ComplexElem<DoubleElem,DoubleElemFactory> cplx = gen.eval( null );
		final ComplexElem<DoubleElem,DoubleElemFactory> cplx_truth = un.eval( null );
		Assert.assertTrue( Math.abs( cplx.getRe().getVal() - cplx_truth.getRe().getVal() ) < 1E-6 );
		Assert.assertTrue( Math.abs( cplx.getIm().getVal() - cplx_truth.getIm().getVal() ) < 1E-6 );
	}
	
	
	
	
}



