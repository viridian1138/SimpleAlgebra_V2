




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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import simplealgebra.CloneThreadCache;
import simplealgebra.ComplexElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.symbolic.SymbolicElem;

/**
 * A factory for Geometric Algebra elems.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the algebra.
 * @param <A> The ord of the algebra.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class GeometricAlgebraMultivectorElemFactory<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<GeometricAlgebraMultivectorElem<U,A,R,S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> {

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _dim The number of dimensions in the algebra.
	 * @param _ord The ord of the algebra.
	 */
	public GeometricAlgebraMultivectorElemFactory( S _fac , U _dim , A _ord )
	{
		fac = _fac;
		dim = _dim;
		ord = _ord;
	}
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> identity() {
		GeometricAlgebraMultivectorElem<U,A, R, S> ret = new GeometricAlgebraMultivectorElem<U,A, R, S>( fac , dim , ord );
		ret.setVal( new HashSet<BigInteger>(), fac.identity() );
		return( ret );
	}

	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> zero() {
		return( new GeometricAlgebraMultivectorElem<U,A, R, S>( fac , dim , ord ) );
	}
	
	
	@Override
	public boolean isMultCommutative()
	{
		return( false );
	}
	
	
	@Override
	public boolean isNestedMultCommutative()
	{
		return( fac.isMultCommutative() );
	}
	
	
	@Override
	public boolean isMultAssociative()
	{
		return( fac.isMultAssociative() );
	}
	
	
	@Override
	public boolean isNestedMultAssociative()
	{
		return( fac.isMultAssociative() );
	}
	
	
	/**
	 * Returns the factory for the enclosed type.
	 * 
	 * @return The factory for the enclosed type.
	 */
	public S getFac()
	{
		return( fac );
	}
	
	
	/**
	 * Returns the number of dimensions.
	 * 
	 * @return The number of dimensions.
	 */
	public U getDim()
	{
		return( dim );
	}
	
	
	/**
	 * Returns the ord.
	 * 
	 * @return The ord.
	 */
	public A getOrd()
	{
		return( ord );
	}
	
	
	@Override
	public SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>>> args )  throws NotInvertibleException
	{
		if( id instanceof GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd )
		{
			switch( (GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd) id )
			{
				case DOT:
				{
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argA
						= args.get( 0 );
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argB
						= args.get( 1 );
					return( new SymbolicDot<U,A,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case DOT_HESTENES:
				{
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argA
						= args.get( 0 );
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argB
						= args.get( 1 );
					return( new SymbolicDotHestenes<U,A,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case LEFT_CONTRACTION:
				{
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argA
						= args.get( 0 );
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argB
						= args.get( 1 );
					return( new SymbolicLeftContraction<U,A,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case RIGHT_CONTRACTION:
				{
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argA
						= args.get( 0 );
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argB
						= args.get( 1 );
					return( new SymbolicRightContraction<U,A,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case WEDGE:
				{
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argA
						= args.get( 0 );
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argB
						= args.get( 1 );
					return( new SymbolicWedge<U,A,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case CROSS:
				{
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argA
						= args.get( 0 );
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argB
						= args.get( 1 );
					return( new SymbolicCross<U,A,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case SCALAR:
				{
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argA
						= args.get( 0 );
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argB
						= args.get( 1 );
					return( new SymbolicScalar<U,A,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case REVERSE_LEFT:
				{
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argA
						= args.get( 0 );
					return( new SymbolicReverseLeft<U,A,R,S>( argA , argA.getFac().getFac() ) );
				}
				// break;
				
				case REVERSE_RIGHT:
				{
					SymbolicElem<GeometricAlgebraMultivectorElem<U,A, R, S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>> argA
						= args.get( 0 );
					return( new SymbolicReverseRight<U,A,R,S>( argA , argA.getFac().getFac() ) );
				}
				// break;
				
			}
		}
		
		return( super.handleSymbolicOptionalOp(id, args) );
	}
	
	
	
	@Override
	public GeometricAlgebraMultivectorElemFactory<U,A,R,S> cloneThread( final BigInteger threadIndex )
	{
		S sfac = fac.cloneThread(threadIndex);
		if( fac != sfac )
		{
			// The NumDimensions dim and Ord ord are presumed to be immutable.
			return( new GeometricAlgebraMultivectorElemFactory<U,A,R,S>( sfac , dim , ord ) );
		}
		return( this );
	}
	
	
	
	@Override
	public GeometricAlgebraMultivectorElemFactory<U, A, R, S> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>> cache) {
		final GeometricAlgebraMultivectorElemFactory<U, A, R, S> ctmp = cache.getFac( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		S sfac = fac.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( fac != sfac )
		{
			// The NumDimensions dim and Ord ord are presumed to be immutable.
			final GeometricAlgebraMultivectorElemFactory<U, A, R, S> rtmp = new GeometricAlgebraMultivectorElemFactory<U, A, R, S>( sfac , dim , ord );
			cache.putFac(this, rtmp);
			return( rtmp );
		}
		cache.putFac(this, this);
		return( this );
	}
	
	
	
	/**
	 * The factory for the enclosed type.
	 */
	private S fac;
	
	/**
	 * The number of dimensions in the algebra.
	 */
	private U dim;
	
	/**
	 * The ord for the algebra.
	 */
	private A ord;


}



