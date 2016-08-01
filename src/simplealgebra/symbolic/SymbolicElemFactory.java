




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

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.WriteElemCache;

/**
 * Factory for symbolic elems.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicElemFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<SymbolicElem<R,S>, SymbolicElemFactory<R,S>> {

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicElemFactory( S _fac )
	{
		fac = _fac;
	}
	
	@Override
	public SymbolicElem<R, S> identity() {
		return( new SymbolicIdentity<R,S>( fac ) );
	}

	@Override
	public SymbolicElem<R, S> zero() {
		return( new SymbolicZero<R,S>( fac ) );
	}
	
	@Override
	public boolean isMultCommutative()
	{
		return( fac.isMultCommutative() );
	}
	
	@Override
	public boolean isNestedMultCommutative()
	{
		return( fac.isNestedMultCommutative() );
	}
	
	
	@Override
	public boolean isMultAssociative()
	{
		return( fac.isMultAssociative() );
	}
	
	@Override
	public boolean isNestedMultAssociative()
	{
		return( fac.isNestedMultAssociative() );
	}
	
	@Override
	public Elem<?,?> totalMagnitudeZero()
	{
		return( fac.totalMagnitudeZero() );
	}
	
	
	@Override
	public SymbolicElemFactory<R,S> cloneThread( final BigInteger threadIndex )
	{
		S sfac = fac.cloneThread(threadIndex);
		if( fac != sfac )
		{
			return( new SymbolicElemFactory<R,S>( sfac ) );
		}
		return( this );
	}
	
	
	@Override
	public SymbolicElemFactory<R, S> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<SymbolicElem<R, S>, SymbolicElemFactory<R, S>> cache) {
		final SymbolicElemFactory<R,S> ctmp = cache.getFac( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		S sfac = fac.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( fac != sfac )
		{
			final SymbolicElemFactory<R,S> rtmp = new SymbolicElemFactory<R,S>( sfac );
			cache.putFac(this, rtmp);
			return( rtmp );
		}
		cache.putFac(this, this);
		return( this );
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> cache , PrintStream ps )
	{
		String st = cache.getFac( this );
		if( st == null )
		{
			final String sta = fac.writeDesc( (WriteElemCache<R,S>)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.putFac(this, st);
			writeElemFactoryTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			writeElemFactoryTypeString( ps );
			ps.print( "( " );
			ps.print( sta );
			ps.println( " );" );
		}
		return( st );
	}
	
	@Override
	public void writeElemTypeString( PrintStream ps )
	{
		ps.print( SymbolicElem.class.getSimpleName() );
		writeOrdinaryEnclosedType( ps );
	}
	
	
	@Override
	public void writeElemFactoryTypeString( PrintStream ps )
	{
		ps.print( SymbolicElemFactory.class.getSimpleName() );
		writeOrdinaryEnclosedType( ps );
	}
	
	/**
	 * Writes the enclosed type of an ordinary symbolic elem.
	 * 
	 * @param ps The stream to which to write the enclosed type.
	 */
	public void writeOrdinaryEnclosedType( PrintStream ps )
	{
		ps.print( "<" );
		fac.writeElemTypeString(ps);
		ps.print( "," );
		fac.writeElemFactoryTypeString(ps);
		ps.print( ">" );
	}
	
	
	/**
	 * Gets the factory for the enclosed type.
	 * 
	 * @return The factory for the enclosed type.
	 */
	public S getFac()
	{
		return( fac );
	}
	
	/**
	 * The factory for the enclosed type.
	 */
	private S fac;
	

}

