




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
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;

public abstract class Stelem<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends SymbolicElem<R,S> 
{

	public Stelem( S _fac )
	{
		super( _fac );
	}
	
	public Stelem( S _fac , DroolsSession ds )
	{
		this( _fac );
		ds.insert( this );
	}
	
	@Override
	public R evalPartialDerivative(ArrayList<Elem<?, ?>> withRespectTo, HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
		Stelem<R,S,K> rm = this.cloneInstance();
		rm.applyPartialDerivative( (ArrayList<K>) withRespectTo );
		return( rm.eval( implicitSpace ) );
	}
	
	
	public Stelem<R,S,K> simplifyPartialDerivative( ArrayList<Elem<?, ?>> withRespectTo ) throws NotInvertibleException
	{
		Stelem<R,S,K> rm = this.cloneInstance();
		rm.applyPartialDerivative( (ArrayList<K>) withRespectTo );
		return( rm );
	}
	
	
	public abstract Stelem<R,S,K> cloneInstance();
	
	
	protected void applyPartialDerivative(ArrayList<K> withRespectTo)
			throws NotInvertibleException {
		final Iterator<K> it = withRespectTo.iterator();
		while( it.hasNext() )
		{
			final K v = it.next();
			BigInteger vl = partialMap.get( v );
			if( vl == null )
			{
				vl = BigInteger.ONE;
			}
			else
			{
				vl = vl.add( BigInteger.ONE );
			}
			partialMap.put( v , vl );
		}
	}

	
	protected HashMap<K,BigInteger> partialMap = new HashMap<K,BigInteger>();

}


