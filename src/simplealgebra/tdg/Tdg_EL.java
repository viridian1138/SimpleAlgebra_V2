



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

import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;



/**
 * Returns geometric measurements for a transdimensional class 
 * of geometric figures with an equidistant locus of points
 * (from a center point).
 * 
 * @author tgreen
 *
 * @param <R> The enclosed elem type.
 * @param <S> The factory for the enclosed elem type.
 */
public class Tdg_EL<R extends Elem<R,?>, S extends ElemFactory<R,S>>
{
	
	/**
	 * The number PI to the desired number of digits.
	 */
	protected ComplexElem<R,S> pi;
	
	/**
	 * The factory for a complex number of the enclosed elem type.
	 */
	protected ComplexElemFactory<R,S> fac;
	
	/**
	 * The factory for the enclosed elem type.
	 */
	protected S sfac;
	
	/**
	 * The identity.
	 */
	protected R ident;
	
	/**
	 * Twice the identity.
	 */
	protected R two;
	
	/**
	 * The inverse of the imaginary number.
	 */
	protected ComplexElem<R,S> inverseI;
	
	/**
	 * Constructor.
	 * @param _pi The number PI to the desired number of digits.
	 */
	public Tdg_EL( ComplexElem<R,S> _pi ) throws NotInvertibleException
	{
		pi = _pi;
		fac = pi.getFac();
		sfac = fac.getFac();
		ident = sfac.identity();
		two = ident.add( ident );
		inverseI = (  
			new ComplexElem<R,S>( sfac.zero() , sfac.identity() ) ).invertLeft();
	}
	
	
	/**
	 * Node abstracting an exponential function.
	 * 
	 * @author tgreen
	 *
	 */
	protected class ExpNode
	{
		
		/**
		 * Constructs the node.
		 * @param _thetaCoeff The multiplicative constant applied to the imaginary number in the exponent.
		 * @param _expCoeff The multiplicative constant applied after evaluating the exponential.
		 */
		public ExpNode( BigInteger _thetaCoeff , ComplexElem<R,S> _expCoeff )
		{
			thetaCoeff = _thetaCoeff;
			expCoeff = _expCoeff;
		}
		
		
		/**
		 * Returns the definite integral of the node evaluated over the interval from zero to PI / 2.
		 * @param numIter The number of iterations to use when calculating exponentials.
		 * @return The definite integral of the node evaluated over the interval from zero to PI / 2.
		 * @throws NotInvertibleException
		 */
		public ComplexElem<R,S> integrate( final int numIter ) throws NotInvertibleException
		{
			
			/*
			 * If thetaCoeff is zero, then the exponent is the identity and the
			 * definite integral of the identity is theta evaluated over the pi/2 interval.
			 */
			if( thetaCoeff.equals( BigInteger.ZERO ) )
			{
				return( expCoeff.mult( pi ).divideBy( 2 ) );
			}
			
			
			final BigInteger thr = thetaCoeff.remainder( BigInteger.valueOf( 4 ) );
			
			
			/*
			 * If thetaCoeff is an even multiple of 4, then the integral multiplied by pi/2
			 * is an even multiple of 2 * pi, and hence always makes a full phase rotation
			 * to zero.
			 */
			if( ( thr ).equals( BigInteger.ZERO ) )
			{
				return( pi.getFac().zero() );
			}
			
			/*
			 * Start of interval e ^ 0 = 1.
			 */
			final ComplexElem<R,S> zA = pi.getFac().identity();
			
			final R thetaB = pi.getRe().divideBy( 2 ).mult( sfac.identity().divideBy( thr ).invertLeft() );
			
			final ComplexElem<R,S> thetaB2 = new ComplexElem<R,S>( sfac.zero() , thetaB );
			
			final ComplexElem<R,S> zB = thetaB2.exp(numIter);
			
			/*
			 * Evaluation of the integral except for multiplicative coefficients.
			 */
			final ComplexElem<R,S> zEval = zB.add( zA.negate() );
			
			/*
			 * Apply multiplicative coefficients to the above integral.
			 */
			
			final ComplexElem<R,S> zEval2 = zEval.divideBy( thetaCoeff ).mult( inverseI );
			
			final ComplexElem<R,S> zEval3 = zEval2.mult( expCoeff );
			
			return( zEval3 );
			
		}
		
