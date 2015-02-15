






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
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.FlowVectorFactory;
import simplealgebra.ddx.MaterialDerivativeFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.et.DerivativeRemap;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.MetricTensorInvertingFactory;
import simplealgebra.et.SimpleCurveMetricTensorFactory;
import simplealgebra.et.SymbolicTensorResym;
import simplealgebra.et.TemporaryIndexFactory;
import simplealgebra.et.SymbolicTensorResym.ResymType;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicOps;
import simplealgebra.symbolic.SymbolicSqrt;
import simplealgebra.symbolic.SymbolicZero;




public class TestMaterialDerivativeFlatAsym3D extends TestCase 
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
			
			
			
			return( new A2_Elem( this.getFac().getFac().getFac().getFac() )
					/* createSymbolicZero( this.getFac().getFac().getFac().getFac() ) */ );
			
		}

		@Override
		public EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			final TestDimensionThree td = new TestDimensionThree();
			
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
			ps.print( "v()" );
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
	 * An symbolic elem for a tensor.  Used to represent the metric tensor.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class SymbolicMetricTensor extends SymbolicElem<EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>,EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>>
	{
		
		/**
		 * The tensor to be represented by the symbolic elem.
		 */
		protected EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
			dval = null;

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _dval The tensor to be represented by the symbolic elem.
		 */
		public SymbolicMetricTensor(
				EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> _fac,
				EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> _dval ) {
			super(_fac);
			dval = _dval;
		}

		@Override
		public EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( dval );
		}

		@Override
		public EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> evalPartialDerivative(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public void writeString( PrintStream ps ) {
			Iterator<ArrayList<BigInteger>> it = dval.getKeyIterator();
			while( it.hasNext() )
			{
				final ArrayList<BigInteger> key = it.next();
				ps.print( "\n" + "** " );
				( dval.getVal( key ) ).writeString( ps );
			}
		}
		
	}
	
	
	
	/**
	 * Factory for generating an instance of the metric tensor.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TestMetricTensorFactory extends MetricTensorInvertingFactory<Object, TestDimensionThree, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory> >
	{
	

		@Override
		public SymbolicElem<EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> getMetricTensor(
				boolean icovariantIndices, Object index0, Object index1 ) {
			
			final TestDimensionThree td = new TestDimensionThree();
			final DoubleElemFactory de = new DoubleElemFactory();
			
			final ArrayList<Object> contravariantIndices = new ArrayList<Object>();
			final ArrayList<Object> covariantIndices = new ArrayList<Object>();
			if( icovariantIndices ) covariantIndices.add( index0 ); else contravariantIndices.add( index0 );
			if( icovariantIndices ) covariantIndices.add( index1 ); else contravariantIndices.add( index1 );
			
			
			final ComplexElemFactory<DoubleElem,DoubleElemFactory> ce = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de );
			
			
			final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> seA = 
					new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( ce );
			
			
			final EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> ge
				= new EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>( seA );
			
			
			
			
			EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> g0 =
						new EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>( 
									seA , contravariantIndices , covariantIndices );
			
			
			
			for( int acnt = 0 ; acnt < 3 ; acnt++ )
			{
				
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt ) );
				ab.add( BigInteger.valueOf( acnt ) );
				SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> as = 
						seA.identity();
				g0.setVal( ab , as );
			}
			

			if( !icovariantIndices )
			{
				g0 = genMatrixInverseLeft( td , seA , g0 );
			}
			
			
			final SymbolicMetricTensor seval = new SymbolicMetricTensor( ge , g0 );
			
			
			return( seval );
		}
				
		
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
			
			

			ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>> tmp = new ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>();
			tmp.add( new X2_Elem( new DoubleElemFactory() ) );
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
			
			
			
			return( new V2_Elem( fac )
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
					|| ( b instanceof A2_Elem ) 
					|| ( b instanceof V0_Elem ) || ( b instanceof V1_Elem ) 
					|| ( b instanceof V2_Elem )
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
	
	
	
	
	protected static class SymRemap extends DerivativeRemap<Object,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		@Override
		public SymbolicElem<EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> remap(
				SymbolicElem<EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> remap) 
		{
			final TestDimensionThree td = new TestDimensionThree();
			final DoubleElemFactory de = new DoubleElemFactory();
			
			final ComplexElemFactory<DoubleElem,DoubleElemFactory> ce = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de );
			
			final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> seA = 
					new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( ce );
			
			final EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> ge
				= new EinsteinTensorElemFactory<Object, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>( seA );
		
		
			
			SymbolicTensorResym<Object,TestDimensionThree,SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> el =
					new SymbolicTensorResym<Object,TestDimensionThree,SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
						( remap , ge , SymbolicTensorResym.ResymType.RESYM_ANTISYMMETRIC , td );
			
			return( el );
		}
		
	}
	
	
	
	protected static class Sym2Mutator implements Mutator<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
	{

		@Override
		public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> mutate(
				SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> in)
				throws NotInvertibleException {
			
			return( in.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null ) );
		}

		@Override
		public boolean exposesDerivatives() {
			return( true ); // Maybe it does, anyway.
		}

		@Override
		public String writeString() {
			return( "Sym2" );
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
	
	
	
	
	public void testMaterialDerivativeFlatAsym3D() throws NotInvertibleException
	{
		final TestDimensionThree td = new TestDimensionThree();
		
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
		
		final TestMetricTensorFactory scfac = new TestMetricTensorFactory( );
		
		final HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace = (HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>>)( new HashMap() );
		
		
		final SymRemap remap = new SymRemap();
		
		
		final MaterialDerivativeFactory<Object, TestDimensionThree, ComplexElem<DoubleElem,DoubleElemFactory>, 
				ComplexElemFactory<DoubleElem,DoubleElemFactory>, SymbolicElem<DoubleElem,DoubleElemFactory>>
			mf = new MaterialDerivativeFactory<Object, TestDimensionThree, ComplexElem<DoubleElem,DoubleElemFactory>, 
					ComplexElemFactory<DoubleElem,DoubleElemFactory>, SymbolicElem<DoubleElem,DoubleElemFactory>>
				( etfac , 
							vectFac ,
							null /* !!!!!!!!!!!!!1 Uveca !!!!!!!!!! */ ,
							new TestTemporaryIndexFactory(),
							scfac,
							td ,
							direcFac,
							flowFac,
							derivT,
							remap ); 
		
		
		
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
		for( i = 0 ; i < 3 ; i++ )
		{
			BigInteger ii = BigInteger.valueOf( i );
			ArrayList<BigInteger> el = new ArrayList<BigInteger>();
			el.add( ii );
				
			final SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>
				valI = mfTen.getVal( el );
			Assert.assertTrue( valI != null );
			// final SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>
			//	valI2 = valI.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null);
			System.out.print( "<P>" );
			valI.writeMathMLWrapped( comp , System.out ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// 		Assert.assertTrue( valI2 != null ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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


