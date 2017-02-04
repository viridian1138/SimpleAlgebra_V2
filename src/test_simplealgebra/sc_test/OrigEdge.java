



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


/**
 * Non-directed edge from the original VRML.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class OrigEdge {

	/**
	 * Constructs the edge.
	 * 
	 * @param _p0 The first point of the edge.
	 * @param _p1 The second point of the edge.
	 */
	public OrigEdge( OrigPoint _p0 , OrigPoint _p1 ) {
		p0 = _p0;
		p1 = _p1;
	}
	
	/**
	 * The first point of the ddge.
	 */
	OrigPoint p0;
	
	/**
	 * The second point of the edge.
	 */
	OrigPoint p1;
	
	@Override
	public int hashCode()
	{
		return( p0.hashCode() + p1.hashCode() );
	}
	
	@Override
	public boolean equals( Object i )
	{
		if( i instanceof OrigEdge )
		{
			OrigEdge ob = (OrigEdge) i;
			
			if( ( p0.equals( ob.p0 ) ) && ( p1.equals( ob.p1 ) ) )
			{
				return( true );
			}
			
			
			if( ( p0.equals( ob.p1 ) ) && ( p1.equals( ob.p0 ) ) )
			{
				return( true );
			}
			
		}
		return( false );
	}

	
	/**
	 * Gets the first point of the edge.
	 * @return The first point of the edge.
	 */
	public OrigPoint getP0() {
		return p0;
	}

	/**
	 * Gets the second point of the edge.
	 * @return The second point of the edge.
	 */
	public OrigPoint getP1() {
		return p1;
	}
	
	

}
