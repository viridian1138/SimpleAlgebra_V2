




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





package simplealgebra.ddx;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Implements a directional derivative operator (usually represented as <math display="inline">
 * <mrow>
 *  <mo>&nabla;</mo>
 * </mrow>
 * </math>) for a Geometric Algebra multivector.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions over which to express the directional derivative.
 * @param <A> The ord of the directional derivative vector.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class DirectionalDerivative<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
		extends DerivativeElem<GeometricAlgebraMultivectorElem<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,GeometricAlgebraMultivectorElemFactory<U,A,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
{

	/**
	 * Constructs the directional derivative.
	 * 
	 * @param _fac Factory for the enclosed type.
	 * @param _dim The number of dimensions over which to express the directional derivative.
	 * @param _ord The ord of the directional derivative vector.
	 * @param _dfac Factory for generating the partial derivatives of the directional derivative.
	 */
	public DirectionalDerivative( GeometricAlgebraMultivectorElemFactory<U,A, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			U _dim ,
			A _ord ,
			DirectionalDerivativePartialFactory<R,S,K> _dfac )
	{
		super( _fac );
		dim = _dim;
		ord = _ord;
		dfac = _dfac;
	}
	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalDerivative(
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, GeometricAlgebraMultivectorElemFactory<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> in ,
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, 
			GeometricAlgebraMultivectorElemFactory<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
			facA = in.getFac();
		
		final GeometricAlgebraMultivectorElemFactory<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
			facB = facA.getFac();
		
		final SymbolicElemFactory<R, S> facC = facB.getFac();
		
		final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> mul = 
				new GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( facC , dim , ord );
		
		BigInteger cnt = BigInteger.ZERO;
		
		final BigInteger max = dim.getVal();
		
		for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( cnt );
			
			SymbolicElem<R,S> val = dfac.getPartial( cnt );
			
			mul.setVal(key, val);
		}
		
		
		final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret =
				mul.mult( in.eval( implicitSpace ) );
		
		return( ret );
	}
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalDerivativeCached(
			SymbolicElem<GeometricAlgebraMultivectorElem<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, GeometricAlgebraMultivectorElemFactory<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> in,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<GeometricAlgebraMultivectorElem<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, GeometricAlgebraMultivectorElemFactory<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>, GeometricAlgebraMultivectorElem<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		
	final SymbolicElemFactory<GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, 
		GeometricAlgebraMultivectorElemFactory<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		facA = in.getFac();
	
	final GeometricAlgebraMultivectorElemFactory<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
		facB = facA.getFac();
	
	final SymbolicElemFactory<R, S> facC = facB.getFac();
	
	final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> mul = 
			new GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( facC , dim , ord );
	
	BigInteger cnt = BigInteger.ZERO;
	
	final BigInteger max = dim.getVal();
	
	for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
	{
		final HashSet<BigInteger> key = new HashSet<BigInteger>();
		key.add( cnt );
		
		SymbolicElem<R,S> val = dfac.getPartial( cnt );
		
		mul.setVal(key, val);
	}
	
	
	final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret =
			mul.mult( in.evalCached( implicitSpace , cache ) );
	
	return( ret );
		
	}
	
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> eval(
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
	
			final GeometricAlgebraMultivectorElemFactory<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
				facB = this.getFac().getFac();
			
			final SymbolicElemFactory<R, S> facC = facB.getFac();
			
			final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> mul = 
					new GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( facC , dim , ord );
			
			BigInteger cnt = BigInteger.ZERO;
			
			final BigInteger max = dim.getVal();
			
			for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
			{
				final HashSet<BigInteger> key = new HashSet<BigInteger>();
				key.add( cnt );
				
				SymbolicElem<R,S> val = dfac.getPartial( cnt );
				
				mul.setVal(key, val);
			}
			
			return( mul );
		}
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalCached(
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , 
			HashMap<SCacheKey<GeometricAlgebraMultivectorElem<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, GeometricAlgebraMultivectorElemFactory<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>, GeometricAlgebraMultivectorElem<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> cache )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
	
			final SCacheKey<GeometricAlgebraMultivectorElem<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, GeometricAlgebraMultivectorElemFactory<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> key
				= new SCacheKey<GeometricAlgebraMultivectorElem<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, GeometricAlgebraMultivectorElemFactory<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>( this , implicitSpace );
			final GeometricAlgebraMultivectorElem<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> iret = cache.get( key );
			if( iret != null )
			{
				return( iret );
			}
			GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>
				ret = eval( implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
	
	

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "directionalDerivative" );
	}
	
	
	@Override
	public DirectionalDerivative<U,A,R,S,K> cloneThread( final BigInteger threadIndex )
	{
		// The NumDimensions dim and Ord ord are presumed to be immutable.
		final GeometricAlgebraMultivectorElemFactory<U,A, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> sfac = this.getFac().getFac().cloneThread(threadIndex);
		final DirectionalDerivativePartialFactory<R,S,K> dfacs = dfac.cloneThread(threadIndex);
		if( ( sfac != this.getFac().getFac() ) || ( dfacs != dfac ) )
		{
			return( new DirectionalDerivative<U,A,R,S,K>( sfac , dim , ord , dfacs ) );
		}
		return( this );
	}
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator<GeometricAlgebraMultivectorElem<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, GeometricAlgebraMultivectorElemFactory<U, A, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> pc,
			PrintStream ps) {
		ps.print( "<mo>&nabla;</mo>" );
	}
	
	
	/**
	 * Gets the number of dimensions over which to express the directional derivative.
	 * 
	 * @return The number of dimensions over which to express the directional derivative.
	 */
	public U getDim() {
		return dim;
	}

	/**
	 * Gets the ord of the directional derivative vector.
	 * 
	 * @return The ord of the directional derivative vector.
	 */
	public A getOrd() {
		return ord;
	}

	/**
	 * Gets the factory for generating the partial derivatives of the directional derivative.
	 * 
	 * @return The factory for generating the partial derivatives of the directional derivative.
	 */
	public DirectionalDerivativePartialFactory<R, S, K> getDfac() {
		return dfac;
	}


	/**
	 * The number of dimensions over which to express the directional derivative.
	 */
	private U dim;
	
	/**
	 * The ord of the directional derivative vector.
	 */
	private A ord;
	
	/**
	 * Factory for generating the partial derivatives of the directional derivative.
	 */
	private DirectionalDerivativePartialFactory<R,S,K> dfac;

}

