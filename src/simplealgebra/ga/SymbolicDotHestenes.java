




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

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Symbolic elem for the "Hestenes" version of the Geometric Algebra dot product <math display="inline">
 * <mrow>
 *   <mi>A</mi>
 *   <mrow><msub><mo>&CenterDot;</mo><mi>H</mi></msub></mrow>
 *   <mi>B</mi>
 * </mrow>
 * </math> over multivectors.  See "Geometric Algebra for Computer Scientists" by L. Dorst.
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
public class SymbolicDotHestenes<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> 
{

	/**
	 * Constructs the "Hestenes" dot product.
	 * 
	 * @param _elemA The left argument of the dot product.
	 * @param _elemB The right argument of the dot product.
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicDotHestenes( 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> _elemA , 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> _elemB , GeometricAlgebraMultivectorElemFactory<U,A, R, S> _fac) 
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	
	/**
	 * Constructs the "Hestenes" dot product for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elemA The left argument of the dot product.
	 * @param _elemB The right argument of the dot product.
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public SymbolicDotHestenes( 
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
		return( elemA.eval( implicitSpace ).handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT_HESTENES , args ) );
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
				elemA.evalCached( implicitSpace , cache ).handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT_HESTENES , args );
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
	public SymbolicDotHestenes<U,A,R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> elemA2
			= elemA.cloneThread(threadIndex);
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> elemB2
			= elemB.cloneThread(threadIndex);
		final GeometricAlgebraMultivectorElemFactory<U,A, R, S> facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elemA2 != elemA ) || ( elemB2 != elemB ) || ( facs != this.getFac().getFac() ) )
		{
			return( new SymbolicDotHestenes<U,A,R,S>( elemA2 , elemB2 , facs ) );
		}
		return( this );
	}
	
	
	@Override
	public SymbolicElem<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<SymbolicElem<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>> cache) 
	{
		final SymbolicElem<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> elemA2
			= elemA.cloneThreadCached( threadIndex , (CloneThreadCache)( cache.getInnerCache() ) );
		final SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> elemB2
			= elemB.cloneThreadCached( threadIndex , (CloneThreadCache)( cache.getInnerCache() ) );
		final GeometricAlgebraMultivectorElemFactory<U,A, R, S> facs = this.getFac().getFac().cloneThreadCached( threadIndex , (CloneThreadCache)( cache.getInnerCache() ) );
		if( ( elemA2 != elemA ) || ( elemB2 != elemB ) || ( facs != this.getFac().getFac() ) )
		{
			final SymbolicDotHestenes<U,A,R,S> rtmp = new SymbolicDotHestenes<U,A,R,S>( elemA2 , elemB2 , facs );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}
	
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "dotHestenes( " );
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
		ps.print( "<mrow><msub><mo>&CenterDot;</mo><mi>H</mi></msub></mrow>" );
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
	 * Returns the left argument of the dot product.
	 * 
	 * @return The left argument of the dot product.
	 */
	public SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> getElemA() {
		return elemA;
	}
	
	/**
	 * Returns the right argument of the dot product.
	 * 
	 * @return The right argument of the dot product.
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
	 * The left argument of the dot product.
	 */
	private SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> elemA;
	
	/**
	 * The right argument of the dot product.
	 */
	private SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> elemB;
	
}
