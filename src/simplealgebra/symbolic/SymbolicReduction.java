




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

import simplealgebra.CloneThreadCache;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ddx.PartialDerivativeOp;

import java.io.*;
import java.math.BigInteger;


/**
 * A symbolic elem that reduces to a value.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
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
	public R evalCached( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ,
			HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( elem instanceof SymbolicElem )
		{
			if( evalSymbolicConstantApprox() )
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
	
	@Override
	public R evalPartialDerivativeCached( ArrayList<? extends Elem<?,?>> withRespectTo , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( elem instanceof SymbolicElem )
		{
			if( evalSymbolicConstantApprox() )
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
		return( evalSymbolicConstant( SymbolicElem.EVAL_MODE.APPROX ) );
	}
	
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		if( elem instanceof SymbolicElem )
		{
			SymbolicElem r = (SymbolicElem) elem;
			return( r.evalSymbolicConstantApprox() );
		}
		else
		{
			return( true );
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
	public SymbolicReduction<R,S> cloneThread( final BigInteger threadIndex )
	{
		final R elems = elem.cloneThread( threadIndex );
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elems != elem ) || ( facs != fac ) )
		{
			return( new SymbolicReduction<R,S>( elems , facs ) );
		}
		return( this );
	}
	
	
	@Override
	public SymbolicElem<R,S> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<SymbolicElem<R, S>, SymbolicElemFactory<R, S>> cache) {
		final SymbolicElem<R,S> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		final S facs = this.getFac().getFac().cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		final R elems = elem.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( ( elems != elem ) || ( facs != fac ) )
		{
			final SymbolicReduction<R,S> rtmp = new SymbolicReduction<R,S>( elems , facs );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
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