		/**
		 * Returns the multiplication of this node by an input node.
		 * @param in The input node by which to multiply.
		 * @return The result of the multiplication.
		 */
		public ExpNode mult( final ExpNode in )
		{
			final BigInteger th = thetaCoeff.add( in.thetaCoeff );
			final ComplexElem<R,S> exp = expCoeff.mult( in.expCoeff );
			return( new ExpNode( th , exp ) );
		}
		
		/**
		 * The multiplicative constant applied to the imaginary number in the exponent.
		 */
		protected BigInteger thetaCoeff;
		
		/**
		 * The multiplicative constant applied after evaluating the exponential.
		 */
		protected ComplexElem<R,S> expCoeff;
		
	}
	
	
	
	/**
	 * Node abstracting a sum of exponential functions.
	 * 
	 * @author tgreen
	 *
	 */
	protected class ExpSum
	{
		
		/**
		 * Returns the definite integral of the node evaluated over the interval from zero to PI / 2.
		 * @param numIter The number of iterations to use when calculating exponentials.
		 * @return The definite integral of the node evaluated over the interval from zero to PI / 2.
		 * @throws NotInvertibleException
		 */
		public ComplexElem<R,S> integrate( final int numIter ) throws NotInvertibleException
		{
			ComplexElem<R,S> sum = fac.zero();
			
			for( final ExpNode r : lst )
			{
				sum = sum.add( r.integrate(numIter) );
			}
			
			return( sum );
		}
		
		/**
		 * Returns the addition of this node with an input node.
		 * @param in The input node to be added.
		 * @return The result of the addition.
		 */
		public ExpSum add( final ExpSum in )
		{
			final ExpSum ret = new ExpSum();
			
			ret.lst.addAll( in.lst );
			
			ret.lst.addAll( lst );
			
			return( ret );
		}
		
		/**
		 * Returns the multiplication of this node by an input node.
		 * @param in The input node by which to multiply.
		 * @return The result of the multiplication.
		 */
		public ExpSum mult( final ExpSum in )
		{
			final ExpSum ret = new ExpSum();
			
			for( final ExpNode i : lst )
			{
				for( final ExpNode j : in.lst )
				{
					ret.lst.add( i.mult( j ) );
				}
			}
			
			return( ret );
		}
		
		/**
		 * Adds an ExpNode to the sum.
		 * @param in The ExpNode to be added.
		 */
		public void add( final ExpNode in )
		{
			lst.add( in );
		}
		
		/**
		 * Adds a cosine function to the sum.
		 * @param thetaCoeff Multiplicative constant to be applied to the theta angle of the cosine.
		 * @param expCoeff Multiplicative constant to be applied after evaluating the cosine.
		 */
		public void addCosine( BigInteger thetaCoeff , ComplexElem<R,S> expCoeff )
		{
			final ComplexElem<R,S> actExpCoeff = expCoeff.divideBy( 2 );
			
			/*
			 * The cosine is broken into two ExpNode instances because the ExpNode is
			 * simpler than applying chains of double-angle theorems to (cosine)^N and (sine)^N.
			 */
			
			add( new ExpNode( thetaCoeff , actExpCoeff ) );
			add( new ExpNode( thetaCoeff.negate() , actExpCoeff ) );
		}
		
		/**
		 * Adds the differential element dx to the sum.
		 */
		public void addDx( )
		{
			/*
			 * dx is cos( theta ) * dTheta, where the dTheta is dropped because it is assumed.
			 */
			addCosine( );
		}
		
		/**
		 * Adds a cosine function to the sum.
		 */
		public void addCosine( )
		{
			addCosine( BigInteger.ONE , fac.identity() );
		}
		
		/**
		 * Adds the identity to the sum.
		 */
		public void addIdentity( )
		{
			/*
			 * The identity is mapped to e ^ 0 = 1.
			 */
			add( new ExpNode( BigInteger.ZERO , fac.identity() ) );
		}
		
		/**
		 * Adds the differential element dTheta to the sum.
		 */
		public void addDTheta( )
		{
			/*
			 * dTheta maps to the identity as the dTheta is assumed.
			 */
			addIdentity( );
		}
		
