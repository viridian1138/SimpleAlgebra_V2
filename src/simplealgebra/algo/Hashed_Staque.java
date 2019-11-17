







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









package simplealgebra.algo;

import java.math.BigInteger;
import java.util.HashMap;


/**
 * Collections-based structure that can act as either a stack or a queue.
 * @author tgreen
 *
 * @param <R> The dayatype stored in the structure.
 */
public class Hashed_Staque<R> {
	
	/**
	 * Underlying storage map.
	 */
	protected final HashMap<BigInteger,R> hm = new HashMap<BigInteger,R>();
	
	/**
	 * The front index of the stack/queue.
	 */
	protected BigInteger front = BigInteger.ZERO;
	
	/**
	 * The rear index of the stack/queue.
	 */
	protected BigInteger rear = BigInteger.ZERO;
	
	
	/**
	 * Whether the structure is empry.
	 * @return Returns true iff. the structure is empty.
	 */
	public boolean empty()
	{
		return( front.compareTo( rear ) <= 0 );
	}
	
	
	/**
	 * Pushes an object onto the stack.
	 * @param in The object to be pushed onto the stack.
	 */
	public void push( R in )
	{
		front = front.add( BigInteger.ONE );
		hm.put( front , in );
	}
	
	
	/**
	 * Puts an object onto the queue.
	 * @param in The object to be added to the queue.
	 */
	public void enq( R in )
	{
		front = front.add( BigInteger.ONE );
		hm.put( front , in );
	}
	
	
	/**
	 * De-queues an object from the queue.
	 * @return The object that was de-queued.
	 */
	public R deq( )
	{
		rear = rear.add( BigInteger.ONE );
		final R ret =  hm.get( rear );
		hm.remove( rear );
		return( ret );
	}
	
	
	/**
	 * Pops an object from the stack.
	 * @return The object that was popped.
	 */
	public R pop( )
	{
		final R ret =  hm.get( front );
		hm.remove( front );
		front = front.subtract( BigInteger.ONE );
		return( ret );
	}
	
	
	/**
	 * Returns the object at the front of the queue.
	 * @return The object at the front of the queue.
	 */
	public R peekFront()
	{
		return( hm.get( front ) );
	}
	
	
	/**
	 * Returns the object at the top of the stack.
	 * @return The object at the top of the stack.
	 */
	public R peekTop()
	{
		return( hm.get( front ) );
	}
	
	
	/**
	 * Returns the object at the end of the queue.
	 * @return The object at the end of the queue.
	 */
	public R peekEnd()
	{
		return( hm.get( rear.add( BigInteger.ONE ) ) );
	}
	
	
	/**
	 * Returns the object at the bottom of the stack.
	 * @return The object at the bottom of the stack.
	 */
	public R peekBottom()
	{
		return( hm.get( rear.add( BigInteger.ONE ) ) );
	}
	
	
	
	

}


