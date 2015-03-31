


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



package simplealgebra.samp;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.Ord;




public class Sampling<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> 
{
	
	
	public R mean( GeometricAlgebraMultivectorElem<U,A,R,S> y )
	{
		R sum = y.getFac().getFac().zero();
		final Iterator<HashSet<BigInteger>> it = y.getKeyIterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> key = it.next();
			if( key.size() != 1 )
			{
				throw( new RuntimeException( "Inconsistent" ) );
			}
			sum = sum.add( y.get(key ) );
		}
		final U dim = y.getFac().getDim();
		final BigInteger ndim = dim.getVal();
		final int adim = ndim.intValue();
		if( !( ndim.equals( BigInteger.valueOf( adim ) ) ) )
		{
			throw( new RuntimeException( "Overflow" ) );
		}
		sum = sum.divideBy( adim );
		return( sum );
	}
	
	
	
	public void slopeTransform( GeometricAlgebraMultivectorElem<U,A,R,S> xv , GeometricAlgebraMultivectorElem<U,A,R,S> yv , 
			R meanX , R meanY ,
			GeometricAlgebraMultivectorElem<U,A,R,S> slopesY ) throws NotInvertibleException
	{
		final Iterator<HashSet<BigInteger>> it = xv.getKeyIterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> key = it.next();
			if( key.size() != 1 )
			{
				throw( new RuntimeException( "Inconsistent" ) );
			}
			final R xsub = xv.get( key ).add( meanX.negate() );
			final R ysub = yv.get( key ).add( meanY.negate() );
			final R mul = ysub.mult( xsub.invertLeft() );
			slopesY.setVal( key , mul );
		}
	}
	
	
	
}




