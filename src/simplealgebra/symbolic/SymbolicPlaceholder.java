




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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;

/**
 * A symbolic elem that serves as a placeholder for another elem of the enclosed type.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicPlaceholder<R extends Elem<R,?>, S extends ElemFactory<R,S>>
{

	/**
	 * Constructs the placeholder.
	 * 
	 * @param _elem The enclosed elem.
	 */
	public SymbolicPlaceholder( R _elem )
	{
		elem = _elem;
	}
	
	/**
	 * Constructs the placeholder for use in a Drools ( <A href="http://drools.org">http://drools.org</A> ) session.
	 * 
	 * @param _elem The enclosed elem.
	 * @param ds The Drools session.
	 */
	public SymbolicPlaceholder( R _elem , DroolsSession ds )
	{
		this( _elem );
		ds.insert( this );
	}
	
	
	
	/**
	 * Gets the enclosed elem.
	 * 
	 * @return The enclosed elem.
	 */
	public R getElem() {
		return elem;
	}
	
	
	/**
	 * Sets the enclosed elem.
	 * 
	 * @param _elem The enclosed elem.
	 */
	public void setElem( R _elem ) {
		elem  = _elem;
	}
	
	
	/**
	 * Inserts this elem into a Drools ( <A href="http://drools.org">http://drools.org</A> ) session.
	 * 
	 * @param session The session in which to insert the elem.
	 */
	public void performInserts( KieSession session )
	{
		if(elem != null ) elem.performInserts( session );
		session.insert( this );
	}


	/**
	 * The enclosed elem.
	 */
	private R elem;

}

