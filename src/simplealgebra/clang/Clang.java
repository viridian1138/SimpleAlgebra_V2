



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





package simplealgebra.clang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.symbolic.SymbolicAbsoluteValue;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicDivideBy;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicInvertLeft;
import simplealgebra.symbolic.SymbolicInvertRight;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicNegate;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.symbolic.SymbolicZero;



/**
 * Class for converting some symbolic expressions to Clang (C Language) native code
 * for execution-time performance optimization.
 * 
 * @author tgreen
 *
 */
public class Clang {
	
		
	
	/**
	 * Class for storing individual variable names contributing to a complex number.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class CplxRec
	{
		
		/**
		 * Constructor.
		 * @param _re
		 * @param _im
		 */
		public CplxRec( String _re , String _im )
		{
			re = _re;
			im = _im;
		}
		
		/**
		 * Variable name for real value.
		 */
		String re;
		
		/**
		 * Variable name for imaginary value.
		 */
		String im;
	}
	
	
	/**
	 * The temporary directory in which to write and build clang code.
	 */
	static File tempDir = null;
	
	/**
	 * ClassLoader for loading generated classes.
	 */
	static URLClassLoader ucl = null;
	
	/**
	 * Allocation number used to assign unique IDs variables, classes, etc.
	 */
	static long allocNum = 0;
	
	
	/**
	 * Accumulated list of parameters for the constructor of the clang Elem.
	 */
	static ArrayList<Object> cnstParamsOuter;
	
	
	/**
	 * Accumulated list of parameters for the constructor of the clang Elem.
	 */
	static ArrayList<Object> cnstParamsInner;
	
	
	
	/**
	 * Stream for adding the actual expression evaluation logic to the generated clang code.
	 */
	static PrintStream psInst = null;
	
	/**
	 * Stream for adding members to the Java front-end for the generated clang code.
	 */
	static PrintStream jmemInst = null;
	
	/**
	 * Stream for adding definitions to the declaration of the constructor for the Java front-end to the generated clang code.
	 */
	static PrintStream jcnstInst = null;
	
	/**
	 * Stream for adding definitions to the implementation of the constructor for the Java front-end to the generated clang code.
	 */
	static PrintStream jcimpInst = null;
	
	/**
	 * Stream for adding local declarations to the generated clang code.
	 */
	static PrintStream jlocmemInst = null;
	
	/**
	 * Stream for adding initialization of local declarations to the generated clang code.
	 */
	static PrintStream jfldInst = null;
	
	/**
	 * Map used to prevent redundant calculations by tracking re-use of SymbolicElems.
	 */
	static HashMap<SymbolicElem<?,?>,Object> reuseMap = null;
	
	
	
	
	/**
	 * Handles a particular Elem of type SymbolicElem<DoubleElem,DoubleElemFactory>
	 * @param in The Elem to be handles.
	 * @return The generated clang variable name for the Elem.
	 * @throws Throwable
	 */
	protected static String hndl_Dbl( SymbolicElem<DoubleElem,DoubleElemFactory> in ) throws Throwable
	{
		if( reuseMap.get( in ) != null )
		{
			return( (String)( reuseMap.get( in ) ) );
		}
		
		if( in instanceof SymbolicZero )
		{
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = 0.0;" );
			return( vName );
		}
		
		if( in instanceof SymbolicIdentity )
		{
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = 1.0;" );
			return( vName );
		}
		
		if( in instanceof SymbolicNegate )
		{
			SymbolicNegate<DoubleElem,DoubleElemFactory> neg
				= (SymbolicNegate<DoubleElem,DoubleElemFactory>) in;
			String arg = hndl_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = - " + arg + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicAdd )
		{
			SymbolicAdd<DoubleElem,DoubleElemFactory> neg
				= (SymbolicAdd<DoubleElem,DoubleElemFactory>) in;
			String argA = hndl_Dbl( neg.getElemA() );
			reuseMap.put( neg.getElemA() , argA );
			String argB = hndl_Dbl( neg.getElemB() );
			reuseMap.put( neg.getElemB() , argB );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = " + argA + " + " + argB + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicMult )
		{
			SymbolicMult<DoubleElem,DoubleElemFactory> neg
				= (SymbolicMult<DoubleElem,DoubleElemFactory>) in;
			String argA = hndl_Dbl( neg.getElemA() );
			reuseMap.put( neg.getElemA() , argA );
			String argB = hndl_Dbl( neg.getElemB() );
			reuseMap.put( neg.getElemB() , argB );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = " + argA + " * " + argB + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicDivideBy )
		{
			SymbolicDivideBy<DoubleElem,DoubleElemFactory> neg
				= (SymbolicDivideBy<DoubleElem,DoubleElemFactory>) in;
			String arg = hndl_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = " + arg + " / " + ( neg.getIval() ) + "L;" );
			return( vName );
		}
		
		if( in instanceof SymbolicInvertLeft )
		{
			SymbolicInvertLeft<DoubleElem,DoubleElemFactory> neg
				= (SymbolicInvertLeft<DoubleElem,DoubleElemFactory>) in;
			String arg = hndl_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = 1.0 / " + arg + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicInvertRight )
		{
			SymbolicInvertRight<DoubleElem,DoubleElemFactory> neg
				= (SymbolicInvertRight<DoubleElem,DoubleElemFactory>) in;
			String arg = hndl_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = 1.0 / " + arg + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicReduction )
		{
			final DoubleElem eval = in.eval( null );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = " + ( eval.getVal() ) + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicAbsoluteValue )
		{
			SymbolicAbsoluteValue<DoubleElem,DoubleElemFactory> neg
				= (SymbolicAbsoluteValue<DoubleElem,DoubleElemFactory>) in;
			String arg = hndl_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = fabs( " + arg + " );" );
			return( vName );
		}
		
		
		System.out.println( in.getClass() );
		allocNum++;
		final long memberNum = allocNum;
		final String memberName = "m_" + memberNum;
		// jmemInst.println( "private SymbolicElem<DoubleElem,DoubleElemFactory> " + memberName + ";" );
		// jcnstInst.println( " , SymbolicElem<DoubleElem,DoubleElemFactory> _" + memberName );
		// jcimpInst.println( memberName + " = arr.get( " + ( cnstParamsInner.size() ) + " );" );
		jlocmemInst.println( "jobject loc" + memberName + " = NULL;" );
		
		// jfldInst.println( "if( loc" + memberName + " == NULL )" );
		jfldInst.println( "if( true )" );
		jfldInst.println( "{" );
		jfldInst.println( "   loc" + memberName + " = env->GetObjectArrayElement( locnarr , " + cnstParamsInner.size() + " );" );
		jfldInst.println( "}" );
		jfldInst.println( "if( loc" + memberName + " == NULL )" );
		jfldInst.println( "{" );
		jfldInst.println( "   printf( \"Unable To Find member " + memberName + "\\n\" );" );
		jfldInst.println( "   exit(1);" );
		jfldInst.println( "}" );
		
		cnstParamsInner.add( in );
		allocNum++;
		final String vNameZ = "t_" + allocNum;
		psInst.println( "jobject " + vNameZ + " = env->CallObjectMethod( loc" + memberName + " , symEval , hsh );" );
		allocNum++;
		final String vName = "t_" + allocNum;
		// psInst.println( "printf( \"Initiated Call \\n\" );" );
		psInst.println( "const jdouble " + vName + " = env->CallDoubleMethod( " + vNameZ + " , dblGetVal );" );
		psInst.println( vNameZ + " = NULL;" );
		// psInst.println( "printf( \"Finsihed Call %lf\\n\" , " + vName + " );" );
		return( vName );
	}
	
	
	

	
	
	/**
	 * Handles a particular Elem of type SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	 * @param in The Elem to be handles.
	 * @return The generated clang variable names for the Elem.
	 * @throws Throwable
	 */
	protected static CplxRec hndl_Cplx_Dbl( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in ) throws Throwable
	{

		if( reuseMap.get( in ) != null )
		{
			System.out.println( "Simplified !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + in );
			return( (CplxRec)( reuseMap.get( in ) ) );
		}
		
		if( in instanceof SymbolicZero )
		{
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = 0.0;" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = 0.0;" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicIdentity )
		{
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = 1.0;" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = 0.0;" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicNegate )
		{
			SymbolicNegate<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicNegate<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec arg = hndl_Cplx_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = - " + arg.re + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = - " + arg.im + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicAdd )
		{
			SymbolicAdd<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicAdd<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec argA = hndl_Cplx_Dbl( neg.getElemA() );
			reuseMap.put( neg.getElemA() , argA );
			CplxRec argB = hndl_Cplx_Dbl( neg.getElemB() );
			reuseMap.put( neg.getElemB() , argB );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + argA.re + " + " + argB.re + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = " + argA.im + " + " + argB.im + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicMult )
		{
			SymbolicMult<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicMult<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec argA = hndl_Cplx_Dbl( neg.getElemA() );
			reuseMap.put( neg.getElemA() , argA );
			CplxRec argB = hndl_Cplx_Dbl( neg.getElemB() );
			reuseMap.put( neg.getElemB() , argB );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + argA.re + " * " + argB.re 
					+ " - " + argA.im + " * " + argB.im + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = " + argA.re + " * " + argB.im 
					+ " + " + argA.im + " * " + argB.re + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicDivideBy )
		{
			SymbolicDivideBy<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicDivideBy<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec arg = hndl_Cplx_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + arg.re + " / " + ( neg.getIval() ) + "L;" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = " + arg.im + " / " + ( neg.getIval() ) + "L;" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicInvertLeft )
		{
			SymbolicInvertLeft<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicInvertLeft<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec arg = hndl_Cplx_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vNameConj = "t_" + allocNum;
			psInst.println( "const double " + vNameConj + " = " + arg.re + " * " + arg.re + " + " + arg.im + " * " + arg.im + ";" );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + arg.re + " / " + vNameConj + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = - " + arg.im + " / " + vNameConj + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicInvertRight )
		{
			SymbolicInvertRight<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicInvertRight<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec arg = hndl_Cplx_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vNameConj = "t_" + allocNum;
			psInst.println( "const double " + vNameConj + " = " + arg.re + " * " + arg.re + " + " + arg.im + " * " + arg.im + ";" );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + arg.re + " / " + vNameConj + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = - " + arg.im + " / " + vNameConj + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicReduction )
		{
			final ComplexElem<DoubleElem,DoubleElemFactory> eval = in.eval( null );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + ( eval.getRe().getVal() ) + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = " + ( eval.getIm().getVal() ) + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		
		System.out.println( in.getClass() );
		allocNum++;
		final long memberNum = allocNum;
		final String memberName = "m_" + memberNum;
		// jmemInst.println( "private SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> " + memberName + ";" );
		// jcnstInst.println( " , SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _" + memberName );
		// jcimpInst.println( memberName + " = arr.get( " + ( cnstParamsInner.size() ) + " );" );
		jlocmemInst.println( "jobject loc" + memberName + " = NULL;" );
		
		// jfldInst.println( "if( loc" + memberName + " == NULL )" );
		jfldInst.println( "if( true )" );
		jfldInst.println( "{" );
		jfldInst.println( "   loc" + memberName + " = env->GetObjectArrayElement( locnarr , " + cnstParamsInner.size() + " );" );
		jfldInst.println( "}" );
		jfldInst.println( "if( loc" + memberName + " == NULL )" );
		jfldInst.println( "{" );
		jfldInst.println( "   printf( \"Unable To Find member " + memberName + "\\n\" );" );
		jfldInst.println( "   exit(1);" );
		jfldInst.println( "}" );
		
		cnstParamsInner.add( in );
		allocNum++;
		final String vNameZ = "t_" + allocNum;
		psInst.println( "jobject " + vNameZ + " = env->CallObjectMethod( loc" + memberName + " , symEval , hsh );" );
		allocNum++;
		final String vNameReZ = "t_" + allocNum;
		psInst.println( "jobject " + vNameReZ + " = env->CallObjectMethod( " + vNameZ + " , cplxGetRe );" );
		allocNum++;
		final String vNameImZ = "t_" + allocNum;
		psInst.println( "jobject " + vNameImZ + " = env->CallObjectMethod( " + vNameZ + " , cplxGetIm );" );
		psInst.println( vNameZ + " = NULL;" );
		allocNum++;
		final String vNameRe = "t_" + allocNum;
		// psInst.println( "printf( \"Initiated Call \\n\" );" );
		psInst.println( "const jdouble " + vNameRe + " = env->CallDoubleMethod( " + vNameReZ + " , dblGetVal );" );
		psInst.println( vNameReZ + " = NULL;" );
		// psInst.println( "printf( \"Finsihed Call %lf\\n\" , " + vNameRe + " );" );
		allocNum++;
		final String vNameIm = "t_" + allocNum;
		// psInst.println( "printf( \"Initiated Call \\n\" );" );
		psInst.println( "const jdouble " + vNameIm + " = env->CallDoubleMethod( " + vNameImZ + " , dblGetVal );" );
		psInst.println( vNameImZ + " = NULL;" );
		// psInst.println( "printf( \"Finsihed Call %lf\\n\" , " + vNameIm + " );" );
		return( new CplxRec( vNameRe , vNameIm ) );
	}

	
	/**
	 * Attempts to convert a SymbolicElem<DoubleElem,DoubleElemFactory> to clang.
	 * @param in The SymbolicElem<DoubleElem,DoubleElemFactory> to be converted.
	 * @return The clang-converted SymbolicElem<DoubleElem,DoubleElemFactory>.
	 * @throws Throwable
	 */
	public static SymbolicElem<DoubleElem,DoubleElemFactory> clang_Dbl( SymbolicElem<DoubleElem,DoubleElemFactory> in ) throws Throwable
	{
		if( tempDir == null )
		{
			allocNum++;
			File fi = File.createTempFile( "tempDir" , "" + allocNum );
			
			if( !( fi.delete() ) )
			{
				throw( new RuntimeException( "Fail" ) );
			}
			
			if( !( fi.mkdir() ) )
			{
				throw( new RuntimeException( "Fail" ) );
			}
			
			tempDir = fi;
		}
		
		allocNum++;
		final long clangAlloc = allocNum;
		final String className = "Clang_" + allocNum;
		final String libName = "libClang_" + allocNum;
		
		final File fjav = new File( tempDir , className + ".java" );
		final File flib = new File( tempDir , libName + ".so" );
		final File fclang = new File( tempDir , className + ".cpp" );
		
		final File fjavac =new File( tempDir , "runJavac.sh" );
		final File fgcc =new File( tempDir , "runGCC.sh" );
		
		final File fPsInst = new File( tempDir , className + "A.txt" );
		final File fJmemInst = new File( tempDir , className + "B.txt" );
		final File fJcnstInst = new File( tempDir , className + "C.txt" );
		final File fJcimpInst = new File( tempDir , className + "D.txt" );
		final File fJlocmemInst = new File( tempDir , className + "E.txt" );
		final File fJfldInst = new File( tempDir , className + "F.txt" );
		
		
		cnstParamsOuter = new ArrayList<Object>();
		cnstParamsInner = new ArrayList<Object>();
		cnstParamsOuter.add( in.getFac().getFac() );
		cnstParamsOuter.add( cnstParamsInner );
		psInst = new PrintStream( new FileOutputStream( fPsInst ) );
		jmemInst = new PrintStream( new FileOutputStream( fJmemInst ) );
		jcnstInst = new PrintStream( new FileOutputStream( fJcnstInst ) );
		jcimpInst = new PrintStream( new FileOutputStream( fJcimpInst ) );
		jlocmemInst = new PrintStream( new FileOutputStream( fJlocmemInst ) );
		jfldInst = new PrintStream( new FileOutputStream( fJfldInst ) );
		reuseMap = new HashMap<SymbolicElem<?,?>,Object>();
		final String vName = hndl_Dbl( in );
		psInst.close();
		jmemInst.close();
		jcnstInst.close();
		jcimpInst.close();
		jlocmemInst.close();
		jfldInst.close();
		psInst = null;
		jmemInst = null;
		jcnstInst = null;
		jcimpInst = null;
		jlocmemInst = null;
		jfldInst = null;
		reuseMap = null;
		
		
		PrintStream ps = new PrintStream( new FileOutputStream( fjav ) );
		
		ps.println( "" );
		ps.println( "import java.io.PrintStream;" );
		ps.println( "import java.util.ArrayList;" );
		ps.println( "import java.util.HashMap;" );
		ps.println( "" );
		ps.println( "import simplealgebra.DoubleElem;" );
		ps.println( "import simplealgebra.DoubleElemFactory;" );
		ps.println( "import simplealgebra.Elem;" );
		ps.println( "import simplealgebra.NotInvertibleException;" );
		ps.println( "import simplealgebra.WriteElemCache;" );
		ps.println( "import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;" );
		ps.println( "import simplealgebra.symbolic.SCacheKey;" );
		ps.println( "import simplealgebra.symbolic.SymbolicElem;" );
		ps.println( "import simplealgebra.symbolic.SymbolicElemFactory;" );
		ps.println( "import simplealgebra.clang.Clang_Dbl;" );
		ps.println( "" );
		ps.println( "" );
		ps.println( "public class " + className + " extends Clang_Dbl  {" );
		ps.println( "" );
		ps.println( "	public " + className + "(DoubleElemFactory _fac" );
		ps.println( " , ArrayList<SymbolicElem<DoubleElem,DoubleElemFactory>> arr" );
		
		LineNumberReader li = new LineNumberReader( new FileReader( fJcnstInst ) );
		String line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( " ) {" );
		ps.println( "		super(_fac, arr);" );
		
		li = new LineNumberReader( new FileReader( fJcimpInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( " }" );
		ps.println( "" );
		
		li = new LineNumberReader( new FileReader( fJmemInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( "" );
		ps.println( "	@Override" );
		// ps.println( "	public native DoubleElem eval(" );
		ps.println( "	public native double evalD(" );
		ps.println( "			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace , SymbolicElem<DoubleElem, DoubleElemFactory>[] dvals )" );
		ps.println( "			throws NotInvertibleException," );
		ps.println( "			MultiplicativeDistributionRequiredException;" );
		ps.println( "" );
		ps.println( " static {" );
		ps.println( "   System.load( \"" + ( flib.getAbsolutePath() ) + "\" );" );
		ps.println( " }" );
		ps.println( "" );
		ps.println( "}" );
		ps.println( "" );

		
		ps.close();
		
		
		System.out.println( fjav.getAbsolutePath() );
		
		
		ps = new PrintStream( new FileOutputStream( fjavac ) );
		
		
		ps.println( "cd " + tempDir.getAbsolutePath() );
		ps.println( ClangConstants.CLANG_JAVAC_COMMAND + ClangConstants.CLANG_SIMPLEALGEBRA_CLASSPATH + " " + className + ".java" );
		ps.println( ClangConstants.CLANG_JAVAH_COMMAND + ClangConstants.CLANG_SIMPLEALGEBRA_CLASSPATH + " " + className );
		
		
		ps.close();
		
		
		
		Process p = Runtime.getRuntime().exec( "sh " + fjavac.getAbsolutePath() );
		p.waitFor();
		
		
		System.out.println( "Finished Javac/Javah." );
		
		
		ps = new PrintStream( new FileOutputStream( fclang ) );
		
		
		ps.println( "" );
		ps.println( "#include<cstdlib>" );
		ps.println( "#include<math.h>" );
		ps.println( "" );
		ps.println( "using namespace std;" );
		ps.println( "" );
		ps.println( "#include \"" + className + ".h\"" );
		ps.println( "" );
		ps.println( "jclass clangClass = NULL;" );
		ps.println( "jclass symClass = NULL;" );
		ps.println( "jclass dblClass = NULL;" );
		ps.println( "jmethodID dblGetVal = NULL;" );
		ps.println( "jmethodID symEval = NULL;" );
		ps.println( "" );
		
		li = new LineNumberReader( new FileReader( fJlocmemInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( "" );
		// ps.println( "JNIEXPORT jobject JNICALL Java_Clang_1" + clangAlloc + "_eval" );
		ps.println( "JNIEXPORT jdouble JNICALL Java_Clang_1" + clangAlloc + "_evalD" );
		ps.println( "  (JNIEnv *env, jobject ths, jobject hsh, jobjectArray locnarr)" );
		ps.println( "{" );
		ps.println( "" );
		// ps.println( "if( clangClass == NULL )" );
		ps.println( "if( true )" );
		ps.println( "{" );
		// ps.println( "   clangClass = env->FindClass( \"" + className + "\" );" );
		ps.println( "   clangClass = env->GetObjectClass( ths );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( clangClass == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find clangClass\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( symClass == NULL )" );
		ps.println( "{" );
		ps.println( "   symClass = env->FindClass( \"simplealgebra/symbolic/SymbolicElem\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( symClass == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find symClass\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( dblClass == NULL )" );
		ps.println( "{" );
		ps.println( "   dblClass = env->FindClass( \"simplealgebra/DoubleElem\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( dblClass == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find dblClass\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( dblGetVal == NULL )" );
		ps.println( "{" );
		ps.println( "   dblGetVal = env->GetMethodID( dblClass , \"getVal\" , \"()D\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( dblGetVal == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find dblGetVal\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( symEval == NULL )" );
		ps.println( "{" );
		ps.println( "   symEval = env->GetMethodID( symClass , \"eval\" , \"(Ljava/util/HashMap;)Lsimplealgebra/Elem;\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( symEval == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find symEval\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		
		
		li = new LineNumberReader( new FileReader( fJfldInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( "" );
		
		li = new LineNumberReader( new FileReader( fPsInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( "const double val = " + vName + ";" );
		
		ps.println( "" );

		ps.println( "return val;" );
		ps.println( "" );
		ps.println( "}" );
		ps.println( "" );
		
		
		ps.close();
		
		
		ps = new PrintStream( new FileOutputStream( fgcc ) );
		
		
		ps.println( "cd " + tempDir.getAbsolutePath() );
		ps.println( ClangConstants.CLANG_NATIVE_COMPILATION_COMMAND + className + ".cpp -o " + className + ".o" );
		ps.println( ClangConstants.CLANG_NATIVE_LINK_COMMAND + libName + ".so " + className + ".o -lc -lm" );
		
		
		ps.close();
		
		
		
		p = Runtime.getRuntime().exec( "sh " + fgcc.getAbsolutePath() );
		p.waitFor();
		
		
		System.out.println( "Finished Native Clang Compile." );
		
		
		if( ucl == null )
		{
			final URL tempUrl = tempDir.toURI().toURL();
			final URL[] urls = { tempUrl };
			ucl = new URLClassLoader( urls );
		}
		Class<? extends SymbolicElem<DoubleElem,DoubleElemFactory>> clss = 
				(Class<? extends SymbolicElem<DoubleElem,DoubleElemFactory>>)( ucl.loadClass( className ) );
		
		System.out.println( "Got Class." );
		System.out.println( clss );
		
		final Constructor<? extends SymbolicElem<DoubleElem,DoubleElemFactory>> cnst =
				(Constructor<? extends SymbolicElem<DoubleElem,DoubleElemFactory>>)( ( clss.getConstructors() )[ 0 ] );
		
		final Object[] cparam = cnstParamsOuter.toArray();
		cnstParamsOuter = null;
		cnstParamsInner = null;
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> gen = cnst.newInstance( cparam );
		
		System.out.println( gen );
		
		return( gen );
	}
	
	
	


	
	/**
	 * Attempts to convert a SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> to clang.
	 * @param in The SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> to be converted.
	 * @return The clang-converted SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>.
	 * @throws Throwable
	 */
	public static SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> clang_Cplx_Dbl( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in ) throws Throwable
	{
		if( tempDir == null )
		{
			allocNum++;
			File fi = File.createTempFile( "tempDir" , "" + allocNum );
			
			if( !( fi.delete() ) )
			{
				throw( new RuntimeException( "Fail" ) );
			}
			
			if( !( fi.mkdir() ) )
			{
				throw( new RuntimeException( "Fail" ) );
			}
			
			tempDir = fi;
		}
		
		allocNum++;
		final long clangAlloc = allocNum;
		final String className = "Clang_" + allocNum;
		final String libName = "libClang_" + allocNum;
		
		final File fjav = new File( tempDir , className + ".java" );
		final File flib = new File( tempDir , libName + ".so" );
		final File fclang = new File( tempDir , className + ".cpp" );
		
		final File fjavac =new File( tempDir , "runJavac.sh" );
		final File fgcc =new File( tempDir , "runGCC.sh" );
		
		final File fPsInst= new File( tempDir , className + "A.txt" );
		final File fJmemInst= new File( tempDir , className + "B.txt" );
		final File fJcnstInst= new File( tempDir , className + "C.txt" );
		final File fJcimpInst= new File( tempDir , className + "D.txt" );
		final File fJlocmemInst= new File( tempDir , className + "E.txt" );
		final File fJfldInst= new File( tempDir , className + "F.txt" );
		
		
		cnstParamsOuter = new ArrayList<Object>();
		cnstParamsInner = new ArrayList<Object>();
		cnstParamsOuter.add( in.getFac().getFac() );
		cnstParamsOuter.add( cnstParamsInner );
		psInst = new PrintStream( new FileOutputStream( fPsInst ) );
		jmemInst = new PrintStream( new FileOutputStream( fJmemInst ) );
		jcnstInst = new PrintStream( new FileOutputStream( fJcnstInst ) );
		jcimpInst = new PrintStream( new FileOutputStream( fJcimpInst ) );
		jlocmemInst = new PrintStream( new FileOutputStream( fJlocmemInst ) );
		jfldInst = new PrintStream( new FileOutputStream( fJfldInst ) );
		reuseMap = new HashMap<SymbolicElem<?,?>,Object>();
		final CplxRec vNames = hndl_Cplx_Dbl( in );
		psInst.close();
		jmemInst.close();
		jcnstInst.close();
		jcimpInst.close();
		jlocmemInst.close();
		jfldInst.close();
		psInst = null;
		jmemInst = null;
		jcnstInst = null;
		jcimpInst = null;
		jlocmemInst = null;
		jfldInst = null;
		reuseMap = null;
		
		
		PrintStream ps = new PrintStream( new FileOutputStream( fjav ) );
		
		ps.println( "" );
		ps.println( "import java.io.PrintStream;" );
		ps.println( "import java.util.ArrayList;" );
		ps.println( "import java.util.HashMap;" );
		ps.println( "" );
		ps.println( "import simplealgebra.DoubleElem;" );
		ps.println( "import simplealgebra.DoubleElemFactory;" );
		ps.println( "import simplealgebra.ComplexElem;" );
		ps.println( "import simplealgebra.ComplexElemFactory;" );
		ps.println( "import simplealgebra.Elem;" );
		ps.println( "import simplealgebra.NotInvertibleException;" );
		ps.println( "import simplealgebra.WriteElemCache;" );
		ps.println( "import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;" );
		ps.println( "import simplealgebra.symbolic.SCacheKey;" );
		ps.println( "import simplealgebra.symbolic.SymbolicElem;" );
		ps.println( "import simplealgebra.symbolic.SymbolicElemFactory;" );
		ps.println( "import simplealgebra.clang.Clang_Cplx_Dbl;" );
		ps.println( "" );
		ps.println( "" );
		ps.println( "public class " + className + " extends Clang_Cplx_Dbl  {" );
		ps.println( "" );
		ps.println( "	public " + className + "(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac" );
		ps.println( " , ArrayList<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> arr" );
		
		LineNumberReader li = new LineNumberReader( new FileReader( fJcnstInst ) );
		String line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( " ) {" );
		ps.println( "		super(_fac, arr);" );
		
		li = new LineNumberReader( new FileReader( fJcimpInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( " }" );
		ps.println( "" );
		
		li = new LineNumberReader( new FileReader( fJmemInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( "" );
		ps.println( "	@Override" );
		ps.println( "	public native void evalD(" );
		ps.println( "			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace , SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>[] dvals )" );
		ps.println( "			throws NotInvertibleException," );
		ps.println( "			MultiplicativeDistributionRequiredException;" );
		ps.println( "" );
		ps.println( " static {" );
		ps.println( "   System.load( \"" + ( flib.getAbsolutePath() ) + "\" );" );
		ps.println( " }" );
		ps.println( "" );
		ps.println( "}" );
		ps.println( "" );

		
		ps.close();
		
		
		System.out.println( fjav.getAbsolutePath() );
		
		
		ps = new PrintStream( new FileOutputStream( fjavac ) );
		
		
		ps.println( "cd " + tempDir.getAbsolutePath() );
		ps.println( ClangConstants.CLANG_JAVAC_COMMAND + ClangConstants.CLANG_SIMPLEALGEBRA_CLASSPATH + " " + className + ".java" );
		ps.println( ClangConstants.CLANG_JAVAH_COMMAND + ClangConstants.CLANG_SIMPLEALGEBRA_CLASSPATH + " " + className );
		
		
		ps.close();
		
		
		
		Process p = Runtime.getRuntime().exec( "sh " + fjavac.getAbsolutePath() );
		p.waitFor();
		
		
		System.out.println( "Finished Javac/Javah." );
		
		
		
		ps = new PrintStream( new FileOutputStream( fclang ) );
		
		
		ps.println( "" );
		ps.println( "#include<cstdlib>" );
		ps.println( "#include<math.h>" );
		ps.println( "" );
		ps.println( "using namespace std;" );
		ps.println( "" );
		ps.println( "#include \"" + className + ".h\"" );
		ps.println( "" );
		ps.println( "jclass clangClass = NULL;" );
		ps.println( "jclass symClass = NULL;" );
		ps.println( "jclass dblClass = NULL;" );
		ps.println( "jclass cplxClass = NULL;" );
		ps.println( "jmethodID cplxGetRe = NULL;" );
		ps.println( "jmethodID cplxGetIm = NULL;" );
		ps.println( "jmethodID dblGetVal = NULL;" );
		ps.println( "jmethodID symEval = NULL;" );
		ps.println( "jfieldID dvalRe = NULL;" );
		ps.println( "jfieldID dvalIm = NULL;" );
		ps.println( "" );
		
		li = new LineNumberReader( new FileReader( fJlocmemInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( "" );
		ps.println( "JNIEXPORT void JNICALL Java_Clang_1" + clangAlloc + "_evalD" );
		ps.println( "  (JNIEnv *env, jobject ths, jobject hsh, jobjectArray locnarr)" );
		ps.println( "{" );
		ps.println( "" );
		// ps.println( "if( clangClass == NULL )" );
		ps.println( "if( true )" );
		ps.println( "{" );
		// ps.println( "   clangClass = env->FindClass( \"" + className + "\" );" );
		ps.println( "   clangClass = env->GetObjectClass( ths );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( clangClass == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find clangClass\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( symClass == NULL )" );
		ps.println( "{" );
		ps.println( "   symClass = env->FindClass( \"simplealgebra/symbolic/SymbolicElem\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( symClass == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find symClass\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( dblClass == NULL )" );
		ps.println( "{" );
		ps.println( "   dblClass = env->FindClass( \"simplealgebra/DoubleElem\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( dblClass == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find dblClass\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( cplxClass == NULL )" );
		ps.println( "{" );
		ps.println( "   cplxClass = env->FindClass( \"simplealgebra/ComplexElem\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( cplxClass == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find cplxClass\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( cplxGetRe == NULL )" );
		ps.println( "{" );
		ps.println( "   cplxGetRe = env->GetMethodID( cplxClass , \"getRe\" , \"()Lsimplealgebra/Elem;\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( cplxGetRe == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find cplxGetRe\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( cplxGetIm == NULL )" );
		ps.println( "{" );
		ps.println( "   cplxGetIm = env->GetMethodID( cplxClass , \"getIm\" , \"()Lsimplealgebra/Elem;\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( cplxGetIm == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find cplxGetIm\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( dblGetVal == NULL )" );
		ps.println( "{" );
		ps.println( "   dblGetVal = env->GetMethodID( dblClass , \"getVal\" , \"()D\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( dblGetVal == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find dblGetVal\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( symEval == NULL )" );
		ps.println( "{" );
		ps.println( "   symEval = env->GetMethodID( symClass , \"eval\" , \"(Ljava/util/HashMap;)Lsimplealgebra/Elem;\" );" );
		ps.println( "}" );
		ps.println( "" );
		ps.println( "if( symEval == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find symEval\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		
		// ps.println( "if( dvalRe == NULL )" );
		ps.println( "if( true )" );
		ps.println( "{" );
		ps.println( "   dvalRe = env->GetFieldID( clangClass , \"dvalRe\" , \"D\" );" );
		ps.println( "}" );
		ps.println( "if( dvalRe == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find member dvalRe\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		
		// ps.println( "if( dvalIm == NULL )" );
		ps.println( "if( true )" );
		ps.println( "{" );
		ps.println( "   dvalIm = env->GetFieldID( clangClass , \"dvalIm\" , \"D\" );" );
		ps.println( "}" );
		ps.println( "if( dvalIm == NULL )" );
		ps.println( "{" );
		ps.println( "   printf( \"Unable To Find member dvalIm\\n\" );" );
		ps.println( "   exit(1);" );
		ps.println( "}" );
		
		
		li = new LineNumberReader( new FileReader( fJfldInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( "" );
		
		li = new LineNumberReader( new FileReader( fPsInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line );
			line = li.readLine();
		}
		li.close();
		ps.println( "const double valRe = " + vNames.re + ";" );
		ps.println( "const double valIm = " + vNames.im + ";" );
		
		// ps.println( "   printf( \"Reached A %lf\\n \" , valRe );" );
		// ps.println( "   printf( \"Reached A %lf\\n \" , valIm );" );
		
		ps.println( "env->SetDoubleField( ths , dvalRe , valRe );" );
		ps.println( "env->SetDoubleField( ths , dvalIm , valIm );" );
		
		ps.println( "" );
/*		ps.println( "   printf( \"Reached A\\n\" );" );
		ps.println( "jobject retRe = env->NewObject( dblClass , dblCnst , valRe );" );
		ps.println( "   printf( \"Reached B\\n\" );" );
		ps.println( "jobject retIm = env->NewObject( dblClass , dblCnst , valIm );" );
		ps.println( "   printf( \"Reached C\\n\" );" );
		ps.println( "jobject ret = env->NewObject( cplxClass , cplxCnst , retRe , retIm );" );
		ps.println( "   printf( \"Reached D\\n\" );" );
		ps.println( "" );
		ps.println( "return ret;" ); */
		ps.println( "" );
		ps.println( "}" );
		ps.println( "" );
		
		
		ps.close();
		
		
		ps = new PrintStream( new FileOutputStream( fgcc ) );
		
		
		ps.println( "cd " + tempDir.getAbsolutePath() );
		ps.println( ClangConstants.CLANG_NATIVE_COMPILATION_COMMAND + className + ".cpp -o " + className + ".o" );
		ps.println( ClangConstants.CLANG_NATIVE_LINK_COMMAND + libName + ".so " + className + ".o -lc -lm" );
		
		
		ps.close();
		
		
		
		p = Runtime.getRuntime().exec( "sh " + fgcc.getAbsolutePath() );
		p.waitFor();
		
		
		System.out.println( "Finished Native Clang Compile." );
		
		
		if( ucl == null )
		{
			final URL tempUrl = tempDir.toURI().toURL();
			final URL[] urls = { tempUrl };
			ucl = new URLClassLoader( urls );
		}
		Class<? extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> clss = 
				(Class<? extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>)( ucl.loadClass( className ) );
		
		System.out.println( "Got Class." );
		System.out.println( clss );
		
		final Constructor<? extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> cnst =
				(Constructor<? extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>)( ( clss.getConstructors() )[ 0 ] );
		
		final Object[] cparam = cnstParamsOuter.toArray();
		cnstParamsOuter = null;
		cnstParamsInner = null;
		
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> gen = cnst.newInstance( cparam );
		
		System.out.println( gen );
		
		return( gen );
	}
	

	
	
	
	
}



