




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





package simplealgebra.ga;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicElem;


/**
 * Symbolic elem. for the Geometric Algebra right-reverse <math display="inline">
 * <mrow>
 *   <msup>
 *   <mrow>
 *   <mi>A</mi>
 *   </mrow>
 *   <mo>&dagger;R</mo></msup>
 * </mrow>
 * </math> over multivectors.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions.
 * @param <A> The ord of the algebra.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicReverseRight<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elemA The enclosed elem.
	 * @param _fac The factory for the enclosed elem.
	 */
	public SymbolicReverseRight( 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> _elemA , 
			GeometricAlgebraMultivectorElemFactory<U,A, R, S> _fac) 
	{
		super( _fac );
		elemA = _elemA;
	}
	
	
	/**
	 * Constructs the elem. for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elemA The enclosed elem.
	 * @param _fac The factory for the enclosed elem.
	 * @param ds The Drools session.
	 */
	public SymbolicReverseRight( 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> _elemA , 
			GeometricAlgebraMultivectorElemFactory<U,A, R, S> _fac , DroolsSession ds ) 
	{
		this( _elemA , _fac );
		ds.insert( this );
	}

	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>> args = new ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>>();
		return( elemA.eval( implicitSpace ).handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.REVERSE_RIGHT , args ) );
	}

	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elemA.exposesDerivatives() );
	}

	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "reverseRight( " );
		elemA.writeString( ps );
		ps.print( " )" );
	}
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>> pc,
			PrintStream ps) 
	{
		ps.print( "<msup>" );
		if( pc.parenNeeded( this ,  elemA , false ) )
		{
			ps.print( "<mfenced><mrow>" );
		}
		else
		{
			ps.print( "<mrow>" );
		}
		elemA.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  elemA , false ) )
		{
			ps.print( "</mrow></mfenced>" );
		}
		else
		{
			ps.print( "</mrow>" );
		}
		ps.print( "<mo>&dagger;R</mo></msup>" );
	}
	
	
	/**
	 * Gets the enclosed elem.
	 * 
	 * @return The enclosed elem.
	 */
	public SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> getElemA() {
		return elemA;
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elemA.performInserts( session );
		super.performInserts( session );
	}

	
	/**
	 * The enclosed elem.
	 */
	private SymbolicElem<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> elemA;
}

