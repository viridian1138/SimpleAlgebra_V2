




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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.stime.SpacetimeAlgebraMultivectorElem;
import simplealgebra.stime.SpacetimeAlgebraMultivectorElemFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Implements a directional derivative operator (also known as the "del operator" as in "del cross E") for a 
 * Spacetime Algebra multivector.
 * 
 * @author thorngreen
 *
 * @param <U>
 * @param <R>
 * @param <S>
 */
public class DirectionalDerivativeSta<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
		extends DerivativeElem<SpacetimeAlgebraMultivectorElem<U,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,SpacetimeAlgebraMultivectorElemFactory<U,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>
{

	public DirectionalDerivativeSta( SpacetimeAlgebraMultivectorElemFactory<U, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			U _dim ,
			DirectionalDerivativePartialFactory<R,S,K> _dfac )
	{
		super( _fac );
		dim = _dim;
		dfac = _dfac;
	}
	
	@Override
	public SpacetimeAlgebraMultivectorElem<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalDerivative(
			SymbolicElem<SpacetimeAlgebraMultivectorElem<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SpacetimeAlgebraMultivectorElemFactory<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> in ,
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		final SymbolicElemFactory<SpacetimeAlgebraMultivectorElem<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, 
			SpacetimeAlgebraMultivectorElemFactory<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
			facA = in.getFac();
		
		final SpacetimeAlgebraMultivectorElemFactory<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
			facB = facA.getFac();
		
		final SymbolicElemFactory<R, S> facC = facB.getFac();
		
		final SpacetimeAlgebraMultivectorElem<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> mul = 
				new SpacetimeAlgebraMultivectorElem<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( facC , dim );
		
		BigInteger cnt = BigInteger.ZERO;
		
		final BigInteger max = dim.getVal();
		
		for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( cnt );
			
			PartialDerivativeOp<R,S,K> val = dfac.getPartial( cnt );
			
			mul.setVal(key, val);
		}
		
		
		final SpacetimeAlgebraMultivectorElem<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret =
				mul.mult( in.eval( implicitSpace ) );
		
		return( ret );
	}
	
	
	@Override
	public SpacetimeAlgebraMultivectorElem<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> eval(
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
	
			final SpacetimeAlgebraMultivectorElemFactory<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
				facB = this.getFac().getFac();
			
			final SymbolicElemFactory<R, S> facC = facB.getFac();
			
			final SpacetimeAlgebraMultivectorElem<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> mul = 
					new SpacetimeAlgebraMultivectorElem<U, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( facC , dim );
			
			BigInteger cnt = BigInteger.ZERO;
			
			final BigInteger max = dim.getVal();
			
			for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
			{
				final HashSet<BigInteger> key = new HashSet<BigInteger>();
				key.add( cnt );
				
				PartialDerivativeOp<R,S,K> val = dfac.getPartial( cnt );
				
				mul.setVal(key, val);
			}
			
			return( mul );
		}
	

	@Override
	public String writeString( ) {
		return( "DirectionalDerivativeSta" );
	}
	
	private U dim;
	private DirectionalDerivativePartialFactory<R,S,K> dfac;
	
	

}

