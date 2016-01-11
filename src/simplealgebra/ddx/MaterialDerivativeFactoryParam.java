




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
 * Class for populating the constructor of a MaterialDerivativeFactory.
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
public class MaterialDerivativeFactoryParam<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
		extends Object
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
	 * Gets a factory for generating temporary indices in the connection coefficient.
	 * 
	 * @return A factory for generating temporary indices in the connection coefficient.
	 */
	public TemporaryIndexFactory<Z> getTemp() {
		return temp;
	}

	/**
	 * Sets a factory for generating temporary indices in the connection coefficient.
	 * 
	 * @param temp A factory for generating temporary indices in the connection coefficient.
	 */
	public void setTemp(TemporaryIndexFactory<Z> temp) {
		this.temp = temp;
	}

	/**
	 * Gets a factory for generating metric tensors.
	 * 
	 * @return A factory for generating metric tensors.
	 */
	public MetricTensorFactory<Z, R, S> getMetric() {
		return metric;
	}

	/**
	 * Sets a factory for generating metric tensors.
	 * 
	 * @param metric A factory for generating metric tensors.
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
	 * Gets the factory for generating the components of the flow vector.
	 * 
	 * @return The factory for generating the components of the flow vector.
	 */
	public FlowVectorFactory<R, S, K> getFlfac() {
		return flfac;
	}

	/**
	 * Sets the factory for generating the components of the flow vector.
	 * 
	 * @param flfac The factory for generating the components of the flow vector.
	 */
	public void setFlfac(FlowVectorFactory<R, S, K> flfac) {
		this.flfac = flfac;
	}

	/**
	 * Gets the "t derivative" scalar operator in the material derivative.
	 * 
	 * @return The "t derivative" scalar operator in the material derivative.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> getDerivT() {
		return derivT;
	}

	/**
	 * Sets the "t derivative" scalar operator in the material derivative.
	 * 
	 * @param derivT The "t derivative" scalar operator in the material derivative.
	 */
	public void setDerivT(
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> derivT) {
		this.derivT = derivT;
	}

	/**
	 * Gets the parameter describing how to remap the covariant part of the derivative.
	 * 
	 * @return The parameter describing how to remap the covariant part of the derivative.
	 */
	public DerivativeRemap<Z, R, S> getRemap() {
		return remap;
	}

	/**
	 * Sets the parameter describing how to remap the covariant part of the derivative.
	 * 
	 * @param remap The parameter describing how to remap the covariant part of the derivative.
	 */
	public void setRemap(DerivativeRemap<Z, R, S> remap) {
		this.remap = remap;
	}

	
	
	
	/**
	 * The factory for the enclosed type.
	 */
	protected EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
		SymbolicElemFactory<R, S>> fac = null;
	
	/**
	 * The expression to which to apply the derivative.
	 */
	protected SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> tensorWithRespectTo = null;
	
	/**
	 * The factory for the underlying coordinate system U.  This is allowed to be null
	 * in instances where the space of the derivatives as the space of the tensor to
	 * which the derivative is applied.
	 */
	protected CoordinateSystemFactory<Z,R,S> coordVecFac = null;
	
	/**
	 * A factory for generating temporary indices in the connection coefficient.
	 */
	protected TemporaryIndexFactory<Z> temp = null;
	
	/**
	 * A factory for generating metric tensors.
	 */
	protected MetricTensorFactory<Z,R,S> metric = null;
	
	/**
	 * The number of dimensions for the index.
	 */
	protected U dim = null;
	
	/**
	 * Factory for generating the partial derivatives of a directional derivative.
	 */
	protected DirectionalDerivativePartialFactory<R,S,K> dfac = null;
	
	/**
	 * Factory for generating the components of the flow vector.
	 */
	protected FlowVectorFactory<R,S,K> flfac = null;
	
	/**
	 * The "t derivative" scalar operator in the material derivative.
	 */
	protected SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> derivT = null;
	
	/**
	 * Parameter describing how to remap the covariant part of the derivative.  Leave as null if no remapping is desired.
	 */
	protected DerivativeRemap<Z,R,S> remap = null;
	
}


