



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
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;



/**
 * Returns geometric measurements for some transdimensional classes of geometric figures.
 * 
 * @author tgreen
 *
 * @param <U>
 * @param <A>
 * @param <R>
 * @param <S>
 */
public class Tdg<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>>
{
	
		

	/**
	 * Returns a geometric measurement of a N-dimensional simplex (i.e. a simplex with N + 1 vertices) where N > 0. 
	 * Returns measurements with units L ^ N where L is length and N is the number of dimensions.
	 * @param vects Vectors to the vertices of the simplex.
	 * @param numDimensions The number of dimensions of the simplex.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The geometric measurement.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcM1_Tr( GeometricAlgebraMultivectorElem<U,A,R,S>[] vects , int numDimensions , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		
		if( !( vects.length == ( numDimensions + 1 ) ) )
		{
			throw( new IllegalArgumentException( "Number of Dimensions Mismatches" ) );
		}
		
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> strt = vects[ 0 ];
		
		GeometricAlgebraMultivectorElem<U,A,R,S> prod = vects[ 1 ].add( strt.negate() );
		
		for( int cnt = 2  ;  cnt < vects.length  ;  cnt++  )
		{
			final GeometricAlgebraMultivectorElem<U,A,R,S> del = vects[ cnt ].add( strt.negate() );
			final ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>> 
				args = new ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>>();
			args.add( del );
			prod = prod.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		}
		
		
		R resSq = prod.getFac().getFac().zero();
		
		for( final R it : prod.getValueSet() )
		{
			resSq = resSq.add( it.mult( it ) );
		}
		
		R res = resSq.powL( prod.getFac().getFac().identity().divideBy( 2 ) , 
				numIterExp , numIterLn );
		
		for( int cnt = numDimensions ; cnt > 1 ; --cnt )
		{
			res = res.divideBy( cnt );
		}
		
		
		return( res );
	}

		
	/**
	 * Returns a geometric measurement of a transdimensional class of geometric figures with 2 ^ N vertices
	 * where N is the number of dimensions including the line segment, the square, the paralellogram, the cube, the orthogonal hexahedron, and the tesseract. 
	 * N must be greater than zero.
	 * Returns measurements with units L ^ N where L is length and N is the number of dimensions.
	 * @param corner Vector to the vertex at one corner of the figure.
	 * @param vects Vectors to all N vertices neighboring the vertex passed in the parameter "corner" where N is the number of dimensions.
	 * @param numDimensions The number of dimensions of the figure.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The geometric measurement.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcM1_Pa( GeometricAlgebraMultivectorElem<U,A,R,S> corner , GeometricAlgebraMultivectorElem<U,A,R,S>[] vects , int numDimensions , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
			
		if( !( vects.length == ( numDimensions ) ) )
		{
			throw( new IllegalArgumentException( "Number of Dimensions Mismatches" ) );
		}
			
		GeometricAlgebraMultivectorElem<U,A,R,S> prod = vects[ 0 ].add( corner.negate() );
			
		for( int cnt = 1  ;  cnt < vects.length  ;  cnt++  )
		{
			final GeometricAlgebraMultivectorElem<U,A,R,S> del = vects[ cnt ].add( corner.negate() );
			final ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>> 
				args = new ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>>();
			args.add( del );
			prod = prod.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		}
			
			
		R resSq = prod.getFac().getFac().zero();
			
		for( final R it : prod.getValueSet() )
		{
			resSq = resSq.add( it.mult( it ) );
		}
			
		final R res = resSq.powL( prod.getFac().getFac().identity().divideBy( 2 ) , 
				numIterExp , numIterLn );
			
			
		return( res );
	}
	

	/**
	 * Returns a geometric measurement of a N-dimensional simplex (i.e. a simplex with N + 1 vertices) where N > 0.
	 * Returns measurements with units L ^ ( N - 1 ) where L is length and N is the number of dimensions.
	 * @param vects Vectors to the vertices of the simplex.
	 * @param numDimensions The number of dimensions of the simplex.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The geometric measurement.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcM2_Tr( GeometricAlgebraMultivectorElem<U,A,R,S>[] vects , int numDimensions , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		
		R sum = vects[ 0 ].getFac().getFac().zero();
		
		for( int cnt = 0  ;  cnt < vects.length  ;  cnt++  )
		{
			GeometricAlgebraMultivectorElem<U,A,R,S>[] vectsB = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( new GeometricAlgebraMultivectorElem[ vects.length - 1 ] );
			int idx = 0;
			for( int cntB = 0  ;  cntB < vects.length  ;  cntB++  )
			{
				if( cntB != cnt )
				{
					vectsB[ idx ] = vects[ cntB ];
					idx++;
				}
			}
			sum = sum.add( calcM1_Tr( vectsB , numDimensions - 1 , numIterExp , numIterLn ) );
		}
		
		
		return( sum );
	}
	

	/**
	 * Returns a geometric measurement of a transdimensional class of geometric figures with 2 ^ N vertices
	 * where N is the number of dimensions including the line segment, the square, the paralellogram, the cube, the orthogonal hexahedron, and the tesseract. 
	 * N must be greater than zero.
	 * Returns measurements with units L ^ N where L is length and N is the number of dimensions.
	 * @param corner Vector to the vertex at one corner of the figure.
	 * @param vects Vectors to all N vertices neighboring the vertex passed in the parameter "corner" where N is the number of dimensions.
	 * @param numDimensions The number of dimensions of the figure.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The geometric measurement.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcM2_Pa( GeometricAlgebraMultivectorElem<U,A,R,S> corner , GeometricAlgebraMultivectorElem<U,A,R,S>[] vects , int numDimensions , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		
		R sum = vects[ 0 ].getFac().getFac().zero();
		
		for( int cnt = 0  ;  cnt < vects.length  ;  cnt++  )
		{
			GeometricAlgebraMultivectorElem<U,A,R,S>[] vectsB = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( new GeometricAlgebraMultivectorElem[ vects.length - 1 ] );
			int idx = 0;
			for( int cntB = 0  ;  cntB < vects.length  ;  cntB++  )
			{
				if( cntB != cnt )
				{
					vectsB[ idx ] = vects[ cntB ];
					idx++;
				}
			}
			sum = sum.add( calcM1_Pa( corner , vectsB , numDimensions - 1 , numIterExp , numIterLn  ) );
		}
		
		
		return( sum.add( sum ) );
	}
	
	
	
}


