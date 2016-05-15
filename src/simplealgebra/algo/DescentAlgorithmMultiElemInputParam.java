






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
import java.util.Iterator;
import java.util.Map.Entry;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Input parameter for a MultiElem descent algorithm.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions.
 * @param <R> The enclosed type for the evaluation.
 * @param <S> The factory for the enclosed type for the evaluation.
 */
public abstract class DescentAlgorithmMultiElemInputParam<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	
	

	/**
	 * Updates function parameters based on the iteration.
	 * 
	 * @param iterationOffset The amount to which the iteration has estimated the function parameter should change.
	 */
	protected abstract void performIterationUpdate( GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> iterationOffset );
	
	
	
	/**
	 * Sets the current value of the iteration.
	 * 
	 * @param value The new value.
	 */
	protected abstract void setIterationValue( GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> value );
	
	
	
	/**
	 * Gets the current value of the iteration.
	 * 
	 * @return The current value.
	 */
	protected abstract GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> getIterationValue( );
	
	
	/**
	 * Caches the current iteration value.
	 */
	protected abstract void cacheIterationValue();
	
	
	/**
	 * Retrieves the current iteration value from the cache.
	 */
	protected abstract void retrieveIterationValue();
	
	
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
	
	
	/**
	 * Returns whether convergence-wise the new function value should be accepted as an improvement over the old function value.
	 * 
	 * @param lastValue The old function value.
	 * @param newValue The new function value.
	 * @return True iff. the new function value should be accepted as an improvement over the old function value.
	 */
	protected boolean evalIterationImproved( GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> lastValue , 
			GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> newValue )
	{
		return( true );
	}
	
	
	
	
	
	
	/**
	 * Gets the functions over which to evaluate the descent algorithm.
	 * 
	 * @return The functions over which to evaluate the descent algorithm.
	 */
	public GeometricAlgebraMultivectorElem<U, GeometricAlgebraOrd<U>, SymbolicElem<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElemFactory<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> getFunctions() {
		return functions;
	}

	/**
	 * Sets the functions over which to evaluate the descent algorithm.
	 * 
	 * @param functions The functions over which to evaluate the descent algorithm.
	 */
	public void setFunctions(
			GeometricAlgebraMultivectorElem<U, GeometricAlgebraOrd<U>, SymbolicElem<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElemFactory<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> functions) {
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
	public U getDim() {
		return dim;
	}

	/**
	 * Sets the number of dimensions over which to evaluate the descent algorithm.
	 * 
	 * @param dim The number of dimensions over which to evaluate the descent algorithm.
	 */
	public void setDim(U dim) {
		this.dim = dim;
	}
	
	
	
	public DescentAlgorithmMultiElemInputParam( )
	{
		
	}

	
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public abstract DescentAlgorithmMultiElemInputParam<U,R,S> cloneThread( final BigInteger threadIndex );
	
	
	
	/**
	 * Copies an instance for cloneThread();
	 * 
	 * @param in The instance to copy.
	 * @param threadIndex The index of the thread for which to clone.
	 */
	protected DescentAlgorithmMultiElemInputParam( final DescentAlgorithmMultiElemInputParam<U,R,S> in , final BigInteger threadIndex )
	{
		functions = in.functions.cloneThread(threadIndex);
		
		withRespectTos = (ArrayList<ArrayList<? extends Elem<?,?>>>)( new ArrayList() );
		for( final ArrayList<? extends Elem<?,?>> va : in.withRespectTos )
		{
			final ArrayList<? extends Elem<?,?>> vaa = (ArrayList<? extends Elem<?,?>>)( new ArrayList() );
			for( final Elem ela : va )
			{
				( (ArrayList<Elem>) ((ArrayList)(vaa)) ).add( ela );
			}
			( (ArrayList) withRespectTos ).add( vaa );
		}
		
		
		implicitSpaceFirstLevel = (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
		for( Entry<? extends Elem<?,?>,? extends Elem<?,?>> ii : in.implicitSpaceFirstLevel.entrySet() )
		{
			final Elem<?,?> ikey = ii.getKey();
			final Elem<?,?> ival = ii.getValue();
			( (HashMap) implicitSpaceFirstLevel ).put( ikey.cloneThread(threadIndex) , ival.cloneThread(threadIndex) );
		}
		
		sfac = in.sfac.cloneThread(threadIndex);
		
		// The NumDimensions dim is presumed to be immutable.
		dim = in.dim;
		
	}
	
	
	
	/**
	 * The functions over which to evaluate the descent algorithm.
	 */
	protected GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
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
	protected U dim;

}




