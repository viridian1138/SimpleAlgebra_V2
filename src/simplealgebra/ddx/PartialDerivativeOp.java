




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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;

/**
 * Implements a partial derivative as used in Calculus.
 * 
 * @author thorngreen
 *
 * @param <R>
 * @param <S>
 * @param <K>
 */
public class PartialDerivativeOp<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends DerivativeElem<R,S>
{

	public PartialDerivativeOp( S _fac , ArrayList<K> _withRespectTo )
	{
		super( _fac );
		withRespectTo = _withRespectTo;
	}
	
	@Override
	public R evalDerivative( SymbolicElem<R,S> in ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( in.evalPartialDerivative( (ArrayList<Elem<?, ?>>) withRespectTo ) );
	}	

	@Override
	public String writeString( ) {
		return( "partialDerivative( " + ( withRespectTo ) + " )" );
	}
	
	/**
	 * @return the withRespectTo
	 */
	public ArrayList<K> getWithRespectTo() {
		return withRespectTo;
	}
	
	private ArrayList<K> withRespectTo;

}

