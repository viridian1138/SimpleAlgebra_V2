



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

import java.util.ArrayList;


public abstract class Elem<T extends Elem<T,?>, R extends ElemFactory<T,R>> {

	public abstract T add( T b );
	
	public abstract T mult( T b );
	
	public abstract T negate( );
	
	public abstract T invertLeft( ) throws NotInvertibleException;
	
	public abstract T invertRight( ) throws NotInvertibleException;
	
	public abstract T divideBy( int val );
	
	public abstract R getFac();
	
	public void validate() throws RuntimeException
	{
	}
	
	public T handleOptionalOp( Object id , ArrayList<T> args ) throws NotInvertibleException
	{
		throw( new RuntimeException( "Operation Not Supported" ) );
	}
	
}

