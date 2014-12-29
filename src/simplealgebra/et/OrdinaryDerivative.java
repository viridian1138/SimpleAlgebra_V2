




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





package simplealgebra.et;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ddx.DerivativeElem;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Implements a factory for the tensor <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>v</mi>
 *  </msub>
 * </mrow>
 * </math> where <math display="inline">
 * <mrow>
 *  <mi>v</mi>
 * </mrow>
 * </math> is a tensor index.  This produces a rank-one tensor
 * with a set of partial derivative operators.  The name of the
 * particular index to be used is passed into the class as a
 * parameter.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions for the index.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class OrdinaryDerivative<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
		extends DerivativeElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
{

	/**
	 * Constructs the tensor factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _index The index for the rank-one tensor.
	 * @param _dim The number of dimensions for the index.
	 * @param _dfac Factory for generating the partial derivatives of a directional derivative.
	 */
	public OrdinaryDerivative( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			Z _index ,
			U _dim ,
			DirectionalDerivativePartialFactory<R,S,K> _dfac )
	{
		super( _fac );
		index = _index;
		dim = _dim;
		dfac = _dfac;
	}
	
	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalDerivative(
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> in ,
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		final SymbolicElemFactory<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, 
			EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
			facA = in.getFac();
		
		final EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
			facB = facA.getFac();
		
		final SymbolicElemFactory<R, S> facC = facB.getFac();
		
		final ArrayList<Z> contravariantIndices = new ArrayList<Z>();
		final ArrayList<Z> covariantIndices = new ArrayList<Z>();
		
		covariantIndices.add( index );
		
		final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> mul = 
				new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( facC , 
						contravariantIndices , covariantIndices );
		
		BigInteger cnt = BigInteger.ZERO;
		
		final BigInteger max = dim.getVal();
		
		for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			final ArrayList<BigInteger> key = new ArrayList<BigInteger>();
			key.add( cnt );
			
			SymbolicElem<R,S> val = dfac.getPartial( cnt );
			
			mul.setVal(key, val);
		}
		
		
		final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret =
				mul.mult( in.eval( implicitSpace ) );
		
		return( ret );
	}
	

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "ordinaryDerivativeTensor" );
	}
	
	/**
	 * The tensor index for the ordinary derivative.
	 */
	private Z index;
	
	/**
	 * The number of dimensions for the index.
	 */
	private U dim;
	
	/**
	 * Factory for generating the partial derivatives of a directional derivative.
	 */
	private DirectionalDerivativePartialFactory<R,S,K> dfac;
	
	

}

