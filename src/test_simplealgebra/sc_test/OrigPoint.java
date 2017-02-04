



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




package test_simplealgebra.sc_test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import test_simplealgebra.TestDimensionThree;


/**
 * Point from the original VRML.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class OrigPoint {

	
	/**
	 * Constructs the point.
	 * @param _index The original VRML index of the point.
	 * @param _locn The location of the point.
	 */
	public OrigPoint( int _index ,
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> _locn ) 
	{
		index = _index;
		locn = _locn;
	}

	
	/**
	 * The original VRML index of the point.
	 */
	protected int index;
	
	/**
	 * The location of the point.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> locn = null;
	
	/**
	 * The first new point generated from the original VRML point.
	 */
	protected NewPoint newP0 = null;
	
	/**
	 * The second new point generated from the original VRML point.
	 */
	protected NewPoint newP1 = null;
	
	/**
	 * The incident VRML faces for the point.
	 */
	protected HashSet<OrigTriangularFace> faceSet = new HashSet<OrigTriangularFace>();
	
	
	/**
	 * The normal at the point.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> n = null;
	
	
	/**
	 * Returns the normal at the point.
	 * @return The normal at the point.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> calcNormal() throws NotInvertibleException , MultiplicativeDistributionRequiredException
	{
		if( n != null )
		{
			return( n );
		}
		for( OrigTriangularFace f : faceSet )
		{
			if( n == null )
			{
				n = f.calcNormal();
			}
			else
			{
				n = n.add( calcNormal() );
			}
		}
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> argDot = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
		argDot.add( n );
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			dmagSq = n.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , argDot );
		
		double dmag = Math.sqrt( dmagSq.get( new HashSet<BigInteger>() ).getVal() );
		
		double dmagInv = 1.0 / dmag;
		
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			dmagv = dmagSq.getFac().zero();
		
		dmagv.setVal( new HashSet<BigInteger>() , new DoubleElem( dmagInv ) );
		
		n = n.mult( dmagv );
			
		return( n );
	}
	
	
	/**
	 * Adds a face to the set of incident faces.
	 * @param in The face to be added.
	 */
	public void add( OrigTriangularFace in )
	{
		faceSet.add( in );
	}

	
	/**
	 * Gets the first new point generated from the original VRML point.
	 * @return The first new point generated from the original VRML point.
	 */
	public NewPoint getNewP0() {
		return newP0;
	}

	/**
	 * Sets the first new point generated from the original VRML point.
	 * @param newP0 The first new point generated from the original VRML point.
	 */
	public void setNewP0(NewPoint newP0) {
		this.newP0 = newP0;
	}

	/**
	 * Gets the second new point generated from the original VRML point.
	 * @return The second new point generated from the original VRML point.
	 */
	public NewPoint getNewP1() {
		return newP1;
	}

	/**
	 * Sets the second new point generated from the original VRML point.
	 * @param newP0 The second new point generated from the original VRML point.
	 */
	public void setNewP1(NewPoint newP1) {
		this.newP1 = newP1;
	}

	/**
	 * Gets the location of the point.
	 * @return The location of the point.
	 */
	public GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, DoubleElem, DoubleElemFactory> getLocn() {
		return locn;
	}
	
	
	@Override
	public int hashCode()
	{
		return( index );
	}
	
	
	@Override
	public boolean equals( Object i )
	{
		if( i instanceof OrigPoint )
		{
			OrigPoint p = (OrigPoint) i;
			
			return( index == p.index );
		}
		return( false );
	}
	
	
	
}


