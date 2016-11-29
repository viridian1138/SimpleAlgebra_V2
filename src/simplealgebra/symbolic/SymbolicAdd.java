



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

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;

/**
 * Symbolic elem. for performing addition.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicAdd<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elemA The left argument of the addition.
	 * @param _elemB The right argument of the addition.
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicAdd( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	/**
	 * Constructs the elem for use in a Drools ( <A href="http://drools.org">http://drools.org</A> ) session.
	 * 
	 * @param _elemA The left argument of the addition.
	 * @param _elemB The right argument of the addition.
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public SymbolicAdd( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac , DroolsSession ds )
	{
		this( _elemA , _elemB , _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elemA.eval( implicitSpace ).add( elemB.eval( implicitSpace ) ) );
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
		final R ret = elemA.evalCached( implicitSpace , cache ).add( elemB.evalCached( implicitSpace , cache ) );
		cache.put( key , ret );
		return( ret );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( elemA.evalPartialDerivative( withRespectTo , implicitSpace ).add( elemB.evalPartialDerivative( withRespectTo , implicitSpace ) ) );
	} 
	
	@Override
	public R evalPartialDerivativeCached( ArrayList<? extends Elem<?,?>> withRespectTo , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final SCacheKey<R,S> key = new SCacheKey<R,S>( this , implicitSpace , withRespectTo );
		final R iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final R ret = elemA.evalPartialDerivativeCached( withRespectTo , implicitSpace , cache ).add( elemB.evalPartialDerivativeCached( withRespectTo , implicitSpace , cache ) );
		cache.put( key , ret );
		return( ret );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( ( elemB.exposesDerivatives() ) || ( elemA.exposesDerivatives() ) );
	}
	
	@Override
	public SymbolicAdd<R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<R,S> elemAs = elemA.cloneThread( threadIndex );
		final SymbolicElem<R,S> elemBs = elemB.cloneThread( threadIndex );
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elemAs != elemA ) || ( elemBs != elemB ) || ( facs != fac ) )
		{
			return( new SymbolicAdd<R,S>( elemAs , elemBs , facs ) );
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
			final SymbolicAdd<R,S> rtmp = new SymbolicAdd<R,S>( elemAs , elemBs , facs );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}

	@Override
	public String writeDesc( WriteElemCache<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			final String elemAs = elemA.writeDesc( cache , ps);
			final String elemBs = elemB.writeDesc( cache , ps);
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( SymbolicAdd.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( SymbolicAdd.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( "( " );
			ps.print( elemAs );
			ps.print( " , " );
			ps.print( elemBs );
			ps.print( " , " );
			ps.print( facs );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
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
		ps.print( "<mo>+</mo>" );
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
	 * Returns the left argument of the addition.
	 * 
	 * @return The left argument of the addition.
	 */
	public SymbolicElem<R, S> getElemA() {
		return elemA;
	}

	/**
	 * Returns the right argument of the addition.
	 * 
	 * @return The right argument of the addition.
	 */
	public SymbolicElem<R, S> getElemB() {
		return elemB;
	}
	
	
	@Override
	public SymbolicElem<R, S> handleOptionalOp( Object id , ArrayList<SymbolicElem<R, S>> args ) throws NotInvertibleException
	{
		
		return( super.handleOptionalOp(id, args) );
	}
	
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		return( elemA.evalSymbolicConstantApprox() && elemB.evalSymbolicConstantApprox() );
	}
	
	
	@Override
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		if( b instanceof SymbolicAdd )
		{
			boolean aa = this.getElemA().symbolicEquals( ((SymbolicAdd<R,S>) b).getElemA() );
			boolean bb = this.getElemB().symbolicEquals( ((SymbolicAdd<R,S>) b).getElemB() );
			if( aa && bb )
			{
				return( true );
			}
				
			aa = this.getElemA().symbolicEquals( ((SymbolicAdd<R,S>) b).getElemB() );
			bb = this.getElemB().symbolicEquals( ((SymbolicAdd<R,S>) b).getElemA() );
			return( aa && bb );
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
	 * Simplifies the elem, where two elems in the underlying negation true are known to be negations of each other.
	 * 
	 * @param elA An elem in the addition tree under this elem.
	 * @param elB An elem in the addition tree under this elem which is the negation of elA.
	 * @param ds The Drools ( <A href="http://drools.org">http://drools.org</A> ) session in which to simplify the addition.
	 * @return The simplified expression, with elA and elB replaced by zero.
	 */
	public SymbolicElem<R, S> handleAddSimplify( SymbolicElem<R, S> elA , SymbolicElem<R, S> elB , DroolsSession ds )
	{
		HashSet<SymbolicElem<R, S>> elS = new HashSet<SymbolicElem<R, S>>();
		elS.add( elA );
		elS.add( elB );
		SymbolicElem<R, S> ret = this.handleAddSimplify( elS , ds );
		if( !( elS.isEmpty() ) )
		{
			throw( new RuntimeException( "Internal Error." ) );
		}
		return( ret );
	}
	
	
	/**
	 * Simplifies the addition of a set of elems.
	 * 
	 * @param elS The set of elems that are to be replaced by zero.
	 * @param ds The Drools ( <A href="http://drools.org">http://drools.org</A> ) session in which to simplify the addition.
	 * @return The simplified expression.
	 */
	public SymbolicElem<R, S> handleAddSimplify( HashSet<SymbolicElem<R, S>> elS , DroolsSession ds )
	{
		SymbolicElem<R,S> elA = elemA;
		SymbolicElem<R,S> elB = elemB;
		if( elS.contains( elA ) )
		{
			elS.remove( elA );
			elA = this.getFac().zero();
			ds.insert( elA );
		}
		else
		{
			if( elA instanceof SymbolicAdd )
			{
				elA = ((SymbolicAdd) elA).handleAddSimplify( elS , ds );
			}
		}
		
		if( elS.contains( elB ) )
		{
			elS.remove( elB );
			elB = this.getFac().zero();
			ds.insert( elB );
		}
		else
		{
			if( elB instanceof SymbolicAdd )
			{
				elB = ((SymbolicAdd) elB).handleAddSimplify( elS , ds );
			}
		}
		
		SymbolicElem<R, S> ret = elA.add( elB );
		// System.out.println( "Insert: " + ret );
		ds.insert( ret );
		return( ret );
	}
	
	

	/**
	 * The left argument of the addition.
	 */
	private SymbolicElem<R,S> elemA;
	
	/**
	 * The right argument of the addition.
	 */
	private SymbolicElem<R,S> elemB;

}

