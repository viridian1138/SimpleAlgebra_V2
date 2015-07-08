



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
	
	
	
	protected boolean altArrs = false;
	
	
	
	protected int[] indextA;
	
	protected int[] indextB;
	
	protected int[] indexxA;
	
	protected int[] indexxB;
	
	protected int[] indexyA;
	
	protected int[] indexyB;
	
	protected int[] indexzA;
	
	protected int[] indexzB;
	
	protected HGHandle argCache[];
	
	
	
	
	
	/**
	 * Constructs the array.
	 * 
	 * @param _param The input parameter.
	 */
	public DbFastArray4D_Dbl( final DbFastArray4D_Param _param )
	{
		graph = _param.getGraph();
		tmult = _param.getTmult();
		xmult = _param.getXmult();
		ymult = _param.getYmult();
		zmult = _param.getZmult();
		tmax = _param.getTmax();
		xmax = _param.getXmax();
		ymax = _param.getYmax();
		zmax = _param.getZmax();
		dsz = calcDsz( _param );
		
		Object hnd = null;
		
		if( dsz > 1 )
		{
			hnd = new HGHandle[ tmult * xmult * ymult * zmult ];
		}
		else
		{
			hnd = new double[ tmult * xmult * ymult * zmult ];
		}
		
		//graph.getTransactionManager().beginTransaction();
		
		hndl = graph.add( hnd ).getPersistent();
		
		//graph.getTransactionManager().commit();
		
		//graph.getCache().close();
		MemoryClearingSystem.handleCheckClear( graph );
		
		
		indextA = createDszIntArray();
		
		indextB = createDszIntArray();
		
		indexxA = createDszIntArray();
		
		indexxB = createDszIntArray();
		
		indexyA = createDszIntArray();
		
		indexyB = createDszIntArray();
		
		indexzA = createDszIntArray();
		
		indexzB = createDszIntArray();
		
		argCache = new HGHandle[ dsz ];
		
	}
	
	
	
	protected boolean indicesMatch( int index )
	{
		return( ( indextA[ index ] == indextB[ index ] ) && 
				( indexxA[ index ] == indexxB[ index ] ) && 
				( indexyA[ index ] == indexyB[ index ] ) && 
				( indexzA[ index ] == indexzB[ index ] ) );
	}
	
	
	
	protected int[] createDszIntArray()
	{
		int[] aa = new int[ dsz ];
		for( int cnt = 0 ; cnt < dsz ; cnt++ )
		{
			aa[ cnt ] = -1000000;
		}
		return( aa );
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
		
		altArrs = !altArrs;
		final int[] indext = altArrs ? indextA : indextB;
		final int[] indexx = altArrs ? indexxA : indexxB;
		final int[] indexy = altArrs ? indexyA : indexyB;
		final int[] indexz = altArrs ? indexzA : indexzB;
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
		boolean matchUp = true;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			if( matchUp && indicesMatch( cnt ) && ( argCache[ cnt ] != null ) )
			{
				cur = argCache[ cnt ];
			}
			else
			{
				matchUp = false;
				//graph.getTransactionManager().beginTransaction();
				HGHandle[] obj = graph.get( cur );
				//graph.getTransactionManager().commit();
				//graph.getCache().close();
				MemoryClearingSystem.handleCheckClear( graph );
				cur = obj[ ( indext[ cnt ] ) * ( xmult * ymult * zmult ) + ( indexx[ cnt ] ) * ( ymult * zmult ) + ( indexy[ cnt ] ) * ( zmult ) + indexz[ cnt ] ];
				argCache[ cnt ] = cur;
				if( cur == null )
				{
					oprev = null;
					return( 0.0 );
				}
			}
		}
		
		
		//graph.getTransactionManager().beginTransaction();
		double[] obj = graph.get( cur );
		//graph.getTransactionManager().commit();
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
		
		altArrs = !altArrs;
		final int[] indext = altArrs ? indextA : indextB;
		final int[] indexx = altArrs ? indexxA : indexxB;
		final int[] indexy = altArrs ? indexyA : indexyB;
		final int[] indexz = altArrs ? indexzA : indexzB;
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
		boolean matchUp = true;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			if( matchUp && indicesMatch( cnt ) && ( argCache[ cnt ] != null ) )
			{
				cur = argCache[ cnt ];
			}
			else
			{
				matchUp = false;
				//graph.getTransactionManager().beginTransaction();
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
				argCache[ cnt ] = cur;
				//graph.getTransactionManager().commit();
				//graph.getCache().close();
				MemoryClearingSystem.handleCheckClear( graph );
			}
		}
		
		
		//graph.getTransactionManager().beginTransaction();
		double[] obj = graph.get( cur );
		//graph.getTransactionManager().commit();
		
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
	 * @param _param The input parameter for the array constructor.
	 * @return The required number of traversal levels.
	 */
	protected int calcDsz( final DbFastArray4D_Param _param )
	{
		int dsz = 0;
		int _tmult = _param.getTmult();
		int _xmult = _param.getXmult();
		int _ymult = _param.getYmult();
		int _zmult = _param.getZmult();
		int _tmax = _param.getTmax();
		int _xmax = _param.getXmax();
		int _ymax = _param.getYmax();
		int _zmax = _param.getZmax();
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

