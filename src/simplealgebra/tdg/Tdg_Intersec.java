



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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;



/**
 * Returns transdimensional calculations of intersections of particular kinds of linear geometric figures in non-degenerate cases.
 * 
 * @author tgreen
 *
 * @param <U> The number of dimensions in the multivectors used.
 * @param <A> The Ord of the multivectors.
 * @param <R> The enclosed type of the multivectors.
 * @param <S> The factory for the enclosed type of the multivectors.
 */
public class Tdg_Intersec<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>>
{
	
	
	/**
	 * Calculates the intersection between a line and another linear figure (e.g. a line or a plane), creating a 
	 *    simple line-line intersection or line-surface intersection, using a transdimensional method.
	 *    It is assumed that the intersection exists (e.g. the line and plane aren't parallel) and is not degenerate.
	 * @param b0 Vector to a point on the line to be intersected.
	 * @param ta Unit vector in the direction of the line to be intersected.
	 * @param b1 Vector to a point on the linear figure to be intersected.
	 * @param tb Multivector (usually a vector or a bivector) parallel to the direction of the linear figure.
	 * @return The calculated point of intersection
	 */
	public GeometricAlgebraMultivectorElem<U,A,R,S> genLnIntersec(  
			GeometricAlgebraMultivectorElem<U,A,R,S> b0,
			GeometricAlgebraMultivectorElem<U,A,R,S> ta,
			GeometricAlgebraMultivectorElem<U,A,R,S> b1,
			GeometricAlgebraMultivectorElem<U,A,R,S> tb
			) throws NotInvertibleException
	{
		final GeometricAlgebraMultivectorElem<U,A,R,S> b1b0 = b1.add( b0.negate() );
		final ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>> tbb = new ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>>();
		tbb.add( tb );
		final GeometricAlgebraMultivectorElem<U,A,R,S> b1b0WedgeTb = b1b0.handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , tbb);
		final GeometricAlgebraMultivectorElem<U,A,R,S> taWedgeTb = ta.handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , tbb);
		final  GeometricAlgebraMultivectorElem<U,A,R,S> delta = extractVect( b1b0WedgeTb.mult( quickInvertLeftBivec( taWedgeTb ) ).mult( ta ) );
		final  GeometricAlgebraMultivectorElem<U,A,R,S> ret = delta.add( b0 );
		return( ret );
	}
	
	
	
	/**
	 * Calculates the line of intersection between two planes, a simple surface-surface intersection (SSI) using a transdimensional method.
	 *   It is assumed that the intersection exists (e.g. the planes aren't parallel) and is not degenerate.
	 * @param b0 Vector to a point on the first plane to be intersected.
	 * @param ma Unit bivector parallel to the direction of the first plane to be intersected.
	 * @param b1 Vector to a point on the second plane to be intersected.
	 * @param mb Unit bivector parallel to the direction of the second plane to be intersected.
	 * @param numIterExp  The number of iterations to use when calculating exponentials.
	 * @param numIterLn  The number of iterations to use when calculating logarithms.
	 * @return An ArrayList where element zero of the ArrayList is the vector in the direction of the line of
	 *    intersection and element one of the ArrayList is a vector to a point on the line of intersection.
	 */
	public ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>> genPlaneIntersec(  
			GeometricAlgebraMultivectorElem<U,A,R,S> b0,
			GeometricAlgebraMultivectorElem<U,A,R,S> ma,
			GeometricAlgebraMultivectorElem<U,A,R,S> b1,
			GeometricAlgebraMultivectorElem<U,A,R,S> mb,
			final int numIterExp , final int numIterLn
			) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>> ret = new ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>>();
		final GeometricAlgebraMultivectorElem<U,A,R,S> dir = genDirection(ma, mb, numIterExp, numIterLn);
		ret.add( dir );
		final GeometricAlgebraMultivectorElem<U,A,R,S> isectDir = extractVect( dir.mult( ma ) );
		final GeometricAlgebraMultivectorElem<U,A,R,S> bn = genLnIntersec( b0 , isectDir , b1 , mb );
		ret.add( bn );
		return( ret );
	}
	
	
	
	/**
	 * Returns a vector that is the direction of the intersection of two planes represented by bivectors.  This is done
	 * by finding a vector that is in the plane of each of the bivectors.
	 * @param bivecA Bivector of first plane in the intersection.
	 * @param bivecB Bivector of second plane in the intersection.
	 * @param numIterExp  The number of iterations to use when calculating exponentials.
	 * @param numIterLn  The number of iterations to use when calculating logarithms.
	 * @return A vector that is the direction of the intersection of the planes.
	 */
	protected GeometricAlgebraMultivectorElem<U,A,R,S> genDirection(  
			GeometricAlgebraMultivectorElem<U,A,R,S> bivecA,
			GeometricAlgebraMultivectorElem<U,A,R,S> bivecB,
			final int numIterExp , final int numIterLn
			) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem<U,A,R,S> drop = extractVect( dropProduct( bivecA , bivecB ) );
		
		R bivecProd = bivecA.getFac().getFac().zero();
		
		for( HashSet<BigInteger> hs : bivecA.getKeySet() )
		{
			bivecProd = bivecProd.add( ( bivecA.getVal( hs ) ).mult( bivecB.getVal( hs ) ) );
		}
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> bivecProdB = bivecA.getFac().zero();
		
		bivecProdB.setVal( new HashSet<BigInteger>() , bivecProd );
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> bivecBP = bivecB.add( bivecProdB.negate().mult( bivecA ) );
		
		R bivecBPLenSq = bivecA.getFac().getFac().zero();
		
		for( HashSet<BigInteger> hs : bivecBP.getKeySet() )
		{
			bivecBPLenSq = bivecBPLenSq.add( ( bivecBP.getVal( hs ) ).mult( bivecBP.getVal( hs ) ) );
		}
		
		final R bivecBPLen = bivecBPLenSq.powL(bivecBPLenSq, numIterExp, numIterLn);
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> gmul = bivecA.getFac().zero();
		
		gmul.setVal( new HashSet<BigInteger>() , bivecBPLen.invertRight() );
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> bivecBPP = bivecBP.mult( gmul );
		
		/*
		 * bivecA and bivecBPP form an ortho-basis on the two-dimensional space of bivectors
		 * defined by bivecA and bivecB.
		 */
		
		drop = extractVect( erasePerpendicular( drop , bivecA ) );
		drop = extractVect( erasePerpendicular( drop , bivecBPP ) );
		
		return( drop );
	}
	
		

	/**
	 * Extracts the vector portion from a multivector.
	 * @param a The multivector from which to extract components.
	 * @return Vector containing the vector components of the input multivector.
	 */
	protected  GeometricAlgebraMultivectorElem<U,A,R,S> extractVect(
			GeometricAlgebraMultivectorElem<U,A,R,S> a )
	{
		final GeometricAlgebraMultivectorElem<U,A,R,S> ret = a.getFac().zero();
		for( final HashSet<BigInteger> keyA : a.getKeySet() )
		{
			if( keyA.size() == 1 ) // Vector components
			{
				ret.setVal( keyA , a.get( keyA ) );
			}
		}
		return( ret );
	}
	
	
	
	/**
	 * Gets a quick right-inverse of a bivector.
	 * @param bivec The input bivector.
	 * @return The right-inverse of the bivector.
	 * @throws NotInvertibleException
	 */
	protected GeometricAlgebraMultivectorElem<U,A,R,S> quickInvertRightBivec( 
			GeometricAlgebraMultivectorElem<U,A,R,S> bivec ) throws NotInvertibleException
	{
		final GeometricAlgebraMultivectorElem<U,A,R,S> mul = bivec.mult( bivec );
		final R ev = mul.getVal( new HashSet<BigInteger>() );
		final GeometricAlgebraMultivectorElem<U,A,R,S> fv = bivec.getFac().zero();
		fv.setVal(new HashSet<BigInteger>() , ev.invertRight() );
		return( bivec.mult( fv ) );
	}
	
	
	
	/**
	 * Gets a quick left-inverse of a bivector.
	 * @param bivec The input bivector.
	 * @return The left-inverse of the bivector.
	 */
	protected GeometricAlgebraMultivectorElem<U,A,R,S> quickInvertLeftBivec( 
			GeometricAlgebraMultivectorElem<U,A,R,S> bivec ) throws NotInvertibleException
	{
		final GeometricAlgebraMultivectorElem<U,A,R,S> mul = bivec.mult( bivec );
		final R ev = mul.getVal( new HashSet<BigInteger>() );
		final GeometricAlgebraMultivectorElem<U,A,R,S> fv = bivec.getFac().zero();
		fv.setVal(new HashSet<BigInteger>() , ev.invertLeft() );
		return( bivec.mult( fv ) );
	}
	
	
	
	/**
	 * Returns a version of a vector that removes components that are perpendicular to a bivector.
	 * @param vec The vector for which to have components removed.
	 * @param bivec The bivector against which to check the vector
	 * @return A version of vector "vec" with components perpendicular to "bivec" removed.
	 */
	protected GeometricAlgebraMultivectorElem<U,A,R,S> erasePerpendicular(  
			GeometricAlgebraMultivectorElem<U,A,R,S> vec,
			GeometricAlgebraMultivectorElem<U,A,R,S> bivec
			) throws NotInvertibleException
	{
		final GeometricAlgebraMultivectorElem<U,A,R,S> mult = vec.mult(bivec);
		final GeometricAlgebraMultivectorElem<U,A,R,S> multB = vec.getFac().zero();
		for( final HashSet<BigInteger> keyM : mult.getKeySet() )
		{
			if( keyM.size() < 3 ) // Eliminate (i.e. skip) trivectors and above
			{
				multB.setVal( keyM , mult.get( keyM ) );
			}
		}
		final GeometricAlgebraMultivectorElem<U,A,R,S> ret = multB.mult( quickInvertRightBivec( bivec ) );
		return( ret );
	}
	
	

	/**
	 * Performs a non-standard product of two multivectors that returns a result with terms that are usually dropped.
	 * @param a The first argument of the product.
	 * @param b The second argument of the product.
	 * @return The product of the two multivectors.
	 */
	protected GeometricAlgebraMultivectorElem<U,A,R,S> dropProduct(  
			GeometricAlgebraMultivectorElem<U,A,R,S> a,
			GeometricAlgebraMultivectorElem<U,A,R,S> b
			)
	{
		GeometricAlgebraMultivectorElem<U,A,R,S> sum = a.getFac().zero();
		
		for( final HashSet<BigInteger> keyA : a.getKeySet() )
		{
			for( final HashSet<BigInteger> keyB : b.getKeySet() )
			{
				final GeometricAlgebraMultivectorElem<U,A,R,S> a2 = a.getFac().zero();
				final GeometricAlgebraMultivectorElem<U,A,R,S> b2 = b.getFac().zero();
				a2.setVal(keyA , a.get(keyA) );
				b2.setVal(keyB , b.get(keyB) );
				final GeometricAlgebraMultivectorElem<U,A,R,S> c2 = a2.mult( b2 );
				for( final HashSet<BigInteger> keyC : c2.getKeySet() )
				{
					final R c22 = c2.get( keyC );
					final HashSet<BigInteger> keyC2 = new HashSet<BigInteger>();
					for( final BigInteger bb : keyA )
					{
						if( !( keyC.contains( bb ) ) )
						{
							keyC2.add( bb );
						}
					}
					for( final BigInteger bb : keyB )
					{
						if( !( keyC.contains( bb ) ) )
						{
							keyC2.add( bb );
						}
					}
					sum.setVal( keyC2 , sum.getVal( keyC2 ).add( c22 ) );
				}
			}
		}
		
		return sum;
	}
	
	
	
} /* Class Tdg_Intersec */


