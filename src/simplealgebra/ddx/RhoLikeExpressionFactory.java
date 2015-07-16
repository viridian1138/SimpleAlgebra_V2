




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
 * Generates a <math display="inline"><mrow><mi>&rho;</mi></mrow></math>-like expression.
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
public class RhoLikeExpressionFactory<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>>
{
	
	
	
	
	
	/**
	 * Constructs the factory.
	 * 
	 * @param _epsilon Symbolic elem for <math display="inline"><mrow><mi>&epsilon;</mi></mrow></math>.
	 * @param _phi Symbolic elem for <math display="inline"><mrow><mi>&phi;</mi></mrow></math>.
	 * @param _delDotA Symbolic elem for <math display="inline"><mrow><mo>&nabla;</mo><mo>&CenterDot;</mo><mi>A</mi></mrow></math>.
	 * @param _tDerivative Symbolic elem for the partial derivative with respect to T.
	 * @param _del Directional Derivative operator.
	 */
	public RhoLikeExpressionFactory( 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
				GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> _epsilon ,
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
				GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> _phi ,
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
				GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> _delDotA ,
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
				GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> _tDerivative ,
			DirectionalDerivative<U,A,R,S,K> _del
			)
	{
		epsilon = _epsilon;
		phi = _phi;
		delDotA = _delDotA;
		tDerivative = _tDerivative;
		del = _del;
	}
	
	
	
	/**
	 * Generates a <math display="inline"><mrow><mi>&rho;</mi></mrow></math>-like expression.
	 * 
	 * @return The <math display="inline"><mrow><mi>&rho;</mi></mrow></math>-like expression.
	 * @throws NotInvertibleException
	 */
	public SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
		,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> genRhoLike( ) throws NotInvertibleException
	{
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
			delPhi = del.mult( phi );
		
		final ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>> args0 = new ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
				,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>>();
			args0.add( delPhi );
		
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> delDotDelPhi =
			del.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , args0 );
		
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
			tDerivDelDotA = tDerivative.mult( delDotA );
		
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
			sum = ( tDerivDelDotA.negate() ).mult( delDotDelPhi.negate() );
		
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>
			,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
			epsilonSum = epsilon.mult( sum );
		
		return( epsilonSum );
	}

	
	/**
	 * Symbolic elem for <math display="inline"><mrow><mi>&epsilon;</mi></mrow></math>.
	 */
	private SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
		GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> epsilon;
	
	/**
	 * Symbolic elem for <math display="inline"><mrow><mi>&phi;</mi></mrow></math>.
	 */
	private SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
		GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> phi;
	
	/**
	 * Symbolic elem for <math display="inline"><mrow><mo>&nabla;</mo><mo>&CenterDot;</mo><mi>A</mi></mrow></math>.
	 */
	private SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
		GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> delDotA;
	
	/**
	 * Symbolic elem for the partial derivative with respect to T.
	 */
	private SymbolicElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
		GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> tDerivative;
	
	/**
	 * Directional Derivative operator.
	 */
	private DirectionalDerivative<U,A,R,S,K> del;
	
}


