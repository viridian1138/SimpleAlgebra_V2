


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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;

/**
 * Complex number for representing e.g. phasors in electrical circuit theory.
 * 
 * See http://en.wikipedia.org/wiki/Complex_number
 * 
 * See http://en.wikipedia.org/wiki/Phasor
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R>
 * @param <S>
 */
public class ComplexElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> 
	extends MutableElem<R,ComplexElem<R,S>,ComplexElemFactory<R,S>>
	{
	
	/**
	 * Defines enumerated commands for complex numbers.
	 * 
	 * @author thorngreen
	 *
	 */
	public static enum ComplexCmd {
		
		/**
		 * Enumerated command for the left-side conjugate of a complex number.
		 */
		CONJUGATE_LEFT,
		
		/**
		 * Enumerated command for the right-side conjugate of a complex number.
		 */
		CONJUGATE_RIGHT
	};

	@Override
	public ComplexElem<R, S> add(ComplexElem<R, S> b) {
		return( new ComplexElem<R,S>( re.add( b.re ) , im.add( b.im ) ) );
	}

	@Override
	public ComplexElem<R, S> mult(ComplexElem<R, S> b) {
		final R a0 = re.mult( b.re );
		final R a1 = re.mult( b.im );
		final R a2 = im.mult( b.re );
		final R a3 = im.mult( b.im ).negate();
		return( new ComplexElem<R,S>( a0.add( a3 ) , a1.add( a2 ) ) );
	}

	@Override
	public ComplexElem<R, S> negate() {
		return( new ComplexElem<R,S>( re.negate() , im.negate() ) );
	}
	
	@Override
	public ComplexElem<R, S> mutate( Mutator<R> mutr ) throws NotInvertibleException {
		return( new ComplexElem<R,S>( mutr.mutate( re ) , mutr.mutate( im ) ) );
	}

	@Override
	public ComplexElem<R, S> invertLeft() throws NotInvertibleException {
		if( re.getFac().isMultCommutative() )
		{
			final R denom = ( re.mult(re) ).add( im.mult(im) );
			final R div = denom.invertLeft();
			final R c = re.mult( div );
			final R d = im.mult( div ).negate();
			return( new ComplexElem<R,S>( c , d ) );
		}
		else
		{
			final NumDimensions nd = new NumDimensions()
			{
				public BigInteger getVal()
				{
					return( BigInteger.valueOf( 2 ) );
				}
			};
			final SquareMatrixElemFactory<NumDimensions,R,S> sfac = 
					new SquareMatrixElemFactory<NumDimensions,R,S>( this.getFac().getFac() , nd );
			final SquareMatrixElem<NumDimensions,R,S> sel = sfac.zero();
			final R a = re;
			final R b = im;
			if( a != null ) sel.setVal(BigInteger.ZERO, BigInteger.ZERO, a);
			if( b != null ) sel.setVal(BigInteger.ONE, BigInteger.ZERO, b.negate());
			if( b != null ) sel.setVal(BigInteger.ZERO, BigInteger.ONE, b);
			if( a != null ) sel.setVal(BigInteger.ONE, BigInteger.ONE, a);
			
			final SquareMatrixElem<NumDimensions,R,S> seli = sel.invertLeft();
			final GeometricAlgebraMultivectorElemFactory<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gfac =
					new GeometricAlgebraMultivectorElemFactory<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S>( this.getFac().getFac() , nd , new GeometricAlgebraOrd<NumDimensions>() );
			final GeometricAlgebraMultivectorElem<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gvc =
					gfac.zero();
			
			final HashSet<BigInteger> gvcKey0 = new HashSet<BigInteger>();
			gvcKey0.add( BigInteger.ZERO );
			final HashSet<BigInteger> gvcKey1 = new HashSet<BigInteger>();
			gvcKey1.add( BigInteger.ONE );
			
			gvc.setVal(gvcKey0, this.getFac().getFac().identity());
			final GeometricAlgebraMultivectorElem<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gvo =
					gfac.zero();
			gvc.rowVectorMult(seli, gvo);
			
			final R c = gvo.getVal(gvcKey0 );
			final R d = gvo.getVal(gvcKey1 );
			return( new ComplexElem<R,S>( c , d ) );
		}
	}
	
	@Override
	public ComplexElem<R, S> invertRight() throws NotInvertibleException {
		if( re.getFac().isMultCommutative() )
		{
			final R denom = ( re.mult(re) ).add( im.mult(im) );
			final R div = denom.invertRight();
			final R c = re.mult( div );
			final R d = im.mult( div ).negate();
			return( new ComplexElem<R,S>( c , d ) );
		}
		else
		{
			final NumDimensions nd = new NumDimensions()
			{
				public BigInteger getVal()
				{
					return( BigInteger.valueOf( 2 ) );
				}
			};
			final SquareMatrixElemFactory<NumDimensions,R,S> sfac = 
					new SquareMatrixElemFactory<NumDimensions,R,S>( this.getFac().getFac() , nd );
			final SquareMatrixElem<NumDimensions,R,S> sel = sfac.zero();
			final R a = re;
			final R b = im;
			if( a != null ) sel.setVal(BigInteger.ZERO, BigInteger.ZERO, a);
			if( b != null ) sel.setVal(BigInteger.ONE, BigInteger.ZERO, b.negate());
			if( b != null ) sel.setVal(BigInteger.ZERO, BigInteger.ONE, b);
			if( a != null ) sel.setVal(BigInteger.ONE, BigInteger.ONE, a);
			
			final SquareMatrixElem<NumDimensions,R,S> seli = sel.handleOptionalOp(SquareMatrixElem.SquareMatrixCmd.INVERT_LEFT_REV_COEFF, null);
			final GeometricAlgebraMultivectorElemFactory<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gfac =
					new GeometricAlgebraMultivectorElemFactory<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S>( this.getFac().getFac() , nd , new GeometricAlgebraOrd<NumDimensions>() );
			final GeometricAlgebraMultivectorElem<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gvc =
					gfac.zero();
			
			final HashSet<BigInteger> gvcKey0 = new HashSet<BigInteger>();
			gvcKey0.add( BigInteger.ZERO );
			final HashSet<BigInteger> gvcKey1 = new HashSet<BigInteger>();
			gvcKey1.add( BigInteger.ONE );
			
			gvc.setVal(gvcKey0, this.getFac().getFac().identity());
			final GeometricAlgebraMultivectorElem<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gvo =
					gfac.zero();
			gvc.rowVectorMult(seli, gvo);
			
			final R c = gvo.getVal(gvcKey0 );
			final R d = gvo.getVal(gvcKey1 );
			return( new ComplexElem<R,S>( c , d ) );
		}
	}
	
	/**
	 * Returns the left-side conjugate of the elem.
	 * 
	 * @return The left-side conjugate of the elem.
	 * @throws NotInvertibleException
	 */
	private ComplexElem<R, S> conjugateLeft() throws NotInvertibleException { 
		if( re.getFac().isMultCommutative() )
		{
			final R c = re;
			final R d = im.negate();
			return( new ComplexElem<R,S>( c , d ) );
		}
		else
		{
			final NumDimensions nd = new NumDimensions()
			{
				public BigInteger getVal()
				{
					return( BigInteger.valueOf( 2 ) );
				}
			};
			final SquareMatrixElemFactory<NumDimensions,R,S> sfac = 
					new SquareMatrixElemFactory<NumDimensions,R,S>( this.getFac().getFac() , nd );
			final SquareMatrixElem<NumDimensions,R,S> sel = sfac.zero();
			final R a = re;
			final R b = im;
			if( a != null ) sel.setVal(BigInteger.ZERO, BigInteger.ZERO, a);
			if( b != null ) sel.setVal(BigInteger.ONE, BigInteger.ZERO, b.negate());
			if( b != null ) sel.setVal(BigInteger.ZERO, BigInteger.ONE, b);
			if( a != null ) sel.setVal(BigInteger.ONE, BigInteger.ONE, a);
			
			final SquareMatrixElem<NumDimensions,R,S> seli = sel.invertLeft();
			final GeometricAlgebraMultivectorElemFactory<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gfac =
					new GeometricAlgebraMultivectorElemFactory<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S>( this.getFac().getFac() , nd , new GeometricAlgebraOrd<NumDimensions>() );
			final GeometricAlgebraMultivectorElem<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gvc =
					gfac.zero();
			
			final HashSet<BigInteger> gvcKey0 = new HashSet<BigInteger>();
			gvcKey0.add( BigInteger.ZERO );
			final HashSet<BigInteger> gvcKey1 = new HashSet<BigInteger>();
			gvcKey1.add( BigInteger.ONE );
			
			gvc.setVal(gvcKey0, ( a.mult(a) ).add( b.mult(b) ) );
			final GeometricAlgebraMultivectorElem<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gvo =
					gfac.zero();
			gvc.rowVectorMult(seli, gvo);
			
			final R c = gvo.getVal(gvcKey0 );
			final R d = gvo.getVal(gvcKey1 );
			return( new ComplexElem<R,S>( c , d ) );
		}
	}
	
	/**
	 * Returns the right-side conjugate of the elem.
	 * 
	 * @return The right-side conjugate of the elem.
	 * @throws NotInvertibleException
	 */
	private ComplexElem<R, S> conjugateRight() throws NotInvertibleException { 
		if( re.getFac().isMultCommutative() )
		{
			final R c = re;
			final R d = im.negate();
			return( new ComplexElem<R,S>( c , d ) );
		}
		else
		{
			final NumDimensions nd = new NumDimensions()
			{
				public BigInteger getVal()
				{
					return( BigInteger.valueOf( 2 ) );
				}
			};
			final SquareMatrixElemFactory<NumDimensions,R,S> sfac = 
					new SquareMatrixElemFactory<NumDimensions,R,S>( this.getFac().getFac() , nd );
			final SquareMatrixElem<NumDimensions,R,S> sel = sfac.zero();
			final R a = re;
			final R b = im;
			if( a != null ) sel.setVal(BigInteger.ZERO, BigInteger.ZERO, a);
			if( b != null ) sel.setVal(BigInteger.ONE, BigInteger.ZERO, b.negate());
			if( b != null ) sel.setVal(BigInteger.ZERO, BigInteger.ONE, b);
			if( a != null ) sel.setVal(BigInteger.ONE, BigInteger.ONE, a);
			
			final SquareMatrixElem<NumDimensions,R,S> seli = sel.handleOptionalOp(SquareMatrixElem.SquareMatrixCmd.INVERT_LEFT_REV_COEFF, null);
			final GeometricAlgebraMultivectorElemFactory<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gfac =
					new GeometricAlgebraMultivectorElemFactory<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S>( this.getFac().getFac() , nd , new GeometricAlgebraOrd<NumDimensions>() );
			final GeometricAlgebraMultivectorElem<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gvc =
					gfac.zero();
			
			final HashSet<BigInteger> gvcKey0 = new HashSet<BigInteger>();
			gvcKey0.add( BigInteger.ZERO );
			final HashSet<BigInteger> gvcKey1 = new HashSet<BigInteger>();
			gvcKey1.add( BigInteger.ONE );
			
			gvc.setVal(gvcKey0, ( a.mult(a) ).add( b.mult(b) ) );
			final GeometricAlgebraMultivectorElem<NumDimensions,GeometricAlgebraOrd<NumDimensions>,R,S> gvo =
					gfac.zero();
			gvc.rowVectorMult(seli, gvo);
			
			final R c = gvo.getVal(gvcKey0 );
			final R d = gvo.getVal(gvcKey1 );
			return( new ComplexElem<R,S>( c , d ) );
		}
	}

	@Override
	public ComplexElem<R, S> divideBy(BigInteger val) {
		return( new ComplexElem<R,S>( re.divideBy(val) , im.divideBy(val) ) );
	}
	
	
	@Override
	public ComplexElem<R, S> handleOptionalOp( Object id , ArrayList<ComplexElem<R, S>> args ) throws NotInvertibleException
	{
		if( id instanceof ComplexElem.ComplexCmd )
		{
			switch( (ComplexElem.ComplexCmd) id )
			{
				case CONJUGATE_LEFT:
				{
					return( conjugateLeft() );
				}
				// break;
				
				case CONJUGATE_RIGHT:
				{
					return( conjugateRight() );
				}
				// break;
			}
		}
		
		return( super.handleOptionalOp(id, args) );
	}

	
	@Override
	public ComplexElemFactory<R, S> getFac() {
		return( new ComplexElemFactory<R,S>( (S)( re.getFac() ) ) );
	}
	
	
	@Override
	public ComplexElem<R,S> cloneThread( final BigInteger threadIndex )
	{
		final R re2 = re.cloneThread(threadIndex);
		final R im2 = im.cloneThread(threadIndex);
		if( ( re2 != re ) || ( im2 != im ) )
		{
			return( new ComplexElem<R,S>( re2 , im2 ) );
		}
		return( this );
	}

	
	/**
	 * Constructs the elem.
	 * 
	 * @param _re The real component of the elem.
	 * @param _im The imaginary component of the elem.
	 */
	public ComplexElem( R _re , R _im )
	{
		re = _re;
		im = _im;
	}
	
	

	/**
	 * Gets the real component of the elem.
	 * 
	 * @return The real component of the elem.
	 */
	public R getRe() {
		return re;
	}

	/**
	 * Gets the imaginary component of the elem.
	 * 
	 * @return The imaginary component of the elem.
	 */
	public R getIm() {
		return im;
	}


	/**
	 * The real component of the elem.
	 */
	private R re;
	
	/**
	 * The imaginary component of the elem.
	 */
	private R im;

}

