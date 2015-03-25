



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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;

/**
 * Symbolic elem. for performing addition.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicAdd<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elemA The left argument of the addition.
	 * @param _elemB The right argument of the addition.
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicAdd( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elemA The left argument of the addition.
	 * @param _elemB The right argument of the addition.
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public SymbolicAdd( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac , DroolsSession ds )
	{
		this( _elemA , _elemB , _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elemA.eval( implicitSpace ).add( elemB.eval( implicitSpace ) ) );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( elemA.evalPartialDerivative( withRespectTo , implicitSpace ).add( elemB.evalPartialDerivative( withRespectTo , implicitSpace ) ) );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( ( elemB.exposesDerivatives() ) || ( elemA.exposesDerivatives() ) );
	}

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "add( " );
		elemA.writeString( ps );
		ps.print( " , " );
		elemB.writeString( ps );
		ps.print( " )" );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator<R,S> pc , PrintStream ps )
	{
		if( pc.parenNeeded( this ,  elemA , false ) )
		{
			ps.print( "<mfenced><mrow>" );
		}
		else
		{
			ps.print( "<mrow>" );
		}
		elemA.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  elemA , false ) )
		{
			ps.print( "</mrow></mfenced>" );
		}
		else
		{
			ps.print( "</mrow>" );
		}
		ps.print( "<mo>+</mo>" );
		if( pc.parenNeeded( this ,  elemB , true ) )
		{
			ps.print( "<mfenced><mrow>" );
		}
		else
		{
			ps.print( "<mrow>" );
		}
		elemB.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  elemB , true ) )
		{
			ps.print( "</mrow></mfenced>" );
		}
		else
		{
			ps.print( "</mrow>" );
		}
	}
	
	
	/**
	 * Returns the left argument of the addition.
	 * 
	 * @return The left argument of the addition.
	 */
	public SymbolicElem<R, S> getElemA() {
		return elemA;
	}

	/**
	 * Returns the right argument of the addition.
	 * 
	 * @return The right argument of the addition.
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
	
	
	
	/**
	 * Simplifies the elem, where two elems in the underlying negation true are known to be negations of each other.
	 * 
	 * @param elA An elem in the addition tree under this elem.
	 * @param elB An elem in the addition tree under this elem which is the negation of elA.
	 * @param ds The Drools ( http://drools.org ) session in which to simplify the addition.
	 * @return The simplified expression, with elA and elB replaced by zero.
	 */
	public SymbolicElem<R, S> handleAddSimplify( SymbolicElem<R, S> elA , SymbolicElem<R, S> elB , DroolsSession ds )
	{
		HashSet<SymbolicElem<R, S>> elS = new HashSet<SymbolicElem<R, S>>();
		elS.add( elA );
		elS.add( elB );
		SymbolicElem<R, S> ret = this.handleAddSimplify( elS , ds );
		if( !( elS.isEmpty() ) )
		{
			throw( new RuntimeException( "Internal Error." ) );
		}
		return( ret );
	}
	
	
	/**
	 * Simplifies the addition of a set of elems.
	 * 
	 * @param elS The set of elems that are to be replaced by zero.
	 * @param ds The Drools ( http://drools.org ) session in which to simplify the addition.
	 * @return The simplified expression.
	 */
	public SymbolicElem<R, S> handleAddSimplify( HashSet<SymbolicElem<R, S>> elS , DroolsSession ds )
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
		
		SymbolicElem<R, S> ret = elA.add( elB );
		// System.out.println( "Insert: " + ret );
		ds.insert( ret );
		return( ret );
	}
	
	

	/**
	 * The left argument of the addition.
	 */
	private SymbolicElem<R,S> elemA;
	
	/**
	 * The right argument of the addition.
	 */
	private SymbolicElem<R,S> elemB;

}

