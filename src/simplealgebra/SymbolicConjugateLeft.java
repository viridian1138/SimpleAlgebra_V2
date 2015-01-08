




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





package simplealgebra;

import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicElem;

import java.io.*;

/**
 * Symbolic elem for the left-side conjugate of a complex number.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicConjugateLeft<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The nested elem.
	 * @param _fac The factory for the nested elem.
	 */
	public SymbolicConjugateLeft( SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> _elem , ComplexElemFactory<R, S> _fac) 
	{
		super( _fac );
		elem = _elem;
	}

	
	@Override
	public ComplexElem<R, S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		ArrayList<ComplexElem<R,S>> args = new ArrayList<ComplexElem<R,S>>();
		return( elem.eval( implicitSpace ).handleOptionalOp( ComplexElem.ComplexCmd.CONJUGATE_LEFT , args ) );
	}

	
	@Override
	public ComplexElem<R, S> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}

	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "conjugateLeft( " );
		elem.writeString( ps );
		ps.print( " )" );
	}
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator<ComplexElem<R, S>, ComplexElemFactory<R, S>> pc,
			PrintStream ps) {
		ps.print( "<msup>" );
		if( pc.parenNeeded( this ,  elem , false ) )
		{
			ps.print( "<mfenced><mrow>" );
		}
		else
		{
			ps.print( "<mrow>" );
		}
		elem.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  elem , false ) )
		{
			ps.print( "</mrow></mfenced>" );
		}
		else
		{
			ps.print( "</mrow>" );
		}
		ps.print( "<mo>*L</mo></msup>" );
	}
	
	
	/**
	 * Returns the nested elem.
	 * 
	 * @return The nested elem.
	 */
	public SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> getElem() {
		return elem;
	}

	/**
	 * The nested elem.
	 */
	private SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> elem;

}

