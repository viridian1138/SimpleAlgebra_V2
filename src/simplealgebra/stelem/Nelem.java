




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





package simplealgebra.stelem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;

public abstract class Nelem<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends SymbolicElem<R,S> 
{

	public Nelem( S _fac , HashMap<K,BigInteger> _coord )
	{
		super( _fac );
		coord = _coord;
	}
	
	public Nelem( S _fac , HashMap<K,BigInteger> _coord , DroolsSession ds )
	{
		this( _fac , _coord );
		ds.insert( this );
	}
	
	@Override
	public R evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	
	protected HashMap<K,BigInteger> coord = new HashMap<K,BigInteger>();

}


