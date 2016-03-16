



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

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.AbstractCache;
import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteNumDimensionsCache;
import simplealgebra.ddx.WriteDirectionalDerivativePartialFactoryCache;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Symbolic elem. for returning either symmetric or antisymmetric
 * portions of a rank-two tensor.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the tensor indices.
 * @param <U> The number of dimensions.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicTensorResym<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> 
{
	
	/**
	 * The resym type.
	 * 
	 * @author thorngreen
	 *
	 */
	public static enum ResymType
	{
		/**
		 * Resym type to return the symmetric portion of the tensor.
		 */
		RESYM_SYMMETRIC,
		/**
		 * Resym type to return the antisymmetric portion of the tensor.
		 */
		RESYM_ANTISYMMETRIC
	};

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The argument.
	 * @param _fac The factory for the enclosed type.
	 * @param _reSym The resym type.
	 * @param _dim The number of dimensions.
	 */
	public SymbolicTensorResym( SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> _elem , 
			EinsteinTensorElemFactory<Z,R,S> _fac , ResymType _reSym , U _dim )
	{
		super( _fac );
		elem = _elem;
		reSym = _reSym;
		dim = _dim;
	}
	
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elem The argument.
	 * @param _fac The factory for the enclosed type.
	 * @param _reSym The resym type.
	 * @param _dim The number of dimensions.
	 * @param ds The Drools session.
	 */
	public SymbolicTensorResym( SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> _elem , 
			EinsteinTensorElemFactory<Z,R,S> _fac , ResymType _reSym , U _dim , DroolsSession ds )
	{
		this( _elem , _fac , _reSym , _dim );
		ds.insert( this );
	}
	
	
	@Override
	public EinsteinTensorElem<Z,R,S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		EinsteinTensorElem<Z,R,S> tmp = elem.eval( implicitSpace );
		final SquareMatrixElem<U,R,S> trnsI = new SquareMatrixElem<U,R,S>( tmp.getFac().getFac() , dim );
		tmp.rankTwoTensorToSquareMatrix( trnsI );
		final ArrayList<SquareMatrixElem<U,R,S>> args = new ArrayList<SquareMatrixElem<U,R,S>>();
		final SquareMatrixElem<U,R,S> trnsO = trnsI.handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.TRANSPOSE , args );
		final EinsteinTensorElem<Z,R,S> tmpT = new EinsteinTensorElem<Z,R,S>( 
				tmp.getFac().getFac() , tmp.getContravariantIndices() , tmp.getCovariantIndices() );
		trnsO.toRankTwoTensor( tmpT );
		tmp = tmp.add( reSym == ResymType.RESYM_SYMMETRIC ? tmpT : tmpT.negate() );
		tmp = tmp.divideBy( 2 );
		return( tmp );
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>, EinsteinTensorElem<Z, R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>> key =
				new SCacheKey<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>( this , implicitSpace );
		final EinsteinTensorElem<Z, R, S> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		EinsteinTensorElem<Z,R,S> tmp = elem.evalCached( implicitSpace , cache );
		final SquareMatrixElem<U,R,S> trnsI = new SquareMatrixElem<U,R,S>( tmp.getFac().getFac() , dim );
		tmp.rankTwoTensorToSquareMatrix( trnsI );
		final ArrayList<SquareMatrixElem<U,R,S>> args = new ArrayList<SquareMatrixElem<U,R,S>>();
		final SquareMatrixElem<U,R,S> trnsO = trnsI.handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.TRANSPOSE , args );
		final EinsteinTensorElem<Z,R,S> tmpT = new EinsteinTensorElem<Z,R,S>( 
				tmp.getFac().getFac() , tmp.getContravariantIndices() , tmp.getCovariantIndices() );
		trnsO.toRankTwoTensor( tmpT );
		tmp = tmp.add( reSym == ResymType.RESYM_SYMMETRIC ? tmpT : tmpT.negate() );
		tmp = tmp.divideBy( 2 );
		cache.put(key, tmp);
		return( tmp );
	}
	
	
	@Override
	public EinsteinTensorElem<Z,R,S> evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>, EinsteinTensorElem<Z, R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elem.exposesDerivatives() );
	}
	
	
	@Override
	public SymbolicTensorResym<Z,U,R,S> cloneThread( final BigInteger threadIndex )
	{
		// The NumDimensions dim is presumed to be immutable.
		final SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> elems = elem.cloneThread(threadIndex);
		final EinsteinTensorElemFactory<Z,R,S> facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elems != elem ) || ( facs != this.getFac().getFac() ) )
		{
			return( new SymbolicTensorResym<Z,U,R,S>( elems , facs , reSym , dim ) );
		}
		return( this );
	}
	
	
	@Override
	public SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>, SymbolicElemFactory<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>> cache) {
		final SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		// The NumDimensions dim is presumed to be immutable.
		final SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> 
			elems = elem.cloneThreadCached( threadIndex , cache );
		final EinsteinTensorElemFactory<Z,R,S> facs = this.getFac().getFac().cloneThreadCached( threadIndex , (CloneThreadCache)( cache.getInnerCache() ) );
		if( ( elems != elem ) || ( facs != this.getFac().getFac() ) )
		{
			final SymbolicTensorResym<Z,U,R,S> rtmp = new SymbolicTensorResym<Z,U,R,S>( elems , facs , reSym , dim );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}
	

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>, SymbolicElemFactory<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>> cache,
			PrintStream ps) 
	{
		String st = cache.get( this );
		if( st == null )
		{
			cache.applyAuxCache( new WriteNumDimensionsCache( cache.getCacheVal() ) );
			final String elemAs = elem.writeDesc( cache , ps);
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			final String staDim = dim.writeDesc( (WriteNumDimensionsCache)( cache.getAuxCache( WriteNumDimensionsCache.class ) ) , ps);
			String sl = cache.getIncrementVal();
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( SymbolicTensorResym.class.getSimpleName() );
			ps.print( "<? extends Object," );
			dim.writeTypeString(ps);
			ps.print( "," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( ">" );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( SymbolicTensorResym.class.getSimpleName() );
			ps.print( "<? extends Object," );
			dim.writeTypeString(ps);
			ps.print( "," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( ">" );
			ps.print( "( " );
			ps.print( elemAs );
			ps.print( " , " );
			ps.print( facs );
			ps.print( " , " );
			ps.print( "ResymType." );
			ps.print( "" + reSym );
			ps.print( " , " );
			ps.print( staDim );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elem.performInserts( session );
		super.performInserts( session );
	}
	
	
	/**
	 * Gets the number of dimensions.
	 * 
	 * @return The number of dimensions.
	 */
	public U getDim() {
		return dim;
	}

	/**
	 * Gets the type of resym to perform.
	 * 
	 * @return The type of resym to perform.
	 */
	public ResymType getReSym() {
		return reSym;
	}

	/**
	 * Gets the argument to be operated upon.
	 * 
	 * @return The argument to be operated upon.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>> getElem() {
		return elem;
	}


	/**
	 * The number of dimensions.
	 */
	private U dim;
	
	/**
	 * The type of resym to perform.
	 */
	private ResymType reSym;
	
	/**
	 * The argument to be operated upon.
	 */
	private SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> elem;

}

