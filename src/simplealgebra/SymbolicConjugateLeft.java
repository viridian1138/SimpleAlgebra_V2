




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
 * Symbolic elem for the left-side conjugate of a complex number defined as satisfying: <math display="block">
 * <mrow>
 *  <msup>
 *        <mrow>
 *          <mfenced open="(" close=")" separators=",">
 *            <mrow>
 *              <mi>a</mi>
 *              <mo>+</mo>
 *              <mi>i</mi>
 *              <mi>b</mi>
 *            </mrow>
 *          </mfenced>
 *        </mrow>
 *      <mrow>
 *        <mo>*</mo>
 *        <mo>L</mo>
 *      </mrow>
 *  </msup>
 *  <mfenced open="(" close=")" separators=",">
 *    <mrow>
 *      <mi>a</mi>
 *      <mo>+</mo>
 *      <mi>i</mi>
 *      <mi>b</mi>
 *    </mrow>
 *  </mfenced>
 *  <mo>=</mo>
 *  <msup>
 *          <mi>a</mi>
 *        <mn>2</mn>
 *  </msup>
 *  <mo>+</mo>
 *  <msup>
 *          <mi>b</mi>
 *        <mn>2</mn>
 *  </msup>
 * </mrow>
 * </math>
 *
 * <P> Note that in SimpleAlgebra this is not necessarily <math display="inline">
 * <mrow>
 *  <mi>a</mi>
 *  <mo>-</mo>
 *  <mi>i</mi>
 *  <mi>b</mi>
 * </mrow>
 * </math> because the product <math display="inline">
 * <mrow>
 *  <mi>a</mi>
 *  <mi>b</mi>
 * </mrow>
 * </math> does not necessarily commute.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
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
	public boolean evalSymbolicConstantApprox()
	{
		return( elem.evalSymbolicConstantApprox() );
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elem.exposesDerivatives() );
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

