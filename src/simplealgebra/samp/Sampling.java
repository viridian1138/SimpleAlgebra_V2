


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
import java.util.Map.Entry;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.Ord;



/**
 * Base class for generating fits from a set of samples.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the vector of samples (the number of samples).
 * @param <A> The Ord of the vector of samples.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class Sampling<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> 
{
	
	/**
	 * Calculates the mean of a set of positions.
	 * 
	 * @param y The set of positions from which to calculate the mean.
	 * @return The calculated mean.
	 */
	public R mean( GeometricAlgebraMultivectorElem<U,A,R,S> y )
	{
		R sum = y.getFac().getFac().zero();
		for( final Entry<HashSet<BigInteger>, R> ii : y.getEntrySet() )
		{
			final HashSet<BigInteger> key = ii.getKey();
			if( key.size() != 1 )
			{
				throw( new RuntimeException( "Inconsistent" ) );
			}
			sum = sum.add( ii.getValue() );
		}
		final U dim = y.getFac().getDim();
		final BigInteger ndim = dim.getVal();
		sum = sum.divideBy( ndim );
		return( sum );
	}
	
	
	/**
	 * Transforms Y-Coordinate positions from coordinate-space to slope-space.
	 * 
	 * @param xv The X-Coordinate positions.
	 * @param yv The Y-Coordinate positions.
	 * @param meanX The mean of the X-Coordinate positions.
	 * @param meanY The mean of the Y-Coordinate positions.
	 * @param slopesY The slope for the Y-Coordinate positions.
	 * @throws NotInvertibleException
	 */
	public void slopeTransform( GeometricAlgebraMultivectorElem<U,A,R,S> xv , GeometricAlgebraMultivectorElem<U,A,R,S> yv , 
			R meanX , R meanY ,
			GeometricAlgebraMultivectorElem<U,A,R,S> slopesY ) throws NotInvertibleException
	{
		for( final Entry<HashSet<BigInteger>, R> ii : xv.getEntrySet() )
		{
			final HashSet<BigInteger> key = ii.getKey();
			if( key.size() != 1 )
			{
				throw( new RuntimeException( "Inconsistent" ) );
			}
			final R xsub = ii.getValue().add( meanX.negate() );
			final R ysub = yv.get( key ).add( meanY.negate() );
			final R mul = ysub.mult( xsub.invertLeft() );
			slopesY.setVal( key , mul );
		}
	}
	
	
	
}




