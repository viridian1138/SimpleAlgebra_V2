



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
import java.util.HashSet;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;

public class SymbolicAdd<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	public SymbolicAdd( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	public SymbolicAdd( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac , DroolsSession ds )
	{
		this( _elemA , _elemB , _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elemA.eval( implicitSpace ).add( elemB.eval( implicitSpace ) ) );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<Elem<?,?>> withRespectTo , HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( elemA.evalPartialDerivative( withRespectTo , implicitSpace ).add( elemB.evalPartialDerivative( withRespectTo , implicitSpace ) ) );
	}

	@Override
	public String writeString( ) {
		return( "add( " + ( elemA.writeString() ) + " , " + ( elemB.writeString() ) + " )" );
	}
	
	
	/**
	 * @return the elemA
	 */
	public SymbolicElem<R, S> getElemA() {
		return elemA;
	}

	/**
	 * @return the elemB
	 */
	public SymbolicElem<R, S> getElemB() {
		return elemB;
	}
	
	
	@Override
	public SymbolicElem<R, S> handleOptionalOp( Object id , ArrayList<SymbolicElem<R, S>> args ) throws NotInvertibleException
	{
		
		return( super.handleOptionalOp(id, args) );
	}
	
	
	@Override
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		if( b instanceof SymbolicAdd )
		{
			boolean aa = this.getElemA().symbolicEquals( ((SymbolicAdd<R,S>) b).getElemA() );
			boolean bb = this.getElemB().symbolicEquals( ((SymbolicAdd<R,S>) b).getElemB() );
			if( aa && bb )
			{
				return( true );
			}
				
			aa = this.getElemA().symbolicEquals( ((SymbolicAdd<R,S>) b).getElemB() );
			bb = this.getElemB().symbolicEquals( ((SymbolicAdd<R,S>) b).getElemA() );
			return( aa && bb );
		}
		
		return( false );
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elemA.performInserts( session );
		elemB.performInserts( session );
		super.performInserts( session );
	}
	
	
	
	public SymbolicAdd<R, S> handleAddSimplify( SymbolicElem<R, S> elA , SymbolicElem<R, S> elB , DroolsSession ds )
	{
		HashSet<SymbolicElem<R, S>> elS = new HashSet<SymbolicElem<R, S>>();
		elS.add( elA );
		elS.add( elB );
		SymbolicAdd<R, S> ret = this.handleAddSimplify( elS , ds );
		if( !( elS.isEmpty() ) )
		{
			throw( new RuntimeException( "Internal Error." ) );
		}
		return( ret );
	}
	
	
	
	public SymbolicAdd<R, S> handleAddSimplify( HashSet<SymbolicElem<R, S>> elS , DroolsSession ds )
	{
		SymbolicElem<R,S> elA = elemA;
		SymbolicElem<R,S> elB = elemB;
		if( elS.contains( elA ) )
		{
			elS.remove( elA );
			elA = this.getFac().zero();
			ds.insert( elA );
		}
		else
		{
			if( elA instanceof SymbolicAdd )
			{
				elA = ((SymbolicAdd) elA).handleAddSimplify( elS , ds );
			}
		}
		
		if( elS.contains( elB ) )
		{
			elS.remove( elB );
			elB = this.getFac().zero();
			ds.insert( elB );
		}
		else
		{
			if( elB instanceof SymbolicAdd )
			{
				elB = ((SymbolicAdd) elB).handleAddSimplify( elS , ds );
			}
		}
		
		SymbolicAdd<R, S> ret = new SymbolicAdd( elA , elB , fac );
		// System.out.println( "Insert: " + ret );
		ds.insert( ret );
		return( ret );
	}
	
	

	private SymbolicElem<R,S> elemA;
	private SymbolicElem<R,S> elemB;

}

