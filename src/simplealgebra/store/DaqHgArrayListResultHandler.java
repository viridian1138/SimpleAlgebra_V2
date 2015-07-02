





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

import java.util.ArrayList;




/**
 * Abstract class for handling DaqHg (Drools As a Query language for HyperGraphDB) query results.
 * 
 * @author thorngreen
 *
 * @param <T> The type of the objects in the result set for the query.
 */
public class DaqHgArrayListResultHandler<T extends Object> extends DaqHgResultHandler<T>
{

	/**
	 * The result set for the query.
	 */
	protected ArrayList<T> resultList;
	
	
	public ArrayList<T> getResultList() {
		return resultList;
	}


	public void setResultList(ArrayList<T> resultList) {
		this.resultList = resultList;
	}


	@Override
	public void handle( T result )
	{
		resultList.add( result );
	}
	
	
}



