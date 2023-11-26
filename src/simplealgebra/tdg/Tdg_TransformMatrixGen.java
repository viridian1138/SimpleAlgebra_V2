


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

import simplealgebra.*;
import simplealgebra.ga.*;
import java.util.*;
import java.util.Map.Entry;


/**
 * Transformation matrix generator for Transdimensional Geometry (TDG).  Uses a transdimensional algorithm to generate a transformation matrix for a particular dimensionality.
 * 
 * @author tgreen
 *
 * @param <U> The number of dimensions in the space of the transform.
 * @param <A> The ord of the multivectors used to generate the transform matrices
 * @param <R> The enclosed type
 * @param <S> The factory for the enclosed type
 */
public class Tdg_TransformMatrixGen<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>>
{

	/**
	 * Factory for generating multivectors
	 */
	protected GeometricAlgebraMultivectorElemFactory<U,A,R,S> gaFactory;
	
	/**
	 * Factory for generating matrices
	 */
	protected SquareMatrixElemFactory<U,R,S> matrixFactory;
	
	
	/**
	 * Constructor
	 * @param _gaFactory Factory for generating multivectors
	 * @param _matrixFactory Factory for generating matrices
	 */
	public Tdg_TransformMatrixGen( final GeometricAlgebraMultivectorElemFactory<U,A,R,S> _gaFactory , final SquareMatrixElemFactory<U,R,S> _matrixFactory )
	{
		gaFactory = _gaFactory;
		matrixFactory = _matrixFactory;
	}
	
	/**
	 * Generates an identity matrix.
	 * @return An identity matrix
	 */
	public SquareMatrixElem<U,R,S> identityMatrix()
	{
		return( matrixFactory.identity() );
	}
	
	/**
	 * Concatenates two transform matrices
	 * @param matA The first matrix to be concatenated
	 * @param matB The second matrix to be concatenated
	 * @return The result of concatenating the matrices
	 */
	public SquareMatrixElem<U,R,S> concatenateMatrices( final SquareMatrixElem<U,R,S> matA , final SquareMatrixElem<U,R,S> matB )
	{
		return( matA.mult( matB ) );
	}
	
	/**
	 * Generates a rotation matrix
	 * @param axisA The index of the first axis of the plane of rotation
	 * @param axisB The index of the second axis of the plane of rotation
	 * @param angleRadians The angle of the rotation in radians
	 * @param numIter The number of iterations to use in the exponential generating the rotation
	 * @return The generated rotation matrix
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> rotationMatrix( final BigInteger axisA , final BigInteger axisB , final R angleRadians , final int numIter ) throws DimensionMismatchException
	{
		final U dim = matrixFactory.getDim();
		final BigInteger dims = dim.getVal();
		
		if( dims.compareTo( axisA ) <= 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		if( dims.compareTo( axisB ) <= 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		if( axisA.compareTo( axisB ) == 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> bivector = gaFactory.zero();
		
		final HashSet<BigInteger> bivectorOrdinate = new HashSet<BigInteger>();
		
		bivectorOrdinate.add( axisA );
		
		bivectorOrdinate.add( axisB );
		
		bivector.setVal( bivectorOrdinate , angleRadians );
		
		/* Generate the rotation from the bivector angle */
		final GeometricAlgebraMultivectorElem<U,A,R,S> res = bivector.exp( numIter );
		
		final SquareMatrixElem<U,R,S> ret = matrixFactory.zero();
		
