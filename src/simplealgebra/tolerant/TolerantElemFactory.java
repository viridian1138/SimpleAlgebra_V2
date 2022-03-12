



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




package simplealgebra.tolerant;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import simplealgebra.*;
import simplealgebra.symbolic.SymbolicElem;


/**
 * Factory for tolerant elems.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <T> The factory for tolerant results.
 */
public class TolerantElemFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>,T extends TolerantResultFactory<R,S,T>> 
	extends ElemFactory<TolerantElem<R,S,T>, TolerantElemFactory<R,S,T>> {

	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _trf The factory for tolerant results.
	 */
	public TolerantElemFactory( S _fac , T _trf )
	{
		fac = _fac;
		trf = _trf;
	}

	@Override
	public TolerantElem<R, S, T> identity() {
		return( new TolerantElem<R,S,T>( fac.identity() , trf ) );
	}

	@Override
	public TolerantElem<R, S, T> zero() {
		return( new TolerantElem<R,S,T>( fac.zero() , trf ) );
	}
	
	
	@Override
	public Elem<?,?> totalMagnitudeZero()
	{
		return( fac.zero() );
	}
	
	
	@Override
	public SymbolicElem<TolerantElem<R, S, T>, TolerantElemFactory<R,S,T>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<TolerantElem<R, S, T>, TolerantElemFactory<R,S,T>>> args )  throws NotInvertibleException
	{	
		return( super.handleSymbolicOptionalOp(id, args) );
	}
	
	
	@Override
	public boolean isMultCommutative()
	{
		return( fac.isMultCommutative() );
	}
	
	
	@Override
	public boolean isNestedMultCommutative()
	{
		return( fac.isMultCommutative() );
	}
	
	
	@Override
	public boolean isMultAssociative()
	{
		return( fac.isMultAssociative() );
	}
	
	
	@Override
	public boolean isNestedMultAssociative()
	{
		return( fac.isMultAssociative() );
	}
	
	
	@Override
	public TolerantElemFactory<R,S,T> cloneThread( final BigInteger threadIndex )
	{
		S sfac = fac.cloneThread(threadIndex);
		T strf = trf.cloneThread(threadIndex);
		if( ( fac != sfac ) || ( trf != strf ) )
		{
			return( new TolerantElemFactory<R,S,T>( sfac , strf ) );
		}
		return( this );
	}
	
	
	@Override
	public TolerantElemFactory<R,S,T> cloneThreadCached( final BigInteger threadIndex , final CloneThreadCache<TolerantElem<R,S,T>,TolerantElemFactory<R,S,T>> cache )
	{
		final TolerantElemFactory<R,S,T> ctmp = cache.getFac( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		S sfac = fac.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		T strf = trf.cloneThread(threadIndex );
		if( ( fac != sfac ) || ( trf != strf ) )
		{
			final TolerantElemFactory<R,S,T> rtmp = new TolerantElemFactory<R,S,T>( sfac , strf );
			cache.putFac(this, rtmp);
			return( rtmp );
		}
		cache.putFac(this, this);
		return( this );
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<TolerantElem<R,S,T>,TolerantElemFactory<R,S,T>> cache , PrintStream ps )
	{
		String st = cache.getFac( this );
		if( st == null )
		{
			cache.applyAuxCache( new WriteTolerantResultFactoryCache<R,S,T>( cache.getCacheVal() ) );
			final String sta = fac.writeDesc( (WriteElemCache<R,S>)( cache.getInnerCache() ) , ps);
			final String stb = trf.writeDesc( (WriteTolerantResultFactoryCache<R,S,T>)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) ((Class)(WriteTolerantResultFactoryCache.class)) ) ) , ps);
			st = cache.getIncrementVal();
			cache.putFac(this, st);
			writeElemFactoryTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			writeElemFactoryTypeString( ps );
			ps.print( "( " );
			ps.print( sta );
			ps.print( " , " );
			ps.print( stb );
			ps.println( " );" );
		}
		return( st );
	}
	
	@Override
	public void writeElemTypeString( PrintStream ps )
	{
		ps.print( TolerantElem.class.getSimpleName() );
		ps.print( "<" );
		fac.writeElemTypeString(ps);
		ps.print( "," );
		fac.writeElemFactoryTypeString(ps);
		ps.print( "," );
		trf.writeTypeString(ps);
		ps.print( ">" );
	}
	
	
	@Override
	public void writeElemFactoryTypeString( PrintStream ps )
	{
		ps.print( TolerantElemFactory.class.getSimpleName() );
		ps.print( "<" );
		fac.writeElemTypeString(ps);
		ps.print( "," );
		fac.writeElemFactoryTypeString(ps);
		ps.print( "," );
		trf.writeTypeString(ps);
		ps.print( ">" );
	}
	
	
	@Override
	public Iterator<TolerantElem<R,S,T>> getApproxLnUnit()
	{
		return( new Iterator<TolerantElem<R,S,T>>()
				{
					/**
					 * The current real iteration.
					 */
					protected Iterator<R> r0 = fac.getApproxLnUnit();
					
					/**
					 * The current state of the real iteration.
					 */
					protected R currentR0;
					
					@Override
					public boolean hasNext() {
						return( r0.hasNext() );
					}

					@Override
					public TolerantElem<R,S,T> next() {
						
						return( new TolerantElem<R,S,T>( r0.next() , trf ) );
					}

					@Override
					public void remove() {
						throw( new RuntimeException( "Not Supported" ) );
					}
					
				} );
	}
	
	
	/**
	 * Gets the factory for enclosed type.
	 * 
	 * @return The factory for the enclosed type.
	 */
	public S getFac()
	{
		return( fac );
	}
	
	
	/**
	 * Gets the factory for tolerant results.
	 * 
	 * @return The factory for the tolerant results.
	 */
	public T getTrf()
	{
		return( trf );
	}
	
	
	/**
	 * The factory for the enclosed type.
	 */
	private S fac;
	
	/**
	 * The factory for tolerant results.
	 */
	private T trf;

}

