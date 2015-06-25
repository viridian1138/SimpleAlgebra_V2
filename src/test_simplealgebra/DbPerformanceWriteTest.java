



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


import org.hypergraphdb.cache.LRUCache;
import org.hypergraphdb.cache.SimpLRU;

import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.cache.HGCache;

import simplealgebra.store.MemoryClearingSystem;
import simplealgebra.store.TypeSystemInit;



/**
 * Tests HypergraphDB.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class DbPerformanceWriteTest extends TestCase {
	
	
	/**
	 * The number of X-Axis entries.
	 */
	static final int X_SZ = 200;
	
	/**
	 * The number of Y-Axis entries.
	 */
	static final int Y_SZ = 100;
	
	/**
	 * The number of T-Axis entries.
	 */
	static final int T_SZ = 10000;
	
	
	
	/**
	 * Creates a random row as a 2-D array.
	 * 
	 * @param rand The input random number generator.
	 * @return The output 2-D array.
	 */
	protected double[][] createRow( Random rand )
	{
		final double[][] vl = new double[ X_SZ ][ Y_SZ ];
		
		for( int x = 0 ; x < X_SZ ; x++ )
		{
			for( int y = 0 ; y < Y_SZ ; y++ )
			{
				vl[ x ][ y ] = rand.nextDouble();
			}
		}
		
		return( vl );
	}

	
	
	
	/**
	 * Test Hypergraph execution
	 */
	public void testDbPerformanceWrite() throws Throwable
	{
		Random rand = new Random( 2345 );
		
		

		String databaseLocation = "mydbV";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );

		
		TypeSystemInit.initType( graph );
		
		
		
		HGHandle[] hgs = new HGHandle[ T_SZ ];
		
		
		System.out.println( "Write Test Start..." );
		
		
		for( int t = 0 ; t < T_SZ ; t++ )
		{
			if( ( t % 10 ) == 0 )
			{
				System.out.println( "/// " + t );
			}
			
			graph.getTransactionManager().beginTransaction();
			
			hgs[ t ] = graph.add( createRow( rand ) ).getPersistent();
			
			graph.getTransactionManager().commit();
			
			//if( ( t % 2 ) == 0 )
			//{
			//	graph.getCache().close();
			//}
			MemoryClearingSystem.handleCheckClear( graph );
			
			//if( ( t % 5 ) == 0 )
			//{
			//	synchronized( Thread.currentThread() )
			//	{
			//		Thread.currentThread().wait( 500 );
			//	}
			//}
			
		}
		
		
		System.out.println( "Write Test End..." );
		
		
		
		
		System.out.println( "Begin Read Test..." );
		
		
		for( int count = 0 ; count < 5000 ; count++ )
		{
			if( ( count % 10 ) == 0 )
			{
				System.out.println( "/// " + count );
			}
			
			int rowNum = rand.nextInt( T_SZ );
			
			graph.getTransactionManager().beginTransaction();
			
			double[][] row = graph.get( hgs[ rowNum ] );
			
			graph.getTransactionManager().commit();
			
			//if( ( count % 2 ) == 0 )
			//{
			//	graph.getCache().close();
			//}
			MemoryClearingSystem.handleCheckClear( graph );
			
			//if( ( count % 5 ) == 0 )
			//{
			//	synchronized( Thread.currentThread() )
			//	{
			//		Thread.currentThread().wait( 500 );
			//	}
			//}
			
			Assert.assertTrue( row != null );
		}
		
		
		System.out.println( "End Read Test..." );
		
		
		
		
		graph.close();
		
		
	}
	
	
	


	
	
}


