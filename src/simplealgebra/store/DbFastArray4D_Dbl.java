



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
 * DB entity resembling a dense 4-D array of doubles.  Basic layout of schema is octree-inspired.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 */
public class DbFastArray4D_Dbl {
	
	/**
	 * The graph in which the array exists.
	 */
	HyperGraph graph;
	
	/**
	 * The size of each cell along the T-axis.
	 */
	int tmult;
	
	/**
	 * The size of each cell along the X-axis.
	 */
	int xmult;
	
	/**
	 * The size of each cell along the Y-axis.
	 */
	int ymult;
	
	/**
	 * The size of each cell along the Z-axis.
	 */
	int zmult;
	
	/**
	 * The size of the array along the T-axis.
	 */
	int tmax;
	
	/**
	 * The size of the array along the X-axis.
	 */
	int xmax;
	
	/**
	 * The size of the array along the Y-axis.
	 */
	int ymax;
	
	/**
	 * The size of the array along the Z-axis.
	 */
	int zmax;
	
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
	 * The X-value associated with the cached object in oprev.
	 */
	int xprev = -10000;
	
	/**
	 * The Y-value associated with the cached object in oprev.
	 */
	int yprev = -10000;
	
	/**
	 * The Z-value associated with the cached object in oprev.
	 */
	int zprev = -10000;
	
	/**
	 * The currently cached set of values, or null if there are no cached values.
	 */
	double[] oprev = null;
	
	/**
	 * True if the values in oprev have changed, and need to be written back to the db.  False otherwise.
	 */
	boolean writeBack = false;
	
	
	
