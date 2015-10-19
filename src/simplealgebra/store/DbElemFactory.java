



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




package simplealgebra.store;

import java.math.BigInteger;
import java.util.ArrayList;

import org.hypergraphdb.HyperGraph;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.SymbolicElem;


/**
 * Factory for DB wrappers.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class DbElemFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<DbElem<R,S>, DbElemFactory<R,S>> {

	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public DbElemFactory( S _fac , HyperGraph _graph )
	{
		fac = _fac;
		graph = _graph;
	}

	@Override
	public DbElem<R, S> identity() {
		return( new DbElem<R,S>( fac.identity() , graph ) );
	}

	@Override
	public DbElem<R, S> zero() {
		return( new DbElem<R,S>( fac.zero() , graph ) );
	}
	
	
	@Override
	public SymbolicElem<DbElem<R, S>, DbElemFactory<R,S>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<DbElem<R, S>, DbElemFactory<R,S>>> args )  throws NotInvertibleException
	{
		throw( new RuntimeException( "Not Supported..." ) ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
	
	
	
	
	
	/**
	 * Gets the graph for the factory.
	 * 
	 * @return The graph for the factory.
	 */
	public HyperGraph getGraph() {
		return graph;
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
	
	
	@Override
	public DbElemFactory<R,S> cloneThread( final BigInteger threadIndex )
	{
		S sfac = fac.cloneThread(threadIndex);
		if( fac != sfac )
		{
			return( new DbElemFactory<R,S>( sfac , graph ) );
		}
		return( this );
	}
	
	
	@Override
	public DbElemFactory<R, S> cloneThreadCached(BigInteger threadIndex,
			CloneThreadCache<DbElem<R, S>, DbElemFactory<R, S>> cache) {
		final DbElemFactory<R,S> ctmp = cache.getFac( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		S sfac = fac.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( fac != sfac )
		{
			final DbElemFactory<R,S> rtmp = new DbElemFactory<R,S>( sfac , graph );
			cache.putFac(this, rtmp);
			return( rtmp );
		}
		cache.putFac(this, this);
		return( this );
	}
	
	
	/**
	 * The factory for the enclosed type.
	 */
	private S fac;
	
	
	/**
	 * The graph on which to perform DB operations.
	 */
	protected HyperGraph graph;

	
}

