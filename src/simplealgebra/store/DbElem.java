


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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.PrimitiveRandom;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.SymbolicElem.EVAL_MODE;


/**
 * DB wrapper for an Elem.  Elem contents are stored offline in the DB.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class DbElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> 
	extends Elem<DbElem<R,S>,DbElemFactory<R,S>>
	{
	


	@Override
	public DbElem<R, S> add(DbElem<R, S> b) {
		return( new DbElem<R,S>( query().add( b.query() ) , graph ) );
	}

	@Override
	public DbElem<R, S> mult(DbElem<R, S> b) {
		return( new DbElem<R,S>( query().mult( b.query() ) , graph ) );
	}

	@Override
	public DbElem<R, S> negate() {
		return( new DbElem<R,S>( query().negate( ) , graph ) );
	}

	@Override
	public DbElem<R, S> invertLeft() throws NotInvertibleException {
		return( new DbElem<R,S>( query().invertLeft( ) , graph ) );
	}
	
	@Override
	public DbElem<R, S> invertRight() throws NotInvertibleException {
		return( new DbElem<R,S>( query().invertRight( ) , graph ) );
	}

	@Override
	public DbElem<R, S> divideBy(BigInteger val) {
		return( new DbElem<R,S>( query().divideBy( val ) , graph ) );
	}
	
	@Override
	public DbElem<R, S> random(PrimitiveRandom in) {
		return( new DbElem<R,S>( query().random(in) , graph ) );
	}
	
	
	
	
	@Override
	public DbElem<R, S> handleOptionalOp( Object id , ArrayList<DbElem<R, S>> args ) throws NotInvertibleException
	{
		final ArrayList<R> args2 = new ArrayList<R>();
		for( final DbElem<R, S> ii : args )
		{
			args2.add( ii.query() );
		}
		final R retA = query().handleOptionalOp( id , args2 );
		return( new DbElem<R,S>( retA , graph ) );
	}

	
	@Override
	public DbElemFactory<R, S> getFac() {
		return( new DbElemFactory<R,S>( fac , graph ) );
	}
	
	
	@Override 
	public Elem<?,?> totalMagnitude()
	{
		return( query().totalMagnitude() );
	}

	
	/**
	 * Constructs the elem.
	 * 
	 * @param _ob The real component of the elem.
	 * @param _graph The graph on which to perform DB operations.
	 */
	public DbElem( R _ob , HyperGraph _graph )
	{
		graph = _graph;
		hbase = graph.add( _ob );
		fac = (S)( _ob.getFac() );
	}
	
	
	/**
	 * Constructs the elem.
	 * 
	 * @param _hbase A reference to a stored elem of type R.
	 * @param _fac The factory for the elem.
	 * @param _graph The graph on which to perform DB operations.
	 */
	public DbElem( HGHandle _hbase , S _fac , HyperGraph _graph )
	{
		graph = _graph;
		hbase = _hbase;
		fac = _fac;
	}
	
	
	/**
	 * Queries the enclosed elem from the DB.
	 * 
	 * @return The enclosed elem.
	 */
	public R query()
	{	
		return( graph.get( hbase ) );
	}
	
	
	
	@Override
	public DbElem<R,S> cloneThread( final BigInteger threadIndex )
	{
		// The HyperGraph graph and the HGHandle hbase are assumed to be thread-safe.
		final S sfac = fac.cloneThread(threadIndex);
		if( fac != sfac )
		{
			return( new DbElem<R,S>( hbase , sfac , graph ) );
		}
		return( this );
	}
	
	
	@Override
	public DbElem<R,S> cloneThreadCached( final BigInteger threadIndex , final CloneThreadCache<DbElem<R,S>,DbElemFactory<R,S>> cache )
	{
		final DbElem<R,S> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		// The HyperGraph graph and the HGHandle hbase are assumed to be thread-safe.
		S sfac = fac.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( fac != sfac )
		{
			final DbElem<R,S> rtmp = new DbElem<R,S>( hbase , sfac , graph );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}
	
	
	
	@Override
	public boolean evalSymbolicZeroApprox( EVAL_MODE mode )
	{
		return( query().evalSymbolicZeroApprox(mode) );
	}
	
	
	
	@Override
	public boolean evalSymbolicIdentityApprox( EVAL_MODE mode )
	{
		return( query().evalSymbolicIdentityApprox(mode) );
	}
	
	
	
	@Override
	public String writeDesc( WriteElemCache<DbElem<R,S>,DbElemFactory<R,S>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			final String sta = fac.writeDesc( (WriteElemCache<R,S>)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			this.getFac().writeElemTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			this.getFac().writeElemTypeString( ps );
			ps.print( "( " );
			ps.print( hbase );
			ps.print( " , " );
			ps.print( sta );
			ps.print( " , " );
			ps.print( graph );
			ps.println( " );" );
		}
		return( st );
	}
	
	

	/**
	 * @return the graph
	 */
	public HyperGraph getGraph() {
		return graph;
	}

	/**
	 * @return the hbase
	 */
	public HGHandle getHbase() {
		return hbase;
	}
	
	
	
	/**
	 * The factory of the enclosed type.
	 */
	S fac;

	/**
	 * The graph on which to perform DB operations.
	 */
	protected HyperGraph graph;
	
	/**
	 * The stored DB base reference.
	 */
	protected HGHandle hbase = null;


}

