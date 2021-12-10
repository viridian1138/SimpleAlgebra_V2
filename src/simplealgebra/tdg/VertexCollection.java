



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





package simplealgebra.tdg;


import java.util.ArrayList;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;


/**
 * Returns geometric measurements for a general all-levels-convex
 * (both the figure and all levels of externally facing sub-figures are convex) 
 * figure of dimension 2 or greater.
 * 
 * @author tgreen
 *
 * @param <U>
 * @param <A>
 * @param <R>
 * @param <S>
 */
public abstract class VertexCollection<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>>
{
	
	/**
	 * Returns a set of simplex instances that sum to the interior of the figure without overlap.  In
	 * other words, the returned simplex instances constitute a non-ovelapping partitioning of the figure.
	 * @param numDim The number of dimensions in the figure, mainly used forintegrity checks.
	 * @param fac Factory for generating multivectors.
	 * @return A set of simplex instances that sum to the interior of the figure without overlap.
	 */
	public abstract ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>[]> getVectsM1(int numDim, GeometricAlgebraMultivectorElemFactory<U,A,R,S> fac);
	
	/**
	 * Returns a geometric measurement of the figure. 
	 * Returns measurements with units L ^ N where L is length and N is the number of dimensions.
	 * @param numDim The number of dimensions in the figure, mainly used forintegrity checks.
	 * @param fac Factory for generating multivectors.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The calculated measurement.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcM1( int numDim , GeometricAlgebraMultivectorElemFactory<U,A,R,S> fac , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		R sum = fac.getFac().zero();
		for( final GeometricAlgebraMultivectorElem<U,A,R,S>[] vects : getVectsM1( numDim , fac ) )
		{
			Tdg<U,A,R,S> tdg = new Tdg<U,A,R,S>();
			sum = sum.add( tdg.calcM1_Tr( vects , numDim , numIterExp , numIterLn ) );
		}
		return( sum );
	}
	
	/**
	 * Returns a geometric measurement of the figure.
	 * Returns measurements with units L ^ ( N - 1 ) where L is length and N is the number of dimensions.
	 * @param numDim The number of dimensions in the figure, mainly used forintegrity checks.
	 * @param fac Factory for generating multivectors.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The calculated measurement.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public abstract R calcM2( int numDim , GeometricAlgebraMultivectorElemFactory<U,A,R,S> fac , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	
	
}



