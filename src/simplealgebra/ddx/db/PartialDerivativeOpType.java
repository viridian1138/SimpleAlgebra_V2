



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



import java.util.ArrayList;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.ddx.PartialDerivativeOp;



/**
 * HyperGraph type for storing PartialDerivativeOp instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class PartialDerivativeOpType<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public PartialDerivativeOpType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		S fac = graph.get( layout[ 0 ] );
		ArrayList<K> withRespectTo = graph.get( layout[ 1 ] );
		if( fac == null )
			throw( new RuntimeException( "Failed" ) );
		if( withRespectTo == null )
			throw( new RuntimeException( "Failed" ) );
		final PartialDerivativeOp<R,S,K> et = new PartialDerivativeOp<R,S,K>( fac , withRespectTo );
		return( et );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		PartialDerivativeOp<R,S,K> oid = (PartialDerivativeOp<R,S,K>)( instance );
		HGHandle facHandle = hg.assertAtom(graph, oid.getFac().getFac() );
		HGHandle wrtHandle = hg.assertAtom(graph, oid.getWithRespectTo() );
		HGPersistentHandle[] hn = { facHandle.getPersistent() , wrtHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		PartialDerivativeOpType<?,?,?> type = new PartialDerivativeOpType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , PartialDerivativeOp.class );
	}


	
	

}

