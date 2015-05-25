





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


/**
 * A stored DB payload for BaseDbArray or a subclass of BaseDbArray.  This class should only be used by BaseDbArray or subclasses of BaseDbArray.
 * 
 * @author thorngreen
 *
 * @param <T> The enclosed type.
 */
public class RelPayload<T extends Object> 
{

	/**
	 * Gets the object encapsulated by the payload.
	 * 
	 * @return The object encapsulated by the payload.
	 */
	public T getD() {
		return d;
	}

	
	/**
	 * Sets the object encapsulated by the payload.
	 * 
	 * @param d The object encapsulated by the payload.
	 */
	public void setD( T d ) {
		this.d = d;
	}

	
	/**
	 * The object encapsulated by the payload.
	 */
	private T d;
	
}

