




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

import java.util.ArrayList;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;



/**
 * Generates the expression <math display="inline">
 * <mrow>
 *  <mo>&nabla;</mo>
 *  <mfenced open="(" close=")"><mrow>
 *  <mfrac>
 *     <mrow>
 *       <mn>1</mn>
 *     </mrow>
 *     <mrow>
 *       <msqrt><mi>&rho;</mi></msqrt>
 *     </mrow>
 *  </mfrac>
 *  <mfenced open="(" close=")"><mrow>
 *  <msup>
 *           <mo>&nabla;</mo>
 *         <mn>2</mn>
 *   </msup>
 *   <msqrt><mi>&rho;</mi></msqrt>
 *   </mrow></mfenced>
 *   </mrow></mfenced>
 * </mrow>
 * </math>
 * 
 * See equation 21.38 from the Feynman Lectures on Physics Volume III.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions over which to express the directional derivative.
 * @param <A> The ord of the directional derivative vector.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class DelRhoExpressionFactory<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>>
{
	
	
	
	
	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac Factory for the enclosed type.
	 * @param _dim The number of dimensions over which to express the directional derivative.
	 * @param _ord The ord of the directional derivative vector.
	 * @param _dfac Factory for generating the partial derivatives of the directional derivative.
	 */
	public DelRhoExpressionFactory( GeometricAlgebraMultivectorElemFactory<U,A, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			U _dim ,
			A _ord ,
			DirectionalDerivativePartialFactory<R,S,K> _dfac )
	{
		fac = _fac;
		dim = _dim;
		ord = _ord;
		dfac = _dfac;
	}
	
	
	
	/**
	 * Generates the expression <math display="inline">
     * <mrow>
     *  <mo>&nabla;</mo>
     *  <mfenced open="(" close=")"><mrow>
     *  <mfrac>
     *     <mrow>
     *       <mn>1</mn>
     *     </mrow>
     *     <mrow>
     *       <msqrt><mi>&rho;</mi></msqrt>
     *     </mrow>
     *  </mfrac>
     *  <mfenced open="(" close=")"><mrow>
     *  <msup>
     *           <mo>&nabla;</mo>
     *         <mn>2</mn>
     *   </msup>
     *   <msqrt><mi>&rho;</mi></msqrt>
     *   </mrow></mfenced>
     *   </mrow></mfenced>
     * </mrow>
     * </math>
     * 
     * See equation 21.38 from the Feynman Lectures on Physics Volume III.
	 * 
	 * @param elSqrtRho Input expression corresponding to the scalar <math display="inline"><mrow><msqrt><mi>&rho;</mi></msqrt></mrow></math>
	 * @return The generated expression.
	 * @throws NotInvertibleException
	 */
	public SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
		,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> genDelRhoExpression(
				SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
				,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> elSqrtRho ) throws NotInvertibleException
	{
		final DirectionalDerivative<U,A,R,S,K> d0 = new DirectionalDerivative<U,A,R,S,K>( fac , dim , ord , dfac );	
	
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
			a0 = d0.mult( elSqrtRho );
		
		final ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>> args0 = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
				,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>>();
		args0.add( a0 );
		
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> a1 =
			d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , args0 );
		
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
			a2a = elSqrtRho.invertRight();
		
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
			a2 = a2a.mult( a1 );
		
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
			a3 = d0.mult( a2 );
		
		return( a3 );
	}

	
	
	/**
	 * The multivector factory.
	 */
	private GeometricAlgebraMultivectorElemFactory<U,A, SymbolicElem<R, S>, 
		SymbolicElemFactory<R, S>> fac;
	
	/**
	 * The number of dimensions over which to express the directional derivative.
	 */
	private U dim;
	
	/**
	 * The ord of the directional derivative vector.
	 */
	private A ord;
	
	/**
	 * Factory for generating the partial derivatives of the directional derivative.
	 */
	private DirectionalDerivativePartialFactory<R,S,K> dfac;
	
}


