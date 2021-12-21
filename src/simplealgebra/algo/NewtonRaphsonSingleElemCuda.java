






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






package simplealgebra.algo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import simplealgebra.BadCreationException;
import simplealgebra.CloneThreadCache;
import simplealgebra.DoubleElem;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.culang.CulangCompile;
import simplealgebra.jlang.JlangCompile;
import simplealgebra.symbolic.*;


/**
 * Newton-Raphson evaluator for a single element, using the formula <math display="inline">
 * <mrow>
 *  <msub>
 *          <mi>x</mi>
 *      <mrow>
 *        <mi>n</mi>
 *        <mo>+</mo>
 *        <mn>1</mn>
 *      </mrow>
 *  </msub>
 *  <mo>=</mo>
 *  <msub>
 *          <mi>x</mi>
 *        <mi>n</mi>
 *  </msub>
 *  <mo>+</mo>
 *  <mfrac>
 *    <mrow>
 *      <mo>f</mo>
 *      <mfenced open="(" close=")" separators=",">
 *        <mrow>
 *          <msub>
 *                  <mi>x</mi>
 *                <mi>n</mi>
 *          </msub>
 *        </mrow>
 *      </mfenced>
 *    </mrow>
 *    <mrow>
 *      <msup>
 *              <mo>f</mo>
 *            <mo>'</mo>
 *      </msup>
 *      <mfenced open="(" close=")" separators=",">
 *        <mrow>
 *          <msub>
 *                  <mi>x</mi>
 *                <mi>n</mi>
 *          </msub>
 *        </mrow>
 *      </mfenced>
 *    </mrow>
 *  </mfrac>
 * </mrow>
 * </math>
 *
 * 
 * <P>See http://en.wikipedia.org/wiki/Newton's_method
 * 
 * Works by taking in a template file and converting it to a set of definitions for Nvidia Cuda.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type for the evaluation.
 * @param <S> The factory for the enclosed type for the evaluation.
 */
public abstract class NewtonRaphsonSingleElemCuda<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	/**
	 * The function over which to evaluate Netwon-Raphson.
	 */
	protected SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> function;
	
	/**
	 * The evaluation of the function for use in the Newton-Raphson method.
	 */
	protected SymbolicElem<R,S> evalClone;
	
	/**
	 * The evaluation of the partial derivative of the function for use in the Newton-Raphson method.
	 */
	protected SymbolicElem<R,S> partialEvalClone;
	
	/**
	 * Compiler for Nvidia Cuda code generation.
	 */
	protected CulangCompile<R,S> compiler = new CulangCompile<R,S>();
	
	
	/**
	 * Constructs the evaluator.
	 * 
	 * @param _function The function over which to evaluate Newton-Raphson.
	 * @param _withRespectTo The variable over which to evaluate the derivative of the function.
	 * @param implicitSpaceFirstLevel The initial implicit space over which to take the function and its derivative.
	 * @param cache Cache to be used for symbolic evals if useCachedEval() returns true.
	 * @param templatePath The path to the template file.
	 * @param replaceMap Map of replacements to perform on #defines in the template file.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public NewtonRaphsonSingleElemCuda( final SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> _function , 
			final ArrayList<? extends Elem<?,?>> _withRespectTo , 
			final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel,
			final HashMap<SCacheKey<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElem<R, S>> cache ,
			final String templatePath , HashMap<String,String> replaceMap )
					throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		function = _function;
		final boolean useCachedEval = useCachedEval();
		evalClone = useCachedEval ? function.evalCached( implicitSpaceFirstLevel, cache) 
				: function.eval( implicitSpaceFirstLevel );
		partialEvalClone = useCachedEval ? function.evalPartialDerivativeCached(_withRespectTo, implicitSpaceFirstLevel, cache) 
				: function.evalPartialDerivative(_withRespectTo, implicitSpaceFirstLevel );
		evalClone = handleSimplification( evalClone , useSimplification() );
		partialEvalClone = handleSimplification( partialEvalClone , useSimplification() );
		compiler.attemptCulangCompile( evalClone , partialEvalClone , templatePath , replaceMap );
	}
	
	
	/**
	 * Handles the simplification of the elem.
	 * 
	 * @param in The elem to be simplified.
	 * @param smplType The type of simplification to be performed.
	 * @return The simplified elem.
	 * @throws NotInvertibleException
	 */
	protected SymbolicElem<R,S> handleSimplification( final SymbolicElem<R,S> in , final SimplificationType smplType ) throws NotInvertibleException
	{
		switch( smplType )
		{
			case NONE:
				return( in );
			case DISTRIBUTE_SIMPLIFY:
				return( in.distributeSimplify() );
			case DISTRIBUTE_SIMPLIFY2:
				return( in.distributeSimplify2() );
		}
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	
	/**
	 * Evaluates (runs) generated Nvidia Cuda code.
	 * 
	 * @param implicitSpaceInitialGuess The implicit space for the initial guess.
	 * @return An iterated result.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public void eval( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		compiler.eval();
	}
	
	
	/**
	 * Returns the type of simplification to be used.  
	 * Override this method to turn off expression simplification.
	 * 
	 * @return The type of simplification to be used.
	 */
	protected SimplificationType useSimplification()
	{
		return( SimplificationType.DISTRIBUTE_SIMPLIFY2 );
	}
	
	
	/**
	 * Returns whether cached evals are to be used.
	 * Override this method to turn on cached evals.
	 * 
	 * @return True iff. cached evals are to be used.
	 */
	protected boolean useCachedEval()
	{
		return( false );
	}


	
}



