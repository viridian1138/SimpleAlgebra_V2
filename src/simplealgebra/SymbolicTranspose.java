




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
import java.util.HashMap;

import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;

public class SymbolicTranspose<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> 
{

	public SymbolicTranspose( SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> _elem , SquareMatrixElemFactory<U, R, S> _fac) 
	{
		super( _fac );
		elem = _elem;
	}

	
	@Override
	public SquareMatrixElem<U, R, S> eval( HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		ArrayList<SquareMatrixElem<U,R,S>> args = new ArrayList<SquareMatrixElem<U,R,S>>();
		return( elem.eval( implicitSpace ).handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.TRANSPOSE , args ) );
	}

	
	@Override
	public SquareMatrixElem<U, R, S> evalPartialDerivative(
			ArrayList<Elem<?, ?>> withRespectTo , HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}

	
	@Override
	public String writeString( ) {
		return( "transpose( " + ( elem.writeString() ) + " )" );
	}
	
	
	/**
	 * @return the elem
	 */
	public SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> getElem() {
		return elem;
	}

	
	private SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elem;
}

