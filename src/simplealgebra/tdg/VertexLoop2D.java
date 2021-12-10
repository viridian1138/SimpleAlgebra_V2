



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
 * Returns geometric measurements for a general vertex loop of a convex 2-D polygon.
 * The handedness of the polygon (i.e left-hand rule vs. right-hand rule) does not matter.
 * 
 * @author tgreen
 *
 * @param <U>
 * @param <A>
 * @param <R>
 * @param <S>
 */
public class VertexLoop2D<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends VertexCollection<U,A,R,S>
{

	/**
	 * Ordered list of vectors to the vertices of the loop.
	 */
	protected GeometricAlgebraMultivectorElem<U,A,R,S>[] vects;
	
	/**
	 * Constructs the vertex loop.
	 * @param vects Ordered list of vectors to the vertices of the loop.
	 */
	public VertexLoop2D( GeometricAlgebraMultivectorElem<U,A,R,S>[] vects )
	{
		this.vects = vects;
	}
	
	@Override
	public ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>[]> getVectsM1(int numDim, GeometricAlgebraMultivectorElemFactory<U,A,R,S> fac)
	{
		if( numDim != 2 )
		{
			throw( new IllegalArgumentException() );
		}
		
		if( vects.length == 3 )
		{
			ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>[]> ar = new ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>[]>();
			ar.add( vects );
			return( ar );
		}
		
		GeometricAlgebraMultivectorElem<U, A, R, S> avg = fac.zero();
		
		for( GeometricAlgebraMultivectorElem<U, A, R, S> v : vects )
		{
			avg = avg.add( v );
		}
		
		avg = avg.divideBy( vects.length );
		
		ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>[]> ar = new ArrayList<GeometricAlgebraMultivectorElem<U,A,R,S>[]>();
		
		
		for( int cnt = 0 ; cnt < vects.length - 1 ; cnt++ )
		{
			final GeometricAlgebraMultivectorElem<U,A,R,S> a0 = vects[ cnt ];
			final GeometricAlgebraMultivectorElem<U,A,R,S> a1 = vects[ cnt + 1 ];
			final GeometricAlgebraMultivectorElem<U,A,R,S>[] vout = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			vout[ 0 ] = a0;
			vout[ 1 ] = a1;
			vout[ 2 ] = avg;
			ar.add( vout );
		}
		

		{
			final GeometricAlgebraMultivectorElem<U,A,R,S> a0 = vects[ 0 ];
			final GeometricAlgebraMultivectorElem<U,A,R,S> a1 = vects[ vects.length - 1 ];
			final GeometricAlgebraMultivectorElem<U,A,R,S>[] vout = (GeometricAlgebraMultivectorElem<U,A,R,S>[])( new GeometricAlgebraMultivectorElem[ 3 ] );
			vout[ 0 ] = a0;
			vout[ 1 ] = a1;
			vout[ 2 ] = avg;
			ar.add( vout );
		}
		
		return( ar );
	}

	
	@Override
	public R calcM2(int numDim,
			GeometricAlgebraMultivectorElemFactory<U, A, R, S> fac,
			int numIterExp, int numIterLn) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		
		final Tdg_Facade<U,A,R,S> facade = new Tdg_Facade<U,A,R,S>();
		R sum = fac.getFac().zero();
		
		
		for( int cnt = 0 ; cnt < vects.length - 1 ; cnt++ )
		{
			final GeometricAlgebraMultivectorElem<U,A,R,S> p1 = vects[ cnt ];
			final GeometricAlgebraMultivectorElem<U,A,R,S> p2 = vects[ cnt + 1 ];
			sum = sum.add( facade.calcLineSegmentLength(p1, p2, numIterExp, numIterLn) );
		}
		

		{
			final GeometricAlgebraMultivectorElem<U,A,R,S> p1 = vects[ 0 ];
			final GeometricAlgebraMultivectorElem<U,A,R,S> p2 = vects[ vects.length - 1 ];
			sum = sum.add( facade.calcLineSegmentLength(p1, p2, numIterExp, numIterLn) );
		}
		
		
		return( sum );
	}
	
	
}


