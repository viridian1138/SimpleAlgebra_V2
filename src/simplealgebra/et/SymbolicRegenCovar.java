



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

import java.util.ArrayList;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;


public class SymbolicRegenCovar<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> 
{

	public SymbolicRegenCovar( SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> _elem , 
			EinsteinTensorElemFactory<Z,R,S> _fac , ArrayList<Z> _newCovar )
	{
		super( _fac );
		elem = _elem;
		newCovar = _newCovar;
	}
	
	@Override
	public EinsteinTensorElem<Z,R,S> eval( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem.eval().regenCovar( newCovar ) );
	}
	
	@Override
	public EinsteinTensorElem<Z,R,S> evalPartialDerivative( ArrayList<Elem<?,?>> withRespectTo ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( elem.evalPartialDerivative( withRespectTo ).regenCovar( newCovar ) );
	}

	@Override
	public String writeString( ) {
		return( "symbolicRegenCovar" );
	}
	
	@Override
	public void performInserts( StatefulKnowledgeSession session , int levels )
	{
		if( levels >= 0 )
		{
			elem.performInserts( session , levels - 1 );
			super.performInserts( session , levels );
		}
	}
	
	private ArrayList<Z> newCovar;
	private SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> elem;

}

