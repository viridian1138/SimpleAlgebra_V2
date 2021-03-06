






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

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
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
public class DescentAlgorithmMultiElemInputParam<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	

	
	
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
	
	
	
	
	
	/**
	 * Returns the callbacks for the calling descent algorithm.
	 * @return The callbacks
	 */
	public DescentAlgorithmMultiElemInputParamCallbacks<U,R,S> getCallbacks() {
		return callbacks;
	}

	/**
	 * Sets the callbacks for the calling descent algorithm.
	 * @param callbacks The callbacks to set
	 */
	public void setCallbacks(DescentAlgorithmMultiElemInputParamCallbacks<U,R,S> callbacks) {
		this.callbacks = callbacks;
	}

	
	
	/**
	 * Constructs the parameter object.
	 */
	public DescentAlgorithmMultiElemInputParam( )
	{
		
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
	
	/**
	 * The set of algorithm callbacks.
	 */
	protected DescentAlgorithmMultiElemInputParamCallbacks<U,R,S> callbacks;

	
}




