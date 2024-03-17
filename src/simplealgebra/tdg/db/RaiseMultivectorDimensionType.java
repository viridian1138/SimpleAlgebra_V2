



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




package simplealgebra.tdg.db;



import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.tdg.DimensionMismatchException;
import simplealgebra.tdg.RaiseMultivectorDimension;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;



/**
 * HyperGraph ( <A href="http://hypergraphdb.org">http://hypergraphdb.org</A> ) type for storing RaiseMultivectorDimension instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the algebra.
 * @param <A> The ord of the algebra.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <Ulower> The number of dimensions in the multivector to be raised.
 * @param <Alower> The Ord of the multivector to be raised.
 */
public class RaiseMultivectorDimensionType<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>, Ulower extends NumDimensions, Alower extends Ord<Ulower>> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public RaiseMultivectorDimensionType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		GeometricAlgebraMultivectorElemFactory<U,A, R, S> fac = graph.get( layout[ 0 ] );
		SymbolicElem<GeometricAlgebraMultivectorElem<Ulower,Alower,R,S>,GeometricAlgebraMultivectorElemFactory<Ulower,Alower,R,S>> lowerVect = graph.get( layout[ 1 ] );
		if( fac == null )
			throw( new RuntimeException( "Failed" ) );
		if( lowerVect == null )
			throw( new RuntimeException( "Failed" ) );
		try
		{
			return( new RaiseMultivectorDimension<U,A,R,S,Ulower,Alower>( fac , lowerVect ) );
		}
		catch(DimensionMismatchException e)
		{
			throw( new RuntimeException("Failed") );
		}
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		RaiseMultivectorDimension<U,A,R,S,Ulower,Alower> oid = (RaiseMultivectorDimension<U,A,R,S,Ulower,Alower>)( instance );
		HGHandle facHandle = hg.assertAtom(graph, oid.getFac().getFac() );
		HGHandle lowerVectHandle = hg.assertAtom(graph, oid.getLowerVect() );
		HGPersistentHandle[] hn = { facHandle.getPersistent() , 
			lowerVectHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		RaiseMultivectorDimensionType<?,?,?,?,?,?> type = new RaiseMultivectorDimensionType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , RaiseMultivectorDimension.class );
	}


	
	

}

