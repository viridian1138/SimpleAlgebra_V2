



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


import java.io.RandomAccessFile;




/**
 * Direct access entity resembling a dense 3-D array (i.e voxel array) of doubles.  Basic layout of schema is inspired by image block virtual memory systems.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 */
public class DrFastArray3D_Dbl {
	
	/**
	 * The graph in which the array exists.
	 */
	RandomAccessFile file;
	
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
	 * The number of octree-like levels in the structure.
	 */
	int dsz;
	
	
	
	/**
	 * The size of a double-precision number in bytes.
	 */
	static final int SZ_DBL = 8;
	
	
	

	
	/**
	 * Boolean indicating whether the "A" or "B" array is in use.
	 */
	protected boolean altArrs = false;
	
	
	/**
	 * T-Axis indices for the "A" array.
	 */
	protected int[] indextA;
	
	/**
	 * T-Axis indices for the "B" array.
	 */
	protected int[] indextB;
	
	/**
	 * X-Axis indices for the "A" array.
	 */
	protected int[] indexxA;
	
	/**
	 * X-Axis indices for the "B" array.
	 */
	protected int[] indexxB;
	
	/**
	 * Y-Axis indices for the "A" array.
	 */
	protected int[] indexyA;
	
	/**
	 * Y-Axis indices for the "B" array.
	 */
	protected int[] indexyB;
	
	
	

	
	
	
	/**
	 * Constructs the array.
	 * 
	 * @param _param The input parameter.
	 * @param path The path at which to put the array image on disk.
	 */
	public DrFastArray3D_Dbl( final DbFastArray3D_Param _param , String path ) throws Throwable
	{
		file = new RandomAccessFile( path , "rw" );
		
		tmult = _param.getTmult();
		xmult = _param.getXmult();
		ymult = _param.getYmult();
		tmax = _param.getTmax();
		xmax = _param.getXmax();
		ymax = _param.getYmax();
		dsz = calcDsz( _param );
		
		
		int[] maxIndicest = createDszIntArray();
		
		int[] maxIndicesx = createDszIntArray();
		
		int[] maxIndicesy = createDszIntArray();
		
		
		
		int t = tmax;
		int x = xmax;
		int y = ymax;
		
		
		for( int cnt = 0 ; cnt < dsz ; cnt++ )
		{
			maxIndicest[ ( dsz - 1 ) - cnt ] = t % tmult; 
			maxIndicesx[ ( dsz - 1 ) - cnt ] = x % xmult; 
			maxIndicesy[ ( dsz - 1 ) - cnt ] = y % ymult;
			t = t / tmult;
			x = x / xmult;
			y = y / ymult;
		}
		
		
		long index = ( maxIndicest[ dsz - 1 ] ) * ( xmult * ymult ) + ( maxIndicesx[ dsz - 1 ] ) * ( ymult ) + maxIndicesy[ dsz - 1 ];
		
		final long num_cell = tmult * xmult * ymult;
		
		long num_mult = num_cell;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			long ind2 = ( maxIndicest[ ( dsz - 2 ) - cnt ] ) * ( xmult * ymult ) + ( maxIndicesx[ ( dsz - 2 ) - cnt ] ) * ( ymult ) + maxIndicesy[ ( dsz - 2 ) - cnt ];
			index += num_mult * ind2;
			
			num_mult *= num_cell;
		}
		
		
		
		final long desiredLength = SZ_DBL * ( index + 1 );
		System.out.println( desiredLength );
		file.setLength( desiredLength );
		
		
		indextA = createDszIntArray();
		
		indextB = createDszIntArray();
		
		indexxA = createDszIntArray();
		
		indexxB = createDszIntArray();
		
		indexyA = createDszIntArray();
		
