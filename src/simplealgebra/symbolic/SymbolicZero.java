





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
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.PrimitiveRandom;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.SymbolicElem.EVAL_MODE;

import java.math.BigInteger;

/**
 * A symbolic elem for the zero value.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicZero<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicZero( S _fac )
	{
		super( _fac );
	}
	
	/**
	 * Constructs the elem for use in a Drools ( <A href="http://drools.org">http://drools.org</A> ) session.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public SymbolicZero( S _fac , DroolsSession ds )
	{
		this( _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) {
		return( fac.zero() );
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
		final R ret = fac.zero();
		cache.put( key , ret );
		return( ret );
	}
	
	@Override
	public boolean isSymbolicZero()
	{
		return( true );
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
		final SCacheKey<R,S> key = new SCacheKey<R,S>( this , implicitSpace , withRespectTo );
		final R iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final R ret = fac.zero();
		cache.put( key , ret );
		return( ret );
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
	public SymbolicZero<R,S> cloneThread( final BigInteger threadIndex )
	{
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		if( facs != fac )
		{
			return( new SymbolicZero<R,S>( facs ) );
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
		final S facs = (S) this.getFac().getFac().cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( facs != fac )
		{
			final SymbolicZero<R,S> rtmp = new SymbolicZero<R,S>( facs );
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
			final String sta = fac.writeDesc( (WriteElemCache<R,S>)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( SymbolicZero.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( SymbolicZero.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( "( " );
			ps.print( sta );
			ps.println( " );" );
		}
		return( st );
	}
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps ) {
		ps.print( "<mn>ZERO</mn>" );
	}
	
	@Override
	public SymbolicElem<R,S> invertLeft( ) throws NotInvertibleException
	{
		throw( new NotInvertibleException() );
	}
	
	@Override
	public SymbolicElem<R,S> invertRight( ) throws NotInvertibleException
	{
		throw( new NotInvertibleException() );
	}
	
	/**
	 * Returns true iff. the parameter equals this element.
	 * 
	 * @param b The parameter to check.
	 * @return True iff. the parameter equals this element.
	 */
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		return( b instanceof SymbolicZero );
	}
	
	@Override
	public boolean evalSymbolicZeroApprox( EVAL_MODE mode )
	{
		return( true );
	}
	
	@Override
	public boolean evalSymbolicIdentityApprox( EVAL_MODE mode )
	{
		return( false );
	}
	
	/**
	 * Returns true iff. the parameter is an instance of SymbolicZero.
	 * 
	 * @param in The parameter to check.
	 * @return True iff. the parameter is an instance of SymbolicZero.
	 */
	public static boolean isSymbolicZero( SymbolicElem in )
	{
		return( in instanceof SymbolicZero );
	}
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		return( true );
	}
	
	@Override
	public SymbolicElem<R, S> add(SymbolicElem<R, S> b) {
		// This simplification has a parallel implementation in the "Add Zero A" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( b );
	}

	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		// This simplification has a parallel implementation in the "MultRightMutatorType Zero A" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( this );
	}

	@Override
	public SymbolicElem<R, S> negate() {
		// This simplification has a parallel implementation in the "Negate Zero" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( this );
	}

	@Override
	public SymbolicElem<R, S> divideBy(BigInteger val) {
		// This simplification has a parallel implementation in the "DivideBy OF Zero --> Zero" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( BigInteger.ZERO.compareTo( val ) != 0 ? this : super.divideBy( val ) );
	}
	
	@Override
	public SymbolicElem<R, S> random(PrimitiveRandom val) {
		// This simplification has a parallel implementation in the "Random OF Zero --> Zero" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( this );
	}

}

