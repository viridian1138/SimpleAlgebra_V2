




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





package simplealgebra.symbolic;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ddx.DerivativeElem;
import simplealgebra.ddx.PartialDerivativeOp;

/**
 * Symbolic elem for a multiplication.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicMult<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the multiplication.
	 * 
	 * @param _elemA The left argument of the multiplication.
	 * @param _elemB The right argument of the multiplication.
	 * @param _fac The enclosed factory.
	 */
	public SymbolicMult( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	/**
	 * Constructs the multiplication for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elemA The left argument of the multiplication.
	 * @param _elemB The right argument of the multiplication.
	 * @param _fac The enclosed factory.
	 * @param ds The Drools session.
	 */
	public SymbolicMult( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac , DroolsSession ds )
	{
		this( _elemA , _elemB , _fac );
		ds.insert( this );
	}
	
	
	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		if( b.isPartialDerivativeZero() )
		{
			if( elemB instanceof PartialDerivativeOp )
			{
				return( this.getFac().zero() );
			}
		}
		return( super.mult( b ) );
	}
	
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		if( elemA instanceof DerivativeElem )
		{
			return( ( (DerivativeElem<R,S>) elemA ).evalDerivative( elemB , implicitSpace ) );
		}
		R ea = null;
		try
		{
			ea = elemA.eval( implicitSpace );
		}
		catch( MultiplicativeDistributionRequiredException ex )
		{
			if( elemA instanceof SymbolicNegate )
			{
				return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).eval( implicitSpace ).negate() );
			}
			
			if( elemA instanceof SymbolicAdd )
			{
				final SymbolicElem<R,S> ia = ((SymbolicAdd) elemA).getElemA();
				final SymbolicElem<R,S> ib = ((SymbolicAdd) elemA).getElemB();
				return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).eval( implicitSpace ) );
			}
			
			if( elemA instanceof SymbolicMult )
			{
				final SymbolicElem<R,S> ia = ((SymbolicMult) elemA).getElemA();
				final SymbolicElem<R,S> ib = ((SymbolicMult) elemA).getElemB();
				return( ( ia.mult( ib.mult( elemB ) ) ).eval( implicitSpace ) );
			}
			
			throw( ex );
		}
		return( ea.mult( elemB.eval( implicitSpace ) ) );
	}
	
	@Override
	public R evalCached( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ,
			HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		final SCacheKey<R,S> key = new SCacheKey<R,S>( this , implicitSpace );
		final R iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		if( elemA instanceof DerivativeElem )
		{
			R ret = ( (DerivativeElem<R,S>) elemA ).evalDerivativeCached( elemB , implicitSpace , cache );
			cache.put( key , ret );
			return( ret );
		}
		R ea = null;
		try
		{
			ea = elemA.evalCached( implicitSpace , cache );
		}
		catch( MultiplicativeDistributionRequiredException ex )
		{
			if( elemA instanceof SymbolicNegate )
			{
				R ret = ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).evalCached( implicitSpace , cache ).negate();
				cache.put(key, ret);
				return( ret );
			}
			
			if( elemA instanceof SymbolicAdd )
			{
				final SymbolicElem<R,S> ia = ((SymbolicAdd) elemA).getElemA();
				final SymbolicElem<R,S> ib = ((SymbolicAdd) elemA).getElemB();
				R ret = ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).evalCached( implicitSpace , cache );
				cache.put(key, ret);
				return( ret );
			}
			
			if( elemA instanceof SymbolicMult )
			{
				final SymbolicElem<R,S> ia = ((SymbolicMult) elemA).getElemA();
				final SymbolicElem<R,S> ib = ((SymbolicMult) elemA).getElemB();
				R ret = ( ia.mult( ib.mult( elemB ) ) ).evalCached( implicitSpace , cache );
				cache.put(key, ret);
				return( ret );
			}
			
			throw( ex );
		}
		R ret = ea.mult( elemB.evalCached( implicitSpace , cache ) );
		cache.put(key, ret);
		return( ret );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( elemA instanceof PartialDerivativeOp )
		{
			final ArrayList<? extends Elem<?, ?>> newWrt = new ArrayList<Elem<?, ?>>();
			final ArrayList neWW = newWrt;
			for( final Object ii : ((PartialDerivativeOp) elemA).getWithRespectTo() )
			{
				neWW.add( ii );
			}
			for( final Elem<?, ?> ii : withRespectTo )
			{
				neWW.add( ii );
			}
			return( elemB.evalPartialDerivative(newWrt, implicitSpace) );
		}
		if( elemA instanceof DerivativeElem )
		{
			SymbolicElem<R,S> sym = (SymbolicElem<R,S>)(((DerivativeElem) elemA).evalDerivative( elemA , implicitSpace ) );
			return( sym.evalPartialDerivative(withRespectTo, implicitSpace) );
		}
		R lt = null;
		{
			R ea = null;
			try
			{
				ea = elemA.evalPartialDerivative( withRespectTo , implicitSpace );
			}
			catch( MultiplicativeDistributionRequiredException ex )
			{
				if( elemA instanceof SymbolicNegate )
				{
					return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).evalPartialDerivative(withRespectTo, implicitSpace).negate() );
				}
				
				if( elemA instanceof SymbolicAdd )
				{
					final SymbolicElem<R,S> ia = ((SymbolicAdd<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicAdd<R,S>) elemA).getElemB();
					return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).evalPartialDerivative( withRespectTo , implicitSpace ) );
				}
				
				if( elemA instanceof SymbolicMult )
				{
					final SymbolicElem<R,S> ia = ((SymbolicMult<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicMult<R,S>) elemA).getElemB();
					return( ( ia.mult( ib.mult( elemB ) ) ).evalPartialDerivative( withRespectTo , implicitSpace ) );
				}
				
				throw( ex );
			}
			lt = ea.mult( elemB.eval(implicitSpace) );
		}
		R rt = null;
		{
			R ea = null;
			try
			{
				ea = elemA.eval(implicitSpace);
			}
			catch( MultiplicativeDistributionRequiredException ex )
			{
				if( elemA instanceof SymbolicNegate )
				{
					return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).evalPartialDerivative(withRespectTo, implicitSpace).negate() );
				}
				
				if( elemA instanceof SymbolicAdd )
				{
					final SymbolicElem<R,S> ia = ((SymbolicAdd<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicAdd<R,S>) elemA).getElemB();
					return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).evalPartialDerivative( withRespectTo , implicitSpace ) );
				}
				
				if( elemA instanceof SymbolicMult )
				{
					final SymbolicElem<R,S> ia = ((SymbolicMult<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicMult<R,S>) elemA).getElemB();
					return( ( ia.mult( ib.mult( elemB ) ) ).evalPartialDerivative( withRespectTo , implicitSpace ) );
				}
				
				throw( ex );
			}
			rt = ea.mult( elemB.evalPartialDerivative( withRespectTo , implicitSpace ) );
		}
		
		return( lt.add( rt ) );
	}
	
	@Override
	public R evalPartialDerivativeCached( ArrayList<? extends Elem<?, ?>> withRespectTo , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( elemA instanceof PartialDerivativeOp )
		{
			final ArrayList<? extends Elem<?, ?>> newWrt = new ArrayList<Elem<?, ?>>();
			final ArrayList neWW = newWrt;
			for( final Object ii : ((PartialDerivativeOp) elemA).getWithRespectTo() )
			{
				neWW.add( ii );
			}
			for( final Elem<?, ?> ii : withRespectTo )
			{
				neWW.add( ii );
			}
			return( elemB.evalPartialDerivativeCached(newWrt, implicitSpace, cache) );
		}
		if( elemA instanceof DerivativeElem )
		{
			SymbolicElem<R,S> sym = (SymbolicElem<R,S>)(((DerivativeElem) elemA).evalDerivativeCached( elemA , implicitSpace , cache ) );
			return( sym.evalPartialDerivativeCached(withRespectTo, implicitSpace, cache) );
		}
		R lt = null;
		{
			R ea = null;
			try
			{
				ea = elemA.evalPartialDerivativeCached( withRespectTo , implicitSpace , cache );
			}
			catch( MultiplicativeDistributionRequiredException ex )
			{
				if( elemA instanceof SymbolicNegate )
				{
					return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).evalPartialDerivativeCached(withRespectTo, implicitSpace, cache).negate() );
				}
				
				if( elemA instanceof SymbolicAdd )
				{
					final SymbolicElem<R,S> ia = ((SymbolicAdd<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicAdd<R,S>) elemA).getElemB();
					return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).evalPartialDerivativeCached( withRespectTo , implicitSpace , cache ) );
				}
				
				if( elemA instanceof SymbolicMult )
				{
					final SymbolicElem<R,S> ia = ((SymbolicMult<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicMult<R,S>) elemA).getElemB();
					return( ( ia.mult( ib.mult( elemB ) ) ).evalPartialDerivativeCached( withRespectTo , implicitSpace , cache ) );
				}
				
				throw( ex );
			}
			lt = ea.mult( elemB.evalCached(implicitSpace, cache) );
		}
		R rt = null;
		{
			R ea = null;
			try
			{
				ea = elemA.evalCached(implicitSpace, cache);
			}
			catch( MultiplicativeDistributionRequiredException ex )
			{
				if( elemA instanceof SymbolicNegate )
				{
					return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).evalPartialDerivativeCached(withRespectTo, implicitSpace, cache).negate() );
				}
				
				if( elemA instanceof SymbolicAdd )
				{
					final SymbolicElem<R,S> ia = ((SymbolicAdd<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicAdd<R,S>) elemA).getElemB();
					return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).evalPartialDerivativeCached( withRespectTo , implicitSpace , cache ) );
				}
				
				if( elemA instanceof SymbolicMult )
				{
					final SymbolicElem<R,S> ia = ((SymbolicMult<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicMult<R,S>) elemA).getElemB();
					return( ( ia.mult( ib.mult( elemB ) ) ).evalPartialDerivativeCached( withRespectTo , implicitSpace , cache ) );
				}
				
				throw( ex );
			}
			rt = ea.mult( elemB.evalPartialDerivativeCached( withRespectTo , implicitSpace , cache ) );
		}
		
		return( lt.add( rt ) );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elemB.exposesDerivatives() );
	}
	
	
	@Override
	public SymbolicMult<R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<R,S> elemAs = elemA.cloneThread( threadIndex );
		final SymbolicElem<R,S> elemBs = elemB.cloneThread( threadIndex );
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elemAs != elemA ) || ( elemBs != elemB ) || ( facs != fac ) )
		{
			return( new SymbolicMult<R,S>( elemAs , elemBs , facs ) );
		}
		return( this );
	}
	
	
	@Override
	public SymbolicElem<R,S> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<SymbolicElem<R, S>, SymbolicElemFactory<R, S>> cache) {
		final SymbolicElem<R,S> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		final S facs = this.getFac().getFac().cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		final SymbolicElem<R,S> elemAs = elemA.cloneThreadCached(threadIndex, cache);
		final SymbolicElem<R,S> elemBs = elemB.cloneThreadCached(threadIndex, cache);
		if( ( elemAs != elemA ) || ( elemBs != elemB ) || ( facs != fac ) )
		{
			final SymbolicMult<R,S> rtmp = new SymbolicMult<R,S>( elemAs , elemBs , facs );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}
	
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "mult( " );
		elemA.writeString( ps );
		ps.print( " , " );
		elemB.writeString( ps );
		ps.print( " )" );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator<R,S> pc , PrintStream ps )
	{
		if( pc.parenNeeded( this ,  elemA , false ) )
		{
			pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		}
		else
		{
			ps.print( "<mrow>" );
		}
		elemA.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  elemA , false ) )
		{
			pc.getParenthesisGenerator().handleParenthesisClose(ps);
		}
		else
		{
			ps.print( "</mrow>" );
		}
		ps.print( "<mo>&InvisibleTimes;</mo>" );
		if( pc.parenNeeded( this ,  elemB , true ) )
		{
			pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		}
		else
		{
			ps.print( "<mrow>" );
		}
		elemB.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  elemB , true ) )
		{
			pc.getParenthesisGenerator().handleParenthesisClose(ps);
		}
		else
		{
			ps.print( "</mrow>" );
		}
	}
	
	/**
	 * Gets the left argument of the multiplication.
	 * 
	 * @return The left argument of the multiplication.
	 */
	public SymbolicElem<R, S> getElemA() {
		return elemA;
	}

	/**
	 * Gets the right argument of the multiplication.
	 * 
	 * @return The right argument of the multiplication.
	 */
	public SymbolicElem<R, S> getElemB() {
		return elemB;
	}
	
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		return( elemA.evalSymbolicConstantApprox() && elemB.evalSymbolicConstantApprox() );
	}
	
	
	
	@Override
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		if( b instanceof SymbolicMult )
		{
			if( this.getFac().isMultCommutative() )
			{
				return( symbolicEqualsCommutative( (SymbolicMult<R,S>) b ) );
			}
			else
			{
				return( symbolicEqualsNonCommutative( (SymbolicMult<R,S>) b ) );
			}
		}
		
		return( false );
	}
	
	
	/**
	 * Returns whether this expression is equal to the one in the parameter for a commutative algebra.
	 * 
	 * @param b The expression to be compared.
	 * @return True if the expressions are found to be equal, false otherwise.
	 */
	protected boolean symbolicEqualsCommutative( SymbolicMult<R,S> b )
	{
		boolean aa = this.getElemA().symbolicEquals( b.getElemA() );
		boolean bb = this.getElemB().symbolicEquals( b.getElemB() );
		if( aa && bb )
		{
			return( true );
		}
		
		aa = this.getElemA().symbolicEquals( b.getElemB() );
		bb = this.getElemB().symbolicEquals( b.getElemA() );
		return( aa && bb );
	}
	
	
	
	/**
	 * Returns whether this expression is equal to the one in the parameter for a non-commutative algebra.
	 * 
	 * @param b The expression to be compared.
	 * @return True if the expressions are found to be equal, false otherwise.
	 */
	protected boolean symbolicEqualsNonCommutative( SymbolicMult<R,S> b )
	{
		final ArrayList<SymbolicElem<R,S>> ind0 = new ArrayList<SymbolicElem<R,S>>();
		final ArrayList<SymbolicElem<R,S>> ind1 = new ArrayList<SymbolicElem<R,S>>();
		this.handleMultInsert( ind0 );
		b.handleMultInsert( ind1 );
		if( ind0.size() == ind1.size() )
		{
			int cnt;
			for( cnt = 0 ; cnt < ind0.size() ; cnt++ )
			{
				if( !( ind0.get( cnt ).symbolicEquals( ind1.get( cnt ) ) ) )
				{
					return( false );
				}
			}
			return( true );
		}
		
		return( false );
	}
	
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elemA.performInserts( session );
		elemB.performInserts( session );
		super.performInserts( session );
	}
	
	
	
	/**
	 * In a case where an elem and its multiplicative inverse appear adjacent to each other
	 * in a multiplicative expression, replace the adjacent expressions with the identity.
	 * 
	 * @param elA The expression to replace.
	 * @param elB The multiplicative inverse of elA, which should also be replaced.
	 * @param ds The Drools session over which to simplify.
	 * @return The simplified version of the expression.
	 */
	public SymbolicElem<R, S> handleMultSimplify( final SymbolicElem<R,S> elA , final SymbolicElem<R,S> elB , final DroolsSession ds )
	{
		final HashSet<Integer> hset = new HashSet<Integer>();
		
		{
			final ArrayList<SymbolicElem<R,S>> ind = new ArrayList<SymbolicElem<R,S>>();
			handleMultInsert( ind );
			int cnt;
			for( cnt = 0 ; cnt < ( ind.size() - 1 ) ; cnt++ )
			{
				final SymbolicElem<R,S> i0 = ind.get( cnt );
				final SymbolicElem<R,S> i1 = ind.get( cnt + 1 );
				if( ( elA == i0 ) && ( elB == i1 ) )
				{
					hset.add( cnt );
					hset.add( cnt + 1 );
				}
			}
		}
		
		
		final int[] index = new int[] { 0 };
		return( handleMultRewrite( index , hset , ds ) );
	}
	
	
	/**
	 * In a case where an elem and its multiplicative inverse appear adjacent to each other
	 * in a multiplicative expression, performs term replacement once adjacent terms have
	 * been identified.
	 * 
	 * @param index The current index in the multiplication tree, passed by reference.
	 * @param hset Set of numerically ordered subexpressions that are to be replaced by the identity.
	 * @param session The Drools session over which to simplify.
	 * @return The simplified version of the expression.
	 */
	private SymbolicElem<R,S> handleMultRewrite( final int[] index , final HashSet<Integer> hset ,
			final DroolsSession session )
	{
		SymbolicElem<R,S> elA = null;
		SymbolicElem<R,S> elB = null;
		
		if( elemA instanceof SymbolicMult )
		{
			elA = ((SymbolicMult) elemA).handleMultRewrite(index, hset, session);
		}
		else
		{
			elA = hset.contains( index[ 0 ] ) ? new SymbolicIdentity<R,S>( fac ) : elemA;
			if( elA != elemA ) session.insert( elA );
			( index[ 0 ] )++;
		}
		
		if( elemB instanceof SymbolicMult )
		{
			elB = ((SymbolicMult) elemB).handleMultRewrite(index, hset, session);
		}
		else
		{
			elB = hset.contains( index[ 0 ] ) ? new SymbolicIdentity<R,S>( fac ) : elemB;
			if( elB != elemB ) session.insert( elB );
			( index[ 0 ] )++;
		}
		
		if( ( elA == elemA ) && ( elB == elemB ) )
		{
			return( this );
		}
		
		SymbolicElem<R,S> ret = elA.mult( elB );
		session.insert( ret );
		return( ret );
	}
	
	
	
	/**
	 * Assembles a tree of multiplications into a single array multiplication.
	 * 
	 * @param ind The arguments of the output array multiplication.
	 */
	private void handleMultInsert( final ArrayList<SymbolicElem<R,S>> ind )
	{
		
		if( elemA instanceof SymbolicMult )
		{
			((SymbolicMult) elemA).handleMultInsert(ind);
		}
		else
		{
			ind.add( elemA );
		}
		
		if( elemB instanceof SymbolicMult )
		{
			((SymbolicMult) elemB).handleMultInsert(ind);
		}
		else
		{
			ind.add( elemB );
		}
		
	}
	
	
	/**
	 * The left argument of the multiplication.
	 */
	private SymbolicElem<R,S> elemA;
	
	/**
	 * The right argument of the multiplication.
	 */
	private SymbolicElem<R,S> elemB;
	

}

