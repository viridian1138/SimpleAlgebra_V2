



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





package simplealgebra.tdg;

import java.util.ArrayList;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;


/**
 * Returns geometric measurements for a general all-levels-convex
 * (both the figure and all levels of externally facing sub-figures are convex) 
 * figure of dimension 3 or greater as an unordered collection of
 * externally facing sub-figures.
 * 
 * @author tgreen
 *
 * @param <U>
 * @param <A>
 * @param <R>
 * @param <S>
 */
public class VertexUnorderedCollection<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends VertexCollection<U,A,R,S>
{

	/**
	 * The extervally facing all-levels-convex sub-figures of the figure.
	 */
	protected ArrayList<VertexCollection<U,A,R,S>> subs;
	
	/**
	 * The vertices of the figure.
	 */
	protected ArrayList<GeometricAlgebraMultivectorElem<U, A, R, S>> vertices;
	
	/**
	 * Constructs the collection.
	 * @param subs The extervally facing all-levels-convex sub-figures of the figure.
	 * @param vertices The vertices of the figure.
	 */
	public VertexUnorderedCollection( ArrayList<VertexCollection<U,A,R,S>> subs , ArrayList<GeometricAlgebraMultivectorElem<U, A, R, S>> vertices )
	{
		this.subs = subs;
		this.vertices = vertices;
	}
	

	@Override
	public ArrayList<GeometricAlgebraMultivectorElem<U, A, R, S>[]> getVectsM1(
			int numDim, GeometricAlgebraMultivectorElemFactory<U, A, R, S> fac) {
		
		GeometricAlgebraMultivectorElem<U, A, R, S> avg = fac.zero();
		
		for( GeometricAlgebraMultivectorElem<U, A, R, S> vc : vertices )
		{
			avg = avg.add( vc );
		}
		avg = avg.divideBy( vertices.size() );
		
		final ArrayList<GeometricAlgebraMultivectorElem<U, A, R, S>[]> ret = new ArrayList<GeometricAlgebraMultivectorElem<U, A, R, S>[]>();
		
		
		for( final VertexCollection<U,A,R,S> sub : subs )
		{
			for( GeometricAlgebraMultivectorElem<U, A, R, S>[] inv : sub.getVectsM1(numDim-1, fac) )
			{
				GeometricAlgebraMultivectorElem<U, A, R, S>[] outv = 
						(GeometricAlgebraMultivectorElem<U, A, R, S>[])( new GeometricAlgebraMultivectorElem[inv.length+1] );
				
				for( int cnt = 0 ; cnt < inv.length ; cnt++ )
				{
					outv[ cnt ] = inv[ cnt ];
				}
				outv[ inv.length ] = avg;
				
				ret.add( outv );
			}
		}
		
		
		return( ret );
		
	}

	@Override
	public R calcM2(int numDim,
			GeometricAlgebraMultivectorElemFactory<U, A, R, S> fac,
			int numIterExp, int numIterLn) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		R sum = fac.getFac().zero();
		for( VertexCollection<U,A,R,S> vc : subs )
		{
			sum = sum.add( vc.calcM1(numDim-1, fac, numIterExp, numIterLn) );
		}
		return( sum );
	}
	
	
}


