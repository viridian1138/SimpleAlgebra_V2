



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




package simplealgebra.et.db;



import java.util.HashSet;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.SymbolicIndexReduction;
import simplealgebra.symbolic.SymbolicElem;



/**
 * HyperGraph type for storing SymbolicIndexReduction instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 * @param <Z> The type of the tensor indices.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 *
 */
public class SymbolicIndexReductionType<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public SymbolicIndexReductionType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> elem = graph.get( layout[ 0 ] );
		EinsteinTensorElemFactory<Z,R,S> fac = graph.get( layout[ 1 ] );
		HashSet<Z> contravariantReduce = graph.get( layout[ 2 ] );
		HashSet<Z> covariantReduce = graph.get( layout[ 2 ] );
		if( elem == null )
			throw( new RuntimeException( "Failed" ) );
		if( fac == null )
			throw( new RuntimeException( "Failed" ) );
		if( contravariantReduce == null )
			throw( new RuntimeException( "Failed" ) );
		if( covariantReduce == null )
			throw( new RuntimeException( "Failed" ) );
		final SymbolicIndexReduction<Z,R,S> et = new SymbolicIndexReduction<Z,R,S>( elem , fac , contravariantReduce , covariantReduce );
		return( et );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		SymbolicIndexReduction<Z,R,S> oid = (SymbolicIndexReduction<Z,R,S>)( instance );
		HGHandle elemHandle = hg.assertAtom(graph, oid.getElem() );
		HGHandle facHandle = hg.assertAtom(graph, oid.getFac().getFac() );
		HGHandle contravariantHandle = graph.add( oid.getContravariantReduce() ); // hg.assertAtom(graph, oid.getCovariantIndices() ); !!!!!!!!!!!!!!!!!!!!!!!!!!
		HGHandle covariantHandle = graph.add( oid.getCovariantReduce() ); // hg.assertAtom(graph, oid.getCovariantIndices() ); !!!!!!!!!!!!!!!!!!!!!!!!!!
		HGPersistentHandle[] hn = { elemHandle.getPersistent() , facHandle.getPersistent() , 
				contravariantHandle.getPersistent() , covariantHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		SymbolicIndexReductionType<?,?,?> type = new SymbolicIndexReductionType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , SymbolicIndexReduction.class );
	}


	
	

}

