





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
import java.util.ArrayList;

import org.hypergraphdb.HyperGraph;


/**
 * DB entity resembling a sparse 4-D array.  For performance reasons it is assumed
 * that there will only be a single write to each index location.
 * 
 * @author thorngreen
 *
 * @param <T> The enclosed type.
 */
public class DbArray4D_SingleWrite<T extends Object> extends BaseDbArray_SingleWrite<T> {
	
	
	/**
	 * Constructs the array.
	 * 
	 * @param _graph The graph on which to perform DB operations.
	 */
	public DbArray4D_SingleWrite( final HyperGraph _graph )
	{
		super( _graph );
	}
	
	
	/**
	 * Generates the 4-D index for the array.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param y The "Y" index of the array.
	 * @param z The "Z" index of the array.
	 * @return The 4-D index for the array.
	 */
	protected ArrayList<BigInteger> gen( BigInteger t , BigInteger x , BigInteger y , BigInteger z )
	{
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( t );
		ret.add( x );
		ret.add( y );
		ret.add( z );
		return( ret );
	}
	
	
	/**
	 * Gets the object at the 4-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param y The "Y" index of the array.
	 * @param z The "Z" index of the array.
	 * @return The object at the 4-D index, or null if no object exists.
	 */
	public T get( BigInteger t , BigInteger x , BigInteger y , BigInteger z )
	{
		return( query( gen( t , x , y , z ) ) );
	}
	
	
	/**
	 * Sets the object at the 4-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param y The "Y" index of the array.
	 * @param z The "Z" index of the array.
	 * @param val The object to be set at the index.
	 */
	public void set( BigInteger t , BigInteger x , BigInteger y , BigInteger z , T val )
	{
		insert( gen( t , x , y , z ) , val );
	}
	
	
	/**
	 * Gets the object at the 4-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param y The "Y" index of the array.
	 * @param z The "Z" index of the array.
	 * @return The object at the 4-D index, or null if no object exists.
	 */
	public T get( int t , int x , int y , int z )
	{
		return( get( BigInteger.valueOf( t ) , BigInteger.valueOf( x ) , BigInteger.valueOf( y ) , BigInteger.valueOf( z ) ) );
	}
	
	
	/**
	 * Sets the object at the 4-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param y The "Y" index of the array.
	 * @param z The "Z" index of the array.
	 * @param val The object to be set at the index.
	 */
	public void set( int t , int x , int y , int z , T val )
	{
		set( BigInteger.valueOf( t ) , BigInteger.valueOf( x ) , BigInteger.valueOf( y ) , BigInteger.valueOf( z ) , val );
	}
	

	
}


