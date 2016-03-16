






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

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.AbsoluteValue;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicAbsoluteValue;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicOps;
import simplealgebra.symbolic.SymbolicZero;




/**
 * Tests symbolic reductions of the absolute value.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestAbsoluteValueSymbolic extends TestCase 
{

	
	/**
	 * Node representing the variable to be tested in the expression.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class AElem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		public AElem(DoubleElemFactory _fac) {
			super(_fac);
		}

		@Override
		public DoubleElem eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
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
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem,DoubleElemFactory>)( cache.getInnerCache() ) , ps);
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
	 * Tests that the absolute value of the identity yields the identity.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testAbsoluteValueIdentity() throws NotInvertibleException
	{
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> ye = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>(dl);
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d0 = ye.identity();
		
		final ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>>
			args = new ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>>();
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d1 = d0.handleOptionalOp( AbsoluteValue.ABSOLUTE_VALUE , args);
		
		Assert.assertTrue( d1 instanceof SymbolicAbsoluteValue );
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d2 = d1.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , args);
		
		Assert.assertTrue( d2 instanceof SymbolicIdentity );
		
	}
	
	
	
	/**
	 * Tests that the absolute value of zero yields zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testAbsoluteValueZero() throws NotInvertibleException
	{
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> ye = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>(dl);
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d0 = ye.zero();
		
		final ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>>
			args = new ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>>();
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d1 = d0.handleOptionalOp( AbsoluteValue.ABSOLUTE_VALUE , args);
		
		Assert.assertTrue( d1 instanceof SymbolicAbsoluteValue );
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d2 = d1.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , args);
		
		Assert.assertTrue( d2 instanceof SymbolicZero );
		
	}
	
	
	
	/**
	 * Tests that the absolute value of zero yields zero.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testDoubleAbsoluteValue() throws NotInvertibleException
	{
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> ye = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>(dl);
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d0 = new AElem( dl );
		
		final ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>>
			args = new ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>>();
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d1 = d0.handleOptionalOp( AbsoluteValue.ABSOLUTE_VALUE , args);
		
		Assert.assertTrue( d1 instanceof SymbolicAbsoluteValue );
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d2 = d1.handleOptionalOp( AbsoluteValue.ABSOLUTE_VALUE , args);
	
		Assert.assertTrue( d2 instanceof SymbolicAbsoluteValue );
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			d3 = d2.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , args);
		
		Assert.assertTrue( d3 instanceof SymbolicAbsoluteValue );
		
		SymbolicAbsoluteValue<DoubleElem,DoubleElemFactory>
			d3a = (SymbolicAbsoluteValue<DoubleElem,DoubleElemFactory>)( d3 );
		
		Assert.assertTrue( ( d3a.getElem() ) instanceof AElem );
		
	}
	
	

	
}


