



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
 * Triangular face from the original VRML.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class OrigTriangularFace {

	
	/**
	 * Constructs the face.
	 * @param _dirEdges The directed edges of the face.
	 */
	public OrigTriangularFace( ArrayList<OrigDirEdge> _dirEdges ) {
		dirEdges = _dirEdges;
		for( OrigDirEdge i : dirEdges )
		{
			i.getP0().add( this );
			i.getP1().add( this );
		}
	}
	
	
	/**
	 * The directed edges of the face.
	 */
	protected ArrayList<OrigDirEdge> dirEdges = null;
	
	
	/**
	 * Gets a directed edge from the face.
	 * @param i The index of the edge (0-3).
	 * @return The directed edge.
	 */
	public OrigDirEdge getDirEdge( int i )
	{
		return( dirEdges.get( i ) );
	}
	
	
	@Override
	public int hashCode()
	{
		return( dirEdges.hashCode() );
	}
	
	
	@Override
	public boolean equals( Object i )
	{
		if( i instanceof OrigTriangularFace )
		{
			OrigTriangularFace p = (OrigTriangularFace) i;
			
			return( dirEdges.equals( p.dirEdges ) );
		}
		return( false );
	}
	
	
	/**
	 * The normal to the face.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> n = null;
	
	
	/**
	 * Calculates the normal to the face.
	 * @return The normal to the face.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> calcNormal() throws NotInvertibleException , MultiplicativeDistributionRequiredException
	{
		if( n != null )
		{
			return( n );
		}
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			d0 = dirEdges.get( 0 ).calcDelta();
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			d1 = dirEdges.get( 1 ).calcDelta();
		
		ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> argCross = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
		argCross.add( d1 );
		
		n = d0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.CROSS , argCross );
		
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

	
}


