




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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.et.ConnectionCoefficientFactory;
import simplealgebra.et.DerivativeRemap;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.MetricTensorFactory;
import simplealgebra.et.OrdinaryDerivativeFactory;
import simplealgebra.et.SymbolicRegenContravar;
import simplealgebra.et.TemporaryIndexFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


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
 *  <msup>
 *        <mrow>
 *          
 *              <mi>U</mi>
 *          
 *        </mrow>
 *      <mrow>
 *        <mi>&nu;</mi>
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
 * </math> term is the ordinary derivative, the <math display="inline">
 * <mrow>
 *  <mi>&Gamma;</mi>
 * </mrow>
 * </math> term is the connection coefficient, 
 * and the U vector represents the underlying coordinate system, particularly that
 * some ordinates such as time may be in different coordinate systems from other ordinates.
 * 
 * 
 * <P>
 * <P>See http://en.wikipedia.org/wiki/Covariant_derivative
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
public class CovariantDerivative<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
		extends DerivativeElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
{

	/**
	 * Constructs the tensor factory.
	 * 
	 * @param param The input parameter for the factory.
	 */
	public CovariantDerivative( 
			CovariantDerivativeFactoryParam<Z,U,R,S,K> param )
	{
		super( param.getFac() );
		derivativeIndex = param.getDerivativeIndex();
		coordVecFac = param.getCoordVecFac();
		temp = param.getTemp();
		metric = param.getMetric();
		remap = param.getRemap();
		odfac = new OrdinaryDerivativeFactory<Z,U,R,S,K>( param.getFac() , param.getDim() , param.getDfac() , null );
	}
	
	
	
	/**
	 * Applies the covariant derivative to an expression.
	 * 
	 * @param tensorWithRespectTo The tensor upon which to take the derivative.
	 * @param implicitSpace Implicit parameter space against which to perform the evaluation.
	 * @return The result of applying the derivative.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
		genTerms( 
				final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> tensorWithRespectTo ,
				final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException 
	{
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			deriv = odfac.getOrdinaryDerivative( tensorWithRespectTo , derivativeIndex );
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			sum = deriv;
		
		
		ConnectionCoefficientFactory<Z,U,R,S,K> afac = new ConnectionCoefficientFactory<Z,U,R,S,K>( metric , 
				temp , odfac );
		
		final ArrayList<Z> iContravar = tensorWithRespectTo.eval( implicitSpace ).getContravariantIndices();
		
		Iterator<Z> it = iContravar.iterator();
		
		while( it.hasNext() )
		{
			Z index = it.next();
			Z r = temp.getTemp();
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
				conn = afac.getConnectionCoefficient( r , derivativeIndex , index );
			
			
			ArrayList<Z> reContravar = new ArrayList<Z>( iContravar.size() );
			Iterator<Z> it2 = ((ArrayList<Z>)(iContravar.clone())).iterator();
			while( it2.hasNext() )
			{
				Z nxt = it2.next();
				reContravar.add( nxt != index ? nxt : r );
			}
			
			
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> src = 
					new SymbolicRegenContravar<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>( tensorWithRespectTo , fac, reContravar );
			
			
			if( coordVecFac != null )
			{
				src = src.mult( coordVecFac.genCoord( derivativeIndex ) );
			}
			
			
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
				sconn = conn.mult( src );
			
			
			sum = sum.add( sconn );
		}
		
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			ret = remap == null ? sum : remap.remap( sum );
		
		
		return( ret );
	}
	
	
	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalDerivative(
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> in ,
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( this.genTerms( in , implicitSpace ).eval( implicitSpace ) );
	}
	
	
	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalDerivativeCached(
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> in,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>, EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		return( this.genTerms( in , implicitSpace ).evalCached( implicitSpace , cache ) );
	}
	
	
	
	@Override
	public CovariantDerivative<Z,U,R,S,K> cloneThread( final BigInteger threadIndex )
	{
		final CovariantDerivativeFactoryParam<Z,U,R,S,K> param = new CovariantDerivativeFactoryParam<Z,U,R,S,K>();

		// The NumDimensions dim and the indices are presumed to be immutable.
		param.setFac( this.getFac().getFac().cloneThread(threadIndex) );
		param.setDerivativeIndex(derivativeIndex);
		param.setCoordVecFac( coordVecFac.cloneThread(threadIndex) );
		param.setTemp( temp.cloneThread(threadIndex) );
		param.setMetric( metric.cloneThread(threadIndex) );
		param.setDim( odfac.getDim() );
		param.setDfac( odfac.getDfac().cloneThread(threadIndex) );
		if( remap != null )
		{
			param.setRemap( remap.cloneThread(threadIndex) );
		}
		
		if( ( param.getFac() != this.getFac().getFac() ) ||  
				( param.getCoordVecFac() != coordVecFac ) || 
				( param.getTemp() != temp ) || 
				( param.getMetric() != metric ) || 
				( param.getDfac() != odfac.getDfac() ) || 
				( param.getRemap() != remap ) )
		{
			return( new CovariantDerivative<Z,U,R,S,K>( param ) );
		}
		return( this );
	}
	
	

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "covariantDerivativeOp[ " + derivativeIndex + " ]" );
	}
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> pc,
			PrintStream ps) {
		
		ps.print( "<msub><mo>&nabla;</mo>" );
		ps.print( "<mi>" + derivativeIndex + "</mi></msub>" );
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
	 * Gets the factory for the underlying coordinate system U.
	 * 
	 * @return The factory for the underlying coordinate system U.
	 */
	public CoordinateSystemFactory<Z, R, S> getCoordVecFac() {
		return coordVecFac;
	}



	/**
	 * The factory for generating temporary indices in the connection coefficient.
	 * 
	 * @return Gets the factory for generating temporary indices in the connection coefficient.
	 */
	public TemporaryIndexFactory<Z> getTemp() {
		return temp;
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
	 * Gets the factory for generating ordinary derivatives.
	 * 
	 * @return The factory for generating ordinary derivatives.
	 */
	public OrdinaryDerivativeFactory<Z, U, R, S, K> getOdfac() {
		return odfac;
	}



	/**
	 * Gets the function for remapping the derivative after it is calculated.
	 * 
	 * @return The function for remapping the derivative after it is calculated.
	 */
	public DerivativeRemap<Z, R, S> getRemap() {
		return remap;
	}





	/**
	 * The tensor index of the covariant derivative.
	 */
	private Z derivativeIndex;
	
	/**
	 * The factory for the underlying coordinate system U.
	 */
	private CoordinateSystemFactory<Z,R,S> coordVecFac;
	
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
	
	/**
	 * Function for remapping the derivative after it is calculated.  Leave as null if no remap is desired.
	 */
	private DerivativeRemap<Z,R,S> remap;


}

