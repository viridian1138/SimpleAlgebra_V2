





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
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Factory for generating metric tensors as defined in General Relativity.
 * 
 * @author thorngreen
 *
 * @param <Z>
 * @param <R>
 * @param <S>
 */
public class EmFieldTensorFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	SymbolicElemFactory<R, S> fac;
	DirectionalDerivativePartialFactory<R, S, K> deriv;
	VectorPotentialFactory<R, S> vect;
	
	public EmFieldTensorFactory( SymbolicElemFactory<R, S> _fac , 
			DirectionalDerivativePartialFactory<R, S, K> _deriv,
			VectorPotentialFactory<R, S> _vect )
	{
		fac = _fac;
		deriv = _deriv;
		vect = _vect;
	}
	
	
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
		getEmFld( Z index0 , Z index1 , BigInteger numElem )
		{
		
			ArrayList<Z> contravariantIndices = new ArrayList<Z>();
			ArrayList<Z> covariantIndices = new ArrayList<Z>();
			
			
			covariantIndices.add( index0 );
			covariantIndices.add( index1 );
			
		
			EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> tel =
				new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( fac , contravariantIndices , covariantIndices );
			
			
			for( BigInteger cnti = BigInteger.ZERO ; cnti.compareTo(numElem) < 0 ; cnti = cnti.add( BigInteger.ONE ) )
			{
				for( BigInteger cntj = BigInteger.ZERO ; cntj.compareTo(numElem) < 0 ; cntj = cntj.add( BigInteger.ONE ) )
				{
					ArrayList<BigInteger> el = new ArrayList<BigInteger>();
					el.add( cnti );
					el.add( cntj );
					final SymbolicElem<R,S> di = deriv.getPartial( cnti );
					final SymbolicElem<R,S> dj = deriv.getPartial( cntj );
					final SymbolicElem<R,S> vi = vect.getVectorPotential( cnti );
					final SymbolicElem<R,S> vj = vect.getVectorPotential( cntj );
					SymbolicElem<R,S> elem = ( di.mult( vj ) ).add( ( dj.mult( vi ) ).negate() );
					tel.setVal( el , elem );
				}
			}
			
			return( tel );
		}

}


