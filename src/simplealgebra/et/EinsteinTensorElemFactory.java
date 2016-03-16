




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




package simplealgebra.et;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.SymbolicElem;


/**
 * A factory for tensor elems.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the tensor indices.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class EinsteinTensorElemFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<EinsteinTensorElem<Z,R,S>, EinsteinTensorElemFactory<Z,R,S>> {

	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public EinsteinTensorElemFactory( S _fac )
	{
		fac = _fac;
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> identity() {
		EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z, R, S>( fac.identity() , fac );
		return( ret );
	}

	@Override
	public EinsteinTensorElem<Z, R, S> zero() {
		return( new EinsteinTensorElem<Z, R, S>( fac , new ArrayList<Z>() , new ArrayList<Z>() ) );
	}
	
	
	/**
	 * Returns the factory for the enclosed type.
	 * 
	 * @return The factory for the enclosed type.
	 */
	public S getFac()
	{
		return( fac );
	}
	
	
	@Override
	public boolean isMultCommutative()
	{
		return( false );
	}
	
	
	@Override
	public boolean isNestedMultCommutative()
	{
		return( fac.isMultCommutative() );
	}
	
	
	@Override
	public boolean isMultAssociative()
	{
		return( false );
	}
	
	
	@Override
	public boolean isNestedMultAssociative()
	{
		return( fac.isMultAssociative() );
	}
	
	
	@Override
	public SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z,R,S>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z,R,S>>> args )  throws NotInvertibleException
	{
		if( id instanceof EinsteinTensorElem.EinsteinTensorCmd )
		{
			switch( (EinsteinTensorElem.EinsteinTensorCmd) id )
			{
				case RANK_TWO_TRACE:
				{
					SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z,R,S>> argA
						= args.get( 0 );
					return( new SymbolicRankTwoTrace<Z,R,S>( argA , argA.getFac().getFac() ) );
				}
				// break;
				
			}
		}
		
		return( super.handleSymbolicOptionalOp(id, args) );
	}
	
	
	@Override
	public EinsteinTensorElemFactory<Z,R,S> cloneThread( final BigInteger threadIndex )
	{
		S sfac = fac.cloneThread( threadIndex );
		if( fac != sfac )
		{
			return( new EinsteinTensorElemFactory<Z,R,S>( sfac ) );
		}
		return( this );
	}
	
	
	@Override
	public EinsteinTensorElemFactory<Z, R, S> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>> cache) {
		final EinsteinTensorElemFactory<Z,R,S> ctmp = cache.getFac( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		S sfac = fac.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( fac != sfac )
		{
			final EinsteinTensorElemFactory<Z,R,S> rtmp = new EinsteinTensorElemFactory<Z,R,S>( sfac );
			cache.putFac(this, rtmp);
			return( rtmp );
		}
		cache.putFac(this, this);
		return( this );
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> cache , PrintStream ps )
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
		ps.print( EinsteinTensorElem.class.getSimpleName() );
		ps.print( "<? extends Object," );
		fac.writeElemTypeString(ps);
		ps.print( "," );
		fac.writeElemFactoryTypeString(ps);
		ps.print( ">" );
	}
	
	
	@Override
	public void writeElemFactoryTypeString( PrintStream ps )
	{
		ps.print( EinsteinTensorElemFactory.class.getSimpleName() );
		ps.print( "<? extends Object," );
		fac.writeElemTypeString(ps);
		ps.print( "," );
		fac.writeElemFactoryTypeString(ps);
		ps.print( ">" );
	}
	
	
	/**
	 * The factory for the enclosed type.
	 */
	private S fac;


}



