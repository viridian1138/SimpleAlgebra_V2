




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

import java.util.ArrayList;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;

public class SymbolicReverseLeft<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<GeometricAlgebraMultivectorElem<U,R,S>,GeometricAlgebraMultivectorElemFactory<U,R,S>> 
{

	public SymbolicReverseLeft( 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,R,S>,GeometricAlgebraMultivectorElemFactory<U,R,S>> _elemA , 
			GeometricAlgebraMultivectorElemFactory<U, R, S> _fac) 
	{
		super( _fac );
		elemA = _elemA;
	}
	
	
	public SymbolicReverseLeft( 
			SymbolicElem<GeometricAlgebraMultivectorElem<U,R,S>,GeometricAlgebraMultivectorElemFactory<U,R,S>> _elemA , 
			GeometricAlgebraMultivectorElemFactory<U, R, S> _fac, DroolsSession ds ) 
	{
		this( _elemA , _fac );
		ds.insert( this );
	}

	
	@Override
	public GeometricAlgebraMultivectorElem<U, R, S> eval() throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		ArrayList<GeometricAlgebraMultivectorElem<U,R,S>> args = new ArrayList<GeometricAlgebraMultivectorElem<U,R,S>>();
		return( elemA.eval().handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.REVERSE_LEFT , args ) );
	}

	
	@Override
	public GeometricAlgebraMultivectorElem<U, R, S> evalPartialDerivative(
			ArrayList<Elem<?, ?>> withRespectTo) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}

	
	@Override
	public String writeString( ) {
		return( "reverseLeft( " + ( elemA.writeString() ) + " )" );
	}
	
	
	/**
	 * @return the elem
	 */
	public SymbolicElem<GeometricAlgebraMultivectorElem<U,R,S>,GeometricAlgebraMultivectorElemFactory<U,R,S>> getElemA() {
		return elemA;
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elemA.performInserts( session );
		super.performInserts( session );
	}

	
	private SymbolicElem<GeometricAlgebraMultivectorElem<U,R,S>,GeometricAlgebraMultivectorElemFactory<U,R,S>> elemA;
}

