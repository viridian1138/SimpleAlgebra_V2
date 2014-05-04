




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





package simplealgebra.symbolic;

import java.util.ArrayList;

import simplealgebra.AbsoluteValue;
import simplealgebra.DoubleElem;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;

public abstract class SymbolicElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends Elem<SymbolicElem<R,S>, SymbolicElemFactory<R,S>> {

	abstract public R eval( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	abstract public R evalPartialDerivative( ArrayList<Elem<?,?>> withRespectTo ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	abstract public String writeString( );
	
	
	public SymbolicElem( S _fac )
	{
		fac = _fac;
	}

	
	@Override
	public SymbolicElem<R, S> add(SymbolicElem<R, S> b) {
		return( new SymbolicAdd<R,S>( this , b , fac ) );
	}

	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		return( new SymbolicMult<R,S>( this , b , fac ) );
	}

	@Override
	public SymbolicElem<R, S> negate() {
		return( new SymbolicNegate<R,S>( this , fac ) );
	}

	@Override
	public SymbolicElem<R, S> invertLeft() throws NotInvertibleException {
		return( new SymbolicInvertLeft<R,S>( this , fac ) );
	}
	
	@Override
	public SymbolicElem<R, S> invertRight() throws NotInvertibleException {
		return( new SymbolicInvertRight<R,S>( this , fac ) );
	}

	@Override
	public SymbolicElem<R, S> divideBy(int val) {
		return( new SymbolicDivideBy<R,S>( this , fac , val ) );
	}

	@Override
	public SymbolicElemFactory<R, S> getFac() {
		return( new SymbolicElemFactory<R,S>( fac ) );
	}
	
	@Override
	public SymbolicElem<R, S> handleOptionalOp( Object id , ArrayList<SymbolicElem<R, S>> args ) throws NotInvertibleException
	{
		if( id instanceof SymbolicOps )
		{
			switch( (SymbolicOps) id )
			{
				case DISTRIBUTE_SIMPLIFY:
				{
					return( this );
				}
				// break;
			}
		}
		
		return( getFac().getFac().handleSymbolicOptionalOp(id, args) );
	}
	
	
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		throw( new RuntimeException( "Not Supported " + this ) );
	}
	
	
	protected SymbolicElem<R,S> distSimp( ) throws NotInvertibleException
	{
		return( this.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null) );
	}
	
	
	protected S fac;
	
}

