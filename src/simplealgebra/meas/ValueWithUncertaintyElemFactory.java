



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




package simplealgebra.meas;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.AbsoluteValue;
import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.SymbolicAbsoluteValue;
import simplealgebra.symbolic.SymbolicElem;

/**
 * Factory for a ValueWithUncertaintyElem.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class ValueWithUncertaintyElemFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<ValueWithUncertaintyElem<R,S>, ValueWithUncertaintyElemFactory<R,S>> {

	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public ValueWithUncertaintyElemFactory( S _fac )
	{
		fac = _fac;
	}

	@Override
	public ValueWithUncertaintyElem<R, S> identity() {
		return( new ValueWithUncertaintyElem<R,S>( fac.identity() , fac.zero() ) );
	}

	@Override
	public ValueWithUncertaintyElem<R, S> zero() {
		return( new ValueWithUncertaintyElem<R,S>( fac.zero() , fac.zero() ) );
	}
	
	
	@Override
	public SymbolicElem<ValueWithUncertaintyElem<R, S>, ValueWithUncertaintyElemFactory<R,S>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<ValueWithUncertaintyElem<R, S>, ValueWithUncertaintyElemFactory<R,S>>> args )  throws NotInvertibleException
	{
		if( id instanceof AbsoluteValue )
		{
			switch( (AbsoluteValue) id )
			{
				case ABSOLUTE_VALUE:
				{
					SymbolicElem<ValueWithUncertaintyElem<R, S>, ValueWithUncertaintyElemFactory<R,S>> arg
						= args.get( 0 );
					return( new SymbolicAbsoluteValue<ValueWithUncertaintyElem<R, S>, ValueWithUncertaintyElemFactory<R,S>>( arg , arg.getFac().getFac() ) );
				}
				// break;
				
			}
		}
		
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
	public ValueWithUncertaintyElemFactory<R,S> cloneThread( final BigInteger threadIndex )
	{
		S sfac = fac.cloneThread(threadIndex);
		if( fac != sfac )
		{
			return( new ValueWithUncertaintyElemFactory<R,S>( sfac ) );
		}
		return( this );
	}
	
	
	@Override
	public ValueWithUncertaintyElemFactory<R,S> cloneThreadCached( final BigInteger threadIndex , final CloneThreadCache<ValueWithUncertaintyElem<R,S>,ValueWithUncertaintyElemFactory<R,S>> cache )
	{
		final ValueWithUncertaintyElemFactory<R,S> ctmp = cache.getFac( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		S sfac = (S) fac.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( fac != sfac )
		{
			final ValueWithUncertaintyElemFactory<R,S> rtmp = new ValueWithUncertaintyElemFactory<R,S>( sfac );
			cache.putFac(this, rtmp);
			return( rtmp );
		}
		cache.putFac(this, this);
		return( this );
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<ValueWithUncertaintyElem<R,S>,ValueWithUncertaintyElemFactory<R,S>> cache , PrintStream ps )
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
		ps.print( ValueWithUncertaintyElem.class.getSimpleName() );
		ps.print( "<" );
		fac.writeElemTypeString(ps);
		ps.print( "," );
		fac.writeElemFactoryTypeString(ps);
		ps.print( ">" );
	}
	
	
	@Override
	public void writeElemFactoryTypeString( PrintStream ps )
	{
		ps.print( ValueWithUncertaintyElemFactory.class.getSimpleName() );
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

