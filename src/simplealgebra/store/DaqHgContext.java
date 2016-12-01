





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
 * Stores the context for a DaqHg (Drools As a Query language for HyperGraphDB) query.  Uses Drools ( <A href="http://drools.org">http://drools.org</A> ) and HyperGraph ( <A href="http://hypergraphdb.org">http://hypergraphdb.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The type of the objects in the result set for the query.
 */
public class DaqHgContext<T extends Object> 
{

	/**
	 * The result handler for the query.
	 */
	protected DaqHgResultHandler<T> resultHandler;
	
	/**
	 * The default primitive query.
	 */
	protected QueryIterable<?> defaultPrimitiveQuery;

	
	/**
	 * Gets the result handle for the query.
	 * 
	 * @return The result handle for the query.
	 */
	public DaqHgResultHandler<T> getResultHandler() {
		return resultHandler;
	}

	/**
	 * Sets the result handler for the query.
	 * 
	 * @param resultHandler The result handle for the query.
	 */
	public void setResultHandler(DaqHgResultHandler<T> resultHandler) {
		this.resultHandler = resultHandler;
	}

	/**
	 * Gets the default primitive query.
	 * 
	 * @return The default primitive query.
	 */
	public QueryIterable<?> getDefaultPrimitiveQuery() {
		return defaultPrimitiveQuery;
	}

	/**
	 * Stores the default primitive query.
	 * 
	 * @param defaultPrimitiveQuery The default primitive query.
	 */
	public void setDefaultPrimitiveQuery(QueryIterable<?> defaultPrimitiveQuery) {
		this.defaultPrimitiveQuery = defaultPrimitiveQuery;
	}

	
	
}



