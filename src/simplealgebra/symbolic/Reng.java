



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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.WriteElemCache;


/**
 * Rule engine node.  Used to represent a symbolic refactoring in Drools ( http://drools.org ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class Reng<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	/**
	 * Constructs the node.
	 * 
	 * @param _strt The starting point of the refactoring.
	 * @param _end The result of the refactoring.
	 */
	public Reng( SymbolicElem<R, S> _strt , SymbolicElem<R, S> _end )
	{
		strt = _strt;
		end = _end;
	}
	
	/**
	 * Gets the starting point of the refactoring.
	 * 
	 * @return The starting point of the refactoring.
	 */
	public SymbolicElem<R, S> getStrt() {
		return strt;
	}
	
	/**
	 * Gets the result of the refactoring.
	 * 
	 * @return The result of the refactoring.
	 */
	public SymbolicElem<R, S> getEnd() {
		return end;
	}
	
	/**
	 * Writes the Reng to a PrintStream.
	 * 
	 * @param ps The print stream.
	 * @return String corresponding to the Reng.
	 */
	public String writeDesc( PrintStream ps )
	{
		final WriteElemCache<SymbolicElem<R, S>, SymbolicElemFactory<R, S>> cache = getStrt().getFac().generateWriteElemCache();
		final String a1 = getStrt().writeDesc( cache , ps );
		final String a2 = getEnd().writeDesc( cache , ps );
		final String a3 = cache.getIncrementVal();
		ps.print( Reng.class.getSimpleName() );
		getStrt().getFac().writeOrdinaryEnclosedType(ps);
		ps.print( " " );
		ps.print( a3 );
		ps.print( " = new " );
		ps.print( Reng.class.getSimpleName() );
		getStrt().getFac().writeOrdinaryEnclosedType(ps);
		ps.print( "( " );
		ps.print( a1 );
		ps.print( " , " );
		ps.print( a2 );
		ps.println( " );" );
		return( a3 );
	}
	
	/**
	 * The starting point of the refactoring.
	 */
	private SymbolicElem<R,S> strt;
	
	/**
	 * The result of the refactoring.
	 */
	private SymbolicElem<R,S> end;

}



