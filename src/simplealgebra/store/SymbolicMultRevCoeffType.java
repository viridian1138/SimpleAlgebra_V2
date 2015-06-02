



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

import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.SymbolicMultRevCoeff;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;



/**
 * HyperGraph type for storing SymbolicMultRevCoeff instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the matrix.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicMultRevCoeffType<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public SymbolicMultRevCoeffType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elemA = graph.get( layout[ 0 ] );
		SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elemB = graph.get( layout[ 1 ] );
		SquareMatrixElemFactory<U,R,S> fac = graph.get( layout[ 2 ] );
		if( elemA == null )
			throw( new RuntimeException( "Failed" ) );
		if( elemB == null )
			throw( new RuntimeException( "Failed" ) );
		if( fac == null )
			throw( new RuntimeException( "Failed" ) );
		return( new SymbolicMultRevCoeff<U,R,S>( elemA , elemB , fac ) );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		SymbolicMultRevCoeff<U,R,S> oid = (SymbolicMultRevCoeff<U,R,S>)( instance );
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
		SymbolicMultRevCoeffType<?,?,?> type = new SymbolicMultRevCoeffType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , SymbolicMultRevCoeff.class );
	}


	
	

}