	/**
	 * Constructs the array.
	 * 
	 * @param _graph The graph in which the array exists.
	 * @param _tmult The size of the each cell along the T-axis.
	 * @param _xmult The size of the each cell along the X-axis.
	 * @param _ymult The size of the each cell along the Y-axis.
	 * @param _zmult The size of the each cell along the Z-axis.
	 * @param _tmax The size of the array along the T-axis.
	 * @param _xmax The size of the array along the X-axis.
	 * @param _ymax The size of the array along the Y-axis.
	 * @param _zmax The size of the array along the Z-axis.
	 */
	public DbFastArray4D_Dbl( final HyperGraph _graph , int _tmult , int _xmult , int _ymult , int _zmult ,
			int _tmax , int _xmax , int _ymax , int _zmax )
	{
		graph = _graph;
		tmult = _tmult;
		xmult = _xmult;
		ymult = _ymult;
		zmult = _zmult;
		tmax = _tmax;
		xmax = _xmax;
		ymax = _ymax;
		zmax = _zmax;
		dsz = calcDsz( _tmult , _xmult , _ymult , _zmult ,
				_tmax , _xmax , _ymax , _zmax );
		
		Object hnd = null;
		
		if( dsz > 1 )
		{
			hnd = new HGHandle[ tmult * xmult * ymult * zmult ];
		}
		else
		{
			hnd = new double[ tmult * xmult * ymult * zmult ];
		}
		
		graph.getTransactionManager().beginTransaction();
		
		hndl = graph.add( hnd ).getPersistent();
		
		graph.getTransactionManager().commit();
		
		//graph.getCache().close();
		MemoryClearingSystem.handleCheckClear( graph );
		
	}
	
	
	/**
	 * Gets the object at the 4-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param y The "Y" index of the array.
	 * @param z The "Z" index of the array.
	 * @return The value at the 4-D index, or zero if no value exists.
	 */
	public double get( int t , int x , int y , int z )
	{
		if( ( t / tmult == tprev / tmult ) && ( x / xmult == xprev / xmult ) 
				&& ( y / ymult == yprev / ymult ) && ( z / zmult == zprev / zmult ) )
		{
			if( oprev != null )
			{
				return( oprev[ ( t % tmult ) * ( xmult * ymult * zmult ) + ( x % xmult ) * ( ymult * zmult ) + ( y % ymult ) * ( zmult) + ( z % zmult ) ] );
			}
			else
			{
				return( 0.0 );
			}
		}
		else
		{
			close();
		}
		
		writeBack = false;
		
		tprev = t;
		xprev = x;
		yprev = y;
		zprev = z;
		
		final int[] indext = new int[ dsz ];
		final int[] indexx = new int[ dsz ];
		final int[] indexy = new int[ dsz ];
		final int[] indexz = new int[ dsz ];
		for( int cnt = 0 ; cnt < dsz ; cnt++ )
		{
			indext[ ( dsz - 1 ) - cnt ] = t % tmult; 
			indexx[ ( dsz - 1 ) - cnt ] = x % xmult; 
			indexy[ ( dsz - 1 ) - cnt ] = y % ymult;
			indexz[ ( dsz - 1 ) - cnt ] = z % zmult;
			t = t / tmult;
			x = x / xmult;
			y = y / ymult;
			z = z / zmult;
		}
		
		
		HGHandle cur = hndl;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			graph.getTransactionManager().beginTransaction();
			HGHandle[] obj = graph.get( cur );
			graph.getTransactionManager().commit();
			//graph.getCache().close();
			MemoryClearingSystem.handleCheckClear( graph );
			cur = obj[ ( indext[ cnt ] ) * ( xmult * ymult * zmult ) + ( indexx[ cnt ] ) * ( ymult * zmult ) + ( indexy[ cnt ] ) * ( zmult ) + indexz[ cnt ] ];
			if( cur == null )
			{
				oprev = null;
				return( 0.0 );
			}
		}
		
		
		graph.getTransactionManager().beginTransaction();
		double[] obj = graph.get( cur );
		graph.getTransactionManager().commit();
		// graph.getCache().close();
		oprev = obj;
		return( obj[ ( indext[ dsz - 1 ] ) * ( xmult * ymult * zmult ) + ( indexx[ dsz - 1 ] ) * ( ymult * zmult ) + ( indexy[ dsz - 1 ] ) * ( zmult ) + indexz[ dsz - 1 ] ] );
	}
	
	
	/**
	 * Sets the value at the 4-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param y The "Y" index of the array.
	 * @param z The "Z" index of the array.
	 * @param val The value to be set at the index.
	 */
	public void set( int t , int x , int y , int z , double val )
	{
		
		if( ( t / tmult == tprev / tmult ) && ( x / xmult == xprev / xmult ) 
				&& ( y / ymult == yprev / ymult ) && ( z / zmult == zprev / zmult ) )
		{
			if( oprev != null )
			{
				oprev[ ( t % tmult ) * ( xmult * ymult * zmult ) + ( x % xmult ) * ( ymult * zmult ) + ( y % ymult ) * ( zmult ) + ( z % zmult ) ] = val;
				
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
		xprev = x;
		yprev = y;
		zprev = z;
		
		final int[] indext = new int[ dsz ];
		final int[] indexx = new int[ dsz ];
		final int[] indexy = new int[ dsz ];
		final int[] indexz = new int[ dsz ];
		for( int cnt = 0 ; cnt < dsz ; cnt++ )
		{
			indext[ ( dsz - 1 ) - cnt ] = t % tmult; 
			indexx[ ( dsz - 1 ) - cnt ] = x % xmult; 
			indexy[ ( dsz - 1 ) - cnt ] = y % ymult; 
			indexz[ ( dsz - 1 ) - cnt ] = z % zmult;
			t = t / tmult;
			x = x / xmult;
			y = y / ymult;
			z = z / zmult;
		}
		
		
		HGHandle cur = hndl;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			graph.getTransactionManager().beginTransaction();
			HGHandle[] obj = graph.get( cur );
			HGHandle acur = obj[ ( indext[ cnt ] ) * ( xmult * ymult * zmult ) + ( indexx[ cnt ] ) * ( ymult * zmult ) + ( indexy[ cnt ] ) * ( zmult ) + indexz[ cnt ] ];
			if( acur == null )
			{
				if( cnt != ( dsz - 2 ) )
				{
					HGHandle[] hnd = new HGHandle[ tmult * xmult * ymult * zmult ];
				
					HGHandle hndd = graph.add( hnd ).getPersistent();
					obj[ ( indext[ cnt ] ) * ( xmult * ymult * zmult ) + ( indexx[ cnt ] ) * ( ymult * zmult ) + ( indexy[ cnt ] ) * ( zmult ) + ( indexz[ cnt ] ) ] = hndd;
					graph.update( obj );
				
					cur = hndd;
				}
				else
				{
					double[] hnd = new double[ tmult * xmult * ymult * zmult ];
				
					HGHandle hndd = graph.add( hnd ).getPersistent();
					obj[ ( indext[ cnt ] ) * ( xmult * ymult * zmult ) + ( indexx[ cnt ] ) * ( ymult * zmult ) + ( indexy[ cnt ] ) * ( zmult ) + ( indexz[ cnt ] ) ] = hndd;
					graph.update( obj );
				
					cur = hndd;
				}
			}
			else
			{
				cur = acur;
			}
			graph.getTransactionManager().commit();
			//graph.getCache().close();
			MemoryClearingSystem.handleCheckClear( graph );
		}
		
		
		graph.getTransactionManager().beginTransaction();
		double[] obj = graph.get( cur );
		graph.getTransactionManager().commit();
		
		obj[ ( indext[ dsz - 1 ] ) * ( xmult * ymult * zmult ) + ( indexx[ dsz - 1 ] ) * ( ymult * zmult ) + ( indexy[ dsz - 1 ] ) * ( zmult ) + ( indexz[ dsz - 1 ] ) ] = val;
		
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
	 * @param _xmult The size of the each cell along the X-axis.
	 * @param _ymult The size of the each cell along the Y-axis.
	 * @param _zmult The size of the each cell along the Z-axis.
	 * @param _tmax The size of the array along the T-axis.
	 * @param _xmax The size of the array along the X-axis.
	 * @param _ymax The size of the array along the Y-axis.
	 * @param _zmax The size of the array along the Z-axis.
	 * @return The required number of traversal levels.
	 */
	protected int calcDsz( int _tmult , int _xmult , int _ymult , int _zmult ,
			int _tmax , int _xmax , int _ymax , int _zmax )
	{
		int dsz = 0;
		while( ( _tmax > _tmult ) || ( _xmax > _xmult ) || ( _ymax > _ymult ) || ( _zmax > _zmult ) )
		{
			_tmax = _tmax / _tmult;
			_xmax = _xmax / _xmult;
			_ymax = _ymax / _ymult;
			_zmax = _zmax / _zmult;
			dsz++;
		}
		return( dsz + 1 );
	}
	
	

}

