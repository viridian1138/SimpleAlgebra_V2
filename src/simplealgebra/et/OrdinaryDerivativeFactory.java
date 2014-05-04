





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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Factory for ordinary derivative operators for a Einstein Tensor.
 * 
 * @author thorngreen
 *
 * @param <Z>
 * @param <U>
 * @param <R>
 * @param <S>
 * @param <K>
 */
public class OrdinaryDerivativeFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	public OrdinaryDerivativeFactory( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			U _dim ,
			DirectionalDerivativePartialFactory<R,S,K> _dfac )
	{
		fac = _fac;
		dim = _dim;
		dfac = _dfac;
	}
	
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getOrdinaryDerivative( SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> term , Z derivativeIndex )
{
		final OrdinaryDerivative<Z,U,R,S,K> ord = new OrdinaryDerivative<Z,U,R,S,K>( fac , derivativeIndex , dim , dfac );
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			ret = ord.mult( term );
		
		return( ret );
}
	
	private EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
		SymbolicElemFactory<R, S>> fac;
	private U dim;
	private DirectionalDerivativePartialFactory<R,S,K> dfac;

}