		for( final Entry<HashSet<BigInteger>, R> v : res.getEntrySet() )
		{
			final HashSet<BigInteger> key = v.getKey();
			final R val = v.getValue();
			
			if( key.isEmpty() )
			{
				/* Scalar portion */
				ret.setVal(axisA, axisA, val);
				ret.setVal(axisB, axisB, val);
			}
			else
			{
				/* Bivector portion <axisA,axisB> */
				final GeometricAlgebraMultivectorElem<U,A,R,S> bivec2 = gaFactory.zero();
				bivec2.setVal(key, val);
				final R ident2 = gaFactory.getFac().identity();
				
				{
					final HashSet<BigInteger> keyn = new HashSet<BigInteger>();
					keyn.add( axisA );
					final GeometricAlgebraMultivectorElem<U,A,R,S> vect2 = gaFactory.zero();
					vect2.setVal(keyn, ident2);
					final GeometricAlgebraMultivectorElem<U,A,R,S> resvect = bivec2.mult( vect2 );
					for( final Entry<HashSet<BigInteger>, R> v2 : resvect.getEntrySet() )
					{
						final R val2 = v2.getValue();
						ret.setVal(axisA, axisB, val2);
					}
				}
				
				{
					final HashSet<BigInteger> keyn = new HashSet<BigInteger>();
					keyn.add( axisB );
					final GeometricAlgebraMultivectorElem<U,A,R,S> vect2 = gaFactory.zero();
					vect2.setVal(keyn, ident2);
					final GeometricAlgebraMultivectorElem<U,A,R,S> resvect = bivec2.mult( vect2 );
					for( final Entry<HashSet<BigInteger>, R> v2 : resvect.getEntrySet() )
					{
						final R val2 = v2.getValue();
						ret.setVal(axisB, axisA, val2);
					}
				}
			}
		}
		
		for( BigInteger cnt = BigInteger.ZERO ; cnt.compareTo( dims ) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			final R identity = matrixFactory.getFac().identity();
			if( ( cnt.compareTo(axisA) != 0 ) && ( cnt.compareTo(axisB) != 0 ) )
			{
				ret.setVal(cnt, cnt, identity);
			}
		}
		
		return( ret );
		
	}
	
	/**
	 * Generates a scaling matrix
	 * @param scale Vector containing the axis scalings
	 * @param strictScale Whether to generate a strict scaling, or whether to assume that the scaling is sparse
	 * @return The generated scaling matrix
	 */
	public SquareMatrixElem<U,R,S> scalingMatrix( final GeometricAlgebraMultivectorElem<U,A,R,S> scale , final boolean strictScale )
	{
		final SquareMatrixElem<U,R,S> ret = strictScale ? matrixFactory.zero() : matrixFactory.identity();
		
		for( final Entry<HashSet<BigInteger>, R> v : scale.getEntrySet() )
		{
			final HashSet<BigInteger> key = v.getKey();
			final R val = v.getValue();
			
			/* Only use the vector portion to generate values */
			if( key.size() == 1 )
			{
				for( final BigInteger kv : key )
				{
					ret.setVal(kv, kv, val);
				}
			}
		}
		
		return( ret );
	}
	
	/**
	 * Generates a shear matrix
	 * @param shears The shearing definition
	 * @return The generated shearing matrix
	 */
	public SquareMatrixElem<U,R,S> shearingMatrix( final HashMap<BigInteger,GeometricAlgebraMultivectorElem<U,A,R,S>> shears )
	{
		final SquareMatrixElem<U,R,S> ret = matrixFactory.identity();
		
		for( final Entry<BigInteger, GeometricAlgebraMultivectorElem<U, A, R, S>> entry : shears.entrySet() )
		{
			final BigInteger row = entry.getKey();
			final GeometricAlgebraMultivectorElem<U, A, R, S> vect = entry.getValue();
			
			for( final Entry<HashSet<BigInteger>, R> vv : vect.getEntrySet())
			{
				final R val = vv.getValue();
				// Only use the vector components
				if( vv.getKey().size() == 1 )
				{
					for( final BigInteger col : vv.getKey() )
					{
						ret.setVal(row, col, val);
					}
				}
			}
			// Enforce unit scaling even if there was a value in the vector
			ret.setVal(row, row, matrixFactory.getFac().identity() );
		}
		
		return( ret );
	}

	/**
	 * Gets the multivector factory
	 * @return The multivector factory
	 */
	public GeometricAlgebraMultivectorElemFactory<U, A, R, S> getGaFactory() {
		return gaFactory;
	}

	/**
	 * Gets the matrix factory
	 * @return The matrix factory
	 */
	public SquareMatrixElemFactory<U, R, S> getMatrixFactory() {
		return matrixFactory;
	}
	
	
	
}



