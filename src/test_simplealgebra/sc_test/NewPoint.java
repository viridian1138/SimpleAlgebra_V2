



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

import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import test_simplealgebra.TestDimensionThree;


/**
 * Point generated for the production of the OpenSCAD model.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class NewPoint {

	
	/**
	 * Constructs the point.
	 * @param _locn The location of the new point.
	 * @param _orig The original VRML point.
	 * @param _dir The direction (plus or minus) from which to take the normal from the original point.
	 */
	private NewPoint( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> _locn ,
			OrigPoint _orig , boolean _dir ) 
	{
		locn = _locn;
		orig = _orig;
		dir = _dir;
	}
	
	
	/**
	 * The location of the point.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> locn = null;
	

	/**
	 * The original VRML point.
	 */
	protected OrigPoint orig;
	
	/**
	 * The direction (plus or minus) from which to take the normal from the original point.
	 */
	protected boolean dir;
	
	
	

	
	/**
	 * Gets the location of the point.
	 * @return The location of the point.
	 */
	public GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, DoubleElem, DoubleElemFactory> getLocn() {
		return locn;
	}


	/**
	 * Gets the underlying original VRML point.
	 * @return The underlying original VRML point.
	 */
	public OrigPoint getOrig() {
		return orig;
	}
	
	
	
	/**
	 * Builds new OpenSCAD points equivalent to the original VRML point.
	 * @param orig The original point from which to build the new points.
	 * @param n The normal at the original point.
	 * @return Array of two new points.
	 */
	public static NewPoint[] genNewPoint( OrigPoint orig , GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, DoubleElem, DoubleElemFactory> n )
	{
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, DoubleElem, DoubleElemFactory>
			p0 = orig.getLocn().add( n.negate() );
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, DoubleElem, DoubleElemFactory>
			p1 = orig.getLocn().add( n );
		
		
		NewPoint np0 = new NewPoint( p0 , orig , false );
		
		NewPoint np1 = new NewPoint( p1 , orig , true );
		
		
		NewPoint[] np = { np0 , np1 };
		
		
		orig.setNewP0( np0 );
		
		orig.setNewP1( np1 );
		
		
		return( np );
		
	}
	
	
	@Override
	public int hashCode()
	{
		final int hc = ( orig.hashCode() ) + ( dir ? 1 : 0 );
		
		return( hc );
	}
	
	
	@Override
	public boolean equals( Object i )
	{
		if( i instanceof NewPoint )
		{
			NewPoint p = (NewPoint) i;
			
			return( ( orig.equals( p.orig ) ) && ( dir == p.dir ) );
		}
		return( false );
	}
	
	
	
}


