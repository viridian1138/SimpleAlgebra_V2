






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






package simplealgebra.algo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor.Adim;
import simplealgebra.ga.*;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Remaps a multivector of functions to multivariate descent algorithm.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions for the multivector.
 * @param <A> The Ord for the multivector.
 * @param <R> The nested type.
 * @param <S> The factory for the nested type.
 */
public abstract class DescentAlgorithmMultiElemRemap<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	/**
	 * The dimensionality for descent algorithm.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class Adim extends NumDimensions
	{
		/**
		 * The number of dimensions.
		 */
		BigInteger dim;
		
		/**
		 * Constructs the dimension.
		 * 
		 * @param _dim The number of dimensions.
		 */
		public Adim( BigInteger _dim )
		{
			dim = _dim;
		}

		/**
		 * Gets the number of dimensions.
		 * @return The number of dimensions.
		 */
		@Override
		public BigInteger getVal() {
			return( dim );
		}
		
	};
	
	
	/**
	 * Encapsulates parameters used in genDescent() calls.
	 * 
	 * @author thorngreen
	 *
	 */
	protected class GenDescentParam
	{
		
		
		
		/**
		 * Gets the functions over which to evaluate the descent algorithm.
		 * 
		 * @return The functions over which to evaluate the descent algorithm.
		 */
		public GeometricAlgebraMultivectorElem<Adim, GeometricAlgebraOrd<Adim>, SymbolicElem<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElemFactory<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> getFunctions() {
			return functions;
		}

		/**
		 * Sets the functions over which to evaluate the descent algorithm.
		 * 
		 * @param functions The functions over which to evaluate the descent algorithm.
		 */
		public void setFunctions(
				GeometricAlgebraMultivectorElem<Adim, GeometricAlgebraOrd<Adim>, SymbolicElem<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElemFactory<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> functions) {
			this.functions = functions;
		}

		/**
		 * Gets the set of variables over which to take derivatives.
		 * 
		 * @return The set of variables over which to take derivatives.
		 */
		public ArrayList<ArrayList<? extends Elem<?, ?>>> getWithRespectTos() {
			return withRespectTos;
		}

		/**
		 * Sets the set of variables over which to take derivatives.
		 * 
		 * @param withRespectTos The set of variables over which to take derivatives.
		 */
		public void setWithRespectTos(
				ArrayList<ArrayList<? extends Elem<?, ?>>> withRespectTos) {
			this.withRespectTos = withRespectTos;
		}

		/**
		 * Gets the initial implicit space over which to take the functions and their derivatives.
		 * 
		 * @return The initial implicit space over which to take the functions and their derivatives.
		 */
		public HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> getImplicitSpaceFirstLevel() {
			return implicitSpaceFirstLevel;
		}

		/**
		 * Sets the initial implicit space over which to take the functions and their derivatives.
		 * 
		 * @param implicitSpaceFirstLevel The initial implicit space over which to take the functions and their derivatives.
		 */
		public void setImplicitSpaceFirstLevel(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel) {
			this.implicitSpaceFirstLevel = implicitSpaceFirstLevel;
		}

		/**
		 * Gets the factory for the enclosed type.
		 * 
		 * @return The factory for the enclosed type.
		 */
		public SymbolicElemFactory<R, S> getSfac() {
			return sfac;
		}

		/**
		 * Sets the factory for the enclosed type.
		 * 
		 * @param sfac The factory for the enclosed type.
		 */
		public void setSfac(SymbolicElemFactory<R, S> sfac) {
			this.sfac = sfac;
		}

		/**
		 * Gets the number of dimensions over which to evaluate the descent algorithm.
		 * 
		 * @return The number of dimensions over which to evaluate the descent algorithm.
		 */
		public Adim getDim() {
			return dim;
		}

		/**
		 * Sets the number of dimensions over which to evaluate the descent algorithm.
		 * 
		 * @param dim The number of dimensions over which to evaluate the descent algorithm.
		 */
		public void setDim(Adim dim) {
			this.dim = dim;
		}

		/**
		 * Gets the cache to be used for symbolic evals or null if there is no cache.
		 * 
		 * @return The cache to be used for symbolic evals or null if there is no cache.
		 */
		public HashMap<SCacheKey<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElem<R, S>> getCache() {
			return cache;
		}

		/**
		 * Sets the cache to be used for symbolic evals.
		 * 
		 * @param cache The cache to be used for symbolic evals.
		 */
		public void setCache(
				HashMap<SCacheKey<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElem<R, S>> cache) {
			this.cache = cache;
		}

		/**
		 * The functions over which to evaluate the descent algorithm.
		 */
		protected GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> functions;
		
		/**
		 * The set of variables over which to take derivatives.
		 */
		protected ArrayList<ArrayList<? extends Elem<?,?>>> withRespectTos;
		
		/**
		 * The initial implicit space over which to take the functions and their derivatives.
		 */
		protected HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel;
				
		/**
		 * The factory for the enclosed type.
		 */
		protected SymbolicElemFactory<R,S> sfac;
		
		/**
		 * The number of dimensions over which to evaluate the descent algorithm.
		 */
		protected Adim dim;
		
		/**
		 * The cache to be used for symbolic evals or null if there is no cache.
		 */
		protected HashMap<SCacheKey<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElem<R, S>> cache;
		
	}
	
	
	
	/**
	 * Mapping from multivector function indices to descent algorithm function indices.
	 */
	protected HashMap<HashSet<BigInteger>,BigInteger> inMapFun = new HashMap<HashSet<BigInteger>,BigInteger>();
	
	
	/**
	 * Mapping from descent algorithm function indices to multivector function indices.
	 */
	protected HashMap<HashSet<BigInteger>,HashSet<BigInteger>> outMapFun = new HashMap<HashSet<BigInteger>,HashSet<BigInteger>>();
	
	
	/**
	 * Mapping from descent multivector offset indices to algorithm offset indices.
	 */
	protected HashMap<HashSet<BigInteger>,HashSet<BigInteger>> inMapOffset = new HashMap<HashSet<BigInteger>,HashSet<BigInteger>>();
	
	
	/**
	 * Mapping from descent algorithm offset indices to multivector offset indices.
	 */
	protected HashMap<HashSet<BigInteger>,HashSet<BigInteger>> outMapOffset = new HashMap<HashSet<BigInteger>,HashSet<BigInteger>>();
	
	
	/**
	 * Multivariate descent algorithm.
	 */
	protected DescentAlgorithmMultiElem<Adim,R,S> descent;
	
	
	
	/**
	 * Number of dimensions for input multivector.
	 */
	protected U idim;
	
	/**
	 * Ord for input multivector.
	 */
	protected A iord;
	
	/**
	 * Number of dimensions for descent algorithm.
	 */
	protected Adim odim;
	
	/**
	 * Factory for enclosed type.
	 */
	protected S fac;
	
	
	
	/**
	 * Maps from an input functions multivector to an output functions vector.  Override this method to change the mapping.
	 * 
	 * @param functions The input functions multivector.
	 * @param ofun The output functions vector.
	 */
	protected void mapFunsInputToOutput( final GeometricAlgebraMultivectorElem<U,A,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> functions ,
			final GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> ofun )
	{
		final Iterator<HashSet<BigInteger>> it = functions.getKeyIterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			
			BigInteger key2 = inMapFun.get( key );
			
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( key2 );
			
			ofun.setVal( hs , functions.get( key ) );
		}
	}
	
	
	/**
	 * Determines the dimension count from the multivector of functions.  Also places 
	 * functional data into the mapping function data members.  Override this method to change the mapping.
	 * 
	 * @param functions The multivector of functions.
	 * @return The dimension count.
	 */
	protected BigInteger mapDimCnt( final GeometricAlgebraMultivectorElem<U,A,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> functions )
	{
		BigInteger dimCnt = BigInteger.ZERO;
		
		
		final Iterator<HashSet<BigInteger>> it = functions.getKeyIterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			
			inMapFun.put(key, dimCnt);
			
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( dimCnt );
			
			outMapFun.put( hs , key );
			
			dimCnt = dimCnt.add( BigInteger.ONE );
		}
		
		return( dimCnt );
	}
	
	
	
	/**
	 * Constructs the remap.
	 * 
	 * @param param Input parameter for the remap.
	 * @param cache Cache to be used for symbolic evals if useCachedEval() returns true.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public DescentAlgorithmMultiElemRemap( final DescentAlgorithmMultiElemRemapParam<U,A,R,S> param,
			final HashMap<SCacheKey<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElem<R, S>> cache )
					throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		idim = param.getDim();
		iord = param.getOrd();
		fac = param.getSfac().getFac().getFac();
		
		
		
		final BigInteger dimCnt = mapDimCnt( param.getFunctions() );
		
		
		odim = new Adim( dimCnt );
		
		
		
		final GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> ofun =
				new GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
					SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>( param.getSfac() , odim , new GeometricAlgebraOrd<Adim>() );
		
		
		
		mapFunsInputToOutput( param.getFunctions() , ofun );
		
		
		
		final ArrayList<ArrayList<? extends Elem<?,?>>> withRespectTos = new ArrayList<ArrayList<? extends Elem<?,?>>>();
		
		final Iterator<HashSet<BigInteger>> it = param.getWithRespectTosI().keySet().iterator();
		BigInteger wcnt = BigInteger.ZERO;
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			
			withRespectTos.add( param.getWithRespectTosI().get( key ) );
			
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( wcnt );
			
			outMapOffset.put( hs , key );
			inMapOffset.put( key , hs );
			
			wcnt = wcnt.add( BigInteger.ONE );
		}
		
		final GenDescentParam gdp = new GenDescentParam();
		
		gdp.setFunctions( ofun );
		gdp.setWithRespectTos( withRespectTos );
		gdp.setImplicitSpaceFirstLevel( param.getImplicitSpaceFirstLevel() );
		gdp.setSfac( param.getSfac().getFac() );
		gdp.setDim( odim );
		gdp.setCache( cache );
		
		descent = genDescent( gdp );
		
	}
	
	
	/**
	 * Runs descent algorithm.
	 * 
	 * @param implicitSpaceInitialGuess The implicit space for the initial guess.
	 * @return An iterated result.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public GeometricAlgebraMultivectorElem<U,A,R,S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,R,S> sv = descent.eval(implicitSpaceInitialGuess);
		GeometricAlgebraMultivectorElem<U,A,R,S> ret =
				new GeometricAlgebraMultivectorElem<U,A,R,S>( fac , idim , iord );
		
		Iterator<HashSet<BigInteger>> it = sv.getKeyIterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			ret.setVal( outMapFun.get( key ) , sv.get( key ) );
		}
		
		return( ret );
	}
	
	
	/**
	 * Constructs a multivariate descent algorithm.
	 * 
	 * @param param The parameters for the generation.
	 * @return An instance of multivariate descent algorithm.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected abstract DescentAlgorithmMultiElem<Adim,R,S> genDescent( final GenDescentParam param ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;

	
	
	/**
	 * Updates function parameters based on the internal descent algorithm iteration results.
	 * 
	 * @param iterationOffset The amount to which the iteration has estimated the function parameter should change.
	 */
	protected void performIterationUpdateInternal( GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,R,S> iterationOffset )
	{
		GeometricAlgebraMultivectorElem<U,A,R,S> ret =
				new GeometricAlgebraMultivectorElem<U,A,R,S>( fac , idim , iord );
		
		Iterator<HashSet<BigInteger>> it = iterationOffset.getKeyIterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			ret.setVal( outMapOffset.get( key ) , iterationOffset.get( key ) );
		}
		
		performIterationUpdate( ret );
	}
	
	
	/**
	 * Updates function parameters for multivector iteration results.
	 * 
	 * @param iterationOffset The amount to which the iteration has estimated the function parameter should change.
	 */
	protected abstract void performIterationUpdate( GeometricAlgebraMultivectorElem<U,A,R,S> iterationOffset );
	
	
	
	/**
	 * Sets the current value of the iteration.
	 * 
	 * @param value The new value.
	 */
	protected void setIterationValueInternal( GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,R,S> value )
	{
		GeometricAlgebraMultivectorElem<U,A,R,S> ret =
				new GeometricAlgebraMultivectorElem<U,A,R,S>( fac , idim , iord );
		
		Iterator<HashSet<BigInteger>> it = value.getKeyIterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			ret.setVal( outMapOffset.get( key ) , value.get( key ) );
		}
		
		setIterationValue( ret );
	}
	
	
	/**
	 * Sets the current value of the iteration.
	 * 
	 * @param value The new value.
	 */
	protected abstract void setIterationValue( GeometricAlgebraMultivectorElem<U,A,R,S> value );
	
	
	
	/**
	 * Gets the current value of the iteration.
	 * 
	 * @return The current value of the iteration.
	 */
	protected GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,R,S> getIterationValueInternal(  )
	{
		GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,R,S> ret =
				new GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,R,S>( fac , odim , new GeometricAlgebraOrd<Adim>() );
		
		GeometricAlgebraMultivectorElem<U,A,R,S> value =
				getIterationValue();
		
		Iterator<HashSet<BigInteger>> it = value.getKeyIterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			ret.setVal( inMapOffset.get( key ) , value.get( key ) );
		}
		
		return( ret );
	}
	
	
	
	
	/**
	 * Copies an instance for cloneThread();
	 * 
	 * @param in The instance to copy.
	 * @param threadIndex The index of the thread for which to clone.
	 */
	public DescentAlgorithmMultiElemRemap( final DescentAlgorithmMultiElemRemap<U,A,R,S> in , final BigInteger threadIndex )
	{

		
		inMapFun = new HashMap<HashSet<BigInteger>,BigInteger>();
		{
			Iterator<HashSet<BigInteger>> it = in.inMapFun.keySet().iterator();
			while( it.hasNext() )
			{
				final HashSet<BigInteger> ikey = (HashSet<BigInteger>)( it.next() );
				final BigInteger ival = in.inMapFun.get( ikey );
				inMapFun.put( (HashSet<BigInteger>)( ikey.clone() ) , ival );
			}
		}
		
		
		
		outMapFun = new HashMap<HashSet<BigInteger>,HashSet<BigInteger>>();
		{
			final Iterator<HashSet<BigInteger>> it = in.outMapFun.keySet().iterator();
			while( it.hasNext() )
			{
				final HashSet<BigInteger> ikey = it.next();
				final HashSet<BigInteger> ival = in.outMapFun.get( ikey );
				outMapFun.put( (HashSet<BigInteger>)( ikey.clone() ) , (HashSet<BigInteger>)( ival.clone() ) );
			}
		}
		
		
		
		
			
		inMapOffset = new HashMap<HashSet<BigInteger>,HashSet<BigInteger>>();
		{
			final Iterator<HashSet<BigInteger>> it = in.inMapOffset.keySet().iterator();
			while( it.hasNext() )
			{
				final HashSet<BigInteger> ikey = it.next();
				final HashSet<BigInteger> ival = in.inMapOffset.get( ikey );
				inMapOffset.put( (HashSet<BigInteger>)( ikey.clone() ) , (HashSet<BigInteger>)( ival.clone() ) );
			}
 		}
		
		
		
		
		outMapOffset = new HashMap<HashSet<BigInteger>,HashSet<BigInteger>>();
		{
			final Iterator<HashSet<BigInteger>> it = in.outMapOffset.keySet().iterator();
			while( it.hasNext() )
			{
				final HashSet<BigInteger> ikey = it.next();
				final HashSet<BigInteger> ival = in.outMapOffset.get( ikey );
				outMapOffset.put( (HashSet<BigInteger>)( ikey.clone() ) , (HashSet<BigInteger>)( ival.clone() ) );
			}
		}
		
		
		
		descent = in.descent.cloneThread(threadIndex);
		
		
		// The NumDimensions idim is presumed to be immutable.
		idim = in.idim;
		
		
		// The Ord iord is presumed to be immutable.
		iord = in.iord;
		
		
		odim = in.odim;
		
		fac = in.fac.cloneThread(threadIndex);
				
	}
	
	
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public abstract DescentAlgorithmMultiElemRemap<U,A,R,S> cloneThread( final BigInteger threadIndex );
	
	
	
	
	
	/**
	 * Gets the current value of the iteration.
	 * 
	 * @return The current value of the iteration.
	 */
	protected abstract GeometricAlgebraMultivectorElem<U,A,R,S> getIterationValue(  );
	
	
	
	/**
	 * Returns whether the iterations have completed.
	 * 
	 * @return True iff. the iterations are to complete.
	 */
	protected abstract boolean iterationsDone( );

	
	/**
	 * Returns the type of simplification to be used.  
	 * Override this method to turn off expression simplification.
	 * 
	 * @return The type of simplification to be used.
	 */
	protected SimplificationType useSimplification()
	{
		return( SimplificationType.DISTRIBUTE_SIMPLIFY2 );
	}
	
	
	/**
	 * Returns whether cached evals are to be used.
	 * Override this method to turn on cached evals.
	 * 
	 * @return True iff. cached evals are to be used.
	 */
	protected boolean useCachedEval()
	{
		return( false );
	}
	
	
	/**
	 * In the event that an attempted descent algorithm iteration diverges from the desired answer, 
	 * gets the maximum number of attempts that can be used to backtrack onto the original pre-iteration value.
	 * 
	 * @return The maximum number of backtrack iterations.
	 */
	protected int getMaxIterationsBacktrack()
	{
		return( 100 );
	}
	
	
	
}




