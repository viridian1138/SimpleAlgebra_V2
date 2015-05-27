



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



import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomType;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.DoubleElem;



/**
 * HyperGraph type for storing DoubleElem instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class DoubleElemType extends HGAtomTypeBase {

	/**
	 * Constructs the type instance.
	 */
	public DoubleElemType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGAtomType type = graph.getTypeSystem().getAtomType( Double.class );
		return( new DoubleElem( (Double)( type.make(handle, null, null) ) ) );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		HGAtomType type = graph.getTypeSystem().getAtomType( Double.class );
		type.release( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		HGAtomType type = graph.getTypeSystem().getAtomType( Double.class );
		return( type.store( ( (DoubleElem) instance ).getVal() ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		DoubleElemType type = new DoubleElemType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , DoubleElem.class );
	}
	

}

