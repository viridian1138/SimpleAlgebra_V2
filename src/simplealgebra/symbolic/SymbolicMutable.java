




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


public class SymbolicMutable<T extends Elem<T,?>, U extends MutableElem<T,U,?>, R extends ElemFactory<U,R> > extends SymbolicElem<U,R> 
{
	
	private SymbolicElem<U,R> elemA;
	private Mutator<U> elemB;

	
	public SymbolicMutable( SymbolicElem<U,R> _elemA , Mutator<U> _elemB , R _fac )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	public SymbolicMutable( SymbolicElem<U,R> _elemA , Mutator<U> _elemB , R _fac , DroolsSession ds )
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
	public void writeString( PrintStream ps ) {
		ps.print( elemB.writeString( ) );
		ps.print( "( " );
		elemA.writeString( ps );
		ps.print( " )" );
	}
	
	
	
	/**
	 * @return the elemA
	 */
	public SymbolicElem<U, R> getElemA() {
		return elemA;
	}

	/**
	 * @return the elemB
	 */
	public Mutator<U> getElemB() {
		return elemB;
	}

	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elemA.performInserts( session );
		super.performInserts( session );
	}

}

