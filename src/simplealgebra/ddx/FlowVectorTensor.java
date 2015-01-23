






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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Implements a tensor for a flow vector.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions over which to express the flow vector.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class FlowVectorTensor<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
	extends SymbolicElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> {

	/**
	 * Constructs the flow vector tensor.
	 * 
	 * @param _fac Factory for the enclosed type.
	 * @param _index The tensor index for the flow vector.
	 * @param _dim The number of dimensions over which to calculate the flow vector.
	 * @param _dfac Factory for generating the components of the flow vector.
	 */
	public FlowVectorTensor( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			Z _index ,
			U _dim ,
			FlowVectorFactory<R,S,K> _dfac ) 
	{
		super( _fac );
		index = _index;
		dim = _dim;
		dfac = _dfac;
	}

	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException 
	{
		final SymbolicElemFactory<R, S> facC = this.getFac().getFac().getFac();
	
		final ArrayList<Z> contravariantIndices = new ArrayList<Z>();
		final ArrayList<Z> covariantIndices = new ArrayList<Z>();
	
		contravariantIndices.add( index );
	
		final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> mul = 
				new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( facC , 
						contravariantIndices , covariantIndices );
	
		BigInteger cnt = BigInteger.ZERO;
	
		final BigInteger max = dim.getVal();
	
		for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			final ArrayList<BigInteger> key = new ArrayList<BigInteger>();
			key.add( cnt );
		
			SymbolicElem<R,S> val = dfac.getComponent( cnt );
		
			mul.setVal(key, val);
		}
	
		return( mul );
	}

	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( dfac.exposesDerivatives() );
	}

	@Override
	public void writeString(PrintStream ps) {
		ps.print( "flowVectorTensor" );
	}
	
	/**
	 * The tensor index for the flow vector.
	 */
	private Z index;
	
	/**
	 * The number of dimensions for the index.
	 */
	private U dim;
	
	/**
	 * Factory for generating the components of the flow vector.
	 */
	private FlowVectorFactory<R,S,K> dfac;

}

