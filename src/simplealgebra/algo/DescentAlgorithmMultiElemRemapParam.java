






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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Input parameter for DescentAlgorithmMultiElemRemap.
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
public class DescentAlgorithmMultiElemRemapParam<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	
	
	
	/**
	 * Gets the input multivector of functions.
	 * 
	 * @return The input multivector of functions.
	 */
	public GeometricAlgebraMultivectorElem<U, A, SymbolicElem<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElemFactory<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> getFunctions() {
		return functions;
	}

	/**
	 * Sets the input multivector of functions.
	 * 
	 * @param functions The input multivector of functions.
	 */
	public void setFunctions(
			GeometricAlgebraMultivectorElem<U, A, SymbolicElem<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElemFactory<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> functions) {
		this.functions = functions;
	}

	/**
	 * Gets the set of variables to take derivatives with respect to.
	 * 
	 * @return The set of variables to take derivatives with respect to.
	 */
	public HashMap<HashSet<BigInteger>, ArrayList<? extends Elem<?, ?>>> getWithRespectTosI() {
		return withRespectTosI;
	}

	/**
	 * Sets the set of variables to take derivatives with respect to.
	 * 
	 * @param withRespectTosI The set of variables to take derivatives with respect to.
	 */
	public void setWithRespectTosI(
			HashMap<HashSet<BigInteger>, ArrayList<? extends Elem<?, ?>>> withRespectTosI) {
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
	 * The number of dimensions in the multivector.
	 * 
	 * @return The number of dimensions in the multivector.
	 */
	public U getDim() {
		return dim;
	}

	/**
	 * Sets the number of dimensions in the multivector.
	 * 
	 * @param dim The number of dimensions in the multivector.
	 */
	public void setDim(U dim) {
		this.dim = dim;
	}

	/**
	 * Gets the input Ord.
	 * 
	 * @return The input Ord.
	 */
	public A getOrd() {
		return ord;
	}

	/**
	 * Sets the input Ord.
	 * 
	 * @param ord The input Ord.
	 */
	public void setOrd(A ord) {
		this.ord = ord;
	}

	
	
	/**
	 * Input multivector of functions.
	 */
	protected GeometricAlgebraMultivectorElem<U,A,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
		SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> functions = null;
	
	/**
	 * Set of variables to take derivatives with respect to.
	 */
	protected HashMap<HashSet<BigInteger>,ArrayList<? extends Elem<?,?>>> withRespectTosI = null;
	
	/**
	 * Implicit space for the initial eval.
	 */
	protected HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel = null;
		
	/**
	 * Factory for enclosed type.
	 */
	protected SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> sfac = null;
	
	/**
	 * The number of dimensions in the multivector.
	 */
	protected U dim = null; 
	
	/**
	 * The input Ord.
	 */
	protected A ord = null;
	
}




