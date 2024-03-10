




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





package simplealgebra.tdg;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.WriteElemCache;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Raises a Geometric Algebra multivector to a higher number of dimensions.
 * This was inspired by an idea from Ship AI about performing products on
 * vectors from different dimensions. 
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the multivector.
 * @param <A> The Ord of the multivector.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <Ulower> The number of dimensions in the multivector to be raised.
 * @param <Alower> The Ord of the multivector to be raised.
 */
public class RaiseMultivectorDimension<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>, Ulower extends NumDimensions, Alower extends Ord<Ulower>> 
	extends SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>>
{
	/**
	 * Symbolic elem for the vector at the lower number of dimensions to be raised.
	 */
    protected SymbolicElem<GeometricAlgebraMultivectorElem<Ulower,Alower,R,S>,GeometricAlgebraMultivectorElemFactory<Ulower,Alower,R,S>>
    	lowerVect;
	
    /**
     * Constructs the dimension raise.
     * 
     * @param _fac Factory for the enclosed type.
     * @param _lowerVect Symbolic elem for the vector at the lower number of dimensions to be raised.
     */
	public RaiseMultivectorDimension(
			GeometricAlgebraMultivectorElemFactory<U, A, R, S> _fac,
			SymbolicElem<GeometricAlgebraMultivectorElem<Ulower,Alower,R,S>,GeometricAlgebraMultivectorElemFactory<Ulower,Alower,R,S>>
	    		_lowerVect
			) throws DimensionMismatchException
	{
		super(_fac);
		lowerVect = _lowerVect;
		if( lowerVect.getFac().getFac().getDim().getVal().compareTo(
				_fac.getDim().getVal() ) > 0 )
		{
			throw( new DimensionMismatchException( ) );
		}
	}

	@Override
	public GeometricAlgebraMultivectorElem<U, A, R, S> eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		final GeometricAlgebraMultivectorElem<Ulower,Alower,R,S>
			lowerV = lowerVect.eval( implicitSpace );
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			higherV = getFac().getFac().zero();
		
		for( final Entry<HashSet<BigInteger>, R> e : lowerV.getEntrySet() )
		{
			higherV.setVal(e.getKey(), e.getValue());
		}
			
		return higherV;
	}

	@Override
	public GeometricAlgebraMultivectorElem<U, A, R, S> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>, GeometricAlgebraMultivectorElem<U, A, R, S>> cache)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		final SCacheKey<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>> key = 
				new SCacheKey<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>( this , implicitSpace );
		final GeometricAlgebraMultivectorElem<U, A, R, S> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		
		final GeometricAlgebraMultivectorElem<Ulower,Alower,R,S>
			lowerV = lowerVect.eval( implicitSpace );
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			higherV = getFac().getFac().zero();
		
		for( final Entry<HashSet<BigInteger>, R> e : lowerV.getEntrySet() )
		{
			higherV.setVal(e.getKey(), e.getValue());
		}
		
		cache.put( key , higherV );
		return( higherV );
	}

	@Override
	public GeometricAlgebraMultivectorElem<U, A, R, S> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		final GeometricAlgebraMultivectorElem<Ulower,Alower,R,S>
			lowerV = lowerVect.evalPartialDerivative(withRespectTo, implicitSpace);
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			higherV = getFac().getFac().zero();
		
		for( final Entry<HashSet<BigInteger>, R> e : lowerV.getEntrySet() )
		{
			higherV.setVal(e.getKey(), e.getValue());
		}
			
		return higherV;
	}

	@Override
	public GeometricAlgebraMultivectorElem<U, A, R, S> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>, GeometricAlgebraMultivectorElem<U, A, R, S>> cache)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		final SCacheKey<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>> key = 
				new SCacheKey<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>( this , implicitSpace );
		final GeometricAlgebraMultivectorElem<U, A, R, S> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		
		final GeometricAlgebraMultivectorElem<Ulower,Alower,R,S>
			lowerV = lowerVect.evalPartialDerivative(withRespectTo, implicitSpace);
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			higherV = getFac().getFac().zero();
		
		for( final Entry<HashSet<BigInteger>, R> e : lowerV.getEntrySet() )
		{
			higherV.setVal(e.getKey(), e.getValue());
		}
		
		cache.put( key , higherV );
		return( higherV );
	}

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>>> cache,
			PrintStream ps) 
	{
		String st = cache.get( this );
		if( st == null )
		{
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			final String elems = lowerVect.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( RaiseMultivectorDimension.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( RaiseMultivectorDimension.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( "( " );
			ps.print( facs );
			ps.print( " , " );
			ps.print( elems );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		ps.print( "<mrow><mi>RaiseDimensionMultivector</mi><mo>&ApplyFunction;</mo>" );
		pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		lowerVect.writeMathML(pc, ps);
		pc.getParenthesisGenerator().handleParenthesisClose(ps);
		ps.print( "</mrow>" );
	}

	
}


