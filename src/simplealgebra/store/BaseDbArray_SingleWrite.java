



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
import java.util.WeakHashMap;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HGValueLink;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.handle.WeakHandle;
import org.hypergraphdb.query.And;
import org.hypergraphdb.query.HGQueryCondition;


/**
 * DB entity resembling a sparse multidimensional array.  For performance reasons it is
 * assumed that there will only be a single write to each index location.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The enclosed type.
 */
public class BaseDbArray_SingleWrite<T extends Object> {
	
	/**
	 * The graph on which to perform DB operations.
	 */
	protected HyperGraph graph;
	
	/**
	 * The stored DB base reference.
	 */
	protected HGHandle hbase = null;
	
	/**
	 * Cache memory to speed read access times.
	 */
	protected final WeakHashMap<ArrayList<BigInteger>,T> cacheMemory = new WeakHashMap<ArrayList<BigInteger>,T>();
	
	
	
	/**
	 * Constructs the array.
	 * 
	 * @param _graph The graph on which to perform DB operations.
	 */
	public BaseDbArray_SingleWrite( final HyperGraph _graph )
	{
		graph = _graph;
		RelBase base = new RelBase();
		hbase = graph.add( base );
	}
	
	
	/**
	 * Gets an object at a particular index.
	 * 
	 * @param arb The index.
	 * @return The object at the index, or null if no such object exists.
	 */
	public T query( final ArrayList<BigInteger> arb )
	{
		T ret = cacheMemory.get( arb );
		if( ret != null )
		{
			return( ret );
		}
		
		HGQueryCondition condition = new And( hg.link( hbase ) , hg.eq( arb ) );
		
		
		HGSearchResult<HGHandle> rs = graph.find( condition );
		
		// System.out.println( "Start Loop..." );
		while( rs.hasNext() )
		{
			HGHandle current = rs.next();
			HGValueLink l = graph.get( current );
			WeakHandle hn = (WeakHandle)( l.getTargetAt( 1 ) );
			RelPayload<T> p = (RelPayload<T>)( hn.get() );
			if( p.getD() != null )
			{
				if( ret == null )
				{
					ret = p.getD();
				}
				else
				{
					throw( new RuntimeException( "Internal Error In Abstract Db Array" ) );
				}
			}
			MemoryClearingSystem.handleCheckClear( graph );
		}
		
		if( ret != null )
		{
			cacheMemory.put( arb , ret );
		}
		MemoryClearingSystem.handleCheckClear( graph );
		return( ret );
	}
	
	
	/**
	 * Inserts an object at a particular index.
	 * 
	 * @param arb The index.
	 * @param val The object to be inserted.
	 */
	public void insert( final ArrayList<BigInteger> arb , T val )
	{
		final RelPayload<T> pa = new RelPayload<T>();
		
		pa.setD( val );
		
		final HGHandle hpa = graph.add( pa );
		
		final HGValueLink hga = new HGValueLink( hbase , hpa );
		
		hga.setValue( arb );
		
		graph.add( hga );
		
		cacheMemory.put( arb , val );
		
		MemoryClearingSystem.handleCheckClear( graph );
	}
	

	
}


