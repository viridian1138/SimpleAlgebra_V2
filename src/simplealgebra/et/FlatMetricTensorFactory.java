





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

import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Factory for generating flat (zero curvature) metric tensors as defined in General Relativity.
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the tensor indices.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class FlatMetricTensorFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	/**
	 * Factory for symbolic elems.
	 */
	SymbolicElemFactory<R, S> fac;
	
	/**
	 * The speed of light squared.
	 */
	SymbolicElem<R, S> cSquared;
	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac Factory for symbolic elems.
	 * @param _cSquared The speed of light squared.
	 */
	public FlatMetricTensorFactory( SymbolicElemFactory<R, S> _fac , SymbolicElem<R, S> _cSquared )
	{
		fac = _fac;
		cSquared = _cSquared;
	}
	
	
	/**
	 * Returns an instance of the metric tensor.
	 * 
	 * @param covariantIndic Whether the metric tensor is to have covariant indices or contravariant indices.
	 * @param index0 First index for the tensor of rank two.
	 * @param index1 Second index for the tensor of rank two.
	 * @param numElem The number of dimensions in the tensor.
	 * @return An instance of the metric tensor.
	 * @throws NotInvertibleException
	 */
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
		getMetricTensor( boolean covariantIndic , Z index0 , Z index1 , BigInteger numElem ) throws NotInvertibleException 
		{
		
			ArrayList<Z> contravariantIndices = new ArrayList<Z>();
			ArrayList<Z> covariantIndices = new ArrayList<Z>();
			
			
			covariantIndices.add( index0 );
			covariantIndices.add( index1 );
			
		
			EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> tel =
				new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( fac , contravariantIndices , covariantIndices );
			
			{
				NonAlteredCSquared<R,S> cSq =
						new NonAlteredCSquared<R,S>( fac , cSquared );
				ArrayList<BigInteger> el = new ArrayList<BigInteger>();
				el.add( BigInteger.ZERO );
				el.add( BigInteger.ZERO );
				tel.setVal( el , cSq.getAlteredCSquared( covariantIndic ) );
			}
			
			for( BigInteger cnt = BigInteger.ONE ; cnt.compareTo(numElem) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
			{
				ArrayList<BigInteger> el = new ArrayList<BigInteger>();
				el.add( cnt );
				el.add( cnt );
				tel.setVal( el , fac.identity() );
			}
			
			return( tel );
		}

}


