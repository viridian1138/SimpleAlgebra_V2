




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





package test_simplealgebra.sort_test;


import simplealgebra.symbolic.DroolsSession;



public class SortNode<R extends Comparable<?> > implements Comparable<SortNode<R>> {
	
	
	public SortNode( R _sortValue , SortNode<R> _next )
	{
		sortValue = _sortValue;
		next = _next;
	}

	
	public SortNode<R> applySort( SortNode<R> p1 , DroolsSession ds )
	{
		SortNode<R> p1p = new SortNode<R>( sortValue , p1.next );
		SortNode<R> p0p = new SortNode( p1.sortValue , p1p );
		ds.insert( p1p );
		ds.insert( p0p );
		return( p0p );
	}
	
	
	public SortNode<R> applyReng( SortNode<R> nxt , DroolsSession ds )
	{
		SortNode<R> p0 = new SortNode<R>( sortValue , nxt );
		ds.insert( p0 );
		return( p0 );
	}
	
	
	public R sortValue = null;
	
	
	public SortNode<R> next = null;


	@Override
	public int compareTo(SortNode<R> arg0) {
		Comparable c = sortValue;
		return( c.compareTo( arg0.sortValue ) );
	}

	
}



