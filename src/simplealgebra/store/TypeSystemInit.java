



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






package simplealgebra.store;

import org.hypergraphdb.HyperGraph;

import simplealgebra.bigfixedpoint.db.BigFixedPointElemFactoryType;
import simplealgebra.bigfixedpoint.db.BigFixedPointElemType;
import simplealgebra.et.db.EinsteinTensorElemFactoryType;
import simplealgebra.et.db.EinsteinTensorElemType;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.db.GeometricAlgebraMultivectorElemFactoryType;
import simplealgebra.ga.db.GeometricAlgebraMultivectorElemType;


/**
 * Common initializer for HyperGraph types.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TypeSystemInit {

	
	/**
	 * Initialize the types in a graph.
	 * 
	 * @param graph The graph in which to initialize the types.
	 */
	public static void initType( HyperGraph graph )
	{
		DoubleElemType.initType( graph );
		ComplexElemType.initType( graph );
		ComplexElemFactoryType.initType( graph );
		
		BigFixedPointElemType.initType( graph );
		BigFixedPointElemFactoryType.initType( graph );
		
		EinsteinTensorElemType.initType( graph );
		EinsteinTensorElemFactoryType.initType( graph );
		
		GeometricAlgebraMultivectorElemType.initType( graph );
		GeometricAlgebraMultivectorElemFactoryType.initType( graph );
		
		SquareMatrixElemType.initType( graph );
		SquareMatrixElemFactoryType.initType( graph );
		
	}

	
}


