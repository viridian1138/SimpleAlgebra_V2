





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
public class FlatMetricTensorFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	SymbolicElemFactory<R, S> fac;
	SymbolicElem<R, S> cSquared;
	
	public FlatMetricTensorFactory( SymbolicElemFactory<R, S> _fac , SymbolicElem<R, S> _cSquared )
	{
		fac = _fac;
		cSquared = _cSquared;
	}
	
	
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
		getMetricTensor( Z index0 , Z index1 )
		{
		
			ArrayList<Z> contravariantIndices = new ArrayList<Z>();
			ArrayList<Z> covariantIndices = new ArrayList<Z>();
			
			
			covariantIndices.add( index0 );
			covariantIndices.add( index1 );
			
		
			EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> tel =
				new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( fac , contravariantIndices , covariantIndices );
			
			{
				ArrayList<BigInteger> el = new ArrayList<BigInteger>();
				el.add( BigInteger.ZERO );
				el.add( BigInteger.ZERO );
				tel.setVal( el , cSquared.negate() );
			}
			
			{
				ArrayList<BigInteger> el = new ArrayList<BigInteger>();
				el.add( BigInteger.ONE );
				el.add( BigInteger.ONE );
				tel.setVal( el , fac.identity() );
			}
			
			{
				ArrayList<BigInteger> el = new ArrayList<BigInteger>();
				el.add( BigInteger.valueOf( 2 ) );
				el.add( BigInteger.valueOf( 2 ) );
				tel.setVal( el , fac.identity() );
			}
			
			{
				ArrayList<BigInteger> el = new ArrayList<BigInteger>();
				el.add( BigInteger.valueOf( 3 ) );
				el.add( BigInteger.valueOf( 3 ) );
				tel.setVal( el , fac.identity() );
			}
			
			return( tel );
		}

}


