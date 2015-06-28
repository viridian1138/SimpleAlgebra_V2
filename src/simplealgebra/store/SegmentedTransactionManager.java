





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




import org.hypergraphdb.*;



/**
 * Manages a transaction that temporarily commits (and hence releases the memory associated with the transaction) 
 * at the point the VM runs out of memory.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 */
public class SegmentedTransactionManager {
	
	/**
	 * Stores whether a segmented transaction is in progress.
	 */
	protected static boolean inSegmentedTransaction = false;
	
	
	/**
	 * Begins the segmented transaction.
	 * 
	 * @param graph The graph over which to execute the transaction.
	 */
	public static void beginSegmentedTransaction( HyperGraph graph )
	{
		graph.getTransactionManager().beginTransaction();
		inSegmentedTransaction = true;
	}
	
	
	/**
	 * Commits the segmented transaction.
	 * 
	 * @param graph The graph over which to execute the transaction.
	 */
	public static void commitSegmentedTransaction( HyperGraph graph )
	{
		graph.getTransactionManager().commit();
		inSegmentedTransaction = false;
	}
	
	
	/**
	 * Segments (temporarily commits) the transaction so that memory can 
	 * be reclaimed.  This method should only be called by the MemoryClearingSystem
	 * class.
	 * 
	 * @param graph The graph over which to execute the transaction.
	 */
	public static void suspendSegmentedTransaction( HyperGraph graph )
	{
		if( inSegmentedTransaction )
		{
			graph.getTransactionManager().commit();
		}
	}
	
	
	/**
	 * Restarts the segmented transaction after memory has 
	 * been reclaimed.  This method should only be called by the MemoryClearingSystem
	 * class.
	 * 
	 * @param graph The graph over which to execute the transaction.
	 */
	public static void restartSegmentedTransaction( HyperGraph graph )
	{
		if( inSegmentedTransaction )
		{
			graph.getTransactionManager().beginTransaction();
		}
	}
	

	
}

