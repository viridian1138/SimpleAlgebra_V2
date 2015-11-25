



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




package simplealgebra.ga.db;



import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.Ord;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;



/**
 * HyperGraph type for storing GeometricAlgebraMultivectorElem instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 * @param <U> The number of dimensions in the algebra.
 * @param <A> The ord of the algebra.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class GeometricAlgebraMultivectorElemType<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public GeometricAlgebraMultivectorElemType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		S fac = graph.get( layout[ 0 ] );
		U dim = graph.get( layout[ 1 ] );
		A ord = graph.get( layout[ 2 ] );
		Map<HashSet<BigInteger>,R> map = graph.get( layout[ 3 ] );
		if( fac == null )
			throw( new RuntimeException( "Failed" ) );
		if( dim == null )
			throw( new RuntimeException( "Failed" ) );
		if( ord == null )
			throw( new RuntimeException( "Failed" ) );
		if( map == null )
			throw( new RuntimeException( "Failed" ) );
		final GeometricAlgebraMultivectorElem<U,A,R,S> ga = new GeometricAlgebraMultivectorElem<U,A,R,S>( fac , dim , ord );
		for( final Entry<HashSet<BigInteger>, R> ii : map.entrySet() )
		{
			HashSet<BigInteger> key = ii.getKey();
			R val = ii.getValue();
			ga.setVal( key , val );
		}
		return( ga );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		GeometricAlgebraMultivectorElem<U,A,R,S> oid = (GeometricAlgebraMultivectorElem<U,A,R,S>)( instance );
		HGHandle facHandle = hg.assertAtom(graph, oid.getFac().getFac() );
		HGHandle dimHandle = hg.assertAtom(graph, oid.getFac().getDim() );
		HGHandle ordHandle = hg.assertAtom(graph, oid.getFac().getOrd() );
		final HashMap<HashSet<BigInteger>,R> map = new HashMap<HashSet<BigInteger>,R>();
		for( final Entry<HashSet<BigInteger>, R> ii : oid.getEntrySet() )
		{
			HashSet<BigInteger> key = ii.getKey();
			R val = ii.getValue();
			map.put( key , val );
		}
		HGHandle mapHandle = hg.assertAtom(graph, map );
		HGPersistentHandle[] hn = { facHandle.getPersistent() , dimHandle.getPersistent() , 
				ordHandle.getPersistent() , mapHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		GeometricAlgebraMultivectorElemType<?,?,?,?> type = new GeometricAlgebraMultivectorElemType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , GeometricAlgebraMultivectorElem.class );
	}


	
	

}

