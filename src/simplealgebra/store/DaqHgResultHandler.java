





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
 * Abstract class for handling DaqHg (Drools As a Query language for HyperGraphDB) query results.  Uses Drools ( <A href="http://drools.org">http://drools.org</A> ) and HyperGraph ( <A href="http://hypergraphdb.org">http://hypergraphdb.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The type of the objects in the result set for the query.
 */
public abstract class DaqHgResultHandler<T extends Object> 
{

	/**
	 * Handles a result.
	 * 
	 * @param result The result to handle.
	 */
	public abstract void handle( T result );
	
	
}



