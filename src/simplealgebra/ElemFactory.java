



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
import java.util.HashMap;

import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;

public abstract class ElemFactory<T extends Elem<T,?>, R extends ElemFactory<T,R>> {

	public abstract T identity();
	
	public abstract T zero();
	
	public T negativeIdentity()
	{
		return( identity().negate() );
	}
	
	public SymbolicElem<T, R> handleSymbolicOptionalOp( Object id , ArrayList<SymbolicElem<T, R>> args ) throws NotInvertibleException
	{
		throw( new RuntimeException( "Operation Not Supported" ) );
	}
	
	public abstract boolean isMultCommutative();
	
	public abstract boolean isNestedMultCommutative();
	
	
	protected Elem<?,?> simplePartialInverse( SymbolicElem<?,?> elem , ArrayList<Elem<?,?>> withRespectTo , HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final Elem<?,?> deriv = elem.evalPartialDerivative(withRespectTo, implicitSpace);
		final SymbolicElem<?,?> term = ( ( (SymbolicElem) elem ).mult( (SymbolicElem) elem ) ).invertLeft().negate();
		final Elem<?,?> ret = ( (Elem)( term.eval(implicitSpace) ) ).mult( (Elem) deriv );
		return( ret );
	}
	
	
	public abstract Elem<?,?> evalPartialInverseLeft( SymbolicElem<?,?> elem , ArrayList<Elem<?,?>> withRespectTo , HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	
	public abstract Elem<?,?> evalPartialInverseRight( SymbolicElem<?,?> elem , ArrayList<Elem<?,?>> withRespectTo , HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	
}

