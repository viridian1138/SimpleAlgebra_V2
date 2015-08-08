



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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;


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
	public EinsteinTensorElem<Z,R,S> evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
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
	public void writeString( PrintStream ps ) {
		ps.print( "symbolicTensorResym" );
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

