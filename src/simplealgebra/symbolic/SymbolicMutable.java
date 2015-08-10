




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
import simplealgebra.MutableElem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;

import java.io.*;
import java.math.BigInteger;

/**
 * A symbolic elem. "A" that takes in another elem. "B" and mutates each enclosed
 * elem. "C" existing inside "B" by some mutator function.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The enclosed type inside the enclosed mutable elem.
 * @param <U> The enclosed mutable elem.
 * @param <R> The factory for the enclosed mutable elem.
 * @param <M> The input Mutator.
 */
public class SymbolicMutable<T extends Elem<T,?>, U extends MutableElem<T,U,?>, R extends ElemFactory<U,R>, M extends Mutator<U> > extends SymbolicElem<U,R> 
{
	
	/**
	 * The enclosed elem.
	 */
	private SymbolicElem<U,R> elemA;
	
	/**
	 * The mutator for the enclosed elem.
	 */
	private M elemB;

	
	/**
	 * Constructs the elem.
	 * 
	 * @param _elemA The enclosed elem.
	 * @param _elemB The mutator for the enclosed elem.
	 * @param _fac The factory for the enclosed elem.
	 */
	public SymbolicMutable( SymbolicElem<U,R> _elemA , M _elemB , R _fac )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	/**
	 * Constructs the elem. for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elemA The enclosed elem.
	 * @param _elemB The mutator for the enclosed elem.
	 * @param _fac The factory for the enclosed elem.
	 * @param ds The Drools session.
	 */
	public SymbolicMutable( SymbolicElem<U,R> _elemA , M _elemB , R _fac , DroolsSession ds )
	{
		this( _elemA , _elemB , _fac );
		ds.insert( this );
	}
	
	@Override
	public U eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		final U evl = elemA.eval( implicitSpace );
		final U mutr = elemB.mutate( evl );
		return( mutr );
	}
	
	@Override
	public U evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( ( elemA.exposesDerivatives() ) || ( elemB.exposesDerivatives() ) );
	}
	
	
	@Override
	public SymbolicMutable<T,U,R,M> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<U,R> elemAs = elemA.cloneThread( threadIndex );
		final M elemBs = (M)( elemB.cloneThread( threadIndex ) );
		final R facs = this.getFac().getFac().cloneThread( threadIndex );
		if( ( elemAs != elemA ) || ( elemBs != elemB ) || ( facs != this.getFac().getFac() ) )
		{
			return( new SymbolicMutable<T,U,R,M>( elemAs , elemBs , facs ) );
		}
		return( this );
	}
	

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( elemB.writeString( ) );
		ps.print( "( " );
		elemA.writeString( ps );
		ps.print( " )" );
	}
	
	
	
	/**
	 * Returns the enclosed elem.
	 * 
	 * @return The enclosed elem.
	 */
	public SymbolicElem<U, R> getElemA() {
		return elemA;
	}

	/**
	 * Returns the mutator for the enclosed elem.
	 * 
	 * @return The mutator for the enclosed elem.
	 */
	public M getElemB() {
		return elemB;
	}

	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elemA.performInserts( session );
		super.performInserts( session );
	}

}

