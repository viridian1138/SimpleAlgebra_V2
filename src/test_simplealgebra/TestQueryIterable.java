



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




package test_simplealgebra;

import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.query.HGQueryCondition;

import simplealgebra.DoubleElem;
import simplealgebra.store.QueryIterable;
import simplealgebra.store.SegmentedTransactionManager;
import simplealgebra.store.TypeSystemInit;



/**  
 * Simple test of the QueryIterable class.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestQueryIterable extends TestCase {
	
	
	
	
	/**
	 * Simple test of the QueryIterable class.
	 * 
	 * @throws Throwable
	 */
	public void testQueryIterable() throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = "mydb";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		

		
		// Add a set of values to be queried.
		
		graph.add( new DoubleElem( 1.111111 ) );
		
		graph.add( new DoubleElem( 2.2222222 ) );
		
		graph.add( new DoubleElem( 3.3333333 ) );
		
		graph.add( new DoubleElem( 3.6333333 ) );
		
		
		
		HGQueryCondition condition = hg.type( DoubleElem.class );
		
		
		
		QueryIterable<DoubleElem> qi = new QueryIterable<DoubleElem>( graph , condition );
		
		
		
		int cnt = 0;
		
		for( final DoubleElem d : qi )
		{
			Assert.assertTrue( d != null );
			cnt++;
		}
		
		
		
		Assert.assertTrue( cnt == 4 );

		
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		
		graph.close();
		
		// System.out.println( "Done..." ); 
		
	}
	

	

	
}



