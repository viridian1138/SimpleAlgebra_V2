




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





package simplealgebra.symbolic;

import java.util.ArrayList;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.MutableElem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;

public class SymbolicMutable<T extends Elem<T,?>, U extends MutableElem<T,U,?>, R extends ElemFactory<U,R> > extends SymbolicElem<U,R> 
{
	
	private SymbolicElem<U,R> elemA;
	private Mutator<U> elemB;

	
	public SymbolicMutable( SymbolicElem<U,R> _elemA , Mutator<U> _elemB , R _fac )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	@Override
	public U eval( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		final U evl = elemA.eval();
		final U mutr = elemB.mutate( evl );
		return( mutr );
	}
	
	@Override
	public U evalPartialDerivative(ArrayList<Elem<?, ?>> withRespectTo)
			throws NotInvertibleException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}

	@Override
	public String writeString( ) {
		return( elemB.writeString() + "( " + elemA.writeString() + " )" );
	}

}

