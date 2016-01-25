



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

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Symbolic elem for the multiplication of two matrices where the multiplication
 * of the enclosed elems of the matrices is performed in reverse-order.  Expressed as: <math display="inline">
 * <mrow>
 *   <mi>A</mi>
 *   <mo>*R</mo>
 *   <mi>B</mi>
 * </mrow>
 * </math>.  Note that the multiplication of the enclosed elems may not be commutative.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the matrix.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicMultRevCoeff<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elemA The left-side argument of the multiplication.
	 * @param _elemB The right-side argument of the multiplication.
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicMultRevCoeff( SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> _elemA , 
			SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> _elemB ,
			SquareMatrixElemFactory<U, R, S> _fac )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elemA The left-side argument of the multiplication.
	 * @param _elemB The right-side argument of the multiplication.
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public SymbolicMultRevCoeff( SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> _elemA , 
			SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> _elemB ,
			SquareMatrixElemFactory<U, R, S> _fac , DroolsSession ds )
	{
		this( _elemA , _elemB , _fac );
		ds.insert( this );
	}
	
	@Override
	public SquareMatrixElem<U, R, S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		ArrayList<SquareMatrixElem<U, R, S>> args = new ArrayList<SquareMatrixElem<U, R, S>>();
		args.add( elemB.eval( implicitSpace ) );
		return( elemA.eval( implicitSpace ).handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.MULT_REV_COEFF , args) );
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
		args.add( elemB.eval( implicitSpace ) );
		final SquareMatrixElem<U, R, S> ret = elemA.evalCached( implicitSpace , cache ).handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.MULT_REV_COEFF , args);
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
		return( elemB.evalSymbolicConstantApprox() );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elemB.exposesDerivatives() );
	}
	
	
	@Override
	public SymbolicMultRevCoeff<U,R,S> cloneThread( final BigInteger threadIndex )
	{
		final SquareMatrixElemFactory<U,R,S> facs = this.getFac().getFac().cloneThread(threadIndex);
		final SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elemsA = elemA.cloneThread(threadIndex);
		final SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elemsB = elemB.cloneThread(threadIndex);
		if( ( facs != this.getFac().getFac() ) || ( elemsA != elemA ) || ( elemsB != elemB ) )
		{
			return( new SymbolicMultRevCoeff<U,R,S>( elemsA , elemsB , facs ) );
		}
		return( this );
	}
	
	
	@Override
	public SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>>, SymbolicElemFactory<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>>> cache) {
		final SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		final SquareMatrixElemFactory<U,R,S> facs = this.getFac().getFac().cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		final SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elemsA = elemA.cloneThreadCached(threadIndex, cache);
		final SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elemsB = elemB.cloneThreadCached(threadIndex, cache);
		if( ( facs != this.getFac().getFac() ) || ( elemsA != elemA ) || ( elemsB != elemB ) )
		{
			final SymbolicMultRevCoeff<U,R,S> rtmp = new SymbolicMultRevCoeff<U,R,S>( elemsA , elemsB , facs );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}

	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "multRevCoeff( " );
		elemA.writeString( ps );
		ps.print( " , " );
		elemB.writeString( ps );
		ps.print( " )" );
	}
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>> pc,
			PrintStream ps) {
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
		ps.print( "<mo>*R</mo>" );
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
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elemA.performInserts( session );
		elemB.performInserts( session );
		super.performInserts( session );
	}
	
	
	
	
	
	/**
	 * Gets the left-side argument of the multiplication.
	 * 
	 * @return The left-side argument of the multiplication.
	 */
	public SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> getElemA() {
		return elemA;
	}

	/**
	 * Gets the right-side argument of the multiplication.
	 * 
	 * @return The right-side argument of the multiplication.
	 */
	public SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> getElemB() {
		return elemB;
	}
	

	/**
	 * The left-side argument of the multiplication.
	 */
	private SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elemA;
	
	/**
	 * The right-side argument of the multiplication.
	 */
	private SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> elemB;

}

