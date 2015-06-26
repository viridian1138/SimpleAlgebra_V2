



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





 
package org.hypergraphdb.cache;

import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.cache.CacheActionQueueSingleton;
import org.hypergraphdb.cache.LRUCache;
import org.hypergraphdb.util.ActionQueueThread;


/**
 * Subclass of LRUCache for which the objects can be evicted by the MemoryClearingSystem cache.
 * 
 * @author thorngreen
 *
 */
public class SimpLRU extends LRUCache<HGPersistentHandle, IncidenceSet> {
	
	/** 
	 * Constructs the cache.
	 * 
	 * @param usedMemoryThreshold The percentage of total memory that
	 * must become used before the cache decides to evict elements (e.g. 
	 * a value of 0.9 means the cache will evict elements when 90% of memory
	 * is currently in use). 
	 * @param evictPercent The percentage of elements to evict when the
	 * usedMemoryThreshold is reached.
	 */
	public SimpLRU(float usedMemoryThreshold, float evictPercent)
	{
		super( usedMemoryThreshold , evictPercent );
	}
	
	/**
	 * Performs the eviction in a blocking fashion.
	 */
	public void evict()
	{
		ActionQueueThread aq = CacheActionQueueSingleton.get();
		aq.addAction(new EvictAction() );
		aq.completeAll();
	}
	
	/**
	 * Performs the eviction in a non-blocking fashion.
	 */
	public void evictNonBlocking()
	{
		CacheActionQueueSingleton.get().addAction(new EvictAction());
	}

}

