





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

import java.util.HashSet;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Factory for Ricci tensors.
 * 
 * @author thorngreen
 *
 * @param <Z>
 * @param <U>
 * @param <R>
 * @param <S>
 * @param <K>
 */
public class RicciTensorFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> fac;
	TemporaryIndexFactory<Z> temp;
	RiemannTensorFactory<Z,U,R,S,K> riemann;
	
	
	
	public RicciTensorFactory( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , MetricTensorFactory<Z,R,S> _metric , 
			TemporaryIndexFactory<Z> _temp , OrdinaryDerivativeFactory<Z,U,R,S,K> _deriv )
	{
		fac = _fac;
		temp = _temp;
		riemann = new RiemannTensorFactory<Z,U,R,S,K>( _metric , _temp , _deriv );
	}
	
	
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getRicciTensor( Z alpha , Z beta )
	{
		final Z lambda = temp.getTemp();
		
		final Z lambdaB = temp.getTemp();
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			rie = riemann.getRiemannTensor(alpha, lambda, beta, lambdaB);
		
		final HashSet<Z> contravariantReduce = new HashSet<Z>();
		final HashSet<Z> covariantReduce = new HashSet<Z>();
		
		contravariantReduce.add( lambdaB );
		covariantReduce.add( lambda );
		
		SymbolicIndexReduction<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>> src = 
				new SymbolicIndexReduction<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>( rie , 
						fac , contravariantReduce , covariantReduce );
		
		return( src );
	}
	
	

}


