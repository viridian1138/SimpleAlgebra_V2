




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





package simplealgebra.tolerant;

import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.PrimitiveRandom;

/**
 * Default factory for generating tolerant results for DoubleElem.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 */
public class DefaultDoubleElemTolerantResultFactory extends
		TolerantResultFactory<DoubleElem, DoubleElemFactory, DefaultDoubleElemTolerantResultFactory> {

	@Override
	public DoubleElem getTolerantInvertLeft(DoubleElem in, Exception ex)
			throws NotInvertibleException {
		if( in.getVal() >= 0.0 )
		{
			return( new DoubleElem( Double.MAX_VALUE ) );
		}
		else
		{
			return( new DoubleElem( -Double.MAX_VALUE ) );
		}
	}

	@Override
	public DoubleElem getTolerantInvertRight(DoubleElem in, Exception ex)
			throws NotInvertibleException {
		if( in.getVal() >= 0.0 )
		{
			return( new DoubleElem( Double.MAX_VALUE ) );
		}
		else
		{
			return( new DoubleElem( -Double.MAX_VALUE ) );
		}
	}

	@Override
	public DoubleElem getTolerantDivideBy(DoubleElem in, BigInteger val,
			Exception ex) {
		if( in.getVal() >= 0.0 )
		{
			return( new DoubleElem( Double.MAX_VALUE ) );
		}
		else
		{
			return( new DoubleElem( -Double.MAX_VALUE ) );
		}
	}

	@Override
	public Elem<?, ?> getTolerantTotalMagnitude(DoubleElem in, Exception ex) {
		return( new DoubleElem( Double.MAX_VALUE ) );
	}

	@Override
	public DoubleElem getTolerantAdd(DoubleElem a, DoubleElem b, Exception ex) {
		if( ( a.getVal() >= 0.0 ) || ( b.getVal() >= 0.0 ) )
		{
			return( new DoubleElem( Double.MAX_VALUE ) );
		}
		else
		{
			return( new DoubleElem( -Double.MAX_VALUE ) );
		}
	}

	@Override
	public DoubleElem getTolerantMult(DoubleElem a, DoubleElem b, Exception ex) {
		final int av = a.getVal() >= 0.0 ? 1 : -1;
		final int bv = b.getVal() >= 0.0 ? 1 : -1;
		final int mulv = av * bv;
		if( mulv > 0 )
		{
			return( new DoubleElem( Double.MAX_VALUE ) );
		}
		else
		{
			return( new DoubleElem( -Double.MAX_VALUE ) );
		}
	}

	@Override
	public DoubleElem getTolerantNegate(DoubleElem a, Exception ex) {
		if( a.getVal() <= 0.0 )
		{
			return( new DoubleElem( Double.MAX_VALUE ) );
		}
		else
		{
			return( new DoubleElem( -Double.MAX_VALUE ) );
		}
	}

	@Override
	public DoubleElem getTolerantOptionalOp(DoubleElem value, Object id,
			ArrayList<DoubleElem> args, Exception ex)
			throws NotInvertibleException {
		return( new DoubleElem( Double.MAX_VALUE ) );
	}

	@Override
	public DoubleElem getTolerantMutate(DoubleElem value,
			Mutator<DoubleElem> mutr, Exception ex)
			throws NotInvertibleException {
		return( new DoubleElem( Double.MAX_VALUE ) );
	}

	@Override
	public DoubleElem getTolerantRandom(DoubleElem value, PrimitiveRandom in,
			Exception ex) {
		return( new DoubleElem( Double.MAX_VALUE ) );
	}

	
}

