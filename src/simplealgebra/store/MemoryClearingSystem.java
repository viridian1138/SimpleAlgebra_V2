



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



import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.cache.SimpLRU;




/**
 * Class for automatically clearing unused Hypergraph memory.
 * 
 * @author thorngreen
 *
 */
public class MemoryClearingSystem {
	
	/**
	 * Maximum number of checks before the transaction is suspended
	 * to prevent performance impacts from excessively long transaction logs.
	 */
	private static final int MAX_TRANSACTION_CNT = 100;
	
	/**
	 * Count of number of checks since last transaction suspend.
	 */
	private static int transactionCnt = 0;
	

	/**
	 * Initializes the memory clearing system.
	 * 
	 * @param graph The graph from which to clear memory.
	 */
	public static void initMemoryClearing( HyperGraph graph )
	{
		graph.getCache().setIncidenceCache(
	        	new SimpLRU(0.5f, 0.3f) );
		transactionCnt = 0;
	}
	
	
	/**
	 * Clears memory from the graph if necessary.
	 * 
	 * @param graph The graph from which to clear memory.
	 */
	public static void handleCheckClear( HyperGraph graph )
	{
		final long freeMemory = Runtime.getRuntime().freeMemory();
		final long totalMemory = Runtime.getRuntime().totalMemory();
		final long totalMem10 = totalMemory / 5L;
		transactionCnt++;
		if( freeMemory <= totalMem10 )
		{
			SegmentedTransactionManager.suspendSegmentedTransaction( graph );
			( (SimpLRU)( graph.getCache().getIncidenceCache() ) ).evict();
			SegmentedTransactionManager.restartSegmentedTransaction( graph );
			transactionCnt = 0;
		}
		else
		{
			if( transactionCnt > MAX_TRANSACTION_CNT )
			{
				System.out.println( "Commit..." );
				SegmentedTransactionManager.suspendSegmentedTransaction( graph );
				SegmentedTransactionManager.restartSegmentedTransaction( graph );
				transactionCnt = 0;
			}
		}
		
	}
	

	
}


