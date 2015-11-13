



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



import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.SquareMatrixElem;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;



/**
 * HyperGraph type for storing SquareMatrixElem instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class SquareMatrixElemType<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public SquareMatrixElemType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		S fac = graph.get( layout[ 0 ] );
		U dim = graph.get( layout[ 1 ] );
		Map<ArrayList<BigInteger>,R> map = graph.get( layout[ 2 ] );
		if( fac == null )
			throw( new RuntimeException( "Failed" ) );
		if( dim == null )
			throw( new RuntimeException( "Failed" ) );
		if( map == null )
			throw( new RuntimeException( "Failed" ) );
		final SquareMatrixElem<U,R,S> mat = new SquareMatrixElem<U,R,S>( fac , dim );
		for( final Map.Entry<ArrayList<BigInteger>, R> ite : map.entrySet() )
		{
			ArrayList<BigInteger> key = ite.getKey();
			R val = ite.getValue();
			final BigInteger row = key.get( 0 );
			final BigInteger col = key.get( 1 );
			mat.setVal( row , col , val );
		}
		return( mat );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		SquareMatrixElem<U,R,S> oid = (SquareMatrixElem<U,R,S>)( instance );
		HGHandle facHandle = hg.assertAtom(graph, oid.getFac().getFac() );
		HGHandle dimHandle = hg.assertAtom(graph, oid.getFac().getDim() );
		final HashMap<ArrayList<BigInteger>,R> map = new HashMap<ArrayList<BigInteger>,R>();
		final ArrayList<String> contravarIndices = new ArrayList<String>();
		final ArrayList<String> covarIndices = new ArrayList<String>();
		covarIndices.add( "u" );
		covarIndices.add( "v" );
		EinsteinTensorElem<String,R,S> oid2 = new EinsteinTensorElem<String,R,S>(	
				oid.getFac().getFac() , contravarIndices, covarIndices );
		oid.toRankTwoTensor( oid2 );
		Iterator<ArrayList<BigInteger>> it = oid2.getKeyIterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			R val = oid2.getVal( key );
			map.put( key , val );
		}
		HGHandle mapHandle = hg.assertAtom(graph, map );
		HGPersistentHandle[] hn = { facHandle.getPersistent() , dimHandle.getPersistent() , 
				mapHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		SquareMatrixElemType<?,?,?> type = new SquareMatrixElemType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , SquareMatrixElem.class );
	}


	
	

}

