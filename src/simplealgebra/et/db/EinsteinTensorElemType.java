



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



import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;



/**
 * HyperGraph type for storing EinsteinTensorElem instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class EinsteinTensorElemType<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public EinsteinTensorElemType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		ArrayList<Z> contravar = graph.get( layout[ 0 ] );
		ArrayList<Z> covar = graph.get( layout[ 1 ] );
		S fac = graph.get( layout[ 2 ] );
		Map<ArrayList<BigInteger>,R> map = graph.get( layout[ 3 ] );
		if( contravar == null )
			throw( new RuntimeException( "Failed" ) );
		if( covar == null )
			throw( new RuntimeException( "Failed" ) );
		if( fac == null )
			throw( new RuntimeException( "Failed" ) );
		if( map == null )
			throw( new RuntimeException( "Failed" ) );
		final EinsteinTensorElem<Z,R,S> et = new EinsteinTensorElem<Z,R,S>( fac , contravar , covar );
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			R val = map.get( key );
			et.setVal( key , val );
		}
		return( et );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		EinsteinTensorElem<Z,R,S> oid = (EinsteinTensorElem<Z,R,S>)( instance );
		HGHandle contravarHandle = graph.add( oid.getContravariantIndices() ); // hg.assertAtom(graph, oid.getContravariantIndices() ); !!!!!!!!!!!!!!!!!!!!!
		HGHandle covarHandle = graph.add( oid.getCovariantIndices() ); // hg.assertAtom(graph, oid.getCovariantIndices() ); !!!!!!!!!!!!!!!!!!!!!!!!!!
		HGHandle facHandle = hg.assertAtom(graph, oid.getFac() );
		final HashMap<ArrayList<BigInteger>,R> map = new HashMap<ArrayList<BigInteger>,R>();
		Iterator<ArrayList<BigInteger>> it = oid.getKeyIterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			R val = oid.getVal( key );
			map.put( key , val );
		}
		HGHandle mapHandle = hg.assertAtom(graph, map );
		HGPersistentHandle[] hn = { contravarHandle.getPersistent() , covarHandle.getPersistent() , 
				facHandle.getPersistent() , mapHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		EinsteinTensorElemType<?,?,?> type = new EinsteinTensorElemType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , EinsteinTensorElem.class );
	}


	
	

}

