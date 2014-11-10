




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




package simplealgebra.stime;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;

public class SpacetimeAlgebraMultivectorElemFactory<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<SpacetimeAlgebraMultivectorElem<U,R,S>, SpacetimeAlgebraMultivectorElemFactory<U,R,S>> {

	
	public SpacetimeAlgebraMultivectorElemFactory( S _fac , U _dim )
	{
		fac = _fac;
		dim = _dim;
	}
	
	
	@Override
	public SpacetimeAlgebraMultivectorElem<U, R, S> identity() {
		SpacetimeAlgebraMultivectorElem<U, R, S> ret = new SpacetimeAlgebraMultivectorElem<U, R, S>( fac , dim );
		ret.setVal( new HashSet<BigInteger>(), fac.identity() );
		return( ret );
	}

	@Override
	public SpacetimeAlgebraMultivectorElem<U, R, S> zero() {
		return( new SpacetimeAlgebraMultivectorElem<U, R, S>( fac , dim ) );
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
	
	
	
	public S getFac()
	{
		return( fac );
	}
	
	
	@Override
	public SymbolicElem<SpacetimeAlgebraMultivectorElem<U, R, S>, SpacetimeAlgebraMultivectorElemFactory<U,R,S>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<SpacetimeAlgebraMultivectorElem<U, R, S>, SpacetimeAlgebraMultivectorElemFactory<U,R,S>>> args )  throws NotInvertibleException
	{
		if( id instanceof SpacetimeAlgebraMultivectorElem.SpacetimeAlgebraMultivectorCmd )
		{
			switch( (SpacetimeAlgebraMultivectorElem.SpacetimeAlgebraMultivectorCmd) id )
			{
				case DOT:
				{
					SymbolicElem<SpacetimeAlgebraMultivectorElem<U, R, S>, SpacetimeAlgebraMultivectorElemFactory<U,R,S>> argA
						= args.get( 0 );
					SymbolicElem<SpacetimeAlgebraMultivectorElem<U, R, S>, SpacetimeAlgebraMultivectorElemFactory<U,R,S>> argB
						= args.get( 1 );
					return( new SymbolicDot<U,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case WEDGE:
				{
					SymbolicElem<SpacetimeAlgebraMultivectorElem<U, R, S>, SpacetimeAlgebraMultivectorElemFactory<U,R,S>> argA
						= args.get( 0 );
					SymbolicElem<SpacetimeAlgebraMultivectorElem<U, R, S>, SpacetimeAlgebraMultivectorElemFactory<U,R,S>> argB
						= args.get( 1 );
					return( new SymbolicWedge<U,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case REVERSE_LEFT:
				{
					SymbolicElem<SpacetimeAlgebraMultivectorElem<U, R, S>, SpacetimeAlgebraMultivectorElemFactory<U,R,S>> argA
						= args.get( 0 );
					return( new SymbolicReverseLeft<U,R,S>( argA , argA.getFac().getFac() ) );
				}
				// break;
				
				case REVERSE_RIGHT:
				{
					SymbolicElem<SpacetimeAlgebraMultivectorElem<U, R, S>, SpacetimeAlgebraMultivectorElemFactory<U,R,S>> argA
						= args.get( 0 );
					return( new SymbolicReverseRight<U,R,S>( argA , argA.getFac().getFac() ) );
				}
				// break;
				
			}
		}
		
		return( super.handleSymbolicOptionalOp(id, args) );
	}
	
	
	private S fac;
	private U dim;

}



