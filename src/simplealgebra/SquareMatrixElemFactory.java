




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

import simplealgebra.symbolic.SymbolicElem;

public class SquareMatrixElemFactory<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<SquareMatrixElem<U,R,S>, SquareMatrixElemFactory<U,R,S>> {

	
	public SquareMatrixElemFactory( S _fac , U _dim )
	{
		fac = _fac;
		dim = _dim;
	}
	
	
	@Override
	public SquareMatrixElem<U, R, S> identity() {
		final BigInteger max = dim.getVal();
		SquareMatrixElem<U, R, S> ret = new SquareMatrixElem<U, R, S>( fac , dim );
		BigInteger cnt = BigInteger.ZERO;
		for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			ret.setVal(cnt, cnt, fac.identity());
		}
		return( ret );
	}

	@Override
	public SquareMatrixElem<U, R, S> zero() {
		return( new SquareMatrixElem<U, R, S>( fac , dim ) );
	}
	
	
	@Override
	public SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>>> args )  throws NotInvertibleException
	{
		if( id instanceof SquareMatrixElem.SquareMatrixCmd )
		{
			switch( (SquareMatrixElem.SquareMatrixCmd) id )
			{
			
				case INVERT_LEFT_REV_COEFF:
				{
					SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> arg
						= args.get( 0 );
					return( new SymbolicInvertLeftRevCoeff<U,R,S>( arg , arg.getFac().getFac() ) );
				}
				// break;
				
				case INVERT_RIGHT_REV_COEFF:
				{
					SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> arg
						= args.get( 0 );
					return( new SymbolicInvertRightRevCoeff<U,R,S>( arg , arg.getFac().getFac() ) );
				}
				// break;
			
				case MULT_REV_COEFF:
				{
					SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> argA
						= args.get( 0 );
					SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> argB
						= args.get( 1 );
					return( new SymbolicMultRevCoeff<U,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case TRANSPOSE:
				{
					SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> arg
						= args.get( 0 );
					return( new SymbolicTranspose<U,R,S>( arg , arg.getFac().getFac() ) );
				}
				// break;
			}
		}
		
		return( super.handleSymbolicOptionalOp(id, args) );
	}
	
	@Override
	public boolean isMultCommutative()
	{
		return( false );
	}
	
	
	public S getFac()
	{
		return( fac );
	}
	
	
	private S fac;
	private U dim;

}



