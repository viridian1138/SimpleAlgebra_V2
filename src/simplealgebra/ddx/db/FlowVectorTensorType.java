



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




package simplealgebra.ddx.db;



import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.ddx.FlowVectorFactory;
import simplealgebra.ddx.FlowVectorTensor;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;



/**
 * HyperGraph type for storing FlowVectorTensor instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions over which to express the flow vector.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class FlowVectorTensorType<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public FlowVectorTensorType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> fac = graph.get( layout[ 0 ] );
		Z index = graph.get( layout[ 1 ] );
		U dim = graph.get( layout[ 2 ] );
		FlowVectorFactory<R,S,K> dfac = graph.get( layout[ 3 ] );
		if( fac == null )
			throw( new RuntimeException( "Failed" ) );
		if( index == null )
			throw( new RuntimeException( "Failed" ) );
		if( dim == null )
			throw( new RuntimeException( "Failed" ) );
		if( dfac == null )
			throw( new RuntimeException( "Failed" ) );
		final FlowVectorTensor<Z,U,R,S,K> et = new FlowVectorTensor<Z,U,R,S,K>( fac , index , dim , dfac );
		return( et );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		FlowVectorTensor<Z,U,R,S,K> oid = (FlowVectorTensor<Z,U,R,S,K>)( instance );
		HGHandle facHandle = hg.assertAtom(graph, oid.getFac().getFac() );
		HGHandle indexHandle = graph.add( oid.getIndex() ); // hg.assertAtom(graph, oid.getDim() ); !!!!!!!!!!!!!!!!!!!!!!!!!
		HGHandle dimHandle = hg.assertAtom(graph, oid.getDim() );
		HGHandle dfacHandle = hg.assertAtom(graph, oid.getDfac() );
		HGPersistentHandle[] hn = { facHandle.getPersistent() , indexHandle.getPersistent() ,
				dimHandle.getPersistent() , dfacHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		FlowVectorTensorType<?,?,?,?,?> type = new FlowVectorTensorType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , FlowVectorTensor.class );
	}


	
	

}

