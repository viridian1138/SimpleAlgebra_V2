




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
import java.util.HashMap;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.et.ConnectionCoefficientFactory;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.MetricTensorFactory;
import simplealgebra.et.OrdinaryDerivativeFactory;
import simplealgebra.et.SymbolicRegenCovar;
import simplealgebra.et.TemporaryIndexFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

import java.io.*;


/**
 * Factory for generating Covariant Derivatives, where a covariant derivative is defined by <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&nabla;</mo>
 *        <mi>&nu;</mi>
 *  </msub>
 *  <msup>
 *          <mi>V</mi>
 *        <mi>&mu;</mi>
 *  </msup>
 *  <mo>=</mo>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>&nu;</mi>
 *  </msub>
 *  <msup>
 *          <mi>V</mi>
 *        <mi>&mu;</mi>
 *  </msup>
 *  <mo>+</mo>
 *  <msubsup>
 *          <mi>&Gamma;</mi>
 *      <mrow>
 *        <mi>&alpha;</mi>
 *        <mi>&nu;</mi>
 *      </mrow>
 *        <mi>&mu;</mi>
 *  </msubsup>
 *  <msup>
 *        <mrow>
 *          
 *              <mi>V</mi>
 *          
 *        </mrow>
 *      <mrow>
 *        <mi>&alpha;</mi>
 *      </mrow>
 *  </msup>
 * </mrow>
 * </math> where the <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>v</mi>
 *  </msub>
 * </mrow>
 * </math> term is the ordinary derivative and the <math display="inline">
 * <mrow>
 *  <mi>&Gamma;</mi>
 * </mrow>
 * </math> term is the connection coefficient.
 * 
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions for the index.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class CovariantDerivativeFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
		extends DerivativeElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
{

	/**
	 * Constructs the tensor factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _tensorWithRespectTo The expression to which to apply the derivative.
	 * @param _derivativeIndex The tensor index of the covariant derivative.
	 * @param _temp A factory for generating temporary indices in the connection coefficient.
	 * @param _metric A factory for generating metric tensors.
	 * @param _dim The number of dimensions for the index.
	 * @param _dfac Factory for generating the partial derivatives of a directional derivative.
	 */
	public CovariantDerivativeFactory( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
				SymbolicElemFactory<R, S>> _fac , 
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> _tensorWithRespectTo,
			Z _derivativeIndex,
			TemporaryIndexFactory<Z> _temp,
			MetricTensorFactory<Z,R,S> _metric,
			U _dim ,
			DirectionalDerivativePartialFactory<R,S,K> _dfac )
	{
		super( _fac );
		tensorWithRespectTo = _tensorWithRespectTo;
		derivativeIndex = _derivativeIndex;
		temp = _temp;
		metric = _metric;
		odfac = new OrdinaryDerivativeFactory<Z,U,R,S,K>( _fac , _dim , _dfac  );
	}
	
	
	
	/**
	 * Applies the covariant derivative to an expression.
	 * 
	 * @param implicitSpace Implicit parameter space against which to perform the evaluation.
	 * @return The result of applying the derivative.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
		genTerms( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException 
	{
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			deriv = odfac.getOrdinaryDerivative( tensorWithRespectTo , derivativeIndex );
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			sum = deriv;
		
		
		ConnectionCoefficientFactory<Z,U,R,S,K> afac = new ConnectionCoefficientFactory<Z,U,R,S,K>( metric , 
				temp , odfac );
		
		final ArrayList<Z> iCovar = tensorWithRespectTo.eval( implicitSpace ).getCovariantIndices();
		
		Iterator<Z> it = iCovar.iterator();
		
		while( it.hasNext() )
		{
			Z index = it.next();
			Z r = temp.getTemp();
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
				conn = afac.getConnectionCoefficient( derivativeIndex , index , r );
			
			ArrayList<Z> reCovar = new ArrayList<Z>( iCovar.size() );
			Iterator<Z> it2 = ((ArrayList<Z>)(iCovar.clone())).iterator();
			while( it2.hasNext() )
			{
				Z nxt = it2.next();
				reCovar.add( nxt != index ? nxt : r );
			}
			
			SymbolicRegenCovar<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>> src = 
					new SymbolicRegenCovar<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>( tensorWithRespectTo , fac, reCovar );
			
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
				sconn = conn.mult( src );
			
			sum = sum.add( sconn );
		}
		
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
		ps.print( "covariantDerivative[ " + derivativeIndex + " ]" );
	}
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> pc,
			PrintStream ps) {
		
		ps.print( "<msub><mo>&nabla;</mo>" );
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
	 * The expression to which to apply the derivative.
	 */
	private SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> tensorWithRespectTo;
	
	/**
	 * The tensor index of the covariant derivative.
	 */
	private Z derivativeIndex;
	
	/**
	 * A factory for generating temporary indices in the connection coefficient.
	 */
	private TemporaryIndexFactory<Z> temp;
	
	/**
	 * A factory for generating metric tensors.
	 */
	private MetricTensorFactory<Z,R,S> metric;
	
	/**
	 * A factory for generating ordinary derivatives.
	 */
	private OrdinaryDerivativeFactory<Z,U,R,S,K> odfac;


}

