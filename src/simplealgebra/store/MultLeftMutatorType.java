



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
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.Elem;
import simplealgebra.MultLeftMutator;



/**
 * HyperGraph type for storing MultLeftMutator instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the matrix.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class MultLeftMutatorType<T extends Elem<T,?>> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public MultLeftMutatorType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		T elem = graph.get( layout[ 0 ] );
		String name = graph.get( layout[ 1 ] );
		if( elem == null )
			throw( new RuntimeException( "Failed" ) );
		if( name == null )
			throw( new RuntimeException( "Failed" ) );
		return( new MultLeftMutator<T>( elem , name ) );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		MultLeftMutator<T> oid = (MultLeftMutator<T>)( instance );
		HGHandle elemAHandle = hg.assertAtom(graph, oid.getElem() );
		HGHandle elemBHandle = graph.add( oid.getName() ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		HGPersistentHandle[] hn = { elemAHandle.getPersistent() , 
			elemBHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		MultLeftMutatorType<?> type = new MultLeftMutatorType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , MultLeftMutator.class );
	}


	
	

}

