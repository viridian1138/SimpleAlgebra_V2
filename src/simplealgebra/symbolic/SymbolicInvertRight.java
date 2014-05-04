




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
import simplealgebra.NotInvertibleException;

public class SymbolicInvertRight<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	public SymbolicInvertRight( SymbolicElem<R,S> _elem , S _fac )
	{
		super( _fac );
		elem = _elem;
	}
	
	@Override
	public R eval( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem.eval().invertRight() );
	}
	
	@Override
	public R evalPartialDerivative(ArrayList<Elem<?, ?>> withRespectTo)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}

	@Override
	public String writeString( ) {
		return( "invertRight( " + ( elem.writeString() ) + " )" );
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
		if( id instanceof SymbolicOps )
		{
			switch( (SymbolicOps) id )
			{
				case DISTRIBUTE_SIMPLIFY:
				{
					SymbolicInvertRight<R,S> ths = this;
					SymbolicElem<R,S> r = elem.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
					if( elem != r )
					{
						ths = new SymbolicInvertRight<R,S>( r , fac );
					}
					
					if( ths.elem instanceof SymbolicNegate )
					{
						// !!!!!!!!
					}
					
					return( ths );
				}
				// break;
			}
		}
		
		return( super.handleOptionalOp(id, args) );
	}
	
	
	@Override
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		if( b instanceof SymbolicInvertRight )
		{
			return( elem.symbolicEquals( ((SymbolicInvertRight<R,S>) b).getElem() ) );
		}
		
		return( false );
	}
	
	
	private SymbolicElem<R,S> elem;

}

