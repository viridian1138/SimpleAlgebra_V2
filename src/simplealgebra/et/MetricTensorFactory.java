





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

import java.io.PrintStream;
import java.math.BigInteger;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Factory for generating metric tensors as defined in General Relativity.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 */
public abstract class MetricTensorFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>>
	extends RankTwoTensorFactory<Z,R,S>
{
	
	/**
	 * Returns a metric tensor.
	 * 
	 * @param covariantIndices
	 * @param index0 The first index of the metric tensor to be created.
	 * @param index1 The second index of the metric tensor to be created.
	 * @return The metric tensor.
	 */
	public abstract SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getMetricTensor( boolean covariantIndices , Z index0 , Z index1 );
	
	
	
	@Override
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getTensor( boolean covariantIndices , Z index0 , Z index1 )
	{
		return( getMetricTensor( covariantIndices , index0 , index1 ) );
	}
	
	
	
	@Override
	public MetricTensorFactory<Z,R,S> cloneThread( final BigInteger threadIndex )
	{
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	
	/**
	 * Writes a description of the instance to the output stream.
	 * 
	 * @param cache Instance cache from which to cache objects.
	 * @param ps Stream to write the description.
	 * @return String describing the id of the object.
	 */
	public String writeDesc( WriteMetricTensorFactoryCache<Z,R,S> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( this.getClass().getSimpleName() );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( this.getClass().getSimpleName() );
			ps.println( "();" );
		}
		return( st );
	}
	

}


