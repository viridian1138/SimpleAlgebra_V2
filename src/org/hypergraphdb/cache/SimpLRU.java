
 
package org.hypergraphdb.cache;

import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.cache.CacheActionQueueSingleton;
import org.hypergraphdb.cache.LRUCache;
import org.hypergraphdb.util.ActionQueueThread;


/**
 * @author thorngreen
 *
 */
public class SimpLRU extends LRUCache<HGPersistentHandle, IncidenceSet> {
	
	/** 
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
	
	public void evict()
	{
		ActionQueueThread aq = CacheActionQueueSingleton.get();
		aq.addAction(new EvictAction() );
		aq.completeAll();
	}
	
	public void evictNonBlocking()
	{
		CacheActionQueueSingleton.get().addAction(new EvictAction());
	}

}

