





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
import java.math.BigInteger;

/**
 * A symbolic elem for the zero value.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicZero<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicZero( S _fac )
	{
		super( _fac );
	}
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
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
	public boolean isSymbolicZero()
	{
		return( true );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException
	{
		return( fac.zero() );
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( false );
	}
	
	
	@Override
	public boolean isPartialDerivativeZero()
	{
		return( true );
	}
	

	@Override
	public void writeString( PrintStream ps ) {
		ps.println( "ZERO" );
	}
	
	@Override
	public void writeMathML( PrecedenceComparator<R,S> pc , PrintStream ps ) {
		ps.print( "<mn>ZERO</mn>" );
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
	
	/**
	 * Returns true iff. the parameter equals this element.
	 * 
	 * @param The parameter to check.
	 * @return True iff. the parameter equals this element.
	 */
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		return( b instanceof SymbolicZero );
	}
	
	/**
	 * Returns true iff. the parameter is an instance of SymbolicZero.
	 * 
	 * @param in The parameter to check.
	 * @return True iff. the parameter is an instance of SymbolicZero.
	 */
	public static boolean isSymbolicZero( SymbolicElem in )
	{
		return( in instanceof SymbolicZero );
	}
	
	@Override
	public boolean evalSymbolicConstant()
	{
		return( true );
	}
	
	@Override
	public SymbolicElem<R, S> add(SymbolicElem<R, S> b) {
		// This simplification has a parallel implementation in the "Add Zero A" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( b );
	}

	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		// This simplification has a parallel implementation in the "Mult Zero A" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( this );
	}

	@Override
	public SymbolicElem<R, S> negate() {
		// This simplification has a parallel implementation in the "Negate Zero" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( this );
	}

	@Override
	public SymbolicElem<R, S> divideBy(BigInteger val) {
		// This simplification has a parallel implementation in the "DivideBy OF Zero --> Zero" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( BigInteger.ZERO.compareTo( val ) != 0 ? this : super.divideBy( val ) );
	}

}

