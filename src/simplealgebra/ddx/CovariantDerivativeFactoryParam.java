




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





package simplealgebra.ddx;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.et.DerivativeRemap;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.MetricTensorFactory;
import simplealgebra.et.TemporaryIndexFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Input parameters for CovariantDerivativeFactory.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions for the index.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class CovariantDerivativeFactoryParam<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
{

	
	
	
	

	/**
	 * Gets the factory for the enclosed type.
	 * 
	 * @return The factory for the enclosed type.
	 */
	public EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> getFac() {
		return fac;
	}


	/**
	 * Sets the factory for the enclosed type.
	 * 
	 * @param fac The factory for the enclosed type.
	 */
	public void setFac(
			EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> fac) {
		this.fac = fac;
	}


	/**
	 * Gets the expression to which to apply the derivative.
	 * 
	 * @return The expression to which to apply the derivative.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> getTensorWithRespectTo() {
		return tensorWithRespectTo;
	}


	/**
	 * Sets the expression to which to apply the derivative.
	 * 
	 * @param tensorWithRespectTo The expression to which to apply the derivative.
	 */
	public void setTensorWithRespectTo(
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> tensorWithRespectTo) {
		this.tensorWithRespectTo = tensorWithRespectTo;
	}


	/**
	 * Gets the tensor index of the covariant derivative.
	 * 
	 * @return The tensor index of the covariant derivative.
	 */
	public Z getDerivativeIndex() {
		return derivativeIndex;
	}


	/**
	 * Sets the tensor index of the covariant derivative.
	 * 
	 * @param derivativeIndex The tensor index of the covariant derivative.
	 */
	public void setDerivativeIndex(Z derivativeIndex) {
		this.derivativeIndex = derivativeIndex;
	}


	/**
	 * Gets the factory for the underlying coordinate system U.
	 * 
	 * @return The factory for the underlying coordinate system U.
	 */
	public CoordinateSystemFactory<Z, R, S> getCoordVecFac() {
		return coordVecFac;
	}


	/**
	 * Sets the factory for the underlying coordinate system U.
	 * 
	 * @param coordVecFac The factory for the underlying coordinate system U.
	 */
	public void setCoordVecFac(CoordinateSystemFactory<Z, R, S> coordVecFac) {
		this.coordVecFac = coordVecFac;
	}


	/**
	 * Gets the factory for generating temporary indices in the connection coefficient.
	 * 
	 * @return The factory for generating temporary indices in the connection coefficient.
	 */
	public TemporaryIndexFactory<Z> getTemp() {
		return temp;
	}


	/**
	 * Sets the factory for generating temporary indices in the connection coefficient.
	 * 
	 * @param temp The factory for generating temporary indices in the connection coefficient.
	 */
	public void setTemp(TemporaryIndexFactory<Z> temp) {
		this.temp = temp;
	}


	/**
	 * Gets the factory for generating metric tensors.
	 * 
	 * @return The factory for generating metric tensors.
	 */
	public MetricTensorFactory<Z, R, S> getMetric() {
		return metric;
	}


	/**
	 * Sets the factory for generating metric tensors.
	 * 
	 * @param metric The factory for generating metric tensors.
	 */
	public void setMetric(MetricTensorFactory<Z, R, S> metric) {
		this.metric = metric;
	}


	/**
	 * Gets the number of dimensions for the index.
	 * 
	 * @return The number of dimensions for the index.
	 */
	public U getDim() {
		return dim;
	}


	/**
	 * Sets the number of dimensions for the index.
	 * 
	 * @param dim The number of dimensions for the index.
	 */
	public void setDim(U dim) {
		this.dim = dim;
	}


	/**
	 * Gets the factory for generating the partial derivatives of a directional derivative.
	 * 
	 * @return The factory for generating the partial derivatives of a directional derivative.
	 */
	public DirectionalDerivativePartialFactory<R, S, K> getDfac() {
		return dfac;
	}


	/**
	 * Sets the factory for generating the partial derivatives of a directional derivative.
	 * 
	 * @param dfac The factory for generating the partial derivatives of a directional derivative.
	 */
	public void setDfac(DirectionalDerivativePartialFactory<R, S, K> dfac) {
		this.dfac = dfac;
	}


	/**
	 * Gets the parameter describing how to remap the derivative.
	 * 
	 * @return The parameter describing how to remap the derivative.
	 */
	public DerivativeRemap<Z, R, S> getRemap() {
		return remap;
	}


	/**
	 * Sets the parameter describing how to remap the derivative.
	 * 
	 * @param remap The parameter describing how to remap the derivative.
	 */
	public void setRemap(DerivativeRemap<Z, R, S> remap) {
		this.remap = remap;
	}


	
	
	
	/**
	 * The factory for the enclosed type.
	 */
	protected EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> fac;
	
	
	/**
	 * The expression to which to apply the derivative.
	 */
	protected SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> tensorWithRespectTo;
	
	
	/**
	 * The tensor index of the covariant derivative.
	 */
	protected Z derivativeIndex;
	
	
	/**
	 * The factory for the underlying coordinate system U.  This is allowed to be null
	 * in instances where the space of the derivatives as the space of the tensor to
	 * which the derivative is applied.
	 */
	protected CoordinateSystemFactory<Z,R,S> coordVecFac;
	
	
	/**
	 * A factory for generating temporary indices in the connection coefficient.
	 */
	protected TemporaryIndexFactory<Z> temp;
	
	
	/**
	 * A factory for generating metric tensors.
	 */
	protected MetricTensorFactory<Z,R,S> metric;
	
	
	/**
	 * The number of dimensions for the index.
	 */
	protected U dim;
	
	
	/**
	 * Factory for generating the partial derivatives of a directional derivative.
	 */
	protected DirectionalDerivativePartialFactory<R,S,K> dfac;
		
	
	/**
	 * Parameter describing how to remap the derivative.  Leave as null if no remapping is desired.
	 */
	protected DerivativeRemap<Z,R,S> remap;
	
	


}

