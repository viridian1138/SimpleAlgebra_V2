



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

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;


/**
 * DB entity resembling a dense 2-D array.  Basic layout of schema is octree-inspired.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The enclosed type.
 */
public class DbFastArray1D<T extends Object> {
	
	/**
	 * The graph in which the array exists.
	 */
	HyperGraph graph;
	
	/**
	 * The size of each cell along the T-axis.
	 */
	int tmult;
	
	/**
	 * The size of the array along the T-axis.
	 */
	int tmax;
	
	/**
	 * The number of octree-like levels in the structure.
	 */
	int dsz;
	
	/**
	 * Reference to the top node of the array.
	 */
	HGHandle hndl;
	
	
	
	/**
	 * The T-value associated with the cached object in oprev.
	 */
	int tprev = -10000;
	
	/**
	 * The currently cached set of values, or null if there are no cached values.
	 */
	Object[] oprev = null;
	
	/**
	 * True if the values in oprev have changed, and need to be written back to the db.  False otherwise.
	 */
	boolean writeBack = false;
	
	
	
	/**
	 * Constructs the array.
	 * 
	 * @param _graph The graph in which the array exists.
	 * @param _tmult The size of the each cell along the T-axis.
	 * @param _tmax The size of the array along the T-axis.
	 */
	public DbFastArray1D( final HyperGraph _graph , int _tmult , int _tmax )
	{
		graph = _graph;
		tmult = _tmult;
		tmax = _tmax;
		dsz = calcDsz( _tmult , _tmax );
		
		Object hnd = null;
		
		if( dsz > 1 )
		{
			hnd = new HGHandle[ tmult ];
		}
		else
		{
			hnd = new Object[ tmult ];
		}
		
		//graph.getTransactionManager().beginTransaction();
		
		hndl = graph.add( hnd ).getPersistent();
		
		//graph.getTransactionManager().commit();
		
		//graph.getCache().close();
		MemoryClearingSystem.handleCheckClear( graph );
		
	}
	
	
	/**
	 * Gets the object at the 2-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @return The object at the 2-D index, or null if no object exists.
	 */
	public T get( int t , int x )
	{
		if( ( t / tmult == tprev / tmult ) )
		{
			if( oprev != null )
			{
				return( (T)( oprev[ ( t % tmult ) ] ) );
			}
			else
			{
				return( null );
			}
		}
		else
		{
			close();
		}
		
		writeBack = false;
		
		tprev = t;
		
		final int[] indext = new int[ dsz ];
		for( int cnt = 0 ; cnt < dsz ; cnt++ )
		{
			indext[ ( dsz - 1 ) - cnt ] = t % tmult; 
			t = t / tmult;
		}
		
		
		HGHandle cur = hndl;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			//graph.getTransactionManager().beginTransaction();
			HGHandle[] obj = graph.get( cur );
			//graph.getTransactionManager().commit();
			//graph.getCache().close();
			MemoryClearingSystem.handleCheckClear( graph );
			cur = obj[ ( indext[ cnt ] ) ];
			if( cur == null )
			{
				oprev = null;
				return( null );
			}
		}
		
		
		//graph.getTransactionManager().beginTransaction();
		Object[] obj = graph.get( cur );
		//graph.getTransactionManager().commit();
		// graph.getCache().close();
		oprev = obj;
		return( (T)( obj[ ( indext[ dsz - 1 ] ) ] ) );
	}
	
	
	/**
	 * Sets the object at the 2-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param val The object to be set at the index.
	 */
	public void set( int t , int x , T val )
	{
		
		if( ( t / tmult == tprev / tmult ) )
		{
			if( oprev != null )
			{
				oprev[ ( t % tmult ) ] = val;
				
				writeBack = true;
				
				return;
			}
		}
		else
		{
			close();
		}
		
		writeBack = true;
		tprev = t;
		
		final int[] indext = new int[ dsz ];
		for( int cnt = 0 ; cnt < dsz ; cnt++ )
		{
			indext[ ( dsz - 1 ) - cnt ] = t % tmult; 
			t = t / tmult;
		}
		
		
		HGHandle cur = hndl;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			//graph.getTransactionManager().beginTransaction();
			HGHandle[] obj = graph.get( cur );
			HGHandle acur = obj[ ( indext[ cnt ] ) ];
			if( acur == null )
			{
				if( cnt != ( dsz - 2 ) )
				{
					HGHandle[] hnd = new HGHandle[ tmult ];
				
					HGHandle hndd = graph.add( hnd ).getPersistent();
					obj[ ( indext[ cnt ] ) ] = hndd;
					graph.update( obj );
				
					cur = hndd;
				}
				else
				{
					Object[] hnd = new Object[ tmult ];
				
					HGHandle hndd = graph.add( hnd ).getPersistent();
					obj[ ( indext[ cnt ] ) ] = hndd;
					graph.update( obj );
				
					cur = hndd;
				}
			}
			else
			{
				cur = acur;
			}
			//graph.getTransactionManager().commit();
			//graph.getCache().close();
			MemoryClearingSystem.handleCheckClear( graph );
		}
		
		
		//graph.getTransactionManager().beginTransaction();
		Object[] obj = graph.get( cur );
		//graph.getTransactionManager().commit();

		obj[ ( indext[ dsz - 1 ] ) ] = val;
		
		oprev = obj;
	}
	
	
	/**
	 * Closes the array.
	 */
	public void close()
	{
		if( writeBack && ( oprev != null ) )
		{
			// graph.getTransactionManager().beginTransaction();
		
			graph.update( oprev );
		
			// graph.getTransactionManager().commit();
		
			//graph.getCache().close();
			MemoryClearingSystem.handleCheckClear( graph );
		}
	}
	
	
	/**
	 * Calculates the required number of traversal levels.
	 * 
	 * @param _tmult The size of the each cell along the T-axis.
	 * @param _tmax The size of the array along the T-axis.
	 * @return The required number of traversal levels.
	 */
	protected int calcDsz( int _tmult , int _tmax )
	{
		int dsz = 0;
		while( ( _tmax >= _tmult ) )
		{
			_tmax = _tmax / _tmult;
			dsz++;
		}
		return( dsz + 1 );
	}
	
	

}

