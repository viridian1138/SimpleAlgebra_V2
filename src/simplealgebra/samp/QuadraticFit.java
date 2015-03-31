


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



package simplealgebra.samp;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.Ord;




public class QuadraticFit<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends
	Sampling<U,A,R,S>
{
	
	
	protected GeometricAlgebraMultivectorElem<U,A,R,S> xv; 
	protected GeometricAlgebraMultivectorElem<U,A,R,S> yv;
	
	protected R meanX;
	protected R meanY;
	protected R slope;
	protected R acc;
	
	
	public QuadraticFit( GeometricAlgebraMultivectorElem<U,A,R,S> _xv , GeometricAlgebraMultivectorElem<U,A,R,S> _yv )
	{
		xv = _xv;
		yv = _yv;
	}
	
	
	public void generateFit() throws NotInvertibleException
	{
		meanX = mean( xv );
		meanY = mean( yv );
		
		
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> slopesY = new GeometricAlgebraMultivectorElem<U,A,R,S>( xv.getFac().getFac() , 
				xv.getFac().getDim() , xv.getFac().getOrd() );
		slopeTransform( xv , yv , meanX , meanY , slopesY  );
		
		slope = mean( slopesY );
		
		
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> slopesY2 = new GeometricAlgebraMultivectorElem<U,A,R,S>( xv.getFac().getFac() , 
				xv.getFac().getDim() , xv.getFac().getOrd() );
		slopeTransform( xv , slopesY , meanX , meanY , slopesY2 );
		
		acc = mean( slopesY2 );
		
		
	}


	
	public R getMeanX() {
		return meanX;
	}


	public R getMeanY() {
		return meanY;
	}


	public R getSlope() {
		return slope;
	}


	public R getAcc() {
		return acc;
	}


	
}




