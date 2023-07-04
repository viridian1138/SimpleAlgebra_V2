



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




package simplealgebra.symbolic;

import org.kie.api.runtime.KieSession;



/**
 * A rule session container for a Drools ( <A href="http://drools.org">http://drools.org</A> ) session.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class DroolsSession {
	
	/**
	 * Constructs the session.
	 * 
	 * @param _elem The Drools stateful knowledge session.
	 */
	public DroolsSession( KieSession _elem )
	{
		elem = _elem;
	}
	
	/**
	 * Gets the Drools stateful knowledge sesison.
	 * 
	 * @return The Drools stateful knowledge session.
	 */
	public KieSession getElem() {
		return elem;
	}
	
	/**
	 * Inserts an object into the knowledge session.
	 * 
	 * @param obj The object to insert.
	 */
	public void insert( Object obj )
	{
		elem.insert( obj );
	}
	
	/**
	 * The Drools stateful knowledge session.
	 */
	private KieSession elem;

}



