




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

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.SymbolicElem.EVAL_MODE;

/**
 * A symbolic elem for the identity.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicIdentity<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicIdentity( S _fac )
	{
		super( _fac );
	}
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public SymbolicIdentity( S _fac , DroolsSession ds )
	{
		this( _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) {
		return( fac.identity() );
	}
	
	@Override
	public R evalCached( HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<R, S>, R> cache ) {
		final SCacheKey<R,S> key = new SCacheKey<R,S>( this , implicitSpace );
		final R iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final R ret = fac.identity();
		cache.put( key , ret );
		return( ret );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException
	{
		return( fac.zero() );
	}
	
	@Override
	public R evalPartialDerivativeCached( ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace, HashMap<SCacheKey<R, S>, R> cache )
	{
		return( fac.zero() );
	}
	
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		return( true );
	}
	
	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		// This simplification has a parallel implementation in the "MultRightMutatorType Ident A" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( b );
	}
	
	@Override
	public SymbolicElem<R, S> invertLeft() throws NotInvertibleException {
		// This simplification has a parallel implementation in the "Invert Left Identity --> Identity" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( this );
	}
	
	@Override
	public SymbolicElem<R, S> invertRight() throws NotInvertibleException {
		// This simplification has a parallel implementation in the "Invert Right Identity --> Identity" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( this );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( false );
	}
	
	@Override
	public boolean isPartialDerivativeZero()
	{
		return( true );
	}
	
	@Override
	public boolean isSymbolicIdentity()
	{
		return( true );
	}
	
	
	@Override
	public SymbolicIdentity<R,S> cloneThread( final BigInteger threadIndex )
	{
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		if( facs != fac )
		{
			return( new SymbolicIdentity<R,S>( facs ) );
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
		if( facs != fac )
		{
			final SymbolicIdentity<R,S> rtmp = new SymbolicIdentity<R,S>( facs );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}
	
	
	@Override
	public boolean evalSymbolicZeroApprox( EVAL_MODE mode )
	{
		return( false );
	}
	
	
	@Override
	public boolean evalSymbolicIdentityApprox( EVAL_MODE mode )
	{
		return( true );
	}
	

	@Override
	public String writeDesc( WriteElemCache<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			final String sta = fac.writeDesc( (WriteElemCache<R,S>)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( SymbolicIdentity.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( SymbolicIdentity.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( "( " );
			ps.print( sta );
			ps.println( " );" );
		}
		return( st );
	}
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps ) {
		ps.print( "<mn>IDENTITY</mn>" );
	}
	
	/**
	 * Returns true iff. the parameter is equal to the identity.
	 * 
	 * @param b The input parameter to test.
	 * @return True iff. the parameter is equal to the identity.
	 */
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		return( b instanceof SymbolicIdentity );
	}

}

