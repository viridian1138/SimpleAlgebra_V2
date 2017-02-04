



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
 * Directed edge from the original VRML.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class OrigDirEdge {

	/**
	 * Constructs the directed edge.
	 * @param _dir The direction with respect to the underlying edge.
	 * @param _edge The underlying edge.
	 */
	public OrigDirEdge( boolean _dir , OrigEdge _edge ) {
		dir = _dir;
		edge = _edge;
	}
	
	/**
	 * The direction with respect to the underlying edge.
	 */
	protected boolean dir;
	
	/**
	 * The underlying edge.
	 */
	protected OrigEdge edge;
	
	
	@Override
	public int hashCode()
	{
		final int hc = ( edge.hashCode() ) + ( dir ? 1 : 0 );
		
		return( hc );
	}
	
	
	@Override
	public boolean equals( Object i )
	{
		if( i instanceof OrigDirEdge )
		{
			OrigDirEdge p = (OrigDirEdge) i;
			
			return( ( edge.equals( p.edge ) ) && ( dir == p.dir ) );
		}
		return( false );
	}
	
	
	/**
	 * Gets the first point of the edge.
	 * @return The first point of the edge.
	 */
	public OrigPoint getP0()
	{
		return( dir ? edge.getP0() : edge.getP1() );
	}
	
	
	/**
	 * Gets the second point of the edge.
	 * @return The second point of the edge.
	 */
	public OrigPoint getP1()
	{
		return( dir ? edge.getP1() : edge.getP0() );
	}
	
	
	/**
	 * Calculates the delta between the points of the edge.
	 * @return The delta between the points of the edge.
	 */
	public GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> calcDelta()
	{
		return( getP1().getLocn().add( getP0().getLocn().negate() ) );
	}

	
}