		indexyB = createDszIntArray();
	
		
	}
	
	

	
	/**
	 * Allocates an int array of size dsz for use in a cache.
	 * 
	 * @return The allocated array.
	 */
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
	 * Gets the object at the 3-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param y The "Y" index of the array.
	 * @return The value at the 3-D index, or zero if no value exists.
	 */
	public double get( int t , int x , int y ) throws Throwable
	{	
		altArrs = !altArrs;
		final int[] indext = altArrs ? indextA : indextB;
		final int[] indexx = altArrs ? indexxA : indexxB;
		final int[] indexy = altArrs ? indexyA : indexyB;
		for( int cnt = 0 ; cnt < dsz ; cnt++ )
		{
			indext[ ( dsz - 1 ) - cnt ] = t % tmult; 
			indexx[ ( dsz - 1 ) - cnt ] = x % xmult; 
			indexy[ ( dsz - 1 ) - cnt ] = y % ymult;
			t = t / tmult;
			x = x / xmult;
			y = y / ymult;
		}
		
		
		long index = ( indext[ dsz - 1 ] ) * ( xmult * ymult ) + ( indexx[ dsz - 1 ] ) * ( ymult ) + indexy[ dsz - 1 ];
		
		final long num_cell = tmult * xmult * ymult;
		
		long num_mult = num_cell;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			long ind2 = ( indext[ ( dsz - 2 ) - cnt ] ) * ( xmult * ymult ) + ( indexx[ ( dsz - 2 ) - cnt ] ) * ( ymult ) + indexy[ ( dsz - 2 ) - cnt ];
			index += num_mult * ind2;
			
			num_mult *= num_cell;
		}
		
		
		file.seek( SZ_DBL * index );
		final double ret = file.readDouble();
		if( file.getFilePointer() != ( SZ_DBL * ( index + 1 ) ) )
		{
			throw( new RuntimeException( "Internal Error" ) );
		}
		return( ret );
	}
	
	
	/**
	 * Sets the value at the 3-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param y The "Y" index of the array.
	 * @param val The value to be set at the index.
	 */
	public void set( int t , int x , int y , double val ) throws Throwable
	{
		
		altArrs = !altArrs;
		final int[] indext = altArrs ? indextA : indextB;
		final int[] indexx = altArrs ? indexxA : indexxB;
		final int[] indexy = altArrs ? indexyA : indexyB;
		for( int cnt = 0 ; cnt < dsz ; cnt++ )
		{
			indext[ ( dsz - 1 ) - cnt ] = t % tmult; 
			indexx[ ( dsz - 1 ) - cnt ] = x % xmult; 
			indexy[ ( dsz - 1 ) - cnt ] = y % ymult;
			t = t / tmult;
			x = x / xmult;
			y = y / ymult;
		}
		
		
		long index = ( indext[ dsz - 1 ] ) * ( xmult * ymult ) + ( indexx[ dsz - 1 ] ) * ( ymult ) + indexy[ dsz - 1 ];
		
		final long num_cell = tmult * xmult * ymult;
		
		long num_mult = num_cell;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			long ind2 = ( indext[ ( dsz - 2 ) - cnt ] ) * ( xmult * ymult ) + ( indexx[ ( dsz - 2 ) - cnt ] ) * ( ymult ) + indexy[ ( dsz - 2 ) - cnt ];
			index += num_mult * ind2;
			
			num_mult *= num_cell;
		}
		
		
		file.seek( SZ_DBL * index );
		file.writeDouble( val );
		if( file.getFilePointer() != ( SZ_DBL * ( index + 1 ) ) )
		{
			throw( new RuntimeException( "Internal Error" ) );
		}
	}
	
	
	/**
	 * Closes the array.
	 */
	public void close() throws Throwable
	{
		file.close();
	}
	
	
	/**
	 * Flushes the contents of the previous array writes.
	 * 
	 * @throws Throwable
	 */
	public void flush( ) throws Throwable
	{
		file.getFD().sync();
	}
	
	
	/**
	 * Calculates the required number of block index levels.
	 * 
	 * @param _param The input parameter for the array constructor.
	 * @return The required number of block index levels.
	 */
	protected int calcDsz( final DbFastArray3D_Param _param )
	{
		int dsz = 0;
		int _tmult = _param.getTmult();
		int _xmult = _param.getXmult();
		int _ymult = _param.getYmult();
		int _tmax = _param.getTmax();
		int _xmax = _param.getXmax();
		int _ymax = _param.getYmax();
		while( ( _tmax >= _tmult ) || ( _xmax >= _xmult ) || ( _ymax >= _ymult ) )
		{
			_tmax = _tmax / _tmult;
			_xmax = _xmax / _xmult;
			_ymax = _ymax / _ymult;
			dsz++;
		}
		return( dsz + 1 );
	}
	
	

}

