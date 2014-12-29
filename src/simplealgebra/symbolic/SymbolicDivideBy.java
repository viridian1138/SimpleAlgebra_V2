



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

import java.io.*;


public class SymbolicDivideBy<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	public SymbolicDivideBy( SymbolicElem<R,S> _elem , S _fac , int _ival )
	{
		super( _fac );
		elem = _elem;
		ival = _ival;
	}
	
	public SymbolicDivideBy( SymbolicElem<R,S> _elem , S _fac , int _ival , DroolsSession ds )
	{
		this( _elem , _fac , _ival );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem.eval( implicitSpace ).divideBy( ival ) );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( elem.evalPartialDerivative( withRespectTo , implicitSpace ).divideBy( ival ) );
	}

	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "divideBy( " );
		elem.writeString( ps );
		ps.print( " , " + ( ival ) + " )" );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator<R,S> pc , PrintStream ps )
	{
		ps.print( "<mfrac><mrow>" );
		elem.writeMathML(pc, ps);
		ps.print( "</mrow><mrow><mn>" + ( ival ) );
		ps.print( "</mn></mrow>" );
		ps.print( "</mfrac>" );
	}
	
	
	
	/**
	 * @return the ival
	 */
	public int getIval() {
		return ival;
	}

	/**
	 * @return the elem
	 */
	public SymbolicElem<R, S> getElem() {
		return elem;
	}

	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elem.performInserts( session );
		super.performInserts( session );
	}
	
	private int ival;
	private SymbolicElem<R,S> elem;

}

