


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
import java.util.HashMap;
import java.util.HashSet;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.Ord;


/**
 * Facade for Tdg_TransformMatrixGenPerspec_Facade that simplifies the generation of transformation matrices for 2D and 3D
 * Elems contain an additional dimension to support translation and perspective transformations
 * 
 * @author tgreen
 *
 * @param <U> The number of dimensions in the space of the transform.
 * @param <A> The ord of the multivectors used to generate the transform matrices
 * @param <R> The enclosed type
 * @param <S> The factory for the enclosed type
 */
public class Tdg_TransformMatrixGenPerspec_Facade<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>>
{

	/**
	 * The transformation matrix generator
	 */
	protected Tdg_TransformMatrixGenPerspec<U,A,R,S> gen;
	
	/**
	 * Constructor
	 * @param _gaFactory Factory for generating multivectors
	 * @param _matrixFactory Factory for generating matrices
	 */
	public Tdg_TransformMatrixGenPerspec_Facade( final GeometricAlgebraMultivectorElemFactory<U,A,R,S> _gaFactory , SquareMatrixElemFactory<U,R,S> _matrixFactory )
	{
		gen = new Tdg_TransformMatrixGenPerspec<U,A,R,S>( _gaFactory , _matrixFactory );
	}
	
	/**
	 * Generates an identity matrix for a 2D space
	 * @return An identity matrix
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> identityMatrix2D() throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 2-D space */
		if( dims.compareTo( BigInteger.valueOf( 3 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		return( gen.identityMatrix() );
	}
	
	/**
	 * Generates an identity matrix for a 3D space
	 * @return An identity matrix
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> identityMatrix3D() throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 3-D space */
		if( dims.compareTo( BigInteger.valueOf( 4 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		return( gen.identityMatrix() );
	}
	
	/**
	 * Concatenates two transform matrices for a 2D space
	 * @param matA The first matrix to be concatenated
	 * @param matB The second matrix to be concatenated
	 * @return The result of concatenating the matrices
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> concatenateMatrices2D( final SquareMatrixElem<U,R,S> matA , final SquareMatrixElem<U,R,S> matB ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 2-D space */
		if( dims.compareTo( BigInteger.valueOf( 3 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		return( gen.concatenateMatrices(matA, matB) );
	}
	
	/**
	 * Concatenates two transform matrices for a 3D space
	 * @param matA The first matrix to be concatenated
	 * @param matB The second matrix to be concatenated
	 * @return The result of concatenating the matrices
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> concatenateMatrices3D( final SquareMatrixElem<U,R,S> matA , final SquareMatrixElem<U,R,S> matB ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 3-D space */
		if( dims.compareTo( BigInteger.valueOf( 4 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		return( gen.concatenateMatrices(matA, matB) );
	}
	
	/**
	 * Generates a rotation matrix for a 2D space.
	 * @param angleRadians The angle of the rotation in radians
	 * @param numIter The number of iterations to use in the exponential generating the rotation
	 * @return The generated rotation matrix
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> rotationMatrix2D( final R angleRadians , final int numIter ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 2-D space */
		if( dims.compareTo( BigInteger.valueOf( 3 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		final BigInteger xAxis = BigInteger.ZERO;
		final BigInteger yAxis = BigInteger.ONE;
		
		final SquareMatrixElem<U,R,S> ret = gen.rotationMatrix(xAxis, yAxis, angleRadians, numIter);
		return( ret );
	}
	
	/**
	 * Generates a rotation matrix for the XY plane for a 3D space.
	 * @param angleRadians The angle of the rotation in radians
	 * @param numIter The number of iterations to use in the exponential generating the rotation
	 * @return The generated rotation matrix
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> rotationMatrixXY_3D( final R angleRadians , final int numIter ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 3-D space */
		if( dims.compareTo( BigInteger.valueOf( 4 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		final BigInteger xAxis = BigInteger.ZERO;
		final BigInteger yAxis = BigInteger.ONE;
		
		final SquareMatrixElem<U,R,S> ret = gen.rotationMatrix(xAxis, yAxis, angleRadians, numIter);
		return( ret );
	}
	
	/**
	 * Generates a rotation matrix for the YZ plane for a 3D space.
	 * @param angleRadians The angle of the rotation in radians
	 * @param numIter The number of iterations to use in the exponential generating the rotation
	 * @return The generated rotation matrix
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> rotationMatrixYZ_3D( final R angleRadians , final int numIter ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 3-D space */
		if( dims.compareTo( BigInteger.valueOf( 4 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		final BigInteger yAxis = BigInteger.ONE;
		final BigInteger zAxis = BigInteger.valueOf(2);
		
		final SquareMatrixElem<U,R,S> ret = gen.rotationMatrix(yAxis, zAxis, angleRadians, numIter);
		return( ret );
	}
	
	/**
	 * Generates a rotation matrix for the XZ plane for a 3D space.
	 * @param angleRadians The angle of the rotation in radians
	 * @param numIter The number of iterations to use in the exponential generating the rotation
	 * @return The generated rotation matrix
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> rotationMatrixXZ_3D( final R angleRadians , final int numIter ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 3-D space */
		if( dims.compareTo( BigInteger.valueOf( 4 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		final BigInteger xAxis = BigInteger.ZERO;
		final BigInteger zAxis = BigInteger.valueOf(2);
		
		final SquareMatrixElem<U,R,S> ret = gen.rotationMatrix(xAxis, zAxis, angleRadians, numIter);
		return( ret );
	}
	
	/**
	 * Generates a scaling matrix in 2D space
	 * @param xScale The X-Axis scaling value
	 * @param yScale The Y-Axis scaling value
	 * @return The scaling matrix in 2D space
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> scalingMatrix2D( final R xScale , final R yScale ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 2-D space */
		if( dims.compareTo( BigInteger.valueOf( 3 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		final BigInteger xAxis = BigInteger.ZERO;
		final BigInteger yAxis = BigInteger.ONE;
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> scale = getGaFactory().zero();
		
		{
			final HashSet<BigInteger> idx = new HashSet<BigInteger>();
			idx.add( xAxis );
			scale.setVal(idx, xScale);
		}
		
		{
			final HashSet<BigInteger> idx = new HashSet<BigInteger>();
			idx.add( yAxis );
			scale.setVal(idx, yScale);
		}
		
		final SquareMatrixElem<U,R,S> ret = gen.scalingMatrix(scale, true);
		
		return( ret );
	}
	
	/**
	 * Generates a scaling matrix in 3D space
	 * @param xScale The X-Axis scaling value
	 * @param yScale The Y-Axis scaling value
	 * @param zScale The Z-Axis scaling value
	 * @return The scaling matrix in 3D space
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> scalingMatrix3D( final R xScale , final R yScale , final R zScale ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 3-D space */
		if( dims.compareTo( BigInteger.valueOf( 4 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		final BigInteger xAxis = BigInteger.ZERO;
		final BigInteger yAxis = BigInteger.ONE;
		final BigInteger zAxis = BigInteger.valueOf(2);
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> scale = getGaFactory().zero();
		
		{
			final HashSet<BigInteger> idx = new HashSet<BigInteger>();
			idx.add( xAxis );
			scale.setVal(idx, xScale);
		}
		
		{
			final HashSet<BigInteger> idx = new HashSet<BigInteger>();
			idx.add( yAxis );
			scale.setVal(idx, yScale);
		}
		
		{
			final HashSet<BigInteger> idx = new HashSet<BigInteger>();
			idx.add( zAxis );
			scale.setVal(idx, zScale);
		}
		
		final SquareMatrixElem<U,R,S> ret = gen.scalingMatrix(scale, true);
		
		return( ret );
	}
	
	/**
	 * Generates a translation matrix in 2D space
	 * @param xTranslation The X-Axis translation value
	 * @param yTranslation The Y-Axis translation value
	 * @return The translation matrix in 2D space
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> translationMatrix2D( final R xTranslation , final R yTranslation ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 2-D space */
		if( dims.compareTo( BigInteger.valueOf( 3 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		final BigInteger xAxis = BigInteger.ZERO;
		final BigInteger yAxis = BigInteger.ONE;
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> translation = getGaFactory().zero();
		
		{
			final HashSet<BigInteger> idx = new HashSet<BigInteger>();
			idx.add( xAxis );
			translation.setVal(idx, xTranslation);
		}
		
		{
			final HashSet<BigInteger> idx = new HashSet<BigInteger>();
			idx.add( yAxis );
			translation.setVal(idx, yTranslation);
		}
		
		final SquareMatrixElem<U,R,S> ret = gen.translationMatrix(translation);
		return( ret );
	}
	
	/**
	 * Generates a translation matrix in 3D space
	 * @param xTranslation The X-Axis translation value
	 * @param yTranslation The Y-Axis translation value
	 * @param zTranslation The Z-Axis translation value
	 * @return The translation matrix in 3D space
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> translationMatrix3D( final R xTranslation , final R yTranslation , final R zTranslation ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 3-D space */
		if( dims.compareTo( BigInteger.valueOf( 4 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		final BigInteger xAxis = BigInteger.ZERO;
		final BigInteger yAxis = BigInteger.ONE;
		final BigInteger zAxis = BigInteger.valueOf(2);
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> translation = getGaFactory().zero();
		
		{
			final HashSet<BigInteger> idx = new HashSet<BigInteger>();
			idx.add( xAxis );
			translation.setVal(idx, xTranslation);
		}
		
		{
			final HashSet<BigInteger> idx = new HashSet<BigInteger>();
			idx.add( yAxis );
			translation.setVal(idx, yTranslation);
		}
		
		{
			final HashSet<BigInteger> idx = new HashSet<BigInteger>();
			idx.add( zAxis );
			translation.setVal(idx, zTranslation);
		}
		
		final SquareMatrixElem<U,R,S> ret = gen.translationMatrix(translation);
		return( ret );
	}
	
	/**
	 * Generates a shear matrix in 2D space
	 * @param shears The shearing definition
	 * @return The generated shearing matrix
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> shearingMatrix2D( final HashMap<BigInteger,GeometricAlgebraMultivectorElem<U,A,R,S>> shears ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 2-D space */
		if( dims.compareTo( BigInteger.valueOf( 3 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
	
		final SquareMatrixElem<U,R,S> ret = gen.shearingMatrix(shears);
		return( ret );
	}
	
	/**
	 * Generates a shear matrix in 3D space
	 * @param shears The shearing definition
	 * @return The generated shearing matrix
	 * @throws DimensionMismatchException 
	 */
	public SquareMatrixElem<U,R,S> shearingMatrix3D( final HashMap<BigInteger,GeometricAlgebraMultivectorElem<U,A,R,S>> shears ) throws DimensionMismatchException
	{
		final U dim = getMatrixFactory().getDim();
		final BigInteger dims = dim.getVal();
		/* Verify it's a 3-D space */
		if( dims.compareTo( BigInteger.valueOf( 4 ) ) != 0 )
		{
			throw( new DimensionMismatchException() );
		}
		
		final SquareMatrixElem<U,R,S> ret = gen.shearingMatrix(shears);
		return( ret );
	}

	/**
	 * Gets the multivector factory
	 * @return The multivector factory
	 */
	public GeometricAlgebraMultivectorElemFactory<U, A, R, S> getGaFactory() {
		return gen.getGaFactory();
	}

	/**
	 * Gets the matrix factory
	 * @return The matrix factory
	 */
	public SquareMatrixElemFactory<U, R, S> getMatrixFactory() {
		return gen.getMatrixFactory();
	}
	
	
}



