




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
import java.util.HashMap;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;

public class SymbolicReduction<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	public SymbolicReduction( R _elem , S _fac )
	{
		super( _fac );
		elem = _elem;
	}
	
	@Override
	public R eval( HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<Elem<?,?>> withRespectTo , HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( elem instanceof SymbolicElem )
		{
			final Elem<?,?> r = ( (SymbolicElem<?,?>) elem ).evalPartialDerivative( withRespectTo , implicitSpace );
			return( (R)( new SymbolicReduction( r , fac ) ) );
		}
		return( fac.zero() );
	}
	
	/**
	 * @return the elem
	 */
	public R getElem() {
		return elem;
	}
	
	
	/**
	 * 
	 * @param _elem
	 */
	public void setElem( R _elem ) {
		elem  = _elem;
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
					SymbolicReduction<R,S> ths = this;
					return( ths );
				}
				// break;
			}
		}
		
		return( super.handleOptionalOp(id, args) );
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		if( elem instanceof SymbolicElem )
		{
			( (SymbolicElem) elem ).performInserts( session );
		}
		super.performInserts( session );
	}
	
	
	@Override
	public String writeString( ) {
		String s = "reduction(";
		if( elem instanceof SymbolicElem )
		{
			s = s + ( (SymbolicElem) elem ).writeString();
		}
		s = s + ")";
		return( s );
	}


	private R elem;

}

