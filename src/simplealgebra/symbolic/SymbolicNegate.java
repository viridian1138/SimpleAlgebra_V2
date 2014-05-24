




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

public class SymbolicNegate<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	public SymbolicNegate( SymbolicElem<R,S> _elem , S _fac )
	{
		super( _fac );
		elem = _elem;
	}
	
	public SymbolicNegate( SymbolicElem<R,S> _elem , S _fac , DroolsSession ds )
	{
		this( _elem , _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem.eval().negate() );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<Elem<?,?>> withRespectTo ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( elem.evalPartialDerivative( withRespectTo ).negate() );
	}

	@Override
	public String writeString( ) {
		return( "negate( " + ( elem.writeString() ) + " )" );
	}
	
	/**
	 * @return the elem
	 */
	public SymbolicElem<R, S> getElem() {
		return elem;
	}
	
	
	@Override
	public SymbolicElem<R, S> handleOptionalOp( Object id , ArrayList<SymbolicElem<R, S>> args ) throws NotInvertibleException
	{
		
		return( super.handleOptionalOp(id, args) );
	}
	
	
	@Override
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		if( b instanceof SymbolicNegate )
		{
			return( elem.symbolicEquals( ((SymbolicNegate<R,S>) b).getElem() ) );
		}
		
		return( false );
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elem.performInserts( session );
		super.performInserts( session );
	}


	private SymbolicElem<R,S> elem;

}

