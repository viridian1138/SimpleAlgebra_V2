



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


import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

import simplealgebra.store.DbFastArray2D;
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
public class DbFastArrayTest extends TestCase {
	
	
	static final int T_SZ = 100;

	
	
	/**
	 * Test Hypergraph execution
	 */
	public void testFastArray2D() throws Throwable
	{
		Random rand = new Random( 2345 );
		
		

		String databaseLocation = "mydbF";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );

		
		TypeSystemInit.initType( graph );
		
		
		
		DbFastArray2D<Double> db =
				new DbFastArray2D<Double>( graph , 3 , 3 , T_SZ , 10 );
		
		
		System.out.println( "Write Test Start..." );
		
		
		for( int t = 0 ; t < T_SZ ; t++ )
		{
			if( ( t % 10 ) == 0 )
			{
				System.out.println( "/// " + t );
			}
			
			for( int x = 0 ; x < 10 ; x++ )
			{
				if( ( x % 10 ) == 0 )
				{
					System.out.println( "+++ " + t );
				}
				
				db.set(t, x, rand.nextDouble() );
			}
			
		}
		
		
		System.out.println( "Write Test End..." );
		
		
		
		
		System.out.println( "Begin Read Test..." );
		
		
		for( int count = 0 ; count < 5000 ; count++ )
		{
			if( ( count % 10 ) == 0 )
			{
				System.out.println( "/// " + count );
			}
			
			Double vl = db.get( rand.nextInt( T_SZ ) , rand.nextInt( 10 ) );
			
			//graph.getCache().close();
			MemoryClearingSystem.handleCheckClear( graph );
			
			Assert.assertTrue( vl != null );
		}
		
		
		System.out.println( "End Read Test..." );
		
		
		
		db.close();
		
		graph.close();
		
		
	}
	
	
	


	
	
}


