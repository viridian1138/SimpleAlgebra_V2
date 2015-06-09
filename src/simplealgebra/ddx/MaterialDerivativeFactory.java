




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

import java.io.PrintStream;
import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.et.DerivativeRemap;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.MetricTensorFactory;
import simplealgebra.et.TemporaryIndexFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Factory for generating Material Derivatives, where a material derivative is defined by <math display="inline">
 * <mrow>
 *  <mfrac>
 *    <mrow>
 *      <mo>D</mo>
 *      <mi>y</mi>
 *    </mrow>
 *    <mrow>
 *      <mo>D</mo>
 *      <mi>t</mi>
 *    </mrow>
 *  </mfrac>
 *  <mo>=</mo>
 *  <mfrac>
 *    <mrow>
 *      <mo>&PartialD;</mo>
 *      <mi>y</mi>
 *    </mrow>
 *    <mrow>
 *      <mo>&PartialD;</mo>
 *      <mi>t</mi>
 *    </mrow>
 *  </mfrac>
 *  <mo>+</mo>
 *  <mi>u</mi>
 *  <mo>&CenterDot;</mo>
 *  <mo>&nabla;</mo>
 *  <mi>y</mi>
 * </mrow>
 * </math>
 *
 * <P>where <math display="inline">
 * <mrow>
 *  <mo>&nabla;</mo>
 *  <mi>y</mi>
 * </mrow>
 * </math> is the covariant derivative.
 * 
 * 
 * 
 * <P>See http://en.wikipedia.org/wiki/material_derivative
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
public class MaterialDerivativeFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
		extends DerivativeElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
{

	/**
	 * Constructs the tensor factory.
	 * 
	 * @param param Object defining the set of input parameters.
	 */
	public MaterialDerivativeFactory( 
			MaterialDerivativeFactoryParam<Z,U,R,S,K> param )
	{
		super( param.getFac() );
		tensorWithRespectTo = param.getTensorWithRespectTo();
		derivativeIndex = param.getTemp().getTemp();
		dim = param.getDim();
		flfac = param.getFlfac();
		derivT = param.getDerivT();
		
		final CovariantDerivativeFactoryParam<Z,U,R,S,K> param2 =
				new CovariantDerivativeFactoryParam<Z,U,R,S,K>();
		
		
		param2.setFac( param.getFac() );
		param2.setTensorWithRespectTo( param.getTensorWithRespectTo() );
		param2.setDerivativeIndex( derivativeIndex );
		param2.setCoordVecFac( param.getCoordVecFac() );
		param2.setTemp( param.getTemp() );
		param2.setMetric( param.getMetric() );
		param2.setDim( param.getDim() );
		param2.setDfac( param.getDfac() );
		param2.setRemap( param.getRemap() );
		
		cofac = new CovariantDerivativeFactory<Z,U,R,S,K>( param2 );
	}
	
	
	
	/**
	 * Applies the material derivative to an expression.
	 * 
	 * @param implicitSpace Implicit parameter space against which to perform the evaluation.
	 * @return The result of applying the derivative.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
		genTerms( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException 
	{
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			covarD = cofac.genTerms( implicitSpace );
		
		FlowVectorTensor<Z,U,R,S,K> fl = 
				new FlowVectorTensor<Z,U,R,S,K>( covarD.getFac().getFac() , 
						derivativeIndex ,
						dim ,
						flfac );
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			flmult = fl.mult( covarD );
		
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			dtt = derivT.mult( tensorWithRespectTo );
		
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			sum = dtt.add( flmult );
		
		return( sum );
		
	}
	
	
	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalDerivative(
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> in ,
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( this.genTerms( implicitSpace ).eval( implicitSpace ) );
	}
	

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "materialDerivative[ " + derivativeIndex + " ]" );
	}
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> pc,
			PrintStream ps) {
		
		ps.print( "<msub><mo>D</mo>" );
		ps.print( "<mi>" + derivativeIndex + "</mi></msub>" );
		if( pc.parenNeeded( this ,  tensorWithRespectTo , true ) )
		{
			ps.print( "<mfenced><mrow>" );
		}
		else
		{
			ps.print( "<mrow>" );
		}
		tensorWithRespectTo.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  tensorWithRespectTo , true ) )
		{
			ps.print( "</mrow></mfenced>" );
		}
		else
		{
			ps.print( "</mrow>" );
		}
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
	 * Gets the tensor index of the covariant derivative.
	 * 
	 * @return The tensor index of the covariant derivative.
	 */
	public Z getDerivativeIndex() {
		return derivativeIndex;
	}



	/**
	 * Gets the number of dimensions over which to calculate the tensor.
	 * 
	 * @return The number of dimensions over which to calculate the tensor.
	 */
	public U getDim() {
		return dim;
	}



	/**
	 * Gets the factory for generating a flow vector.
	 * 
	 * @return The factory for generating a flow vector.
	 */
	public FlowVectorFactory<R, S, K> getFlfac() {
		return flfac;
	}



	/**
	 * Gets the factory for generating covariant derivatives.
	 * 
	 * @return The factory for generating covariant derivatives.
	 */
	public CovariantDerivativeFactory<Z, U, R, S, K> getCofac() {
		return cofac;
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
	 * The expression to which to apply the derivative.
	 */
	private SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> tensorWithRespectTo;
	
	/**
	 * The tensor index of the covariant derivative.
	 */
	private Z derivativeIndex;
	
	/**
	 * The number of dimensions over which to calculate the tensor.
	 */
	private U dim;
	
	/**
	 * A factory for generating a flow vector.
	 */
	private FlowVectorFactory<R,S,K> flfac;
	
	/**
	 * A factory for generating covariant derivatives.
	 */
	private CovariantDerivativeFactory<Z,U,R,S,K> cofac;

	/**
	 * The "t derivative" scalar operator in the material derivative.
	 */
	private SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> derivT;

}

