




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





package simplealgebra.ddx;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicElem;

/**
 * Implements a partial derivative as used in Calculus.
 * 
 * @author thorngreen
 *
 * @param <R>
 * @param <S>
 * @param <K>
 */
public class PartialDerivativeOp<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends DerivativeElem<R,S>
{

	public PartialDerivativeOp( S _fac , ArrayList<K> _withRespectTo )
	{
		super( _fac );
		withRespectTo = _withRespectTo;
	}
	
	@Override
	public R evalDerivative( SymbolicElem<R,S> in , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( in.evalPartialDerivative( withRespectTo , implicitSpace ) );
	}	

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "partialDerivative( " );
		Iterator<K> it = withRespectTo.iterator();
		while( it.hasNext() )
		{
			K nxt = it.next();
			if( nxt instanceof SymbolicElem )
			{
				( (SymbolicElem) nxt ).writeString( ps );
				ps.print( " " );
			}
			else
			{
				ps.print( nxt );
			}
		}
		ps.print( " )" );
	}
	
	@Override
	public void writeMathML( PrecedenceComparator<R,S> pc , PrintStream ps )
	{
		final int sz = withRespectTo.size();
		if( sz > 1 )
		{
			ps.print( "<mfrac><mrow><msup><mo>&part;</mo><mn>" );
			ps.print( "" + sz );
			ps.print( "</mn></msup></mrow><mrow>" );
		}
		else
		{
			ps.print( "<mfrac><mrow><mo>&part;</mo></mrow><mrow>" );
		}
		Iterator<K> it = withRespectTo.iterator();
		while( it.hasNext() )
		{
			K nxt = it.next();
			if( nxt instanceof SymbolicElem )
			{
				ps.print( "<mrow><mo>&part;</mo>" );
				( (SymbolicElem) nxt ).writeMathML( pc , ps );
				ps.print( "</mrow>" );
			}
			else
			{
				ps.print( "<mrow><mo>&part;</mo><mi>" );
				ps.print( nxt );
				ps.print( "</mi></mrow>" );
			}
		}
		ps.print( "</mrow></mfrac>" );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		if( b instanceof PartialDerivativeOp )
		{
			PartialDerivativeOp bp = (PartialDerivativeOp) b;
			if( withRespectTo.size() == bp.withRespectTo.size() )
			{
				for( int cnt = 0 ; cnt < withRespectTo.size() ; cnt++ )
				{
					final K elA = withRespectTo.get( cnt );
					final Object elB = bp.withRespectTo.get( cnt );
					if( ( elA instanceof SymbolicElem ) && ( elB instanceof SymbolicElem ) )
					{
						if( !( ( (SymbolicElem) elA ).symbolicEquals( (SymbolicElem) elB ) ) )
						{
							return( false );
						}
					}
					else
					{
						if( !( elA.equals( elB ) ) )
						{
							return( false );
						}
					}
				}
				
				return( true );
			}
		}
		
		return( false );
	}
	
	/**
	 * @return the withRespectTo
	 */
	public ArrayList<K> getWithRespectTo() {
		return withRespectTo;
	}
	
	private ArrayList<K> withRespectTo;

}

