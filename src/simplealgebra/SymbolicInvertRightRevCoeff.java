



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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.*;


/**
 * Symbolic elem for the right-side inverse of a square matrix where the
 * enclosed elements in the matrix are multiplied in reverse-order.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the matrix.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicInvertRightRevCoeff<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The enclosed elem.
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicInvertRightRevCoeff( SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> _elem , 
			SquareMatrixElemFactory<U, R, S> _fac )
	{
		super( _fac );
		elem = _elem;
	}
	
	@Override
	public SquareMatrixElem<U, R, S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		ArrayList<SquareMatrixElem<U, R, S>> args = new ArrayList<SquareMatrixElem<U, R, S>>();
		return( elem.eval( implicitSpace ).handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.INVERT_RIGHT_REV_COEFF , args) );
	}
	
	@Override
	public SquareMatrixElem<U, R, S> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>>, SquareMatrixElem<U, R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>> key = new SCacheKey<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>>( this , implicitSpace );
		final SquareMatrixElem<U, R, S> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		ArrayList<SquareMatrixElem<U, R, S>> args = new ArrayList<SquareMatrixElem<U, R, S>>();
		final SquareMatrixElem<U, R, S> ret = elem.evalCached( implicitSpace , cache ).handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.INVERT_RIGHT_REV_COEFF , args);
		cache.put( key , ret );
		return( ret );
	}
	
	@Override
	public SquareMatrixElem<U, R, S> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}
	
	@Override
	public SquareMatrixElem<U, R, S> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>>, SquareMatrixElem<U, R, S>> cache)
			throws NotInvertibleException,
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
	public SymbolicInvertRightRevCoeff<U,R,S> cloneThread( final BigInteger threadIndex )
	{
		final SquareMatrixElemFactory<U,R,S> facs = this.getFac().getFac().cloneThread(threadIndex);
		final SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elems = elem.cloneThread(threadIndex);
		if( ( facs != this.getFac().getFac() ) || ( elems != elem ) )
		{
			return( new SymbolicInvertRightRevCoeff<U,R,S>( elems , facs ) );
		}
		return( this );
	}
	

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "invertRightRevCoeff( " );
		elem.writeString( ps );
		ps.print( " )" );
	}
	
	@Override
	public void writeMathML(
			PrecedenceComparator<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>> pc,
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
		ps.print( "<mi>-1RR</mi></msup>" );
	}
	
	
	/**
	 * Returns the enclosed elem.
	 * 
	 * @return The enclosed elem.
	 */
	public SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> getElem() {
		return elem;
	}
	

	/**
	 * The enclosed elem.
	 */
	private SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elem;

}

