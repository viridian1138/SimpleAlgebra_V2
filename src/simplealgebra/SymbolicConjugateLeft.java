




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

public class SymbolicConjugateLeft<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> 
{

	public SymbolicConjugateLeft( SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> _elem , ComplexElemFactory<R, S> _fac) 
	{
		super( _fac );
		elem = _elem;
	}

	
	@Override
	public ComplexElem<R, S> eval( HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		ArrayList<ComplexElem<R,S>> args = new ArrayList<ComplexElem<R,S>>();
		return( elem.eval( implicitSpace ).handleOptionalOp( ComplexElem.ComplexCmd.CONJUGATE_LEFT , args ) );
	}

	
	@Override
	public ComplexElem<R, S> evalPartialDerivative(
			ArrayList<Elem<?, ?>> withRespectTo , HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}

	
	@Override
	public String writeString( ) {
		return( "conjugateLeft( " + ( elem.writeString() ) + " )" );
	}
	
	
	/**
	 * @return the elem
	 */
	public SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> getElem() {
		return elem;
	}

	
	private SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> elem;
}

