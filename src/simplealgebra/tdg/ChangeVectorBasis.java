




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
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
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
 * Transforms a vector to a different basis using a transform matrix.
 * This was inspired by an idea from Ship AI about fundamental operations for 
 * vectors from different dimensions. 
 * 
 * See:  <A href="https://en.wikipedia.org/wiki/Change_of_basis">https://en.wikipedia.org/wiki/Change_of_basis</A>
 * 
 * There are multiple possibilities for implementing such an operation.  If
 * orthonormal bases are assumed, then Geometric Algebra rotations can be used
 * ( <A href="https://math.stackexchange.com/questions/4058304/what-is-the-equivalent-of-a-change-of-basis-in-geometric-algebra">https://math.stackexchange.com/questions/4058304/what-is-the-equivalent-of-a-change-of-basis-in-geometric-algebra<A> ).
 * By contrast, matrices are more general but their lack of a geometrical
 * representation creates what professor David Hestenes called a "Tower of
 * Babel" where the underlying meaning of the transform is difficult to
 * translate (pun intended).  This implementation uses matrices so that the 
 * complete set of basis transforms can be represented in spite of the 
 * aforementioned drawbacks of matrices.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the multivector.
 * @param <A> The Ord of the multivector.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class ChangeVectorBasis<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> 
	extends SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>>
{
	/**
	 * Symbolic elem for the column vector to have its basis changed.
	 */
    protected SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>>
    	columnVect;
    
    /**
	 * Symbolic elem for the square matrix used to transform the column vector.
	 */
    protected SymbolicElem<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>>
    	transform;
	
    /**
     * Constructs the vector basis change.
     * 
     * @param _fac Factory for the enclosed type.
     * @param _columnVect Symbolic elem for the column vector to have its basis changed.
     * @param _transform Symbolic elem for the square matrix used to transform the column vector.
     */
	public ChangeVectorBasis(
			GeometricAlgebraMultivectorElemFactory<U, A, R, S> _fac,
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>>
	    		_columnVect,
			SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U, R, S>>
    			_transform
			) throws DimensionMismatchException
	{
		super(_fac);
		columnVect = _columnVect;
		transform = _transform;
	}

	@Override
	public GeometricAlgebraMultivectorElem<U, A, R, S> eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			columnV = columnVect.eval( implicitSpace );
		
		final SquareMatrixElem<U,R,S>
			transM = transform.eval( implicitSpace );
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			ocolV = getFac().getFac().zero();
		
		columnV.colVectorMultLeftDefault(transM, ocolV);
			
		return ocolV;
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
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			columnV = columnVect.eval( implicitSpace );
	
		final SquareMatrixElem<U,R,S>
			transM = transform.eval( implicitSpace );
	
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			ocolV = getFac().getFac().zero();
	
		columnV.colVectorMultLeftDefault(transM, ocolV);
		
		cache.put( key , ocolV );
		return( ocolV );
	}

	@Override
	public GeometricAlgebraMultivectorElem<U, A, R, S> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			columnV = columnVect.eval( implicitSpace );
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			columnVp = columnVect.evalPartialDerivative( withRespectTo, implicitSpace );
	
		final SquareMatrixElem<U,R,S>
			transM = transform.eval( implicitSpace );
	
		final SquareMatrixElem<U,R,S>
			transMp = transform.evalPartialDerivative( withRespectTo, implicitSpace );
	
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			ocolVa = getFac().getFac().zero();
	
		columnV.colVectorMultLeftDefault(transMp, ocolVa);
	
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			ocolVb = getFac().getFac().zero();
	
		columnVp.colVectorMultLeftDefault(transM, ocolVb);
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			ocolV = ocolVa.add(ocolVb);
			
		return ocolV;
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
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			columnV = columnVect.eval( implicitSpace );
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			columnVp = columnVect.evalPartialDerivative( withRespectTo, implicitSpace );
	
		final SquareMatrixElem<U,R,S>
			transM = transform.eval( implicitSpace );
	
		final SquareMatrixElem<U,R,S>
			transMp = transform.evalPartialDerivative( withRespectTo, implicitSpace );
	
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			ocolVa = getFac().getFac().zero();
	
		columnV.colVectorMultLeftDefault(transMp, ocolVa);
	
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			ocolVb = getFac().getFac().zero();
	
		columnVp.colVectorMultLeftDefault(transM, ocolVb);
		
		final GeometricAlgebraMultivectorElem<U,A,R,S>
			ocolV = ocolVa.add(ocolVb);
		
		cache.put( key , ocolV );
		return( ocolV );
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
			final String elems = columnVect.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			final String trans = transform.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
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
			ps.print( " , " );
			ps.print( trans );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		ps.print( "<mrow><mi>RaiseDimensionMultivector</mi><mo>&ApplyFunction;</mo>" );
		pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		columnVect.writeMathML(pc, ps);
		ps.print( "<mo>,</mo>" );
		transform.writeMathML(pc, ps);
		pc.getParenthesisGenerator().handleParenthesisClose(ps);
		ps.print( "</mrow>" );
	}

	
}


