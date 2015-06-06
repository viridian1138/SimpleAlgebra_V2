



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
import simplealgebra.ddx.db.DirectionalDerivativeType;
import simplealgebra.ddx.db.PartialDerivativeOpType;
import simplealgebra.et.db.EinsteinTensorElemFactoryType;
import simplealgebra.et.db.EinsteinTensorElemType;
import simplealgebra.et.db.OrdinaryDerivativeType;
import simplealgebra.et.db.SymbolicIndexReductionType;
import simplealgebra.et.db.SymbolicRegenContravarType;
import simplealgebra.et.db.SymbolicRegenCovarType;
import simplealgebra.et.db.SymbolicTensorResymType;
import simplealgebra.ga.db.GeometricAlgebraMultivectorElemFactoryType;
import simplealgebra.ga.db.GeometricAlgebraMultivectorElemType;
import simplealgebra.ga.db.SymbolicDotType;
import simplealgebra.ga.db.SymbolicReverseLeftType;
import simplealgebra.ga.db.SymbolicReverseRightType;
import simplealgebra.ga.db.SymbolicWedgeType;
import simplealgebra.meas.db.ValueWithUncertaintyElemFactoryType;
import simplealgebra.meas.db.ValueWithUncertaintyElemType;
import simplealgebra.symbolic.db.SymbolicAbsoluteValueType;
import simplealgebra.symbolic.db.SymbolicAddType;
import simplealgebra.symbolic.db.SymbolicDivideByType;
import simplealgebra.symbolic.db.SymbolicElemFactoryType;
import simplealgebra.symbolic.db.SymbolicIdentityType;
import simplealgebra.symbolic.db.SymbolicInvertLeftType;
import simplealgebra.symbolic.db.SymbolicInvertRightType;
import simplealgebra.symbolic.db.SymbolicMultType;
import simplealgebra.symbolic.db.SymbolicMutableType;
import simplealgebra.symbolic.db.SymbolicNegateType;
import simplealgebra.symbolic.db.SymbolicPlaceholderType;
import simplealgebra.symbolic.db.SymbolicReductionType;
import simplealgebra.symbolic.db.SymbolicSqrtType;
import simplealgebra.symbolic.db.SymbolicZeroType;


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
		DbElemType.initType( graph );
		DbElemFactoryType.initType( graph );
		
		BigFixedPointElemType.initType( graph );
		BigFixedPointElemFactoryType.initType( graph );
		
		EinsteinTensorElemType.initType( graph );
		EinsteinTensorElemFactoryType.initType( graph );
		OrdinaryDerivativeType.initType( graph );
		SymbolicIndexReductionType.initType( graph );
		SymbolicRegenContravarType.initType( graph );
		SymbolicRegenCovarType.initType( graph );
		SymbolicTensorResymType.initType( graph );
		
		GeometricAlgebraMultivectorElemType.initType( graph );
		GeometricAlgebraMultivectorElemFactoryType.initType( graph );
		SymbolicDotType.initType( graph );
		SymbolicReverseLeftType.initType( graph );
		SymbolicReverseRightType.initType( graph );
		SymbolicWedgeType.initType( graph );
		
		SquareMatrixElemType.initType( graph );
		SquareMatrixElemFactoryType.initType( graph );
		SymbolicTransposeType.initType( graph );
		SymbolicMultRevCoeffType.initType( graph );
		SymbolicInvertLeftRevCoeffType.initType( graph );
		SymbolicInvertRightRevCoeffType.initType( graph );	
		SymbolicConjugateLeftType.initType( graph );
		SymbolicConjugateRightType.initType( graph );
		MultLeftMutatorType.initType( graph );
		MultRightMutatorType.initType( graph );
		
		SymbolicAbsoluteValueType.initType( graph );
		SymbolicAddType.initType( graph );
		SymbolicDivideByType.initType( graph );
		SymbolicElemFactoryType.initType( graph );
		SymbolicIdentityType.initType( graph );
		SymbolicInvertLeftType.initType( graph );
		SymbolicInvertRightType.initType( graph );
		SymbolicMultType.initType( graph );
		SymbolicMutableType.initType( graph );
		SymbolicNegateType.initType( graph );
		SymbolicPlaceholderType.initType( graph );
		SymbolicReductionType.initType( graph );
		SymbolicSqrtType.initType( graph );
		SymbolicZeroType.initType( graph );
		
		ValueWithUncertaintyElemType.initType( graph );
		ValueWithUncertaintyElemFactoryType.initType( graph );
		
		PartialDerivativeOpType.initType( graph );
		DirectionalDerivativeType.initType( graph );
		
	}

	
}


