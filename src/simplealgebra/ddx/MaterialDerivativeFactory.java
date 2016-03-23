




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
import java.util.HashMap;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.AbstractCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteNumDimensionsCache;
import simplealgebra.et.DerivativeRemap;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.MetricTensorFactory;
import simplealgebra.et.TemporaryIndexFactory;
import simplealgebra.et.WriteDerivativeRemapCache;
import simplealgebra.et.WriteMetricTensorFactoryCache;
import simplealgebra.et.WriteOrdinaryDerivativeFactoryCache;
import simplealgebra.et.WriteTemporaryIndexFactoryCache;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
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
	 * Constructs the tensor factory for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param param Object defining the set of input parameters.
	 * @param ds The Drools session.
	 */
	public MaterialDerivativeFactory( 
			MaterialDerivativeFactoryParam<Z,U,R,S,K> param , DroolsSession ds )
	{
		this( param );
		ds.insert( this );
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
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalDerivativeCached(
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> in,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>, EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		return( this.genTerms( implicitSpace ).evalCached( implicitSpace , cache ) );
	}
	
	
	
	@Override
	public MaterialDerivativeFactory<Z,U,R,S,K> cloneThread( final BigInteger threadIndex )
	{
		final MaterialDerivativeFactoryParam<Z,U,R,S,K> param = new MaterialDerivativeFactoryParam();
		
		// The NumDimensions dim and the indices are presumed to be immutable.
		param.setFac( this.getFac().getFac().cloneThread(threadIndex) );
		param.setTensorWithRespectTo( tensorWithRespectTo.cloneThread(threadIndex) );
		if( cofac.getCoordVecFac() != null )
		{
			param.setCoordVecFac( cofac.getCoordVecFac().cloneThread(threadIndex) );
		}
		param.setTemp( cofac.getTemp().cloneThread(threadIndex) );
		param.setMetric( cofac.getMetric().cloneThread(threadIndex) );
		param.setDim( dim );
		param.setDfac( cofac.getOdfac().getDfac().cloneThread(threadIndex) );
		param.setFlfac( flfac.cloneThread(threadIndex) );
		param.setDerivT( derivT.cloneThread(threadIndex) );
		if( cofac.getRemap() != null )
		{
			param.setRemap( cofac.getRemap().cloneThread(threadIndex) );
		}
		
		if( ( param.getFac() != this.getFac().getFac() ) || 
				( param.getTensorWithRespectTo() != tensorWithRespectTo ) || 
				( param.getCoordVecFac() != cofac.getCoordVecFac() ) || 
				( param.getTemp() != cofac.getTemp() ) || 
				( param.getMetric() != cofac.getMetric() ) || 
				( param.getDfac() != cofac.getOdfac().getDfac() ) ||
				( param.getFlfac() != flfac ) ||
				( param.getDerivT() != derivT ) ||
				( param.getRemap() != cofac.getRemap() ) )
		{
			return( new MaterialDerivativeFactory<Z,U,R,S,K>( param ) );
		}
		return( this );
	}
	
	

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>, SymbolicElemFactory<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>> cache,
			PrintStream ps) {
		String st = cache.get( this );
		if( st == null )
		{
			cache.applyAuxCache( new WriteDirectionalDerivativePartialFactoryCache( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteNumDimensionsCache( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteCoordinateSystemFactoryCache<Z,R,S>( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteTemporaryIndexFactoryCache<Z>( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteDerivativeRemapCache<Z,R,S>( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteMetricTensorFactoryCache<Z,R,S>( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteFlowVectorFactoryCache<R,S,K>( cache.getCacheVal() ) );
			
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			final String tens = tensorWithRespectTo.writeDesc( cache , ps );
			String coordvs = null;
			if( cofac.getCoordVecFac() != null )
			{
				coordvs = cofac.getCoordVecFac().writeDesc( (WriteCoordinateSystemFactoryCache)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) WriteCoordinateSystemFactoryCache.class ) ) , (WriteElemCache) cache , ps );
			}
			final String temps = cofac.getTemp().writeDesc( (WriteTemporaryIndexFactoryCache)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) WriteTemporaryIndexFactoryCache.class ) ) , ps);
			final String metrics = cofac.getMetric().writeDesc( (WriteMetricTensorFactoryCache)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) WriteMetricTensorFactoryCache.class ) ) , ps);
			final String dims = dim.writeDesc( (WriteNumDimensionsCache)( cache.getAuxCache( WriteNumDimensionsCache.class ) )  , ps );
			
			
			final String dfacs = cofac.getOdfac().writeDesc( ( (WriteOrdinaryDerivativeFactoryCache)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) WriteOrdinaryDerivativeFactoryCache.class ) ) ) , (WriteElemCache<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>) cache.getInnerCache() , ps);
			
			
			final String flfacs = flfac.writeDesc( (WriteFlowVectorFactoryCache)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) WriteFlowVectorFactoryCache.class ) ) , ps );
			
			
			final String derivts = derivT.writeDesc( cache , ps );
			
			String remaps = null;
			if( cofac.getRemap() != null )
			{
				remaps = cofac.getRemap().writeDesc( (WriteDerivativeRemapCache)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) WriteDerivativeRemapCache.class ) ) , ps);
			}
			
			
			st = cache.getIncrementVal();
			cache.put(this, st);
			final String stp = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( MaterialDerivativeFactoryParam.class.getSimpleName() );
			ps.print( "<" );
			ps.print( "? extends Object" );
			ps.print( "," );
			dim.writeTypeString(ps);
			ps.print( "," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( "," );
			ps.print( "? extends Elem<?,?>" );
			ps.print( ">" );
			ps.print( " " );
			ps.print( stp );
			ps.print( " = new " );
			ps.print( MaterialDerivativeFactoryParam.class.getSimpleName() );
			ps.print( "<" );
			ps.print( "? extends Object" );
			ps.print( "," );
			dim.writeTypeString(ps);
			ps.print( "," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( "," );
			ps.print( "? extends Elem<?,?>" );
			ps.print( ">" );
			ps.print( "( " );
			ps.println( " );" );
			
			ps.print( stp );
			ps.print( ".setFac( " );
			ps.print( facs );
			ps.println( " );" );
			
			ps.print( stp );
			ps.print( ".setTensorWithRespectTo( " );
			ps.print( tens );
			ps.println( " );" );
			
			if( coordvs != null )
			{
				ps.print( stp );
				ps.print( ".setCoordVecFac( " );
				ps.print( coordvs );
				ps.println( " );" );
			}
			
			ps.print( stp );
			ps.print( ".setTemp( " );
			ps.print( temps );
			ps.println( " );" );
			
			ps.print( stp );
			ps.print( ".setMetric( " );
			ps.print( metrics );
			ps.println( " );" );
			
			ps.print( stp );
			ps.print( ".setDim( " );
			ps.print( dims );
			ps.println( " );" );
			
			ps.print( stp );
			ps.print( ".setDfac( " );
			ps.print( dfacs );
			ps.println( " );" );
			
			ps.print( stp );
			ps.print( ".setFlfac( " );
			ps.print( flfacs );
			ps.println( " );" );
			
			ps.print( stp );
			ps.print( ".setDerivT( " );
			ps.print( derivts );
			ps.println( " );" );
			
			if( remaps != null )
			{
				ps.print( stp );
				ps.print( ".setRemap( " );
				ps.print( remaps );
				ps.println( " );" );
			}
			
			ps.print( MaterialDerivativeFactory.class.getSimpleName() );
			ps.print( "<" );
			ps.print( "? extends Object" );
			ps.print( "," );
			dim.writeTypeString(ps);
			ps.print( "," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( "," );
			ps.print( "? extends Elem<?,?>" );
			ps.print( ">" );
			ps.print( " " );
			ps.print( stp );
			ps.print( " = new " );
			ps.print( MaterialDerivativeFactory.class.getSimpleName() );
			ps.print( "<" );
			ps.print( "? extends Object" );
			ps.print( "," );
			dim.writeTypeString(ps);
			ps.print( "," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( "," );
			ps.print( "? extends Elem<?,?>" );
			ps.print( ">" );
			ps.print( "( " );
			ps.print( stp );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator pc,
			PrintStream ps) {
		
		ps.print( "<msub><mo>D</mo>" );
		ps.print( "<mi>" + derivativeIndex + "</mi></msub>" );
		if( pc.parenNeeded( this ,  tensorWithRespectTo , true ) )
		{
			pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		}
		else
		{
			ps.print( "<mrow>" );
		}
		tensorWithRespectTo.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  tensorWithRespectTo , true ) )
		{
			pc.getParenthesisGenerator().handleParenthesisClose(ps);
		}
		else
		{
			ps.print( "</mrow>" );
		}
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		tensorWithRespectTo.performInserts( session );
		super.performInserts( session );
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

