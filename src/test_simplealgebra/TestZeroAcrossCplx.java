






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


import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.symbolic.SymbolicZero;

import java.io.*;



/**
 * Verifies that the expression 0 * a + ( 0 * a ) * i simplifies to zero.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestZeroAcrossCplx extends TestCase 
{

	
	/**
	 * Node representing the variable to be tested in the expression.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class AElem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the node.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public AElem(DoubleElemFactory _fac ) {
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
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof AElem )
			{
				return( true );
			}
			
			return( false );
		}

		

		

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem, DoubleElemFactory>) cache.getInnerCache() , ps);
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
	 * Runs the test.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testZeroAcrossCplx() throws NotInvertibleException
	{
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> se = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>(dl);
		
		final ComplexElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ce =
				new ComplexElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( se );
		
		final SymbolicElemFactory<ComplexElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<ComplexElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( ce );
		
		final AElem a0 = new AElem( dl );
		
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> a00 = a0.mult( se.zero() );
		
		
		// System.out.println( a00 );
		
		
		final ComplexElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> c0 =
				new ComplexElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( a00 , a00 );
		
		
		final SymbolicElem<ComplexElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
			d0 = new SymbolicReduction<ComplexElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( c0 , ce );
		
		
		// final SymbolicElem<ComplexElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
		//	d0a = d0.add( d0 );
		
		
		final SymbolicElem<ComplexElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
			d1 = d0.distributeSimplify2();
		
		
//		System.out.println( "***" );
//		System.out.println( d0 );
//		System.out.println( d1 );
		
		
		
		Assert.assertTrue( d1 instanceof SymbolicZero );
		
	}
	
	
}



