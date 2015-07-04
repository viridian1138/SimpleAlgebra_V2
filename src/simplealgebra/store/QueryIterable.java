



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

import java.util.Collection;
import java.util.Iterator;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.query.HGQueryCondition;

/**
 * An iterable that encapsulates a HyperGraph query.  This makes the query useable
 * by classes that recognize the Iterable interface.  The class also implements the full
 * Collection interface, but doesn't support most of the Collection methods.
 * 
 * @author thorngreen
 *
 */
public class QueryIterable<T extends Object> implements Collection<T>, Iterable<T> {

	/**
	 * The graph over which to query.
	 */
	private HyperGraph graph;
	
	/**
	 * The condition for the query.
	 */
	private HGQueryCondition condition;

	/**
	 * Constructs the iterable.
	 * 
	 * @param _graph The qraph over which to query.
	 * @param _condition The condition for the query.
	 */
	public QueryIterable( HyperGraph _graph , HGQueryCondition _condition ) {
		graph = _graph;
		condition = _condition;
	}
	
	
	/**
	 * The iterator for the individual query instances.
	 * 
	 * @author thorngreen
	 *
	 */
	protected class QueryIterator implements Iterator<T>
	{
		/**
		 * The search result over which to iterate.
		 */
		protected HGSearchResult<HGHandle> rs;
		
		/**
		 * Constructs the iterator.
		 * @param _rs The seqrch result over which to iterate.
		 */
		public QueryIterator( HGSearchResult<HGHandle> _rs )
		{
			rs = _rs;
		}

		@Override
		public boolean hasNext() {
			return( rs.hasNext() );
		}

		@Override
		public T next() {
			HGHandle current = rs.next();
			T ret = graph.get( current );
			return( ret );
		}

		@Override
		public void remove() {
			rs.remove();
		}
		
	};
	
	
	@Override
	public Iterator<T> iterator()
	{
		HGSearchResult<HGHandle> rs = graph.find( condition );
		return( new QueryIterator( rs ) );
	}


	@Override
	public int size() {
		int cnt = 0;
		Iterator<T> it = iterator();
		while( it.hasNext() )
		{
			T ob = it.next();
			cnt++;
		}
		return( cnt );
	}


	@Override
	public boolean isEmpty() {
		return( !( iterator().hasNext() ) );
	}


	@Override
	public boolean contains(Object o) {
		Iterator<T> it = iterator();
		while( it.hasNext() )
		{
			T ob = it.next();
			if( ob.equals( o ) )
			{
				return( true );
			}
		}
		return( false );
	}


	@Override
	public Object[] toArray() {
		throw( new RuntimeException( "Not Supported" ) );
	}


	@Override
	public <T> T[] toArray(T[] a) {
		throw( new RuntimeException( "Not Supported" ) );
	}


	@Override
	public boolean add(T e) {
		throw( new RuntimeException( "Not Supported" ) );
	}


	@Override
	public boolean remove(Object o) {
		throw( new RuntimeException( "Not Supported" ) );
	}


	@Override
	public boolean containsAll(Collection<?> c) {
		throw( new RuntimeException( "Not Supported" ) );
	}


	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw( new RuntimeException( "Not Supported" ) );
	}


	@Override
	public boolean removeAll(Collection<?> c) {
		throw( new RuntimeException( "Not Supported" ) );
	}


	@Override
	public boolean retainAll(Collection<?> c) {
		throw( new RuntimeException( "Not Supported" ) );
	}


	@Override
	public void clear() {
		throw( new RuntimeException( "Not Supported" ) );
	}
	

	
}


