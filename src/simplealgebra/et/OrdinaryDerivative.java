




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





package simplealgebra.et;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.AbstractCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteNumDimensionsCache;
import simplealgebra.ddx.DerivativeElem;
import simplealgebra.ddx.DirectionalDerivative;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.WriteDirectionalDerivativePartialFactoryCache;
import simplealgebra.ga.WriteOrdCache;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Implements a factory for the tensor <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>v</mi>
 *  </msub>
 * </mrow>
 * </math> where <math display="inline">
 * <mrow>
 *  <mi>v</mi>
 * </mrow>
 * </math> is a tensor index.  This produces a rank-one tensor
 * with a set of partial derivative operators.  The name of the
 * particular index to be used is passed into the class as a
 * parameter.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions for the index.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class OrdinaryDerivative<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
		extends DerivativeElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
{

	/**
	 * Constructs the tensor factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _index The index for the rank-one tensor.
	 * @param _dim The number of dimensions for the index.
	 * @param _dfac Factory for generating the partial derivatives of a directional derivative.
	 */
	public OrdinaryDerivative( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			Z _index ,
			U _dim ,
			DirectionalDerivativePartialFactory<R,S,K> _dfac )
	{
		super( _fac );
		index = _index;
		dim = _dim;
		dfac = _dfac;
	}
	
	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalDerivative(
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> in ,
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		final SymbolicElemFactory<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, 
			EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
			facA = in.getFac();
		
		final EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
			facB = facA.getFac();
		
		final SymbolicElemFactory<R, S> facC = facB.getFac();
		
		final ArrayList<Z> contravariantIndices = new ArrayList<Z>();
		final ArrayList<Z> covariantIndices = new ArrayList<Z>();
		
		covariantIndices.add( index );
		
		final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> mul = 
				new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( facC , 
						contravariantIndices , covariantIndices );
		
		BigInteger cnt = BigInteger.ZERO;
		
		final BigInteger max = dim.getVal();
		
		for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			final ArrayList<BigInteger> key = new ArrayList<BigInteger>();
			key.add( cnt );
			
			SymbolicElem<R,S> val = dfac.getPartial( cnt );
			
			mul.setVal(key, val);
		}
		
		
		final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret =
				mul.mult( in.eval( implicitSpace ) );
		
		return( ret );
	}
	
	
	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalDerivativeCached(
			SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> in,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>, EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		
		final SymbolicElemFactory<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, 
			EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
			facA = in.getFac();
	
		final EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
			facB = facA.getFac();
	
		final SymbolicElemFactory<R, S> facC = facB.getFac();
	
		final ArrayList<Z> contravariantIndices = new ArrayList<Z>();
		final ArrayList<Z> covariantIndices = new ArrayList<Z>();
	
		covariantIndices.add( index );
	
		final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> mul = 
				new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( facC , 
						contravariantIndices , covariantIndices );
	
		BigInteger cnt = BigInteger.ZERO;
	
		final BigInteger max = dim.getVal();
	
		for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			final ArrayList<BigInteger> key = new ArrayList<BigInteger>();
			key.add( cnt );
		
			SymbolicElem<R,S> val = dfac.getPartial( cnt );
		
			mul.setVal(key, val);
		}
	
	
		final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret =
				mul.mult( in.evalCached( implicitSpace , cache ) );
	
		return( ret );
	}
	
	
	@Override
	public OrdinaryDerivative<Z,U,R,S,K> cloneThread( final BigInteger threadIndex )
	{
		// It is presumed that the Z index and the NumDimensions dim are immutable.
		final DirectionalDerivativePartialFactory<R,S,K> dfacs = dfac.cloneThread( threadIndex );
		final EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> facs = this.getFac().getFac().cloneThread( threadIndex );
		if( ( dfacs != dfac ) || ( facs != this.getFac().getFac() ) )
		{
			return( new OrdinaryDerivative<Z,U,R,S,K>( facs , index , dim , dfacs ) );
		}
		return( this );
	}
	
	

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>, SymbolicElemFactory<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>> cache,
			PrintStream ps) {
		String st = cache.get( this );
		if( st == null )
		{
			cache.applyAuxCache( new WriteDirectionalDerivativePartialFactoryCache( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteNumDimensionsCache( cache.getCacheVal() ) );
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			final String staiF = dfac.writeDesc( ( (WriteDirectionalDerivativePartialFactoryCache)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) WriteDirectionalDerivativePartialFactoryCache.class ) ) ) , ps);
			final String staDim = dim.writeDesc( (WriteNumDimensionsCache)( cache.getAuxCache( WriteNumDimensionsCache.class ) ) , ps);
			String sl = cache.getIncrementVal();
			ps.print( "final " );
			ps.print( index.getClass().getSimpleName() );
			ps.print( " = new " );
			ps.print( index.getClass().getSimpleName() );
			ps.print( "( \"" );
			ps.print( index );
			ps.println( "\" );" );
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( OrdinaryDerivative.class.getSimpleName() );
			ps.print( "<? extends Object," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( ">" );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( OrdinaryDerivative.class.getSimpleName() );
			ps.print( "<? extends Object," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( ">" );
			ps.print( "( " );
			ps.print( facs );
			ps.print( " , " );
			ps.print( sl );
			ps.print( " , " );
			ps.print( staDim );
			ps.print( " , " );
			ps.print( staiF );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator pc,
			PrintStream ps) 
	{		
		ps.print( "<msub><mo>&PartialD;</mo>" );
		ps.print( "<mi>" + index + "</mi></msub>" );
	}
	
	
	
	/**
	 * Gets the tensor index for the ordinary derivative.
	 * 
	 * @return The tensor index for the ordinary derivative.
	 */
	public Z getIndex() {
		return index;
	}

	/**
	 * Gets the number of dimensions for the index.
	 * 
	 * @return The number of dimensions for the index.
	 */
	public U getDim() {
		return dim;
	}

	/**
	 * Gets the factory for generating the partial derivatives of a directional derivative.
	 * 
	 * @return The factory for generating the partial derivatives of a directional derivative.
	 */
	public DirectionalDerivativePartialFactory<R, S, K> getDfac() {
		return dfac;
	}



	/**
	 * The tensor index for the ordinary derivative.
	 */
	private Z index;
	
	/**
	 * The number of dimensions for the index.
	 */
	private U dim;
	
	/**
	 * Factory for generating the partial derivatives of a directional derivative.
	 */
	private DirectionalDerivativePartialFactory<R,S,K> dfac;

	

}

