





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
import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;

public class SymbolicZero<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	public SymbolicZero( S _fac )
	{
		super( _fac );
	}
	
	public SymbolicZero( S _fac , DroolsSession ds )
	{
		this( _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) {
		return( fac.zero() );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException
	{
		return( fac.zero() );
	}
	

	@Override
	public String writeString( ) {
		return( "ZERO" );
	}
	
	@Override
	public SymbolicElem<R,S> invertLeft( ) throws NotInvertibleException
	{
		throw( new NotInvertibleException() );
	}
	
	@Override
	public SymbolicElem<R,S> invertRight( ) throws NotInvertibleException
	{
		throw( new NotInvertibleException() );
	}
	
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		return( b instanceof SymbolicZero );
	}
	
	public static boolean isSymbolicZero( SymbolicElem in )
	{
		return( in instanceof SymbolicZero );
	}
	
	@Override
	public SymbolicElem<R, S> add(SymbolicElem<R, S> b) {
		return( b );
	}

	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		return( this );
	}

	@Override
	public SymbolicElem<R, S> negate() {
		return( this );
	}

	@Override
	public SymbolicElem<R, S> divideBy(int val) {
		return( this );
	}

}

