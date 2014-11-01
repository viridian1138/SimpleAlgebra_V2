




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

import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ddx.PartialDerivativeOp;


public class SymbolicReduction<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	public SymbolicReduction( R _elem , S _fac )
	{
		super( _fac );
		elem = _elem;
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( elem instanceof SymbolicElem )
		{
			if( isSymbolicElemSimpleConst( elem ) )
			{
				return( fac.zero() );
			}
			else
			{
				final R partialD = (R)( new PartialDerivativeOp( ( (SymbolicElemFactory) fac ).getFac() , withRespectTo ) );
				return( partialD.mult( elem ) );
			}
		}
		return( fac.zero() );
	}
	
	
	protected boolean isSymbolicElemSimpleConst( Elem elem )
	{
		SymbolicElem e = (SymbolicElem) elem;
		if( !( e instanceof SymbolicReduction ) )
		{
			return( false );
		}
		else
		{
			Elem r = ( (SymbolicReduction) e ).getElem();
			if( r instanceof SymbolicElem )
			{
				return( isSymbolicElemSimpleConst( r ) );
			}
			else
			{
				return( true );
			}
		}
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
	public boolean symbolicEquals( SymbolicElem<R,S> b )
	{
		if( ( b instanceof SymbolicReduction ) && ( elem instanceof SymbolicElem ) )
		{
			return( ( (SymbolicElem) elem ).symbolicEquals( (SymbolicElem)( ( (SymbolicReduction) b ).getElem() ) ) );
		}
		return( false );
	}
	
	
	@Override
	public boolean equals( Object b )
	{
		if( b instanceof SymbolicReduction )
		{
			return( this.symbolicEquals( (SymbolicReduction) b ) );
		}
		return( false );
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

