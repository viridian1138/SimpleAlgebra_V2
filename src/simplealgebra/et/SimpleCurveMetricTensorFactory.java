





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






package simplealgebra.et;

import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Factory for generating simple metric tensors as defined in General Relativity.
 * 
 * <P>
 * <P> See <A href="http://physics.stackexchange.com/questions/33950/what-is-the-equation-of-the-gravitational-potential-in-general-relativity">http://physics.stackexchange.com/questions/33950/what-is-the-equation-of-the-gravitational-potential-in-general-relativity</A>
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the tensor indices.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SimpleCurveMetricTensorFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	/**
	 * Factory for symbolic elems.
	 */
	SymbolicElemFactory<R, S> fac;
	
	/**
	 * The square of the speed of light.
	 */
	SymbolicElem<R, S> cSquared;
	
	/**
	 *  2 * U( x ) where U( x ) is the grav. potential along x.
	 */
	SymbolicElem<R, S> t_2Ux;
	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac Factory for symbolic elems.
	 * @param _cSquared The square of the speed of light.
	 * @param _t_2Ux -- 2 * U( x ) where U( x ) is the grav. potential along x.
	 * 
	 *  http://physics.stackexchange.com/questions/33950/what-is-the-equation-of-the-gravitational-potential-in-general-relativity
	 *  
	 *  http://en.wikipedia.org/wiki/Metric_tensor_%28general_relativity%29
	 *  
	 */
	public SimpleCurveMetricTensorFactory( SymbolicElemFactory<R, S> _fac , SymbolicElem<R, S> _cSquared , SymbolicElem<R, S> _t_2Ux )
	{
		fac = _fac;
		cSquared = _cSquared;
		t_2Ux = _t_2Ux;
	}
	
	
	/**
	 * Returns an instance of the metric tensor.
	 * 
	 * @param covariantIndic Whether the metric tensor is to have covariant indices or contravariant indices.
	 * @param index0 First index for the tensor of rank two.
	 * @param index1 Second index for the tensor of rank two.
	 * @param numElem The number of dimensions in the tensor.
	 * @return An instance of the metric tensor.
	 * @throws NotInvertibleException
	 */
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
		getMetricTensor( boolean covariantIndic , Z index0 , Z index1 , final BigInteger numElem ) throws NotInvertibleException
		{
		
			ArrayList<Z> contravariantIndices = new ArrayList<Z>();
			ArrayList<Z> covariantIndices = new ArrayList<Z>();
			
			
			if( covariantIndic )
			{
				covariantIndices.add( index0 );
				covariantIndices.add( index1 );
			}
			else
			{
				contravariantIndices.add( index0 );
				contravariantIndices.add( index1 );
			}
			
		
			EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> tel =
				new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( fac , contravariantIndices , covariantIndices );
			
			{
				PotentialAlteredCSquared<R,S> cSq =
						new PotentialAlteredCSquared<R,S>( fac , cSquared , t_2Ux );
				ArrayList<BigInteger> el = new ArrayList<BigInteger>();
				el.add( BigInteger.ZERO );
				el.add( BigInteger.ZERO );
				tel.setVal( el , cSq.getAlteredCSquared( true /* covariantIndic */ ) );
			}
			
			for( BigInteger cnt = BigInteger.ONE ; cnt.compareTo(numElem) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
			{
				ArrayList<BigInteger> el = new ArrayList<BigInteger>();
				el.add( cnt );
				el.add( cnt );
				tel.setVal( el , fac.identity() );
			}
			
			
			if( !covariantIndic ) 
			{
				final NumDimensions nd = new NumDimensions()
				{

					@Override
					public BigInteger getVal() {
						return( numElem );
					}
					
				};
				
				final SquareMatrixElem<NumDimensions,SymbolicElem<R, S>,SymbolicElemFactory<R, S>> tmp = 
						new SquareMatrixElem<NumDimensions,SymbolicElem<R, S>,SymbolicElemFactory<R, S>>( fac , nd );
				
				tel.rankTwoTensorToSquareMatrix( tmp );
				
				try
				{
					final SquareMatrixElem<NumDimensions,SymbolicElem<R, S>,SymbolicElemFactory<R, S>> inv = tmp.invertLeft();
					
					final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> retA =
							new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>(fac , contravariantIndices , covariantIndices );
					
					inv.toRankTwoTensor( retA );
					
					tel = retA;
				}
				catch( NotInvertibleException ex )
				{
					throw( new RuntimeException( "Invert Failed" ) );
				}
				
			}
			
			
	//		{
	//			ArrayList<BigInteger> el = new ArrayList<BigInteger>();
	//			el.add( BigInteger.ZERO );
	//			el.add( BigInteger.ZERO );
	//			SymbolicElem<R,S> els = tel.getVal( el );
	//			els.writeString( System.out );
	//			System.out.println( "" );
	//		}
			
			
			return( tel );
		}

}


