






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







package test_simplealgebra;


import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.FlowVectorFactory;
import simplealgebra.ddx.MaterialDerivativeFactory;
import simplealgebra.ddx.MaterialDerivativeFactoryParam;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.MetricTensorInvertingFactory;
import simplealgebra.et.SimpleCurveMetricTensorFactory;
import simplealgebra.et.TemporaryIndexFactory;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicOps;
import simplealgebra.symbolic.SymbolicSqrt;
import simplealgebra.symbolic.SymbolicZero;
import test_simplealgebra.TestMaterialDerivativeAsym4D.TestTemporaryIndexFactory;



/**
 * Tests the ability to generate the symbolic terms of the material derivative in 4-D.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestMaterialDerivativeSymbolic extends TestCase 
{

	
	/**
	 * Returns a symbolic instance of zero.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @return The symbolic instance of zero.
	 */
	private static SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>
		createSymbolicZero( ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac )
	{
		return( new SymbolicZero<ComplexElem<DoubleElem, DoubleElemFactory>,ComplexElemFactory<DoubleElem, DoubleElemFactory>>( _fac ) );
	}
	
	
	
	/**
	 * Elem representing the square of the speed of light.
	 * 
	 * @author thorngreen
	 *
	 */
	private class CSquaredElem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public CSquaredElem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof CSquaredElem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public boolean isPartialDerivativeZero()
		{
			return( true );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "cSquared( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> pc , PrintStream ps )
		{
			ps.print( "<msup><mi>c</mi><mn>2</mn></msup>" );
		}
		
	}
	
	
	
	/**
	 * Elem representing the potential term.
	 * 
	 * http://physics.stackexchange.com/questions/33950/what-is-the-equation-of-the-gravitational-potential-in-general-relativity
	 *  
	 * http://en.wikipedia.org/wiki/Metric_tensor_%28general_relativity%29
	 * 
	 * @author thorngreen
	 *
	 */
	private class T_2UxElem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public T_2UxElem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof T_2UxElem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "T_2Ux( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> pc , PrintStream ps )
		{
			ps.print( "<mn>2</mn><mo>&InvisibleTimes;</mo><mi>U</mi><mfenced><mi>x</mi></mfenced>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 0-th component of the vector potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private class A0_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public A0_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof A0_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "A0( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>A</mi><mn>0</mn></msub>" );
		}
		
	}
	
	
	/**
	 * Node representing the 1st component of the vector potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private class A1_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public A1_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof A1_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "A1( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>A</mi><mn>1</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 2nd component of the vector potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private class A2_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public A2_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof A2_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "A2( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>A</mi><mn>2</mn></msub>" );
		}
		
	}
	
	
	
	
	
	
	/**
	 * Node representing the 3rd component of the vector potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private class A3_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public A3_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof A3_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "A3( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>A</mi><mn>3</mn></msub>" );
		}
		
	}
	
	
	
	
	
	/**
	 * Node representing the 0-th ordinate of the flow velocity.
	 * 
	 * @author thorngreen
	 *
	 */
	private class V0_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public V0_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof V0_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "V0( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>v</mi><mn>0</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 1st ordinate of the flow velocity.
	 * 
	 * @author thorngreen
	 *
	 */
	private class V1_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public V1_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof V1_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "V1( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>v</mi><mn>1</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 2nd ordinate of the flow velocity.
	 * 
	 * @author thorngreen
	 *
	 */
	private class V2_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public V2_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof V2_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "V2( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>v</mi><mn>2</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 3rd ordinate of the flow velocity.
	 * 
	 * @author thorngreen
	 *
	 */
	private class V3_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public V3_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof V3_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "V3( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>v</mi><mn>3</mn></msub>" );
		}
		
	}
	
	
	
	
	/**
	 * Elem representing the vector potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private class VectFac extends SymbolicElem<EinsteinTensorElem<Object,SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,EinsteinTensorElemFactory<Object,SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
	{

		
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _index The contravariant index of the tensor.
		 */
		public VectFac(
				EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> _fac,
				Object _index ) {
			super(_fac);
			index = _index;
		}

		/**
		 * Returns one component of the vector potential.
		 * 
		 * @param basisIndex The index of the component.
		 * @return The component of the vector potential.
		 */
		public SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> getVectorPotential( BigInteger basisIndex )
		{
			
			if( basisIndex.equals( BigInteger.ZERO ) )
			{
				return( new A0_Elem( this.getFac().getFac().getFac().getFac() )
						/* createSymbolicZero( this.getFac().getFac().getFac().getFac() ) */ );
			}
			
			
			if( basisIndex.equals( BigInteger.ONE ) )
			{
				return( new A1_Elem( this.getFac().getFac().getFac().getFac() ) 
						/* createSymbolicZero( this.getFac().getFac().getFac().getFac() ) */ );
			}
			
			
			if( basisIndex.equals( BigInteger.valueOf( 2 ) ) )
			{
				return( new A2_Elem( this.getFac().getFac().getFac().getFac() )
						/* createSymbolicZero( this.getFac().getFac().getFac().getFac() ) */ );
			}
			
			
			return( new A3_Elem( this.getFac().getFac().getFac().getFac() ) 
						/* createSymbolicZero( this.getFac().getFac().getFac().getFac() ) */ );
		}

		@Override
		public EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			final TestDimensionFour td = new TestDimensionFour();
			
			final SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> facC = 
					this.getFac().getFac().getFac();
			
			final ArrayList<Object> contravariantIndices = new ArrayList<Object>();
			final ArrayList<Object> covariantIndices = new ArrayList<Object>();
		
			contravariantIndices.add( index );
		
			final EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> mul = 
					new EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>( facC , 
							contravariantIndices , covariantIndices );
		
			BigInteger cnt = BigInteger.ZERO;
		
			final BigInteger max = td.getVal();
		
			for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
			{
				final ArrayList<BigInteger> key = new ArrayList<BigInteger>();
				key.add( cnt );
			
				SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> val = 
						this.getVectorPotential( cnt );
			
				mul.setVal(key, val);
			}
		
			return( mul );
		}

		@Override
		public EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			final EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
				v = this.eval( implicitSpace );
			
			final PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>,SymbolicElem<DoubleElem,DoubleElemFactory>> op = 
					new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>,SymbolicElem<DoubleElem,DoubleElemFactory>>(
							this.getFac().getFac().getFac().getFac(), (ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>) withRespectTo );
			
			final ArrayList<Object> indices = new ArrayList<Object>();
			
			final EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
				el = new EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>(
						this.getFac().getFac().getFac(), indices, indices);
			
			el.setVal( new ArrayList<BigInteger>() , op );
			
			return( el.mult( v ) );
		}

		@Override
		public void writeString(PrintStream ps) {
			ps.print( "A()" );
		}
		
		/**
		 * The contravariant index of the tensor.
		 */
		private Object index;

	};
	
	
	
	/**
	 * Node representing the 0-th ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private class X0_Elem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public X0_Elem(DoubleElemFactory _fac ) {
			super(_fac);
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof X0_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "X0( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<DoubleElem,DoubleElemFactory> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>x</mi><mn>0</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 1st ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private class X1_Elem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public X1_Elem(DoubleElemFactory _fac ) {
			super(_fac);
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof X1_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "X1( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<DoubleElem,DoubleElemFactory> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>x</mi><mn>1</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 2nd ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private class X2_Elem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public X2_Elem(DoubleElemFactory _fac ) {
			super(_fac);
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof X2_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "X2( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<DoubleElem,DoubleElemFactory> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>x</mi><mn>2</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 3rd ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private class X3_Elem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public X3_Elem(DoubleElemFactory _fac ) {
			super(_fac);
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof X3_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "X3( )" );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator<DoubleElem,DoubleElemFactory> pc , PrintStream ps )
		{
			ps.print( "<msub><mi>x</mi><mn>3</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Elem for the derivative with respect to X0 as a tensor.
	 * 
	 * @author thorngreen
	 *
	 */
	private class DerivTElem extends SymbolicElem<EinsteinTensorElem<Object,SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,EinsteinTensorElemFactory<Object,SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public DerivTElem(
				EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> _fac) {
			super(_fac);
		}

		@Override
		public EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			final ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>> lst =
					new ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>>();
			
			lst.add( new X0_Elem( this.getFac().getFac().getFac().getFac().getFac() ) );
			
			final PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>,SymbolicElem<DoubleElem,DoubleElemFactory>> op = 
					new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>,SymbolicElem<DoubleElem,DoubleElemFactory>>(
							this.getFac().getFac().getFac().getFac(), lst );
			
			final ArrayList<Object> indices = new ArrayList<Object>();
			
			EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
				el = new EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>(
						this.getFac().getFac().getFac(), indices, indices);
			
			el.setVal( new ArrayList<BigInteger>() , op );
			
			return( el );
		}

		@Override
		public EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			final EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
			v = this.eval( implicitSpace );
		
			final PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>,SymbolicElem<DoubleElem,DoubleElemFactory>> op = 
					new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>,SymbolicElem<DoubleElem,DoubleElemFactory>>(
							this.getFac().getFac().getFac().getFac(), (ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>) withRespectTo );
		
			final ArrayList<Object> indices = new ArrayList<Object>();
		
			final EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
				el = new EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>(
						this.getFac().getFac().getFac(), indices, indices);
		
			el.setVal( new ArrayList<BigInteger>() , op );
		
			return( el.mult( v ) );
		}

		@Override
		public void writeString(PrintStream ps) {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
	}
	
	
	
	/**
	 * A simple instance of a metric tensor.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class SimpleMetric extends SymbolicElem<EinsteinTensorElem<Object,SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,EinsteinTensorElemFactory<Object,SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
	{

		/**
		 * Constructs the metric tensor.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _icovariantIndices Indicates whether the indices should be contravariant or covariant.
		 * @param _index0 First tensor index of the metric.
		 * @param _index1 Second tensor index of the metric.
		 * @param _cSquared Symbolic elem for the square of the speed of light.
		 * @param _t_2Ux Symbolic elem for the potential term.
		 */
		public SimpleMetric(
				EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> _fac,
				boolean _icovariantIndices, Object _index0, Object _index1,
				SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> _cSquared,
				SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> _t_2Ux ) {
			super(_fac);
			icovariantIndices = _icovariantIndices;
			index0 = _index0;
			index1 = _index1;
			cSquared = _cSquared;
			t_2Ux = _t_2Ux;
		}

		@Override
		public EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			final TestDimensionFour td = new TestDimensionFour();
			
			
			SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>
				se = this.getFac().getFac().getFac();
			
			
			SimpleCurveMetricTensorFactory<Object, ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> cnf =
						new SimpleCurveMetricTensorFactory<Object, ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>(
								se, cSquared, t_2Ux);
			
			
			
			EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> ret =
					cnf.getMetricTensor(icovariantIndices, index0, index1, td.getVal() );
			
			return( ret );
		}

		@Override
		public EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			final EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
			v = this.eval( implicitSpace );
		
			final PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>,SymbolicElem<DoubleElem,DoubleElemFactory>> op = 
					new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>,SymbolicElem<DoubleElem,DoubleElemFactory>>(
							this.getFac().getFac().getFac().getFac(), (ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>) withRespectTo );
		
			final ArrayList<Object> indices = new ArrayList<Object>();
		
			final EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
				el = new EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>(
						this.getFac().getFac().getFac(), indices, indices);
		
			el.setVal( new ArrayList<BigInteger>() , op );
		
			return( el.mult( v ) );
		}

		@Override
		public void writeString(PrintStream ps) {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		
		/**
		 * Indicates whether the indices should be contravariant or covariant.
		 */
		private boolean icovariantIndices;
		
		/**
		 * First tensor index of the metric.
		 */
		private Object index0;
		
		/**
		 * Second tensor index of the metric.
		 */
		private Object index1;
		
		/**
		 * Symbolic elem for the square of the speed of light.
		 */
		private SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> cSquared;
		
		/**
		 * Symbolic elem for the potential term.
		 */
		private SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> t_2Ux;
		
	}
	
	
	
	/**
	 * Factory for generating an instance of the metric tensor.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TestMetricTensorFactory extends MetricTensorInvertingFactory<Object, TestDimensionFour, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory> >
	{
		
		/**
		 * Constructs the factory for metric tensors.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _cSquared Symbolic elem for the square of the speed of light.
		 * @param _t_2Ux Symbolic elem for the potential term.
		 */
		public TestMetricTensorFactory( EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> _fac ,
				SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> _cSquared,
				SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> _t_2Ux )
		{
			fac = _fac;
			cSquared = _cSquared;
			t_2Ux = _t_2Ux;
		}

		@Override
		public SymbolicElem<EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> getMetricTensor(
				boolean covariantIndices, Object index0, Object index1 ) {
			
			return( new SimpleMetric( fac , covariantIndices , index0 , index1 , cSquared , t_2Ux ) );
		}
		
		/**
		 * The factory for the enclosed type.
		 */
		private EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> fac;
		
		/**
		 * Symbolic elem for the square of the speed of light.
		 */
		private SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> cSquared;
		
		/**
		 * Symbolic elem for the potential term.
		 */
		private SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> t_2Ux;
		
		
	}
	
	
	
	/**
	 * Defines a directional derivative for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	private class DirecFac extends DirectionalDerivativePartialFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>, SymbolicElem<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * The factory for the enclosed type.
		 */
		ComplexElemFactory<DoubleElem,DoubleElemFactory> fac;
		
		/**
		 * Constructs the directional derivative factory.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public DirecFac( ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac )
		{
			fac = _fac;
		}
		
		@Override
		public PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>, SymbolicElem<DoubleElem, DoubleElemFactory>> getPartial(
				BigInteger basisIndex) {
			
			if( basisIndex.equals( BigInteger.ZERO ) )
			{
				ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>> tmp = new ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>();
				tmp.add( new X0_Elem( new DoubleElemFactory() ) );
				return( new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>, 
						SymbolicElem<DoubleElem, DoubleElemFactory>>( fac , tmp ) );
			}
			
			
			if( basisIndex.equals( BigInteger.ONE ) )
			{
				ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>> tmp = new ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>();
				tmp.add( new X1_Elem( new DoubleElemFactory() ) );
				return( new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>, 
						SymbolicElem<DoubleElem, DoubleElemFactory>>( fac , tmp ) );
			}
			
			
			if( basisIndex.equals( BigInteger.valueOf( 2 ) ) )
			{
				ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>> tmp = new ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>();
				tmp.add( new X2_Elem( new DoubleElemFactory() ) );
				return( new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>, 
						SymbolicElem<DoubleElem, DoubleElemFactory>>( fac , tmp ) );
			}
			
			
			ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>> tmp = new ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>();
			tmp.add( new X3_Elem( new DoubleElemFactory() ) );
			return( new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>, 
					SymbolicElem<DoubleElem, DoubleElemFactory>>( fac , tmp ) );
		}
		
	}
	
	
	
	
	/**
	 * Factory for the flow velocity.
	 * 
	 * @author thorngreen
	 *
	 */
	private class FlowFac extends FlowVectorFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>, SymbolicElem<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * The factory for the enclosed type.
		 */
		ComplexElemFactory<DoubleElem,DoubleElemFactory> fac;
		
		/**
		 * Constructs the factory.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public FlowFac( ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac )
		{
			fac = _fac;
		}
		
		@Override
		public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> getComponent(
				BigInteger basisIndex) {
			
			if( basisIndex.equals( BigInteger.ZERO ) )
			{
				return( new V0_Elem( fac ) 
						/* createSymbolicZero( fac ) */ );
			}
			
			
			if( basisIndex.equals( BigInteger.ONE ) )
			{
				return( new V1_Elem( fac )
						/* createSymbolicZero( fac ) */ );
			}
			
			
			if( basisIndex.equals( BigInteger.valueOf( 2 ) ) )
			{
				return( new V2_Elem( fac )
						/* createSymbolicZero( fac ) */ );
			}
			
			
			return( new V3_Elem( fac ) 
						/* createSymbolicZero( fac ) */ );
		}
		
	}
	
	
	
	/**
	 * Provides precedence comparison rules for the test.  Used when generating MathML.
	 * 
	 * @author thorngreen
	 *
	 */
	private class PrecCompare extends PrecedenceComparator<ComplexElem<DoubleElem, DoubleElemFactory>,ComplexElemFactory<DoubleElem, DoubleElemFactory>>
	{

		/**
		 * Constructs the precedence comparison object.
		 */
		public PrecCompare()
		{
		}
		
		@Override
		public boolean parenNeeded(
				SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> a,
				SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> b,
				boolean after) {
			
			if( ( b instanceof A0_Elem ) || ( b instanceof A1_Elem ) 
					|| ( b instanceof A2_Elem ) || ( b instanceof A3_Elem )
					|| ( b instanceof CSquaredElem ) || ( a instanceof SymbolicSqrt ) 
					|| ( b instanceof SymbolicSqrt ) || ( a instanceof PartialDerivativeOp )
					|| ( b instanceof PartialDerivativeOp ) || ( a instanceof T_2UxElem )
					|| ( b instanceof T_2UxElem ) )
			{
				return( false );
			}
			
			if( ( a instanceof SymbolicAdd ) && ( b instanceof SymbolicMult ) )
			{
				return( false );
			}
			
			// TODO Auto-generated method stub
			
			return( true );
		}
		
	}
	
	
	/**
	 * Generates temporary indices for use in tensor arithmetic.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TestTemporaryIndexFactory extends TemporaryIndexFactory<Object>
	{
		/**
		 * The current temporary index.
		 */
		protected static int tempIndex = 0;

		@Override
		public String getTemp() {
			String ret = "temp" + tempIndex;
			tempIndex++;
			return( ret );
		}
		
	}
	
	
	
	/**
	 * Tests the ability to generate the symbolic terms of the material derivative in 4-D.
	 */
	public void testMaterialDerivativeSymbolic() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> ce =
				new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dl );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			cSquared = new CSquaredElem( ce );
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			t_2Ux = new T_2UxElem( ce );
		
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>(ce);
		
		
		final EinsteinTensorElemFactory<Object,SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> etfac =
				new EinsteinTensorElemFactory<Object,SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( ye );
		
		
		
		
		final Object z0 = new Object()
		{
			
		};
		
		
		final Object z1 = new Object()
		{
			
		};
		
	
		
		final VectFac vectFac = new VectFac( etfac , "u" );
		
		
		final DirecFac direcFac = new DirecFac( ce );
		
		final FlowFac flowFac = new FlowFac( ce );
		
		final DerivTElem derivT = new DerivTElem( etfac );
		
		final TestMetricTensorFactory scfac = new TestMetricTensorFactory( etfac, cSquared , t_2Ux );
		
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>)( new HashMap() );
		
		
		final MaterialDerivativeFactoryParam<Object, TestDimensionFour, ComplexElem<DoubleElem,DoubleElemFactory>, 
			ComplexElemFactory<DoubleElem,DoubleElemFactory>, SymbolicElem<DoubleElem,DoubleElemFactory>> param =
			new MaterialDerivativeFactoryParam<Object, TestDimensionFour, ComplexElem<DoubleElem,DoubleElemFactory>, 
			ComplexElemFactory<DoubleElem,DoubleElemFactory>, SymbolicElem<DoubleElem,DoubleElemFactory>>();
		
		param.setFac( etfac );
		param.setTensorWithRespectTo( vectFac );
		param.setCoordVecFac( null ); /* !!!!!!!!!!!!!1 Uveca !!!!!!!!!! */
		param.setTemp( new TestTemporaryIndexFactory() );
		param.setMetric( scfac );
		param.setDim( td );
		param.setDfac( direcFac );
		param.setFlfac( flowFac );
		param.setDerivT( derivT );
		param.setRemap( null );
		
		
		final MaterialDerivativeFactory<Object, TestDimensionFour, ComplexElem<DoubleElem,DoubleElemFactory>, 
				ComplexElemFactory<DoubleElem,DoubleElemFactory>, SymbolicElem<DoubleElem,DoubleElemFactory>>
			mf = new MaterialDerivativeFactory<Object, TestDimensionFour, ComplexElem<DoubleElem,DoubleElemFactory>, 
					ComplexElemFactory<DoubleElem,DoubleElemFactory>, SymbolicElem<DoubleElem,DoubleElemFactory>>( param ); 
		
		
		
		EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
			mfTen = null;
		
		
		try
		{
			mfTen = mf.genTerms( implicitSpace ).eval( implicitSpace );
		}
		catch( Throwable ex )
		{
			ex.printStackTrace( System.out );
			throw( new RuntimeException( ex ) );
		}
		
		
	
		// System.out.println( d3.writeString() );
		
		
		
		Assert.assertTrue( mfTen != null );
		
		
		final PrecCompare comp = new PrecCompare();
		
		
		// System.out.println( "***" );
		int i;
		for( i = 0 ; i < TestDimensionFour.FOUR ; i++ )
		{
			BigInteger ii = BigInteger.valueOf( i );
			ArrayList<BigInteger> el = new ArrayList<BigInteger>();
			el.add( ii );
				
			final SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>
				valI = mfTen.getVal( el );
			Assert.assertTrue( valI != null );
			final SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>
				valI2 = valI.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null);
			System.out.print( "<P>" );
			valI2.writeMathMLWrapped( comp , System.out );
			Assert.assertTrue( valI2 != null );
	//		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	//			valA = valI.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null);
	//		valA.writeMathMLWrapped( comp , System.out );
	//		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	//			val = valA.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
	//		val.writeMathMLWrapped( comp , System.out );
			System.out.println( "" );
				
//			Assert.assertTrue( !( valI instanceof SymbolicZero ) );
				
			// System.out.println( "" + i + " " + j + " " + ( val.writeString() ) );
		}
		
		
		
	}
	
	
	
	
	
}


