



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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;



/**
 * A facade that makes class simplealgebra.tdg.Tdg easier to use.
 * 
 * @author tgreen
 *
 */
public class Tdg_Facade<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>>
{
	
	/**
	 * The Tdg that is being abstracted.
	 */
	protected final Tdg<U,A,R,S> tdg = new Tdg<U,A,R,S>();

	
	/**
	 * Returns the length of a line segment.
	 * @param p1 Vector to a vertex of the line segment.
	 * @param p2 Vector to a vertex of the line segment.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The length.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcLineSegmentLength( GeometricAlgebraMultivectorElem<U,A,R,S> p1 , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p1 , p2 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM1_Tr(vects, 1 /* i.e. one-dimensional figure */ , numIterExp, numIterLn) );
	}

	
	/**
	 * Returns the area of a triangle.
	 * @param p1 Vector to a vertex of the triangle.
	 * @param p2 Vector to a vertex of the triangle.
	 * @param p3 Vector to a vertex of the triangle.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The area.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcTriangleArea( GeometricAlgebraMultivectorElem<U,A,R,S> p1 , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , GeometricAlgebraMultivectorElem<U,A,R,S> p3 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p1 , p2 , p3 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM1_Tr(vects, 2 /* i.e. two-dimensional figure */ , numIterExp, numIterLn) );
	}

	
	/**
	 * Returns the circumfrence of a triangle.
	 * @param p1 Vector to a vertex of the triangle.
	 * @param p2 Vector to a vertex of the triangle.
	 * @param p3 Vector to a vertex of the triangle.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The circumfrence.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcTriangleCircumfrence( GeometricAlgebraMultivectorElem<U,A,R,S> p1 , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , GeometricAlgebraMultivectorElem<U,A,R,S> p3 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p1 , p2 , p3 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM2_Tr(vects, 2 /* i.e. two-dimensional figure */ , numIterExp, numIterLn) );
	}

	
	/**
	 * Returns the area of a parallelogram.
	 * @param corner Vector to a vertex on one corner of the paralellogram.
	 * @param p2 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p3 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The area.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcParalellogramArea( GeometricAlgebraMultivectorElem<U,A,R,S> corner , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , GeometricAlgebraMultivectorElem<U,A,R,S> p3 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p2 , p3 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM1_Pa(corner, vects, 2 /* i.e. two-dimensional figure */ , numIterExp, numIterLn) );
	}

	
	/**
	 * Returns the circumfrence of a parallelogram.
	 * @param corner Vector to a vertex on one corner of the paralellogram.
	 * @param p2 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p3 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The circumfrence.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcParalellogramCircumfrence( GeometricAlgebraMultivectorElem<U,A,R,S> corner , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , GeometricAlgebraMultivectorElem<U,A,R,S> p3 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p2 , p3 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM2_Pa(corner, vects, 2 /* i.e. two-dimensional figure */ , numIterExp, numIterLn) );
	}

	
	/**
	 * Returns the volume of a tetrahedron.
	 * @param p1 Vector to a vertex of the tetrahedron.
	 * @param p2 Vector to a vertex of the tetrahedron.
	 * @param p3 Vector to a vertex of the tetrahedron.
	 * @param p4 Vector to a vertex of the tetrahedron.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The volume.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcTetrahedronVolume( GeometricAlgebraMultivectorElem<U,A,R,S> p1 , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , GeometricAlgebraMultivectorElem<U,A,R,S> p3 , GeometricAlgebraMultivectorElem<U,A,R,S> p4 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p1 , p2 , p3 , p4 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM1_Tr(vects, 3 /* i.e. three-dimensional figure */ , numIterExp, numIterLn) );
	}

	
	/**
	 * Returns the surface-area of a tetrahedron.
	 * @param p1 Vector to a vertex of the tetrahedron.
	 * @param p2 Vector to a vertex of the tetrahedron.
	 * @param p3 Vector to a vertex of the tetrahedron.
	 * @param p4 Vector to a vertex of the tetrahedron.
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The area.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcTetrahedronSurfaceArea( GeometricAlgebraMultivectorElem<U,A,R,S> p1 , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , GeometricAlgebraMultivectorElem<U,A,R,S> p3 , GeometricAlgebraMultivectorElem<U,A,R,S> p4 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p1 , p2 , p3 , p4 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM2_Tr(vects, 3 /* i.e. three-dimensional figure */ , numIterExp, numIterLn) );
	}

	
	/**
	 * Returns the volume of an orthogonal hexahedron (or a hexahedron that is like an orthogonal hexahedron in that it has parallel planes).
	 * @param corner Vector to a vertex on one corner of the orthogonal hexahedron.
	 * @param p2 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p3 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p4 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The volume.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcOrthogonalHexahedronVolume( GeometricAlgebraMultivectorElem<U,A,R,S> corner , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , 
			GeometricAlgebraMultivectorElem<U,A,R,S> p3 , GeometricAlgebraMultivectorElem<U,A,R,S> p4 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p2 , p3 , p4 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM1_Pa(corner, vects, 3 /* i.e. three-dimensional figure */ , numIterExp, numIterLn) );
	}

	
	/**
	 * Returns the surface-area of an orthogonal hexahedron (or a hexahedron that is like an orthogonal hexahedron in that it has parallel planes).
	 * @param corner Vector to a vertex on one corner of the orthogonal hexahedron.
	 * @param p2 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p3 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p4 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The area.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcOrthogonalHexahedronSurfaceArea( GeometricAlgebraMultivectorElem<U,A,R,S> corner , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , 
			GeometricAlgebraMultivectorElem<U,A,R,S> p3 , GeometricAlgebraMultivectorElem<U,A,R,S> p4 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p2 , p3 , p4 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM2_Pa(corner, vects, 3 /* i.e. three-dimensional figure */ , numIterExp, numIterLn) );
	}

	
	/**
	 * Returns the hyper-volume of a tesseract (or a figure that is like a tesseract in that it has parallel hyperplanes).
	 * @param corner Vector to a vertex on one corner of the tesseract.
	 * @param p2 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p3 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p4 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p5 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The hyper-volume.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcTesseractHyperVolume( GeometricAlgebraMultivectorElem<U,A,R,S> corner , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , 
			GeometricAlgebraMultivectorElem<U,A,R,S> p3 , GeometricAlgebraMultivectorElem<U,A,R,S> p4 , 
			GeometricAlgebraMultivectorElem<U,A,R,S> p5 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p2 , p3 , p4 , p5 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM1_Pa(corner, vects, 4 /* i.e. four-dimensional figure */ , numIterExp, numIterLn) );
	}

	
	/**
	 * Returns the surface-volume of a tesseract (or a figure that is like a tesseract in that it has parallel hyperplanes).
	 * @param corner Vector to a vertex on one corner of the tesseract.
	 * @param p2 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p3 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p4 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param p5 Vector to a vertex neighboring the vertex passed in the parameter "corner".
	 * @param numIterExp Number of exponential iterations to use during square root calculations.
	 * @param numIterLn Number of log iterations to use during square root calculations.
	 * @return The volume.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R calcTesseractSurfaceVolume( GeometricAlgebraMultivectorElem<U,A,R,S> corner , GeometricAlgebraMultivectorElem<U,A,R,S> p2 , 
			GeometricAlgebraMultivectorElem<U,A,R,S> p3 , GeometricAlgebraMultivectorElem<U,A,R,S> p4 , 
			GeometricAlgebraMultivectorElem<U,A,R,S> p5 , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem[] vectsI = { p2 , p3 , p4 , p5 };
		GeometricAlgebraMultivectorElem<U,A,R,S>[] vects = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( vectsI );
		return( tdg.calcM2_Pa(corner, vects, 4 /* i.e. four-dimensional figure */ , numIterExp, numIterLn) );
	}

		

	
	
	
}


