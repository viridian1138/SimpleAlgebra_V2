



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




package simplealgebra.symbolic.db;



import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.MutableElem;
import simplealgebra.Mutator;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicMutable;



/**
 * HyperGraph type for storing SymbolicMutable instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The enclosed type inside the enclosed mutable elem.
 * @param <U> The enclosed mutable elem.
 * @param <R> The factory for the enclosed mutable elem.
 */
public class SymbolicMutableType<T extends Elem<T,?>, U extends MutableElem<T,U,?>, R extends ElemFactory<U,R>, M extends Mutator<U> > extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public SymbolicMutableType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		SymbolicElem<U,R> elemA = graph.get( layout[ 0 ] );
		M elemB = graph.get( layout[ 1 ] );
		R fac = graph.get( layout[ 2 ] );
		if( elemA == null )
			throw( new RuntimeException( "Failed" ) );
		if( elemB == null )
			throw( new RuntimeException( "Failed" ) );
		if( fac == null )
			throw( new RuntimeException( "Failed" ) );
		return( new SymbolicMutable<T,U,R,M>( elemA , elemB , fac ) );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		SymbolicMutable<T,U,R,M> oid = (SymbolicMutable<T,U,R,M>)( instance );
		HGHandle elemAHandle = hg.assertAtom(graph, oid.getElemA() );
		HGHandle elemBHandle = hg.assertAtom(graph, oid.getElemB() );
		HGHandle facHandle = hg.assertAtom(graph, oid.getFac().getFac() );
		HGPersistentHandle[] hn = { elemAHandle.getPersistent() , 
			elemBHandle.getPersistent() , facHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		SymbolicMutableType<?,?,?,?> type = new SymbolicMutableType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , SymbolicMutable.class );
	}


	
	

}

