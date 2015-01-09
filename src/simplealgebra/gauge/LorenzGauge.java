






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
import simplealgebra.et.AlteredCSquared;
import simplealgebra.et.VectorPotentialFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;



/**
 * Factory for returning the components of the Lorenz gauge
 * defined by <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>u</mi>
 *  </msub>
 *  <msup>
 *          <mi>A</mi>
 *        <mi>u</mi>
 *  </msup>
 *  <mo>=</mo>
 *  <mn>0</mn>
 * </mrow>
 * </math>
 * 
 * <P> See http://en.wikipedia.org/wiki/Lorenz_gauge_condition
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <K> The type against which to take derivatives.
 */
public class LorenzGauge<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends Gauge<R, S> {
	
	/**
	 * The factory for the enclosed type.
	 */
	SymbolicElemFactory<R, S> fac;
	
	/**
	 * Node representing C-squared.
	 */
	AlteredCSquared<R, S> cSq;
	
	/**
	 * The directional derivative.
	 */
	DirectionalDerivativePartialFactory<R, S, K> deriv;
	
	/**
	 * The vector potential.
	 */
	VectorPotentialFactory<R, S> vect;
	
	/**
	 * Constructs the node.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _cSq Node representing C-squared.
	 * @param _deriv The directional derivative.
	 * @param _vect The vector potential.
	 */
	public LorenzGauge( SymbolicElemFactory<R, S> _fac , AlteredCSquared<R, S> _cSq ,
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
			final SymbolicElem<R,S> d = deriv.getPartial( BigInteger.ZERO );
			final SymbolicElem<R,S> v = vect.getVectorPotential( BigInteger.ZERO );
			ret = ret.add( ( d.mult( v ) ).mult( cSq.getAlteredCSquared( false ).negate() ) );
		}
		
		for( BigInteger cnti = BigInteger.ONE ; cnti.compareTo(numElem) < 0 ; cnti = cnti.add( BigInteger.ONE ) )
		{
			final SymbolicElem<R,S> d = deriv.getPartial( cnti );
			final SymbolicElem<R,S> v = vect.getVectorPotential( cnti );
			ret = ret.add( d.mult( v ) );
		}
		
		return( ret );
	}

}

