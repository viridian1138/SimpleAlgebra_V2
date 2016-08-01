



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




package simplealgebra;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.symbolic.SymbolicElem;


/**
 * Factory for complex number elems.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class ComplexElemFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<ComplexElem<R,S>, ComplexElemFactory<R,S>> {

	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public ComplexElemFactory( S _fac )
	{
		fac = _fac;
	}

	@Override
	public ComplexElem<R, S> identity() {
		return( new ComplexElem<R,S>( fac.identity() , fac.zero() ) );
	}

	@Override
	public ComplexElem<R, S> zero() {
		return( new ComplexElem<R,S>( fac.zero() , fac.zero() ) );
	}
	
	
	@Override
	public Elem<?,?> totalMagnitudeZero()
	{
		return( fac.zero() );
	}
	
	
	@Override
	public SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R,S>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R,S>>> args )  throws NotInvertibleException
	{
		if( id instanceof ComplexElem.ComplexCmd )
		{
			switch( (ComplexElem.ComplexCmd) id )
			{
				case CONJUGATE_LEFT:
				{
					SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R,S>> arg
						= args.get( 0 );
					return( new SymbolicConjugateLeft<R,S>( arg , arg.getFac().getFac() ) );
				}
				// break;
				
				case CONJUGATE_RIGHT:
				{
					SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R,S>> arg
						= args.get( 0 );
					return( new SymbolicConjugateRight<R,S>( arg , arg.getFac().getFac() ) );
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
	public ComplexElemFactory<R,S> cloneThread( final BigInteger threadIndex )
	{
		S sfac = fac.cloneThread(threadIndex);
		if( fac != sfac )
		{
			return( new ComplexElemFactory<R,S>( sfac ) );
		}
		return( this );
	}
	
	
	@Override
	public ComplexElemFactory<R,S> cloneThreadCached( final BigInteger threadIndex , final CloneThreadCache<ComplexElem<R,S>,ComplexElemFactory<R,S>> cache )
	{
		final ComplexElemFactory<R,S> ctmp = cache.getFac( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		S sfac = fac.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( fac != sfac )
		{
			final ComplexElemFactory<R,S> rtmp = new ComplexElemFactory<R,S>( sfac );
			cache.putFac(this, rtmp);
			return( rtmp );
		}
		cache.putFac(this, this);
		return( this );
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<ComplexElem<R,S>,ComplexElemFactory<R,S>> cache , PrintStream ps )
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
		ps.print( ComplexElem.class.getSimpleName() );
		ps.print( "<" );
		fac.writeElemTypeString(ps);
		ps.print( "," );
		fac.writeElemFactoryTypeString(ps);
		ps.print( ">" );
	}
	
	
	@Override
	public void writeElemFactoryTypeString( PrintStream ps )
	{
		ps.print( ComplexElemFactory.class.getSimpleName() );
		ps.print( "<" );
		fac.writeElemTypeString(ps);
		ps.print( "," );
		fac.writeElemFactoryTypeString(ps);
		ps.print( ">" );
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
	 * The factory for the enclosed type.
	 */
	private S fac;

}

