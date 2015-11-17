




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





package simplealgebra.stelem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * An elem defining a partial derivative that is evaluated over a discretized space such as the discretized
 * space in the Euler Method.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <K> The type of the implicit space terms that are to be mapped into discretized coordinates.
 */
public abstract class Stelem<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public Stelem( SymbolicElemFactory<R,S> _fac )
	{
		super( _fac );
	}
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public Stelem( SymbolicElemFactory<R,S> _fac , DroolsSession ds )
	{
		this( _fac );
		ds.insert( this );
	}
	
	@Override
	public SymbolicElem<R,S> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
		Stelem<R,S,K> rm = this.cloneInstance();
		rm.applyPartialDerivative( (ArrayList<K>) withRespectTo );
		return( rm.eval( implicitSpace ) );
	}
	
	@Override
	public SymbolicElem<R, S> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElem<R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		Stelem<R,S,K> rm = this.cloneInstance();
		rm.applyPartialDerivative( (ArrayList<K>) withRespectTo );
		return( rm.evalCached( implicitSpace , cache ) );
	}
	
	
	/**
	 * Copies the Stelem for threading.
	 * @param in The Stelem to be copied.
	 * @param threadIndex The thread index.
	 */
	protected Stelem( final Stelem<R,S,K> in , final BigInteger threadIndex )
	{
		super( in.getFac().getFac().cloneThread(threadIndex) );
		for( final Entry<K,BigInteger> ii : in.partialMap.entrySet() )
		{
			final K ikey = ii.getKey();
			final BigInteger ival = ii.getValue();
			partialMap.put( (K)( ikey.cloneThread(threadIndex) ) , ival );
		}
	}
	
	
	/**
	 * Copies the Stelem for threading.
	 * @param in The Stelem to be copied.
	 * @param threadIndex The thread index.
	 * @param cache The elem cache.
	 */
	protected Stelem( final Stelem<R,S,K> in , final BigInteger threadIndex , CloneThreadCache<SymbolicElem<R, S>, SymbolicElemFactory<R, S>> cache )
	{
		super( in.getFac().getFac().cloneThreadCached(threadIndex, cache) );
		for( final Entry<K,BigInteger> ii : in.partialMap.entrySet() )
		{
			final K ikey = ii.getKey();
			final BigInteger ival = ii.getValue();
			partialMap.put( (K)( ikey.cloneThread(threadIndex) ) , ival );
		}
	}
	
	
	/**
	 * Returns the result of adding the partial derivatives in the parameter.
	 * 
	 * @param withRespectTo The partial derivatives to be added.
	 * @return An instance of the Stelem with the partial derivatives added.
	 * @throws NotInvertibleException
	 */
	public Stelem<R,S,K> simplifyPartialDerivative( ArrayList<K> withRespectTo ) throws NotInvertibleException
	{
		Stelem<R,S,K> rm = this.cloneInstance();
		rm.applyPartialDerivative( withRespectTo );
		return( rm );
	}
	
	
	/**
	 * Clones the elem.
	 * 
	 * @return The clone of the elem.
	 */
	public abstract Stelem<R,S,K> cloneInstance();
	
	
	/**
	 * Applies a set of partial derivatives to the partialMap member.
	 * 
	 * @param withRespectTo The set of partial derivatives to apply.
	 * @throws NotInvertibleException
	 */
	protected void applyPartialDerivative(ArrayList<K> withRespectTo)
			throws NotInvertibleException {
		for( final K v : withRespectTo )
		{
			BigInteger vl = partialMap.get( v );
			if( vl == null )
			{
				vl = BigInteger.ONE;
			}
			else
			{
				vl = vl.add( BigInteger.ONE );
			}
			partialMap.put( v , vl );
		}
	}

	
	/**
	 * A map storing the number of partial derivatives for each implicit space term.
	 */
	protected HashMap<K,BigInteger> partialMap = new HashMap<K,BigInteger>();

	
}


