



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


import java.math.BigInteger;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.tolerant.DefaultDoubleElemTolerantResultFactory;
import simplealgebra.tolerant.TolerantElem;
import simplealgebra.tolerant.TolerantElemFactory;



/**
 * Tests tolerant matrix inverses.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestTolerantMatrix extends TestCase {
	
	
	/**
	 * Test method for performing a tolerant left-inverse of a zero matrix.
	 */
	public void testInvertZeroLeft() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final DefaultDoubleElemTolerantResultFactory dtrf = new DefaultDoubleElemTolerantResultFactory();
		
		TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> tf = new TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>(de, dtrf);
		
		SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> mat
			= new SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>(tf, td);
		
		
		SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> matI
			= mat.invertLeft();
		
		Assert.assertTrue( matI != null );
		
		
		for( int row = 0 ; row < TestDimensionFive.FIVE ; row++ )
		{
			for( int col = 0 ; col < TestDimensionFive.FIVE ; col++ )
			{
				TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> el = matI.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				DoubleElem dv = el.getValue();
				System.out.println( row + " " + col + " " + ( dv.getVal() ) );
			}
		}
		
		
	}
	
	
	/**
	 * Test method for performing a tolerant right-inverse of a zero matrix.
	 */
	public void testInvertZeroRight() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final DefaultDoubleElemTolerantResultFactory dtrf = new DefaultDoubleElemTolerantResultFactory();
		
		TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> tf = new TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>(de, dtrf);
		
		SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> mat
			= new SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>(tf, td);
		
		
		SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> matI
			= mat.invertRight();
		
		Assert.assertTrue( matI != null );
		
		
		for( int row = 0 ; row < TestDimensionFive.FIVE ; row++ )
		{
			for( int col = 0 ; col < TestDimensionFive.FIVE ; col++ )
			{
				TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> el = matI.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				DoubleElem dv = el.getValue();
				System.out.println( row + " " + col + " " + ( dv.getVal() ) );
			}
		}
		
		
	}
	
	
	/**
	 * Test method for performing a tolerant left-inverse of a zero matrix of matrices.
	 */
	public void testInvertZeroLeftNested() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final DefaultDoubleElemTolerantResultFactory dtrf = new DefaultDoubleElemTolerantResultFactory();
		
		TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> tf = new TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>(de, dtrf);
		
		SquareMatrixElemFactory<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> se
			= new SquareMatrixElemFactory<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>(tf, td);
		
		SquareMatrixElem<TestDimensionThree,SquareMatrixElem<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>,SquareMatrixElemFactory<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>> mat
			= new SquareMatrixElem<TestDimensionThree,SquareMatrixElem<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>,SquareMatrixElemFactory<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>>(se, td);
		
		
		SquareMatrixElem<TestDimensionThree,SquareMatrixElem<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>,SquareMatrixElemFactory<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>> matI
			= mat.invertLeft();
		
		Assert.assertTrue( matI != null );
		
		for( int rowa = 0 ; rowa < TestDimensionThree.THREE ; rowa++ )
		{
			for( int cola = 0 ; cola < TestDimensionThree.THREE ; cola++ )
			{
				SquareMatrixElem<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>
					ell = matI.getVal( BigInteger.valueOf(rowa) , 
							BigInteger.valueOf(cola) );
				for( int row = 0 ; row < TestDimensionThree.THREE ; row++ )
				{
					for( int col = 0 ; col < TestDimensionThree.THREE ; col++ )
					{
						TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> el = ell.getVal( BigInteger.valueOf(row) , 
							BigInteger.valueOf(col) );
						DoubleElem dv = el.getValue();
						System.out.println( rowa + " " + cola + " " + row + " " + col + " " + ( dv.getVal() ) );
					}
				}
			}
		}
		
		
	}
	
	
	/**
	 * Test method for performing a tolerant right-inverse of a zero matrix of matrices.
	 */
	public void testInvertZeroRightNested() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final DefaultDoubleElemTolerantResultFactory dtrf = new DefaultDoubleElemTolerantResultFactory();
		
		TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> tf = new TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>(de, dtrf);
		
		SquareMatrixElemFactory<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> se
			= new SquareMatrixElemFactory<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>(tf, td);
		
		SquareMatrixElem<TestDimensionThree,SquareMatrixElem<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>,SquareMatrixElemFactory<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>> mat
			= new SquareMatrixElem<TestDimensionThree,SquareMatrixElem<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>,SquareMatrixElemFactory<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>>(se, td);
		
		
		SquareMatrixElem<TestDimensionThree,SquareMatrixElem<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>,SquareMatrixElemFactory<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>> matI
			= mat.invertRight();
		
		Assert.assertTrue( matI != null );
		
		for( int rowa = 0 ; rowa < TestDimensionThree.THREE ; rowa++ )
		{
			for( int cola = 0 ; cola < TestDimensionThree.THREE ; cola++ )
			{
				SquareMatrixElem<TestDimensionThree,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>
					ell = matI.getVal( BigInteger.valueOf(rowa) , 
							BigInteger.valueOf(cola) );
				for( int row = 0 ; row < TestDimensionThree.THREE ; row++ )
				{
					for( int col = 0 ; col < TestDimensionThree.THREE ; col++ )
					{
						TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> el = ell.getVal( BigInteger.valueOf(row) , 
							BigInteger.valueOf(col) );
						DoubleElem dv = el.getValue();
						System.out.println( rowa + " " + cola + " " + row + " " + col + " " + ( dv.getVal() ) );
					}
				}
			}
		}
		
		
	}
	
	
	/**
	 * Test method for performing a tolerant left-inverse of a matrix with repeated rows.
	 */
	public void testInvertRepeatedRowLeft() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final DefaultDoubleElemTolerantResultFactory dtrf = new DefaultDoubleElemTolerantResultFactory();
		
		TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> tf = new TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>(de, dtrf);
		
		SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> mat
			= new SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>(tf, td);
		

		
		for( int cnt = 0; cnt < TestDimensionFive.FIVE ; cnt++ )
		{
			if( cnt == 1 )
			{

				mat.setVal( BigInteger.valueOf(1) , 
						BigInteger.valueOf(0), tf.identity() );
			}
			else
			{
				mat.setVal( BigInteger.valueOf(cnt) , 
						BigInteger.valueOf(cnt), tf.identity() );
			}
		}
		
		
		SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> matI
			= mat.invertLeft();
		
		Assert.assertTrue( matI != null );
		
		
		for( int row = 0 ; row < TestDimensionFive.FIVE ; row++ )
		{
			for( int col = 0 ; col < TestDimensionFive.FIVE ; col++ )
			{
				TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> el = matI.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				DoubleElem dv = el.getValue();
				System.out.println( row + " " + col + " " + ( dv.getVal() ) );
			}
		}
		
		
	}
	
	
	/**
	 * Test method for performing a tolerant right-inverse of a matrix with repeated rows.
	 */
	public void testInvertRepeatedRowRight() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final DefaultDoubleElemTolerantResultFactory dtrf = new DefaultDoubleElemTolerantResultFactory();
		
		TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> tf = new TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>(de, dtrf);
		
		SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> mat
			= new SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>(tf, td);
		

		
		for( int cnt = 0; cnt < TestDimensionFive.FIVE ; cnt++ )
		{
			if( cnt == 1 )
			{

				mat.setVal( BigInteger.valueOf(1) , 
						BigInteger.valueOf(0), tf.identity() );
			}
			else
			{
				mat.setVal( BigInteger.valueOf(cnt) , 
						BigInteger.valueOf(cnt), tf.identity() );
			}
		}
		
		
		SquareMatrixElem<TestDimensionFive,TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> matI
			= mat.invertRight();
		
		Assert.assertTrue( matI != null );
		
		
		for( int row = 0 ; row < TestDimensionFive.FIVE ; row++ )
		{
			for( int col = 0 ; col < TestDimensionFive.FIVE ; col++ )
			{
				TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> el = matI.getVal( BigInteger.valueOf(row) , 
						BigInteger.valueOf(col) );
				DoubleElem dv = el.getValue();
				System.out.println( row + " " + col + " " + ( dv.getVal() ) );
			}
		}
		
		
	}
	
	
	/**
	 * Test method for performing a tolerant left-inverse of a zero complex number.
	 */
	public void testInvertCplxZeroLeft() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final DefaultDoubleElemTolerantResultFactory dtrf = new DefaultDoubleElemTolerantResultFactory();
		
		TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> tf = new TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>(de, dtrf);
		
		ComplexElemFactory<TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> ce
			= new ComplexElemFactory<TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>(tf);
		
		ComplexElem<TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> cplx
			= ce.zero();
		
		ComplexElem<TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> cplxI
			= cplx.invertLeft();
		
		Assert.assertTrue( cplxI != null );
		
		
		System.out.println( "re: " + ( cplxI.getRe().getValue().getVal() ) );
		
		System.out.println( "im: " + ( cplxI.getIm().getValue().getVal() ) );
		
		
	}
	
	
	/**
	 * Test method for performing a tolerant right-inverse of a zero complex number.
	 */
	public void testInvertCplxZeroRight() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final DefaultDoubleElemTolerantResultFactory dtrf = new DefaultDoubleElemTolerantResultFactory();
		
		TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> tf = new TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>(de, dtrf);
		
		ComplexElemFactory<TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> ce
			= new ComplexElemFactory<TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>>(tf);
		
		ComplexElem<TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> cplx
			= ce.zero();
		
		ComplexElem<TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>,TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>> cplxI
			= cplx.invertRight();
		
		Assert.assertTrue( cplxI != null );
		
		
		System.out.println( "re: " + ( cplxI.getRe().getValue().getVal() ) );
		
		System.out.println( "im: " + ( cplxI.getIm().getValue().getVal() ) );
		
		
	}
	
	
	/**
	 * Tests running writeDesc() on tolerant elems.
	 */
	public void testWriteTolerant()
	{
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final DefaultDoubleElemTolerantResultFactory dtrf = new DefaultDoubleElemTolerantResultFactory();
		
		TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> tf = new TolerantElemFactory<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory>(de, dtrf);
		
		TolerantElem<DoubleElem,DoubleElemFactory,DefaultDoubleElemTolerantResultFactory> ident = tf.identity();
		
		String aa = tf.writeDesc( tf.generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		String bb = ident.writeDesc( ident.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + bb );
		
	}
	
	

	
	
}


