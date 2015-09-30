




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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.Sqrt;

/**
 * A symbolic elem for a square root.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicSqrt<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The enclosed elem.
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicSqrt( SymbolicElem<R,S> _elem , S _fac )
	{
		super( _fac );
		elem = _elem;
	}
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elem The enclosed elem.
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public SymbolicSqrt( SymbolicElem<R,S> _elem , S _fac , DroolsSession ds )
	{
		this( _elem , _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		final R el = elem.eval( implicitSpace );
		if( el instanceof SymbolicElem )
		{
			final SymbolicElem<?,?> elemA = (SymbolicElem<?,?>) el;
			final SymbolicElemFactory<?,?> elemAfac = (SymbolicElemFactory<?,?>)( this.getFac() );
			final SymbolicSqrt<?,?> ret = new SymbolicSqrt( elemA , elemAfac.getFac() );
			return( (R) ret );
		}
		else
		{
			ArrayList<R> args = new ArrayList<R>();
			return( el.handleOptionalOp( Sqrt.SQRT , args ) );
		}
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final R sqrtV = this.eval( implicitSpace );
		final R dv = elem.evalPartialDerivative( withRespectTo , implicitSpace );
		return( ( sqrtV.invertLeft() ).mult( dv ).divideBy( BigInteger.valueOf( 2 ) ) );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elem.exposesDerivatives() );
	}
	
	
	@Override
	public SymbolicSqrt<R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<R,S> elems = elem.cloneThread( threadIndex );
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elems != elem ) || ( facs != fac ) )
		{
			return( new SymbolicSqrt<R,S>( elems , facs ) );
		}
		return( this );
	}

	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "sqrt( " );
		elem.writeString( ps );
		ps.print( " )" );
	}
	
	@Override
	public void writeMathML( PrecedenceComparator<R,S> pc , PrintStream ps )
	{
		ps.print( "<msqrt><mrow>" );
		elem.writeMathML(pc, ps);
		ps.print( "</mrow></msqrt>" );
	}
	
	/**
	 * Gets the enclosed elem.
	 * 
	 * @return The enclosed elem.
	 */
	public SymbolicElem<R, S> getElem() {
		return elem;
	}
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		return( elem.evalSymbolicConstantApprox() );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		if( b instanceof SymbolicSqrt )
		{
			return( elem.symbolicEquals( ((SymbolicSqrt<R,S>) b).getElem() ) );
		}
		
		return( false );
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elem.performInserts( session );
		super.performInserts( session );
	}


	/**
	 * The enclosed elem.
	 */
	private SymbolicElem<R,S> elem;

}

