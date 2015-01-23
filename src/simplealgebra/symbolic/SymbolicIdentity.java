




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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;

/**
 * A symbolic elem for the identity.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicIdentity<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicIdentity( S _fac )
	{
		super( _fac );
	}
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public SymbolicIdentity( S _fac , DroolsSession ds )
	{
		this( _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) {
		return( fac.identity() );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException
	{
		return( fac.zero() );
	}
	
	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		return( b );
	}
	
	@Override
	public SymbolicElem<R, S> invertLeft() throws NotInvertibleException {
		return( this );
	}
	
	@Override
	public SymbolicElem<R, S> invertRight() throws NotInvertibleException {
		return( this );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( false );
	}
	
	@Override
	protected boolean isSymbolicIdentity()
	{
		return( true );
	}

	@Override
	public void writeString( PrintStream ps ) {
		ps.println( "IDENTITY" );
	}
	
	@Override
	public void writeMathML( PrecedenceComparator<R,S> pc , PrintStream ps ) {
		ps.print( "<mn>IDENTITY</mn>" );
	}
	
	/**
	 * Returns true iff. the parameter is equal to the identity.
	 * 
	 * @param b The input parameter to test.
	 * @return True iff. the parameter is equal to the identity.
	 */
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		return( b instanceof SymbolicIdentity );
	}

}

