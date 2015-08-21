






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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Input parameters for DescentAlgorithmMultiElemRemapTensor.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the tensor indices.
 * @param <R> The nested type.
 * @param <S> The factory for the nested type.
 */
public class DescentAlgorithmMultiElemRemapTensorParam<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	 
	 	 
	 
	
	
	
	/**
	 * Gets the input tensor of functions.
	 * 
	 * @return The input tensor of functions.
	 */
	public EinsteinTensorElem<Z, SymbolicElem<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElemFactory<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> getFunctions() {
		return functions;
	}

	/**
	 * Sets the input tensor of functions.
	 * 
	 * @param functions The input tensor of functions.
	 */
	public void setFunctions(
			EinsteinTensorElem<Z, SymbolicElem<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElemFactory<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> functions) {
		this.functions = functions;
	}

	/**
	 * Gets the set of variables to take derivatives with respect to.
	 * 
	 * @return The set of variables to take derivatives with respect to.
	 */
	public HashMap<ArrayList<BigInteger>, ArrayList<? extends Elem<?, ?>>> getWithRespectTosI() {
		return withRespectTosI;
	}

	/**
	 * Sets the set of variables to take derivatives with respect to.
	 * 
	 * @param withRespectTosI The set of variables to take derivatives with respect to.
	 */
	public void setWithRespectTosI(
			HashMap<ArrayList<BigInteger>, ArrayList<? extends Elem<?, ?>>> withRespectTosI) {
		this.withRespectTosI = withRespectTosI;
	}

	/**
	 * Gets the implicit space for the initial eval.
	 * 
	 * @return The implicit space for the initial eval.
	 */
	public HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> getImplicitSpaceFirstLevel() {
		return implicitSpaceFirstLevel;
	}

	/**
	 * Sets the implicit space for the initial eval.
	 * 
	 * @param implicitSpaceFirstLevel The implicit space for the initial eval.
	 */
	public void setImplicitSpaceFirstLevel(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel) {
		this.implicitSpaceFirstLevel = implicitSpaceFirstLevel;
	}

	/**
	 * Gets the factory for enclosed type.
	 * 
	 * @return The factory for enclosed type.
	 */
	public SymbolicElemFactory<SymbolicElem<R, S>, SymbolicElemFactory<R, S>> getSfac() {
		return sfac;
	}

	/**
	 * Sets the factory for enclosed type.
	 * 
	 * @param sfac The factory for enclosed type.
	 */
	public void setSfac(
			SymbolicElemFactory<SymbolicElem<R, S>, SymbolicElemFactory<R, S>> sfac) {
		this.sfac = sfac;
	}

	/**
	 * Gets the contravariant indices of the input tensor.
	 * 
	 * @return The contravariant indices of the input tensor.
	 */
	public ArrayList<Z> getContravariantIndices() {
		return contravariantIndices;
	}

	/**
	 * Sets the contravariant indices of the input tensor.
	 * 
	 * @param contravariantIndices The contravariant indices of the input tensor.
	 */
	public void setContravariantIndices(ArrayList<Z> contravariantIndices) {
		this.contravariantIndices = contravariantIndices;
	}

	/**
	 * Gets the covariant indices of the input tensor.
	 * 
	 * @return The covariant indices of the input tensor.
	 */
	public ArrayList<Z> getCovariantIndices() {
		return covariantIndices;
	}

	/**
	 * Sets the covariant indices of the input tensor.
	 * 
	 * @param covariantIndices The covariant indices of the input tensor.
	 */
	public void setCovariantIndices(ArrayList<Z> covariantIndices) {
		this.covariantIndices = covariantIndices;
	}
	
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public DescentAlgorithmMultiElemRemapTensorParam<Z,R,S>
		cloneThread( final BigInteger threadIndex )
	{
		final DescentAlgorithmMultiElemRemapTensorParam<Z,R,S> ret = new DescentAlgorithmMultiElemRemapTensorParam<Z,R,S>();
		
		
		ret.functions = functions.cloneThread(threadIndex);
		
		
		
		
		ret.withRespectTosI = ( HashMap<ArrayList<BigInteger>,ArrayList<? extends Elem<?,?>>> )( new HashMap() );
		
		{
			Iterator<ArrayList<BigInteger>> it = withRespectTosI.keySet().iterator();
			
			while( it.hasNext() )
			{
				final ArrayList<BigInteger> ikey = it.next();
				final ArrayList<? extends Elem<?,?>> ival = withRespectTosI.get( ikey );
				final ArrayList<? extends Elem<?,?>> ivalc = (ArrayList<? extends Elem<?,?>>)( new ArrayList() );
				final Iterator<Elem> ita = (Iterator<Elem>)( ival.iterator() );
				while( ita.hasNext() )
				{
					( (ArrayList) ivalc ).add( ita.next().cloneThread(threadIndex) );
				}
				( (HashMap) ( ret.withRespectTosI ) ).put( ikey.clone() , ivalc );
			}
		}
		
		
		
		
		ret.implicitSpaceFirstLevel = (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
		{
			Iterator<? extends Elem<?,?>> it = implicitSpaceFirstLevel.keySet().iterator();
		
			while( it.hasNext() )
			{
				final Elem<?,?> ikey = it.next();
				final Elem<?,?> ival = implicitSpaceFirstLevel.get( ikey );
				( (HashMap) ( ret.implicitSpaceFirstLevel ) ).put( ikey.cloneThread(threadIndex) , ival.cloneThread(threadIndex) );
			}
		}
		
		
		
		ret.sfac = sfac.cloneThread(threadIndex);
		
		
		ret.contravariantIndices = new ArrayList<Z>();
		{
			Iterator<Z> it = contravariantIndices.iterator();
			while( it.hasNext() )
			{
				// It is presumed that the Z indices are immutable.
				ret.contravariantIndices.add( it.next() );
			}
		}
		
		
		ret.covariantIndices = new ArrayList<Z>();
		{
			Iterator<Z> it = covariantIndices.iterator();
			while( it.hasNext() )
			{
				// It is presumed that the Z indices are immutable.
				ret.covariantIndices.add( it.next() );
			}
		}
		

		return( ret );
		
	}
	
	
	
	
	/**
	 * Input tensor of functions.
	 */
	protected EinsteinTensorElem<Z,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
		SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> functions;
	
	
	/**
	 * Set of variables to take derivatives with respect to.
	 */
	protected HashMap<ArrayList<BigInteger>,ArrayList<? extends Elem<?,?>>> withRespectTosI;
	
	
	/**
	 * Implicit space for the initial eval.
	 */
	protected HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel;
	
	
	/**
	 * Factory for enclosed type.
	 */
	protected SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> sfac;
	
	
	/**
	 * The contravariant indices of the input tensor.
	 */
	protected ArrayList<Z> contravariantIndices;
	
	
	/**
	 * The covariant indices of the input tensor.
	 */
	protected ArrayList<Z> covariantIndices;
	
	
}




