




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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;


/**
 * Simple class to write files in ".raw" format.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public abstract class RawFileWriter
{

	/**
	 * Constructs the writer.
	 */
	public RawFileWriter()
	{
		// Does Nothing.
	}
	
	
	/**
	 * Gets the buffer size for the output stream.
	 * @return The buffer size for the output stream.
	 */
	protected abstract int bufferSize();
	
	
	/**
	 * Gets the value at a particular (t, x, y, z).
	 * @param t The T-Axis parameter.
	 * @param x The X-Axis parameter.
	 * @param y The Y-Axis parameter.
	 * @param z The Z-Axis parameter.
	 * @return The value at the (t, x, y, z).
	 * @throws Throwable
	 */
	protected abstract double getVal( int t , int x , int y , int z ) throws Throwable;
	
	
	/**
	 * Gets the T-Axis start.
	 * @return The T-Axis start.
	 */
	protected abstract int getTStrt();
	
	/**
	 * Gets the T-Axis end.
	 * @return The T-Axis end.
	 */
	protected abstract int getTEnd();
	
	
	/**
	 * Gets the Z-Axis start.
	 * @return The Z-Axis start.
	 */
	protected abstract int getZStrt();
	
	/**
	 * Gets the Z-Axis end.
	 * @return The Z-Axis end.
	 */
	protected abstract int getZEnd();
	
	
	/**
	 * Gets the Y-Axis start.
	 * @return The Y-Axis start.
	 */
	protected abstract int getYStrt();
	
	/**
	 * Gets the Y-Axis end.
	 * @return The Y-Axis end.
	 */
	protected abstract int getYEnd();
	
	
	/**
	 * Gets the X-Axis start.
	 * @return The X-Axis start.
	 */
	protected abstract int getXStrt();
	
	/**
	 * Gets the X-Axis end.
	 * @return The X-Axis end.
	 */
	protected abstract int getXEnd();
	
	
	
	
	/**
	 * Writes double data to a file.
	 * 
	 * @param pathName The path to the file.
	 * @throws Throwable
	 */
	public void writeDouble( String pathName ) throws Throwable
	{
		final int T_STRT = getTStrt();
		final int T_END = getTEnd();
		
		final int Z_STRT = getZStrt();
		final int Z_END = getZEnd();
		
		final int Y_STRT = getYStrt();
		final int Y_END = getYEnd();
		
		final int X_STRT = getXStrt();
		final int X_END = getXEnd();
		
		
		FileOutputStream fo = new FileOutputStream( pathName );
		BufferedOutputStream bo = new BufferedOutputStream( fo , bufferSize() );
		DataOutputStream ds = new DataOutputStream( bo );
		
		for( int t = T_STRT ; t < T_END ; t++ )
		{
			for( int z = Z_STRT ; z < Z_END ; z++ )
			{
				for( int y = Y_STRT ; y < Y_END ; y++ )
				{
					for( int x = X_STRT ; x < X_END ; x++ )
					{
						ds.writeDouble( getVal( t , x , y , z ) );
					}
				}
			}
		}
		
		ds.close();
		
	}
	
	
	
	/**
	 * Writes float data to a file.
	 * 
	 * @param pathName The path to the file.
	 * @throws Throwable
	 */
	public void writeFloat( String pathName ) throws Throwable
	{
		final int T_STRT = getTStrt();
		final int T_END = getTEnd();
		
		final int Z_STRT = getZStrt();
		final int Z_END = getZEnd();
		
		final int Y_STRT = getYStrt();
		final int Y_END = getYEnd();
		
		final int X_STRT = getXStrt();
		final int X_END = getXEnd();
		
		
		FileOutputStream fo = new FileOutputStream( pathName );
		BufferedOutputStream bo = new BufferedOutputStream( fo , bufferSize() );
		DataOutputStream ds = new DataOutputStream( bo );
		
		for( int t = T_STRT ; t < T_END ; t++ )
		{
			for( int z = Z_STRT ; z < Z_END ; z++ )
			{
				for( int y = Y_STRT ; y < Y_END ; y++ )
				{
					for( int x = X_STRT ; x < X_END ; x++ )
					{
						ds.writeFloat( (float)( getVal( t , x , y , z ) ) );
					}
				}
			}
		}
		
		ds.close();
		
	}
	
	
	
	/**
	 * Calculates the maximum value in the dataset.
	 * 
	 * @throws Throwable
	 */
	public double calcMax( ) throws Throwable
	{
		final int T_STRT = getTStrt();
		final int T_END = getTEnd();
		
		final int Z_STRT = getZStrt();
		final int Z_END = getZEnd();
		
		final int Y_STRT = getYStrt();
		final int Y_END = getYEnd();
		
		final int X_STRT = getXStrt();
		final int X_END = getXEnd();
		
		
		double dd = getVal( T_STRT , X_STRT , Y_STRT , Z_STRT );
		
		
		for( int t = T_STRT ; t < T_END ; t++ )
		{
			for( int z = Z_STRT ; z < Z_END ; z++ )
			{
				for( int y = Y_STRT ; y < Y_END ; y++ )
				{
					for( int x = X_STRT ; x < X_END ; x++ )
					{
						dd = Math.max( dd , getVal( t , x , y , z ) );
					}
				}
			}
		}
		
		return( dd );
	}
	
	
	
	/**
	 * Calculates the maximum absolute value in the dataset.
	 * 
	 * @throws Throwable
	 */
	public double calcMaxAbs( ) throws Throwable
	{
		final int T_STRT = getTStrt();
		final int T_END = getTEnd();
		
		final int Z_STRT = getZStrt();
		final int Z_END = getZEnd();
		
		final int Y_STRT = getYStrt();
		final int Y_END = getYEnd();
		
		final int X_STRT = getXStrt();
		final int X_END = getXEnd();
		
		
		double dd = Math.abs( getVal( T_STRT , X_STRT , Y_STRT , Z_STRT ) );
		
		
		for( int t = T_STRT ; t < T_END ; t++ )
		{
			for( int z = Z_STRT ; z < Z_END ; z++ )
			{
				for( int y = Y_STRT ; y < Y_END ; y++ )
				{
					for( int x = X_STRT ; x < X_END ; x++ )
					{
						dd = Math.max( dd , Math.abs( getVal( t , x , y , z ) ) );
					}
				}
			}
		}
		
		return( dd );
	}
	
	
	
	
	
}



