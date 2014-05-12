



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

public class SymbolicAdd<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	public SymbolicAdd( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	@Override
	public R eval( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elemA.eval().add( elemB.eval() ) );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<Elem<?,?>> withRespectTo ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( elemA.evalPartialDerivative( withRespectTo ).add( elemB.evalPartialDerivative( withRespectTo ) ) );
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
		if( id instanceof SymbolicOps )
		{
			switch( (SymbolicOps) id )
			{
				case DISTRIBUTE_SIMPLIFY:
				{
					SymbolicElem<R,S> ra = elemA.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
					SymbolicElem<R,S> rb = elemB.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
					
					
					StatefulKnowledgeSession session = getDistributeSimplifyKnowledgeBase().newStatefulKnowledgeSession();
					
					SymbolicPlaceholder<R,S> place = new SymbolicPlaceholder<R,S>( 
							( elemA != ra ) || ( elemB != rb ) ?
							new SymbolicAdd<R,S>( ra , rb , fac ) : this , fac );
					
					place.performInserts( session , 5 );
					
					session.fireAllRules();
						
					return( place.getElem() );
				}
				// break;
			}
		}
		
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
	public void performInserts( StatefulKnowledgeSession session , int levels )
	{
		if( levels >= 0 )
		{
			elemA.performInserts( session , levels - 1 );
			elemB.performInserts( session , levels - 1 );
			super.performInserts( session , levels );
		}
	}
	

	private SymbolicElem<R,S> elemA;
	private SymbolicElem<R,S> elemB;

}

