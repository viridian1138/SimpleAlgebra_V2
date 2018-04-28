


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
import java.util.HashSet;
import java.util.Iterator;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicElem.EVAL_MODE;

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
 * @param <R> The enclosed elem type.
 * @param <S> The factory for the enclosed elem type.
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
	public ComplexElem<R, S> random( PrimitiveRandom in ) {
		return( new ComplexElem<R,S>( re.random(in) , im.random(in) ) );
	}
	
	
	/**
	 * Produces one possible initial natural logarithm approximation.
	 * @param comb Possibility number ranging from zero to 3.
	 * @param currentComb The current unit guess to integrate.
	 * @param numIterExp The number of iterations for the underlying exponential approximation.
	 * @return One possible initial natural logarithm approximation.
	 * @throws NotInvertibleException
	 */
	protected ComplexElem<R, S> estimateLnApprox( final int comb , final ComplexElem<R,S> currentComb , final int numIterExp ) throws NotInvertibleException
	{
		final R a0 = ( comb & 1 ) != 0 ? getRe() : getRe().negate();
		final R a1 = ( comb & 2 ) != 0 ? getIm() : getIm().negate();
		final R a = a0.add( a1 );
		
		final R ident = getFac().getFac().identity();
		
		final R r0 = currentComb.getIm();
		
		R stval0 = a.add( currentComb.getRe() );
		R stinit;
		
		do
		{
			stinit = stval0;
			stval0 = evalBetterLnApprox( new ComplexElem<R,S>( stval0 , r0 ) , 
					new ComplexElem<R,S>( stval0.divideBy( 2 ) , r0 ) , numIterExp ).getRe();
		}
		while( stval0 != stinit );
		
		
		final R mult2 = ident.add( ident );
		
		
		do
		{
			stinit = stval0;
			stval0 = evalBetterLnApprox( new ComplexElem<R,S>( stval0 , r0 ) , 
					new ComplexElem<R,S>( stval0.mult( mult2 ) , r0 ) , numIterExp ).getRe();
		}
		while( stval0 != stinit );
		
		
		final ComplexElem<R,S> stu = new ComplexElem<R,S>( stval0 , r0 );
		
		
		final ComplexElem<R,S> i1 = ( ( this ).mult( ( stu.negate().exp( numIterExp ) ) ) ).add( getFac().identity().negate() );
		final ComplexElem<R,S> i2 = i1.mult( i1 );
		final ComplexElem<R,S> i3 = i2.mult( i1 );
		final ComplexElem<R,S> val = i1.add( i2.divideBy( 2 ).negate() ).add( i3.divideBy( 3 ) );
		final ComplexElem<R,S> ret = evalBetterLnApprox( val.add( stu ) , stu , numIterExp );
		return( ret );
		
		
	}
	
	
	
	/**
	 * Returns a series approximation of asinh for the imaginary part of the complex number.
	 * See <A href="http://www.efunda.com/math/taylor_series/inverse_hyperbolic.cfm">http://www.efunda.com/math/taylor_series/inverse_hyperbolic.cfm</A>
	 * @return A series approximation of asinh for the imaginary part of the complex number.
	 * @throws NotInvertibleException
	 */
	protected ComplexElem<R, S> asinhPortionSeries( ) throws NotInvertibleException
	{
		
		final ComplexElem<R,S> x = new ComplexElem<R,S>( getFac().getFac().zero() , this.getIm() );
		
		final ComplexElem<R,S> x2 = x.mult( x );
		final ComplexElem<R,S> x3 = x2.mult( x );
		final ComplexElem<R,S> x4 = x3.mult( x );
		final ComplexElem<R,S> x5 = x4.mult( x );
		final ComplexElem<R,S> x6 = x5.mult( x );
		final ComplexElem<R,S> x7 = x6.mult( x );
		final ComplexElem<R,S> x8 = x7.mult( x );
		final ComplexElem<R,S> x9 = x8.mult( x );
		
		final ComplexElem<R,S> c3 = getFac().identity().divideBy( 1 ).invertLeft().divideBy( 6 ).negate();
		
		final ComplexElem<R,S> c5 = getFac().identity().divideBy( 3 ).invertLeft().divideBy( 40 );
		
		final ComplexElem<R,S> c7 = getFac().identity().divideBy( 5 ).invertLeft().divideBy( 112 ).negate();
		
		final ComplexElem<R,S> c9 = getFac().identity().divideBy( 35 ).invertLeft().divideBy( 1152 );
		
		final ComplexElem<R,S> ret = ( x ).add( c3.mult( x3 ) ).add( c5.mult( x5 ) ).add( c7.mult( x7 ) ).add( c9.mult( x9 ) );
		
		return( ret );
	}
	
	
	
	
	@Override
	protected ComplexElem<R, S> estimateLnApprox( final int numIterExp ) throws NotInvertibleException
	{
		
		ComplexElem<R, S> stval0 = getFac().identity();
		
		
		try
		{
			final R rin = re.ln( numIterExp , numIterExp );
			final ComplexElem<R,S> tst = new ComplexElem<R,S>( rin , getFac().getFac().zero() );
			stval0 = evalBetterLnApprox( stval0 , tst , numIterExp );
		}
		catch( NotInvertibleException ex ) 
		{
			// Do Nothing.
		}
		catch( MultiplicativeDistributionRequiredException ex ) 
		{
			// Do Nothing.
		}
		catch( BadCreationException ex )
		{
			// Do Nothing.
		}
		
		
		try
		{
			final R rin = ( re.negate() ).ln( numIterExp , numIterExp );
			final R PI = getFac().getFac().identity().divideBy( 314159265 ).invertLeft().divideBy( 100000000 );
			final ComplexElem<R,S> tst = new ComplexElem<R,S>( rin , PI );
			stval0 = evalBetterLnApprox( stval0 , tst , numIterExp );
		}
		catch( NotInvertibleException ex ) 
		{
			// Do Nothing.
		}
		catch( MultiplicativeDistributionRequiredException ex ) 
		{
			// Do Nothing.
		}
		catch( BadCreationException ex )
		{
			// Do Nothing.
		}
		
		
		
		final R magSq = re.mult( re ).add( im.mult( im ) );
		
		final ArrayList<ComplexElem<R,S>> magLns = new ArrayList<ComplexElem<R,S>>();
		
		
		
		try
		{
			final R rin = magSq.ln( numIterExp , numIterExp );
			final ComplexElem<R,S> tst = new ComplexElem<R,S>( rin , getFac().getFac().zero() );
			magLns.add( tst );
		}
		catch( NotInvertibleException ex ) 
		{
			// Do Nothing.
		}
		catch( MultiplicativeDistributionRequiredException ex ) 
		{
			// Do Nothing.
		}
		catch( BadCreationException ex )
		{
			// Do Nothing.
		}
		
		
		
		try
		{
			final R rin = ( magSq.negate() ).ln( numIterExp , numIterExp );
			final R PI = getFac().getFac().identity().divideBy( 314159265 ).invertLeft().divideBy( 100000000 );
			final ComplexElem<R,S> tst = new ComplexElem<R,S>( rin , PI );
			magLns.add( tst );
		}
		catch( NotInvertibleException ex ) 
		{
			// Do Nothing.
		}
		catch( MultiplicativeDistributionRequiredException ex ) 
		{
			// Do Nothing.
		}
		catch( BadCreationException ex )
		{
			// Do Nothing.
		}
		
	
		final ArrayList<ComplexElem<R,S>[]> dirs = new ArrayList<ComplexElem<R,S>[]>();
		
		for( final ComplexElem<R,S> magLn : magLns )
		{
			ComplexElem<R,S> multp = ( magLn.divideBy( 2 ).negate() ).exp( numIterExp );
			ComplexElem[] dirA = { magLn , multp.mult( this ) };
			dirs.add( (ComplexElem<R,S>[]) dirA );
			if( !( getFac().isMultCommutative() ) )
			{
				ComplexElem[] dirB = { magLn , this.mult( multp ) };
				dirs.add( (ComplexElem<R,S>[]) dirB );
			}
			
		}
		
		
		for( final ComplexElem<R,S>[] dirl : dirs )
		{
			final ComplexElem<R,S> magLn = dirl[ 0 ];
			final ComplexElem<R,S> dir = dirl[ 1 ];
			final ComplexElem<R,S> refAng = dir.asinhPortionSeries( );
			
			stval0 = evalBetterLnApprox( stval0 , magLn.add( refAng ) , numIterExp );
			
			final R PI = getFac().getFac().identity().divideBy( 314159265 ).invertLeft().divideBy( 100000000 );
			final ComplexElem<R,S> iPI = new ComplexElem<R,S>( getFac().getFac().zero() , PI );
			
			stval0 = evalBetterLnApprox( stval0 , magLn.add( iPI ).add( refAng.negate() ) , numIterExp );
			
		}
		
		
		return( stval0 );
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
	public Elem<?,?> totalMagnitude()
	{
		final Elem r1 = re.totalMagnitude();
		final Elem r2 = im.totalMagnitude();
		return( r1.add( r2 ) );
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
	
	
	@Override
	public ComplexElem<R,S> cloneThreadCached( final BigInteger threadIndex , final CloneThreadCache<ComplexElem<R,S>,ComplexElemFactory<R,S>> cache )
	{
		final ComplexElem<R,S> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		final R re2 = re.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		final R im2 = im.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( ( re2 != re ) || ( im2 != im ) )
		{
			final ComplexElem<R,S> rtmp = new ComplexElem<R,S>( re2 , im2 );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		re.performInserts( session );
		im.performInserts( session );
		super.performInserts( session );
	}
	
	
	@Override
	public boolean evalSymbolicZeroApprox( EVAL_MODE mode )
	{
		return( ( re.evalSymbolicZeroApprox(mode) ) && ( im.evalSymbolicZeroApprox(mode) ) );
	}
	
	
	@Override
	public boolean evalSymbolicIdentityApprox( EVAL_MODE mode )
	{
		return( ( re.evalSymbolicIdentityApprox(mode) ) && ( im.evalSymbolicZeroApprox(mode) ) );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		if( pc.parenNeeded( this ,  re , false ) )
		{
			pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		}
		else
		{
			ps.print( "<mrow>" );
		}
		re.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  re , false ) )
		{
			pc.getParenthesisGenerator().handleParenthesisClose(ps);
		}
		else
		{
			ps.print( "</mrow>" );
		}
		ps.print( "<mo>+</mo>" );
		if( pc.parenNeeded( this ,  im , true ) )
		{
			pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		}
		else
		{
			ps.print( "<mrow>" );
		}
		im.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  im , true ) )
		{
			pc.getParenthesisGenerator().handleParenthesisClose(ps);
		}
		else
		{
			ps.print( "</mrow>" );
		}
		ps.print( "<mo>&InvisibleTimes;</mo>" );
		ps.print( "<mi>&ImaginaryI;</mi>" );
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<ComplexElem<R,S>,ComplexElemFactory<R,S>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			final String res = re.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			final String ims = im.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			this.getFac().writeElemTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			this.getFac().writeElemTypeString( ps );
			ps.print( "( " );
			ps.print( res );
			ps.print( " , " );
			ps.print( ims );
			ps.println( " );" );
		}
		return( st );
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

