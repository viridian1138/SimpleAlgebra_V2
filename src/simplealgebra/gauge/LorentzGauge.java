






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








package simplealgebra.gauge;

import java.math.BigInteger;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.et.AlteredCSquared;
import simplealgebra.et.VectorPotentialFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;



public class LorentzGauge<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends Gauge<R, S> {
	
	SymbolicElemFactory<R, S> fac;
	AlteredCSquared<R, S> cSq;
	DirectionalDerivativePartialFactory<R, S, K> deriv;
	VectorPotentialFactory<R, S> vect;
	
	public LorentzGauge( SymbolicElemFactory<R, S> _fac , AlteredCSquared<R, S> _cSq ,
			DirectionalDerivativePartialFactory<R, S, K> _deriv,
			VectorPotentialFactory<R, S> _vect )
	{
		fac = _fac;
		cSq = _cSq;
		deriv = _deriv;
		vect = _vect;
	}
	
	@Override
	public SymbolicElem<R, S> getGauge( BigInteger numElem ) throws NotInvertibleException
	{
		SymbolicElem<R,S> ret = fac.zero();
		
		{
			final PartialDerivativeOp<R,S,K> d = deriv.getPartial( BigInteger.ZERO );
			final SymbolicElem<R,S> v = vect.getVectorPotential( BigInteger.ZERO );
			ret = ret.add( ( d.mult( v ) ).mult( cSq.getAlteredCSquared( false ).negate() ) );
		}
		
		for( BigInteger cnti = BigInteger.ONE ; cnti.compareTo(numElem) < 0 ; cnti = cnti.add( BigInteger.ONE ) )
		{
			final PartialDerivativeOp<R,S,K> d = deriv.getPartial( cnti );
			final SymbolicElem<R,S> v = vect.getVectorPotential( cnti );
			ret = ret.add( d.mult( v ) );
		}
		
		return( ret );
	}

}

