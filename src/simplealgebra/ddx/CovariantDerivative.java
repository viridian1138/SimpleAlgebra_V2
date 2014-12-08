




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
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Implements a covariant derivative operator as used in General Relativity.
 * 
 * @author thorngreen
 *
 * @param <Z>
 * @param <U>
 * @param <R>
 * @param <S>
 * @param <K>
 */
public class CovariantDerivative<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
		extends DerivativeElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
{

	public CovariantDerivative( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
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
	public String writeString( ) {
		return( "covariantDerivative" );
	}
	
	
	
	private SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> tensorWithRespectTo;
	private Z derivativeIndex;
	private TemporaryIndexFactory<Z> temp;
	private MetricTensorFactory<Z,R,S> metric;
	private OrdinaryDerivativeFactory<Z,U,R,S,K> odfac;
	
	

}

