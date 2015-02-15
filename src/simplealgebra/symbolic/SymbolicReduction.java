




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

import java.io.*;


/**
 * A symbolic elem that reduces to a value.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicReduction<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the reduction.
	 * 
	 * @param _elem The enclosed elem.
	 * @param _fac The enclosed factory.
	 */
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
	
	
	/**
	 * Returns true if the partial derivative of the elem reduces to zero.
	 * 
	 * @param withRespectTo The term with which to take the partial derivative with respect to.
	 * @return true if the partial derivative of the elem reduces to zero.
	 */
	public boolean partialDerivativeReducesToZero( ArrayList<Elem<?,?>> withRespectTo )
	{
		if( elem instanceof SymbolicElem )
		{
			if( isSymbolicElemSimpleConst( elem ) )
			{
				return( true );
			}
			else
			{
				return( false );
			}
		}
		return( true );
	}
	
	
	/**
	 * Returns true iff. the parameter is a simple constant.
	 * 
	 * @param elem The parameter to test.
	 * @return True iff. the parameter is a simple constant.
	 */
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
	 * Gets the enclosed elem.
	 * 
	 * @return The enclosed elem.
	 */
	public R getElem() {
		return elem;
	}
	
	
	/**
	 * Sets the enclosed elem.
	 * 
	 * @param _elem The enclosed elem.
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
	public boolean exposesDerivatives()
	{
		if( elem instanceof SymbolicElem )
		{
			( (SymbolicElem) elem ).exposesDerivatives( );
		}
		return( false );
	}
	
	
	
	@Override
	public boolean isPartialDerivativeZero()
	{
		if( elem instanceof SymbolicElem )
		{
			( (SymbolicElem) elem ).isPartialDerivativeZero( );
		}
		return( true );
	}
	
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "reduction( " );
		if( elem instanceof SymbolicElem )
		{
			( (SymbolicElem) elem ).writeString( ps );
		}
		ps.print( " )" );
	}


	/**
	 * The enclosed elem.
	 */
	private R elem;

}

