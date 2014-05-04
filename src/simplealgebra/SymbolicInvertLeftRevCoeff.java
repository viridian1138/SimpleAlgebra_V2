



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




package simplealgebra;

import java.util.ArrayList;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.*;


public class SymbolicInvertLeftRevCoeff<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> 
{

	public SymbolicInvertLeftRevCoeff( SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> _elem , 
			SquareMatrixElemFactory<U, R, S> _fac )
	{
		super( _fac );
		elem = _elem;
	}
	
	@Override
	public SquareMatrixElem<U, R, S> eval( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		ArrayList<SquareMatrixElem<U, R, S>> args = new ArrayList<SquareMatrixElem<U, R, S>>();
		return( elem.eval().handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.INVERT_LEFT_REV_COEFF , args) );
	}
	
	@Override
	public SquareMatrixElem<U, R, S> evalPartialDerivative(ArrayList<Elem<?, ?>> withRespectTo)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}

	@Override
	public String writeString( ) {
		return( "invertLeftRevCoeff( " + ( elem.writeString() ) + " )" );
	}
	
	
	/**
	 * @return the elemA
	 */
	public SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> getElem() {
		return elem;
	}
	

	private SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elem;

}