		/**
		 * Adds x to the sum.
		 */
		public void addX( )
		{
			/*
			 * x is r * cos( theta ) where r is assumed to be the identity.
			 * Hence x maps to cosine.
			 */
			addCosine( );
		}
		
		/**
		 * The list of ExpNode instances that are summed.
		 */
		protected final ArrayList<ExpNode> lst = new ArrayList<ExpNode>();
		
	}
	
	
	
	/**
	 * Returns the leading multiplicative coefficient for
	 * a geometric measurement for a transdimensional class 
     * of geometric figures with an equidistant locus of points
     * (from a center point).
	 * @param numDimensions The number of dimensions of the geometric figure.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The multiplicative coefficient.
	 * @throws NotInvertibleException
	 */
	protected R calcCoeffM2( final BigInteger numDimensions , final int numIter ) throws NotInvertibleException
	{	
		if( numDimensions.equals( BigInteger.ONE ) )
		{
			return( two );
		}
		
		final R prevCoeffM2 = calcCoeffM2( numDimensions.subtract( BigInteger.ONE ) , numIter );
		
		ExpSum es = new ExpSum();
		es.addDTheta();
		for( BigInteger cnt = BigInteger.valueOf( 3 ) ; cnt.compareTo( numDimensions ) <= 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			ExpSum es2 = new ExpSum();
			es2.addX();
			es = es.mult( es2 );
		}
		
		final ComplexElem<R,S> integ = es.integrate(numIter);
		final R ret = integ.getRe().mult( prevCoeffM2 ).mult( two );
		return( ret );
	}
	
	
	
	/**
	 * Returns the leading multiplicative coefficient for
	 * a geometric measurement for a transdimensional class 
     * of geometric figures with an equidistant locus of points
     * (from a center point).
	 * @param numDimensions The number of dimensions of the geometric figure.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The multiplicative coefficient.
	 * @throws NotInvertibleException
	 */
	protected R calcCoeffM1( final BigInteger numDimensions , final int numIter ) throws NotInvertibleException
	{	
		if( numDimensions.equals( BigInteger.ZERO ) )
		{
			return( ident );
		}
		
		final R prevCoeffM1 = calcCoeffM1( numDimensions.subtract( BigInteger.ONE ) , numIter );
		
		ExpSum es = new ExpSum();
		es.addDx();
		for( BigInteger cnt = BigInteger.valueOf( 2 ) ; cnt.compareTo( numDimensions ) <= 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			ExpSum es2 = new ExpSum();
			es2.addX();
			es = es.mult( es2 );
		}
		
		final ComplexElem<R,S> integ = es.integrate(numIter);
		final R ret = integ.getRe().mult( prevCoeffM1 ).mult( two );
		return( ret );
	}
	
	
	
	/**
	 * Returns a geometric measurement for a transdimensional class 
     * of geometric figures with an equidistant locus of points
     * (from a center point).
	 * @param numDimensions The number of dimensions of the geometric figure.
	 * @param radius The radius of the geometric figure.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The geometric measurement.
	 * @throws NotInvertibleException
	 */
	public R calcM2( final BigInteger numDimensions , final R radius , final int numIter ) throws NotInvertibleException
	{
		final R coeff = calcCoeffM2( numDimensions , numIter );
		R ret = coeff;
		for( BigInteger cnt = BigInteger.ONE ; cnt.compareTo( numDimensions ) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			ret = ret.mult( radius );
		}
		return( ret );
	}
	
	
	
	/**
	 * Returns a geometric measurement for a transdimensional class 
     * of geometric figures with an equidistant locus of points
     * (from a center point).
	 * @param numDimensions The number of dimensions of the geometric figure.
	 * @param radius The radius of the geometric figure.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The geometric measurement.
	 * @throws NotInvertibleException
	 */
	public R calcM1( final BigInteger numDimensions , final R radius , final int numIter ) throws NotInvertibleException
	{
		final R coeff = calcCoeffM1( numDimensions , numIter );
		R ret = coeff;
		for( BigInteger cnt = BigInteger.ONE ; cnt.compareTo( numDimensions ) <= 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			ret = ret.mult( radius );
		}
		return( ret );
	}
	
	
	
	
}


