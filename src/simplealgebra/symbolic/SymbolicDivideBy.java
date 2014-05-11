



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

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;

public class SymbolicDivideBy<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	public SymbolicDivideBy( SymbolicElem<R,S> _elem , S _fac , int _ival )
	{
		super( _fac );
		elem = _elem;
		ival = _ival;
	}
	
	@Override
	public R eval( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem.eval().divideBy( ival ) );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<Elem<?,?>> withRespectTo ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( elem.evalPartialDerivative( withRespectTo ).divideBy( ival ) );
	}

	@Override
	public String writeString( ) {
		return( "divideBy( " + ( elem.writeString() ) + " , " + ( ival ) + " )" );
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
	
	private int ival;
	private SymbolicElem<R,S> elem;

}

