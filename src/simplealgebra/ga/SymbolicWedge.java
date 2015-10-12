




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





package simplealgebra.ga;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;


/**
 * Symbolic elem for the Geometric Algebra wedge product <math display="inline">
 * <mrow>
 *   <mi>A</mi>
 *   <mo>&and;</mo>
 *   <mi>B</mi>
 * </mrow>
 * </math> over multivectors.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions for the multivector.
 * @param <A> The Ord for the multivector.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicWedge<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> 
{

	/**
	 * Constructs the wedge product.
	 * 
	 * @param _elemA The left argument of the wedge product.
	 * @param _elemB The right argument of the wedge product.
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicWedge( 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> _elemA , 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> _elemB , GeometricAlgebraMultivectorElemFactory<U,A, R, S> _fac) 
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	
	/**
	 * Constructs the wedge product for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elemA The left argument of the wedge product.
	 * @param _elemB The right argument of the wedge product.
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public SymbolicWedge( 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> _elemA , 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> _elemB , GeometricAlgebraMultivectorElemFactory<U,A, R, S> _fac ,
			DroolsSession ds ) 
	{
		this( _elemA , _elemB , _fac );
		ds.insert( this );
	}

	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>> args = new ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>>();
		args.add( elemB.eval( implicitSpace ) );
		return( elemA.eval( implicitSpace ).handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args ) );
	}
	
	

	@Override
	public GeometricAlgebraMultivectorElem<U, A, R, S> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>, GeometricAlgebraMultivectorElem<U, A, R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>> key =
				new SCacheKey<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>( this , implicitSpace );
		final GeometricAlgebraMultivectorElem<U, A, R, S> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>> args = new ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>>();
		args.add( elemB.evalCached( implicitSpace , cache ) );
		final GeometricAlgebraMultivectorElem<U, A, R, S> ret = 
				elemA.evalCached( implicitSpace , cache ).handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		cache.put(key, ret);
		return( ret );
	}

	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}
	
	@Override
	public GeometricAlgebraMultivectorElem<U, A, R, S> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>, GeometricAlgebraMultivectorElem<U, A, R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}
	
	
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		return( elemA.evalSymbolicConstantApprox() && elemB.evalSymbolicConstantApprox() );
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elemB.exposesDerivatives() );
	}

	
	@Override
	public SymbolicWedge<U,A,R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>>
			elemAs = elemA.cloneThread(threadIndex);
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>>
			elemBs = elemB.cloneThread(threadIndex);
		final GeometricAlgebraMultivectorElemFactory<U,A, R, S>
			facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elemAs != elemA ) || ( elemBs != elemB ) || ( facs != this.getFac().getFac() ) )
		{
			return( new SymbolicWedge<U,A,R,S>( elemAs , elemBs , facs ) );
		}
		return( this );
	}
	
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "wedge( " );
		elemA.writeString( ps );
		ps.print( " , " );
		elemB.writeString( ps );
		ps.print( " )" );
	}
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>> pc,
			PrintStream ps) 
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
		ps.print( "<mo>&and;</mo>" );
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
	 * Returns the left argument of the wedge product.
	 * 
	 * @return The left argument of the wedge prodict.
	 */
	public SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> getElemA() {
		return elemA;
	}
	
	/**
	 * Returns the right argument of the wedge product.
	 * 
	 * @return The right argument of the wedge product.
	 */
	public SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> getElemB() {
		return elemB;
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elemA.performInserts( session );
		elemB.performInserts( session );
		super.performInserts( session );
	}

	
	/**
	 * The left argument of the wedge product.
	 */
	private SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> elemA;
	
	/**
	 * The right argument of the wedge product.
	 */
	private SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> elemB;

}

